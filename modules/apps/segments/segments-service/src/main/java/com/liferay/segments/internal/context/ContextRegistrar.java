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

package com.liferay.segments.internal.context;

import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.segments.internal.odata.entity.ContextEntityModel;

import java.util.ArrayList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Eduardo García
 */
@Component(immediate = true, service = ContextRegistrar.class)
public class ContextRegistrar {

	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		register(new ContextEntityModel(new ArrayList<>()));
	}

	@Deactivate
	public void deactivate() {
		unregister();
	}

	public void register(ContextEntityModel contextEntityModel) {
		_serviceRegistration = _bundleContext.registerService(
			EntityModel.class, contextEntityModel,
			new HashMapDictionary<String, Object>() {
				{
					put("entity.model.name", ContextEntityModel.NAME);
				}
			});
	}

	public void unregister() {
		_serviceRegistration.unregister();
	}

	private BundleContext _bundleContext;
	private ServiceRegistration<EntityModel> _serviceRegistration;

}