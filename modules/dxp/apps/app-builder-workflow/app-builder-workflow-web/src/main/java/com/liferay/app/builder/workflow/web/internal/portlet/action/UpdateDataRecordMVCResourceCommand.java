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

package com.liferay.app.builder.workflow.web.internal.portlet.action;

import com.liferay.data.engine.rest.dto.v2_0.DataRecord;
import com.liferay.data.engine.rest.resource.v2_0.DataRecordResource;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowException;
import com.liferay.portal.kernel.workflow.WorkflowTask;
import com.liferay.portal.kernel.workflow.WorkflowTaskManager;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.portlet.ResourceRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rafael Praxedes
 */
@Component(
	immediate = true,
	property = {
		"app.builder.app.scope=workflow",
		"mvc.command.name=/app_builder/update_data_record"
	},
	service = MVCResourceCommand.class
)
public class UpdateDataRecordMVCResourceCommand
	extends BaseAppBuilderAppMVCResourceCommand<DataRecord> {

	@Override
	protected Optional<DataRecord> doTransactionalCommand(
			ResourceRequest resourceRequest)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		DataRecord dataRecord = _updateDataRecord(
			resourceRequest, themeDisplay);

		List<WorkflowTask> workflowTasks = _workflowTaskManager.search(
			themeDisplay.getCompanyId(), themeDisplay.getUserId(), null,
			new String[] {ParamUtil.getString(resourceRequest, "taskName")},
			null, null, null, new Long[] {themeDisplay.getUserId()}, null, null,
			false, null, null,
			new Long[] {
				ParamUtil.getLong(resourceRequest, "workflowInstanceId")
			},
			true, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		if (workflowTasks.isEmpty()) {
			throw new WorkflowException(
				StringBundler.concat(
					"Workflow task not found with name ",
					ParamUtil.getString(resourceRequest, "taskName"),
					" and workflow instance ID ",
					ParamUtil.getLong(resourceRequest, "workflowInstanceId")));
		}

		WorkflowTask workflowTask = workflowTasks.get(0);

		Map<String, Serializable> workflowContext =
			workflowTask.getOptionalAttributes();

		workflowContext.put(
			WorkflowConstants.CONTEXT_USER_ID,
			String.valueOf(themeDisplay.getUserId()));

		_workflowTaskManager.completeWorkflowTask(
			themeDisplay.getCompanyId(), themeDisplay.getUserId(),
			workflowTask.getWorkflowTaskId(),
			ParamUtil.getString(resourceRequest, "transitionName"), null,
			workflowContext);

		return Optional.of(dataRecord);
	}

	private DataRecord _updateDataRecord(
			ResourceRequest resourceRequest, ThemeDisplay themeDisplay)
		throws Exception {

		DataRecordResource.Builder dataRecordResourceBuilder =
			_dataRecordResourceFactory.create();

		DataRecordResource dataRecordResource = dataRecordResourceBuilder.user(
			themeDisplay.getUser()
		).build();

		return dataRecordResource.patchDataRecord(
			ParamUtil.getLong(resourceRequest, "dataRecordId"),
			DataRecord.toDTO(
				ParamUtil.getString(resourceRequest, "dataRecord")));
	}

	@Reference
	private DataRecordResource.Factory _dataRecordResourceFactory;

	@Reference
	private WorkflowTaskManager _workflowTaskManager;

}