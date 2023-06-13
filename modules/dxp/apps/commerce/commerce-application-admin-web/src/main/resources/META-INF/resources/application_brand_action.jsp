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
CommerceApplicationAdminDisplayContext commerceApplicationAdminDisplayContext = (CommerceApplicationAdminDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

CommerceApplicationBrand commerceApplicationBrand = (CommerceApplicationBrand)row.getObject();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<c:if test="<%= commerceApplicationAdminDisplayContext.hasCommerceApplicationBrandPermissions(commerceApplicationBrand.getCommerceApplicationBrandId(), ActionKeys.UPDATE) %>">
		<portlet:renderURL var="editURL">
			<portlet:param name="mvcRenderCommandName" value="/commerce_application_admin/edit_commerce_application_brand" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="commerceApplicationBrandId" value="<%= String.valueOf(commerceApplicationBrand.getCommerceApplicationBrandId()) %>" />
		</portlet:renderURL>

		<liferay-ui:icon
			message="edit"
			url="<%= editURL %>"
		/>
	</c:if>

	<c:if test="<%= commerceApplicationAdminDisplayContext.hasCommerceApplicationBrandPermissions(commerceApplicationBrand.getCommerceApplicationBrandId(), ActionKeys.DELETE) %>">
		<portlet:actionURL name="/commerce_application_admin/edit_commerce_application_brand" var="deleteURL">
			<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.DELETE %>" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="commerceApplicationBrandId" value="<%= String.valueOf(commerceApplicationBrand.getCommerceApplicationBrandId()) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete
			url="<%= deleteURL %>"
		/>
	</c:if>
</liferay-ui:icon-menu>