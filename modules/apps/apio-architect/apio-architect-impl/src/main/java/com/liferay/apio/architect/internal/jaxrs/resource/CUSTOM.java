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

package com.liferay.apio.architect.internal.jaxrs.resource;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.ws.rs.HttpMethod;

/**
 * Indicates that the annotated method responds to HTTP custom requests (other
 * than the HTTP methods included in {@link HttpMethod}).
 *
 * @author Alejandro Hernández
 * @review
 */
@HttpMethod(CUSTOM.NAME)
@Retention(RUNTIME)
@Target(METHOD)
public @interface CUSTOM {

	public static final String NAME = "CUSTOM";

}