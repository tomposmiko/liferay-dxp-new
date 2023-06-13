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
Layout selLayout = layoutsAdminDisplayContext.getSelLayout();

LayoutLookAndFeelDisplayContext layoutLookAndFeelDisplayContext = new LayoutLookAndFeelDisplayContext(request, layoutsAdminDisplayContext, liferayPortletResponse);
%>

<clay:alert
	displayType="info"
	message='<%= LanguageUtil.get(request, "theme-css-client-extension-and-custom-css-are-disabled-when-using-the-inherited-theme") %>'
/>

<liferay-frontend:fieldset
	collapsed="<%= false %>"
	collapsible="<%= true %>"
	label="theme-css-client-extension"
>
	<react:component
		module="js/ThemeCSSReplacementSelector"
		props="<%= layoutsAdminDisplayContext.getThemeCSSReplacementSelectorProps() %>"
	/>
</liferay-frontend:fieldset>

<liferay-frontend:fieldset
	collapsed="<%= false %>"
	collapsible="<%= true %>"
	label="css-client-extensions"
>
	<react:component
		module="js/layout/look_and_feel/GlobalCSSCETsConfiguration"
		props="<%= layoutLookAndFeelDisplayContext.getGlobalCSSCETsConfigurationProps(Layout.class.getName(), selLayout.getPlid()) %>"
	/>
</liferay-frontend:fieldset>

<c:if test='<%= FeatureFlagManagerUtil.isEnabled("LPS-166479") %>'>
	<liferay-frontend:fieldset
		collapsed="<%= false %>"
		collapsible="<%= true %>"
		label="theme-spritemap-client-extension"
	>
		<react:component
			module="js/layout/look_and_feel/ThemeSpritemapCETsConfiguration"
			props="<%= layoutLookAndFeelDisplayContext.getThemeSpritemapCETConfigurationProps(Layout.class.getName(), selLayout.getPlid()) %>"
		/>
	</liferay-frontend:fieldset>
</c:if>

<liferay-frontend:fieldset
	collapsed="<%= false %>"
	collapsible="<%= true %>"
	label="custom-css"
>
	<aui:input disabled="<%= selLayout.isInheritLookAndFeel() %>" label="css" name="regularCss" type="textarea" value="<%= selLayout.getCssText() %>" wrapperCssClass="mb-0" />

	<p class="text-secondary">
		<liferay-ui:message key="this-css-is-loaded-after-the-theme" />
	</p>
</liferay-frontend:fieldset>