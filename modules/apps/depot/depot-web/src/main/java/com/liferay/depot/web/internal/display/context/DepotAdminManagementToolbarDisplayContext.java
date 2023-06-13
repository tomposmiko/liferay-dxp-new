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

package com.liferay.depot.web.internal.display.context;

import com.liferay.depot.constants.DepotActionKeys;
import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.web.internal.resource.DepotEntryPermission;
import com.liferay.depot.web.internal.resource.DepotPermission;
import com.liferay.depot.web.internal.util.DepotEntryURLUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.SearchContainerManagementToolbarDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alejandro Tardín
 */
public class DepotAdminManagementToolbarDisplayContext
	extends SearchContainerManagementToolbarDisplayContext {

	public DepotAdminManagementToolbarDisplayContext(
			HttpServletRequest httpServletRequest,
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			DepotAdminDisplayContext depotAdminDisplayContext)
		throws PortalException {

		super(
			httpServletRequest, liferayPortletRequest, liferayPortletResponse,
			depotAdminDisplayContext.searchContainer());

		_depotAdminDisplayContext = depotAdminDisplayContext;
	}

	@Override
	public List<DropdownItem> getActionDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.putData(
							"action", "deleteSelectedDepotEntries");
						dropdownItem.setIcon("times-circle");
						dropdownItem.setLabel(
							LanguageUtil.get(request, "delete"));
						dropdownItem.setQuickAction(true);
					});
			}
		};
	}

	@Override
	public String getClearResultsURL() {
		PortletURL clearResultsURL = getPortletURL();

		clearResultsURL.setParameter("keywords", StringPool.BLANK);
		clearResultsURL.setParameter("orderByCol", getOrderByCol());
		clearResultsURL.setParameter("orderByType", getOrderByType());

		return clearResultsURL.toString();
	}

	public Map<String, Object> getComponentContext() throws PortalException {
		return HashMapBuilder.<String, Object>put(
			"deleteDepotEntriesURL",
			() -> {
				PortletURL deleteDepotEntries =
					liferayPortletResponse.createActionURL();

				deleteDepotEntries.setParameter(
					ActionRequest.ACTION_NAME, "/depot_entry/delete");

				return deleteDepotEntries.toString();
			}
		).build();
	}

	@Override
	public String getComponentId() {
		return "depotAdminManagementToolbar";
	}

	@Override
	public CreationMenu getCreationMenu() {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		try {
			PortletURL addDepotEntryURL =
				DepotEntryURLUtil.getAddDepotEntryActionURL(
					themeDisplay.getURLCurrent(), liferayPortletResponse);

			return new CreationMenu() {
				{
					addPrimaryDropdownItem(
						dropdownItem -> {
							dropdownItem.putData("action", "addDepotEntry");
							dropdownItem.putData(
								"addDepotEntryURL",
								addDepotEntryURL.toString());
							dropdownItem.setLabel(
								LanguageUtil.get(request, "add"));
						});
				}
			};
		}
		catch (Exception exception) {
		}

		return null;
	}

	@Override
	public String getDefaultEventHandler() {
		return "depotAdminManagementToolbarDefaultEventHandler";
	}

	public Map<String, Object> getRowData(DepotEntry depotEntry)
		throws PortalException {

		return HashMapBuilder.<String, Object>put(
			"actions", StringUtil.merge(_getAvailableActions(depotEntry))
		).build();
	}

	@Override
	public String getSearchActionURL() {
		PortletURL searchTagURL = getPortletURL();

		searchTagURL.setParameter("orderByCol", getOrderByCol());
		searchTagURL.setParameter("orderByType", getOrderByType());

		return searchTagURL.toString();
	}

	@Override
	public Boolean isShowCreationMenu() {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		return DepotPermission.contains(
			themeDisplay.getPermissionChecker(), themeDisplay.getScopeGroupId(),
			DepotActionKeys.ADD_DEPOT_ENTRY);
	}

	@Override
	protected String getDefaultDisplayStyle() {
		return _depotAdminDisplayContext.getDefaultDisplayStyle();
	}

	@Override
	protected String[] getDisplayViews() {
		return new String[] {"list", "descriptive", "icon"};
	}

	@Override
	protected String[] getNavigationKeys() {
		return new String[] {"all"};
	}

	@Override
	protected String[] getOrderByKeys() {
		return new String[] {"descriptive-name"};
	}

	private List<String> _getAvailableActions(DepotEntry depotEntry)
		throws PortalException {

		List<String> availableActions = new ArrayList<>();

		if (_hasDeleteDepotEntryPermission(depotEntry)) {
			availableActions.add("deleteSelectedDepotEntries");
		}

		return availableActions;
	}

	private boolean _hasDeleteDepotEntryPermission(DepotEntry depotEntry)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (!DepotEntryPermission.contains(
				themeDisplay.getPermissionChecker(),
				depotEntry.getDepotEntryId(), ActionKeys.DELETE)) {

			return false;
		}

		return true;
	}

	private final DepotAdminDisplayContext _depotAdminDisplayContext;

}