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

package com.liferay.content.dashboard.web.internal.display.context;

import com.liferay.content.dashboard.web.internal.item.type.ContentDashboardItemType;
import com.liferay.portal.kernel.dao.search.SearchContainer;

/**
 * @author Cristina González
 */
public class ContentDashboardItemTypeItemSelectorViewDisplayContext {

	public ContentDashboardItemTypeItemSelectorViewDisplayContext(
		String itemSelectedEventName,
		SearchContainer<? extends ContentDashboardItemType> searchContainer) {

		_itemSelectedEventName = itemSelectedEventName;
		_searchContainer = searchContainer;
	}

	public String getItemSelectedEventName() {
		return _itemSelectedEventName;
	}

	public SearchContainer<? extends ContentDashboardItemType>
		getSearchContainer() {

		return _searchContainer;
	}

	private final String _itemSelectedEventName;
	private final SearchContainer<? extends ContentDashboardItemType>
		_searchContainer;

}