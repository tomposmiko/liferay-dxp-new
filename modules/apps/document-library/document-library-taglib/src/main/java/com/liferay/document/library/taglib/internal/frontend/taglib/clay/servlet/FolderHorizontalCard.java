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

package com.liferay.document.library.taglib.internal.frontend.taglib.clay.servlet;

import com.liferay.document.library.taglib.internal.display.context.RepositoryBrowserTagDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.HorizontalCard;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Adolfo Pérez
 */
public class FolderHorizontalCard implements HorizontalCard {

	public FolderHorizontalCard(
		Folder folder,
		ModelResourcePermission<Folder> folderModelResourcePermission,
		HttpServletRequest httpServletRequest,
		RepositoryBrowserTagDisplayContext repositoryBrowserTagDisplayContext) {

		_folder = folder;
		_folderModelResourcePermission = folderModelResourcePermission;
		_httpServletRequest = httpServletRequest;
		_repositoryBrowserTagDisplayContext =
			repositoryBrowserTagDisplayContext;

		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Override
	public List<DropdownItem> getActionDropdownItems() {
		return DropdownItemListBuilder.add(
			() -> _folderModelResourcePermission.contains(
				_themeDisplay.getPermissionChecker(), _folder,
				ActionKeys.UPDATE),
			dropdownItem -> {
				dropdownItem.putData("action", "rename");
				dropdownItem.putData(
					"renameURL",
					_repositoryBrowserTagDisplayContext.getRenameFolderURL(
						_folder));
				dropdownItem.putData("value", _folder.getName());
				dropdownItem.setIcon("pencil");
				dropdownItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "rename"));
			}
		).add(
			() -> _folderModelResourcePermission.contains(
				_themeDisplay.getPermissionChecker(), _folder,
				ActionKeys.DELETE),
			dropdownItem -> {
				dropdownItem.putData("action", "delete");
				dropdownItem.putData(
					"deleteURL",
					_repositoryBrowserTagDisplayContext.getDeleteFolderURL(
						_folder));
				dropdownItem.setIcon("trash");
				dropdownItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "delete"));
			}
		).build();
	}

	@Override
	public String getDefaultEventHandler() {
		return "repositoryBrowserEventHandler";
	}

	@Override
	public String getHref() {
		return _repositoryBrowserTagDisplayContext.getFolderURL(_folder);
	}

	@Override
	public String getInputName() {
		try {
			SearchContainer<Object> searchContainer =
				_repositoryBrowserTagDisplayContext.getSearchContainer();

			RowChecker rowChecker = searchContainer.getRowChecker();

			if (rowChecker == null) {
				return null;
			}

			return rowChecker.getRowIds();
		}
		catch (PortalException portalException) {
			return ReflectionUtil.throwException(portalException);
		}
	}

	@Override
	public String getInputValue() {
		return String.valueOf(_folder.getFolderId());
	}

	@Override
	public String getTitle() {
		return _folder.getName();
	}

	private final Folder _folder;
	private final ModelResourcePermission<Folder>
		_folderModelResourcePermission;
	private final HttpServletRequest _httpServletRequest;
	private final RepositoryBrowserTagDisplayContext
		_repositoryBrowserTagDisplayContext;
	private final ThemeDisplay _themeDisplay;

}