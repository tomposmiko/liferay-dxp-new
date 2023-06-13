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

package com.liferay.search.experiences.rest.internal.graphql.mutation.v1_0;

import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineExportTaskResource;
import com.liferay.portal.vulcan.batch.engine.resource.VulcanBatchEngineImportTaskResource;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.search.experiences.rest.dto.v1_0.EmbeddingProviderConfiguration;
import com.liferay.search.experiences.rest.dto.v1_0.EmbeddingProviderValidationResult;
import com.liferay.search.experiences.rest.dto.v1_0.SXPBlueprint;
import com.liferay.search.experiences.rest.dto.v1_0.SXPElement;
import com.liferay.search.experiences.rest.dto.v1_0.SearchResponse;
import com.liferay.search.experiences.rest.resource.v1_0.EmbeddingProviderValidationResultResource;
import com.liferay.search.experiences.rest.resource.v1_0.FieldMappingInfoResource;
import com.liferay.search.experiences.rest.resource.v1_0.KeywordQueryContributorResource;
import com.liferay.search.experiences.rest.resource.v1_0.ModelPrefilterContributorResource;
import com.liferay.search.experiences.rest.resource.v1_0.QueryPrefilterContributorResource;
import com.liferay.search.experiences.rest.resource.v1_0.SXPBlueprintResource;
import com.liferay.search.experiences.rest.resource.v1_0.SXPElementResource;
import com.liferay.search.experiences.rest.resource.v1_0.SXPParameterContributorDefinitionResource;
import com.liferay.search.experiences.rest.resource.v1_0.SearchIndexResource;
import com.liferay.search.experiences.rest.resource.v1_0.SearchResponseResource;
import com.liferay.search.experiences.rest.resource.v1_0.SearchableAssetNameResource;

import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
@Generated("")
public class Mutation {

	public static void
		setEmbeddingProviderValidationResultResourceComponentServiceObjects(
			ComponentServiceObjects<EmbeddingProviderValidationResultResource>
				embeddingProviderValidationResultResourceComponentServiceObjects) {

		_embeddingProviderValidationResultResourceComponentServiceObjects =
			embeddingProviderValidationResultResourceComponentServiceObjects;
	}

	public static void setFieldMappingInfoResourceComponentServiceObjects(
		ComponentServiceObjects<FieldMappingInfoResource>
			fieldMappingInfoResourceComponentServiceObjects) {

		_fieldMappingInfoResourceComponentServiceObjects =
			fieldMappingInfoResourceComponentServiceObjects;
	}

	public static void
		setKeywordQueryContributorResourceComponentServiceObjects(
			ComponentServiceObjects<KeywordQueryContributorResource>
				keywordQueryContributorResourceComponentServiceObjects) {

		_keywordQueryContributorResourceComponentServiceObjects =
			keywordQueryContributorResourceComponentServiceObjects;
	}

	public static void
		setModelPrefilterContributorResourceComponentServiceObjects(
			ComponentServiceObjects<ModelPrefilterContributorResource>
				modelPrefilterContributorResourceComponentServiceObjects) {

		_modelPrefilterContributorResourceComponentServiceObjects =
			modelPrefilterContributorResourceComponentServiceObjects;
	}

	public static void
		setQueryPrefilterContributorResourceComponentServiceObjects(
			ComponentServiceObjects<QueryPrefilterContributorResource>
				queryPrefilterContributorResourceComponentServiceObjects) {

		_queryPrefilterContributorResourceComponentServiceObjects =
			queryPrefilterContributorResourceComponentServiceObjects;
	}

	public static void setSXPBlueprintResourceComponentServiceObjects(
		ComponentServiceObjects<SXPBlueprintResource>
			sxpBlueprintResourceComponentServiceObjects) {

		_sxpBlueprintResourceComponentServiceObjects =
			sxpBlueprintResourceComponentServiceObjects;
	}

