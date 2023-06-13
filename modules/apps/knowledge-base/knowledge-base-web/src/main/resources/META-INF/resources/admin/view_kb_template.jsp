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

<%@ include file="/admin/init.jsp" %>

<c:choose>
	<c:when test='<%= GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-156421")) %>'>
		<liferay-util:include page="/admin/common/vertical_menu.jsp" servletContext="<%= application %>" />

		<div class="knowledge-base-admin-content">
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/admin/common/top_tabs.jsp" servletContext="<%= application %>" />
	</c:otherwise>
</c:choose>

<%
KBTemplate kbTemplate = (KBTemplate)request.getAttribute(KBWebKeys.KNOWLEDGE_BASE_KB_TEMPLATE);
%>

<div class="container-fluid container-fluid-max-xl container-form-lg">
	<div class="kb-article sheet">
		<div class="kb-entity-body">
			<div class="kb-article-title">
				<%= HtmlUtil.escape(kbTemplate.getTitle()) %>
			</div>

			<%= kbTemplate.getContent() %>

			<liferay-util:include page="/admin/kb_template_comments.jsp" servletContext="<%= application %>" />
		</div>
	</div>
</div>

<c:if test='<%= GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-156421")) %>'>
	</div>
</c:if>