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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/ddm" prefix="liferay-ddm" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.portal.kernel.dao.search.SearchContainer" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.search.Document" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %><%@
page import="com.liferay.portal.search.web.internal.result.display.context.SearchResultSummaryDisplayContext" %><%@
page import="com.liferay.portal.search.web.internal.search.results.configuration.SearchResultsPortletInstanceConfiguration" %><%@
page import="com.liferay.portal.search.web.internal.search.results.portlet.SearchResultsPortletDisplayContext" %>

<%@ page import="java.util.HashMap" %><%@
page import="java.util.List" %><%@
page import="java.util.Map" %>

<portlet:defineObjects />

<%
SearchResultsPortletDisplayContext searchResultsPortletDisplayContext = (SearchResultsPortletDisplayContext)java.util.Objects.requireNonNull(request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT));

if (searchResultsPortletDisplayContext.isRenderNothing()) {
	return;
}

SearchResultsPortletInstanceConfiguration searchResultsPortletInstanceConfiguration = searchResultsPortletDisplayContext.getSearchResultsPortletInstanceConfiguration();

List<SearchResultSummaryDisplayContext> searchResultSummaryDisplayContexts = searchResultsPortletDisplayContext.getSearchResultSummaryDisplayContexts();

SearchContainer<Document> searchContainer = searchResultsPortletDisplayContext.getSearchContainer();

Map<String, Object> contextObjects = new HashMap<String, Object>();

contextObjects.put("namespace", renderResponse.getNamespace());
contextObjects.put("searchContainer", searchContainer);
contextObjects.put("searchResultsPortletDisplayContext", searchResultsPortletDisplayContext);
%>

<c:choose>
	<c:when test="<%= searchResultSummaryDisplayContexts.isEmpty() %>">
		<div class="sheet taglib-empty-result-message">
			<div class="taglib-empty-result-message-header"></div>

			<div class="sheet-text text-center">
				<%= LanguageUtil.format(request, "no-results-were-found-that-matched-the-keywords-x", "<strong>" + HtmlUtil.escape(searchResultsPortletDisplayContext.getKeywords()) + "</strong>", false) %>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<liferay-ddm:template-renderer
			className="<%= SearchResultSummaryDisplayContext.class.getName() %>"
			contextObjects="<%= contextObjects %>"
			displayStyle="<%= searchResultsPortletInstanceConfiguration.displayStyle() %>"
			displayStyleGroupId="<%= searchResultsPortletDisplayContext.getDisplayStyleGroupId() %>"
			entries="<%= searchResultSummaryDisplayContexts %>"
		/>

		<aui:form useNamespace="<%= false %>">
			<liferay-ui:search-paginator
				id='<%= renderResponse.getNamespace() + "searchContainerTag" %>'
				markupView="lexicon"
				searchContainer="<%= searchContainer %>"
			/>
		</aui:form>
	</c:otherwise>
</c:choose>