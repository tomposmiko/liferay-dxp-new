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

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.search.elasticsearch7.internal.connection.ElasticsearchClientResolver;
import com.liferay.portal.search.elasticsearch7.internal.util.JSONUtil;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = SearchSearchRequestExecutor.class)
public class SearchSearchRequestExecutorImpl
	implements SearchSearchRequestExecutor {

	@Override
	public SearchSearchResponse execute(
		SearchSearchRequest searchSearchRequest) {

		SearchRequest searchRequest = new SearchRequest();

		if (searchSearchRequest.getPointInTime() == null) {
			searchRequest.indices(searchSearchRequest.getIndexNames());
		}

		if (searchSearchRequest.isRequestCache()) {
			searchRequest.requestCache(searchSearchRequest.isRequestCache());
		}

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		_searchSearchRequestAssembler.assemble(
			searchSourceBuilder, searchSearchRequest, searchRequest);

		if (_log.isTraceEnabled()) {
			String prettyPrintedRequestString = _getPrettyPrintedRequestString(
				searchSourceBuilder);

			_log.trace("Search query: " + prettyPrintedRequestString);
		}

		SearchResponse searchResponse = null;

		if (searchSearchRequest.getScrollId() != null) {
			searchResponse = _getScrollSearchResponse(searchSearchRequest);
		}
		else {
			searchResponse = _getSearchResponse(
				searchRequest, searchSearchRequest);
		}

		SearchSearchResponse searchSearchResponse = new SearchSearchResponse();

		_searchSearchResponseAssembler.assemble(
			searchSourceBuilder, searchResponse, searchSearchRequest,
			searchSearchResponse);

		if (_log.isDebugEnabled()) {
			_log.debug(
				StringBundler.concat(
					"The search engine processed ",
					searchSearchResponse.getSearchRequestString(), " in ",
					searchSearchResponse.getExecutionTime(), " ms"));
		}

		return searchSearchResponse;
	}

	private String _getPrettyPrintedRequestString(
		SearchSourceBuilder searchSourceBuilder) {

		try {
			return JSONUtil.getPrettyPrintedJSONString(searchSourceBuilder);
		}
		catch (Exception exception) {
			return exception.getMessage();
		}
	}

	private SearchResponse _getScrollSearchResponse(
		SearchSearchRequest searchSearchRequest) {

		RestHighLevelClient restHighLevelClient =
			_elasticsearchClientResolver.getRestHighLevelClient(
				searchSearchRequest.getConnectionId(),
				searchSearchRequest.isPreferLocalCluster());

		SearchScrollRequest searchScrollRequest = new SearchScrollRequest(
			searchSearchRequest.getScrollId());

		if (searchSearchRequest.getScrollKeepAliveMinutes() > 0) {
			searchScrollRequest.scroll(
				TimeValue.timeValueMinutes(
					searchSearchRequest.getScrollKeepAliveMinutes()));
		}

		try {
			return restHighLevelClient.scroll(
				searchScrollRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private SearchResponse _getSearchResponse(
		SearchRequest searchRequest, SearchSearchRequest searchSearchRequest) {

		RestHighLevelClient restHighLevelClient =
			_elasticsearchClientResolver.getRestHighLevelClient(
				searchSearchRequest.getConnectionId(),
				searchSearchRequest.isPreferLocalCluster());

		try {
			return restHighLevelClient.search(
				searchRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SearchSearchRequestExecutorImpl.class);

	@Reference
	private ElasticsearchClientResolver _elasticsearchClientResolver;

	@Reference
	private SearchSearchRequestAssembler _searchSearchRequestAssembler;

	@Reference
	private SearchSearchResponseAssembler _searchSearchResponseAssembler;

}