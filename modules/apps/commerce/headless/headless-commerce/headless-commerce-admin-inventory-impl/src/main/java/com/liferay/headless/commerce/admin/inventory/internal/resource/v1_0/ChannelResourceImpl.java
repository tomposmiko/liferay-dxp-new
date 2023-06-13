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

package com.liferay.headless.commerce.admin.inventory.internal.resource.v1_0;

import com.liferay.commerce.product.model.CommerceChannelRel;
import com.liferay.commerce.product.service.CommerceChannelRelService;
import com.liferay.headless.commerce.admin.inventory.dto.v1_0.Channel;
import com.liferay.headless.commerce.admin.inventory.dto.v1_0.WarehouseChannel;
import com.liferay.headless.commerce.admin.inventory.internal.dto.v1_0.ChannelDTOConverter;
import com.liferay.headless.commerce.admin.inventory.resource.v1_0.ChannelResource;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.fields.NestedField;
import com.liferay.portal.vulcan.fields.NestedFieldSupport;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Crescenzo Rega
 */
@Component(
	enabled = false,
	properties = "OSGI-INF/liferay/rest/v1_0/channel.properties",
	scope = ServiceScope.PROTOTYPE,
	service = {ChannelResource.class, NestedFieldSupport.class}
)
public class ChannelResourceImpl
	extends BaseChannelResourceImpl implements NestedFieldSupport {

	@NestedField(parentClass = WarehouseChannel.class, value = "channel")
	@Override
	public Channel getWarehouseChannelChannel(Long id) throws Exception {
		CommerceChannelRel commerceChannelRel =
			_commerceChannelRelService.getCommerceChannelRel(id);

		return _channelDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				commerceChannelRel.getCommerceChannelId(),
				contextAcceptLanguage.getPreferredLocale()));
	}

	@Reference
	private ChannelDTOConverter _channelDTOConverter;

	@Reference
	private CommerceChannelRelService _commerceChannelRelService;

}