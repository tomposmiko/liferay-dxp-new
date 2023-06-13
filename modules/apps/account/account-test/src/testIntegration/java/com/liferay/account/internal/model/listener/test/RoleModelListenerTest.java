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

package com.liferay.account.internal.model.listener.test;

import com.liferay.account.constants.AccountRoleConstants;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.model.AccountRole;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountRoleLocalService;
import com.liferay.account.service.test.AccountEntryTestUtil;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.RequiredRoleException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Drew Brokke
 */
@RunWith(Arquillian.class)
public class RoleModelListenerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_company = CompanyTestUtil.addCompany();
	}

	@Test
	public void testDeleteCompany() throws Exception {
		List<Long> requiredRoleIds = Stream.of(
			AccountRoleConstants.REQUIRED_ROLE_NAMES
		).map(
			requiredRoleName -> _roleLocalService.fetchRole(
				_company.getCompanyId(), requiredRoleName)
		).map(
			Role::getRoleId
		).collect(
			Collectors.toList()
		);

		_companyLocalService.deleteCompany(_company);

		_company = null;

		for (long requiredRoleId : requiredRoleIds) {
			Assert.assertNull(_roleLocalService.fetchRole(requiredRoleId));
		}
	}

	@Test
	public void testDeleteDefaultAccountRole() throws Exception {
		for (String requiredRoleName :
				AccountRoleConstants.REQUIRED_ROLE_NAMES) {

			try {
				_roleLocalService.deleteRole(
					_roleLocalService.getRole(
						_company.getCompanyId(), requiredRoleName));

				Assert.fail(
					"Allowed to delete default role: " + requiredRoleName);
			}
			catch (ModelListenerException modelListenerException) {
				Throwable throwable = modelListenerException.getCause();

				Assert.assertTrue(throwable instanceof RequiredRoleException);

				String message = throwable.getMessage();

				Assert.assertTrue(
					message.contains(" is a default account role"));
			}
		}
	}

	@Test(expected = ModelListenerException.class)
	public void testDeleteRole() throws Exception {
		_accountEntry = AccountEntryTestUtil.addAccountEntry(
			_accountEntryLocalService, WorkflowConstants.STATUS_APPROVED);

		_accountRole = _accountRoleLocalService.addAccountRole(
			TestPropsValues.getUserId(), _accountEntry.getAccountEntryId(),
			RandomTestUtil.randomString(), null, null);

		try {
			_roleLocalService.deleteRole(_accountRole.getRoleId());

			Assert.fail(
				"Allowed to delete a role associated with an account role");
		}
		catch (ModelListenerException modelListenerException) {
			Throwable throwable = modelListenerException.getCause();

			Assert.assertTrue(throwable instanceof RequiredRoleException);

			String message = throwable.getMessage();

			Assert.assertTrue(
				message.contains(" is required by account role "));

			throw modelListenerException;
		}
	}

	@DeleteAfterTestRun
	private AccountEntry _accountEntry;

	@Inject
	private AccountEntryLocalService _accountEntryLocalService;

	@DeleteAfterTestRun
	private AccountRole _accountRole;

	@Inject
	private AccountRoleLocalService _accountRoleLocalService;

	@DeleteAfterTestRun
	private Company _company;

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private RoleLocalService _roleLocalService;

}