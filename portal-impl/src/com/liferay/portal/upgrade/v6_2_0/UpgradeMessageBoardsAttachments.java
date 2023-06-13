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

package com.liferay.portal.upgrade.v6_2_0;

import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.StringBundler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * @author Eudaldo Alonso
 */
public class UpgradeMessageBoardsAttachments
	extends BaseAttachmentsUpgradeProcess {

	@Override
	protected String getClassName() {
		return "com.liferay.portlet.messageboards.model.MBMessage";
	}

	@Override
	protected long getContainerModelFolderId(
			long groupId, long companyId, long resourcePrimKey,
			long containerModelId, long userId, String userName,
			Timestamp createDate)
		throws Exception {

		long repositoryId = getRepositoryId(
			groupId, companyId, userId, userName, createDate, getClassNameId(),
			getPortletId());

		long repositoryFolderId = getFolderId(
			groupId, companyId, userId, userName, createDate, repositoryId,
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, getPortletId(), false);

		long threadFolderId = getFolderId(
			groupId, companyId, userId, userName, createDate, repositoryId,
			repositoryFolderId, String.valueOf(containerModelId), false);

		return getFolderId(
			groupId, companyId, userId, userName, createDate, repositoryId,
			threadFolderId, String.valueOf(resourcePrimKey), false);
	}

	@Override
	protected String getDirName(long containerModelId, long resourcePrimKey) {
		return StringBundler.concat(
			"messageboards/", String.valueOf(containerModelId), "/",
			String.valueOf(resourcePrimKey));
	}

	@Override
	protected String getPortletId() {
		return "19";
	}

	@Override
	protected void updateAttachments() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer();
			PreparedStatement preparedStatement = connection.prepareStatement(
				"select messageId, groupId, companyId, userId, userName, " +
					"threadId from MBMessage where classNameId = 0 and " +
						"classPK = 0");
			ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				long messageId = resultSet.getLong("messageId");
				long groupId = resultSet.getLong("groupId");
				long companyId = resultSet.getLong("companyId");
				long userId = resultSet.getLong("userId");
				String userName = resultSet.getString("userName");
				long threadId = resultSet.getLong("threadId");

				updateEntryAttachments(
					companyId, groupId, messageId, threadId, userId, userName);
			}
		}
	}

}