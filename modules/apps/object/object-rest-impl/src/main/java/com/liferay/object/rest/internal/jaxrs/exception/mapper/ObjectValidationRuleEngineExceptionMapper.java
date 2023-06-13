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

package com.liferay.object.rest.internal.jaxrs.exception.mapper;

import com.liferay.object.exception.ObjectValidationRuleEngineException;
import com.liferay.portal.vulcan.jaxrs.exception.mapper.BaseExceptionMapper;
import com.liferay.portal.vulcan.jaxrs.exception.mapper.Problem;

import javax.ws.rs.core.Response;

/**
 * @author Luis Miguel Barcos
 */
public class ObjectValidationRuleEngineExceptionMapper
	extends BaseExceptionMapper<ObjectValidationRuleEngineException> {

	@Override
	public Response toResponse(
		ObjectValidationRuleEngineException
			objectValidationRuleEngineException) {

		Problem problem = getProblem(objectValidationRuleEngineException);

		return Response.status(
			problem.getStatus()
		).entity(
			problem
		).type(
			getMediaType()
		).build();
	}

	@Override
	protected Problem getProblem(
		ObjectValidationRuleEngineException
			objectValidationRuleEngineException) {

		return new Problem(
			Response.Status.BAD_REQUEST,
			objectValidationRuleEngineException.getMessage());
	}

}