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
DisplayPageTypeSiteNavigationMenuTypeDisplayContext displayPageTypeSiteNavigationMenuTypeDisplayContext = (DisplayPageTypeSiteNavigationMenuTypeDisplayContext)request.getAttribute(DisplayPageTypeSiteNavigationMenuTypeDisplayContext.class.getName());
%>

<c:choose>
	<c:when test="<%= displayPageTypeSiteNavigationMenuTypeDisplayContext.isFFMultipleSelectionEnabled() %>">
		<div>
			<react:component
				module="js/DisplayPageItemContextualSidebar"
				props="<%= displayPageTypeSiteNavigationMenuTypeDisplayContext.getDisplayPageItemContextualSidebarContext() %>"
			/>
		</div>
	</c:when>
	<c:otherwise>
		<aui:input id="classNameId" name="TypeSettingsProperties--classNameId--" type="hidden" value="<%= displayPageTypeSiteNavigationMenuTypeDisplayContext.getClassNameId() %>">
			<aui:validator name="required" />
		</aui:input>

		<aui:input id="classPK" name="TypeSettingsProperties--classPK--" type="hidden" value="<%= displayPageTypeSiteNavigationMenuTypeDisplayContext.getClassPK() %>">
			<aui:validator name="required" />
		</aui:input>

		<aui:input id="classTypeId" name="TypeSettingsProperties--classTypeId--" type="hidden" value="<%= displayPageTypeSiteNavigationMenuTypeDisplayContext.getClassTypeId() %>" />

		<aui:input id="title" name="TypeSettingsProperties--title--" type="hidden" value="<%= displayPageTypeSiteNavigationMenuTypeDisplayContext.getTitle() %>" />

		<aui:input id="type" name="TypeSettingsProperties--type--" type="hidden" value="<%= displayPageTypeSiteNavigationMenuTypeDisplayContext.getType() %>" />

		<aui:input autoFocus="<%= true %>" disabled="<%= true %>" label="title" localized="<%= false %>" name="originalTitle" placeholder="title" type="text" value="<%= displayPageTypeSiteNavigationMenuTypeDisplayContext.getOriginalTitle() %>" />

		<div>
			<p class="list-group-title">
				<liferay-ui:message key="item-type" />
			</p>

			<p class="small" id="<portlet:namespace />itemTypeLabel">
				<%= HtmlUtil.escape(displayPageTypeSiteNavigationMenuTypeDisplayContext.getItemType()) %>
			</p>
		</div>

		<%
		String itemSubtype = displayPageTypeSiteNavigationMenuTypeDisplayContext.getItemSubtype();
		%>

		<div class="<%= Validator.isNull(itemSubtype) ? "d-none" : "" %>" id="<portlet:namespace />itemSubtype">
			<div>
				<p class="list-group-title">
					<liferay-ui:message key="item-subtype" />
				</p>

				<p class="small" id="<portlet:namespace />itemSubtypeLabel">
					<%= HtmlUtil.escape(itemSubtype) %>
				</p>
			</div>
		</div>

		<clay:button
			additionalProps="<%= displayPageTypeSiteNavigationMenuTypeDisplayContext.getChooseInfoItemButtonContext() %>"
			cssClass="mb-4"
			displayType="secondary"
			id='<%= liferayPortletResponse.getNamespace() + "chooseInfoItem" %>'
			label='<%= LanguageUtil.get(resourceBundle, "choose") %>'
			propsTransformer="js/ChooseInfoItemButtonPropsTransformer"
			small="<%= true %>"
		/>
	</c:otherwise>
</c:choose>