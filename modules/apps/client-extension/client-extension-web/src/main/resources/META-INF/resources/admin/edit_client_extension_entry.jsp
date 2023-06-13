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

<%@ include file="/admin/init.jsp" %>

<%
EditClientExtensionEntryDisplayContext editClientExtensionEntryDisplayContext = (EditClientExtensionEntryDisplayContext)renderRequest.getAttribute(ClientExtensionAdminWebKeys.EDIT_CLIENT_EXTENSION_ENTRY_DISPLAY_CONTEXT);

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(editClientExtensionEntryDisplayContext.getRedirect());

renderResponse.setTitle(editClientExtensionEntryDisplayContext.getTitle());
%>

<portlet:actionURL name="/client_extension_admin/edit_client_extension_entry" var="editClientExtensionEntryURL" />

<liferay-frontend:edit-form
	action="<%= editClientExtensionEntryURL %>"
	method="post"
>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= editClientExtensionEntryDisplayContext.getCmd() %>" />
	<aui:input name="redirect" type="hidden" value="<%= editClientExtensionEntryDisplayContext.getRedirect() %>" />
	<aui:input name="externalReferenceCode" type="hidden" value="<%= editClientExtensionEntryDisplayContext.getExternalReferenceCode() %>" />

	<liferay-ui:error exception="<%= ClientExtensionEntryTypeSettingsException.class %>">

		<%
		ClientExtensionEntryTypeSettingsException clientExtensionEntryTypeSettingsException = (ClientExtensionEntryTypeSettingsException)errorException;
		%>

		<liferay-ui:message arguments="<%= clientExtensionEntryTypeSettingsException.getMessageArguments() %>" key="<%= clientExtensionEntryTypeSettingsException.getMessageKey() %>" />
	</liferay-ui:error>

	<liferay-frontend:edit-form-body>
		<liferay-frontend:fieldset-group>
			<%@ include file="/admin/edit_common_fields.jspf" %>

			<%@ include file="/admin/edit_custom_element_fields.jspf" %>

			<%@ include file="/admin/edit_global_css_fields.jspf" %>

			<%@ include file="/admin/edit_global_js_fields.jspf" %>

			<%@ include file="/admin/edit_iframe_fields.jspf" %>

			<%@ include file="/admin/edit_portlet_fields.jspf" %>

			<%@ include file="/admin/edit_theme_css_fields.jspf" %>

			<%@ include file="/admin/edit_theme_favicon_fields.jspf" %>

			<%@ include file="/admin/edit_theme_js_fields.jspf" %>
		</liferay-frontend:fieldset-group>
	</liferay-frontend:edit-form-body>

	<liferay-frontend:edit-form-footer>
		<clay:button
			label='<%= WorkflowDefinitionLinkLocalServiceUtil.hasWorkflowDefinitionLink(themeDisplay.getCompanyId(), 0L, ClientExtensionEntry.class.getName()) ? "submit-for-publication" : "publish" %>'
			type="submit"
		/>

		<clay:link
			displayType="secondary"
			href="<%= editClientExtensionEntryDisplayContext.getRedirect() %>"
			label="cancel"
			type="button"
		/>
	</liferay-frontend:edit-form-footer>
</liferay-frontend:edit-form>