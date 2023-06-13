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
long liveGroupId = (long)request.getAttribute("site.liveGroupId");

UnicodeProperties groupTypeSettingsUnicodeProperties = (UnicodeProperties)request.getAttribute("site.groupTypeSettings");

List<Role> defaultSiteRoles = new ArrayList<>();

long[] defaultSiteRoleIds = StringUtil.split(groupTypeSettingsUnicodeProperties.getProperty("defaultSiteRoleIds"), 0L);

for (long defaultSiteRoleId : defaultSiteRoleIds) {
	defaultSiteRoles.add(RoleLocalServiceUtil.getRole(defaultSiteRoleId));
}

List<Team> defaultTeams = new ArrayList<>();

long[] defaultTeamIds = StringUtil.split(groupTypeSettingsUnicodeProperties.getProperty("defaultTeamIds"), 0L);

for (long defaultTeamId : defaultTeamIds) {
	defaultTeams.add(TeamLocalServiceUtil.getTeam(defaultTeamId));
}
%>

<liferay-util:buffer
	var="removeRoleIcon"
>
	<clay:icon
		symbol="times-circle"
	/>
</liferay-util:buffer>

<p class="small text-secondary">
	<liferay-ui:message key="select-the-default-roles-and-teams-for-new-members" />
</p>

<clay:content-row
	containerElement="h3"
	cssClass="sheet-subtitle"
>
	<clay:content-col
		expand="<%= true %>"
	>
		<span class="heading-text"><liferay-ui:message key="site-roles" /></span>
	</clay:content-col>

	<clay:content-col>
		<clay:button
			aria-label='<%= LanguageUtil.get(request, "select") %>'
			cssClass="modify-link"
			displayType="secondary"
			id='<%= liferayPortletResponse.getNamespace() + "selectSiteRoleLink" %>'
			label='<%= LanguageUtil.get(request, "select") %>'
			small="<%= true %>"
			title='<%= LanguageUtil.get(request, "select") %>'
		/>
	</clay:content-col>
</clay:content-row>

<liferay-ui:search-container
	compactEmptyResultsMessage="<%= true %>"
	emptyResultsMessage="none"
	headerNames="title,null"
	id="siteRolesSearchContainer"
	total="<%= defaultSiteRoles.size() %>"
>
	<liferay-ui:search-container-results
		results="<%= defaultSiteRoles %>"
	/>

	<liferay-ui:search-container-row
		className="com.liferay.portal.kernel.model.Role"
		keyProperty="roleId"
		modelVar="role"
	>
		<liferay-ui:search-container-column-text
			name="title"
			truncate="<%= true %>"
			value="<%= HtmlUtil.escape(role.getTitle(locale)) %>"
		/>

		<liferay-ui:search-container-column-text>
			<clay:button
				aria-label='<%= LanguageUtil.get(request, "remove") %>'
				borderless="<%= true %>"
				cssClass="lfr-portal-tooltip modify-link"
				data-rowId="<%= role.getRoleId() %>"
				displayType="secondary"
				icon="times-circle"
				monospaced="<%= true %>"
				title='<%= LanguageUtil.get(request, "remove") %>'
				type="button"
			/>
		</liferay-ui:search-container-column-text>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator
		markupView="lexicon"
		paginate="<%= false %>"
	/>
</liferay-ui:search-container>

<clay:content-row
	containerElement="h3"
	cssClass="sheet-subtitle"
>
	<clay:content-col
		expand="<%= true %>"
	>
		<span class="heading-text"><liferay-ui:message key="teams" /></span>
	</clay:content-col>

	<clay:content-col>
		<clay:button
			aria-label='<%= LanguageUtil.get(request, "select") %>'
			cssClass="modify-link"
			displayType="secondary"
			id='<%= liferayPortletResponse.getNamespace() + "selectTeamLink" %>'
			label='<%= LanguageUtil.get(request, "select") %>'
			small="<%= true %>"
			title='<%= LanguageUtil.get(request, "select") %>'
		/>
	</clay:content-col>
</clay:content-row>

<liferay-ui:search-container
	compactEmptyResultsMessage="<%= true %>"
	emptyResultsMessage="none"
	headerNames="title,null"
	id="teamsSearchContainer"
	total="<%= defaultTeams.size() %>"
>
	<liferay-ui:search-container-results
		results="<%= defaultTeams %>"
	/>

	<liferay-ui:search-container-row
		className="com.liferay.portal.kernel.model.Team"
		keyProperty="teamId"
		modelVar="team"
	>
		<liferay-ui:search-container-column-text
			name="title"
			truncate="<%= true %>"
			value="<%= HtmlUtil.escape(team.getName()) %>"
		/>

		<liferay-ui:search-container-column-text>
			<clay:button
				aria-label='<%= LanguageUtil.get(request, "remove") %>'
				borderless="<%= true %>"
				cssClass="lfr-portal-tooltip modify-link"
				data-rowId="<%= team.getTeamId() %>"
				displayType="secondary"
				icon="times-circle"
				monospaced="<%= true %>"
				title='<%= LanguageUtil.get(request, "remove") %>'
				type="button"
			/>
		</liferay-ui:search-container-column-text>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator
		markupView="lexicon"
		paginate="<%= false %>"
	/>
