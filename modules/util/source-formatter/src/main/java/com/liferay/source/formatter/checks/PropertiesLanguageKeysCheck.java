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

package com.liferay.source.formatter.checks;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.io.unsync.UnsyncStringReader;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Peter Shin
 */
public class PropertiesLanguageKeysCheck extends BaseFileCheck {

	public void setAllowedLanguageKeys(String allowedLanguageKeys) {
		Collections.addAll(
			_allowedLanguageKeys, StringUtil.split(allowedLanguageKeys));
	}

	@Override
	protected String doProcess(
			String fileName, String absolutePath, String content)
		throws Exception {

		if (!fileName.endsWith("/content/Language.properties")) {
			return content;
		}

		try (UnsyncBufferedReader unsyncBufferedReader =
				new UnsyncBufferedReader(new UnsyncStringReader(content))) {

			String line = null;

			while ((line = unsyncBufferedReader.readLine()) != null) {
				String[] array = line.split("=", 2);

				if (array.length < 2) {
					continue;
				}

				String key = array[0];

				if (_isAllowedLanguageKey(key)) {
					continue;
				}

				String value = array[1];

				if (value.matches("(?s).*<([a-zA-Z0-9]+)[^>]*>.*?<\\/\\1>.*")) {
					addMessage(
						fileName, "Remove HTML markup for '" + key + "'",
						"language_keys.markdown",
						getLineCount(content, content.indexOf(line)));
				}
			}
		}

		return content;
	}

	private boolean _isAllowedLanguageKey(String key) {
		String s = key.replaceAll("[^\\w.-]", StringPool.BLANK);

		for (String allowedLanguageKey : _allowedLanguageKeys) {
			if (s.equals(allowedLanguageKey)) {
				return true;
			}
		}

		return false;
	}

	private final List<String> _allowedLanguageKeys = new ArrayList<>();

}