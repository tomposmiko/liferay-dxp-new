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

package com.liferay.portal.workflow.metrics.internal.search.index;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.index.CreateIndexRequest;
import com.liferay.portal.search.engine.adapter.index.DeleteIndexRequest;
import com.liferay.portal.search.engine.adapter.index.IndicesExistsIndexRequest;
import com.liferay.portal.search.engine.adapter.index.IndicesExistsIndexResponse;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Inácio Nery
 */
public abstract class BaseWorkflowMetricsIndex implements WorkflowMetricsIndex {

	@Override
	public boolean createIndex(long companyId) throws PortalException {
		if (_hasIndex(getIndexName(companyId))) {
			return false;
		}

		_createIndex(getIndexName(companyId));

		return true;
	}

	@Override
	public boolean removeIndex(long companyId) throws PortalException {
		if (!_hasIndex(getIndexName(companyId))) {
			return false;
		}

		searchEngineAdapter.execute(
			new DeleteIndexRequest(getIndexName(companyId)));

		return true;
	}

	@Reference(target = "(search.engine.impl=Elasticsearch)")
	protected volatile SearchEngineAdapter searchEngineAdapter;

	private String _createIndex(String indexName) {
		IndicesExistsIndexResponse indicesExistsIndexResponse =
			searchEngineAdapter.execute(
				new IndicesExistsIndexRequest(indexName));

		if (indicesExistsIndexResponse.isExists()) {
			return indexName;
		}

		try {
			CreateIndexRequest createIndexRequest = new CreateIndexRequest(
				indexName);

			createIndexRequest.setSource(
				JSONUtil.put(
					"mappings",
					JSONUtil.put(
						getIndexType(),
						() -> {
							JSONObject jsonObject = _readJSONObject(
								"mappings.json");

							return jsonObject.get(getIndexType());
						})
				).put(
					"settings", _readJSONObject("settings.json")
				).toString());

			searchEngineAdapter.execute(createIndexRequest);

			return indexName;
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		return null;
	}

	private boolean _hasIndex(String indexName) {
		IndicesExistsIndexRequest indicesExistsIndexRequest =
			new IndicesExistsIndexRequest(indexName);

		IndicesExistsIndexResponse indicesExistsIndexResponse =
			searchEngineAdapter.execute(indicesExistsIndexRequest);

		return indicesExistsIndexResponse.isExists();
	}

	private JSONObject _readJSONObject(String fileName) throws JSONException {
		return JSONFactoryUtil.createJSONObject(
			StringUtil.read(getClass(), "/META-INF/search/" + fileName));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseWorkflowMetricsIndex.class);

}