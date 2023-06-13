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

package com.liferay.portal.workflow.metrics.rest.internal.resource.v1_0;

import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.search.aggregation.Aggregations;
import com.liferay.portal.search.aggregation.bucket.Bucket;
import com.liferay.portal.search.aggregation.bucket.FilterAggregation;
import com.liferay.portal.search.aggregation.bucket.FilterAggregationResult;
import com.liferay.portal.search.aggregation.bucket.TermsAggregation;
import com.liferay.portal.search.aggregation.bucket.TermsAggregationResult;
import com.liferay.portal.search.aggregation.metrics.TopHitsAggregationResult;
import com.liferay.portal.search.aggregation.pipeline.BucketSelectorPipelineAggregation;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.engine.adapter.search.CountSearchRequest;
import com.liferay.portal.search.engine.adapter.search.CountSearchResponse;
import com.liferay.portal.search.engine.adapter.search.SearchRequestExecutor;
import com.liferay.portal.search.engine.adapter.search.SearchSearchRequest;
import com.liferay.portal.search.engine.adapter.search.SearchSearchResponse;
import com.liferay.portal.search.hits.SearchHit;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.query.BooleanQuery;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.query.TermsQuery;
import com.liferay.portal.search.script.Scripts;
import com.liferay.portal.search.sort.SortOrder;
import com.liferay.portal.search.sort.Sorts;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;
import com.liferay.portal.workflow.metrics.model.WorkflowMetricsSLADefinition;
import com.liferay.portal.workflow.metrics.rest.dto.v1_0.Assignee;
import com.liferay.portal.workflow.metrics.rest.dto.v1_0.Creator;
import com.liferay.portal.workflow.metrics.rest.dto.v1_0.Instance;
import com.liferay.portal.workflow.metrics.rest.dto.v1_0.SLAResult;
import com.liferay.portal.workflow.metrics.rest.dto.v1_0.Transition;
import com.liferay.portal.workflow.metrics.rest.internal.dto.v1_0.util.AssigneeUtil;
import com.liferay.portal.workflow.metrics.rest.internal.resource.exception.NoSuchInstanceException;
import com.liferay.portal.workflow.metrics.rest.internal.resource.helper.ResourceHelper;
import com.liferay.portal.workflow.metrics.rest.resource.v1_0.InstanceResource;
import com.liferay.portal.workflow.metrics.search.index.InstanceWorkflowMetricsIndexer;
import com.liferay.portal.workflow.metrics.search.index.name.WorkflowMetricsIndexNameBuilder;
import com.liferay.portal.workflow.metrics.service.WorkflowMetricsSLADefinitionLocalService;
import com.liferay.portal.workflow.metrics.sla.processor.WorkflowMetricsSLAStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Rafael Praxedes
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/instance.properties",
	scope = ServiceScope.PROTOTYPE, service = InstanceResource.class
)
public class InstanceResourceImpl extends BaseInstanceResourceImpl {

	@Override
	public void deleteProcessInstance(Long processId, Long instanceId)
		throws Exception {

		_instanceWorkflowMetricsIndexer.deleteInstance(
			contextCompany.getCompanyId(), instanceId);
	}

