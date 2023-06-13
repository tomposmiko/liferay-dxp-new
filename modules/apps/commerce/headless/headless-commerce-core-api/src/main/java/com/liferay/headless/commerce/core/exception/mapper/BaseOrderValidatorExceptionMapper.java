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

package com.liferay.headless.commerce.core.exception.mapper;

import com.liferay.commerce.exception.CommerceOrderValidatorException;
import com.liferay.commerce.order.CommerceOrderValidatorResult;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.vulcan.jaxrs.exception.mapper.BaseExceptionMapper;
import com.liferay.portal.vulcan.jaxrs.exception.mapper.Problem;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * @author Andrea Sbarra
 */
@Provider
public class BaseOrderValidatorExceptionMapper
	extends BaseExceptionMapper<CommerceOrderValidatorException> {

	@Override
	public Response toResponse(
		CommerceOrderValidatorException commerceOrderValidatorException) {

		return super.toResponse(commerceOrderValidatorException);
	}

	@Override
	protected Problem getProblem(
		CommerceOrderValidatorException commerceOrderValidatorException) {

		List<CommerceOrderValidatorResult> commerceOrderValidatorResults =
			commerceOrderValidatorException.getCommerceOrderValidatorResults();

		Stream<CommerceOrderValidatorResult> stream =
			commerceOrderValidatorResults.stream();

		return new Problem(
			stream.filter(
				CommerceOrderValidatorResult::hasMessageResult
			).map(
				CommerceOrderValidatorResult::getLocalizedMessage
			).collect(
				Collectors.joining(StringPool.COMMA_AND_SPACE)
			),
			Response.Status.BAD_REQUEST,
			CommerceOrderValidatorException.class.getSimpleName(),
			CommerceOrderValidatorException.class.getSimpleName());
	}

}