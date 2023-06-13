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

package com.liferay.headless.user.notification.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.headless.user.notification.client.dto.v1_0.UserNotification;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.notifications.NotificationEvent;
import com.liferay.portal.kernel.service.UserNotificationEventLocalService;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.util.PropsUtil;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * @author Carlos Correa
 */
@RunWith(Arquillian.class)
public class UserNotificationResourceTest
	extends BaseUserNotificationResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		_irrelevantUser = UserTestUtil.addGroupAdminUser(irrelevantGroup);
		_testUser = UserTestUtil.addGroupAdminUser(testGroup);

		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-83384", "true"
			).build());
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();

		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-83384", "false"
			).build());
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"read"};
	}

	@Override
	protected UserNotification
			testGetMyUserNotificationsPage_addUserNotification(
				UserNotification userNotification)
		throws Exception {

		return _addUserNotification(
			false, testGroup.getCreatorUserId(), userNotification);
	}

	@Override
	protected UserNotification
			testGetUserAccountUserNotificationsPage_addUserNotification(
				Long userAccountId, UserNotification userNotification)
		throws Exception {

		return _addUserNotification(false, userAccountId, userNotification);
	}

	@Override
	protected Long
			testGetUserAccountUserNotificationsPage_getIrrelevantUserAccountId()
		throws Exception {

		return _irrelevantUser.getUserId();
	}

	@Override
	protected Long testGetUserAccountUserNotificationsPage_getUserAccountId()
		throws Exception {

		return _testUser.getUserId();
	}

	@Override
	protected UserNotification testGetUserNotification_addUserNotification()
		throws Exception {

		return _addUserNotification(false);
	}

	@Override
	protected UserNotification
			testGraphQLGetUserNotification_addUserNotification()
		throws Exception {

		return _addUserNotification(false);
	}

	@Override
	protected UserNotification testPutUserNotificationRead_addUserNotification()
		throws Exception {

		return _addUserNotification(true);
	}

	@Override
	protected UserNotification
			testPutUserNotificationUnread_addUserNotification()
		throws Exception {

		return _addUserNotification(false);
	}

	private UserNotification _addUserNotification(boolean read)
		throws Exception {

		return _addUserNotification(read, _testUser.getUserId(), null);
	}

	private UserNotification _addUserNotification(
			boolean read, long userId, UserNotification userNotification)
		throws Exception {

		Date date = RandomTestUtil.nextDate();

		if (userNotification != null) {
			date = userNotification.getDateCreated();
		}

		UserNotificationEvent userNotificationEvent =
			_userNotificationEventLocalService.addUserNotificationEvent(
				userId,
				new NotificationEvent(
					date.getTime(), RandomTestUtil.randomString(),
					_jsonFactory.createJSONObject()));

		if (read) {
			userNotificationEvent =
				_userNotificationEventLocalService.updateUserNotificationEvent(
					userNotificationEvent.getUuid(),
					userNotificationEvent.getCompanyId(), read);
		}

		return userNotificationResource.getUserNotification(
			userNotificationEvent.getUserNotificationEventId());
	}

	private User _irrelevantUser;

	@Inject
	private JSONFactory _jsonFactory;

	private User _testUser;

	@Inject
	private UserNotificationEventLocalService
		_userNotificationEventLocalService;

}