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

<%@ include file="/blogs_admin/init.jsp" %>

<%
int delta = ParamUtil.getInteger(request, SearchContainer.DEFAULT_DELTA_PARAM);
String orderByCol = ParamUtil.getString(request, "orderByCol", "title");
String orderByType = ParamUtil.getString(request, "orderByType", "asc");

DLMimeTypeDisplayContext dlMimeTypeDisplayContext = (DLMimeTypeDisplayContext)request.getAttribute(BlogsWebKeys.DL_MIME_TYPE_DISPLAY_CONTEXT);

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("mvcRenderCommandName", "/blogs/view");
portletURL.setParameter("navigation", "images");

if (delta > 0) {
	portletURL.setParameter("delta", String.valueOf(delta));
}

portletURL.setParameter("orderBycol", orderByCol);
portletURL.setParameter("orderByType", orderByType);

request.setAttribute("view_images.jsp-portletURL", portletURL);

SearchContainer blogImagesSearchContainer = new SearchContainer(renderRequest, PortletURLUtil.clone(portletURL, liferayPortletResponse), null, "no-images-were-found");

blogImagesSearchContainer.setOrderByComparator(DLUtil.getRepositoryModelOrderByComparator(orderByCol, orderByType));

blogImagesSearchContainer.setRowChecker(new EmptyOnClickRowChecker(renderResponse));

BlogImagesDisplayContext blogImagesDisplayContext = new BlogImagesDisplayContext(liferayPortletRequest);

blogImagesDisplayContext.populateResults(blogImagesSearchContainer);

BlogImagesManagementToolbarDisplayContext blogImagesManagementToolbarDisplayContext = new BlogImagesManagementToolbarDisplayContext(liferayPortletRequest, liferayPortletResponse, currentURLObj);

String displayStyle = blogImagesManagementToolbarDisplayContext.getDisplayStyle();
%>

<clay:management-toolbar
	actionItems="<%= blogImagesManagementToolbarDisplayContext.getActionDropdownItems() %>"
	clearResultsURL="<%= blogImagesManagementToolbarDisplayContext.getSearchActionURL() %>"
	disabled="<%= blogImagesSearchContainer.getTotal() <= 0 %>"
	filterItems="<%= blogImagesManagementToolbarDisplayContext.getFilterDropdownItems() %>"
	searchActionURL="<%= blogImagesManagementToolbarDisplayContext.getSearchActionURL() %>"
	searchContainerId="images"
	searchFormName="searchFm"
	showCreationMenu="<%= false %>"
	showInfoButton="<%= false %>"
	sortingOrder="<%= blogImagesManagementToolbarDisplayContext.getOrderByType() %>"
	sortingURL="<%= String.valueOf(blogImagesManagementToolbarDisplayContext.getSortingURL()) %>"
	totalItems="<%= blogImagesSearchContainer.getTotal() %>"
	viewTypes="<%= blogImagesManagementToolbarDisplayContext.getViewTypes() %>"
/>

<div class="container-fluid-1280 main-content-body">
	<portlet:actionURL name="/blogs/edit_image" var="editImageURL" />

	<aui:form action="<%= editImageURL %>" name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" />
		<aui:input name="redirect" type="hidden" value="<%= portletURL.toString() %>" />
		<aui:input name="deleteFileEntryIds" type="hidden" />

		<liferay-ui:search-container
			id="images"
			searchContainer="<%= blogImagesSearchContainer %>"
		>
			<liferay-ui:search-container-row
				className="com.liferay.portal.kernel.repository.model.FileEntry"
				keyProperty="fileEntryId"
				modelVar="fileEntry"
			>
				<liferay-portlet:renderURL varImpl="rowURL">
					<portlet:param name="mvcRenderCommandName" value="/blogs/edit_image" />
					<portlet:param name="fileEntryId" value="<%= String.valueOf(fileEntry.getFileEntryId()) %>" />
				</liferay-portlet:renderURL>

				<%@ include file="/blogs_admin/image_search_columns.jspf" %>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				displayStyle="<%= displayStyle %>"
				markupView="lexicon"
			/>
		</liferay-ui:search-container>
	</aui:form>
</div>

<aui:script>
	function <portlet:namespace />deleteImages() {
		if (confirm('<%= UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-the-selected-images") %>')) {
			var form = AUI.$(document.<portlet:namespace />fm);

			form.fm('<%= Constants.CMD %>').val('<%= Constants.DELETE %>');
			form.fm('deleteFileEntryIds').val(Liferay.Util.listCheckedExcept(form, '<portlet:namespace />allRowIds'));

			submitForm(form);
		}
	}
</aui:script>