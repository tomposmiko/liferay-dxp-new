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

<%@ include file="/icon_options/init.jsp" %>

<%
List<PortletConfigurationIcon> portletConfigurationIcons = (List<PortletConfigurationIcon>)request.getAttribute("liferay-frontend:icon-options:portletConfigurationIcons");
%>

<clay:dropdown-menu
	borderless="<%= true %>"
	cssClass="component-action portlet-options text-white"
	displayType="secondary"
	dropdownItems='<%= (List<DropdownItem>)request.getAttribute("liferay-frontend:icon-options:dropdownItems") %>'
	icon="ellipsis-v"
	monospaced='<%= (boolean)request.getAttribute("liferay-frontend:icon-options:monospaced") %>'
	propsTransformer="icon_options/js/PortletOptionsDropdownPropsTransformer"
	small="<%= true %>"
/>

<%
for (PortletConfigurationIcon portletConfigurationIcon : portletConfigurationIcons) {
	portletConfigurationIcon.include(request, PipingServletResponseFactory.createPipingServletResponse(pageContext));
}
%>