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

package com.liferay.portal.search.elasticsearch7.internal.search.engine.adapter.search;

import com.liferay.portal.kernel.search.query.QueryTranslator;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.elasticsearch7.internal.SearchHitDocumentTranslatorImpl;
import com.liferay.portal.search.elasticsearch7.internal.aggregation.ElasticsearchAggregationVisitorFixture;
import com.liferay.portal.search.elasticsearch7.internal.aggregation.pipeline.ElasticsearchPipelineAggregationVisitorFixture;
import com.liferay.portal.search.elasticsearch7.internal.connection.ElasticsearchClientResolver;
import com.liferay.portal.search.elasticsearch7.internal.facet.CompositeFacetProcessor;
import com.liferay.portal.search.elasticsearch7.internal.facet.DefaultFacetProcessor;
import com.liferay.portal.search.elasticsearch7.internal.facet.DefaultFacetTranslator;
import com.liferay.portal.search.elasticsearch7.internal.facet.FacetProcessor;
import com.liferay.portal.search.elasticsearch7.internal.facet.FacetTranslator;
import com.liferay.portal.search.elasticsearch7.internal.facet.ModifiedFacetProcessor;
import com.liferay.portal.search.elasticsearch7.internal.facet.NestedFacetProcessor;
import com.liferay.portal.search.elasticsearch7.internal.filter.ElasticsearchFilterTranslatorFixture;
import com.liferay.portal.search.elasticsearch7.internal.groupby.DefaultGroupByTranslator;
import com.liferay.portal.search.elasticsearch7.internal.highlight.DefaultHighlighterTranslator;
import com.liferay.portal.search.elasticsearch7.internal.query.ElasticsearchQueryTranslator;
import com.liferay.portal.search.elasticsearch7.internal.query.ElasticsearchQueryTranslatorFixture;
import com.liferay.portal.search.elasticsearch7.internal.search.response.DefaultSearchResponseTranslator;
import com.liferay.portal.search.elasticsearch7.internal.search.response.SearchResponseTranslator;
import com.liferay.portal.search.elasticsearch7.internal.sort.DefaultSortTranslator;
import com.liferay.portal.search.elasticsearch7.internal.sort.ElasticsearchSortFieldTranslator;
import com.liferay.portal.search.elasticsearch7.internal.sort.ElasticsearchSortFieldTranslatorFixture;
import com.liferay.portal.search.elasticsearch7.internal.stats.DefaultStatsTranslator;
import com.liferay.portal.search.elasticsearch7.internal.stats.StatsTranslator;
import com.liferay.portal.search.elasticsearch7.internal.suggest.ElasticsearchSuggesterTranslatorFixture;
import com.liferay.portal.search.engine.adapter.search.SearchRequestExecutor;
import com.liferay.portal.search.filter.ComplexQueryBuilderFactory;
import com.liferay.portal.search.internal.aggregation.AggregationResultsImpl;
import com.liferay.portal.search.internal.document.DocumentBuilderFactoryImpl;
import com.liferay.portal.search.internal.facet.ModifiedFacetImpl;
import com.liferay.portal.search.internal.facet.NestedFacetImpl;
import com.liferay.portal.search.internal.filter.ComplexQueryBuilderFactoryImpl;
import com.liferay.portal.search.internal.geolocation.GeoBuildersImpl;
import com.liferay.portal.search.internal.groupby.GroupByResponseFactoryImpl;
import com.liferay.portal.search.internal.highlight.HighlightFieldBuilderFactoryImpl;
import com.liferay.portal.search.internal.hits.SearchHitBuilderFactoryImpl;
import com.liferay.portal.search.internal.hits.SearchHitsBuilderFactoryImpl;
import com.liferay.portal.search.internal.legacy.groupby.GroupByRequestFactoryImpl;
import com.liferay.portal.search.internal.legacy.stats.StatsRequestBuilderFactoryImpl;
import com.liferay.portal.search.internal.legacy.stats.StatsResultsTranslatorImpl;
import com.liferay.portal.search.internal.query.QueriesImpl;
import com.liferay.portal.search.internal.stats.StatsResponseBuilderFactoryImpl;
import com.liferay.portal.search.legacy.stats.StatsRequestBuilderFactory;
import com.liferay.portal.search.query.Queries;

import java.util.Collections;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * @author Michael C. Han
 */
public class SearchRequestExecutorFixture {

	public SearchRequestExecutor getSearchRequestExecutor() {
		return _searchRequestExecutor;
	}

