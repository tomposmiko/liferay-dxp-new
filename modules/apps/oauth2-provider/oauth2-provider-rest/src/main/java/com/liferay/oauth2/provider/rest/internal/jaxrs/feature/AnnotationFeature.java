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

package com.liferay.oauth2.provider.rest.internal.jaxrs.feature;

import com.liferay.oauth2.provider.rest.internal.scope.logic.ScopeLogic;
import com.liferay.oauth2.provider.rest.spi.scope.checker.container.request.filter.BaseScopeCheckerContainerRequestFilter;
import com.liferay.oauth2.provider.scope.ScopeChecker;
import com.liferay.oauth2.provider.scope.spi.scope.finder.ScopeFinder;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(|(oauth2.scope.checker.type=annotations)(oauth2.scopechecker.type=annotations))",
		"osgi.jaxrs.extension=true",
		"osgi.jaxrs.extension.select=(osgi.jaxrs.name=Liferay.OAuth2)",
		"osgi.jaxrs.name=Liferay.OAuth2.annotations.feature"
	},
	scope = ServiceScope.PROTOTYPE, service = Feature.class
)
@Provider
public class AnnotationFeature implements Feature {

	@Override
	public boolean configure(FeatureContext featureContext1) {
		Set<String> scopes = new HashSet<>();

		featureContext1.register(
			(DynamicFeature)(resourceInfo, featureContext2) -> scopes.addAll(
				RequiresScopeAnnotationFinder.find(
					resourceInfo.getResourceClass())));

		featureContext1.register(
			new AnnotationContainerScopeCheckerContainerRequestFilter(),
			Priorities.AUTHORIZATION - 8);

		Configuration configuration = featureContext1.getConfiguration();

		Map<String, Object> applicationProperties =
			(Map<String, Object>)configuration.getProperty(
				"osgi.jaxrs.application.serviceProperties");

		_propertyAccessorFunction = applicationProperties::get;
		_serviceRegistration = _bundleContext.registerService(
			ScopeFinder.class, new CollectionScopeFinder(scopes),
			new Hashtable<>(applicationProperties));

		return true;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	private BundleContext _bundleContext;
	private Function<String, Object> _propertyAccessorFunction;

	@Reference
	private ScopeChecker _scopeChecker;

	@Reference(target = "(oauth2.scope.checker.type=annotations)")
	private ScopeLogic _scopeLogic;

	private ServiceRegistration<ScopeFinder> _serviceRegistration;

	private class AnnotationContainerScopeCheckerContainerRequestFilter
		extends BaseScopeCheckerContainerRequestFilter {

		public boolean isContainerRequestContextAllowed(
			ContainerRequestContext containerRequestContext) {

			return _scopeLogic.check(
				_propertyAccessorFunction, _resourceInfo.getResourceClass(),
				_resourceInfo.getResourceMethod(), _scopeChecker);
		}

		@Context
		private ResourceInfo _resourceInfo;

	}

}