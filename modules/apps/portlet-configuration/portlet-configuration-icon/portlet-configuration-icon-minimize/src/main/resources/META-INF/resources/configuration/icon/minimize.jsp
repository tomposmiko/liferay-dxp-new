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

<%@ include file="/configuration/icon/init.jsp" %>

<liferay-util:buffer
	var="onClickBuffer"
>
	Liferay.Portlet.minimize('#p_p_id_<%= portletDisplay.getId() %>_', this);
</liferay-util:buffer>

<liferay-ui:icon
	iconCssClass="portlet-minimize portlet-minimize-icon"
	message='<%= portletDisplay.isStateMin() ? "restore" : "minimize" %>'
	onClick="<%= onClickBuffer %>"
	url="javascript:void(0);"
/>