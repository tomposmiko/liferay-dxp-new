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

<%@ include file="/init.jsp" %>

<%
LayoutPortletsDisplayContext layoutPortletsDisplayContext = new LayoutPortletsDisplayContext(renderRequest, renderResponse, request);

LayoutPortletsManagementToolbarDisplayContext layoutPortletsManagementToolbarDisplayContext = new LayoutPortletsManagementToolbarDisplayContext(liferayPortletRequest, liferayPortletResponse, request, layoutPortletsDisplayContext);
%>

<clay:management-toolbar
	displayContext="<%= layoutPortletsManagementToolbarDisplayContext %>"
/>

<aui:form action="" cssClass="container-fluid-1280" name="fm">
	<liferay-ui:search-container
		searchContainer="<%= layoutPortletsDisplayContext.getSearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.portal.kernel.model.Portlet"
			cssClass="selectable"
			escapedModel="<%= true %>"
			keyProperty="portletId"
			modelVar="portlet"
		>
			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand-small table-cell-minw-200 table-title"
				name="name"
				property="displayName"
			/>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand table-cell-minw-300"
				name="categories"
				value="<%= layoutPortletsDisplayContext.getPortletCategoryLabels(portlet.getRootPortletId()) %>"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="<%= layoutPortletsDisplayContext.getDisplayStyle() %>"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>