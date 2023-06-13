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
DisplayPageUsagesDisplayContext displayPageUsagesDisplayContext = new DisplayPageUsagesDisplayContext(request, renderRequest, renderResponse);

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(displayPageUsagesDisplayContext.getRedirect());

LayoutPageTemplateEntry layoutPageTemplateEntry = LayoutPageTemplateEntryServiceUtil.fetchLayoutPageTemplateEntry(displayPageUsagesDisplayContext.getLayoutPageTemplateEntryId());

renderResponse.setTitle(LanguageUtil.format(request, "usages-x", layoutPageTemplateEntry.getName()));
%>

<clay:management-toolbar
	displayContext="<%= new DisplayPageUsagesManagementToolbarDisplayContext(request, liferayPortletRequest, liferayPortletResponse, displayPageUsagesDisplayContext.getSearchContainer()) %>"
/>

<clay:container-fluid>
	<liferay-ui:search-container
		searchContainer="<%= displayPageUsagesDisplayContext.getSearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.asset.display.page.model.AssetDisplayPageEntry"
			keyProperty="assetDisplayPageEntryId"
			modelVar="assetDisplayPageEntry"
		>
			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand table-cell-minw-200 table-title"
				name="title"
				value="<%= HtmlUtil.escape(displayPageUsagesDisplayContext.getTitle(assetDisplayPageEntry, themeDisplay.getLocale())) %>"
			/>

			<liferay-ui:search-container-column-date
				cssClass="table-cell-expand-smallest table-cell-ws-nowrap"
				name="modified-date"
				value="<%= assetDisplayPageEntry.getModifiedDate() %>"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="list"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</clay:container-fluid>