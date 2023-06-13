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

<%@ include file="/preview/init.jsp" %>

<%
List<String> previewFileURLs = (List<String>)request.getAttribute(DLPreviewVideoWebKeys.PREVIEW_FILE_URLS);
String videoPosterURL = (String)request.getAttribute(DLPreviewVideoWebKeys.VIDEO_POSTER_URL);
%>

<liferay-util:html-top
	outputKey="document_library_preview_video_css"
>
	<link href="<%= PortalUtil.getStaticResourceURL(request, application.getContextPath() + "/preview/css/main.css") %>" rel="stylesheet" type="text/css" />
</liferay-util:html-top>

<div class="preview-file">
	<div class="preview-file-container preview-file-max-height">
		<video
			class="preview-file-video"
			controls
			controlsList="nodownload"

			<c:if test="<%= Validator.isNotNull(videoPosterURL) %>">
				poster="<%= videoPosterURL %>"
			</c:if>
		>

			<%
			for (String previewFileURL : previewFileURLs) {
				String type = null;

				if (Validator.isNotNull(previewFileURL)) {
					if (previewFileURL.endsWith("mp4")) {
						type = "video/mp4";
					}
					else if (previewFileURL.endsWith("ogv")) {
						type = "video/ogv";
					}
				}

				if (type != null) {
			%>

					<source src="<%= previewFileURL %>" type="<%= type %>" />

			<%
				}
			}
			%>

		</video>
	</div>
</div>