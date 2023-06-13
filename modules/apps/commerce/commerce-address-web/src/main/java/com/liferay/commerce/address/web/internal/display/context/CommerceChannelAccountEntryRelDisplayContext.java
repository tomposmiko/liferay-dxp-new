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

package com.liferay.commerce.address.web.internal.display.context;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryService;
import com.liferay.commerce.address.web.internal.display.context.helper.CommerceCountryRequestHelper;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.product.constants.CommerceChannelAccountEntryRelConstants;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.model.CommerceChannelAccountEntryRel;
import com.liferay.commerce.product.service.CommerceChannelAccountEntryRelService;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alessio Antonio Rendina
 */
public class CommerceChannelAccountEntryRelDisplayContext {

	public CommerceChannelAccountEntryRelDisplayContext(
			ModelResourcePermission<AccountEntry>
				accountEntryModelResourcePermission,
			AccountEntryService accountEntryService,
			CommerceAddressService commerceAddressService,
			CommerceChannelAccountEntryRelService
				commerceChannelAccountEntryRelService,
			CommerceChannelService commerceChannelService,
			HttpServletRequest httpServletRequest, Language language)
		throws PortalException {

		_accountEntryModelResourcePermission =
			accountEntryModelResourcePermission;
		_accountEntryService = accountEntryService;
		_commerceAddressService = commerceAddressService;
		_commerceChannelAccountEntryRelService =
			commerceChannelAccountEntryRelService;
		_commerceChannelService = commerceChannelService;
		_language = language;

		long accountEntryId = ParamUtil.getLong(
			httpServletRequest, "accountEntryId");

		_accountEntry = _accountEntryService.getAccountEntry(accountEntryId);

		_commerceCountryRequestHelper = new CommerceCountryRequestHelper(
			httpServletRequest);

		_type = ParamUtil.getInteger(httpServletRequest, "type");
	}

	public CommerceChannelAccountEntryRel fetchCommerceChannelAccountEntryRel()
		throws PortalException {

		long commerceChannelAccountEntryRelId = ParamUtil.getLong(
			_commerceCountryRequestHelper.getRequest(),
			"commerceChannelAccountEntryRelId");

		return _commerceChannelAccountEntryRelService.
			fetchCommerceChannelAccountEntryRel(
				commerceChannelAccountEntryRelId);
	}

	public AccountEntry getAccountEntry() {
		return _accountEntry;
	}

	public String getAddCommerceChannelAccountEntryRelRenderURL(int type) {
		return PortletURLBuilder.createRenderURL(
			_commerceCountryRequestHelper.getLiferayPortletResponse()
		).setMVCRenderCommandName(
			"/commerce_address/edit_account_entry_default_commerce_address"
		).setParameter(
			"accountEntryId", _accountEntry.getAccountEntryId()
		).setParameter(
			"type", type
		).setWindowState(
			LiferayWindowState.POP_UP
		).buildString();
	}

	public String getAddressSelectLabel(int type) {
		if (CommerceChannelAccountEntryRelConstants.TYPE_BILLING_ADDRESS ==
				type) {

			return "billing-address";
		}

		if (CommerceChannelAccountEntryRelConstants.TYPE_SHIPPING_ADDRESS ==
				type) {

			return "shipping-address";
		}

		return "address";
	}

	public List<CommerceAddress> getCommerceAddresses() throws PortalException {
		if (CommerceChannelAccountEntryRelConstants.TYPE_BILLING_ADDRESS ==
				_type) {

			return _commerceAddressService.getBillingCommerceAddresses(
				_commerceCountryRequestHelper.getCompanyId(),
				AccountEntry.class.getName(),
				_accountEntry.getAccountEntryId());
		}

		if (CommerceChannelAccountEntryRelConstants.TYPE_SHIPPING_ADDRESS ==
				_type) {

			return _commerceAddressService.getShippingCommerceAddresses(
				_commerceCountryRequestHelper.getCompanyId(),
				AccountEntry.class.getName(),
				_accountEntry.getAccountEntryId());
		}

		return Collections.emptyList();
	}

	public String getCommerceChannelsEmptyOptionKey() throws PortalException {
		int commerceChannelAccountEntryRelsCount =
			_commerceChannelAccountEntryRelService.
				getCommerceChannelAccountEntryRelsCount(
					_accountEntry.getAccountEntryId(), _type);

		if (commerceChannelAccountEntryRelsCount > 0) {
			return "all-other-channels";
		}

		return "all-channels";
	}

