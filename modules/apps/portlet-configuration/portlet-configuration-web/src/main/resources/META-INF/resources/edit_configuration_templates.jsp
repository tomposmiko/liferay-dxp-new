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
PortletConfigurationTemplatesDisplayContext portletConfigurationTemplatesDisplayContext = new PortletConfigurationTemplatesDisplayContext(request, renderRequest, renderResponse);
%>

<div class="portlet-configuration-edit-templates">
	<portlet:actionURL name="deleteArchivedSetups" var="deleteArchivedSetupsURL">
		<portlet:param name="mvcPath" value="/edit_configuration_templates.jsp" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="portletConfiguration" value="<%= Boolean.TRUE.toString() %>" />
		<portlet:param name="portletResource" value="<%= portletResource %>" />
	</portlet:actionURL>

	<aui:form action="<%= deleteArchivedSetupsURL %>" name="fm">
		<div class="portlet-configuration-body-content">
			<clay:navigation-bar
				navigationItems="<%= portletConfigurationTemplatesDisplayContext.getNavigationItems() %>"
			/>

			<clay:management-toolbar
				actionDropdownItems="<%= portletConfigurationTemplatesDisplayContext.getActionDropdownItems() %>"
				componentId="archivedSettingsManagementToolbar"
				disabled="<%= portletConfigurationTemplatesDisplayContext.isDisabledManagementBar() %>"
				filterDropdownItems="<%= portletConfigurationTemplatesDisplayContext.getFilterDropdownItems() %>"
				itemsTotal="<%= portletConfigurationTemplatesDisplayContext.getTotalItems() %>"
				searchContainerId="archivedSettings"
				showSearch="<%= false %>"
				sortingOrder="<%= portletConfigurationTemplatesDisplayContext.getOrderByType() %>"
				sortingURL="<%= portletConfigurationTemplatesDisplayContext.getSortingURL() %>"
				viewTypeItems="<%= portletConfigurationTemplatesDisplayContext.getViewTypeItems() %>"
			/>

			<div class="container-fluid-1280">
				<liferay-ui:error exception="<%= NoSuchPortletItemException.class %>" message="the-setup-could-not-be-found" />

				<div class="button-holder text-center">
					<portlet:renderURL var="addConfigurationTemplateURL">
						<portlet:param name="mvcPath" value="/add_configuration_template.jsp" />
						<portlet:param name="redirect" value="<%= currentURL %>" />
						<portlet:param name="portletResource" value="<%= portletResource %>" />
					</portlet:renderURL>

					<aui:button href="<%= addConfigurationTemplateURL %>" value="save-current-configuration-as-template" />
				</div>

				<liferay-ui:search-container
					id="archivedSettings"
					searchContainer="<%= portletConfigurationTemplatesDisplayContext.getArchivedSettingsSearchContainer() %>"
				>
					<liferay-ui:search-container-row
						className="com.liferay.portal.kernel.settings.ArchivedSettings"
						keyProperty="name"
						modelVar="archivedSettings"
					>
						<c:choose>
							<c:when test='<%= Objects.equals(portletConfigurationTemplatesDisplayContext.getDisplayStyle(), "descriptive") %>'>
								<liferay-ui:search-container-column-icon
									icon="archive"
								/>

								<liferay-ui:search-container-column-text
									colspan="<%= 2 %>"
								>
									<h6 class="text-default">
										<liferay-ui:message arguments="<%= new String[] {LanguageUtil.getTimeDescription(locale, System.currentTimeMillis() - archivedSettings.getModifiedDate().getTime(), true), HtmlUtil.escape(archivedSettings.getUserName())} %>" key="x-ago-by-x" translateArguments="<%= false %>" />
									</h6>

									<h5>
										<%= HtmlUtil.escape(archivedSettings.getName()) %>
									</h5>
								</liferay-ui:search-container-column-text>

								<liferay-ui:search-container-column-jsp
									path="/configuration_template_action.jsp"
								/>
							</c:when>
							<c:when test='<%= Objects.equals(portletConfigurationTemplatesDisplayContext.getDisplayStyle(), "icon") %>'>

								<%
								row.setCssClass("entry-card lfr-asset-item");
								%>

								<liferay-ui:search-container-column-text>
									<liferay-frontend:icon-vertical-card
										actionJsp="/configuration_template_action.jsp"
										actionJspServletContext="<%= application %>"
										icon="archive"
										resultRow="<%= row %>"
										title="<%= archivedSettings.getName() %>"
									>
										<liferay-frontend:vertical-card-header>
											<liferay-ui:message arguments="<%= new String[] {LanguageUtil.getTimeDescription(locale, System.currentTimeMillis() - archivedSettings.getModifiedDate().getTime(), true), HtmlUtil.escape(archivedSettings.getUserName())} %>" key="x-ago-by-x" translateArguments="<%= false %>" />
										</liferay-frontend:vertical-card-header>
									</liferay-frontend:icon-vertical-card>
								</liferay-ui:search-container-column-text>
							</c:when>
							<c:when test='<%= Objects.equals(portletConfigurationTemplatesDisplayContext.getDisplayStyle(), "list") %>'>
								<liferay-ui:search-container-column-text
									name="name"
									truncate="<%= true %>"
								>
									<%= HtmlUtil.escape(archivedSettings.getName()) %>
								</liferay-ui:search-container-column-text>

								<liferay-ui:search-container-column-text
									name="user-name"
									truncate="<%= true %>"
								>
									<%= HtmlUtil.escape(archivedSettings.getUserName()) %>
								</liferay-ui:search-container-column-text>

								<liferay-ui:search-container-column-date
									name="modified-date"
									property="modifiedDate"
								/>

								<liferay-ui:search-container-column-jsp
									path="/configuration_template_action.jsp"
								/>
							</c:when>
						</c:choose>
					</liferay-ui:search-container-row>

					<liferay-ui:search-iterator
						displayStyle="<%= portletConfigurationTemplatesDisplayContext.getDisplayStyle() %>"
						markupView="lexicon"
					/>
				</liferay-ui:search-container>
			</div>
		</div>
	</aui:form>
</div>

<aui:script sandbox="<%= true %>">
	var deleteArchivedSettings = function() {
		if (confirm('<liferay-ui:message key="are-you-sure-you-want-to-delete-this" />')) {
			submitForm($(document.<portlet:namespace />fm));
		}
	}

	var ACTIONS = {
		'deleteArchivedSettings': deleteArchivedSettings
	};

	Liferay.componentReady('archivedSettingsManagementToolbar').then(
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