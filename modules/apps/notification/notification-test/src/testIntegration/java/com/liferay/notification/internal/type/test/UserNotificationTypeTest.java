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
import com.liferay.notification.constants.NotificationRecipientConstants;
import com.liferay.notification.constants.NotificationTemplateConstants;
import com.liferay.notification.context.NotificationContext;
import com.liferay.notification.context.NotificationContextBuilder;
import com.liferay.notification.model.NotificationQueueEntry;
import com.liferay.notification.model.NotificationRecipient;
import com.liferay.notification.model.NotificationRecipientSetting;
import com.liferay.notification.model.NotificationTemplate;
import com.liferay.object.model.ObjectEntry;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.UserNotificationEventLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Feliphe Marinho
 */
@RunWith(Arquillian.class)
public class UserNotificationTypeTest extends BaseNotificationTypeTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testSendNotificationRecipientTypeRole() throws Exception {
		_testSendNotification(
			Arrays.asList(
				createNotificationRecipientSetting(
					"roleName", RoleConstants.ADMINISTRATOR),
				createNotificationRecipientSetting("roleName", role.getName())),
			NotificationRecipientConstants.TYPE_ROLE);
	}

	@Test
	public void testSendNotificationRecipientTypeTerm() throws Exception {
		_testSendNotification(
			Arrays.asList(
				createNotificationRecipientSetting("term", getTerm("creator")),
				createNotificationRecipientSetting(
					"term", user1.getScreenName())),
			NotificationRecipientConstants.TYPE_TERM);
	}

	@Test
	public void testSendNotificationRecipientTypeUser() throws Exception {
		_testSendNotification(
			Arrays.asList(
				createNotificationRecipientSetting(
					"userScreenName", user1.getScreenName()),
				createNotificationRecipientSetting(
					"userScreenName", user2.getScreenName())),
			NotificationRecipientConstants.TYPE_USER);
	}

	private void _assertNotificationRecipientSetting(
		NotificationRecipientSetting notificationRecipientSetting,
		String userFullName) {

		Assert.assertEquals(
			"userFullName", notificationRecipientSetting.getName());
		Assert.assertEquals(
			notificationRecipientSetting.getValue(), userFullName);
	}

	private NotificationContext _createNotificationContext(
			List<NotificationRecipientSetting> notificationRecipientSettings,
			String recipientType)
		throws Exception {

		NotificationContext notificationContext = new NotificationContext();

		notificationContext.setClassName(RandomTestUtil.randomString());
		notificationContext.setClassPK(RandomTestUtil.randomLong());

		NotificationTemplate notificationTemplate =
			notificationTemplateLocalService.createNotificationTemplate(0L);

		notificationTemplate.setEditorType(
			NotificationTemplateConstants.EDITOR_TYPE_RICH_TEXT);
		notificationTemplate.setName(RandomTestUtil.randomString());
		notificationTemplate.setRecipientType(recipientType);
		notificationTemplate.setSubject(
			ListUtil.toString(getTermNames(), StringPool.BLANK));
		notificationTemplate.setType(
			NotificationConstants.TYPE_USER_NOTIFICATION);

		notificationContext.setNotificationTemplate(notificationTemplate);

		notificationContext.setNotificationRecipient(
			notificationRecipientLocalService.createNotificationRecipient(0L));
		notificationContext.setNotificationRecipientSettings(
			notificationRecipientSettings);
		notificationContext.setType(
			NotificationConstants.TYPE_USER_NOTIFICATION);

		return notificationContext;
	}

	private void _testSendNotification(
			List<NotificationRecipientSetting> notificationRecipientSettings,
			String recipientType)
		throws Exception {

		List<NotificationQueueEntry> notificationQueueEntries =
			notificationQueueEntryLocalService.getNotificationQueueEntries(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		Assert.assertEquals(
			notificationQueueEntries.toString(), 0,
			notificationQueueEntries.size());

		Assert.assertEquals(
			0,
			_userNotificationEventLocalService.getUserNotificationEventsCount(
				user1.getUserId()));

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
					_createNotificationContext(
						notificationRecipientSettings, recipientType))
			).termValues(
				HashMapBuilder.<String, Object>put(
					"creator", user2.getUserId()
				).put(
					"currentUserId", user2.getUserId()
				).putAll(
					randomObjectEntryValues
				).build()
			).userId(
				user2.getUserId()
			).build(),
			NotificationConstants.TYPE_USER_NOTIFICATION);

		notificationQueueEntries =
			notificationQueueEntryLocalService.getNotificationQueueEntries(
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		Assert.assertEquals(
			1,
			_userNotificationEventLocalService.getUserNotificationEventsCount(
				user1.getUserId()));

		_userNotificationEventLocalService.deleteUserNotificationEvents(
			user1.getUserId());

		Assert.assertEquals(
			notificationQueueEntries.toString(), 1,
			notificationQueueEntries.size());

		notificationQueueEntry = notificationQueueEntries.get(0);

		assertTerms(
			getTermValues(),
			ListUtil.fromString(
				notificationQueueEntry.getSubject(), StringPool.COMMA));

		NotificationRecipient notificationRecipient =
			notificationQueueEntry.getNotificationRecipient();

		notificationRecipientSettings =
			notificationRecipient.getNotificationRecipientSettings();

		Assert.assertEquals(
			notificationRecipientSettings.toString(), 2,
			notificationRecipientSettings.size());
		_assertNotificationRecipientSetting(
			notificationRecipientSettings.get(0), user1.getFullName());
		_assertNotificationRecipientSetting(
			notificationRecipientSettings.get(1), user2.getFullName());
	}

	@Inject
	private UserNotificationEventLocalService
		_userNotificationEventLocalService;

}