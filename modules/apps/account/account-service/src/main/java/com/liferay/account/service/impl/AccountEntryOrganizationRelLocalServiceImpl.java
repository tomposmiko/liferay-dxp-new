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

import com.liferay.account.exception.DuplicateAccountEntryOrganizationRelException;
import com.liferay.account.model.AccountEntryOrganizationRel;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.base.AccountEntryOrganizationRelLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.liferay.account.model.AccountEntryOrganizationRel",
	service = AopService.class
)
public class AccountEntryOrganizationRelLocalServiceImpl
	extends AccountEntryOrganizationRelLocalServiceBaseImpl {

	@Override
	public AccountEntryOrganizationRel addAccountEntryOrganizationRel(
			long accountEntryId, long organizationId)
		throws PortalException {

		if (hasAccountEntryOrganizationRel(accountEntryId, organizationId)) {
			throw new DuplicateAccountEntryOrganizationRelException();
		}

		accountEntryLocalService.getAccountEntry(accountEntryId);
		organizationLocalService.getOrganization(organizationId);

		AccountEntryOrganizationRel accountEntryOrganizationRel =
			createAccountEntryOrganizationRel(counterLocalService.increment());

		accountEntryOrganizationRel.setAccountEntryId(accountEntryId);
		accountEntryOrganizationRel.setOrganizationId(organizationId);

		return updateAccountEntryOrganizationRel(accountEntryOrganizationRel);
	}

	@Override
	public void addAccountEntryOrganizationRels(
			long accountEntryId, long[] organizationIds)
		throws PortalException {

		for (long organizationId : organizationIds) {
			addAccountEntryOrganizationRel(accountEntryId, organizationId);
		}
	}

	@Override
	public List<AccountEntryOrganizationRel> getAccountEntryOrganizationRels(
		long accountEntryId) {

		return accountEntryOrganizationRelPersistence.findByAccountEntryId(
			accountEntryId);
	}

	@Override
	public int getAccountEntryOrganizationRelsCount(long accountEntryId) {
		return accountEntryOrganizationRelPersistence.countByAccountEntryId(
			accountEntryId);
	}

	@Override
	public boolean hasAccountEntryOrganizationRel(
		long accountEntryId, long organizationId) {

		AccountEntryOrganizationRel accountEntryOrganizationRel =
			accountEntryOrganizationRelPersistence.fetchByA_O(
				accountEntryId, organizationId);

		if (accountEntryOrganizationRel != null) {
			return true;
		}

		return false;
	}

	@Reference
	protected AccountEntryLocalService accountEntryLocalService;

}