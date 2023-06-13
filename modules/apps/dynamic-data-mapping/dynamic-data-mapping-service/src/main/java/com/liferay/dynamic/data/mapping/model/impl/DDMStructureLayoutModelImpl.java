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

package com.liferay.dynamic.data.mapping.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.dynamic.data.mapping.model.DDMStructureLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructureLayoutModel;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;

import com.liferay.exportimport.kernel.lar.StagedModelType;

import com.liferay.petra.string.StringBundler;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the DDMStructureLayout service. Represents a row in the &quot;DDMStructureLayout&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>DDMStructureLayoutModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link DDMStructureLayoutImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DDMStructureLayoutImpl
 * @generated
 */
@ProviderType
public class DDMStructureLayoutModelImpl extends BaseModelImpl<DDMStructureLayout>
	implements DDMStructureLayoutModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a ddm structure layout model instance should use the <code>DDMStructureLayout</code> interface instead.
	 */
	public static final String TABLE_NAME = "DDMStructureLayout";
	public static final Object[][] TABLE_COLUMNS = {
			{ "uuid_", Types.VARCHAR },
			{ "structureLayoutId", Types.BIGINT },
			{ "groupId", Types.BIGINT },
			{ "companyId", Types.BIGINT },
			{ "userId", Types.BIGINT },
			{ "userName", Types.VARCHAR },
			{ "createDate", Types.TIMESTAMP },
			{ "modifiedDate", Types.TIMESTAMP },
			{ "structureVersionId", Types.BIGINT },
			{ "name", Types.CLOB },
			{ "description", Types.CLOB },
			{ "definition", Types.CLOB }
		};
	public static final Map<String, Integer> TABLE_COLUMNS_MAP = new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("structureLayoutId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("structureVersionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("name", Types.CLOB);
		TABLE_COLUMNS_MAP.put("description", Types.CLOB);
		TABLE_COLUMNS_MAP.put("definition", Types.CLOB);
	}

	public static final String TABLE_SQL_CREATE = "create table DDMStructureLayout (uuid_ VARCHAR(75) null,structureLayoutId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,structureVersionId LONG,name TEXT null,description TEXT null,definition TEXT null)";
	public static final String TABLE_SQL_DROP = "drop table DDMStructureLayout";
	public static final String ORDER_BY_JPQL = " ORDER BY ddmStructureLayout.structureLayoutId ASC";
	public static final String ORDER_BY_SQL = " ORDER BY DDMStructureLayout.structureLayoutId ASC";
	public static final String DATA_SOURCE = "liferayDataSource";
	public static final String SESSION_FACTORY = "liferaySessionFactory";
	public static final String TX_MANAGER = "liferayTransactionManager";
	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.dynamic.data.mapping.service.util.ServiceProps.get(
				"value.object.entity.cache.enabled.com.liferay.dynamic.data.mapping.model.DDMStructureLayout"),
			true);
	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.dynamic.data.mapping.service.util.ServiceProps.get(
				"value.object.finder.cache.enabled.com.liferay.dynamic.data.mapping.model.DDMStructureLayout"),
			true);
	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.dynamic.data.mapping.service.util.ServiceProps.get(
				"value.object.column.bitmask.enabled.com.liferay.dynamic.data.mapping.model.DDMStructureLayout"),
			true);
	public static final long COMPANYID_COLUMN_BITMASK = 1L;
	public static final long GROUPID_COLUMN_BITMASK = 2L;
	public static final long STRUCTUREVERSIONID_COLUMN_BITMASK = 4L;
	public static final long UUID_COLUMN_BITMASK = 8L;
	public static final long STRUCTURELAYOUTID_COLUMN_BITMASK = 16L;
	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.dynamic.data.mapping.service.util.ServiceProps.get(
				"lock.expiration.time.com.liferay.dynamic.data.mapping.model.DDMStructureLayout"));

	public DDMStructureLayoutModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _structureLayoutId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setStructureLayoutId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _structureLayoutId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return DDMStructureLayout.class;
	}

	@Override
	public String getModelClassName() {
		return DDMStructureLayout.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<DDMStructureLayout, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<DDMStructureLayout, Object>> entry : attributeGetterFunctions.entrySet()) {
			String attributeName = entry.getKey();
			Function<DDMStructureLayout, Object> attributeGetterFunction = entry.getValue();

			attributes.put(attributeName,
				attributeGetterFunction.apply((DDMStructureLayout)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<DDMStructureLayout, Object>> attributeSetterBiConsumers =
			getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<DDMStructureLayout, Object> attributeSetterBiConsumer = attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept((DDMStructureLayout)this,
					entry.getValue());
			}
		}
	}

	public Map<String, Function<DDMStructureLayout, Object>> getAttributeGetterFunctions() {
		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<DDMStructureLayout, Object>> getAttributeSetterBiConsumers() {
		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<DDMStructureLayout, Object>> _attributeGetterFunctions;
	private static final Map<String, BiConsumer<DDMStructureLayout, Object>> _attributeSetterBiConsumers;

	static {
		Map<String, Function<DDMStructureLayout, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<DDMStructureLayout, Object>>();
		Map<String, BiConsumer<DDMStructureLayout, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<DDMStructureLayout, ?>>();


		attributeGetterFunctions.put("uuid", DDMStructureLayout::getUuid);
		attributeSetterBiConsumers.put("uuid", (BiConsumer<DDMStructureLayout, String>)DDMStructureLayout::setUuid);
		attributeGetterFunctions.put("structureLayoutId", DDMStructureLayout::getStructureLayoutId);
		attributeSetterBiConsumers.put("structureLayoutId", (BiConsumer<DDMStructureLayout, Long>)DDMStructureLayout::setStructureLayoutId);
		attributeGetterFunctions.put("groupId", DDMStructureLayout::getGroupId);
		attributeSetterBiConsumers.put("groupId", (BiConsumer<DDMStructureLayout, Long>)DDMStructureLayout::setGroupId);
		attributeGetterFunctions.put("companyId", DDMStructureLayout::getCompanyId);
		attributeSetterBiConsumers.put("companyId", (BiConsumer<DDMStructureLayout, Long>)DDMStructureLayout::setCompanyId);
		attributeGetterFunctions.put("userId", DDMStructureLayout::getUserId);
		attributeSetterBiConsumers.put("userId", (BiConsumer<DDMStructureLayout, Long>)DDMStructureLayout::setUserId);
		attributeGetterFunctions.put("userName", DDMStructureLayout::getUserName);
		attributeSetterBiConsumers.put("userName", (BiConsumer<DDMStructureLayout, String>)DDMStructureLayout::setUserName);
		attributeGetterFunctions.put("createDate", DDMStructureLayout::getCreateDate);
		attributeSetterBiConsumers.put("createDate", (BiConsumer<DDMStructureLayout, Date>)DDMStructureLayout::setCreateDate);
		attributeGetterFunctions.put("modifiedDate", DDMStructureLayout::getModifiedDate);
		attributeSetterBiConsumers.put("modifiedDate", (BiConsumer<DDMStructureLayout, Date>)DDMStructureLayout::setModifiedDate);
		attributeGetterFunctions.put("structureVersionId", DDMStructureLayout::getStructureVersionId);
		attributeSetterBiConsumers.put("structureVersionId", (BiConsumer<DDMStructureLayout, Long>)DDMStructureLayout::setStructureVersionId);
		attributeGetterFunctions.put("name", DDMStructureLayout::getName);
		attributeSetterBiConsumers.put("name", (BiConsumer<DDMStructureLayout, String>)DDMStructureLayout::setName);
		attributeGetterFunctions.put("description", DDMStructureLayout::getDescription);
		attributeSetterBiConsumers.put("description", (BiConsumer<DDMStructureLayout, String>)DDMStructureLayout::setDescription);
		attributeGetterFunctions.put("definition", DDMStructureLayout::getDefinition);
		attributeSetterBiConsumers.put("definition", (BiConsumer<DDMStructureLayout, String>)DDMStructureLayout::setDefinition);


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
	public long getStructureLayoutId() {
		return _structureLayoutId;
	}

	@Override
	public void setStructureLayoutId(long structureLayoutId) {
		_structureLayoutId = structureLayoutId;
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
	public long getStructureVersionId() {
		return _structureVersionId;
	}

	@Override
	public void setStructureVersionId(long structureVersionId) {
		_columnBitmask |= STRUCTUREVERSIONID_COLUMN_BITMASK;

		if (!_setOriginalStructureVersionId) {
			_setOriginalStructureVersionId = true;

			_originalStructureVersionId = _structureVersionId;
		}

		_structureVersionId = structureVersionId;
	}

	public long getOriginalStructureVersionId() {
		return _originalStructureVersionId;
	}

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
	public String getName(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getName(languageId);
	}

	@Override
	public String getName(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getName(languageId, useDefault);
	}

	@Override
	public String getName(String languageId) {
		return LocalizationUtil.getLocalization(getName(), languageId);
	}

	@Override
	public String getName(String languageId, boolean useDefault) {
		return LocalizationUtil.getLocalization(getName(), languageId,
			useDefault);
	}

	@Override
	public String getNameCurrentLanguageId() {
		return _nameCurrentLanguageId;
	}

	@JSON
	@Override
	public String getNameCurrentValue() {
		Locale locale = getLocale(_nameCurrentLanguageId);

		return getName(locale);
	}

	@Override
	public Map<Locale, String> getNameMap() {
		return LocalizationUtil.getLocalizationMap(getName());
	}

	@Override
	public void setName(String name) {
		_name = name;
	}

	@Override
	public void setName(String name, Locale locale) {
		setName(name, locale, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setName(String name, Locale locale, Locale defaultLocale) {
		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(name)) {
			setName(LocalizationUtil.updateLocalization(getName(), "Name",
					name, languageId, defaultLanguageId));
		}
		else {
			setName(LocalizationUtil.removeLocalization(getName(), "Name",
					languageId));
		}
	}

	@Override
	public void setNameCurrentLanguageId(String languageId) {
		_nameCurrentLanguageId = languageId;
	}

	@Override
	public void setNameMap(Map<Locale, String> nameMap) {
		setNameMap(nameMap, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setNameMap(Map<Locale, String> nameMap, Locale defaultLocale) {
		if (nameMap == null) {
			return;
		}

		setName(LocalizationUtil.updateLocalization(nameMap, getName(), "Name",
				LocaleUtil.toLanguageId(defaultLocale)));
	}

	@Override
	public String getDescription() {
		if (_description == null) {
			return "";
		}
		else {
			return _description;
		}
	}

	@Override
	public String getDescription(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getDescription(languageId);
	}

	@Override
	public String getDescription(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getDescription(languageId, useDefault);
	}

	@Override
	public String getDescription(String languageId) {
		return LocalizationUtil.getLocalization(getDescription(), languageId);
	}

	@Override
	public String getDescription(String languageId, boolean useDefault) {
		return LocalizationUtil.getLocalization(getDescription(), languageId,
			useDefault);
	}

	@Override
	public String getDescriptionCurrentLanguageId() {
		return _descriptionCurrentLanguageId;
	}

	@JSON
	@Override
	public String getDescriptionCurrentValue() {
		Locale locale = getLocale(_descriptionCurrentLanguageId);

		return getDescription(locale);
	}

	@Override
	public Map<Locale, String> getDescriptionMap() {
		return LocalizationUtil.getLocalizationMap(getDescription());
	}

	@Override
	public void setDescription(String description) {
		_description = description;
	}

	@Override
	public void setDescription(String description, Locale locale) {
		setDescription(description, locale, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setDescription(String description, Locale locale,
		Locale defaultLocale) {
		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(description)) {
			setDescription(LocalizationUtil.updateLocalization(
					getDescription(), "Description", description, languageId,
					defaultLanguageId));
		}
		else {
			setDescription(LocalizationUtil.removeLocalization(
					getDescription(), "Description", languageId));
		}
	}

	@Override
	public void setDescriptionCurrentLanguageId(String languageId) {
		_descriptionCurrentLanguageId = languageId;
	}

	@Override
	public void setDescriptionMap(Map<Locale, String> descriptionMap) {
		setDescriptionMap(descriptionMap, LocaleUtil.getSiteDefault());
	}

	@Override
	public void setDescriptionMap(Map<Locale, String> descriptionMap,
		Locale defaultLocale) {
		if (descriptionMap == null) {
			return;
		}

		setDescription(LocalizationUtil.updateLocalization(descriptionMap,
				getDescription(), "Description",
				LocaleUtil.toLanguageId(defaultLocale)));
	}

	@Override
	public String getDefinition() {
		if (_definition == null) {
			return "";
		}
		else {
			return _definition;
		}
	}

	@Override
	public void setDefinition(String definition) {
		_definition = definition;
	}

	public com.liferay.dynamic.data.mapping.model.DDMFormLayout getDDMFormLayout() {
		return null;
	}

	public void setDDMFormLayout(
		com.liferay.dynamic.data.mapping.model.DDMFormLayout ddmFormLayout) {
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(PortalUtil.getClassNameId(
				DDMStructureLayout.class.getName()));
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(getCompanyId(),
			DDMStructureLayout.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public String[] getAvailableLanguageIds() {
		Set<String> availableLanguageIds = new TreeSet<String>();

		Map<Locale, String> nameMap = getNameMap();

		for (Map.Entry<Locale, String> entry : nameMap.entrySet()) {
			Locale locale = entry.getKey();
			String value = entry.getValue();

			if (Validator.isNotNull(value)) {
				availableLanguageIds.add(LocaleUtil.toLanguageId(locale));
			}
		}

		Map<Locale, String> descriptionMap = getDescriptionMap();

		for (Map.Entry<Locale, String> entry : descriptionMap.entrySet()) {
			Locale locale = entry.getKey();
			String value = entry.getValue();

			if (Validator.isNotNull(value)) {
				availableLanguageIds.add(LocaleUtil.toLanguageId(locale));
			}
		}

		return availableLanguageIds.toArray(new String[availableLanguageIds.size()]);
	}

	@Override
	public String getDefaultLanguageId() {
		String xml = getName();

		if (xml == null) {
			return "";
		}

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		return LocalizationUtil.getDefaultLanguageId(xml, defaultLocale);
	}

	@Override
	public void prepareLocalizedFieldsForImport() throws LocaleException {
		Locale defaultLocale = LocaleUtil.fromLanguageId(getDefaultLanguageId());

		Locale[] availableLocales = LocaleUtil.fromLanguageIds(getAvailableLanguageIds());

		Locale defaultImportLocale = LocalizationUtil.getDefaultImportLocale(DDMStructureLayout.class.getName(),
				getPrimaryKey(), defaultLocale, availableLocales);

		prepareLocalizedFieldsForImport(defaultImportLocale);
	}

	@Override
	@SuppressWarnings("unused")
	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale)
		throws LocaleException {
		Locale defaultLocale = LocaleUtil.getSiteDefault();

		String modelDefaultLanguageId = getDefaultLanguageId();

		String name = getName(defaultLocale);

		if (Validator.isNull(name)) {
			setName(getName(modelDefaultLanguageId), defaultLocale);
		}
		else {
			setName(getName(defaultLocale), defaultLocale, defaultLocale);
		}

		String description = getDescription(defaultLocale);

		if (Validator.isNull(description)) {
			setDescription(getDescription(modelDefaultLanguageId), defaultLocale);
		}
		else {
			setDescription(getDescription(defaultLocale), defaultLocale,
				defaultLocale);
		}
	}

	@Override
	public DDMStructureLayout toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (DDMStructureLayout)ProxyUtil.newProxyInstance(_classLoader,
					_escapedModelInterfaces, new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		DDMStructureLayoutImpl ddmStructureLayoutImpl = new DDMStructureLayoutImpl();

		ddmStructureLayoutImpl.setUuid(getUuid());
		ddmStructureLayoutImpl.setStructureLayoutId(getStructureLayoutId());
		ddmStructureLayoutImpl.setGroupId(getGroupId());
		ddmStructureLayoutImpl.setCompanyId(getCompanyId());
		ddmStructureLayoutImpl.setUserId(getUserId());
		ddmStructureLayoutImpl.setUserName(getUserName());
		ddmStructureLayoutImpl.setCreateDate(getCreateDate());
		ddmStructureLayoutImpl.setModifiedDate(getModifiedDate());
		ddmStructureLayoutImpl.setStructureVersionId(getStructureVersionId());
		ddmStructureLayoutImpl.setName(getName());
		ddmStructureLayoutImpl.setDescription(getDescription());
		ddmStructureLayoutImpl.setDefinition(getDefinition());

		ddmStructureLayoutImpl.resetOriginalValues();

		return ddmStructureLayoutImpl;
	}

	@Override
	public int compareTo(DDMStructureLayout ddmStructureLayout) {
		long primaryKey = ddmStructureLayout.getPrimaryKey();

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

		if (!(obj instanceof DDMStructureLayout)) {
			return false;
		}

		DDMStructureLayout ddmStructureLayout = (DDMStructureLayout)obj;

		long primaryKey = ddmStructureLayout.getPrimaryKey();

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
		DDMStructureLayoutModelImpl ddmStructureLayoutModelImpl = this;

		ddmStructureLayoutModelImpl._originalUuid = ddmStructureLayoutModelImpl._uuid;

		ddmStructureLayoutModelImpl._originalGroupId = ddmStructureLayoutModelImpl._groupId;

		ddmStructureLayoutModelImpl._setOriginalGroupId = false;

		ddmStructureLayoutModelImpl._originalCompanyId = ddmStructureLayoutModelImpl._companyId;

		ddmStructureLayoutModelImpl._setOriginalCompanyId = false;

		ddmStructureLayoutModelImpl._setModifiedDate = false;

		ddmStructureLayoutModelImpl._originalStructureVersionId = ddmStructureLayoutModelImpl._structureVersionId;

		ddmStructureLayoutModelImpl._setOriginalStructureVersionId = false;

		setDDMFormLayout(null);

		ddmStructureLayoutModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<DDMStructureLayout> toCacheModel() {
		DDMStructureLayoutCacheModel ddmStructureLayoutCacheModel = new DDMStructureLayoutCacheModel();

		ddmStructureLayoutCacheModel.uuid = getUuid();

		String uuid = ddmStructureLayoutCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			ddmStructureLayoutCacheModel.uuid = null;
		}

		ddmStructureLayoutCacheModel.structureLayoutId = getStructureLayoutId();

		ddmStructureLayoutCacheModel.groupId = getGroupId();

		ddmStructureLayoutCacheModel.companyId = getCompanyId();

		ddmStructureLayoutCacheModel.userId = getUserId();

		ddmStructureLayoutCacheModel.userName = getUserName();

		String userName = ddmStructureLayoutCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			ddmStructureLayoutCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			ddmStructureLayoutCacheModel.createDate = createDate.getTime();
		}
		else {
			ddmStructureLayoutCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			ddmStructureLayoutCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			ddmStructureLayoutCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		ddmStructureLayoutCacheModel.structureVersionId = getStructureVersionId();

		ddmStructureLayoutCacheModel.name = getName();

		String name = ddmStructureLayoutCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			ddmStructureLayoutCacheModel.name = null;
		}

		ddmStructureLayoutCacheModel.description = getDescription();

		String description = ddmStructureLayoutCacheModel.description;

		if ((description != null) && (description.length() == 0)) {
			ddmStructureLayoutCacheModel.description = null;
		}

		ddmStructureLayoutCacheModel.definition = getDefinition();

		String definition = ddmStructureLayoutCacheModel.definition;

		if ((definition != null) && (definition.length() == 0)) {
			ddmStructureLayoutCacheModel.definition = null;
		}

		ddmStructureLayoutCacheModel._ddmFormLayout = getDDMFormLayout();

		return ddmStructureLayoutCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<DDMStructureLayout, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler((4 * attributeGetterFunctions.size()) +
				2);

		sb.append("{");

		for (Map.Entry<String, Function<DDMStructureLayout, Object>> entry : attributeGetterFunctions.entrySet()) {
			String attributeName = entry.getKey();
			Function<DDMStructureLayout, Object> attributeGetterFunction = entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((DDMStructureLayout)this));
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
		Map<String, Function<DDMStructureLayout, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler((5 * attributeGetterFunctions.size()) +
				4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<DDMStructureLayout, Object>> entry : attributeGetterFunctions.entrySet()) {
			String attributeName = entry.getKey();
			Function<DDMStructureLayout, Object> attributeGetterFunction = entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((DDMStructureLayout)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader = DDMStructureLayout.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
			DDMStructureLayout.class, ModelWrapper.class
		};
	private String _uuid;
	private String _originalUuid;
	private long _structureLayoutId;
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
	private long _structureVersionId;
	private long _originalStructureVersionId;
	private boolean _setOriginalStructureVersionId;
	private String _name;
	private String _nameCurrentLanguageId;
	private String _description;
	private String _descriptionCurrentLanguageId;
	private String _definition;
	private long _columnBitmask;
	private DDMStructureLayout _escapedModel;
}