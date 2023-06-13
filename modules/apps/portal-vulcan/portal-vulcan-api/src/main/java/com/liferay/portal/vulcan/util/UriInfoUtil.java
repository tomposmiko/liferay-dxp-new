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

package com.liferay.portal.vulcan.util;

import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * @author Javier Gamarra
 */
public class UriInfoUtil {

	public static String getAbsolutePath(UriInfo uriInfo) {
		return String.valueOf(
			_updateUriBuilder(
				uriInfo.getAbsolutePathBuilder()
			).build());
	}

	public static String getBasePath(UriInfo uriInfo) {
		return String.valueOf(
			getBaseUriBuilder(
				uriInfo
			).build());
	}

	public static UriBuilder getBaseUriBuilder(UriInfo uriInfo) {
		return _updateUriBuilder(uriInfo.getBaseUriBuilder());
	}

	private static boolean _isHttpsEnabled() {
		if (Http.HTTPS.equals(
				PropsUtil.get(PropsKeys.PORTAL_INSTANCE_PROTOCOL)) ||
			Http.HTTPS.equals(PropsUtil.get(PropsKeys.WEB_SERVER_PROTOCOL))) {

			return true;
		}

		return false;
	}

	private static UriBuilder _updateUriBuilder(UriBuilder uriBuilder) {
		if (!Validator.isBlank(PortalUtil.getPathContext())) {
			URI uri = uriBuilder.build();

			uriBuilder.replacePath(PortalUtil.getPathContext(uri.getPath()));
		}

		if (_isHttpsEnabled()) {
			uriBuilder.scheme(Http.HTTPS);
		}

		return uriBuilder;
	}

}