</liferay-ui:search-container>

<aui:script use="liferay-search-container">
	const bindModifyLink = function (config) {
		const searchContainer = config.searchContainer;

		searchContainer.get('contentBox').delegate(
			'click',
			(event) => {
				const link = event.currentTarget;

				searchContainer.deleteRow(
					link.ancestor('tr'),
					link.getAttribute('data-rowId')
				);
			},
			'.modify-link'
		);
	};

	const bindSelectLink = function (config) {
		const searchContainer = config.searchContainer;

		const selectLink = document.getElementById(config.linkId);

		selectLink.addEventListener('click', (event) => {
			let searchContainerData = searchContainer.getData();

			if (!searchContainerData.length) {
				searchContainerData = [];
			}
			else {
				searchContainerData = searchContainerData.split(',');
			}

			const ids = document.getElementById(config.inputId).value;

			const uri = new URL(config.uri);

			uri.searchParams.set(config.urlParam, ids);

			Liferay.Util.openSelectionModal({
				onSelect: function (event) {
					const entityId = event.entityid;

					const rowColumns = [
						Liferay.Util.escape(event.entityname),
						'<button aria-label="<%= LanguageUtil.get(request, "remove") %>" class="btn btn-monospaced btn-outline-borderless btn-outline-secondary float-right lfr-portal-tooltip modify-link" data-rowId="' +
							entityId +
							'" title="<%= LanguageUtil.get(request, "remove") %>" type="button"><%= UnicodeFormatter.toString(removeRoleIcon) %></button>',
					];

					searchContainer.addRow(rowColumns, entityId);

					searchContainer.updateDataStore();
				},
				selectEventName: config.id,
				selectedData: searchContainerData,
				title: config.title,
				url: uri.toString(),
			});
		});
	};

	<%
	PortletURL selectSiteRoleURL = PortletURLBuilder.create(
		PortletProviderUtil.getPortletURL(request, Role.class.getName(), PortletProvider.Action.BROWSE)
	).setParameter(
		"eventName", liferayPortletResponse.getNamespace() + "selectSiteRole"
	).setParameter(
		"groupId", liveGroupId
	).setParameter(
		"roleType", RoleConstants.TYPE_SITE
	).setParameter(
		"step", "2"
	).setWindowState(
		LiferayWindowState.POP_UP
	).buildPortletURL();

	String selectSiteRolePortletId = PortletProviderUtil.getPortletId(Role.class.getName(), PortletProvider.Action.BROWSE);
	%>

	const siteRolesConfig = {
		id: '<portlet:namespace />selectSiteRole',
		idAttr: 'roleid',
		inputId: '<portlet:namespace />siteRolesSearchContainerPrimaryKeys',
		linkId: '<portlet:namespace />selectSiteRoleLink',
		searchContainer: Liferay.SearchContainer.get(
			'<portlet:namespace />siteRolesSearchContainer'
		),
		title: '<liferay-ui:message arguments="site-role" key="select-x" />',
		titleAttr: 'roletitle',
		uri: '<%= selectSiteRoleURL.toString() %>',
		urlParam:
			'<%= PortalUtil.getPortletNamespace(selectSiteRolePortletId) %>roleIds',
	};

	bindModifyLink(siteRolesConfig);
	bindSelectLink(siteRolesConfig);

	<%
	PortletURL selectTeamURL = PortletURLBuilder.create(
		PortletProviderUtil.getPortletURL(request, Team.class.getName(), PortletProvider.Action.BROWSE)
	).setParameter(
		"eventName", liferayPortletResponse.getNamespace() + "selectTeam"
	).setParameter(
		"groupId", liveGroupId
	).setWindowState(
		LiferayWindowState.POP_UP
	).buildPortletURL();

	String selectTeamPortletId = PortletProviderUtil.getPortletId(Team.class.getName(), PortletProvider.Action.BROWSE);
	%>

	const teamsConfig = {
		id: '<portlet:namespace />selectTeam',
		idAttr: 'teamid',
		inputId: '<portlet:namespace />teamsSearchContainerPrimaryKeys',
		linkId: '<portlet:namespace />selectTeamLink',
		searchContainer: Liferay.SearchContainer.get(
			'<portlet:namespace />teamsSearchContainer'
		),
		title: '<liferay-ui:message arguments="team" key="select-x" />',
		titleAttr: 'teamname',
		uri: '<%= selectTeamURL.toString() %>',
		urlParam:
			'<%= PortalUtil.getPortletNamespace(selectTeamPortletId) %>teamIds',
	};

	bindModifyLink(teamsConfig);
	bindSelectLink(teamsConfig);
</aui:script>