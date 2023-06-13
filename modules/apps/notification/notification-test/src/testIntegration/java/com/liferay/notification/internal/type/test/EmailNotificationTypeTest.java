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

package com.liferay.notification.internal.type.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.notification.constants.NotificationConstants;
import com.liferay.notification.constants.NotificationQueueEntryConstants;
import com.liferay.notification.constants.NotificationRecipientConstants;
import com.liferay.notification.constants.NotificationTemplateConstants;
import com.liferay.notification.context.NotificationContext;
import com.liferay.notification.context.NotificationContextBuilder;
import com.liferay.notification.model.NotificationQueueEntry;
import com.liferay.notification.model.NotificationRecipient;
import com.liferay.notification.model.NotificationTemplate;
import com.liferay.notification.util.NotificationRecipientSettingUtil;
import com.liferay.object.model.ObjectEntry;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Feliphe Marinho
 */
@RunWith(Arquillian.class)
public class EmailNotificationTypeTest extends BaseNotificationTypeTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testSendNotification() throws Exception {
		Assert.assertEquals(
			0,
			notificationQueueEntryLocalService.
				getNotificationQueueEntriesCount());

		ObjectEntry objectEntry = objectEntryLocalService.addObjectEntry(
			user2.getUserId(), 0, objectDefinition.getObjectDefinitionId(),
			randomObjectEntryValues,
			ServiceContextTestUtil.getServiceContext());

		sendNotification(
			new NotificationContextBuilder(
			).className(
				objectDefinition.getClassName()
			).classPK(
				objectEntry.getObjectEntryId()
			).notificationTemplate(
				notificationTemplateLocalService.addNotificationTemplate(
					_createNotificationContext())
			).termValues(
				HashMapBuilder.<String, Object>put(
					"creator", user2.getUserId()
				).put(
					"currentUserId", user2.getUserId()
				).putAll(
					randomObjectEntryValues
				).build()
			).userId(
				user1.getUserId()
			).build(),
			NotificationConstants.TYPE_EMAIL);

		List<NotificationQueueEntry> notificationQueueEntries =
			notificationQueueEntryLocalService.getNotificationEntries(
				NotificationConstants.TYPE_EMAIL,
				NotificationQueueEntryConstants.STATUS_SENT);

		Assert.assertEquals(
			notificationQueueEntries.toString(), 1,
			notificationQueueEntries.size());

		NotificationQueueEntry notificationQueueEntry =
			notificationQueueEntries.get(0);

		Assert.assertEquals(
			NotificationQueueEntryConstants.STATUS_SENT,
			notificationQueueEntry.getStatus());

		NotificationRecipient notificationRecipient =
			notificationQueueEntry.getNotificationRecipient();

		Map<String, Object> notificationRecipientSettingsMap =
			NotificationRecipientSettingUtil.toMap(
				notificationRecipient.getNotificationRecipientSettings());

		Assert.assertEquals(
			user2.getEmailAddress() + ",bcc@liferay.com",
			notificationRecipientSettingsMap.get("bcc"));
		Assert.assertEquals(
			user2.getEmailAddress() + ",cc@liferay.com",
			notificationRecipientSettingsMap.get("cc"));
		Assert.assertEquals(
			user2.getEmailAddress(),
			notificationRecipientSettingsMap.get("from"));
		Assert.assertEquals(
			user2.getFirstName(),
			notificationRecipientSettingsMap.get("fromName"));
		Assert.assertEquals(
			user2.getEmailAddress(),
			notificationRecipientSettingsMap.get("to"));

		assertTerms(
			getTermValues(),
			ListUtil.fromString(
				notificationQueueEntry.getBody(), StringPool.COMMA));
		assertTerms(
			getTermValues(),
			ListUtil.fromString(
				notificationQueueEntry.getSubject(), StringPool.COMMA));
	}

	private NotificationContext _createNotificationContext() throws Exception {
		NotificationContext notificationContext = new NotificationContext();

		notificationContext.setNotificationRecipient(
			notificationRecipientLocalService.createNotificationRecipient(0L));
		notificationContext.setNotificationRecipientSettings(
			Arrays.asList(
				createNotificationRecipientSetting(
					"bcc", "[%CURRENT_USER_EMAIL_ADDRESS%],bcc@liferay.com"),
				createNotificationRecipientSetting(
					"cc", "[%CURRENT_USER_EMAIL_ADDRESS%],cc@liferay.com"),
				createNotificationRecipientSetting(
					"from", "[%CURRENT_USER_EMAIL_ADDRESS%]"),
				createNotificationRecipientSetting(
					"fromName",
					Collections.singletonMap(
						LocaleUtil.US, "[%CURRENT_USER_FIRST_NAME%]")),
				createNotificationRecipientSetting(
					"to",
					Collections.singletonMap(
						LocaleUtil.US, "[%CURRENT_USER_EMAIL_ADDRESS%]"))));

		NotificationTemplate notificationTemplate =
			notificationTemplateLocalService.createNotificationTemplate(0L);

		notificationTemplate.setBody(
			ListUtil.toString(getTermNames(), StringPool.BLANK));
		notificationTemplate.setEditorType(
			NotificationTemplateConstants.EDITOR_TYPE_RICH_TEXT);
		notificationTemplate.setName(RandomTestUtil.randomString());
		notificationTemplate.setRecipientType(
			NotificationRecipientConstants.TYPE_EMAIL);
		notificationTemplate.setSubject(
			ListUtil.toString(getTermNames(), StringPool.BLANK));
		notificationTemplate.setType(NotificationConstants.TYPE_EMAIL);

		notificationContext.setNotificationTemplate(notificationTemplate);

		notificationContext.setType(NotificationConstants.TYPE_EMAIL);

		return notificationContext;
	}

}