	@Override
	public Instance getProcessInstance(Long processId, Long instanceId)
		throws Exception {

		SearchSearchRequest searchSearchRequest = new SearchSearchRequest();

		TermsAggregation termsAggregation = _aggregations.terms(
			"instanceId", "instanceId");

		FilterAggregation indexFilterAggregation = _aggregations.filter(
			"tasksIndex",
			_queries.term(
				"_index",
				_taskWorkflowMetricsIndexNameBuilder.getIndexName(
					contextCompany.getCompanyId())));

		TermsAggregation assigneeTypeTermsAggregation = _aggregations.terms(
			"assigneeType", "assigneeType");

		TermsAggregation assigneeIdTermsAggregation = _aggregations.terms(
			"assigneeId", "assigneeIds");

		assigneeIdTermsAggregation.setSize(10000);

		assigneeTypeTermsAggregation.addChildAggregation(
			assigneeIdTermsAggregation);

		indexFilterAggregation.addChildAggregation(
			assigneeTypeTermsAggregation);

		FilterAggregation onTimeFilterAggregation = _aggregations.filter(
			"onTime", _resourceHelper.createMustNotBooleanQuery());

		onTimeFilterAggregation.addChildAggregation(
			_resourceHelper.createOnTimeScriptedMetricAggregation());

		FilterAggregation overdueFilterAggregation = _aggregations.filter(
			"overdue", _resourceHelper.createMustNotBooleanQuery());

		overdueFilterAggregation.addChildAggregation(
			_resourceHelper.createOverdueScriptedMetricAggregation());

		TermsAggregation slaDefinitionIdTermsAggregation = _aggregations.terms(
			"slaDefinitionId", "slaDefinitionId");

		slaDefinitionIdTermsAggregation.addChildAggregation(
			_aggregations.topHits("topHits"));
		slaDefinitionIdTermsAggregation.setSize(10000);

		TermsAggregation taskNameTermsAggregation = _aggregations.terms(
			"name", "name");

		taskNameTermsAggregation.setSize(10000);

		termsAggregation.addChildrenAggregations(
			indexFilterAggregation, onTimeFilterAggregation,
			overdueFilterAggregation, slaDefinitionIdTermsAggregation,
			taskNameTermsAggregation);

		searchSearchRequest.addAggregation(termsAggregation);

		searchSearchRequest.setIndexNames(
			_instanceWorkflowMetricsIndexNameBuilder.getIndexName(
				contextCompany.getCompanyId()),
			_slaInstanceResultWorkflowMetricsIndexNameBuilder.getIndexName(
				contextCompany.getCompanyId()),
			_taskWorkflowMetricsIndexNameBuilder.getIndexName(
				contextCompany.getCompanyId()));

		BooleanQuery booleanQuery = _createInstancesBooleanQuery(
			new Long[0], new Long[0], null, null, null, processId,
			new String[0], null, new String[0]);

		searchSearchRequest.setQuery(
			booleanQuery.addMustQueryClauses(
				_queries.term("instanceId", instanceId)));

		SearchSearchResponse searchSearchResponse =
			_searchRequestExecutor.executeSearchRequest(searchSearchRequest);

		return Stream.of(
			searchSearchResponse.getSearchHits()
		).map(
			SearchHits::getSearchHits
		).flatMap(
			List::stream
		).map(
			SearchHit::getDocument
		).filter(
			document -> document.getString(
				"uid"
			).startsWith(
				"WorkflowMetricsInstance"
			)
		).findFirst(
		).map(
			this::_createInstance
		).map(
			instance -> {
				Stream.of(
					searchSearchResponse.getAggregationResultsMap()
				).map(
					aggregationResultsMap ->
						(TermsAggregationResult)aggregationResultsMap.get(
							"instanceId")
				).map(
					TermsAggregationResult::getBuckets
				).flatMap(
					Collection::stream
				).findFirst(
				).ifPresent(
					bucket -> {
						_setAssignees(bucket, instance);
						_setSLAResults(bucket, instance);
						_setTaskNames(bucket, instance);
						_setTransitions(instance);
					}
				);

				return instance;
			}
		).orElseThrow(
			() -> new NoSuchInstanceException(
				"No instance exists with the instance ID " + instanceId)
		);
	}

	@Override
	public Page<Instance> getProcessInstancesPage(
			Long processId, Long[] assigneeIds, Long[] classPKs,
			Boolean completed, Date dateEnd, Date dateStart,
			String[] slaStatuses, String[] taskNames, Pagination pagination)
		throws Exception {

		long instanceCount = _getInstanceCount(
			assigneeIds, classPKs, completed, dateEnd, dateStart, processId,
			slaStatuses, taskNames);

		if (instanceCount > 0) {
			long startInstanceId = 0;

			if (pagination.getEndPosition() > 10000) {
				int endPosition = pagination.getEndPosition();

				while (endPosition > 10000) {
					startInstanceId = _getInstanceId(
						assigneeIds, classPKs, completed, dateEnd, dateStart,
						processId, slaStatuses, startInstanceId, taskNames);

					endPosition = endPosition - 10000;
				}

				pagination = Pagination.of(
					endPosition / pagination.getPageSize(),
					pagination.getPageSize());
			}

			return Page.of(
				_getInstances(
					assigneeIds, classPKs, completed, dateEnd, dateStart,
					pagination, processId, slaStatuses, startInstanceId,
					taskNames),
				pagination, instanceCount);
		}

		return Page.of(Collections.emptyList());
	}

