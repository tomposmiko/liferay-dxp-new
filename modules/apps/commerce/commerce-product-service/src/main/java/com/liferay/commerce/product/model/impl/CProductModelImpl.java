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

package com.liferay.commerce.product.model.impl;

import com.liferay.commerce.product.model.CProduct;
import com.liferay.commerce.product.model.CProductModel;
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
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.Serializable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;

import java.sql.Blob;
import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the CProduct service. Represents a row in the &quot;CProduct&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>CProductModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link CProductImpl}.
 * </p>
 *
 * @author Marco Leo
 * @see CProductImpl
 * @generated
 */
public class CProductModelImpl
	extends BaseModelImpl<CProduct> implements CProductModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a c product model instance should use the <code>CProduct</code> interface instead.
	 */
	public static final String TABLE_NAME = "CProduct";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"ctCollectionId", Types.BIGINT},
		{"uuid_", Types.VARCHAR}, {"externalReferenceCode", Types.VARCHAR},
		{"CProductId", Types.BIGINT}, {"groupId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"userId", Types.BIGINT},
		{"userName", Types.VARCHAR}, {"createDate", Types.TIMESTAMP},
		{"modifiedDate", Types.TIMESTAMP},
		{"publishedCPDefinitionId", Types.BIGINT},
		{"latestVersion", Types.INTEGER}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("ctCollectionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("externalReferenceCode", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("CProductId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("publishedCPDefinitionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("latestVersion", Types.INTEGER);
	}

	public static final String TABLE_SQL_CREATE =
		"create table CProduct (mvccVersion LONG default 0 not null,ctCollectionId LONG default 0 not null,uuid_ VARCHAR(75) null,externalReferenceCode VARCHAR(75) null,CProductId LONG not null,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,publishedCPDefinitionId LONG,latestVersion INTEGER,primary key (CProductId, ctCollectionId))";

	public static final String TABLE_SQL_DROP = "drop table CProduct";

	public static final String ORDER_BY_JPQL =
		" ORDER BY cProduct.createDate DESC";

	public static final String ORDER_BY_SQL =
		" ORDER BY CProduct.createDate DESC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static final boolean ENTITY_CACHE_ENABLED = true;

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static final boolean FINDER_CACHE_ENABLED = true;

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static final boolean COLUMN_BITMASK_ENABLED = true;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long EXTERNALREFERENCECODE_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long GROUPID_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long UUID_COLUMN_BITMASK = 8L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long CREATEDATE_COLUMN_BITMASK = 16L;

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(
		com.liferay.commerce.product.service.util.ServiceProps.get(
			"lock.expiration.time.com.liferay.commerce.product.model.CProduct"));

	public CProductModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _CProductId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setCProductId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _CProductId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return CProduct.class;
	}

	@Override
	public String getModelClassName() {
		return CProduct.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<CProduct, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<CProduct, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<CProduct, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName, attributeGetterFunction.apply((CProduct)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<CProduct, Object>> attributeSetterBiConsumers =
			getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<CProduct, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(CProduct)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<CProduct, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<CProduct, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, CProduct>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			CProduct.class.getClassLoader(), CProduct.class,
			ModelWrapper.class);

		try {
			Constructor<CProduct> constructor =
				(Constructor<CProduct>)proxyClass.getConstructor(
					InvocationHandler.class);

			return invocationHandler -> {
				try {
					return constructor.newInstance(invocationHandler);
				}
				catch (ReflectiveOperationException
							reflectiveOperationException) {

					throw new InternalError(reflectiveOperationException);
				}
			};
		}
		catch (NoSuchMethodException noSuchMethodException) {
			throw new InternalError(noSuchMethodException);
		}
	}

	private static final Map<String, Function<CProduct, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<CProduct, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<CProduct, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<CProduct, Object>>();
		Map<String, BiConsumer<CProduct, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<CProduct, ?>>();

		attributeGetterFunctions.put("mvccVersion", CProduct::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<CProduct, Long>)CProduct::setMvccVersion);
		attributeGetterFunctions.put(
			"ctCollectionId", CProduct::getCtCollectionId);
		attributeSetterBiConsumers.put(
			"ctCollectionId",
			(BiConsumer<CProduct, Long>)CProduct::setCtCollectionId);
		attributeGetterFunctions.put("uuid", CProduct::getUuid);
		attributeSetterBiConsumers.put(
			"uuid", (BiConsumer<CProduct, String>)CProduct::setUuid);
		attributeGetterFunctions.put(
			"externalReferenceCode", CProduct::getExternalReferenceCode);
		attributeSetterBiConsumers.put(
			"externalReferenceCode",
			(BiConsumer<CProduct, String>)CProduct::setExternalReferenceCode);
		attributeGetterFunctions.put("CProductId", CProduct::getCProductId);
		attributeSetterBiConsumers.put(
			"CProductId", (BiConsumer<CProduct, Long>)CProduct::setCProductId);
		attributeGetterFunctions.put("groupId", CProduct::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId", (BiConsumer<CProduct, Long>)CProduct::setGroupId);
		attributeGetterFunctions.put("companyId", CProduct::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId", (BiConsumer<CProduct, Long>)CProduct::setCompanyId);
		attributeGetterFunctions.put("userId", CProduct::getUserId);
		attributeSetterBiConsumers.put(
			"userId", (BiConsumer<CProduct, Long>)CProduct::setUserId);
		attributeGetterFunctions.put("userName", CProduct::getUserName);
		attributeSetterBiConsumers.put(
			"userName", (BiConsumer<CProduct, String>)CProduct::setUserName);
		attributeGetterFunctions.put("createDate", CProduct::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate", (BiConsumer<CProduct, Date>)CProduct::setCreateDate);
		attributeGetterFunctions.put("modifiedDate", CProduct::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<CProduct, Date>)CProduct::setModifiedDate);
		attributeGetterFunctions.put(
			"publishedCPDefinitionId", CProduct::getPublishedCPDefinitionId);
		attributeSetterBiConsumers.put(
			"publishedCPDefinitionId",
			(BiConsumer<CProduct, Long>)CProduct::setPublishedCPDefinitionId);
		attributeGetterFunctions.put(
			"latestVersion", CProduct::getLatestVersion);
		attributeSetterBiConsumers.put(
			"latestVersion",
			(BiConsumer<CProduct, Integer>)CProduct::setLatestVersion);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

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

	@Override
	public long getCtCollectionId() {
		return _ctCollectionId;
	}

	@Override
	public void setCtCollectionId(long ctCollectionId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_ctCollectionId = ctCollectionId;
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

	@Override
	public String getExternalReferenceCode() {
		if (_externalReferenceCode == null) {
			return "";
		}
		else {
			return _externalReferenceCode;
		}
	}

	@Override
	public void setExternalReferenceCode(String externalReferenceCode) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_externalReferenceCode = externalReferenceCode;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalExternalReferenceCode() {
		return getColumnOriginalValue("externalReferenceCode");
	}

	@Override
	public long getCProductId() {
		return _CProductId;
	}

	@Override
	public void setCProductId(long CProductId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_CProductId = CProductId;
	}

	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_groupId = groupId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public long getOriginalGroupId() {
		return GetterUtil.getLong(this.<Long>getColumnOriginalValue("groupId"));
	}

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

	@Override
	public long getPublishedCPDefinitionId() {
		return _publishedCPDefinitionId;
	}

	@Override
	public void setPublishedCPDefinitionId(long publishedCPDefinitionId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_publishedCPDefinitionId = publishedCPDefinitionId;
	}

	@Override
	public int getLatestVersion() {
		return _latestVersion;
	}

	@Override
	public void setLatestVersion(int latestVersion) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_latestVersion = latestVersion;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(CProduct.class.getName()));
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
			getCompanyId(), CProduct.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public CProduct toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, CProduct>
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
		CProductImpl cProductImpl = new CProductImpl();

		cProductImpl.setMvccVersion(getMvccVersion());
		cProductImpl.setCtCollectionId(getCtCollectionId());
		cProductImpl.setUuid(getUuid());
		cProductImpl.setExternalReferenceCode(getExternalReferenceCode());
		cProductImpl.setCProductId(getCProductId());
		cProductImpl.setGroupId(getGroupId());
		cProductImpl.setCompanyId(getCompanyId());
		cProductImpl.setUserId(getUserId());
		cProductImpl.setUserName(getUserName());
		cProductImpl.setCreateDate(getCreateDate());
		cProductImpl.setModifiedDate(getModifiedDate());
		cProductImpl.setPublishedCPDefinitionId(getPublishedCPDefinitionId());
		cProductImpl.setLatestVersion(getLatestVersion());

		cProductImpl.resetOriginalValues();

		return cProductImpl;
	}

	@Override
	public CProduct cloneWithOriginalValues() {
		CProductImpl cProductImpl = new CProductImpl();

		cProductImpl.setMvccVersion(
			this.<Long>getColumnOriginalValue("mvccVersion"));
		cProductImpl.setCtCollectionId(
			this.<Long>getColumnOriginalValue("ctCollectionId"));
		cProductImpl.setUuid(this.<String>getColumnOriginalValue("uuid_"));
		cProductImpl.setExternalReferenceCode(
			this.<String>getColumnOriginalValue("externalReferenceCode"));
		cProductImpl.setCProductId(
			this.<Long>getColumnOriginalValue("CProductId"));
		cProductImpl.setGroupId(this.<Long>getColumnOriginalValue("groupId"));
		cProductImpl.setCompanyId(
			this.<Long>getColumnOriginalValue("companyId"));
		cProductImpl.setUserId(this.<Long>getColumnOriginalValue("userId"));
		cProductImpl.setUserName(
			this.<String>getColumnOriginalValue("userName"));
		cProductImpl.setCreateDate(
			this.<Date>getColumnOriginalValue("createDate"));
		cProductImpl.setModifiedDate(
			this.<Date>getColumnOriginalValue("modifiedDate"));
		cProductImpl.setPublishedCPDefinitionId(
			this.<Long>getColumnOriginalValue("publishedCPDefinitionId"));
		cProductImpl.setLatestVersion(
			this.<Integer>getColumnOriginalValue("latestVersion"));

		return cProductImpl;
	}

	@Override
	public int compareTo(CProduct cProduct) {
		int value = 0;

		value = DateUtil.compareTo(getCreateDate(), cProduct.getCreateDate());

		value = value * -1;

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof CProduct)) {
			return false;
		}

		CProduct cProduct = (CProduct)object;

		long primaryKey = cProduct.getPrimaryKey();

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
		return ENTITY_CACHE_ENABLED;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		_columnOriginalValues = Collections.emptyMap();

		_setModifiedDate = false;

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<CProduct> toCacheModel() {
		CProductCacheModel cProductCacheModel = new CProductCacheModel();

		cProductCacheModel.mvccVersion = getMvccVersion();

		cProductCacheModel.ctCollectionId = getCtCollectionId();

		cProductCacheModel.uuid = getUuid();

		String uuid = cProductCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			cProductCacheModel.uuid = null;
		}

		cProductCacheModel.externalReferenceCode = getExternalReferenceCode();

		String externalReferenceCode = cProductCacheModel.externalReferenceCode;

		if ((externalReferenceCode != null) &&
			(externalReferenceCode.length() == 0)) {

			cProductCacheModel.externalReferenceCode = null;
		}

		cProductCacheModel.CProductId = getCProductId();

		cProductCacheModel.groupId = getGroupId();

		cProductCacheModel.companyId = getCompanyId();

		cProductCacheModel.userId = getUserId();

		cProductCacheModel.userName = getUserName();

		String userName = cProductCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			cProductCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			cProductCacheModel.createDate = createDate.getTime();
		}
		else {
			cProductCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			cProductCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			cProductCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		cProductCacheModel.publishedCPDefinitionId =
			getPublishedCPDefinitionId();

		cProductCacheModel.latestVersion = getLatestVersion();

		return cProductCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<CProduct, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 2);

		sb.append("{");

		for (Map.Entry<String, Function<CProduct, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<CProduct, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("\"");
			sb.append(attributeName);
			sb.append("\": ");

			Object value = attributeGetterFunction.apply((CProduct)this);

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
		Map<String, Function<CProduct, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<CProduct, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<CProduct, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((CProduct)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, CProduct>
			_escapedModelProxyProviderFunction = _getProxyProviderFunction();

	}

	private long _mvccVersion;
	private long _ctCollectionId;
	private String _uuid;
	private String _externalReferenceCode;
	private long _CProductId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private long _publishedCPDefinitionId;
	private int _latestVersion;

	public <T> T getColumnValue(String columnName) {
		columnName = _attributeNames.getOrDefault(columnName, columnName);

		Function<CProduct, Object> function = _attributeGetterFunctions.get(
			columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((CProduct)this);
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
		_columnOriginalValues.put("ctCollectionId", _ctCollectionId);
		_columnOriginalValues.put("uuid_", _uuid);
		_columnOriginalValues.put(
			"externalReferenceCode", _externalReferenceCode);
		_columnOriginalValues.put("CProductId", _CProductId);
		_columnOriginalValues.put("groupId", _groupId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put(
			"publishedCPDefinitionId", _publishedCPDefinitionId);
		_columnOriginalValues.put("latestVersion", _latestVersion);
	}

	private static final Map<String, String> _attributeNames;

	static {
		Map<String, String> attributeNames = new HashMap<>();

		attributeNames.put("uuid_", "uuid");

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

		columnBitmasks.put("ctCollectionId", 2L);

		columnBitmasks.put("uuid_", 4L);

		columnBitmasks.put("externalReferenceCode", 8L);

		columnBitmasks.put("CProductId", 16L);

		columnBitmasks.put("groupId", 32L);

		columnBitmasks.put("companyId", 64L);

		columnBitmasks.put("userId", 128L);

		columnBitmasks.put("userName", 256L);

		columnBitmasks.put("createDate", 512L);

		columnBitmasks.put("modifiedDate", 1024L);

		columnBitmasks.put("publishedCPDefinitionId", 2048L);

		columnBitmasks.put("latestVersion", 4096L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private CProduct _escapedModel;

}