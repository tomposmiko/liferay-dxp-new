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

package com.liferay.document.library.web.internal.portlet.action;

import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.document.library.repository.authorization.capability.AuthorizationCapability;
import com.liferay.document.library.web.internal.constants.DLWebKeys;
import com.liferay.document.library.web.internal.portlet.toolbar.contributor.DLPortletToolbarContributorRegistry;
import com.liferay.document.library.web.internal.util.DLTrashUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderConstants;
import com.liferay.portal.kernel.repository.Repository;
import com.liferay.portal.kernel.repository.RepositoryProviderUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Iván Zaera
 */
@Component(
	property = {
		"javax.portlet.name=" + DLPortletKeys.DOCUMENT_LIBRARY,
		"javax.portlet.name=" + DLPortletKeys.DOCUMENT_LIBRARY_ADMIN,
		"mvc.command.name=/", "mvc.command.name=/document_library/view",
		"mvc.command.name=/document_library/view_folder"
	},
	service = MVCRenderCommand.class
)
public class DLViewMVCRenderCommand extends GetFolderMVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		try {
			renderRequest.setAttribute(
				DLWebKeys.DOCUMENT_LIBRARY_PORTLET_TOOLBAR_CONTRIBUTOR,
				_dlPortletToolbarContributorRegistry.
					getDLPortletToolbarContributor());

			if (_pingFolderRepository(renderRequest, renderResponse)) {
				return MVCRenderConstants.MVC_PATH_VALUE_SKIP_DISPATCH;
			}

			return super.render(renderRequest, renderResponse);
		}
		catch (PortalException pe) {
			SessionErrors.add(renderRequest, "repositoryPingFailed", pe);

			return "/document_library/error.jsp";
		}
		catch (IOException ioe) {
			throw new PortletException(ioe);
		}
	}

	@Override
	protected DLTrashUtil getDLTrashUtil() {
		return _dlTrashUtil;
	}

	@Override
	protected String getPath() {
		return "/document_library/view.jsp";
	}

	private boolean _pingFolderRepository(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortalException {

		String mvcRenderCommandName = ParamUtil.getString(
			renderRequest, "mvcRenderCommandName");

		if (!mvcRenderCommandName.equals("/document_library/view_folder")) {
			return false;
		}

		long folderId = ParamUtil.getLong(renderRequest, "folderId");

		if (folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			return false;
		}

		DLFolder dlFolder = _dlFolderLocalService.fetchDLFolder(folderId);

		if ((dlFolder == null) || !dlFolder.isMountPoint()) {
			return false;
		}

		Repository repository = RepositoryProviderUtil.getRepository(
			dlFolder.getRepositoryId());

		if (repository.isCapabilityProvided(AuthorizationCapability.class)) {
			AuthorizationCapability authorizationCapability =
				repository.getCapability(AuthorizationCapability.class);

			authorizationCapability.authorize(renderRequest, renderResponse);

			return authorizationCapability.hasCustomRedirectFlow(
				renderRequest, renderResponse);
		}

		_dlAppService.getFileEntriesCount(
			dlFolder.getRepositoryId(), dlFolder.getFolderId());

		return false;
	}

	@Reference
	private DLAppService _dlAppService;

	@Reference
	private DLFolderLocalService _dlFolderLocalService;

	@Reference
	private DLPortletToolbarContributorRegistry
		_dlPortletToolbarContributorRegistry;

	@Reference
	private DLTrashUtil _dlTrashUtil;

}