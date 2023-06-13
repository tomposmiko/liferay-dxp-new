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
JournalSelectDDMStructureDisplayContext journalSelectDDMStructureDisplayContext = new JournalSelectDDMStructureDisplayContext(renderRequest, renderResponse);

SearchContainer<DDMStructure> structureSearch = journalSelectDDMStructureDisplayContext.getStructureSearch();
%>

<clay:management-toolbar
	clearResultsURL="<%= journalSelectDDMStructureDisplayContext.getClearResultsURL() %>"
	disabled="<%= journalSelectDDMStructureDisplayContext.isDisabledManagementBar() %>"
	filterDropdownItems="<%= journalSelectDDMStructureDisplayContext.getFilterItemsDropdownItems() %>"
	itemsTotal="<%= journalSelectDDMStructureDisplayContext.getTotalItems() %>"
	searchActionURL="<%= journalSelectDDMStructureDisplayContext.getSearchActionURL() %>"
	searchFormName="searchForm"
	selectable="<%= false %>"
	sortingOrder="<%= journalSelectDDMStructureDisplayContext.getOrderByType() %>"
	sortingURL="<%= journalSelectDDMStructureDisplayContext.getSortingURL() %>"
/>

<aui:form cssClass="container-fluid-1280" method="post" name="selectStructureFm">
	<liferay-ui:search-container
		searchContainer="<%= structureSearch %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.dynamic.data.mapping.model.DDMStructure"
			keyProperty="structureId"
			modelVar="structure"
		>
			<liferay-ui:search-container-column-text
				name="id"
				value="<%= String.valueOf(structure.getStructureId()) %>"
			/>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-content"
				name="name"
			>
				<c:choose>
					<c:when test="<%= structure.getStructureId() != journalSelectDDMStructureDisplayContext.getClassPK() %>">

						<%
						Map<String, Object> data = new HashMap<>();

						data.put("ddmstructureid", structure.getStructureId());
						data.put("ddmstructurekey", structure.getStructureKey());
						data.put("name", structure.getName(locale));
						%>

						<aui:a cssClass="selector-button" data="<%= data %>" href="javascript:;">
							<%= HtmlUtil.escape(structure.getUnambiguousName(structureSearch.getResults(), themeDisplay.getScopeGroupId(), locale)) %>
						</aui:a>
					</c:when>
					<c:otherwise>
						<%= HtmlUtil.escape(structure.getUnambiguousName(structureSearch.getResults(), themeDisplay.getScopeGroupId(), locale)) %>
					</c:otherwise>
				</c:choose>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-content"
				name="description"
				truncate="<% true %>"
				value="<%= HtmlUtil.escape(structure.getDescription(locale)) %>"
			/>

			<liferay-ui:search-container-column-date
				name="modified-date"
				value="<%= structure.getModifiedDate() %>"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="list"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>

<aui:script>
	Liferay.Util.selectEntityHandler('#<portlet:namespace />selectStructureFm', '<%= HtmlUtil.escapeJS(journalSelectDDMStructureDisplayContext.getEventName()) %>');
</aui:script>