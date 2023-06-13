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

package com.liferay.account.internal.search.spi.model.permission.test;

import com.liferay.account.constants.AccountActionKeys;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.model.AccountGroup;
import com.liferay.account.model.AccountRole;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountEntryUserRelLocalService;
import com.liferay.account.service.AccountGroupLocalService;
import com.liferay.account.service.AccountGroupRelLocalService;
import com.liferay.account.service.AccountGroupService;
import com.liferay.account.service.AccountRoleLocalService;
import com.liferay.account.service.test.util.AccountEntryTestUtil;
import com.liferay.account.service.test.util.AccountGroupTestUtil;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DataGuard;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Pei-Jung Lan
 */
@DataGuard(scope = DataGuard.Scope.METHOD)
@RunWith(Arquillian.class)
public class AccountGroupSearchPermissionFilterContributorTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Test
	public void testWhenHasAccountEntryViewAccountGroupsPermissionSearch()
		throws Exception {

		AccountGroup accountGroup = AccountGroupTestUtil.addAccountGroup(
			_accountGroupLocalService, RandomTestUtil.randomString(),
			RandomTestUtil.randomString());
		AccountEntry accountEntry = AccountEntryTestUtil.addAccountEntry(
			_accountEntryLocalService);

		_accountGroupRelLocalService.addAccountGroupRel(
			accountGroup.getAccountGroupId(), AccountEntry.class.getName(),
			accountEntry.getAccountEntryId());

		AccountGroupTestUtil.addAccountGroup(
			_accountGroupLocalService, RandomTestUtil.randomString(),
			RandomTestUtil.randomString());
		AccountGroupTestUtil.addAccountGroup(
			_accountGroupLocalService, RandomTestUtil.randomString(),
			RandomTestUtil.randomString());

		User user = UserTestUtil.addUser();

		_accountEntryUserRelLocalService.addAccountEntryUserRel(
			accountEntry.getAccountEntryId(), user.getUserId());

		_assertSearch(user);

		AccountRole accountRole = _accountRoleLocalService.addAccountRole(
			TestPropsValues.getUserId(), accountEntry.getAccountEntryId(),
			RandomTestUtil.randomString(),
			RandomTestUtil.randomLocaleStringMap(),
			RandomTestUtil.randomLocaleStringMap());

		RoleTestUtil.addResourcePermission(
			accountRole.getRole(), AccountEntry.class.getName(),
			ResourceConstants.SCOPE_GROUP_TEMPLATE, "0",
			AccountActionKeys.VIEW_ACCOUNT_GROUPS);

		_accountRoleLocalService.associateUser(
			accountEntry.getAccountEntryId(), accountRole.getAccountRoleId(),
			user.getUserId());

		_assertSearch(user, accountGroup);
	}

	private void _assertSearch(User user, AccountGroup... expectedAccountGroups)
		throws Exception {

		PermissionChecker originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		try {
			PermissionThreadLocal.setPermissionChecker(
				PermissionCheckerFactoryUtil.create(user));

			BaseModelSearchResult<AccountGroup> baseModelSearchResult =
				_accountGroupService.searchAccountGroups(
					user.getCompanyId(), null, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null);

			Assert.assertEquals(
				expectedAccountGroups.length,
				baseModelSearchResult.getLength());

			List<AccountGroup> accountGroups =
				baseModelSearchResult.getBaseModels();

			Assert.assertTrue(
				accountGroups.containsAll(
					Arrays.asList(expectedAccountGroups)));
		}
		finally {
			PermissionThreadLocal.setPermissionChecker(
				originalPermissionChecker);
		}
	}

	@Inject
	private AccountEntryLocalService _accountEntryLocalService;

	@Inject
	private AccountEntryUserRelLocalService _accountEntryUserRelLocalService;

	@Inject
	private AccountGroupLocalService _accountGroupLocalService;

	@Inject
	private AccountGroupRelLocalService _accountGroupRelLocalService;

	@Inject
	private AccountGroupService _accountGroupService;

	@Inject
	private AccountRoleLocalService _accountRoleLocalService;

	@Inject
	private ResourcePermissionLocalService _resourcePermissionLocalService;

}