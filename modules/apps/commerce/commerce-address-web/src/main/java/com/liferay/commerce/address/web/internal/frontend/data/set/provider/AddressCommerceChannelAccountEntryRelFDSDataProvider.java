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

package com.liferay.commerce.address.web.internal.frontend.data.set.provider;

import com.liferay.commerce.address.web.internal.constants.CommerceAddressFDSNames;
import com.liferay.commerce.address.web.internal.model.Address;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.model.CommerceChannelAccountEntryRel;
import com.liferay.commerce.product.service.CommerceChannelAccountEntryRelService;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.frontend.data.set.provider.FDSDataProvider;
import com.liferay.frontend.data.set.provider.search.FDSKeywords;
import com.liferay.frontend.data.set.provider.search.FDSPagination;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"fds.data.provider.key=" + CommerceAddressFDSNames.ACCOUNT_ENTRY_BILLING_ADDRESSES,
		"fds.data.provider.key=" + CommerceAddressFDSNames.ACCOUNT_ENTRY_SHIPPING_ADDRESSES
	},
	service = FDSDataProvider.class
)
public class AddressCommerceChannelAccountEntryRelFDSDataProvider
	implements FDSDataProvider<Address> {

	@Override
	public List<Address> getItems(
			FDSKeywords fdsKeywords, FDSPagination fdsPagination,
			HttpServletRequest httpServletRequest, Sort sort)
		throws PortalException {

		long accountEntryId = ParamUtil.getLong(
			httpServletRequest, "accountEntryId");
		int type = ParamUtil.getInteger(httpServletRequest, "type");

		return TransformUtil.transform(
			_commerceChannelAccountEntryRelService.
				getCommerceChannelAccountEntryRels(
					accountEntryId, type, fdsPagination.getStartPosition(),
					fdsPagination.getEndPosition(), null),
			commerceChannelAccountEntryRel -> {
				CommerceAddress commerceAddress =
					_commerceAddressService.getCommerceAddress(
						commerceChannelAccountEntryRel.getClassPK());

				return new Address(
					commerceChannelAccountEntryRel.getAccountEntryId(),
					_getChannelName(
						accountEntryId,
						commerceChannelAccountEntryRel.getCommerceChannelId(),
						httpServletRequest, type),
					commerceChannelAccountEntryRel.
						getCommerceChannelAccountEntryRelId(),
					_getDescriptiveCommerceAddress(commerceAddress),
					commerceAddress.getName(),
					commerceChannelAccountEntryRel.getType());
			});
	}

	@Override
	public int getItemsCount(
			FDSKeywords fdsKeywords, HttpServletRequest httpServletRequest)
		throws PortalException {

		return _commerceChannelAccountEntryRelService.
			getCommerceChannelAccountEntryRelsCount(
				ParamUtil.getLong(httpServletRequest, "accountEntryId"),
				ParamUtil.getInteger(httpServletRequest, "type"));
	}

	private String _getChannelName(
			long accountEntryId, long commerceChannelId,
			HttpServletRequest httpServletRequest, int type)
		throws PortalException {

		CommerceChannel commerceChannel =
			_commerceChannelService.fetchCommerceChannel(commerceChannelId);

		if (commerceChannel == null) {
			List<CommerceChannelAccountEntryRel>
				commerceChannelAccountEntryRels =
					_commerceChannelAccountEntryRelService.
						getCommerceChannelAccountEntryRels(
							accountEntryId, type, QueryUtil.ALL_POS,
							QueryUtil.ALL_POS, null);

			if (commerceChannelAccountEntryRels.size() == 1) {
				CommerceChannelAccountEntryRel commerceChannelAccountEntryRel =
					commerceChannelAccountEntryRels.get(0);

				if (commerceChannelAccountEntryRel.getCommerceChannelId() ==
						0) {

					return _language.get(httpServletRequest, "all-channels");
				}
			}
			else if (!commerceChannelAccountEntryRels.isEmpty()) {
				return _language.get(httpServletRequest, "all-other-channels");
			}

			return _language.get(httpServletRequest, "all-channels");
		}

		return commerceChannel.getName();
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
	private CommerceChannelAccountEntryRelService
		_commerceChannelAccountEntryRelService;

	@Reference
	private CommerceChannelService _commerceChannelService;

	@Reference
	private Language _language;

}