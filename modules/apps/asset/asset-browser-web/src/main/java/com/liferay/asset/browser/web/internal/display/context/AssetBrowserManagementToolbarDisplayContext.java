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

package com.liferay.asset.browser.web.internal.display.context;

import com.liferay.asset.browser.web.internal.constants.AssetBrowserPortletKeys;
import com.liferay.asset.constants.AssetWebKeys;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.util.AssetHelper;
import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.SearchContainerManagementToolbarDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenuUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class AssetBrowserManagementToolbarDisplayContext
	extends SearchContainerManagementToolbarDisplayContext {

	public AssetBrowserManagementToolbarDisplayContext(
		HttpServletRequest httpServletRequest,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		AssetBrowserDisplayContext assetBrowserDisplayContext) {

		super(
			httpServletRequest, liferayPortletRequest, liferayPortletResponse,
			assetBrowserDisplayContext.getAssetBrowserSearch());

		_assetBrowserDisplayContext = assetBrowserDisplayContext;

		_assetHelper = (AssetHelper)httpServletRequest.getAttribute(
			AssetWebKeys.ASSET_HELPER);
		_portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(
			liferayPortletRequest);
		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Override
	public String getClearResultsURL() {
		PortletURL clearResultsURL = getPortletURL();

		clearResultsURL.setParameter("keywords", StringPool.BLANK);

		return clearResultsURL.toString();
	}

	@Override
	public String getComponentId() {
		return "assetBrowserManagementToolbar";
	}

	@Override
	public CreationMenu getCreationMenu() {
		if (!_assetBrowserDisplayContext.isShowAddButton()) {
			return null;
		}

		String addButtonURL = _getAddButtonURL();

		if (Validator.isNull(addButtonURL)) {
			return null;
		}

		return CreationMenuUtil.addPrimaryDropdownItem(
			dropdownItem -> {
				dropdownItem.setHref(addButtonURL);
				dropdownItem.setLabel(
					LanguageUtil.format(
						request, "add-x", _getAddButtonLabel(), false));
			});
	}

	@Override
	public List<DropdownItem> getFilterNavigationDropdownItems() {
		long[] groupIds = _assetBrowserDisplayContext.getSelectedGroupIds();

		if (groupIds.length <= 1) {
			return null;
		}

		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setActive(
							_assetBrowserDisplayContext.getGroupId() == 0);
						dropdownItem.setHref(getPortletURL(), "groupId", 0);
						dropdownItem.setLabel(LanguageUtil.get(request, "all"));
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
								getPortletURL(), "groupId", groupId);
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
	public String getSearchActionURL() {
		PortletURL searchActionURL = getPortletURL();

		return searchActionURL.toString();
	}

	@Override
	public String getSearchContainerId() {
		return "selectAssetEntries";
	}

	@Override
	public Boolean isDisabled() {
		return false;
	}

	@Override
	public Boolean isSelectable() {
		return _assetBrowserDisplayContext.isMultipleSelection();
	}

	@Override
	protected String[] getDisplayViews() {
		return new String[] {"list", "descriptive", "icon"};
	}

	@Override
	protected String getFilterNavigationDropdownItemsLabel() {
		return LanguageUtil.get(request, "sites");
	}

	@Override
	protected String getOrderByCol() {
		if (_orderByCol != null) {
			return _orderByCol;
		}

		String orderByCol = ParamUtil.getString(
			liferayPortletRequest, getOrderByColParam());

		if (Validator.isNotNull(orderByCol)) {
			_portalPreferences.setValue(
				AssetBrowserPortletKeys.ASSET_BROWSER, "order-by-col",
				orderByCol);
		}
		else {
			orderByCol = _portalPreferences.getValue(
				AssetBrowserPortletKeys.ASSET_BROWSER, "order-by-col",
				"modified-date");
		}

		_orderByCol = orderByCol;

		return _orderByCol;
	}

	@Override
	protected String[] getOrderByKeys() {
		return new String[] {"title", "modified-date"};
	}

	@Override
	protected String getOrderByType() {
		if (_orderByType != null) {
			return _orderByType;
		}

		String orderByType = ParamUtil.getString(
			liferayPortletRequest, getOrderByTypeParam());

		if (Validator.isNotNull(orderByType)) {
			_portalPreferences.setValue(
				AssetBrowserPortletKeys.ASSET_BROWSER, "order-by-type",
				orderByType);
		}
		else {
			orderByType = _portalPreferences.getValue(
				AssetBrowserPortletKeys.ASSET_BROWSER, "order-by-type", "asc");
		}

		_orderByType = orderByType;

		return _orderByType;
	}

	private String _getAddButtonLabel() {
		AssetRendererFactory assetRendererFactory =
			_assetBrowserDisplayContext.getAssetRendererFactory();

		if (assetRendererFactory.isSupportsClassTypes() &&
			(_assetBrowserDisplayContext.getSubtypeSelectionId() > 0)) {

			return assetRendererFactory.getTypeName(
				_themeDisplay.getLocale(),
				_assetBrowserDisplayContext.getSubtypeSelectionId());
		}

		return assetRendererFactory.getTypeName(_themeDisplay.getLocale());
	}

	private String _getAddButtonURL() {
		long groupId = _assetBrowserDisplayContext.getGroupId();

		if (groupId == 0) {
			groupId = _themeDisplay.getScopeGroupId();
		}

		AssetRendererFactory assetRendererFactory =
			_assetBrowserDisplayContext.getAssetRendererFactory();

		PortletURL addPortletURL = null;

		try {
			if (assetRendererFactory.isSupportsClassTypes() &&
				(_assetBrowserDisplayContext.getSubtypeSelectionId() > 0)) {

				addPortletURL = _assetHelper.getAddPortletURL(
					liferayPortletRequest, liferayPortletResponse, groupId,
					_assetBrowserDisplayContext.getTypeSelection(),
					_assetBrowserDisplayContext.getSubtypeSelectionId(), null,
					null, getPortletURL().toString());
			}
			else {
				addPortletURL = _assetHelper.getAddPortletURL(
					liferayPortletRequest, liferayPortletResponse, groupId,
					_assetBrowserDisplayContext.getTypeSelection(), 0, null,
					null, getPortletURL().toString());
			}
		}
		catch (Exception exception) {
			return StringPool.BLANK;
		}

		if (addPortletURL == null) {
			return StringPool.BLANK;
		}

		addPortletURL.setParameter("groupId", String.valueOf(groupId));

		return HttpUtil.addParameter(
			addPortletURL.toString(), "refererPlid", _themeDisplay.getPlid());
	}

	private final AssetBrowserDisplayContext _assetBrowserDisplayContext;
	private final AssetHelper _assetHelper;
	private String _orderByCol;
	private String _orderByType;
	private final PortalPreferences _portalPreferences;
	private final ThemeDisplay _themeDisplay;

}