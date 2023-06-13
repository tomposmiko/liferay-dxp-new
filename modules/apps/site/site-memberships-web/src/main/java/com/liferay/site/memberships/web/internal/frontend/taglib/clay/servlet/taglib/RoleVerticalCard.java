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

package com.liferay.site.memberships.web.internal.frontend.taglib.clay.servlet.taglib;

import com.liferay.frontend.taglib.clay.servlet.taglib.BaseBaseClayCard;
import com.liferay.frontend.taglib.clay.servlet.taglib.VerticalCard;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.RenderRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class RoleVerticalCard extends BaseBaseClayCard implements VerticalCard {

	public RoleVerticalCard(
		Role role, RenderRequest renderRequest, RowChecker rowChecker) {

		super(role, rowChecker);

		_role = role;

		_httpServletRequest = PortalUtil.getHttpServletRequest(renderRequest);
	}

	@Override
	public String getIcon() {
		return "users";
	}

	@Override
	public String getSubtitle() {
		return LanguageUtil.get(_httpServletRequest, _role.getTypeLabel());
	}

	@Override
	public String getTitle() {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return _role.getTitle(themeDisplay.getLocale());
	}

	private final HttpServletRequest _httpServletRequest;
	private final Role _role;

}