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

import com.liferay.headless.workflow.dto.v1_0.WorkflowLog;
import com.liferay.headless.workflow.dto.v1_0.WorkflowTask;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
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
public abstract class BaseWorkflowLogResourceTestCase {

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
	public void testGetWorkflowLog() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetWorkflowTaskWorkflowLogsPage() throws Exception {
			Assert.assertTrue(true);
	}

	protected void assertResponseCode(int expectedResponseCode, Http.Response actualResponse) {
		Assert.assertEquals(expectedResponseCode, actualResponse.getResponseCode());
	}

	protected WorkflowLog invokeGetWorkflowLog(
				Long workflowLogId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/workflow-logs/{workflow-log-id}", workflowLogId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), WorkflowLogImpl.class);
	}

	protected Http.Response invokeGetWorkflowLogResponse(
				Long workflowLogId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/workflow-logs/{workflow-log-id}", workflowLogId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Page<WorkflowLog> invokeGetWorkflowTaskWorkflowLogsPage(
				Long workflowTaskId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/workflow-tasks/{workflow-task-id}/workflow-logs", workflowTaskId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<WorkflowLogImpl>>() {});
	}

	protected Http.Response invokeGetWorkflowTaskWorkflowLogsPageResponse(
				Long workflowTaskId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/workflow-tasks/{workflow-task-id}/workflow-logs", workflowTaskId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}

	protected void assertEquals(WorkflowLog workflowLog1, WorkflowLog workflowLog2) {
		Assert.assertTrue(workflowLog1 + " does not equal " + workflowLog2, equals(workflowLog1, workflowLog2));
	}

	protected void assertEquals(List<WorkflowLog> workflowLogs1, List<WorkflowLog> workflowLogs2) {
		Assert.assertEquals(workflowLogs1.size(), workflowLogs2.size());

		for (int i = 0; i < workflowLogs1.size(); i++) {
			WorkflowLog workflowLog1 = workflowLogs1.get(i);
			WorkflowLog workflowLog2 = workflowLogs2.get(i);

			assertEquals(workflowLog1, workflowLog2);
	}
	}

	protected boolean equals(WorkflowLog workflowLog1, WorkflowLog workflowLog2) {
		if (workflowLog1 == workflowLog2) {
			return true;
	}

		return false;
	}

	protected WorkflowLog randomWorkflowLog() {
		return new WorkflowLogImpl() {
			{

						auditPerson = RandomTestUtil.randomString();
						commentLog = RandomTestUtil.randomString();
						dateCreated = RandomTestUtil.nextDate();
						id = RandomTestUtil.randomLong();
						person = RandomTestUtil.randomString();
						previousPerson = RandomTestUtil.randomString();
						previousState = RandomTestUtil.randomString();
						state = RandomTestUtil.randomString();
						taskId = RandomTestUtil.randomLong();
						type = RandomTestUtil.randomString();
	}
		};
	}

	protected Group testGroup;

	protected static class WorkflowLogImpl implements WorkflowLog {

	public String getAuditPerson() {
				return auditPerson;
	}

	public void setAuditPerson(String auditPerson) {
				this.auditPerson = auditPerson;
	}

	@JsonIgnore
	public void setAuditPerson(
				UnsafeSupplier<String, Throwable> auditPersonUnsafeSupplier) {

				try {
					auditPerson = auditPersonUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String auditPerson;
	public String getCommentLog() {
				return commentLog;
	}

	public void setCommentLog(String commentLog) {
				this.commentLog = commentLog;
	}

	@JsonIgnore
	public void setCommentLog(
				UnsafeSupplier<String, Throwable> commentLogUnsafeSupplier) {

				try {
					commentLog = commentLogUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String commentLog;
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
	public String getPerson() {
				return person;
	}

	public void setPerson(String person) {
				this.person = person;
	}

	@JsonIgnore
	public void setPerson(
				UnsafeSupplier<String, Throwable> personUnsafeSupplier) {

				try {
					person = personUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String person;
	public String getPreviousPerson() {
				return previousPerson;
	}

	public void setPreviousPerson(String previousPerson) {
				this.previousPerson = previousPerson;
	}

	@JsonIgnore
	public void setPreviousPerson(
				UnsafeSupplier<String, Throwable> previousPersonUnsafeSupplier) {

				try {
					previousPerson = previousPersonUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String previousPerson;
	public String getPreviousState() {
				return previousState;
	}

	public void setPreviousState(String previousState) {
				this.previousState = previousState;
	}

	@JsonIgnore
	public void setPreviousState(
				UnsafeSupplier<String, Throwable> previousStateUnsafeSupplier) {

				try {
					previousState = previousStateUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String previousState;
	public String getState() {
				return state;
	}

	public void setState(String state) {
				this.state = state;
	}

	@JsonIgnore
	public void setState(
				UnsafeSupplier<String, Throwable> stateUnsafeSupplier) {

				try {
					state = stateUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String state;
	public WorkflowTask getTask() {
				return task;
	}

	public void setTask(WorkflowTask task) {
				this.task = task;
	}

	@JsonIgnore
	public void setTask(
				UnsafeSupplier<WorkflowTask, Throwable> taskUnsafeSupplier) {

				try {
					task = taskUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected WorkflowTask task;
	public Long getTaskId() {
				return taskId;
	}

	public void setTaskId(Long taskId) {
				this.taskId = taskId;
	}

	@JsonIgnore
	public void setTaskId(
				UnsafeSupplier<Long, Throwable> taskIdUnsafeSupplier) {

				try {
					taskId = taskIdUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long taskId;
	public String getType() {
				return type;
	}

	public void setType(String type) {
				this.type = type;
	}

	@JsonIgnore
	public void setType(
				UnsafeSupplier<String, Throwable> typeUnsafeSupplier) {

				try {
					type = typeUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String type;

	public String toString() {
			StringBundler sb = new StringBundler();

			sb.append("{");

					sb.append("auditPerson=");

				sb.append(auditPerson);
					sb.append(", commentLog=");

				sb.append(commentLog);
					sb.append(", dateCreated=");

				sb.append(dateCreated);
					sb.append(", id=");

				sb.append(id);
					sb.append(", person=");

				sb.append(person);
					sb.append(", previousPerson=");

				sb.append(previousPerson);
					sb.append(", previousState=");

				sb.append(previousState);
					sb.append(", state=");

				sb.append(state);
					sb.append(", task=");

				sb.append(task);
					sb.append(", taskId=");

				sb.append(taskId);
					sb.append(", type=");

				sb.append(type);

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