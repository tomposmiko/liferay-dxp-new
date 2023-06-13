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

package com.liferay.poshi.runner.junit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runners.Parameterized;
import org.junit.runners.model.RunnerScheduler;

/**
 * @author Kenji Heigel
 */
public class ParallelParameterized extends Parameterized {

	public ParallelParameterized(Class<?> clazz) throws Throwable {
		super(clazz);

		setScheduler(new ThreadPoolScheduler());
	}

	private static class ThreadPoolScheduler implements RunnerScheduler {

		public ThreadPoolScheduler() {
			_executorService = Executors.newFixedThreadPool(3);
		}

		@Override
		public void finished() {
			_executorService.shutdown();

			try {
				_executorService.awaitTermination(10, TimeUnit.MINUTES);
			}
			catch (InterruptedException interruptedException) {
				throw new RuntimeException(interruptedException);
			}
		}

		@Override
		public void schedule(Runnable childStatement) {
			_executorService.submit(childStatement);
		}

		private final ExecutorService _executorService;

	}

}