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
long accountGroupId = ParamUtil.getLong(request, "accountGroupId");

boolean filterManageableAccountEntries = true;

if ((accountGroupId > 0) && AccountGroupPermission.contains(permissionChecker, accountGroupId, AccountActionKeys.ASSIGN_ACCOUNTS)) {
	filterManageableAccountEntries = false;
}

SearchContainer<AccountEntryDisplay> accountEntryDisplaySearchContainer = AccountEntryDisplaySearchContainerFactory.create(liferayPortletRequest, liferayPortletResponse, filterManageableAccountEntries);

if (accountGroupId > 0) {
	accountEntryDisplaySearchContainer.setRowChecker(new AccountGroupAccountEntryRowChecker(liferayPortletResponse, accountGroupId));
}

SelectAccountEntryManagementToolbarDisplayContext selectAccountEntryManagementToolbarDisplayContext = new SelectAccountEntryManagementToolbarDisplayContext(request, liferayPortletRequest, liferayPortletResponse, accountEntryDisplaySearchContainer);

if (selectAccountEntryManagementToolbarDisplayContext.isSingleSelect()) {
	accountEntryDisplaySearchContainer.setRowChecker(null);
}
%>

<clay:management-toolbar
	managementToolbarDisplayContext="<%= selectAccountEntryManagementToolbarDisplayContext %>"
/>

<clay:container-fluid
	id='<%= liferayPortletResponse.getNamespace() + "selectAccountEntry" %>'
>
	<liferay-ui:search-container
		searchContainer="<%= accountEntryDisplaySearchContainer %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.account.admin.web.internal.display.AccountEntryDisplay"
			keyProperty="accountEntryId"
			modelVar="accountEntryDisplay"
		>

			<%
			String cssClass = "table-cell-expand";

			Optional<User> userOptional = accountEntryDisplay.getPersonAccountEntryUserOptional();

			boolean disabled = userOptional.isPresent();

			if (disabled) {
				cssClass += " text-muted";
			}
			%>

			<liferay-ui:search-container-column-text
				cssClass='<%= cssClass + " table-title" %>'
				name="name"
				value="<%= HtmlUtil.escape(accountEntryDisplay.getName()) %>"
			/>

			<liferay-ui:search-container-column-text
				cssClass="<%= cssClass %>"
				name="type"
				translate="<%= true %>"
				value="<%= HtmlUtil.escape(accountEntryDisplay.getType()) %>"
			/>

			<c:if test="<%= selectAccountEntryManagementToolbarDisplayContext.isSingleSelect() %>">
				<liferay-ui:search-container-column-text>
					<aui:button
						cssClass="choose-account selector-button"
						data='<%=
							HashMapBuilder.<String, Object>put(
								"accountentryid", accountEntryDisplay.getAccountEntryId()
							).put(
								"entityid", accountEntryDisplay.getAccountEntryId()
							).put(
								"entityname", accountEntryDisplay.getName()
							).build()
						%>'
						disabled="<%= disabled %>"
						value="choose"
					/>
				</liferay-ui:search-container-column-text>
			</c:if>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</clay:container-fluid>