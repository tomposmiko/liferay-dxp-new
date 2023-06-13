/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.web.internal.model.display.main;

import com.liferay.osb.faro.constants.FaroProjectConstants;
import com.liferay.osb.faro.engine.client.CerebroEngineClient;
import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.provisioning.client.constants.ProductConstants;
import com.liferay.osb.faro.provisioning.client.model.OSBAccountEntry;
import com.liferay.osb.faro.provisioning.client.model.OSBOfferingEntry;
import com.liferay.osb.faro.web.internal.constants.FaroSubscriptionConstants;
import com.liferay.osb.faro.web.internal.subscription.FaroSubscriptionPlan;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Matthew Kong
 */
@SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
public class FaroSubscriptionDisplay {

	public FaroSubscriptionDisplay() {
	}

	public FaroSubscriptionDisplay(OSBAccountEntry osbAccountEntry) {
		OSBOfferingEntry baseOSBOfferingEntry = _getBaseOSBOfferingEntry(
			osbAccountEntry.getOfferingEntries());

		if (baseOSBOfferingEntry == null) {
			return;
		}

		if (baseOSBOfferingEntry.getStatus() ==
				ProductConstants.OSB_OFFERING_ENTRY_STATUS_ACTIVE) {

			_active = true;
		}
		else {
			_active = false;
		}

		_endDate = baseOSBOfferingEntry.getSupportEndDate();
		_name = ProductConstants.getProductName(
			baseOSBOfferingEntry.getProductEntryId());
		_startDate = baseOSBOfferingEntry.getStartDate();

		FaroSubscriptionPlan baseFaroSubscriptionPlan =
			FaroSubscriptionConstants.getFaroSubscriptionPlanByProductEntryId(
				baseOSBOfferingEntry.getProductEntryId());

		_individualsLimit = baseFaroSubscriptionPlan.getIndividualsLimit();
		_pageViewsLimit = baseFaroSubscriptionPlan.getPageViewsLimit();

		for (OSBOfferingEntry osbOfferingEntry :
				osbAccountEntry.getOfferingEntries()) {

			FaroSubscriptionPlan faroSubscriptionPlan =
				FaroSubscriptionConstants.
					getFaroSubscriptionPlanByProductEntryId(
						osbOfferingEntry.getProductEntryId());

			if ((faroSubscriptionPlan != null) &&
				StringUtil.equals(
					faroSubscriptionPlan.getBaseSubscriptionPlan(),
					baseFaroSubscriptionPlan.getName())) {

				_addOns.add(new AddOn(osbOfferingEntry));

				_individualsLimit = _computeLimit(
					faroSubscriptionPlan.getIndividualsLimit(),
					_individualsLimit, osbOfferingEntry.getQuantity());
				_pageViewsLimit = _computeLimit(
					faroSubscriptionPlan.getPageViewsLimit(), _pageViewsLimit,
					osbOfferingEntry.getQuantity());
			}
		}
	}

	public long getIndividualsCount() {
		return _individualsCount;
	}

	public long getIndividualsLimit() {
		return _individualsLimit;
	}

	public String getName() {
		return _name;
	}

	public long getPageViewsCount() {
		return _pageViewsCount;
	}

	public long getPageViewsLimit() {
		return _pageViewsLimit;
	}

	public Date getStartDate() {
		if (_startDate == null) {
			return null;
		}

		return new Date(_startDate.getTime());
	}

	public boolean isActive() {
		return _active;
	}

	public void setCounts(
			FaroProject faroProject, CerebroEngineClient cerebroEngineClient,
			ContactsEngineClient contactsEngineClient)
		throws Exception {

		if ((faroProject == null) ||
			!StringUtil.equals(
				faroProject.getState(), FaroProjectConstants.STATE_READY)) {

			return;
		}

		_individualsCount = contactsEngineClient.getIndividualsCount(
			faroProject, false);

		_individualsStatus = getStatus(_individualsCount, _individualsLimit);

		_pageViewsCount = GetterUtil.getInteger(
			cerebroEngineClient.getPageViews(
				faroProject, _startDate, new Date()));

		_pageViewsStatus = getStatus(_pageViewsCount, _pageViewsLimit);
	}

	public static class AddOn {

		public AddOn() {
		}

		public AddOn(OSBOfferingEntry osbOfferingEntry) {
			_name = ProductConstants.getProductName(
				osbOfferingEntry.getProductEntryId());
			_quantity = osbOfferingEntry.getQuantity();
		}

		public String getName() {
			return _name;
		}

		public int getQuantity() {
			return _quantity;
		}

		public void setName(String name) {
			_name = name;
		}

		public void setQuantity(int quantity) {
			_quantity = quantity;
		}

		private String _name;
		private int _quantity;

	}

	protected int getStatus(long count, long limit) {
		if (limit < 0) {
			return FaroSubscriptionConstants.STATUS_OK;
		}

		if (count > limit) {
			return FaroSubscriptionConstants.STATUS_LIMIT_OVER;
		}
		else if (((double)count / limit) >
					FaroSubscriptionConstants.LIMIT_APPROACHING_THRESHOLD) {

			return FaroSubscriptionConstants.STATUS_LIMIT_APPROACHING;
		}

		return FaroSubscriptionConstants.STATUS_OK;
	}

	private long _computeLimit(
		long addOnLimit, long currentLimit, long quantity) {

		if (currentLimit < 0) {
			return currentLimit;
		}

		return currentLimit + (addOnLimit * quantity);
	}

	private OSBOfferingEntry _getBaseOSBOfferingEntry(
		List<OSBOfferingEntry> osbOfferingEntries) {

		OSBOfferingEntry baseOSBOfferingEntry = null;

		List<String> baseProductEntryIds =
			ProductConstants.getBaseProductEntryIds();

		for (OSBOfferingEntry osbOfferingEntry : osbOfferingEntries) {
			if (!baseProductEntryIds.contains(
					osbOfferingEntry.getProductEntryId())) {

				continue;
			}

			if ((baseOSBOfferingEntry == null) ||
				((baseOSBOfferingEntry.getStatus() !=
					osbOfferingEntry.getStatus()) &&
				 (osbOfferingEntry.getStatus() ==
					 ProductConstants.OSB_OFFERING_ENTRY_STATUS_ACTIVE)) ||
				((baseOSBOfferingEntry.getStatus() ==
					osbOfferingEntry.getStatus()) &&
				 _isAfter(baseOSBOfferingEntry, osbOfferingEntry))) {

				baseOSBOfferingEntry = osbOfferingEntry;
			}
		}

		return baseOSBOfferingEntry;
	}

	private boolean _isAfter(
		OSBOfferingEntry baseOSBOfferingEntry,
		OSBOfferingEntry osbOfferingEntry) {

		int value = DateUtil.compareTo(
			osbOfferingEntry.getStartDate(),
			baseOSBOfferingEntry.getStartDate());

		if (value > 0) {
			return true;
		}

		return false;
	}

	private boolean _active;
	private final List<AddOn> _addOns = new ArrayList<>();
	private Date _endDate;
	private long _individualsCount;
	private long _individualsLimit;
	private int _individualsStatus;
	private String _name;
	private long _pageViewsCount;
	private long _pageViewsLimit;
	private int _pageViewsStatus;
	private Date _startDate;

}