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

package com.liferay.object.rest.internal.manager.v1_0;

import com.liferay.account.constants.AccountConstants;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.object.constants.ObjectConstants;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectRelationshipConstants;
import com.liferay.object.exception.NoSuchObjectEntryException;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.related.models.ObjectRelatedModelsProvider;
import com.liferay.object.related.models.ObjectRelatedModelsProviderRegistry;
import com.liferay.object.rest.dto.v1_0.ObjectEntry;
import com.liferay.object.rest.internal.dto.v1_0.converter.ObjectEntryDTOConverter;
import com.liferay.object.rest.internal.petra.sql.dsl.expression.OrderByExpressionUtil;
import com.liferay.object.rest.internal.resource.v1_0.ObjectEntryResourceImpl;
import com.liferay.object.rest.manager.v1_0.BaseObjectEntryManager;
import com.liferay.object.rest.manager.v1_0.ObjectEntryManager;
import com.liferay.object.rest.petra.sql.dsl.expression.FilterPredicateFactory;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectEntryService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.object.service.ObjectRelationshipService;
import com.liferay.object.system.SystemObjectDefinitionMetadata;
import com.liferay.object.system.SystemObjectDefinitionMetadataTracker;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.GroupThreadLocal;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.aggregation.Aggregations;
import com.liferay.portal.search.aggregation.bucket.FilterAggregation;
import com.liferay.portal.search.aggregation.bucket.NestedAggregation;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.portal.vulcan.aggregation.Aggregation;
import com.liferay.portal.vulcan.aggregation.Facet;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.util.ActionUtil;
import com.liferay.portal.vulcan.util.SearchUtil;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.io.Serializable;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Javier de Arcos
 */
