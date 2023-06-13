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
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.ItemSelectorViewDescriptor;
import com.liferay.item.selector.criteria.AssetEntryItemSelectorReturnType;
import com.liferay.item.selector.criteria.asset.criterion.AssetEntryItemSelectorCriterion;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Barbara Cabrera
 */
public class AssetEntryItemSelectorViewDescriptor
	implements ItemSelectorViewDescriptor<AssetEntry> {

	public AssetEntryItemSelectorViewDescriptor(
		HttpServletRequest httpServletRequest,
		AssetBrowserDisplayContext assetBrowserDisplayContext,
		AssetEntryItemSelectorCriterion assetEntryItemSelectorCriterion,
		PortletURL portletURL) {

		_httpServletRequest = httpServletRequest;
		_assetBrowserDisplayContext = assetBrowserDisplayContext;
		_assetEntryItemSelectorCriterion = assetEntryItemSelectorCriterion;
		_portletURL = portletURL;

		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Override
	public String getDefaultDisplayStyle() {
		return "list";
	}

	@Override
	public List<DropdownItem> getFilterNavigationDropdownItems() {
		long[] groupIds = _assetBrowserDisplayContext.getSelectedGroupIds();

		if (groupIds.length <= 1) {
			return DropdownItemListBuilder.add(
				dropdownItem -> {
					dropdownItem.setActive(
						_assetBrowserDisplayContext.isSearchEverywhere());
					dropdownItem.setHref(_portletURL, "scope", "everywhere");
					dropdownItem.setLabel(
						LanguageUtil.get(_httpServletRequest, "everywhere"));
				}
			).add(
				dropdownItem -> {
					dropdownItem.setActive(
						!_assetBrowserDisplayContext.isSearchEverywhere());
					dropdownItem.setHref(_portletURL, "scope", "current");
					dropdownItem.setLabel(_getCurrentScopeLabel());
				}
			).build();
		}

		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setActive(
							_assetBrowserDisplayContext.isSearchEverywhere());
						dropdownItem.setHref(
							_portletURL, "scope", "everywhere", "groupId",
							null);
						dropdownItem.setLabel(
							LanguageUtil.get(
								_httpServletRequest, "everywhere"));
					});

				for (long groupId : groupIds) {
					Group group = GroupLocalServiceUtil.fetchGroup(groupId);

					if (group == null) {
						continue;
					}

					add(
						dropdownItem -> {
							dropdownItem.setActive(
								_assetBrowserDisplayContext.getGroupId() ==
									groupId);
							dropdownItem.setHref(
								_portletURL, "groupId", groupId, "scope",
								"current");
							dropdownItem.setLabel(
								HtmlUtil.escape(
									group.getDescriptiveName(
										_themeDisplay.getLocale())));
						});
				}
			}
		};
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

		return _assetBrowserDisplayContext.getAssetEntrySearchContainer();
	}

	@Override
	public boolean isMultipleSelection() {
		return !_assetEntryItemSelectorCriterion.isSingleSelect();
	}

	@Override
	public boolean isShowSearch() {
		return true;
	}

	private String _getCurrentScopeLabel() {
		Group group = _themeDisplay.getScopeGroup();

		if (group.isSite()) {
			return LanguageUtil.get(_httpServletRequest, "current-site");
		}

		if (group.isOrganization()) {
			return LanguageUtil.get(
				_httpServletRequest, "current-organization");
		}

		if (group.isDepot()) {
			return LanguageUtil.get(
				_httpServletRequest, "current-asset-library");
		}

		return LanguageUtil.get(_httpServletRequest, "current-scope");
	}

	private final AssetBrowserDisplayContext _assetBrowserDisplayContext;
	private final AssetEntryItemSelectorCriterion
		_assetEntryItemSelectorCriterion;
	private final HttpServletRequest _httpServletRequest;
	private final PortletURL _portletURL;
	private final ThemeDisplay _themeDisplay;

}