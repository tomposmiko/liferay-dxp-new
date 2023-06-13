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

package com.liferay.forms.apio.internal.util;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Paulo Cruz
 */
public final class FormInstanceRepresentorUtil {

	public static List<String> getAvailableLanguages(
		DDMFormInstance ddmFormInstance) {

		Stream<String> availableLanguagesStream = Arrays.stream(
			ddmFormInstance.getAvailableLanguageIds());

		return availableLanguagesStream.collect(Collectors.toList());
	}

}