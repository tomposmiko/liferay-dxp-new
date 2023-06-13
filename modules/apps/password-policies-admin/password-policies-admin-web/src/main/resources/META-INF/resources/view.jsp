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
String displayStyle = ParamUtil.getString(request, "displayStyle");

if (Validator.isNull(displayStyle)) {
	displayStyle = portalPreferences.getValue(PasswordPoliciesAdminPortletKeys.PASSWORD_POLICIES_ADMIN, "display-style", "list");
}
else {
	portalPreferences.setValue(PasswordPoliciesAdminPortletKeys.PASSWORD_POLICIES_ADMIN, "display-style", displayStyle);

	request.setAttribute(WebKeys.SINGLE_PAGE_APPLICATION_CLEAR_CACHE, Boolean.TRUE);
}

boolean passwordPolicyEnabled = LDAPSettingsUtil.isPasswordPolicyEnabled(company.getCompanyId());

String description = LanguageUtil.get(request, "javax.portlet.description.com_liferay_password_policies_admin_web_portlet_PasswordPoliciesAdminPortlet") + " " + LanguageUtil.get(request, "when-no-password-policy-is-assigned-to-a-user,-either-explicitly-or-through-an-organization,-the-default-password-policy-is-used");

portletDisplay.setDescription(description);
%>

<clay:navigation-bar
	inverted="<%= true %>"
	navigationItems="<%= passwordPolicyDisplayContext.getViewPasswordPoliciesNavigationItems() %>"
/>

<%
ViewPasswordPoliciesManagementToolbarDisplayContext viewPasswordPoliciesManagementToolbarDisplayContext = new ViewPasswordPoliciesManagementToolbarDisplayContext(request, renderRequest, renderResponse, displayStyle);

SearchContainer searchContainer = viewPasswordPoliciesManagementToolbarDisplayContext.getSearchContainer();

PortletURL portletURL = viewPasswordPoliciesManagementToolbarDisplayContext.getPortletURL();
%>

<clay:management-toolbar
	actionDropdownItems="<%= viewPasswordPoliciesManagementToolbarDisplayContext.getActionDropdownItems() %>"
	clearResultsURL="<%= viewPasswordPoliciesManagementToolbarDisplayContext.getClearResultsURL() %>"
	creationMenu="<%= viewPasswordPoliciesManagementToolbarDisplayContext.getCreationMenu() %>"
	itemsTotal="<%= searchContainer.getTotal() %>"
	searchActionURL="<%= viewPasswordPoliciesManagementToolbarDisplayContext.getSearchActionURL() %>"
	searchContainerId="passwordPolicies"
	searchFormName="searchFm"
	selectable="<%= true %>"
	showCreationMenu="<%= viewPasswordPoliciesManagementToolbarDisplayContext.showCreationMenu() %>"
	showSearch="<%= true %>"
	sortingOrder="<%= searchContainer.getOrderByType() %>"
	sortingURL="<%= viewPasswordPoliciesManagementToolbarDisplayContext.getSortingURL() %>"
	viewTypeItems="<%= viewPasswordPoliciesManagementToolbarDisplayContext.getViewTypeItems() %>"
/>

<aui:form action="<%= portletURL.toString() %>" cssClass="container-fluid-1280" method="get" name="fm">
	<aui:input name="passwordPolicyIds" type="hidden" />
	<aui:input name="redirect" type="hidden" value="<%= portletURL.toString() %>" />

	<div id="breadcrumb">
		<liferay-ui:breadcrumb
			showCurrentGroup="<%= false %>"
			showGuestGroup="<%= false %>"
			showLayout="<%= false %>"
			showPortletBreadcrumb="<%= true %>"
		/>
	</div>

	<c:if test="<%= passwordPolicyEnabled %>">
		<div class="alert alert-info">
			<liferay-ui:message key="you-are-using-ldaps-password-policy" />
		</div>
	</c:if>

	<c:if test="<%= !passwordPolicyEnabled && windowState.equals(WindowState.MAXIMIZED) %>">
		<liferay-ui:search-container
			id="passwordPolicies"
			searchContainer="<%= searchContainer %>"
			var="passwordPolicySearchContainer"
		>
			<liferay-ui:search-container-row
				className="com.liferay.portal.kernel.model.PasswordPolicy"
				escapedModel="<%= true %>"
				keyProperty="passwordPolicyId"
				modelVar="passwordPolicy"
			>
				<portlet:renderURL var="rowURL">
					<portlet:param name="mvcPath" value="/edit_password_policy.jsp" />
					<portlet:param name="redirect" value="<%= passwordPolicySearchContainer.getIteratorURL().toString() %>" />
					<portlet:param name="passwordPolicyId" value="<%= String.valueOf(passwordPolicy.getPasswordPolicyId()) %>" />
				</portlet:renderURL>

				<%@ include file="/search_columns.jspf" %>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				displayStyle="<%= displayStyle %>"
				markupView="lexicon"
				searchContainer="<%= passwordPolicySearchContainer %>"
			/>
		</liferay-ui:search-container>
	</c:if>
</aui:form>

<aui:script>
	function <portlet:namespace />deletePasswordPolicies() {
		if (confirm('<liferay-ui:message key="are-you-sure-you-want-to-delete-this" />')) {
			var form = document.getElementById('<portlet:namespace />fm');

			if (form) {
				form.setAttribute('method', 'post');

				var passwordPolicyIds = form.querySelector('#<portlet:namespace />passwordPolicyIds');

				if (passwordPolicyIds) {
					passwordPolicyIds.setAttribute('value', Liferay.Util.listCheckedExcept(form, '<portlet:namespace />allRowIds'));
				}

				var lifecycle = form.querySelector('#p_p_lifecycle');

				if (lifecycle) {
					lifecycle.setAttribute('value', '1');
				}

				submitForm(form, '<portlet:actionURL name="deletePasswordPolicies" />');
			}
		}
	}
</aui:script>