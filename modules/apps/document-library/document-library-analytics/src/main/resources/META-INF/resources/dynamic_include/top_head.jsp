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

<%@ include file="/dynamic_include/init.jsp" %>

<script data-senna-track="temporary" type="text/javascript">
	if (window.Analytics) {
		window.<%= DocumentLibraryAnalyticsConstants.JS_PREFIX %>isViewFileEntry = false;
	}
</script>

<aui:script sandbox="<%= true %>">
	var pathnameRegexp = /\/documents\/(\d+)\/(\d+)\/(.+?)\/([^&]+)/;

	function handleDownloadClick(event) {
		if (event.target.nodeName.toLowerCase() === 'a') {
			if (window.Analytics) {
				var anchor = event.target;
				var match = pathnameRegexp.exec(anchor.pathname);

				if (match) {
					var getParameterValue = function(parameterName) {
						var result = null;
						var tmp = [];

						anchor
							.search
							.substr(1)
							.split("&")
							.forEach(
								function(item) {
									tmp = item.split("=");
									if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
								}
							);
						return result;
					}

					var groupId = match[1];
					var fileEntryUUID = match[4];

					fetch(
						'<%= PortalUtil.getPortalURL(request) %><%= Portal.PATH_MODULE %><%= DocumentLibraryAnalyticsConstants.PATH_RESOLVE_FILE_ENTRY %>?groupId=' + encodeURIComponent(groupId) + '&uuid=' + encodeURIComponent(fileEntryUUID),
						{
							credentials: 'include',
							method: 'GET'
						}
					).then(function(response) {
						return response.json();
					}).then(function(response) {
						Analytics.send(
							'documentDownloaded',
							'Document',
							{
								groupId: groupId,
								fileEntryId: response.fileEntryId,
								preview: !!window.<%= DocumentLibraryAnalyticsConstants.JS_PREFIX %>isViewFileEntry,
								title: decodeURIComponent(match[3].replace(/\+/ig, ' ')),
								version: getParameterValue('version')
							}
						);
					}).catch(function() {
						return;
					});
				}
			}
		}
	}

	document.body.addEventListener('click', handleDownloadClick);

	var onDestroyPortlet = function() {
		document.body.removeEventListener('click', handleDownloadClick);
		Liferay.detach('destroyPortlet', onDestroyPortlet);
	}

	Liferay.on('destroyPortlet', onDestroyPortlet);
</aui:script>