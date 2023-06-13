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
String searchContainerId = ParamUtil.getString(request, "searchContainerId");
%>

<clay:management-toolbar
	actionDropdownItems="<%= journalDisplayContext.getActionDropdownItems() %>"
	clearResultsURL="<%= journalDisplayContext.getClearResultsURL() %>"
	componentId="journalWebManagementToolbar"
	creationMenu="<%= journalDisplayContext.getCreationMenu() %>"
	disabled="<%= journalDisplayContext.isDisabled() %>"
	filterDropdownItems="<%= journalDisplayContext.getFilterDropdownItems() %>"
	infoPanelId="infoPanelId"
	itemsTotal="<%= journalDisplayContext.getTotalItems() %>"
	searchActionURL="<%= journalDisplayContext.getSearchActionURL() %>"
	searchContainerId="<%= searchContainerId %>"
	searchFormName="fm1"
	showCreationMenu="<%= journalDisplayContext.isShowAddButton() %>"
	showInfoButton="<%= journalDisplayContext.isShowInfoButton() %>"
	showSearch="<%= journalDisplayContext.isShowSearch() %>"
	sortingOrder="<%= journalDisplayContext.getOrderByType() %>"
	sortingURL="<%= journalDisplayContext.getSortingURL() %>"
	viewTypeItems="<%= journalDisplayContext.getViewTypeItems() %>"
/>

<aui:script sandbox="<%= true %>">
	var deleteEntries = function() {
		if (<%= trashHelper.isTrashEnabled(scopeGroupId) %> || confirm(' <%= UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-the-selected-entries") %>')) {
			Liferay.fire(
				'<%= renderResponse.getNamespace() %>editEntry',
				{
					action: '<%= trashHelper.isTrashEnabled(scopeGroupId) ? "moveEntriesToTrash" : "deleteEntries" %>'
				}
			);
		}
	}

	var expireEntries = function() {
		Liferay.fire(
			'<portlet:namespace />editEntry',
			{
				action: 'expireEntries'
			}
		);
	};

	var moveEntries = function() {
		Liferay.fire(
			'<portlet:namespace />editEntry',
			{
				action: 'moveEntries'
			}
		);
	};

	<portlet:renderURL var="viewDDMStructureArticlesURL">
		<portlet:param name="navigation" value="structure" />
		<portlet:param name="folderId" value="<%= String.valueOf(JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID) %>" />
		<portlet:param name="showEditActions" value="<%= String.valueOf(journalDisplayContext.isShowEditActions()) %>" />
	</portlet:renderURL>

	var openStructuresSelector = function() {
		Liferay.Util.openDDMPortlet(
			{
				basePortletURL: '<%= PortletURLFactoryUtil.create(request, PortletProviderUtil.getPortletId(DDMStructure.class.getName(), PortletProvider.Action.VIEW), themeDisplay.getPlid(), PortletRequest.RENDER_PHASE) %>',
				classPK: <%= journalDisplayContext.getDDMStructurePrimaryKey() %>,
				dialog: {
					destroyOnHide: true
				},
				eventName: '<portlet:namespace />selectStructure',
				groupId: <%= themeDisplay.getScopeGroupId() %>,
				mvcPath: '/select_structure.jsp',
				navigationStartsOn: '<%= DDMNavigationHelper.SELECT_STRUCTURE %>',
				refererPortletName: '<%= JournalPortletKeys.JOURNAL + ".filterStructure" %>',

				<%
				Portlet portlet = PortletLocalServiceUtil.getPortletById(portletDisplay.getId());
				%>

				refererWebDAVToken: '<%= HtmlUtil.escapeJS(WebDAVUtil.getStorageToken(portlet)) %>',

				showAncestorScopes: true,
				title: '<%= UnicodeLanguageUtil.get(request, "structures") %>'
			},
			function(event) {
				var uri = '<%= viewDDMStructureArticlesURL %>';

				uri = Liferay.Util.addParams('<portlet:namespace />ddmStructureKey=' + event.ddmstructurekey, uri);

				location.href = uri;
			}
		);
	}

	var openViewMoreStructuresSelector = function() {
		Liferay.Util.openWindow(
			{
				dialog: {
					destroyOnHide: true,
					modal: true
				},
				id: '<portlet:namespace />selectAddMenuItem',
				title: '<liferay-ui:message key="more" />',

				<portlet:renderURL var="viewMoreURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
					<portlet:param name="mvcPath" value="/view_more_menu_items.jsp" />
					<portlet:param name="folderId" value="<%= String.valueOf(journalDisplayContext.getFolderId()) %>" />
					<portlet:param name="eventName" value='<%= renderResponse.getNamespace() + "selectAddMenuItem" %>' />
				</portlet:renderURL>

				uri: '<%= viewMoreURL %>'
			}
		);
	}

	var ACTIONS = {
		'deleteEntries': deleteEntries,
		'expireEntries': expireEntries,
		'moveEntries': moveEntries,
		'openStructuresSelector': openStructuresSelector,
		'openViewMoreStructuresSelector': openViewMoreStructuresSelector
	};

	Liferay.componentReady('journalWebManagementToolbar').then(
		function(managementToolbar) {
			managementToolbar.on(
				['actionItemClicked', 'filterItemClicked'],
				function(event) {
					var itemData = event.data.item.data;

					if (itemData && itemData.action && ACTIONS[itemData.action]) {
						ACTIONS[itemData.action]();
					}
				}
			);

			managementToolbar.on('creationMenuMoreButtonClicked', openViewMoreStructuresSelector);
		}
	);
</aui:script>