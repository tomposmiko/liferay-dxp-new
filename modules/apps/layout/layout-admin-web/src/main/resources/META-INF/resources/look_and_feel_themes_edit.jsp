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
Layout selLayout = layoutsAdminDisplayContext.getSelLayout();
LayoutSet selLayoutSet = layoutsAdminDisplayContext.getSelLayoutSet();
%>

<p class="h4 mb-3 mt-4"><liferay-ui:message key="current-theme" /></p>

<div id="<portlet:namespace />currentThemeContainer">
	<liferay-util:include page="/look_and_feel_theme_details.jsp" servletContext="<%= application %>" />
</div>

<aui:input label="insert-custom-css-that-is-loaded-after-the-theme" name="regularCss" placeholder="css" type="textarea" value="<%= (selLayout != null) ? selLayout.getCssText() : selLayoutSet.getCss() %>" />