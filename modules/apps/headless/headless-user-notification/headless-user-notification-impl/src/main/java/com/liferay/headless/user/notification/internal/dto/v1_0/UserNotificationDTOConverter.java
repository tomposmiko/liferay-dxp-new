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

package com.liferay.headless.user.notification.internal.dto.v1_0;

import com.liferay.headless.user.notification.dto.v1_0.UserNotification;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.notifications.UserNotificationFeedEntry;
import com.liferay.portal.kernel.notifications.UserNotificationHandler;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserNotificationEventLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;

import java.util.Date;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos Correa
 */
@Component(
	property = "dto.class.name=com.liferay.portal.kernel.model.UserNotificationEvent",
	service = {DTOConverter.class, UserNotificationDTOConverter.class}
)
public class UserNotificationDTOConverter
	implements DTOConverter<UserNotificationEvent, UserNotification> {

	@Override
	public String getContentType() {
		return UserNotification.class.getSimpleName();
	}

	@Override
	public UserNotificationEvent getObject(String externalReferenceCode)
		throws Exception {

		return _userNotificationEventLocalService.getUserNotificationEvent(
			GetterUtil.getLong(externalReferenceCode));
	}

	@Override
	public UserNotification toDTO(
			DTOConverterContext dtoConverterContext,
			UserNotificationEvent userNotificationEvent)
		throws Exception {

		JSONObject jsonObject = _jsonFactory.createJSONObject(
			userNotificationEvent.getPayload());

		return new UserNotification() {
			{
				dateCreated = new Date(userNotificationEvent.getTimestamp());
				id = userNotificationEvent.getUserNotificationEventId();
				message = _getNotificationMessage(
					dtoConverterContext, userNotificationEvent);
				read = userNotificationEvent.isArchived();

				setActions(
					()-> {
						if (dtoConverterContext == null) {
							return null;
						}

						return dtoConverterContext.getActions();
					});
				setType(
					()-> {
						if (!jsonObject.has("notificationType")) {
							return null;
						}

						return jsonObject.getInt("notificationType");
					});
			}
		};
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, UserNotificationHandler.class, "javax.portlet.name");
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	private String _getNotificationMessage(
			DTOConverterContext dtoConverterContext,
			UserNotificationEvent userNotificationEvent)
		throws Exception {

		UserNotificationHandler userNotificationHandler =
			_serviceTrackerMap.getService(userNotificationEvent.getType());

		if (userNotificationHandler == null) {
			return null;
		}

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			dtoConverterContext.getHttpServletRequest());

		serviceContext.setLanguageId(
			_language.getLanguageId(dtoConverterContext.getLocale()));

		UserNotificationFeedEntry userNotificationFeedEntry =
			userNotificationHandler.interpret(
				userNotificationEvent, serviceContext);

		if (userNotificationFeedEntry == null) {
			return null;
		}

		return userNotificationFeedEntry.getTitle();
	}

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	private ServiceTrackerMap<String, UserNotificationHandler>
		_serviceTrackerMap;

	@Reference
	private UserNotificationEventLocalService
		_userNotificationEventLocalService;

}