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

package com.liferay.asset.display.page.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.asset.display.page.model.AssetDisplayPageEntry;
import com.liferay.asset.display.page.model.AssetDisplayPageEntryModel;

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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The base model implementation for the AssetDisplayPageEntry service. Represents a row in the &quot;AssetDisplayPageEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link AssetDisplayPageEntryModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AssetDisplayPageEntryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetDisplayPageEntryImpl
 * @see AssetDisplayPageEntry
 * @see AssetDisplayPageEntryModel
 * @generated
 */
@ProviderType
public class AssetDisplayPageEntryModelImpl extends BaseModelImpl<AssetDisplayPageEntry>
	implements AssetDisplayPageEntryModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a asset display page entry model instance should use the {@link AssetDisplayPageEntry} interface instead.
	 */
	public static final String TABLE_NAME = "AssetDisplayPageEntry";
	public static final Object[][] TABLE_COLUMNS = {
			{ "uuid_", Types.VARCHAR },
			{ "assetDisplayPageEntryId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "classNameId", Types.BIGINT },
			{ "classPK", Types.BIGINT },
			{ "layoutPageTemplateEntryId", Types.BIGINT },
			{ "type_", Types.INTEGER },
			{ "plid", Types.BIGINT }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("assetDisplayPageEntryId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("classNameId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("classPK", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("layoutPageTemplateEntryId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("type_", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("plid", Types.BIGINT);
	}

	public static final String TABLE_SQL_CREATE = "create table AssetDisplayPageEntry (uuid_ VARCHAR(75) null,assetDisplayPageEntryId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,classNameId LONG,classPK LONG,layoutPageTemplateEntryId LONG,type_ INTEGER,plid LONG)";
	public static final String TABLE_SQL_DROP = "drop table AssetDisplayPageEntry";
	public static final String ORDER_BY_JPQL = " ORDER BY assetDisplayPageEntry.assetDisplayPageEntryId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY AssetDisplayPageEntry.assetDisplayPageEntryId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.asset.display.page.service.util.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.asset.display.page.model.AssetDisplayPageEntry"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.asset.display.page.service.util.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.asset.display.page.model.AssetDisplayPageEntry"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.asset.display.page.service.util.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.asset.display.page.model.AssetDisplayPageEntry"),
			true);
	public static final long CLASSNAMEID_COLUMN_BITMASK = 1L;
	public static final long CLASSPK_COLUMN_BITMASK = 2L;
	public static final long COMPANYID_COLUMN_BITMASK = 4L;
	public static final long GROUPID_COLUMN_BITMASK = 8L;
	public static final long LAYOUTPAGETEMPLATEENTRYID_COLUMN_BITMASK = 16L;
	public static final long UUID_COLUMN_BITMASK = 32L;
	public static final long ASSETDISPLAYPAGEENTRYID_COLUMN_BITMASK = 64L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.asset.display.page.service.util.ServiceProps.get(
				"lock.expiration.time.com.liferay.asset.display.page.model.AssetDisplayPageEntry"));

	public AssetDisplayPageEntryModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _assetDisplayPageEntryId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setAssetDisplayPageEntryId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _assetDisplayPageEntryId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return AssetDisplayPageEntry.class;
	}

	@Override
	public String getModelClassName() {
		return AssetDisplayPageEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("assetDisplayPageEntryId", getAssetDisplayPageEntryId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("classNameId", getClassNameId());
		attributes.put("classPK", getClassPK());
		attributes.put("layoutPageTemplateEntryId",
			getLayoutPageTemplateEntryId());
		attributes.put("type", getType());
		attributes.put("plid", getPlid());

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

		Long assetDisplayPageEntryId = (Long)attributes.get(
				"assetDisplayPageEntryId");

		if (assetDisplayPageEntryId != null) {
			setAssetDisplayPageEntryId(assetDisplayPageEntryId);
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

		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		Long classPK = (Long)attributes.get("classPK");

		if (classPK != null) {
			setClassPK(classPK);
		}

		Long layoutPageTemplateEntryId = (Long)attributes.get(
				"layoutPageTemplateEntryId");

		if (layoutPageTemplateEntryId != null) {
			setLayoutPageTemplateEntryId(layoutPageTemplateEntryId);
		}

		Integer type = (Integer)attributes.get("type");

		if (type != null) {
			setType(type);
		}

		Long plid = (Long)attributes.get("plid");

		if (plid != null) {
			setPlid(plid);
		}
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
	public long getAssetDisplayPageEntryId() {
		return _assetDisplayPageEntryId;
	}

	@Override
	public void setAssetDisplayPageEntryId(long assetDisplayPageEntryId) {
		_assetDisplayPageEntryId = assetDisplayPageEntryId;
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
	public long getLayoutPageTemplateEntryId() {
		return _layoutPageTemplateEntryId;
	}

	@Override
	public void setLayoutPageTemplateEntryId(long layoutPageTemplateEntryId) {
		_columnBitmask |= LAYOUTPAGETEMPLATEENTRYID_COLUMN_BITMASK;

		if (!_setOriginalLayoutPageTemplateEntryId) {
			_setOriginalLayoutPageTemplateEntryId = true;

			_originalLayoutPageTemplateEntryId = _layoutPageTemplateEntryId;
		}

		_layoutPageTemplateEntryId = layoutPageTemplateEntryId;
	}

	public long getOriginalLayoutPageTemplateEntryId() {
		return _originalLayoutPageTemplateEntryId;
	}

	@Override
	public int getType() {
		return _type;
	}

	@Override
	public void setType(int type) {
		_type = type;
	}

	@Override
	public long getPlid() {
		return _plid;
	}

	@Override
	public void setPlid(long plid) {
		_plid = plid;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(PortalUtil.getClassNameId(
				AssetDisplayPageEntry.class.getName()), getClassNameId());
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			AssetDisplayPageEntry.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public AssetDisplayPageEntry toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (AssetDisplayPageEntry)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		AssetDisplayPageEntryImpl assetDisplayPageEntryImpl = new AssetDisplayPageEntryImpl();

		assetDisplayPageEntryImpl.setUuid(getUuid());
		assetDisplayPageEntryImpl.setAssetDisplayPageEntryId(getAssetDisplayPageEntryId());
		assetDisplayPageEntryImpl.setGroupId(getGroupId());
		assetDisplayPageEntryImpl.setCompanyId(getCompanyId());
		assetDisplayPageEntryImpl.setUserId(getUserId());
		assetDisplayPageEntryImpl.setUserName(getUserName());
		assetDisplayPageEntryImpl.setCreateDate(getCreateDate());
		assetDisplayPageEntryImpl.setModifiedDate(getModifiedDate());
		assetDisplayPageEntryImpl.setClassNameId(getClassNameId());
		assetDisplayPageEntryImpl.setClassPK(getClassPK());
		assetDisplayPageEntryImpl.setLayoutPageTemplateEntryId(getLayoutPageTemplateEntryId());
		assetDisplayPageEntryImpl.setType(getType());
		assetDisplayPageEntryImpl.setPlid(getPlid());

		assetDisplayPageEntryImpl.resetOriginalValues();

		return assetDisplayPageEntryImpl;
	}

	@Override
	public int compareTo(AssetDisplayPageEntry assetDisplayPageEntry) {
		long primaryKey = assetDisplayPageEntry.getPrimaryKey();

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

		if (!(obj instanceof AssetDisplayPageEntry)) {
			return false;
		}

		AssetDisplayPageEntry assetDisplayPageEntry = (AssetDisplayPageEntry)obj;

		long primaryKey = assetDisplayPageEntry.getPrimaryKey();

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
		AssetDisplayPageEntryModelImpl assetDisplayPageEntryModelImpl = this;

		assetDisplayPageEntryModelImpl._originalUuid = assetDisplayPageEntryModelImpl._uuid;

		assetDisplayPageEntryModelImpl._originalGroupId = assetDisplayPageEntryModelImpl._groupId;

		assetDisplayPageEntryModelImpl._setOriginalGroupId = false;

		assetDisplayPageEntryModelImpl._originalCompanyId = assetDisplayPageEntryModelImpl._companyId;

		assetDisplayPageEntryModelImpl._setOriginalCompanyId = false;

		assetDisplayPageEntryModelImpl._setModifiedDate = false;

		assetDisplayPageEntryModelImpl._originalClassNameId = assetDisplayPageEntryModelImpl._classNameId;

		assetDisplayPageEntryModelImpl._setOriginalClassNameId = false;

		assetDisplayPageEntryModelImpl._originalClassPK = assetDisplayPageEntryModelImpl._classPK;

		assetDisplayPageEntryModelImpl._setOriginalClassPK = false;

		assetDisplayPageEntryModelImpl._originalLayoutPageTemplateEntryId = assetDisplayPageEntryModelImpl._layoutPageTemplateEntryId;

		assetDisplayPageEntryModelImpl._setOriginalLayoutPageTemplateEntryId = false;

		assetDisplayPageEntryModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<AssetDisplayPageEntry> toCacheModel() {
		AssetDisplayPageEntryCacheModel assetDisplayPageEntryCacheModel = new AssetDisplayPageEntryCacheModel();

		assetDisplayPageEntryCacheModel.uuid = getUuid();

		String uuid = assetDisplayPageEntryCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			assetDisplayPageEntryCacheModel.uuid = null;
		}

		assetDisplayPageEntryCacheModel.assetDisplayPageEntryId = getAssetDisplayPageEntryId();

		assetDisplayPageEntryCacheModel.groupId = getGroupId();

		assetDisplayPageEntryCacheModel.companyId = getCompanyId();

		assetDisplayPageEntryCacheModel.userId = getUserId();

		assetDisplayPageEntryCacheModel.userName = getUserName();

		String userName = assetDisplayPageEntryCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			assetDisplayPageEntryCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			assetDisplayPageEntryCacheModel.createDate = createDate.getTime();
		}
		else {
			assetDisplayPageEntryCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			assetDisplayPageEntryCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			assetDisplayPageEntryCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		assetDisplayPageEntryCacheModel.classNameId = getClassNameId();

		assetDisplayPageEntryCacheModel.classPK = getClassPK();

		assetDisplayPageEntryCacheModel.layoutPageTemplateEntryId = getLayoutPageTemplateEntryId();

		assetDisplayPageEntryCacheModel.type = getType();

		assetDisplayPageEntryCacheModel.plid = getPlid();

		return assetDisplayPageEntryCacheModel;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(27);

		sb.append("{uuid=");
		sb.append(getUuid());
		sb.append(", assetDisplayPageEntryId=");
		sb.append(getAssetDisplayPageEntryId());
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
		sb.append(", classNameId=");
		sb.append(getClassNameId());
		sb.append(", classPK=");
		sb.append(getClassPK());
		sb.append(", layoutPageTemplateEntryId=");
		sb.append(getLayoutPageTemplateEntryId());
		sb.append(", type=");
		sb.append(getType());
		sb.append(", plid=");
		sb.append(getPlid());
		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		StringBundler sb = new StringBundler(43);

		sb.append("<model><model-name>");
		sb.append("com.liferay.asset.display.page.model.AssetDisplayPageEntry");
		sb.append("</model-name>");

		sb.append(
			"<column><column-name>uuid</column-name><column-value><![CDATA[");
		sb.append(getUuid());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>assetDisplayPageEntryId</column-name><column-value><![CDATA[");
		sb.append(getAssetDisplayPageEntryId());
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
			"<column><column-name>classNameId</column-name><column-value><![CDATA[");
		sb.append(getClassNameId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>classPK</column-name><column-value><![CDATA[");
		sb.append(getClassPK());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>layoutPageTemplateEntryId</column-name><column-value><![CDATA[");
		sb.append(getLayoutPageTemplateEntryId());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>type</column-name><column-value><![CDATA[");
		sb.append(getType());
		sb.append("]]></column-value></column>");
		sb.append(
			"<column><column-name>plid</column-name><column-value><![CDATA[");
		sb.append(getPlid());
		sb.append("]]></column-value></column>");

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = AssetDisplayPageEntry.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			AssetDisplayPageEntry.class, ModelWrapper.class
		};
	private String _uuid;
	private String _originalUuid;
	private long _assetDisplayPageEntryId;
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
	private long _classNameId;
	private long _originalClassNameId;
	private boolean _setOriginalClassNameId;
	private long _classPK;
	private long _originalClassPK;
	private boolean _setOriginalClassPK;
	private long _layoutPageTemplateEntryId;
	private long _originalLayoutPageTemplateEntryId;
	private boolean _setOriginalLayoutPageTemplateEntryId;
	private int _type;
	private long _plid;
	private long _columnBitmask;
	private AssetDisplayPageEntry _escapedModel;
}