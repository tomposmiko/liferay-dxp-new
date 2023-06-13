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

<%@ include file="/html/taglib/ui/panel/init.jsp" %>

		</div>
	</div>
</div>

<c:if test="<%= collapsible %>">
	<aui:script sandbox="<%= true %>" use="aui-base,liferay-store">
		var storeTask = A.debounce(Liferay.Store, 100);

		function onPanelHide(event) {
			if (event.panel.getAttribute('id') === '<%= id %>Content') {
				var task = {};

				task['<%= id %>'] = true;

				storeTask(task);
			}
		}

		function onPanelShow(event) {
			if (event.panel.getAttribute('id') === '<%= id %>Content') {
				var task = {};

				task['<%= id %>'] = false;

				storeTask(task);
			}
		}

		function onStartNavigate() {
			Liferay.detach('liferay.collapse.hide', onPanelHide);
			Liferay.detach('liferay.collapse.show', onPanelShow);
			Liferay.detach('startNavigate', onStartNavigate);
		}

		Liferay.on('liferay.collapse.hide', onPanelHide);
		Liferay.on('liferay.collapse.show', onPanelShow);
		Liferay.on('startNavigate', onStartNavigate);
	</aui:script>
</c:if>