@Component(
	immediate = true,
	property = "object.entry.manager.storage.type=" + ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT,
	service = ObjectEntryManager.class
)
public class DefaultObjectEntryManagerImpl
	extends BaseObjectEntryManager implements ObjectEntryManager {

	@Override
	public ObjectEntry addObjectEntry(
			DTOConverterContext dtoConverterContext,
			ObjectDefinition objectDefinition, ObjectEntry objectEntry,
			String scopeKey)
		throws Exception {

		return _toObjectEntry(
			dtoConverterContext, objectDefinition,
			_objectEntryService.addObjectEntry(
				getGroupId(objectDefinition, scopeKey),
				objectDefinition.getObjectDefinitionId(),
				_toObjectValues(
					objectDefinition.getObjectDefinitionId(),
					objectEntry.getProperties(),
					dtoConverterContext.getLocale()),
				_createServiceContext(
					objectEntry.getProperties(),
					dtoConverterContext.getUserId())));
	}

	@Override
	public ObjectEntry addObjectRelationshipMappingTableValues(
			DTOConverterContext dtoConverterContext,
			ObjectDefinition objectDefinition, String objectRelationshipName,
			long primaryKey1, long primaryKey2)
		throws Exception {

		ObjectRelationship objectRelationship =
			_objectRelationshipService.getObjectRelationship(
				objectDefinition.getObjectDefinitionId(),
				objectRelationshipName);

		_objectRelationshipService.addObjectRelationshipMappingTableValues(
			objectRelationship.getObjectRelationshipId(), primaryKey1,
			primaryKey2, new ServiceContext());

		return getObjectEntry(
			dtoConverterContext,
			_objectDefinitionLocalService.getObjectDefinition(
				objectRelationship.getObjectDefinitionId2()),
			primaryKey2);
	}

	@Override
	public ObjectEntry addOrUpdateObjectEntry(
			long companyId, DTOConverterContext dtoConverterContext,
			String externalReferenceCode, ObjectDefinition objectDefinition,
			ObjectEntry objectEntry, String scopeKey)
		throws Exception {

		ServiceContext serviceContext = _createServiceContext(
			objectEntry.getProperties(), dtoConverterContext.getUserId());

		serviceContext.setCompanyId(companyId);

		return _toObjectEntry(
			dtoConverterContext, objectDefinition,
			_objectEntryService.addOrUpdateObjectEntry(
				externalReferenceCode, getGroupId(objectDefinition, scopeKey),
				objectDefinition.getObjectDefinitionId(),
				_toObjectValues(
					objectDefinition.getObjectDefinitionId(),
					objectEntry.getProperties(),
					dtoConverterContext.getLocale()),
				serviceContext));
	}

	@Override
	public void deleteObjectEntry(
			ObjectDefinition objectDefinition, long objectEntryId)
		throws Exception {

		_checkObjectEntryObjectDefinitionId(
			objectDefinition,
			_objectEntryService.getObjectEntry(objectEntryId));

		_objectEntryService.deleteObjectEntry(objectEntryId);
	}

	@Override
	public void deleteObjectEntry(
			String externalReferenceCode, long companyId,
			ObjectDefinition objectDefinition, String scopeKey)
		throws Exception {

		com.liferay.object.model.ObjectEntry objectEntry =
			_objectEntryService.getObjectEntry(
				externalReferenceCode, companyId,
				getGroupId(objectDefinition, scopeKey));

		_checkObjectEntryObjectDefinitionId(objectDefinition, objectEntry);

		_objectEntryService.deleteObjectEntry(objectEntry.getObjectEntryId());
	}

	@Override
	public ObjectEntry fetchObjectEntry(
			DTOConverterContext dtoConverterContext,
			ObjectDefinition objectDefinition, long objectEntryId)
		throws Exception {

		com.liferay.object.model.ObjectEntry objectEntry =
			_objectEntryService.fetchObjectEntry(objectEntryId);

		if (objectEntry != null) {
			if (objectDefinition == null) {
				objectDefinition =
					_objectDefinitionLocalService.getObjectDefinition(
						objectEntry.getObjectDefinitionId());
			}

			return _toObjectEntry(
				dtoConverterContext, objectDefinition, objectEntry);
		}

		return null;
	}

	@Override
	public Page<ObjectEntry> getObjectEntries(
			long companyId, ObjectDefinition objectDefinition, String scopeKey,
			Aggregation aggregation, DTOConverterContext dtoConverterContext,
			Filter filter, Pagination pagination, String search, Sort[] sorts)
		throws Exception {

		long groupId = getGroupId(objectDefinition, scopeKey);

		return SearchUtil.search(
			HashMapBuilder.put(
				"create",
				ActionUtil.addAction(
					"ADD_OBJECT_ENTRY", ObjectEntryResourceImpl.class, 0L,
					"postObjectEntry", null, objectDefinition.getUserId(),
					_getObjectEntriesPermissionName(
						objectDefinition.getObjectDefinitionId()),
					groupId, dtoConverterContext.getUriInfo())
			).put(
				"get",
				ActionUtil.addAction(
					ActionKeys.VIEW, ObjectEntryResourceImpl.class, 0L,
					"getObjectEntriesPage", null, objectDefinition.getUserId(),
					_getObjectEntriesPermissionName(
						objectDefinition.getObjectDefinitionId()),
					groupId, dtoConverterContext.getUriInfo())
			).build(),
			booleanQuery -> {
				BooleanFilter booleanFilter =
					booleanQuery.getPreBooleanFilter();

				booleanFilter.add(
					new TermFilter(
						"objectDefinitionId",
						String.valueOf(
							objectDefinition.getObjectDefinitionId())),
					BooleanClauseOccur.MUST);
			},
			filter, objectDefinition.getClassName(), search, pagination,
			queryConfig -> queryConfig.setSelectedFieldNames(
				Field.ENTRY_CLASS_PK),
			searchContext -> {
				searchContext.addVulcanAggregation(aggregation);
				searchContext.setAttribute(
					Field.STATUS, WorkflowConstants.STATUS_ANY);
				searchContext.setAttribute(
					"objectDefinitionId",
					objectDefinition.getObjectDefinitionId());

				UriInfo uriInfo = dtoConverterContext.getUriInfo();

				if (uriInfo != null) {
					MultivaluedMap<String, String> queryParameters =
						uriInfo.getQueryParameters();

					searchContext.setAttribute(
						"searchByObjectView",
						queryParameters.containsKey("searchByObjectView"));
				}

				searchContext.setCompanyId(companyId);
				searchContext.setGroupIds(new long[] {groupId});

				SearchRequestBuilder searchRequestBuilder =
					_searchRequestBuilderFactory.builder(searchContext);

				_processVulcanAggregation(
					_aggregations, _queries, searchRequestBuilder, aggregation);
			},
			sorts,
			document -> getObjectEntry(
				dtoConverterContext, objectDefinition,
				GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK))));
	}

	@Override
	public Page<ObjectEntry> getObjectEntries(
			long companyId, ObjectDefinition objectDefinition, String scopeKey,
			Aggregation aggregation, DTOConverterContext dtoConverterContext,
			Pagination pagination, Predicate predicate, String search,
			Sort[] sorts)
		throws Exception {

		long groupId = getGroupId(objectDefinition, scopeKey);

		long[] accountEntryIds = {_NONEXISTING_ACCOUNT_ENTRY_ID};

		if (objectDefinition.isAccountEntryRestricted()) {
			List<AccountEntry> accountEntries =
				_accountEntryLocalService.getUserAccountEntries(
					dtoConverterContext.getUserId(),
					AccountConstants.PARENT_ACCOUNT_ENTRY_ID_DEFAULT, null,
					new String[] {
						AccountConstants.ACCOUNT_ENTRY_TYPE_BUSINESS,
						AccountConstants.ACCOUNT_ENTRY_TYPE_PERSON
					},
					WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS);

			accountEntryIds = ListUtil.toLongArray(
				accountEntries, AccountEntry::getAccountEntryId);
		}

		int start = QueryUtil.ALL_POS;
		int end = QueryUtil.ALL_POS;

		if (pagination != null) {
			start = pagination.getStartPosition();
			end = pagination.getEndPosition();
		}

		List<Facet> facets = new ArrayList<>();

		if ((aggregation != null) &&
			(aggregation.getAggregationTerms() != null)) {

			Map<String, String> aggregationTerms =
				aggregation.getAggregationTerms();

			for (Map.Entry<String, String> entry1 :
					aggregationTerms.entrySet()) {

				List<Facet.FacetValue> facetValues = new ArrayList<>();

				Map<Object, Long> aggregationCounts =
					_objectEntryLocalService.getAggregationCounts(
						objectDefinition.getObjectDefinitionId(),
						entry1.getKey(), predicate, start, end);

				for (Map.Entry<Object, Long> entry2 :
						aggregationCounts.entrySet()) {

					Long value = entry2.getValue();

					facetValues.add(
						new Facet.FacetValue(
							value.intValue(), String.valueOf(entry2.getKey())));
				}

				facets.add(new Facet(entry1.getKey(), facetValues));
			}
		}

		return Page.of(
			HashMapBuilder.put(
				"create",
				ActionUtil.addAction(
					"ADD_OBJECT_ENTRY", ObjectEntryResourceImpl.class, 0L,
					"postObjectEntry", null, objectDefinition.getUserId(),
					_getObjectEntriesPermissionName(
						objectDefinition.getObjectDefinitionId()),
					groupId, dtoConverterContext.getUriInfo())
			).put(
				"get",
				ActionUtil.addAction(
					ActionKeys.VIEW, ObjectEntryResourceImpl.class, 0L,
					"getObjectEntriesPage", null, objectDefinition.getUserId(),
					_getObjectEntriesPermissionName(
						objectDefinition.getObjectDefinitionId()),
					groupId, dtoConverterContext.getUriInfo())
			).build(),
			facets,
			TransformUtil.transform(
				_objectEntryLocalService.getValuesList(
					objectDefinition.getObjectDefinitionId(), groupId,
					accountEntryIds, predicate, search, start, end,
					OrderByExpressionUtil.getOrderByExpressions(
						objectDefinition.getObjectDefinitionId(),
						_objectFieldLocalService, sorts)),
				values -> _getObjectEntry(
					dtoConverterContext, objectDefinition, values)),
			pagination,
			_objectEntryLocalService.getValuesListCount(
				objectDefinition.getObjectDefinitionId(), groupId,
				accountEntryIds, predicate, search));
	}

	@Override
	public Page<ObjectEntry> getObjectEntries(
			long companyId, ObjectDefinition objectDefinition, String scopeKey,
			Aggregation aggregation, DTOConverterContext dtoConverterContext,
			String filterString, Pagination pagination, String search,
			Sort[] sorts)
		throws Exception {

		return getObjectEntries(
			companyId, objectDefinition, scopeKey, aggregation,
			dtoConverterContext, pagination,
			_filterPredicateFactory.create(
				filterString, objectDefinition.getObjectDefinitionId()),
			search, sorts);
	}

	@Override
	public ObjectEntry getObjectEntry(
			DTOConverterContext dtoConverterContext,
			ObjectDefinition objectDefinition, long objectEntryId)
		throws Exception {

		com.liferay.object.model.ObjectEntry objectEntry =
			_objectEntryService.getObjectEntry(objectEntryId);

		_checkObjectEntryObjectDefinitionId(objectDefinition, objectEntry);

		return _toObjectEntry(
			dtoConverterContext, objectDefinition, objectEntry);
	}

	@Override
	public ObjectEntry getObjectEntry(
			DTOConverterContext dtoConverterContext,
			String externalReferenceCode, long companyId,
			ObjectDefinition objectDefinition, String scopeKey)
		throws Exception {

		com.liferay.object.model.ObjectEntry objectEntry =
			_objectEntryService.getObjectEntry(
				externalReferenceCode, companyId,
				getGroupId(objectDefinition, scopeKey));

		_checkObjectEntryObjectDefinitionId(objectDefinition, objectEntry);

		return _toObjectEntry(
			dtoConverterContext, objectDefinition, objectEntry);
	}

	@Override
	public Page<ObjectEntry> getObjectEntryRelatedObjectEntries(
			DTOConverterContext dtoConverterContext,
			ObjectDefinition objectDefinition, Long objectEntryId,
			String objectRelationshipName, Pagination pagination)
		throws Exception {

		ObjectRelationship objectRelationship =
			_objectRelationshipService.getObjectRelationship(
				objectDefinition.getObjectDefinitionId(),
				objectRelationshipName);

		ObjectDefinition relatedObjectDefinition = _getRelatedObjectDefinition(
			objectDefinition, objectRelationship);

		ObjectRelatedModelsProvider objectRelatedModelsProvider =
			_objectRelatedModelsProviderRegistry.getObjectRelatedModelsProvider(
				relatedObjectDefinition.getClassName(),
				objectRelationship.getType());

		if (objectDefinition.isSystem()) {
			return _getSystemObjectRelatedObjectEntries(
				dtoConverterContext, objectDefinition, objectEntryId,
				objectRelationship, objectRelatedModelsProvider, pagination);
		}

		com.liferay.object.model.ObjectEntry objectEntry =
			_objectEntryService.getObjectEntry(objectEntryId);

		return Page.of(
			HashMapBuilder.put(
				"get",
				ActionUtil.addAction(
					ActionKeys.VIEW, ObjectEntryResourceImpl.class,
					objectEntryId,
					"getCurrentObjectEntriesObjectRelationshipNamePage", null,
					objectEntry.getUserId(),
					_getObjectEntryPermissionName(
						objectDefinition.getObjectDefinitionId()),
					objectEntry.getGroupId(), dtoConverterContext.getUriInfo())
			).build(),
			_toObjectEntries(
				dtoConverterContext,
				objectRelatedModelsProvider.getRelatedModels(
					objectEntry.getGroupId(),
					objectRelationship.getObjectRelationshipId(),
					objectEntry.getPrimaryKey(), pagination.getStartPosition(),
					pagination.getEndPosition())));
	}

	@Override
	public Page<Object> getRelatedSystemObjectEntries(
			ObjectDefinition objectDefinition, Long objectEntryId,
			String objectRelationshipName, Pagination pagination)
		throws Exception {

		ObjectRelationship objectRelationship =
			_objectRelationshipService.getObjectRelationship(
				objectDefinition.getObjectDefinitionId(),
				objectRelationshipName);

		ObjectDefinition relatedObjectDefinition = _getRelatedObjectDefinition(
			objectDefinition, objectRelationship);

		ObjectRelatedModelsProvider objectRelatedModelsProvider =
			_objectRelatedModelsProviderRegistry.getObjectRelatedModelsProvider(
				relatedObjectDefinition.getClassName(),
				objectRelationship.getType());

		com.liferay.object.model.ObjectEntry objectEntry =
			_objectEntryService.getObjectEntry(objectEntryId);

		return Page.of(
			TransformUtil.transform(
				(List<BaseModel<?>>)
					objectRelatedModelsProvider.getRelatedModels(
						objectEntry.getGroupId(),
						objectRelationship.getObjectRelationshipId(),
						objectEntry.getPrimaryKey(),
						pagination.getStartPosition(),
						pagination.getEndPosition()),
				baseModel -> _toDTO(baseModel, objectEntry)));
	}

	@Override
	public ObjectEntry updateObjectEntry(
			DTOConverterContext dtoConverterContext,
			ObjectDefinition objectDefinition, long objectEntryId,
			ObjectEntry objectEntry)
		throws Exception {

		com.liferay.object.model.ObjectEntry serviceBuilderObjectEntry =
			_objectEntryService.getObjectEntry(objectEntryId);

		_checkObjectEntryObjectDefinitionId(
			objectDefinition, serviceBuilderObjectEntry);

		return _toObjectEntry(
			dtoConverterContext, objectDefinition,
			_objectEntryService.updateObjectEntry(
				objectEntryId,
				_toObjectValues(
					serviceBuilderObjectEntry.getObjectDefinitionId(),
					objectEntry.getProperties(),
					dtoConverterContext.getLocale()),
				_createServiceContext(
					objectEntry.getProperties(),
					dtoConverterContext.getUserId())));
	}

	private void _checkObjectEntryObjectDefinitionId(
			ObjectDefinition objectDefinition,
			com.liferay.object.model.ObjectEntry objectEntry)
		throws Exception {

		if (objectDefinition.getObjectDefinitionId() !=
				objectEntry.getObjectDefinitionId()) {

			throw new NoSuchObjectEntryException();
		}
	}

	private ServiceContext _createServiceContext(
		Map<String, Object> properties, long userId) {

		ServiceContext serviceContext = new ServiceContext();

		if (properties.get("categoryIds") != null) {
			serviceContext.setAssetCategoryIds(
				ListUtil.toLongArray(
					(List<String>)properties.get("categoryIds"),
					Long::parseLong));
		}

		if (properties.get("tagNames") != null) {
			serviceContext.setAssetTagNames(
				ArrayUtil.toStringArray(
					(List<String>)properties.get("tagNames")));
		}

		serviceContext.setUserId(userId);

		return serviceContext;
	}

	private String _getObjectEntriesPermissionName(long objectDefinitionId) {
		return ObjectConstants.RESOURCE_NAME + "#" + objectDefinitionId;
	}

	private ObjectEntry _getObjectEntry(
			DTOConverterContext dtoConverterContext,
			ObjectDefinition objectDefinition, Map<String, Serializable> values)
		throws Exception {

		com.liferay.object.model.ObjectEntry objectEntry =
			_objectEntryService.getObjectEntry(
				GetterUtil.getLong(
					values.get(objectDefinition.getPKObjectFieldName())));

		objectEntry.setValues(values);

		_checkObjectEntryObjectDefinitionId(objectDefinition, objectEntry);

		return _toObjectEntry(
			dtoConverterContext, objectDefinition, objectEntry);
	}

	private String _getObjectEntryPermissionName(long objectDefinitionId) {
		return ObjectDefinition.class.getName() + "#" + objectDefinitionId;
	}

	private ObjectDefinition _getRelatedObjectDefinition(
			ObjectDefinition objectDefinition,
			ObjectRelationship objectRelationship)
		throws Exception {

		long objectDefinitionId1 = objectRelationship.getObjectDefinitionId1();

		if (objectDefinitionId1 != objectDefinition.getObjectDefinitionId()) {
			return _objectDefinitionLocalService.getObjectDefinition(
				objectRelationship.getObjectDefinitionId1());
		}

		return _objectDefinitionLocalService.getObjectDefinition(
			objectRelationship.getObjectDefinitionId2());
	}

	private Page<ObjectEntry> _getSystemObjectRelatedObjectEntries(
			DTOConverterContext dtoConverterContext,
			ObjectDefinition objectDefinition, long objectEntryId,
			ObjectRelationship objectRelationship,
			ObjectRelatedModelsProvider objectRelatedModelsProvider,
			Pagination pagination)
		throws Exception {

		long groupId = GroupThreadLocal.getGroupId();

		SystemObjectDefinitionMetadata systemObjectDefinitionMetadata =
			_systemObjectDefinitionMetadataTracker.
				getSystemObjectDefinitionMetadata(objectDefinition.getName());

		AssetEntry assetEntry = _assetEntryLocalService.getEntry(
			systemObjectDefinitionMetadata.getModelClassName(), objectEntryId);

		if (Objects.equals(
				systemObjectDefinitionMetadata.getScope(),
				ObjectDefinitionConstants.SCOPE_SITE)) {

			groupId = assetEntry.getGroupId();
		}

		return Page.of(
			Collections.emptyMap(),
			_toObjectEntries(
				dtoConverterContext,
				objectRelatedModelsProvider.getRelatedModels(
					groupId, objectRelationship.getObjectRelationshipId(),
					assetEntry.getClassPK(), pagination.getStartPosition(),
					pagination.getEndPosition())));
	}

	private boolean _hasRelatedObjectEntries(
			String deletionType, ObjectDefinition objectDefinition,
			com.liferay.object.model.ObjectEntry objectEntry)
		throws PortalException {

		for (ObjectRelationship objectRelationship :
				_objectRelationshipLocalService.getObjectRelationships(
					objectDefinition.getObjectDefinitionId(), deletionType,
					false)) {

			ObjectRelatedModelsProvider objectRelatedModelsProvider =
				_objectRelatedModelsProviderRegistry.
					getObjectRelatedModelsProvider(
						objectDefinition.getClassName(),
						objectRelationship.getType());

			int count = objectRelatedModelsProvider.getRelatedModelsCount(
				objectEntry.getGroupId(),
				objectRelationship.getObjectRelationshipId(),
				objectEntry.getPrimaryKey());

			if (count > 0) {
				return true;
			}
		}

		return false;
	}

	private void _processVulcanAggregation(
		Aggregations aggregations, Queries queries,
		SearchRequestBuilder searchRequestBuilder,
		Aggregation vulcanAggregation) {

		if (vulcanAggregation == null) {
			return;
		}

		Map<String, String> aggregationTerms =
			vulcanAggregation.getAggregationTerms();

		for (Map.Entry<String, String> entry : aggregationTerms.entrySet()) {
			String value = entry.getValue();

			if (!value.startsWith("nestedFieldArray")) {
				continue;
			}

			NestedAggregation nestedAggregation = aggregations.nested(
				entry.getKey(), "nestedFieldArray");

			String[] valueParts = value.split(StringPool.POUND);

			FilterAggregation filterAggregation = aggregations.filter(
				"filterAggregation",
				queries.term("nestedFieldArray.fieldName", valueParts[1]));

			filterAggregation.addChildAggregation(
				aggregations.terms(entry.getKey(), valueParts[0]));

			nestedAggregation.addChildAggregation(filterAggregation);

			searchRequestBuilder.addAggregation(nestedAggregation);
		}
	}

	private Date _toDate(Locale locale, String valueString) {
		if (Validator.isNull(valueString)) {
			return null;
		}

		try {
			return DateUtil.parseDate(
				"yyyy-MM-dd'T'HH:mm:ss'Z'", valueString, locale);
		}
		catch (ParseException parseException1) {
			try {
				return DateUtil.parseDate("yyyy-MM-dd", valueString, locale);
			}
			catch (ParseException parseException2) {
				throw new BadRequestException(
					"Unable to parse date that does not conform to ISO-8601",
					parseException2);
			}
		}
	}

	private Object _toDTO(
			BaseModel<?> baseModel,
			com.liferay.object.model.ObjectEntry objectEntry)
		throws Exception {

		DTOConverter<BaseModel<?>, ?> dtoConverter =
			(DTOConverter<BaseModel<?>, ?>)
				_dtoConverterRegistry.getDTOConverter(
					baseModel.getModelClassName());

		if (dtoConverter == null) {
			throw new InternalServerErrorException(
				"No DTO converter found for " + baseModel.getModelClassName());
		}

		User user = _userLocalService.getUser(objectEntry.getUserId());

		DefaultDTOConverterContext defaultDTOConverterContext =
			new DefaultDTOConverterContext(
				false, Collections.emptyMap(), _dtoConverterRegistry,
				baseModel.getPrimaryKeyObj(), user.getLocale(), null, user);

		return dtoConverter.toDTO(defaultDTOConverterContext, baseModel);
	}

	private List<ObjectEntry> _toObjectEntries(
		DTOConverterContext dtoConverterContext,
		List<com.liferay.object.model.ObjectEntry> objectEntries) {

		return TransformUtil.transform(
			objectEntries,
			objectEntry -> _toObjectEntry(
				dtoConverterContext,
				_objectDefinitionLocalService.getObjectDefinition(
					objectEntry.getObjectDefinitionId()),
				objectEntry));
	}

	private ObjectEntry _toObjectEntry(
			DTOConverterContext dtoConverterContext,
			ObjectDefinition objectDefinition,
			com.liferay.object.model.ObjectEntry objectEntry)
		throws Exception {

		DefaultDTOConverterContext defaultDTOConverterContext =
			new DefaultDTOConverterContext(
				dtoConverterContext.isAcceptAllLanguages(),
				HashMapBuilder.put(
					"delete",
					() -> {
						if (_hasRelatedObjectEntries(
								ObjectRelationshipConstants.
									DELETION_TYPE_PREVENT,
								objectDefinition, objectEntry)) {

							return null;
						}

						return ActionUtil.addAction(
							ActionKeys.DELETE, ObjectEntryResourceImpl.class,
							objectEntry.getObjectEntryId(), "deleteObjectEntry",
							null, objectEntry.getUserId(),
							_getObjectEntryPermissionName(
								objectEntry.getObjectDefinitionId()),
							objectEntry.getGroupId(),
							dtoConverterContext.getUriInfo());
					}
				).put(
					"get",
					ActionUtil.addAction(
						ActionKeys.VIEW, ObjectEntryResourceImpl.class,
						objectEntry.getObjectEntryId(), "getObjectEntry", null,
						objectEntry.getUserId(),
						_getObjectEntryPermissionName(
							objectEntry.getObjectDefinitionId()),
						objectEntry.getGroupId(),
						dtoConverterContext.getUriInfo())
				).put(
					"permissions",
					ActionUtil.addAction(
						ActionKeys.PERMISSIONS, ObjectEntryResourceImpl.class,
						objectEntry.getObjectEntryId(), "patchObjectEntry",
						null, objectEntry.getUserId(),
						_getObjectEntryPermissionName(
							objectEntry.getObjectDefinitionId()),
						objectEntry.getGroupId(),
						dtoConverterContext.getUriInfo())
				).put(
					"update",
					ActionUtil.addAction(
						ActionKeys.UPDATE, ObjectEntryResourceImpl.class,
						objectEntry.getObjectEntryId(), "putObjectEntry", null,
						objectEntry.getUserId(),
						_getObjectEntryPermissionName(
							objectEntry.getObjectDefinitionId()),
						objectEntry.getGroupId(),
						dtoConverterContext.getUriInfo())
				).build(),
				dtoConverterContext.getDTOConverterRegistry(),
				dtoConverterContext.getHttpServletRequest(),
				objectEntry.getObjectEntryId(), dtoConverterContext.getLocale(),
				dtoConverterContext.getUriInfo(),
				dtoConverterContext.getUser());

		defaultDTOConverterContext.setAttribute(
			"objectDefinition", objectDefinition);

		return _objectEntryDTOConverter.toDTO(
			defaultDTOConverterContext, objectEntry);
	}

	private Map<String, Serializable> _toObjectValues(
		long objectDefinitionId, Map<String, Object> properties,
		Locale locale) {

		List<ObjectField> objectFields =
			_objectFieldLocalService.getObjectFields(objectDefinitionId);

		Map<String, Serializable> values = new HashMap<>();

		for (ObjectField objectField : objectFields) {
			String name = objectField.getName();

			Object object = properties.get(name);

			if ((object == null) && !objectField.isRequired()) {
				continue;
			}

			if (Objects.equals(
					objectField.getDBType(),
					ObjectFieldConstants.DB_TYPE_DATE)) {

				values.put(name, _toDate(locale, String.valueOf(object)));

				continue;
			}

			if ((objectField.getListTypeDefinitionId() != 0) &&
				(object instanceof Map)) {

				Map<String, String> map = (HashMap<String, String>)object;

				values.put(name, map.get("key"));

				continue;
			}

			values.put(name, (Serializable)object);
		}

		return values;
	}

	private static final long _NONEXISTING_ACCOUNT_ENTRY_ID = -1;

	@Reference
	private AccountEntryLocalService _accountEntryLocalService;

	@Reference
	private Aggregations _aggregations;

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

	@Reference
	private FilterPredicateFactory _filterPredicateFactory;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectEntryDTOConverter _objectEntryDTOConverter;

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

	@Reference
	private ObjectEntryService _objectEntryService;

	@Reference
	private ObjectFieldLocalService _objectFieldLocalService;

	@Reference
	private ObjectRelatedModelsProviderRegistry
		_objectRelatedModelsProviderRegistry;

	@Reference
	private ObjectRelationshipLocalService _objectRelationshipLocalService;

	@Reference
	private ObjectRelationshipService _objectRelationshipService;

	@Reference
	private Queries _queries;

	@Reference
	private SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Reference
	private SystemObjectDefinitionMetadataTracker
		_systemObjectDefinitionMetadataTracker;

	@Reference
	private UserLocalService _userLocalService;

}