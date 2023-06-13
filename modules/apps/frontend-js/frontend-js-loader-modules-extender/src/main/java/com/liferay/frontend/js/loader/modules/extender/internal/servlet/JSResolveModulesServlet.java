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

package com.liferay.frontend.js.loader.modules.extender.internal.servlet;

import com.liferay.frontend.js.loader.modules.extender.internal.configuration.Details;
import com.liferay.frontend.js.loader.modules.extender.internal.npm.NPMRegistryResolutionStateDigest;
import com.liferay.frontend.js.loader.modules.extender.internal.resolution.BrowserModulesResolution;
import com.liferay.frontend.js.loader.modules.extender.internal.resolution.BrowserModulesResolver;
import com.liferay.frontend.js.loader.modules.extender.npm.NPMRegistry;
import com.liferay.frontend.js.loader.modules.extender.npm.NPMRegistryUpdatesListener;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.url.builder.AbsolutePortalURLBuilder;
import com.liferay.portal.url.builder.AbsolutePortalURLBuilderFactory;

import java.io.IOException;
import java.io.PrintWriter;

import java.net.URLDecoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Rodolfo Roza Miranda
 */
@Component(
	configurationPid = "com.liferay.frontend.js.loader.modules.extender.internal.configuration.Details",
	property = {
		"osgi.http.whiteboard.servlet.name=com.liferay.frontend.js.loader.modules.extender.internal.servlet.JSResolveModulesServlet",
		"osgi.http.whiteboard.servlet.pattern=/js_resolve_modules/*",
		"service.ranking:Integer=" + Details.MAX_VALUE_LESS_1K
	},
	service = {
		JSResolveModulesServlet.class, NPMRegistryUpdatesListener.class,
		Servlet.class
	}
)
public class JSResolveModulesServlet
	extends HttpServlet implements NPMRegistryUpdatesListener {

	public String getURL() {
		State state = _state.get();

		return state.url;
	}

	@Override
	public void onAfterUpdate() {
		_updateState(_npmRegistry.get());
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_serviceTracker = ServiceTrackerFactory.open(
			bundleContext, NPMRegistry.class,
			new ServiceTrackerCustomizer<NPMRegistry, NPMRegistry>() {

				@Override
				public NPMRegistry addingService(
					ServiceReference<NPMRegistry> serviceReference) {

					NPMRegistry npmRegistry = bundleContext.getService(
						serviceReference);

					_npmRegistry.set(npmRegistry);

					_updateState(npmRegistry);

					return npmRegistry;
				}

				@Override
				public void modifiedService(
					ServiceReference<NPMRegistry> serviceReference,
					NPMRegistry npmRegistry) {
				}

				@Override
				public void removedService(
					ServiceReference<NPMRegistry> serviceReference,
					NPMRegistry npmRegistry) {

					_npmRegistry.set(null);

					_updateState(null);
				}

			});
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();

		_serviceTracker = null;
	}

	@Override
	protected void service(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		State state = _state.get();

		if (state == null) {
			httpServletResponse.sendError(
				HttpServletResponse.SC_SERVICE_UNAVAILABLE);

			return;
		}

		if (!state.expectedPathInfo.equals(httpServletRequest.getPathInfo())) {
			AbsolutePortalURLBuilder absolutePortalURLBuilder =
				_absolutePortalURLBuilderFactory.getAbsolutePortalURLBuilder(
					httpServletRequest);

			String url = absolutePortalURLBuilder.forServlet(
				getURL()
			).build();

			// Send a redirect so that the AMD loader knows that it must update
			// its resolvePath to the new URL

			httpServletResponse.sendRedirect(url);

			return;
		}

		// See https://ashton.codes/set-cache-control-max-age-1-year

		httpServletResponse.addHeader(
			HttpHeaders.CACHE_CONTROL, "public, max-age=31536000, immutable");
		httpServletResponse.setCharacterEncoding(StringPool.UTF8);
		httpServletResponse.setContentType(ContentTypes.APPLICATION_JSON);

		PrintWriter printWriter = new PrintWriter(
			httpServletResponse.getOutputStream(), true);

		BrowserModulesResolution browserModulesResolution =
			_browserModulesResolver.resolve(
				_getModuleNames(httpServletRequest), httpServletRequest);

		printWriter.write(browserModulesResolution.toJSON());

		printWriter.close();
	}

	private List<String> _getModuleNames(HttpServletRequest httpServletRequest)
		throws IOException {

		String[] moduleNames = null;

		String method = httpServletRequest.getMethod();

		if (method.equals("GET")) {
			moduleNames = ParamUtil.getStringValues(
				httpServletRequest, "modules");
		}
		else {
			String body = StringUtil.read(httpServletRequest.getInputStream());

			body = URLDecoder.decode(
				body, httpServletRequest.getCharacterEncoding());

			if (!body.isEmpty()) {
				body = body.substring(8);

				moduleNames = body.split(StringPool.COMMA);
			}
		}

		if (moduleNames != null) {
			return Arrays.asList(moduleNames);
		}

		return Collections.emptyList();
	}

	private void _updateState(NPMRegistry npmRegistry) {
		if (npmRegistry == null) {
			_state.set(null);

			return;
		}

		NPMRegistryResolutionStateDigest npmRegistryResolutionStateDigest =
			new NPMRegistryResolutionStateDigest(npmRegistry);

		_state.set(new State(npmRegistryResolutionStateDigest.getDigest()));
	}

	@Reference
	private AbsolutePortalURLBuilderFactory _absolutePortalURLBuilderFactory;

	@Reference
	private BrowserModulesResolver _browserModulesResolver;

	private final AtomicReference<NPMRegistry> _npmRegistry =
		new AtomicReference<>();
	private ServiceTracker<NPMRegistry, NPMRegistry> _serviceTracker;
	private final AtomicReference<State> _state = new AtomicReference<>();

	private static class State {

		public State(String digest) {
			expectedPathInfo = StringPool.SLASH + digest;
			url = "/js_resolve_modules/" + digest;
		}

		public final String expectedPathInfo;
		public final String url;

	}

}