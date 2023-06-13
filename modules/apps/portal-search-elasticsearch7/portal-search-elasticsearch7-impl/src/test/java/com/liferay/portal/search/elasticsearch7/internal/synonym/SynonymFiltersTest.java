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

package com.liferay.portal.search.elasticsearch7.internal.synonym;

import com.liferay.portal.search.elasticsearch7.internal.connection.ElasticsearchClientResolver;
import com.liferay.portal.search.elasticsearch7.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch7.internal.connection.IndexName;
import com.liferay.portal.search.elasticsearch7.internal.document.SingleFieldFixture;
import com.liferay.portal.search.elasticsearch7.internal.index.LiferayTypeMappingsConstants;
import com.liferay.portal.search.elasticsearch7.internal.query.QueryBuilderFactories;
import com.liferay.portal.search.elasticsearch7.internal.query.SearchAssert;
import com.liferay.portal.search.elasticsearch7.internal.search.engine.adapter.ElasticsearchSearchEngineAdapterImpl;
import com.liferay.portal.search.elasticsearch7.internal.search.engine.adapter.ElasticsearchSearchEngineAdapterIndexRequestTest;
import com.liferay.portal.search.elasticsearch7.internal.search.engine.adapter.index.IndexRequestExecutorFixture;
import com.liferay.portal.search.elasticsearch7.internal.util.ResourceUtil;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.index.CreateIndexRequest;
import com.liferay.portal.search.engine.adapter.index.CreateIndexResponse;
import com.liferay.portal.search.engine.adapter.index.IndexRequestExecutor;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Adam Brandizzi
 */
public class SynonymFiltersTest {

	@Before
	public void setUp() throws Exception {
		_elasticsearchFixture = new ElasticsearchFixture(
			ElasticsearchSearchEngineAdapterIndexRequestTest.class.
				getSimpleName());

		_elasticsearchFixture.setUp();

		Client client = _elasticsearchFixture.getClient();

		AdminClient adminClient = client.admin();

		_indicesAdminClient = adminClient.indices();

		_searchEngineAdapter = createSearchEngineAdapter(_elasticsearchFixture);

		_singleFieldFixture = new SingleFieldFixture(
			client, new IndexName(_INDEX_NAME),
			LiferayTypeMappingsConstants.LIFERAY_DOCUMENT_TYPE);

		_singleFieldFixture.setField(_FIELD_NAME);
		_singleFieldFixture.setQueryBuilderFactory(QueryBuilderFactories.MATCH);
	}

	@After
	public void tearDown() throws Exception {
		deleteIndex();

		_elasticsearchFixture.tearDown();
	}

	@Test
	public void testSynonymFilterFailsWithSpaceInSynonymSetAndMatchPhraseQuery()
		throws Exception {

		createIndex("synonym-filter-spaced");

		_singleFieldFixture.indexDocument("git hash");
		_singleFieldFixture.indexDocument("stable");

		assertMatchPhraseQuerySearch("stable", "git hash");
	}

	@Test
	public void testSynonymFilterIgnoresQuoteInSearchString() throws Exception {
		createIndex("synonym-filter-unquoted");

		_singleFieldFixture.indexDocument("\"stable\"");
		_singleFieldFixture.indexDocument("upstream");

		_singleFieldFixture.assertSearch(
			"\"stable\"", "\"stable\"", "upstream");
	}

	@Test
	public void testSynonymFilterIgnoresQuoteInSynonymSet() throws Exception {
		createIndex("synonym-filter-quoted");

		_singleFieldFixture.indexDocument("\"stable\"");
		_singleFieldFixture.indexDocument("upstream");

		_singleFieldFixture.assertSearch("stable", "\"stable\"", "upstream");
	}

	@Test
	public void testSynonymFilterIgnoresSpaceInSearchString() throws Exception {
		createIndex("synonym-filter-spaced");

		_singleFieldFixture.indexDocument("git hash");
		_singleFieldFixture.indexDocument("stable");

		_singleFieldFixture.assertSearch("git hash", "git hash", "stable");
	}

	@Test
	public void testSynonymFilterIgnoresSpaceInSynonymSet() throws Exception {
		createIndex("synonym-filter-spaced");

		_singleFieldFixture.indexDocument("git hash");
		_singleFieldFixture.indexDocument("stable");

		_singleFieldFixture.assertSearch("stable", "git hash", "stable");
	}

