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

package com.liferay.portal.search.internal.legacy.searcher;

import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.search.aggregation.Aggregation;
import com.liferay.portal.search.aggregation.pipeline.PipelineAggregation;
import com.liferay.portal.search.filter.ComplexQueryPart;
import com.liferay.portal.search.query.Query;
import com.liferay.portal.search.searcher.FacetContext;
import com.liferay.portal.search.searcher.SearchRequest;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.portal.search.sort.Sort;
import com.liferay.portal.search.stats.StatsRequest;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author André de Oliveira
 */
public class SearchRequestBuilderImpl implements SearchRequestBuilder {

	public SearchRequestBuilderImpl(SearchContext searchContext) {
		_facetContext = new FacetContextImpl(searchContext);
		_searchContext = searchContext;
	}

	@Override
	public SearchRequestBuilder addAggregation(Aggregation aggregation) {
		Map<String, Aggregation> map = getSearchContextKeyAggregationsMap();

		map.put(aggregation.getName(), aggregation);

		return this;
	}

	@Override
	public SearchRequestBuilder addComplexQueryPart(
		ComplexQueryPart complexQueryPart) {

		List<ComplexQueryPart> list = getSearchContextKeyComplexQueryParts();

		list.add(complexQueryPart);

		return this;
	}

	@Override
	public SearchRequestBuilder addPipelineAggregation(
		PipelineAggregation pipelineAggregation) {

		Map<String, PipelineAggregation> map =
			getSearchContextKeyPipelineAggregationsMap();

		map.put(pipelineAggregation.getName(), pipelineAggregation);

		return this;
	}

	@Override
	public SearchRequestBuilder addSelectedFieldNames(
		String... selectedFieldNames) {

		QueryConfig queryConfig = _searchContext.getQueryConfig();

		queryConfig.addSelectedFieldNames(selectedFieldNames);

		return this;
	}

	@Override
	public SearchRequest build() {
		return new SearchRequestImpl();
	}

	@Override
	public SearchRequestBuilder entryClassNames(String... entryClassNames) {
		_searchContext.setEntryClassNames(entryClassNames);

		return this;
	}

	@Override
	public SearchRequestBuilder explain(boolean explain) {
		_searchContext.setAttribute(
			_SEARCH_CONTEXT_KEY_EXPLAIN, Boolean.valueOf(explain));

		return this;
	}

	@Override
	public SearchRequestBuilder includeResponseString(
		boolean includeResponseString) {

		_searchContext.setAttribute(
			_SEARCH_CONTEXT_KEY_INCLUDE_RESPONSE_STRING,
			Boolean.valueOf(includeResponseString));

		return this;
	}

	@Override
	public SearchRequestBuilder modelIndexerClasses(Class<?>... classes) {
		if (classes != null) {
			_modelIndexerClasses = Arrays.asList(classes);
		}
		else {
			_modelIndexerClasses = Collections.emptyList();
		}

		return this;
	}

	@Override
	public SearchRequestBuilder postFilterQuery(Query query) {
		_postFilterQuery = query;

		return this;
	}

	@Override
	public SearchRequestBuilder query(Query query) {
		_searchContext.setAttribute(_SEARCH_CONTEXT_KEY_QUERY, query);

		return this;
	}

	@Override
	public SearchRequestBuilder rescoreQuery(Query rescoreQuery) {
		_searchContext.setAttribute(
			_SEARCH_CONTEXT_KEY_RESCORE_QUERY, rescoreQuery);

		return this;
	}

	@Override
	public SearchRequestBuilder sorts(Sort... sorts) {
		_searchContext.setAttribute(
			_SEARCH_CONTEXT_KEY_SORTS, new ArrayList<>(Arrays.asList(sorts)));

		return this;
	}

	@Override
	public SearchRequestBuilder statsRequests(StatsRequest... statsRequests) {
		_searchContext.setAttribute(
			_SEARCH_CONTEXT_KEY_STATS_REQUESTS,
			new ArrayList<>(Arrays.asList(statsRequests)));

		return this;
	}

	@Override
	public SearchRequestBuilder withFacetContext(
		Consumer<FacetContext> facetContextConsumer) {

		facetContextConsumer.accept(_facetContext);

		return this;
	}

	@Override
	public <T> T withFacetContextGet(
		Function<FacetContext, T> facetContextFunction) {

		return facetContextFunction.apply(_facetContext);
	}

	@Override
	public SearchRequestBuilder withSearchContext(
		Consumer<SearchContext> consumer) {

		consumer.accept(_searchContext);

		return this;
	}

	@Override
	public <T> T withSearchContextGet(
		Function<SearchContext, T> searchContextFunction) {

		return searchContextFunction.apply(_searchContext);
	}

	public class SearchRequestImpl implements SearchRequest {

		@Override
		public Map<String, Aggregation> getAggregationsMap() {
			Map<String, Aggregation> map =
				(Map<String, Aggregation>)_searchContext.getAttribute(
					_SEARCH_CONTEXT_KEY_AGGREGATIONS_MAP);

			if (map == null) {
				return Collections.emptyMap();
			}

			return map;
		}

		@Override
		public List<ComplexQueryPart> getComplexQueryParts() {
			List<ComplexQueryPart> list =
				(List<ComplexQueryPart>)_searchContext.getAttribute(
					_SEARCH_CONTEXT_KEY_COMPLEX_QUERY_PARTS);

			if (list != null) {
				return list;
			}

			return Collections.emptyList();
		}

