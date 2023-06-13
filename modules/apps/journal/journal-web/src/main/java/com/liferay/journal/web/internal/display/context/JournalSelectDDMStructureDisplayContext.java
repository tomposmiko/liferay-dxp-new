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
import com.liferay.dynamic.data.mapping.service.DDMStructureLinkLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMStructureServiceUtil;
import com.liferay.dynamic.data.mapping.util.DDMUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.journal.model.JournalArticle;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.List;
import java.util.Objects;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class JournalSelectDDMStructureDisplayContext {

	public JournalSelectDDMStructureDisplayContext(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;

		_request = PortalUtil.getHttpServletRequest(renderRequest);
	}

	public long getClassPK() {
		if (_classPK != null) {
			return _classPK;
		}

		_classPK = ParamUtil.getLong(_renderRequest, "classPK");

		return _classPK;
	}

	public String getClearResultsURL() {
		PortletURL clearResultsURL = _getPortletURL();

		clearResultsURL.setParameter("keywords", StringPool.BLANK);

		return clearResultsURL.toString();
	}

	public String getEventName() {
		return _renderResponse.getNamespace() + "selectStructure";
	}

	public List<DropdownItem> getFilterItemsDropdownItems() {
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

	public String getSearchActionURL() {
		PortletURL portletURL = _renderResponse.createRenderURL();

		portletURL.setParameter("mvcPath", "/select_structure.jsp");
		portletURL.setParameter("classPK", String.valueOf(getClassPK()));
		portletURL.setParameter("eventName", getEventName());

		return portletURL.toString();
	}

	public String getSortingURL() {
		PortletURL sortingURL = _getPortletURL();

		sortingURL.setParameter(
			"orderByType",
			Objects.equals(getOrderByType(), "asc") ? "desc" : "asc");

		return sortingURL.toString();
	}

	public SearchContainer getStructureSearch() throws Exception {
		if (_structureSearch != null) {
			return _structureSearch;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		SearchContainer structureSearch = new SearchContainer(
			_renderRequest, _getPortletURL(), null, "there-are-no-structures");

		if (Validator.isNotNull(_getKeywords())) {
			structureSearch.setEmptyResultsMessage("no-structures-were-found");
		}

		String orderByCol = getOrderByCol();
		String orderByType = getOrderByType();

		OrderByComparator<DDMStructure> orderByComparator =
			DDMUtil.getStructureOrderByComparator(
				getOrderByCol(), getOrderByType());

		structureSearch.setOrderByCol(orderByCol);
		structureSearch.setOrderByComparator(orderByComparator);
		structureSearch.setOrderByType(orderByType);

		long[] groupIds = PortalUtil.getCurrentAndAncestorSiteGroupIds(
			themeDisplay.getScopeGroupId());

		int total = 0;

		if (_isSearchRestriction()) {
			total = DDMStructureLinkLocalServiceUtil.getStructureLinksCount(
				_getSearchRestrictionClassNameId(),
				_getSearchRestrictionClassPK());
		}
		else {
			total = DDMStructureServiceUtil.searchCount(
				themeDisplay.getCompanyId(), groupIds,
				PortalUtil.getClassNameId(JournalArticle.class.getName()),
				_getKeywords(), WorkflowConstants.STATUS_ANY);
		}

		structureSearch.setTotal(total);

		List<DDMStructure> results = null;

		if (_isSearchRestriction()) {
			results =
				DDMStructureLinkLocalServiceUtil.getStructureLinkStructures(
					_getSearchRestrictionClassNameId(),
					_getSearchRestrictionClassPK(), structureSearch.getStart(),
					structureSearch.getEnd());
		}
		else {
			results = DDMStructureServiceUtil.search(
				themeDisplay.getCompanyId(), groupIds,
				PortalUtil.getClassNameId(JournalArticle.class.getName()),
				_getKeywords(), WorkflowConstants.STATUS_ANY,
				structureSearch.getStart(), structureSearch.getEnd(),
				structureSearch.getOrderByComparator());
		}

		structureSearch.setResults(results);

		_structureSearch = structureSearch;

		return structureSearch;
	}

	public int getTotalItems() throws Exception {
		SearchContainer<?> searchContainer = getStructureSearch();

		return searchContainer.getTotal();
	}

	public boolean isDisabledManagementBar() throws Exception {
		if (isSearch()) {
			return false;
		}

		if (getTotalItems() > 0) {
			return false;
		}

		return true;
	}

	public boolean isSearch() {
		if (Validator.isNotNull(_getKeywords())) {
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
						dropdownItem.setHref(
							_getPortletURL(), "navigation", "all");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "all"));
					});
			}
		};
	}

	private String _getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_renderRequest, "keywords");

		return _keywords;
	}

	private List<DropdownItem> _getOrderByDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setActive(
							Objects.equals(getOrderByCol(), "modified-date"));
						dropdownItem.setHref(
							_getPortletURL(), "orderByCol", "modified-date");
						dropdownItem.setLabel(
							LanguageUtil.get(_request, "modified-date"));
					});

				add(
					dropdownItem -> {
						dropdownItem.setActive(
							Objects.equals(getOrderByCol(), "id"));
						dropdownItem.setHref(
							_getPortletURL(), "orderByCol", "id");
						dropdownItem.setLabel(LanguageUtil.get(_request, "id"));
					});
			}
		};
	}

	private PortletURL _getPortletURL() {
		PortletURL portletURL = _renderResponse.createRenderURL();

		portletURL.setParameter("mvcPath", "/select_structure.jsp");

		long classPK = getClassPK();

		if (classPK != 0) {
			portletURL.setParameter("classPK", String.valueOf(classPK));
		}

		String keywords = _getKeywords();

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

	private long _getSearchRestrictionClassNameId() {
		if (_searchRestrictionClassNameId != null) {
			return _searchRestrictionClassNameId;
		}

		_searchRestrictionClassNameId = ParamUtil.getLong(
			_request, "searchRestrictionClassNameId");

		return _searchRestrictionClassNameId;
	}

	private long _getSearchRestrictionClassPK() {
		if (_searchRestrictionClassPK != null) {
			return _searchRestrictionClassPK;
		}

		_searchRestrictionClassPK = ParamUtil.getLong(
			_request, "searchRestrictionClassPK");

		return _searchRestrictionClassPK;
	}

	private boolean _isSearchRestriction() {
		if (_searchRestriction != null) {
			return _searchRestriction;
		}

		_searchRestriction = ParamUtil.getBoolean(
			_request, "searchRestriction");

		return _searchRestriction;
	}

	private Long _classPK;
	private String _keywords;
	private String _orderByCol;
	private String _orderByType;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final HttpServletRequest _request;
	private Boolean _searchRestriction;
	private Long _searchRestrictionClassNameId;
	private Long _searchRestrictionClassPK;
	private SearchContainer _structureSearch;

}