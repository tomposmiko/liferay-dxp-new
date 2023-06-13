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

package com.liferay.segments.internal.field.customizer;

import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceComparator;
import com.liferay.osgi.service.tracker.collections.map.ServiceReferenceMapper;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.osgi.util.StringPlus;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.segments.field.customizer.SegmentsFieldCustomizer;
import com.liferay.segments.field.customizer.SegmentsFieldCustomizerRegistry;

import java.util.Collections;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Eduardo García
 */
@Component(service = SegmentsFieldCustomizerRegistry.class)
public class SegmentsFieldCustomizerRegistryImpl
	implements SegmentsFieldCustomizerRegistry {

	@Override
	public SegmentsFieldCustomizer getSegmentsFieldCustomizer(
		String entityName, String fieldName) {

		List<SegmentsFieldCustomizer> segmentsFieldCustomizers =
			_getSegmentsFieldCustomizers(entityName);

		if (ListUtil.isEmpty(segmentsFieldCustomizers)) {
			return null;
		}

		for (SegmentsFieldCustomizer segmentsFieldCustomizer :
				segmentsFieldCustomizers) {

			List<String> fieldNames = segmentsFieldCustomizer.getFieldNames();

			if (fieldNames.contains(fieldName)) {
				return segmentsFieldCustomizer;
			}
		}

		return null;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerMap = ServiceTrackerMapFactory.openMultiValueMap(
			bundleContext, SegmentsFieldCustomizer.class,
			"(segments.field.customizer.entity.name=*)",
			new FieldCustomizerServiceReferenceMapper(),
			Collections.reverseOrder(
				new PropertyServiceReferenceComparator<>(
					"segments.field.customizer.priority")));
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	private List<SegmentsFieldCustomizer> _getSegmentsFieldCustomizers(
		String name) {

		if (Validator.isNull(name)) {
			return null;
		}

		List<SegmentsFieldCustomizer> segmentsFieldCustomizers =
			_serviceTrackerMap.getService(name);

		if (segmentsFieldCustomizers == null) {
			return Collections.emptyList();
		}

		return segmentsFieldCustomizers;
	}

	private ServiceTrackerMap<String, List<SegmentsFieldCustomizer>>
		_serviceTrackerMap;

	private class FieldCustomizerServiceReferenceMapper
		implements ServiceReferenceMapper<String, SegmentsFieldCustomizer> {

		@Override
		public void map(
			ServiceReference<SegmentsFieldCustomizer> serviceReference,
			Emitter<String> emitter) {

			List<String> entityNames = StringPlus.asList(
				serviceReference.getProperty(
					"segments.field.customizer.entity.name"));

			for (String entityName : entityNames) {
				emitter.emit(entityName);
			}
		}

	}

}