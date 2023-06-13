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

package com.liferay.portal.vulcan.dto.converter;

/**
 * @author Rubén Pulido
 * @author Víctor Galán
 */
public interface DTOConverter<E, D> {

	public String getContentType();

	public default E getObject(String externalReferenceCode) throws Exception {
		return null;
	}

	public default D toDTO(DTOConverterContext dtoConverterContext)
		throws Exception {

		E object = getObject(String.valueOf(dtoConverterContext.getId()));

		return toDTO(dtoConverterContext, object);
	}

	public default D toDTO(DTOConverterContext dtoConverterContext, E object)
		throws Exception {

		return null;
	}

	public default D toDTO(E object) throws Exception {
		return toDTO(null, object);
	}

}