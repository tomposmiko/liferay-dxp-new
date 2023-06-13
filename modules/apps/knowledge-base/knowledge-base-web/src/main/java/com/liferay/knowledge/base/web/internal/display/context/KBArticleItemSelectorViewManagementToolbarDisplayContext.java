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

package com.liferay.knowledge.base.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.SearchContainerManagementToolbarDisplayContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.util.ArrayUtil;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alicia García
 */
public class KBArticleItemSelectorViewManagementToolbarDisplayContext
	extends SearchContainerManagementToolbarDisplayContext {

	public KBArticleItemSelectorViewManagementToolbarDisplayContext(
			HttpServletRequest httpServletRequest,
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			KBArticleItemSelectorViewDisplayContext
				kbArticleItemSelectorViewDisplayContext)
		throws Exception {

		super(
			httpServletRequest, liferayPortletRequest, liferayPortletResponse,
			kbArticleItemSelectorViewDisplayContext.getSearchContainer());

		_kbArticleItemSelectorViewDisplayContext =
			kbArticleItemSelectorViewDisplayContext;
	}

	@Override
	public String getClearResultsURL() {
		return PortletURLBuilder.create(
			getPortletURL()
		).setKeywords(
			StringPool.BLANK
		).setParameter(
			"scope", StringPool.BLANK
		).buildString();
	}

	@Override
	public String getSortingOrder() {
		if (Objects.equals(getOrderByCol(), "priority")) {
			return null;
		}

		return super.getSortingOrder();
	}

	@Override
	public Boolean isDisabled() {
		return false;
	}

	@Override
	public Boolean isSelectable() {
		return false;
	}

	@Override
	protected String getDefaultDisplayStyle() {
		return "descriptive";
	}

	@Override
	protected String[] getOrderByKeys() {
		String[] orderColumns = {
			"priority", "modified-date", "title", "view-count"
		};

		if (_kbArticleItemSelectorViewDisplayContext.isSearch()) {
			orderColumns = ArrayUtil.append(orderColumns, "relevance");
		}

		return orderColumns;
	}

	private final KBArticleItemSelectorViewDisplayContext
		_kbArticleItemSelectorViewDisplayContext;

}