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

<%@ include file="/admin/init.jsp" %>

<%
long kbFolderClassNameId = PortalUtil.getClassNameId(KBFolderConstants.getClassName());

long parentResourceClassNameId = ParamUtil.getLong(request, "parentResourceClassNameId", kbFolderClassNameId);

long parentResourcePrimKey = ParamUtil.getLong(request, "parentResourcePrimKey", KBFolderConstants.DEFAULT_PARENT_FOLDER_ID);

boolean kbFolderView = parentResourceClassNameId == kbFolderClassNameId;

KBAdminManagementToolbarDisplayContext kbAdminManagementToolbarDisplayContext = new KBAdminManagementToolbarDisplayContext(request, liferayPortletRequest, liferayPortletResponse, renderRequest, renderResponse, portletConfig);
KBArticleURLHelper kbArticleURLHelper = new KBArticleURLHelper(renderRequest, renderResponse);
%>

<c:choose>
	<c:when test='<%= FeatureFlagManagerUtil.isEnabled("LPS-156421") %>'>
		<liferay-util:include page="/admin/common/vertical_menu.jsp" servletContext="<%= application %>" />

		<div class="knowledge-base-admin-content">
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/admin/common/top_tabs.jsp" servletContext="<%= application %>" />
	</c:otherwise>
</c:choose>

<clay:management-toolbar
	actionDropdownItems="<%= kbAdminManagementToolbarDisplayContext.getActionDropdownItems() %>"
	clearResultsURL="<%= String.valueOf(kbAdminManagementToolbarDisplayContext.getSearchURL()) %>"
	creationMenu="<%= kbAdminManagementToolbarDisplayContext.getCreationMenu() %>"
	disabled="<%= kbAdminManagementToolbarDisplayContext.isDisabled() %>"
	filterDropdownItems="<%= kbAdminManagementToolbarDisplayContext.getFilterDropdownItems() %>"
	infoPanelId="infoPanelId"
	itemsTotal="<%= kbAdminManagementToolbarDisplayContext.getTotal() %>"
	propsTransformer="admin/js/ManagementToolbarPropsTransformer"
	searchActionURL="<%= String.valueOf(kbAdminManagementToolbarDisplayContext.getSearchURL()) %>"
	searchContainerId="kbObjects"
	selectable="<%= true %>"
	showInfoButton="<%= kbAdminManagementToolbarDisplayContext.isShowInfoButton() %>"
	sortingOrder="<%= kbAdminManagementToolbarDisplayContext.getOrderByType() %>"
	sortingURL="<%= String.valueOf(kbAdminManagementToolbarDisplayContext.getSortingURL()) %>"
/>

