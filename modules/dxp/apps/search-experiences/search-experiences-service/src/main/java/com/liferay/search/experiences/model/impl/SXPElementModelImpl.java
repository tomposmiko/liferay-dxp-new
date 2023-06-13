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

package com.liferay.search.experiences.model.impl;

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
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.search.experiences.model.SXPElement;
import com.liferay.search.experiences.model.SXPElementModel;
import com.liferay.search.experiences.model.SXPElementSoap;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.sql.Blob;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the SXPElement service. Represents a row in the &quot;SXPElement&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>SXPElementModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link SXPElementImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SXPElementImpl
 * @generated
 */
@JSON(strict = true)
public class SXPElementModelImpl
	extends BaseModelImpl<SXPElement> implements SXPElementModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a sxp element model instance should use the <code>SXPElement</code> interface instead.
	 */
	public static final String TABLE_NAME = "SXPElement";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"uuid_", Types.VARCHAR},
		{"sxpElementId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"description", Types.VARCHAR}, {"elementDefinitionJSON", Types.CLOB},
		{"hidden_", Types.BOOLEAN}, {"readOnly", Types.BOOLEAN},
		{"schemaVersion", Types.VARCHAR}, {"title", Types.VARCHAR},
		{"type_", Types.INTEGER}, {"status", Types.INTEGER}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("sxpElementId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("description", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("elementDefinitionJSON", Types.CLOB);
		TABLE_COLUMNS_MAP.put("hidden_", Types.BOOLEAN);
		TABLE_COLUMNS_MAP.put("readOnly", Types.BOOLEAN);
		TABLE_COLUMNS_MAP.put("schemaVersion", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("title", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("type_", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("status", Types.INTEGER);
	}

	public static final String TABLE_SQL_CREATE =
		"create table SXPElement (mvccVersion LONG default 0 not null,uuid_ VARCHAR(75) null,sxpElementId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,description STRING null,elementDefinitionJSON TEXT null,hidden_ BOOLEAN,readOnly BOOLEAN,schemaVersion VARCHAR(75) null,title STRING null,type_ INTEGER,status INTEGER)";

	public static final String TABLE_SQL_DROP = "drop table SXPElement";

	public static final String ORDER_BY_JPQL =
		" ORDER BY sxpElement.sxpElementId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY SXPElement.sxpElementId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long READONLY_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long STATUS_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long TYPE_COLUMN_BITMASK = 8L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long UUID_COLUMN_BITMASK = 16L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long SXPELEMENTID_COLUMN_BITMASK = 32L;

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setEntityCacheEnabled(boolean entityCacheEnabled) {
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static void setFinderCacheEnabled(boolean finderCacheEnabled) {
	}

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static SXPElement toModel(SXPElementSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		SXPElement model = new SXPElementImpl();

		model.setMvccVersion(soapModel.getMvccVersion());
		model.setUuid(soapModel.getUuid());
		model.setSXPElementId(soapModel.getSXPElementId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setDescription(soapModel.getDescription());
		model.setElementDefinitionJSON(soapModel.getElementDefinitionJSON());
		model.setHidden(soapModel.isHidden());
		model.setReadOnly(soapModel.isReadOnly());
		model.setSchemaVersion(soapModel.getSchemaVersion());
		model.setTitle(soapModel.getTitle());
		model.setType(soapModel.getType());
		model.setStatus(soapModel.getStatus());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static List<SXPElement> toModels(SXPElementSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<SXPElement> models = new ArrayList<SXPElement>(soapModels.length);

		for (SXPElementSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public SXPElementModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _sxpElementId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setSXPElementId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _sxpElementId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return SXPElement.class;
	}

	@Override
	public String getModelClassName() {
		return SXPElement.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<SXPElement, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<SXPElement, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SXPElement, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName, attributeGetterFunction.apply((SXPElement)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<SXPElement, Object>> attributeSetterBiConsumers =
			getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<SXPElement, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(SXPElement)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<SXPElement, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<SXPElement, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<SXPElement, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<SXPElement, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<SXPElement, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<SXPElement, Object>>();
		Map<String, BiConsumer<SXPElement, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<SXPElement, ?>>();

		attributeGetterFunctions.put("mvccVersion", SXPElement::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<SXPElement, Long>)SXPElement::setMvccVersion);
		attributeGetterFunctions.put("uuid", SXPElement::getUuid);
		attributeSetterBiConsumers.put(
			"uuid", (BiConsumer<SXPElement, String>)SXPElement::setUuid);
		attributeGetterFunctions.put(
			"sxpElementId", SXPElement::getSXPElementId);
		attributeSetterBiConsumers.put(
			"sxpElementId",
			(BiConsumer<SXPElement, Long>)SXPElement::setSXPElementId);
		attributeGetterFunctions.put("companyId", SXPElement::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<SXPElement, Long>)SXPElement::setCompanyId);
		attributeGetterFunctions.put("userId", SXPElement::getUserId);
		attributeSetterBiConsumers.put(
			"userId", (BiConsumer<SXPElement, Long>)SXPElement::setUserId);
		attributeGetterFunctions.put("userName", SXPElement::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<SXPElement, String>)SXPElement::setUserName);
		attributeGetterFunctions.put("createDate", SXPElement::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<SXPElement, Date>)SXPElement::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", SXPElement::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<SXPElement, Date>)SXPElement::setModifiedDate);
		attributeGetterFunctions.put("description", SXPElement::getDescription);
		attributeSetterBiConsumers.put(
			"description",
			(BiConsumer<SXPElement, String>)SXPElement::setDescription);
		attributeGetterFunctions.put(
			"elementDefinitionJSON", SXPElement::getElementDefinitionJSON);
		attributeSetterBiConsumers.put(
			"elementDefinitionJSON",
			(BiConsumer<SXPElement, String>)
				SXPElement::setElementDefinitionJSON);
		attributeGetterFunctions.put("hidden", SXPElement::getHidden);
		attributeSetterBiConsumers.put(
			"hidden", (BiConsumer<SXPElement, Boolean>)SXPElement::setHidden);
		attributeGetterFunctions.put("readOnly", SXPElement::getReadOnly);
		attributeSetterBiConsumers.put(
			"readOnly",
			(BiConsumer<SXPElement, Boolean>)SXPElement::setReadOnly);
		attributeGetterFunctions.put(
			"schemaVersion", SXPElement::getSchemaVersion);
		attributeSetterBiConsumers.put(
			"schemaVersion",
			(BiConsumer<SXPElement, String>)SXPElement::setSchemaVersion);
		attributeGetterFunctions.put("title", SXPElement::getTitle);
		attributeSetterBiConsumers.put(
			"title", (BiConsumer<SXPElement, String>)SXPElement::setTitle);
		attributeGetterFunctions.put("type", SXPElement::getType);
		attributeSetterBiConsumers.put(
			"type", (BiConsumer<SXPElement, Integer>)SXPElement::setType);
		attributeGetterFunctions.put("status", SXPElement::getStatus);
		attributeSetterBiConsumers.put(
			"status", (BiConsumer<SXPElement, Integer>)SXPElement::setStatus);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@JSON
	@Override
	public long getMvccVersion() {
		return _mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_mvccVersion = mvccVersion;
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
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_uuid = uuid;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalUuid() {
		return getColumnOriginalValue("uuid_");
	}

	@JSON
	@Override
	public long getSXPElementId() {
		return _sxpElementId;
	}

	@Override
	public void setSXPElementId(long sxpElementId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_sxpElementId = sxpElementId;
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_companyId = companyId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalCompanyId() {
		return GetterUtil.getLong(
			this.<Long>getColumnOriginalValue("companyId"));
	}

	@JSON
	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException portalException) {
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
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_userName = userName;
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

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

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_modifiedDate = modifiedDate;
	}

	@JSON
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
		return LocalizationUtil.getLocalization(
			getDescription(), languageId, useDefault);
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
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_description = description;
	}

	@Override
	public void setDescription(String description, Locale locale) {
		setDescription(description, locale, LocaleUtil.getDefault());
	}

	@Override
	public void setDescription(
		String description, Locale locale, Locale defaultLocale) {

		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(description)) {
			setDescription(
				LocalizationUtil.updateLocalization(
					getDescription(), "Description", description, languageId,
					defaultLanguageId));
		}
		else {
			setDescription(
				LocalizationUtil.removeLocalization(
					getDescription(), "Description", languageId));
		}
	}

	@Override
	public void setDescriptionCurrentLanguageId(String languageId) {
		_descriptionCurrentLanguageId = languageId;
	}

	@Override
	public void setDescriptionMap(Map<Locale, String> descriptionMap) {
		setDescriptionMap(descriptionMap, LocaleUtil.getDefault());
	}

	@Override
	public void setDescriptionMap(
		Map<Locale, String> descriptionMap, Locale defaultLocale) {

		if (descriptionMap == null) {
			return;
		}

		setDescription(
			LocalizationUtil.updateLocalization(
				descriptionMap, getDescription(), "Description",
				LocaleUtil.toLanguageId(defaultLocale)));
	}

	@JSON
	@Override
	public String getElementDefinitionJSON() {
		if (_elementDefinitionJSON == null) {
			return "";
		}
		else {
			return _elementDefinitionJSON;
		}
	}

	@Override
	public void setElementDefinitionJSON(String elementDefinitionJSON) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_elementDefinitionJSON = elementDefinitionJSON;
	}

	@JSON
	@Override
	public boolean getHidden() {
		return _hidden;
	}

	@JSON
	@Override
	public boolean isHidden() {
		return _hidden;
	}

	@Override
	public void setHidden(boolean hidden) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_hidden = hidden;
	}

	@JSON
	@Override
	public boolean getReadOnly() {
		return _readOnly;
	}

	@JSON
	@Override
	public boolean isReadOnly() {
		return _readOnly;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_readOnly = readOnly;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public boolean getOriginalReadOnly() {
		return GetterUtil.getBoolean(
			this.<Boolean>getColumnOriginalValue("readOnly"));
	}

	@JSON
	@Override
	public String getSchemaVersion() {
		if (_schemaVersion == null) {
			return "";
		}
		else {
			return _schemaVersion;
		}
	}

	@Override
	public void setSchemaVersion(String schemaVersion) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_schemaVersion = schemaVersion;
	}

	@JSON
	@Override
	public String getTitle() {
		if (_title == null) {
			return "";
		}
		else {
			return _title;
		}
	}

	@Override
	public String getTitle(Locale locale) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getTitle(languageId);
	}

	@Override
	public String getTitle(Locale locale, boolean useDefault) {
		String languageId = LocaleUtil.toLanguageId(locale);

		return getTitle(languageId, useDefault);
	}

	@Override
	public String getTitle(String languageId) {
		return LocalizationUtil.getLocalization(getTitle(), languageId);
	}

	@Override
	public String getTitle(String languageId, boolean useDefault) {
		return LocalizationUtil.getLocalization(
			getTitle(), languageId, useDefault);
	}

	@Override
	public String getTitleCurrentLanguageId() {
		return _titleCurrentLanguageId;
	}

	@JSON
	@Override
	public String getTitleCurrentValue() {
		Locale locale = getLocale(_titleCurrentLanguageId);

		return getTitle(locale);
	}

	@Override
	public Map<Locale, String> getTitleMap() {
		return LocalizationUtil.getLocalizationMap(getTitle());
	}

	@Override
	public void setTitle(String title) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_title = title;
	}

	@Override
	public void setTitle(String title, Locale locale) {
		setTitle(title, locale, LocaleUtil.getDefault());
	}

	@Override
	public void setTitle(String title, Locale locale, Locale defaultLocale) {
		String languageId = LocaleUtil.toLanguageId(locale);
		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		if (Validator.isNotNull(title)) {
			setTitle(
				LocalizationUtil.updateLocalization(
					getTitle(), "Title", title, languageId, defaultLanguageId));
		}
		else {
			setTitle(
				LocalizationUtil.removeLocalization(
					getTitle(), "Title", languageId));
		}
	}

	@Override
	public void setTitleCurrentLanguageId(String languageId) {
		_titleCurrentLanguageId = languageId;
	}

	@Override
	public void setTitleMap(Map<Locale, String> titleMap) {
		setTitleMap(titleMap, LocaleUtil.getDefault());
	}

	@Override
	public void setTitleMap(
		Map<Locale, String> titleMap, Locale defaultLocale) {

		if (titleMap == null) {
			return;
		}

		setTitle(
			LocalizationUtil.updateLocalization(
				titleMap, getTitle(), "Title",
				LocaleUtil.toLanguageId(defaultLocale)));
	}

	@JSON
	@Override
	public int getType() {
		return _type;
	}

	@Override
	public void setType(int type) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_type = type;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public int getOriginalType() {
		return GetterUtil.getInteger(
			this.<Integer>getColumnOriginalValue("type_"));
	}

	@JSON
	@Override
	public int getStatus() {
		return _status;
	}

	@Override
	public void setStatus(int status) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_status = status;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public int getOriginalStatus() {
		return GetterUtil.getInteger(
			this.<Integer>getColumnOriginalValue("status"));
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(SXPElement.class.getName()));
	}

	public long getColumnBitmask() {
		if (_columnBitmask > 0) {
			return _columnBitmask;
		}

		if ((_columnOriginalValues == null) ||
			(_columnOriginalValues == Collections.EMPTY_MAP)) {

			return 0;
		}

		for (Map.Entry<String, Object> entry :
				_columnOriginalValues.entrySet()) {

			if (!Objects.equals(
					entry.getValue(), getColumnValue(entry.getKey()))) {

				_columnBitmask |= _columnBitmasks.get(entry.getKey());
			}
		}

		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), SXPElement.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public String[] getAvailableLanguageIds() {
		Set<String> availableLanguageIds = new TreeSet<String>();

		Map<Locale, String> descriptionMap = getDescriptionMap();

		for (Map.Entry<Locale, String> entry : descriptionMap.entrySet()) {
			Locale locale = entry.getKey();
			String value = entry.getValue();

			if (Validator.isNotNull(value)) {
				availableLanguageIds.add(LocaleUtil.toLanguageId(locale));
			}
		}

		Map<Locale, String> titleMap = getTitleMap();

		for (Map.Entry<Locale, String> entry : titleMap.entrySet()) {
			Locale locale = entry.getKey();
			String value = entry.getValue();

			if (Validator.isNotNull(value)) {
				availableLanguageIds.add(LocaleUtil.toLanguageId(locale));
			}
		}

		return availableLanguageIds.toArray(
			new String[availableLanguageIds.size()]);
	}

	@Override
	public String getDefaultLanguageId() {
		String xml = getDescription();

		if (xml == null) {
			return "";
		}

		Locale defaultLocale = LocaleUtil.getDefault();

		return LocalizationUtil.getDefaultLanguageId(xml, defaultLocale);
	}

	@Override
	public void prepareLocalizedFieldsForImport() throws LocaleException {
		Locale defaultLocale = LocaleUtil.fromLanguageId(
			getDefaultLanguageId());

		Locale[] availableLocales = LocaleUtil.fromLanguageIds(
			getAvailableLanguageIds());

		Locale defaultImportLocale = LocalizationUtil.getDefaultImportLocale(
			SXPElement.class.getName(), getPrimaryKey(), defaultLocale,
			availableLocales);

		prepareLocalizedFieldsForImport(defaultImportLocale);
	}

	@Override
	@SuppressWarnings("unused")
	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale)
		throws LocaleException {

		Locale defaultLocale = LocaleUtil.getDefault();

		String modelDefaultLanguageId = getDefaultLanguageId();

		String description = getDescription(defaultLocale);

		if (Validator.isNull(description)) {
			setDescription(
				getDescription(modelDefaultLanguageId), defaultLocale);
		}
		else {
			setDescription(
				getDescription(defaultLocale), defaultLocale, defaultLocale);
		}

		String title = getTitle(defaultLocale);

		if (Validator.isNull(title)) {
			setTitle(getTitle(modelDefaultLanguageId), defaultLocale);
		}
		else {
			setTitle(getTitle(defaultLocale), defaultLocale, defaultLocale);
		}
	}

	@Override
	public SXPElement toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, SXPElement>
				escapedModelProxyProviderFunction =
					EscapedModelProxyProviderFunctionHolder.
						_escapedModelProxyProviderFunction;

			_escapedModel = escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		SXPElementImpl sxpElementImpl = new SXPElementImpl();

		sxpElementImpl.setMvccVersion(getMvccVersion());
		sxpElementImpl.setUuid(getUuid());
		sxpElementImpl.setSXPElementId(getSXPElementId());
		sxpElementImpl.setCompanyId(getCompanyId());
		sxpElementImpl.setUserId(getUserId());
		sxpElementImpl.setUserName(getUserName());
		sxpElementImpl.setCreateDate(getCreateDate());
		sxpElementImpl.setModifiedDate(getModifiedDate());
		sxpElementImpl.setDescription(getDescription());
		sxpElementImpl.setElementDefinitionJSON(getElementDefinitionJSON());
		sxpElementImpl.setHidden(isHidden());
		sxpElementImpl.setReadOnly(isReadOnly());
		sxpElementImpl.setSchemaVersion(getSchemaVersion());
		sxpElementImpl.setTitle(getTitle());
		sxpElementImpl.setType(getType());
		sxpElementImpl.setStatus(getStatus());

		sxpElementImpl.resetOriginalValues();

		return sxpElementImpl;
	}

	@Override
	public int compareTo(SXPElement sxpElement) {
		long primaryKey = sxpElement.getPrimaryKey();

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
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof SXPElement)) {
			return false;
		}

		SXPElement sxpElement = (SXPElement)object;

		long primaryKey = sxpElement.getPrimaryKey();

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

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isEntityCacheEnabled() {
		return true;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isFinderCacheEnabled() {
		return true;
	}

	@Override
	public void resetOriginalValues() {
		_columnOriginalValues = Collections.emptyMap();

		_setModifiedDate = false;

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<SXPElement> toCacheModel() {
		SXPElementCacheModel sxpElementCacheModel = new SXPElementCacheModel();

		sxpElementCacheModel.mvccVersion = getMvccVersion();

		sxpElementCacheModel.uuid = getUuid();

		String uuid = sxpElementCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			sxpElementCacheModel.uuid = null;
		}

		sxpElementCacheModel.sxpElementId = getSXPElementId();

		sxpElementCacheModel.companyId = getCompanyId();

		sxpElementCacheModel.userId = getUserId();

		sxpElementCacheModel.userName = getUserName();

		String userName = sxpElementCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			sxpElementCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			sxpElementCacheModel.createDate = createDate.getTime();
		}
		else {
			sxpElementCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			sxpElementCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			sxpElementCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		sxpElementCacheModel.description = getDescription();

		String description = sxpElementCacheModel.description;

		if ((description != null) && (description.length() == 0)) {
			sxpElementCacheModel.description = null;
		}

		sxpElementCacheModel.elementDefinitionJSON = getElementDefinitionJSON();

		String elementDefinitionJSON =
			sxpElementCacheModel.elementDefinitionJSON;

		if ((elementDefinitionJSON != null) &&
			(elementDefinitionJSON.length() == 0)) {

			sxpElementCacheModel.elementDefinitionJSON = null;
		}

		sxpElementCacheModel.hidden = isHidden();

		sxpElementCacheModel.readOnly = isReadOnly();

		sxpElementCacheModel.schemaVersion = getSchemaVersion();

		String schemaVersion = sxpElementCacheModel.schemaVersion;

		if ((schemaVersion != null) && (schemaVersion.length() == 0)) {
			sxpElementCacheModel.schemaVersion = null;
		}

		sxpElementCacheModel.title = getTitle();

		String title = sxpElementCacheModel.title;

		if ((title != null) && (title.length() == 0)) {
			sxpElementCacheModel.title = null;
		}

		sxpElementCacheModel.type = getType();

		sxpElementCacheModel.status = getStatus();

		return sxpElementCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<SXPElement, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 2);

		sb.append("{");

		for (Map.Entry<String, Function<SXPElement, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SXPElement, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("\"");
			sb.append(attributeName);
			sb.append("\": ");

			Object value = attributeGetterFunction.apply((SXPElement)this);

			if (value == null) {
				sb.append("null");
			}
			else if (value instanceof Blob || value instanceof Date ||
					 value instanceof Map || value instanceof String) {

				sb.append(
					"\"" + StringUtil.replace(value.toString(), "\"", "'") +
						"\"");
			}
			else {
				sb.append(value);
			}

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
		Map<String, Function<SXPElement, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<SXPElement, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SXPElement, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((SXPElement)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, SXPElement>
			_escapedModelProxyProviderFunction =
				ProxyUtil.getProxyProviderFunction(
					SXPElement.class, ModelWrapper.class);

	}

	private long _mvccVersion;
	private String _uuid;
	private long _sxpElementId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _description;
	private String _descriptionCurrentLanguageId;
	private String _elementDefinitionJSON;
	private boolean _hidden;
	private boolean _readOnly;
	private String _schemaVersion;
	private String _title;
	private String _titleCurrentLanguageId;
	private int _type;
	private int _status;

	public <T> T getColumnValue(String columnName) {
		columnName = _attributeNames.getOrDefault(columnName, columnName);

		Function<SXPElement, Object> function = _attributeGetterFunctions.get(
			columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((SXPElement)this);
	}

	public <T> T getColumnOriginalValue(String columnName) {
		if (_columnOriginalValues == null) {
			return null;
		}

		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		return (T)_columnOriginalValues.get(columnName);
	}

	private void _setColumnOriginalValues() {
		_columnOriginalValues = new HashMap<String, Object>();

		_columnOriginalValues.put("mvccVersion", _mvccVersion);
		_columnOriginalValues.put("uuid_", _uuid);
		_columnOriginalValues.put("sxpElementId", _sxpElementId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put("description", _description);
		_columnOriginalValues.put(
			"elementDefinitionJSON", _elementDefinitionJSON);
		_columnOriginalValues.put("hidden_", _hidden);
		_columnOriginalValues.put("readOnly", _readOnly);
		_columnOriginalValues.put("schemaVersion", _schemaVersion);
		_columnOriginalValues.put("title", _title);
		_columnOriginalValues.put("type_", _type);
		_columnOriginalValues.put("status", _status);
	}

	private static final Map<String, String> _attributeNames;

	static {
		Map<String, String> attributeNames = new HashMap<>();

		attributeNames.put("uuid_", "uuid");
		attributeNames.put("hidden_", "hidden");
		attributeNames.put("type_", "type");

		_attributeNames = Collections.unmodifiableMap(attributeNames);
	}

	private transient Map<String, Object> _columnOriginalValues;

	public static long getColumnBitmask(String columnName) {
		return _columnBitmasks.get(columnName);
	}

	private static final Map<String, Long> _columnBitmasks;

	static {
		Map<String, Long> columnBitmasks = new HashMap<>();

		columnBitmasks.put("mvccVersion", 1L);

		columnBitmasks.put("uuid_", 2L);

		columnBitmasks.put("sxpElementId", 4L);

		columnBitmasks.put("companyId", 8L);

		columnBitmasks.put("userId", 16L);

		columnBitmasks.put("userName", 32L);

		columnBitmasks.put("createDate", 64L);

		columnBitmasks.put("modifiedDate", 128L);

		columnBitmasks.put("description", 256L);

		columnBitmasks.put("elementDefinitionJSON", 512L);

		columnBitmasks.put("hidden_", 1024L);

		columnBitmasks.put("readOnly", 2048L);

		columnBitmasks.put("schemaVersion", 4096L);

		columnBitmasks.put("title", 8192L);

		columnBitmasks.put("type_", 16384L);

		columnBitmasks.put("status", 32768L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private SXPElement _escapedModel;

}