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

package com.liferay.portal.kernel.backgroundtask;

import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.petra.lang.SafeClosable;
import com.liferay.petra.lang.SafeCloseable;

/**
 * @author Michael C. Han
 */
public class BackgroundTaskThreadLocal {

	public static long getBackgroundTaskId() {
		return _backgroundTaskId.get();
	}

	public static boolean hasBackgroundTask() {
		long backgroundTaskId = getBackgroundTaskId();

		if (backgroundTaskId > 0) {
			return true;
		}

		return false;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #setBackgroundTaskIdWithSafeClosable(long)}
	 */
	@Deprecated
	public static void setBackgroundTaskId(long backgroundTaskId) {
		if (backgroundTaskId > 0) {
			_backgroundTaskId.set(backgroundTaskId);
		}
	}

	/**
	 * @deprecated As of Cavanaugh (7.4.x), replaced by {@link
	 *             #setBackgroundTaskIdWithSafeCloseable(long)}
	 */
	@Deprecated
	public static SafeClosable setBackgroundTaskIdWithSafeClosable(
		long backgroundTaskId) {

		if (backgroundTaskId > 0) {
			return _backgroundTaskId.setWithSafeClosable(backgroundTaskId);
		}

		return () -> {
		};
	}

	public static SafeCloseable setBackgroundTaskIdWithSafeCloseable(
		long backgroundTaskId) {

		if (backgroundTaskId > 0) {
			return _backgroundTaskId.setWithSafeCloseable(backgroundTaskId);
		}

		return () -> {
		};
	}

	private static final CentralizedThreadLocal<Long> _backgroundTaskId =
		new CentralizedThreadLocal<>(
			BackgroundTaskThreadLocal.class + "._backgroundTaskId", () -> 0L);

}