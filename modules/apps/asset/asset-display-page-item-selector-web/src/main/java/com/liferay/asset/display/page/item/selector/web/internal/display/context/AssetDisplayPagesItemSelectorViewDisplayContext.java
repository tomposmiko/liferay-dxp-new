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

package com.liferay.asset.display.page.item.selector.web.internal.display.context;

import com.liferay.asset.display.contributor.AssetDisplayContributor;
import com.liferay.asset.display.contributor.AssetDisplayContributorTracker;
import com.liferay.asset.display.page.item.selector.criterion.AssetDisplayPageSelectorCriterion;
import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.ClassType;
import com.liferay.asset.kernel.model.ClassTypeReader;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.SafeConsumer;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryServiceUtil;
import com.liferay.layout.page.template.util.comparator.LayoutPageTemplateEntryCreateDateComparator;
import com.liferay.layout.page.template.util.comparator.LayoutPageTemplateEntryNameComparator;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.List;
import java.util.Objects;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class AssetDisplayPagesItemSelectorViewDisplayContext {

	public AssetDisplayPagesItemSelectorViewDisplayContext(
		HttpServletRequest request,
		AssetDisplayContributorTracker assetDisplayContributorTracker,
		AssetDisplayPageSelectorCriterion assetDisplayPageSelectorCriterion,
		String itemSelectedEventName, PortletURL portletURL) {

		_request = request;
		_assetDisplayContributorTracker = assetDisplayContributorTracker;
		_assetDisplayPageSelectorCriterion = assetDisplayPageSelectorCriterion;
		_itemSelectedEventName = itemSelectedEventName;
		_portletURL = portletURL;

		_portletRequest = (PortletRequest)request.getAttribute(
			JavaConstants.JAVAX_PORTLET_REQUEST);
		_portletResponse = (PortletResponse)request.getAttribute(
			JavaConstants.JAVAX_PORTLET_RESPONSE);
		_themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public SearchContainer getAssetDisplayPageSearchContainer()
		throws PortletException {

		if (_assetDisplayPageSearchContainer != null) {
			return _assetDisplayPageSearchContainer;
		}

		SearchContainer<LayoutPageTemplateEntry>
			assetDisplayPageSearchContainer = new SearchContainer<>(
				_portletRequest, _getPortletURL(), null,
				"there-are-no-display-pages");

		assetDisplayPageSearchContainer.setOrderByCol(_getOrderByCol());

		OrderByComparator<LayoutPageTemplateEntry> orderByComparator =
			_getLayoutPageTemplateEntryOrderByComparator(
				_getOrderByCol(), getOrderByType());

		assetDisplayPageSearchContainer.setOrderByComparator(orderByComparator);

		assetDisplayPageSearchContainer.setOrderByType(getOrderByType());

		List<LayoutPageTemplateEntry> layoutPageTemplateEntries = null;
		int layoutPageTemplateEntriesCount = 0;

		if (Validator.isNotNull(_getKeywords())) {
			layoutPageTemplateEntriesCount =
				LayoutPageTemplateEntryServiceUtil.
					getLayoutPageTemplateEntriesCount(
						_themeDisplay.getScopeGroupId(),
						_assetDisplayPageSelectorCriterion.getClassNameId(),
						_assetDisplayPageSelectorCriterion.getClassTypeId(),
						_getKeywords(),
						LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE,
						WorkflowConstants.STATUS_APPROVED);

			layoutPageTemplateEntries =
				LayoutPageTemplateEntryServiceUtil.getLayoutPageTemplateEntries(
					_themeDisplay.getScopeGroupId(),
					_assetDisplayPageSelectorCriterion.getClassNameId(),
					_assetDisplayPageSelectorCriterion.getClassTypeId(),
					_getKeywords(),
					LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE,
					WorkflowConstants.STATUS_APPROVED,
					assetDisplayPageSearchContainer.getStart(),
					assetDisplayPageSearchContainer.getEnd(),
					assetDisplayPageSearchContainer.getOrderByComparator());
		}
		else {
			layoutPageTemplateEntriesCount =
				LayoutPageTemplateEntryServiceUtil.
					getLayoutPageTemplateEntriesCount(
						_themeDisplay.getScopeGroupId(),
						_assetDisplayPageSelectorCriterion.getClassNameId(),
						_assetDisplayPageSelectorCriterion.getClassTypeId(),
						LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE,
						WorkflowConstants.STATUS_APPROVED);

			layoutPageTemplateEntries =
				LayoutPageTemplateEntryServiceUtil.getLayoutPageTemplateEntries(
					_themeDisplay.getScopeGroupId(),
					_assetDisplayPageSelectorCriterion.getClassNameId(),
					_assetDisplayPageSelectorCriterion.getClassTypeId(),
					LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE,
					WorkflowConstants.STATUS_APPROVED,
					assetDisplayPageSearchContainer.getStart(),
					assetDisplayPageSearchContainer.getEnd(),
					assetDisplayPageSearchContainer.getOrderByComparator());
		}

		assetDisplayPageSearchContainer.setTotal(
			layoutPageTemplateEntriesCount);
		assetDisplayPageSearchContainer.setResults(layoutPageTemplateEntries);

		_assetDisplayPageSearchContainer = assetDisplayPageSearchContainer;

		return _assetDisplayPageSearchContainer;
	}

	public String getClearResultsURL() throws PortletException {
		PortletURL clearResultsURL = _getPortletURL();

		clearResultsURL.setParameter("keywords", StringPool.BLANK);

		return clearResultsURL.toString();
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

	public String getItemSelectedEventName() {
		return _itemSelectedEventName;
	}

	public String getOrderByType() {
		if (Validator.isNotNull(_orderByType)) {
			return _orderByType;
		}

		_orderByType = ParamUtil.getString(_request, "orderByType", "asc");

		return _orderByType;
	}

	public String getSearchActionURL() throws PortletException {
		PortletURL searchActionURL = _getPortletURL();

		return searchActionURL.toString();
	}

	public String getSortingURL() throws PortletException {
		PortletURL sortingURL = _getPortletURL();

		sortingURL.setParameter(
			"orderByType",
			Objects.equals(getOrderByType(), "asc") ? "desc" : "asc");

		return sortingURL.toString();
	}

	public String getSubtypeLabel(
			LayoutPageTemplateEntry layoutPageTemplateEntry)
		throws PortalException {

		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				layoutPageTemplateEntry.getClassName());

		if ((assetRendererFactory == null) ||
			(layoutPageTemplateEntry.getClassTypeId() <= 0)) {

			return StringPool.BLANK;
		}

		ClassTypeReader classTypeReader =
			assetRendererFactory.getClassTypeReader();

		ClassType classType = classTypeReader.getClassType(
			layoutPageTemplateEntry.getClassTypeId(),
			_themeDisplay.getLocale());

		return classType.getName();
	}

	public int getTotalItems() throws Exception {
		SearchContainer assetDisplayPageSearchContainer =
			getAssetDisplayPageSearchContainer();

		return assetDisplayPageSearchContainer.getTotal();
	}

	public String getTypeLabel(
		LayoutPageTemplateEntry layoutPageTemplateEntry) {

		AssetDisplayContributor assetDisplayContributor =
			_assetDisplayContributorTracker.getAssetDisplayContributor(
				layoutPageTemplateEntry.getClassName());

		if (assetDisplayContributor == null) {
			return StringPool.BLANK;
		}

		return assetDisplayContributor.getLabel(_themeDisplay.getLocale());
	}

	public boolean isDisabledManagementBar() throws Exception {
		if (getTotalItems() > 0) {
			return false;
		}

		if (Validator.isNotNull(_getKeywords())) {
			return false;
		}

		return true;
	}

	private List<DropdownItem> _getFilterNavigationDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					SafeConsumer.ignore(
						dropdownItem -> {
							dropdownItem.setActive(true);
							dropdownItem.setHref(_getPortletURL());
							dropdownItem.setLabel(
								LanguageUtil.get(_request, "all"));
						}));
			}
		};
	}

	private String _getKeywords() {
		if (Validator.isNotNull(_keywords)) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_request, "keywords");

		return _keywords;
	}

	private OrderByComparator<LayoutPageTemplateEntry>
		_getLayoutPageTemplateEntryOrderByComparator(
			String orderByCol, String orderByType) {

		boolean orderByAsc = false;

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		OrderByComparator<LayoutPageTemplateEntry> orderByComparator = null;

		if (orderByCol.equals("create-date")) {
			orderByComparator = new LayoutPageTemplateEntryCreateDateComparator(
				orderByAsc);
		}
		else if (orderByCol.equals("name")) {
			orderByComparator = new LayoutPageTemplateEntryNameComparator(
				orderByAsc);
		}

		return orderByComparator;
	}

	private String _getOrderByCol() {
		if (Validator.isNotNull(_orderByCol)) {
			return _orderByCol;
		}

		_orderByCol = ParamUtil.getString(
			_request, "orderByCol", "create-date");

		return _orderByCol;
	}

	private List<DropdownItem> _getOrderByDropdownItems() {
		return new DropdownItemList() {
			{
				add(
					SafeConsumer.ignore(
						dropdownItem -> {
							dropdownItem.setActive(
								Objects.equals(
									_getOrderByCol(), "create-date"));
							dropdownItem.setHref(
								_getPortletURL(), "orderByCol", "create-date");
							dropdownItem.setLabel(
								LanguageUtil.get(_request, "create-date"));
						}));

				add(
					SafeConsumer.ignore(
						dropdownItem -> {
							dropdownItem.setActive(
								Objects.equals(_getOrderByCol(), "name"));
							dropdownItem.setHref(
								_getPortletURL(), "orderByCol", "name");
							dropdownItem.setLabel(
								LanguageUtil.get(_request, "name"));
						}));
			}
		};
	}

	private PortletURL _getPortletURL() throws PortletException {
		PortletURL portletURL = PortletURLUtil.clone(
			_portletURL,
			PortalUtil.getLiferayPortletResponse(_portletResponse));

		portletURL.setParameter("orderByCol", _getOrderByCol());
		portletURL.setParameter("orderByType", getOrderByType());

		return portletURL;
	}

	private final AssetDisplayContributorTracker
		_assetDisplayContributorTracker;
	private SearchContainer _assetDisplayPageSearchContainer;
	private final AssetDisplayPageSelectorCriterion
		_assetDisplayPageSelectorCriterion;
	private String _displayStyle;
	private final String _itemSelectedEventName;
	private String _keywords;
	private String _orderByCol;
	private String _orderByType;
	private final PortletRequest _portletRequest;
	private final PortletResponse _portletResponse;
	private final PortletURL _portletURL;
	private final HttpServletRequest _request;
	private final ThemeDisplay _themeDisplay;

}