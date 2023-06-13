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

package com.liferay.headless.delivery.internal.dto.v1_0.converter;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.headless.delivery.dto.v1_0.DocumentFolder;
import com.liferay.headless.delivery.dto.v1_0.converter.DTOConverter;
import com.liferay.headless.delivery.dto.v1_0.converter.DTOConverterContext;
import com.liferay.headless.delivery.internal.dto.v1_0.util.CreatorUtil;
import com.liferay.headless.delivery.internal.dto.v1_0.util.CustomFieldsUtil;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.subscription.service.SubscriptionLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Rubén Pulido
 */
@Component(
	property = "asset.entry.class.name=com.liferay.document.library.kernel.model.DLFolder",
	service = {DocumentFolderDTOConverter.class, DTOConverter.class}
)
public class DocumentFolderDTOConverter implements DTOConverter {

	@Override
	public String getContentType() {
		return DocumentFolder.class.getSimpleName();
	}

	@Override
	public DocumentFolder toDTO(DTOConverterContext dtoConverterContext)
		throws Exception {

		Folder folder = _dlAppService.getFolder(
			dtoConverterContext.getResourcePrimKey());

		return new DocumentFolder() {
			{
				creator = CreatorUtil.toCreator(
					_portal, _userLocalService.getUser(folder.getUserId()));
				customFields = CustomFieldsUtil.toCustomFields(
					DLFolder.class.getName(), folder.getFolderId(),
					folder.getCompanyId(), dtoConverterContext.getLocale());
				dateCreated = folder.getCreateDate();
				dateModified = folder.getModifiedDate();
				description = folder.getDescription();
				id = folder.getFolderId();
				name = folder.getName();
				numberOfDocumentFolders = _dlAppService.getFoldersCount(
					folder.getRepositoryId(), folder.getFolderId());
				numberOfDocuments = _dlAppService.getFileEntriesCount(
					folder.getRepositoryId(), folder.getFolderId());
				siteId = folder.getGroupId();
				subscribed = _subscriptionLocalService.isSubscribed(
					folder.getCompanyId(), dtoConverterContext.getUserId(),
					DLFolder.class.getName(), folder.getFolderId());
			}
		};
	}

	@Reference
	private DLAppService _dlAppService;

	@Reference
	private Portal _portal;

	@Reference
	private SubscriptionLocalService _subscriptionLocalService;

	@Reference
	private UserLocalService _userLocalService;

}