		@Override
		public List<String> getEntryClassNames() {
			return Arrays.asList(_searchContext.getEntryClassNames());
		}

		@Override
		public List<Class<?>> getModelIndexerClasses() {
			return Collections.unmodifiableList(_modelIndexerClasses);
		}

		@Override
		public Map<String, PipelineAggregation> getPipelineAggregationsMap() {
			Map<String, PipelineAggregation> map =
				(Map<String, PipelineAggregation>)_searchContext.getAttribute(
					_SEARCH_CONTEXT_KEY_PIPELINE_AGGREGATIONS_MAP);

			if (map == null) {
				return Collections.emptyMap();
			}

			return map;
		}

		@Override
		public Query getPostFilterQuery() {
			return _postFilterQuery;
		}

		@Override
		public Query getQuery() {
			return (Query)_searchContext.getAttribute(
				_SEARCH_CONTEXT_KEY_QUERY);
		}

		@Override
		public Query getRescoreQuery() {
			return (Query)_searchContext.getAttribute(
				_SEARCH_CONTEXT_KEY_RESCORE_QUERY);
		}

		public SearchContext getSearchContext() {
			return _searchContext;
		}

		@Override
		public List<Sort> getSorts() {
			Serializable serializable = _searchContext.getAttribute(
				_SEARCH_CONTEXT_KEY_SORTS);

			if (serializable != null) {
				return (List<Sort>)serializable;
			}

			return Collections.emptyList();
		}

		@Override
		public List<StatsRequest> getStatsRequests() {
			Serializable serializable = _searchContext.getAttribute(
				_SEARCH_CONTEXT_KEY_STATS_REQUESTS);

			if (serializable != null) {
				return (List<StatsRequest>)serializable;
			}

			return Collections.emptyList();
		}

		@Override
		public boolean isExplain() {
			return GetterUtil.getBoolean(
				_searchContext.getAttribute(_SEARCH_CONTEXT_KEY_EXPLAIN));
		}

		@Override
		public boolean isIncludeResponseString() {
			return GetterUtil.getBoolean(
				_searchContext.getAttribute(
					_SEARCH_CONTEXT_KEY_INCLUDE_RESPONSE_STRING));
		}

	}

	protected Map<String, Aggregation> getSearchContextKeyAggregationsMap() {
		synchronized (_searchContext) {
			LinkedHashMap<String, Aggregation> linkedHashMap =
				(LinkedHashMap<String, Aggregation>)_searchContext.getAttribute(
					_SEARCH_CONTEXT_KEY_AGGREGATIONS_MAP);

			if (linkedHashMap != null) {
				return linkedHashMap;
			}

			linkedHashMap = new LinkedHashMap<>();

			_searchContext.setAttribute(
				_SEARCH_CONTEXT_KEY_AGGREGATIONS_MAP, linkedHashMap);

			return linkedHashMap;
		}
	}

	protected List<ComplexQueryPart> getSearchContextKeyComplexQueryParts() {
		synchronized (_searchContext) {
			ArrayList<ComplexQueryPart> list =
				(ArrayList<ComplexQueryPart>)_searchContext.getAttribute(
					_SEARCH_CONTEXT_KEY_COMPLEX_QUERY_PARTS);

			if (list != null) {
				return list;
			}

			list = new ArrayList<>();

			_searchContext.setAttribute(
				_SEARCH_CONTEXT_KEY_COMPLEX_QUERY_PARTS, list);

			return list;
		}
	}

	protected Map<String, PipelineAggregation>
		getSearchContextKeyPipelineAggregationsMap() {

		synchronized (_searchContext) {
			LinkedHashMap<String, PipelineAggregation> linkedHashMap =
				(LinkedHashMap<String, PipelineAggregation>)
					_searchContext.getAttribute(
						_SEARCH_CONTEXT_KEY_PIPELINE_AGGREGATIONS_MAP);

			if (linkedHashMap != null) {
				return linkedHashMap;
			}

			linkedHashMap = new LinkedHashMap<>();

			_searchContext.setAttribute(
				_SEARCH_CONTEXT_KEY_PIPELINE_AGGREGATIONS_MAP, linkedHashMap);

			return linkedHashMap;
		}
	}

	private static final String _SEARCH_CONTEXT_KEY_AGGREGATIONS_MAP =
		"search.request.aggregations.map";

	private static final String _SEARCH_CONTEXT_KEY_COMPLEX_QUERY_PARTS =
		"search.request.complex.query.parts";

	private static final String _SEARCH_CONTEXT_KEY_EXPLAIN =
		"search.request.explain";

	private static final String _SEARCH_CONTEXT_KEY_INCLUDE_RESPONSE_STRING =
		"search.request.include.response.string";

	private static final String _SEARCH_CONTEXT_KEY_PIPELINE_AGGREGATIONS_MAP =
		"search.request.pipeline.aggregations.map";

	private static final String _SEARCH_CONTEXT_KEY_QUERY =
		"search.request.query";

	private static final String _SEARCH_CONTEXT_KEY_RESCORE_QUERY =
		"search.request.rescore.query";

	private static final String _SEARCH_CONTEXT_KEY_SORTS =
		"search.request.sorts";

	private static final String _SEARCH_CONTEXT_KEY_STATS_REQUESTS =
		"search.request.stats.requests";

	private final FacetContext _facetContext;
	private List<Class<?>> _modelIndexerClasses = Collections.emptyList();
	private Query _postFilterQuery;
	private final SearchContext _searchContext;

}