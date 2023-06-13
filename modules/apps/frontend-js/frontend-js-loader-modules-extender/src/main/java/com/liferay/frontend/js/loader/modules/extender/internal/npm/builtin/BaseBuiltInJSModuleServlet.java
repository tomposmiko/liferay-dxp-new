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

package com.liferay.frontend.js.loader.modules.extender.internal.npm.builtin;

import com.liferay.frontend.js.loader.modules.extender.npm.JSBundle;
import com.liferay.frontend.js.loader.modules.extender.npm.JSPackage;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MimeTypes;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.net.URL;

import java.util.Locale;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * Provides a base abstract class to implement servlets that return JavaScript
 * modules tracked by the {@link
 * com.liferay.frontend.js.loader.modules.extender.npm.NPMRegistry}.
 *
 * @author Adolfo Pérez
 */
public abstract class BaseBuiltInJSModuleServlet extends HttpServlet {

	@Override
	public void destroy() {
		_bundleSymbolicNameServiceTrackerMap.close();
	}

	@Override
	public void init() {
		Bundle bundle = FrameworkUtil.getBundle(getClass());

		_bundleSymbolicNameServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundle.getBundleContext(), ResourceBundleLoader.class,
				"bundle.symbolic.name");
	}

	protected abstract MimeTypes getMimeTypes();

	/**
	 * Returns the requested resource descriptor. This is a template method that
	 * must be implemented by subclasses to lookup the requested resource.
	 *
	 * @param  pathInfo the request's pathInfo
	 * @return the {@link String} content of the resource or null
	 */
	protected abstract ResourceDescriptor getResourceDescriptor(
		String pathInfo);

	@Override
	protected void service(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		String pathInfo = request.getPathInfo();

		ResourceDescriptor resourceDescriptor = getResourceDescriptor(pathInfo);

		if (resourceDescriptor == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);

			return;
		}

		_setContentType(response, pathInfo);

		String languageId = request.getParameter("languageId");

		Locale locale = LocaleUtil.fromLanguageId(languageId);

		_sendResource(response, resourceDescriptor, locale);
	}

	private void _sendResource(
			HttpServletResponse response, ResourceDescriptor resourceDescriptor,
			Locale locale)
		throws IOException {

		JSPackage jsPackage = resourceDescriptor.getJsPackage();

		URL url = jsPackage.getResourceURL(resourceDescriptor.getPackagePath());

		if (url == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);

			return;
		}

		try (InputStream inputStream = url.openStream()) {
			String content = StringUtil.read(inputStream);

			response.setCharacterEncoding(StringPool.UTF8);

			PrintWriter printWriter = response.getWriter();

			JSBundle jsBundle = jsPackage.getJSBundle();

			ResourceBundleLoader resourceBundleLoader =
				_bundleSymbolicNameServiceTrackerMap.getService(
					jsBundle.getName());

			if (resourceBundleLoader != null) {
				content = LanguageUtil.process(
					() -> resourceBundleLoader.loadResourceBundle(locale),
					locale, content);
			}

			printWriter.print(content);
		}
		catch (IOException ioe) {
			_log.error("Unable to read " + resourceDescriptor.toString(), ioe);

			response.sendError(
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				"Unable to read file");
		}
	}

	private void _setContentType(
		HttpServletResponse response, String pathInfo) {

		String extension = FileUtil.getExtension(pathInfo);

		if (extension.equals(".js")) {
			response.setContentType(ContentTypes.TEXT_JAVASCRIPT_UTF8);
		}
		else if (extension.equals(".map")) {
			response.setContentType(ContentTypes.APPLICATION_JSON);
		}
		else {
			MimeTypes mimeTypes = getMimeTypes();

			response.setContentType(mimeTypes.getContentType(pathInfo));
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseBuiltInJSModuleServlet.class);

	private ServiceTrackerMap<String, ResourceBundleLoader>
		_bundleSymbolicNameServiceTrackerMap;

}