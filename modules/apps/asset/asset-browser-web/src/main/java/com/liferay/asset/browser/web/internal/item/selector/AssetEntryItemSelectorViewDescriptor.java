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

package com.liferay.asset.browser.web.internal.item.selector;

import com.liferay.asset.browser.web.internal.display.context.AssetBrowserDisplayContext;
import com.liferay.asset.browser.web.internal.display.context.AssetBrowserManagementToolbarDisplayContext;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.ItemSelectorViewDescriptor;
import com.liferay.item.selector.criteria.AssetEntryItemSelectorReturnType;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;

import javax.portlet.PortletException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Barbara Cabrera
 */
public class AssetEntryItemSelectorViewDescriptor
	implements ItemSelectorViewDescriptor<AssetEntry> {

	public AssetEntryItemSelectorViewDescriptor(
		HttpServletRequest httpServletRequest,
		AssetBrowserDisplayContext assetBrowserDisplayContext,
		AssetBrowserManagementToolbarDisplayContext
			assetBrowserManagementToolbarDisplayContext) {

		_httpServletRequest = httpServletRequest;
		_assetBrowserDisplayContext = assetBrowserDisplayContext;
		_assetBrowserManagementToolbarDisplayContext =
			assetBrowserManagementToolbarDisplayContext;
	}

	@Override
	public String getDefaultDisplayStyle() {
		return "list";
	}

	@Override
	public List<DropdownItem> getFilterNavigationDropdownItems() {
		return _assetBrowserManagementToolbarDisplayContext.
			getFilterNavigationDropdownItems();
	}

	@Override
	public ItemDescriptor getItemDescriptor(AssetEntry assetEntry) {
		return new AssetEntryItemDescriptor(
			_assetBrowserDisplayContext, assetEntry, _httpServletRequest);
	}

	@Override
	public ItemSelectorReturnType getItemSelectorReturnType() {
		return new AssetEntryItemSelectorReturnType();
	}

	@Override
	public String[] getOrderByKeys() {
		return new String[] {"title", "modified-date"};
	}

	@Override
	public SearchContainer<AssetEntry> getSearchContainer()
		throws PortalException {

		try {
			return _assetBrowserDisplayContext.getAssetBrowserSearch();
		}
		catch (PortletException portletException) {
			throw new PortalException(portletException);
		}
	}

	@Override
	public boolean isShowSearch() {
		return true;
	}

	private final AssetBrowserDisplayContext _assetBrowserDisplayContext;
	private final AssetBrowserManagementToolbarDisplayContext
		_assetBrowserManagementToolbarDisplayContext;
	private final HttpServletRequest _httpServletRequest;

}