	@Override
	public void patchProcessInstance(
			Long processId, Long instanceId, Instance instance)
		throws Exception {

		getProcessInstance(processId, instanceId);

		_instanceWorkflowMetricsIndexer.updateInstance(
			LocalizedMapUtil.getLocalizedMap(instance.getAssetTitle_i18n()),
			LocalizedMapUtil.getLocalizedMap(instance.getAssetType_i18n()),
			contextCompany.getCompanyId(), instanceId,
			instance.getDateModified());
	}

	@Override
	public void patchProcessInstanceComplete(
			Long processId, Long instanceId, Instance instance)
		throws Exception {

		getProcessInstance(processId, instanceId);

		_instanceWorkflowMetricsIndexer.completeInstance(
			contextCompany.getCompanyId(), instance.getDateCompletion(),
			instance.getDuration(), instanceId, instance.getDateModified());
	}

	@Override
	public Instance postProcessInstance(Long processId, Instance instance)
		throws Exception {

		Creator creator = instance.getCreator();

		return _createInstance(
			_instanceWorkflowMetricsIndexer.addInstance(
				LocalizedMapUtil.getLocalizedMap(instance.getAssetTitle_i18n()),
				LocalizedMapUtil.getLocalizedMap(instance.getAssetType_i18n()),
				instance.getClassName(), instance.getClassPK(),
				contextCompany.getCompanyId(), null, instance.getDateCreated(),
				instance.getId(), instance.getDateModified(), processId,
				instance.getProcessVersion(), creator.getId(),
				creator.getName()));
	}

	private Assignee _createAssignee(boolean reviewer) {
		Assignee assignee = new Assignee();

		assignee.setId(-1L);
		assignee.setName(
			_language.get(
				ResourceBundleUtil.getModuleAndPortalResourceBundle(
					contextAcceptLanguage.getPreferredLocale(),
					InstanceResourceImpl.class),
				"unassigned"));
		assignee.setReviewer(reviewer);

		return assignee;
	}

	private BooleanQuery _createBooleanQuery(long processId) {
		BooleanQuery booleanQuery = _queries.booleanQuery();

		return booleanQuery.addMustQueryClauses(
			_queries.term("companyId", contextCompany.getCompanyId()),
			_queries.term("deleted", Boolean.FALSE),
			_queries.term("processId", processId));
	}

	private BooleanQuery _createBooleanQuery(long processId, long instanceId) {
		BooleanQuery booleanQuery = _queries.booleanQuery();

		BooleanQuery tasksBooleanQuery = _queries.booleanQuery();

		tasksBooleanQuery.addFilterQueryClauses(
			_queries.term(
				"_index",
				_taskWorkflowMetricsIndexNameBuilder.getIndexName(
					contextCompany.getCompanyId())));
		tasksBooleanQuery.addMustQueryClauses(
			_createTasksBooleanQuery(processId, instanceId));

		BooleanQuery transitionsBooleanQuery = _queries.booleanQuery();

		transitionsBooleanQuery.addFilterQueryClauses(
			_queries.term(
				"_index",
				_transitionWorkflowMetricsIndexNameBuilder.getIndexName(
					contextCompany.getCompanyId())));
		transitionsBooleanQuery.addMustQueryClauses(
			_createBooleanQuery(processId));

		return booleanQuery.addShouldQueryClauses(
			tasksBooleanQuery, transitionsBooleanQuery);
	}

	private Instance _createInstance(Document document) {
		Instance instance = new Instance() {
			{
				assetTitle = document.getString(
					_getLocalizedName("assetTitle"));
				assetType = document.getString(_getLocalizedName("assetType"));
				classPK = document.getLong("classPK");
				completed = document.getBoolean("completed");
				creator = _toCreator(document.getLong("userId"));
				dateCompletion = _parseDate(document.getDate("completionDate"));
				dateCreated = _parseDate(document.getDate("createDate"));
				dateModified = _parseDate(document.getDate("modifiedDate"));
				id = document.getLong("instanceId");
				processId = document.getLong("processId");
				slaStatus = Instance.SLAStatus.create(
					document.getString("slaStatus"));
			}
		};

		_populateWithTasks(document, instance);

		return instance;
	}

