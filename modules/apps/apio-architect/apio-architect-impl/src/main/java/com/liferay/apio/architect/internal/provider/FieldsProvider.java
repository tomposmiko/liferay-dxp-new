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

package com.liferay.apio.architect.internal.provider;

import com.liferay.apio.architect.internal.response.control.Fields;
import com.liferay.apio.architect.provider.Provider;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * Lets consumers use the {@code fields} affordance in order to select which
 * fields must be included in representations.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@Component(service = Provider.class)
public class FieldsProvider implements Provider<Fields> {

	public Fields createContext(HttpServletRequest httpServletRequest) {
		Map<String, String[]> parameterMap =
			httpServletRequest.getParameterMap();

		Set<Map.Entry<String, String[]>> set = parameterMap.entrySet();

		Stream<Map.Entry<String, String[]>> stream = set.stream();

		Map<String, List<String>> fieldsMap = stream.filter(
			entry -> {
				String key = entry.getKey();

				return key.matches(_REGEXP);
			}
		).filter(
			entry -> {
				String[] value = entry.getValue();

				if (value.length == 1) {
					return true;
				}

				return false;
			}
		).filter(
			entry -> !entry.getValue()[0].isEmpty()
		).collect(
			Collectors.toMap(
				entry -> _getTypeFunction.apply(entry.getKey()),
				entry -> Arrays.asList(entry.getValue()[0].split(",")))
		);

		return types -> field -> {
			Stream<String> typesStream = types.stream();

			List<String> fields = typesStream.map(
				fieldsMap::get
			).filter(
				Objects::nonNull
			).flatMap(
				List::stream
			).collect(
				Collectors.toList()
			);

			return fields.isEmpty() || fields.contains(field);
		};
	}

	private static final String _REGEXP = "fields\\[([A-Z|a-z]+)]";

	private static final Function<String, String> _getTypeFunction =
		key -> key.substring(key.indexOf("[") + 1, key.indexOf("]"));

}