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

<%@ include file="/screen_navigation/init.jsp" %>

<%
String containerCssClass = (String)request.getAttribute("liferay-frontend:screen-navigation:containerCssClass");
String containerWrapperCssClass = (String)request.getAttribute("liferay-frontend:screen-navigation:containerWrapperCssClass");
String fullContainerCssClass = (String)request.getAttribute("liferay-frontend:screen-navigation:fullContainerCssClass");
String headerContainerCssClass = (String)request.getAttribute("liferay-frontend:screen-navigation:headerContainerCssClass");
String id = (String)request.getAttribute("liferay-frontend:screen-navigation:id");
String menubarCssClass = (String)request.getAttribute("liferay-frontend:screen-navigation:menubarCssClass");
String navCssClass = (String)request.getAttribute("liferay-frontend:screen-navigation:navCssClass");
PortletURL portletURL = (PortletURL)request.getAttribute("liferay-frontend:screen-navigation:portletURL");
ScreenNavigationCategory selectedScreenNavigationCategory = (ScreenNavigationCategory)request.getAttribute("liferay-frontend:screen-navigation:selectedScreenNavigationCategory");
ScreenNavigationEntry selectedScreenNavigationEntry = (ScreenNavigationEntry)request.getAttribute("liferay-frontend:screen-navigation:selectedScreenNavigationEntry");
List<ScreenNavigationCategory> screenNavigationCategories = (List<ScreenNavigationCategory>)request.getAttribute("liferay-frontend:screen-navigation:screenNavigationCategories");
List<ScreenNavigationEntry> screenNavigationEntries = (List<ScreenNavigationEntry>)request.getAttribute("liferay-frontend:screen-navigation:screenNavigationEntries");
%>

<c:if test="<%= screenNavigationCategories.size() > 1 %>">
	<div class="page-header">
		<div class="<%= headerContainerCssClass %>">
			<nav>
				<ul class="nav nav-underline">

					<%
					for (ScreenNavigationCategory screenNavigationCategory : screenNavigationCategories) {
						PortletURL screenNavigationCategoryURL = PortletURLUtil.clone(portletURL, liferayPortletResponse);

						screenNavigationCategoryURL.setParameter("screenNavigationCategoryKey", screenNavigationCategory.getCategoryKey());
						screenNavigationCategoryURL.setParameter("screenNavigationEntryKey", StringPool.BLANK);
					%>

						<li class="nav-item">
							<a class="nav-link <%= Objects.equals(selectedScreenNavigationCategory.getCategoryKey(), screenNavigationCategory.getCategoryKey()) ? "active" : StringPool.BLANK %>" href="<%= screenNavigationCategoryURL.toString() %>"><%= screenNavigationCategory.getLabel(themeDisplay.getLocale()) %></a>
						</li>

					<%
					}
					%>

				</ul>
			</nav>
		</div>
	</div>
</c:if>

<div class="<%= containerWrapperCssClass %>">
	<div class="row">
		<c:if test="<%= screenNavigationEntries.size() > 1 %>">
			<div class="<%= navCssClass %>">
				<nav class="<%= menubarCssClass %>">
					<a aria-controls="<%= id %>" aria-expanded="false" class="menubar-toggler" data-toggle="collapse" href="#<%= id %>" role="button">
						<%= selectedScreenNavigationEntry.getLabel(locale) %>

						<aui:icon image="caret-bottom" markupView="lexicon" />
					</a>

					<div class="collapse menubar-collapse" id="<%= id %>">
						<ul class="nav nav-nested">

							<%
							for (ScreenNavigationEntry screenNavigationEntry : screenNavigationEntries) {
								PortletURL screenNavigationEntryURL = PortletURLUtil.clone(portletURL, liferayPortletResponse);

								screenNavigationEntryURL.setParameter("screenNavigationCategoryKey", screenNavigationEntry.getCategoryKey());
								screenNavigationEntryURL.setParameter("screenNavigationEntryKey", screenNavigationEntry.getEntryKey());
							%>

								<li class="nav-item">
									<a class="nav-link <%= Objects.equals(selectedScreenNavigationEntry.getEntryKey(), screenNavigationEntry.getEntryKey()) ? "active" : StringPool.BLANK %>" href="<%= screenNavigationEntryURL %>"><%= screenNavigationEntry.getLabel(themeDisplay.getLocale()) %></a>
								</li>

							<%
							}
							%>

						</ul>
					</div>
				</nav>
			</div>
		</c:if>

		<div class="<%= (screenNavigationEntries.size() > 1) ? containerCssClass : fullContainerCssClass %>">

			<%
			selectedScreenNavigationEntry.render(request, PipingServletResponse.createPipingServletResponse(pageContext));
			%>

		</div>
	</div>
</div>