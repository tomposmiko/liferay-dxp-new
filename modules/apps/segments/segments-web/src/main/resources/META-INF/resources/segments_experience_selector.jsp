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
SegmentsExperienceSelectorDisplayContext segmentsExperienceSelectorDisplayContext = (SegmentsExperienceSelectorDisplayContext)request.getAttribute(SegmentsExperienceSelectorDisplayContext.class.getName());
%>

<div class="border-left border-secondary control-menu-nav-item ml-3 pl-3">
	<react:component
		module="js/components/ExperiencePicker"
		props="<%= segmentsExperienceSelectorDisplayContext.getData() %>"
	/>
</div>