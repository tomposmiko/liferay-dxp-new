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

package com.liferay.oauth2.provider.redirect;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Raymond Augé
 */
public class OAuth2RedirectURIInterpolator {

	public static final String TOKEN_PORT_WITH_COLON = "@port-with-colon@";

	public static final String TOKEN_PROTOCOL = "@protocol@";

	public static List<String> interpolateRedirectURIsList(
		HttpServletRequest httpServletRequest, List<String> redirectURIsList,
		Portal portal) {

		List<String> interpolattedRedirectURIsList = new ArrayList<>();

		String protocol = Http.HTTP;

		boolean secure = portal.isSecure(httpServletRequest);

		if (secure) {
			protocol = Http.HTTPS;
		}

		String portWithColon =
			":" + portal.getForwardedPort(httpServletRequest);

		if (!secure && Objects.equals(":80", portWithColon)) {
			portWithColon = StringPool.BLANK;
		}

		if (secure && Objects.equals(":443", portWithColon)) {
			portWithColon = StringPool.BLANK;
		}

		for (String redirectURI : redirectURIsList) {
			interpolattedRedirectURIsList.add(
				StringUtil.replace(
					redirectURI, _TOKENS,
					new String[] {portWithColon, protocol}));
		}

		return interpolattedRedirectURIsList;
	}

	private static final String[] _TOKENS = {
		TOKEN_PORT_WITH_COLON, TOKEN_PROTOCOL
	};

}