	public CreationMenu getCreationMenu(int type) throws Exception {
		CreationMenu creationMenu = new CreationMenu();

		if (hasPermission(ActionKeys.UPDATE)) {
			creationMenu.addDropdownItem(
				dropdownItem -> {
					dropdownItem.setHref(
						getAddCommerceChannelAccountEntryRelRenderURL(type));
					dropdownItem.setLabel(
						_language.get(
							_commerceCountryRequestHelper.getRequest(),
							"add-default-address"));
					dropdownItem.setTarget("modal");
				});
		}

		return creationMenu;
	}

	public List<CommerceChannel> getFilteredCommerceChannels()
		throws PortalException {

		long[] commerceChannelIds = _getFilteredCommerceChannelIds();

		List<CommerceChannel> commerceChannels =
			_commerceChannelService.getCommerceChannels(
				_commerceCountryRequestHelper.getCompanyId());

		Stream<CommerceChannel> commerceChannelsStream =
			commerceChannels.stream();

		return commerceChannelsStream.filter(
			commerceChannel -> !ArrayUtil.contains(
				commerceChannelIds, commerceChannel.getCommerceChannelId())
		).collect(
			Collectors.toList()
		);
	}

	public String getModalTitle() {
		if (CommerceChannelAccountEntryRelConstants.TYPE_BILLING_ADDRESS ==
				_type) {

			return _language.get(
				_commerceCountryRequestHelper.getRequest(),
				"set-default-billing-address");
		}

		if (CommerceChannelAccountEntryRelConstants.TYPE_SHIPPING_ADDRESS ==
				_type) {

			return _language.get(
				_commerceCountryRequestHelper.getRequest(),
				"set-default-shipping-address");
		}

		return StringPool.BLANK;
	}

	public int getType() {
		return _type;
	}

	public boolean hasPermission(String actionId) throws PortalException {
		return _accountEntryModelResourcePermission.contains(
			_commerceCountryRequestHelper.getPermissionChecker(),
			_accountEntry.getAccountEntryId(), actionId);
	}

	public boolean isCommerceAddressSelected(long commerceAddressId)
		throws PortalException {

		CommerceChannelAccountEntryRel commerceChannelAccountEntryRel =
			fetchCommerceChannelAccountEntryRel();

		if (commerceChannelAccountEntryRel == null) {
			return false;
		}

		if (commerceChannelAccountEntryRel.getClassPK() == commerceAddressId) {
			return true;
		}

		return false;
	}

	public boolean isCommerceChannelSelected(long commerceChannelId)
		throws PortalException {

		CommerceChannelAccountEntryRel commerceChannelAccountEntryRel =
			fetchCommerceChannelAccountEntryRel();

		if (commerceChannelAccountEntryRel == null) {
			if (commerceChannelId == 0) {
				return true;
			}

			return false;
		}

		if (commerceChannelAccountEntryRel.getCommerceChannelId() ==
				commerceChannelId) {

			return true;
		}

		return false;
	}

	private long[] _getFilteredCommerceChannelIds() throws PortalException {
		CommerceChannelAccountEntryRel commerceChannelAccountEntryRel =
			fetchCommerceChannelAccountEntryRel();

		List<CommerceChannelAccountEntryRel> commerceChannelAccountEntryRels =
			_commerceChannelAccountEntryRelService.
				getCommerceChannelAccountEntryRels(
					_accountEntry.getAccountEntryId(), _type, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null);

		Stream<CommerceChannelAccountEntryRel>
			commerceChannelAccountEntryRelsStream =
				commerceChannelAccountEntryRels.stream();

		return commerceChannelAccountEntryRelsStream.mapToLong(
			CommerceChannelAccountEntryRel::getCommerceChannelId
		).filter(
			commerceChannelId -> {
				if ((commerceChannelAccountEntryRel == null) ||
					(commerceChannelId !=
						commerceChannelAccountEntryRel.
							getCommerceChannelId())) {

					return true;
				}

				return false;
			}
		).toArray();
	}

	private final AccountEntry _accountEntry;
	private final ModelResourcePermission<AccountEntry>
		_accountEntryModelResourcePermission;
	private final AccountEntryService _accountEntryService;
	private final CommerceAddressService _commerceAddressService;
	private final CommerceChannelAccountEntryRelService
		_commerceChannelAccountEntryRelService;
	private final CommerceChannelService _commerceChannelService;
	private final CommerceCountryRequestHelper _commerceCountryRequestHelper;
	private final Language _language;
	private final int _type;

}