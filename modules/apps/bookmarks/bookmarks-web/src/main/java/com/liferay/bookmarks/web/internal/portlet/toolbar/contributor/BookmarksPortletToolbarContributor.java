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

package com.liferay.bookmarks.web.internal.portlet.toolbar.contributor;

import com.liferay.bookmarks.configuration.BookmarksGroupServiceOverriddenConfiguration;
import com.liferay.bookmarks.constants.BookmarksConstants;
import com.liferay.bookmarks.constants.BookmarksFolderConstants;
import com.liferay.bookmarks.constants.BookmarksPortletKeys;
import com.liferay.bookmarks.constants.BookmarksWebKeys;
import com.liferay.bookmarks.exception.NoSuchFolderException;
import com.liferay.bookmarks.model.BookmarksFolder;
import com.liferay.bookmarks.service.BookmarksFolderService;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.portal.kernel.bean.BeanParamUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.portlet.toolbar.contributor.BasePortletToolbarContributor;
import com.liferay.portal.kernel.portlet.toolbar.contributor.PortletToolbarContributor;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.MenuItem;
import com.liferay.portal.kernel.servlet.taglib.ui.URLMenuItem;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + BookmarksPortletKeys.BOOKMARKS,
		"mvc.render.command.name=-", "mvc.render.command.name=/bookmarks/view",
		"mvc.render.command.name=/bookmarks/view_folder"
	},
	service = {
		BookmarksPortletToolbarContributor.class,
		PortletToolbarContributor.class
	}
)
public class BookmarksPortletToolbarContributor
	extends BasePortletToolbarContributor {

	protected void addPortletTitleAddBookmarkMenuItem(
			List<MenuItem> menuItems, BookmarksFolder folder,
			ThemeDisplay themeDisplay, PortletRequest portletRequest)
		throws PortalException {

		long folderId = _getFolderId(folder);

		if (!containsPermission(
				themeDisplay.getPermissionChecker(),
				themeDisplay.getScopeGroupId(), folderId,
				ActionKeys.ADD_ENTRY)) {

			return;
		}

		URLMenuItem urlMenuItem = new URLMenuItem();

		urlMenuItem.setLabel(
			LanguageUtil.get(
				_portal.getHttpServletRequest(portletRequest), "bookmark"));

		PortletURL portletURL = _portal.getControlPanelPortletURL(
			portletRequest, themeDisplay.getScopeGroup(),
			BookmarksPortletKeys.BOOKMARKS_ADMIN, 0, 0,
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter(
			"mvcRenderCommandName", "/bookmarks/edit_entry");
		portletURL.setParameter(
			"redirect", _portal.getCurrentURL(portletRequest));

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		portletURL.setParameter("portletResource", portletDisplay.getId());

		portletURL.setParameter("folderId", String.valueOf(folderId));

		urlMenuItem.setURL(portletURL.toString());

		menuItems.add(urlMenuItem);
	}

	protected void addPortletTitleAddFolderMenuItem(
			List<MenuItem> menuItems, BookmarksFolder folder,
			ThemeDisplay themeDisplay, PortletRequest portletRequest)
		throws PortalException {

		long folderId = _getFolderId(folder);

		if (!containsPermission(
				themeDisplay.getPermissionChecker(),
				themeDisplay.getScopeGroupId(), folderId,
				ActionKeys.ADD_FOLDER)) {

			return;
		}

		URLMenuItem urlMenuItem = new URLMenuItem();

		urlMenuItem.setLabel(
			LanguageUtil.get(
				_portal.getHttpServletRequest(portletRequest), "folder"));

		PortletURL portletURL = _portal.getControlPanelPortletURL(
			portletRequest, themeDisplay.getScopeGroup(),
			BookmarksPortletKeys.BOOKMARKS_ADMIN, 0, 0,
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter(
			"mvcRenderCommandName", "/bookmarks/edit_folder");
		portletURL.setParameter(
			"redirect", _portal.getCurrentURL(portletRequest));

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		portletURL.setParameter("portletResource", portletDisplay.getId());

		portletURL.setParameter("parentFolderId", String.valueOf(folderId));

		urlMenuItem.setURL(portletURL.toString());

		menuItems.add(urlMenuItem);
	}

	protected boolean containsPermission(
		PermissionChecker permissionChecker, long groupId, long folderId,
		String actionId) {

		try {
			return ModelResourcePermissionUtil.contains(
				_bookmarksFolderModelResourcePermission, permissionChecker,
				groupId, folderId, actionId);
		}
		catch (PortalException portalException) {

			// LPS-52675

			if (_log.isDebugEnabled()) {
				_log.debug(portalException, portalException);
			}

			return false;
		}
	}

	@Override
	protected List<MenuItem> getPortletTitleMenuItems(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		BookmarksFolder folder = _getFolder(themeDisplay, portletRequest);

		List<MenuItem> menuItems = new ArrayList<>();

		try {
			addPortletTitleAddFolderMenuItem(
				menuItems, folder, themeDisplay, portletRequest);
		}
		catch (PortalException portalException) {
			_log.error("Unable to add folder menu item", portalException);
		}

		try {
			addPortletTitleAddBookmarkMenuItem(
				menuItems, folder, themeDisplay, portletRequest);
		}
		catch (PortalException portalException) {
			_log.error("Unable to add bookmark menu item", portalException);
		}

		return menuItems;
	}

	private BookmarksFolder _getFolder(
		ThemeDisplay themeDisplay, PortletRequest portletRequest) {

		BookmarksFolder folder = (BookmarksFolder)portletRequest.getAttribute(
			BookmarksWebKeys.BOOKMARKS_FOLDER);

		if (folder != null) {
			return folder;
		}

		long rootFolderId = BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID;

		try {
			BookmarksGroupServiceOverriddenConfiguration
				bookmarksGroupServiceOverriddenConfiguration =
					ConfigurationProviderUtil.getConfiguration(
						BookmarksGroupServiceOverriddenConfiguration.class,
						new GroupServiceSettingsLocator(
							themeDisplay.getScopeGroupId(),
							BookmarksConstants.SERVICE_NAME));

			rootFolderId =
				bookmarksGroupServiceOverriddenConfiguration.rootFolderId();
		}
		catch (ConfigurationException configurationException) {
			_log.error(
				"Unable to obtain bookmarks root folder ID for group " +
					themeDisplay.getScopeGroupId(),
				configurationException);
		}

		long folderId = BeanParamUtil.getLong(
			folder, portletRequest, "folderId", rootFolderId);

		if (folderId != BookmarksFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			try {
				folder = _bookmarksFolderService.getFolder(folderId);
			}
			catch (NoSuchFolderException noSuchFolderException) {
				folder = null;
			}
			catch (PortalException portalException) {
				_log.error(portalException, portalException);
			}
		}

		return folder;
	}

	private long _getFolderId(BookmarksFolder folder) {
		long folderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;

		if (folder != null) {
			folderId = folder.getFolderId();
		}

		return folderId;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BookmarksPortletToolbarContributor.class);

	@Reference(
		target = "(model.class.name=com.liferay.bookmarks.model.BookmarksFolder)"
	)
	private ModelResourcePermission<BookmarksFolder>
		_bookmarksFolderModelResourcePermission;

	@Reference
	private BookmarksFolderService _bookmarksFolderService;

	@Reference
	private Portal _portal;

}