	public static void setSXPElementResourceComponentServiceObjects(
		ComponentServiceObjects<SXPElementResource>
			sxpElementResourceComponentServiceObjects) {

		_sxpElementResourceComponentServiceObjects =
			sxpElementResourceComponentServiceObjects;
	}

	public static void
		setSXPParameterContributorDefinitionResourceComponentServiceObjects(
			ComponentServiceObjects<SXPParameterContributorDefinitionResource>
				sxpParameterContributorDefinitionResourceComponentServiceObjects) {

		_sxpParameterContributorDefinitionResourceComponentServiceObjects =
			sxpParameterContributorDefinitionResourceComponentServiceObjects;
	}

	public static void setSearchIndexResourceComponentServiceObjects(
		ComponentServiceObjects<SearchIndexResource>
			searchIndexResourceComponentServiceObjects) {

		_searchIndexResourceComponentServiceObjects =
			searchIndexResourceComponentServiceObjects;
	}

	public static void setSearchResponseResourceComponentServiceObjects(
		ComponentServiceObjects<SearchResponseResource>
			searchResponseResourceComponentServiceObjects) {

		_searchResponseResourceComponentServiceObjects =
			searchResponseResourceComponentServiceObjects;
	}

	public static void setSearchableAssetNameResourceComponentServiceObjects(
		ComponentServiceObjects<SearchableAssetNameResource>
			searchableAssetNameResourceComponentServiceObjects) {

		_searchableAssetNameResourceComponentServiceObjects =
			searchableAssetNameResourceComponentServiceObjects;
	}

	@GraphQLField
	public EmbeddingProviderValidationResult
			createTextEmbeddingValidateProviderConfiguration(
				@GraphQLName("embeddingProviderConfiguration")
					EmbeddingProviderConfiguration
						embeddingProviderConfiguration)
		throws Exception {

		return _applyComponentServiceObjects(
			_embeddingProviderValidationResultResourceComponentServiceObjects,
			this::_populateResourceContext,
			embeddingProviderValidationResultResource ->
				embeddingProviderValidationResultResource.
					postTextEmbeddingValidateProviderConfiguration(
						embeddingProviderConfiguration));
	}

	@GraphQLField
	public Response createFieldMappingInfosPageExportBatch(
			@GraphQLName("external") Boolean external,
			@GraphQLName("indexName") String indexName,
			@GraphQLName("query") String query,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("contentType") String contentType,
			@GraphQLName("fieldNames") String fieldNames)
		throws Exception {

		return _applyComponentServiceObjects(
			_fieldMappingInfoResourceComponentServiceObjects,
			this::_populateResourceContext,
			fieldMappingInfoResource ->
				fieldMappingInfoResource.postFieldMappingInfosPageExportBatch(
					external, indexName, query, callbackURL, contentType,
					fieldNames));
	}

