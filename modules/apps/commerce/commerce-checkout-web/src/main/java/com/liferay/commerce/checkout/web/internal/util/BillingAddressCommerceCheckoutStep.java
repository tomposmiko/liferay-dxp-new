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

package com.liferay.commerce.checkout.web.internal.util;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountRoleLocalService;
import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalService;
import com.liferay.commerce.checkout.helper.CommerceCheckoutStepHttpHelper;
import com.liferay.commerce.checkout.web.internal.display.context.BillingAddressCheckoutStepDisplayContext;
import com.liferay.commerce.constants.CommerceAddressConstants;
import com.liferay.commerce.constants.CommerceCheckoutWebKeys;
import com.liferay.commerce.constants.CommerceOrderActionKeys;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.exception.CommerceAddressCityException;
import com.liferay.commerce.exception.CommerceAddressCountryException;
import com.liferay.commerce.exception.CommerceAddressNameException;
import com.liferay.commerce.exception.CommerceAddressStreetException;
import com.liferay.commerce.exception.CommerceAddressZipException;
import com.liferay.commerce.exception.CommerceOrderBillingAddressException;
import com.liferay.commerce.exception.CommerceOrderDefaultBillingAddressException;
import com.liferay.commerce.exception.CommerceOrderShippingAddressException;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.product.constants.CommerceChannelAccountEntryRelConstants;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.model.CommerceChannelAccountEntryRel;
import com.liferay.commerce.product.service.CommerceChannelAccountEntryRelLocalService;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.commerce.util.BaseCommerceCheckoutStep;
import com.liferay.commerce.util.CommerceCheckoutStep;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Andrea Di Giorgi
 * @author Alessio Antonio Rendina
 * @author Luca Pellizzon
 */
