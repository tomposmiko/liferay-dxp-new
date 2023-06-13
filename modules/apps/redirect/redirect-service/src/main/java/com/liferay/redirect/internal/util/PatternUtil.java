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

package com.liferay.redirect.internal.util;

import com.liferay.petra.string.StringPool;
import com.liferay.redirect.model.RedirectPatternEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Adolfo Pérez
 */
public class PatternUtil {

	public static List<RedirectPatternEntry> parse(String[] patternStrings) {
		List<RedirectPatternEntry> redirectPatternEntries = new ArrayList<>();

		for (String patternString : patternStrings) {
			String[] parts = patternString.split("\\s+", 2);

			if ((parts.length != 2) || parts[0].isEmpty() ||
				parts[1].isEmpty()) {

				continue;
			}

			redirectPatternEntries.add(
				new RedirectPatternEntry(
					Pattern.compile(_normalize(parts[0])), parts[1],
					StringPool.BLANK));
		}

		return redirectPatternEntries;
	}

	private static String _normalize(String patternString) {
		if (patternString.startsWith(StringPool.CARET)) {
			patternString = patternString.substring(1);
		}

		if (patternString.startsWith(StringPool.SLASH)) {
			patternString = patternString.substring(1);
		}

		return StringPool.CARET + patternString;
	}

}