<div class="closed sidenav-container sidenav-right" id="<portlet:namespace />infoPanelId">
	<liferay-portlet:resourceURL copyCurrentRenderParameters="<%= false %>" id="infoPanel" var="sidebarPanelURL">
		<portlet:param name="parentResourceClassNameId" value="<%= String.valueOf(parentResourceClassNameId) %>" />
		<portlet:param name="parentResourcePrimKey" value="<%= String.valueOf(parentResourcePrimKey) %>" />
		<portlet:param name="showSidebarHeader" value="<%= Boolean.TRUE.toString() %>" />
	</liferay-portlet:resourceURL>

	<liferay-frontend:sidebar-panel
		resourceURL="<%= sidebarPanelURL %>"
		searchContainerId="kbObjects"
	>

		<%
		request.setAttribute(KBWebKeys.SHOW_SIDEBAR_HEADER, Boolean.TRUE);
		%>

		<liferay-util:include page="/admin/info_panel.jsp" servletContext="<%= application %>" />
	</liferay-frontend:sidebar-panel>

	<clay:container-fluid
		cssClass="container-view sidenav-content"
	>

		<%
		KBAdminViewDisplayContext kbAdminViewDisplayContext = new KBAdminViewDisplayContext(parentResourceClassNameId, parentResourcePrimKey, request, liferayPortletResponse);

		kbAdminViewDisplayContext.populatePortletBreadcrumbEntries(currentURLObj);
		%>

		<liferay-portlet:actionURL name="/knowledge_base/delete_kb_articles_and_folders" varImpl="deleteKBArticlesAndFoldersURL" />

		<aui:form action="<%= deleteKBArticlesAndFoldersURL %>" name="fm">
			<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

			<liferay-ui:error exception="<%= KBArticlePriorityException.class %>" message='<%= LanguageUtil.format(request, "please-enter-a-priority-that-is-greater-than-x", "0", false) %>' translateMessage="<%= false %>" />

			<c:if test='<%= SessionMessages.contains(renderRequest, "importedKBArticlesCount") %>'>

				<%
				int importedKBArticlesCount = GetterUtil.getInteger(SessionMessages.get(renderRequest, "importedKBArticlesCount"));
				%>

				<c:choose>
					<c:when test="<%= importedKBArticlesCount > 0 %>">
						<div class="alert alert-success">
							<liferay-ui:message key="your-request-completed-successfully" />

							<liferay-ui:message arguments="<%= importedKBArticlesCount %>" key="a-total-of-x-articles-have-been-imported" />
						</div>
					</c:when>
					<c:otherwise>
						<clay:alert
							displayType="warning"
							message='<%= LanguageUtil.format(request, "the-content-was-imported-but-no-articles-were-found-with-one-of-the-supported-extensions-x", HtmlUtil.escape(StringUtil.merge(kbGroupServiceConfiguration.markdownImporterArticleExtensions(), StringPool.COMMA_AND_SPACE))) %>'
							title="Alert"
						/>
					</c:otherwise>
				</c:choose>
			</c:if>

			<liferay-site-navigation:breadcrumb
				breadcrumbEntries="<%= BreadcrumbEntriesUtil.getBreadcrumbEntries(request, false, false, false, false, true) %>"
			/>

			<%
			SearchContainer<Object> kbAdminManagementToolbarDisplayContextSearchContainer = kbAdminManagementToolbarDisplayContext.getSearchContainer();
			%>

			<c:choose>
				<c:when test='<%= !FeatureFlagManagerUtil.isEnabled("LPS-156421") || kbAdminManagementToolbarDisplayContextSearchContainer.hasResults() || kbAdminManagementToolbarDisplayContext.isSearch() %>'>

					<%
					KBArticleViewDisplayContext kbArticleViewDisplayContext = new KBArticleViewDisplayContext(request, liferayPortletRequest, liferayPortletResponse, renderResponse);
					%>

					<liferay-ui:search-container
						id="kbObjects"
						searchContainer="<%= kbAdminManagementToolbarDisplayContextSearchContainer %>"
					>
						<liferay-ui:search-container-row
							className="Object"
							modelVar="kbObject"
						>
							<c:choose>
								<c:when test="<%= kbObject instanceof KBFolder %>">

									<%
									KBFolder kbFolder = (KBFolder)kbObject;

									row.setData(
										HashMapBuilder.<String, Object>put(
											"actions", StringUtil.merge(kbAdminManagementToolbarDisplayContext.getAvailableActions(kbFolder))
										).build());

									row.setPrimaryKey(String.valueOf(kbFolder.getKbFolderId()));
									%>

									<liferay-ui:search-container-column-icon
										icon="folder"
										toggleRowChecker="<%= true %>"
									/>

									<liferay-ui:search-container-column-text
										colspan="<%= 2 %>"
									>
										<liferay-portlet:renderURL varImpl="rowURL">
											<portlet:param name="mvcPath" value="/admin/view_kb_folders.jsp" />
											<portlet:param name="redirect" value="<%= currentURL %>" />
											<portlet:param name="parentResourceClassNameId" value="<%= String.valueOf(kbFolder.getClassNameId()) %>" />
											<portlet:param name="parentResourcePrimKey" value="<%= String.valueOf(kbFolder.getKbFolderId()) %>" />
											<portlet:param name="selectedItemId" value="<%= String.valueOf(kbFolder.getKbFolderId()) %>" />
										</liferay-portlet:renderURL>

										<h2 class="h5">
											<aui:a href="<%= rowURL.toString() %>">
												<%= HtmlUtil.escape(kbFolder.getName()) %>
											</aui:a>
										</h2>

										<span class="text-default">

											<%
											Date modifiedDate = kbFolder.getModifiedDate();

											String modifiedDateDescription = LanguageUtil.getTimeDescription(request, System.currentTimeMillis() - modifiedDate.getTime(), true);
											%>

											<liferay-ui:message arguments="<%= new String[] {HtmlUtil.escape(kbFolder.getUserName()), modifiedDateDescription} %>" key="x-modified-x-ago" />
										</span>
									</liferay-ui:search-container-column-text>

									<liferay-ui:search-container-column-text>

										<%
										KBDropdownItemsProvider kbDropdownItemsProvider = new KBDropdownItemsProvider(liferayPortletRequest, liferayPortletResponse);
										%>

										<clay:dropdown-actions
											aria-label='<%= LanguageUtil.get(request, "show-actions") %>'
											dropdownItems="<%= kbDropdownItemsProvider.getKBFolderDropdownItems(kbFolder) %>"
											propsTransformer="admin/js/KBDropdownPropsTransformer"
										/>
									</liferay-ui:search-container-column-text>
								</c:when>
								<c:otherwise>

									<%
									KBArticle kbArticle = (KBArticle)kbObject;

									row.setData(
										HashMapBuilder.<String, Object>put(
											"actions", StringUtil.merge(kbAdminManagementToolbarDisplayContext.getAvailableActions(kbArticle))
										).build());

									row.setPrimaryKey(String.valueOf(kbArticle.getResourcePrimKey()));
									%>

									<liferay-ui:search-container-column-user
										showDetails="<%= false %>"
										userId="<%= kbArticle.getUserId() %>"
									/>

									<liferay-ui:search-container-column-text
										colspan="<%= 2 %>"
									>
										<h2 class="h5">

											<%
											PortletURL viewURL = kbArticleURLHelper.createViewWithRedirectURL(kbArticle, currentURL);
											%>

											<aui:a href="<%= viewURL.toString() %>">
												<%= HtmlUtil.escape(kbArticle.getTitle()) %>
											</aui:a>
										</h2>

										<span class="text-default">
											<liferay-ui:message arguments="<%= new String[] {HtmlUtil.escape(kbArticle.getUserName()), kbArticleViewDisplayContext.getModifiedDateDescription(kbArticle)} %>" key="x-modified-x-ago" />
										</span>

										<c:if test="<%= kbArticleViewDisplayContext.getChildKBArticlesCount(scopeGroupId, kbArticle) > 0 %>">
											<span class="text-default">
												<aui:a href="<%= kbArticleViewDisplayContext.getChildKBArticlesURLString(currentURL, kbArticle) %>">
													<liferay-ui:message arguments="<%= kbArticleViewDisplayContext.getChildKBArticlesCount(scopeGroupId, kbArticle) %>" key="x-child-articles" />
												</aui:a>
											</span>
										</c:if>

									<span class="text-default">
										<aui:workflow-status helpMessage='<%= kbArticle.isExpired() ? dateFormatDateTime.format(kbArticle.getExpirationDate()) : "" %>' markupView="lexicon" showHelpMessage="<%= kbArticle.isExpired() %>" showIcon="<%= false %>" showLabel="<%= false %>" status="<%= kbArticle.getStatus() %>" />

										<c:if test="<%= kbArticleViewDisplayContext.isExpiringSoon(kbArticle) %>">
											<span class="label label-warning">
												<span class="label-item label-item-expand"><liferay-ui:message key="expiring-soon" /></span>
											</span>

											<liferay-ui:icon-help message='<%= kbArticle.getExpirationDate()!= null ? dateFormatDateTime.format(kbArticle.getExpirationDate()) : "" %>' />
										</c:if>
									</span>
									</liferay-ui:search-container-column-text>

									<liferay-ui:search-container-column-text>
										<clay:dropdown-actions
											aria-label='<%= LanguageUtil.get(request, "show-actions") %>'
											dropdownItems="<%= kbArticleViewDisplayContext.getKBArticleDropdownItems(kbArticle) %>"
											propsTransformer="admin/js/KBDropdownPropsTransformer"
										/>
									</liferay-ui:search-container-column-text>
								</c:otherwise>
							</c:choose>
						</liferay-ui:search-container-row>

						<liferay-ui:search-iterator
							displayStyle="descriptive"
							markupView="lexicon"
							resultRowSplitter="<%= kbFolderView ? new KBResultRowSplitter() : null %>"
						/>
					</liferay-ui:search-container>
				</c:when>
				<c:otherwise>
					<liferay-frontend:empty-result-message
						actionDropdownItems="<%= kbAdminManagementToolbarDisplayContext.getEmptyStateActionDropdownItems() %>"
						animationType="<%= EmptyResultMessageKeys.AnimationType.EMPTY %>"
						buttonCssClass="secondary"
						title='<%= (parentResourcePrimKey == KBFolderConstants.DEFAULT_PARENT_FOLDER_ID) ? LanguageUtil.get(request, "knowledge-base-is-empty") : LanguageUtil.get(request, "this-folder-is-empty") %>'
					/>
				</c:otherwise>
			</c:choose>
		</aui:form>
	</clay:container-fluid>
</div>

<c:if test='<%= FeatureFlagManagerUtil.isEnabled("LPS-156421") %>'>
	</div>
</c:if>