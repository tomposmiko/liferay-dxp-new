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

package com.liferay.commerce.currency.web.internal.frontend.data.set.provider;

import com.liferay.account.constants.AccountPortletKeys;
import com.liferay.account.model.AccountEntry;
import com.liferay.commerce.currency.web.internal.constants.CommerceCurrencyFDSNames;
import com.liferay.commerce.currency.web.internal.model.Currency;
import com.liferay.frontend.data.set.provider.FDSActionProvider;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.List;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	property = "fds.data.provider.key=" + CommerceCurrencyFDSNames.ACCOUNT_ENTRY_CURRENCIES,
	service = FDSActionProvider.class
)
public class CurrencyCommerceChannelAccountEntryRelFDSActionProvider
	implements FDSActionProvider {

	@Override
	public List<DropdownItem> getDropdownItems(
			long groupId, HttpServletRequest httpServletRequest, Object model)
		throws PortalException {

		Currency currency = (Currency)model;

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		return DropdownItemListBuilder.add(
			() -> _accountEntryModelResourcePermission.contains(
				permissionChecker, currency.getAccountEntryId(),
				ActionKeys.UPDATE),
			dropdownItem -> {
				dropdownItem.setHref(
					_getCommerceChannelAccountEntryRelEditURL(
						currency.getAccountEntryId(),
						currency.getCommerceChannelAccountEntryRelId(),
						httpServletRequest, currency.getType()));
				dropdownItem.setLabel(
					_language.get(httpServletRequest, Constants.EDIT));
				dropdownItem.setTarget("modal-lg");
			}
		).add(
			() -> _accountEntryModelResourcePermission.contains(
				permissionChecker, currency.getAccountEntryId(),
				ActionKeys.UPDATE),
			dropdownItem -> {
				dropdownItem.setHref(
					_getCommerceChannelAccountEntryRelDeleteURL(
						currency.getCommerceChannelAccountEntryRelId(),
						httpServletRequest));
				dropdownItem.setLabel(
					_language.get(httpServletRequest, Constants.DELETE));
			}
		).build();
	}

	private String _getCommerceChannelAccountEntryRelDeleteURL(
		long commerceChannelAccountEntryRelId,
		HttpServletRequest httpServletRequest) {

		return PortletURLBuilder.create(
			_portal.getControlPanelPortletURL(
				httpServletRequest, AccountPortletKeys.ACCOUNT_ENTRIES_ADMIN,
				PortletRequest.ACTION_PHASE)
		).setActionName(
			"/commerce_currency/edit_account_entry_default_commerce_currency"
		).setCMD(
			Constants.DELETE
		).setRedirect(
			ParamUtil.getString(
				httpServletRequest, "currentUrl",
				_portal.getCurrentURL(httpServletRequest))
		).setParameter(
			"commerceChannelAccountEntryRelId", commerceChannelAccountEntryRelId
		).buildString();
	}

	private String _getCommerceChannelAccountEntryRelEditURL(
		long accountEntryId, long commerceChannelAccountEntryRelId,
		HttpServletRequest httpServletRequest, int type) {

		return PortletURLBuilder.create(
			_portal.getControlPanelPortletURL(
				httpServletRequest, AccountPortletKeys.ACCOUNT_ENTRIES_ADMIN,
				PortletRequest.RENDER_PHASE)
		).setMVCRenderCommandName(
			"/commerce_currency/edit_account_entry_default_commerce_currency"
		).setParameter(
			"accountEntryId", accountEntryId
		).setParameter(
			"commerceChannelAccountEntryRelId", commerceChannelAccountEntryRelId
		).setParameter(
			"type", type
		).setWindowState(
			LiferayWindowState.POP_UP
		).buildString();
	}

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(model.class.name=com.liferay.account.model.AccountEntry)"
	)
	private volatile ModelResourcePermission<AccountEntry>
		_accountEntryModelResourcePermission;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

}