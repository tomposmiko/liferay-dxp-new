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

package com.liferay.commerce.checkout.web.internal.display.context;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountRoleLocalService;
import com.liferay.commerce.constants.CommerceCheckoutWebKeys;
import com.liferay.commerce.constants.CommerceOrderActionKeys;
import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.product.constants.CommerceChannelAccountEntryRelConstants;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.model.CommerceChannelAccountEntryRel;
import com.liferay.commerce.product.service.CommerceChannelAccountEntryRelLocalService;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.commerce.util.comparator.CommerceAddressNameComparator;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andrea Di Giorgi
 * @author Luca Pellizzon
 * @author Alessio Antonio Rendina
 */
public abstract class BaseAddressCheckoutStepDisplayContext {

	public BaseAddressCheckoutStepDisplayContext(
		AccountEntryLocalService accountEntryLocalService,
		ModelResourcePermission<AccountEntry>
			accountEntryModelResourcePermission,
		AccountRoleLocalService accountRoleLocalService,
		CommerceAddressService commerceAddressService,
		CommerceChannelAccountEntryRelLocalService
			commerceChannelAccountEntryRelLocalService,
		CommerceChannelLocalService commerceChannelLocalService,
		HttpServletRequest httpServletRequest,
		PortletResourcePermission portletResourcePermission) {

		this.accountEntryLocalService = accountEntryLocalService;
		this.accountEntryModelResourcePermission =
			accountEntryModelResourcePermission;
		this.accountRoleLocalService = accountRoleLocalService;
		this.commerceAddressService = commerceAddressService;
		this.commerceChannelAccountEntryRelLocalService =
			commerceChannelAccountEntryRelLocalService;
		this.commerceChannelLocalService = commerceChannelLocalService;
		this.portletResourcePermission = portletResourcePermission;

		_commerceContext = (CommerceContext)httpServletRequest.getAttribute(
			CommerceWebKeys.COMMERCE_CONTEXT);
		_commerceOrder = (CommerceOrder)httpServletRequest.getAttribute(
			CommerceCheckoutWebKeys.COMMERCE_ORDER);
	}

	public CommerceAddress getCommerceAddress(long commerceAddressId)
		throws PortalException {

		return commerceAddressService.fetchCommerceAddress(commerceAddressId);
	}

	public List<CommerceAddress> getCommerceAddresses() throws PortalException {
		return commerceAddressService.getCommerceAddressesByCompanyId(
			_commerceOrder.getCompanyId(), AccountEntry.class.getName(),
			_commerceOrder.getCommerceAccountId(), QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, new CommerceAddressNameComparator());
	}

	public CommerceContext getCommerceContext() {
		return _commerceContext;
	}

	public abstract String getCommerceCountrySelectionColumnName();

	public abstract String getCommerceCountrySelectionMethodName();

	public CommerceOrder getCommerceOrder() {
		return _commerceOrder;
	}

	public abstract long getDefaultCommerceAddressId(long commerceChannelId)
		throws PortalException;

	public abstract String getParamName();

	public abstract String getTitle();

	public boolean hasPermission(
			PermissionChecker permissionChecker, AccountEntry accountEntry,
			String actionId)
		throws PortalException {

		if (accountEntry.isGuestAccount() || accountEntry.isPersonalAccount() ||
			accountEntryModelResourcePermission.contains(
				permissionChecker, accountEntry.getAccountEntryId(),
				actionId)) {

			return true;
		}

		return false;
	}

	public boolean hasViewBillingAddressPermission(
			PermissionChecker permissionChecker, AccountEntry accountEntry)
		throws PortalException {

		if (accountEntry.isGuestAccount() || accountEntry.isPersonalAccount() ||
			portletResourcePermission.contains(
				permissionChecker, accountEntry.getAccountEntryGroup(),
				CommerceOrderActionKeys.VIEW_BILLING_ADDRESS)) {

			return true;
		}

		return false;
	}

	public boolean isShippingUsedAsBilling() throws PortalException {
		AccountEntry accountEntry = _commerceOrder.getAccountEntry();

		CommerceAddress defaultBillingCommerceAddress = null;
		CommerceAddress defaultShippingCommerceAddress = null;

		if (accountEntry != null) {
			CommerceChannel commerceChannel =
				commerceChannelLocalService.getCommerceChannelByOrderGroupId(
					_commerceOrder.getGroupId());

			CommerceChannelAccountEntryRel
				billingAddressCommerceChannelAccountEntryRel =
					commerceChannelAccountEntryRelLocalService.
						fetchCommerceChannelAccountEntryRel(
							accountEntry.getAccountEntryId(),
							commerceChannel.getCommerceChannelId(),
							CommerceChannelAccountEntryRelConstants.
								TYPE_BILLING_ADDRESS);

			if (billingAddressCommerceChannelAccountEntryRel != null) {
				defaultBillingCommerceAddress =
					commerceAddressService.getCommerceAddress(
						billingAddressCommerceChannelAccountEntryRel.
							getClassPK());
			}

			CommerceChannelAccountEntryRel
				shippingAddressCommerceChannelAccountEntryRel =
					commerceChannelAccountEntryRelLocalService.
						fetchCommerceChannelAccountEntryRel(
							accountEntry.getAccountEntryId(),
							commerceChannel.getCommerceChannelId(),
							CommerceChannelAccountEntryRelConstants.
								TYPE_SHIPPING_ADDRESS);

			if (shippingAddressCommerceChannelAccountEntryRel != null) {
				defaultShippingCommerceAddress =
					commerceAddressService.getCommerceAddress(
						shippingAddressCommerceChannelAccountEntryRel.
							getClassPK());
			}
		}

		long defaultBillingCommerceAddressId = 0;
		long defaultShippingCommerceAddressId = 0;

		if (defaultBillingCommerceAddress != null) {
			defaultBillingCommerceAddressId =
				defaultBillingCommerceAddress.getCommerceAddressId();
		}

		if (defaultShippingCommerceAddress != null) {
			defaultShippingCommerceAddressId =
				defaultShippingCommerceAddress.getCommerceAddressId();
		}

		CommerceAddress shippingAddress = _commerceOrder.getShippingAddress();
		CommerceAddress billingAddress = _commerceOrder.getBillingAddress();

		if (((accountEntry != null) &&
			 (defaultBillingCommerceAddressId ==
				 defaultShippingCommerceAddressId) &&
			 (billingAddress == null) && (shippingAddress == null)) ||
			((billingAddress != null) && (shippingAddress != null) &&
			 (billingAddress.getCommerceAddressId() ==
				 shippingAddress.getCommerceAddressId())) ||
			((billingAddress != null) && (shippingAddress == null) &&
			 (defaultShippingCommerceAddressId ==
				 billingAddress.getCommerceAddressId())) ||
			((billingAddress == null) && (shippingAddress != null) &&
			 (defaultBillingCommerceAddressId ==
				 shippingAddress.getCommerceAddressId()))) {

			return true;
		}

		return false;
	}

	protected final AccountEntryLocalService accountEntryLocalService;
	protected final ModelResourcePermission<AccountEntry>
		accountEntryModelResourcePermission;
	protected final AccountRoleLocalService accountRoleLocalService;
	protected final CommerceAddressService commerceAddressService;
	protected final CommerceChannelAccountEntryRelLocalService
		commerceChannelAccountEntryRelLocalService;
	protected final CommerceChannelLocalService commerceChannelLocalService;
	protected PortletResourcePermission portletResourcePermission;

	private final CommerceContext _commerceContext;
	private final CommerceOrder _commerceOrder;

}