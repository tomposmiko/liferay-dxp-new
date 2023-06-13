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

package com.liferay.headless.commerce.admin.pricing.internal.jaxrs.exception.mapper;

import com.liferay.commerce.constants.CommercePriceConstants;
import com.liferay.commerce.price.list.exception.CommercePriceListMaxPriceValueException;
import com.liferay.headless.commerce.core.exception.mapper.BaseExceptionMapper;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.LocaleUtil;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Chiappa
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(osgi.jaxrs.name=Liferay.Headless.Commerce.Admin.Pricing)",
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Liferay.Headless.Commerce.Admin.Pricing.PriceListMaxPriceValueExceptionMapper"
	},
	service = ExceptionMapper.class
)
@Provider
public class PriceListMaxPriceValueExceptionMapper
	extends BaseExceptionMapper<CommercePriceListMaxPriceValueException> {

	@Override
	public String getErrorDescription() {
		return _language.format(
			LocaleUtil.US, "price-max-value-is-x",
			CommercePriceConstants.PRICE_VALUE_MAX);
	}

	@Override
	public Response.Status getStatus() {
		return Response.Status.BAD_REQUEST;
	}

	@Reference
	private Language _language;

}