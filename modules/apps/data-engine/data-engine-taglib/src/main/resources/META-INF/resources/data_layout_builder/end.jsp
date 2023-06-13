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

<%@ include file="/data_layout_builder/init.jsp" %>

<div id="<%= componentId + "container" %>"></div>

<portlet:renderURL var="basePortletURL" />

<%
Map<String, Object> data = new HashMap<>();

data.put("context", dataLayoutJSONObject);
data.put("dataDefinitionId", dataDefinitionId);
data.put("dataLayoutBuilderElementId", renderResponse.getNamespace() + "-data-layout-builder");
data.put("dataLayoutBuilderId", componentId);
data.put("dataLayoutId", dataLayoutId);
data.put("fieldTypes", fieldTypesJSONArray);
data.put("fieldTypesModules", fieldTypesModules);
data.put("spritemap", themeDisplay.getPathThemeImages() + "/lexicon/icons.svg");
%>

<react:component
	data="<%= data %>"
	module="data_layout_builder/js/App.es"
/>