/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.layout.seo.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.liferay.layout.seo.service.http.LayoutSEOEntryServiceSoap}.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class LayoutSEOEntrySoap implements Serializable {

	public static LayoutSEOEntrySoap toSoapModel(LayoutSEOEntry model) {
		LayoutSEOEntrySoap soapModel = new LayoutSEOEntrySoap();

		soapModel.setMvccVersion(model.getMvccVersion());
		soapModel.setUuid(model.getUuid());
		soapModel.setLayoutSEOEntryId(model.getLayoutSEOEntryId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setPrivateLayout(model.isPrivateLayout());
		soapModel.setLayoutId(model.getLayoutId());
		soapModel.setEnabled(model.isEnabled());
		soapModel.setCanonicalURL(model.getCanonicalURL());
		soapModel.setLastPublishDate(model.getLastPublishDate());

		return soapModel;
	}

	public static LayoutSEOEntrySoap[] toSoapModels(LayoutSEOEntry[] models) {
		LayoutSEOEntrySoap[] soapModels = new LayoutSEOEntrySoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static LayoutSEOEntrySoap[][] toSoapModels(
		LayoutSEOEntry[][] models) {

		LayoutSEOEntrySoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels =
				new LayoutSEOEntrySoap[models.length][models[0].length];
		}
		else {
			soapModels = new LayoutSEOEntrySoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static LayoutSEOEntrySoap[] toSoapModels(
		List<LayoutSEOEntry> models) {

		List<LayoutSEOEntrySoap> soapModels = new ArrayList<LayoutSEOEntrySoap>(
			models.size());

		for (LayoutSEOEntry model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new LayoutSEOEntrySoap[soapModels.size()]);
	}

	public LayoutSEOEntrySoap() {
	}

	public long getPrimaryKey() {
		return _layoutSEOEntryId;
	}

	public void setPrimaryKey(long pk) {
		setLayoutSEOEntryId(pk);
	}

	public long getMvccVersion() {
		return _mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getLayoutSEOEntryId() {
		return _layoutSEOEntryId;
	}

	public void setLayoutSEOEntryId(long layoutSEOEntryId) {
		_layoutSEOEntryId = layoutSEOEntryId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public boolean getPrivateLayout() {
		return _privateLayout;
	}

	public boolean isPrivateLayout() {
		return _privateLayout;
	}

	public void setPrivateLayout(boolean privateLayout) {
		_privateLayout = privateLayout;
	}

	public long getLayoutId() {
		return _layoutId;
	}

	public void setLayoutId(long layoutId) {
		_layoutId = layoutId;
	}

	public boolean getEnabled() {
		return _enabled;
	}

	public boolean isEnabled() {
		return _enabled;
	}

	public void setEnabled(boolean enabled) {
		_enabled = enabled;
	}

	public String getCanonicalURL() {
		return _canonicalURL;
	}

	public void setCanonicalURL(String canonicalURL) {
		_canonicalURL = canonicalURL;
	}

	public Date getLastPublishDate() {
		return _lastPublishDate;
	}

	public void setLastPublishDate(Date lastPublishDate) {
		_lastPublishDate = lastPublishDate;
	}

	private long _mvccVersion;
	private String _uuid;
	private long _layoutSEOEntryId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _privateLayout;
	private long _layoutId;
	private boolean _enabled;
	private String _canonicalURL;
	private Date _lastPublishDate;

}