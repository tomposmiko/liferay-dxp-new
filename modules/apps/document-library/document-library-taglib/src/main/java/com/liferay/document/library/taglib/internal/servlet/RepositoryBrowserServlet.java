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

package com.liferay.document.library.taglib.internal.servlet;

import com.liferay.document.library.kernel.exception.DuplicateFileEntryException;
import com.liferay.document.library.kernel.exception.DuplicateFolderNameException;
import com.liferay.document.library.kernel.model.DLVersionNumberIncrease;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileShortcut;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadServletRequest;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	property = {
		"osgi.http.whiteboard.servlet.name=com.liferay.document.library.taglib.internal.servlet.RepositoryBrowserServlet",
		"osgi.http.whiteboard.servlet.pattern=/repository_browser",
		"servlet.init.httpMethods=DELETE,POST,PUT"
	},
	service = Servlet.class
)
public class RepositoryBrowserServlet extends HttpServlet {

	@Override
	protected void doDelete(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		try {
			long fileEntryId = ParamUtil.getLong(
				httpServletRequest, "fileEntryId");

			if (fileEntryId != 0) {
				_dlAppService.deleteFileEntry(fileEntryId);
			}

			long fileShortcutId = ParamUtil.getLong(
				httpServletRequest, "fileShortcutId");

			if (fileShortcutId != 0) {
				_dlAppService.deleteFileShortcut(fileShortcutId);
			}

			long folderId = ParamUtil.getLong(httpServletRequest, "folderId");

			if (folderId != 0) {
				_dlAppService.deleteFolder(folderId);
			}

			long[] repositoryEntryIds = ParamUtil.getLongValues(
				httpServletRequest, "repositoryEntryIds");

			if ((repositoryEntryIds != null) &&
				(repositoryEntryIds.length > 0)) {

				for (long repositoryEntryId : repositoryEntryIds) {
					_deleteRepositoryEntry(repositoryEntryId);
				}
			}

			SessionMessages.add(
				httpServletRequest, "requestProcessed",
				"your-request-completed-successfully");

			_sendResponse(httpServletResponse, HttpServletResponse.SC_OK);
		}
		catch (PortalException portalException) {
			throw new ServletException(portalException);
		}
	}

	@Override
	protected void doPost(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		try {
			String name = ParamUtil.getString(httpServletRequest, "name");

			if (Validator.isNull(name)) {
				_sendResponse(
					httpServletResponse, HttpServletResponse.SC_BAD_REQUEST);

				return;
			}

			long fileEntryId = ParamUtil.getLong(
				httpServletRequest, "fileEntryId");

			if (fileEntryId != 0) {
				_dlAppService.updateFileEntry(
					fileEntryId, null, null, name, null, null, null,
					DLVersionNumberIncrease.NONE, (byte[])null, null, null,
					ServiceContextFactory.getInstance(
						FileEntry.class.getName(), httpServletRequest));
			}

			long folderId = ParamUtil.getLong(httpServletRequest, "folderId");

			if (folderId != 0) {
				_dlAppService.updateFolder(
					folderId, name, null,
					ServiceContextFactory.getInstance(
						Folder.class.getName(), httpServletRequest));
			}

			SessionMessages.add(
				httpServletRequest, "requestProcessed",
				"your-request-completed-successfully");

			_sendResponse(httpServletResponse, HttpServletResponse.SC_OK);
		}
		catch (DuplicateFileEntryException | DuplicateFolderNameException
					exception) {

			SessionErrors.add(httpServletRequest, exception.getClass());

			_sendResponse(httpServletResponse, HttpServletResponse.SC_CONFLICT);
		}
		catch (PortalException portalException) {
			throw new ServletException(portalException);
		}
	}

