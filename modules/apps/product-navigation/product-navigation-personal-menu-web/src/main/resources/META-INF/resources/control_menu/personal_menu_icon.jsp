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

<li class="control-menu-nav-item">
	<span class="user-avatar-link">
		<liferay-util:buffer
			var="userAvatar"
		>
			<span class="sticker">
				<liferay-ui:user-portrait
					user="<%= user %>"
				/>

				<c:if test="<%= themeDisplay.isImpersonated() %>">
					<span class="sticker sticker-bottom-right sticker-circle sticker-outside sticker-sm sticker-user-icon">
						<aui:icon image="user" markupView="lexicon" />
					</span>
				</c:if>
			</span>
		</liferay-util:buffer>

		<liferay-product-navigation:personal-menu
			expanded="<%= true %>"
			label="<%= userAvatar %>"
		/>

		<%
		int notificationsCount = GetterUtil.getInteger(request.getAttribute(PersonalMenuWebKeys.NOTIFICATIONS_COUNT));
		%>

		<c:if test="<%= notificationsCount > 0 %>">

			<%
			String notificationsURL = PersonalApplicationURLUtil.getPersonalApplicationURL(request, PortletProviderUtil.getPortletId(UserNotificationEvent.class.getName(), PortletProvider.Action.VIEW));
			%>

			<aui:a href="<%= (notificationsURL != null) ? notificationsURL.toString() : null %>">
				<span class="badge badge-danger panel-notifications-count">
					<span class="badge-item badge-item-expand"><%= notificationsCount %></span>
				</span>
			</aui:a>
		</c:if>
	</span>
</li>