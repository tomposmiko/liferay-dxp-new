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
ContentPageEditorDisplayContext contentPageEditorDisplayContext = (ContentPageEditorDisplayContext)request.getAttribute(ContentPageEditorWebKeys.LIFERAY_SHARED_CONTENT_PAGE_EDITOR_DISPLAY_CONTEXT);
%>

<div class="management-bar navbar navbar-expand-md page-editor-toolbar" id="<%= contentPageEditorDisplayContext.getPortletNamespace() %>pageEditorToolbar">
	<div class="container-fluid container-fluid-max-xl">
		<ul class="navbar-nav">
		</ul>

		<ul class="navbar-nav">
			<c:if test="<%= Validator.isNotNull(contentPageEditorDisplayContext.getDiscardDraftURL()) %>">
				<li class="nav-item">
					<button class="btn btn-secondary nav-btn" disabled type="submit">
						<c:choose>
							<c:when test="<%= contentPageEditorDisplayContext.isSingleSegmentsExperienceMode() %>">
								<liferay-ui:message key="discard-variant" />
							</c:when>
							<c:otherwise>
								<liferay-ui:message key="discard-draft" />
							</c:otherwise>
						</c:choose>
					</button>
				</li>
			</c:if>

			<c:if test="<%= Validator.isNotNull(contentPageEditorDisplayContext.getPublishURL()) %>">
				<li class="nav-item">
					<button class="btn btn-primary nav-btn" disabled type="submit">
						<c:choose>
							<c:when test="<%= contentPageEditorDisplayContext.isSingleSegmentsExperienceMode() %>">
								<liferay-ui:message key="save-variant" />
							</c:when>
							<c:otherwise>
								<liferay-ui:message key="publish" />
							</c:otherwise>
						</c:choose>
					</button>
				</li>
			</c:if>
		</ul>
	</div>
</div>