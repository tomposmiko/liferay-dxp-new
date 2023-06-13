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

<%@ include file="/dynamic_include/init.jsp" %>

<%
LayoutInformationMessagesDisplayContext layoutInformationMessagesDisplayContext = new LayoutInformationMessagesDisplayContext(request);
%>

<li class="control-menu-nav-item lfr-portal-tooltip">
	<clay:button
		aria-label='<%= LanguageUtil.get(request, "additional-information") %>'
		cssClass="control-menu-icon icon-monospaced"
		data-qa-id="info"
		displayType="unstyled"
		symbol="information-live"
		title='<%= LanguageUtil.get(request, "additional-information") %>'
	/>

	<react:component
		data="<%= layoutInformationMessagesDisplayContext.getData() %>"
		module="js/dynamic_include/InformationMessages"
	/>
</li>