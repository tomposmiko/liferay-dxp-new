<%--
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
--%>

<%@ include file="/admin/init.jsp" %>

<%
String backURL = ParamUtil.getString(request, "backURL");

KaleoProcess kaleoProcess = (KaleoProcess)request.getAttribute(KaleoFormsWebKeys.KALEO_PROCESS);

long kaleoProcessId = BeanParamUtil.getLong(kaleoProcess, request, "kaleoProcessId");

long ddmStructureId = KaleoFormsUtil.getKaleoProcessDDMStructureId(kaleoProcessId, portletSession);

String workflowDefinition = ParamUtil.getString(request, "workflowDefinition");

KaleoTaskFormPair initialStateKaleoTaskFormPair = KaleoFormsUtil.getInitialStateKaleoTaskFormPair(kaleoProcessId, ddmStructureId, workflowDefinition, KaleoFormsUtil.getInitialStateName(company.getCompanyId(), workflowDefinition), portletSession);
%>

<div id="<portlet:namespace />formsSearchContainer">
	<liferay-portlet:renderURL varImpl="portletURL" />

	<liferay-ui:search-container
		searchContainer='<%= new SearchContainer<Object>(renderRequest, portletURL, null, "no-tasks-were-found") %>'
	>
		<liferay-ui:search-container-results>

			<%
			KaleoTaskFormPairs kaleoTaskFormPairs = KaleoFormsUtil.getKaleoTaskFormPairs(company.getCompanyId(), kaleoProcessId, ddmStructureId, workflowDefinition, portletSession);

			kaleoTaskFormPairs.add(0, initialStateKaleoTaskFormPair);

			searchContainer.setResults(kaleoTaskFormPairs.list());
			searchContainer.setTotal(kaleoTaskFormPairs.size());
			%>

		</liferay-ui:search-container-results>

		<liferay-ui:search-container-row
			className="com.liferay.portal.workflow.kaleo.forms.model.KaleoTaskFormPair"
			modelVar="taskFormsPair"
		>
			<liferay-ui:search-container-row-parameter
				name="backURL"
				value="<%= backURL %>"
			/>

			<liferay-ui:search-container-column-text
				name="task"
				value="<%= HtmlUtil.escape(taskFormsPair.getWorkflowTaskName()) %>"
			/>

			<%
			long ddmTemplateId = taskFormsPair.getDDMTemplateId();

			String formName = StringPool.BLANK;

			if (ddmTemplateId > 0) {
				try {
					DDMTemplate ddmTemplate = DDMTemplateLocalServiceUtil.getTemplate(ddmTemplateId);

					formName = ddmTemplate.getName(locale);
				}
				catch (PortalException pe) {
				}
			}
			%>

			<liferay-util:buffer
				var="taskInputBuffer"
			>
				<c:if test="<%= taskFormsPair.equals(initialStateKaleoTaskFormPair) %>">
					<aui:input name="ddmTemplateId" type="hidden" value="<%= Validator.isNull(formName) ? StringPool.BLANK : String.valueOf(ddmTemplateId) %>">
						<aui:validator name="required" />
					</aui:input>
				</c:if>
			</liferay-util:buffer>

			<liferay-ui:search-container-column-text
				name="form"
				value="<%= HtmlUtil.escape(formName) + taskInputBuffer %>"
			/>

			<liferay-ui:search-container-column-jsp
				align="right"
				cssClass="entry-action"
				path="/admin/process/task_template_action.jsp"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</div>

<portlet:resourceURL id="saveInPortletSession" var="saveInPortletSessionURL" />

<liferay-frontend:component
	context='<%=
		HashMapBuilder.<String, Object>put(
			"backURL", HtmlUtil.escapeURL(backURL)
		).put(
			"itemSelectorURL",
			PortletURLBuilder.create(
				PortletURLFactoryUtil.create(request, DDMPortletKeys.DYNAMIC_DATA_MAPPING, themeDisplay.getPlid(), PortletRequest.RENDER_PHASE)
			).setMVCPath(
				"/select_template.jsp"
			).setParameter(
				"classNameId", PortalUtil.getClassNameId(DDMStructure.class)
			).setParameter(
				"navigationStartsOn", DDMNavigationHelper.SELECT_TEMPLATE
			).setParameter(
				"portletResourceNamespace", liferayPortletResponse.getNamespace()
			).setParameter(
				"refererPortletName", portletDisplay.getId()
			).setParameter(
				"resourceClassNameId", scopeClassNameId
			).setParameter(
				"scopeTitle", LanguageUtil.get(request, "form")
			).setParameter(
				"showBackURL", false
			).setParameter(
				"showHeader", false
			).setParameter(
				"structureAvailableFields", liferayPortletResponse.getNamespace() + "getAvailableFields"
			).setWindowState(
				LiferayWindowState.POP_UP
			).buildString()
		).put(
			"portletNamespace", liferayPortletResponse.getNamespace()
		).put(
			"saveInPortletSessionURL", saveInPortletSessionURL
		).build()
	%>'
	module="admin/js/KaleoFormsTemplateSelector"
/>

<aui:script use="aui-base,aui-io-request,liferay-util">
	Liferay.provide(
		window,
		'<portlet:namespace />editFormTemplate',
		(uri) => {
			var A = AUI();

			var WIN = A.config.win;

			Liferay.Util.openWindow({
				dialog: {
					destroyOnHide: true,
				},
				id: A.guid(),
				refreshWindow: WIN,
				title: '<liferay-ui:message key="forms" />',
				uri: uri,
			});
		},
		['liferay-util']
	);

	Liferay.provide(
		window,
		'<portlet:namespace />unassignForm',
		(event) => {
			var A = AUI();

			var taskFormPairsParamName = event.taskFormPairsParamName;

			var data = {};

			data[taskFormPairsParamName] = 0;

			A.io.request('<portlet:resourceURL id="saveInPortletSession" />', {
				after: {
					success: function () {
						window.location = decodeURIComponent(
							'<%= HtmlUtil.escapeURL(backURL) %>'
						);
					},
				},
				data: data,
			});
		},
		['aui-base']
	);
</aui:script>