@Component(
	property = {
		"commerce.checkout.step.name=" + BillingAddressCommerceCheckoutStep.NAME,
		"commerce.checkout.step.order:Integer=30"
	},
	service = CommerceCheckoutStep.class
)
public class BillingAddressCommerceCheckoutStep
	extends BaseCommerceCheckoutStep {

	public static final String NAME = "billing-address";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean isActive(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		CommerceOrder commerceOrder =
			(CommerceOrder)httpServletRequest.getAttribute(
				CommerceCheckoutWebKeys.COMMERCE_ORDER);

		if (!commerceOrder.isOpen()) {
			return false;
		}

		boolean activeBillingAddressCommerceCheckoutStep =
			_commerceCheckoutStepHttpHelper.
				isActiveBillingAddressCommerceCheckoutStep(
					httpServletRequest, commerceOrder);

		CommerceAccount commerceAccount = commerceOrder.getCommerceAccount();

		if (!commerceOrder.isGuestOrder() &&
			!commerceAccount.isPersonalAccount()) {

			CommerceAddress defaultBillingCommerceAddress = null;

			if (commerceAccount != null) {
				AccountEntry accountEntry =
					_accountEntryLocalService.fetchAccountEntry(
						commerceAccount.getCommerceAccountId());

				if (accountEntry != null) {
					CommerceChannel commerceChannel =
						_commerceChannelLocalService.
							getCommerceChannelByOrderGroupId(
								commerceOrder.getGroupId());

					CommerceChannelAccountEntryRel
						billingAddressCommerceChannelAccountEntryRel =
							_commerceChannelAccountEntryRelLocalService.
								fetchCommerceChannelAccountEntryRel(
									accountEntry.getAccountEntryId(),
									commerceChannel.getCommerceChannelId(),
									CommerceChannelAccountEntryRelConstants.
										TYPE_BILLING_ADDRESS);

					if (billingAddressCommerceChannelAccountEntryRel != null) {
						defaultBillingCommerceAddress =
							_commerceAddressService.getCommerceAddress(
								billingAddressCommerceChannelAccountEntryRel.
									getClassPK());
					}
				}
			}

			long defaultBillingCommerceAddressId = 0;

			if (defaultBillingCommerceAddress != null) {
				defaultBillingCommerceAddressId =
					defaultBillingCommerceAddress.getCommerceAddressId();
			}

			if ((defaultBillingCommerceAddressId <= 0) &&
				(commerceOrder.getBillingAddressId() <= 0)) {

				if (_hasViewBillingAddressPermission(
						httpServletRequest, commerceAccount)) {

					return true;
				}

				List<CommerceAddress> accountBillingCommerceAddresses =
					_commerceAddressService.getBillingCommerceAddresses(
						commerceAccount.getCompanyId(),
						AccountEntry.class.getName(),
						commerceAccount.getCommerceAccountId());

				if (accountBillingCommerceAddresses.isEmpty()) {
					return true;
				}

				CommerceAddress commerceAddress =
					accountBillingCommerceAddresses.get(0);

				_commerceOrderService.updateBillingAddress(
					commerceOrder.getCommerceOrderId(),
					commerceAddress.getCommerceAddressId());

				return false;
			}

			if ((defaultBillingCommerceAddressId > 0) &&
				(defaultBillingCommerceAddressId !=
					commerceOrder.getBillingAddressId())) {

				_commerceOrderService.updateBillingAddress(
					commerceOrder.getCommerceOrderId(),
					defaultBillingCommerceAddressId);
			}

			if (_hasViewBillingAddressPermission(
					httpServletRequest, commerceAccount)) {

				return activeBillingAddressCommerceCheckoutStep;
			}

			return false;
		}

		return activeBillingAddressCommerceCheckoutStep;
	}

	@Override
	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			AddressCommerceCheckoutStepUtil addressCommerceCheckoutStepUtil =
				new AddressCommerceCheckoutStepUtil(
					_commerceAccountLocalService,
					CommerceAddressConstants.ADDRESS_TYPE_BILLING,
					_commerceOrderService, _commerceAddressService,
					_commerceOrderModelResourcePermission);

			addressCommerceCheckoutStepUtil.updateCommerceOrderAddress(
				actionRequest,
				CommerceCheckoutWebKeys.BILLING_ADDRESS_PARAM_NAME);
		}
		catch (Exception exception) {
			if (exception instanceof CommerceAddressCityException ||
				exception instanceof CommerceAddressCountryException ||
				exception instanceof CommerceAddressNameException ||
				exception instanceof CommerceAddressStreetException ||
				exception instanceof CommerceAddressZipException ||
				exception instanceof CommerceOrderBillingAddressException ||
				exception instanceof CommerceOrderShippingAddressException) {

				SessionErrors.add(actionRequest, exception.getClass());

				return;
			}

			throw exception;
		}
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		CommerceAddress defaultBillingCommerceAddress = null;

		BillingAddressCheckoutStepDisplayContext
			billingAddressCheckoutStepDisplayContext =
				new BillingAddressCheckoutStepDisplayContext(
					_accountEntryLocalService, _accountRoleLocalService,
					_accountEntryModelResourcePermission,
					_commerceAddressService,
					_commerceChannelAccountEntryRelLocalService,
					_commerceChannelLocalService, httpServletRequest,
					_portletResourcePermission);

		CommerceOrder commerceOrder =
			billingAddressCheckoutStepDisplayContext.getCommerceOrder();

		CommerceAccount commerceAccount = commerceOrder.getCommerceAccount();

		if (commerceAccount != null) {
			AccountEntry accountEntry =
				_accountEntryLocalService.fetchAccountEntry(
					commerceAccount.getCommerceAccountId());

			if (accountEntry != null) {
				CommerceChannel commerceChannel =
					_commerceChannelLocalService.
						getCommerceChannelByOrderGroupId(
							commerceOrder.getGroupId());

				CommerceChannelAccountEntryRel
					billingAddressCommerceChannelAccountEntryRel =
						_commerceChannelAccountEntryRelLocalService.
							fetchCommerceChannelAccountEntryRel(
								accountEntry.getAccountEntryId(),
								commerceChannel.getCommerceChannelId(),
								CommerceChannelAccountEntryRelConstants.
									TYPE_BILLING_ADDRESS);

				if (billingAddressCommerceChannelAccountEntryRel != null) {
					defaultBillingCommerceAddress =
						_commerceAddressService.getCommerceAddress(
							billingAddressCommerceChannelAccountEntryRel.
								getClassPK());
				}
			}
		}

		long defaultBillingCommerceAddressId = 0;

		if (defaultBillingCommerceAddress != null) {
			defaultBillingCommerceAddressId =
				defaultBillingCommerceAddress.getCommerceAddressId();
		}

		List<CommerceAddress> accountBillingCommerceAddresses =
			_commerceAddressService.getBillingCommerceAddresses(
				commerceAccount.getCompanyId(), AccountEntry.class.getName(),
				commerceAccount.getCommerceAccountId());

		if (!commerceOrder.isGuestOrder() &&
			!commerceAccount.isPersonalAccount() &&
			(defaultBillingCommerceAddressId <= 0) &&
			(commerceOrder.getBillingAddressId() <= 0) &&
			!_hasViewBillingAddressPermission(
				httpServletRequest, commerceAccount) &&
			accountBillingCommerceAddresses.isEmpty()) {

			httpServletRequest.setAttribute(
				CommerceCheckoutWebKeys.SHOW_ERROR_NO_BILLING_ADDRESS,
				Boolean.TRUE);

			SessionMessages.add(
				httpServletRequest,
				_portal.getPortletId(httpServletRequest) +
					SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);

			SessionErrors.add(
				httpServletRequest,
				CommerceOrderDefaultBillingAddressException.class);
		}

		if (!commerceOrder.isOpen()) {
			httpServletRequest.setAttribute(
				CommerceCheckoutWebKeys.COMMERCE_CHECKOUT_STEP_ORDER_DETAIL_URL,
				_commerceCheckoutStepHttpHelper.getOrderDetailURL(
					httpServletRequest, commerceOrder));

			_jspRenderer.renderJSP(
				httpServletRequest, httpServletResponse, "/error.jsp");
		}
		else {
			httpServletRequest.setAttribute(
				CommerceCheckoutWebKeys.COMMERCE_CHECKOUT_STEP_DISPLAY_CONTEXT,
				billingAddressCheckoutStepDisplayContext);

			_jspRenderer.renderJSP(
				httpServletRequest, httpServletResponse,
				"/checkout_step/address.jsp");
		}
	}

	@Override
	public boolean showControls(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		CommerceOrder commerceOrder =
			(CommerceOrder)httpServletRequest.getAttribute(
				CommerceCheckoutWebKeys.COMMERCE_ORDER);

		try {
			CommerceAddress defaultBillingCommerceAddress = null;

			CommerceAccount commerceAccount =
				commerceOrder.getCommerceAccount();

			if (commerceAccount != null) {
				AccountEntry accountEntry =
					_accountEntryLocalService.fetchAccountEntry(
						commerceAccount.getCommerceAccountId());

				if (accountEntry != null) {
					CommerceChannel commerceChannel =
						_commerceChannelLocalService.
							getCommerceChannelByOrderGroupId(
								commerceOrder.getGroupId());

					CommerceChannelAccountEntryRel
						billingAddressCommerceChannelAccountEntryRel =
							_commerceChannelAccountEntryRelLocalService.
								fetchCommerceChannelAccountEntryRel(
									accountEntry.getAccountEntryId(),
									commerceChannel.getCommerceChannelId(),
									CommerceChannelAccountEntryRelConstants.
										TYPE_BILLING_ADDRESS);

					if (billingAddressCommerceChannelAccountEntryRel != null) {
						defaultBillingCommerceAddress =
							_commerceAddressService.getCommerceAddress(
								billingAddressCommerceChannelAccountEntryRel.
									getClassPK());
					}
				}
			}

			long defaultBillingCommerceAddressId = 0;

			if (defaultBillingCommerceAddress != null) {
				defaultBillingCommerceAddressId =
					defaultBillingCommerceAddress.getCommerceAddressId();
			}

			List<CommerceAddress> accountBillingCommerceAddresses =
				_commerceAddressService.getBillingCommerceAddresses(
					commerceAccount.getCompanyId(),
					AccountEntry.class.getName(),
					commerceAccount.getCommerceAccountId());

			if (!commerceOrder.isGuestOrder() &&
				!commerceAccount.isPersonalAccount() &&
				(defaultBillingCommerceAddressId <= 0) &&
				(commerceOrder.getBillingAddressId() <= 0) &&
				!_hasViewBillingAddressPermission(
					httpServletRequest, commerceAccount) &&
				accountBillingCommerceAddresses.isEmpty()) {

				return false;
			}
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return false;
		}

		if (!commerceOrder.isOpen()) {
			return false;
		}

		return super.showControls(httpServletRequest, httpServletResponse);
	}

	private PermissionChecker _getPermissionChecker(
		HttpServletRequest httpServletRequest) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return PermissionCheckerFactoryUtil.create(themeDisplay.getUser());
	}

	private boolean _hasViewBillingAddressPermission(
			HttpServletRequest httpServletRequest,
			CommerceAccount commerceAccount)
		throws PortalException {

		return _portletResourcePermission.contains(
			_getPermissionChecker(httpServletRequest),
			commerceAccount.getCommerceAccountGroup(),
			CommerceOrderActionKeys.VIEW_BILLING_ADDRESS);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BillingAddressCommerceCheckoutStep.class);

	@Reference
	private AccountEntryLocalService _accountEntryLocalService;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(model.class.name=com.liferay.account.model.AccountEntry)"
	)
	private volatile ModelResourcePermission<AccountEntry>
		_accountEntryModelResourcePermission;

	@Reference
	private AccountRoleLocalService _accountRoleLocalService;

	@Reference
	private CommerceAccountLocalService _commerceAccountLocalService;

	@Reference
	private CommerceAddressService _commerceAddressService;

	@Reference
	private CommerceChannelAccountEntryRelLocalService
		_commerceChannelAccountEntryRelLocalService;

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceCheckoutStepHttpHelper _commerceCheckoutStepHttpHelper;

	@Reference(
		target = "(model.class.name=com.liferay.commerce.model.CommerceOrder)"
	)
	private ModelResourcePermission<CommerceOrder>
		_commerceOrderModelResourcePermission;

	@Reference
	private CommerceOrderService _commerceOrderService;

	@Reference
	private JSPRenderer _jspRenderer;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(resource.name=" + CommerceOrderConstants.RESOURCE_NAME + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

}