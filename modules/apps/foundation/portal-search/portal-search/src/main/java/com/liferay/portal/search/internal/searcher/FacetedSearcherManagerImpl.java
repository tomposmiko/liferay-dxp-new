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

package com.liferay.portal.search.internal.searcher;

import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.util.ExpandoBridgeFactory;
import com.liferay.expando.kernel.util.ExpandoBridgeIndexer;
import com.liferay.portal.kernel.search.IndexSearcherHelper;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.SearchEngineHelper;
import com.liferay.portal.kernel.search.facet.faceted.searcher.FacetedSearcher;
import com.liferay.portal.kernel.search.facet.faceted.searcher.FacetedSearcherManager;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.search.internal.expando.ExpandoQueryContributorHelper;
import com.liferay.portal.search.permission.SearchPermissionFilterContributor;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author André de Oliveira
 */
@Component(immediate = true, service = FacetedSearcherManager.class)
public class FacetedSearcherManagerImpl implements FacetedSearcherManager {

	@Override
	public FacetedSearcher createFacetedSearcher() {
		return new FacetedSearcherImpl(
			new ExpandoQueryContributorHelper(
				expandoBridgeFactory, expandoBridgeIndexer,
				expandoColumnLocalService, getLocalization()),
			groupLocalService, indexerRegistry, indexSearcherHelper,
			searchEngineHelper, _searchPermissionFilterContributors);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		unbind = "removeSearchPermissionFilterContributor"
	)
	protected void addSearchPermissionFilterContributor(
		SearchPermissionFilterContributor searchPermissionFilterContributor) {

		_searchPermissionFilterContributors.add(
			searchPermissionFilterContributor);
	}

	protected Localization getLocalization() {

		// See LPS-72507

		if (localization != null) {
			return localization;
		}

		return LocalizationUtil.getLocalization();
	}

	protected void removeSearchPermissionFilterContributor(
		SearchPermissionFilterContributor searchPermissionFilterContributor) {

		_searchPermissionFilterContributors.remove(
			searchPermissionFilterContributor);
	}

	@Reference
	protected ExpandoBridgeFactory expandoBridgeFactory;

	@Reference
	protected ExpandoBridgeIndexer expandoBridgeIndexer;

	@Reference
	protected ExpandoColumnLocalService expandoColumnLocalService;

	@Reference
	protected GroupLocalService groupLocalService;

	@Reference
	protected IndexerRegistry indexerRegistry;

	@Reference
	protected IndexSearcherHelper indexSearcherHelper;

	protected Localization localization;

	@Reference
	protected SearchEngineHelper searchEngineHelper;

	private final Collection<SearchPermissionFilterContributor>
		_searchPermissionFilterContributors = new CopyOnWriteArrayList<>();

}