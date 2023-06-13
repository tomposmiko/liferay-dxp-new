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
String redirect = ParamUtil.getString(request, "redirect");

String backURL = ParamUtil.getString(request, "backURL", redirect);

if (Validator.isNull(backURL)) {
	backURL = PortalUtil.getLayoutFullURL(layoutsAdminDisplayContext.getSelLayout(), themeDisplay);
}

String portletResource = ParamUtil.getString(request, "portletResource");

Layout selLayout = layoutsAdminDisplayContext.getSelLayout();

LayoutRevision layoutRevision = LayoutStagingUtil.getLayoutRevision(selLayout);

String layoutSetBranchName = StringPool.BLANK;

boolean incomplete = false;

if (layoutRevision != null) {
	long layoutSetBranchId = layoutRevision.getLayoutSetBranchId();

	incomplete = StagingUtil.isIncomplete(selLayout, layoutSetBranchId);

	if (incomplete) {
		LayoutSetBranch layoutSetBranch = LayoutSetBranchLocalServiceUtil.getLayoutSetBranch(layoutSetBranchId);

		layoutSetBranchName = layoutSetBranch.getName();

		if (LayoutSetBranchConstants.MASTER_BRANCH_NAME.equals(layoutSetBranchName)) {
			layoutSetBranchName = LanguageUtil.get(request, layoutSetBranchName);
		}

		portletDisplay.setShowStagingIcon(false);
	}
}

if ((layoutRevision != null) && StagingUtil.isIncomplete(selLayout, layoutRevision.getLayoutSetBranchId())) {
	portletDisplay.setShowStagingIcon(false);
}

if (Validator.isNotNull(backURL)) {
	portletDisplay.setShowBackIcon(true);
	portletDisplay.setURLBack(backURL);
}

renderResponse.setTitle(layoutsAdminDisplayContext.getConfigurationTitle(selLayout, locale));
%>

<c:choose>
	<c:when test="<%= incomplete %>">
		<liferay-ui:message arguments="<%= new Object[] {HtmlUtil.escape(selLayout.getName(locale)), HtmlUtil.escape(layoutSetBranchName)} %>" key="the-page-x-is-not-enabled-in-x,-but-is-available-in-other-pages-variations" translateArguments="<%= false %>" />

		<aui:button-row>
			<aui:button id="enableLayoutButton" name="enableLayout" value='<%= LanguageUtil.format(request, "enable-in-x", HtmlUtil.escape(layoutSetBranchName), false) %>' />

			<aui:button cssClass="remove-layout" id="deleteLayoutButton" name="deleteLayout" value="delete-in-all-pages-variations" />

			<script>
				(function () {
					var enableLayoutButton = document.getElementById(
						'<portlet:namespace />enableLayoutButton'
					);

					if (enableLayoutButton) {
						enableLayoutButton.addEventListener('click', (event) => {
							<portlet:actionURL name="/layout_admin/enable_layout" var="enableLayoutURL">
								<portlet:param name="redirect" value="<%= String.valueOf(layoutsAdminDisplayContext.getLayoutScreenNavigationPortletURL()) %>" />
								<portlet:param name="incompleteLayoutRevisionId" value="<%= String.valueOf(layoutRevision.getLayoutRevisionId()) %>" />
							</portlet:actionURL>

							submitForm(document.hrefFm, '<%= enableLayoutURL %>');
						});
					}

					var deleteLayoutButton = document.getElementById(
						'<portlet:namespace />deleteLayoutButton'
					);

					if (deleteLayoutButton) {
						deleteLayoutButton.addEventListener('click', (event) => {
							<portlet:actionURL name="/layout_admin/delete_layout" var="deleteLayoutURL">
								<portlet:param name="redirect" value="<%= String.valueOf(layoutsAdminDisplayContext.getLayoutScreenNavigationPortletURL()) %>" />
								<portlet:param name="selPlid" value="<%= String.valueOf(layoutsAdminDisplayContext.getSelPlid()) %>" />
								<portlet:param name="layoutSetBranchId" value="0" />
							</portlet:actionURL>

							submitForm(document.hrefFm, '<%= deleteLayoutURL %>');
						});
					}
				})();
			</script>
		</aui:button-row>
	</c:when>
	<c:otherwise>
		<liferay-ui:success key='<%= portletResource + "layoutUpdated" %>' message='<%= LanguageUtil.get(resourceBundle, "the-page-was-updated-successfully") %>' />

		<liferay-frontend:screen-navigation
			containerCssClass="col-lg-8"
			containerWrapperCssClass="container-fluid container-fluid-max-xl container-form-lg"
			context="<%= selLayout %>"
			inverted="<%= true %>"
			key="<%= LayoutScreenNavigationEntryConstants.SCREEN_NAVIGATION_KEY_LAYOUT %>"
			menubarCssClass="menubar menubar-transparent menubar-vertical-expand-lg"
			navCssClass="col-lg-3"
			portletURL="<%= layoutsAdminDisplayContext.getLayoutScreenNavigationPortletURL() %>"
		/>
	</c:otherwise>
</c:choose>