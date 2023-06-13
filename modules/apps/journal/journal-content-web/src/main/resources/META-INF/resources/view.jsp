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

<liferay-util:dynamic-include key="com.liferay.journal.content.web#/view.jsp#pre" />

<%
JournalArticle article = journalContentDisplayContext.getArticle();
JournalArticleDisplay articleDisplay = journalContentDisplayContext.getArticleDisplay();

journalContentDisplayContext.incrementViewCounter();

AssetRendererFactory<JournalArticle> assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClass(JournalArticle.class);

if (journalContentDisplayContext.isShowArticle()) {
	renderResponse.setTitle(articleDisplay.getTitle());
}
%>

<c:choose>
	<c:when test="<%= article == null %>">
		<c:choose>
			<c:when test="<%= Validator.isNull(journalContentDisplayContext.getArticleId()) %>">
				<div class="alert alert-info text-center">
					<div>
						<liferay-ui:message key="this-application-is-not-visible-to-users-yet" />
					</div>

					<c:if test="<%= journalContentDisplayContext.isShowSelectArticleLink() %>">
						<div>
							<aui:a href="javascript:void(0);" onClick="<%= portletDisplay.getURLConfigurationJS() %>"><liferay-ui:message key="select-web-content-to-make-it-visible" /></aui:a>
						</div>
					</c:if>
				</div>
			</c:when>
			<c:otherwise>

				<%
				JournalArticle selectedArticle = journalContentDisplayContext.getSelectedArticle();
				%>

				<div class="alert alert-warning text-center">
					<c:choose>
						<c:when test="<%= (selectedArticle != null) && selectedArticle.isInTrash() %>">
							<liferay-ui:message arguments="<%= HtmlUtil.escape(selectedArticle.getTitle(locale)) %>" key="the-web-content-article-x-was-moved-to-the-recycle-bin" />
						</c:when>
						<c:when test="<%= (selectedArticle != null) && (selectedArticle.getDDMStructure() == null) %>">
							<liferay-ui:message arguments="<%= HtmlUtil.escape(selectedArticle.getTitle(locale)) %>" key="is-temporarily-unavailable" />
						</c:when>
						<c:otherwise>
							<liferay-ui:message key="the-selected-web-content-no-longer-exists" />
						</c:otherwise>
					</c:choose>

					<c:if test="<%= journalContentDisplayContext.isShowSelectArticleLink() %>">
						<liferay-util:buffer
							var="selectJournalArticleLink"
						>
							<aui:a href="javascript:void(0);" label="select-another" onClick="<%= portletDisplay.getURLConfigurationJS() %>" />
						</liferay-util:buffer>

						<div>
							<c:choose>
								<c:when test="<%= journalContentDisplayContext.hasRestorePermission() %>">

									<%
									AssetRenderer<JournalArticle> assetRenderer = assetRendererFactory.getAssetRenderer(selectedArticle, 0);
									%>

									<portlet:actionURL name="restoreJournalArticle" var="restoreJournalArticleURL">
										<portlet:param name="classPK" value="<%= String.valueOf(assetRenderer.getClassPK()) %>" />
										<portlet:param name="redirect" value="<%= currentURL %>" />
									</portlet:actionURL>

									<liferay-util:buffer
										var="restoreJournalArticleLink"
									>
										<aui:a href="<%= restoreJournalArticleURL %>" label="undo" />
									</liferay-util:buffer>

									<liferay-ui:message arguments="<%= new String[] {restoreJournalArticleLink, selectJournalArticleLink} %>" key="do-you-want-to-x-or-x-web-content" />
								</c:when>
								<c:otherwise>
									<liferay-ui:message arguments="<%= selectJournalArticleLink %>" key="do-you-want-to-x-web-content" />
								</c:otherwise>
							</c:choose>
						</div>
					</c:if>
				</div>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="<%= !journalContentDisplayContext.hasViewPermission() %>">
				<div class="alert alert-danger">
					<liferay-ui:message key="you-do-not-have-the-roles-required-to-access-this-web-content-entry" />
				</div>
			</c:when>
			<c:when test="<%= Validator.isNotNull(journalContentDisplayContext.getArticleId()) %>">
				<c:choose>
					<c:when test="<%= journalContentDisplayContext.isExpired() %>">
						<div class="alert alert-warning">
							<liferay-ui:message arguments="<%= HtmlUtil.escape(article.getTitle(locale)) %>" key="x-is-expired" />
						</div>
					</c:when>
					<c:when test="<%= article.getDDMStructure() == null %>">
						<div class="alert alert-warning">
							<liferay-ui:message arguments="<%= HtmlUtil.escape(article.getTitle(locale)) %>" key="is-temporarily-unavailable" />
						</div>
					</c:when>
					<c:when test="<%= !journalContentDisplayContext.isPreview() && !article.isApproved() %>">

						<%
						AssetRenderer<JournalArticle> assetRenderer = assetRendererFactory.getAssetRenderer(article.getResourcePrimKey());
						%>

						<liferay-util:buffer
							var="scheduledOrNotApprovedMessage"
						>
							<c:choose>
								<c:when test="<%= article.isScheduled() %>">
									<liferay-ui:message arguments="<%= new Object[] {HtmlUtil.escape(article.getTitle(locale)), dateFormatDateTime.format(article.getDisplayDate())} %>" key="x-is-scheduled-and-will-be-displayed-on-x" />
								</c:when>
								<c:otherwise>
									<liferay-ui:message arguments="<%= HtmlUtil.escape(article.getTitle(locale)) %>" key="x-is-not-approved" />
								</c:otherwise>
							</c:choose>
						</liferay-util:buffer>

						<div class="alert alert-warning">
							<c:choose>
								<c:when test="<%= assetRenderer.hasEditPermission(permissionChecker) %>">
									<a href="<%= assetRenderer.getURLEdit(liferayPortletRequest, liferayPortletResponse, WindowState.NORMAL, currentURLObj) %>">
										<%= scheduledOrNotApprovedMessage %>
									</a>
								</c:when>
								<c:otherwise>
									<%= scheduledOrNotApprovedMessage %>
								</c:otherwise>
							</c:choose>
						</div>
					</c:when>
					<c:when test="<%= articleDisplay != null %>">

						<%
						AssetRenderer<JournalArticle> assetRenderer = assetRendererFactory.getAssetRenderer(article.getResourcePrimKey());

						Map<String, Object> data = HashMapBuilder.<String, Object>put(
							"fragments-editor-item-id", PortalUtil.getClassNameId(JournalArticle.class) + "-" + assetRenderer.getClassPK()
						).put(
							"fragments-editor-item-type", "fragments-editor-mapped-item"
						).build();
						%>

						<div class="<%= journalContentDisplayContext.isPreview() ? "p-1 preview-asset-entry" : StringPool.BLANK %>" <%= AUIUtil.buildData(data) %>>
							<liferay-journal:journal-article-display
								articleDisplay="<%= articleDisplay %>"
								paginationURL="<%= renderResponse.createRenderURL() %>"
							/>
						</div>
					</c:when>
				</c:choose>
			</c:when>
		</c:choose>
	</c:otherwise>
