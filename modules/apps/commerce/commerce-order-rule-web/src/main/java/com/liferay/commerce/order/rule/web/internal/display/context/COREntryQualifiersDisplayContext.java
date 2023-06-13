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

package com.liferay.commerce.order.rule.web.internal.display.context;

import com.liferay.account.constants.AccountPortletKeys;
import com.liferay.commerce.currency.service.CommerceCurrencyService;
import com.liferay.commerce.model.CommerceOrderType;
import com.liferay.commerce.order.rule.entry.type.COREntryTypeJSPContributorRegistry;
import com.liferay.commerce.order.rule.entry.type.COREntryTypeRegistry;
import com.liferay.commerce.order.rule.model.COREntry;
import com.liferay.commerce.order.rule.service.COREntryRelService;
import com.liferay.commerce.order.rule.service.COREntryService;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.frontend.data.set.model.FDSActionDropdownItem;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alessio Antonio Rendina
 */
public class COREntryQualifiersDisplayContext extends COREntryDisplayContext {

	public COREntryQualifiersDisplayContext(
		CommerceCurrencyService commerceCurrencyService,
		ModelResourcePermission<COREntry> corEntryModelResourcePermission,
		COREntryRelService corEntryRelService, COREntryService corEntryService,
		COREntryTypeJSPContributorRegistry corEntryTypeJSPContributorRegistry,
		COREntryTypeRegistry corEntryTypeRegistry,
		HttpServletRequest httpServletRequest, Portal portal) {

		super(
			commerceCurrencyService, corEntryModelResourcePermission,
			corEntryService, corEntryTypeJSPContributorRegistry,
			corEntryTypeRegistry, httpServletRequest, portal);

		_corEntryRelService = corEntryRelService;
	}

	public String getAccountEntryCOREntriesAPIURL() throws PortalException {
		return "/o/headless-commerce-admin-order/v1.0/order-rules/" +
			getCOREntryId() + "/order-rule-accounts?nestedFields=account";
	}

	public List<FDSActionDropdownItem> getAccountEntryFDSActionDropdownItems()
		throws PortalException {

		return _getFDSActionTemplates(
			PortletURLBuilder.create(
				portal.getControlPanelPortletURL(
					httpServletRequest,
					AccountPortletKeys.ACCOUNT_ENTRIES_ADMIN,
					PortletRequest.RENDER_PHASE)
			).setMVCRenderCommandName(
				"/account_admin/edit_account_entry"
			).setRedirect(
				corEntryRequestHelper.getCurrentURL()
			).setParameter(
				"accountEntryId", "{account.id}"
			).buildString(),
			false);
	}

	public String getAccountGroupCOREntriesAPIURL() throws PortalException {
		return "/o/headless-commerce-admin-order/v1.0/order-rules/" +
			getCOREntryId() +
				"/order-rule-account-groups?nestedFields=accountGroup";
	}

	public List<FDSActionDropdownItem> getAccountGroupFDSActionDropdownItems()
		throws PortalException {

		return ListUtil.fromArray(
			new FDSActionDropdownItem(
				null, "trash", "remove",
				LanguageUtil.get(httpServletRequest, "remove"), "delete",
				"delete", "headless"));
	}

	public String getActiveAccountEligibility() throws PortalException {
		long accountEntryCOREntryRelsCount =
			_corEntryRelService.getAccountEntryCOREntryRelsCount(
				getCOREntryId(), null);

		if (accountEntryCOREntryRelsCount > 0) {
			return "accounts";
		}

		long accountGroupCOREntryRelsCount =
			_corEntryRelService.getAccountGroupCOREntryRelsCount(
				getCOREntryId(), null);

		if (accountGroupCOREntryRelsCount > 0) {
			return "accountGroups";
		}

		return "all";
	}

	public String getActiveChannelEligibility() throws PortalException {
		long commerceChannelCOREntryRelsCount =
			_corEntryRelService.getCommerceChannelCOREntryRelsCount(
				getCOREntryId(), null);

		if (commerceChannelCOREntryRelsCount > 0) {
			return "channels";
		}

		return "all";
	}

