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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.cookies.CookiesManagerUtil;
import com.liferay.portal.kernel.cookies.UnsupportedCookieException;
import com.liferay.portal.kernel.exception.CookieNotSupportedException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Brian Wing Shun Chan
 * @author Minhchau Dang
 * @deprecated As of Cavanaugh (7.4.x), replace by {@link CookiesManagerUtil}
 */
@Deprecated
public class CookieKeys {

	public static final String COMMERCE_CONTINUE_AS_GUEST =
		"COMMERCE_CONTINUE_AS_GUEST";

	public static final String COMPANY_ID = "COMPANY_ID";

	public static final String COOKIE_SUPPORT = "COOKIE_SUPPORT";

	public static final String GUEST_LANGUAGE_ID = "GUEST_LANGUAGE_ID";

	public static final String ID = "ID";

	public static final String JSESSIONID = "JSESSIONID";

	public static final String LOGIN = "LOGIN";

	public static final int MAX_AGE = (int)(Time.YEAR / 1000);

	public static final String PASSWORD = "PASSWORD";

	public static final String REMEMBER_ME = "REMEMBER_ME";

	public static final String REMOTE_PREFERENCE_PREFIX = "REMOTE_PREFERENCE_";

	public static final String USER_UUID = "USER_UUID";

	public static void addCookie(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, Cookie cookie) {

		CookiesManagerUtil.addCookie(
			cookie, httpServletRequest, httpServletResponse);
	}

	public static void addCookie(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, Cookie cookie,
		boolean secure) {

		CookiesManagerUtil.addCookie(
			cookie, httpServletRequest, httpServletResponse, secure);
	}

	public static void addSupportCookie(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		CookiesManagerUtil.addSupportCookie(
			httpServletRequest, httpServletResponse);
	}

	public static void deleteCookies(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, String domain,
		String... cookieNames) {

		CookiesManagerUtil.deleteCookies(
			domain, httpServletRequest, httpServletResponse, cookieNames);
	}

	public static String getCookie(
		HttpServletRequest httpServletRequest, String name) {

		return CookiesManagerUtil.getCookieValue(name, httpServletRequest);
	}

	public static String getCookie(
		HttpServletRequest httpServletRequest, String name,
		boolean toUpperCase) {

		return CookiesManagerUtil.getCookieValue(
			name, httpServletRequest, toUpperCase);
	}

	public static String getDomain(HttpServletRequest httpServletRequest) {
		return CookiesManagerUtil.getDomain(httpServletRequest);
	}

	public static String getDomain(String host) {
		return CookiesManagerUtil.getDomain(host);
	}

	public static boolean hasSessionId(HttpServletRequest httpServletRequest) {
		return CookiesManagerUtil.hasSessionId(httpServletRequest);
	}

	public static boolean isEncodedCookie(String name) {
		return CookiesManagerUtil.isEncodedCookie(name);
	}

	public static void validateSupportCookie(
			HttpServletRequest httpServletRequest)
		throws CookieNotSupportedException {

		try {
			CookiesManagerUtil.validateSupportCookie(httpServletRequest);
		}
		catch (UnsupportedCookieException unsupportedCookieException) {
			throw new CookieNotSupportedException(unsupportedCookieException);
		}
	}

}