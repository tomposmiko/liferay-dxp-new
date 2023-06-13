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

package com.liferay.apio.architect.sample.liferay.portal.internal.converter;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import com.liferay.apio.architect.converter.ExceptionConverter;
import com.liferay.apio.architect.error.APIError;
import com.liferay.document.library.kernel.exception.DuplicateFileEntryException;

import org.osgi.service.component.annotations.Component;

/**
 * Converts a {@code DuplicateFileEntryException} to its {@link APIError}
 * representation.
 *
 * @author Alejandro Hernández
 */
@Component(immediate = true)
public class DuplicateFileEntryExceptionConverter
	implements ExceptionConverter<DuplicateFileEntryException> {

	@Override
	public APIError convert(DuplicateFileEntryException dfee) {
		return new APIError(
			dfee, "Duplicate entry", "bad-request",
			BAD_REQUEST.getStatusCode());
	}

}