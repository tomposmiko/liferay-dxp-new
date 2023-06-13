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

package com.liferay.layout.taglib.internal.display.context;

import com.liferay.fragment.helper.FragmentEntryLinkHelper;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentEntryLinkLocalServiceUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.layout.model.LayoutClassedModelUsage;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.service.LayoutClassedModelUsageLocalServiceUtil;
import com.liferay.layout.taglib.internal.helper.LayoutClassedModelUsagesHelper;
import com.liferay.layout.taglib.internal.servlet.ServletContextUtil;
import com.liferay.layout.util.LayoutClassedModelUsageActionMenuContributor;
import com.liferay.layout.util.LayoutClassedModelUsageActionMenuContributorRegistryUtil;
import com.liferay.layout.util.comparator.LayoutClassedModelUsageModifiedDateComparator;
import com.liferay.layout.util.constants.LayoutClassedModelUsageConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Pavel Savinov
 */
public class LayoutClassedModelUsagesDisplayContext {

	public LayoutClassedModelUsagesDisplayContext(
		RenderRequest renderRequest, RenderResponse renderResponse,
		String className, long classPK) {

		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
		_className = className;
		_classPK = classPK;

		_classNameId = PortalUtil.getClassNameId(className);
		_fragmentEntryLinkHelper =
			(FragmentEntryLinkHelper)renderRequest.getAttribute(
				FragmentEntryLinkHelper.class.getName());
		_themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public int getAllUsageCount() {
		return LayoutClassedModelUsageLocalServiceUtil.
			getLayoutClassedModelUsagesCount(_classNameId, _classPK);
	}

	public int getDisplayPagesUsageCount() {
		return LayoutClassedModelUsageLocalServiceUtil.
			getLayoutClassedModelUsagesCount(
				_classNameId, _classPK,
				LayoutClassedModelUsageConstants.TYPE_DISPLAY_PAGE_TEMPLATE);
	}

	public List<DropdownItem> getLayoutClassedModelUsageActionDropdownItems(
		LayoutClassedModelUsage layoutClassedModelUsage) {

		if (!isShowPreview(layoutClassedModelUsage)) {
			return Collections.emptyList();
		}

		LayoutClassedModelUsageActionMenuContributor
			layoutClassedModelUsageActionMenuContributor =
				LayoutClassedModelUsageActionMenuContributorRegistryUtil.
					getLayoutClassedModelUsageActionMenuContributor(_className);

		if (layoutClassedModelUsageActionMenuContributor == null) {
			return Collections.emptyList();
		}

		return layoutClassedModelUsageActionMenuContributor.
			getLayoutClassedModelUsageActionDropdownItems(
				PortalUtil.getHttpServletRequest(_renderRequest),
				layoutClassedModelUsage);
	}

	public String getLayoutClassedModelUsageName(
		LayoutClassedModelUsage layoutClassedModelUsage) {

		LayoutClassedModelUsagesHelper layoutClassedModelUsagesHelper =
			ServletContextUtil.getLayoutClassedModelUsagesHelper();

		return layoutClassedModelUsagesHelper.getName(
			layoutClassedModelUsage, _themeDisplay.getLocale());
	}

	public String getLayoutClassedModelUsageTypeLabel(
		LayoutClassedModelUsage layoutClassedModelUsage) {

		LayoutClassedModelUsagesHelper layoutClassedModelUsagesHelper =
			ServletContextUtil.getLayoutClassedModelUsagesHelper();

		return layoutClassedModelUsagesHelper.getTypeLabel(
			layoutClassedModelUsage);
	}

	public String getLayoutClassedModelUsageWhereLabel(
			LayoutClassedModelUsage layoutClassedModelUsage)
		throws PortalException {

		if ((layoutClassedModelUsage.getContainerType() !=
				PortalUtil.getClassNameId(FragmentEntryLink.class)) &&
			(layoutClassedModelUsage.getContainerType() !=
				PortalUtil.getClassNameId(LayoutPageTemplateStructure.class))) {

			return LanguageUtil.format(
				_themeDisplay.getLocale(), "x-widget",
				PortalUtil.getPortletTitle(
					PortletIdCodec.decodePortletName(
						layoutClassedModelUsage.getContainerKey()),
					_themeDisplay.getLocale()));
		}

		if (layoutClassedModelUsage.getContainerType() ==
				PortalUtil.getClassNameId(FragmentEntryLink.class)) {

			FragmentEntryLink fragmentEntryLink =
				FragmentEntryLinkLocalServiceUtil.getFragmentEntryLink(
					GetterUtil.getLong(
						layoutClassedModelUsage.getContainerKey()));

			String name = _fragmentEntryLinkHelper.getFragmentEntryName(
				fragmentEntryLink, _themeDisplay.getLocale());

			if (Validator.isNull(name)) {
				return StringPool.BLANK;
			}

			if (!fragmentEntryLink.isTypeSection()) {
				return LanguageUtil.format(
					_themeDisplay.getLocale(), "x-element", name);
			}

			return LanguageUtil.format(
				_themeDisplay.getLocale(), "x-section", name);
		}

		if (layoutClassedModelUsage.getContainerType() ==
				PortalUtil.getClassNameId(LayoutPageTemplateStructure.class)) {

			return LanguageUtil.get(_themeDisplay.getLocale(), "section");
		}

		return StringPool.BLANK;
	}

	public String getNavigation() {
		if (Validator.isNotNull(_navigation)) {
			return _navigation;
		}

		_navigation = ParamUtil.getString(_renderRequest, "navigation", "all");

		return _navigation;
	}

	public int getPagesUsageCount() {
		return LayoutClassedModelUsageLocalServiceUtil.
			getLayoutClassedModelUsagesCount(
				_classNameId, _classPK,
				LayoutClassedModelUsageConstants.TYPE_LAYOUT);
	}

	public int getPageTemplatesUsageCount() {
		return LayoutClassedModelUsageLocalServiceUtil.
			getLayoutClassedModelUsagesCount(
				_classNameId, _classPK,
				LayoutClassedModelUsageConstants.TYPE_PAGE_TEMPLATE);
	}

	public PortletURL getPortletURL() throws PortletException {
		return PortletURLUtil.clone(
			PortletURLUtil.getCurrent(_renderRequest, _renderResponse),
			_renderResponse);
	}

	public String getPreviewURL(LayoutClassedModelUsage layoutClassedModelUsage)
		throws Exception {

		LayoutClassedModelUsagesHelper layoutClassedModelUsagesHelper =
			ServletContextUtil.getLayoutClassedModelUsagesHelper();

		return layoutClassedModelUsagesHelper.getPreviewURL(
			layoutClassedModelUsage,
			PortalUtil.getHttpServletRequest(_renderRequest));
	}

	public String getRedirect() {
		if (_redirect != null) {
			return _redirect;
		}

		_redirect = ParamUtil.getString(_renderRequest, "redirect");

		return _redirect;
	}

	public SearchContainer<LayoutClassedModelUsage> getSearchContainer()
		throws PortletException {

		if (_searchContainer != null) {
			return _searchContainer;
		}

		SearchContainer<LayoutClassedModelUsage>
			layoutClassedModelUsagesSearchContainer = new SearchContainer(
				_renderRequest, getPortletURL(), null, "there-are-no-usages");

		layoutClassedModelUsagesSearchContainer.setOrderByCol(_getOrderByCol());

		boolean orderByAsc = false;

		String orderByType = _getOrderByType();

		if (orderByType.equals("asc")) {
			orderByAsc = true;
		}

		layoutClassedModelUsagesSearchContainer.setOrderByComparator(
			new LayoutClassedModelUsageModifiedDateComparator(orderByAsc));
		layoutClassedModelUsagesSearchContainer.setOrderByType(
			_getOrderByType());

		if (Objects.equals(getNavigation(), "pages")) {
			layoutClassedModelUsagesSearchContainer.setResultsAndTotal(
				() ->
					LayoutClassedModelUsageLocalServiceUtil.
						getLayoutClassedModelUsages(
							_classNameId, _classPK,
							LayoutClassedModelUsageConstants.TYPE_LAYOUT,
							layoutClassedModelUsagesSearchContainer.getStart(),
							layoutClassedModelUsagesSearchContainer.getEnd(),
							layoutClassedModelUsagesSearchContainer.
								getOrderByComparator()),
				getPagesUsageCount());
		}
		else if (Objects.equals(getNavigation(), "page-templates")) {
			layoutClassedModelUsagesSearchContainer.setResultsAndTotal(
				() ->
					LayoutClassedModelUsageLocalServiceUtil.
						getLayoutClassedModelUsages(
							_classNameId, _classPK,
							LayoutClassedModelUsageConstants.TYPE_PAGE_TEMPLATE,
							layoutClassedModelUsagesSearchContainer.getStart(),
							layoutClassedModelUsagesSearchContainer.getEnd(),
							layoutClassedModelUsagesSearchContainer.
								getOrderByComparator()),
				getPageTemplatesUsageCount());
		}
		else if (Objects.equals(getNavigation(), "display-page-templates")) {
			layoutClassedModelUsagesSearchContainer.setResultsAndTotal(
				() ->
					LayoutClassedModelUsageLocalServiceUtil.
						getLayoutClassedModelUsages(
							_classNameId, _classPK,
							LayoutClassedModelUsageConstants.
								TYPE_DISPLAY_PAGE_TEMPLATE,
							layoutClassedModelUsagesSearchContainer.getStart(),
							layoutClassedModelUsagesSearchContainer.getEnd(),
							layoutClassedModelUsagesSearchContainer.
								getOrderByComparator()),
				getDisplayPagesUsageCount());
		}
		else {
			layoutClassedModelUsagesSearchContainer.setResultsAndTotal(
				() ->
					LayoutClassedModelUsageLocalServiceUtil.
						getLayoutClassedModelUsages(
							_classNameId, _classPK,
							layoutClassedModelUsagesSearchContainer.getStart(),
							layoutClassedModelUsagesSearchContainer.getEnd(),
							layoutClassedModelUsagesSearchContainer.
								getOrderByComparator()),
				getAllUsageCount());
		}

		_searchContainer = layoutClassedModelUsagesSearchContainer;

		return _searchContainer;
	}

	public Map<String, Object> getUsagesData() {
		return HashMapBuilder.<String, Object>put(
			"getUsagesURL",
			_getLayoutClassedModelUsagesURL(_className, _classPK)
		).build();
	}

	public boolean isShowPreview(
		LayoutClassedModelUsage layoutClassedModelUsage) {

		LayoutClassedModelUsagesHelper layoutClassedModelUsagesHelper =
			ServletContextUtil.getLayoutClassedModelUsagesHelper();

		return layoutClassedModelUsagesHelper.isShowPreview(
			layoutClassedModelUsage);
	}

	private String _getLayoutClassedModelUsagesURL(
		String className, long classPK) {

		StringBundler sb = new StringBundler(6);

		sb.append(
			PortalUtil.getPortalURL(
				PortalUtil.getHttpServletRequest(_renderRequest)));
		sb.append(_themeDisplay.getPathMain());
		sb.append("/portal/get_layout_classed_model_usages?className=");
		sb.append(className);
		sb.append("&classPK=");
		sb.append(String.valueOf(classPK));

		return sb.toString();
	}

	private String _getOrderByCol() {
		if (Validator.isNotNull(_orderByCol)) {
			return _orderByCol;
		}

		_orderByCol = ParamUtil.getString(
			_renderRequest, "orderByCol", "modified-date");

		return _orderByCol;
	}

	private String _getOrderByType() {
		if (Validator.isNotNull(_orderByType)) {
			return _orderByType;
		}

		_orderByType = ParamUtil.getString(
			_renderRequest, "orderByType", "asc");

		return _orderByType;
	}

	private final String _className;
	private final long _classNameId;
	private final long _classPK;
	private final FragmentEntryLinkHelper _fragmentEntryLinkHelper;
	private String _navigation;
	private String _orderByCol;
	private String _orderByType;
	private String _redirect;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private SearchContainer<LayoutClassedModelUsage> _searchContainer;
	private final ThemeDisplay _themeDisplay;

}