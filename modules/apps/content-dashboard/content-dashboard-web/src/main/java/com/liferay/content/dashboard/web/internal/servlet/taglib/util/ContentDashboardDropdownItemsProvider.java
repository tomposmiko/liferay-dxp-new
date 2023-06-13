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

package com.liferay.content.dashboard.web.internal.servlet.taglib.util;

import com.liferay.content.dashboard.item.ContentDashboardItem;
import com.liferay.content.dashboard.item.action.ContentDashboardItemAction;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemListBuilder;
import com.liferay.info.item.InfoItemReference;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.url.builder.ResourceURLBuilder;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Cristina González
 */
public class ContentDashboardDropdownItemsProvider {

	public ContentDashboardDropdownItemsProvider(
		Language language, LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse, Portal portal) {

		_language = language;
		_liferayPortletRequest = liferayPortletRequest;
		_liferayPortletResponse = liferayPortletResponse;
		_portal = portal;
	}

	public List<DropdownItem> getDropdownItems(
		ContentDashboardItem<?> contentDashboardItem) {

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			_liferayPortletRequest);

		Locale locale = _portal.getLocale(_liferayPortletRequest);

		return DropdownItemListBuilder.addAll(
			DropdownItemList.of(
				(DropdownItem[])TransformUtil.transformToArray(
					contentDashboardItem.getContentDashboardItemActions(
						httpServletRequest,
						ContentDashboardItemAction.Type.VIEW,
						ContentDashboardItemAction.Type.EDIT),
					contentDashboardItemAction -> _toDropdownItem(
						contentDashboardItemAction, locale),
					DropdownItem.class))
		).addAll(
			DropdownItemList.of(
				() -> {
					InfoItemReference infoItemReference =
						contentDashboardItem.getInfoItemReference();

					return DropdownItemBuilder.setData(
						HashMapBuilder.<String, Object>put(
							"action", "showInfo"
						).put(
							"className", infoItemReference.getClassName()
						).put(
							"classPK", infoItemReference.getClassPK()
						).put(
							"fetchURL",
							ResourceURLBuilder.createResourceURL(
								_liferayPortletResponse
							).setBackURL(
								_portal.getCurrentURL(_liferayPortletRequest)
							).setParameter(
								"className", infoItemReference.getClassName()
							).setParameter(
								"classPK", infoItemReference.getClassPK()
							).setResourceID(
								"/content_dashboard" +
									"/get_content_dashboard_item_info"
							).buildString()
						).build()
					).setIcon(
						"info-circle-open"
					).setLabel(
						_language.get(locale, "info")
					).setQuickAction(
						true
					).build();
				})
		).addAll(
			TransformUtil.transform(
				contentDashboardItem.getContentDashboardItemActions(
					httpServletRequest,
					ContentDashboardItemAction.Type.VIEW_IN_PANEL),
				contentDashboardItemAction -> _toViewInPanelDropdownItem(
					contentDashboardItem, contentDashboardItemAction, locale))
		).build();
	}

	private DropdownItem _toDropdownItem(
		ContentDashboardItemAction contentDashboardItemAction, Locale locale) {

		if (contentDashboardItemAction == null) {
			return null;
		}

		return DropdownItemBuilder.setHref(
			contentDashboardItemAction.getURL(locale)
		).setIcon(
			contentDashboardItemAction.getIcon()
		).setLabel(
			contentDashboardItemAction.getLabel(locale)
		).setQuickAction(
			true
		).build();
	}

	private DropdownItem _toViewInPanelDropdownItem(
		ContentDashboardItem<?> contentDashboardItem,
		ContentDashboardItemAction contentDashboardItemAction, Locale locale) {

		InfoItemReference infoItemReference =
			contentDashboardItem.getInfoItemReference();

		return DropdownItemBuilder.setData(
			HashMapBuilder.<String, Object>put(
				"action", "showMetrics"
			).put(
				"className", infoItemReference.getClassName()
			).put(
				"classPK", infoItemReference.getClassPK()
			).put(
				"fetchURL", contentDashboardItemAction.getURL(locale)
			).build()
		).setIcon(
			contentDashboardItemAction.getIcon()
		).setLabel(
			contentDashboardItemAction.getLabel(locale)
		).setQuickAction(
			true
		).build();
	}

	private final Language _language;
	private final LiferayPortletRequest _liferayPortletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final Portal _portal;

}