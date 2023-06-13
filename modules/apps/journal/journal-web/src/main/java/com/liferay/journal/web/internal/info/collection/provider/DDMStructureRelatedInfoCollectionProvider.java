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

package com.liferay.journal.web.internal.info.collection.provider;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.depot.util.SiteConnectedGroupGroupProviderUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.info.collection.provider.CollectionQuery;
import com.liferay.info.collection.provider.RelatedInfoItemCollectionProvider;
import com.liferay.info.collection.provider.SingleFormVariationInfoCollectionProvider;
import com.liferay.info.pagination.InfoPage;
import com.liferay.info.pagination.Pagination;
import com.liferay.info.sort.Sort;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.journal.web.internal.util.JournalSearcherUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * @author Jürgen Kappler
 */
public class DDMStructureRelatedInfoCollectionProvider
	implements RelatedInfoItemCollectionProvider<AssetCategory, JournalArticle>,
			   SingleFormVariationInfoCollectionProvider<JournalArticle> {

	public DDMStructureRelatedInfoCollectionProvider(
		DDMStructure ddmStructure,
		JournalArticleLocalService journalArticleLocalService) {

		_ddmStructure = ddmStructure;
		_journalArticleLocalService = journalArticleLocalService;
	}

	@Override
	public InfoPage<JournalArticle> getCollectionInfoPage(
		CollectionQuery collectionQuery) {

		Optional<Object> relatedItemOptional =
			collectionQuery.getRelatedItemObjectOptional();

		Object relatedItem = relatedItemOptional.orElse(null);

		if (!(relatedItem instanceof AssetCategory)) {
			return InfoPage.of(
				Collections.emptyList(), collectionQuery.getPagination(), 0);
		}

		List<JournalArticle> articles =
			JournalSearcherUtil.searchJournalArticles(
				searchContext -> _populateSearchContext(
					(AssetCategory)relatedItem, collectionQuery,
					searchContext));

		return InfoPage.of(
			articles, collectionQuery.getPagination(), articles.size());
	}

	@Override
	public String getCollectionItemClassName() {
		return JournalArticle.class.getName();
	}

	@Override
	public String getFormVariationKey() {
		return String.valueOf(_ddmStructure.getStructureId());
	}

	@Override
	public String getKey() {
		StringBundler sb = new StringBundler(5);

		sb.append(RelatedInfoItemCollectionProvider.class.getName());
		sb.append(StringPool.DASH);
		sb.append(JournalArticle.class.getName());
		sb.append(StringPool.DASH);
		sb.append(_ddmStructure.getStructureId());

		return sb.toString();
	}

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(locale, _ddmStructure.getName(locale));
	}

	@Override
	public String getSourceItemClassName() {
		return AssetCategory.class.getName();
	}

	@Override
	public boolean isAvailable() {
		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		try {
			if (ArrayUtil.contains(
					SiteConnectedGroupGroupProviderUtil.
						getCurrentAndAncestorSiteAndDepotGroupIds(
							serviceContext.getScopeGroupId(), true),
					_ddmStructure.getGroupId())) {

				return true;
			}
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}

		return false;
	}

	private SearchContext _populateSearchContext(
		AssetCategory assetCategory, CollectionQuery collectionQuery,
		SearchContext searchContext) {

		searchContext.setAndSearch(true);
		searchContext.setAssetCategoryIds(
			new long[] {assetCategory.getCategoryId()});

		Map<String, Serializable> attributes = searchContext.getAttributes();

		attributes.put(Field.STATUS, WorkflowConstants.STATUS_APPROVED);
		attributes.put("ddmStructureKey", _ddmStructure.getStructureKey());
		attributes.put("head", true);
		attributes.put("latest", true);

		searchContext.setAttributes(attributes);

		searchContext.setClassTypeIds(
			new long[] {_ddmStructure.getStructureId()});

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		searchContext.setCompanyId(serviceContext.getCompanyId());

		Pagination pagination = collectionQuery.getPagination();

		searchContext.setEnd(pagination.getEnd());

		searchContext.setEntryClassNames(
			new String[] {JournalArticle.class.getName()});
		searchContext.setGroupIds(
			new long[] {serviceContext.getScopeGroupId()});

		Optional<Sort> sortOptional = collectionQuery.getSortOptional();

		if (sortOptional.isPresent()) {
			Sort sort = sortOptional.get();

			searchContext.setSorts(
				new com.liferay.portal.kernel.search.Sort(
					sort.getFieldName(),
					com.liferay.portal.kernel.search.Sort.LONG_TYPE,
					sort.isReverse()));
		}

		searchContext.setStart(pagination.getStart());

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		return searchContext;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DDMStructureRelatedInfoCollectionProvider.class);

	private final DDMStructure _ddmStructure;
	private final JournalArticleLocalService _journalArticleLocalService;

}