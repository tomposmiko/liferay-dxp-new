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
DLOpenerGoogleDriveFileReference dlOpenerGoogleDriveFileReference = (DLOpenerGoogleDriveFileReference)request.getAttribute(DLOpenerGoogleDriveWebKeys.DL_OPENER_GOOGLE_DRIVE_FILE_REFERENCE);

long cssLastModifiedTime = PortalWebResourcesUtil.getLastModified(PortalWebResourceConstants.RESOURCE_TYPE_CSS);

String googleDocsEditURL = ParamUtil.getString(request, "googleDocsEditURL");
String googleDocsRedirect = ParamUtil.getString(request, "googleDocsRedirect");
%>

<!DOCTYPE html>
<html>
	<head>
		<meta content="initial-scale=1.0, width=device-width" name="viewport">
		<link href="<%= HtmlUtil.escapeAttribute(PortalUtil.getStaticResourceURL(request, themeDisplay.getCDNDynamicResourcesHost() + PortalWebResourcesUtil.getContextPath(PortalWebResourceConstants.RESOURCE_TYPE_CSS) + "/main.css", cssLastModifiedTime)) %>" id="liferayPortalCSS" rel="stylesheet" type="text/css" />
		<link class="lfr-css-file" href="<%= HtmlUtil.escapeAttribute(PortalUtil.getStaticResourceURL(request, themeDisplay.getPathThemeCss() + "/clay.css")) %>" id="liferayAUICSS" rel="stylesheet" type="text/css" />
		<link href="<%= HtmlUtil.escapeAttribute(PortalUtil.getStaticResourceURL(request, StringBundler.concat(themeDisplay.getCDNBaseURL(), PortalUtil.getPathProxy(), application.getContextPath(), "/css/google_docs.css"))) %>" id="liferayGoogleDriveCSS" rel="stylesheet" type="text/css" />
	</head>

	<body>
		<portlet:actionURL name="/document_library/edit_in_google_docs" var="actionURL">
			<portlet:param name="fileEntryId" value="<%= String.valueOf(dlOpenerGoogleDriveFileReference.getFileEntryId()) %>" />
			<portlet:param name="redirect" value="<%= googleDocsRedirect %>" />
		</portlet:actionURL>

		<form action="<%= actionURL %>" method="post">
		<div class="autofit-padded autofit-row autofit-row-center google-docs-toolbar">
			<div class="autofit-col autofit-col-expand">
				<div class="autofit-section">
					<clay:button
						icon="angle-left"
						id="closeAndCheckinBtn"
						label='<%= LanguageUtil.format(resourceBundle, "save-and-return-to-x", themeDisplay.getSiteGroupName()) %>'
						name="<%= renderResponse.getNamespace() + Constants.CMD %>"
						size="sm"
						type="submit"
						value="<%= DLOpenerGoogleDriveWebConstants.GOOGLE_DRIVE_CHECKIN %>"
					/>
				</div>
			</div>

			<div class="autofit-col">
				<clay:button
					id="discardChangesBtn"
					label='<%= LanguageUtil.get(resourceBundle, "discard-changes") %>'
					name="<%= renderResponse.getNamespace() + Constants.CMD %>"
					size="sm"
					type="submit"
					value="<%= DLOpenerGoogleDriveWebConstants.GOOGLE_DRIVE_CANCEL_CHECKOUT %>"
				/>
			</div>
		</div>

		<iframe class="google-docs-iframe" frameborder="0" id="<portlet:namespace />gDocsIFrame" src="<%= googleDocsEditURL %>"></iframe>
	</body>
</html>