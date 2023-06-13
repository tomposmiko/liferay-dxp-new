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

package com.liferay.account.service.impl;

import com.liferay.account.constants.AccountActionKeys;
import com.liferay.account.model.AccountGroup;
import com.liferay.account.service.base.AccountGroupServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = {
		"json.web.service.context.name=account",
		"json.web.service.context.path=AccountGroup"
	},
	service = AopService.class
)
public class AccountGroupServiceImpl extends AccountGroupServiceBaseImpl {

	@Override
	public AccountGroup addAccountGroup(
			long userId, String description, String name)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(), AccountActionKeys.ADD_ACCOUNT_GROUP);

		return accountGroupLocalService.addAccountGroup(
			userId, description, name);
	}

	@Override
	public AccountGroup deleteAccountGroup(long accountGroupId)
		throws PortalException {

		_accountGroupModelResourcePermission.check(
			getPermissionChecker(), accountGroupId, ActionKeys.DELETE);

		return accountGroupLocalService.deleteAccountGroup(accountGroupId);
	}

	@Override
	public void deleteAccountGroups(long[] accountGroupIds)
		throws PortalException {

		for (long accountGroupId : accountGroupIds) {
			deleteAccountGroup(accountGroupId);
		}
	}

	@Override
	public AccountGroup updateAccountGroup(
			long accountGroupId, String description, String name)
		throws PortalException {

		_accountGroupModelResourcePermission.check(
			getPermissionChecker(), accountGroupId, ActionKeys.UPDATE);

		return accountGroupLocalService.updateAccountGroup(
			accountGroupId, description, name);
	}

	@Reference(
		target = "(model.class.name=com.liferay.account.model.AccountGroup)"
	)
	private ModelResourcePermission<AccountGroup>
		_accountGroupModelResourcePermission;

}