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

package com.liferay.document.library.opener.google.drive.web.internal.portlet.action;

import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.kernel.model.DLVersionNumberIncrease;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.opener.google.drive.DLOpenerGoogleDriveFileReference;
import com.liferay.document.library.opener.google.drive.DLOpenerGoogleDriveManager;
import com.liferay.document.library.opener.google.drive.constants.DLOpenerGoogleDriveMimeTypes;
import com.liferay.document.library.opener.google.drive.upload.UniqueFileEntryTitleProvider;
import com.liferay.document.library.opener.google.drive.web.internal.constants.DLOpenerGoogleDriveWebConstants;
import com.liferay.document.library.opener.google.drive.web.internal.constants.DLOpenerGoogleDriveWebKeys;
import com.liferay.document.library.opener.google.drive.web.internal.util.OAuth2Helper;
import com.liferay.document.library.opener.google.drive.web.internal.util.State;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	property = {
		"javax.portlet.name=" + DLPortletKeys.DOCUMENT_LIBRARY,
		"javax.portlet.name=" + DLPortletKeys.DOCUMENT_LIBRARY_ADMIN,
		"mvc.command.name=/document_library/edit_in_google_docs"
	},
	service = MVCActionCommand.class
)
public class EditInGoogleDriveMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		try {
			if (_dlOpenerGoogleDriveManager.hasValidCredential(
					themeDisplay.getCompanyId(), themeDisplay.getUserId())) {

				long fileEntryId = ParamUtil.getLong(
					actionRequest, "fileEntryId");

				_executeCommand(actionRequest, fileEntryId);
			}
			else {
				_performAuthorizationFlow(actionRequest, actionResponse);
			}
		}
		catch (PortalException pe) {
			SessionErrors.add(actionRequest, pe.getClass());
		}
	}

	private DLOpenerGoogleDriveFileReference _addGoogleDriveFileEntry(
			long repositoryId, long folderId, String contentType,
			ServiceContext serviceContext)
		throws PortalException {

		String title = _uniqueFileEntryTitleProvider.provide(
			serviceContext.getScopeGroupId(), folderId,
			serviceContext.getLocale());

		FileEntry fileEntry = _dlAppService.addFileEntry(
			repositoryId, folderId, null, contentType, title, StringPool.BLANK,
			StringPool.BLANK, new byte[0], serviceContext);

		_dlAppService.checkOutFileEntry(
			fileEntry.getFileEntryId(), serviceContext);

		return _dlOpenerGoogleDriveManager.create(
			serviceContext.getUserId(), fileEntry);
	}

	private DLOpenerGoogleDriveFileReference _checkOutGoogleDriveFileEntry(
			long fileEntryId, ServiceContext serviceContext)
		throws PortalException {

		_dlAppService.checkOutFileEntry(fileEntryId, serviceContext);

		return _dlOpenerGoogleDriveManager.checkOut(
			serviceContext.getUserId(),
			_dlAppService.getFileEntry(fileEntryId));
	}

	private void _executeCommand(ActionRequest actionRequest, long fileEntryId)
		throws PortalException {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		if (cmd.equals(DLOpenerGoogleDriveWebConstants.GOOGLE_DRIVE_ADD)) {
			try {
				long repositoryId = ParamUtil.getLong(
					actionRequest, "repositoryId");
				long folderId = ParamUtil.getLong(actionRequest, "folderId");
				String contentType = ParamUtil.getString(
					actionRequest, "contentType",
					DLOpenerGoogleDriveMimeTypes.APPLICATION_VND_DOCX);

				ServiceContext serviceContext =
					ServiceContextFactory.getInstance(actionRequest);

				_saveDLOpenerGoogleDriveFileReference(
					actionRequest,
					TransactionInvokerUtil.invoke(
						_transactionConfig,
						() -> _addGoogleDriveFileEntry(
							repositoryId, folderId, contentType,
							serviceContext)));

				hideDefaultSuccessMessage(actionRequest);
			}
			catch (PortalException pe) {
				throw pe;
			}
			catch (Throwable throwable) {
				throw new PortalException(throwable);
			}
		}
		else if (cmd.equals(
					DLOpenerGoogleDriveWebConstants.
						GOOGLE_DRIVE_CANCEL_CHECKOUT)) {

			_dlAppService.cancelCheckOut(fileEntryId);
		}
		else if (cmd.equals(
					DLOpenerGoogleDriveWebConstants.GOOGLE_DRIVE_CHECKIN)) {

			DLVersionNumberIncrease dlVersionNumberIncrease =
				DLVersionNumberIncrease.valueOf(
					ParamUtil.getString(actionRequest, "versionIncrease"),
					DLVersionNumberIncrease.AUTOMATIC);

			String changeLog = ParamUtil.getString(actionRequest, "changeLog");

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				actionRequest);

			_dlAppService.checkInFileEntry(
				fileEntryId, dlVersionNumberIncrease, changeLog,
				serviceContext);
		}
		else if (cmd.equals(
					DLOpenerGoogleDriveWebConstants.GOOGLE_DRIVE_CHECKOUT)) {

			try {
				ServiceContext serviceContext =
					ServiceContextFactory.getInstance(actionRequest);

				_saveDLOpenerGoogleDriveFileReference(
					actionRequest,
					TransactionInvokerUtil.invoke(
						_transactionConfig,
						() -> _checkOutGoogleDriveFileEntry(
							fileEntryId, serviceContext)));

				hideDefaultSuccessMessage(actionRequest);
			}
			catch (PortalException pe) {
				throw pe;
			}
			catch (Throwable throwable) {
				throw new PortalException(throwable);
			}
		}
		else if (cmd.equals(
					DLOpenerGoogleDriveWebConstants.GOOGLE_DRIVE_EDIT)) {

			ThemeDisplay themeDisplay =
				(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

			_saveDLOpenerGoogleDriveFileReference(
				actionRequest,
				_dlOpenerGoogleDriveManager.requestEditAccess(
					themeDisplay.getUserId(),
					_dlAppService.getFileEntry(fileEntryId)));
		}
		else {
			throw new IllegalArgumentException();
		}
	}

	private String _getFailureURL(PortletRequest portletRequest)
		throws PortalException {

		LiferayPortletURL liferayPortletURL = PortletURLFactoryUtil.create(
			portletRequest, _portal.getPortletId(portletRequest),
			_portal.getControlPanelPlid(portletRequest),
			PortletRequest.RENDER_PHASE);

		return liferayPortletURL.toString();
	}

	private String _getSuccessURL(PortletRequest portletRequest) {
		return _portal.getCurrentCompleteURL(
			_portal.getHttpServletRequest(portletRequest));
	}

	private void _performAuthorizationFlow(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String state = StringUtil.randomString(5);

		State.save(
			_portal.getOriginalServletRequest(
				_portal.getHttpServletRequest(actionRequest)),
			themeDisplay.getUserId(), _getSuccessURL(actionRequest),
			_getFailureURL(actionRequest), state);

		actionResponse.sendRedirect(
			_dlOpenerGoogleDriveManager.getAuthorizationURL(
				themeDisplay.getCompanyId(), state,
				_oAuth2Helper.getRedirectURI(actionRequest)));
	}

	private void _saveDLOpenerGoogleDriveFileReference(
		PortletRequest portletRequest,
		DLOpenerGoogleDriveFileReference dlOpenerGoogleDriveFileReference) {

		portletRequest.setAttribute(
			DLOpenerGoogleDriveWebKeys.DL_OPENER_GOOGLE_DRIVE_FILE_REFERENCE,
			dlOpenerGoogleDriveFileReference);
	}

	@Reference
	private DLAppService _dlAppService;

	@Reference
	private DLOpenerGoogleDriveManager _dlOpenerGoogleDriveManager;

	@Reference
	private OAuth2Helper _oAuth2Helper;

	@Reference
	private Portal _portal;

	private final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Propagation.REQUIRED, new Class<?>[] {Exception.class});

	@Reference
	private UniqueFileEntryTitleProvider _uniqueFileEntryTitleProvider;

}