	@Override
	protected void doPut(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		try {
			long repositoryId = ParamUtil.getLong(
				httpServletRequest, "repositoryId");

			if (repositoryId <= 0) {
				_sendResponse(
					httpServletResponse, HttpServletResponse.SC_BAD_REQUEST);

				return;
			}

			UploadServletRequest uploadServletRequest =
				_portal.getUploadServletRequest(httpServletRequest);

			File file = uploadServletRequest.getFile("file");

			if (file != null) {
				long parentFolderId = ParamUtil.getLong(
					httpServletRequest, "parentFolderId");

				String sourceFileName = uploadServletRequest.getFileName(
					"file");

				String title = FileUtil.stripExtension(sourceFileName);

				_dlAppService.addFileEntry(
					null, repositoryId, parentFolderId, sourceFileName,
					uploadServletRequest.getContentType("file"), title, null,
					null, null, file, null, null,
					ServiceContextFactory.getInstance(
						FileEntry.class.getName(), httpServletRequest));

				SessionMessages.add(httpServletRequest, "requestProcessed");

				_sendResponse(httpServletResponse, HttpServletResponse.SC_OK);

				return;
			}

			String name = ParamUtil.getString(httpServletRequest, "name");

			if (Validator.isNull(name)) {
				httpServletResponse.sendError(
					HttpServletResponse.SC_BAD_REQUEST);

				return;
			}

			long parentFolderId = ParamUtil.getLong(
				httpServletRequest, "parentFolderId");

			_dlAppService.addFolder(
				null, repositoryId, parentFolderId, name, StringPool.BLANK,
				ServiceContextFactory.getInstance(
					Folder.class.getName(), httpServletRequest));

			SessionMessages.add(
				httpServletRequest, "requestProcessed",
				"your-request-completed-successfully");

			_sendResponse(httpServletResponse, HttpServletResponse.SC_OK);
		}
		catch (DuplicateFileEntryException | DuplicateFolderNameException
					exception) {

			SessionErrors.add(httpServletRequest, exception.getClass());

			_sendResponse(httpServletResponse, HttpServletResponse.SC_CONFLICT);
		}
		catch (PortalException portalException) {
			throw new ServletException(portalException);
		}
	}

	@Override
	protected void service(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException, ServletException {

		try {
			User user = _portal.getUser(httpServletRequest);

			if ((user == null) || user.isDefaultUser()) {
				throw new PrincipalException.MustBeAuthenticated(
					StringPool.BLANK);
			}

			PrincipalThreadLocal.setName(user.getUserId());

			PermissionThreadLocal.setPermissionChecker(
				_permissionCheckerFactory.create(user));

			super.service(httpServletRequest, httpServletResponse);
		}
		catch (IOException | PortalException | ServletException exception) {
			_log.error(exception);

			_sendResponse(
				httpServletResponse,
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void _deleteRepositoryEntry(long repositoryEntryId) {
		try {
			FileEntry fileEntry = _dlAppService.getFileEntry(repositoryEntryId);

			_dlAppService.deleteFileEntry(fileEntry.getFileEntryId());

			return;
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}

		try {
			FileShortcut fileShortcut = _dlAppService.getFileShortcut(
				repositoryEntryId);

			_dlAppService.deleteFileShortcut(fileShortcut.getFileShortcutId());

			return;
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}

		try {
			Folder folder = _dlAppService.getFolder(repositoryEntryId);

			_dlAppService.deleteFolder(folder.getFolderId());
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}
	}

	private void _sendResponse(
			HttpServletResponse httpServletResponse, int status)
		throws IOException {

		httpServletResponse.setContentType(ContentTypes.APPLICATION_JSON);
		httpServletResponse.setStatus(status);

		boolean success = false;

		if (status == HttpServletResponse.SC_OK) {
			success = true;
		}

		ServletResponseUtil.write(
			httpServletResponse,
			JSONUtil.put(
				"success", success
			).toString());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RepositoryBrowserServlet.class);

	@Reference
	private DLAppService _dlAppService;

	@Reference
	private PermissionCheckerFactory _permissionCheckerFactory;

	@Reference
	private Portal _portal;

}