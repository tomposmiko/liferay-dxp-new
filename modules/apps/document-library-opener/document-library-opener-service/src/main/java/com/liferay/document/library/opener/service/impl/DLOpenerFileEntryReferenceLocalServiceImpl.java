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

package com.liferay.document.library.opener.service.impl;

import com.liferay.document.library.opener.model.DLOpenerFileEntryReference;
import com.liferay.document.library.opener.service.base.DLOpenerFileEntryReferenceLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.UserLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	property = "model.class.name=com.liferay.document.library.opener.model.DLOpenerFileEntryReference",
	service = AopService.class
)
public class DLOpenerFileEntryReferenceLocalServiceImpl
	extends DLOpenerFileEntryReferenceLocalServiceBaseImpl {

	@Override
	public DLOpenerFileEntryReference addDLOpenerFileEntryReference(
			long userId, String referenceKey, FileEntry fileEntry, int type)
		throws PortalException {

		return _addDLOpenerFileEntryReference(
			userId, referenceKey, fileEntry, type);
	}

	@Override
	public DLOpenerFileEntryReference addPlaceholderDLOpenerFileEntryReference(
			long userId, FileEntry fileEntry, int type)
		throws PortalException {

		return _addDLOpenerFileEntryReference(userId, null, fileEntry, type);
	}

	@Override
	public void deleteDLOpenerFileEntryReference(FileEntry fileEntry)
		throws PortalException {

		DLOpenerFileEntryReference dlOpenerFileEntryReference =
			dlOpenerFileEntryReferencePersistence.findByFileEntryId(
				fileEntry.getFileEntryId());

		dlOpenerFileEntryReferenceLocalService.deleteDLOpenerFileEntryReference(
			dlOpenerFileEntryReference);
	}

	@Override
	public DLOpenerFileEntryReference fetchDLOpenerFileEntryReference(
		FileEntry fileEntry) {

		return dlOpenerFileEntryReferencePersistence.fetchByFileEntryId(
			fileEntry.getFileEntryId());
	}

	@Override
	public DLOpenerFileEntryReference getDLOpenerFileEntryReference(
			FileEntry fileEntry)
		throws PortalException {

		return dlOpenerFileEntryReferencePersistence.findByFileEntryId(
			fileEntry.getFileEntryId());
	}

	@Override
	public DLOpenerFileEntryReference updateDLOpenerFileEntryReference(
		String referenceKey, FileEntry fileEntry) {

		DLOpenerFileEntryReference dlOpenerFileEntryReference =
			dlOpenerFileEntryReferencePersistence.fetchByFileEntryId(
				fileEntry.getFileEntryId());

		dlOpenerFileEntryReference.setReferenceKey(referenceKey);

		return dlOpenerFileEntryReferencePersistence.update(
			dlOpenerFileEntryReference);
	}

	private DLOpenerFileEntryReference _addDLOpenerFileEntryReference(
			long userId, String referenceKey, FileEntry fileEntry, int type)
		throws PortalException {

		User user = _userLocalService.getUser(userId);

		long dlOpenerFileEntryReferenceId = counterLocalService.increment();

		DLOpenerFileEntryReference dlOpenerFileEntryReference =
			dlOpenerFileEntryReferenceLocalService.
				createDLOpenerFileEntryReference(dlOpenerFileEntryReferenceId);

		dlOpenerFileEntryReference.setGroupId(fileEntry.getGroupId());
		dlOpenerFileEntryReference.setCompanyId(fileEntry.getCompanyId());
		dlOpenerFileEntryReference.setUserId(user.getUserId());
		dlOpenerFileEntryReference.setUserName(user.getFullName());
		dlOpenerFileEntryReference.setReferenceKey(referenceKey);
		dlOpenerFileEntryReference.setFileEntryId(fileEntry.getFileEntryId());
		dlOpenerFileEntryReference.setType(type);

		return dlOpenerFileEntryReferencePersistence.update(
			dlOpenerFileEntryReference);
	}

	@Reference
	private UserLocalService _userLocalService;

}