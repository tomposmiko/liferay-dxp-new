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

package com.liferay.headless.workflow.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import com.liferay.headless.workflow.dto.v1_0.WorkflowLog;
import com.liferay.headless.workflow.resource.v1_0.WorkflowLogResource;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.InvocationTargetException;

import java.net.URL;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseWorkflowLogResourceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	@Before
	public void setUp() throws Exception {
		irrelevantGroup = GroupTestUtil.addGroup();
		testGroup = GroupTestUtil.addGroup();

		_resourceURL = new URL(
			"http://localhost:8080/o/headless-workflow/v1.0");
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testGetWorkflowLog() throws Exception {
		WorkflowLog postWorkflowLog = testGetWorkflowLog_addWorkflowLog();

		WorkflowLog getWorkflowLog = invokeGetWorkflowLog(
			postWorkflowLog.getId());

		assertEquals(postWorkflowLog, getWorkflowLog);
		assertValid(getWorkflowLog);
	}

	protected WorkflowLog testGetWorkflowLog_addWorkflowLog() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected WorkflowLog invokeGetWorkflowLog(Long workflowLogId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath("/workflow-logs/{workflow-log-id}", workflowLogId);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(string, WorkflowLog.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokeGetWorkflowLogResponse(Long workflowLogId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath("/workflow-logs/{workflow-log-id}", workflowLogId);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetWorkflowTaskWorkflowLogsPage() throws Exception {
		Long workflowTaskId =
			testGetWorkflowTaskWorkflowLogsPage_getWorkflowTaskId();
		Long irrelevantWorkflowTaskId =
			testGetWorkflowTaskWorkflowLogsPage_getIrrelevantWorkflowTaskId();

		if ((irrelevantWorkflowTaskId != null)) {
			WorkflowLog irrelevantWorkflowLog =
				testGetWorkflowTaskWorkflowLogsPage_addWorkflowLog(
					irrelevantWorkflowTaskId, randomIrrelevantWorkflowLog());

			Page<WorkflowLog> page = invokeGetWorkflowTaskWorkflowLogsPage(
				irrelevantWorkflowTaskId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantWorkflowLog),
				(List<WorkflowLog>)page.getItems());
			assertValid(page);
		}

		WorkflowLog workflowLog1 =
			testGetWorkflowTaskWorkflowLogsPage_addWorkflowLog(
				workflowTaskId, randomWorkflowLog());

		WorkflowLog workflowLog2 =
			testGetWorkflowTaskWorkflowLogsPage_addWorkflowLog(
				workflowTaskId, randomWorkflowLog());

		Page<WorkflowLog> page = invokeGetWorkflowTaskWorkflowLogsPage(
			workflowTaskId, Pagination.of(1, 2));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(workflowLog1, workflowLog2),
			(List<WorkflowLog>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetWorkflowTaskWorkflowLogsPageWithPagination()
		throws Exception {

		Long workflowTaskId =
			testGetWorkflowTaskWorkflowLogsPage_getWorkflowTaskId();

		WorkflowLog workflowLog1 =
			testGetWorkflowTaskWorkflowLogsPage_addWorkflowLog(
				workflowTaskId, randomWorkflowLog());

		WorkflowLog workflowLog2 =
			testGetWorkflowTaskWorkflowLogsPage_addWorkflowLog(
				workflowTaskId, randomWorkflowLog());

		WorkflowLog workflowLog3 =
			testGetWorkflowTaskWorkflowLogsPage_addWorkflowLog(
				workflowTaskId, randomWorkflowLog());

		Page<WorkflowLog> page1 = invokeGetWorkflowTaskWorkflowLogsPage(
			workflowTaskId, Pagination.of(1, 2));

		List<WorkflowLog> workflowLogs1 = (List<WorkflowLog>)page1.getItems();

		Assert.assertEquals(workflowLogs1.toString(), 2, workflowLogs1.size());

		Page<WorkflowLog> page2 = invokeGetWorkflowTaskWorkflowLogsPage(
			workflowTaskId, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<WorkflowLog> workflowLogs2 = (List<WorkflowLog>)page2.getItems();

		Assert.assertEquals(workflowLogs2.toString(), 1, workflowLogs2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(workflowLog1, workflowLog2, workflowLog3),
			new ArrayList<WorkflowLog>() {
				{
					addAll(workflowLogs1);
					addAll(workflowLogs2);
				}
			});
	}

	protected WorkflowLog testGetWorkflowTaskWorkflowLogsPage_addWorkflowLog(
			Long workflowTaskId, WorkflowLog workflowLog)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetWorkflowTaskWorkflowLogsPage_getWorkflowTaskId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetWorkflowTaskWorkflowLogsPage_getIrrelevantWorkflowTaskId()
		throws Exception {

		return null;
	}

	protected Page<WorkflowLog> invokeGetWorkflowTaskWorkflowLogsPage(
			Long workflowTaskId, Pagination pagination)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/workflow-tasks/{workflow-task-id}/workflow-logs",
					workflowTaskId);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		return _outputObjectMapper.readValue(
			string,
			new TypeReference<Page<WorkflowLog>>() {
			});
	}

	protected Http.Response invokeGetWorkflowTaskWorkflowLogsPageResponse(
			Long workflowTaskId, Pagination pagination)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/workflow-tasks/{workflow-task-id}/workflow-logs",
					workflowTaskId);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	protected void assertResponseCode(
		int expectedResponseCode, Http.Response actualResponse) {

		Assert.assertEquals(
			expectedResponseCode, actualResponse.getResponseCode());
	}

	protected void assertEquals(
		WorkflowLog workflowLog1, WorkflowLog workflowLog2) {

		Assert.assertTrue(
			workflowLog1 + " does not equal " + workflowLog2,
			equals(workflowLog1, workflowLog2));
	}

	protected void assertEquals(
		List<WorkflowLog> workflowLogs1, List<WorkflowLog> workflowLogs2) {

		Assert.assertEquals(workflowLogs1.size(), workflowLogs2.size());

		for (int i = 0; i < workflowLogs1.size(); i++) {
			WorkflowLog workflowLog1 = workflowLogs1.get(i);
			WorkflowLog workflowLog2 = workflowLogs2.get(i);

			assertEquals(workflowLog1, workflowLog2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<WorkflowLog> workflowLogs1, List<WorkflowLog> workflowLogs2) {

		Assert.assertEquals(workflowLogs1.size(), workflowLogs2.size());

		for (WorkflowLog workflowLog1 : workflowLogs1) {
			boolean contains = false;

			for (WorkflowLog workflowLog2 : workflowLogs2) {
				if (equals(workflowLog1, workflowLog2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				workflowLogs2 + " does not contain " + workflowLog1, contains);
		}
	}

	protected void assertValid(WorkflowLog workflowLog) {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertValid(Page<WorkflowLog> page) {
		boolean valid = false;

		Collection<WorkflowLog> workflowLogs = page.getItems();

		int size = workflowLogs.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected boolean equals(
		WorkflowLog workflowLog1, WorkflowLog workflowLog2) {

		if (workflowLog1 == workflowLog2) {
			return true;
		}

		return false;
	}

	protected Collection<EntityField> getEntityFields() throws Exception {
		if (!(_workflowLogResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_workflowLogResource;

		EntityModel entityModel = entityModelResource.getEntityModel(
			new MultivaluedHashMap());

		Map<String, EntityField> entityFieldsMap =
			entityModel.getEntityFieldsMap();

		return entityFieldsMap.values();
	}

	protected List<EntityField> getEntityFields(EntityField.Type type)
		throws Exception {

		Collection<EntityField> entityFields = getEntityFields();

		Stream<EntityField> stream = entityFields.stream();

		return stream.filter(
			entityField -> Objects.equals(entityField.getType(), type)
		).collect(
			Collectors.toList()
		);
	}

	protected String getFilterString(
		EntityField entityField, String operator, WorkflowLog workflowLog) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("auditPerson")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("commentLog")) {
			sb.append("'");
			sb.append(String.valueOf(workflowLog.getCommentLog()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("dateCreated")) {
			sb.append(_dateFormat.format(workflowLog.getDateCreated()));

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("person")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("previousPerson")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("previousState")) {
			sb.append("'");
			sb.append(String.valueOf(workflowLog.getPreviousState()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("state")) {
			sb.append("'");
			sb.append(String.valueOf(workflowLog.getState()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("taskId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("type")) {
			sb.append("'");
			sb.append(String.valueOf(workflowLog.getType()));
			sb.append("'");

			return sb.toString();
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected WorkflowLog randomWorkflowLog() {
		return new WorkflowLog() {
			{
				commentLog = RandomTestUtil.randomString();
				dateCreated = RandomTestUtil.nextDate();
				id = RandomTestUtil.randomLong();
				previousState = RandomTestUtil.randomString();
				state = RandomTestUtil.randomString();
				taskId = RandomTestUtil.randomLong();
				type = RandomTestUtil.randomString();
			}
		};
	}

	protected WorkflowLog randomIrrelevantWorkflowLog() {
		return randomWorkflowLog();
	}

	protected WorkflowLog randomPatchWorkflowLog() {
		return randomWorkflowLog();
	}

	protected Group irrelevantGroup;
	protected Group testGroup;

	protected static class Page<T> {

		public Collection<T> getItems() {
			return new ArrayList<>(items);
		}

		public long getLastPage() {
			return lastPage;
		}

		public long getPage() {
			return page;
		}

		public long getPageSize() {
			return pageSize;
		}

		public long getTotalCount() {
			return totalCount;
		}

		@JsonProperty
		protected Collection<T> items;

		@JsonProperty
		protected long lastPage;

		@JsonProperty
		protected long page;

		@JsonProperty
		protected long pageSize;

		@JsonProperty
		protected long totalCount;

	}

	private Http.Options _createHttpOptions() {
		Http.Options options = new Http.Options();

		options.addHeader("Accept", "application/json");

		String userNameAndPassword = "test@liferay.com:test";

		String encodedUserNameAndPassword = Base64.encode(
			userNameAndPassword.getBytes());

		options.addHeader(
			"Authorization", "Basic " + encodedUserNameAndPassword);

		options.addHeader("Content-Type", "application/json");

		return options;
	}

	private String _toPath(String template, Object... values) {
		if (ArrayUtil.isEmpty(values)) {
			return template;
		}

		for (int i = 0; i < values.length; i++) {
			template = template.replaceFirst(
				"\\{.*?\\}", String.valueOf(values[i]));
		}

		return template;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseWorkflowLogResourceTestCase.class);

	private static BeanUtilsBean _beanUtilsBean = new BeanUtilsBean() {

		@Override
		public void copyProperty(Object bean, String name, Object value)
			throws IllegalAccessException, InvocationTargetException {

			if (value != null) {
				super.copyProperty(bean, name, value);
			}
		}

	};
	private static DateFormat _dateFormat;
	private final static ObjectMapper _inputObjectMapper = new ObjectMapper() {
		{
			setFilterProvider(
				new SimpleFilterProvider() {
					{
						addFilter(
							"Liferay.Vulcan",
							SimpleBeanPropertyFilter.serializeAll());
					}
				});
			setSerializationInclusion(JsonInclude.Include.NON_NULL);
		}
	};
	private final static ObjectMapper _outputObjectMapper = new ObjectMapper() {
		{
			setFilterProvider(
				new SimpleFilterProvider() {
					{
						addFilter(
							"Liferay.Vulcan",
							SimpleBeanPropertyFilter.serializeAll());
					}
				});
		}
	};

	@Inject
	private WorkflowLogResource _workflowLogResource;

	private URL _resourceURL;

}