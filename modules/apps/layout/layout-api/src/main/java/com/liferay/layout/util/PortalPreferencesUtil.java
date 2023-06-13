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

package com.liferay.layout.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rubén Pulido
 */
public class PortalPreferencesUtil {

	public static List<String> getSortedPortalPreferencesValues(
		PortalPreferences portalPreferences, String namespace, String key) {

		return ListUtil.fromArray(
			_removeNumberPrefix(
				portalPreferences.getValues(namespace, key, new String[0])));
	}

	public static void updateSortedPortalPreferencesValues(
		PortalPreferences portalPreferences, String namespace, String key,
		String[] sortedValues) {

		portalPreferences.setValues(
			namespace, key, _addNumberPrefix(sortedValues));
	}

	private static String[] _addNumberPrefix(String[] sortedValues) {
		String[] numberedSortedValues = new String[sortedValues.length];

		for (int i = 0; i < sortedValues.length; i++) {
			numberedSortedValues[i] =
				i + StringPool.DOUBLE_DOLLAR + sortedValues[i];
		}

		return numberedSortedValues;
	}

	private static String[] _removeNumberPrefix(String[] numberedValues) {
		String[] sortedValues = new String[numberedValues.length];

		Map<String, String> numberValuesMap = new HashMap<>();

		for (String numberedValue : numberedValues) {
			numberValuesMap.put(
				numberedValue.substring(
					0, numberedValue.indexOf(StringPool.DOUBLE_DOLLAR)),
				numberedValue.substring(
					numberedValue.indexOf(StringPool.DOUBLE_DOLLAR) + 2));
		}

		for (int i = 0; i < numberedValues.length; i++) {
			sortedValues[i] = numberValuesMap.get(String.valueOf(i));
		}

		return sortedValues;
	}

}