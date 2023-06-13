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

package com.liferay.layout.admin.web.internal.servlet.taglib.ui;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorConstants;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pei-Jung Lan
 */
@Component(
	property = "form.navigator.entry.order:Integer=90",
	service = FormNavigatorEntry.class
)
public class LayoutSEOFormNavigatorEntry extends BaseLayoutFormNavigatorEntry {

	@Override
	public String getCategoryKey() {
		return FormNavigatorConstants.CATEGORY_KEY_LAYOUT_SEO;
	}

	@Override
	public String getKey() {
		return "seo";
	}

	@Override
	public boolean isVisible(User user, Layout layout) {
		Group group = _groupLocalService.fetchGroup(layout.getGroupId());

		if ((group != null) && group.isLayoutPrototype()) {
			return false;
		}

		return super.isVisible(user, layout);
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.layout.admin.web)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

	@Override
	protected String getJspPath() {
		return "/layout/seo.jsp";
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutSEOFormNavigatorEntry.class);

	@Reference
	private GroupLocalService _groupLocalService;

}