	@GraphQLField
	public Response createKeywordQueryContributorsPageExportBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("contentType") String contentType,
			@GraphQLName("fieldNames") String fieldNames)
		throws Exception {

		return _applyComponentServiceObjects(
			_keywordQueryContributorResourceComponentServiceObjects,
			this::_populateResourceContext,
			keywordQueryContributorResource ->
				keywordQueryContributorResource.
					postKeywordQueryContributorsPageExportBatch(
						callbackURL, contentType, fieldNames));
	}

	@GraphQLField
	public Response createModelPrefilterContributorsPageExportBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("contentType") String contentType,
			@GraphQLName("fieldNames") String fieldNames)
		throws Exception {

		return _applyComponentServiceObjects(
			_modelPrefilterContributorResourceComponentServiceObjects,
			this::_populateResourceContext,
			modelPrefilterContributorResource ->
				modelPrefilterContributorResource.
					postModelPrefilterContributorsPageExportBatch(
						callbackURL, contentType, fieldNames));
	}

	@GraphQLField
	public Response createQueryPrefilterContributorsPageExportBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("contentType") String contentType,
			@GraphQLName("fieldNames") String fieldNames)
		throws Exception {

		return _applyComponentServiceObjects(
			_queryPrefilterContributorResourceComponentServiceObjects,
			this::_populateResourceContext,
			queryPrefilterContributorResource ->
				queryPrefilterContributorResource.
					postQueryPrefilterContributorsPageExportBatch(
						callbackURL, contentType, fieldNames));
	}

	@GraphQLField
	public Response createSXPBlueprintsPageExportBatch(
			@GraphQLName("search") String search,
			@GraphQLName("filter") String filterString,
			@GraphQLName("sort") String sortsString,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("contentType") String contentType,
			@GraphQLName("fieldNames") String fieldNames)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpBlueprintResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpBlueprintResource ->
				sxpBlueprintResource.postSXPBlueprintsPageExportBatch(
					search,
					_filterBiFunction.apply(sxpBlueprintResource, filterString),
					_sortsBiFunction.apply(sxpBlueprintResource, sortsString),
					callbackURL, contentType, fieldNames));
	}

	@GraphQLField
	public SXPBlueprint createSXPBlueprint(
			@GraphQLName("sxpBlueprint") SXPBlueprint sxpBlueprint)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpBlueprintResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpBlueprintResource -> sxpBlueprintResource.postSXPBlueprint(
				sxpBlueprint));
	}

	@GraphQLField
	public Response createSXPBlueprintBatch(
			@GraphQLName("sxpBlueprint") SXPBlueprint sxpBlueprint,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpBlueprintResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpBlueprintResource -> sxpBlueprintResource.postSXPBlueprintBatch(
				sxpBlueprint, callbackURL, object));
	}

	@GraphQLField
	public SXPBlueprint createSXPBlueprintValidate(
			@GraphQLName("string") String string)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpBlueprintResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpBlueprintResource ->
				sxpBlueprintResource.postSXPBlueprintValidate(string));
	}

	@GraphQLField
	public boolean deleteSXPBlueprint(
			@GraphQLName("sxpBlueprintId") Long sxpBlueprintId)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_sxpBlueprintResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpBlueprintResource -> sxpBlueprintResource.deleteSXPBlueprint(
				sxpBlueprintId));

		return true;
	}

	@GraphQLField
	public Response deleteSXPBlueprintBatch(
			@GraphQLName("sxpBlueprintId") Long sxpBlueprintId,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpBlueprintResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpBlueprintResource ->
				sxpBlueprintResource.deleteSXPBlueprintBatch(
					sxpBlueprintId, callbackURL, object));
	}

	@GraphQLField
	public SXPBlueprint patchSXPBlueprint(
			@GraphQLName("sxpBlueprintId") Long sxpBlueprintId,
			@GraphQLName("sxpBlueprint") SXPBlueprint sxpBlueprint)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpBlueprintResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpBlueprintResource -> sxpBlueprintResource.patchSXPBlueprint(
				sxpBlueprintId, sxpBlueprint));
	}

	@GraphQLField
	public SXPBlueprint createSXPBlueprintCopy(
			@GraphQLName("sxpBlueprintId") Long sxpBlueprintId)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpBlueprintResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpBlueprintResource -> sxpBlueprintResource.postSXPBlueprintCopy(
				sxpBlueprintId));
	}

	@GraphQLField
	public Response createSXPElementsPageExportBatch(
			@GraphQLName("search") String search,
			@GraphQLName("filter") String filterString,
			@GraphQLName("sort") String sortsString,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("contentType") String contentType,
			@GraphQLName("fieldNames") String fieldNames)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpElementResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpElementResource ->
				sxpElementResource.postSXPElementsPageExportBatch(
					search,
					_filterBiFunction.apply(sxpElementResource, filterString),
					_sortsBiFunction.apply(sxpElementResource, sortsString),
					callbackURL, contentType, fieldNames));
	}

	@GraphQLField
	public SXPElement createSXPElement(
			@GraphQLName("sxpElement") SXPElement sxpElement)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpElementResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpElementResource -> sxpElementResource.postSXPElement(
				sxpElement));
	}

	@GraphQLField
	public Response createSXPElementBatch(
			@GraphQLName("sxpElement") SXPElement sxpElement,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpElementResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpElementResource -> sxpElementResource.postSXPElementBatch(
				sxpElement, callbackURL, object));
	}

	@GraphQLField
	public SXPElement createSXPElementValidate(
			@GraphQLName("string") String string)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpElementResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpElementResource -> sxpElementResource.postSXPElementValidate(
				string));
	}

	@GraphQLField
	public boolean deleteSXPElement(
			@GraphQLName("sxpElementId") Long sxpElementId)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_sxpElementResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpElementResource -> sxpElementResource.deleteSXPElement(
				sxpElementId));

		return true;
	}

	@GraphQLField
	public Response deleteSXPElementBatch(
			@GraphQLName("sxpElementId") Long sxpElementId,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpElementResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpElementResource -> sxpElementResource.deleteSXPElementBatch(
				sxpElementId, callbackURL, object));
	}

	@GraphQLField
	public SXPElement patchSXPElement(
			@GraphQLName("sxpElementId") Long sxpElementId,
			@GraphQLName("sxpElement") SXPElement sxpElement)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpElementResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpElementResource -> sxpElementResource.patchSXPElement(
				sxpElementId, sxpElement));
	}

	@GraphQLField
	public SXPElement createSXPElementCopy(
			@GraphQLName("sxpElementId") Long sxpElementId)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpElementResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpElementResource -> sxpElementResource.postSXPElementCopy(
				sxpElementId));
	}

	@GraphQLField
	public Response createSXPParameterContributorDefinitionsPageExportBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("contentType") String contentType,
			@GraphQLName("fieldNames") String fieldNames)
		throws Exception {

		return _applyComponentServiceObjects(
			_sxpParameterContributorDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			sxpParameterContributorDefinitionResource ->
				sxpParameterContributorDefinitionResource.
					postSXPParameterContributorDefinitionsPageExportBatch(
						callbackURL, contentType, fieldNames));
	}

	@GraphQLField
	public Response createSearchIndexesPageExportBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("contentType") String contentType,
			@GraphQLName("fieldNames") String fieldNames)
		throws Exception {

		return _applyComponentServiceObjects(
			_searchIndexResourceComponentServiceObjects,
			this::_populateResourceContext,
			searchIndexResource ->
				searchIndexResource.postSearchIndexesPageExportBatch(
					callbackURL, contentType, fieldNames));
	}

	@GraphQLField
	public SearchResponse createSearch(
			@GraphQLName("query") String query,
			@GraphQLName("pageSize") int pageSize,
			@GraphQLName("page") int page,
			@GraphQLName("sxpBlueprint") SXPBlueprint sxpBlueprint)
		throws Exception {

		return _applyComponentServiceObjects(
			_searchResponseResourceComponentServiceObjects,
			this::_populateResourceContext,
			searchResponseResource -> searchResponseResource.postSearch(
				query, Pagination.of(page, pageSize), sxpBlueprint));
	}

	@GraphQLField
	public Response createSearchableAssetNamesPageExportBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("contentType") String contentType,
			@GraphQLName("fieldNames") String fieldNames)
		throws Exception {

		return _applyComponentServiceObjects(
			_searchableAssetNameResourceComponentServiceObjects,
			this::_populateResourceContext,
			searchableAssetNameResource ->
				searchableAssetNameResource.
					postSearchableAssetNamesPageExportBatch(
						callbackURL, contentType, fieldNames));
	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private <T, E1 extends Throwable, E2 extends Throwable> void
			_applyVoidComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeConsumer<T, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			unsafeFunction.accept(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(
			EmbeddingProviderValidationResultResource
				embeddingProviderValidationResultResource)
		throws Exception {

		embeddingProviderValidationResultResource.setContextAcceptLanguage(
			_acceptLanguage);
		embeddingProviderValidationResultResource.setContextCompany(_company);
		embeddingProviderValidationResultResource.setContextHttpServletRequest(
			_httpServletRequest);
		embeddingProviderValidationResultResource.setContextHttpServletResponse(
			_httpServletResponse);
		embeddingProviderValidationResultResource.setContextUriInfo(_uriInfo);
		embeddingProviderValidationResultResource.setContextUser(_user);
		embeddingProviderValidationResultResource.setGroupLocalService(
			_groupLocalService);
		embeddingProviderValidationResultResource.setRoleLocalService(
			_roleLocalService);
	}

	private void _populateResourceContext(
			FieldMappingInfoResource fieldMappingInfoResource)
		throws Exception {

		fieldMappingInfoResource.setContextAcceptLanguage(_acceptLanguage);
		fieldMappingInfoResource.setContextCompany(_company);
		fieldMappingInfoResource.setContextHttpServletRequest(
			_httpServletRequest);
		fieldMappingInfoResource.setContextHttpServletResponse(
			_httpServletResponse);
		fieldMappingInfoResource.setContextUriInfo(_uriInfo);
		fieldMappingInfoResource.setContextUser(_user);
		fieldMappingInfoResource.setGroupLocalService(_groupLocalService);
		fieldMappingInfoResource.setRoleLocalService(_roleLocalService);

		fieldMappingInfoResource.setVulcanBatchEngineExportTaskResource(
			_vulcanBatchEngineExportTaskResource);

		fieldMappingInfoResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			KeywordQueryContributorResource keywordQueryContributorResource)
		throws Exception {

		keywordQueryContributorResource.setContextAcceptLanguage(
			_acceptLanguage);
		keywordQueryContributorResource.setContextCompany(_company);
		keywordQueryContributorResource.setContextHttpServletRequest(
			_httpServletRequest);
		keywordQueryContributorResource.setContextHttpServletResponse(
			_httpServletResponse);
		keywordQueryContributorResource.setContextUriInfo(_uriInfo);
		keywordQueryContributorResource.setContextUser(_user);
		keywordQueryContributorResource.setGroupLocalService(
			_groupLocalService);
		keywordQueryContributorResource.setRoleLocalService(_roleLocalService);

		keywordQueryContributorResource.setVulcanBatchEngineExportTaskResource(
			_vulcanBatchEngineExportTaskResource);

		keywordQueryContributorResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			ModelPrefilterContributorResource modelPrefilterContributorResource)
		throws Exception {

		modelPrefilterContributorResource.setContextAcceptLanguage(
			_acceptLanguage);
		modelPrefilterContributorResource.setContextCompany(_company);
		modelPrefilterContributorResource.setContextHttpServletRequest(
			_httpServletRequest);
		modelPrefilterContributorResource.setContextHttpServletResponse(
			_httpServletResponse);
		modelPrefilterContributorResource.setContextUriInfo(_uriInfo);
		modelPrefilterContributorResource.setContextUser(_user);
		modelPrefilterContributorResource.setGroupLocalService(
			_groupLocalService);
		modelPrefilterContributorResource.setRoleLocalService(
			_roleLocalService);

		modelPrefilterContributorResource.
			setVulcanBatchEngineExportTaskResource(
				_vulcanBatchEngineExportTaskResource);

		modelPrefilterContributorResource.
			setVulcanBatchEngineImportTaskResource(
				_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			QueryPrefilterContributorResource queryPrefilterContributorResource)
		throws Exception {

		queryPrefilterContributorResource.setContextAcceptLanguage(
			_acceptLanguage);
		queryPrefilterContributorResource.setContextCompany(_company);
		queryPrefilterContributorResource.setContextHttpServletRequest(
			_httpServletRequest);
		queryPrefilterContributorResource.setContextHttpServletResponse(
			_httpServletResponse);
		queryPrefilterContributorResource.setContextUriInfo(_uriInfo);
		queryPrefilterContributorResource.setContextUser(_user);
		queryPrefilterContributorResource.setGroupLocalService(
			_groupLocalService);
		queryPrefilterContributorResource.setRoleLocalService(
			_roleLocalService);

		queryPrefilterContributorResource.
			setVulcanBatchEngineExportTaskResource(
				_vulcanBatchEngineExportTaskResource);

		queryPrefilterContributorResource.
			setVulcanBatchEngineImportTaskResource(
				_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			SXPBlueprintResource sxpBlueprintResource)
		throws Exception {

		sxpBlueprintResource.setContextAcceptLanguage(_acceptLanguage);
		sxpBlueprintResource.setContextCompany(_company);
		sxpBlueprintResource.setContextHttpServletRequest(_httpServletRequest);
		sxpBlueprintResource.setContextHttpServletResponse(
			_httpServletResponse);
		sxpBlueprintResource.setContextUriInfo(_uriInfo);
		sxpBlueprintResource.setContextUser(_user);
		sxpBlueprintResource.setGroupLocalService(_groupLocalService);
		sxpBlueprintResource.setRoleLocalService(_roleLocalService);

		sxpBlueprintResource.setVulcanBatchEngineExportTaskResource(
			_vulcanBatchEngineExportTaskResource);

		sxpBlueprintResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(SXPElementResource sxpElementResource)
		throws Exception {

		sxpElementResource.setContextAcceptLanguage(_acceptLanguage);
		sxpElementResource.setContextCompany(_company);
		sxpElementResource.setContextHttpServletRequest(_httpServletRequest);
		sxpElementResource.setContextHttpServletResponse(_httpServletResponse);
		sxpElementResource.setContextUriInfo(_uriInfo);
		sxpElementResource.setContextUser(_user);
		sxpElementResource.setGroupLocalService(_groupLocalService);
		sxpElementResource.setRoleLocalService(_roleLocalService);

		sxpElementResource.setVulcanBatchEngineExportTaskResource(
			_vulcanBatchEngineExportTaskResource);

		sxpElementResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			SXPParameterContributorDefinitionResource
				sxpParameterContributorDefinitionResource)
		throws Exception {

		sxpParameterContributorDefinitionResource.setContextAcceptLanguage(
			_acceptLanguage);
		sxpParameterContributorDefinitionResource.setContextCompany(_company);
		sxpParameterContributorDefinitionResource.setContextHttpServletRequest(
			_httpServletRequest);
		sxpParameterContributorDefinitionResource.setContextHttpServletResponse(
			_httpServletResponse);
		sxpParameterContributorDefinitionResource.setContextUriInfo(_uriInfo);
		sxpParameterContributorDefinitionResource.setContextUser(_user);
		sxpParameterContributorDefinitionResource.setGroupLocalService(
			_groupLocalService);
		sxpParameterContributorDefinitionResource.setRoleLocalService(
			_roleLocalService);

		sxpParameterContributorDefinitionResource.
			setVulcanBatchEngineExportTaskResource(
				_vulcanBatchEngineExportTaskResource);

		sxpParameterContributorDefinitionResource.
			setVulcanBatchEngineImportTaskResource(
				_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			SearchIndexResource searchIndexResource)
		throws Exception {

		searchIndexResource.setContextAcceptLanguage(_acceptLanguage);
		searchIndexResource.setContextCompany(_company);
		searchIndexResource.setContextHttpServletRequest(_httpServletRequest);
		searchIndexResource.setContextHttpServletResponse(_httpServletResponse);
		searchIndexResource.setContextUriInfo(_uriInfo);
		searchIndexResource.setContextUser(_user);
		searchIndexResource.setGroupLocalService(_groupLocalService);
		searchIndexResource.setRoleLocalService(_roleLocalService);

		searchIndexResource.setVulcanBatchEngineExportTaskResource(
			_vulcanBatchEngineExportTaskResource);

		searchIndexResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private void _populateResourceContext(
			SearchResponseResource searchResponseResource)
		throws Exception {

		searchResponseResource.setContextAcceptLanguage(_acceptLanguage);
		searchResponseResource.setContextCompany(_company);
		searchResponseResource.setContextHttpServletRequest(
			_httpServletRequest);
		searchResponseResource.setContextHttpServletResponse(
			_httpServletResponse);
		searchResponseResource.setContextUriInfo(_uriInfo);
		searchResponseResource.setContextUser(_user);
		searchResponseResource.setGroupLocalService(_groupLocalService);
		searchResponseResource.setRoleLocalService(_roleLocalService);
	}

	private void _populateResourceContext(
			SearchableAssetNameResource searchableAssetNameResource)
		throws Exception {

		searchableAssetNameResource.setContextAcceptLanguage(_acceptLanguage);
		searchableAssetNameResource.setContextCompany(_company);
		searchableAssetNameResource.setContextHttpServletRequest(
			_httpServletRequest);
		searchableAssetNameResource.setContextHttpServletResponse(
			_httpServletResponse);
		searchableAssetNameResource.setContextUriInfo(_uriInfo);
		searchableAssetNameResource.setContextUser(_user);
		searchableAssetNameResource.setGroupLocalService(_groupLocalService);
		searchableAssetNameResource.setRoleLocalService(_roleLocalService);

		searchableAssetNameResource.setVulcanBatchEngineExportTaskResource(
			_vulcanBatchEngineExportTaskResource);

		searchableAssetNameResource.setVulcanBatchEngineImportTaskResource(
			_vulcanBatchEngineImportTaskResource);
	}

	private static ComponentServiceObjects
		<EmbeddingProviderValidationResultResource>
			_embeddingProviderValidationResultResourceComponentServiceObjects;
	private static ComponentServiceObjects<FieldMappingInfoResource>
		_fieldMappingInfoResourceComponentServiceObjects;
	private static ComponentServiceObjects<KeywordQueryContributorResource>
		_keywordQueryContributorResourceComponentServiceObjects;
	private static ComponentServiceObjects<ModelPrefilterContributorResource>
		_modelPrefilterContributorResourceComponentServiceObjects;
	private static ComponentServiceObjects<QueryPrefilterContributorResource>
		_queryPrefilterContributorResourceComponentServiceObjects;
	private static ComponentServiceObjects<SXPBlueprintResource>
		_sxpBlueprintResourceComponentServiceObjects;
	private static ComponentServiceObjects<SXPElementResource>
		_sxpElementResourceComponentServiceObjects;
	private static ComponentServiceObjects
		<SXPParameterContributorDefinitionResource>
			_sxpParameterContributorDefinitionResourceComponentServiceObjects;
	private static ComponentServiceObjects<SearchIndexResource>
		_searchIndexResourceComponentServiceObjects;
	private static ComponentServiceObjects<SearchResponseResource>
		_searchResponseResourceComponentServiceObjects;
	private static ComponentServiceObjects<SearchableAssetNameResource>
		_searchableAssetNameResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private BiFunction<Object, String, Filter> _filterBiFunction;
	private GroupLocalService _groupLocalService;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private RoleLocalService _roleLocalService;
	private BiFunction<Object, String, Sort[]> _sortsBiFunction;
	private UriInfo _uriInfo;
	private com.liferay.portal.kernel.model.User _user;
	private VulcanBatchEngineExportTaskResource
		_vulcanBatchEngineExportTaskResource;
	private VulcanBatchEngineImportTaskResource
		_vulcanBatchEngineImportTaskResource;

}