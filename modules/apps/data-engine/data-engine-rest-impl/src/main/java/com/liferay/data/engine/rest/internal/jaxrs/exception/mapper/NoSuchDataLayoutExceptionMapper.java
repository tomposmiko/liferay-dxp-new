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

package com.liferay.data.engine.rest.internal.jaxrs.exception.mapper;

import com.liferay.data.engine.rest.dto.v1_0.DataLayout;
import com.liferay.dynamic.data.mapping.exception.NoSuchStructureLayoutException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(osgi.jaxrs.name=Liferay.Data.Engine.REST)",
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Liferay.Data.Engine.REST.NoSuchDataLayoutExceptionMapper"
	},
	service = ExceptionMapper.class
)
public class NoSuchDataLayoutExceptionMapper
	implements ExceptionMapper<NoSuchStructureLayoutException> {

	@Override
	public Response toResponse(
		NoSuchStructureLayoutException noSuchStructureLayoutException) {

		return Response.status(
			Response.Status.NOT_FOUND
		).entity(
			new DataLayout()
		).build();
	}

}