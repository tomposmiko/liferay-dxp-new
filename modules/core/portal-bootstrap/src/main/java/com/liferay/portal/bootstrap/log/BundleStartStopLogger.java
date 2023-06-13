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

package com.liferay.portal.bootstrap.log;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.concurrent.atomic.AtomicBoolean;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Raymond Augé
 */
public class BundleStartStopLogger implements SynchronousBundleListener {

	public BundleStartStopLogger(BundleContext bundleContext)
		throws InvalidSyntaxException {

		ServiceTracker<Object, Void> serviceTracker =
			new ServiceTracker<Object, Void>(
				bundleContext,
				bundleContext.createFilter(
					StringBundler.concat(
						"(&(objectClass=",
						"com.liferay.portal.kernel.module.framework.",
						"ModuleServiceLifecycle)",
						"(module.service.lifecycle=portal.initialized))")),
				null) {

				@Override
				public Void addingService(
					ServiceReference<Object> serviceReference) {

					_portalStarted.set(true);

					close();

					return null;
				}

			};

		serviceTracker.open();
	}

	@Override
	public void bundleChanged(BundleEvent bundleEvent) {
		Bundle bundle = bundleEvent.getBundle();

		if (bundle.getSymbolicName() == null) {
			_log.error(bundle.getLocation() + " has a null symbolic name");
		}

		if (_portalStarted.get()) {
			if (_log.isInfoEnabled()) {
				if (bundleEvent.getType() == BundleEvent.STARTED) {
					_log.info("STARTED " + bundle);
				}
				else if (bundleEvent.getType() == BundleEvent.STOPPED) {
					_log.info("STOPPED " + bundle);
				}
			}
		}
		else if (_log.isDebugEnabled()) {
			if (bundleEvent.getType() == BundleEvent.STARTED) {
				_log.debug("STARTED " + bundle);
			}
			else if (bundleEvent.getType() == BundleEvent.STOPPED) {
				_log.debug("STOPPED " + bundle);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BundleStartStopLogger.class);

	private final AtomicBoolean _portalStarted = new AtomicBoolean();

}