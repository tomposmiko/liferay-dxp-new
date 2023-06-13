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

package com.liferay.commerce.product.internal.model.listener;

import com.liferay.account.model.AccountEntry;
import com.liferay.commerce.product.constants.CommerceChannelAccountEntryRelConstants;
import com.liferay.commerce.product.model.CommerceChannelAccountEntryRel;
import com.liferay.commerce.product.service.CommerceChannelAccountEntryRelLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Address;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(enabled = false, immediate = true, service = ModelListener.class)
public class AccountEntryModelListener extends BaseModelListener<AccountEntry> {

	@Override
	public void onAfterUpdate(
			AccountEntry originalAccountEntry, AccountEntry accountEntry)
		throws ModelListenerException {

		if ((accountEntry.getDefaultBillingAddressId() ==
				originalAccountEntry.getDefaultBillingAddressId()) &&
			(accountEntry.getDefaultShippingAddressId() ==
				originalAccountEntry.getDefaultShippingAddressId())) {

			return;
		}

		try {
			CommerceChannelAccountEntryRel
				defaultBillingAddressCommerceChannelAccountEntryRel =
					_commerceChannelAccountEntryRelLocalService.
						fetchCommerceChannelAccountEntryRel(
							accountEntry.getAccountEntryId(),
							Address.class.getName(),
							originalAccountEntry.getDefaultBillingAddressId(),
							0,
							CommerceChannelAccountEntryRelConstants.
								TYPE_BILLING_ADDRESS);

			if ((defaultBillingAddressCommerceChannelAccountEntryRel != null) &&
				(accountEntry.getDefaultBillingAddressId() !=
					originalAccountEntry.getDefaultBillingAddressId())) {

				if (accountEntry.getDefaultBillingAddressId() > 0) {
					_commerceChannelAccountEntryRelLocalService.
						updateCommerceChannelAccountEntryRel(
							defaultBillingAddressCommerceChannelAccountEntryRel.
								getCommerceChannelAccountEntryRelId(),
							0, accountEntry.getDefaultBillingAddressId(),
							defaultBillingAddressCommerceChannelAccountEntryRel.
								isOverrideEligibility(),
							defaultBillingAddressCommerceChannelAccountEntryRel.
								getPriority());
				}
				else {
					_commerceChannelAccountEntryRelLocalService.
						deleteCommerceChannelAccountEntryRel(
							defaultBillingAddressCommerceChannelAccountEntryRel.
								getCommerceChannelAccountEntryRelId());
				}
			}
			else if ((defaultBillingAddressCommerceChannelAccountEntryRel ==
						null) &&
					 (accountEntry.getDefaultBillingAddressId() > 0) &&
					 (accountEntry.getDefaultBillingAddressId() !=
						 originalAccountEntry.getDefaultBillingAddressId())) {

				_commerceChannelAccountEntryRelLocalService.
					addCommerceChannelAccountEntryRel(
						accountEntry.getUserId(),
						accountEntry.getAccountEntryId(),
						Address.class.getName(),
						accountEntry.getDefaultBillingAddressId(), 0, false, 0,
						CommerceChannelAccountEntryRelConstants.
							TYPE_BILLING_ADDRESS);
			}

			CommerceChannelAccountEntryRel
				defaultShippingAddressCommerceChannelAccountEntryRel =
					_commerceChannelAccountEntryRelLocalService.
						fetchCommerceChannelAccountEntryRel(
							accountEntry.getAccountEntryId(),
							Address.class.getName(),
							originalAccountEntry.getDefaultShippingAddressId(),
							0,
							CommerceChannelAccountEntryRelConstants.
								TYPE_SHIPPING_ADDRESS);

			if ((defaultShippingAddressCommerceChannelAccountEntryRel !=
					null) &&
				(accountEntry.getDefaultShippingAddressId() !=
					originalAccountEntry.getDefaultShippingAddressId())) {

				if (accountEntry.getDefaultShippingAddressId() > 0) {
					_commerceChannelAccountEntryRelLocalService.
						updateCommerceChannelAccountEntryRel(
							defaultShippingAddressCommerceChannelAccountEntryRel.
								getCommerceChannelAccountEntryRelId(),
							0, accountEntry.getDefaultShippingAddressId(),
							defaultShippingAddressCommerceChannelAccountEntryRel.
								isOverrideEligibility(),
							defaultShippingAddressCommerceChannelAccountEntryRel.
								getPriority());
				}
				else {
					_commerceChannelAccountEntryRelLocalService.
						deleteCommerceChannelAccountEntryRel(
							defaultShippingAddressCommerceChannelAccountEntryRel.
								getCommerceChannelAccountEntryRelId());
				}
			}
			else if ((defaultShippingAddressCommerceChannelAccountEntryRel ==
						null) &&
					 (accountEntry.getDefaultShippingAddressId() > 0) &&
					 (accountEntry.getDefaultShippingAddressId() !=
						 originalAccountEntry.getDefaultShippingAddressId())) {

				_commerceChannelAccountEntryRelLocalService.
					addCommerceChannelAccountEntryRel(
						accountEntry.getUserId(),
						accountEntry.getAccountEntryId(),
						Address.class.getName(),
						accountEntry.getDefaultShippingAddressId(), 0, false, 0,
						CommerceChannelAccountEntryRelConstants.
							TYPE_SHIPPING_ADDRESS);
			}
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}
	}

	@Override
	public void onBeforeRemove(AccountEntry accountEntry) {
		try {
			_commerceChannelAccountEntryRelLocalService.
				deleteCommerceChannelAccountEntryRelsByAccountEntryId(
					accountEntry.getAccountEntryId());
		}
		catch (PortalException portalException) {
			_log.error(portalException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AccountEntryModelListener.class);

	@Reference
	private CommerceChannelAccountEntryRelLocalService
		_commerceChannelAccountEntryRelLocalService;

}