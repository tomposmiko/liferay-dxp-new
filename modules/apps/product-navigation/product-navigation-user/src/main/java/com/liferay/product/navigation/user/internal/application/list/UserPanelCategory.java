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

package com.liferay.product.navigation.user.internal.application.list;

import com.liferay.application.list.BaseJSPPanelCategory;
import com.liferay.application.list.constants.ApplicationListWebKeys;
import com.liferay.application.list.constants.PanelCategoryKeys;

import java.io.IOException;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author     Adolfo Pérez
 * @deprecated As of Mueller (7.2.x), with no direct replacement
 * @see com.liferay.product.navigation.personal.menu.PersonalMenuEntry
 */
@Deprecated
public class UserPanelCategory extends BaseJSPPanelCategory {

	@Override
	public String getHeaderJspPath() {
		return "/user_header.jsp";
	}

	@Override
	public String getJspPath() {
		return "/user_body.jsp";
	}

	@Override
	public String getKey() {
		return PanelCategoryKeys.USER;
	}

	@Override
	public String getLabel(Locale locale) {
		return null;
	}

	@Override
	public boolean include(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		request.setAttribute(ApplicationListWebKeys.PANEL_CATEGORY, this);

		return super.include(request, response);
	}

	@Override
	public boolean includeHeader(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		request.setAttribute(ApplicationListWebKeys.PANEL_CATEGORY, this);

		return super.includeHeader(request, response);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

}