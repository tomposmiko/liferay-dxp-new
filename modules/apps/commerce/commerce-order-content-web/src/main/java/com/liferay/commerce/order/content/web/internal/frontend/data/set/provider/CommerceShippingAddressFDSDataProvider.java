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

package com.liferay.commerce.order.content.web.internal.frontend.data.set.provider;

import com.liferay.account.model.AccountEntry;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.order.content.web.internal.constants.CommerceOrderFDSNames;
import com.liferay.commerce.order.content.web.internal.model.Address;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.commerce.service.CommerceOrderService;
import com.liferay.frontend.data.set.provider.FDSDataProvider;
import com.liferay.frontend.data.set.provider.search.FDSKeywords;
import com.liferay.frontend.data.set.provider.search.FDSPagination;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 */
@Component(
	enabled = false, immediate = true,
	property = "fds.data.provider.key=" + CommerceOrderFDSNames.SHIPPING_ADDRESSES,
	service = FDSDataProvider.class
)
public class CommerceShippingAddressFDSDataProvider
	implements FDSDataProvider<Address> {

	@Override
	public List<Address> getItems(
			FDSKeywords fdsKeywords, FDSPagination fdsPagination,
			HttpServletRequest httpServletRequest, Sort sort)
		throws PortalException {

		List<Address> addresses = new ArrayList<>();

		long commerceOrderId = ParamUtil.getLong(
			httpServletRequest, "commerceOrderId");

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			commerceOrderId);

		List<CommerceAddress> commerceAddresses =
			_commerceAddressService.getShippingCommerceAddresses(
				commerceOrder.getCompanyId(), AccountEntry.class.getName(),
				commerceOrder.getCommerceAccountId(), fdsKeywords.getKeywords(),
				fdsPagination.getStartPosition(),
				fdsPagination.getEndPosition(), sort);

		for (CommerceAddress commerceAddress : commerceAddresses) {
			addresses.add(
				new Address(
					commerceAddress.getCommerceAddressId(),
					commerceAddress.getName(),
					_getDescriptiveCommerceAddress(commerceAddress)));
		}

		return addresses;
	}

	@Override
	public int getItemsCount(
			FDSKeywords fdsKeywords, HttpServletRequest httpServletRequest)
		throws PortalException {

		long commerceOrderId = ParamUtil.getLong(
			httpServletRequest, "commerceOrderId");

		CommerceOrder commerceOrder = _commerceOrderService.getCommerceOrder(
			commerceOrderId);

		return _commerceAddressService.getShippingCommerceAddressesCount(
			commerceOrder.getCompanyId(), AccountEntry.class.getName(),
			commerceOrder.getCommerceAccountId(), fdsKeywords.getKeywords());
	}

	private String _getDescriptiveCommerceAddress(
			CommerceAddress commerceAddress)
		throws PortalException {

		if (commerceAddress == null) {
			return StringPool.BLANK;
		}

		Region region = commerceAddress.getRegion();

		StringBundler sb = new StringBundler((region == null) ? 5 : 7);

		sb.append(commerceAddress.getStreet1());
		sb.append(StringPool.SPACE);
		sb.append(commerceAddress.getCity());
		sb.append(StringPool.NEW_LINE);

		if (region != null) {
			sb.append(region.getRegionCode());
			sb.append(StringPool.SPACE);
		}

		sb.append(commerceAddress.getZip());

		return sb.toString();
	}

	@Reference
	private CommerceAddressService _commerceAddressService;

	@Reference
	private CommerceOrderService _commerceOrderService;

}