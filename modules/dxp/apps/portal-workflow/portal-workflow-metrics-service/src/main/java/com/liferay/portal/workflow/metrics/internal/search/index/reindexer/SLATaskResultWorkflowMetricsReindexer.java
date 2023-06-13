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

package com.liferay.portal.workflow.metrics.internal.search.index.reindexer;

import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalRunMode;
import com.liferay.portal.search.capabilities.SearchCapabilities;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.document.BulkDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.IndexDocumentRequest;
import com.liferay.portal.search.engine.adapter.index.IndicesExistsIndexRequest;
import com.liferay.portal.search.engine.adapter.index.IndicesExistsIndexResponse;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.hits.SearchHit;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.query.BooleanQuery;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.workflow.metrics.internal.search.index.SLATaskResultWorkflowMetricsIndexer;
import com.liferay.portal.workflow.metrics.search.background.task.WorkflowMetricsReindexStatusMessageSender;
import com.liferay.portal.workflow.metrics.search.index.name.WorkflowMetricsIndexNameBuilder;
import com.liferay.portal.workflow.metrics.search.index.reindexer.WorkflowMetricsReindexer;

import java.util.concurrent.atomic.AtomicInteger;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(
	property = "workflow.metrics.index.entity.name=sla-task-result",
	service = WorkflowMetricsReindexer.class
)
public class SLATaskResultWorkflowMetricsReindexer
	implements WorkflowMetricsReindexer {

	@Override
	public void reindex(long companyId) {
		_creatDefaultDocuments(companyId);
	}

	@Reference
	protected SearchEngineAdapter searchEngineAdapter;

	private void _creatDefaultDocuments(long companyId) {
		if (!_searchCapabilities.isWorkflowMetricsSupported() ||
			!_hasIndex(
				_nodeWorkflowMetricsIndexNameBuilder.getIndexName(companyId))) {

			return;
		}

		SearchSearchRequest searchSearchRequest = new SearchSearchRequest();

		searchSearchRequest.setIndexNames(
			_nodeWorkflowMetricsIndexNameBuilder.getIndexName(companyId));

		BooleanQuery booleanQuery = _queries.booleanQuery();

		booleanQuery.addFilterQueryClauses(
			_queries.term("companyId", companyId),
			_queries.term("deleted", Boolean.FALSE));

		searchSearchRequest.setQuery(booleanQuery);

		searchSearchRequest.setSize(10000);

		SearchSearchResponse searchSearchResponse = searchEngineAdapter.execute(
			searchSearchRequest);

		SearchHits searchHits = searchSearchResponse.getSearchHits();

		if (searchHits.getTotalHits() == 0) {
			return;
		}

		AtomicInteger atomicCounter = new AtomicInteger(0);

		BulkDocumentRequest bulkDocumentRequest = new BulkDocumentRequest();

		for (SearchHit searchHit : searchHits.getSearchHits()) {
			Document document = searchHit.getDocument();

			bulkDocumentRequest.addBulkableDocumentRequest(
				new IndexDocumentRequest(
					_slaTaskResultWorkflowMetricsIndexer.getIndexName(
						companyId),
					_slaTaskResultWorkflowMetricsIndexer.creatDefaultDocument(
						companyId, document.getLong("nodeId"),
						document.getLong("processId"),
						document.getString("name"))) {

					{
						setType(
							_slaTaskResultWorkflowMetricsIndexer.
								getIndexType());
					}
				});

			_workflowMetricsReindexStatusMessageSender.sendStatusMessage(
				atomicCounter.incrementAndGet(), searchHits.getTotalHits(),
				"sla-task-result");
		}

		if (ListUtil.isNotEmpty(
				bulkDocumentRequest.getBulkableDocumentRequests())) {

			if (PortalRunMode.isTestMode()) {
				bulkDocumentRequest.setRefresh(true);
			}

			searchEngineAdapter.execute(bulkDocumentRequest);
		}
	}

	private boolean _hasIndex(String indexName) {
		IndicesExistsIndexRequest indicesExistsIndexRequest =
			new IndicesExistsIndexRequest(indexName);

		IndicesExistsIndexResponse indicesExistsIndexResponse =
			searchEngineAdapter.execute(indicesExistsIndexRequest);

		return indicesExistsIndexResponse.isExists();
	}

	@Reference(target = "(workflow.metrics.index.entity.name=node)")
	private WorkflowMetricsIndexNameBuilder
		_nodeWorkflowMetricsIndexNameBuilder;

	@Reference
	private Queries _queries;

	@Reference
	private SearchCapabilities _searchCapabilities;

	@Reference
	private SLATaskResultWorkflowMetricsIndexer
		_slaTaskResultWorkflowMetricsIndexer;

	@Reference
	private WorkflowMetricsReindexStatusMessageSender
		_workflowMetricsReindexStatusMessageSender;

}