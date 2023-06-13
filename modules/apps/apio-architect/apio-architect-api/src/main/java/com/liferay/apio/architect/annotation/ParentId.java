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

package com.liferay.apio.architect.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.liferay.apio.architect.identifier.Identifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Defines an annotation that provides information about a parent type ID.
 *
 * <p>
 * This annotation should always be used on an {@link
 * com.liferay.apio.architect.router.ActionRouter} method parameter representing
 * a parent resource ID.
 * </p>
 *
 * <p>
 * A {@link com.liferay.apio.architect.uri.mapper.PathIdentifierMapper} for the
 * ID type must exist for Apio to automatically convert it from the request.
 * </p>
 *
 * @author Alejandro Hernández
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface ParentId {

	/**
	 * Returns the parent resource's class.
	 *
	 * @return the parent resource's class
	 */
	public Class<? extends Identifier<?>> value();

}