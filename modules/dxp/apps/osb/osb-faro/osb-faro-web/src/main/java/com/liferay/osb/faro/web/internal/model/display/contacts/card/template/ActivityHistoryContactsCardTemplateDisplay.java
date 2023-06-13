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

package com.liferay.osb.faro.web.internal.model.display.contacts.card.template;

import com.liferay.osb.faro.contacts.model.ContactsCardTemplate;
import com.liferay.osb.faro.engine.client.ContactsEngineClient;
import com.liferay.osb.faro.engine.client.model.ActivityAggregation;
import com.liferay.osb.faro.engine.client.model.Results;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.web.internal.model.display.main.FaroEntityDisplay;
import com.liferay.portal.kernel.util.MapUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Matthew Kong
 */
public class ActivityHistoryContactsCardTemplateDisplay
	extends ContactsCardTemplateDisplay {

	public ActivityHistoryContactsCardTemplateDisplay() {
	}

	public ActivityHistoryContactsCardTemplateDisplay(
			FaroProject faroProject, ContactsCardTemplate contactsCardTemplate,
			int size, ContactsEngineClient contactsEngineClient)
		throws Exception {

		super(contactsCardTemplate, size, _SUPPORTED_SIZES);

		_interval = MapUtil.getString(settings, "interval");
		_max = MapUtil.getInteger(settings, "max");
	}

	@Override
	public Map<String, Object> getContactsCardData(
		FaroProject faroProject, FaroEntityDisplay faroEntityDisplay,
		ContactsEngineClient contactsEngineClient) {

		Map<String, Object> contactsCardData = new HashMap<>();

		Results<ActivityAggregation> results =
			contactsEngineClient.getActivityAggregations(
				faroProject, null, faroEntityDisplay.getId(), null, null, null,
				_interval, _max);

		contactsCardData.put("activityHistory", results.getItems());

		return contactsCardData;
	}

	private static final int[] _SUPPORTED_SIZES = {4};

	private String _interval;
	private int _max;

}