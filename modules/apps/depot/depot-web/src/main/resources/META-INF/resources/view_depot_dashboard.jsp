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
DepotAdminViewDepotDashboardDisplayContext depotAdminViewDepotDashboardDisplayContext = (DepotAdminViewDepotDashboardDisplayContext)request.getAttribute(DepotAdminViewDepotDashboardDisplayContext.class.getName());

boolean panelsShown = false;
%>

<clay:container-fluid
	cssClass="lfr-depot-dashboard-container"
>
	<liferay-ui:breadcrumb
		showLayout="<%= false %>"
	/>

	<%
	for (PanelCategory panelCategory : depotAdminViewDepotDashboardDisplayContext.getPanelCategories()) {
		Collection<PanelApp> panelApps = depotAdminViewDepotDashboardDisplayContext.getPanelApps(panelCategory);

		panelsShown = panelsShown || !panelApps.isEmpty();
	%>

		<c:if test="<%= !panelApps.isEmpty() %>">
			<div class="spliter-spaced splitter">
				<%= panelCategory.getLabel(locale) %>
			</div>

			<ul class="display-style-icon list-unstyled row">

				<%
				for (PanelApp panelApp : panelApps) {
				%>

					<li class="entry-card entry-display-style lfr-asset-item">
						<c:choose>
							<c:when test="<%= depotAdminViewDepotDashboardDisplayContext.isPrimaryPanelCategory(panelCategory) %>">
								<clay:vertical-card
									verticalCard="<%= depotAdminViewDepotDashboardDisplayContext.getDepotDashboardApplicationVerticalCard(panelApp, locale) %>"
								/>
							</c:when>
							<c:otherwise>
								<clay:horizontal-card
									horizontalCard="<%= depotAdminViewDepotDashboardDisplayContext.getDepotDashboardApplicationHorizontalCard(panelApp, locale) %>"
								/>
							</c:otherwise>
						</c:choose>
					</li>

				<%
				}
				%>

			</ul>
		</c:if>

	<%
	}
	%>

	<c:if test="<%= !panelsShown %>">
		<clay:alert
			displayType="info"
			message="you-do-not-have-access-to-any-applications-in-this-asset-library"
		/>
	</c:if>
</clay:container-fluid>