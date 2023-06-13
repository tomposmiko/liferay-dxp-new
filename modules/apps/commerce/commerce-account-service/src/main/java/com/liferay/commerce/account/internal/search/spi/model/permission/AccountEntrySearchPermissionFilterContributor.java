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

package com.liferay.commerce.account.internal.search.spi.model.permission;

import com.liferay.account.model.AccountEntry;
import com.liferay.commerce.account.constants.CommerceAccountActionKeys;
import com.liferay.commerce.account.internal.util.AccountEntryUtil;
import com.liferay.commerce.context.CommerceContextThreadLocal;
import com.liferay.commerce.context.CommerceGroupThreadLocal;
import com.liferay.commerce.product.constants.CommerceChannelAccountEntryRelConstants;
import com.liferay.commerce.product.model.CommerceChannelAccountEntryRel;
import com.liferay.commerce.product.service.CommerceChannelAccountEntryRelService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.TermsFilter;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.search.spi.model.permission.SearchPermissionFilterContributor;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Crescenzo Rega
 */
@Component(service = SearchPermissionFilterContributor.class)
public class AccountEntrySearchPermissionFilterContributor
	implements SearchPermissionFilterContributor {

	@Override
	public void contribute(
		BooleanFilter booleanFilter, long companyId, long[] groupIds,
		long userId, PermissionChecker permissionChecker, String className) {

		if (!className.equals(AccountEntry.class.getName())) {
			return;
		}

		try {
			if (_accountEntryModelResourcePermission.contains(
					permissionChecker, 0,
					CommerceAccountActionKeys.
						MANAGE_AVAILABLE_ACCOUNTS_VIA_USER_CHANNEL_REL)) {

				List<CommerceChannelAccountEntryRel>
					commerceChannelAccountEntryRels = new ArrayList<>();

				commerceChannelAccountEntryRels.addAll(
					_commerceChannelAccountEntryRelService.
						getCommerceChannelAccountEntryRels(
							User.class.getName(), userId, 0,
							CommerceChannelAccountEntryRelConstants.TYPE_USER));

				long commerceChannelId = AccountEntryUtil.getCommerceChannelId(
					CommerceContextThreadLocal.get(),
					CommerceGroupThreadLocal.get());

				if (commerceChannelId > 0) {
					commerceChannelAccountEntryRels.addAll(
						_commerceChannelAccountEntryRelService.
							getCommerceChannelAccountEntryRels(
								User.class.getName(), userId, commerceChannelId,
								CommerceChannelAccountEntryRelConstants.
									TYPE_USER));
				}

				TermsFilter termsFilter = new TermsFilter(Field.ENTRY_CLASS_PK);

				for (CommerceChannelAccountEntryRel
						commerceChannelAccountEntryRel :
							commerceChannelAccountEntryRels) {

					termsFilter.addValue(
						String.valueOf(
							commerceChannelAccountEntryRel.
								getAccountEntryId()));
				}

				if (!termsFilter.isEmpty()) {
					booleanFilter.add(termsFilter, BooleanClauseOccur.SHOULD);
				}
			}
		}
		catch (PortalException portalException) {
			_log.error(portalException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AccountEntrySearchPermissionFilterContributor.class);

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(model.class.name=com.liferay.account.model.AccountEntry)"
	)
	private volatile ModelResourcePermission<AccountEntry>
		_accountEntryModelResourcePermission;

	@Reference
	private CommerceChannelAccountEntryRelService
		_commerceChannelAccountEntryRelService;

}