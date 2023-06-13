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

package com.liferay.item.selector.web.internal;

import com.liferay.item.selector.ItemSelectorViewDescriptor;
import com.liferay.item.selector.TableItemView;
import com.liferay.portal.kernel.dao.search.SearchEntry;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.taglib.search.StatusSearchEntry;
import com.liferay.taglib.search.TextSearchEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Eudaldo Alonso
 */
public class DefaultTableItemView implements TableItemView {

	public DefaultTableItemView(
		ItemSelectorViewDescriptor.ItemDescriptor itemDescriptor) {

		_itemDescriptor = itemDescriptor;
	}

	@Override
	public List<String> getHeaderNames() {
		List<String> headerNames = new ArrayList<>();

		headerNames.add("title");
		headerNames.add("user");
		headerNames.add("modified-date");

		if (_itemDescriptor.getStatus() != null) {
			headerNames.add("status");
		}

		return headerNames;
	}

	@Override
	public List<SearchEntry> getSearchEntries(Locale locale) {
		List<SearchEntry> searchEntries = new ArrayList<>();

		TextSearchEntry titleTextSearchEntry = new TextSearchEntry();

		titleTextSearchEntry.setCssClass(
			"entry entry-selector table-cell-expand table-cell-minw-200");
		titleTextSearchEntry.setName(_itemDescriptor.getTitle(locale));

		searchEntries.add(titleTextSearchEntry);

		TextSearchEntry userNameSearchEntry = new TextSearchEntry();

		userNameSearchEntry.setCssClass(
			"table-cell-expand-smaller table-cell-minw-150");
		userNameSearchEntry.setName(_itemDescriptor.getUserName());

		searchEntries.add(userNameSearchEntry);

		TextSearchEntry modifiedDateSearchEntry = new TextSearchEntry();

		modifiedDateSearchEntry.setCssClass(
			"table-cell-expand-smallest table-cell-ws-nowrap");

		Date modifiedDate = _itemDescriptor.getModifiedDate();

		if (Objects.nonNull(modifiedDate)) {
			modifiedDateSearchEntry.setName(
				LanguageUtil.format(
					locale, "x-ago",
					LanguageUtil.getTimeDescription(
						locale,
						System.currentTimeMillis() - modifiedDate.getTime(),
						true)));
		}

		searchEntries.add(modifiedDateSearchEntry);

		if (_itemDescriptor.getStatus() != null) {
			StatusSearchEntry statusSearchEntry = new StatusSearchEntry();

			statusSearchEntry.setCssClass("text-nowrap");
			statusSearchEntry.setStatus(_itemDescriptor.getStatus());

			searchEntries.add(statusSearchEntry);
		}

		return searchEntries;
	}

	private final ItemSelectorViewDescriptor.ItemDescriptor _itemDescriptor;

}