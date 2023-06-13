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

package com.liferay.asset.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.asset.model.AssetEntryUsage;
import com.liferay.asset.model.AssetEntryUsageModel;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;

import com.liferay.exportimport.kernel.lar.StagedModelType;

import com.liferay.petra.string.StringBundler;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the AssetEntryUsage service. Represents a row in the &quot;AssetEntryUsage&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>AssetEntryUsageModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AssetEntryUsageImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetEntryUsageImpl
 * @generated
 */
@ProviderType
public class AssetEntryUsageModelImpl extends BaseModelImpl<AssetEntryUsage>
	implements AssetEntryUsageModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a asset entry usage model instance should use the <code>AssetEntryUsage</code> interface instead.
	 */
	public static final String TABLE_NAME = "AssetEntryUsage";
	public static final Object[][] TABLE_COLUMNS = {
			{ "uuid_", Types.VARCHAR },
			{ "assetEntryUsageId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "assetEntryId", Types.BIGINT },
			{ "classNameId", Types.BIGINT },
			{ "classPK", Types.BIGINT },
			{ "portletId", Types.VARCHAR },
			{ "lastPublishDate", Types.TIMESTAMP }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("assetEntryUsageId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("assetEntryId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("classNameId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("classPK", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("portletId", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("lastPublishDate", Types.TIMESTAMP);
	}

	public static final String TABLE_SQL_CREATE = "create table AssetEntryUsage (uuid_ VARCHAR(75) null,assetEntryUsageId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,assetEntryId LONG,classNameId LONG,classPK LONG,portletId VARCHAR(200) null,lastPublishDate DATE null)";
	public static final String TABLE_SQL_DROP = "drop table AssetEntryUsage";
	public static final String ORDER_BY_JPQL = " ORDER BY assetEntryUsage.assetEntryUsageId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY AssetEntryUsage.assetEntryUsageId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.asset.service.util.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.asset.model.AssetEntryUsage"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.asset.service.util.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.asset.model.AssetEntryUsage"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.asset.service.util.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.asset.model.AssetEntryUsage"),
			true);
	public static final long ASSETENTRYID_COLUMN_BITMASK = 1L;
	public static final long CLASSNAMEID_COLUMN_BITMASK = 2L;
	public static final long CLASSPK_COLUMN_BITMASK = 4L;
	public static final long COMPANYID_COLUMN_BITMASK = 8L;
	public static final long GROUPID_COLUMN_BITMASK = 16L;
	public static final long PORTLETID_COLUMN_BITMASK = 32L;
	public static final long UUID_COLUMN_BITMASK = 64L;
	public static final long ASSETENTRYUSAGEID_COLUMN_BITMASK = 128L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.asset.service.util.ServiceProps.get(
				"lock.expiration.time.com.liferay.asset.model.AssetEntryUsage"));

	public AssetEntryUsageModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _assetEntryUsageId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setAssetEntryUsageId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _assetEntryUsageId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return AssetEntryUsage.class;
	}

	@Override
	public String getModelClassName() {
		return AssetEntryUsage.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<AssetEntryUsage, Object>> attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<AssetEntryUsage, Object>> entry : attributeGetterFunctions.entrySet()) {
			String attributeName = entry.getKey();
			Function<AssetEntryUsage, Object> attributeGetterFunction = entry.getValue();

			attributes.put(attributeName,
				attributeGetterFunction.apply((AssetEntryUsage)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<AssetEntryUsage, Object>> attributeSetterBiConsumers =
			getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<AssetEntryUsage, Object> attributeSetterBiConsumer = attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept((AssetEntryUsage)this,
					entry.getValue());
			}
		}
	}

	public Map<String, Function<AssetEntryUsage, Object>> getAttributeGetterFunctions() {
		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<AssetEntryUsage, Object>> getAttributeSetterBiConsumers() {
		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<AssetEntryUsage, Object>> _attributeGetterFunctions;
	private static final Map<String, BiConsumer<AssetEntryUsage, Object>> _attributeSetterBiConsumers;

	static {
		Map<String, Function<AssetEntryUsage, Object>> attributeGetterFunctions = new LinkedHashMap<String, Function<AssetEntryUsage, Object>>();
		Map<String, BiConsumer<AssetEntryUsage, ?>> attributeSetterBiConsumers = new LinkedHashMap<String, BiConsumer<AssetEntryUsage, ?>>();


		attributeGetterFunctions.put("uuid", AssetEntryUsage::getUuid);
		attributeSetterBiConsumers.put("uuid", (BiConsumer<AssetEntryUsage, String>)AssetEntryUsage::setUuid);
		attributeGetterFunctions.put("assetEntryUsageId", AssetEntryUsage::getAssetEntryUsageId);
		attributeSetterBiConsumers.put("assetEntryUsageId", (BiConsumer<AssetEntryUsage, Long>)AssetEntryUsage::setAssetEntryUsageId);
		attributeGetterFunctions.put("groupId", AssetEntryUsage::getGroupId);
		attributeSetterBiConsumers.put("groupId", (BiConsumer<AssetEntryUsage, Long>)AssetEntryUsage::setGroupId);
		attributeGetterFunctions.put("companyId", AssetEntryUsage::getCompanyId);
		attributeSetterBiConsumers.put("companyId", (BiConsumer<AssetEntryUsage, Long>)AssetEntryUsage::setCompanyId);
		attributeGetterFunctions.put("userId", AssetEntryUsage::getUserId);
		attributeSetterBiConsumers.put("userId", (BiConsumer<AssetEntryUsage, Long>)AssetEntryUsage::setUserId);
		attributeGetterFunctions.put("userName", AssetEntryUsage::getUserName);
		attributeSetterBiConsumers.put("userName", (BiConsumer<AssetEntryUsage, String>)AssetEntryUsage::setUserName);
		attributeGetterFunctions.put("createDate", AssetEntryUsage::getCreateDate);
		attributeSetterBiConsumers.put("createDate", (BiConsumer<AssetEntryUsage, Date>)AssetEntryUsage::setCreateDate);
		attributeGetterFunctions.put("modifiedDate", AssetEntryUsage::getModifiedDate);
		attributeSetterBiConsumers.put("modifiedDate", (BiConsumer<AssetEntryUsage, Date>)AssetEntryUsage::setModifiedDate);
		attributeGetterFunctions.put("assetEntryId", AssetEntryUsage::getAssetEntryId);
		attributeSetterBiConsumers.put("assetEntryId", (BiConsumer<AssetEntryUsage, Long>)AssetEntryUsage::setAssetEntryId);
		attributeGetterFunctions.put("classNameId", AssetEntryUsage::getClassNameId);
		attributeSetterBiConsumers.put("classNameId", (BiConsumer<AssetEntryUsage, Long>)AssetEntryUsage::setClassNameId);
		attributeGetterFunctions.put("classPK", AssetEntryUsage::getClassPK);
		attributeSetterBiConsumers.put("classPK", (BiConsumer<AssetEntryUsage, Long>)AssetEntryUsage::setClassPK);
		attributeGetterFunctions.put("portletId", AssetEntryUsage::getPortletId);
		attributeSetterBiConsumers.put("portletId", (BiConsumer<AssetEntryUsage, String>)AssetEntryUsage::setPortletId);
		attributeGetterFunctions.put("lastPublishDate", AssetEntryUsage::getLastPublishDate);
		attributeSetterBiConsumers.put("lastPublishDate", (BiConsumer<AssetEntryUsage, Date>)AssetEntryUsage::setLastPublishDate);


		_attributeGetterFunctions = Collections.unmodifiableMap(attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap((Map)attributeSetterBiConsumers);
	}

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

	@Override
	public long getAssetEntryUsageId() {
		return _assetEntryUsageId;
	}

	@Override
	public void setAssetEntryUsageId(long assetEntryUsageId) {
		_assetEntryUsageId = assetEntryUsageId;
	}

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

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

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

	@Override
	public long getAssetEntryId() {
		return _assetEntryId;
	}

	@Override
	public void setAssetEntryId(long assetEntryId) {
		_columnBitmask |= ASSETENTRYID_COLUMN_BITMASK;

		if (!_setOriginalAssetEntryId) {
			_setOriginalAssetEntryId = true;

			_originalAssetEntryId = _assetEntryId;
		}

		_assetEntryId = assetEntryId;
	}

	public long getOriginalAssetEntryId() {
		return _originalAssetEntryId;
	}

	@Override
	public String getClassName() {
		if (getClassNameId() <= 0) {
			return "";
		}

		return PortalUtil.getClassName(getClassNameId());
	}

	@Override
	public void setClassName(String className) {
		long classNameId = 0;

		if (Validator.isNotNull(className)) {
			classNameId = PortalUtil.getClassNameId(className);
		}

		setClassNameId(classNameId);
	}

	@Override
	public long getClassNameId() {
		return _classNameId;
	}

	@Override
	public void setClassNameId(long classNameId) {
		_columnBitmask |= CLASSNAMEID_COLUMN_BITMASK;

		if (!_setOriginalClassNameId) {
			_setOriginalClassNameId = true;

			_originalClassNameId = _classNameId;
		}

		_classNameId = classNameId;
	}

	public long getOriginalClassNameId() {
		return _originalClassNameId;
	}

	@Override
	public long getClassPK() {
		return _classPK;
	}

	@Override
	public void setClassPK(long classPK) {
		_columnBitmask |= CLASSPK_COLUMN_BITMASK;

		if (!_setOriginalClassPK) {
			_setOriginalClassPK = true;

			_originalClassPK = _classPK;
		}

		_classPK = classPK;
	}

	public long getOriginalClassPK() {
		return _originalClassPK;
	}

	@Override
	public String getPortletId() {
		if (_portletId == null) {
			return "";
		}
		else {
			return _portletId;
		}
	}

	@Override
	public void setPortletId(String portletId) {
		_columnBitmask |= PORTLETID_COLUMN_BITMASK;

		if (_originalPortletId == null) {
			_originalPortletId = _portletId;
		}

		_portletId = portletId;
	}

	public String getOriginalPortletId() {
		return GetterUtil.getString(_originalPortletId);
	}

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
				AssetEntryUsage.class.getName()), getClassNameId());
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			AssetEntryUsage.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public AssetEntryUsage toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (AssetEntryUsage)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		AssetEntryUsageImpl assetEntryUsageImpl = new AssetEntryUsageImpl();

		assetEntryUsageImpl.setUuid(getUuid());
		assetEntryUsageImpl.setAssetEntryUsageId(getAssetEntryUsageId());
		assetEntryUsageImpl.setGroupId(getGroupId());
		assetEntryUsageImpl.setCompanyId(getCompanyId());
		assetEntryUsageImpl.setUserId(getUserId());
		assetEntryUsageImpl.setUserName(getUserName());
		assetEntryUsageImpl.setCreateDate(getCreateDate());
		assetEntryUsageImpl.setModifiedDate(getModifiedDate());
		assetEntryUsageImpl.setAssetEntryId(getAssetEntryId());
		assetEntryUsageImpl.setClassNameId(getClassNameId());
		assetEntryUsageImpl.setClassPK(getClassPK());
		assetEntryUsageImpl.setPortletId(getPortletId());
		assetEntryUsageImpl.setLastPublishDate(getLastPublishDate());

		assetEntryUsageImpl.resetOriginalValues();

		return assetEntryUsageImpl;
	}

	@Override
	public int compareTo(AssetEntryUsage assetEntryUsage) {
		long primaryKey = assetEntryUsage.getPrimaryKey();

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

		if (!(obj instanceof AssetEntryUsage)) {
			return false;
		}

		AssetEntryUsage assetEntryUsage = (AssetEntryUsage)obj;

		long primaryKey = assetEntryUsage.getPrimaryKey();

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
		AssetEntryUsageModelImpl assetEntryUsageModelImpl = this;

		assetEntryUsageModelImpl._originalUuid = assetEntryUsageModelImpl._uuid;

		assetEntryUsageModelImpl._originalGroupId = assetEntryUsageModelImpl._groupId;

		assetEntryUsageModelImpl._setOriginalGroupId = false;

		assetEntryUsageModelImpl._originalCompanyId = assetEntryUsageModelImpl._companyId;

		assetEntryUsageModelImpl._setOriginalCompanyId = false;

		assetEntryUsageModelImpl._setModifiedDate = false;

		assetEntryUsageModelImpl._originalAssetEntryId = assetEntryUsageModelImpl._assetEntryId;

		assetEntryUsageModelImpl._setOriginalAssetEntryId = false;

		assetEntryUsageModelImpl._originalClassNameId = assetEntryUsageModelImpl._classNameId;

		assetEntryUsageModelImpl._setOriginalClassNameId = false;

		assetEntryUsageModelImpl._originalClassPK = assetEntryUsageModelImpl._classPK;

		assetEntryUsageModelImpl._setOriginalClassPK = false;

		assetEntryUsageModelImpl._originalPortletId = assetEntryUsageModelImpl._portletId;

		assetEntryUsageModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<AssetEntryUsage> toCacheModel() {
		AssetEntryUsageCacheModel assetEntryUsageCacheModel = new AssetEntryUsageCacheModel();

		assetEntryUsageCacheModel.uuid = getUuid();

		String uuid = assetEntryUsageCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			assetEntryUsageCacheModel.uuid = null;
		}

		assetEntryUsageCacheModel.assetEntryUsageId = getAssetEntryUsageId();

		assetEntryUsageCacheModel.groupId = getGroupId();

		assetEntryUsageCacheModel.companyId = getCompanyId();

		assetEntryUsageCacheModel.userId = getUserId();

		assetEntryUsageCacheModel.userName = getUserName();

		String userName = assetEntryUsageCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			assetEntryUsageCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			assetEntryUsageCacheModel.createDate = createDate.getTime();
		}
		else {
			assetEntryUsageCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			assetEntryUsageCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			assetEntryUsageCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		assetEntryUsageCacheModel.assetEntryId = getAssetEntryId();

		assetEntryUsageCacheModel.classNameId = getClassNameId();

		assetEntryUsageCacheModel.classPK = getClassPK();

		assetEntryUsageCacheModel.portletId = getPortletId();

		String portletId = assetEntryUsageCacheModel.portletId;

		if ((portletId != null) && (portletId.length() == 0)) {
			assetEntryUsageCacheModel.portletId = null;
		}

		Date lastPublishDate = getLastPublishDate();

		if (lastPublishDate != null) {
			assetEntryUsageCacheModel.lastPublishDate = lastPublishDate.getTime();
		}
		else {
			assetEntryUsageCacheModel.lastPublishDate = Long.MIN_VALUE;
		}

		return assetEntryUsageCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<AssetEntryUsage, Object>> attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler((4 * attributeGetterFunctions.size()) +
				2);

		sb.append("{");

		for (Map.Entry<String, Function<AssetEntryUsage, Object>> entry : attributeGetterFunctions.entrySet()) {
			String attributeName = entry.getKey();
			Function<AssetEntryUsage, Object> attributeGetterFunction = entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((AssetEntryUsage)this));
			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		Map<String, Function<AssetEntryUsage, Object>> attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler((5 * attributeGetterFunctions.size()) +
				4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<AssetEntryUsage, Object>> entry : attributeGetterFunctions.entrySet()) {
			String attributeName = entry.getKey();
			Function<AssetEntryUsage, Object> attributeGetterFunction = entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((AssetEntryUsage)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = AssetEntryUsage.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			AssetEntryUsage.class, ModelWrapper.class
		};
	private String _uuid;
	private String _originalUuid;
	private long _assetEntryUsageId;
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
	private long _assetEntryId;
	private long _originalAssetEntryId;
	private boolean _setOriginalAssetEntryId;
	private long _classNameId;
	private long _originalClassNameId;
	private boolean _setOriginalClassNameId;
	private long _classPK;
	private long _originalClassPK;
	private boolean _setOriginalClassPK;
	private String _portletId;
	private String _originalPortletId;
	private Date _lastPublishDate;
	private long _columnBitmask;
	private AssetEntryUsage _escapedModel;
}