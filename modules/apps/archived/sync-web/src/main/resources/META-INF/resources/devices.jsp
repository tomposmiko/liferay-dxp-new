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
String tabs1 = ParamUtil.getString(request, "tabs1", "devices");

String orderByCol = ParamUtil.getString(request, "orderByCol", "name");
String orderByType = ParamUtil.getString(request, "orderByType", "asc");

PortletURL portletURL = PortletURLBuilder.createRenderURL(
	renderResponse
).setTabs1(
	tabs1
).buildPortletURL();

SearchContainer<SyncDevice> syncDeviceSearchContainer = new SearchContainer<>(renderRequest, portletURL, null, "no-devices-were-found");

syncDeviceSearchContainer.setOrderByCol(orderByCol);

OrderByComparator<SyncDevice> orderByComparator = null;

if (orderByCol.equals("name")) {
	orderByComparator = OrderByComparatorFactoryUtil.create("SyncDevice", "userName", orderByType.equals("asc"));
}
else if (orderByCol.equals("build")) {
	orderByComparator = OrderByComparatorFactoryUtil.create("SyncDevice", "buildNumber", orderByType.equals("asc"));
}
else if (orderByCol.equals("last-seen")) {
	orderByComparator = OrderByComparatorFactoryUtil.create("SyncDevice", "modifiedDate", orderByType.equals("asc"));
}
else {
	orderByComparator = OrderByComparatorFactoryUtil.create("SyncDevice", orderByCol, orderByType.equals("asc"));
}

syncDeviceSearchContainer.setOrderByComparator(orderByComparator);

syncDeviceSearchContainer.setOrderByType(orderByType);

String portletId = (String)request.getAttribute(WebKeys.PORTLET_ID);

if (portletId.equals(SyncPortletKeys.SYNC_ADMIN_PORTLET)) {
	String keywords = ParamUtil.getString(request, "keywords");

	syncDeviceSearchContainer.setResultsAndTotal(SyncDeviceLocalServiceUtil.search(themeDisplay.getCompanyId(), keywords, syncDeviceSearchContainer.getStart(), syncDeviceSearchContainer.getEnd(), orderByComparator));
}
else {
	syncDeviceSearchContainer.setResultsAndTotal(SyncDeviceLocalServiceUtil.getSyncDevices(themeDisplay.getUserId(), syncDeviceSearchContainer.getStart(), syncDeviceSearchContainer.getEnd(), orderByComparator));
}
%>

<clay:management-toolbar
	managementToolbarDisplayContext="<%= new DevicesManagementToolbarDisplayContext(request, liferayPortletRequest, liferayPortletResponse, syncDeviceSearchContainer) %>"
/>

<clay:container-fluid>
	<aui:form method="post" name="fm">
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

		<liferay-ui:search-container
			searchContainer="<%= syncDeviceSearchContainer %>"
		>
			<liferay-ui:search-container-row
				className="com.liferay.sync.model.SyncDevice"
				escapedModel="<%= true %>"
				keyProperty="syncDeviceId"
				modelVar="syncDevice"
			>
				<liferay-ui:search-container-column-text
					cssClass="content-column name-column title-column"
					name="name"
					property="userName"
				/>

				<liferay-ui:search-container-column-text
					name="location"
					value="<%= syncDevice.getHostname() %>"
				/>

				<liferay-ui:search-container-column-text
					name="type"
					orderable="<%= true %>"
				/>

				<liferay-ui:search-container-column-text
					name="build"
					property="buildNumber"
				/>

				<liferay-ui:search-container-column-date
					name="last-seen"
					property="modifiedDate"
				/>

				<liferay-ui:search-container-column-text
					name="status"
					translate="<%= true %>"
					value="<%= SyncDeviceConstants.getStatusLabel(syncDevice.getStatus()) %>"
				/>

				<liferay-ui:search-container-column-jsp
					align="right"
					cssClass="entry-action-column"
					path="/devices_action.jsp"
				/>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				markupView="lexicon"
			/>
		</liferay-ui:search-container>
	</aui:form>
</clay:container-fluid>