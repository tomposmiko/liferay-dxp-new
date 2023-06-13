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

package com.liferay.commerce.product.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CommerceChannelService}.
 *
 * @author Marco Leo
 * @see CommerceChannelService
 * @generated
 */
public class CommerceChannelServiceWrapper
	implements CommerceChannelService, ServiceWrapper<CommerceChannelService> {

	public CommerceChannelServiceWrapper(
		CommerceChannelService commerceChannelService) {

		_commerceChannelService = commerceChannelService;
	}

	@Override
	public com.liferay.commerce.product.model.CommerceChannel
			addCommerceChannel(
				long siteGroupId, String name, String type,
				com.liferay.portal.kernel.util.UnicodeProperties
					typeSettingsUnicodeProperties,
				String commerceCurrencyCode, String externalReferenceCode,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.addCommerceChannel(
			siteGroupId, name, type, typeSettingsUnicodeProperties,
			commerceCurrencyCode, externalReferenceCode, serviceContext);
	}

	@Override
	public com.liferay.commerce.product.model.CommerceChannel
			deleteCommerceChannel(long commerceChannelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.deleteCommerceChannel(commerceChannelId);
	}

	@Override
	public com.liferay.commerce.product.model.CommerceChannel
			fetchByExternalReferenceCode(
				long companyId, String externalReferenceCode)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.fetchByExternalReferenceCode(
			companyId, externalReferenceCode);
	}

	@Override
	public com.liferay.commerce.product.model.CommerceChannel
			fetchCommerceChannel(long commerceChannelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.fetchCommerceChannel(commerceChannelId);
	}

	@Override
	public com.liferay.commerce.product.model.CommerceChannel
			getCommerceChannel(long commerceChannelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.getCommerceChannel(commerceChannelId);
	}

	@Override
	public com.liferay.commerce.product.model.CommerceChannel
			getCommerceChannelByOrderGroupId(long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.getCommerceChannelByOrderGroupId(
			groupId);
	}

	@Override
	public java.util.List<com.liferay.commerce.product.model.CommerceChannel>
			getCommerceChannels(int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.getCommerceChannels(start, end);
	}

	@Override
	public java.util.List<com.liferay.commerce.product.model.CommerceChannel>
			getCommerceChannels(long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.getCommerceChannels(companyId);
	}

	@Override
	public java.util.List<com.liferay.commerce.product.model.CommerceChannel>
			getCommerceChannels(
				long companyId, String keywords, int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.getCommerceChannels(
			companyId, keywords, start, end);
	}

	@Override
	public int getCommerceChannelsCount(long companyId, String keywords)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.getCommerceChannelsCount(
			companyId, keywords);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _commerceChannelService.getOSGiServiceIdentifier();
	}

	@Override
	public java.util.List<com.liferay.commerce.product.model.CommerceChannel>
			searchCommerceChannels(long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.searchCommerceChannels(companyId);
	}

	@Override
	public java.util.List<com.liferay.commerce.product.model.CommerceChannel>
			searchCommerceChannels(
				long companyId, String keywords, int start, int end,
				com.liferay.portal.kernel.search.Sort sort)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.searchCommerceChannels(
			companyId, keywords, start, end, sort);
	}

	@Override
	public int searchCommerceChannelsCount(long companyId, String keywords)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.searchCommerceChannelsCount(
			companyId, keywords);
	}

	@Override
	public com.liferay.commerce.product.model.CommerceChannel
			updateCommerceChannel(
				long commerceChannelId, long siteGroupId, String name,
				String type,
				com.liferay.portal.kernel.util.UnicodeProperties
					typeSettingsUnicodeProperties,
				String commerceCurrencyCode)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.updateCommerceChannel(
			commerceChannelId, siteGroupId, name, type,
			typeSettingsUnicodeProperties, commerceCurrencyCode);
	}

	@Override
	public com.liferay.commerce.product.model.CommerceChannel
			updateCommerceChannel(
				long commerceChannelId, long siteGroupId, String name,
				String type,
				com.liferay.portal.kernel.util.UnicodeProperties
					typeSettingsUnicodeProperties,
				String commerceCurrencyCode, String priceDisplayType,
				boolean discountsTargetNetPrice)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.updateCommerceChannel(
			commerceChannelId, siteGroupId, name, type,
			typeSettingsUnicodeProperties, commerceCurrencyCode,
			priceDisplayType, discountsTargetNetPrice);
	}

	@Override
	public com.liferay.commerce.product.model.CommerceChannel
			updateCommerceChannelExternalReferenceCode(
				long commerceChannelId, String externalReferenceCode)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceChannelService.
			updateCommerceChannelExternalReferenceCode(
				commerceChannelId, externalReferenceCode);
	}

	@Override
	public CommerceChannelService getWrappedService() {
		return _commerceChannelService;
	}

	@Override
	public void setWrappedService(
		CommerceChannelService commerceChannelService) {

		_commerceChannelService = commerceChannelService;
	}

	private CommerceChannelService _commerceChannelService;

}