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

<%@ include file="/init.jsp" %>

<%
ViewSXPElementsDisplayContext viewSXPElementsDisplayContext = (ViewSXPElementsDisplayContext)request.getAttribute(SXPWebKeys.VIEW_SXP_ELEMENTS_DISPLAY_CONTEXT);
%>

<aui:form action="<%= viewSXPElementsDisplayContext.getPortletURL() %>" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= String.valueOf(viewSXPElementsDisplayContext.getPortletURL()) %>" />

	<div id="<portlet:namespace />viewSXPElements">
		<react:component
			module="sxp_blueprint_admin/js/view_sxp_elements/index"
			props='<%=
				HashMapBuilder.<String, Object>put(
					"apiURL", viewSXPElementsDisplayContext.getAPIURL()
				).put(
					"defaultLocale", LocaleUtil.toLanguageId(LocaleUtil.getDefault())
				).put(
					"deleteSXPElementURL",
					PortletURLBuilder.createActionURL(
						liferayPortletResponse
					).setActionName(
						"/sxp_blueprint_admin/edit_sxp_element"
					).setCMD(
						Constants.DELETE
					).buildString()
				).put(
					"editSXPElementURL",
					PortletURLBuilder.createRenderURL(
						liferayPortletResponse
					).setMVCRenderCommandName(
						"/sxp_blueprint_admin/edit_sxp_element"
					).buildString()
				).put(
					"formName", "fm"
				).put(
					"hasAddSXPElementPermission", viewSXPElementsDisplayContext.hasAddSXPElementPermission()
				).put(
					"namespace", liferayPortletResponse.getNamespace()
				).build()
			%>'
		/>
	</div>
</aui:form>

<liferay-frontend:component
	module="sxp_blueprint_admin/js/utils/openInitialSuccessToastHandler"
/>

<c:if test="<%= SessionErrors.contains(renderRequest, SXPElementReadOnlyException.class) %>">
	<aui:script>
		Liferay.Util.openToast({
			message:
				'<liferay-ui:message key="system-read-only-elements-cannot-be-deleted" />',
			type: 'danger',
		});
	</aui:script>
</c:if>