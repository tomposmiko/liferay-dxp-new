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
User selUser = (User)request.getAttribute(UsersAdminWebKeys.SELECTED_USER);
%>

<div class="autofit-padded-no-gutters autofit-row">
	<span class="autofit-col autofit-col-expand">
		<h4 class="sheet-tertiary-title">
			<liferay-ui:message key="custom-fields" />
		</h4>
	</span>

	<c:if test="<%= PortletPermissionUtil.contains(permissionChecker, PortletKeys.EXPANDO, ActionKeys.ACCESS_IN_CONTROL_PANEL) %>">
		<span class="autofit-col">

			<%
			boolean hasVisibleCustomFields = CustomFieldsUtil.hasVisibleCustomFields(company.getCompanyId(), User.class);

			PortletProvider.Action action = PortletProvider.Action.EDIT;

			if (hasVisibleCustomFields) {
				action = PortletProvider.Action.MANAGE;
			}

			PortletURL customFieldsURL = PortletProviderUtil.getPortletURL(request, ExpandoColumn.class.getName(), action);

			customFieldsURL.setParameter("redirect", currentURL);
			customFieldsURL.setParameter("modelResource", User.class.getName());
			%>

			<liferay-ui:icon
				cssClass="modify-link"
				label="<%= true %>"
				linkCssClass="btn btn-secondary btn-sm"
				message='<%= hasVisibleCustomFields ? "manage" : "add" %>'
				method="get"
				url="<%= customFieldsURL.toString() %>"
			/>
		</span>
	</c:if>
</div>

<liferay-expando:custom-attribute-list
	className="com.liferay.portal.kernel.model.User"
	classPK="<%= (selUser != null) ? selUser.getUserId() : 0 %>"
	editable="<%= true %>"
	label="<%= true %>"
/>