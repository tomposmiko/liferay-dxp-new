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

package com.liferay.document.library.opener.google.drive.web.internal;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;

import com.liferay.document.library.opener.constants.DLOpenerFileEntryReferenceConstants;
import com.liferay.document.library.opener.google.drive.DLOpenerGoogleDriveFileReference;
import com.liferay.document.library.opener.google.drive.DLOpenerGoogleDriveManager;
import com.liferay.document.library.opener.google.drive.web.internal.background.task.UploadGoogleDriveDocumentBackgroundTaskExecutor;
import com.liferay.document.library.opener.google.drive.web.internal.constants.GoogleDriveBackgroundTaskConstants;
import com.liferay.document.library.opener.model.DLOpenerFileEntryReference;
import com.liferay.document.library.opener.service.DLOpenerFileEntryReferenceLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.background.task.constants.BackgroundTaskContextMapConstants;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManager;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import java.security.GeneralSecurityException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(immediate = true, service = DLOpenerGoogleDriveManager.class)
public class DLOpenerGoogleDriveManagerImpl
	implements DLOpenerGoogleDriveManager {

	@Override
	public DLOpenerGoogleDriveFileReference checkOut(
			long userId, FileEntry fileEntry)
		throws PortalException {

		final String jobName =
			"googleDriveFileEntry-" + fileEntry.getFileEntryId();

		Map<String, Serializable> taskContextMap = new HashMap<>();

		taskContextMap.put(
			GoogleDriveBackgroundTaskConstants.CMD,
			GoogleDriveBackgroundTaskConstants.CHECKOUT);
		taskContextMap.put(
			GoogleDriveBackgroundTaskConstants.COMPANY_ID,
			fileEntry.getCompanyId());
		taskContextMap.put(
			BackgroundTaskContextMapConstants.DELETE_ON_SUCCESS, true);
		taskContextMap.put(
			GoogleDriveBackgroundTaskConstants.FILE_ENTRY_ID,
			fileEntry.getFileEntryId());
		taskContextMap.put(GoogleDriveBackgroundTaskConstants.USER_ID, userId);

		BackgroundTask backgroundTask =
			_backgroundTaskManager.addBackgroundTask(
				userId, CompanyConstants.SYSTEM, jobName,
				UploadGoogleDriveDocumentBackgroundTaskExecutor.class.getName(),
				taskContextMap, new ServiceContext());

		_dlOpenerFileEntryReferenceLocalService.
			addPlaceholderDLOpenerFileEntryReference(
				userId, fileEntry,
				DLOpenerFileEntryReferenceConstants.TYPE_EDIT);

		return new DLOpenerGoogleDriveFileReference(
			fileEntry.getFileEntryId(),
			new CachingSupplier<>(
				() -> _getGoogleDriveFileTitle(userId, fileEntry)),
			() -> _getContentFile(userId, fileEntry),
			backgroundTask.getBackgroundTaskId());
	}

	@Override
	public DLOpenerGoogleDriveFileReference create(
			long userId, FileEntry fileEntry)
		throws PortalException {

		final String jobName =
			"googleDriveFileEntry-" + fileEntry.getFileEntryId();

		Map<String, Serializable> taskContextMap = new HashMap<>();

		taskContextMap.put(
			GoogleDriveBackgroundTaskConstants.CMD,
			GoogleDriveBackgroundTaskConstants.CREATE);
		taskContextMap.put(
			GoogleDriveBackgroundTaskConstants.COMPANY_ID,
			fileEntry.getCompanyId());
		taskContextMap.put(
			BackgroundTaskContextMapConstants.DELETE_ON_SUCCESS, true);
		taskContextMap.put(
			GoogleDriveBackgroundTaskConstants.FILE_ENTRY_ID,
			fileEntry.getFileEntryId());
		taskContextMap.put(GoogleDriveBackgroundTaskConstants.USER_ID, userId);

		BackgroundTask backgroundTask =
			_backgroundTaskManager.addBackgroundTask(
				userId, CompanyConstants.SYSTEM, jobName,
				UploadGoogleDriveDocumentBackgroundTaskExecutor.class.getName(),
				taskContextMap, new ServiceContext());

		_dlOpenerFileEntryReferenceLocalService.
			addPlaceholderDLOpenerFileEntryReference(
				userId, fileEntry,
				DLOpenerFileEntryReferenceConstants.TYPE_NEW);

		return new DLOpenerGoogleDriveFileReference(
			fileEntry.getFileEntryId(),
			new CachingSupplier<>(
				() -> _getGoogleDriveFileTitle(userId, fileEntry)),
			() -> _getContentFile(userId, fileEntry),
			backgroundTask.getBackgroundTaskId());
	}

	@Override
	public void delete(long userId, FileEntry fileEntry)
		throws PortalException {

		try {
			Drive drive = new Drive.Builder(
				_netHttpTransport, _jsonFactory,
				_getCredential(fileEntry.getCompanyId(), userId)
			).build();

			Drive.Files driveFiles = drive.files();

			Drive.Files.Delete driveFilesDelete = driveFiles.delete(
				_getGoogleDriveFileId(fileEntry));

			driveFilesDelete.execute();

			_dlOpenerFileEntryReferenceLocalService.
				deleteDLOpenerFileEntryReference(fileEntry);
		}
		catch (IOException ioe) {
			throw new PortalException(ioe);
		}
	}

	@Override
	public String getAuthorizationURL(
			long companyId, String state, String redirectUri)
		throws PortalException {

		return _oAuth2Manager.getAuthorizationURL(
			companyId, state, redirectUri);
	}

	@Override
	public boolean hasValidCredential(long companyId, long userId)
		throws IOException, PortalException {

		Credential credential = _oAuth2Manager.getCredential(companyId, userId);

		if (credential == null) {
			return false;
		}

		if ((credential.getExpiresInSeconds() <= 0) &&
			!credential.refreshToken()) {

			return false;
		}

		return true;
	}

	@Override
	public boolean isConfigured(long companyId) {
		return _oAuth2Manager.isConfigured(companyId);
	}

	@Override
	public boolean isGoogleDriveFile(FileEntry fileEntry) {
		DLOpenerFileEntryReference dlOpenerFileEntryReference =
			_dlOpenerFileEntryReferenceLocalService.
				fetchDLOpenerFileEntryReference(fileEntry);

		if (dlOpenerFileEntryReference == null) {
			return false;
		}

		return true;
	}

	@Override
	public void requestAuthorizationToken(
			long companyId, long userId, String code, String redirectUri)
		throws IOException, PortalException {

		_oAuth2Manager.requestAuthorizationToken(
			companyId, userId, code, redirectUri);
	}

	@Override
	public DLOpenerGoogleDriveFileReference requestEditAccess(
			long userId, FileEntry fileEntry)
		throws PortalException {

		String googleDriveFileId = _getGoogleDriveFileId(fileEntry);

		if (Validator.isNull(googleDriveFileId)) {
			throw new IllegalArgumentException(
				StringBundler.concat(
					"File entry ", fileEntry.getFileEntryId(),
					" is not a Google Drive file"));
		}

		_checkCredential(fileEntry.getCompanyId(), userId);

		return new DLOpenerGoogleDriveFileReference(
			fileEntry.getFileEntryId(),
			new CachingSupplier<>(
				() -> _getGoogleDriveFileTitle(userId, fileEntry)),
			() -> _getContentFile(userId, fileEntry), 0);
	}

	@Activate
	protected void activate() throws GeneralSecurityException, IOException {
		_jsonFactory = JacksonFactory.getDefaultInstance();
		_netHttpTransport = GoogleNetHttpTransport.newTrustedTransport();
	}

	private void _checkCredential(long companyId, long userId)
		throws PortalException {

		_getCredential(companyId, userId);
	}

	private File _getContentFile(long userId, FileEntry fileEntry) {
		try {
			Drive drive = new Drive.Builder(
				_netHttpTransport, _jsonFactory,
				_getCredential(fileEntry.getCompanyId(), userId)
			).build();

			Drive.Files driveFiles = drive.files();

			Drive.Files.Export driveFilesExport = driveFiles.export(
				_getGoogleDriveFileId(fileEntry), fileEntry.getMimeType());

			try (InputStream is =
					driveFilesExport.executeMediaAsInputStream()) {

				return FileUtil.createTempFile(is);
			}
		}
		catch (IOException | PortalException e) {
			throw new RuntimeException(e);
		}
	}

	private Credential _getCredential(long companyId, long userId)
		throws PortalException {

		Credential credential = _oAuth2Manager.getCredential(companyId, userId);

		if (credential == null) {
			throw new PrincipalException(
				StringBundler.concat(
					"User ", userId,
					" does not have a valid Google credential"));
		}

		return credential;
	}

	private String _getGoogleDriveFileId(FileEntry fileEntry)
		throws PortalException {

		DLOpenerFileEntryReference dlOpenerFileEntryReference =
			_dlOpenerFileEntryReferenceLocalService.
				getDLOpenerFileEntryReference(fileEntry);

		return dlOpenerFileEntryReference.getReferenceKey();
	}

	private String _getGoogleDriveFileTitle(long userId, FileEntry fileEntry) {
		try {
			Drive drive = new Drive.Builder(
				_netHttpTransport, _jsonFactory,
				_getCredential(fileEntry.getCompanyId(), userId)
			).build();

			Drive.Files driveFiles = drive.files();

			Drive.Files.Get driveFilesGet = driveFiles.get(
				_getGoogleDriveFileId(fileEntry));

			com.google.api.services.drive.model.File file =
				driveFilesGet.execute();

			return file.getName();
		}
		catch (IOException | PortalException e) {
			throw new RuntimeException(e);
		}
	}

	@Reference
	private BackgroundTaskManager _backgroundTaskManager;

	@Reference
	private DLOpenerFileEntryReferenceLocalService
		_dlOpenerFileEntryReferenceLocalService;

	private JsonFactory _jsonFactory;
	private NetHttpTransport _netHttpTransport;

	@Reference
	private OAuth2Manager _oAuth2Manager;

	private static class CachingSupplier<T> implements Supplier<T> {

		public CachingSupplier(Supplier<T> supplier) {
			_supplier = supplier;
		}

		@Override
		public T get() {
			if (_value != null) {
				return _value;
			}

			_value = _supplier.get();

			return _value;
		}

		private final Supplier<T> _supplier;
		private T _value;

	}

}