	private BooleanQuery _createInstancesBooleanQuery(
		Long[] assigneeIds, Long[] classPKs, Boolean completed, Date dateEnd,
		Date dateStart, long processId, String[] slaStatuses,
		Long startInstanceId, String[] taskNames) {

		BooleanQuery booleanQuery = _queries.booleanQuery();

		booleanQuery.addMustNotQueryClauses(_queries.term("instanceId", 0));

		if (assigneeIds.length > 0) {
			BooleanQuery nestedBooleanQuery = _queries.booleanQuery();

			TermsQuery termsQuery = _queries.terms("tasks.assigneeIds");

			termsQuery.addValues(
				Stream.of(
					assigneeIds
				).filter(
					assigneeId -> assigneeId > 0
				).map(
					String::valueOf
				).toArray(
					Object[]::new
				));

			nestedBooleanQuery.addShouldQueryClauses(termsQuery);

			if (ArrayUtil.contains(assigneeIds, -1L)) {
				nestedBooleanQuery.addShouldQueryClauses(
					_queries.term("tasks.assigneeType", Role.class.getName()));
			}

			booleanQuery.addMustQueryClauses(
				_queries.nested("tasks", nestedBooleanQuery));
		}

		if (ArrayUtil.isNotEmpty(classPKs)) {
			TermsQuery termsQuery = _queries.terms("classPK");

			termsQuery.addValues(
				Stream.of(
					classPKs
				).map(
					String::valueOf
				).toArray(
					Object[]::new
				));

			booleanQuery.addMustQueryClauses(termsQuery);
		}

		if (completed != null) {
			booleanQuery.addMustQueryClauses(
				_queries.term("completed", completed));
		}

		if ((dateEnd != null) && (dateStart != null)) {
			booleanQuery.addMustQueryClauses(
				_queries.dateRangeTerm(
					"completionDate", true, true, _getDate(dateStart),
					_getDate(dateEnd)));
		}

		if (startInstanceId != null) {
			booleanQuery.addMustQueryClauses(
				_queries.rangeTerm(
					"instanceId", false, false, startInstanceId, null));
		}

		if (ArrayUtil.isNotEmpty(slaStatuses)) {
			TermsQuery termsQuery = _queries.terms("slaStatus");

			termsQuery.addValues(slaStatuses);

			booleanQuery.addMustQueryClauses(termsQuery);
		}

		if (ArrayUtil.isNotEmpty(taskNames)) {
			TermsQuery termsQuery = _queries.terms("tasks.taskName");

			termsQuery.addValues(taskNames);

			booleanQuery.addMustQueryClauses(
				_queries.nested("tasks", termsQuery));
		}

		return booleanQuery.addMustQueryClauses(
			_queries.term("deleted", Boolean.FALSE),
			_queries.term("processId", processId));
	}

	private SLAResult _createSLAResult(Map<String, Object> sourcesMap) {
		return new SLAResult() {
			{
				dateOverdue = _parseDate(
					GetterUtil.getString(sourcesMap.get("overdueDate")));

				id = GetterUtil.getLong(sourcesMap.get("slaDefinitionId"));

				name = _getSLAName(id);

				onTime = GetterUtil.getBoolean(sourcesMap.get("onTime"));
				remainingTime = GetterUtil.getLong(
					sourcesMap.get("remainingTime"));
				status = _getSLAResultStatus(
					GetterUtil.getString(sourcesMap.get("status")));
			}
		};
	}

	private BooleanQuery _createTasksBooleanQuery(
		long processId, long instanceId) {

		BooleanQuery booleanQuery = _queries.booleanQuery();

		booleanQuery.addMustNotQueryClauses(_queries.term("taskId", 0));

		return booleanQuery.addMustQueryClauses(
			_queries.term("companyId", contextCompany.getCompanyId()),
			_queries.term("completed", Boolean.FALSE),
			_queries.term("deleted", Boolean.FALSE),
			_queries.term("instanceId", instanceId),
			_queries.term("processId", processId));
	}

