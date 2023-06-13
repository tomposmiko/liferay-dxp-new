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

package com.liferay.users.admin.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItemList;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.usersadmin.search.UserSearch;
import com.liferay.portlet.usersadmin.search.UserSearchTerms;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Pei-Jung Lan
 */
public class ViewUsersManagementToolbarDisplayContext {

	public ViewUsersManagementToolbarDisplayContext(
		HttpServletRequest request, RenderRequest renderRequest,
		RenderResponse renderResponse, String displayStyle, String navigation,
		int status) {

		_request = request;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_displayStyle = displayStyle;
		_navigation = navigation;
		_status = status;

		_currentURL = PortletURLUtil.getCurrent(
			_renderRequest, _renderResponse);
	}

	public List<DropdownItem> getActionDropdownItems() {
		return new DropdownItemList() {
			{
				if (isShowRestoreButton()) {
					add(
						dropdownItem -> {
							dropdownItem.setHref(
								StringBundler.concat(
									"javascript:",
									_renderResponse.getNamespace(),
									"deleteUsers('", Constants.RESTORE, "');"));
							dropdownItem.setIcon("icon-undo");
							dropdownItem.setLabel(
								LanguageUtil.get(_request, "restore"));
							dropdownItem.setQuickAction(true);
						});
				}

				if (isShowDeleteButton()) {
					add(
						dropdownItem -> {
							UserSearchTerms userSearchTerms =
								(UserSearchTerms)_userSearch.getSearchTerms();

							String action = Constants.DELETE;

							if (userSearchTerms.isActive()) {
								action = Constants.DEACTIVATE;
							}

							dropdownItem.setHref(
								StringBundler.concat(
									"javascript:",
									_renderResponse.getNamespace(),
									"deleteUsers('", action, "');"));
							dropdownItem.setIcon("trash");
							dropdownItem.setLabel(
								LanguageUtil.get(_request, action));
							dropdownItem.setQuickAction(true);
						});
				}
			}
		};
	}

	public String getClearResultsURL() {
		PortletURL clearResultsURL = getPortletURL();

		clearResultsURL.setParameter("keywords", StringPool.BLANK);

		return clearResultsURL.toString();
	}

	public CreationMenu getCreationMenu() throws PortalException {
		return new CreationMenu() {
			{
				addPrimaryDropdownItem(
					dropdownItem -> {
						dropdownItem.setHref(
							_renderResponse.createRenderURL(),
							"mvcRenderCommandName", "/users_admin/edit_user");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "add-user"));
					});
			}
		};
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

	public String getOrderByCol() {
		return _userSearch.getOrderByCol();
	}

	public String getOrderByType() {
		return _userSearch.getOrderByType();
	}

	public PortletURL getPortletURL() {
		try {
			PortletURL portletURL = PortletURLUtil.clone(
				_currentURL, _renderResponse);

			portletURL.setParameter("orderByCol", getOrderByCol());
			portletURL.setParameter("orderByType", getOrderByType());

			return portletURL;
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}

			return _renderResponse.createRenderURL();
		}
	}

	public String getSearchActionURL() {
		PortletURL searchActionURL = getPortletURL();

		return searchActionURL.toString();
	}

	public SearchContainer getSearchContainer() {
		if (_userSearch != null) {
			return _userSearch;
		}

		PortletURL portletURL = (PortletURL)_request.getAttribute(
			"view.jsp-portletURL");

		UserSearch userSearch = new UserSearch(
			_renderRequest, "cur2", portletURL);

		RowChecker rowChecker = new EmptyOnClickRowChecker(_renderResponse);

		rowChecker.setRowIds("rowIdsUser");

		userSearch.setRowChecker(rowChecker);

		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		UserSearchTerms searchTerms =
			(UserSearchTerms)userSearch.getSearchTerms();

		if (_navigation.equals("active")) {
			searchTerms.setStatus(WorkflowConstants.STATUS_APPROVED);
		}
		else if (_navigation.equals("inactive")) {
			searchTerms.setStatus(WorkflowConstants.STATUS_INACTIVE);
		}

		int total = UserLocalServiceUtil.searchCount(
			themeDisplay.getCompanyId(), searchTerms.getKeywords(),
			searchTerms.getStatus(), new LinkedHashMap<String, Object>());

		userSearch.setTotal(total);

		List<User> results = UserLocalServiceUtil.search(
			themeDisplay.getCompanyId(), searchTerms.getKeywords(),
			searchTerms.getStatus(), new LinkedHashMap<String, Object>(),
			userSearch.getStart(), userSearch.getEnd(),
			userSearch.getOrderByComparator());

		userSearch.setResults(results);

		_userSearch = userSearch;

		return _userSearch;
	}

	public String getSortingURL() {
		PortletURL sortingURL = getPortletURL();

		sortingURL.setParameter(
			"orderByType",
			Objects.equals(getOrderByType(), "asc") ? "desc" : "asc");

		return sortingURL.toString();
	}

	public List<ViewTypeItem> getViewTypeItems() {
		return new ViewTypeItemList(getPortletURL(), _displayStyle) {
			{
				addCardViewTypeItem();
				addListViewTypeItem();
				addTableViewTypeItem();
			}
		};
	}

	public boolean isShowDeleteButton() {
		UserSearchTerms searchTerms =
			(UserSearchTerms)getSearchContainer().getSearchTerms();

		if ((searchTerms.getStatus() != WorkflowConstants.STATUS_ANY) &&
			(searchTerms.isActive() ||
			 (!searchTerms.isActive() && PropsValues.USERS_DELETE))) {

			return true;
		}

		return false;
	}

	public boolean isShowRestoreButton() {
		UserSearchTerms searchTerms =
			(UserSearchTerms)getSearchContainer().getSearchTerms();

		if ((searchTerms.getStatus() != WorkflowConstants.STATUS_ANY) &&
			!searchTerms.isActive()) {

			return true;
		}

		return false;
	}

	public boolean showCreationMenu() throws PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		return PortalPermissionUtil.contains(
			themeDisplay.getPermissionChecker(), ActionKeys.ADD_USER);
	}

	private List<DropdownItem> _getFilterNavigationDropdownItems() {
		DropdownItemList navigationDropdownitems = new DropdownItemList();

		for (String navigation : new String[] {"active", "inactive"}) {
			navigationDropdownitems.add(
				dropdownItem -> {
					dropdownItem.setActive(navigation.equals(_navigation));
					dropdownItem.setHref(
						getPortletURL(), "navigation", navigation);
					dropdownItem.setLabel(
						LanguageUtil.get(_request, navigation));
				});
		}

		return navigationDropdownitems;
	}

	private List<DropdownItem> _getOrderByDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setActive(
							Objects.equals(getOrderByCol(), "first-name"));
						dropdownItem.setHref(
							getPortletURL(), "orderByCol", "first-name");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "first-name"));
					});

				add(
					dropdownItem -> {
						dropdownItem.setActive(
							Objects.equals(getOrderByCol(), "last-name"));
						dropdownItem.setHref(
							getPortletURL(), "orderByCol", "last-name");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "last-name"));
					});

				add(
					dropdownItem -> {
						dropdownItem.setActive(
							Objects.equals(getOrderByCol(), "screen-name"));
						dropdownItem.setHref(
							getPortletURL(), "orderByCol", "screen-name");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "screen-name"));
					});
			}
		};
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ViewUsersManagementToolbarDisplayContext.class);

	private final PortletURL _currentURL;
	private final String _displayStyle;
	private final String _navigation;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final HttpServletRequest _request;
	private final int _status;
	private UserSearch _userSearch;

}