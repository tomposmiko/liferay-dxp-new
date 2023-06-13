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

package com.liferay.object.rest.internal.resource.v1_0;

import com.liferay.object.action.engine.ObjectActionEngine;
import com.liferay.object.constants.ObjectActionTriggerConstants;
import com.liferay.object.entry.util.ObjectEntryNameUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.rest.dto.v1_0.ObjectEntry;
import com.liferay.object.rest.manager.v1_0.ObjectEntryManager;
import com.liferay.object.rest.manager.v1_0.ObjectEntryManagerRegistry;
import com.liferay.object.rest.odata.entity.v1_0.ObjectEntryEntityModel;
import com.liferay.object.rest.petra.sql.dsl.expression.FilterPredicateFactory;
import com.liferay.object.scope.ObjectScopeProvider;
import com.liferay.object.scope.ObjectScopeProviderRegistry;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.service.ObjectRelationshipService;
import com.liferay.object.system.SystemObjectDefinitionMetadata;
import com.liferay.object.system.SystemObjectDefinitionMetadataRegistry;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.aggregation.Aggregation;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.io.Serializable;

import java.util.Collection;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;

/**
 * @author Javier Gamarra
 */
public class ObjectEntryResourceImpl extends BaseObjectEntryResourceImpl {

	public ObjectEntryResourceImpl(
		FilterPredicateFactory filterPredicateFactory, JSONFactory jsonFactory,
		ObjectActionEngine objectActionEngine,
		ObjectDefinitionLocalService objectDefinitionLocalService,
		ObjectEntryLocalService objectEntryLocalService,
		ObjectEntryManagerRegistry objectEntryManagerRegistry,
		ObjectFieldLocalService objectFieldLocalService,
		ObjectRelationshipService objectRelationshipService,
		ObjectScopeProviderRegistry objectScopeProviderRegistry,
		SystemObjectDefinitionMetadataRegistry
			systemObjectDefinitionMetadataRegistry) {

		_filterPredicateFactory = filterPredicateFactory;
		_jsonFactory = jsonFactory;
		_objectActionEngine = objectActionEngine;
		_objectDefinitionLocalService = objectDefinitionLocalService;
		_objectEntryLocalService = objectEntryLocalService;
		_objectEntryManagerRegistry = objectEntryManagerRegistry;
		_objectFieldLocalService = objectFieldLocalService;
		_objectRelationshipService = objectRelationshipService;
		_objectScopeProviderRegistry = objectScopeProviderRegistry;
		_systemObjectDefinitionMetadataRegistry =
			systemObjectDefinitionMetadataRegistry;
	}

	@Override
	public void create(
			Collection<ObjectEntry> objectEntries,
			Map<String, Serializable> parameters)
		throws Exception {

		_loadObjectDefinition(parameters);

		ObjectScopeProvider objectScopeProvider =
			_objectScopeProviderRegistry.getObjectScopeProvider(
				_objectDefinition.getScope());

		if (objectScopeProvider.isGroupAware()) {
			UnsafeConsumer<ObjectEntry, Exception> objectEntryUnsafeConsumer =
				null;

			String createStrategy = (String)parameters.getOrDefault(
				"createStrategy", "INSERT");

			if (StringUtil.equalsIgnoreCase(createStrategy, "INSERT")) {
				objectEntryUnsafeConsumer = objectEntry -> postScopeScopeKey(
					(String)parameters.get("scopeKey"), objectEntry);
			}

			if (StringUtil.equalsIgnoreCase(createStrategy, "UPSERT")) {
				objectEntryUnsafeConsumer =
					objectEntry -> putScopeScopeKeyByExternalReferenceCode(
						(String)parameters.get("scopeKey"),
						objectEntry.getExternalReferenceCode(), objectEntry);
			}

			if (objectEntryUnsafeConsumer == null) {
				throw new NotSupportedException(
					"Create strategy \"" + createStrategy +
						"\" is not supported for object entry");
			}

			contextBatchUnsafeConsumer.accept(
				objectEntries, objectEntryUnsafeConsumer);
		}
		else {
			super.create(objectEntries, parameters);
		}
	}

	@Override
	public void delete(
			Collection<ObjectEntry> objectEntries,
			Map<String, Serializable> parameters)
		throws Exception {

		_loadObjectDefinition(parameters);

		super.delete(objectEntries, parameters);
	}