	private List<Assignee> _getAssignees(Bucket bucket) {
		List<Assignee> assignees = new ArrayList<>();

		FilterAggregationResult filterAggregationResult =
			(FilterAggregationResult)bucket.getChildAggregationResult(
				"tasksIndex");

		TermsAggregationResult assigneeTypeAggregationResult =
			(TermsAggregationResult)
				filterAggregationResult.getChildAggregationResult(
					"assigneeType");

		Bucket userBucket = assigneeTypeAggregationResult.getBucket(
			User.class.getName());

		if (userBucket != null) {
			TermsAggregationResult termsAggregationResult =
				(TermsAggregationResult)userBucket.getChildAggregationResult(
					"assigneeId");

			Collection<Bucket> buckets = termsAggregationResult.getBuckets();

			Stream<Bucket> stream = buckets.stream();

			stream.map(
				Bucket::getKey
			).map(
				GetterUtil::getLong
			).map(
				userId -> AssigneeUtil.toAssignee(
					_language, _portal,
					ResourceBundleUtil.getModuleAndPortalResourceBundle(
						contextAcceptLanguage.getPreferredLocale(),
						InstanceResourceImpl.class),
					userId, _userLocalService::fetchUser)
			).sorted(
				Comparator.comparing(
					Assignee::getName,
					Comparator.nullsLast(String::compareToIgnoreCase))
			).filter(
				Objects::nonNull
			).forEachOrdered(
				assignees::add
			);
		}

		Bucket roleBucket = assigneeTypeAggregationResult.getBucket(
			Role.class.getName());

		if (roleBucket != null) {
			boolean reviewer = false;

			TermsAggregationResult termsAggregationResult =
				(TermsAggregationResult)roleBucket.getChildAggregationResult(
					"assigneeId");

			for (Bucket assigneeIdBucket :
					termsAggregationResult.getBuckets()) {

				long roleId = GetterUtil.getLong(assigneeIdBucket.getKey());

				if (ArrayUtil.contains(contextUser.getRoleIds(), roleId)) {
					reviewer = true;

					break;
				}
			}

			assignees.add(_createAssignee(reviewer));
		}

		return assignees;
	}

