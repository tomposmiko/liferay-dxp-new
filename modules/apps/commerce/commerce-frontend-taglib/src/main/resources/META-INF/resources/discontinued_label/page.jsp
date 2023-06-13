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

<%@ include file="/discontinued_label/init.jsp" %>

<span class="<%= discontinued ? StringPool.BLANK : "invisible" + StringPool.SPACE %>label label-danger m-0 <%= namespace %>discontinued-label">
	<span class="label-item label-item-expand"><liferay-ui:message key="discontinued" /></span>
</span>

<liferay-frontend:component
	context='<%=
		HashMapBuilder.<String, Object>put(
			"namespace", namespace
		).build()
	%>'
	module="discontinued_label/js/DiscontinuedLabelCPInstanceChangeHandler"
/>