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

<clay:management-toolbar
	actionDropdownItems="<%= assetListDisplayContext.getAssetListEntryActionItemsDropdownItems() %>"
	clearResultsURL="<%= assetListDisplayContext.getAssetListEntryClearResultsURL() %>"
	componentId="assetListEntriesEntriesManagementToolbar"
	creationMenu="<%= assetListDisplayContext.isShowAddAssetListEntryAction() ? assetListDisplayContext.getCreationMenu() : null %>"
	disabled="<%= assetListDisplayContext.getAssetListEntriesCount() <= 0 %>"
	filterDropdownItems="<%= assetListDisplayContext.getAssetListEntryFilterItemsDropdownItems() %>"
	itemsTotal="<%= assetListDisplayContext.getAssetListEntryTotalItems() %>"
	searchActionURL="<%= assetListDisplayContext.getAssetListEntrySearchActionURL() %>"
	searchContainerId="assetListEntries"
	sortingOrder="<%= assetListDisplayContext.getOrderByType() %>"
	sortingURL="<%= assetListDisplayContext.getSortingURL() %>"
/>

<portlet:actionURL name="/asset_list/delete_asset_list_entry" var="deleteAssetListEntryURL">
	<portlet:param name="redirect" value="<%= currentURL %>" />
</portlet:actionURL>

<aui:form action="<%= deleteAssetListEntryURL %>" cssClass="container-fluid-1280" name="fm">
	<c:choose>
		<c:when test="<%= assetListDisplayContext.getAssetListEntriesCount() > 0 %>">
			<liferay-ui:search-container
				id="assetListEntries"
				searchContainer="<%= assetListDisplayContext.getAssetListEntriesSearchContainer() %>"
			>
				<liferay-ui:search-container-row
					className="com.liferay.asset.list.model.AssetListEntry"
					keyProperty="assetListEntryId"
					modelVar="assetListEntry"
				>

					<%
					String editURL = StringPool.BLANK;

					if (AssetListEntryPermission.contains(permissionChecker, assetListEntry, ActionKeys.UPDATE)) {
						PortletURL editAssetListEntryURL = liferayPortletResponse.createRenderURL();

						editAssetListEntryURL.setParameter("mvcPath", "/edit_asset_list_entry.jsp");
						editAssetListEntryURL.setParameter("redirect", currentURL);
						editAssetListEntryURL.setParameter("assetListEntryId", String.valueOf(assetListEntry.getAssetListEntryId()));

						editURL = editAssetListEntryURL.toString();
					}
					%>

					<liferay-ui:search-container-column-icon
						icon="list"
					/>

					<liferay-ui:search-container-column-text
						colspan="<%= 2 %>"
					>
						<h5>
							<aui:a href="<%= editURL %>">
								<%= HtmlUtil.escape(assetListEntry.getTitle()) %>
							</aui:a>
						</h5>

						<h6 class="text-default">
							<strong><liferay-ui:message key="<%= HtmlUtil.escape(assetListEntry.getTypeLabel()) %>" /></strong>
						</h6>

						<%
						Date statusDate = assetListEntry.getCreateDate();
						%>

						<liferay-ui:message arguments="<%= LanguageUtil.getTimeDescription(request, System.currentTimeMillis() - statusDate.getTime(), true) %>" key="x-ago" translateArguments="<%= false %>" />
					</liferay-ui:search-container-column-text>

					<liferay-ui:search-container-column-jsp
						path="/asset_list_entry_action.jsp"
					/>
				</liferay-ui:search-container-row>

				<liferay-ui:search-iterator
					displayStyle="descriptive"
					markupView="lexicon"
				/>
			</liferay-ui:search-container>
		</c:when>
		<c:otherwise>
			<liferay-frontend:empty-result-message
				actionDropdownItems="<%= assetListDisplayContext.isShowAddAssetListEntryAction() ? assetListDisplayContext.getAddAssetListEntryDropdownItems() : null %>"
				componentId="emptyResultMessageComponent"
				description="<%= assetListDisplayContext.getEmptyResultMessageDescription() %>"
				elementType='<%= LanguageUtil.get(request, "asset-lists") %>'
			/>
		</c:otherwise>
	</c:choose>
</aui:form>

<aui:script require="metal-dom/src/all/dom as dom,frontend-js-web/liferay/modal/commands/OpenSimpleInputModal.es as modalCommands">
	var addAssetListEntry = function(event) {
		event.preventDefault();

		var itemData = event.data.item.data;

		modalCommands.openSimpleInputModal(
			{
				dialogTitle: itemData.title,
				formSubmitURL: itemData.addAssetListEntryURL,
				mainFieldLabel: '<liferay-ui:message key="title" />',
				mainFieldName: 'title',
				mainFieldPlaceholder: '<liferay-ui:message key="title" />',
				namespace: '<portlet:namespace />',
				spritemap: '<%= themeDisplay.getPathThemeImages() %>/lexicon/icons.svg'
			}
		);
	};

	var deleteSelectedAssetListEntries = function() {
		if (confirm('<liferay-ui:message key="are-you-sure-you-want-to-delete-this" />')) {
			submitForm(document.querySelector('#<portlet:namespace />fm'));
		}
	};

	var updateAssetListEntryMenuItemClickHandler = dom.delegate(
		document.body,
		'click',
		'.<portlet:namespace />update-asset-list-entry-action-option > a',
		function(event) {
			var data = event.delegateTarget.dataset;

			event.preventDefault();

			modalCommands.openSimpleInputModal(
				{
					dialogTitle: '<liferay-ui:message key="rename-asset-list" />',
					formSubmitURL: data.formSubmitUrl,
					idFieldName: 'id',
					idFieldValue: data.idFieldValue,
					mainFieldLabel: '<liferay-ui:message key="title" />',
					mainFieldName: 'title',
					mainFieldPlaceholder: '<liferay-ui:message key="title" />',
					mainFieldValue: data.mainFieldValue,
					namespace: '<portlet:namespace />',
					spritemap: '<%= themeDisplay.getPathThemeImages() %>/lexicon/icons.svg'
				}
			);
		}
	);

	function handleDestroyPortlet() {
		updateAssetListEntryMenuItemClickHandler.removeListener();

		Liferay.detach('destroyPortlet', handleDestroyPortlet);
	}

	var ACTIONS = {
		'addAssetListEntry': addAssetListEntry,
		'deleteSelectedAssetListEntries': deleteSelectedAssetListEntries
	};

	Liferay.componentReady('assetListEntriesEntriesManagementToolbar').then(
		function(managementToolbar) {
			managementToolbar.on(
				['actionItemClicked', 'creationMenuItemClicked'],
					function(event) {
						var itemData = event.data.item.data;

						if (itemData && itemData.action && ACTIONS[itemData.action]) {
							ACTIONS[itemData.action](event);
						}
					}
				);
		}
	);

	<c:if test="<%= assetListDisplayContext.getAssetListEntriesCount() == 0 %>">
		Liferay.componentReady('emptyResultMessageComponent').then(
			function(emptyResultMessageComponent) {
				emptyResultMessageComponent.on(
					'itemClicked',
					function(event) {
						var itemData = event.data.item.data;

						if (itemData && itemData.action && ACTIONS[itemData.action]) {
							ACTIONS[itemData.action](event);
						}
					}
				);
			}
		);
	</c:if>

	Liferay.on('destroyPortlet', handleDestroyPortlet);
</aui:script>