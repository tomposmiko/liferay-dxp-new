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

<%@ include file="/personal_menu/init.jsp" %>

<%
String namespace = StringUtil.randomId() + StringPool.UNDERLINE;

boolean expanded = (boolean)request.getAttribute("liferay-product-navigation:personal-menu:expanded");
String label = (String)request.getAttribute("liferay-product-navigation:personal-menu:label");
%>

<style type="text/css">
	#clay_dropdown_portal .dropdown-menu-personal-menu {
		max-height: fit-content;
	}

	#clay_dropdown_portal .dropdown-menu-personal-menu .dropdown-item-indicator {
		padding-right: 0.5rem;
	}

	div.personal-menu-dropdown .btn:focus {
		box-shadow: none;
	}

	div.personal-menu-dropdown .dropdown-item {
		color: #6B6C7E;
	}
</style>

<div class="personal-menu-dropdown" id="<%= namespace + "personal_menu_dropdown" %>">
	<div id="<%= namespace + "personal_menu_dropdown_toggle" %>" style="cursor: pointer;">
		<%= label %>
	</div>
</div>

<%
ResourceURL resourceURL = PortletURLFactoryUtil.create(request, PersonalMenuPortletKeys.PERSONAL_MENU, PortletRequest.RESOURCE_PHASE);

resourceURL.setParameter("currentURL", themeDisplay.getURLCurrent());
resourceURL.setParameter("portletId", themeDisplay.getPpid());
resourceURL.setResourceID("/get_personal_menu_items");
%>

<aui:script require="clay-dropdown/src/ClayDropdown as ClayDropdown,metal-dom/src/dom as dom">
	var toggle = document.getElementById('<%= namespace + "personal_menu_dropdown_toggle" %>');

	if (toggle) {
		dom.once(
			toggle,
			'click',
			function(event) {
				fetch(
					'<%= resourceURL.toString() %>',
					{
						credentials: 'include',
						method: 'GET'
					}
				).then(
					function(response) {
						return response.json();
					}
				).then(
					function(personalMenuItems) {
						var personalMenu = new ClayDropdown.default(
							{
								element: '#<%= namespace + "personal_menu_dropdown_toggle" %>',
								events: {
									'willAttach': function(event) {
										if (<%= expanded %>) {
											this.expanded = true;
										}

										var dropdown = this;

										this.refs.dropdown.refs.portal.on(
											'rendered',
											function(event) {
												if (dropdown.expanded) {
													this.element.classList.add('dropdown-menu-personal-menu');
													this.element.classList.remove('dropdown-menu-indicator-start');
												}
											}
										);
									}
								},
								items: personalMenuItems,
								itemsIconAlignment: 'left',
								label: toggle.innerHTML,
								showToggleIcon: false,
								spritemap: '<%= themeDisplay.getPathThemeImages().concat("/clay/icons.svg") %>'
							},
							'#<%= namespace + "personal_menu_dropdown" %>'
						);

						Liferay.once(
							'beforeNavigate',
							function(event) {
								personalMenu.refs.dropdown.refs.portal.detach();
							}
						);
					}
				);
			}
		);
	}
</aui:script>