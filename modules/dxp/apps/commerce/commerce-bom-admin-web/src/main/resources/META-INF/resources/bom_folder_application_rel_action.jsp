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
CommerceBOMAdminDisplayContext commerceBOMAdminDisplayContext = (CommerceBOMAdminDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

CommerceBOMFolderApplicationRel commerceBOMFolderApplicationRel = (CommerceBOMFolderApplicationRel)row.getObject();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<c:if test="<%= commerceBOMAdminDisplayContext.hasCommerceBOMFolderPermissions(commerceBOMFolderApplicationRel.getCommerceBOMFolderId(), ActionKeys.UPDATE) %>">
		<portlet:actionURL name="/commerce_bom_admin/edit_commerce_bom_folder_application_rel" var="deleteURL">
			<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.DELETE %>" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="commerceBOMFolderApplicationRelId" value="<%= String.valueOf(commerceBOMFolderApplicationRel.getCommerceBOMFolderApplicationRelId()) %>" />
			<portlet:param name="commerceBOMFolderId" value="<%= String.valueOf(commerceBOMFolderApplicationRel.getCommerceBOMFolderId()) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete
			url="<%= deleteURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>