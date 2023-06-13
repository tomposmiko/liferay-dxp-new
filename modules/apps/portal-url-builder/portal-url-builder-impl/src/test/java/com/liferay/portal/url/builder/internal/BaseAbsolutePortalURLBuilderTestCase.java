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

package com.liferay.portal.url.builder.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.servlet.BrowserSniffer;
import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.url.builder.facet.CacheAwareAbsolutePortalURLBuilder.CachePolicy;
import com.liferay.portal.url.builder.internal.util.CacheHelper;

import java.util.Dictionary;

import javax.servlet.http.HttpServletRequest;

import org.mockito.Mockito;

import org.osgi.framework.Bundle;

/**
 * @author Iván Zaera Avellón
 */
public abstract class BaseAbsolutePortalURLBuilderTestCase {

	protected Bundle mockBundle() {
		Bundle bundle = Mockito.mock(Bundle.class);

		Dictionary<String, String> headers = HashMapDictionaryBuilder.put(
			"Web-ContextPath", "/wcp"
		).build();

		Mockito.when(
			bundle.getHeaders(StringPool.BLANK)
		).thenReturn(
			headers
		);

		return bundle;
	}

	protected CacheHelper mockCacheHelper() {
		CacheHelper cacheHelper = Mockito.mock(CacheHelper.class);

		Mockito.doAnswer(
			invocationOnMock -> {
				StringBundler sb = invocationOnMock.getArgumentAt(
					0, StringBundler.class);

				sb.append("mac=aG9saQ==");

				return null;
			}
		).when(
			cacheHelper
		).appendCacheParam(
			Mockito.any(StringBundler.class), Mockito.any(Bundle.class),
			Mockito.any(CachePolicy.class), Mockito.anyString()
		);

		return cacheHelper;
	}

	protected HttpServletRequest mockHttpServletRequest() {
		HttpServletRequest httpServletRequest = Mockito.mock(
			HttpServletRequest.class);

		ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);

		Mockito.when(
			themeDisplay.getLanguageId()
		).thenReturn(
			"es"
		);

		Theme theme = Mockito.mock(Theme.class);

		Mockito.when(
			theme.getThemeId()
		).thenReturn(
			"atheme"
		);

		Mockito.when(
			themeDisplay.getTheme()
		).thenReturn(
			theme
		);

		Mockito.when(
			httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY)
		).thenReturn(
			themeDisplay
		);

		return httpServletRequest;
	}

	protected Portal mockPortal(boolean context, boolean proxy, boolean cdnHost)
		throws PortalException {

		Portal portal = Mockito.mock(Portal.class);

		String pathProxy = proxy ? "/proxy" : StringPool.BLANK;

		Mockito.when(
			portal.getPathProxy()
		).thenReturn(
			pathProxy
		);

		Mockito.when(
			portal.getPathContext()
		).thenReturn(
			pathProxy + (context ? "/context" : StringPool.BLANK)
		);

		Mockito.when(
			portal.getCDNHost(Mockito.any(HttpServletRequest.class))
		).thenReturn(
			cdnHost ? "http://cdn-host" : StringPool.BLANK
		);

		Mockito.when(
			portal.isCDNDynamicResourcesEnabled(
				Mockito.any(HttpServletRequest.class))
		).thenReturn(
			cdnHost ? true : false
		);

		Mockito.doAnswer(
			invocationOnMock -> {
				String uri = invocationOnMock.getArgumentAt(1, String.class);
				String queryString = invocationOnMock.getArgumentAt(
					2, String.class);
				Long timestamp = invocationOnMock.getArgumentAt(3, Long.class);

				return StringBundler.concat(
					uri, StringPool.QUESTION, queryString, "&t=", timestamp);
			}
		).when(
			portal
		).getStaticResourceURL(
			Mockito.any(HttpServletRequest.class), Mockito.anyString(),
			Mockito.anyString(), Mockito.anyLong()
		);

		return portal;
	}

	protected void setUp() throws Exception {
		BrowserSnifferUtil browserSnifferUtil = new BrowserSnifferUtil();

		browserSnifferUtil.setBrowserSniffer(_mockBrowserSniffer());
	}

	private BrowserSniffer _mockBrowserSniffer() {
		BrowserSniffer browserSniffer = Mockito.mock(BrowserSniffer.class);

		Mockito.when(
			browserSniffer.getBrowserId(Mockito.any(HttpServletRequest.class))
		).thenReturn(
			BrowserSniffer.BROWSER_ID_FIREFOX
		);

		return browserSniffer;
	}

}