	@Override
	public void deleteByExternalReferenceCode(String externalReferenceCode)
		throws Exception {

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		objectEntryManager.deleteObjectEntry(
			externalReferenceCode, contextCompany.getCompanyId(),
			_objectDefinition, null);
	}

	@Override
	public void deleteObjectEntry(Long objectEntryId) throws Exception {
		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		objectEntryManager.deleteObjectEntry(_objectDefinition, objectEntryId);
	}

	@Override
	public void deleteScopeScopeKeyByExternalReferenceCode(
			String scopeKey, String externalReferenceCode)
		throws Exception {

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		objectEntryManager.deleteObjectEntry(
			externalReferenceCode, contextCompany.getCompanyId(),
			_objectDefinition, scopeKey);
	}

	@Override
	public ObjectEntry getByExternalReferenceCode(String externalReferenceCode)
		throws Exception {

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		return objectEntryManager.getObjectEntry(
			_getDTOConverterContext(null), externalReferenceCode,
			contextCompany.getCompanyId(), _objectDefinition, null);
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap) {
		return new ObjectEntryEntityModel(
			_objectFieldLocalService.getObjectFields(
				_objectDefinition.getObjectDefinitionId()));
	}

	@Override
	public Page<ObjectEntry> getObjectEntriesPage(
			Boolean flatten, String search, Aggregation aggregation,
			Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception {

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		Predicate predicate = null;

		if (contextHttpServletRequest != null) {
			predicate = _filterPredicateFactory.create(
				ParamUtil.getString(contextHttpServletRequest, "filter"),
				_objectDefinition.getObjectDefinitionId());
		}

		return objectEntryManager.getObjectEntries(
			contextCompany.getCompanyId(), _objectDefinition, null, aggregation,
			_getDTOConverterContext(null), pagination, predicate, search,
			sorts);
	}

	@Override
	public ObjectEntry getObjectEntry(Long objectEntryId) throws Exception {
		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		return objectEntryManager.getObjectEntry(
			_getDTOConverterContext(objectEntryId), _objectDefinition,
			objectEntryId);
	}

	@Override
	public ObjectEntry getScopeScopeKeyByExternalReferenceCode(
			String scopeKey, String externalReferenceCode)
		throws Exception {

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		return objectEntryManager.getObjectEntry(
			_getDTOConverterContext(null), externalReferenceCode,
			contextCompany.getCompanyId(), _objectDefinition, scopeKey);
	}

	@Override
	public Page<ObjectEntry> getScopeScopeKeyPage(
			String scopeKey, Boolean flatten, String search,
			Aggregation aggregation, Filter filter, Pagination pagination,
			Sort[] sorts)
		throws Exception {

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		return objectEntryManager.getObjectEntries(
			contextCompany.getCompanyId(), _objectDefinition, scopeKey,
			aggregation, _getDTOConverterContext(null), pagination,
			_filterPredicateFactory.create(
				ParamUtil.getString(contextHttpServletRequest, "filter"),
				_objectDefinition.getObjectDefinitionId()),
			search, sorts);
	}

	@Override
	public ObjectEntry postObjectEntry(ObjectEntry objectEntry)
		throws Exception {

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		return objectEntryManager.addObjectEntry(
			_getDTOConverterContext(null), _objectDefinition, objectEntry,
			null);
	}

	@Override
	public ObjectEntry postScopeScopeKey(
			String scopeKey, ObjectEntry objectEntry)
		throws Exception {

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		return objectEntryManager.addObjectEntry(
			_getDTOConverterContext(null), _objectDefinition, objectEntry,
			scopeKey);
	}

	@Override
	public ObjectEntry putByExternalReferenceCode(
			String externalReferenceCode, ObjectEntry objectEntry)
		throws Exception {

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		return objectEntryManager.addOrUpdateObjectEntry(
			contextCompany.getCompanyId(), _getDTOConverterContext(null),
			externalReferenceCode, _objectDefinition, objectEntry, null);
	}

	@Override
	public ObjectEntry
			putByExternalReferenceCodeCurrentExternalReferenceCodeObjectRelationshipNameRelatedExternalReferenceCode(
				String currentExternalReferenceCode,
				String objectRelationshipName,
				String relatedExternalReferenceCode)
		throws Exception {

		ObjectRelationship objectRelationship =
			_objectRelationshipService.getObjectRelationship(
				_objectDefinition.getObjectDefinitionId(),
				objectRelationshipName);

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		long primaryKey1 = _getPrimaryKey(
			currentExternalReferenceCode,
			objectRelationship.getObjectDefinitionId1());
		long primaryKey2 = _getPrimaryKey(
			relatedExternalReferenceCode,
			objectRelationship.getObjectDefinitionId2());

		return _getRelatedObjectEntry(
			_objectDefinitionLocalService.getObjectDefinition(
				objectRelationship.getObjectDefinitionId2()),
			objectEntryManager.addObjectRelationshipMappingTableValues(
				_getDTOConverterContext(primaryKey1), objectRelationship,
				primaryKey1, primaryKey2));
	}

	@Override
	public void
			putByExternalReferenceCodeObjectEntryExternalReferenceCodeObjectActionObjectActionName(
				String objectEntryExternalReferenceCode,
				String objectActionName)
		throws Exception {

		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-166918"))) {
			throw new UnsupportedOperationException();
		}

		_executeObjectAction(
			objectActionName,
			getByExternalReferenceCode(objectEntryExternalReferenceCode));
	}

