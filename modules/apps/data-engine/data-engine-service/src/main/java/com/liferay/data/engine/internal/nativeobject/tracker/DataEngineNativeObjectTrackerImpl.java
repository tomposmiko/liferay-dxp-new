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

package com.liferay.data.engine.internal.nativeobject.tracker;

import com.liferay.data.engine.nativeobject.DataEngineNativeObject;
import com.liferay.data.engine.nativeobject.tracker.DataEngineNativeObjectTracker;
import com.liferay.osgi.service.tracker.collections.map.ServiceReferenceMapperFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Collection;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Jeyvison Nascimento
 */
@Component(service = DataEngineNativeObjectTracker.class)
public class DataEngineNativeObjectTrackerImpl
	implements DataEngineNativeObjectTracker {

	@Override
	public DataEngineNativeObject getDataEngineNativeObject(String className) {
		return _serviceTrackerMap.getService(StringUtil.toUpperCase(className));
	}

	@Override
	public Collection<DataEngineNativeObject> getDataEngineNativeObjects() {
		return _serviceTrackerMap.values();
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, DataEngineNativeObject.class, null,
			ServiceReferenceMapperFactory.create(
				bundleContext,
				(dataEngineNativeObject, emitter) -> emitter.emit(
					StringUtil.toUpperCase(
						dataEngineNativeObject.getClassName()))));
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	private ServiceTrackerMap<String, DataEngineNativeObject>
		_serviceTrackerMap;

}