	public void setUp() {
		FacetProcessor<?> facetProcessor = _getFacetProcessor();

		ElasticsearchQueryTranslatorFixture
			elasticsearchQueryTranslatorFixture =
				new ElasticsearchQueryTranslatorFixture();

		ElasticsearchQueryTranslator elasticsearchQueryTranslator =
			elasticsearchQueryTranslatorFixture.
				getElasticsearchQueryTranslator();

		ElasticsearchSortFieldTranslatorFixture
			elasticsearchSortFieldTranslatorFixture =
				new ElasticsearchSortFieldTranslatorFixture(
					elasticsearchQueryTranslator);

		StatsTranslator statsTranslator = new DefaultStatsTranslator();

		ReflectionTestUtil.setFieldValue(
			statsTranslator, "_statsResponseBuilderFactory",
			new StatsResponseBuilderFactoryImpl());

		_searchRequestExecutor = _createSearchRequestExecutor(
			createComplexQueryBuilderFactory(new QueriesImpl()),
			_elasticsearchClientResolver, elasticsearchQueryTranslator,
			elasticsearchSortFieldTranslatorFixture.
				getElasticsearchSortFieldTranslator(),
			facetProcessor, new StatsRequestBuilderFactoryImpl(),
			statsTranslator);
	}

	protected static CommonSearchSourceBuilderAssembler
		createCommonSearchSourceBuilderAssembler(
			ElasticsearchQueryTranslator elasticsearchQueryTranslator,
			FacetProcessor<?> facetProcessor, StatsTranslator statsTranslator,
			ComplexQueryBuilderFactory complexQueryBuilderFactory) {

		com.liferay.portal.search.elasticsearch7.internal.legacy.query.
			ElasticsearchQueryTranslatorFixture
				legacyElasticsearchQueryTranslatorFixture =
					new com.liferay.portal.search.elasticsearch7.internal.
						legacy.query.ElasticsearchQueryTranslatorFixture();

		com.liferay.portal.search.elasticsearch7.internal.legacy.query.
			ElasticsearchQueryTranslator legacyElasticsearchQueryTranslator =
				legacyElasticsearchQueryTranslatorFixture.
					getElasticsearchQueryTranslator();

		ElasticsearchAggregationVisitorFixture
			elasticsearchAggregationVisitorFixture =
				new ElasticsearchAggregationVisitorFixture();

		ElasticsearchFilterTranslatorFixture
			elasticsearchFilterTranslatorFixture =
				new ElasticsearchFilterTranslatorFixture(
					legacyElasticsearchQueryTranslator);

		ElasticsearchPipelineAggregationVisitorFixture
			elasticsearchPipelineAggregationVisitorFixture =
				new ElasticsearchPipelineAggregationVisitorFixture();

		CommonSearchSourceBuilderAssembler commonSearchSourceBuilderAssembler =
			new CommonSearchSourceBuilderAssemblerImpl();

		ReflectionTestUtil.setFieldValue(
			commonSearchSourceBuilderAssembler, "_aggregationTranslator",
			elasticsearchAggregationVisitorFixture.
				getElasticsearchAggregationVisitor());
		ReflectionTestUtil.setFieldValue(
			commonSearchSourceBuilderAssembler, "_complexQueryBuilderFactory",
			complexQueryBuilderFactory);
		ReflectionTestUtil.setFieldValue(
			commonSearchSourceBuilderAssembler, "_facetTranslator",
			_createFacetTranslator(
				facetProcessor, legacyElasticsearchQueryTranslator));
		ReflectionTestUtil.setFieldValue(
			commonSearchSourceBuilderAssembler,
			"_filterToQueryBuilderTranslator",
			elasticsearchFilterTranslatorFixture.
				getElasticsearchFilterTranslator());
		ReflectionTestUtil.setFieldValue(
			commonSearchSourceBuilderAssembler,
			"_legacyQueryToQueryBuilderTranslator",
			legacyElasticsearchQueryTranslator);
		ReflectionTestUtil.setFieldValue(
			commonSearchSourceBuilderAssembler,
			"_pipelineAggregationTranslator",
			elasticsearchPipelineAggregationVisitorFixture.
				getElasticsearchPipelineAggregationVisitor());
		ReflectionTestUtil.setFieldValue(
			commonSearchSourceBuilderAssembler,
			"_queryToQueryBuilderTranslator", elasticsearchQueryTranslator);
		ReflectionTestUtil.setFieldValue(
			commonSearchSourceBuilderAssembler, "_statsTranslator",
			statsTranslator);

		return commonSearchSourceBuilderAssembler;
	}

