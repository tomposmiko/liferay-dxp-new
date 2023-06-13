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

package com.liferay.layout.page.template.admin.web.internal.display.context;

import com.liferay.asset.display.page.model.AssetDisplayPageEntry;
import com.liferay.asset.display.page.service.AssetDisplayPageEntryService;
import com.liferay.asset.kernel.exception.NoSuchEntryException;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryService;
import com.liferay.info.field.InfoFieldValue;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.item.provider.InfoItemFieldValuesProvider;
import com.liferay.info.item.provider.InfoItemObjectProvider;
import com.liferay.info.search.InfoSearchClassMapperTracker;
import com.liferay.layout.page.template.admin.constants.LayoutPageTemplateAdminPortletKeys;
import com.liferay.layout.page.template.admin.web.internal.util.comparator.AssetDisplayPageEntryModifiedDateComparator;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.SearchOrderByUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;
import java.util.Objects;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rubén Pulido
 */
public class AssetDisplayPageUsagesDisplayContext {

	public AssetDisplayPageUsagesDisplayContext(
		AssetDisplayPageEntryService assetDisplayPageEntryService,
		AssetEntryService assetEntryService,
		HttpServletRequest httpServletRequest,
		InfoSearchClassMapperTracker infoSearchClassMapperTracker,
		InfoItemServiceRegistry infoItemServiceRegistry, Portal portal,
		RenderRequest renderRequest, RenderResponse renderResponse) {

		_assetDisplayPageEntryService = assetDisplayPageEntryService;
		_assetEntryService = assetEntryService;
		_httpServletRequest = httpServletRequest;
		_infoSearchClassMapperTracker = infoSearchClassMapperTracker;
		_infoItemServiceRegistry = infoItemServiceRegistry;
		_portal = portal;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
	}

	public long getClassNameId() {
		if (Validator.isNotNull(_classNameId)) {
			return _classNameId;
		}

		_classNameId = ParamUtil.getLong(_httpServletRequest, "classNameId");

		return _classNameId;
	}

	public long getClassTypeId() {
		if (Validator.isNotNull(_classTypeId)) {
			return _classTypeId;
		}

		_classTypeId = ParamUtil.getLong(_httpServletRequest, "classTypeId");

		return _classTypeId;
	}

	public long getLayoutPageTemplateEntryId() {
		if (Validator.isNotNull(_layoutPageTemplateEntryId)) {
			return _layoutPageTemplateEntryId;
		}

		_layoutPageTemplateEntryId = ParamUtil.getLong(
			_httpServletRequest, "layoutPageTemplateEntryId");

		return _layoutPageTemplateEntryId;
	}

	public String getOrderByCol() {
		if (Validator.isNotNull(_orderByCol)) {
			return _orderByCol;
		}

		_orderByCol = SearchOrderByUtil.getOrderByCol(
			_httpServletRequest,
			LayoutPageTemplateAdminPortletKeys.LAYOUT_PAGE_TEMPLATES,
			"asset-display-usage-order-by-col", "modified-date");

		return _orderByCol;
	}

	public String getOrderByType() {
		if (Validator.isNotNull(_orderByType)) {
			return _orderByType;
		}

		_orderByType = SearchOrderByUtil.getOrderByType(
			_httpServletRequest,
			LayoutPageTemplateAdminPortletKeys.LAYOUT_PAGE_TEMPLATES,
			"asset-display-usage-order-by-type", "asc");

		return _orderByType;
	}

	public PortletURL getPortletURL() {
		return PortletURLBuilder.createRenderURL(
			_renderResponse
		).setMVCRenderCommandName(
			"/layout_page_template_admin/view_asset_display_page_usages"
		).setRedirect(
			getRedirect()
		).setParameter(
			"layoutPageTemplateEntryId", getLayoutPageTemplateEntryId()
		).buildPortletURL();
	}

	public String getRedirect() {
		if (_redirect != null) {
			return _redirect;
		}

		_redirect = ParamUtil.getString(_renderRequest, "redirect");

		return _redirect;
	}

