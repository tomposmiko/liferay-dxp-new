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

package com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.search;

import com.liferay.portal.kernel.search.filter.FilterTranslator;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.aggregation.AggregationTranslator;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregationTranslator;
import com.liferay.portal.search.elasticsearch6.internal.facet.FacetTranslator;
import com.liferay.portal.search.elasticsearch6.internal.filter.FilterToQueryBuilderTranslator;
import com.liferay.portal.search.elasticsearch6.internal.query.QueryToQueryBuilderTranslator;
import com.liferay.portal.search.elasticsearch6.internal.stats.StatsTranslator;
import com.liferay.portal.search.engine.adapter.search.BaseSearchRequest;
import com.liferay.portal.search.filter.ComplexQueryBuilderFactory;
import com.liferay.portal.search.filter.ComplexQueryPart;
import com.liferay.portal.search.query.BooleanQuery;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.rescore.Rescore;
import com.liferay.portal.search.stats.StatsRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;
import org.elasticsearch.search.rescore.QueryRescorerBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = CommonSearchRequestBuilderAssembler.class)
public class CommonSearchRequestBuilderAssemblerImpl
	implements CommonSearchRequestBuilderAssembler {

	@Override
	public void assemble(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		setAggregations(searchRequestBuilder, baseSearchRequest);
		setExplain(searchRequestBuilder, baseSearchRequest);
		setFacets(searchRequestBuilder, baseSearchRequest);
		setIndexBoosts(searchRequestBuilder, baseSearchRequest);
		setIndices(searchRequestBuilder, baseSearchRequest);
		setMinScore(searchRequestBuilder, baseSearchRequest);
		setPipelineAggregations(searchRequestBuilder, baseSearchRequest);
		setPostFilter(searchRequestBuilder, baseSearchRequest);
		setQuery(searchRequestBuilder, baseSearchRequest);
		setRequestCache(searchRequestBuilder, baseSearchRequest);
		setRescorer(searchRequestBuilder, baseSearchRequest);
		setStatsRequests(searchRequestBuilder, baseSearchRequest);
		setTimeout(searchRequestBuilder, baseSearchRequest);
		setTrackTotalHits(searchRequestBuilder, baseSearchRequest);
		setTypes(searchRequestBuilder, baseSearchRequest);
	}

	protected BooleanQuery buildComplexQuery(
		List<ComplexQueryPart> complexQueryParts) {

		return (BooleanQuery)_complexQueryBuilderFactory.builder(
		).addParts(
			complexQueryParts
		).build();
	}

	protected QueryBuilder combine(
		BoolQueryBuilder boolQueryBuilder, QueryBuilder queryBuilder,
		BiConsumer<BoolQueryBuilder, QueryBuilder> biConsumer) {

		if (boolQueryBuilder == null) {
			return queryBuilder;
		}

		if (queryBuilder != null) {
			biConsumer.accept(boolQueryBuilder, queryBuilder);
		}

		return boolQueryBuilder;
	}

	protected QueryBuilder combine(
		QueryBuilder queryBuilder1, QueryBuilder queryBuilder2) {

		if (queryBuilder1 == null) {
			return queryBuilder2;
		}

		if (queryBuilder2 == null) {
			return queryBuilder1;
		}

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		return boolQueryBuilder.must(
			queryBuilder1
		).must(
			queryBuilder2
		);
	}

	protected void copy(List<Query> clauses, Consumer<QueryBuilder> consumer) {
		for (Query query : clauses) {
			consumer.accept(translateQuery(query));
		}
	}

	protected QueryBuilder getQueryBuilder(
		BaseSearchRequest baseSearchRequest) {

		QueryBuilder queryBuilder1 = combine(
			translateQuery(baseSearchRequest.getQuery()),
			translateQuery(baseSearchRequest.getQuery71()));

		List<ComplexQueryPart> complexQueryParts =
			baseSearchRequest.getComplexQueryParts();

		if (complexQueryParts.isEmpty()) {
			QueryBuilder queryBuilder2 = combine(
				translate(Collections.emptyList()), queryBuilder1,
				BoolQueryBuilder::must);

			return combine(
				translate(Collections.emptyList()), queryBuilder2,
				BoolQueryBuilder::should);
		}

		List<ComplexQueryPart> additiveComplexQueryParts = new ArrayList<>();

		List<ComplexQueryPart> noneAdditiveComplexQueryParts =
			new ArrayList<>();

		for (ComplexQueryPart complexQueryPart : complexQueryParts) {
			if (complexQueryPart.isAdditive()) {
				additiveComplexQueryParts.add(complexQueryPart);
			}
			else {
				noneAdditiveComplexQueryParts.add(complexQueryPart);
			}
		}

		QueryBuilder queryBuilder2 = combine(
			translate(noneAdditiveComplexQueryParts), queryBuilder1,
			BoolQueryBuilder::must);

		return combine(
			translate(additiveComplexQueryParts), queryBuilder2,
			BoolQueryBuilder::should);
	}

	protected void setAggregations(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		Map<String, Aggregation> aggregationsMap =
			baseSearchRequest.getAggregationsMap();

		if (MapUtil.isNotEmpty(aggregationsMap)) {
			Collection<Aggregation> aggregations = aggregationsMap.values();

			aggregations.forEach(
				aggregation -> {
					AggregationBuilder aggregationBuilder =
						_aggregationTranslator.translate(aggregation);

					searchRequestBuilder.addAggregation(aggregationBuilder);
				});
		}
	}

	@Reference(target = "(search.engine.impl=Elasticsearch)", unbind = "-")
	protected void setAggregationTranslator(
		AggregationTranslator<AggregationBuilder> aggregationTranslator) {

		_aggregationTranslator = aggregationTranslator;
	}

	@Reference(unbind = "-")
	protected void setComplexQueryBuilderFactory(
		ComplexQueryBuilderFactory complexQueryBuilderFactory) {

		_complexQueryBuilderFactory = complexQueryBuilderFactory;
	}

	protected void setExplain(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		if (baseSearchRequest.getExplain() != null) {
			searchRequestBuilder.setExplain(baseSearchRequest.getExplain());
		}
	}

	protected void setFacets(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		_facetTranslator.translate(
			searchRequestBuilder, baseSearchRequest.getQuery71(),
			baseSearchRequest.getFacets(),
			baseSearchRequest.isBasicFacetSelection());
	}

	@Reference(unbind = "-")
	protected void setFacetTranslator(FacetTranslator facetTranslator) {
		_facetTranslator = facetTranslator;
	}

	@Reference(unbind = "-")
	protected void setFilterToQueryBuilderTranslator(
		FilterToQueryBuilderTranslator filterToQueryBuilderTranslator) {

		_filterToQueryBuilderTranslator = filterToQueryBuilderTranslator;
	}

	protected void setIndexBoosts(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		Map<String, Float> indexBoosts = baseSearchRequest.getIndexBoosts();

		if (MapUtil.isNotEmpty(indexBoosts)) {
			indexBoosts.forEach(searchRequestBuilder::addIndexBoost);
		}
	}

	protected void setIndices(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		searchRequestBuilder.setIndices(baseSearchRequest.getIndexNames());
	}

	@Reference(unbind = "-")
	protected void setLegacyQueryToQueryBuilderTranslator(
		com.liferay.portal.search.elasticsearch6.internal.legacy.query.
			QueryToQueryBuilderTranslator queryToQueryBuilderTranslator) {

		_legacyQueryToQueryBuilderTranslator = queryToQueryBuilderTranslator;
	}

	protected void setMinScore(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		if (baseSearchRequest.getMinimumScore() != null) {
			searchRequestBuilder.setMinScore(
				baseSearchRequest.getMinimumScore());
		}
	}

	protected void setPipelineAggregations(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		Map<String, PipelineAggregation> pipelineAggregationsMap =
			baseSearchRequest.getPipelineAggregationsMap();

		if (MapUtil.isNotEmpty(pipelineAggregationsMap)) {
			Collection<PipelineAggregation> pipelineAggregations =
				pipelineAggregationsMap.values();

			pipelineAggregations.forEach(
				pipelineAggregation -> {
					PipelineAggregationBuilder pipelineAggregationBuilder =
						_pipelineAggregationTranslator.translate(
							pipelineAggregation);

					searchRequestBuilder.addAggregation(
						pipelineAggregationBuilder);
				});
		}
	}

	@Reference(target = "(search.engine.impl=Elasticsearch)", unbind = "-")
	protected void setPipelineAggregationTranslator(
		PipelineAggregationTranslator<PipelineAggregationBuilder>
			pipelineAggregationTranslator) {

		_pipelineAggregationTranslator = pipelineAggregationTranslator;
	}

	protected void setPostFilter(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		if (baseSearchRequest.getPostFilterQuery() != null) {
			searchRequestBuilder.setPostFilter(
				_queryToQueryBuilderTranslator.translate(
					baseSearchRequest.getPostFilterQuery()));
		}
		else if (baseSearchRequest.getPostFilter() != null) {
			searchRequestBuilder.setPostFilter(
				_filterToQueryBuilderTranslator.translate(
					baseSearchRequest.getPostFilter(), null));
		}
	}

	protected void setQuery(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		searchRequestBuilder.setQuery(getQueryBuilder(baseSearchRequest));
	}

	@Reference(unbind = "-")
	protected void setQueryToQueryBuilderTranslator(
		QueryToQueryBuilderTranslator queryToQueryBuilderTranslator) {

		_queryToQueryBuilderTranslator = queryToQueryBuilderTranslator;
	}

	protected void setRequestCache(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		if (baseSearchRequest.getRequestCache() != null) {
			searchRequestBuilder.setRequestCache(
				baseSearchRequest.getRequestCache());
		}
	}

	protected void setRescorer(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		setRescorers(searchRequestBuilder, baseSearchRequest.getRescores());

		setRescorerQuery(
			searchRequestBuilder, baseSearchRequest.getRescoreQuery());
	}

	protected void setRescorerQuery(
		SearchRequestBuilder searchRequestBuilder, Query query) {

		if (query == null) {
			return;
		}

		searchRequestBuilder.addRescorer(
			new QueryRescorerBuilder(
				_queryToQueryBuilderTranslator.translate(query)));
	}

	protected void setRescorers(
		SearchRequestBuilder searchRequestBuilder, List<Rescore> rescores) {

		if (rescores == null) {
			return;
		}

		for (Rescore rescore : rescores) {
			QueryRescorerBuilder queryRescorerBuilder =
				new QueryRescorerBuilder(
					_queryToQueryBuilderTranslator.translate(
						rescore.getQuery()));

			queryRescorerBuilder.windowSize(rescore.getWindowSize());

			searchRequestBuilder.addRescorer(queryRescorerBuilder);
		}
	}

	protected void setStatsRequests(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		List<StatsRequest> statsRequests = baseSearchRequest.getStatsRequests();

		if (!ListUtil.isEmpty(statsRequests)) {
			statsRequests.forEach(
				statsRequest -> _statsTranslator.populateRequest(
					searchRequestBuilder, statsRequest));
		}
	}

	@Reference(unbind = "-")
	protected void setStatsTranslator(StatsTranslator statsTranslator) {
		_statsTranslator = statsTranslator;
	}

	protected void setTimeout(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		if (baseSearchRequest.getTimeoutInMilliseconds() != null) {
			searchRequestBuilder.setTimeout(
				TimeValue.timeValueMillis(
					baseSearchRequest.getTimeoutInMilliseconds()));
		}
	}

	protected void setTrackTotalHits(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		if (baseSearchRequest.getTrackTotalHits() != null) {
			searchRequestBuilder.setTrackTotalHits(
				baseSearchRequest.getTrackTotalHits());
		}
	}

	protected void setTypes(
		SearchRequestBuilder searchRequestBuilder,
		BaseSearchRequest baseSearchRequest) {

		if (baseSearchRequest.getTypes() != null) {
			searchRequestBuilder.setTypes(baseSearchRequest.getTypes());
		}
	}

	protected void transfer(
		BooleanQuery booleanQuery, BoolQueryBuilder boolQueryBuilder) {

		copy(booleanQuery.getFilterQueryClauses(), boolQueryBuilder::filter);
		copy(booleanQuery.getMustNotQueryClauses(), boolQueryBuilder::mustNot);
		copy(booleanQuery.getMustQueryClauses(), boolQueryBuilder::must);
		copy(booleanQuery.getShouldQueryClauses(), boolQueryBuilder::should);
	}

	protected BoolQueryBuilder translate(
		List<ComplexQueryPart> complexQueryParts) {

		if (ListUtil.isEmpty(complexQueryParts)) {
			return null;
		}

		BooleanQuery booleanQuery = buildComplexQuery(complexQueryParts);

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		transfer(booleanQuery, boolQueryBuilder);

		return boolQueryBuilder;
	}

	protected QueryBuilder translateQuery(
		com.liferay.portal.kernel.search.Query query) {

		if (query == null) {
			return null;
		}

		QueryBuilder queryBuilder =
			_legacyQueryToQueryBuilderTranslator.translate(query, null);

		if ((query.getPreBooleanFilter() == null) ||
			(query instanceof com.liferay.portal.kernel.search.BooleanQuery)) {

			return queryBuilder;
		}

		// LPS-86537 the following is only present to allow for backwards
		// compatibility.  Not all Query should have filters allowed according
		// to Elasticsearch's API.

		// See related note in BooleanQueryTranslatorImpl

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		boolQueryBuilder.filter(
			_filterToQueryBuilderTranslator.translate(
				query.getPreBooleanFilter(), null));
		boolQueryBuilder.must(queryBuilder);

		return boolQueryBuilder;
	}

	protected QueryBuilder translateQuery(Query query) {
		if (query != null) {
			return _queryToQueryBuilderTranslator.translate(query);
		}

		return null;
	}

	private AggregationTranslator<AggregationBuilder> _aggregationTranslator;
	private ComplexQueryBuilderFactory _complexQueryBuilderFactory;
	private FacetTranslator _facetTranslator;
	private FilterTranslator<QueryBuilder> _filterToQueryBuilderTranslator;
	private com.liferay.portal.search.elasticsearch6.internal.legacy.query.
		QueryToQueryBuilderTranslator _legacyQueryToQueryBuilderTranslator;
	private PipelineAggregationTranslator<PipelineAggregationBuilder>
		_pipelineAggregationTranslator;
	private QueryToQueryBuilderTranslator _queryToQueryBuilderTranslator;
	private StatsTranslator _statsTranslator;

}