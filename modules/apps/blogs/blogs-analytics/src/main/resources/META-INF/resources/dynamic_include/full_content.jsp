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
AssetEntry assetEntry = (AssetEntry)request.getAttribute("view.jsp-assetEntry");

BlogsEntry blogsEntry = (BlogsEntry)request.getAttribute(WebKeys.BLOGS_ENTRY);
%>

<aui:script require="blogs-analytics@1.0.0/js/track-blogs-entry.es as trackBlogsEntry">
	trackBlogsEntry.default('<%= blogsEntry.getEntryId() %>', '<portlet:namespace /><%= assetEntry.getEntryId() %>');
</aui:script>