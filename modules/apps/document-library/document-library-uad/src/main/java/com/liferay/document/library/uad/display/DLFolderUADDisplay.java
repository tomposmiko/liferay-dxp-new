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

package com.liferay.document.library.uad.display;

import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.user.associated.data.display.UADDisplay;

import java.io.Serializable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	immediate = true, service = {DLFolderUADDisplay.class, UADDisplay.class}
)
public class DLFolderUADDisplay extends BaseDLFolderUADDisplay {

	@Override
	public String getEditURL(
			DLFolder dlFolder, LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
			portal.getControlPanelPlid(liferayPortletRequest),
			DLPortletKeys.DOCUMENT_LIBRARY_ADMIN, PortletRequest.RENDER_PHASE);

		portletURL.setParameter(
			"mvcRenderCommandName", "/document_library/edit_folder");
		portletURL.setParameter(
			"redirect", portal.getCurrentURL(liferayPortletRequest));
		portletURL.setParameter(
			"folderId", String.valueOf(dlFolder.getFolderId()));
		portletURL.setParameter(
			"repositoryId", String.valueOf(dlFolder.getRepositoryId()));

		return portletURL.toString();
	}

	@Override
	public Map<String, Object> getFieldValues(
		DLFolder dlFolder, String[] fieldNames, Locale locale) {

		Map<String, Object> fieldValues = super.getFieldValues(
			dlFolder, fieldNames, locale);

		List<String> fieldNamesList = Arrays.asList(fieldNames);

		if (fieldNamesList.contains("type")) {
			fieldValues.put("type", "--");
		}

		return fieldValues;
	}

	@Override
	public String getName(DLFolder dlFolder, Locale locale) {
		return dlFolder.getName();
	}

	@Override
	public Class<?> getParentContainerClass() {
		return DLFolder.class;
	}

	@Override
	public Serializable getParentContainerId(DLFolder dlFolder) {
		return dlFolder.getParentFolderId();
	}

	@Override
	public DLFolder getTopLevelContainer(
		Class<?> parentContainerClass, Serializable parentContainerId,
		Object childObject) {

		try {
			DLFolder childFolder = null;

			if (childObject instanceof DLFileEntry) {
				DLFileEntry dlFileEntry = (DLFileEntry)childObject;

				childFolder = dlFileEntry.getFolder();
			}
			else {
				childFolder = (DLFolder)childObject;
			}

			long parentFolderId = (long)parentContainerId;

			if ((childFolder.getFolderId() == parentFolderId) ||
				((parentFolderId !=
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) &&
				 !StringUtil.contains(
					 childFolder.getTreePath(), String.valueOf(parentFolderId),
					 "/"))) {

				return null;
			}

			if (childFolder.getParentFolderId() == parentFolderId) {
				return childFolder;
			}

			List<Long> ancestorFolderIds = childFolder.getAncestorFolderIds();

			if (parentFolderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
				return get(ancestorFolderIds.get(ancestorFolderIds.size() - 1));
			}

			if (ancestorFolderIds.contains(parentFolderId)) {
				return get(
					ancestorFolderIds.get(
						ancestorFolderIds.indexOf(parentFolderId) - 1));
			}
		}
		catch (PortalException pe) {
			_log.error(pe, pe);
		}

		return null;
	}

	@Override
	public boolean isUserOwned(DLFolder dlFolder, long userId) {
		if (dlFolder.getUserId() == userId) {
			return true;
		}

		return false;
	}

	@Reference
	protected Portal portal;

	private static final Log _log = LogFactoryUtil.getLog(
		DLFolderUADDisplay.class);

}