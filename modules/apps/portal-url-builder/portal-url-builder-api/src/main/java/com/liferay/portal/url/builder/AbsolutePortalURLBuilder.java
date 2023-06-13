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

package com.liferay.portal.url.builder;

import com.liferay.portal.kernel.model.portlet.PortletDependency;

import org.osgi.framework.Bundle;

/**
 * Provides a builder for constructing absolute URLs pointing to portal
 * resources.
 *
 * @author Iván Zaera Avellón
 * @see    BuildableAbsolutePortalURLBuilder
 */
public interface AbsolutePortalURLBuilder {

	/**
	 * Returns URLs for portal images. Image resources live in {@link
	 * com.liferay.portal.kernel.util.Portal#PATH_IMAGE}.
	 *
	 * @param  relativeURL the image's relative URL
	 * @return a builder that returns image URLs
	 */
	public ImageAbsolutePortalURLBuilder forImage(String relativeURL);

	/**
	 * Returns URLs for portal's main resources. Main resources live in {@link
	 * com.liferay.portal.kernel.util.Portal#PATH_MAIN}.
	 *
	 * @param  relativeURL the resource's relative URL
	 * @return a builder that returns main resource URLs
	 */
	public MainAbsolutePortalURLBuilder forMain(String relativeURL);

	/**
	 * Returns URLs for module resources. Module resources live in {@link
	 * com.liferay.portal.kernel.util.Portal#PATH_MODULE} + bundle's web context
	 * path.
	 *
	 * @param  bundle the bundle that contains the resource
	 * @param  relativeURL the resource's relative URL
	 * @return
	 * @review
	 */
	public ModuleAbsolutePortalURLBuilder forModule(
		Bundle bundle, String relativeURL);

	/**
	 * Returns URLs for portlet dependency resources. Portlet dependency
	 * resources live in the portal's root path.
	 *
	 * @param  portletDependency the portlet dependency resource
	 * @param  cssURN the URN for CSS portlet dependency resources
	 * @param  javaScriptURN the URN for JavaScript portlet dependency resources
	 * @return a builder that returns portlet dependency resource URLs
	 */
	public PortletDependencyAbsolutePortalURLBuilder forPortletDependency(
		PortletDependency portletDependency, String cssURN,
		String javaScriptURN);

	/**
	 * Returns URLs for arbitrary resources. Arbitrary resources live in the
	 * portal's root path (that can be "/" or "/something" if the portal has not
	 * been installed as the ROOT webapp).
	 *
	 * WARNING: Do not use this method unless none of the others serve your
	 * purpose. Otherwise you may end up hard coding configurable paths.
	 *
	 * @param  relativeURL the resource's relative URL
	 * @return a builder that returns arbitrary resource URLs
	 * @see    PortalContextLoaderListener.getPortalServletContextPath()
	 * @review
	 */
	public ResourceAbsolutePortalURLBuilder forResource(String relativeURL);

	/**
	 * Returns URLs for OSGi whiteboard servlet instances. The servlet class
	 * must be annotated with the OSGi @Component annotation for this method to
	 * work.
	 *
	 * OSGi whiteboard servlets live in
	 * {@link com.liferay.portal.kernel.util.Portal#PATH_MODULE}.
	 *
	 * @param  servletPattern the value of the
	 *         osgi.http.whiteboard.servlet.pattern property
	 * @return a builder that returns servlet URLs
	 * @review
	 */
	public WhiteboardAbsolutePortalURLBuilder forWhiteboard(
		String servletPattern);

	/**
	 * Returns absolute URLs without the CDN part.
	 *
	 * @return the same builder
	 * @see    com.liferay.portal.kernel.util.Portal#getCDNHost(
	 *         javax.servlet.http.HttpServletRequest)
	 */
	public AbsolutePortalURLBuilder ignoreCDNHost();

	/**
	 * Returns absolute URLs without the proxy part.
	 *
	 * @return the same builder
	 * @see    com.liferay.portal.kernel.util.Portal#getPathProxy()
	 */
	public AbsolutePortalURLBuilder ignorePathProxy();

}