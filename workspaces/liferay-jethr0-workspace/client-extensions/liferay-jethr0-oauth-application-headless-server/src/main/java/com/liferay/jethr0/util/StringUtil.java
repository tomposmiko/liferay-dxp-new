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

package com.liferay.jethr0.util;

import java.util.Locale;

/**
 * @author Michael Hashimoto
 */
public class StringUtil {

	public static String combine(Object... objects) {
		StringBuilder sb = new StringBuilder();

		for (Object object : objects) {
			sb.append(String.valueOf(object));
		}

		return sb.toString();
	}

	public static String toLowerCase(String s) {
		return toLowerCase(s, LocaleUtil.getDefault());
	}

	public static String toLowerCase(String s, Locale locale) {
		if (s == null) {
			return null;
		}

		StringBuilder sb = null;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c > 127) {

				// Found non-ascii char, fallback to the slow unicode detection

				if (locale == null) {
					locale = LocaleUtil.getDefault();
				}

				return s.toLowerCase(locale);
			}

			if ((c >= 'A') && (c <= 'Z')) {
				if (sb == null) {
					sb = new StringBuilder(s);
				}

				sb.setCharAt(i, (char)(c + 32));
			}
		}

		if (sb == null) {
			return s;
		}

		return sb.toString();
	}

}