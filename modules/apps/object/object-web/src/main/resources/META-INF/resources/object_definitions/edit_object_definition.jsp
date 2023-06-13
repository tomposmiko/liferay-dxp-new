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
ObjectDefinition objectDefinition = (ObjectDefinition)request.getAttribute(ObjectWebKeys.OBJECT_DEFINITION);
ObjectDefinitionsDetailsDisplayContext objectDefinitionsDetailsDisplayContext = (ObjectDefinitionsDetailsDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<c:choose>
	<c:when test='<%= GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-155914")) %>'>
		<div class="lfr-object__edit-object-definition">
			<div>
				<react:component
					module="js/components/ManagementToolbar"
					props='<%=
						HashMapBuilder.<String, Object>put(
							"backURL", ParamUtil.getString(request, "backURL", String.valueOf(renderResponse.createRenderURL()))
						).put(
							"externalReferenceCode", objectDefinition.getExternalReferenceCode()
						).put(
							"hasPublishObjectPermission", objectDefinitionsDetailsDisplayContext.hasPublishObjectPermission()
						).put(
							"hasUpdateObjectDefinitionPermission", objectDefinitionsDetailsDisplayContext.hasUpdateObjectDefinitionPermission()
						).put(
							"isApproved", objectDefinition.isApproved()
						).put(
							"label", objectDefinition.getLabel(locale, true)
						).put(
							"objectDefinitionId", objectDefinition.getObjectDefinitionId()
						).put(
							"portletNamespace", liferayPortletResponse.getNamespace()
						).put(
							"screenNavigationCategoryKey", ParamUtil.getString(request, "screenNavigationCategoryKey")
						).put(
							"system", objectDefinition.isSystem()
						).build()
					%>'
				/>
			</div>

			<liferay-frontend:screen-navigation
				context="<%= objectDefinition %>"
				key="<%= ObjectDefinitionsScreenNavigationEntryConstants.SCREEN_NAVIGATION_KEY_OBJECT_DEFINITION %>"
				portletURL='<%=
					PortletURLBuilder.createRenderURL(
						renderResponse
					).setMVCRenderCommandName(
						"/object_definitions/edit_object_definition"
					).setParameter(
						"objectDefinitionId", objectDefinition.getObjectDefinitionId()
					).build()
				%>'
			/>
		</div>
	</c:when>
	<c:otherwise>
		<liferay-frontend:screen-navigation
			context="<%= objectDefinition %>"
			key="<%= ObjectDefinitionsScreenNavigationEntryConstants.SCREEN_NAVIGATION_KEY_OBJECT_DEFINITION %>"
			portletURL='<%=
				PortletURLBuilder.createRenderURL(
					renderResponse
				).setMVCRenderCommandName(
					"/object_definitions/edit_object_definition"
				).setParameter(
					"objectDefinitionId", objectDefinition.getObjectDefinitionId()
				).build()
			%>'
		/>
	</c:otherwise>
</c:choose>