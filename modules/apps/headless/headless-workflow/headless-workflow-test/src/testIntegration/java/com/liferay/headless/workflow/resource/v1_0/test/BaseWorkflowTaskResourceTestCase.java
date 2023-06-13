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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.headless.workflow.dto.v1_0.ObjectReviewed;
import com.liferay.headless.workflow.dto.v1_0.WorkflowLog;
import com.liferay.headless.workflow.dto.v1_0.WorkflowTask;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseWorkflowTaskResourceTestCase {

	@Before
	public void setUp() throws Exception {
		testGroup = GroupTestUtil.addGroup();

		_resourceURL = new URL(
			"http://localhost:8080/o/headless-workflow/v1.0");
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testGetRoleWorkflowTasksPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetWorkflowTasksPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetWorkflowTask() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPostWorkflowTaskAssignToMe() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPostWorkflowTaskAssignToUser() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPostWorkflowTaskChangeTransition() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPostWorkflowTaskUpdateDueDate() throws Exception {
			Assert.assertTrue(true);
	}

	protected void assertResponseCode(int expectedResponseCode, Http.Response actualResponse) {
		Assert.assertEquals(expectedResponseCode, actualResponse.getResponseCode());
	}

	protected Page<WorkflowTask> invokeGetRoleWorkflowTasksPage(
				Long roleId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/roles/{role-id}/workflow-tasks", roleId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<WorkflowTaskImpl>>() {});
	}

	protected Http.Response invokeGetRoleWorkflowTasksPageResponse(
				Long roleId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/roles/{role-id}/workflow-tasks", roleId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Page<WorkflowTask> invokeGetWorkflowTasksPage(
				Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/workflow-tasks", pagination));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<WorkflowTaskImpl>>() {});
	}

	protected Http.Response invokeGetWorkflowTasksPageResponse(
				Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/workflow-tasks", pagination));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected WorkflowTask invokeGetWorkflowTask(
				Long workflowTaskId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/workflow-tasks/{workflow-task-id}", workflowTaskId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), WorkflowTaskImpl.class);
	}

	protected Http.Response invokeGetWorkflowTaskResponse(
				Long workflowTaskId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/workflow-tasks/{workflow-task-id}", workflowTaskId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected WorkflowTask invokePostWorkflowTaskAssignToMe(
				Long workflowTaskId,WorkflowTask workflowTask)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(workflowTask), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/workflow-tasks/{workflow-task-id}/assign-to-me", workflowTaskId));

				options.setPost(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), WorkflowTaskImpl.class);
	}

	protected Http.Response invokePostWorkflowTaskAssignToMeResponse(
				Long workflowTaskId,WorkflowTask workflowTask)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(workflowTask), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/workflow-tasks/{workflow-task-id}/assign-to-me", workflowTaskId));

				options.setPost(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected WorkflowTask invokePostWorkflowTaskAssignToUser(
				Long workflowTaskId,WorkflowTask workflowTask)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(workflowTask), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/workflow-tasks/{workflow-task-id}/assign-to-user", workflowTaskId));

				options.setPost(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), WorkflowTaskImpl.class);
	}

	protected Http.Response invokePostWorkflowTaskAssignToUserResponse(
				Long workflowTaskId,WorkflowTask workflowTask)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(workflowTask), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/workflow-tasks/{workflow-task-id}/assign-to-user", workflowTaskId));

				options.setPost(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected WorkflowTask invokePostWorkflowTaskChangeTransition(
				Long workflowTaskId,WorkflowTask workflowTask)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(workflowTask), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/workflow-tasks/{workflow-task-id}/change-transition", workflowTaskId));

				options.setPost(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), WorkflowTaskImpl.class);
	}

	protected Http.Response invokePostWorkflowTaskChangeTransitionResponse(
				Long workflowTaskId,WorkflowTask workflowTask)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(workflowTask), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/workflow-tasks/{workflow-task-id}/change-transition", workflowTaskId));

				options.setPost(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected WorkflowTask invokePostWorkflowTaskUpdateDueDate(
				Long workflowTaskId,WorkflowTask workflowTask)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(workflowTask), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/workflow-tasks/{workflow-task-id}/update-due-date", workflowTaskId));

				options.setPost(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), WorkflowTaskImpl.class);
	}

	protected Http.Response invokePostWorkflowTaskUpdateDueDateResponse(
				Long workflowTaskId,WorkflowTask workflowTask)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(workflowTask), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/workflow-tasks/{workflow-task-id}/update-due-date", workflowTaskId));

				options.setPost(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}

	protected void assertEquals(WorkflowTask workflowTask1, WorkflowTask workflowTask2) {
		Assert.assertTrue(workflowTask1 + " does not equal " + workflowTask2, equals(workflowTask1, workflowTask2));
	}

	protected void assertEquals(List<WorkflowTask> workflowTasks1, List<WorkflowTask> workflowTasks2) {
		Assert.assertEquals(workflowTasks1.size(), workflowTasks2.size());

		for (int i = 0; i < workflowTasks1.size(); i++) {
			WorkflowTask workflowTask1 = workflowTasks1.get(i);
			WorkflowTask workflowTask2 = workflowTasks2.get(i);

			assertEquals(workflowTask1, workflowTask2);
	}
	}

	protected boolean equals(WorkflowTask workflowTask1, WorkflowTask workflowTask2) {
		if (workflowTask1 == workflowTask2) {
			return true;
	}

		return false;
	}

	protected WorkflowTask randomWorkflowTask() {
		return new WorkflowTaskImpl() {
			{

						completed = RandomTestUtil.randomBoolean();
						dateCompleted = RandomTestUtil.nextDate();
						dateCreated = RandomTestUtil.nextDate();
						definitionName = RandomTestUtil.randomString();
						description = RandomTestUtil.randomString();
						dueDate = RandomTestUtil.nextDate();
						id = RandomTestUtil.randomLong();
						name = RandomTestUtil.randomString();
	}
		};
	}

	protected Group testGroup;

	protected static class WorkflowTaskImpl implements WorkflowTask {

	public Boolean getCompleted() {
				return completed;
	}

	public void setCompleted(Boolean completed) {
				this.completed = completed;
	}

	@JsonIgnore
	public void setCompleted(
				UnsafeSupplier<Boolean, Throwable> completedUnsafeSupplier) {

				try {
					completed = completedUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Boolean completed;
	public Date getDateCompleted() {
				return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
				this.dateCompleted = dateCompleted;
	}

	@JsonIgnore
	public void setDateCompleted(
				UnsafeSupplier<Date, Throwable> dateCompletedUnsafeSupplier) {

				try {
					dateCompleted = dateCompletedUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Date dateCompleted;
	public Date getDateCreated() {
				return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
				this.dateCreated = dateCreated;
	}

	@JsonIgnore
	public void setDateCreated(
				UnsafeSupplier<Date, Throwable> dateCreatedUnsafeSupplier) {

				try {
					dateCreated = dateCreatedUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Date dateCreated;
	public String getDefinitionName() {
				return definitionName;
	}

	public void setDefinitionName(String definitionName) {
				this.definitionName = definitionName;
	}

	@JsonIgnore
	public void setDefinitionName(
				UnsafeSupplier<String, Throwable> definitionNameUnsafeSupplier) {

				try {
					definitionName = definitionNameUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String definitionName;
	public String getDescription() {
				return description;
	}

	public void setDescription(String description) {
				this.description = description;
	}

	@JsonIgnore
	public void setDescription(
				UnsafeSupplier<String, Throwable> descriptionUnsafeSupplier) {

				try {
					description = descriptionUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String description;
	public Date getDueDate() {
				return dueDate;
	}

	public void setDueDate(Date dueDate) {
				this.dueDate = dueDate;
	}

	@JsonIgnore
	public void setDueDate(
				UnsafeSupplier<Date, Throwable> dueDateUnsafeSupplier) {

				try {
					dueDate = dueDateUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Date dueDate;
	public Long getId() {
				return id;
	}

	public void setId(Long id) {
				this.id = id;
	}

	@JsonIgnore
	public void setId(
				UnsafeSupplier<Long, Throwable> idUnsafeSupplier) {

				try {
					id = idUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long id;
	public WorkflowLog[] getLogs() {
				return logs;
	}

	public void setLogs(WorkflowLog[] logs) {
				this.logs = logs;
	}

	@JsonIgnore
	public void setLogs(
				UnsafeSupplier<WorkflowLog[], Throwable> logsUnsafeSupplier) {

				try {
					logs = logsUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected WorkflowLog[] logs;
	public Long[] getLogsIds() {
				return logsIds;
	}

	public void setLogsIds(Long[] logsIds) {
				this.logsIds = logsIds;
	}

	@JsonIgnore
	public void setLogsIds(
				UnsafeSupplier<Long[], Throwable> logsIdsUnsafeSupplier) {

				try {
					logsIds = logsIdsUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long[] logsIds;
	public String getName() {
				return name;
	}

	public void setName(String name) {
				this.name = name;
	}

	@JsonIgnore
	public void setName(
				UnsafeSupplier<String, Throwable> nameUnsafeSupplier) {

				try {
					name = nameUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String name;
	public ObjectReviewed getObjectReviewed() {
				return objectReviewed;
	}

	public void setObjectReviewed(ObjectReviewed objectReviewed) {
				this.objectReviewed = objectReviewed;
	}

	@JsonIgnore
	public void setObjectReviewed(
				UnsafeSupplier<ObjectReviewed, Throwable> objectReviewedUnsafeSupplier) {

				try {
					objectReviewed = objectReviewedUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected ObjectReviewed objectReviewed;
	public String[] getTransitions() {
				return transitions;
	}

	public void setTransitions(String[] transitions) {
				this.transitions = transitions;
	}

	@JsonIgnore
	public void setTransitions(
				UnsafeSupplier<String[], Throwable> transitionsUnsafeSupplier) {

				try {
					transitions = transitionsUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String[] transitions;

	public String toString() {
			StringBundler sb = new StringBundler();

			sb.append("{");

					sb.append("completed=");

				sb.append(completed);
					sb.append(", dateCompleted=");

				sb.append(dateCompleted);
					sb.append(", dateCreated=");

				sb.append(dateCreated);
					sb.append(", definitionName=");

				sb.append(definitionName);
					sb.append(", description=");

				sb.append(description);
					sb.append(", dueDate=");

				sb.append(dueDate);
					sb.append(", id=");

				sb.append(id);
					sb.append(", logs=");

				sb.append(logs);
					sb.append(", logsIds=");

				sb.append(logsIds);
					sb.append(", name=");

				sb.append(name);
					sb.append(", objectReviewed=");

				sb.append(objectReviewed);
					sb.append(", transitions=");

				sb.append(transitions);

			sb.append("}");

			return sb.toString();
	}

	}

	protected static class Page<T> {

	public Collection<T> getItems() {
			return new ArrayList<>(items);
	}

	public int getItemsPerPage() {
			return itemsPerPage;
	}

	public int getLastPageNumber() {
			return lastPageNumber;
	}

	public int getPageNumber() {
			return pageNumber;
	}

	public int getTotalCount() {
			return totalCount;
	}

	@JsonProperty
	protected Collection<T> items;

	@JsonProperty
	protected int itemsPerPage;

	@JsonProperty
	protected int lastPageNumber;

	@JsonProperty
	protected int pageNumber;

	@JsonProperty
	protected int totalCount;

	}

	private Http.Options _createHttpOptions() {
		Http.Options options = new Http.Options();

		options.addHeader("Accept", "application/json");

		String userNameAndPassword = "test@liferay.com:test";

		String encodedUserNameAndPassword = Base64.encode(userNameAndPassword.getBytes());

		options.addHeader("Authorization", "Basic " + encodedUserNameAndPassword);

		options.addHeader("Content-Type", "application/json");

		return options;
	}

	private String _toPath(String template, Object value) {
		return template.replaceFirst("\\{.*\\}", String.valueOf(value));
	}

	private final static ObjectMapper _inputObjectMapper = new ObjectMapper() {
		{
			setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}
	};
	private final static ObjectMapper _outputObjectMapper = new ObjectMapper();

	private URL _resourceURL;

}