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

<%@ include file="/wiki/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

String uploadProgressId = PortalUtil.generateRandomKey(request, "portlet_wiki_import_pages_uploadProgressId");
String importProgressId = PortalUtil.generateRandomKey(request, "portlet_wiki_import_pages_importProgressId");

WikiNode node = (WikiNode)request.getAttribute(WikiWebKeys.WIKI_NODE);

long nodeId = BeanParamUtil.getLong(node, request, "nodeId");

PortletURL portletURL = PortletURLBuilder.createRenderURL(
	renderResponse
).setMVCRenderCommandName(
	"/wiki/import_pages"
).setRedirect(
	redirect
).setParameter(
	"nodeId", nodeId
).buildPortletURL();

portletDisplay.setShowBackIcon(true);

WikiURLHelper wikiURLHelper = new WikiURLHelper(wikiRequestHelper, renderResponse, wikiGroupServiceConfiguration);

PortletURL backToNodeURL = wikiURLHelper.getBackToNodeURL(node);

portletDisplay.setURLBack(backToNodeURL.toString());

renderResponse.setTitle(LanguageUtil.get(request, "import-pages"));
%>

<portlet:actionURL name="/wiki/import_pages" var="importPagesURL" />

<clay:container-fluid
	cssClass="container-form-lg"
>
	<aui:form action="<%= importPagesURL %>" enctype="multipart/form-data" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + liferayPortletResponse.getNamespace() + "importPages();" %>'>
		<aui:input name="<%= Constants.CMD %>" type="hidden" />
		<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
		<aui:input name="importProgressId" type="hidden" value="<%= importProgressId %>" />
		<aui:input name="nodeId" type="hidden" value="<%= nodeId %>" />

		<div class="sheet">
			<div class="panel-group panel-group-flush">
				<liferay-ui:tabs
					names="MediaWiki"
					param="tabs2"
					url="<%= portletURL.toString() %>"
				/>

				<liferay-ui:error exception="<%= ImportFilesException.class %>" message="please-provide-all-mandatory-files-and-make-sure-the-file-types-are-valid" />
				<liferay-ui:error exception="<%= NoSuchNodeException.class %>" message="the-node-could-not-be-found" />

				<liferay-util:include page="/wiki/import/mediawiki.jsp" servletContext="<%= application %>" />

				<div class="sheet-footer">
					<aui:button type="submit" value="import" />

					<aui:button href="<%= redirect %>" type="cancel" />
				</div>
			</div>
		</div>
	</aui:form>

	<liferay-ui:upload-progress
		id="<%= uploadProgressId %>"
		message="uploading"
	/>

	<liferay-ui:upload-progress
		id="<%= importProgressId %>"
		message="importing"
	/>
</clay:container-fluid>

<aui:script>
	function <portlet:namespace />importPages() {
		<%= uploadProgressId %>.startProgress();
		<%= importProgressId %>.startProgress();

		submitForm(document.<portlet:namespace />fm);
	}
</aui:script>