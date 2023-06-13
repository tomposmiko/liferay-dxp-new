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

package com.liferay.headless.document.library.internal.jaxrs.exception.mapper;

import com.liferay.document.library.kernel.exception.DuplicateFileEntryException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.osgi.service.component.annotations.Component;

/**
 * Converts any {@code DuplicateFileEntryException} to a {@code 409} error.
 *
 * @author Alejandro Hernández
 * @review
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(osgi.jaxrs.name=Liferay.Headless.Document.Library)",
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Liferay.Headless.Document.Library.DuplicateFileEntryExceptionMapper"
	},
	service = ExceptionMapper.class
)
public class DuplicateFileEntryExceptionMapper
	implements ExceptionMapper<DuplicateFileEntryException> {

	@Override
	public Response toResponse(DuplicateFileEntryException dfee) {
		String message = dfee.getMessage();

		message = message.replace("file entry", "document");

		return Response.status(
			409
		).type(
			MediaType.TEXT_PLAIN
		).entity(
			message
		).build();
	}

}