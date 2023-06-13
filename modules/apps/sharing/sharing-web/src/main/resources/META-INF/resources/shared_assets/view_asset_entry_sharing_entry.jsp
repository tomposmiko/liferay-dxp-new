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

<%@ include file="/shared_assets/init.jsp" %>

<%
AssetRenderer assetRenderer = (AssetRenderer)renderRequest.getAttribute(AssetRenderer.class.getName());

AssetRendererFactory assetRendererFactory = assetRenderer.getAssetRendererFactory();

AssetEntry assetEntry = assetRendererFactory.getAssetEntry(assetRendererFactory.getClassName(), assetRenderer.getClassPK());

SharedAssetsViewDisplayContext sharedAssetsViewDisplayContext = (SharedAssetsViewDisplayContext)renderRequest.getAttribute(SharedAssetsViewDisplayContext.class.getName());

SharingEntry sharingEntry = (SharingEntry)renderRequest.getAttribute(SharingEntry.class.getName());

String redirect = ParamUtil.getString(request, "redirect");

if (Validator.isNull(redirect)) {
	PortletURL portletURL = liferayPortletResponse.createRenderURL();

	redirect = portletURL.toString();
}

Group scopeGroup = themeDisplay.getScopeGroup();

if (scopeGroup.equals(themeDisplay.getControlPanelGroup())) {
	portletDisplay.setShowBackIcon(true);
	portletDisplay.setURLBack(redirect);
}
else {
	portletDisplay.setPortletDecorate(false);
	portletDisplay.setShowBackIcon(false);
}
%>

<c:if test="<%= scopeGroup.equals(themeDisplay.getControlPanelGroup()) %>">
	<div class="upper-tbar-container-fixed">
</c:if>

<div class="tbar upper-tbar">
	<div class="container-fluid container-fluid-max-xl">
		<ul class="tbar-nav">
			<c:if test="<%= !scopeGroup.equals(themeDisplay.getControlPanelGroup()) %>">
				<li class="d-none d-sm-flex tbar-item">
					<clay:link
						elementClasses="btn btn-monospaced btn-outline-borderless btn-outline-secondary btn-sm"
						href="<%= redirect %>"
						icon="angle-left"
					/>
				</li>
			</c:if>

			<li class="tbar-item tbar-item-expand">
				<div class="tbar-section text-left">
					<h2 class="text-truncate-inline upper-tbar-title" title="<%= HtmlUtil.escapeAttribute(assetRenderer.getTitle(locale)) %>">
						<span class="text-truncate"><%= HtmlUtil.escape(assetRenderer.getTitle(locale)) %></span>
					</h2>
				</div>
			</li>
			<li class="tbar-item">
				<liferay-ui:menu
					menu="<%= sharedAssetsViewDisplayContext.getSharingEntryMenu(sharingEntry) %>"
				/>
			</li>
		</ul>
	</div>
</div>

<c:if test="<%= scopeGroup.equals(themeDisplay.getControlPanelGroup()) %>">
	</div>
</c:if>

<liferay-asset:asset-display
	assetEntry="<%= assetEntry %>"
	assetRenderer="<%= assetRenderer %>"
	assetRendererFactory="<%= assetRendererFactory %>"
	showExtraInfo="<%= true %>"
	template="<%= AssetRenderer.TEMPLATE_FULL_CONTENT %>"
/>

<c:if test="<%= assetRenderer.isCommentable() %>">
	<div class="container-fluid-1280">
		<liferay-comment:discussion
			className="<%= assetEntry.getClassName() %>"
			classPK="<%= assetEntry.getClassPK() %>"
			formName='<%= "fm" + assetEntry.getClassPK() %>'
			ratingsEnabled="<%= false %>"
			redirect="<%= currentURL %>"
			userId="<%= assetRenderer.getUserId() %>"
		/>
	</div>
</c:if>