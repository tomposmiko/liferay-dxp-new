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

<div class="frontend-js-components-sample-web">
	<liferay-ui:tabs
		names="Management Toolbar, Test Walkable, Translation Manager, Walkable"
		refresh="<%= false %>"
	>
		<liferay-ui:section>
			<liferay-util:include page="/partials/management_toolbar.jsp" servletContext="<%= application %>" />
		</liferay-ui:section>

		<liferay-ui:section>
			<liferay-util:include page="/partials/test_walkable.jsp" servletContext="<%= application %>" />
		</liferay-ui:section>

		<liferay-ui:section>
			<liferay-util:include page="/partials/translation_manager.jsp" servletContext="<%= application %>" />
		</liferay-ui:section>

		<liferay-ui:section>
			<liferay-util:include page="/partials/walkable.jsp" servletContext="<%= application %>" />
		</liferay-ui:section>
	</liferay-ui:tabs>
</div>