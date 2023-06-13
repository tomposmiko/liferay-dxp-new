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
Group group = layoutsAdminDisplayContext.getGroup();

LayoutSet layoutSet = layoutsAdminDisplayContext.getSelLayoutSet();

Theme rootTheme = layoutSet.getTheme();

Layout selLayout = layoutsAdminDisplayContext.getSelLayout();

PortletURL redirectURL = layoutsAdminDisplayContext.getRedirectURL();
%>

<liferay-ui:error-marker
	key="<%= WebKeys.ERROR_SECTION %>"
	value="look-and-feel"
/>

<aui:model-context bean="<%= selLayout %>" model="<%= Layout.class %>" />

<%
LayoutLookAndFeelDisplayContext layoutLookAndFeelDisplayContext = new LayoutLookAndFeelDisplayContext(request, layoutsAdminDisplayContext, liferayPortletResponse);
%>

<aui:input name="devices" type="hidden" value="regular" />

<liferay-util:buffer
	var="rootNodeNameLink"
>
	<c:choose>
		<c:when test="<%= themeDisplay.isStateExclusive() %>">
			<liferay-ui:message key="see-theme-configuration" />
		</c:when>
		<c:otherwise>
			<clay:link
				href="<%= redirectURL.toString() %>"
				label='<%= LanguageUtil.get(request, "see-theme-configuration") %>'
			/>
		</c:otherwise>
	</c:choose>
</liferay-util:buffer>

<%
String taglibLabel = null;

if (group.isLayoutPrototype()) {
	taglibLabel = LanguageUtil.get(request, "use-the-same-look-and-feel-of-the-pages-in-which-this-template-is-used");
}
else {
	taglibLabel = LanguageUtil.format(request, "use-the-inherited-theme-x", rootNodeNameLink, false);
}
%>

<clay:sheet-section
	cssClass='<%= (selLayout.getMasterLayoutPlid() <= 0) ? "mb-5" : "hide mb-5" %>'
	id='<%= liferayPortletResponse.getNamespace() + "themeContainer" %>'
>
	<h3 class="mb-4 text-uppercase"><liferay-ui:message key="theme" /></h3>

	<clay:radio
		checked="<%= selLayout.isInheritLookAndFeel() %>"
		id='<%= liferayPortletResponse.getNamespace() + "regularInheritLookAndFeel" %>'
		label="<%= taglibLabel %>"
		name='<%= liferayPortletResponse.getNamespace() + "regularInheritLookAndFeel" %>'
		value="true"
	/>

	<clay:radio
		checked="<%= !selLayout.isInheritLookAndFeel() %>"
		id='<%= liferayPortletResponse.getNamespace() + "regularUniqueLookAndFeel" %>'
		label='<%= LanguageUtil.get(request, "define-a-custom-theme-for-this-page") %>'
		name='<%= liferayPortletResponse.getNamespace() + "regularInheritLookAndFeel" %>'
		value="false"
	/>

	<c:if test="<%= !group.isLayoutPrototype() %>">
		<div class="lfr-inherit-theme-options <%= selLayout.isInheritLookAndFeel() ? StringPool.BLANK : "hide" %>" id="<portlet:namespace />inheritThemeOptions">
			<liferay-util:include page="/look_and_feel_themes.jsp" servletContext="<%= application %>">
				<liferay-util:param name="companyId" value="<%= String.valueOf(group.getCompanyId()) %>" />
				<liferay-util:param name="editable" value="<%= Boolean.FALSE.toString() %>" />
				<liferay-util:param name="themeId" value="<%= rootTheme.getThemeId() %>" />
			</liferay-util:include>
		</div>
	</c:if>

	<div class="lfr-inherit-theme-options <%= !selLayout.isInheritLookAndFeel() ? StringPool.BLANK : "hide" %>" id="<portlet:namespace />themeOptions">
		<liferay-util:include page="/look_and_feel_themes.jsp" servletContext="<%= application %>" />
	</div>
</clay:sheet-section>

<hr class="mb-5 separator" />

<clay:sheet-section
	cssClass="mb-5"
