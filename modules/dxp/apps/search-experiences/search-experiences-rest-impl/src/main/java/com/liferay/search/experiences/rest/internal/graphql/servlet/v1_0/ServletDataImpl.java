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

package com.liferay.search.experiences.rest.internal.graphql.servlet.v1_0;

import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.vulcan.graphql.servlet.ServletData;
import com.liferay.search.experiences.rest.internal.graphql.mutation.v1_0.Mutation;
import com.liferay.search.experiences.rest.internal.graphql.query.v1_0.Query;
import com.liferay.search.experiences.rest.internal.resource.v1_0.EmbeddingProviderValidationResultResourceImpl;
import com.liferay.search.experiences.rest.internal.resource.v1_0.FieldMappingInfoResourceImpl;
import com.liferay.search.experiences.rest.internal.resource.v1_0.KeywordQueryContributorResourceImpl;
import com.liferay.search.experiences.rest.internal.resource.v1_0.MLModelResourceImpl;
import com.liferay.search.experiences.rest.internal.resource.v1_0.ModelPrefilterContributorResourceImpl;
import com.liferay.search.experiences.rest.internal.resource.v1_0.QueryPrefilterContributorResourceImpl;
import com.liferay.search.experiences.rest.internal.resource.v1_0.SXPBlueprintResourceImpl;
import com.liferay.search.experiences.rest.internal.resource.v1_0.SXPElementResourceImpl;
import com.liferay.search.experiences.rest.internal.resource.v1_0.SXPParameterContributorDefinitionResourceImpl;
import com.liferay.search.experiences.rest.internal.resource.v1_0.SearchIndexResourceImpl;
import com.liferay.search.experiences.rest.internal.resource.v1_0.SearchResponseResourceImpl;
import com.liferay.search.experiences.rest.internal.resource.v1_0.SearchableAssetNameDisplayResourceImpl;
import com.liferay.search.experiences.rest.internal.resource.v1_0.SearchableAssetNameResourceImpl;
import com.liferay.search.experiences.rest.resource.v1_0.EmbeddingProviderValidationResultResource;
import com.liferay.search.experiences.rest.resource.v1_0.FieldMappingInfoResource;
import com.liferay.search.experiences.rest.resource.v1_0.KeywordQueryContributorResource;
import com.liferay.search.experiences.rest.resource.v1_0.MLModelResource;
import com.liferay.search.experiences.rest.resource.v1_0.ModelPrefilterContributorResource;
import com.liferay.search.experiences.rest.resource.v1_0.QueryPrefilterContributorResource;
import com.liferay.search.experiences.rest.resource.v1_0.SXPBlueprintResource;
import com.liferay.search.experiences.rest.resource.v1_0.SXPElementResource;
import com.liferay.search.experiences.rest.resource.v1_0.SXPParameterContributorDefinitionResource;
import com.liferay.search.experiences.rest.resource.v1_0.SearchIndexResource;
import com.liferay.search.experiences.rest.resource.v1_0.SearchResponseResource;
import com.liferay.search.experiences.rest.resource.v1_0.SearchableAssetNameDisplayResource;
import com.liferay.search.experiences.rest.resource.v1_0.SearchableAssetNameResource;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = ServletData.class)
@Generated("")
public class ServletDataImpl implements ServletData {

	@Activate
	public void activate(BundleContext bundleContext) {
		Mutation.
			setEmbeddingProviderValidationResultResourceComponentServiceObjects(
				_embeddingProviderValidationResultResourceComponentServiceObjects);
		Mutation.setFieldMappingInfoResourceComponentServiceObjects(
			_fieldMappingInfoResourceComponentServiceObjects);
		Mutation.setKeywordQueryContributorResourceComponentServiceObjects(
			_keywordQueryContributorResourceComponentServiceObjects);
		Mutation.setModelPrefilterContributorResourceComponentServiceObjects(
			_modelPrefilterContributorResourceComponentServiceObjects);
		Mutation.setQueryPrefilterContributorResourceComponentServiceObjects(
			_queryPrefilterContributorResourceComponentServiceObjects);
		Mutation.setSXPBlueprintResourceComponentServiceObjects(
			_sxpBlueprintResourceComponentServiceObjects);
		Mutation.setSXPElementResourceComponentServiceObjects(
			_sxpElementResourceComponentServiceObjects);
		Mutation.
			setSXPParameterContributorDefinitionResourceComponentServiceObjects(
				_sxpParameterContributorDefinitionResourceComponentServiceObjects);
		Mutation.setSearchIndexResourceComponentServiceObjects(
			_searchIndexResourceComponentServiceObjects);
		Mutation.setSearchResponseResourceComponentServiceObjects(
			_searchResponseResourceComponentServiceObjects);
		Mutation.setSearchableAssetNameResourceComponentServiceObjects(
			_searchableAssetNameResourceComponentServiceObjects);

		Query.setFieldMappingInfoResourceComponentServiceObjects(
			_fieldMappingInfoResourceComponentServiceObjects);
		Query.setKeywordQueryContributorResourceComponentServiceObjects(
			_keywordQueryContributorResourceComponentServiceObjects);
		Query.setMLModelResourceComponentServiceObjects(
			_mlModelResourceComponentServiceObjects);
		Query.setModelPrefilterContributorResourceComponentServiceObjects(
			_modelPrefilterContributorResourceComponentServiceObjects);
		Query.setQueryPrefilterContributorResourceComponentServiceObjects(
			_queryPrefilterContributorResourceComponentServiceObjects);
		Query.setSXPBlueprintResourceComponentServiceObjects(
			_sxpBlueprintResourceComponentServiceObjects);
		Query.setSXPElementResourceComponentServiceObjects(
			_sxpElementResourceComponentServiceObjects);
		Query.
			setSXPParameterContributorDefinitionResourceComponentServiceObjects(
				_sxpParameterContributorDefinitionResourceComponentServiceObjects);
		Query.setSearchIndexResourceComponentServiceObjects(
			_searchIndexResourceComponentServiceObjects);
		Query.setSearchableAssetNameResourceComponentServiceObjects(
			_searchableAssetNameResourceComponentServiceObjects);
		Query.setSearchableAssetNameDisplayResourceComponentServiceObjects(
			_searchableAssetNameDisplayResourceComponentServiceObjects);
	}

