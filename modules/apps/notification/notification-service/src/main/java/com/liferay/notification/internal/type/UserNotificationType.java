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

package com.liferay.notification.internal.type;

import com.liferay.notification.constants.NotificationConstants;
import com.liferay.notification.constants.NotificationQueueEntryConstants;
import com.liferay.notification.context.NotificationContext;
import com.liferay.notification.internal.type.users.provider.UsersProvider;
import com.liferay.notification.internal.type.users.provider.UsersProviderTracker;
import com.liferay.notification.model.NotificationQueueEntry;
import com.liferay.notification.model.NotificationRecipient;
import com.liferay.notification.model.NotificationRecipientSetting;
import com.liferay.notification.model.NotificationTemplate;
import com.liferay.notification.type.BaseNotificationType;
import com.liferay.notification.type.NotificationType;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserNotificationDeliveryConstants;
import com.liferay.portal.kernel.service.UserNotificationEventLocalService;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Feliphe Marinho
 */
@Component(
	immediate = true,
	property = "notification.type.key=" + NotificationConstants.TYPE_USER_NOTIFICATION,
	service = NotificationType.class
)
public class UserNotificationType extends BaseNotificationType {

	@Override
	public List<NotificationRecipientSetting>
		createNotificationRecipientSettings(
			long notificationRecipientId, Object[] recipients, User user) {

		List<NotificationRecipientSetting> notificationRecipientSettings =
			new ArrayList<>();

		for (Object recipient : recipients) {
			Map<String, Object> recipientMap = (Map<String, Object>)recipient;

			for (Map.Entry<String, Object> entry : recipientMap.entrySet()) {
				NotificationRecipientSetting notificationRecipientSetting =
					notificationRecipientSettingLocalService.
						createNotificationRecipientSetting(0L);

				notificationRecipientSetting.setCompanyId(user.getCompanyId());
				notificationRecipientSetting.setUserId(user.getUserId());
				notificationRecipientSetting.setUserName(user.getFullName());

				notificationRecipientSetting.setNotificationRecipientId(
					notificationRecipientId);
				notificationRecipientSetting.setName(entry.getKey());
				notificationRecipientSetting.setValue(
					String.valueOf(entry.getValue()));

				notificationRecipientSettings.add(notificationRecipientSetting);
			}
		}

		return notificationRecipientSettings;
	}

	@Override
	public String getRecipientSummary(
		NotificationQueueEntry notificationQueueEntry) {

		NotificationRecipient notificationRecipient =
			notificationQueueEntry.getNotificationRecipient();

		List<String> values = new ArrayList<>();

		for (NotificationRecipientSetting notificationRecipientSetting :
				notificationRecipient.getNotificationRecipientSettings()) {

			values.add(notificationRecipientSetting.getValue());
		}

		return ListUtil.toString(
			values, (String)null, StringPool.COMMA_AND_SPACE);
	}

	@Override
	public String getType() {
		return NotificationConstants.TYPE_USER_NOTIFICATION;
	}

	@Override
	public String getTypeLanguageKey() {
		return "user-notification";
	}

	@Override
	public void sendNotification(NotificationContext notificationContext)
		throws PortalException {

		List<Map<String, String>> notificationRecipientSettings =
			new ArrayList<>();

		NotificationTemplate notificationTemplate =
			notificationContext.getNotificationTemplate();

		UsersProvider usersProvider =
			_usersProviderServiceTracker.getUsersProvider(
				notificationTemplate.getRecipientType());

		for (User user : usersProvider.provide(notificationContext)) {
			siteDefaultLocale = portal.getSiteDefaultLocale(user.getGroupId());
			userLocale = user.getLocale();

			_userNotificationEventLocalService.sendUserNotificationEvents(
				user.getUserId(), notificationContext.getPortletId(),
				UserNotificationDeliveryConstants.TYPE_WEBSITE,
				JSONUtil.put(
					"className", notificationContext.getClassName()
				).put(
					"classPK", notificationContext.getClassPK()
				).put(
					"externalReferenceCode",
					notificationContext.getExternalReferenceCode()
				).put(
					"notificationMessage",
					formatLocalizedContent(
						notificationTemplate.getSubjectMap(),
						notificationContext)
				).put(
					"portletId", notificationContext.getPortletId()
				));

			notificationRecipientSettings.add(
				HashMapBuilder.put(
					"userFullName", user.getFullName()
				).build());
		}

		User user = userLocalService.getUser(notificationContext.getUserId());

		siteDefaultLocale = portal.getSiteDefaultLocale(user.getGroupId());
		userLocale = user.getLocale();

		prepareNotificationContext(
			user, null, notificationContext, notificationRecipientSettings,
			formatLocalizedContent(
				notificationTemplate.getSubjectMap(), notificationContext));

		notificationQueueEntryLocalService.addNotificationQueueEntry(
			notificationContext);
	}

	@Override
	public Object[] toRecipients(
		List<NotificationRecipientSetting> notificationRecipientSettings) {

		return TransformUtil.transformToArray(
			notificationRecipientSettings,
			notificationRecipientSetting -> HashMapBuilder.put(
				notificationRecipientSetting.getName(),
				notificationRecipientSetting.getValue()
			).build(),
			Object.class);
	}

	@Override
	protected NotificationQueueEntry createNotificationQueueEntry(
		User user, String body, NotificationContext notificationContext,
		String subject) {

		NotificationQueueEntry notificationQueueEntry =
			super.createNotificationQueueEntry(
				user, body, notificationContext, subject);

		notificationQueueEntry.setStatus(
			NotificationQueueEntryConstants.STATUS_SENT);

		return notificationQueueEntry;
	}

	@Reference
	private UserNotificationEventLocalService
		_userNotificationEventLocalService;

	@Reference
	private UsersProviderTracker _usersProviderServiceTracker;

}