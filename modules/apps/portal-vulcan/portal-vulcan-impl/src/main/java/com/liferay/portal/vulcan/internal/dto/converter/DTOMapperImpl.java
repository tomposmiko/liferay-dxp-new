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

package com.liferay.portal.vulcan.internal.dto.converter;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Javier de Arcos
 */
@Component(service = DTOMapper.class)
public class DTOMapperImpl implements DTOMapper {

	@Override
	public String toInternalDTOClassName(String externalDTOClassName) {
		return _serviceTrackerMap.getService(externalDTOClassName);
	}

	@Activate
	protected void activate(BundleContext bundleContext) throws Exception {
		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext,
			(Class<DTOConverter<?, ?>>)(Class<?>)DTOConverter.class, null,
			(serviceReference, emitter) -> {
				String externalDTOClassName =
					(String)serviceReference.getProperty(
						"external.dto.class.name");

				if (externalDTOClassName == null) {
					DTOConverter<?, ?> dtoConverter = bundleContext.getService(
						serviceReference);

					externalDTOClassName = _getDTOConverterGenericType(
						dtoConverter, 1);

					bundleContext.ungetService(serviceReference);
				}

				if (externalDTOClassName != null) {
					emitter.emit(externalDTOClassName);
				}
			},
			new ServiceTrackerCustomizer<DTOConverter<?, ?>, String>() {

				@Override
				public String addingService(
					ServiceReference<DTOConverter<?, ?>> serviceReference) {

					String internalDTOClassName =
						(String)serviceReference.getProperty("dto.class.name");

					if (internalDTOClassName == null) {
						DTOConverter<?, ?> dtoConverter =
							bundleContext.getService(serviceReference);

						internalDTOClassName = _getDTOConverterGenericType(
							dtoConverter, 0);

						bundleContext.ungetService(serviceReference);
					}

					return internalDTOClassName;
				}

				@Override
				public void modifiedService(
					ServiceReference<DTOConverter<?, ?>> serviceReference,
					String internalDTOClassName) {
				}

				@Override
				public void removedService(
					ServiceReference<DTOConverter<?, ?>> serviceReference,
					String internalDTOClassName) {
				}

			});
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
	}

	private String _getDTOConverterGenericType(
		DTOConverter<?, ?> dtoConverter, int genericTypeIndex) {

		Class<?> dtoConverterClass = dtoConverter.getClass();

		Type[] genericInterfaceTypes = dtoConverterClass.getGenericInterfaces();

		for (Type genericInterfaceType : genericInterfaceTypes) {
			if (genericInterfaceType instanceof ParameterizedType) {
				ParameterizedType parameterizedType =
					(ParameterizedType)genericInterfaceType;

				if (parameterizedType.getRawType() != DTOConverter.class) {
					continue;
				}

				Type[] genericTypes =
					parameterizedType.getActualTypeArguments();

				Class<DTOConverter<?, ?>> resourceGenericType =
					(Class<DTOConverter<?, ?>>)genericTypes[genericTypeIndex];

				return resourceGenericType.getName();
			}
		}

		return null;
	}

	private ServiceTrackerMap<String, String> _serviceTrackerMap;

}