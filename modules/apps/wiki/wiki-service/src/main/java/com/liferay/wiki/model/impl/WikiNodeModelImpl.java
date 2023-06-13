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

package com.liferay.wiki.model.impl;

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
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.wiki.model.WikiNode;
import com.liferay.wiki.model.WikiNodeModel;

import java.io.Serializable;

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
 * The base model implementation for the WikiNode service. Represents a row in the &quot;WikiNode&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface <code>WikiNodeModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link WikiNodeImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see WikiNodeImpl
 * @generated
 */
@JSON(strict = true)
public class WikiNodeModelImpl
	extends BaseModelImpl<WikiNode> implements WikiNodeModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a wiki node model instance should use the <code>WikiNode</code> interface instead.
	 */
	public static final String TABLE_NAME = "WikiNode";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"ctCollectionId", Types.BIGINT},
		{"uuid_", Types.VARCHAR}, {"externalReferenceCode", Types.VARCHAR},
		{"nodeId", Types.BIGINT}, {"groupId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"userId", Types.BIGINT},
		{"userName", Types.VARCHAR}, {"createDate", Types.TIMESTAMP},
		{"modifiedDate", Types.TIMESTAMP}, {"name", Types.VARCHAR},
		{"description", Types.VARCHAR}, {"lastPostDate", Types.TIMESTAMP},
		{"lastPublishDate", Types.TIMESTAMP}, {"status", Types.INTEGER},
		{"statusByUserId", Types.BIGINT}, {"statusByUserName", Types.VARCHAR},
		{"statusDate", Types.TIMESTAMP}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("ctCollectionId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("externalReferenceCode", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("nodeId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("name", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("description", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("lastPostDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("lastPublishDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("status", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("statusByUserId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("statusByUserName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("statusDate", Types.TIMESTAMP);
	}

	public static final String TABLE_SQL_CREATE =
		"create table WikiNode (mvccVersion LONG default 0 not null,ctCollectionId LONG default 0 not null,uuid_ VARCHAR(75) null,externalReferenceCode VARCHAR(75) null,nodeId LONG not null,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,name VARCHAR(75) null,description STRING null,lastPostDate DATE null,lastPublishDate DATE null,status INTEGER,statusByUserId LONG,statusByUserName VARCHAR(75) null,statusDate DATE null,primary key (nodeId, ctCollectionId))";

	public static final String TABLE_SQL_DROP = "drop table WikiNode";

	public static final String ORDER_BY_JPQL = " ORDER BY wikiNode.name ASC";

	public static final String ORDER_BY_SQL = " ORDER BY WikiNode.name ASC";

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
	public static final long NAME_COLUMN_BITMASK = 8L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long STATUS_COLUMN_BITMASK = 16L;

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #getColumnBitmask(String)}
	 */
	@Deprecated
	public static final long UUID_COLUMN_BITMASK = 32L;

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

	public WikiNodeModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _nodeId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setNodeId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _nodeId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return WikiNode.class;
	}

	@Override
	public String getModelClassName() {
		return WikiNode.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<WikiNode, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<WikiNode, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<WikiNode, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName, attributeGetterFunction.apply((WikiNode)this));
		}

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<WikiNode, Object>> attributeSetterBiConsumers =
			getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<WikiNode, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(WikiNode)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<WikiNode, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<WikiNode, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<WikiNode, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<WikiNode, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<WikiNode, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<WikiNode, Object>>();
		Map<String, BiConsumer<WikiNode, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<WikiNode, ?>>();

		attributeGetterFunctions.put("mvccVersion", WikiNode::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<WikiNode, Long>)WikiNode::setMvccVersion);
		attributeGetterFunctions.put(
			"ctCollectionId", WikiNode::getCtCollectionId);
		attributeSetterBiConsumers.put(
			"ctCollectionId",
			(BiConsumer<WikiNode, Long>)WikiNode::setCtCollectionId);
		attributeGetterFunctions.put("uuid", WikiNode::getUuid);
		attributeSetterBiConsumers.put(
			"uuid", (BiConsumer<WikiNode, String>)WikiNode::setUuid);
		attributeGetterFunctions.put(
			"externalReferenceCode", WikiNode::getExternalReferenceCode);
		attributeSetterBiConsumers.put(
			"externalReferenceCode",
			(BiConsumer<WikiNode, String>)WikiNode::setExternalReferenceCode);
		attributeGetterFunctions.put("nodeId", WikiNode::getNodeId);
		attributeSetterBiConsumers.put(
			"nodeId", (BiConsumer<WikiNode, Long>)WikiNode::setNodeId);
		attributeGetterFunctions.put("groupId", WikiNode::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId", (BiConsumer<WikiNode, Long>)WikiNode::setGroupId);
		attributeGetterFunctions.put("companyId", WikiNode::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId", (BiConsumer<WikiNode, Long>)WikiNode::setCompanyId);
		attributeGetterFunctions.put("userId", WikiNode::getUserId);
		attributeSetterBiConsumers.put(
			"userId", (BiConsumer<WikiNode, Long>)WikiNode::setUserId);
		attributeGetterFunctions.put("userName", WikiNode::getUserName);
		attributeSetterBiConsumers.put(
			"userName", (BiConsumer<WikiNode, String>)WikiNode::setUserName);
		attributeGetterFunctions.put("createDate", WikiNode::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate", (BiConsumer<WikiNode, Date>)WikiNode::setCreateDate);
		attributeGetterFunctions.put("modifiedDate", WikiNode::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<WikiNode, Date>)WikiNode::setModifiedDate);
		attributeGetterFunctions.put("name", WikiNode::getName);
		attributeSetterBiConsumers.put(
			"name", (BiConsumer<WikiNode, String>)WikiNode::setName);
		attributeGetterFunctions.put("description", WikiNode::getDescription);
		attributeSetterBiConsumers.put(
			"description",
			(BiConsumer<WikiNode, String>)WikiNode::setDescription);
		attributeGetterFunctions.put("lastPostDate", WikiNode::getLastPostDate);
		attributeSetterBiConsumers.put(
			"lastPostDate",
			(BiConsumer<WikiNode, Date>)WikiNode::setLastPostDate);
		attributeGetterFunctions.put(
			"lastPublishDate", WikiNode::getLastPublishDate);
		attributeSetterBiConsumers.put(
			"lastPublishDate",
			(BiConsumer<WikiNode, Date>)WikiNode::setLastPublishDate);
		attributeGetterFunctions.put("status", WikiNode::getStatus);
		attributeSetterBiConsumers.put(
			"status", (BiConsumer<WikiNode, Integer>)WikiNode::setStatus);
		attributeGetterFunctions.put(
			"statusByUserId", WikiNode::getStatusByUserId);
		attributeSetterBiConsumers.put(
			"statusByUserId",
			(BiConsumer<WikiNode, Long>)WikiNode::setStatusByUserId);
		attributeGetterFunctions.put(
			"statusByUserName", WikiNode::getStatusByUserName);
		attributeSetterBiConsumers.put(
			"statusByUserName",
			(BiConsumer<WikiNode, String>)WikiNode::setStatusByUserName);
		attributeGetterFunctions.put("statusDate", WikiNode::getStatusDate);
		attributeSetterBiConsumers.put(
			"statusDate", (BiConsumer<WikiNode, Date>)WikiNode::setStatusDate);

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

	@JSON
	@Override
	public long getNodeId() {
		return _nodeId;
	}

	@Override
	public void setNodeId(long nodeId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_nodeId = nodeId;
	}

	@JSON
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
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_name = name;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #getColumnOriginalValue(String)}
	 */
	@Deprecated
	public String getOriginalName() {
		return getColumnOriginalValue("name");
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
	public void setDescription(String description) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_description = description;
	}

	@JSON
	@Override
	public Date getLastPostDate() {
		return _lastPostDate;
	}

	@Override
	public void setLastPostDate(Date lastPostDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_lastPostDate = lastPostDate;
	}

	@JSON
	@Override
	public Date getLastPublishDate() {
		return _lastPublishDate;
	}

	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_lastPublishDate = lastPublishDate;
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

	@JSON
	@Override
	public long getStatusByUserId() {
		return _statusByUserId;
	}

	@Override
	public void setStatusByUserId(long statusByUserId) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_statusByUserId = statusByUserId;
	}

	@Override
	public String getStatusByUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getStatusByUserId());

			return user.getUuid();
		}
		catch (PortalException portalException) {
			return "";
		}
	}

	@Override
	public void setStatusByUserUuid(String statusByUserUuid) {
	}

	@JSON
	@Override
	public String getStatusByUserName() {
		if (_statusByUserName == null) {
			return "";
		}
		else {
			return _statusByUserName;
		}
	}

	@Override
	public void setStatusByUserName(String statusByUserName) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_statusByUserName = statusByUserName;
	}

	@JSON
	@Override
	public Date getStatusDate() {
		return _statusDate;
	}

	@Override
	public void setStatusDate(Date statusDate) {
		if (_columnOriginalValues == Collections.EMPTY_MAP) {
			_setColumnOriginalValues();
		}

		_statusDate = statusDate;
	}

	@Override
	public long getContainerModelId() {
		return getNodeId();
	}

	@Override
	public void setContainerModelId(long containerModelId) {
		_nodeId = containerModelId;
	}

	@Override
	public String getContainerModelName() {
		return String.valueOf(getName());
	}

	@Override
	public long getParentContainerModelId() {
		return 0;
	}

	@Override
	public void setParentContainerModelId(long parentContainerModelId) {
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(WikiNode.class.getName()));
	}

	@Override
	public long getTrashEntryClassPK() {
		return getPrimaryKey();
	}

	@Override
	public boolean isInTrash() {
		if (getStatus() == WorkflowConstants.STATUS_IN_TRASH) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isApproved() {
		if (getStatus() == WorkflowConstants.STATUS_APPROVED) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isDenied() {
		if (getStatus() == WorkflowConstants.STATUS_DENIED) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isDraft() {
		if (getStatus() == WorkflowConstants.STATUS_DRAFT) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isExpired() {
		if (getStatus() == WorkflowConstants.STATUS_EXPIRED) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isInactive() {
		if (getStatus() == WorkflowConstants.STATUS_INACTIVE) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isIncomplete() {
		if (getStatus() == WorkflowConstants.STATUS_INCOMPLETE) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isPending() {
		if (getStatus() == WorkflowConstants.STATUS_PENDING) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isScheduled() {
		if (getStatus() == WorkflowConstants.STATUS_SCHEDULED) {
			return true;
		}
		else {
			return false;
		}
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
			getCompanyId(), WikiNode.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public WikiNode toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, WikiNode>
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
		WikiNodeImpl wikiNodeImpl = new WikiNodeImpl();

		wikiNodeImpl.setMvccVersion(getMvccVersion());
		wikiNodeImpl.setCtCollectionId(getCtCollectionId());
		wikiNodeImpl.setUuid(getUuid());
		wikiNodeImpl.setExternalReferenceCode(getExternalReferenceCode());
		wikiNodeImpl.setNodeId(getNodeId());
		wikiNodeImpl.setGroupId(getGroupId());
		wikiNodeImpl.setCompanyId(getCompanyId());
		wikiNodeImpl.setUserId(getUserId());
		wikiNodeImpl.setUserName(getUserName());
		wikiNodeImpl.setCreateDate(getCreateDate());
		wikiNodeImpl.setModifiedDate(getModifiedDate());
		wikiNodeImpl.setName(getName());
		wikiNodeImpl.setDescription(getDescription());
		wikiNodeImpl.setLastPostDate(getLastPostDate());
		wikiNodeImpl.setLastPublishDate(getLastPublishDate());
		wikiNodeImpl.setStatus(getStatus());
		wikiNodeImpl.setStatusByUserId(getStatusByUserId());
		wikiNodeImpl.setStatusByUserName(getStatusByUserName());
		wikiNodeImpl.setStatusDate(getStatusDate());

		wikiNodeImpl.resetOriginalValues();

		return wikiNodeImpl;
	}

	@Override
	public WikiNode cloneWithOriginalValues() {
		WikiNodeImpl wikiNodeImpl = new WikiNodeImpl();

		wikiNodeImpl.setMvccVersion(
			this.<Long>getColumnOriginalValue("mvccVersion"));
		wikiNodeImpl.setCtCollectionId(
			this.<Long>getColumnOriginalValue("ctCollectionId"));
		wikiNodeImpl.setUuid(this.<String>getColumnOriginalValue("uuid_"));
		wikiNodeImpl.setExternalReferenceCode(
			this.<String>getColumnOriginalValue("externalReferenceCode"));
		wikiNodeImpl.setNodeId(this.<Long>getColumnOriginalValue("nodeId"));
		wikiNodeImpl.setGroupId(this.<Long>getColumnOriginalValue("groupId"));
		wikiNodeImpl.setCompanyId(
			this.<Long>getColumnOriginalValue("companyId"));
		wikiNodeImpl.setUserId(this.<Long>getColumnOriginalValue("userId"));
		wikiNodeImpl.setUserName(
			this.<String>getColumnOriginalValue("userName"));
		wikiNodeImpl.setCreateDate(
			this.<Date>getColumnOriginalValue("createDate"));
		wikiNodeImpl.setModifiedDate(
			this.<Date>getColumnOriginalValue("modifiedDate"));
		wikiNodeImpl.setName(this.<String>getColumnOriginalValue("name"));
		wikiNodeImpl.setDescription(
			this.<String>getColumnOriginalValue("description"));
		wikiNodeImpl.setLastPostDate(
			this.<Date>getColumnOriginalValue("lastPostDate"));
		wikiNodeImpl.setLastPublishDate(
			this.<Date>getColumnOriginalValue("lastPublishDate"));
		wikiNodeImpl.setStatus(this.<Integer>getColumnOriginalValue("status"));
		wikiNodeImpl.setStatusByUserId(
			this.<Long>getColumnOriginalValue("statusByUserId"));
		wikiNodeImpl.setStatusByUserName(
			this.<String>getColumnOriginalValue("statusByUserName"));
		wikiNodeImpl.setStatusDate(
			this.<Date>getColumnOriginalValue("statusDate"));

		return wikiNodeImpl;
	}

	@Override
	public int compareTo(WikiNode wikiNode) {
		int value = 0;

		value = getName().compareToIgnoreCase(wikiNode.getName());

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

		if (!(object instanceof WikiNode)) {
			return false;
		}

		WikiNode wikiNode = (WikiNode)object;

		long primaryKey = wikiNode.getPrimaryKey();

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
	public CacheModel<WikiNode> toCacheModel() {
		WikiNodeCacheModel wikiNodeCacheModel = new WikiNodeCacheModel();

		wikiNodeCacheModel.mvccVersion = getMvccVersion();

		wikiNodeCacheModel.ctCollectionId = getCtCollectionId();

		wikiNodeCacheModel.uuid = getUuid();

		String uuid = wikiNodeCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			wikiNodeCacheModel.uuid = null;
		}

		wikiNodeCacheModel.externalReferenceCode = getExternalReferenceCode();

		String externalReferenceCode = wikiNodeCacheModel.externalReferenceCode;

		if ((externalReferenceCode != null) &&
			(externalReferenceCode.length() == 0)) {

			wikiNodeCacheModel.externalReferenceCode = null;
		}

		wikiNodeCacheModel.nodeId = getNodeId();

		wikiNodeCacheModel.groupId = getGroupId();

		wikiNodeCacheModel.companyId = getCompanyId();

		wikiNodeCacheModel.userId = getUserId();

		wikiNodeCacheModel.userName = getUserName();

		String userName = wikiNodeCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			wikiNodeCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			wikiNodeCacheModel.createDate = createDate.getTime();
		}
		else {
			wikiNodeCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			wikiNodeCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			wikiNodeCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		wikiNodeCacheModel.name = getName();

		String name = wikiNodeCacheModel.name;

		if ((name != null) && (name.length() == 0)) {
			wikiNodeCacheModel.name = null;
		}

		wikiNodeCacheModel.description = getDescription();

		String description = wikiNodeCacheModel.description;

		if ((description != null) && (description.length() == 0)) {
			wikiNodeCacheModel.description = null;
		}

		Date lastPostDate = getLastPostDate();

		if (lastPostDate != null) {
			wikiNodeCacheModel.lastPostDate = lastPostDate.getTime();
		}
		else {
			wikiNodeCacheModel.lastPostDate = Long.MIN_VALUE;
		}

		Date lastPublishDate = getLastPublishDate();

		if (lastPublishDate != null) {
			wikiNodeCacheModel.lastPublishDate = lastPublishDate.getTime();
		}
		else {
			wikiNodeCacheModel.lastPublishDate = Long.MIN_VALUE;
		}

		wikiNodeCacheModel.status = getStatus();

		wikiNodeCacheModel.statusByUserId = getStatusByUserId();

		wikiNodeCacheModel.statusByUserName = getStatusByUserName();

		String statusByUserName = wikiNodeCacheModel.statusByUserName;

		if ((statusByUserName != null) && (statusByUserName.length() == 0)) {
			wikiNodeCacheModel.statusByUserName = null;
		}

		Date statusDate = getStatusDate();

		if (statusDate != null) {
			wikiNodeCacheModel.statusDate = statusDate.getTime();
		}
		else {
			wikiNodeCacheModel.statusDate = Long.MIN_VALUE;
		}

		return wikiNodeCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<WikiNode, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			(5 * attributeGetterFunctions.size()) + 2);

		sb.append("{");

		for (Map.Entry<String, Function<WikiNode, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<WikiNode, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("\"");
			sb.append(attributeName);
			sb.append("\": ");

			Object value = attributeGetterFunction.apply((WikiNode)this);

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

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, WikiNode>
			_escapedModelProxyProviderFunction =
				ProxyUtil.getProxyProviderFunction(
					WikiNode.class, ModelWrapper.class);

	}

	private long _mvccVersion;
	private long _ctCollectionId;
	private String _uuid;
	private String _externalReferenceCode;
	private long _nodeId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _name;
	private String _description;
	private Date _lastPostDate;
	private Date _lastPublishDate;
	private int _status;
	private long _statusByUserId;
	private String _statusByUserName;
	private Date _statusDate;

	public <T> T getColumnValue(String columnName) {
		columnName = _attributeNames.getOrDefault(columnName, columnName);

		Function<WikiNode, Object> function = _attributeGetterFunctions.get(
			columnName);

		if (function == null) {
			throw new IllegalArgumentException(
				"No attribute getter function found for " + columnName);
		}

		return (T)function.apply((WikiNode)this);
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
		_columnOriginalValues.put("nodeId", _nodeId);
		_columnOriginalValues.put("groupId", _groupId);
		_columnOriginalValues.put("companyId", _companyId);
		_columnOriginalValues.put("userId", _userId);
		_columnOriginalValues.put("userName", _userName);
		_columnOriginalValues.put("createDate", _createDate);
		_columnOriginalValues.put("modifiedDate", _modifiedDate);
		_columnOriginalValues.put("name", _name);
		_columnOriginalValues.put("description", _description);
		_columnOriginalValues.put("lastPostDate", _lastPostDate);
		_columnOriginalValues.put("lastPublishDate", _lastPublishDate);
		_columnOriginalValues.put("status", _status);
		_columnOriginalValues.put("statusByUserId", _statusByUserId);
		_columnOriginalValues.put("statusByUserName", _statusByUserName);
		_columnOriginalValues.put("statusDate", _statusDate);
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

		columnBitmasks.put("nodeId", 16L);

		columnBitmasks.put("groupId", 32L);

		columnBitmasks.put("companyId", 64L);

		columnBitmasks.put("userId", 128L);

		columnBitmasks.put("userName", 256L);

		columnBitmasks.put("createDate", 512L);

		columnBitmasks.put("modifiedDate", 1024L);

		columnBitmasks.put("name", 2048L);

		columnBitmasks.put("description", 4096L);

		columnBitmasks.put("lastPostDate", 8192L);

		columnBitmasks.put("lastPublishDate", 16384L);

		columnBitmasks.put("status", 32768L);

		columnBitmasks.put("statusByUserId", 65536L);

		columnBitmasks.put("statusByUserName", 131072L);

		columnBitmasks.put("statusDate", 262144L);

		_columnBitmasks = Collections.unmodifiableMap(columnBitmasks);
	}

	private long _columnBitmask;
	private WikiNode _escapedModel;

}