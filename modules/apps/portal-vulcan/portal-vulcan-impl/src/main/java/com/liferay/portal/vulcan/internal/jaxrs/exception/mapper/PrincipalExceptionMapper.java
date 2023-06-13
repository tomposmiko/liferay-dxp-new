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

package com.liferay.portal.vulcan.internal.jaxrs.exception.mapper;

import com.liferay.portal.kernel.security.auth.PrincipalException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Converts any {@code PrincipalException} to a {@code 404} error.
 *
 * @author Brian Wing Shun Chan
 * @review
 */
public class PrincipalExceptionMapper
	implements ExceptionMapper<PrincipalException> {

	@Override
	public Response toResponse(PrincipalException pe) {
		return Response.status(
			Response.Status.NOT_FOUND
		).build();
	}

}