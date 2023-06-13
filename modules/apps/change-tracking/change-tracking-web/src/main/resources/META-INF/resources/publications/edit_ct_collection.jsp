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

<%@ include file="/publications/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

CTCollection ctCollection = (CTCollection)request.getAttribute(CTWebKeys.CT_COLLECTION);

String actionName = "/change_tracking/edit_ct_collection";
long ctCollectionId = CTConstants.CT_COLLECTION_ID_PRODUCTION;
String description = StringPool.BLANK;
String name = StringPool.BLANK;
String saveButtonLabel = "create";
boolean showTemplates = false;

boolean revert = ParamUtil.getBoolean(request, "revert");

if (revert) {
	actionName = "/change_tracking/undo_ct_collection";
	ctCollectionId = ctCollection.getCtCollectionId();
	name = StringBundler.concat(LanguageUtil.get(request, "revert"), " \"", ctCollection.getName(), "\"");
	saveButtonLabel = "revert-and-create-publication";

	renderResponse.setTitle(LanguageUtil.get(resourceBundle, "revert"));
}
else if (ctCollection != null) {
	ctCollectionId = ctCollection.getCtCollectionId();
	description = ctCollection.getDescription();
	name = ctCollection.getName();
	saveButtonLabel = "save";

	renderResponse.setTitle(StringBundler.concat(LanguageUtil.format(resourceBundle, "edit-x", new Object[] {ctCollection.getName()})));
}
else {
	showTemplates = true;

	renderResponse.setTitle(LanguageUtil.get(request, "create-new-publication"));
}

if (!FeatureFlagManagerUtil.isEnabled("LPS-161313")) {
	showTemplates = false;
}

portletDisplay.setURLBack(redirect);
portletDisplay.setShowBackIcon(true);
%>

<clay:container-fluid
	cssClass="container-form-lg edit-publication-container"
>
	<liferay-portlet:actionURL name="<%= actionName %>" var="actionURL">
		<liferay-portlet:param name="mvcRenderCommandName" value="/change_tracking/view_publications" />
		<liferay-portlet:param name="redirect" value="<%= redirect %>" />
	</liferay-portlet:actionURL>

	<liferay-portlet:resourceURL id="/change_tracking/invite_users" var="inviteUsersURL" />

	<react:component
		module="publications/js/views/ChangeTrackingCollectionEditView"
		props='<%=
			HashMapBuilder.<String, Object>put(
				"actionUrl", actionURL
			).put(
				"ctCollectionId", ctCollectionId
			).put(
				"ctCollectionTemplates", request.getAttribute(CTWebKeys.CT_COLLECTION_TEMPLATES)
			).put(
				"ctCollectionTemplatesData", request.getAttribute(CTWebKeys.CT_COLLECTION_TEMPLATES_DATA)
			).put(
				"descriptionFieldMaxLength", ModelHintsUtil.getMaxLength(CTCollection.class.getName(), "description")
			).put(
				"inviteUsersURL", inviteUsersURL
			).put(
				"nameFieldMaxLength", ModelHintsUtil.getMaxLength(CTCollection.class.getName(), "name")
			).put(
				"namespace", liferayPortletResponse.getNamespace()
			).put(
				"publicationDescription", description
			).put(
				"publicationName", name
			).put(
				"redirect", redirect
			).put(
				"revertingPublication", revert
			).put(
				"saveButtonLabel", LanguageUtil.get(request, saveButtonLabel)
			).put(
				"showTemplates", showTemplates
			).build()
		%>'
	/>
</clay:container-fluid>