	public String getApplicationName() {
		return "Liferay.Search.Experiences.REST";
	}

	@Override
	public Mutation getMutation() {
		return new Mutation();
	}

	@Override
	public String getPath() {
		return "/search-experiences-rest-graphql/v1_0";
	}

	@Override
	public Query getQuery() {
		return new Query();
	}

	public ObjectValuePair<Class<?>, String> getResourceMethodObjectValuePair(
		String methodName, boolean mutation) {

		if (mutation) {
			return _resourceMethodObjectValuePairs.get(
				"mutation#" + methodName);
		}

		return _resourceMethodObjectValuePairs.get("query#" + methodName);
	}

	private static final Map<String, ObjectValuePair<Class<?>, String>>
		_resourceMethodObjectValuePairs =
			new HashMap<String, ObjectValuePair<Class<?>, String>>() {
				{
					put(
						"mutation#createTextEmbeddingValidateProviderConfiguration",
						new ObjectValuePair<>(
							EmbeddingProviderValidationResultResourceImpl.class,
							"postTextEmbeddingValidateProviderConfiguration"));
					put(
						"mutation#createFieldMappingInfosPageExportBatch",
						new ObjectValuePair<>(
							FieldMappingInfoResourceImpl.class,
							"postFieldMappingInfosPageExportBatch"));
					put(
						"mutation#createKeywordQueryContributorsPageExportBatch",
						new ObjectValuePair<>(
							KeywordQueryContributorResourceImpl.class,
							"postKeywordQueryContributorsPageExportBatch"));
					put(
						"mutation#createModelPrefilterContributorsPageExportBatch",
						new ObjectValuePair<>(
							ModelPrefilterContributorResourceImpl.class,
							"postModelPrefilterContributorsPageExportBatch"));
					put(
						"mutation#createQueryPrefilterContributorsPageExportBatch",
						new ObjectValuePair<>(
							QueryPrefilterContributorResourceImpl.class,
							"postQueryPrefilterContributorsPageExportBatch"));
					put(
						"mutation#createSXPBlueprintsPageExportBatch",
						new ObjectValuePair<>(
							SXPBlueprintResourceImpl.class,
							"postSXPBlueprintsPageExportBatch"));
					put(
						"mutation#createSXPBlueprint",
						new ObjectValuePair<>(
							SXPBlueprintResourceImpl.class,
							"postSXPBlueprint"));
					put(
						"mutation#createSXPBlueprintBatch",
						new ObjectValuePair<>(
							SXPBlueprintResourceImpl.class,
							"postSXPBlueprintBatch"));
					put(
						"mutation#createSXPBlueprintValidate",
						new ObjectValuePair<>(
							SXPBlueprintResourceImpl.class,
							"postSXPBlueprintValidate"));
					put(
						"mutation#deleteSXPBlueprint",
						new ObjectValuePair<>(
							SXPBlueprintResourceImpl.class,
							"deleteSXPBlueprint"));
					put(
						"mutation#deleteSXPBlueprintBatch",
						new ObjectValuePair<>(
							SXPBlueprintResourceImpl.class,
							"deleteSXPBlueprintBatch"));
					put(
						"mutation#patchSXPBlueprint",
						new ObjectValuePair<>(
							SXPBlueprintResourceImpl.class,
							"patchSXPBlueprint"));
					put(
						"mutation#createSXPBlueprintCopy",
						new ObjectValuePair<>(
							SXPBlueprintResourceImpl.class,
							"postSXPBlueprintCopy"));
					put(
						"mutation#createSXPElementsPageExportBatch",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class,
							"postSXPElementsPageExportBatch"));
					put(
						"mutation#createSXPElement",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class, "postSXPElement"));
					put(
						"mutation#createSXPElementBatch",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class,
							"postSXPElementBatch"));
					put(
						"mutation#createSXPElementValidate",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class,
							"postSXPElementValidate"));
					put(
						"mutation#deleteSXPElement",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class, "deleteSXPElement"));
					put(
						"mutation#deleteSXPElementBatch",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class,
							"deleteSXPElementBatch"));
					put(
						"mutation#patchSXPElement",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class, "patchSXPElement"));
					put(
						"mutation#createSXPElementCopy",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class,
							"postSXPElementCopy"));
					put(
						"mutation#createSXPParameterContributorDefinitionsPageExportBatch",
						new ObjectValuePair<>(
							SXPParameterContributorDefinitionResourceImpl.class,
							"postSXPParameterContributorDefinitionsPageExportBatch"));
					put(
						"mutation#createSearchIndexesPageExportBatch",
						new ObjectValuePair<>(
							SearchIndexResourceImpl.class,
							"postSearchIndexesPageExportBatch"));
					put(
						"mutation#createSearch",
						new ObjectValuePair<>(
							SearchResponseResourceImpl.class, "postSearch"));
					put(
						"mutation#createSearchableAssetNamesPageExportBatch",
						new ObjectValuePair<>(
							SearchableAssetNameResourceImpl.class,
							"postSearchableAssetNamesPageExportBatch"));

					put(
						"query#fieldMappingInfos",
						new ObjectValuePair<>(
							FieldMappingInfoResourceImpl.class,
							"getFieldMappingInfosPage"));
					put(
						"query#keywordQueryContributors",
						new ObjectValuePair<>(
							KeywordQueryContributorResourceImpl.class,
							"getKeywordQueryContributorsPage"));
					put(
						"query#sentenceTransformerMLModels",
						new ObjectValuePair<>(
							MLModelResourceImpl.class,
							"getSentenceTransformerMLModelsPage"));
					put(
						"query#modelPrefilterContributors",
						new ObjectValuePair<>(
							ModelPrefilterContributorResourceImpl.class,
							"getModelPrefilterContributorsPage"));
					put(
						"query#queryPrefilterContributors",
						new ObjectValuePair<>(
							QueryPrefilterContributorResourceImpl.class,
							"getQueryPrefilterContributorsPage"));
					put(
						"query#sXPBlueprints",
						new ObjectValuePair<>(
							SXPBlueprintResourceImpl.class,
							"getSXPBlueprintsPage"));
					put(
						"query#sXPBlueprint",
						new ObjectValuePair<>(
							SXPBlueprintResourceImpl.class, "getSXPBlueprint"));
					put(
						"query#sXPBlueprintExport",
						new ObjectValuePair<>(
							SXPBlueprintResourceImpl.class,
							"getSXPBlueprintExport"));
					put(
						"query#sXPElements",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class,
							"getSXPElementsPage"));
					put(
						"query#sXPElement",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class, "getSXPElement"));
					put(
						"query#sXPElementExport",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class,
							"getSXPElementExport"));
					put(
						"query#sXPParameterContributorDefinitions",
						new ObjectValuePair<>(
							SXPParameterContributorDefinitionResourceImpl.class,
							"getSXPParameterContributorDefinitionsPage"));
					put(
						"query#searchIndexes",
						new ObjectValuePair<>(
							SearchIndexResourceImpl.class,
							"getSearchIndexesPage"));
					put(
						"query#searchableAssetNames",
						new ObjectValuePair<>(
							SearchableAssetNameResourceImpl.class,
							"getSearchableAssetNamesPage"));
					put(
						"query#searchableAssetNameLanguage",
						new ObjectValuePair<>(
							SearchableAssetNameDisplayResourceImpl.class,
							"getSearchableAssetNameLanguagePage"));

					put(
						"query#SXPElement.export",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class,
							"getSXPElementExport"));
					put(
						"query#ElementInstance.sXPElement",
						new ObjectValuePair<>(
							SXPElementResourceImpl.class, "getSXPElement"));
					put(
						"query#SXPBlueprint.export",
						new ObjectValuePair<>(
							SXPBlueprintResourceImpl.class,
							"getSXPBlueprintExport"));
				}
			};

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<EmbeddingProviderValidationResultResource>
		_embeddingProviderValidationResultResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<FieldMappingInfoResource>
		_fieldMappingInfoResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<KeywordQueryContributorResource>
		_keywordQueryContributorResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<ModelPrefilterContributorResource>
		_modelPrefilterContributorResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<QueryPrefilterContributorResource>
		_queryPrefilterContributorResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SXPBlueprintResource>
		_sxpBlueprintResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SXPElementResource>
		_sxpElementResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SXPParameterContributorDefinitionResource>
		_sxpParameterContributorDefinitionResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SearchIndexResource>
		_searchIndexResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SearchResponseResource>
		_searchResponseResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SearchableAssetNameResource>
		_searchableAssetNameResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<MLModelResource>
		_mlModelResourceComponentServiceObjects;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private ComponentServiceObjects<SearchableAssetNameDisplayResource>
		_searchableAssetNameDisplayResourceComponentServiceObjects;

}