<%--
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
--%>

<%@ include file="/definition/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

WorkflowDefinition workflowDefinition = (WorkflowDefinition)request.getAttribute(WebKeys.WORKFLOW_DEFINITION);

String content = workflowDefinition.getContent();

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

renderResponse.setTitle(workflowDefinition.getTitle(LanguageUtil.getLanguageId(request)));

String state = (String)request.getParameter(WorkflowWebKeys.WORKFLOW_JSP_STATE);

boolean previewBeforeRestore = WorkflowWebKeys.WORKFLOW_PREVIEW_BEFORE_RESTORE_STATE.equals(state);
%>

<liferay-portlet:renderURL var="editWorkflowDefinitionURL">
	<portlet:param name="mvcPath" value="/definition/edit_workflow_definition.jsp" />
	<portlet:param name="redirect" value="<%= redirect %>" />
	<portlet:param name="name" value="<%= workflowDefinition.getName() %>" />
	<portlet:param name="version" value="<%= String.valueOf(workflowDefinition.getVersion()) %>" />
</liferay-portlet:renderURL>

<div class="<%= previewBeforeRestore ? "" : "container-fluid-1280" %>" id="container">
	<aui:model-context bean="<%= workflowDefinition %>" model="<%= WorkflowDefinition.class %>" />

	<liferay-frontend:info-bar>
		<div class="container-fluid-1280">
			<c:if test="<%= !previewBeforeRestore %>">
				<div class="info-bar-item">
					<c:choose>
						<c:when test="<%= workflowDefinition.isActive() %>">
							<span class="label label-info">
								<liferay-ui:message key="published" />
							</span>
						</c:when>
						<c:otherwise>
							<span class="label label-secondary">
								<liferay-ui:message key="not-published" />
							</span>
						</c:otherwise>
					</c:choose>
				</div>
			</c:if>

			<%
			String userName = workflowDefinitionDisplayContext.getUserName(workflowDefinition);
			%>

			<span>
				<c:choose>
					<c:when test="<%= userName == null %>">
						<%= dateFormatTime.format(workflowDefinition.getModifiedDate()) %>
					</c:when>
					<c:when test="<%= previewBeforeRestore %>">
						<liferay-ui:message arguments="<%= new String[] {dateFormatTime.format(workflowDefinition.getModifiedDate()), userName} %>" key="revision-from-x-by-x" translateArguments="<%= false %>" />
					</c:when>
					<c:otherwise>
						<liferay-ui:message arguments="<%= new String[] {dateFormatTime.format(workflowDefinition.getModifiedDate()), userName} %>" key="x-by-x" translateArguments="<%= false %>" />
					</c:otherwise>
				</c:choose>
			</span>
		</div>
	</liferay-frontend:info-bar>

	<aui:input name="content" type="hidden" value="<%= content %>" />

	<aui:fieldset cssClass="workflow-definition-content">
		<aui:col>
			<aui:field-wrapper label="title">
				<liferay-ui:input-localized disabled="<%= true %>" name="title" xml='<%= BeanPropertiesUtil.getString(workflowDefinition, "title") %>' />
			</aui:field-wrapper>
		</aui:col>

		<aui:col cssClass="workflow-definition-content-source-wrapper" id="contentSourceWrapper">
			<div class="workflow-definition-content-source" id="<portlet:namespace />contentEditor"></div>
		</aui:col>
	</aui:fieldset>

	<c:choose>
		<c:when test="<%= !previewBeforeRestore %>">
			<aui:button-row>
				<aui:button href="<%= editWorkflowDefinitionURL %>" primary="<%= true %>" value="edit" />
			</aui:button-row>
		</c:when>
	</c:choose>
</div>

<aui:script use="aui-ace-editor,liferay-xml-formatter">
	var STR_VALUE = 'value';

	var contentEditor = new A.AceEditor(
		{
			boundingBox: '#<portlet:namespace />contentEditor',
			height: 600,
			mode: 'xml',
			readOnly: 'true',
			tabSize: 4,
			width: '100%'
		}
	).render();

	var xmlFormatter = new Liferay.XMLFormatter();

	var editorContentElement = A.one('#<portlet:namespace />content');

	if (editorContentElement) {
		var content = xmlFormatter.format(editorContentElement.val());

		contentEditor.set(STR_VALUE, content);
	}
</aui:script>