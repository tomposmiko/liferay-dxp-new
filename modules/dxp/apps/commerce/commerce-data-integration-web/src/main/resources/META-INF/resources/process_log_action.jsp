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
CommerceDataIntegrationProcessLogDisplayContext commerceDataIntegrationProcessLogDisplayContext = (CommerceDataIntegrationProcessLogDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

CommerceDataIntegrationProcessLog commerceDataIntegrationProcessLog = (CommerceDataIntegrationProcessLog)row.getObject();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<c:if test="<%= CommerceDataintegrationProcessPermission.contains(permissionChecker, commerceDataIntegrationProcessLogDisplayContext.getCommerceDataIntegrationProcess(), ActionKeys.UPDATE) %>">
		<portlet:renderURL var="viewURL">
			<portlet:param name="mvcRenderCommandName" value="/commerce_data_integration/view_commerce_data_integration_process_log" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="cDataIntegrationProcessLogId" value="<%= String.valueOf(commerceDataIntegrationProcessLog.getCommerceDataIntegrationProcessLogId()) %>" />
		</portlet:renderURL>

		<liferay-ui:icon
			message="view"
			url="<%= viewURL %>"
		/>

		<portlet:actionURL name="/commerce_data_integration/edit_commerce_data_integration_process_log" var="deleteURL">
			<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.DELETE %>" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="cDataIntegrationProcessLogId" value="<%= String.valueOf(commerceDataIntegrationProcessLog.getCommerceDataIntegrationProcessLogId()) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete
			url="<%= deleteURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>