	@Test
	public void testSynonymGraphFilterIgnoresQuoteInSearchString()
		throws Exception {

		createIndex("synonym-graph-filter-unquoted");

		_singleFieldFixture.indexDocument("\"stable\"");
		_singleFieldFixture.indexDocument("upstream");

		_singleFieldFixture.assertSearch(
			"\"stable\"", "\"stable\"", "upstream");
	}

	@Test
	public void testSynonymGraphFilterIgnoresQuoteInSynonymSet()
		throws Exception {

		createIndex("synonym-graph-filter-quoted");

		_singleFieldFixture.indexDocument("\"stable\"");
		_singleFieldFixture.indexDocument("upstream");

		_singleFieldFixture.assertSearch("stable", "\"stable\"", "upstream");
	}

	@Test
	public void testSynonymGraphFilterIgnoresSpaceInSearchString()
		throws Exception {

		createIndex("synonym-graph-filter-spaced");

		_singleFieldFixture.indexDocument("git hash");
		_singleFieldFixture.indexDocument("stable");

		_singleFieldFixture.assertSearch("git hash", "git hash", "stable");
	}

	@Test
	public void testSynonymGraphFilterIgnoresSpaceInSynonymSet()
		throws Exception {

		createIndex("synonym-graph-filter-spaced");

		_singleFieldFixture.indexDocument("git hash");
		_singleFieldFixture.indexDocument("stable");

		_singleFieldFixture.assertSearch("stable", "git hash", "stable");
	}

	@Test
	public void testSynonymGraphFilterWorksWithSpaceInSynonymSetAndMatchPhraseQuery()
		throws Exception {

		createIndex("synonym-graph-filter-spaced");

		_singleFieldFixture.indexDocument("git hash");
		_singleFieldFixture.indexDocument("stable");

		assertMatchPhraseQuerySearch("stable", "git hash", "stable");
	}

	protected static IndexRequestExecutor createIndexRequestExecutor(
		ElasticsearchClientResolver elasticsearchClientResolver) {

		IndexRequestExecutorFixture indexRequestExecutorFixture =
			new IndexRequestExecutorFixture() {
				{
					setElasticsearchClientResolver(elasticsearchClientResolver);
				}
			};

		indexRequestExecutorFixture.setUp();

		return indexRequestExecutorFixture.getIndexRequestExecutor();
	}

	protected static SearchEngineAdapter createSearchEngineAdapter(
		ElasticsearchClientResolver elasticsearchClientResolver) {

		return new ElasticsearchSearchEngineAdapterImpl() {
			{
				setIndexRequestExecutor(
					createIndexRequestExecutor(elasticsearchClientResolver));
			}
		};
	}

	protected void assertMatchPhraseQuerySearch(
			String text, String... expectedValues)
		throws Exception {

		MatchPhraseQueryBuilder matchPhraseQueryBuilder =
			new MatchPhraseQueryBuilder(_FIELD_NAME, text);

		SearchAssert.assertSearch(
			_elasticsearchFixture.getClient(), _FIELD_NAME,
			matchPhraseQueryBuilder, expectedValues);
	}

	protected void createIndex(String suffix) {
		CreateIndexRequest createIndexRequest = new CreateIndexRequest(
			_INDEX_NAME);

		createIndexRequest.setSource(getSource(suffix));

		CreateIndexResponse createIndexResponse = _searchEngineAdapter.execute(
			createIndexRequest);

		Assert.assertTrue(createIndexResponse.isAcknowledged());
	}

	protected void deleteIndex() {
		DeleteIndexRequestBuilder deleteIndexRequestBuilder =
			_indicesAdminClient.prepareDelete(_INDEX_NAME);

		deleteIndexRequestBuilder.get();
	}

	protected String getSource(String suffix) {
		return ResourceUtil.getResourceAsString(
			getClass(),
			"dependencies/synonym-filters-test-" + suffix + ".json");
	}

	private static final String _FIELD_NAME = "content";

	private static final String _INDEX_NAME = "test_synonyms";

	private ElasticsearchFixture _elasticsearchFixture;
	private IndicesAdminClient _indicesAdminClient;
	private SearchEngineAdapter _searchEngineAdapter;
	private SingleFieldFixture _singleFieldFixture;

}