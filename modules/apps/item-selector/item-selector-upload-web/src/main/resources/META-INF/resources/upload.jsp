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
ItemSelectorUploadViewDisplayContext itemSelectorUploadViewDisplayContext = (ItemSelectorUploadViewDisplayContext)request.getAttribute(ItemSelectorUploadView.ITEM_SELECTOR_UPLOAD_VIEW_DISPLAY_CONTEXT);

ItemSelectorReturnTypeResolver<?, ?> itemSelectorReturnTypeResolver = itemSelectorUploadViewDisplayContext.getItemSelectorReturnTypeResolver();

Class<?> itemSelectorReturnTypeClass = itemSelectorReturnTypeResolver.getItemSelectorReturnTypeClass();

String uploadURL = itemSelectorUploadViewDisplayContext.getURL();

String namespace = itemSelectorUploadViewDisplayContext.getNamespace();

if (Validator.isNotNull(namespace)) {
	uploadURL = HttpComponentsUtil.addParameter(uploadURL, namespace + "returnType", itemSelectorReturnTypeClass.getName());
}
%>

<clay:container-fluid
	cssClass="lfr-item-viewer"
	id="itemSelectorUploadContainer"
>
	<c:choose>
		<c:when test='<%= GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-160919")) %>'>
			<liferay-util:html-top
				outputKey="item_selector_repository_entry_browser"
			>
				<link href="<%= PortalUtil.getStaticResourceURL(request, PortalUtil.getPathModule() + "/item-selector-taglib/repository_entry_browser/css/main.css") %>" rel="stylesheet" />
			</liferay-util:html-top>

			<div class="dropzone-wrapper dropzone-wrapper-search-container-empty">
				<div class="dropzone dropzone-disabled"><span aria-hidden="true" class="loading-animation loading-animation-sm"></span></div>

				<react:component
					module="js/ItemSelectorRepositoryEntryBrowser"
					props='<%=
						HashMapBuilder.<String, Object>put(
							"closeCaption", itemSelectorUploadViewDisplayContext.getTitle(locale)
						).put(
							"editImageURL", uploadURL
						).put(
							"itemSelectedEventName", itemSelectorUploadViewDisplayContext.getItemSelectedEventName()
						).put(
							"maxFileSize", itemSelectorUploadViewDisplayContext.getMaxFileSize()
						).put(
							"mimeTypeRestriction", itemSelectorUploadViewDisplayContext.getMimeTypeRestriction()
						).put(
							"rootNode", "#itemSelectorUploadContainer"
						).put(
							"uploadItemReturnType", HtmlUtil.escapeAttribute(itemSelectorReturnTypeClass.getName())
						).put(
							"uploadItemURL", uploadURL
						).put(
							"validExtensions", StringUtil.merge(itemSelectorUploadViewDisplayContext.getExtensions())
						).build()
					%>'
				/>
			</div>
		</c:when>
		<c:otherwise>
			<div class="drop-enabled drop-zone item-selector upload-view">
				<div id="uploadDescription">
					<c:if test="<%= !BrowserSnifferUtil.isMobile(request) %>">
						<p>
							<strong><liferay-ui:message arguments="<%= itemSelectorUploadViewDisplayContext.getRepositoryName() %>" key="drag-and-drop-to-upload-to-x-or" /></strong>
						</p>
					</c:if>

					<p>
						<input accept="<%= ArrayUtil.isEmpty(itemSelectorUploadViewDisplayContext.getExtensions()) ? "*" : StringUtil.merge(itemSelectorUploadViewDisplayContext.getExtensions()) %>" class="input-file" id="<portlet:namespace />inputFile" type="file" />

						<label class="btn btn-secondary" for="<portlet:namespace />inputFile"><liferay-ui:message key="select-file" /></label>
					</p>
				</div>
			</div>

			<liferay-ui:drop-here-info
				message="drop-files-here"
			/>

			<div class="item-selector-preview-container"></div>

			<liferay-frontend:component
				context='<%=
					HashMapBuilder.<String, Object>put(
						"closeCaption", itemSelectorUploadViewDisplayContext.getTitle(locale)
					).put(
						"editImageURL", uploadURL
					).put(
						"eventName", itemSelectorUploadViewDisplayContext.getItemSelectedEventName()
					).put(
						"maxFileSize", itemSelectorUploadViewDisplayContext.getMaxFileSize()
					).put(
						"rootNode", "#itemSelectorUploadContainer"
					).put(
						"uploadItemReturnType", HtmlUtil.escapeAttribute(itemSelectorReturnTypeClass.getName())
					).put(
						"uploadItemURL", uploadURL
					).put(
						"validExtensions", ArrayUtil.isEmpty(itemSelectorUploadViewDisplayContext.getExtensions()) ? "*" : StringUtil.merge(itemSelectorUploadViewDisplayContext.getExtensions())
					).build()
				%>'
				module="js/ItemSelectorRepositoryEntryBrowser"
			/>
		</c:otherwise>
	</c:choose>
</clay:container-fluid>