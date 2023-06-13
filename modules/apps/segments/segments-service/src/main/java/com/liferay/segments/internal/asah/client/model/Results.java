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

package com.liferay.segments.internal.asah.client.model;

import java.util.Collections;
import java.util.List;

/**
 * @author Shinn Lok
 * @author David Arques
 */
public class Results<T> {

	public Results() {
	}

	public Results(List<T> items, int total) {
		_items = items;
		_total = total;
	}

	public List<T> getItems() {
		return _items;
	}

	public int getTotal() {
		return _total;
	}

	public void setItems(List<T> items) {
		_items = items;
	}

	public void setTotal(int total) {
		_total = total;
	}

	private List<T> _items = Collections.emptyList();
	private int _total;

}