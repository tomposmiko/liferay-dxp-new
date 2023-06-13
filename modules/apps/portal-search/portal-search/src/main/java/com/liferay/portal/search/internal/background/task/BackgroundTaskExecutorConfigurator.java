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

package com.liferay.portal.search.internal.background.task;

import com.liferay.petra.executor.PortalExecutorManager;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.search.index.ConcurrentReindexManager;

import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Michael C. Han
 */
@Component(service = {})
public class BackgroundTaskExecutorConfigurator {

	@Activate
	protected void activate(BundleContext bundleContext) {
		BackgroundTaskExecutor reindexPortalBackgroundTaskExecutor =
			new ReindexPortalBackgroundTaskExecutor(
				bundleContext, _concurrentReindexManager,
				_portalExecutorManager);

		_registerBackgroundTaskExecutor(
			bundleContext, reindexPortalBackgroundTaskExecutor);

		_registerBackgroundTaskExecutor(
			bundleContext, _reindexSingleIndexerBackgroundTaskExecutor);
	}

	@Deactivate
	protected void deactivate() {
		for (ServiceRegistration<BackgroundTaskExecutor> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}
	}

	private void _registerBackgroundTaskExecutor(
		BundleContext bundleContext,
		BackgroundTaskExecutor backgroundTaskExecutor) {

		Class<?> clazz = backgroundTaskExecutor.getClass();

		ServiceRegistration<BackgroundTaskExecutor> serviceRegistration =
			bundleContext.registerService(
				BackgroundTaskExecutor.class, backgroundTaskExecutor,
				HashMapDictionaryBuilder.<String, Object>put(
					"background.task.executor.class.name", clazz.getName()
				).build());

		_serviceRegistrations.add(serviceRegistration);
	}

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile ConcurrentReindexManager _concurrentReindexManager;

	@Reference
	private PortalExecutorManager _portalExecutorManager;

	@Reference
	private ReindexSingleIndexerBackgroundTaskExecutor
		_reindexSingleIndexerBackgroundTaskExecutor;

	private final Set<ServiceRegistration<BackgroundTaskExecutor>>
		_serviceRegistrations = new HashSet<>();

}