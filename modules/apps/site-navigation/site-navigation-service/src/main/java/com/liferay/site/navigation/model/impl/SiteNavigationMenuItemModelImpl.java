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

package com.liferay.site.navigation.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;

import com.liferay.exportimport.kernel.lar.StagedModelType;

import com.liferay.petra.string.StringBundler;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;

import com.liferay.site.navigation.model.SiteNavigationMenuItem;
import com.liferay.site.navigation.model.SiteNavigationMenuItemModel;
import com.liferay.site.navigation.model.SiteNavigationMenuItemSoap;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the SiteNavigationMenuItem service. Represents a row in the &quot;SiteNavigationMenuItem&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link SiteNavigationMenuItemModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link SiteNavigationMenuItemImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SiteNavigationMenuItemImpl
 * @see SiteNavigationMenuItem
 * @see SiteNavigationMenuItemModel
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class SiteNavigationMenuItemModelImpl extends BaseModelImpl<SiteNavigationMenuItem>
	implements SiteNavigationMenuItemModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a site navigation menu item model instance should use the {@link SiteNavigationMenuItem} interface instead.
	 */
	public static final String TABLE_NAME = "SiteNavigationMenuItem";
	public static final Object[][] TABLE_COLUMNS = {
			{ "uuid_", Types.VARCHAR },
			{ "siteNavigationMenuItemId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "siteNavigationMenuId", Types.BIGINT },
			{ "parentSiteNavigationMenuItemId", Types.BIGINT },
			{ "name", Types.VARCHAR },
			{ "type_", Types.VARCHAR },
			{ "typeSettings", Types.CLOB },
			{ "order_", Types.INTEGER },
			{ "lastPublishDate", Types.TIMESTAMP }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("siteNavigationMenuItemId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("siteNavigationMenuId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("parentSiteNavigationMenuItemId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("name", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("type_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("typeSettings", Types.CLOB);
		TABLE_COLUMNS_MAP.put("order_", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("lastPublishDate", Types.TIMESTAMP);
	}

	public static final String TABLE_SQL_CREATE = "create table SiteNavigationMenuItem (uuid_ VARCHAR(75) null,siteNavigationMenuItemId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,siteNavigationMenuId LONG,parentSiteNavigationMenuItemId LONG,name VARCHAR(255) null,type_ VARCHAR(75) null,typeSettings TEXT null,order_ INTEGER,lastPublishDate DATE null)";
	public static final String TABLE_SQL_DROP = "drop table SiteNavigationMenuItem";
	public static final String ORDER_BY_JPQL = " ORDER BY siteNavigationMenuItem.siteNavigationMenuItemId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY SiteNavigationMenuItem.siteNavigationMenuItemId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.site.navigation.service.util.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.site.navigation.model.SiteNavigationMenuItem"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.site.navigation.service.util.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.site.navigation.model.SiteNavigationMenuItem"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.site.navigation.service.util.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.site.navigation.model.SiteNavigationMenuItem"),
			true);
	public static final long COMPANYID_COLUMN_BITMASK = 1L;
	public static final long GROUPID_COLUMN_BITMASK = 2L;
	public static final long NAME_COLUMN_BITMASK = 4L;
	public static final long PARENTSITENAVIGATIONMENUITEMID_COLUMN_BITMASK = 8L;
	public static final long SITENAVIGATIONMENUID_COLUMN_BITMASK = 16L;
	public static final long UUID_COLUMN_BITMASK = 32L;
	public static final long SITENAVIGATIONMENUITEMID_COLUMN_BITMASK = 64L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static SiteNavigationMenuItem toModel(
		SiteNavigationMenuItemSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		SiteNavigationMenuItem model = new SiteNavigationMenuItemImpl();

		model.setUuid(soapModel.getUuid());
		model.setSiteNavigationMenuItemId(soapModel.getSiteNavigationMenuItemId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setSiteNavigationMenuId(soapModel.getSiteNavigationMenuId());
		model.setParentSiteNavigationMenuItemId(soapModel.getParentSiteNavigationMenuItemId());
		model.setName(soapModel.getName());
		model.setType(soapModel.getType());
		model.setTypeSettings(soapModel.getTypeSettings());
		model.setOrder(soapModel.getOrder());
		model.setLastPublishDate(soapModel.getLastPublishDate());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<SiteNavigationMenuItem> toModels(
		SiteNavigationMenuItemSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<SiteNavigationMenuItem> models = new ArrayList<SiteNavigationMenuItem>(soapModels.length);

		for (SiteNavigationMenuItemSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.site.navigation.service.util.ServiceProps.get(
				"lock.expiration.time.com.liferay.site.navigation.model.SiteNavigationMenuItem"));

	public SiteNavigationMenuItemModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _siteNavigationMenuItemId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setSiteNavigationMenuItemId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _siteNavigationMenuItemId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return SiteNavigationMenuItem.class;
	}

	@Override
	public String getModelClassName() {
		return SiteNavigationMenuItem.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("siteNavigationMenuItemId", getSiteNavigationMenuItemId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("siteNavigationMenuId", getSiteNavigationMenuId());
		attributes.put("parentSiteNavigationMenuItemId",
			getParentSiteNavigationMenuItemId());
		attributes.put("name", getName());
		attributes.put("type", getType());
		attributes.put("typeSettings", getTypeSettings());
		attributes.put("order", getOrder());
		attributes.put("lastPublishDate", getLastPublishDate());

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long siteNavigationMenuItemId = (Long)attributes.get(
				"siteNavigationMenuItemId");

		if (siteNavigationMenuItemId != null) {
			setSiteNavigationMenuItemId(siteNavigationMenuItemId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long siteNavigationMenuId = (Long)attributes.get("siteNavigationMenuId");

		if (siteNavigationMenuId != null) {
			setSiteNavigationMenuId(siteNavigationMenuId);
		}

		Long parentSiteNavigationMenuItemId = (Long)attributes.get(
				"parentSiteNavigationMenuItemId");

		if (parentSiteNavigationMenuItemId != null) {
			setParentSiteNavigationMenuItemId(parentSiteNavigationMenuItemId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String type = (String)attributes.get("type");

		if (type != null) {
			setType(type);
		}

		String typeSettings = (String)attributes.get("typeSettings");

		if (typeSettings != null) {
			setTypeSettings(typeSettings);
		}

		Integer order = (Integer)attributes.get("order");

		if (order != null) {
			setOrder(order);
		}

		Date lastPublishDate = (Date)attributes.get("lastPublishDate");

		if (lastPublishDate != null) {
			setLastPublishDate(lastPublishDate);
		}
	}

	@JSON
	@Override
	public String getUuid() {
		if (_uuid == null) {
			return "";
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		_columnBitmask |= UUID_COLUMN_BITMASK;

		if (_originalUuid == null) {
			_originalUuid = _uuid;
		}

		_uuid = uuid;
	}

	public String getOriginalUuid() {
		return GetterUtil.getString(_originalUuid);
	}

	@JSON
	@Override
	public long getSiteNavigationMenuItemId() {
		return _siteNavigationMenuItemId;
	}

	@Override
	public void setSiteNavigationMenuItemId(long siteNavigationMenuItemId) {
		_siteNavigationMenuItemId = siteNavigationMenuItemId;
	}

	@JSON
	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		_columnBitmask |= GROUPID_COLUMN_BITMASK;

		if (!_setOriginalGroupId) {
			_setOriginalGroupId = true;

			_originalGroupId = _groupId;
		}

		_groupId = groupId;
	}

	public long getOriginalGroupId() {
		return _originalGroupId;
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_columnBitmask |= COMPANYID_COLUMN_BITMASK;

		if (!_setOriginalCompanyId) {
			_setOriginalCompanyId = true;

			_originalCompanyId = _companyId;
		}

		_companyId = companyId;
	}

	public long getOriginalCompanyId() {
		return _originalCompanyId;
	}

	@JSON
	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@JSON
	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@JSON
	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		_modifiedDate = modifiedDate;
	}

	@JSON
	@Override
	public long getSiteNavigationMenuId() {
		return _siteNavigationMenuId;
	}

	@Override
	public void setSiteNavigationMenuId(long siteNavigationMenuId) {
		_columnBitmask |= SITENAVIGATIONMENUID_COLUMN_BITMASK;

		if (!_setOriginalSiteNavigationMenuId) {
			_setOriginalSiteNavigationMenuId = true;

			_originalSiteNavigationMenuId = _siteNavigationMenuId;
		}

		_siteNavigationMenuId = siteNavigationMenuId;
	}

	public long getOriginalSiteNavigationMenuId() {
		return _originalSiteNavigationMenuId;
	}

	@JSON
	@Override
	public long getParentSiteNavigationMenuItemId() {
		return _parentSiteNavigationMenuItemId;
	}

	@Override
	public void setParentSiteNavigationMenuItemId(
		long parentSiteNavigationMenuItemId) {
		_columnBitmask |= PARENTSITENAVIGATIONMENUITEMID_COLUMN_BITMASK;

		if (!_setOriginalParentSiteNavigationMenuItemId) {
			_setOriginalParentSiteNavigationMenuItemId = true;

			_originalParentSiteNavigationMenuItemId = _parentSiteNavigationMenuItemId;
		}

		_parentSiteNavigationMenuItemId = parentSiteNavigationMenuItemId;
	}

	public long getOriginalParentSiteNavigationMenuItemId() {
		return _originalParentSiteNavigationMenuItemId;
	}

	@JSON
	@Override
	public String getName() {
		if (_name == null) {
			return "";
		}
		else {
			return _name;
		}
	}

	@Override
	public void setName(String name) {
		_columnBitmask |= NAME_COLUMN_BITMASK;

		if (_originalName == null) {
			_originalName = _name;
		}

		_name = name;
	}

	public String getOriginalName() {
		return GetterUtil.getString(_originalName);
	}

	@JSON
	@Override
	public String getType() {
		if (_type == null) {
			return "";
		}
		else {
			return _type;
		}
	}

	@Override
	public void setType(String type) {
		_type = type;
	}

	@JSON
	@Override
	public String getTypeSettings() {
		if (_typeSettings == null) {
			return "";
		}
		else {
			return _typeSettings;
		}
	}

	@Override
	public void setTypeSettings(String typeSettings) {
		_typeSettings = typeSettings;
	}

	@JSON
	@Override
	public int getOrder() {
		return _order;
	}

	@Override
	public void setOrder(int order) {
		_order = order;
	}

	@JSON
	@Override
	public Date getLastPublishDate() {
		return _lastPublishDate;
	}

	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_lastPublishDate = lastPublishDate;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(PortalUtil.getClassNameId(
				SiteNavigationMenuItem.class.getName()));
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			SiteNavigationMenuItem.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public SiteNavigationMenuItem toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (SiteNavigationMenuItem)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		SiteNavigationMenuItemImpl siteNavigationMenuItemImpl = new SiteNavigationMenuItemImpl();

		siteNavigationMenuItemImpl.setUuid(getUuid());
		siteNavigationMenuItemImpl.setSiteNavigationMenuItemId(getSiteNavigationMenuItemId());
		siteNavigationMenuItemImpl.setGroupId(getGroupId());
		siteNavigationMenuItemImpl.setCompanyId(getCompanyId());
		siteNavigationMenuItemImpl.setUserId(getUserId());
		siteNavigationMenuItemImpl.setUserName(getUserName());
		siteNavigationMenuItemImpl.setCreateDate(getCreateDate());
		siteNavigationMenuItemImpl.setModifiedDate(getModifiedDate());
		siteNavigationMenuItemImpl.setSiteNavigationMenuId(getSiteNavigationMenuId());
		siteNavigationMenuItemImpl.setParentSiteNavigationMenuItemId(getParentSiteNavigationMenuItemId());
		siteNavigationMenuItemImpl.setName(getName());
		siteNavigationMenuItemImpl.setType(getType());
		siteNavigationMenuItemImpl.setTypeSettings(getTypeSettings());
		siteNavigationMenuItemImpl.setOrder(getOrder());
		siteNavigationMenuItemImpl.setLastPublishDate(getLastPublishDate());

		siteNavigationMenuItemImpl.resetOriginalValues();

		return siteNavigationMenuItemImpl;
	}

	@Override
	public int compareTo(SiteNavigationMenuItem siteNavigationMenuItem) {
		long primaryKey = siteNavigationMenuItem.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SiteNavigationMenuItem)) {
			return false;
		}

		SiteNavigationMenuItem siteNavigationMenuItem = (SiteNavigationMenuItem)obj;

		long primaryKey = siteNavigationMenuItem.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		SiteNavigationMenuItemModelImpl siteNavigationMenuItemModelImpl = this;

		siteNavigationMenuItemModelImpl._originalUuid = siteNavigationMenuItemModelImpl._uuid;

		siteNavigationMenuItemModelImpl._originalGroupId = siteNavigationMenuItemModelImpl._groupId;

		siteNavigationMenuItemModelImpl._setOriginalGroupId = false;

		siteNavigationMenuItemModelImpl._originalCompanyId = siteNavigationMenuItemModelImpl._companyId;

		siteNavigationMenuItemModelImpl._setOriginalCompanyId = false;

		siteNavigationMenuItemModelImpl._setModifiedDate = false;

		siteNavigationMenuItemModelImpl._originalSiteNavigationMenuId = siteNavigationMenuItemModelImpl._siteNavigationMenuId;

		siteNavigationMenuItemModelImpl._setOriginalSiteNavigationMenuId = false;

		siteNavigationMenuItemModelImpl._originalParentSiteNavigationMenuItemId = siteNavigationMenuItemModelImpl._parentSiteNavigationMenuItemId;

		siteNavigationMenuItemModelImpl._setOriginalParentSiteNavigationMenuItemId = false;

		siteNavigationMenuItemModelImpl._originalName = siteNavigationMenuItemModelImpl._name;

		siteNavigationMenuItemModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<SiteNavigationMenuItem> toCacheModel() {
		SiteNavigationMenuItemCacheModel siteNavigationMenuItemCacheModel = new SiteNavigationMenuItemCacheModel();

		siteNavigationMenuItemCacheModel.uuid = getUuid();

		String uuid = siteNavigationMenuItemCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			siteNavigationMenuItemCacheModel.uuid = null;
		}

		siteNavigationMenuItemCacheModel.siteNavigationMenuItemId = getSiteNavigationMenuItemId();

		siteNavigationMenuItemCacheModel.groupId = getGroupId();

		siteNavigationMenuItemCacheModel.companyId = getCompanyId();

		siteNavigationMenuItemCacheModel.userId = getUserId();

		siteNavigationMenuItemCacheModel.userName = getUserName();

		String userName = siteNavigationMenuItemCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			siteNavigationMenuItemCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			siteNavigationMenuItemCacheModel.createDate = createDate.getTime();
		}
		else {
			siteNavigationMenuItemCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			siteNavigationMenuItemCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			siteNavigationMenuItemCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		siteNavigationMenuItemCacheModel.siteNavigationMenuId = getSiteNavigationMenuId();

		siteNavigationMenuItemCacheModel.parentSiteNavigationMenuItemId = getParentSiteNavigationMenuItemId();

		siteNavigationMenuItemCacheModel.name = getName();

		String name = siteNavigationMenuItemCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			siteNavigationMenuItemCacheModel.name = null;
		}

		siteNavigationMenuItemCacheModel.type = getType();

		String type = siteNavigationMenuItemCacheModel.type;

		if ((type != null) && (type.length() == 0)) {
			siteNavigationMenuItemCacheModel.type = null;
		}

		siteNavigationMenuItemCacheModel.typeSettings = getTypeSettings();

		String typeSettings = siteNavigationMenuItemCacheModel.typeSettings;

		if ((typeSettings != null) && (typeSettings.length() == 0)) {
			siteNavigationMenuItemCacheModel.typeSettings = null;
		}

		siteNavigationMenuItemCacheModel.order = getOrder();

		Date lastPublishDate = getLastPublishDate();

		if (lastPublishDate != null) {
			siteNavigationMenuItemCacheModel.lastPublishDate = lastPublishDate.getTime();
		}
		else {
			siteNavigationMenuItemCacheModel.lastPublishDate = Long.MIN_VALUE;
		}

		return siteNavigationMenuItemCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(31);

		sb.append("{uuid=");
		sb.append(getUuid());
		sb.append(", siteNavigationMenuItemId=");
		sb.append(getSiteNavigationMenuItemId());
		sb.append(", groupId=");
		sb.append(getGroupId());
		sb.append(", companyId=");
		sb.append(getCompanyId());
		sb.append(", userId=");
		sb.append(getUserId());
		sb.append(", userName=");
		sb.append(getUserName());
		sb.append(", createDate=");
		sb.append(getCreateDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append(", siteNavigationMenuId=");
		sb.append(getSiteNavigationMenuId());
		sb.append(", parentSiteNavigationMenuItemId=");
		sb.append(getParentSiteNavigationMenuItemId());
		sb.append(", name=");
		sb.append(getName());
		sb.append(", type=");
		sb.append(getType());
		sb.append(", typeSettings=");
		sb.append(getTypeSettings());
		sb.append(", order=");
		sb.append(getOrder());
		sb.append(", lastPublishDate=");
		sb.append(getLastPublishDate());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(49);

		sb.append("<model><model-name>");
		sb.append("com.liferay.site.navigation.model.SiteNavigationMenuItem");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>uuid</column-name><column-value><![CDATA[");
		sb.append(getUuid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>siteNavigationMenuItemId</column-name><column-value><![CDATA[");
		sb.append(getSiteNavigationMenuItemId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>groupId</column-name><column-value><![CDATA[");
		sb.append(getGroupId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>companyId</column-name><column-value><![CDATA[");
		sb.append(getCompanyId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userId</column-name><column-value><![CDATA[");
		sb.append(getUserId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>userName</column-name><column-value><![CDATA[");
		sb.append(getUserName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>createDate</column-name><column-value><![CDATA[");
		sb.append(getCreateDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>modifiedDate</column-name><column-value><![CDATA[");
		sb.append(getModifiedDate());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>siteNavigationMenuId</column-name><column-value><![CDATA[");
		sb.append(getSiteNavigationMenuId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>parentSiteNavigationMenuItemId</column-name><column-value><![CDATA[");
		sb.append(getParentSiteNavigationMenuItemId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>name</column-name><column-value><![CDATA[");
		sb.append(getName());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>type</column-name><column-value><![CDATA[");
		sb.append(getType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>typeSettings</column-name><column-value><![CDATA[");
		sb.append(getTypeSettings());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>order</column-name><column-value><![CDATA[");
		sb.append(getOrder());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>lastPublishDate</column-name><column-value><![CDATA[");
		sb.append(getLastPublishDate());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = SiteNavigationMenuItem.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			SiteNavigationMenuItem.class, ModelWrapper.class
		};
	private String _uuid;
	private String _originalUuid;
	private long _siteNavigationMenuItemId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private long _siteNavigationMenuId;
	private long _originalSiteNavigationMenuId;
	private boolean _setOriginalSiteNavigationMenuId;
	private long _parentSiteNavigationMenuItemId;
	private long _originalParentSiteNavigationMenuItemId;
	private boolean _setOriginalParentSiteNavigationMenuItemId;
	private String _name;
	private String _originalName;
	private String _type;
	private String _typeSettings;
	private int _order;
	private Date _lastPublishDate;
	private long _columnBitmask;
	private SiteNavigationMenuItem _escapedModel;
}