	public String getActiveOrderTypeEligibility() throws PortalException {
		long commerceOrderTypeCOREntryRelsCount =
			_corEntryRelService.getCommerceOrderTypeCOREntryRelsCount(
				getCOREntryId(), null);

		if (commerceOrderTypeCOREntryRelsCount > 0) {
			return "orderTypes";
		}

		return "all";
	}

	public String getCommerceChannelCOREntriesApiURL() throws PortalException {
		return "/o/headless-commerce-admin-order/v1.0/order-rules/" +
			getCOREntryId() + "/order-rule-channels?nestedFields=channel";
	}

	public List<FDSActionDropdownItem>
			getCommerceChannelCOREntryFDSActionDropdownItems()
		throws PortalException {

		return _getHeadlessFDSActionTemplates(
			PortletURLBuilder.create(
				PortletProviderUtil.getPortletURL(
					httpServletRequest, CommerceChannel.class.getName(),
					PortletProvider.Action.MANAGE)
			).setMVCRenderCommandName(
				"/commerce_channels/edit_commerce_channel"
			).setRedirect(
				corEntryRequestHelper.getCurrentURL()
			).setParameter(
				"commerceChannelId", "{channel.id}"
			).buildString(),
			false);
	}

	public String getCommerceOrderTypeCOREntriesAPIURL()
		throws PortalException {

		return "/o/headless-commerce-admin-order/v1.0/order-rules/" +
			getCOREntryId() + "/order-rule-order-types?nestedFields=orderType";
	}

	public List<FDSActionDropdownItem>
			getCommerceOrderTypeFDSActionDropdownItems()
		throws PortalException {

		return _getFDSActionTemplates(
			PortletURLBuilder.create(
				PortletProviderUtil.getPortletURL(
					httpServletRequest, CommerceOrderType.class.getName(),
					PortletProvider.Action.MANAGE)
			).setMVCRenderCommandName(
				"/commerce_order_type/edit_commerce_order_type"
			).setRedirect(
				corEntryRequestHelper.getCurrentURL()
			).setParameter(
				"commerceOrderTypeId", "{orderType.id}"
			).buildString(),
			false);
	}

	private List<FDSActionDropdownItem> _getFDSActionTemplates(
		String portletURL, boolean sidePanel) {

		List<FDSActionDropdownItem> fdsActionDropdownItems = new ArrayList<>();

		FDSActionDropdownItem fdsActionDropdownItem = new FDSActionDropdownItem(
			portletURL, "pencil", "edit",
			LanguageUtil.get(httpServletRequest, "edit"), "get", null, null);

		if (sidePanel) {
			fdsActionDropdownItem.setTarget("sidePanel");
		}

		fdsActionDropdownItems.add(fdsActionDropdownItem);

		fdsActionDropdownItems.add(
			new FDSActionDropdownItem(
				null, "trash", "remove",
				LanguageUtil.get(httpServletRequest, "remove"), "delete",
				"delete", "headless"));

		return fdsActionDropdownItems;
	}

	private List<FDSActionDropdownItem> _getHeadlessFDSActionTemplates(
		String portletURL, boolean sidePanel) {

		List<FDSActionDropdownItem> fdsActionDropdownItems = new ArrayList<>();

		FDSActionDropdownItem fdsActionDropdownItem = new FDSActionDropdownItem(
			portletURL, "pencil", "edit",
			LanguageUtil.get(httpServletRequest, "edit"), "get", null, null);

		if (sidePanel) {
			fdsActionDropdownItem.setTarget("sidePanel");
		}

		fdsActionDropdownItems.add(fdsActionDropdownItem);

		fdsActionDropdownItems.add(
			new FDSActionDropdownItem(
				null, "trash", "remove",
				LanguageUtil.get(httpServletRequest, "remove"), "delete",
				"delete", "headless"));

		return fdsActionDropdownItems;
	}

	private final COREntryRelService _corEntryRelService;

}