</c:choose>

<c:if test="<%= (articleDisplay != null) && journalContentDisplayContext.hasViewPermission() %>">

	<%
	String viewMode = ParamUtil.getString(request, "viewMode");
	%>

	<c:if test='<%= journalContentDisplayContext.isEnabledContentMetadataAssetAddonEntry("enableRelatedAssets") %>'>
		<div class="asset-links content-metadata-asset-addon-entries">
			<div class="content-metadata-asset-addon-entry content-metadata-asset-addon-entry-links">
				<liferay-asset:asset-links
					className="<%= JournalArticle.class.getName() %>"
					classPK="<%= articleDisplay.getResourcePrimKey() %>"
				/>
			</div>
		</div>
	</c:if>

	<%
	boolean enableDOC = journalContentDisplayContext.isEnabledUserToolAssetAddonEntry("enableDOC") && journalContentDisplayContext.isEnabledConversion("doc");
	boolean enableODT = journalContentDisplayContext.isEnabledUserToolAssetAddonEntry("enableODT") && journalContentDisplayContext.isEnabledConversion("odt");
	boolean enablePDF = journalContentDisplayContext.isEnabledUserToolAssetAddonEntry("enablePDF") && journalContentDisplayContext.isEnabledConversion("pdf");
	boolean enablePrint = journalContentDisplayContext.isEnabledUserToolAssetAddonEntry("enablePrint");
	boolean enableRatings = journalContentDisplayContext.isEnabledContentMetadataAssetAddonEntry("enableRatings") && !viewMode.equals(Constants.PRINT);
	boolean enableTXT = journalContentDisplayContext.isEnabledUserToolAssetAddonEntry("enableTXT") && journalContentDisplayContext.isEnabledConversion("txt");
	boolean showAvailableLocales = journalContentDisplayContext.isEnabledUserToolAssetAddonEntry("showAvailableLocales");
	%>

	<c:if test="<%= enableDOC || enableODT || enablePDF || enablePrint || enableRatings || enableTXT || showAvailableLocales %>">
		<hr class="separator" />

		<clay:content-row
			cssClass="user-tool-asset-addon-entries"
			floatElements=""
			verticalAlign="center"
		>
			<c:if test="<%= enableRatings %>">
				<clay:content-col>
					<div class="content-metadata-asset-addon-entry content-metadata-ratings">
						<liferay-ratings:ratings
							className="<%= JournalArticle.class.getName() %>"
							classPK="<%= articleDisplay.getResourcePrimKey() %>"
						/>
					</div>
				</clay:content-col>
			</c:if>

			<c:if test="<%= showAvailableLocales %>">
				<liferay-util:include page="/locales.jsp" servletContext="<%= application %>" />
			</c:if>

			<c:if test="<%= enablePrint %>">
				<liferay-util:include page="/print.jsp" servletContext="<%= application %>" />
			</c:if>

			<c:if test="<%= enablePDF %>">
				<liferay-util:include page="/conversions.jsp" servletContext="<%= application %>">
					<liferay-util:param name="extension" value="pdf" />
				</liferay-util:include>
			</c:if>

			<c:if test="<%= enableDOC %>">
				<liferay-util:include page="/conversions.jsp" servletContext="<%= application %>">
					<liferay-util:param name="extension" value="doc" />
				</liferay-util:include>
			</c:if>

			<c:if test="<%= enableODT %>">
				<liferay-util:include page="/conversions.jsp" servletContext="<%= application %>">
					<liferay-util:param name="extension" value="odt" />
				</liferay-util:include>
			</c:if>

			<c:if test="<%= enableTXT %>">
				<liferay-util:include page="/conversions.jsp" servletContext="<%= application %>">
					<liferay-util:param name="extension" value="txt" />
				</liferay-util:include>
			</c:if>
		</clay:content-row>
	</c:if>

	<c:if test='<%= journalContentDisplayContext.articleCommentsEnabled() && journalContentDisplayContext.isEnabledContentMetadataAssetAddonEntry("enableComments") %>'>
		<hr class="separator" />

		<div class="asset-links content-metadata-asset-addon-entries">
			<div class="content-metadata-asset-addon-entry content-metadata-comments">
				<liferay-comment:discussion
					className="<%= JournalArticle.class.getName() %>"
					classPK="<%= articleDisplay.getResourcePrimKey() %>"
					hideControls="<%= viewMode.equals(Constants.PRINT) %>"
					ratingsEnabled='<%= journalContentDisplayContext.isEnabledContentMetadataAssetAddonEntry("enableCommentRatings") && !viewMode.equals(Constants.PRINT) %>'
					redirect="<%= currentURLObj.toString() %>"
					userId="<%= articleDisplay.getUserId() %>"
				/>
			</div>
		</div>
	</c:if>
</c:if>

<liferay-util:dynamic-include key="com.liferay.journal.content.web#/view.jsp#post" />