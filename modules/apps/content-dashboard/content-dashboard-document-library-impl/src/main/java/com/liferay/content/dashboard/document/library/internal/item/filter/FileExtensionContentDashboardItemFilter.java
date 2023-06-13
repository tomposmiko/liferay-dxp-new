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

package com.liferay.content.dashboard.document.library.internal.item.filter;

import com.liferay.content.dashboard.document.library.internal.item.selector.file.extension.criterio.ContentDashboardFileExtensionItemSelectorCriterion;
import com.liferay.content.dashboard.item.filter.ContentDashboardItemFilter;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.criteria.UUIDItemSelectorReturnType;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Cristina González
 */
public class FileExtensionContentDashboardItemFilter
	implements ContentDashboardItemFilter {

	public FileExtensionContentDashboardItemFilter(
		HttpServletRequest httpServletRequest, ItemSelector itemSelector,
		Language language, Portal portal) {

		_httpServletRequest = httpServletRequest;
		_itemSelector = itemSelector;
		_language = language;
		_portal = portal;
	}

	@Override
	public String getIcon() {
		return null;
	}

	@Override
	public String getLabel(Locale locale) {
		return _language.get(locale, "filter-by-extension");
	}

	@Override
	public String getName() {
		return "file-extension";
	}

	@Override
	public String getParameterLabel(Locale locale) {
		return _language.get(locale, "extension");
	}

	@Override
	public String getParameterName() {
		return "fileExtension";
	}

	@Override
	public List<String> getParameterValues() {
		return _getFileExtensions(_httpServletRequest);
	}

	@Override
	public Type getType() {
		return Type.ITEM_SELECTOR;
	}

	@Override
	public String getURL() {
		try {
			PortletRequest portletRequest =
				(PortletRequest)_httpServletRequest.getAttribute(
					JavaConstants.JAVAX_PORTLET_REQUEST);

			RequestBackedPortletURLFactory requestBackedPortletURLFactory =
				RequestBackedPortletURLFactoryUtil.create(portletRequest);

			ContentDashboardFileExtensionItemSelectorCriterion
				contentDashboardFileExtensionItemSelectorCriterion =
					new ContentDashboardFileExtensionItemSelectorCriterion();

			contentDashboardFileExtensionItemSelectorCriterion.
				setDesiredItemSelectorReturnTypes(
					Collections.singletonList(
						new UUIDItemSelectorReturnType()));

			PortletResponse portletResponse =
				(PortletResponse)_httpServletRequest.getAttribute(
					JavaConstants.JAVAX_PORTLET_RESPONSE);

			return PortletURLBuilder.create(
				_itemSelector.getItemSelectorURL(
					requestBackedPortletURLFactory,
					portletResponse.getNamespace() + "selectedFileExtension",
					contentDashboardFileExtensionItemSelectorCriterion)
			).setParameter(
				"checkedFileExtensions",
				() -> {
					List<String> fileExtensions = getParameterValues();

					return fileExtensions.toArray(new String[0]);
				}
			).buildString();
		}
		catch (Exception exception) {
			_log.error(exception);

			return StringPool.BLANK;
		}
	}

	private List<String> _getFileExtensions(
		HttpServletRequest httpServletRequest) {

		return Arrays.asList(
			ParamUtil.getStringValues(httpServletRequest, "fileExtension"));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FileExtensionContentDashboardItemFilter.class);

	private final HttpServletRequest _httpServletRequest;
	private final ItemSelector _itemSelector;
	private final Language _language;
	private final Portal _portal;

}