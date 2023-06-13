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

package com.liferay.document.library.web.internal.display.context;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryTypeServiceUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemList;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian I. Kim
 */
public class DLViewMoreMenuItemsDisplayContext {

	public DLViewMoreMenuItemsDisplayContext(
		long folderId, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		_folderId = folderId;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;

		_httpServletRequest = PortalUtil.getHttpServletRequest(renderRequest);
	}

	public String getClearResultsURL() {
		return getSearchActionURL();
	}

	public String getEventName() {
		if (_eventName != null) {
			return _eventName;
		}

		_eventName = ParamUtil.getString(
			_renderRequest, "eventName",
			_renderResponse.getNamespace() + "selectAddMenuItem");

		return _eventName;
	}

	public List<NavigationItem> getNavigationItems() {
		return new NavigationItemList() {
			{
				add(
					navigationItem -> {
						navigationItem.setActive(true);
						navigationItem.setHref(StringPool.BLANK);
						navigationItem.setLabel(
							LanguageUtil.get(
								_httpServletRequest, "document-types"));
					});
			}
		};
	}

	public PortletURL getPortletURL() {
		PortletURL portletURL = _renderResponse.createRenderURL();

		portletURL.setParameter(
			"mvcPath", "/document_library/view_more_menu_items.jsp");
		portletURL.setParameter("folderId", String.valueOf(_folderId));
		portletURL.setParameter("eventName", getEventName());

		return portletURL;
	}

	public String getSearchActionURL() {
		return String.valueOf(getPortletURL());
	}

	public SearchContainer getSearchContainer() throws PortalException {
		if (_searchContainer != null) {
			return _searchContainer;
		}

		SearchContainer searchContainer = new SearchContainer(
			_renderRequest, new DisplayTerms(_httpServletRequest),
			new DisplayTerms(_httpServletRequest),
			SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA,
			getPortletURL(), null,
			LanguageUtil.get(_httpServletRequest, "there-are-no-results"));

		DisplayTerms searchTerms = searchContainer.getSearchTerms();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		long folderId = _getPrimaryFolderId(_folderId);
		boolean includeBasicFileEntryType = ParamUtil.getBoolean(
			_renderRequest, "includeBasicFileEntryType");

		searchContainer.setResults(
			DLFileEntryTypeServiceUtil.search(
				themeDisplay.getCompanyId(), folderId,
				PortalUtil.getCurrentAndAncestorSiteGroupIds(
					themeDisplay.getScopeGroupId()),
				searchTerms.getKeywords(), includeBasicFileEntryType,
				_inherited, searchContainer.getStart(),
				searchContainer.getEnd()));
		searchContainer.setTotal(
			DLFileEntryTypeServiceUtil.searchCount(
				themeDisplay.getCompanyId(), folderId,
				PortalUtil.getCurrentAndAncestorSiteGroupIds(
					themeDisplay.getScopeGroupId()),
				searchTerms.getKeywords(), includeBasicFileEntryType,
				_inherited));

		_searchContainer = searchContainer;

		return _searchContainer;
	}

	public int getTotalItems() throws PortalException {
		SearchContainer searchContainer = getSearchContainer();

		return searchContainer.getTotal();
	}

	private long _getPrimaryFolderId(long folderId) throws PortalException {
		while (folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			Folder folder = DLAppServiceUtil.getFolder(folderId);

			if ((folder != null) && (folder.getModel() instanceof DLFolder)) {
				DLFolder dlFolder = (DLFolder)folder.getModel();

				if (dlFolder.getRestrictionType() ==
						DLFolderConstants.
							RESTRICTION_TYPE_FILE_ENTRY_TYPES_AND_WORKFLOW) {

					_inherited = false;

					break;
				}

				folderId = dlFolder.getParentFolderId();
			}
		}

		return folderId;
	}

	private String _eventName;
	private final long _folderId;
	private final HttpServletRequest _httpServletRequest;
	private boolean _inherited = true;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private SearchContainer _searchContainer;

}