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

package com.liferay.commerce.payment.service.impl;

import com.liferay.commerce.payment.model.CommercePaymentEntry;
import com.liferay.commerce.payment.service.base.CommercePaymentEntryServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 */
@Component(
	property = {
		"json.web.service.context.name=commerce",
		"json.web.service.context.path=CommercePaymentEntry"
	},
	service = AopService.class
)
public class CommercePaymentEntryServiceImpl
	extends CommercePaymentEntryServiceBaseImpl {

	@Override
	public List<CommercePaymentEntry> getCommercePaymentEntries(
			long companyId, long classNameId, long classPK, int start, int end,
			OrderByComparator<CommercePaymentEntry> orderByComparator)
		throws PortalException {

		PermissionChecker permissionChecker = getPermissionChecker();

		if (!permissionChecker.hasPermission(
				null, CommercePaymentEntry.class.getName(), companyId,
				ActionKeys.VIEW)) {

			throw new PrincipalException.MustHavePermission(
				permissionChecker, CommercePaymentEntry.class.getName(), 0,
				ActionKeys.VIEW);
		}

		return commercePaymentEntryLocalService.getCommercePaymentEntries(
			companyId, classNameId, classPK, start, end, orderByComparator);
	}

	@Override
	public CommercePaymentEntry getCommercePaymentEntry(
			long commercePaymentEntryId)
		throws PortalException {

		_commercePaymentEntryModelResourcePermission.check(
			getPermissionChecker(), commercePaymentEntryId, ActionKeys.VIEW);

		return commercePaymentEntryLocalService.getCommercePaymentEntry(
			commercePaymentEntryId);
	}

	@Override
	public List<CommercePaymentEntry> search(
			long companyId, long[] classNameIds, long[] classPKs,
			String[] currencyCodes, String keywords,
			String[] paymentMethodNames, int[] paymentStatuses,
			boolean excludeStatuses, int start, int end, String orderByField,
			boolean reverse)
		throws PortalException {

		BaseModelSearchResult<CommercePaymentEntry> baseModelSearchResult =
			commercePaymentEntryLocalService.searchCommercePaymentEntries(
				companyId, keywords,
				LinkedHashMapBuilder.<String, Object>put(
					"classNameIds", classNameIds
				).put(
					"classPKs", classPKs
				).put(
					"currencyCodes", currencyCodes
				).put(
					"paymentMethodNames", paymentMethodNames
				).put(
					"permissionUserId", getPermissionChecker().getUserId()
				).put(
					"paymentStatuses", paymentStatuses
				).put(
					"excludeStatuses", excludeStatuses
				).build(),
				start, end, orderByField, reverse);

		return baseModelSearchResult.getBaseModels();
	}

	@Reference(
		target = "(model.class.name=com.liferay.commerce.payment.model.CommercePaymentEntry)"
	)
	private ModelResourcePermission<CommercePaymentEntry>
		_commercePaymentEntryModelResourcePermission;

}