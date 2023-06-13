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
String backURL = layoutsAdminDisplayContext.getRedirect();

if (Validator.isNull(backURL)) {
	PortletURL portletURL = layoutsAdminDisplayContext.getPortletURL();

	backURL = portletURL.toString();
}

SelectLayoutPageTemplateEntryDisplayContext selectLayoutPageTemplateEntryDisplayContext = new SelectLayoutPageTemplateEntryDisplayContext(request);

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(backURL);

renderResponse.setTitle(LanguageUtil.get(request, "select-template"));
%>

<div class="container-fluid container-fluid-max-xl container-view" id="<portlet:namespace />layoutPageTemplateEntries">
	<div class="row">
		<div class="col-lg-3">
			<nav class="menubar menubar-transparent menubar-vertical-expand-lg">
				<ul class="nav nav-nested">
					<li class="nav-item">
						<p class="text-uppercase">
							<strong><liferay-ui:message key="collections" /></strong>
						</p>

						<ul class="nav nav-stacked">

							<%
							List<LayoutPageTemplateCollection> layoutPageTemplateCollections = LayoutPageTemplateCollectionServiceUtil.getLayoutPageTemplateCollections(scopeGroupId);

							for (LayoutPageTemplateCollection layoutPageTemplateCollection : layoutPageTemplateCollections) {
								int layoutPageTemplateEntriesCount = LayoutPageTemplateEntryServiceUtil.getLayoutPageTemplateEntriesCount(themeDisplay.getScopeGroupId(), layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(), WorkflowConstants.STATUS_APPROVED);
							%>

								<c:if test="<%= layoutPageTemplateEntriesCount > 0 %>">
									<li class="nav-item">
										<a class="nav-link truncate-text <%= (selectLayoutPageTemplateEntryDisplayContext.getLayoutPageTemplateCollectionId() == layoutPageTemplateCollection.getLayoutPageTemplateCollectionId()) ? "active" : StringPool.BLANK %>" href="<%= layoutsAdminDisplayContext.getSelectLayoutPageTemplateEntryURL(layoutPageTemplateCollection.getLayoutPageTemplateCollectionId(), layoutsAdminDisplayContext.isPrivateLayout()) %>">
											<%= layoutPageTemplateCollection.getName() %>
										</a>
									</li>
								</c:if>

							<%
							}
							%>

							<li class="nav-item">

								<%
								String basicPagesURL = layoutsAdminDisplayContext.getSelectLayoutPageTemplateEntryURL(0, layoutsAdminDisplayContext.getSelPlid(), "basic-pages", layoutsAdminDisplayContext.isPrivateLayout());
								%>

								<a class="nav-link truncate-text <%= selectLayoutPageTemplateEntryDisplayContext.isBasicPages() ? "active" : StringPool.BLANK %>" href="<%= basicPagesURL %>">
									<liferay-ui:message key="basic-pages" />
								</a>
							</li>
							<li class="nav-item">

								<%
								String globalTemplatesURL = layoutsAdminDisplayContext.getSelectLayoutPageTemplateEntryURL(0, layoutsAdminDisplayContext.getSelPlid(), "global-templates", layoutsAdminDisplayContext.isPrivateLayout());
								%>

								<a class="nav-link truncate-text <%= selectLayoutPageTemplateEntryDisplayContext.isGlobalTemplates() ? "active" : StringPool.BLANK %>" href="<%= globalTemplatesURL %>">
									<liferay-ui:message key="global-templates" />
								</a>
							</li>
						</ul>
					</li>
				</ul>
			</nav>
		</div>

		<div class="col-lg-9">
			<div class="sheet">
				<h3 class="sheet-title">
					<c:choose>
						<c:when test="<%= selectLayoutPageTemplateEntryDisplayContext.isContentPages() %>">

							<%
							LayoutPageTemplateCollection layoutPageTemplateCollection = LayoutPageTemplateCollectionLocalServiceUtil.fetchLayoutPageTemplateCollection(selectLayoutPageTemplateEntryDisplayContext.getLayoutPageTemplateCollectionId());
							%>

							<c:if test="<%= layoutPageTemplateCollection != null %>">
								<%= layoutPageTemplateCollection.getName() %>
							</c:if>
						</c:when>
						<c:when test="<%= selectLayoutPageTemplateEntryDisplayContext.isBasicPages() %>">
							<liferay-ui:message key="basic-pages" />
						</c:when>
						<c:when test="<%= selectLayoutPageTemplateEntryDisplayContext.isGlobalTemplates() %>">
							<liferay-ui:message key="global-templates" />
						</c:when>
					</c:choose>
				</h3>

				<c:choose>
					<c:when test="<%= selectLayoutPageTemplateEntryDisplayContext.isContentPages() %>">
						<liferay-ui:search-container
							total="<%= selectLayoutPageTemplateEntryDisplayContext.getLayoutPageTemplateEntriesCount() %>"
						>
							<liferay-ui:search-container-results
								results="<%= selectLayoutPageTemplateEntryDisplayContext.getLayoutPageTemplateEntries(searchContainer.getStart(), searchContainer.getEnd()) %>"
							/>

							<liferay-ui:search-container-row
								className="com.liferay.layout.page.template.model.LayoutPageTemplateEntry"
								keyProperty="layoutPageTemplateEntryId"
								modelVar="layoutPageTemplateEntry"
							>

								<%
								row.setCssClass("entry-card lfr-asset-item " + row.getCssClass());
								%>

								<portlet:renderURL var="addLayoutURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
									<portlet:param name="mvcRenderCommandName" value="/layout/add_layout" />
									<portlet:param name="layoutPageTemplateEntryId" value="<%= String.valueOf(layoutPageTemplateEntry.getLayoutPageTemplateEntryId()) %>" />
								</portlet:renderURL>

								<liferay-ui:search-container-column-text>

									<%
									Map<String, Object> addLayoutData = new HashMap<>();

									addLayoutData.put("add-layout-url", addLayoutURL);

									String imagePreviewURL = layoutPageTemplateEntry.getImagePreviewURL(themeDisplay);
									%>

									<c:choose>
										<c:when test="<%= Validator.isNotNull(imagePreviewURL) %>">
											<liferay-frontend:vertical-card
												cssClass="add-layout-action-option"
												data="<%= addLayoutData %>"
												imageCSSClass="aspect-ratio-bg-contain"
												imageUrl="<%= imagePreviewURL %>"
												title="<%= layoutPageTemplateEntry.getName() %>"
												url="javascript:;"
											>
												<liferay-frontend:vertical-card-header>
													<liferay-ui:message arguments="<%= LanguageUtil.getTimeDescription(request, System.currentTimeMillis() - layoutPageTemplateEntry.getCreateDate().getTime(), true) %>" key="x-ago" translateArguments="<%= false %>" />
												</liferay-frontend:vertical-card-header>
											</liferay-frontend:vertical-card>
										</c:when>
										<c:otherwise>
											<liferay-frontend:icon-vertical-card
												cssClass="add-layout-action-option"
												data="<%= addLayoutData %>"
												icon='<%= Objects.equals(layoutPageTemplateEntry.getType(), LayoutPageTemplateEntryTypeConstants.TYPE_WIDGET_PAGE) ? "page-template" : "page" %>'
												title="<%= layoutPageTemplateEntry.getName() %>"
												url="javascript:;"
											>
												<liferay-frontend:vertical-card-header>
													<liferay-ui:message arguments="<%= LanguageUtil.getTimeDescription(request, System.currentTimeMillis() - layoutPageTemplateEntry.getCreateDate().getTime(), true) %>" key="x-ago" translateArguments="<%= false %>" />
												</liferay-frontend:vertical-card-header>
											</liferay-frontend:icon-vertical-card>
										</c:otherwise>
									</c:choose>
								</liferay-ui:search-container-column-text>
							</liferay-ui:search-container-row>

							<liferay-ui:search-iterator
								displayStyle="icon"
								markupView="lexicon"
								searchResultCssClass="display-style-icon list-unstyled row"
							/>
						</liferay-ui:search-container>

						<aui:script use="aui-base">
							var addLayoutActionOptionQueryClickHandler = A.one('#<portlet:namespace />layoutPageTemplateEntries').delegate(
								'click',
								function(event) {
									var actionElement = event.currentTarget;

									Liferay.Util.openWindow(
										{
											dialog: {
												destroyOnHide: true,
												height: 480,
												resizable: false,
												width: 640
											},
											dialogIframe: {
												bodyCssClass: 'dialog-with-footer'
											},
											id: '<portlet:namespace />addLayoutDialog',
											title: '<liferay-ui:message key="add-page" />',
											uri: actionElement.getData('add-layout-url')
										}
									);
								},
								'.add-layout-action-option'
							);

							function handleDestroyPortlet () {
								addLayoutActionOptionQueryClickHandler.detach();

								Liferay.detach('destroyPortlet', handleDestroyPortlet);
							}

							Liferay.on('destroyPortlet', handleDestroyPortlet);
						</aui:script>
					</c:when>
					<c:when test="<%= selectLayoutPageTemplateEntryDisplayContext.isBasicPages() %>">
						<liferay-util:include page="/select_basic_pages.jsp" servletContext="<%= application %>" />
					</c:when>
					<c:otherwise>
						<liferay-util:include page="/select_global_templates.jsp" servletContext="<%= application %>" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</div>