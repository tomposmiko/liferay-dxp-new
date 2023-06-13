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

<%@ include file="/dynamic_section/init.jsp" %>

<%
JSONArray collaboratorsJSONArray = JSONFactoryUtil.createJSONArray();
%>

<div class="autofit-row widget-metadata">
	<div class="autofit-col inline-item-before">

		<%
		FileEntry fileEntry = (FileEntry)request.getAttribute("info_panel.jsp-fileEntry");

		User owner = UserLocalServiceUtil.fetchUser(fileEntry.getUserId());
		%>

		<div class="lfr-portal-tooltip" data-title="<%= LanguageUtil.format(resourceBundle, "x-is-the-owner", owner.getFullName()) %>">
			<liferay-ui:user-portrait
				cssClass="user-icon-lg"
				user="<%= owner %>"
			/>
		</div>
	</div>

	<div class="autofit-col autofit-col-expand inline-item-after">
		<div class="autofit-row">

			<%
			List<User> sharingEntryToUsers = (List<User>)request.getAttribute("info_panel_file_entry.jsp-sharingEntryToUsers");

			for (User sharingEntryToUser : sharingEntryToUsers) {
				JSONObject collaboratorJSONObject = JSONFactoryUtil.createJSONObject();

				collaboratorJSONObject.put("id", sharingEntryToUser.getUserId());
				collaboratorJSONObject.put("imageSrc", sharingEntryToUser.getPortraitURL(themeDisplay));
				collaboratorJSONObject.put("name", sharingEntryToUser.getFullName());

				collaboratorsJSONArray.put(collaboratorJSONObject);
			%>

				<div class="autofit-col">
					<div class="lfr-portal-tooltip" data-title="<%= sharingEntryToUser.getFullName() %>">
						<liferay-ui:user-portrait
							cssClass="user-icon-lg"
							user="<%= sharingEntryToUser %>"
						/>
					</div>
				</div>

			<%
			}

			int countSharingEntryToUserIds = GetterUtil.getInteger(request.getAttribute("info_panel_file_entry.jsp-countSharingEntryToUserIds"));
			%>

			<c:if test="<%= countSharingEntryToUserIds > 4 %>">

				<%
				int moreCollaboratorsCount = countSharingEntryToUserIds - 4;
				%>

				<div class="lfr-portal-tooltip rounded-circle sticker sticker-lg sticker-secondary" data-title="<%= LanguageUtil.format(resourceBundle, (moreCollaboratorsCount == 1) ? "x-more-collaborator" : "x-more-collaborators", moreCollaboratorsCount) %>">
					+<%= moreCollaboratorsCount %>
				</div>
			</c:if>
		</div>
	</div>
</div>

<div class="autofit-row sidebar-panel">
	<clay:button
		elementClasses="manage-collaborators-btn"
		id='<%= liferayPortletResponse.getNamespace() + "manageCollaboratorsButton" %>'
		label='<%= LanguageUtil.get(resourceBundle, "manage-collaborators") %>'
		size="sm"
		style="link"
	/>
</div>

<%
PortletURL manageCollaboratorsRenderURL = PortletProviderUtil.getPortletURL(request, SharingEntry.class.getName(), PortletProvider.Action.MANAGE);

manageCollaboratorsRenderURL.setParameter("classNameId", String.valueOf(ClassNameLocalServiceUtil.getClassNameId(DLFileEntry.class.getName())));
manageCollaboratorsRenderURL.setParameter("classPK", String.valueOf(fileEntry.getFileEntryId()));
manageCollaboratorsRenderURL.setParameter("manageCollaboratorDialogId", liferayPortletResponse.getNamespace() + "manageCollaboratorsDialog");
manageCollaboratorsRenderURL.setWindowState(LiferayWindowState.POP_UP);
%>

<aui:script>
	var button = document.getElementById('<portlet:namespace/>manageCollaboratorsButton');

	button.addEventListener(
		'click',
		function() {
			Liferay.Util.openWindow(
				{
					dialog: {
						destroyOnHide: true,
						height: 470,
						width: 600,
						on: {
							visibleChange: function(event) {
								if (!event.newVal) {
									Liferay.Util.getOpener().Liferay.fire('refreshInfoPanel');
								}
							}
						}
					},
					id: '<portlet:namespace />manageCollaboratorsDialog',
					title: '<%= LanguageUtil.get(resourceBundle, "collaborators") %>',
					uri: '<%= manageCollaboratorsRenderURL.toString() %>'
				}
			);
		}
	);
</aui:script>