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

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.servlet.taglib.ui.BaseJSPFormNavigatorEntry;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorConstants;
import com.liferay.portal.kernel.util.Portal;

import java.util.Locale;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Pei-Jung Lan
 */
public abstract class BaseLayoutFormNavigatorEntry
	extends BaseJSPFormNavigatorEntry<Layout> {

	@Override
	public String getCategoryKey() {
		return StringPool.BLANK;
	}

	@Override
	public String getFormNavigatorId() {
		return FormNavigatorConstants.FORM_NAVIGATOR_ID_LAYOUT;
	}

	@Override
	public String getLabel(Locale locale) {
		return LanguageUtil.get(locale, getKey());
	}

	@Override
	public boolean isVisible(User user, Layout layout) {
		Layout draftLayout = layoutLocalService.fetchLayout(
			portal.getClassNameId(Layout.class), layout.getPlid());

		if (draftLayout != null) {
			return false;
		}

		return super.isVisible(user, layout);
	}

	@Reference
	protected LayoutLocalService layoutLocalService;

	@Reference
	protected Portal portal;

}