	protected static ComplexQueryBuilderFactory
		createComplexQueryBuilderFactory(Queries queries) {

		ComplexQueryBuilderFactoryImpl complexQueryBuilderFactoryImpl =
			new ComplexQueryBuilderFactoryImpl();

		ReflectionTestUtil.setFieldValue(
			complexQueryBuilderFactoryImpl, "_queries", queries);

		return complexQueryBuilderFactoryImpl;
	}

	protected void setElasticsearchClientResolver(
		ElasticsearchClientResolver elasticsearchClientResolver) {

		_elasticsearchClientResolver = elasticsearchClientResolver;
	}

	protected void setFacetProcessor(FacetProcessor<?> facetProcessor) {
		_facetProcessor = facetProcessor;
	}

	private static FacetTranslator _createFacetTranslator(
		FacetProcessor<?> facetProcessor,
		QueryTranslator<QueryBuilder> queryTranslator) {

		DefaultFacetTranslator defaultFacetTranslator =
			new DefaultFacetTranslator();

		ElasticsearchFilterTranslatorFixture
			elasticsearchFilterTranslatorFixture =
				new ElasticsearchFilterTranslatorFixture(queryTranslator);

		ReflectionTestUtil.setFieldValue(
			defaultFacetTranslator, "_facetProcessor",
			(FacetProcessor<SearchRequestBuilder>)facetProcessor);
		ReflectionTestUtil.setFieldValue(
			defaultFacetTranslator, "_filterTranslator",
			elasticsearchFilterTranslatorFixture.
				getElasticsearchFilterTranslator());

		return defaultFacetTranslator;
	}

	private CountSearchRequestExecutor _createCountSearchRequestExecutor(
		ElasticsearchClientResolver elasticsearchClientResolver,
		CommonSearchSourceBuilderAssembler commonSearchSourceBuilderAssembler,
		StatsTranslator statsTranslator) {

		CountSearchRequestExecutor countSearchRequestExecutor =
			new CountSearchRequestExecutorImpl();

		CommonSearchResponseAssembler commonSearchResponseAssembler =
			new CommonSearchResponseAssemblerImpl();

		ReflectionTestUtil.setFieldValue(
			commonSearchResponseAssembler, "_statsTranslator", statsTranslator);

		ReflectionTestUtil.setFieldValue(
			countSearchRequestExecutor, "_commonSearchResponseAssembler",
			commonSearchResponseAssembler);

		ReflectionTestUtil.setFieldValue(
			countSearchRequestExecutor, "_commonSearchSourceBuilderAssembler",
			commonSearchSourceBuilderAssembler);
		ReflectionTestUtil.setFieldValue(
			countSearchRequestExecutor, "_elasticsearchClientResolver",
			elasticsearchClientResolver);

		return countSearchRequestExecutor;
	}

	private MultisearchSearchRequestExecutor
		_createMultisearchSearchRequestExecutor(
			ElasticsearchClientResolver elasticsearchClientResolver,
			SearchSearchRequestAssembler searchSearchRequestAssembler,
			SearchSearchResponseAssembler searchSearchResponseAssembler) {

		MultisearchSearchRequestExecutor multisearchSearchRequestExecutor =
			new MultisearchSearchRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			multisearchSearchRequestExecutor, "_elasticsearchClientResolver",
			elasticsearchClientResolver);
		ReflectionTestUtil.setFieldValue(
			multisearchSearchRequestExecutor, "_searchSearchRequestAssembler",
			searchSearchRequestAssembler);
		ReflectionTestUtil.setFieldValue(
			multisearchSearchRequestExecutor, "_searchSearchResponseAssembler",
			searchSearchResponseAssembler);

