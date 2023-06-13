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

package com.liferay.headless.workflow.internal.graphql.mutation.v1_0;

import com.liferay.headless.workflow.dto.v1_0.ChangeTransition;
import com.liferay.headless.workflow.dto.v1_0.WorkflowTask;
import com.liferay.headless.workflow.dto.v1_0.WorkflowTaskAssignToMe;
import com.liferay.headless.workflow.dto.v1_0.WorkflowTaskAssignToUser;
import com.liferay.headless.workflow.resource.v1_0.WorkflowTaskResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLInvokeDetached;
import graphql.annotations.annotationTypes.GraphQLName;

import javax.annotation.Generated;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class Mutation {

	public static void setWorkflowTaskResourceComponentServiceObjects(
		ComponentServiceObjects<WorkflowTaskResource>
			workflowTaskResourceComponentServiceObjects) {

		_workflowTaskResourceComponentServiceObjects =
			workflowTaskResourceComponentServiceObjects;
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public WorkflowTask postWorkflowTaskAssignToMe(
			@GraphQLName("workflow-task-id") Long workflowTaskId,
			@GraphQLName("WorkflowTaskAssignToMe") WorkflowTaskAssignToMe
				workflowTaskAssignToMe)
		throws Exception {

		return _applyComponentServiceObjects(
			_workflowTaskResourceComponentServiceObjects,
			this::_populateResourceContext,
			workflowTaskResource ->
				workflowTaskResource.postWorkflowTaskAssignToMe(
					workflowTaskId, workflowTaskAssignToMe));
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public WorkflowTask postWorkflowTaskAssignToUser(
			@GraphQLName("workflow-task-id") Long workflowTaskId,
			@GraphQLName("WorkflowTaskAssignToUser") WorkflowTaskAssignToUser
				workflowTaskAssignToUser)
		throws Exception {

		return _applyComponentServiceObjects(
			_workflowTaskResourceComponentServiceObjects,
			this::_populateResourceContext,
			workflowTaskResource ->
				workflowTaskResource.postWorkflowTaskAssignToUser(
					workflowTaskId, workflowTaskAssignToUser));
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public WorkflowTask postWorkflowTaskChangeTransition(
			@GraphQLName("workflow-task-id") Long workflowTaskId,
			@GraphQLName("ChangeTransition") ChangeTransition changeTransition)
		throws Exception {

		return _applyComponentServiceObjects(
			_workflowTaskResourceComponentServiceObjects,
			this::_populateResourceContext,
			workflowTaskResource ->
				workflowTaskResource.postWorkflowTaskChangeTransition(
					workflowTaskId, changeTransition));
	}

	@GraphQLField
	@GraphQLInvokeDetached
	public WorkflowTask postWorkflowTaskUpdateDueDate(
			@GraphQLName("workflow-task-id") Long workflowTaskId,
			@GraphQLName("WorkflowTaskAssignToMe") WorkflowTaskAssignToMe
				workflowTaskAssignToMe)
		throws Exception {

		return _applyComponentServiceObjects(
			_workflowTaskResourceComponentServiceObjects,
			this::_populateResourceContext,
			workflowTaskResource ->
				workflowTaskResource.postWorkflowTaskUpdateDueDate(
					workflowTaskId, workflowTaskAssignToMe));
	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private <T, E1 extends Throwable, E2 extends Throwable> void
			_applyVoidComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeConsumer<T, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			unsafeFunction.accept(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(
			WorkflowTaskResource workflowTaskResource)
		throws Exception {

		workflowTaskResource.setContextCompany(
			CompanyLocalServiceUtil.getCompany(
				CompanyThreadLocal.getCompanyId()));
	}

	private static ComponentServiceObjects<WorkflowTaskResource>
		_workflowTaskResourceComponentServiceObjects;

}