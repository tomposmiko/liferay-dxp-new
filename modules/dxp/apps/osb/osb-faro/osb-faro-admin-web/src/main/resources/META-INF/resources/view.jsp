<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/init.jsp" %>

<%
String tabs1 = ParamUtil.getString(request, "tabs1", "projects");
%>

<clay:navigation-bar
	inverted="<%= true %>"
	navigationItems='<%=
		new JSPNavigationItemList(pageContext) {
			{
				add(
					navigationItem -> {
						navigationItem.setActive(tabs1.equals("projects"));
						navigationItem.setHref(renderResponse.createRenderURL(), "mvcPath", "/view.jsp", "tabs1", "projects");
						navigationItem.setLabel(LanguageUtil.get(request, "projects"));
					});
				add(
					navigationItem -> {
						navigationItem.setActive(tabs1.equals("system_info"));
						navigationItem.setHref(renderResponse.createRenderURL(), "mvcPath", "/view.jsp", "tabs1", "system_info");
						navigationItem.setLabel(LanguageUtil.get(request, "system-info"));
					});
			}
		}
	%>'
/>

<c:choose>
	<c:when test='<%= tabs1.equals("projects") %>'>
		<liferay-util:include page="/projects.jsp" servletContext="<%= application %>" />
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/system_info.jsp" servletContext="<%= application %>" />
	</c:otherwise>
</c:choose>