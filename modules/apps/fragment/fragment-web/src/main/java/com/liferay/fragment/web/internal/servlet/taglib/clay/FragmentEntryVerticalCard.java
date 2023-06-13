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

package com.liferay.fragment.web.internal.servlet.taglib.clay;

import com.liferay.fragment.constants.FragmentConstants;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.web.internal.constants.FragmentWebKeys;
import com.liferay.frontend.taglib.clay.servlet.taglib.soy.BaseBaseClayCard;
import com.liferay.frontend.taglib.clay.servlet.taglib.soy.VerticalCard;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.RenderRequest;

/**
 * @author Eudaldo Alonso
 */
public abstract class FragmentEntryVerticalCard
	extends BaseBaseClayCard implements VerticalCard {

	public FragmentEntryVerticalCard(
		FragmentEntry fragmentEntry, RenderRequest renderRequest,
		RowChecker rowChecker) {

		super(fragmentEntry, rowChecker);

		this.fragmentEntry = fragmentEntry;
		themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Override
	public String getDefaultEventHandler() {
		return FragmentWebKeys.FRAGMENT_ENTRY_DROPDOWN_DEFAULT_EVENT_HANDLER;
	}

	@Override
	public String getIcon() {
		return "code";
	}

	@Override
	public String getImageSrc() {
		return fragmentEntry.getImagePreviewURL(themeDisplay);
	}

	@Override
	public String getStickerCssClass() {
		if (fragmentEntry.getType() == FragmentConstants.TYPE_COMPONENT) {
			return "file-icon-color-4";
		}

		return "file-icon-color-2";
	}

	@Override
	public String getStickerIcon() {
		if (fragmentEntry.getType() == FragmentConstants.TYPE_COMPONENT) {
			return "cards2";
		}

		return "cards-full";
	}

	@Override
	public String getTitle() {
		return HtmlUtil.escape(fragmentEntry.getName());
	}

	protected final FragmentEntry fragmentEntry;
	protected final ThemeDisplay themeDisplay;

}