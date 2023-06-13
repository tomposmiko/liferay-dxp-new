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

package com.liferay.portal.tools.rest.builder.internal.util;

/**
 * @author Javier Gamarra
 */
public class PathUtil {

	public static String getLastSegment(String path, int offsetIndex) {
		int index = _getLastIndexof(path, "/", offsetIndex);

		if (index == -1) {
			return "";
		}

		return CamelCaseUtil.toCamelCase(path.substring(index), true);
	}

	private static int _getLastIndexof(
		String sourceString, String matchingString, int offsetIndex) {

		int index = sourceString.indexOf(matchingString);

		if (index == -1) {
			return -1;
		}

		for (int i = 1; i < offsetIndex; i++) {
			index = sourceString.indexOf(matchingString, index + 1);

			if (index == -1) {
				return -1;
			}
		}

		return index;
	}

}