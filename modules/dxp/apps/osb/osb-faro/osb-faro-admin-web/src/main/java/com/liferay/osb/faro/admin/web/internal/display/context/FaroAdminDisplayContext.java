/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.admin.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.osb.faro.admin.web.internal.model.FaroProjectAdminDisplay;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Collections;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shinn Lok
 */
public class FaroAdminDisplayContext {

	public FaroAdminDisplayContext(
		HttpServletRequest httpServletRequest, RenderRequest renderRequest,
		RenderResponse renderResponse) {

		_httpServletRequest = httpServletRequest;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;

		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public List<DropdownItem> getActionDropdownItems(
		FaroProjectAdminDisplay faroProjectAdminDisplay) {

		if (faroProjectAdminDisplay.isOffline()) {
			return Collections.emptyList();
		}

		PortletURL portletURL = PortletURLBuilder.create(
			_renderResponse.createActionURL()
		).setRedirect(
			ParamUtil.getString(
				_httpServletRequest, "redirect", _themeDisplay.getURLCurrent())
		).buildPortletURL();

		return DropdownItemListBuilder.add(
			dropdownItem -> {
				dropdownItem.setHref(
					portletURL, ActionRequest.ACTION_NAME,
					"/faro_admin/deactivate_project", "faroProjectId",
					faroProjectAdminDisplay.getFaroProjectId());
				dropdownItem.setLabel(
					LanguageUtil.get(
						_httpServletRequest, "deactivate-project"));
			}
		).add(
			dropdownItem -> {
				dropdownItem.setHref(
					portletURL, ActionRequest.ACTION_NAME,
					"/faro_admin/refresh_liferay", "faroProjectId",
					faroProjectAdminDisplay.getFaroProjectId());
				dropdownItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "refresh-liferay"));
			}
		).add(
			dropdownItem -> {
				dropdownItem.setHref(
					portletURL, ActionRequest.ACTION_NAME,
					"/faro_admin/refresh_project", "groupId",
					faroProjectAdminDisplay.getGroupId());
				dropdownItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "refresh-project"));
			}
		).build();
	}

	public PortletURL getPortletURL() {
		PortletURL portletURL = _renderResponse.createRenderURL();

		String keywords = _getKeywords();

		if (Validator.isNotNull(keywords)) {
			portletURL.setParameter("keywords", keywords);
		}

		String navigation = ParamUtil.getString(
			_httpServletRequest, "navigation");

		if (Validator.isNotNull(navigation)) {
			portletURL.setParameter(
				"navigation", HtmlUtil.escapeJS(_getNavigation()));
		}

		String orderByCol = _getOrderByCol();

		if (Validator.isNotNull(orderByCol)) {
			portletURL.setParameter("orderByCol", orderByCol);
		}

		String orderByType = _getOrderByType();

		if (Validator.isNotNull(orderByType)) {
			portletURL.setParameter("orderByType", orderByType);
		}

		return portletURL;
	}

	public SearchContainer<FaroProjectAdminDisplay> getSearchContainer()
		throws Exception {

		if (_searchContainer != null) {
			return _searchContainer;
		}

		SearchContainer<FaroProjectAdminDisplay> searchContainer =
			new SearchContainer<>(
				_renderRequest, getPortletURL(), null, "there-are-no-projects");

		searchContainer.setOrderByCol(_getOrderByCol());
		searchContainer.setOrderByType(_getOrderByType());

		Indexer<FaroProject> indexer = IndexerRegistryUtil.nullSafeGetIndexer(
			FaroProject.class);

		SearchContext searchContext = new SearchContext();

		if (_isBasic()) {
			searchContext.setAttribute("subscriptionName", "Basic");
		}
		else if (_isBusiness()) {
			searchContext.setAttribute("subscriptionName", "Business");
		}
		else if (_isEnterprise()) {
			searchContext.setAttribute("subscriptionName", "Enterprise");
		}
		else if (_isInactive()) {
			searchContext.setAttribute("inactive", Boolean.TRUE);
		}
		else if (_isNavigationUsageLimitApproaching()) {
			searchContext.setAttribute("maxUsage", 100);
			searchContext.setAttribute("minUsage", 80);
		}
		else if (_isNavigationUsageLimitExceeded()) {
			searchContext.setAttribute("maxUsage", Integer.MAX_VALUE);
			searchContext.setAttribute("minUsage", 100);
		}
		else if (_isOffline()) {
			searchContext.setAttribute("offline", Boolean.TRUE);
		}

		searchContext.setEnd(searchContainer.getEnd());
		searchContext.setKeywords(
			ParamUtil.getString(_httpServletRequest, "keywords"));
		searchContext.setStart(searchContainer.getStart());

		String orderByCol = _orderByCol;

		if (orderByCol.equals("createDate") ||
			orderByCol.equals("individualsUsage") ||
			orderByCol.equals("pageViewsUsage")) {

			orderByCol += "_sortable";
		}

		boolean reverse = false;

		if (_orderByType.equals("asc")) {
			reverse = true;
		}

		searchContext.setSorts(new Sort(orderByCol, reverse));

		Hits hits = indexer.search(searchContext);

		searchContainer.setResultsAndTotal(
			() -> TransformUtil.transform(
				hits.toList(), FaroProjectAdminDisplay::new),
			hits.getLength());

		searchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_renderResponse));

		_searchContainer = searchContainer;

		return searchContainer;
	}

	private String _getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_httpServletRequest, "keywords");

		return _keywords;
	}

	private String _getNavigation() {
		if (_navigation != null) {
			return _navigation;
		}

		_navigation = ParamUtil.getString(
			_httpServletRequest, "navigation", "all");

		return _navigation;
	}

	private String _getOrderByCol() {
		if (Validator.isNotNull(_orderByCol)) {
			return _orderByCol;
		}

		_orderByCol = ParamUtil.getString(
			_httpServletRequest, "orderByCol", "createDate");

		return _orderByCol;
	}

	private String _getOrderByType() {
		if (Validator.isNotNull(_orderByType)) {
			return _orderByType;
		}

		_orderByType = ParamUtil.getString(
			_httpServletRequest, "orderByType", "asc");

		return _orderByType;
	}

	private boolean _isBasic() {
		if (StringUtil.equals(_getNavigation(), "basic")) {
			return true;
		}

		return false;
	}

	private boolean _isBusiness() {
		if (StringUtil.equals(_getNavigation(), "business")) {
			return true;
		}

		return false;
	}

	private boolean _isEnterprise() {
		if (StringUtil.equals(_getNavigation(), "enterprise")) {
			return true;
		}

		return false;
	}

	private boolean _isInactive() {
		if (StringUtil.equals(_getNavigation(), "inactive")) {
			return true;
		}

		return false;
	}

	private boolean _isNavigationUsageLimitApproaching() {
		if (StringUtil.equals(_getNavigation(), "usage-limit-approaching")) {
			return true;
		}

		return false;
	}

	private boolean _isNavigationUsageLimitExceeded() {
		if (StringUtil.equals(_getNavigation(), "usage-limit-exceeded")) {
			return true;
		}

		return false;
	}

	private boolean _isOffline() {
		if (StringUtil.equals(_getNavigation(), "offline")) {
			return true;
		}

		return false;
	}

	private final HttpServletRequest _httpServletRequest;
	private String _keywords;
	private String _navigation;
	private String _orderByCol;
	private String _orderByType;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private SearchContainer<FaroProjectAdminDisplay> _searchContainer;
	private final ThemeDisplay _themeDisplay;

}