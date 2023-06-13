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

package com.liferay.layout.admin.web.internal.item.selector;

import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.ItemSelectorViewDescriptor;
import com.liferay.item.selector.criteria.AssetEntryItemSelectorReturnType;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalServiceUtil;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryServiceUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class MasterLayoutPageTemplateEntryItemSelectorViewDescriptor
	implements ItemSelectorViewDescriptor<LayoutPageTemplateEntry> {

	public MasterLayoutPageTemplateEntryItemSelectorViewDescriptor(
		HttpServletRequest httpServletRequest, PortletURL portletURL) {

		_httpServletRequest = httpServletRequest;
		_portletURL = portletURL;

		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	@Override
	public String getDefaultDisplayStyle() {
		return "icon";
	}

	@Override
	public String[] getDisplayViews() {
		return new String[] {"icon"};
	}

	@Override
	public ItemDescriptor getItemDescriptor(
		LayoutPageTemplateEntry layoutPageTemplateEntry) {

		return new MasterLayoutPageTemplateEntryItemDescriptor(
			layoutPageTemplateEntry);
	}

	@Override
	public ItemSelectorReturnType getItemSelectorReturnType() {
		return new AssetEntryItemSelectorReturnType();
	}

	@Override
	public String[] getOrderByKeys() {
		return new String[] {"name", "create-date"};
	}

	@Override
	public SearchContainer<LayoutPageTemplateEntry> getSearchContainer()
		throws PortalException {

		SearchContainer<LayoutPageTemplateEntry>
			masterLayoutPageTemplateEntrySearchContainer =
				new SearchContainer<>(
					(PortletRequest)_httpServletRequest.getAttribute(
						JavaConstants.JAVAX_PORTLET_REQUEST),
					_portletURL, null, "there-are-no-master-pages");

		List<LayoutPageTemplateEntry> masterLayoutPageTemplateEntries =
			new ArrayList<>();

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateEntryLocalServiceUtil.
				createLayoutPageTemplateEntry(0);

		layoutPageTemplateEntry.setName(
			LanguageUtil.get(_httpServletRequest, "blank"));

		LayoutPageTemplateEntry defaultLayoutPageTemplateEntry =
			LayoutPageTemplateEntryServiceUtil.
				fetchDefaultLayoutPageTemplateEntry(
					_themeDisplay.getScopeGroupId(),
					LayoutPageTemplateEntryTypeConstants.TYPE_MASTER_LAYOUT,
					WorkflowConstants.STATUS_APPROVED);

		if (defaultLayoutPageTemplateEntry == null) {
			layoutPageTemplateEntry.setDefaultTemplate(true);
		}

		layoutPageTemplateEntry.setStatus(WorkflowConstants.STATUS_APPROVED);

		masterLayoutPageTemplateEntries.add(layoutPageTemplateEntry);

		Group scopeGroup = _themeDisplay.getScopeGroup();

		long scopeGroupId = _themeDisplay.getScopeGroupId();

		if (scopeGroup.isLayoutPrototype()) {
			LayoutPageTemplateEntry layoutPrototypeLayoutPageTemplateEntry =
				LayoutPageTemplateEntryLocalServiceUtil.
					fetchFirstLayoutPageTemplateEntry(scopeGroup.getClassPK());

			scopeGroupId = layoutPrototypeLayoutPageTemplateEntry.getGroupId();
		}

		masterLayoutPageTemplateEntries.addAll(
			LayoutPageTemplateEntryServiceUtil.getLayoutPageTemplateEntries(
				scopeGroupId,
				LayoutPageTemplateEntryTypeConstants.TYPE_MASTER_LAYOUT,
				WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null));

		masterLayoutPageTemplateEntrySearchContainer.setResultsAndTotal(
			masterLayoutPageTemplateEntries);

		return masterLayoutPageTemplateEntrySearchContainer;
	}

	@Override
	public boolean isShowBreadcrumb() {
		return false;
	}

	@Override
	public boolean isShowManagementToolbar() {
		return false;
	}

	private final HttpServletRequest _httpServletRequest;
	private final PortletURL _portletURL;
	private final ThemeDisplay _themeDisplay;

}