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

package com.liferay.layout.util.comparator;

import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * @author Eudaldo Alonso
 */
public class LayoutLeftPlidComparator extends OrderByComparator<Layout> {

	public static final String ORDER_BY_ASC = "leftPlid ASC";

	public static final String ORDER_BY_DESC = "leftPlid DESC";

	public static final String[] ORDER_BY_FIELDS = {"leftPlid"};

	public LayoutLeftPlidComparator() {
		this(false);
	}

	public LayoutLeftPlidComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(Layout layout1, Layout layout2) {
		long leftPlid1 = layout1.getLeftPlid();
		long leftPlid2 = layout2.getLeftPlid();

		int value = 0;

		if (leftPlid1 < leftPlid2) {
			value = -1;
		}
		else if (leftPlid1 > leftPlid2) {
			value = 1;
		}

		if (_ascending) {
			return value;
		}

		return -value;
	}

	@Override
	public String getOrderBy() {
		if (_ascending) {
			return ORDER_BY_ASC;
		}

		return ORDER_BY_DESC;
	}

	@Override
	public String[] getOrderByFields() {
		return ORDER_BY_FIELDS;
	}

	@Override
	public boolean isAscending() {
		return _ascending;
	}

	private final boolean _ascending;

}