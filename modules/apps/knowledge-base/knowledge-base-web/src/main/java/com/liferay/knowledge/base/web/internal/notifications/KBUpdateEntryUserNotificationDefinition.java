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

package com.liferay.knowledge.base.web.internal.notifications;

import com.liferay.knowledge.base.constants.KBPortletKeys;
import com.liferay.portal.kernel.model.UserNotificationDeliveryConstants;
import com.liferay.portal.kernel.notifications.UserNotificationDefinition;
import com.liferay.portal.kernel.notifications.UserNotificationDeliveryType;

import org.osgi.service.component.annotations.Component;

/**
 * @author Alicia García
 */
@Component(
	property = "javax.portlet.name=" + KBPortletKeys.KNOWLEDGE_BASE_ADMIN,
	service = UserNotificationDefinition.class
)
public class KBUpdateEntryUserNotificationDefinition
	extends UserNotificationDefinition {

	public KBUpdateEntryUserNotificationDefinition() {
		super(
			KBPortletKeys.KNOWLEDGE_BASE_ADMIN, 0,
			UserNotificationDefinition.NOTIFICATION_TYPE_UPDATE_ENTRY,
			"receive-a-notification-when-someone-updates-a-knowledge-base-" +
				"article-in-a-folder-you-are-subscribed-to");

		addUserNotificationDeliveryType(
			new UserNotificationDeliveryType(
				"email", UserNotificationDeliveryConstants.TYPE_EMAIL, true,
				true));
		addUserNotificationDeliveryType(
			new UserNotificationDeliveryType(
				"website", UserNotificationDeliveryConstants.TYPE_WEBSITE, true,
				true));
	}

}