	@Override
	public ObjectEntry putObjectEntry(
			Long objectEntryId, ObjectEntry objectEntry)
		throws Exception {

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		return objectEntryManager.updateObjectEntry(
			_getDTOConverterContext(objectEntryId), _objectDefinition,
			objectEntryId, objectEntry);
	}

	@Override
	public void putObjectEntryObjectActionObjectActionName(
			Long objectEntryId, String objectActionName)
		throws Exception {

		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-166918"))) {
			throw new UnsupportedOperationException();
		}

		_executeObjectAction(objectActionName, getObjectEntry(objectEntryId));
	}

	@Override
	public ObjectEntry putScopeScopeKeyByExternalReferenceCode(
			String scopeKey, String externalReferenceCode,
			ObjectEntry objectEntry)
		throws Exception {

		ObjectEntryManager objectEntryManager =
			_objectEntryManagerRegistry.getObjectEntryManager(
				_objectDefinition.getStorageType());

		return objectEntryManager.addOrUpdateObjectEntry(
			contextCompany.getCompanyId(), _getDTOConverterContext(null),
			externalReferenceCode, _objectDefinition, objectEntry, scopeKey);
	}

	@Override
	public Page<ObjectEntry> read(
			Filter filter, Pagination pagination, Sort[] sorts,
			Map<String, Serializable> parameters, String search)
		throws Exception {

		_loadObjectDefinition(parameters);

		ObjectScopeProvider objectScopeProvider =
			_objectScopeProviderRegistry.getObjectScopeProvider(
				_objectDefinition.getScope());

		if (objectScopeProvider.isGroupAware()) {
			return getScopeScopeKeyPage(
				(String)parameters.get("scopeKey"),
				Boolean.parseBoolean((String)parameters.get("flatten")), search,
				null, filter, pagination, sorts);
		}

		return getObjectEntriesPage(
			Boolean.parseBoolean((String)parameters.get("flatten")), search,
			null, filter, pagination, sorts);
	}

	public void setObjectDefinition(ObjectDefinition objectDefinition) {
		_objectDefinition = objectDefinition;
	}

	@Override
	public void update(
			Collection<ObjectEntry> objectEntries,
			Map<String, Serializable> parameters)
		throws Exception {

		_loadObjectDefinition(parameters);

		super.update(objectEntries, parameters);
	}

	@Override
	protected Long getPermissionCheckerGroupId(Object id) throws Exception {
		com.liferay.object.model.ObjectEntry objectEntry =
			_objectEntryLocalService.getObjectEntry(GetterUtil.getLong(id));

		return objectEntry.getGroupId();
	}

	@Override
	protected String getPermissionCheckerResourceName(Object id)
		throws Exception {

		return ObjectDefinition.class.getName() + "#" +
			_objectDefinition.getObjectDefinitionId();
	}

	private void _executeObjectAction(
			String objectActionName, ObjectEntry objectEntry)
		throws Exception {

		com.liferay.object.model.ObjectEntry serviceBuilderObjectEntry =
			_objectEntryLocalService.getObjectEntry(objectEntry.getId());

		_objectActionEngine.executeObjectAction(
			objectActionName, ObjectActionTriggerConstants.KEY_STANDALONE,
			_objectDefinition.getObjectDefinitionId(),
			JSONUtil.put(
				"classPK", serviceBuilderObjectEntry.getObjectEntryId()
			).put(
				"objectEntry",
				HashMapBuilder.putAll(
					serviceBuilderObjectEntry.getModelAttributes()
				).put(
					"values", serviceBuilderObjectEntry.getValues()
				).build()
			).put(
				"objectEntryDTO" + _objectDefinition.getShortName(),
				() -> {
					JSONObject jsonObject = _jsonFactory.createJSONObject(
						_jsonFactory.looseSerializeDeep(objectEntry));

					return jsonObject.toMap();
				}
			),
			contextUser.getUserId());
	}

	private DefaultDTOConverterContext _getDTOConverterContext(
		Long objectEntryId) {

		return new DefaultDTOConverterContext(
			contextAcceptLanguage.isAcceptAllLanguages(), null, null,
			contextHttpServletRequest, objectEntryId,
			contextAcceptLanguage.getPreferredLocale(), contextUriInfo,
			contextUser);
	}

	private long _getPrimaryKey(
			String externalReferenceCode, long objectDefinitionId)
		throws Exception {

		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.fetchObjectDefinition(
				objectDefinitionId);

		if (objectDefinition.isSystem()) {
			SystemObjectDefinitionMetadata systemObjectDefinitionMetadata =
				_systemObjectDefinitionMetadataRegistry.
					getSystemObjectDefinitionMetadata(
						objectDefinition.getName());

			BaseModel<?> baseModel =
				systemObjectDefinitionMetadata.
					getBaseModelByExternalReferenceCode(
						externalReferenceCode, objectDefinition.getCompanyId());

			return (long)baseModel.getPrimaryKeyObj();
		}

		com.liferay.object.model.ObjectEntry objectEntry =
			_objectEntryLocalService.getObjectEntry(
				externalReferenceCode,
				objectDefinition.getObjectDefinitionId());

		return objectEntry.getObjectEntryId();
	}

	private ObjectEntry _getRelatedObjectEntry(
		ObjectDefinition objectDefinition, ObjectEntry objectEntry) {

		Map<String, Map<String, String>> actions = objectEntry.getActions();

		for (Map.Entry<String, Map<String, String>> entry :
				actions.entrySet()) {

			Map<String, String> map = entry.getValue();

			String href = map.get("href");

			map.put(
				"href",
				StringUtil.replace(
					href,
					StringUtil.lowerCaseFirstLetter(
						_objectDefinition.getPluralLabel(
							contextAcceptLanguage.getPreferredLocale())),
					StringUtil.lowerCaseFirstLetter(
						objectDefinition.getPluralLabel(
							contextAcceptLanguage.getPreferredLocale()))));
		}

		return objectEntry;
	}

	private void _loadObjectDefinition(Map<String, Serializable> parameters)
		throws Exception {

		String taskItemDelegateName = (String)parameters.get(
			"taskItemDelegateName");

		if (taskItemDelegateName != null) {
			taskItemDelegateName = ObjectEntryNameUtil.fromTechnicalName(
				taskItemDelegateName);

			_objectDefinition =
				_objectDefinitionLocalService.fetchObjectDefinition(
					contextCompany.getCompanyId(), taskItemDelegateName);

			if (_objectDefinition != null) {
				return;
			}
		}

		String parameterValue = (String)parameters.get("objectDefinitionId");

		if ((parameterValue != null) && (parameterValue.length() > 2)) {
			String[] objectDefinitionIds = StringUtil.split(
				parameterValue.substring(1, parameterValue.length() - 1), ",");

			if (objectDefinitionIds.length > 0) {
				_objectDefinition =
					_objectDefinitionLocalService.getObjectDefinition(
						GetterUtil.getLong(objectDefinitionIds[0]));

				return;
			}
		}

		throw new NotFoundException("Missing parameter \"objectDefinitionId\"");
	}

	private final FilterPredicateFactory _filterPredicateFactory;
	private final JSONFactory _jsonFactory;
	private final ObjectActionEngine _objectActionEngine;

	@Context
	private ObjectDefinition _objectDefinition;

	private final ObjectDefinitionLocalService _objectDefinitionLocalService;
	private final ObjectEntryLocalService _objectEntryLocalService;
	private final ObjectEntryManagerRegistry _objectEntryManagerRegistry;
	private final ObjectFieldLocalService _objectFieldLocalService;
	private final ObjectRelationshipService _objectRelationshipService;
	private final ObjectScopeProviderRegistry _objectScopeProviderRegistry;
	private final SystemObjectDefinitionMetadataRegistry
		_systemObjectDefinitionMetadataRegistry;

}