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

package com.liferay.asset.list.web.internal.servlet.taglib.util;

import com.liferay.asset.list.web.internal.display.context.EditAssetListDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.segments.constants.SegmentsEntryConstants;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Diego Hu
 */
public class AssetListEntryVariationActionDropdownItemsProvider {

	public AssetListEntryVariationActionDropdownItemsProvider(
		EditAssetListDisplayContext editAssetListDisplayContext,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		_editAssetListDisplayContext = editAssetListDisplayContext;
		_liferayPortletResponse = liferayPortletResponse;

		_httpServletRequest = PortalUtil.getHttpServletRequest(
			liferayPortletRequest);
		_themeDisplay = (ThemeDisplay)liferayPortletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public List<DropdownItem> getActionDropdownItems() {
		return DropdownItemListBuilder.add(
			dropdownItem -> {
				dropdownItem.putData(
					"action", "viewAssetListEntryVariationItems");
				dropdownItem.putData(
					"viewAssetListEntryVariationItemsURL",
					PortletURLBuilder.createRenderURL(
						_liferayPortletResponse
					).setMVCPath(
						"/view_asset_list_items.jsp"
					).setRedirect(
						_themeDisplay.getURLCurrent()
					).setParameter(
						"assetListEntryId",
						_editAssetListDisplayContext.getAssetListEntryId()
					).setParameter(
						"segmentsEntryId",
						_editAssetListDisplayContext.getSegmentsEntryId()
					).setWindowState(
						LiferayWindowState.POP_UP
					).buildString());
				dropdownItem.setIcon("view");
				dropdownItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "view-items"));
			}
		).add(
			() ->
				(_editAssetListDisplayContext.getSegmentsEntryId() !=
					SegmentsEntryConstants.ID_DEFAULT) &&
				!_editAssetListDisplayContext.isLiveGroup(),
			dropdownItem -> {
				dropdownItem.putData("action", "deleteAssetListEntryVariation");
				dropdownItem.putData(
					"deleteAssetListEntryVariationURL",
					PortletURLBuilder.createActionURL(
						_liferayPortletResponse
					).setActionName(
						"/asset_list/delete_asset_list_entry_variation"
					).setParameter(
						"assetListEntryId",
						_editAssetListDisplayContext.getAssetListEntryId()
					).setParameter(
						"segmentsEntryId",
						_editAssetListDisplayContext.getSegmentsEntryId()
					).buildString());
				dropdownItem.setIcon("trash");
				dropdownItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "delete"));
			}
		).build();
	}

	private final EditAssetListDisplayContext _editAssetListDisplayContext;
	private final HttpServletRequest _httpServletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final ThemeDisplay _themeDisplay;

}