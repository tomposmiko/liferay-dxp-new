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

package com.liferay.person.apio.internal.architect.provider;

import com.liferay.apio.architect.provider.Provider;
import com.liferay.person.apio.internal.query.FullNameQuery;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Alejandro Hernández
 */
@Component(service = Provider.class)
public class FullNameQueryProvider implements Provider<FullNameQuery> {

	@Override
	public FullNameQuery createContext(HttpServletRequest httpServletRequest) {
		return () -> Optional.ofNullable(
			httpServletRequest.getParameter("query")
		).filter(
			string -> string.startsWith("name=")
		).map(
			string -> string.substring(5)
		);
	}

}