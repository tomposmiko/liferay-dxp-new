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

package com.liferay.portal.search.web.internal.portlet.preferences;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.PortletPreferences;

/**
 * @author André de Oliveira
 */
public abstract class BasePortletPreferences {

	public BasePortletPreferences(PortletPreferences portletPreferences) {
		_portletPreferences = portletPreferences;
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return GetterUtil.getBoolean(_getValue(key), defaultValue);
	}

	public int getInteger(String key, int defaultValue) {
		return GetterUtil.getInteger(_getValue(key), defaultValue);
	}

	public String getString(String key, String defaultValue) {
		return GetterUtil.getString(_getValue(key), defaultValue);
	}

	private String _getValue(String key) {
		if (_portletPreferences == null) {
			return null;
		}

		String value = _portletPreferences.getValue(key, StringPool.BLANK);

		value = StringUtil.trim(value);

		if (Validator.isBlank(value)) {
			return null;
		}

		return value;
	}

	private final PortletPreferences _portletPreferences;

}