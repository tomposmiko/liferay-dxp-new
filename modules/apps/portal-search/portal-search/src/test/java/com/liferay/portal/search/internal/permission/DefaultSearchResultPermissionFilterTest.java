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

package com.liferay.portal.search.internal.permission;

import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.HitsImpl;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.RelatedEntryIndexerRegistry;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.FacetPostProcessor;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.search.configuration.DefaultSearchResultPermissionFilterConfiguration;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.searcher.SearchRequest;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Arrays;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Gustavo Lima
 */
public class DefaultSearchResultPermissionFilterTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testSearchGroupAdmin() {
		_groupAdmin = true;
		_permissionFilteredSearchResultAccurateCountThreshold = 0;

		DefaultSearchResultPermissionFilter
			defaultSearchResultPermissionFilter =
				_getDefaultSearchResultPermissionFilter();

		SearchContext searchContext = _getSearchContext(4);

		_assertPagination(
			searchContext, defaultSearchResultPermissionFilter, 4, 10);

		searchContext = _getSearchContext(8);

		_assertPagination(
			searchContext, defaultSearchResultPermissionFilter, 8, 10);

		searchContext = _getSearchContext(10);

		_assertPagination(
			searchContext, defaultSearchResultPermissionFilter, 10, 10);
	}

	@Test
	public void testSearchGuestWithoutThreshold() {
		_groupAdmin = false;
		_permissionFilteredSearchResultAccurateCountThreshold = 0;

		DefaultSearchResultPermissionFilter
			defaultSearchResultPermissionFilter =
				_getDefaultSearchResultPermissionFilter();

		SearchContext searchContext = _getSearchContext(4);

		_assertPagination(
			searchContext, defaultSearchResultPermissionFilter, 4, 10);

		searchContext = _getSearchContext(8);

		_assertPagination(
			searchContext, defaultSearchResultPermissionFilter, 8, 10);

		searchContext = _getSearchContext(10);

		_assertPagination(
			searchContext, defaultSearchResultPermissionFilter, 9, 9);
	}

	@Test
	public void testSearchGuestWithThreshold() {
		_groupAdmin = false;
		_permissionFilteredSearchResultAccurateCountThreshold = 20;

		DefaultSearchResultPermissionFilter
			defaultSearchResultPermissionFilter =
				_getDefaultSearchResultPermissionFilter();

		SearchContext searchContext = _getSearchContext(4);

		_assertPagination(
			searchContext, defaultSearchResultPermissionFilter, 4, 9);

		searchContext = _getSearchContext(8);

		_assertPagination(
			searchContext, defaultSearchResultPermissionFilter, 8, 9);

		searchContext = _getSearchContext(10);

		_assertPagination(
			searchContext, defaultSearchResultPermissionFilter, 9, 9);
	}

	private void _assertPagination(
		SearchContext searchContext,
		DefaultSearchResultPermissionFilter defaultSearchResultPermissionFilter,
		int pageCount, int totalCount) {

		Hits hits = defaultSearchResultPermissionFilter.search(searchContext);

		Document[] docs = hits.getDocs();

		Assert.assertEquals(hits.toString(), totalCount, hits.getLength());

		Assert.assertEquals(Arrays.toString(docs), pageCount, docs.length);
	}

	private DefaultSearchResultPermissionFilter
		_getDefaultSearchResultPermissionFilter() {

		_mockSearchResultPermissionFilterConfiguration();

		SearchRequestBuilderFactory searchRequestBuilderFactory =
			_getSearchRequestBuilderFactory();

		return new DefaultSearchResultPermissionFilter(
			Mockito.mock(FacetPostProcessor.class),
			Mockito.mock(IndexerRegistry.class), _permissionChecker,
			Mockito.mock(Props.class),
			Mockito.mock(RelatedEntryIndexerRegistry.class), _searchFunction,
			searchRequestBuilderFactory,
			_defaultSearchResultPermissionFilterConfiguration);
	}

	private Document _getDocument(String companyId, int index) {
		Document document = Mockito.mock(Document.class);

		Mockito.when(
			document.get(Field.COMPANY_ID)
		).thenReturn(
			companyId
		);

		Mockito.when(
			document.get(Field.ENTRY_CLASS_NAME)
		).thenReturn(
			"com.liferay.journal.model.JournalArticle"
		);

		Mockito.when(
			document.get(Field.ENTRY_CLASS_PK)
		).thenReturn(
			String.valueOf(index)
		);

		return document;
	}

	private Hits _getHits(int size) {
		Hits hits = new HitsImpl();

		if (_permissionFilteredSearchResultAccurateCountThreshold > size) {
			size = _permissionFilteredSearchResultAccurateCountThreshold;

			int maxDocSize = _PRIVATE_DOCUMENTS + _PUBLIC_DOCUMENTS;

			if (size > maxDocSize) {
				size = maxDocSize;
			}
		}

		Document[] documents = Arrays.copyOf(_documents, size);

		float[] scores = new float[size];

		for (int i = 0; i < size; i++) {
			scores[i] = i;
		}

		hits.setDocs(documents);
		hits.setLength(_documents.length);
		hits.setScores(scores);

		return Mockito.spy(hits);
	}

	private SearchContext _getSearchContext(int end) {
		SearchContext searchContext = new SearchContext();

		_mockPermission(searchContext);

		_setUpDocuments();

		searchContext.setEnd(end);
		searchContext.setStart(0);

		Mockito.when(
			_searchFunction.apply(searchContext)
		).thenReturn(
			_getHits(end)
		);

		return searchContext;
	}

	private SearchRequestBuilderFactory _getSearchRequestBuilderFactory() {
		SearchRequestBuilderFactory searchRequestBuilderFactory = Mockito.mock(
			SearchRequestBuilderFactory.class);

		SearchRequestBuilder searchRequestBuilder = Mockito.mock(
			SearchRequestBuilder.class);

		Mockito.when(
			searchRequestBuilderFactory.builder(Mockito.any())
		).thenReturn(
			searchRequestBuilder
		);

		Mockito.when(
			searchRequestBuilder.build()
		).thenReturn(
			_searchRequest
		);

		return searchRequestBuilderFactory;
	}

	private void _mockPermission(SearchContext searchContext) {
		searchContext.setAttribute(Field.STATUS, 1);

		searchContext.setAttribute(Field.GROUP_ID, _USER_GROUP_ID);

		Mockito.when(
			_permissionChecker.getCompanyId()
		).thenReturn(
			_USER_GROUP_ID
		);

		Mockito.when(
			_permissionChecker.isGroupAdmin(_USER_GROUP_ID)
		).thenReturn(
			_groupAdmin
		);
	}

	private void _mockSearchResultPermissionFilterConfiguration() {
		Mockito.when(
			_defaultSearchResultPermissionFilterConfiguration.
				permissionFilteredSearchResultAccurateCountThreshold()
		).thenReturn(
			_permissionFilteredSearchResultAccurateCountThreshold
		);

		Mockito.when(
			_defaultSearchResultPermissionFilterConfiguration.
				searchQueryResultWindowLimit()
		).thenReturn(
			100
		);
	}

	private void _setUpDocuments() {
		_documents = new Document[_PRIVATE_DOCUMENTS + _PUBLIC_DOCUMENTS];

		for (int i = 0; i < _PUBLIC_DOCUMENTS; i++) {
			Document document = _getDocument("1", i);

			_documents[i] = document;
		}

		for (int i = _PUBLIC_DOCUMENTS; i < _documents.length; i++) {
			Document document = _getDocument("0", i);

			_documents[i] = document;
		}
	}

	private static final int _PRIVATE_DOCUMENTS = 1;

	private static final int _PUBLIC_DOCUMENTS = 9;

	private static final long _USER_GROUP_ID = 1L;

	private static final SearchRequest _searchRequest = Mockito.mock(
		SearchRequest.class);

	private final DefaultSearchResultPermissionFilterConfiguration
		_defaultSearchResultPermissionFilterConfiguration = Mockito.mock(
			DefaultSearchResultPermissionFilterConfiguration.class);
	private Document[] _documents;
	private boolean _groupAdmin;
	private final PermissionChecker _permissionChecker = Mockito.mock(
		PermissionChecker.class);
	private int _permissionFilteredSearchResultAccurateCountThreshold;
	private final Function<SearchContext, Hits> _searchFunction = Mockito.mock(
		Function.class);

}