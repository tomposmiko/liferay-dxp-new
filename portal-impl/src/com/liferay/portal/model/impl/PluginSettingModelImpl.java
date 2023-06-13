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

package com.liferay.portal.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.PluginSetting;
import com.liferay.portal.kernel.model.PluginSettingModel;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
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
 * The base model implementation for the PluginSetting service. Represents a row in the &quot;PluginSetting&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>PluginSettingModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link PluginSettingImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see PluginSettingImpl
 * @generated
 */
@JSON(strict = true)
public class PluginSettingModelImpl
	extends BaseModelImpl<PluginSetting> implements PluginSettingModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a plugin setting model instance should use the <code>PluginSetting</code> interface instead.
	 */
	public static final String TABLE_NAME = "PluginSetting";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"pluginSettingId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"pluginId", Types.VARCHAR},
		{"pluginType", Types.VARCHAR}, {"roles", Types.VARCHAR},
		{"active_", Types.BOOLEAN}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("pluginSettingId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("pluginId", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("pluginType", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("roles", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("active_", Types.BOOLEAN);
	}

	public static final String TABLE_SQL_CREATE =
		"create table PluginSetting (mvccVersion LONG default 0 not null,pluginSettingId LONG not null primary key,companyId LONG,pluginId VARCHAR(75) null,pluginType VARCHAR(75) null,roles STRING null,active_ BOOLEAN)";

	public static final String TABLE_SQL_DROP = "drop table PluginSetting";

	public static final String ORDER_BY_JPQL =
		" ORDER BY pluginSetting.pluginSettingId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY PluginSetting.pluginSettingId ASC";

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
	public static final long PLUGINID_COLUMN_BITMASK = 2L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long PLUGINTYPE_COLUMN_BITMASK = 4L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *		#getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long PLUGINSETTINGID_COLUMN_BITMASK = 8L;

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(
		com.liferay.portal.util.PropsUtil.get(
			"lock.expiration.time.com.liferay.portal.kernel.model.PluginSetting"));

	public PluginSettingModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _pluginSettingId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setPluginSettingId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _pluginSettingId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return PluginSetting.class;
	}

	@Override
	public String getModelClassName() {
		return PluginSetting.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<PluginSetting, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<PluginSetting, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<PluginSetting, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((PluginSetting)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<PluginSetting, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<PluginSetting, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(PluginSetting)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<PluginSetting, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<PluginSetting, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, PluginSetting>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			PluginSetting.class.getClassLoader(), PluginSetting.class,
			ModelWrapper.class);

		try {
			Constructor<PluginSetting> constructor =
				(Constructor<PluginSetting>)proxyClass.getConstructor(
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

	private static final Map<String, Function<PluginSetting, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<PluginSetting, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<PluginSetting, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<PluginSetting, Object>>();
		Map<String, BiConsumer<PluginSetting, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<PluginSetting, ?>>();

		attributeGetterFunctions.put(
			"mvccVersion", PluginSetting::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<PluginSetting, Long>)PluginSetting::setMvccVersion);
		attributeGetterFunctions.put(
			"pluginSettingId", PluginSetting::getPluginSettingId);
		attributeSetterBiConsumers.put(
			"pluginSettingId",
			(BiConsumer<PluginSetting, Long>)PluginSetting::setPluginSettingId);
		attributeGetterFunctions.put("companyId", PluginSetting::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<PluginSetting, Long>)PluginSetting::setCompanyId);
		attributeGetterFunctions.put("pluginId", PluginSetting::getPluginId);
		attributeSetterBiConsumers.put(
			"pluginId",
			(BiConsumer<PluginSetting, String>)PluginSetting::setPluginId);
		attributeGetterFunctions.put(
			"pluginType", PluginSetting::getPluginType);
		attributeSetterBiConsumers.put(
			"pluginType",
			(BiConsumer<PluginSetting, String>)PluginSetting::setPluginType);
		attributeGetterFunctions.put("roles", PluginSetting::getRoles);
		attributeSetterBiConsumers.put(
			"roles",
			(BiConsumer<PluginSetting, String>)PluginSetting::setRoles);
		attributeGetterFunctions.put("active", PluginSetting::getActive);
		attributeSetterBiConsumers.put(
			"active",
			(BiConsumer<PluginSetting, Boolean>)PluginSetting::setActive);

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
	public long getPluginSettingId() {
		return _pluginSettingId;
	}

	@Override
	public void setPluginSettingId(long pluginSettingId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_pluginSettingId = pluginSettingId;
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
	public String getPluginId() {
		if (_pluginId == null) {
			return "";
		}
		else {
			return _pluginId;
		}
	}

	@Override
	public void setPluginId(String pluginId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_pluginId = pluginId;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalPluginId() {
		return getColumnOriginalValue("pluginId");
	}

	@JSON
	@Override
	public String getPluginType() {
		if (_pluginType == null) {
			return "";
		}
		else {
			return _pluginType;
		}
	}

	@Override
	public void setPluginType(String pluginType) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_pluginType = pluginType;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalPluginType() {
		return getColumnOriginalValue("pluginType");
	}

	@JSON
	@Override
	public String getRoles() {
		if (_roles == null) {
			return "";
		}
		else {
			return _roles;
		}
	}

	@Override
	public void setRoles(String roles) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_roles = roles;
	}

	@JSON
	@Override
	public boolean getActive() {
		return _active;
	}

	@JSON
	@Override
	public boolean isActive() {
		return _active;
	}

	@Override
	public void setActive(boolean active) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_active = active;
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
			getCompanyId(), PluginSetting.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public PluginSetting toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, PluginSetting>
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
		PluginSettingImpl pluginSettingImpl = new PluginSettingImpl();

		pluginSettingImpl.setMvccVersion(getMvccVersion());
		pluginSettingImpl.setPluginSettingId(getPluginSettingId());
		pluginSettingImpl.setCompanyId(getCompanyId());
		pluginSettingImpl.setPluginId(getPluginId());
		pluginSettingImpl.setPluginType(getPluginType());
		pluginSettingImpl.setRoles(getRoles());
		pluginSettingImpl.setActive(isActive());

		pluginSettingImpl.resetOriginalValues();

		return pluginSettingImpl;
	}

	@Override
	public PluginSetting cloneWithOriginalValues() {
		PluginSettingImpl pluginSettingImpl = new PluginSettingImpl();

		pluginSettingImpl.setMvccVersion(
			this.<Long>getColumnOriginalValue("mvccVersion"));
		pluginSettingImpl.setPluginSettingId(
			this.<Long>getColumnOriginalValue("pluginSettingId"));
		pluginSettingImpl.setCompanyId(
			this.<Long>getColumnOriginalValue("companyId"));
		pluginSettingImpl.setPluginId(
			this.<String>getColumnOriginalValue("pluginId"));
		pluginSettingImpl.setPluginType(
			this.<String>getColumnOriginalValue("pluginType"));
		pluginSettingImpl.setRoles(
			this.<String>getColumnOriginalValue("roles"));
		pluginSettingImpl.setActive(
			this.<Boolean>getColumnOriginalValue("active_"));

		return pluginSettingImpl;
	}

	@Override
	public int compareTo(PluginSetting pluginSetting) {
		long primaryKey = pluginSetting.getPrimaryKey();

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

		if (!(object instanceof PluginSetting)) {
			return false;
		}

		PluginSetting pluginSetting = (PluginSetting)object;

		long primaryKey = pluginSetting.getPrimaryKey();

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

		_columnBitmask = 0;
	}

	@Override
	public CacheModel<PluginSetting> toCacheModel() {
		PluginSettingCacheModel pluginSettingCacheModel =
			new PluginSettingCacheModel();

		pluginSettingCacheModel.mvccVersion = getMvccVersion();

		pluginSettingCacheModel.pluginSettingId = getPluginSettingId();

		pluginSettingCacheModel.companyId = getCompanyId();

		pluginSettingCacheModel.pluginId = getPluginId();

		String pluginId = pluginSettingCacheModel.pluginId;

		if ((pluginId != null) && (pluginId.length() == 0)) {
			pluginSettingCacheModel.pluginId = null;
		}

		pluginSettingCacheModel.pluginType = getPluginType();

		String pluginType = pluginSettingCacheModel.pluginType;

		if ((pluginType != null) && (pluginType.length() == 0)) {
			pluginSettingCacheModel.pluginType = null;
		}

		pluginSettingCacheModel.roles = getRoles();

		String roles = pluginSettingCacheModel.roles;

		if ((roles != null) && (roles.length() == 0)) {
			pluginSettingCacheModel.roles = null;
		}

		pluginSettingCacheModel.active = isActive();

		return pluginSettingCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<PluginSetting, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 2);

		sb.append("{");

		for (Map.Entry<String, Function<PluginSetting, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<PluginSetting, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("\"");
			sb.append(attributeName);
			sb.append("\": ");

			Object value = attributeGetterFunction.apply((PluginSetting)this);

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
		Map<String, Function<PluginSetting, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<PluginSetting, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<PluginSetting, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((PluginSetting)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, PluginSetting>
			_escapedModelProxyProviderFunction = _getProxyProviderFunction();

	}

	private long _mvccVersion;
	private long _pluginSettingId;
	private long _companyId;
	private String _pluginId;
	private String _pluginType;
	private String _roles;
	private boolean _active;

	public <T> T getColumnValue(String columnName) {
		columnName = _attributeNames.getOrDefault(columnName, columnName);

		Function<PluginSetting, Object> function =
			_attributeGetterFunctions.get(columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((PluginSetting)this);
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
		_columnOriginalValues.put("pluginSettingId", _pluginSettingId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("pluginId", _pluginId);
		_columnOriginalValues.put("pluginType", _pluginType);
		_columnOriginalValues.put("roles", _roles);
		_columnOriginalValues.put("active_", _active);
	}

	private static final Map<String, String> _attributeNames;

	static {
		Map<String, String> attributeNames = new HashMap<>();

		attributeNames.put("active_", "active");

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

		columnBitmasks.put("pluginSettingId", 2L);

		columnBitmasks.put("companyId", 4L);

		columnBitmasks.put("pluginId", 8L);

		columnBitmasks.put("pluginType", 16L);

		columnBitmasks.put("roles", 32L);

		columnBitmasks.put("active_", 64L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private PluginSetting _escapedModel;

}