>
	<h3 class="mb-4 text-uppercase"><liferay-ui:message key="basic-settings" /></h3>

	<div>
		<react:component
			module="js/layout/look_and_feel/Favicon"
			props="<%= layoutsAdminDisplayContext.getFaviconButtonProps() %>"
		/>
	</div>

	<div class="d-flex">
		<c:if test="<%= layoutLookAndFeelDisplayContext.hasEditableMasterLayout() %>">
			<div class="flex-grow-1 mr-4">
				<react:component
					module="js/layout/look_and_feel/MasterLayoutConfiguration"
					props="<%= layoutLookAndFeelDisplayContext.getMasterLayoutConfigurationProps() %>"
				/>
			</div>
		</c:if>

		<div class="flex-grow-1">
			<react:component
				module="js/layout/look_and_feel/StyleBookConfiguration"
				props="<%= layoutLookAndFeelDisplayContext.getStyleBookConfigurationProps() %>"
			/>
		</div>
	</div>
</clay:sheet-section>

<hr class="mb-5 separator" />

<clay:sheet-section
	cssClass="mb-5"
>
	<h3 class="mb-4 text-uppercase"><liferay-ui:message key="customization" /></h3>

	<%
	List<TabsItem> tabsItems = layoutLookAndFeelDisplayContext.getTabsItems();
	%>

	<clay:tabs
		tabsItems="<%= tabsItems %>"
	>

		<%
		for (TabsItem tabsItem : tabsItems) {
		%>

			<div>
				<liferay-util:include page='<%= "/layout/" + tabsItem.get("panelId") + ".jsp" %>' servletContext="<%= application %>" />
			</div>

		<%
		}
		%>

	</clay:tabs>
</clay:sheet-section>

<aui:script sandbox="<%= true %>">
	const regularCss = document.getElementById('<portlet:namespace />regularCss');
	const regularCssLabel = document.querySelector(
		'[for="<portlet:namespace />regularCss"]'
	);
	const regularInheritLookAndFeel = document.getElementById(
		'<portlet:namespace />regularInheritLookAndFeel'
	);
	const regularUniqueLookAndFeel = document.getElementById(
		'<portlet:namespace />regularUniqueLookAndFeel'
	);
	const inheritThemeOptions = document.getElementById(
		'<portlet:namespace />inheritThemeOptions'
	);
	const themeOptions = document.getElementById(
		'<portlet:namespace />themeOptions'
	);

	regularInheritLookAndFeel.addEventListener('change', (event) => {
		event.target.checked = true;
		regularUniqueLookAndFeel.checked = false;

		inheritThemeOptions.classList.toggle('hide');
		themeOptions.classList.toggle('hide');

		Liferay.Util.toggleDisabled([regularCss, regularCssLabel], true);
	});

	regularUniqueLookAndFeel.addEventListener('change', (event) => {
		event.target.checked = true;
		regularInheritLookAndFeel.checked = false;

		inheritThemeOptions.classList.toggle('hide');
		themeOptions.classList.toggle('hide');

		Liferay.Util.toggleDisabled([regularCss, regularCssLabel], false);
	});
</aui:script>

<c:if test="<%= layoutLookAndFeelDisplayContext.hasStyleBooks() %>">
	<aui:script sandbox="<%= true %>">
		const regularInheritLookAndFeel = document.getElementById(
			'<portlet:namespace />regularInheritLookAndFeel'
		);

		const regularUniqueLookAndFeelCheckbox = document.getElementById(
			'<portlet:namespace />regularUniqueLookAndFeel'
		);

		const styleBookWarning = document.getElementById(
			'<portlet:namespace />styleBookWarning'
		);

		regularInheritLookAndFeel.addEventListener('change', (event) => {
			if (event.target.checked) {
				styleBookWarning.classList.add('hide');
			}
		});

		regularUniqueLookAndFeelCheckbox.addEventListener('change', (event) => {
			if (event.target.checked) {
				styleBookWarning.classList.remove('hide');
			}
		});
	</aui:script>
</c:if>