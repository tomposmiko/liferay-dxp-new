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

package com.liferay.portal.kernel.util;

import java.util.TreeMap;

/**
 * @author Hugo Huijser
 */
public class TreeMapBuilder<K, V> {

	public static <K, V> TreeMapWrapper<K, V> put(K key, V value) {
		return new TreeMapWrapper<>(key, value);
	}

	public static final class TreeMapWrapper<K, V> {

		public TreeMapWrapper(K key, V value) {
			_treeMap.put(key, value);
		}

		public TreeMap<K, V> build() {
			return _treeMap;
		}

		public TreeMapWrapper<K, V> put(K key, V value) {
			_treeMap.put(key, value);

			return this;
		}

		private final TreeMap<K, V> _treeMap = new TreeMap<>();

	}

}