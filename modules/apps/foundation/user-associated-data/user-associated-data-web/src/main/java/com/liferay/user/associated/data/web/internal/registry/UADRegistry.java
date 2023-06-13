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

package com.liferay.user.associated.data.web.internal.registry;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.user.associated.data.anonymizer.UADAnonymizer;
import com.liferay.user.associated.data.display.UADDisplay;
import com.liferay.user.associated.data.exporter.UADExporter;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
* @author William Newbury
*/
@Component(immediate = true, service = UADRegistry.class)
public class UADRegistry {

	public UADAnonymizer getUADAnonymizer(String key) {
		return _uadAnonymizerServiceTrackerMap.getService(key);
	}

	public Set<String> getUADAnonymizerKeySet() {
		return _uadAnonymizerServiceTrackerMap.keySet();
	}

	public Collection<UADAnonymizer> getUADAnonymizers() {
		return _uadAnonymizerServiceTrackerMap.values();
	}

	public UADDisplay getUADDisplay(String key) {
		return _uadDisplayServiceTrackerMap.getService(key);
	}

	public Set<String> getUADDisplayKeySet() {
		return _uadDisplayServiceTrackerMap.keySet();
	}

	public Collection<UADDisplay> getUADDisplays() {
		return _uadDisplayServiceTrackerMap.values();
	}

	public Stream<UADDisplay> getUADDisplayStream() {
		return getUADDisplays().stream();
	}

	public UADExporter getUADExporter(String key) {
		return _uadExporterServiceTrackerMap.getService(key);
	}

	public Set<String> getUADExporterKeySet() {
		return _uadExporterServiceTrackerMap.keySet();
	}

	public Collection<UADExporter> getUADExporters() {
		return _uadExporterServiceTrackerMap.values();
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_uadAnonymizerServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, UADAnonymizer.class, "model.class.name");
		_uadDisplayServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, UADDisplay.class, "model.class.name");
		_uadExporterServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, UADExporter.class, "model.class.name");
	}

	@Deactivate
	protected void deactivate() {
		_uadAnonymizerServiceTrackerMap.close();
		_uadDisplayServiceTrackerMap.close();
		_uadExporterServiceTrackerMap.close();
	}

	private ServiceTrackerMap<String, UADAnonymizer>
		_uadAnonymizerServiceTrackerMap;
	private ServiceTrackerMap<String, UADDisplay> _uadDisplayServiceTrackerMap;
	private ServiceTrackerMap<String, UADExporter>
		_uadExporterServiceTrackerMap;

}