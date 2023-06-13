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
MasterLayoutDisplayContext masterLayoutDisplayContext = new MasterLayoutDisplayContext(request, renderRequest, renderResponse);
%>

<clay:navigation-bar
	inverted="<%= true %>"
	navigationItems="<%= layoutPageTemplatesAdminDisplayContext.getNavigationItems() %>"
/>

<%
MasterLayoutManagementToolbarDisplayContext masterLayoutManagementToolbarDisplayContext = new MasterLayoutManagementToolbarDisplayContext(request, liferayPortletRequest, liferayPortletResponse, masterLayoutDisplayContext);
%>

<clay:management-toolbar
	displayContext="<%= masterLayoutManagementToolbarDisplayContext %>"
/>

<liferay-ui:success key="masterPagePublished" message="the-master-page-was-published-succesfully" />

<portlet:actionURL name="/layout_page_template/delete_master_layout" var="deleteMasterLayoutURL">
	<portlet:param name="redirect" value="<%= currentURL %>" />
</portlet:actionURL>

<aui:form action="<%= deleteMasterLayoutURL %>" cssClass="container-fluid-1280" name="fm">
	<liferay-ui:error key="<%= PortalException.class.getName() %>" message="one-or-more-entries-could-not-be-deleted" />
	<liferay-ui:error key="<%= RequiredLayoutPageTemplateEntryException.class.getName() %>" message="you-cannot-delete-master-pages-that-are-used-by-a-page,-a-page-template,-or-a-display-page-template" />

	<liferay-ui:search-container
		id="<%= masterLayoutManagementToolbarDisplayContext.getSearchContainerId() %>"
		searchContainer="<%= masterLayoutDisplayContext.getMasterLayoutsSearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.layout.page.template.model.LayoutPageTemplateEntry"
			cssClass="selectable"
			escapedModel="<%= true %>"
			keyProperty="layoutPrototypeId"
			modelVar="layoutPageTemplateEntry"
		>

			<%
			row.setCssClass("entry-card lfr-asset-item");

			row.setData(
				HashMapBuilder.<String, Object>put(
					"actions", masterLayoutManagementToolbarDisplayContext.getAvailableActions(layoutPageTemplateEntry)
				).build());
			%>

			<liferay-ui:search-container-column-text>
				<clay:vertical-card
					verticalCard="<%= new MasterLayoutVerticalCard(layoutPageTemplateEntry, renderRequest, renderResponse, searchContainer.getRowChecker()) %>"
				/>
			</liferay-ui:search-container-column-text>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="icon"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>

<portlet:actionURL name="/layout_page_template/update_layout_page_template_entry_preview" var="masterLayoutPreviewURL">
	<portlet:param name="redirect" value="<%= currentURL %>" />
</portlet:actionURL>

<aui:form action="<%= masterLayoutPreviewURL %>" name="masterLayoutPreviewFm">
	<aui:input name="layoutPageTemplateEntryId" type="hidden" />
	<aui:input name="fileEntryId" type="hidden" />
</aui:form>

<liferay-frontend:component
	componentId="<%= LayoutPageTemplateAdminWebKeys.MASTER_LAYOUT_DROPDOWN_DEFAULT_EVENT_HANDLER %>"
	module="js/MasterLayoutDropdownDefaultEventHandler.es"
/>

<liferay-frontend:component
	componentId="<%= masterLayoutManagementToolbarDisplayContext.getDefaultEventHandler() %>"
	module="js/MasterLayoutManagementToolbarDefaultEventHandler.es"
/>