		return multisearchSearchRequestExecutor;
	}

	private SearchRequestExecutor _createSearchRequestExecutor(
		ComplexQueryBuilderFactory complexQueryBuilderFactory,
		ElasticsearchClientResolver elasticsearchClientResolver,
		ElasticsearchQueryTranslator elasticsearchQueryTranslator,
		ElasticsearchSortFieldTranslator elasticsearchSortFieldTranslator,
		FacetProcessor<?> facetProcessor,
		StatsRequestBuilderFactory statsRequestBuilderFactory,
		StatsTranslator statsTranslator) {

		CommonSearchSourceBuilderAssembler commonSearchSourceBuilderAssembler =
			createCommonSearchSourceBuilderAssembler(
				elasticsearchQueryTranslator, facetProcessor, statsTranslator,
				complexQueryBuilderFactory);

		SearchSearchRequestAssembler searchSearchRequestAssembler =
			_createSearchSearchRequestAssembler(
				elasticsearchQueryTranslator, elasticsearchSortFieldTranslator,
				commonSearchSourceBuilderAssembler, statsRequestBuilderFactory,
				statsTranslator);

		SearchSearchResponseAssembler searchSearchResponseAssembler =
			_createSearchSearchResponseAssembler(
				statsRequestBuilderFactory, statsTranslator);

		SearchRequestExecutor searchRequestExecutor =
			new ElasticsearchSearchRequestExecutor();

		ReflectionTestUtil.setFieldValue(
			searchRequestExecutor, "_countSearchRequestExecutor",
			_createCountSearchRequestExecutor(
				elasticsearchClientResolver, commonSearchSourceBuilderAssembler,
				statsTranslator));
		ReflectionTestUtil.setFieldValue(
			searchRequestExecutor, "_multisearchSearchRequestExecutor",
			_createMultisearchSearchRequestExecutor(
				elasticsearchClientResolver, searchSearchRequestAssembler,
				searchSearchResponseAssembler));
		ReflectionTestUtil.setFieldValue(
			searchRequestExecutor, "_searchSearchRequestExecutor",
			_createSearchSearchRequestExecutor(
				elasticsearchClientResolver, searchSearchRequestAssembler,
				searchSearchResponseAssembler));
		ReflectionTestUtil.setFieldValue(
			searchRequestExecutor, "_suggestSearchRequestExecutor",
			_createSuggestSearchRequestExecutor(elasticsearchClientResolver));

		return searchRequestExecutor;
	}

	private SearchSearchRequestAssembler _createSearchSearchRequestAssembler(
		ElasticsearchQueryTranslator elasticsearchQueryTranslator,
		ElasticsearchSortFieldTranslator elasticsearchSortFieldTranslator,
		CommonSearchSourceBuilderAssembler commonSearchSourceBuilderAssembler,
		StatsRequestBuilderFactory statsRequestBuilderFactory,
		StatsTranslator statsTranslator) {

		SearchSearchRequestAssembler searchSearchRequestAssembler =
			new SearchSearchRequestAssemblerImpl();

		ReflectionTestUtil.setFieldValue(
			searchSearchRequestAssembler, "_commonSearchSourceBuilderAssembler",
			commonSearchSourceBuilderAssembler);
		ReflectionTestUtil.setFieldValue(
			searchSearchRequestAssembler, "_groupByRequestFactory",
			new GroupByRequestFactoryImpl());
		ReflectionTestUtil.setFieldValue(
			searchSearchRequestAssembler, "_groupByTranslator",
			new DefaultGroupByTranslator());
		ReflectionTestUtil.setFieldValue(
			searchSearchRequestAssembler, "_highlighterTranslator",
			new DefaultHighlighterTranslator());
		ReflectionTestUtil.setFieldValue(
			searchSearchRequestAssembler, "_queryToQueryBuilderTranslator",
			elasticsearchQueryTranslator);
		ReflectionTestUtil.setFieldValue(
			searchSearchRequestAssembler, "_sortFieldTranslator",
			elasticsearchSortFieldTranslator);
		ReflectionTestUtil.setFieldValue(
			searchSearchRequestAssembler, "_sortTranslator",
			new DefaultSortTranslator());
		ReflectionTestUtil.setFieldValue(
			searchSearchRequestAssembler, "_statsRequestBuilderFactory",
			statsRequestBuilderFactory);
		ReflectionTestUtil.setFieldValue(
			searchSearchRequestAssembler, "_statsTranslator", statsTranslator);

		return searchSearchRequestAssembler;
	}

	private SearchSearchRequestExecutor _createSearchSearchRequestExecutor(
		ElasticsearchClientResolver elasticsearchClientResolver,
		SearchSearchRequestAssembler searchSearchRequestAssembler,
		SearchSearchResponseAssembler searchSearchResponseAssembler) {

		SearchSearchRequestExecutor searchSearchRequestExecutor =
			new SearchSearchRequestExecutorImpl();

		ReflectionTestUtil.setFieldValue(
			searchSearchRequestExecutor, "_elasticsearchClientResolver",
			elasticsearchClientResolver);
		ReflectionTestUtil.setFieldValue(
			searchSearchRequestExecutor, "_searchSearchRequestAssembler",
			searchSearchRequestAssembler);
		ReflectionTestUtil.setFieldValue(
			searchSearchRequestExecutor, "_searchSearchResponseAssembler",
			searchSearchResponseAssembler);

		return searchSearchRequestExecutor;
	}

	private SearchSearchResponseAssembler _createSearchSearchResponseAssembler(
		StatsRequestBuilderFactory statsRequestBuilderFactory,
		StatsTranslator statsTranslator) {

		SearchSearchResponseAssembler searchSearchResponseAssembler =
			new SearchSearchResponseAssemblerImpl();

		ReflectionTestUtil.setFieldValue(
			searchSearchResponseAssembler, "_aggregationResults",
			new AggregationResultsImpl());

		CommonSearchResponseAssembler commonSearchResponseAssembler =
			new CommonSearchResponseAssemblerImpl();

		ReflectionTestUtil.setFieldValue(
			commonSearchResponseAssembler, "_statsTranslator", statsTranslator);

		ReflectionTestUtil.setFieldValue(
			searchSearchResponseAssembler, "_commonSearchResponseAssembler",
			commonSearchResponseAssembler);

		ReflectionTestUtil.setFieldValue(
			searchSearchResponseAssembler, "_documentBuilderFactory",
			new DocumentBuilderFactoryImpl());
		ReflectionTestUtil.setFieldValue(
			searchSearchResponseAssembler, "_geoBuilders",
			new GeoBuildersImpl());
		ReflectionTestUtil.setFieldValue(
			searchSearchResponseAssembler, "_highlightFieldBuilderFactory",
			new HighlightFieldBuilderFactoryImpl());
		ReflectionTestUtil.setFieldValue(
			searchSearchResponseAssembler, "_searchHitBuilderFactory",
			new SearchHitBuilderFactoryImpl());
		ReflectionTestUtil.setFieldValue(
			searchSearchResponseAssembler, "_searchHitsBuilderFactory",
			new SearchHitsBuilderFactoryImpl());

		SearchResponseTranslator searchResponseTranslator =
			new DefaultSearchResponseTranslator();

		ReflectionTestUtil.setFieldValue(
			searchResponseTranslator, "_groupByResponseFactory",
			new GroupByResponseFactoryImpl());
		ReflectionTestUtil.setFieldValue(
			searchResponseTranslator, "_searchHitDocumentTranslator",
			new SearchHitDocumentTranslatorImpl());
		ReflectionTestUtil.setFieldValue(
			searchResponseTranslator, "_statsRequestBuilderFactory",
			statsRequestBuilderFactory);
		ReflectionTestUtil.setFieldValue(
			searchResponseTranslator, "_statsResultsTranslator",
			new StatsResultsTranslatorImpl());
		ReflectionTestUtil.setFieldValue(
			searchResponseTranslator, "_statsTranslator", statsTranslator);

		ReflectionTestUtil.setFieldValue(
			searchSearchResponseAssembler, "_searchResponseTranslator",
			searchResponseTranslator);

		return searchSearchResponseAssembler;
	}

	private SuggestSearchRequestExecutor _createSuggestSearchRequestExecutor(
		ElasticsearchClientResolver elasticsearchClientResolver) {

		SuggestSearchRequestExecutor suggestSearchRequestExecutor =
			new SuggestSearchRequestExecutorImpl();

		ElasticsearchSuggesterTranslatorFixture
			elasticsearchSuggesterTranslatorFixture =
				new ElasticsearchSuggesterTranslatorFixture();

		ReflectionTestUtil.setFieldValue(
			suggestSearchRequestExecutor, "_elasticsearchClientResolver",
			elasticsearchClientResolver);
		ReflectionTestUtil.setFieldValue(
			suggestSearchRequestExecutor, "_suggesterTranslator",
			elasticsearchSuggesterTranslatorFixture.
				getElasticsearchSuggesterTranslator());

		return suggestSearchRequestExecutor;
	}

	private FacetProcessor<?> _getFacetProcessor() {
		if (_facetProcessor != null) {
			return _facetProcessor;
		}

		return new CompositeFacetProcessor() {
			{
				defaultFacetProcessor = new DefaultFacetProcessor();

				setFacetProcessor(
					new ModifiedFacetProcessor(),
					Collections.singletonMap(
						"class.name", ModifiedFacetImpl.class.getName()));
				setFacetProcessor(
					new NestedFacetProcessor(),
					Collections.singletonMap(
						"class.name", NestedFacetImpl.class.getName()));
			}
		};
	}

	private ElasticsearchClientResolver _elasticsearchClientResolver;
	private FacetProcessor<?> _facetProcessor;
	private SearchRequestExecutor _searchRequestExecutor;

}