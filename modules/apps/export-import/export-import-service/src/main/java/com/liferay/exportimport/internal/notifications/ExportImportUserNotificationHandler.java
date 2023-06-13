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

package com.liferay.exportimport.internal.notifications;

import com.liferay.exportimport.constants.ExportImportPortletKeys;
import com.liferay.exportimport.kernel.configuration.constants.ExportImportConfigurationConstants;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.exportimport.kernel.service.ExportImportConfigurationLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.background.task.model.BackgroundTask;
import com.liferay.portal.background.task.service.BackgroundTaskLocalService;
import com.liferay.portal.kernel.backgroundtask.constants.BackgroundTaskConstants;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplayFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.notifications.BaseUserNotificationHandler;
import com.liferay.portal.kernel.notifications.UserNotificationHandler;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.Portal;

import java.util.Locale;

import javax.portlet.PortletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Akos Thurzo
 */
@Component(
	property = "javax.portlet.name=" + ExportImportPortletKeys.EXPORT_IMPORT,
	service = UserNotificationHandler.class
)
public class ExportImportUserNotificationHandler
	extends BaseUserNotificationHandler {

	public ExportImportUserNotificationHandler() {
		setOpenDialog(true);
		setPortletId(ExportImportPortletKeys.EXPORT_IMPORT);
	}

	@Override
	protected String getBody(
			UserNotificationEvent userNotificationEvent,
			ServiceContext serviceContext)
		throws Exception {

		return _getMessage(userNotificationEvent, serviceContext);
	}

	@Override
	protected String getLink(
			UserNotificationEvent userNotificationEvent,
			ServiceContext serviceContext)
		throws Exception {

		JSONObject jsonObject = _jsonFactory.createJSONObject(
			userNotificationEvent.getPayload());

		long backgroundTaskId = jsonObject.getLong("backgroundTaskId");

		BackgroundTask backgroundTask =
			_backgroundTaskLocalService.fetchBackgroundTask(backgroundTaskId);

		if (backgroundTask == null) {
			return StringPool.BLANK;
		}

		return PortletURLBuilder.create(
			PortletURLFactoryUtil.create(
				serviceContext.getRequest(),
				ExportImportPortletKeys.EXPORT_IMPORT,
				PortletRequest.RENDER_PHASE)
		).setMVCPath(
			"/view_export_import.jsp"
		).setBackURL(
			serviceContext.getCurrentURL()
		).setParameter(
			"backgroundTaskId", backgroundTaskId
		).buildString();
	}

	@Override
	protected String getTitle(
			UserNotificationEvent userNotificationEvent,
			ServiceContext serviceContext)
		throws Exception {

		return _getMessage(userNotificationEvent, serviceContext);
	}

	private String _getMessage(
			UserNotificationEvent userNotificationEvent,
			ServiceContext serviceContext)
		throws Exception {

		Locale locale = _portal.getLocale(serviceContext.getRequest());

		JSONObject jsonObject = _jsonFactory.createJSONObject(
			userNotificationEvent.getPayload());

		ExportImportConfiguration exportImportConfiguration = null;

		try {
			exportImportConfiguration =
				_exportImportConfigurationLocalService.
					getExportImportConfiguration(
						jsonObject.getLong("exportImportConfigurationId"));
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return _language.get(
				locale,
				"the-process-referenced-by-this-notification-does-not-exist");
		}

		String typeLabel = ExportImportConfigurationConstants.getTypeLabel(
			exportImportConfiguration.getType());

		String message = "x-" + typeLabel;

		int status = jsonObject.getInt("status");

		if (status == BackgroundTaskConstants.STATUS_SUCCESSFUL) {
			message += "-process-finished-successfully";
		}
		else if (status == BackgroundTaskConstants.STATUS_FAILED) {
			message += "-process-failed";
		}
		else {
			return "Unable to process notification: " +
				_html.escape(jsonObject.toString());
		}

		long backgroundTaskId = jsonObject.getLong("backgroundTaskId");

		BackgroundTaskDisplay backgroundTaskDisplay =
			_backgroundTaskDisplayFactory.getBackgroundTaskDisplay(
				backgroundTaskId);

		String processName = backgroundTaskDisplay.getDisplayName(
			serviceContext.getRequest());

		return _language.format(locale, message, processName);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ExportImportUserNotificationHandler.class);

	@Reference
	private BackgroundTaskDisplayFactory _backgroundTaskDisplayFactory;

	@Reference
	private BackgroundTaskLocalService _backgroundTaskLocalService;

	@Reference
	private ExportImportConfigurationLocalService
		_exportImportConfigurationLocalService;

	@Reference
	private Html _html;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

}