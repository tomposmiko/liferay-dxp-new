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

package com.liferay.account.service.test;

import com.liferay.account.constants.AccountConstants;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.model.AccountEntryUserRel;
import com.liferay.account.model.AccountRole;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountEntryUserRelLocalService;
import com.liferay.account.service.AccountRoleLocalService;
import com.liferay.account.service.AccountRoleLocalServiceUtil;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserGroupRoleLocalService;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.comparator.RoleNameComparator;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Drew Brokke
 */
@RunWith(Arquillian.class)
public class AccountRoleLocalServiceTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_accountEntry1 = AccountEntryTestUtil.addAccountEntry(
			_accountEntryLocalService);
		_accountEntry2 = AccountEntryTestUtil.addAccountEntry(
			_accountEntryLocalService);
		_accountEntry3 = AccountEntryTestUtil.addAccountEntry(
			_accountEntryLocalService);
	}

	@Test
	public void testAddAccountRole() throws Exception {
		List<AccountRole> accountRoles =
			_accountRoleLocalService.getAccountRolesByAccountEntryIds(
				new long[] {_accountEntry1.getAccountEntryId()});

		Assert.assertEquals(accountRoles.toString(), 0, accountRoles.size());

		String name = RandomTestUtil.randomString(50);

		_addAccountRole(_accountEntry1.getAccountEntryId(), name);

		accountRoles =
			_accountRoleLocalService.getAccountRolesByAccountEntryIds(
				new long[] {_accountEntry1.getAccountEntryId()});

		Assert.assertEquals(accountRoles.toString(), 1, accountRoles.size());

		AccountRole accountRole = accountRoles.get(0);

		Assert.assertEquals(name, accountRole.getRoleName());
	}

	@Test
	public void testAssociateUser() throws Exception {
		AccountRole accountRole = _addAccountRole(
			_accountEntry1.getAccountEntryId(), RandomTestUtil.randomString());

		User user = UserTestUtil.addUser();

		_users.add(user);

		AccountEntryUserRel accountEntryUserRel =
			_accountEntryUserRelLocalService.addAccountEntryUserRel(
				_accountEntry1.getAccountEntryId(), user.getUserId());

		_accountEntryUserRels.add(accountEntryUserRel);

		Assert.assertFalse(
			_hasRoleId(
				_accountEntry1.getAccountEntryId(), accountRole.getRoleId(),
				user.getUserId()));

		_accountRoleLocalService.associateUser(
			_accountEntry1.getAccountEntryId(), accountRole.getAccountRoleId(),
			user.getUserId());

		Assert.assertTrue(
			_hasRoleId(
				_accountEntry1.getAccountEntryId(), accountRole.getRoleId(),
				user.getUserId()));

		_accountRoleLocalService.unassociateUser(
			_accountEntry1.getAccountEntryId(), accountRole.getAccountRoleId(),
			user.getUserId());

		Assert.assertFalse(
			_hasRoleId(
				_accountEntry1.getAccountEntryId(), accountRole.getRoleId(),
				user.getUserId()));

		// Permissions

		AccountRole defaultAccountRole = _addAccountRole(
			AccountConstants.ACCOUNT_ENTRY_ID_DEFAULT,
			RandomTestUtil.randomString());

		long[] roleIds = _getRoleIds(user);

		Assert.assertFalse(
			ArrayUtil.contains(roleIds, accountRole.getRoleId()));
		Assert.assertFalse(
			ArrayUtil.contains(roleIds, defaultAccountRole.getRoleId()));

		_accountRoleLocalService.associateUser(
			_accountEntry1.getAccountEntryId(), accountRole.getAccountRoleId(),
			user.getUserId());
		_accountRoleLocalService.associateUser(
			_accountEntry1.getAccountEntryId(),
			defaultAccountRole.getAccountRoleId(), user.getUserId());

		roleIds = _getRoleIds(user);

		Assert.assertTrue(ArrayUtil.contains(roleIds, accountRole.getRoleId()));
		Assert.assertTrue(
			ArrayUtil.contains(roleIds, defaultAccountRole.getRoleId()));

		_assertHasPermission(user, ActionKeys.DELETE, false);
		_assertHasPermission(user, ActionKeys.MANAGE_USERS, false);
		_assertHasPermission(user, ActionKeys.UPDATE, false);

		_resourcePermissionLocalService.addResourcePermission(
			TestPropsValues.getCompanyId(), AccountEntry.class.getName(),
			ResourceConstants.SCOPE_COMPANY,
			String.valueOf(TestPropsValues.getCompanyId()),
			accountRole.getRoleId(), ActionKeys.DELETE);
		_resourcePermissionLocalService.addResourcePermission(
			TestPropsValues.getCompanyId(), AccountEntry.class.getName(),
			ResourceConstants.SCOPE_GROUP,
			String.valueOf(_accountEntry1.getAccountEntryGroupId()),
			accountRole.getRoleId(), ActionKeys.MANAGE_USERS);
		_resourcePermissionLocalService.addResourcePermission(
			TestPropsValues.getCompanyId(), AccountEntry.class.getName(),
			ResourceConstants.SCOPE_GROUP_TEMPLATE, "0",
			defaultAccountRole.getRoleId(), ActionKeys.UPDATE);

		_assertHasPermission(user, ActionKeys.DELETE, true);
		_assertHasPermission(user, ActionKeys.MANAGE_USERS, true);
		_assertHasPermission(user, ActionKeys.UPDATE, true);

		_accountEntryUserRelLocalService.deleteAccountEntryUserRel(
			accountEntryUserRel);

		_accountEntryUserRels.remove(accountEntryUserRel);

		_assertHasPermission(user, ActionKeys.DELETE, true);
		_assertHasPermission(user, ActionKeys.MANAGE_USERS, true);
		_assertHasPermission(user, ActionKeys.UPDATE, true);
	}

	@Test
	public void testDeleteAccountRole() throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(
			AccountRoleLocalServiceTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		ServiceRegistration serviceRegistration = bundleContext.registerService(
			ModelListener.class,
			new BaseModelListener<UserGroupRole>() {

				@Override
				public void onBeforeRemove(UserGroupRole userGroupRole)
					throws ModelListenerException {

					try {
						userGroupRole.getRole();
					}
					catch (PortalException portalException) {
						throw new ModelListenerException(portalException);
					}
				}

			},
			new HashMapDictionary<>());

		try {
			_testDeleteAccountRole(_accountRoleLocalService::deleteAccountRole);
			_testDeleteAccountRole(
				accountRole -> _accountRoleLocalService.deleteAccountRole(
					accountRole.getAccountRoleId()));
		}
		finally {
			serviceRegistration.unregister();
		}
	}

	@Test
	public void testGetAccountRoles() throws Exception {
		List<AccountRole> accountRoles =
			_accountRoleLocalService.getAccountRolesByAccountEntryIds(
				new long[] {_accountEntry1.getAccountEntryId()});

		Assert.assertNotNull(accountRoles);

		Assert.assertEquals(accountRoles.toString(), 0, accountRoles.size());

		_addAccountRole(
			_accountEntry1.getAccountEntryId(),
			RandomTestUtil.randomString(50));
		_addAccountRole(
			_accountEntry2.getAccountEntryId(),
			RandomTestUtil.randomString(50));

		accountRoles =
			_accountRoleLocalService.getAccountRolesByAccountEntryIds(
				new long[] {_accountEntry1.getAccountEntryId()});

		Assert.assertNotNull(accountRoles);

		Assert.assertEquals(accountRoles.toString(), 1, accountRoles.size());
	}

	@Test
	public void testGetAccountRolesMultipleAccountEntries() throws Exception {
		List<AccountRole> accountRoles = new ArrayList<>();

		accountRoles.add(
			_addAccountRole(
				_accountEntry1.getAccountEntryId(),
				RandomTestUtil.randomString(50)));
		accountRoles.add(
			_addAccountRole(
				_accountEntry2.getAccountEntryId(),
				RandomTestUtil.randomString(50)));

		_addAccountRole(
			_accountEntry3.getAccountEntryId(),
			RandomTestUtil.randomString(50));

		List<AccountRole> actualAccountRoles =
			_accountRoleLocalService.getAccountRolesByAccountEntryIds(
				new long[] {
					_accountEntry1.getAccountEntryId(),
					_accountEntry2.getAccountEntryId()
				});

		Assert.assertEquals(
			actualAccountRoles.toString(), 2, actualAccountRoles.size());

		long[] expectedRoleIds = ListUtil.toLongArray(
			accountRoles, AccountRole::getRoleId);

		Arrays.sort(expectedRoleIds);

		long[] actualRoleIds = ListUtil.toLongArray(
			actualAccountRoles, AccountRole::getRoleId);

		Arrays.sort(actualRoleIds);

		Assert.assertArrayEquals(expectedRoleIds, actualRoleIds);
	}

	@Test
	public void testSearchAccountRoles() throws Exception {
		String keywords = RandomTestUtil.randomString();

		AccountRole accountRole = _addAccountRole(
			_accountEntry1.getAccountEntryId(),
			keywords + RandomTestUtil.randomString());

		_addAccountRole(
			_accountEntry1.getAccountEntryId(), RandomTestUtil.randomString());
		_addAccountRole(
			_accountEntry2.getAccountEntryId(),
			keywords + RandomTestUtil.randomString());

		BaseModelSearchResult<AccountRole> baseModelSearchResult =
			AccountRoleLocalServiceUtil.searchAccountRoles(
				_accountEntry1.getAccountEntryId(), StringPool.BLANK,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Assert.assertEquals(2, baseModelSearchResult.getLength());

		baseModelSearchResult = AccountRoleLocalServiceUtil.searchAccountRoles(
			_accountEntry1.getAccountEntryId(), keywords, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);

		Assert.assertEquals(1, baseModelSearchResult.getLength());

		List<AccountRole> accountRoles = baseModelSearchResult.getBaseModels();

		Assert.assertEquals(accountRole, accountRoles.get(0));
	}

	@Test
	public void testSearchAccountRolesWithPagination() throws Exception {
		String keywords = RandomTestUtil.randomString();

		for (int i = 0; i < 5; i++) {
			_addAccountRole(_accountEntry1.getAccountEntryId(), keywords + i);
		}

		BaseModelSearchResult<AccountRole> baseModelSearchResult =
			AccountRoleLocalServiceUtil.searchAccountRoles(
				_accountEntry1.getAccountEntryId(), keywords, 0, 2, null);

		Assert.assertEquals(
			_accountRoles.toString(), 5, baseModelSearchResult.getLength());

		List<AccountRole> accountRoles = baseModelSearchResult.getBaseModels();

		Assert.assertEquals(accountRoles.toString(), 2, accountRoles.size());
		Assert.assertEquals(_accountRoles.subList(0, 2), accountRoles);

		baseModelSearchResult = AccountRoleLocalServiceUtil.searchAccountRoles(
			_accountEntry1.getAccountEntryId(), keywords, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, new RoleNameComparator(false));

		List<AccountRole> expectedAccountRoles = ListUtil.sort(
			_accountRoles, Collections.reverseOrder());

		Assert.assertEquals(
			expectedAccountRoles, baseModelSearchResult.getBaseModels());
	}

	private AccountRole _addAccountRole(long accountEntryId, String name)
		throws Exception {

		AccountRole accountRole = _accountRoleLocalService.addAccountRole(
			TestPropsValues.getUserId(), accountEntryId, name, null, null);

		_accountRoles.add(accountRole);

		return accountRole;
	}

	private void _assertHasPermission(
		User user, String actionKey, boolean hasPermission) {

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		Assert.assertEquals(
			hasPermission,
			permissionChecker.hasPermission(
				_accountEntry1.getAccountEntryGroup(),
				AccountEntry.class.getName(),
				_accountEntry1.getAccountEntryId(), actionKey));
	}

	private long[] _getRoleIds(User user) throws Exception {
		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		return permissionChecker.getRoleIds(
			user.getUserId(), _accountEntry1.getAccountEntryGroupId());
	}

	private boolean _hasRoleId(
			long accountEntryId, long roleId, long accountUserId)
		throws Exception {

		long[] accountRoleIds = ListUtil.toLongArray(
			_accountRoleLocalService.getAccountRoles(
				accountEntryId, accountUserId),
			AccountRole::getRoleId);

		return ArrayUtil.contains(accountRoleIds, roleId);
	}

	private void _testDeleteAccountRole(
			UnsafeFunction<AccountRole, AccountRole, PortalException>
				deleteAccountRoleFunction)
		throws Exception {

		AccountRole accountRole = _addAccountRole(
			_accountEntry1.getAccountEntryId(), RandomTestUtil.randomString());

		User user = UserTestUtil.addUser();

		_users.add(user);

		_accountRoleLocalService.associateUser(
			_accountEntry1.getAccountEntryId(), accountRole.getAccountRoleId(),
			user.getUserId());

		long[] roleIds = _getRoleIds(user);

		Assert.assertTrue(ArrayUtil.contains(roleIds, accountRole.getRoleId()));

		deleteAccountRoleFunction.apply(accountRole);

		_accountRoles.remove(accountRole);

		Assert.assertFalse(
			ArrayUtil.contains(
				_getRoleIds(_users.get(0)), accountRole.getRoleId()));
	}

	@DeleteAfterTestRun
	private AccountEntry _accountEntry1;

	@DeleteAfterTestRun
	private AccountEntry _accountEntry2;

	@DeleteAfterTestRun
	private AccountEntry _accountEntry3;

	@Inject
	private AccountEntryLocalService _accountEntryLocalService;

	@Inject
	private AccountEntryUserRelLocalService _accountEntryUserRelLocalService;

	@DeleteAfterTestRun
	private final List<AccountEntryUserRel> _accountEntryUserRels =
		new ArrayList<>();

	@Inject
	private AccountRoleLocalService _accountRoleLocalService;

	@DeleteAfterTestRun
	private final List<AccountRole> _accountRoles = new ArrayList<>();

	@Inject
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Inject
	private RoleLocalService _roleLocalService;

	@Inject
	private UserGroupRoleLocalService _userGroupRoleLocalService;

	@DeleteAfterTestRun
	private final List<User> _users = new ArrayList<>();

}