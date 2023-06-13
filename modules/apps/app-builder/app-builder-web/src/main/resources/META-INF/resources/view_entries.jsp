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

<div id="<portlet:namespace />-app-builder-root">

	<%
	AppBuilderApp appBuilderApp = (AppBuilderApp)request.getAttribute(AppBuilderWebKeys.APP);

	Map<String, Object> data = new HashMap<>();

	data.put("appDeploymentType", request.getAttribute(AppBuilderWebKeys.APP_DEPLOYMENT_TYPE));
	data.put("appId", appBuilderApp.getAppBuilderAppId());
	data.put("basePortletURL", String.valueOf(renderResponse.createRenderURL()));
	data.put("dataDefinitionId", appBuilderApp.getDdmStructureId());
	data.put("dataLayoutId", appBuilderApp.getDdmStructureLayoutId());
	data.put("dataListViewId", appBuilderApp.getDeDataListViewId());
	data.put("showFormView", request.getAttribute(AppBuilderWebKeys.SHOW_FORM_VIEW));
	data.put("showTableView", request.getAttribute(AppBuilderWebKeys.SHOW_TABLE_VIEW));
	%>

	<react:component
		data="<%= data %>"
		module="js/pages/entry/ViewEntriesApp.es"
	/>
</div>