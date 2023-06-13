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

package com.liferay.portal.servlet;

import com.liferay.petra.string.CharPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portal.kernel.servlet.DirectRequestDispatcherFactory;
import com.liferay.portal.kernel.servlet.DirectServletRegistryUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Raymond Augé
 * @author Shuyang Zhou
 */
@DoPrivileged
public class DirectRequestDispatcherFactoryImpl
	implements DirectRequestDispatcherFactory {

	@Override
	public RequestDispatcher getRequestDispatcher(
		ServletContext servletContext, String path) {

		RequestDispatcher requestDispatcher = doGetRequestDispatcher(
			servletContext, path);

		return new ClassLoaderRequestDispatcherWrapper(
			servletContext, requestDispatcher);
	}

	@Override
	public RequestDispatcher getRequestDispatcher(
		ServletRequest servletRequest, String path) {

		ServletContext servletContext =
			(ServletContext)servletRequest.getAttribute(WebKeys.CTX);

		if (servletContext == null) {
			return servletRequest.getRequestDispatcher(path);
		}

		return getRequestDispatcher(servletContext, path);
	}

	public interface PACL {

		public RequestDispatcher getRequestDispatcher(
			ServletContext servletContext, RequestDispatcher requestDispatcher);

	}

	protected RequestDispatcher doGetRequestDispatcher(
		ServletContext servletContext, String path) {

		if (!PropsValues.DIRECT_SERVLET_CONTEXT_ENABLED) {
			return new IndirectRequestDispatcher(
				servletContext.getRequestDispatcher(path));
		}

		if ((path == null) || (path.length() == 0)) {
			return null;
		}

		if (path.charAt(0) != CharPool.SLASH) {
			throw new IllegalArgumentException(
				"Path " + path + " is not relative to context root");
		}

		String contextPath = servletContext.getContextPath();

		String fullPath = contextPath.concat(path);

		String queryString = null;

		int pos = fullPath.indexOf(CharPool.QUESTION);

		if (pos != -1) {
			queryString = fullPath.substring(pos + 1);

			fullPath = fullPath.substring(0, pos);
		}

		Servlet servlet = DirectServletRegistryUtil.getServlet(fullPath);

		RequestDispatcher requestDispatcher = null;

		if (servlet == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("No servlet found for " + fullPath);
			}

			requestDispatcher = servletContext.getRequestDispatcher(path);

			requestDispatcher = new DirectServletPathRegisterDispatcher(
				path, requestDispatcher);
		}
		else {
			if (_log.isDebugEnabled()) {
				_log.debug("Servlet found for " + fullPath);
			}

			requestDispatcher = new DirectRequestDispatcher(
				servlet, path, queryString);
		}

		return _pacl.getRequestDispatcher(servletContext, requestDispatcher);
	}

	private static final String _EQUINOX_REQUEST_CLASS_NAME =
		"org.eclipse.equinox.http.servlet.internal.servlet." +
			"HttpServletRequestWrapperImpl";

	private static final Log _log = LogFactoryUtil.getLog(
		DirectRequestDispatcherFactoryImpl.class);

	private static final PACL _pacl = new NoPACL();

	/**
	 * See LPS-79937. We need to protect against redispatch from the module
	 * framework back to the portal, which means we have to unwrap the request.
	 */
	private static class IndirectRequestDispatcher
		implements RequestDispatcher {

		public IndirectRequestDispatcher(RequestDispatcher requestDispatcher) {
			_requestDispatcher = requestDispatcher;
		}

		@Override
		public void forward(ServletRequest request, ServletResponse response)
			throws IOException, ServletException {

			Class<?> clazz = request.getClass();

			if (_EQUINOX_REQUEST_CLASS_NAME.equals(clazz.getName())) {
				HttpServletRequestWrapper wrapper =
					(HttpServletRequestWrapper)request;

				request = wrapper.getRequest();
			}

			_requestDispatcher.forward(request, response);
		}

		@Override
		public void include(ServletRequest request, ServletResponse response)
			throws IOException, ServletException {

			Class<?> clazz = request.getClass();

			if (_EQUINOX_REQUEST_CLASS_NAME.equals(clazz.getName())) {
				HttpServletRequestWrapper wrapper =
					(HttpServletRequestWrapper)request;

				request = wrapper.getRequest();
			}

			_requestDispatcher.include(request, response);
		}

		private final RequestDispatcher _requestDispatcher;

	}

	private static class NoPACL implements PACL {

		@Override
		public RequestDispatcher getRequestDispatcher(
			ServletContext servletContext,
			RequestDispatcher requestDispatcher) {

			return requestDispatcher;
		}

	}

}