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

package com.liferay.portal.service;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.UserGroupLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserGroupTestUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Drew Brokke
 */
public class UserGroupServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerTestRule.INSTANCE);

	@Test
	public void testGetGtUserGroups() throws Exception {
		for (int i = 0; i < 10; i++) {
			_userGroups.add(UserGroupTestUtil.addUserGroup());
		}

		long parentUserGroupId = 0;
		int size = 5;

		List<UserGroup> userGroups = UserGroupServiceUtil.getGtUserGroups(
			0, TestPropsValues.getCompanyId(), parentUserGroupId, size);

		Assert.assertFalse(userGroups.isEmpty());
		Assert.assertEquals(userGroups.toString(), size, userGroups.size());

		UserGroup lastUserGroup = userGroups.get(userGroups.size() - 1);

		userGroups = UserGroupServiceUtil.getGtUserGroups(
			lastUserGroup.getUserGroupId(), TestPropsValues.getCompanyId(),
			parentUserGroupId, size);

		Assert.assertFalse(userGroups.isEmpty());
		Assert.assertEquals(userGroups.toString(), size, userGroups.size());

		long previousUserGroupId = 0;

		for (UserGroup userGroup : userGroups) {
			long userGroupId = userGroup.getUserGroupId();

			Assert.assertTrue(userGroupId > lastUserGroup.getUserGroupId());
			Assert.assertTrue(userGroupId > previousUserGroupId);

			previousUserGroupId = userGroupId;
		}
	}

	@Test
	public void testGetUserGroupsLikeName() throws Exception {
		String name = RandomTestUtil.randomString(10);

		List<UserGroup> expectedUserGroups = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			UserGroup userGroup = UserGroupTestUtil.addUserGroup();

			userGroup.setName(name + i);

			UserGroupLocalServiceUtil.updateUserGroup(userGroup);

			expectedUserGroups.add(userGroup);
		}

		_userGroups.addAll(expectedUserGroups);
		_userGroups.add(UserGroupTestUtil.addUserGroup());
		_userGroups.add(UserGroupTestUtil.addUserGroup());
		_userGroups.add(UserGroupTestUtil.addUserGroup());

		assertExpectedUserGroups(expectedUserGroups, name + "%");
		assertExpectedUserGroups(
			expectedUserGroups, StringUtil.toLowerCase(name) + "%");
		assertExpectedUserGroups(
			expectedUserGroups, StringUtil.toUpperCase(name) + "%");
		assertExpectedUserGroups(_userGroups, null);
		assertExpectedUserGroups(_userGroups, "");
	}

	protected void assertExpectedUserGroups(
			List<UserGroup> expectedUserGroups, String nameSearch)
		throws Exception {

		List<UserGroup> actualUserGroups = UserGroupServiceUtil.getUserGroups(
			TestPropsValues.getCompanyId(), nameSearch, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS);

		Assert.assertEquals(
			actualUserGroups.toString(), expectedUserGroups.size(),
			actualUserGroups.size());
		Assert.assertTrue(
			actualUserGroups.toString(),
			actualUserGroups.containsAll(expectedUserGroups));

		Assert.assertEquals(
			expectedUserGroups.size(),
			UserGroupServiceUtil.getUserGroupsCount(
				TestPropsValues.getCompanyId(), nameSearch));
	}

	@DeleteAfterTestRun
	private final List<UserGroup> _userGroups = new ArrayList<>();

}