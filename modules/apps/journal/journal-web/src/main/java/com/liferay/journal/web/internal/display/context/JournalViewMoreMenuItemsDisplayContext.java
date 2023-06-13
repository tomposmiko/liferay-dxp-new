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

package com.liferay.journal.web.internal.display.context;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.util.comparator.StructureModifiedDateComparator;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemList;
import com.liferay.journal.service.JournalFolderServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class JournalViewMoreMenuItemsDisplayContext {

	public JournalViewMoreMenuItemsDisplayContext(
		RenderRequest renderRequest, RenderResponse renderResponse,
		long folderId, int restrictionType) {

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_folderId = folderId;
		_restrictionType = restrictionType;

		_httpServletRequest = PortalUtil.getHttpServletRequest(_renderRequest);
	}

	public List<DDMStructure> getDDMStructures() throws PortalException {
		if (ListUtil.isNotEmpty(_ddmStructures)) {
			return _ddmStructures;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)_renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (Validator.isNull(_getKeywords())) {
			_ddmStructures = JournalFolderServiceUtil.getDDMStructures(
				PortalUtil.getCurrentAndAncestorSiteGroupIds(
					themeDisplay.getScopeGroupId()),
				_folderId, _restrictionType);
		}
		else {
			_ddmStructures = JournalFolderServiceUtil.searchDDMStructures(
				themeDisplay.getCompanyId(),
				PortalUtil.getCurrentAndAncestorSiteGroupIds(
					themeDisplay.getScopeGroupId()),
				_folderId, _restrictionType, _getKeywords(), QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, _getOrderByComparator());
		}

		return _ddmStructures;
	}

	public SearchContainer getDDMStructuresSearchContainer()
		throws PortalException {

		if (_ddmStructuresSearchContainer != null) {
			return _ddmStructuresSearchContainer;
		}

		SearchContainer searchContainer = new SearchContainer(
			_renderRequest, getPortletURL(), null, "no-results-were-found");

		searchContainer.setOrderByCol(getOrderByCol());
		searchContainer.setOrderByComparator(_getOrderByComparator());
		searchContainer.setOrderByType(getOrderByType());

		List<DDMStructure> ddmStructures = getDDMStructures();

		searchContainer.setTotal(ddmStructures.size());

		List<DDMStructure> results = ListUtil.subList(
			ddmStructures, searchContainer.getStart(),
			searchContainer.getEnd());

		searchContainer.setResults(results);

		_ddmStructuresSearchContainer = searchContainer;

		return _ddmStructuresSearchContainer;
	}

	public String getEventName() {
		if (_eventName != null) {
			return _eventName;
		}

		_eventName = ParamUtil.getString(
			_renderRequest, "eventName",
			_renderResponse.getNamespace() + "selectAddMenuItem");

		return _eventName;
	}

	public List<NavigationItem> getNavigationItems() {
		return new NavigationItemList() {
			{
				add(
					navigationItem -> {
						navigationItem.setActive(true);
						navigationItem.setHref(StringPool.BLANK);
						navigationItem.setLabel(
							LanguageUtil.get(
								_httpServletRequest, "all-menu-items"));
					});
			}
		};
	}

	public String getOrderByCol() {
		if (_orderByCol != null) {
			return _orderByCol;
		}

		_orderByCol = ParamUtil.getString(
			_renderRequest, "orderByCol", "modified-date");

		return _orderByCol;
	}

	public String getOrderByType() {
		if (_orderByType != null) {
			return _orderByType;
		}

		_orderByType = ParamUtil.getString(
			_renderRequest, "orderByType", "asc");

		return _orderByType;
	}

	public PortletURL getPortletURL() {
		PortletURL portletURL = _renderResponse.createRenderURL();

		portletURL.setParameter("mvcPath", "/view_more_menu_items.jsp");
		portletURL.setParameter("folderId", String.valueOf(_folderId));
		portletURL.setParameter("eventName", getEventName());

		return portletURL;
	}

	public String getRedirect() {
		if (_redirect != null) {
			return _redirect;
		}

		_redirect = ParamUtil.getString(_renderRequest, "redirect");

		return _redirect;
	}

	private String _getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_renderRequest, "keywords");

		return _keywords;
	}

	private OrderByComparator _getOrderByComparator() {
		boolean orderByAsc = false;

		String orderByType = getOrderByType();

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		return new StructureModifiedDateComparator(orderByAsc);
	}

	private List<DDMStructure> _ddmStructures;
	private SearchContainer _ddmStructuresSearchContainer;
	private String _eventName;
	private final long _folderId;
	private final HttpServletRequest _httpServletRequest;
	private String _keywords;
	private String _orderByCol;
	private String _orderByType;
	private String _redirect;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final int _restrictionType;

}