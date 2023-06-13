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

package com.liferay.fragment.web.internal.display.context;

import com.liferay.fragment.constants.FragmentActionKeys;
import com.liferay.fragment.web.internal.security.permission.resource.FragmentPermission;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class InheritedFragmentManagementToolbarDisplayContext
	extends FragmentManagementToolbarDisplayContext {

	public InheritedFragmentManagementToolbarDisplayContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		HttpServletRequest httpServletRequest,
		FragmentDisplayContext fragmentDisplayContext) {

		super(
			liferayPortletRequest, liferayPortletResponse, httpServletRequest,
			fragmentDisplayContext.getFragmentEntriesSearchContainer(),
			fragmentDisplayContext);
	}

	@Override
	public List<DropdownItem> getActionDropdownItems() {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.putData(
							"action", "exportSelectedFragmentEntries");
						dropdownItem.setIcon("import-export");
						dropdownItem.setLabel(
							LanguageUtil.get(request, "export"));
						dropdownItem.setQuickAction(true);
					});

				if (FragmentPermission.contains(
						themeDisplay.getPermissionChecker(),
						themeDisplay.getScopeGroupId(),
						FragmentActionKeys.MANAGE_FRAGMENT_ENTRIES)) {

					add(
						dropdownItem -> {
							dropdownItem.putData(
								"action", "copyToSelectedFragmentEntries");
							dropdownItem.setIcon("paste");
							dropdownItem.setLabel(
								LanguageUtil.get(request, "copy-to"));
							dropdownItem.setQuickAction(true);
						});
				}
			}
		};
	}

	@Override
	public Map<String, Object> getComponentContext() throws Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Map<String, Object> componentContext = new HashMap<>();

		ResourceURL exportFragmentEntriesURL =
			liferayPortletResponse.createResourceURL();

		exportFragmentEntriesURL.setResourceID(
			"/fragment/export_fragment_entries");

		componentContext.put(
			"exportFragmentEntriesURL", exportFragmentEntriesURL.toString());

		long fragmentCollectionId = ParamUtil.getLong(
			liferayPortletRequest, "fragmentCollectionId");

		componentContext.put("fragmentCollectionId", fragmentCollectionId);

		PortletURL copyFragmentEntryURL =
			liferayPortletResponse.createActionURL();

		copyFragmentEntryURL.setParameter(
			ActionRequest.ACTION_NAME, "/fragment/copy_fragment_entry");
		copyFragmentEntryURL.setParameter(
			"redirect", themeDisplay.getURLCurrent());

		componentContext.put(
			"copyFragmentEntryURL", copyFragmentEntryURL.toString());

		PortletURL selectFragmentCollectionURL =
			liferayPortletResponse.createActionURL();

		selectFragmentCollectionURL.setParameter(
			"mvcRenderCommandName", "/fragment/select_fragment_collection");
		selectFragmentCollectionURL.setWindowState(LiferayWindowState.POP_UP);

		componentContext.put(
			"selectFragmentCollectionURL",
			selectFragmentCollectionURL.toString());

		return componentContext;
	}

}