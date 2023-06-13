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

package com.liferay.site.memberships.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItemList;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.OrganizationConstants;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portlet.usersadmin.search.OrganizationSearch;
import com.liferay.portlet.usersadmin.search.OrganizationSearchTerms;
import com.liferay.site.memberships.web.internal.constants.SiteMembershipsPortletKeys;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import javax.portlet.ActionRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class OrganizationsDisplayContext {

	public OrganizationsDisplayContext(
		HttpServletRequest request, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		_request = request;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
	}

	public List<DropdownItem> getActionDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.putData(
							"action", "deleteSelectedOrganizations");
						dropdownItem.setIcon("times-circle");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "delete"));
						dropdownItem.setQuickAction(true);
					});
			}
		};
	}

	public String getClearResultsURL() {
		PortletURL clearResultsURL = getPortletURL();

		clearResultsURL.setParameter("keywords", StringPool.BLANK);

		return clearResultsURL.toString();
	}

	public String getDisplayStyle() {
		if (Validator.isNotNull(_displayStyle)) {
			return _displayStyle;
		}

		PortalPreferences portalPreferences =
			PortletPreferencesFactoryUtil.getPortalPreferences(_request);

		_displayStyle = portalPreferences.getValue(
			SiteMembershipsPortletKeys.SITE_MEMBERSHIPS_ADMIN, "display-style",
			"icon");

		return _displayStyle;
	}

	public List<DropdownItem> getFilterDropdownItems() {
		return new DropdownItemList() {
			{
				addGroup(
					dropdownGroupItem -> {
						dropdownGroupItem.setDropdownItems(
							_getFilterNavigationDropdownItems());
						dropdownGroupItem.setLabel(
							LanguageUtil.get(_request, "filter-by-navigation"));
					});

				addGroup(
					dropdownGroupItem -> {
						dropdownGroupItem.setDropdownItems(
							_getOrderByDropdownItems());
						dropdownGroupItem.setLabel(
							LanguageUtil.get(_request, "order-by"));
					});
			}
		};
	}

	public long getGroupId() {
		if (_groupId != null) {
			return _groupId;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		_groupId = ParamUtil.getLong(
			_request, "groupId", themeDisplay.getSiteGroupIdOrLiveGroupId());

		return _groupId;
	}

	public String getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_renderRequest, "keywords");

		return _keywords;
	}

	public String getOrderByCol() {
		if (_orderByCol != null) {
			return _orderByCol;
		}

		_orderByCol = ParamUtil.getString(_renderRequest, "orderByCol", "name");

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

	public SearchContainer getOrganizationSearchContainer() {
		if (_organizationSearch != null) {
			return _organizationSearch;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		OrganizationSearch organizationSearch = new OrganizationSearch(
			_renderRequest, getPortletURL());

		organizationSearch.setEmptyResultsMessage(
			"no-organization-was-found-that-is-a-member-of-this-site");
		organizationSearch.setRowChecker(
			new EmptyOnClickRowChecker(_renderResponse));

		OrganizationSearchTerms searchTerms =
			(OrganizationSearchTerms)organizationSearch.getSearchTerms();

		LinkedHashMap<String, Object> organizationParams =
			new LinkedHashMap<>();

		organizationParams.put("groupOrganization", Long.valueOf(getGroupId()));
		organizationParams.put(
			"organizationsGroups", Long.valueOf(getGroupId()));

		int organizationsCount = OrganizationLocalServiceUtil.searchCount(
			themeDisplay.getCompanyId(),
			OrganizationConstants.ANY_PARENT_ORGANIZATION_ID,
			searchTerms.getKeywords(), searchTerms.getType(),
			searchTerms.getRegionIdObj(), searchTerms.getCountryIdObj(),
			organizationParams);

		organizationSearch.setTotal(organizationsCount);

		List<Organization> organizations = OrganizationLocalServiceUtil.search(
			themeDisplay.getCompanyId(),
			OrganizationConstants.ANY_PARENT_ORGANIZATION_ID,
			searchTerms.getKeywords(), searchTerms.getType(),
			searchTerms.getRegionIdObj(), searchTerms.getCountryIdObj(),
			organizationParams, organizationSearch.getStart(),
			organizationSearch.getEnd(),
			organizationSearch.getOrderByComparator());

		organizationSearch.setResults(organizations);

		_organizationSearch = organizationSearch;

		return _organizationSearch;
	}

	public PortletURL getPortletURL() {
		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletURL portletURL = _renderResponse.createRenderURL();

		portletURL.setParameter("mvcPath", "/view.jsp");
		portletURL.setParameter("tabs1", "organizations");
		portletURL.setParameter("redirect", themeDisplay.getURLCurrent());
		portletURL.setParameter("groupId", String.valueOf(getGroupId()));

		String displayStyle = getDisplayStyle();

		if (Validator.isNotNull(displayStyle)) {
			portletURL.setParameter("displayStyle", displayStyle);
		}

		String keywords = getKeywords();

		if (Validator.isNotNull(keywords)) {
			portletURL.setParameter("keywords", keywords);
		}

		String orderByCol = getOrderByCol();

		if (Validator.isNotNull(orderByCol)) {
			portletURL.setParameter("orderByCol", orderByCol);
		}

		String orderByType = getOrderByType();

		if (Validator.isNotNull(orderByType)) {
			portletURL.setParameter("orderByType", orderByType);
		}

		return portletURL;
	}

	public String getSearchActionURL() {
		PortletURL searchActionURL = getPortletURL();

		return searchActionURL.toString();
	}

	public String getSortingURL() {
		PortletURL sortingURL = getPortletURL();

		sortingURL.setParameter(
			"orderByType",
			Objects.equals(getOrderByType(), "asc") ? "desc" : "asc");

		return sortingURL.toString();
	}

	public int getTotalItems() {
		SearchContainer organizationSearchContainer =
			getOrganizationSearchContainer();

		return organizationSearchContainer.getTotal();
	}

	public List<ViewTypeItem> getViewTypeItems() {
		PortletURL portletURL = _renderResponse.createActionURL();

		portletURL.setParameter(
			ActionRequest.ACTION_NAME, "changeDisplayStyle");
		portletURL.setParameter("redirect", PortalUtil.getCurrentURL(_request));

		return new ViewTypeItemList(portletURL, getDisplayStyle()) {
			{
				addCardViewTypeItem();
				addListViewTypeItem();
				addTableViewTypeItem();
			}
		};
	}

	public boolean isDisabledManagementBar() {
		if (getTotalItems() <= 0) {
			return true;
		}

		return false;
	}

	public boolean isShowSearch() {
		if (getTotalItems() > 0) {
			return true;
		}

		if (Validator.isNotNull(getKeywords())) {
			return true;
		}

		return false;
	}

	private List<DropdownItem> _getFilterNavigationDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setActive(true);
						dropdownItem.setHref(getPortletURL());
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "all"));
					});
			}
		};
	}

	private List<DropdownItem> _getOrderByDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setActive(
							Objects.equals(getOrderByCol(), "name"));
						dropdownItem.setHref(
							getPortletURL(), "orderByCol", "name");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "name"));
					});

				add(
					dropdownItem -> {
						dropdownItem.setActive(
							Objects.equals(getOrderByCol(), "type"));
						dropdownItem.setHref(
							getPortletURL(), "orderByCol", "type");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "type"));
					});
			}
		};
	}

	private String _displayStyle;
	private Long _groupId;
	private String _keywords;
	private String _orderByCol;
	private String _orderByType;
	private OrganizationSearch _organizationSearch;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final HttpServletRequest _request;

}