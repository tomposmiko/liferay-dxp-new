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

<%@ include file="/repository_browser/init.jsp" %>

<%
RepositoryBrowserTagDisplayContext repositoryBrowserTagDisplayContext = (RepositoryBrowserTagDisplayContext)request.getAttribute(RepositoryBrowserTagDisplayContext.class.getName());
%>

<clay:management-toolbar
	additionalProps="<%= repositoryBrowserTagDisplayContext.getAdditionalProps() %>"
	managementToolbarDisplayContext="<%= repositoryBrowserTagDisplayContext.getManagementToolbarDisplayContext() %>"
	propsTransformer="repository_browser/js/RepositoryBrowserManagementToolbarPropsTransformer"
/>

<clay:container-fluid>
	<liferay-site-navigation:breadcrumb
		breadcrumbEntries="<%= repositoryBrowserTagDisplayContext.getBreadcrumbEntries() %>"
	/>

	<input id="<portlet:namespace />file" style="display: none;" type="file" />

	<liferay-ui:search-container
		id="repositoryEntries"
		searchContainer="<%= repositoryBrowserTagDisplayContext.getSearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.portal.kernel.repository.model.RepositoryEntry"
			keyProperty="primaryKey"
			modelVar="repositoryEntry"
		>
			<c:choose>
				<c:when test="<%= repositoryBrowserTagDisplayContext.isDescriptiveDisplayStyle() %>">
					<c:choose>
						<c:when test="<%= !repositoryBrowserTagDisplayContext.isRepositoryEntryThumbnailAvailable(repositoryEntry) %>">
							<liferay-ui:search-container-column-icon
								icon="<%= repositoryBrowserTagDisplayContext.getRepositoryEntryIcon(repositoryEntry) %>"
							/>
						</c:when>
						<c:otherwise>
							<liferay-ui:search-container-column-image
								src="<%= repositoryBrowserTagDisplayContext.getRepositoryEntryThumbnailSrc(repositoryEntry) %>"
							/>
						</c:otherwise>
					</c:choose>

					<liferay-ui:search-container-column-text
						colspan="<%= 2 %>"
					>
						<c:choose>
							<c:when test="<%= repositoryBrowserTagDisplayContext.isRepositoryEntryNavigable(repositoryEntry) %>">
								<p class="font-weight-bold h5">
									<aui:a href="<%= repositoryBrowserTagDisplayContext.getRepositoryEntryURL(repositoryEntry) %>">
										<%= HtmlUtil.escape(repositoryBrowserTagDisplayContext.getRepositoryEntryTitle(repositoryEntry)) %>
									</aui:a>
								</p>
							</c:when>
							<c:otherwise>
								<p class="font-weight-bold h5">
									<%= HtmlUtil.escape(repositoryBrowserTagDisplayContext.getRepositoryEntryTitle(repositoryEntry)) %>
								</p>
							</c:otherwise>
						</c:choose>

						<span class="text-default">
							<liferay-ui:message arguments="<%= new String[] {repositoryEntry.getUserName(), repositoryBrowserTagDisplayContext.getRepositoryEntryModifiedDateDescription(repositoryEntry)} %>" key="x-modified-x-ago" />
						</span>
					</liferay-ui:search-container-column-text>

					<liferay-ui:search-container-column-text>
						<clay:dropdown-actions
							dropdownItems="<%= repositoryBrowserTagDisplayContext.getActionDropdownItems(repositoryEntry) %>"
							propsTransformer="repository_browser/js/RepositoryBrowserDropdownPropsTransformer"
						/>
					</liferay-ui:search-container-column-text>
				</c:when>
				<c:when test="<%= repositoryBrowserTagDisplayContext.isIconDisplayStyle() %>">
					<liferay-ui:search-container-column-text>
						<c:choose>
							<c:when test="<%= repositoryBrowserTagDisplayContext.isVerticalCard(repositoryEntry) %>">
								<clay:vertical-card
									verticalCard="<%= repositoryBrowserTagDisplayContext.getVerticalCard(repositoryEntry) %>"
								/>
							</c:when>
							<c:otherwise>
								<clay:horizontal-card
									horizontalCard="<%= repositoryBrowserTagDisplayContext.getHorizontalCard(repositoryEntry) %>"
								/>
							</c:otherwise>
						</c:choose>
					</liferay-ui:search-container-column-text>
				</c:when>
				<c:otherwise>
					<liferay-ui:search-container-column-text
						cssClass="table-cell-expand table-cell-minw-200 table-title"
						name="title"
					>
						<%= HtmlUtil.escape(repositoryBrowserTagDisplayContext.getRepositoryEntryTitle(repositoryEntry)) %>
					</liferay-ui:search-container-column-text>

					<liferay-ui:search-container-column-text
						cssClass="table-cell-expand-smaller"
						name="size"
						value="<%= repositoryBrowserTagDisplayContext.getRepositoryEntrySizeValue(repositoryEntry) %>"
					/>

					<liferay-ui:search-container-column-text
						cssClass="table-cell-expand-smaller table-cell-minw-150"
						name="modified-date"
					>
						<liferay-ui:message arguments="<%= new String[] {repositoryEntry.getUserName(), repositoryBrowserTagDisplayContext.getRepositoryEntryModifiedDateDescription(repositoryEntry)} %>" key="x-modified-x-ago" />
					</liferay-ui:search-container-column-text>

					<liferay-ui:search-container-column-text>
						<clay:dropdown-actions
							dropdownItems="<%= repositoryBrowserTagDisplayContext.getActionDropdownItems(repositoryEntry) %>"
							propsTransformer="repository_browser/js/RepositoryBrowserDropdownPropsTransformer"
						/>
					</liferay-ui:search-container-column-text>
				</c:otherwise>
			</c:choose>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="<%= repositoryBrowserTagDisplayContext.getDisplayStyle() %>"
			markupView="lexicon"
			resultRowSplitter="<%= repositoryBrowserTagDisplayContext.getResultRowSplitter() %>"
		/>
	</liferay-ui:search-container>
</clay:container-fluid>

<div>
	<liferay-frontend:component
		componentId="repositoryBrowserEventHandler"
		module="repository_browser/js/RepositoryBrowserDropdownDefaultEventHandler"
	/>

	<liferay-frontend:component
		context="<%= repositoryBrowserTagDisplayContext.getRepositoryBrowserComponentContext() %>"
		module="repository_browser/js/RepositoryBrowserComponent"
	/>
</div>