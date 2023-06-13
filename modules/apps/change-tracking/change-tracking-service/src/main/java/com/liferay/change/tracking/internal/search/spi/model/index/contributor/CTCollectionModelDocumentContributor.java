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

package com.liferay.change.tracking.internal.search.spi.model.index.contributor;

import com.liferay.change.tracking.constants.CTDestinationNames;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.messaging.SchedulerResponse;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;

import java.util.Date;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author David Truong
 */
@Component(
	property = "indexer.class.name=com.liferay.change.tracking.model.CTCollection",
	service = ModelDocumentContributor.class
)
public class CTCollectionModelDocumentContributor
	implements ModelDocumentContributor<CTCollection> {

	@Override
	public void contribute(Document document, CTCollection ctCollection) {
		document.addDate(Field.CREATE_DATE, ctCollection.getCreateDate());
		document.addText(Field.DESCRIPTION, ctCollection.getDescription());
		document.addDate(Field.MODIFIED_DATE, ctCollection.getModifiedDate());
		document.addText(Field.NAME, ctCollection.getName());
		document.addKeyword(Field.STATUS, ctCollection.getStatus());

		User user = _userLocalService.fetchUser(ctCollection.getUserId());

		if (user != null) {
			document.addKeyword(Field.USER_ID, user.getUserId());
			document.addText(Field.USER_NAME, user.getFullName());
		}

		document.addDate("scheduledDate", _getScheduledDate(ctCollection));
	}

	private Date _getScheduledDate(CTCollection ctCollection) {
		if (ctCollection.getStatus() != WorkflowConstants.STATUS_SCHEDULED) {
			return null;
		}

		try {
			SchedulerResponse schedulerResponse =
				_schedulerEngineHelper.getScheduledJob(
					String.valueOf(ctCollection.getCtCollectionId()),
					CTDestinationNames.CT_COLLECTION_SCHEDULED_PUBLISH,
					StorageType.PERSISTED);

			if (schedulerResponse == null) {
				return null;
			}

			return _schedulerEngineHelper.getStartTime(schedulerResponse);
		}
		catch (SchedulerException schedulerException) {
			if (_log.isWarnEnabled()) {
				_log.warn(schedulerException);
			}

			return null;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CTCollectionModelDocumentContributor.class);

	@Reference
	private SchedulerEngineHelper _schedulerEngineHelper;

	@Reference
	private UserLocalService _userLocalService;

}