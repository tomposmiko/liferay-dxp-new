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

package com.liferay.analytics.dxp.entity.internal.retriever;

import com.liferay.analytics.dxp.entity.retriever.AnalyticsDXPEntityRetriever;
import com.liferay.analytics.dxp.entity.retriever.AnalyticsDXPEntityRetrieverTracker;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Marcos Martins
 */
@Component(immediate = true, service = AnalyticsDXPEntityRetrieverTracker.class)
public class AnalyticsDXPEntityRetrieverTrackerImpl
	implements AnalyticsDXPEntityRetrieverTracker {

	@Override
	public AnalyticsDXPEntityRetriever getAnalyticsDXPEntityRetriever(
		String className) {

		return _serviceTrackerMap.getService(className);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, AnalyticsDXPEntityRetriever.class,
			"analytics.dxp.entity.retriever.class.name");
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	private ServiceTrackerMap<String, AnalyticsDXPEntityRetriever>
		_serviceTrackerMap;

}