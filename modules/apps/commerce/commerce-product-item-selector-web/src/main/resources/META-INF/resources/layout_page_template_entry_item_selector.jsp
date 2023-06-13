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
LayoutPageTemplateEntryItemSelectorViewDisplayContext layoutPageTemplateEntryItemSelectorViewDisplayContext = (LayoutPageTemplateEntryItemSelectorViewDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

SearchContainer<LayoutPageTemplateEntry> layoutPageTemplateEntrySearchContainer = layoutPageTemplateEntryItemSelectorViewDisplayContext.getSearchContainer();

String displayStyle = layoutPageTemplateEntryItemSelectorViewDisplayContext.getDisplayStyle();

String itemSelectedEventName = layoutPageTemplateEntryItemSelectorViewDisplayContext.getItemSelectedEventName();
%>

<clay:management-toolbar
	managementToolbarDisplayContext="<%= new LayoutPageTemplateEntryItemSelectorViewManagementToolbarDisplayContext(request, liferayPortletRequest, liferayPortletResponse, layoutPageTemplateEntrySearchContainer) %>"
/>

<div class="container-fluid container-fluid-max-xl" id="<portlet:namespace />layoutPageTemplateEntrySelectorWrapper">
	<liferay-ui:search-container
		id="layoutPageTemplateEntries"
		searchContainer="<%= layoutPageTemplateEntrySearchContainer %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.layout.page.template.model.LayoutPageTemplateEntry"
			cssClass="layout-page-template-entry-row"
			keyProperty="LayoutPageTemplateEntryId"
			modelVar="layoutPageTemplateEntry"
		>

			<%
			row.setData(
				HashMapBuilder.<String, Object>put(
					"layout-page-template-entry-id", layoutPageTemplateEntry.getUuid()
				).put(
					"name", layoutPageTemplateEntry.getName()
				).build());
			%>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand"
				name="name"
			>
				<div class="layout-page-template-entry-title" data-id="<%= layoutPageTemplateEntry.getLayoutPageTemplateEntryId() %>">
					<%= HtmlUtil.escape(layoutPageTemplateEntry.getName()) %>
				</div>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-date
				cssClass="table-cell-expand"
				name="create-date"
				property="createDate"
			/>

			<liferay-ui:search-container-column-date
				cssClass="table-cell-expand"
				name="modified-date"
				property="modifiedDate"
			/>

			<liferay-ui:search-container-column-status
				cssClass="table-cell-expand"
				name="status"
				status="<%= layoutPageTemplateEntry.getStatus() %>"
			/>

			<c:if test="<%= layoutPageTemplateEntryItemSelectorViewDisplayContext.isSingleSelection() %>">
				<liferay-ui:search-container-column-text
					cssClass="table-cell-expand"
				>
					<aui:button cssClass="selector-button" value="choose" />
				</liferay-ui:search-container-column-text>
			</c:if>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="<%= displayStyle %>"
			markupView="lexicon"
			searchContainer="<%= layoutPageTemplateEntrySearchContainer %>"
		/>
	</liferay-ui:search-container>
</div>

<c:choose>
	<c:when test="<%= layoutPageTemplateEntryItemSelectorViewDisplayContext.isSingleSelection() %>">
		<aui:script use="aui-base">
			A.one('#<portlet:namespace />layoutPageTemplateEntries').delegate(
				'click',
				function (event) {
					var row = this.ancestor('tr');

					var data = row.getDOM().dataset;

					Liferay.Util.getOpener().Liferay.fire(
						'<%= HtmlUtil.escapeJS(itemSelectedEventName) %>',
						{
							data: {id: data.layoutPageTemplateEntryId, name: data.name},
						}
					);

					var popupWindow = Liferay.Util.getWindow();

					if (popupWindow !== null) {
						Liferay.Util.getWindow().hide();
					}
				},
				'.selector-button'
			);
		</aui:script>
	</c:when>
</c:choose>