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

package com.liferay.commerce.pricing.web.internal.display.context;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryService;
import com.liferay.commerce.discount.constants.CommerceDiscountConstants;
import com.liferay.commerce.discount.model.CommerceDiscount;
import com.liferay.commerce.discount.service.CommerceDiscountService;
import com.liferay.commerce.price.list.constants.CommercePriceListConstants;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.service.CommercePriceListService;
import com.liferay.commerce.pricing.web.internal.display.context.helper.CommercePricingRequestHelper;
import com.liferay.commerce.product.constants.CommerceChannelAccountEntryRelConstants;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.model.CommerceChannelAccountEntryRel;
import com.liferay.commerce.product.service.CommerceChannelAccountEntryRelService;
import com.liferay.commerce.product.service.CommerceChannelService;
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
import com.liferay.portal.kernel.workflow.WorkflowConstants;

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
			CommerceChannelAccountEntryRelService
				commerceChannelAccountEntryRelService,
			CommerceChannelService commerceChannelService,
			CommerceDiscountService commerceDiscountService,
			CommercePriceListService commercePriceListService,
			HttpServletRequest httpServletRequest, Language language)
		throws PortalException {

		_accountEntryModelResourcePermission =
			accountEntryModelResourcePermission;
		_accountEntryService = accountEntryService;
		_commerceChannelAccountEntryRelService =
			commerceChannelAccountEntryRelService;
		_commerceChannelService = commerceChannelService;
		_commerceDiscountService = commerceDiscountService;
		_commercePriceListService = commercePriceListService;
		_language = language;

		long accountEntryId = ParamUtil.getLong(
			httpServletRequest, "accountEntryId");

		_accountEntry = _accountEntryService.getAccountEntry(accountEntryId);

		_commercePricingRequestHelper = new CommercePricingRequestHelper(
			httpServletRequest);

		_type = ParamUtil.getInteger(httpServletRequest, "type");
	}

	public CommerceChannelAccountEntryRel fetchCommerceChannelAccountEntryRel()
		throws PortalException {

		long commerceChannelAccountEntryRelId = ParamUtil.getLong(
			_commercePricingRequestHelper.getRequest(),
			"commerceChannelAccountEntryRelId");

		return _commerceChannelAccountEntryRelService.
			fetchCommerceChannelAccountEntryRel(
				commerceChannelAccountEntryRelId);
	}

	public AccountEntry getAccountEntry() {
		return _accountEntry;
	}

	public String getAddCommerceChannelAccountEntryRelRenderURL(int type) {
		String mvcRenderCommandName = StringPool.BLANK;

		if (CommerceChannelAccountEntryRelConstants.TYPE_PRICE_LIST == type) {
			mvcRenderCommandName =
				"/commerce_pricing" +
					"/edit_account_entry_default_commerce_price_list";
		}
		else if (CommerceChannelAccountEntryRelConstants.TYPE_DISCOUNT ==
					type) {

			mvcRenderCommandName =
				"/commerce_pricing" +
					"/edit_account_entry_default_commerce_discount";
		}

		return PortletURLBuilder.createRenderURL(
			_commercePricingRequestHelper.getLiferayPortletResponse()
		).setMVCRenderCommandName(
			mvcRenderCommandName
		).setParameter(
			"accountEntryId", _accountEntry.getAccountEntryId()
		).setParameter(
			"type", type
		).setWindowState(
			LiferayWindowState.POP_UP
		).buildString();
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

	public List<CommerceDiscount> getCommerceDiscounts()
		throws PortalException {

		if (CommerceChannelAccountEntryRelConstants.TYPE_DISCOUNT == _type) {
			return _commerceDiscountService.getCommerceDiscounts(
				_commercePricingRequestHelper.getCompanyId(),
				CommerceDiscountConstants.LEVEL_L1, true,
				WorkflowConstants.STATUS_APPROVED);
		}

		return Collections.emptyList();
	}

	public List<CommercePriceList> getCommercePriceLists()
		throws PortalException {

		if (CommerceChannelAccountEntryRelConstants.TYPE_PRICE_LIST == _type) {
			return _commercePriceListService.getCommercePriceLists(
				_commercePricingRequestHelper.getCompanyId(),
				CommercePriceListConstants.TYPE_PRICE_LIST,
				WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null);
		}

		return Collections.emptyList();
	}

	public CreationMenu getCreationMenu(int type) throws Exception {
		CreationMenu creationMenu = new CreationMenu();

		if (hasPermission(ActionKeys.UPDATE)) {
			creationMenu.addDropdownItem(
				dropdownItem -> {
					dropdownItem.setHref(
						getAddCommerceChannelAccountEntryRelRenderURL(type));
					dropdownItem.setLabel(_getDropdownItemLabel(type));
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
				_commercePricingRequestHelper.getCompanyId());

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
		if (CommerceChannelAccountEntryRelConstants.TYPE_PRICE_LIST == _type) {
			return _language.get(
				_commercePricingRequestHelper.getRequest(),
				"set-default-price-list");
		}
		else if (CommerceChannelAccountEntryRelConstants.TYPE_DISCOUNT ==
					_type) {

			return _language.get(
				_commercePricingRequestHelper.getRequest(),
				"set-default-discount");
		}

		return StringPool.BLANK;
	}

	public int getType() {
		return _type;
	}

	public boolean hasPermission(String actionId) throws PortalException {
		return _accountEntryModelResourcePermission.contains(
			_commercePricingRequestHelper.getPermissionChecker(),
			_accountEntry.getAccountEntryId(), actionId);
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

	public boolean isEntrySelected(long classPK) throws PortalException {
		CommerceChannelAccountEntryRel commerceChannelAccountEntryRel =
			fetchCommerceChannelAccountEntryRel();

		if (commerceChannelAccountEntryRel == null) {
			return false;
		}

		if (commerceChannelAccountEntryRel.getClassPK() == classPK) {
			return true;
		}

		return false;
	}

	private String _getDropdownItemLabel(int type) {
		String key = StringPool.BLANK;

		if (CommerceChannelAccountEntryRelConstants.TYPE_PRICE_LIST == type) {
			key = "add-default-price-list";
		}
		else if (CommerceChannelAccountEntryRelConstants.TYPE_DISCOUNT ==
					type) {

			key = "add-default-discount";
		}

		return _language.get(_commercePricingRequestHelper.getRequest(), key);
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
	private final CommerceChannelAccountEntryRelService
		_commerceChannelAccountEntryRelService;
	private final CommerceChannelService _commerceChannelService;
	private final CommerceDiscountService _commerceDiscountService;
	private final CommercePriceListService _commercePriceListService;
	private final CommercePricingRequestHelper _commercePricingRequestHelper;
	private final Language _language;
	private final int _type;

}