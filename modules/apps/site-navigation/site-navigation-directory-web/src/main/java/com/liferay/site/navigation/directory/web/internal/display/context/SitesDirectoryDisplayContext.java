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

package com.liferay.site.navigation.directory.web.internal.display.context;

import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.site.navigation.directory.web.internal.configuration.SitesDirectoryPortletInstanceConfiguration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Juergen Kappler
 */
public class SitesDirectoryDisplayContext {

	public SitesDirectoryDisplayContext(HttpServletRequest httpServletRequest)
		throws ConfigurationException {

		_httpServletRequest = httpServletRequest;

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		_sitesDirectoryPortletInstanceConfiguration =
			portletDisplay.getPortletInstanceConfiguration(
				SitesDirectoryPortletInstanceConfiguration.class);
	}

	public String getDisplayStyle() {
		if (_displayStyle != null) {
			return _displayStyle;
		}

		_displayStyle = ParamUtil.getString(
			_httpServletRequest, "displayStyle",
			_sitesDirectoryPortletInstanceConfiguration.displayStyle());

		return _displayStyle;
	}

	public String getSites() {
		if (_sites != null) {
			return _sites;
		}

		_sites = ParamUtil.getString(
			_httpServletRequest, "sites",
			_sitesDirectoryPortletInstanceConfiguration.sites());

		return _sites;
	}

	private String _displayStyle;
	private final HttpServletRequest _httpServletRequest;
	private String _sites;
	private final SitesDirectoryPortletInstanceConfiguration
		_sitesDirectoryPortletInstanceConfiguration;

}