	public SearchContainer<AssetDisplayPageEntry> getSearchContainer() {
		if (_searchContainer != null) {
			return _searchContainer;
		}

		SearchContainer<AssetDisplayPageEntry> searchContainer =
			new SearchContainer<>(
				_renderRequest, getPortletURL(), null,
				"there-are-no-display-page-template-usages");

		searchContainer.setOrderByCol(getOrderByCol());

		boolean orderByAsc = false;

		if (Objects.equals(getOrderByType(), "asc")) {
			orderByAsc = true;
		}

		searchContainer.setOrderByComparator(
			new AssetDisplayPageEntryModifiedDateComparator(orderByAsc));
		searchContainer.setOrderByType(getOrderByType());
		searchContainer.setResultsAndTotal(
			() -> _assetDisplayPageEntryService.getAssetDisplayPageEntries(
				getClassNameId(), getClassTypeId(),
				getLayoutPageTemplateEntryId(), isDefaultTemplate(),
				searchContainer.getStart(), searchContainer.getEnd(),
				searchContainer.getOrderByComparator()),
			_assetDisplayPageEntryService.getAssetDisplayPageEntriesCount(
				getClassNameId(), getClassTypeId(),
				getLayoutPageTemplateEntryId(), isDefaultTemplate()));
		searchContainer.setRowChecker(
			new EmptyOnClickRowChecker(_renderResponse));

		_searchContainer = searchContainer;

		return _searchContainer;
	}

	public String getTitle(
			AssetDisplayPageEntry assetDisplayPageEntry, Locale locale)
		throws PortalException {

		String className = _infoSearchClassMapperTracker.getSearchClassName(
			assetDisplayPageEntry.getClassName());

		try {
			AssetEntry assetEntry = _assetEntryService.getEntry(
				className, assetDisplayPageEntry.getClassPK());

			return assetEntry.getTitle(locale);
		}
		catch (PortalException portalException) {
			if (!(portalException instanceof NoSuchEntryException)) {
				throw portalException;
			}
		}

		InfoItemObjectProvider<?> infoItemObjectProvider =
			_infoItemServiceRegistry.getFirstInfoItemService(
				InfoItemObjectProvider.class,
				_portal.getClassName(getClassNameId()));

		if (infoItemObjectProvider == null) {
			return StringPool.BLANK;
		}

		InfoItemFieldValuesProvider<Object> infoItemFieldValuesProvider =
			_infoItemServiceRegistry.getFirstInfoItemService(
				InfoItemFieldValuesProvider.class,
				_portal.getClassName(getClassNameId()));

		if (infoItemFieldValuesProvider == null) {
			return StringPool.BLANK;
		}

		Object infoItem = infoItemObjectProvider.getInfoItem(
			new ClassPKInfoItemIdentifier(assetDisplayPageEntry.getClassPK()));

		if (infoItem == null) {
			return StringPool.BLANK;
		}

		InfoFieldValue<Object> infoFieldValue =
			infoItemFieldValuesProvider.getInfoFieldValue(infoItem, "title");

		if (infoFieldValue == null) {
			return StringPool.BLANK;
		}

		Object infoFieldValueValue = infoFieldValue.getValue(
			LocaleUtil.getMostRelevantLocale());

		if (infoFieldValueValue == null) {
			return StringPool.BLANK;
		}

		return String.valueOf(infoFieldValueValue);
	}

	public boolean isDefaultTemplate() {
		if (_defaultTemplate != null) {
			return _defaultTemplate;
		}

		_defaultTemplate = ParamUtil.getBoolean(
			_httpServletRequest, "defaultTemplate");

		return _defaultTemplate;
	}

	private final AssetDisplayPageEntryService _assetDisplayPageEntryService;
	private final AssetEntryService _assetEntryService;
	private Long _classNameId;
	private Long _classTypeId;
	private Boolean _defaultTemplate;
	private final HttpServletRequest _httpServletRequest;
	private final InfoItemServiceRegistry _infoItemServiceRegistry;
	private final InfoSearchClassMapperTracker _infoSearchClassMapperTracker;
	private Long _layoutPageTemplateEntryId;
	private String _orderByCol;
	private String _orderByType;
	private final Portal _portal;
	private String _redirect;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private SearchContainer<AssetDisplayPageEntry> _searchContainer;

}