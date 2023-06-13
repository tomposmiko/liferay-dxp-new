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
long fragmentEntryId = ParamUtil.getLong(renderRequest, "fragmentEntryId");

FragmentEntry fragmentEntry = FragmentEntryLocalServiceUtil.fetchFragmentEntry(fragmentEntryId);

String css = BeanParamUtil.getString(fragmentEntry, renderRequest, "css");
String html = BeanParamUtil.getString(fragmentEntry, renderRequest, "html");
String js = BeanParamUtil.getString(fragmentEntry, renderRequest, "js");

FragmentEntryLink fragmentEntryLink = FragmentEntryLinkLocalServiceUtil.createFragmentEntryLink(0);

fragmentEntryLink.setCss(css);
fragmentEntryLink.setHtml(html);
fragmentEntryLink.setJs(js);
fragmentEntryLink.setFragmentEntryId(fragmentEntryId);

DefaultFragmentRendererContext defaultFragmentRendererContext = new DefaultFragmentRendererContext(fragmentEntryLink);

defaultFragmentRendererContext.setMode(FragmentEntryLinkConstants.VIEW);

try {
%>

	<%= fragmentRendererController.render(defaultFragmentRendererContext, request, response) %>

<%
}
catch (IOException ioe) {
%>

	<div class="alert alert-danger">
		<liferay-ui:message key="<%= ioe.getMessage() %>" />
	</div>

<%
}
%>