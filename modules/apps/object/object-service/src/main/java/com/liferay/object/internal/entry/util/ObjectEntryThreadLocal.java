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

package com.liferay.object.internal.entry.util;

import com.liferay.petra.lang.CentralizedThreadLocal;

/**
 * @author Marcela Cunha
 */
public class ObjectEntryThreadLocal {

	public static boolean isSkipObjectEntryResourcePermission() {
		return _skipObjectEntryResourcePermissionThreadLocal.get();
	}

	public static void setSkipObjectEntryResourcePermission(
		boolean skipObjectEntryResourcePermission) {

		_skipObjectEntryResourcePermissionThreadLocal.set(
			skipObjectEntryResourcePermission);
	}

	private static final ThreadLocal<Boolean>
		_skipObjectEntryResourcePermissionThreadLocal =
			new CentralizedThreadLocal<>(
				ObjectEntryThreadLocal.class +
					"._skipObjectEntryResourcePermissionThreadLocal",
				() -> false);

}