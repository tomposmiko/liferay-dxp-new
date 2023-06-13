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

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/ddm" prefix="liferay-ddm" %>

<%@ page import="com.liferay.portal.kernel.dao.search.SearchContainer" %><%@
page import="com.liferay.portal.kernel.model.User" %><%@
page import="com.liferay.portal.kernel.search.Document" %><%@
page import="com.liferay.portal.kernel.util.HashMapBuilder" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %><%@
page import="com.liferay.portal.search.web.internal.result.display.context.SearchResultSummaryDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.search.results.configuration.SearchResultsPortletInstanceConfiguration" %><%@
page import="com.liferay.portal.search.web.internal.search.results.portlet.SearchResultsPortletDisplayContext" %>

<%@ page import="java.util.List" %>

<portlet:defineObjects />

<%
SearchResultsPortletDisplayContext searchResultsPortletDisplayContext = (SearchResultsPortletDisplayContext)java.util.Objects.requireNonNull(request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT));

if (searchResultsPortletDisplayContext.isRenderNothing()) {
	return;
}

SearchResultsPortletInstanceConfiguration searchResultsPortletInstanceConfiguration = searchResultsPortletDisplayContext.getSearchResultsPortletInstanceConfiguration();

List<SearchResultSummaryDisplayContext> searchResultSummaryDisplayContexts = searchResultsPortletDisplayContext.getSearchResultSummaryDisplayContexts();

SearchContainer<Document> searchContainer = searchResultsPortletDisplayContext.getSearchContainer();
%>

<liferay-ddm:template-renderer
	className="<%= SearchResultSummaryDisplayContext.class.getName() %>"
	contextObjects='<%=
		HashMapBuilder.<String, Object>put(
			"namespace", liferayPortletResponse.getNamespace()
		).put(
			"searchContainer", searchContainer
		).put(
			"searchResultsPortletDisplayContext", searchResultsPortletDisplayContext
		).put(
			"userClassName", User.class.getName()
		).build()
	%>'
	displayStyle="<%= searchResultsPortletInstanceConfiguration.displayStyle() %>"
	displayStyleGroupId="<%= searchResultsPortletDisplayContext.getDisplayStyleGroupId() %>"
	entries="<%= searchResultSummaryDisplayContexts %>"
/>