	private String _getDate(Date date) {
		try {
			return DateUtil.getDate(
				date, "yyyyMMddHHmmss", LocaleUtil.getDefault());
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(exception, exception);
			}

			return null;
		}
	}

	private long _getInstanceCount(
		Long[] assigneeIds, Long[] classPKs, Boolean completed, Date dateEnd,
		Date dateStart, long processId, String[] slaStatuses,
		String[] taskNames) {

		CountSearchRequest countSearchRequest = new CountSearchRequest();

		countSearchRequest.setIndexNames(
			_instanceWorkflowMetricsIndexNameBuilder.getIndexName(
				contextCompany.getCompanyId()));

		BooleanQuery booleanQuery = _queries.booleanQuery();

		countSearchRequest.setQuery(
			booleanQuery.addFilterQueryClauses(
				_createInstancesBooleanQuery(
					assigneeIds, classPKs, completed, dateEnd, dateStart,
					processId, slaStatuses, null, taskNames)));

		CountSearchResponse countSearchResponse =
			_searchRequestExecutor.executeSearchRequest(countSearchRequest);

		return countSearchResponse.getCount();
	}

	private long _getInstanceId(
		Long[] assigneeIds, Long[] classPKs, Boolean completed, Date dateEnd,
		Date dateStart, long processId, String[] slaStatuses,
		long startInstanceId, String[] taskNames) {

		SearchSearchRequest searchSearchRequest = new SearchSearchRequest();

		searchSearchRequest.addSorts(_sorts.field("instanceId", SortOrder.ASC));
		searchSearchRequest.setSelectedFieldNames("instanceId");
		searchSearchRequest.setIndexNames(
			_instanceWorkflowMetricsIndexNameBuilder.getIndexName(
				contextCompany.getCompanyId()));

		BooleanQuery booleanQuery = _queries.booleanQuery();

		searchSearchRequest.setQuery(
			booleanQuery.addFilterQueryClauses(
				_createInstancesBooleanQuery(
					assigneeIds, classPKs, completed, dateEnd, dateStart,
					processId, slaStatuses, startInstanceId, taskNames)));

		searchSearchRequest.setSize(1);
		searchSearchRequest.setStart(9999);

		return Stream.of(
			_searchRequestExecutor.executeSearchRequest(searchSearchRequest)
		).map(
			SearchSearchResponse::getSearchHits
		).map(
			SearchHits::getSearchHits
		).flatMap(
			List::stream
		).map(
			SearchHit::getDocument
		).mapToLong(
			document -> document.getLong("instanceId")
		).findFirst(
		).orElse(
			startInstanceId
		);
	}

	private Collection<Instance> _getInstances(
		Long[] assigneeIds, Long[] classPKs, Boolean completed, Date dateEnd,
		Date dateStart, Pagination pagination, long processId,
		String[] slaStatuses, Long startInstanceId, String[] taskNames) {

		SearchSearchRequest searchSearchRequest = new SearchSearchRequest();

		searchSearchRequest.addSorts(_sorts.field("instanceId", SortOrder.ASC));
		searchSearchRequest.setFetchSource(true);
		searchSearchRequest.setSelectedFieldNames("");
		searchSearchRequest.setIndexNames(
			_instanceWorkflowMetricsIndexNameBuilder.getIndexName(
				contextCompany.getCompanyId()));

		BooleanQuery booleanQuery = _queries.booleanQuery();

		searchSearchRequest.setQuery(
			booleanQuery.addFilterQueryClauses(
				_createInstancesBooleanQuery(
					assigneeIds, classPKs, completed, dateEnd, dateStart,
					processId, slaStatuses, startInstanceId, taskNames)));

		searchSearchRequest.setSize(pagination.getPageSize());
		searchSearchRequest.setStart(pagination.getStartPosition());

		return Stream.of(
			_searchRequestExecutor.executeSearchRequest(searchSearchRequest)
		).map(
			SearchSearchResponse::getSearchHits
		).map(
			SearchHits::getSearchHits
		).flatMap(
			List::stream
		).map(
			SearchHit::getDocument
		).map(
			this::_createInstance
		).collect(
			Collectors.toList()
		);
	}

	private String _getLocalizedName(String name) {
		return Field.getLocalizedName(
			contextAcceptLanguage.getPreferredLocale(), name);
	}

	private List<String> _getNextTransitionNames(
		long processId, long instanceId) {

		SearchSearchRequest searchSearchRequest = new SearchSearchRequest();

		TermsAggregation termsAggregation = _aggregations.terms(
			"nodeId", "nodeId");

		termsAggregation.setSize(10000);

		FilterAggregation countFilterAggregation = _aggregations.filter(
			"countFilter",
			_queries.term(
				"_index",
				_taskWorkflowMetricsIndexNameBuilder.getIndexName(
					contextCompany.getCompanyId())));

		countFilterAggregation.addChildrenAggregations(
			_aggregations.valueCount("nodeCount", "nodeId"));

		FilterAggregation nameFilterAggregation = _aggregations.filter(
			"nameFilter",
			_queries.term(
				"_index",
				_transitionWorkflowMetricsIndexNameBuilder.getIndexName(
					contextCompany.getCompanyId())));

		TermsAggregation nameTermsAggregation = _aggregations.terms(
			"name", "name");

		nameTermsAggregation.setSize(10000);

		nameFilterAggregation.addChildAggregation(nameTermsAggregation);

		termsAggregation.addChildrenAggregations(
			countFilterAggregation, nameFilterAggregation);

		BucketSelectorPipelineAggregation bucketSelectorPipelineAggregation =
			_aggregations.bucketSelector(
				"bucketSelector", _scripts.script("params.nodeCount > 0"));

		bucketSelectorPipelineAggregation.addBucketPath(
			"nodeCount", "countFilter>nodeCount.value");

		termsAggregation.addPipelineAggregations(
			bucketSelectorPipelineAggregation);

		searchSearchRequest.addAggregation(termsAggregation);

		searchSearchRequest.setIndexNames(
			_taskWorkflowMetricsIndexNameBuilder.getIndexName(
				contextCompany.getCompanyId()),
			_transitionWorkflowMetricsIndexNameBuilder.getIndexName(
				contextCompany.getCompanyId()));

		BooleanQuery booleanQuery = _queries.booleanQuery();

		searchSearchRequest.setQuery(
			booleanQuery.addFilterQueryClauses(
				_createBooleanQuery(processId, instanceId)));

		return Stream.of(
			_searchRequestExecutor.executeSearchRequest(searchSearchRequest)
		).map(
			SearchSearchResponse::getAggregationResultsMap
		).map(
			aggregationResultsMap ->
				(TermsAggregationResult)aggregationResultsMap.get("nodeId")
		).map(
			TermsAggregationResult::getBuckets
		).flatMap(
			Collection::stream
		).map(
			bucket -> Stream.of(
				(FilterAggregationResult)bucket.getChildAggregationResult(
					"nameFilter")
			).map(
				filterAggregationResult ->
					(TermsAggregationResult)
						filterAggregationResult.getChildAggregationResult(
							"name")
			).map(
				TermsAggregationResult::getBuckets
			).flatMap(
				Collection::stream
			).map(
				Bucket::getKey
			).collect(
				Collectors.toCollection(ArrayList::new)
			)
		).flatMap(
			Collection::stream
		).sorted(
		).collect(
			Collectors.toCollection(ArrayList::new)
		);
	}

	private String _getSLAName(long slaDefinitionId) {
		try {
			WorkflowMetricsSLADefinition workflowMetricsSLADefinition =
				_workflowMetricsSLADefinitionLocalService.
					getWorkflowMetricsSLADefinition(slaDefinitionId);

			return workflowMetricsSLADefinition.getName();
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(exception, exception);
			}

			return null;
		}
	}

	private SLAResult.Status _getSLAResultStatus(String status) {
		if (Objects.equals(status, WorkflowMetricsSLAStatus.COMPLETED.name()) ||
			Objects.equals(status, WorkflowMetricsSLAStatus.STOPPED.name())) {

			return SLAResult.Status.STOPPED;
		}
		else if (Objects.equals(
					status, WorkflowMetricsSLAStatus.PAUSED.name())) {

			return SLAResult.Status.PAUSED;
		}

		return SLAResult.Status.RUNNING;
	}

	private List<String> _getTaskNames(Bucket bucket) {
		TermsAggregationResult termsAggregationResult =
			(TermsAggregationResult)bucket.getChildAggregationResult("name");

		Collection<Bucket> buckets = termsAggregationResult.getBuckets();

		Stream<Bucket> stream = buckets.stream();

		return stream.map(
			Bucket::getKey
		).map(
			taskName -> _language.get(
				ResourceBundleUtil.getModuleAndPortalResourceBundle(
					contextAcceptLanguage.getPreferredLocale(),
					InstanceResourceImpl.class),
				taskName)
		).collect(
			Collectors.toList()
		);
	}

	private Date _parseDate(String dateString) {
		try {
			return DateUtil.parseDate(
				"yyyyMMddHHmmss", dateString, LocaleUtil.getDefault());
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(exception, exception);
			}

			return null;
		}
	}

	private void _populateWithTasks(Document document, Instance instance) {
		SortedSet<Assignee> assignees = new TreeSet<>(
			Comparator.comparing(
				Assignee::getName,
				Comparator.nullsLast(String::compareToIgnoreCase)));

		SortedSet<String> taskNames = new TreeSet<>();

		for (Object taskObject : document.getValues("tasks")) {
			Map<String, Object> task = (Map<String, Object>)taskObject;

			if (Objects.equals(
					task.get("assigneeType"), User.class.getName())) {

				for (Object assigneeId : (List<?>)task.get("assigneeIds")) {
					Assignee assignee = AssigneeUtil.toAssignee(
						_language, _portal,
						ResourceBundleUtil.getModuleAndPortalResourceBundle(
							contextAcceptLanguage.getPreferredLocale(),
							InstanceResourceImpl.class),
						GetterUtil.getLong(assigneeId),
						_userLocalService::fetchUser);

					if (assignee != null) {
						assignees.add(assignee);
					}
				}
			}
			else if (Objects.equals(
						task.get("assigneeType"), Role.class.getName())) {

				boolean reviewer = false;

				for (Object assigneeId : (List<?>)task.get("assigneeIds")) {
					if (ArrayUtil.contains(
							contextUser.getRoleIds(),
							GetterUtil.getLong(assigneeId))) {

						reviewer = true;

						break;
					}
				}

				assignees.add(_createAssignee(reviewer));
			}

			taskNames.add(
				_language.get(
					ResourceBundleUtil.getModuleAndPortalResourceBundle(
						contextAcceptLanguage.getPreferredLocale(),
						InstanceResourceImpl.class),
					(String)task.get("taskName")));
		}

		instance.setAssignees(assignees.toArray(new Assignee[0]));
		instance.setTaskNames(taskNames.toArray(new String[0]));

		if ((assignees.size() == 1) && (taskNames.size() == 1)) {
			Assignee assignee = assignees.first();

			if (Objects.equals(assignee.getId(), contextUser.getUserId())) {
				instance.setTransitions(_toTransitions(instance));
			}
		}
	}

	private void _setAssignees(Bucket bucket, Instance instance) {
		List<Assignee> assignees = _getAssignees(bucket);

		if (ListUtil.isNull(assignees)) {
			return;
		}

		instance.setAssignees(assignees.toArray(new Assignee[0]));
	}

	private void _setSLAResults(Bucket bucket, Instance instance) {
		instance.setSlaResults(
			Stream.of(
				(TermsAggregationResult)bucket.getChildAggregationResult(
					"slaDefinitionId")
			).map(
				TermsAggregationResult::getBuckets
			).flatMap(
				Collection::stream
			).map(
				childBucket -> Stream.of(
					(TopHitsAggregationResult)
						childBucket.getChildAggregationResult("topHits")
				).map(
					TopHitsAggregationResult::getSearchHits
				).map(
					SearchHits::getSearchHits
				).flatMap(
					List::stream
				).findFirst(
				).map(
					SearchHit::getSourcesMap
				).map(
					this::_createSLAResult
				).orElseGet(
					SLAResult::new
				)
			).toArray(
				SLAResult[]::new
			));
	}

	private void _setTaskNames(Bucket bucket, Instance instance) {
		List<String> taskNames = _getTaskNames(bucket);

		if (ListUtil.isNull(taskNames)) {
			return;
		}

		instance.setTaskNames(taskNames.toArray(new String[0]));
	}

	private void _setTransitions(Instance instance) {
		if (ArrayUtil.isEmpty(instance.getAssignees()) ||
			(ArrayUtil.getLength(instance.getTaskNames()) != 1)) {

			return;
		}

		Assignee[] assignees = instance.getAssignees();

		if (!Objects.equals(assignees[0].getId(), contextUser.getUserId())) {
			return;
		}

		instance.setTransitions(_toTransitions(instance));
	}

	private Creator _toCreator(Long userId) {
		if (Objects.isNull(userId)) {
			return null;
		}

		User user = _userLocalService.fetchUser(userId);

		return new Creator() {
			{
				id = userId;

				setName(
					() -> {
						if (user == null) {
							return String.valueOf(userId);
						}

						return user.getFullName();
					});
			}
		};
	}

	private Transition _toTransition(String name) {
		Transition transition = new Transition();

		transition.setLabel(
			_language.get(
				ResourceBundleUtil.getModuleAndPortalResourceBundle(
					contextAcceptLanguage.getPreferredLocale(),
					InstanceResourceImpl.class),
				name));
		transition.setName(name);

		return transition;
	}

	private Transition[] _toTransitions(Instance instance) {
		return transformToArray(
			_getNextTransitionNames(instance.getProcessId(), instance.getId()),
			this::_toTransition, Transition.class);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		InstanceResourceImpl.class);

	@Reference
	private Aggregations _aggregations;

	@Reference
	private InstanceWorkflowMetricsIndexer _instanceWorkflowMetricsIndexer;

	@Reference(target = "(workflow.metrics.index.entity.name=instance)")
	private WorkflowMetricsIndexNameBuilder
		_instanceWorkflowMetricsIndexNameBuilder;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference
	private Queries _queries;

	@Reference
	private ResourceHelper _resourceHelper;

	@Reference
	private Scripts _scripts;

	@Reference
	private SearchRequestExecutor _searchRequestExecutor;

	@Reference(
		target = "(workflow.metrics.index.entity.name=sla-instance-result)"
	)
	private WorkflowMetricsIndexNameBuilder
		_slaInstanceResultWorkflowMetricsIndexNameBuilder;

	@Reference
	private Sorts _sorts;

	@Reference(target = "(workflow.metrics.index.entity.name=task)")
	private WorkflowMetricsIndexNameBuilder
		_taskWorkflowMetricsIndexNameBuilder;

	@Reference(target = "(workflow.metrics.index.entity.name=transition)")
	private WorkflowMetricsIndexNameBuilder
		_transitionWorkflowMetricsIndexNameBuilder;

	@Reference
	private UserLocalService _userLocalService;

	@Reference
	private WorkflowMetricsSLADefinitionLocalService
		_workflowMetricsSLADefinitionLocalService;

}