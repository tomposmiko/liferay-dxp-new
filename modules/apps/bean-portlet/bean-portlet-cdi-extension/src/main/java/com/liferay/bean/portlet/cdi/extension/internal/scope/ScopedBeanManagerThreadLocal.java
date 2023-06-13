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

package com.liferay.bean.portlet.cdi.extension.internal.scope;

import com.liferay.petra.function.UnsafeRunnable;
import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Supplier;

import java.io.Closeable;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Neil Griffin
 */
public class ScopedBeanManagerThreadLocal {

	public static Runnable captureScopedBeanManagers() {
		Deque<ScopedBeanManager> scopedBeanManagers = _instance.get();

		return () -> _instance.set(scopedBeanManagers);
	}

	public static ScopedBeanManager getCurrentScopedBeanManager() {
		Deque<ScopedBeanManager> scopedBeanManagers = _instance.get();

		return scopedBeanManagers.peek();
	}

	public static Closeable install(ScopedBeanManager scopedBeanManager) {
		Deque<ScopedBeanManager> scopedBeanManagerStack = _instance.get();

		scopedBeanManagerStack.push(scopedBeanManager);

		return () -> {
			if (scopedBeanManagerStack.isEmpty()) {
				_log.error(
					"Unable to destroy scoped beans when scopes are inactive");
			}
			else {
				ScopedBeanManager closingScopedBeanManager =
					scopedBeanManagerStack.pop();

				if (scopedBeanManagerStack.isEmpty()) {
					closingScopedBeanManager.destroyScopedBeans();

					_instance.remove();
				}
			}
		};
	}

	public static <T extends Throwable> void invokeWithScopedBeanManager(
			UnsafeRunnable<T> unsafeRunnable,
			Supplier<ScopedBeanManager> supplier)
		throws T {

		Deque<ScopedBeanManager> scopedBeanManagers = _instance.get();

		boolean empty = scopedBeanManagers.isEmpty();

		if (empty) {
			scopedBeanManagers.push(supplier.get());
		}

		try {
			unsafeRunnable.run();
		}
		finally {
			if (empty) {
				scopedBeanManagers.pop();
			}
		}
	}

	public static void remove() {
		_instance.remove();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ScopedBeanManagerThreadLocal.class);

	private static final ThreadLocal<Deque<ScopedBeanManager>> _instance =
		new CentralizedThreadLocal<>(
			ScopedBeanManagerThreadLocal.class + "._instance",
			ConcurrentLinkedDeque::new);

}