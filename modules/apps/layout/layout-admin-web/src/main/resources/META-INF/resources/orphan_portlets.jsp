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
OrphanPortletsDisplayContext orphanPortletsDisplayContext = new OrphanPortletsDisplayContext(request, liferayPortletRequest, liferayPortletResponse);

Layout selLayout = orphanPortletsDisplayContext.getSelLayout();

List<Portlet> portlets = orphanPortletsDisplayContext.getOrphanPortlets();

portletDisplay.setDescription(LanguageUtil.get(request, "orphan-widgets-description"));
portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(orphanPortletsDisplayContext.getBackURL());

renderResponse.setTitle(LanguageUtil.get(request, "orphan-widgets"));
%>

<clay:management-toolbar
	actionDropdownItems="<%= orphanPortletsDisplayContext.getActionDropdownItems() %>"
	componentId="portletsManagementToolbar"
	filterDropdownItems="<%= orphanPortletsDisplayContext.getFilterDropdownItems() %>"
	itemsTotal="<%= portlets.size() %>"
	searchContainerId="portlets"
	showSearch="<%= false %>"
	sortingOrder="<%= orphanPortletsDisplayContext.getOrderByType() %>"
	sortingURL="<%= orphanPortletsDisplayContext.getSortingURL() %>"
	viewTypeItems="<%= orphanPortletsDisplayContext.getViewTypeItems() %>"
/>

<div class="container-fluid-1280">
	<div class="alert alert-warning" role="alert">
		<span class="alert-indicator">
			<aui:icon image="warning-full" markupView="lexicon" />
		</span>

		<strong class="lead"><liferay-ui:message key="warning" />:</strong>

		<c:choose>
			<c:when test="<%= selLayout.isLayoutPrototypeLinkActive() %>">
				<liferay-ui:message key="layout-inherits-from-a-prototype-widgets-cannot-be-manipulated" />
			</c:when>
			<c:otherwise>
				<liferay-ui:message key="warning-preferences-of-selected-widgets-will-be-reset-or-deleted" />
			</c:otherwise>
		</c:choose>

		<button aria-label="Close" class="close" data-dismiss="alert" type="button">
			<aui:icon image="times" markupView="lexicon" />
		</button>
	</div>

	<portlet:actionURL name="/layout/delete_orphan_portlets" var="deleteOrphanPortletsURL">
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="backURL" value="<%= orphanPortletsDisplayContext.getBackURL() %>" />
		<portlet:param name="selPlid" value="<%= String.valueOf(orphanPortletsDisplayContext.getSelPlid()) %>" />
	</portlet:actionURL>

	<aui:form action="<%= deleteOrphanPortletsURL %>" name="fm">
		<liferay-ui:search-container
			deltaConfigurable="<%= false %>"
			id="portlets"
			iteratorURL="<%= orphanPortletsDisplayContext.getPortletURL() %>"
			rowChecker="<%= selLayout.isLayoutPrototypeLinkActive() ? null : new EmptyOnClickRowChecker(liferayPortletResponse) %>"
		>
			<liferay-ui:search-container-results
				results="<%= portlets %>"
			/>

			<liferay-ui:search-container-row
				className="com.liferay.portal.kernel.model.Portlet"
				escapedModel="<%= true %>"
				keyProperty="portletId"
				modelVar="portlet"
			>
				<c:choose>
					<c:when test='<%= Objects.equals(orphanPortletsDisplayContext.getDisplayStyle(), "descriptive") %>'>
						<liferay-ui:search-container-column-icon
							icon="archive"
							toggleRowChecker="<%= true %>"
						/>

						<liferay-ui:search-container-column-text
							colspan="<%= 2 %>"
						>
							<h5>
								<%= PortalUtil.getPortletTitle(portlet, application, locale) %>
							</h5>

							<h6 class="text-default">
								<span><%= portlet.getPortletId() %></span>
							</h6>

							<h6 class="text-default">
								<strong><liferay-ui:message key="status" /></strong>: <%= orphanPortletsDisplayContext.getStatus(portlet) %>
							</h6>
						</liferay-ui:search-container-column-text>

						<liferay-ui:search-container-column-jsp
							path="/orphan_portlets_action.jsp"
						/>
					</c:when>
					<c:when test='<%= Objects.equals(orphanPortletsDisplayContext.getDisplayStyle(), "list") %>'>
						<liferay-ui:search-container-column-text
							name="title"
							truncate="<%= true %>"
							value="<%= PortalUtil.getPortletTitle(portlet, application, locale) %>"
						/>

						<liferay-ui:search-container-column-text
							name="portlet-id"
							property="portletId"
							truncate="<%= true %>"
						/>

						<liferay-ui:search-container-column-text
							name="status"
							value="<%= orphanPortletsDisplayContext.getStatus(portlet) %>"
						/>

						<liferay-ui:search-container-column-jsp
							path="/orphan_portlets_action.jsp"
						/>
					</c:when>
				</c:choose>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				displayStyle="<%= orphanPortletsDisplayContext.getDisplayStyle() %>"
				markupView="lexicon"
				type="none"
			/>
		</liferay-ui:search-container>
	</aui:form>
</div>

<aui:script sandbox="<%= true %>">
	var deleteOrphanPortlets = function() {
		if (confirm('<liferay-ui:message key="are-you-sure-you-want-to-delete-this" />')) {
			submitForm($(document.<portlet:namespace />fm));
		}
	}

	var ACTIONS = {
		'deleteOrphanPortlets': deleteOrphanPortlets
	};

	Liferay.componentReady('portletsManagementToolbar').then(
		function(managementToolbar) {
			managementToolbar.on(
				'actionItemClicked',
				function(event) {
					var itemData = event.data.item.data;

					if (itemData && itemData.action && ACTIONS[itemData.action]) {
						ACTIONS[itemData.action]();
					}
				}
			);
		}
	);
</aui:script>