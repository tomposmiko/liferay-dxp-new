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
CPDefinitionGroupedEntriesDisplayContext cpDefinitionGroupedEntriesDisplayContext = (CPDefinitionGroupedEntriesDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CPDefinition cpDefinition = cpDefinitionGroupedEntriesDisplayContext.getCPDefinition();
SearchContainer<CPDefinitionGroupedEntry> cpDefinitionGroupedEntrySearchContainer = cpDefinitionGroupedEntriesDisplayContext.getSearchContainer();

PortletURL portletURL = cpDefinitionGroupedEntriesDisplayContext.getPortletURL();

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(catalogURL);

renderResponse.setTitle(cpDefinition.getName(themeDisplay.getLanguageId()));
%>

<liferay-ui:error exception="<%= NoSuchCPDefinitionException.class %>" message="please-select-a-valid-product" />

<clay:management-toolbar
	managementToolbarDisplayContext="<%= new CPDefinitionGroupedManagementToolbarDisplayContext(cpDefinitionGroupedEntriesDisplayContext, request, liferayPortletRequest, liferayPortletResponse) %>"
	propsTransformer="js/CPDefinitionGroupedManagementToolbarPropsTransformer"
/>

<div id="<portlet:namespace />cpDefinitionGroupedEntriesContainer">
	<div class="closed sidenav-container sidenav-right" id="<portlet:namespace />infoPanelId">
		<c:if test="<%= cpDefinitionGroupedEntriesDisplayContext.isShowInfoPanel() %>">
			<liferay-portlet:resourceURL copyCurrentRenderParameters="<%= false %>" id="/cp_definitions/cp_definition_grouped_entry_info_panel" var="sidebarPanelURL" />

			<liferay-frontend:sidebar-panel
				resourceURL="<%= sidebarPanelURL %>"
				searchContainerId="cpDefinitionGroupedEntries"
			>
				<liferay-util:include page="/cp_definition_grouped_entry_info_panel.jsp" servletContext="<%= application %>" />
			</liferay-frontend:sidebar-panel>
		</c:if>

		<div class="sidenav-content">
			<portlet:actionURL name="/cp_definitions/edit_cp_definition_grouped_entry" var="editCPDefinitionGroupedEntryURL" />

			<aui:form action="<%= editCPDefinitionGroupedEntryURL %>" cssClass="container-fluid container-fluid-max-xl" method="post" name="fm">
				<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.DELETE %>" />
				<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
				<aui:input name="deleteCPDefinitionGroupedEntryIds" type="hidden" />

				<div id="<portlet:namespace />entriesContainer">
					<liferay-ui:search-container
						id="cpDefinitionGroupedEntries"
						iteratorURL="<%= portletURL %>"
						searchContainer="<%= cpDefinitionGroupedEntrySearchContainer %>"
					>
						<liferay-ui:search-container-row
							className="com.liferay.commerce.product.type.grouped.model.CPDefinitionGroupedEntry"
							keyProperty="CPDefinitionGroupedEntryId"
							modelVar="cpDefinitionGroupedEntry"
						>

							<%
							PortletURL rowURL = PortletURLBuilder.createRenderURL(
								renderResponse
							).setMVCRenderCommandName(
								"/cp_definitions/edit_cp_definition_grouped_entry"
							).setParameter(
								"cpDefinitionGroupedEntryId", cpDefinitionGroupedEntry.getCPDefinitionGroupedEntryId()
							).setParameter(
								"cpDefinitionId", cpDefinitionGroupedEntry.getCPDefinitionId()
							).buildPortletURL();

							CProduct cProduct = cpDefinitionGroupedEntry.getEntryCProduct();

							CPDefinition cProductCPDefinition = CPDefinitionLocalServiceUtil.getCPDefinition(cProduct.getPublishedCPDefinitionId());
							%>

							<liferay-ui:search-container-column-text
								cssClass="font-weight-bold important table-cell-expand"
								href="<%= rowURL %>"
								name="name"
								value="<%= HtmlUtil.escape(cProductCPDefinition.getName(themeDisplay.getLanguageId())) %>"
							/>

							<liferay-ui:search-container-column-text
								cssClass="table-cell-expand"
								property="quantity"
							/>

							<liferay-ui:search-container-column-text
								cssClass="table-cell-expand"
								property="priority"
							/>

							<liferay-ui:search-container-column-jsp
								cssClass="entry-action-column"
								path="/cp_definition_grouped_entry_action.jsp"
							/>
						</liferay-ui:search-container-row>

						<liferay-ui:search-iterator
							markupView="lexicon"
							searchContainer="<%= cpDefinitionGroupedEntrySearchContainer %>"
						/>
					</liferay-ui:search-container>
				</div>
			</aui:form>
		</div>
	</div>
</div>

<portlet:actionURL name="/cp_definitions/edit_cp_definition_grouped_entry" var="addDefinitionGroupedEntryURL">
	<portlet:param name="mvcRenderCommandName" value="/cp_definitions/view_cp_definition_grouped_entries" />
</portlet:actionURL>

<aui:form action="<%= addDefinitionGroupedEntryURL %>" cssClass="hide" name="addCPDefinitionGroupedEntryFm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.ADD %>" />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="cpDefinitionId" type="hidden" value="<%= cpDefinition.getCPDefinitionId() %>" />
	<aui:input name="entryCPDefinitionIds" type="hidden" value="" />
</aui:form>