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

package com.liferay.osb.faro.admin.web.internal.model;

import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.model.FaroUser;
import com.liferay.osb.faro.service.FaroUserLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;

import java.text.DecimalFormat;

import java.util.Date;

/**
 * @author Matthew Kong
 */
public class FaroProjectAdminDisplay {

	public FaroProjectAdminDisplay() {
	}

	public FaroProjectAdminDisplay(Document document) {
		_corpProjectName = document.get("corpProjectName");
		_corpProjectUuid = document.get("corpProjectUuid");

		try {
			_createDate = document.getDate("createDate");
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		_faroProjectId = GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK));
		_groupId = GetterUtil.getLong(document.get(Field.GROUP_ID));
		_individualsUsage = _decimalFormat.format(
			GetterUtil.getDouble(document.get("individualsUsage")));

		try {
			_lastAccessDate = document.getDate("lastAccessDate");
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		_name = document.get(Field.NAME);
		_offline = GetterUtil.getBoolean(document.get("offline"));
		_owner = _getOwner();
		_pageViewsUsage = _decimalFormat.format(
			GetterUtil.getDouble(document.get("pageViewsUsage")));
		_serverLocation = document.get("serverLocation");
		_subscriptionName = document.get("subscriptionName");
	}

	public FaroProjectAdminDisplay(FaroProject faroProject, Document document) {
		this(document);

		_individualsCount = GetterUtil.getLong(
			document.get("individualsCount"));
		_individualsLimit = GetterUtil.getLong(
			document.get("individualsLimit"));
		_pageViewsCount = GetterUtil.getLong(document.get("pageViewsCount"));
		_pageViewsLimit = GetterUtil.getLong(document.get("pageViewsLimit"));
		_serverLocation = faroProject.getServerLocation();
		_subscription = faroProject.getSubscription();
		_weDeployKey = faroProject.getWeDeployKey();
	}

	public String getCorpProjectName() {
		return _corpProjectName;
	}

	public String getCorpProjectUuid() {
		return _corpProjectUuid;
	}

	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	public long getFaroProjectId() {
		return _faroProjectId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public long getIndividualsCount() {
		return _individualsCount;
	}

	public long getIndividualsLimit() {
		return _individualsLimit;
	}

	public String getIndividualsUsage() {
		return _individualsUsage;
	}

	public Date getLastAccessDate() {
		if (_lastAccessDate == null) {
			return null;
		}

		return new Date(_lastAccessDate.getTime());
	}

	public String getName() {
		return _name;
	}

	public String getOwner() {
		return _owner;
	}

	public long getPageViewsCount() {
		return _pageViewsCount;
	}

	public long getPageViewsLimit() {
		return _pageViewsLimit;
	}

	public String getPageViewsUsage() {
		return _pageViewsUsage;
	}

	public String getServerLocation() {
		return _serverLocation;
	}

	public String getSubscription() {
		return _subscription;
	}

	public String getSubscriptionName() {
		return _subscriptionName;
	}

	public String getWeDeployKey() {
		return _weDeployKey;
	}

	public boolean isOffline() {
		return _offline;
	}

	public void setCorpProjectName(String corpProjectName) {
		_corpProjectName = corpProjectName;
	}

	public void setCorpProjectUuid(String corpProjectUuid) {
		_corpProjectUuid = corpProjectUuid;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setFaroProjectId(long faroProjectId) {
		_faroProjectId = faroProjectId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public void setIndividualsCount(long individualsCount) {
		_individualsCount = individualsCount;
	}

	public void setIndividualsLimit(long individualsLimit) {
		_individualsLimit = individualsLimit;
	}

	public void setIndividualsUsage(String individualsUsage) {
		_individualsUsage = individualsUsage;
	}

	public void setLastAccessDate(Date lastAccessDate) {
		if (lastAccessDate != null) {
			_lastAccessDate = new Date(lastAccessDate.getTime());
		}
	}

	public void setName(String name) {
		_name = name;
	}

	public void setOffline(boolean offline) {
		_offline = offline;
	}

	public void setOwner(String owner) {
		_owner = owner;
	}

	public void setPageViewsCount(long pageViewsCount) {
		_pageViewsCount = pageViewsCount;
	}

	public void setPageViewsLimit(long pageViewsLimit) {
		_pageViewsLimit = pageViewsLimit;
	}

	public void setPageViewsUsage(String pageViewsUsage) {
		_pageViewsUsage = pageViewsUsage;
	}

	public void setServerLocation(String serverLocation) {
		_serverLocation = serverLocation;
	}

	public void setSubscription(String subscription) {
		_subscription = subscription;
	}

	public void setSubscriptionName(String subscriptionName) {
		_subscriptionName = subscriptionName;
	}

	public void setWeDeployKey(String weDeployKey) {
		_weDeployKey = weDeployKey;
	}

	private String _getOwner() {
		FaroUser faroUser = FaroUserLocalServiceUtil.fetchOwnerFaroUser(
			_groupId);

		if (faroUser.getLiveUserId() <= 0) {
			return faroUser.getEmailAddress();
		}

		User user = UserLocalServiceUtil.fetchUser(faroUser.getLiveUserId());

		if (user != null) {
			return user.getFullName() + " " + user.getEmailAddress();
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FaroProjectAdminDisplay.class);

	private static final DecimalFormat _decimalFormat = new DecimalFormat(
		"#.##");

	private String _corpProjectName;
	private String _corpProjectUuid;
	private Date _createDate;
	private long _faroProjectId;
	private long _groupId;
	private long _individualsCount;
	private long _individualsLimit;
	private String _individualsUsage;
	private Date _lastAccessDate;
	private String _name;
	private boolean _offline;
	private String _owner;
	private long _pageViewsCount;
	private long _pageViewsLimit;
	private String _pageViewsUsage;
	private String _serverLocation;
	private String _subscription;
	private String _subscriptionName;
	private String _weDeployKey;

}