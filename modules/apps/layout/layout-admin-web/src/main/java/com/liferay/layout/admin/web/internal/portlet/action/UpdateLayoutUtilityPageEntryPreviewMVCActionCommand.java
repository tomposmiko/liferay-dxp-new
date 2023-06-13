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

package com.liferay.layout.admin.web.internal.portlet.action;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.utility.page.model.LayoutUtilityPageEntry;
import com.liferay.layout.utility.page.service.LayoutUtilityPageEntryService;
import com.liferay.portal.kernel.model.Repository;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepository;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.TempFileEntryUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	property = {
		"javax.portlet.name=" + LayoutAdminPortletKeys.GROUP_PAGES,
		"mvc.command.name=/layout_admin/update_layout_utility_page_entry_preview"
	},
	service = MVCActionCommand.class
)
public class UpdateLayoutUtilityPageEntryPreviewMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long layoutUtilityPageEntryId = ParamUtil.getLong(
			actionRequest, "layoutUtilityPageEntryId");

		long fileEntryId = ParamUtil.getLong(actionRequest, "fileEntryId");

		FileEntry fileEntry = _dlAppLocalService.getFileEntry(fileEntryId);

		Repository repository = _portletFileRepository.fetchPortletRepository(
			themeDisplay.getScopeGroupId(), LayoutAdminPortletKeys.GROUP_PAGES);

		if (repository == null) {
			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

			repository = _portletFileRepository.addPortletRepository(
				themeDisplay.getScopeGroupId(),
				LayoutAdminPortletKeys.GROUP_PAGES, serviceContext);
		}

		LayoutUtilityPageEntry layoutUtilityPageEntry =
			_layoutUtilityPageEntryService.fetchLayoutUtilityPageEntry(
				layoutUtilityPageEntryId);

		if (layoutUtilityPageEntry.getPreviewFileEntryId() > 0) {
			DLFileEntry oldDLFileEntry =
				_dlFileEntryLocalService.fetchDLFileEntry(
					layoutUtilityPageEntry.getPreviewFileEntryId());

			if (oldDLFileEntry != null) {
				_portletFileRepository.deletePortletFileEntry(
					oldDLFileEntry.getFileEntryId());
			}
		}

		String fileName =
			layoutUtilityPageEntryId + "_preview." + fileEntry.getExtension();

		fileEntry = _portletFileRepository.addPortletFileEntry(
			null, themeDisplay.getScopeGroupId(), themeDisplay.getUserId(),
			LayoutUtilityPageEntry.class.getName(), layoutUtilityPageEntryId,
			LayoutAdminPortletKeys.GROUP_PAGES, repository.getDlFolderId(),
			fileEntry.getContentStream(), fileName, fileEntry.getMimeType(),
			false);

		_layoutUtilityPageEntryService.updateLayoutUtilityPageEntry(
			layoutUtilityPageEntryId, fileEntry.getFileEntryId());

		TempFileEntryUtil.deleteTempFileEntry(fileEntryId);

		sendRedirect(actionRequest, actionResponse);
	}

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private DLFileEntryLocalService _dlFileEntryLocalService;

	@Reference
	private DLFolderLocalService _dlFolderLocalService;

	@Reference
	private LayoutUtilityPageEntryService _layoutUtilityPageEntryService;

	@Reference
	private PortletFileRepository _portletFileRepository;

}