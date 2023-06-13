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

package com.liferay.portal.kernel.portlet.render;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Iván Zaera Avellón
 */
public class PortletRenderUtil {

	public static PortletRenderParts getPortletRenderParts(
		HttpServletRequest httpServletRequest, String portletHTML,
		Portlet portlet) {

		boolean portletOnLayout = false;

		if (portlet.isInstanceable()) {
			String rootPortletId = _getRootPortletId(portlet);
			String portletId = portlet.getPortletId();

			for (Portlet layoutPortlet : _getAllPortlets(httpServletRequest)) {

				// Check to see if an instance of this portlet is already in the
				// layout, but ignore the portlet that was just added

				String layoutPortletRootPortletId = _getRootPortletId(
					layoutPortlet);

				if (rootPortletId.equals(layoutPortletRootPortletId) &&
					!portletId.equals(layoutPortlet.getPortletId())) {

					portletOnLayout = true;

					break;
				}
			}
		}

		return _getPortletRenderParts(
			httpServletRequest, portletHTML, portlet, portletOnLayout);
	}

	public static void writeFooterCSSPaths(
		HttpServletRequest httpServletRequest, Collection<Portlet> portlets,
		Writer writer) {

		Collection<String> urls = _getURLs(
			httpServletRequest,
			Arrays.asList(_FOOTER_PORTAL_CSS, _FOOTER_PORTLET_CSS), portlets,
			URLType.CSS);

		for (String url : urls) {
			_writeCSSPath(new PrintWriter(writer, true), url, null);
		}
	}

	public static void writeFooterJavaScriptPaths(
		HttpServletRequest httpServletRequest, Collection<Portlet> portlets,
		Writer writer) {

		Collection<String> urls = _getURLs(
			httpServletRequest,
			Arrays.asList(_FOOTER_PORTAL_JS, _FOOTER_PORTLET_JS), portlets,
			URLType.JAVASCRIPT);

		for (String url : urls) {
			_writeJavaScriptPath(new PrintWriter(writer, true), url, null);
		}
	}

	public static void writeFooterPaths(
			HttpServletResponse httpServletResponse,
			PortletRenderParts portletRenderParts)
		throws IOException {

		_writePaths(
			httpServletResponse, portletRenderParts.getFooterCssPaths(),
			portletRenderParts.getFooterJavaScriptPaths());
	}

	public static void writeHeaderCSSPaths(
		HttpServletRequest httpServletRequest, Collection<Portlet> portlets,
		Writer writer) {

		Collection<String> urls = _getURLs(
			httpServletRequest,
			Arrays.asList(_HEADER_PORTAL_CSS, _HEADER_PORTLET_CSS), portlets,
			URLType.CSS);

		for (String url : urls) {
			_writeCSSPath(
				new PrintWriter(writer, true), url,
				HashMapBuilder.put(
					"data-senna-track", "temporary"
				).put(
					"id",
					HtmlUtil.escapeAttribute(
						StringUtil.toHexString(url.hashCode()))
				).build());
		}
	}

	public static void writeHeaderJavaScriptPaths(
		HttpServletRequest httpServletRequest, Collection<Portlet> portlets,
		Writer writer) {

		Collection<String> urls = _getURLs(
			httpServletRequest,
			Arrays.asList(_HEADER_PORTAL_JS, _HEADER_PORTLET_JS), portlets,
			URLType.JAVASCRIPT);

		for (String url : urls) {
			_writeJavaScriptPath(
				new PrintWriter(writer, true), url,
				Collections.singletonMap("data-senna-track", "temporary"));
		}
	}

	public static void writeHeaderPaths(
			HttpServletResponse httpServletResponse,
			PortletRenderParts portletRenderParts)
		throws IOException {

		_writePaths(
			httpServletResponse, portletRenderParts.getHeaderCssPaths(),
			portletRenderParts.getHeaderJavaScriptPaths());
	}

	private static List<Portlet> _getAllPortlets(
		HttpServletRequest httpServletRequest) {

		List<Portlet> allPortlets =
			(List<Portlet>)httpServletRequest.getAttribute(
				WebKeys.ALL_PORTLETS);

		if (ListUtil.isNotEmpty(allPortlets)) {
			return allPortlets;
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		LayoutTypePortlet layoutTypePortlet =
			themeDisplay.getLayoutTypePortlet();

		allPortlets = layoutTypePortlet.getAllPortlets();

		httpServletRequest.setAttribute(WebKeys.ALL_PORTLETS, allPortlets);

		return allPortlets;
	}

	private static Collection<String> _getComboServletURLs(
		Collection<PortletResourceAccessor> portletResourceAccessors,
		Collection<Portlet> portlets, Predicate<String> predicate,
		long timestamp, String urlPrefix, Set<String> visitedURLs) {

		if (predicate == null) {
			predicate = s -> true;
		}

		List<String> urls = new ArrayList<>();

		StringBundler comboServletSB = new StringBundler();

		for (Portlet portlet : portlets) {
			for (PortletResourceAccessor portletResourceAccessor :
					portletResourceAccessors) {

				String contextPath = null;

				if (portletResourceAccessor.isPortalResource()) {
					contextPath = PortalUtil.getPathContext();
				}
				else {
					contextPath =
						PortalUtil.getPathProxy() + portlet.getContextPath();
				}

				Collection<String> portletResources =
					portletResourceAccessor.get(portlet);

				for (String portletResource : portletResources) {
					if (!predicate.test(portletResource)) {
						continue;
					}

					boolean module = false;

					if (portletResource.startsWith("module:")) {
						module = true;

						portletResource = portletResource.substring(7);
					}

					boolean absolute = HttpComponentsUtil.hasProtocol(
						portletResource);

					if (!absolute) {
						portletResource = contextPath + portletResource;
					}

					if (module) {
						portletResource = "module:" + portletResource;
					}

					if (visitedURLs.contains(portletResource)) {
						continue;
					}

					visitedURLs.add(portletResource);

					if (absolute || module) {
						urls.add(portletResource);
					}
					else {
						comboServletSB.append(StringPool.AMPERSAND);

						if (!portletResourceAccessor.isPortalResource()) {
							comboServletSB.append(portlet.getPortletId());
							comboServletSB.append(StringPool.COLON);
						}

						comboServletSB.append(
							HtmlUtil.escapeURL(portletResource));

						timestamp = Math.max(timestamp, portlet.getTimestamp());
					}
				}
			}
		}

		if (comboServletSB.length() > 0) {
			String url = urlPrefix + comboServletSB;

			url = HttpComponentsUtil.addParameter(url, "t", timestamp);

			urls.add(url);
		}

		return urls;
	}

	private static PortletRenderParts _getPortletRenderParts(
		HttpServletRequest httpServletRequest, String portletHTML,
		Portlet portlet, boolean portletOnLayout) {

		Collection<String> footerCssPaths = Collections.emptyList();
		Collection<String> footerJavaScriptPaths = Collections.emptyList();
		Collection<String> headerCssPaths = Collections.emptyList();
		Collection<String> headerJavaScriptPaths = Collections.emptyList();

		if (!portletOnLayout && portlet.isAjaxable()) {
			footerCssPaths = _getURLs(
				httpServletRequest,
				Arrays.asList(_FOOTER_PORTLET_CSS, _FOOTER_PORTAL_CSS),
				Arrays.asList(portlet), URLType.CSS);
			footerJavaScriptPaths = _getURLs(
				httpServletRequest,
				Arrays.asList(_FOOTER_PORTLET_JS, _FOOTER_PORTAL_JS),
				Arrays.asList(portlet), URLType.JAVASCRIPT);
			headerCssPaths = _getURLs(
				httpServletRequest,
				Arrays.asList(_HEADER_PORTLET_CSS, _HEADER_PORTAL_CSS),
				Arrays.asList(portlet), URLType.CSS);
			headerJavaScriptPaths = _getURLs(
				httpServletRequest,
				Arrays.asList(_HEADER_PORTLET_JS, _HEADER_PORTAL_JS),
				Arrays.asList(portlet), URLType.JAVASCRIPT);
		}

		return new PortletRenderParts(
			footerCssPaths, footerJavaScriptPaths, headerCssPaths,
			headerJavaScriptPaths, portletHTML, !portlet.isAjaxable());
	}

	private static String _getRootPortletId(Portlet portlet) {

		// Workaround for portlet#getRootPortletId because that does not return
		// the proper root portlet ID for OpenSocial and WSRP portlets

		Portlet rootPortlet = portlet.getRootPortlet();

		return rootPortlet.getPortletId();
	}

	private static Collection<String> _getStaticURLs(
		HttpServletRequest httpServletRequest,
		Collection<PortletResourceAccessor> portletResourceAccessors,
		Collection<Portlet> portlets, Set<String> visitedURLs) {

		List<String> urls = new ArrayList<>();

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		for (Portlet portlet : portlets) {
			Portlet rootPortlet = portlet.getRootPortlet();

			for (PortletResourceAccessor portletResourceAccessor :
					portletResourceAccessors) {

				String contextPath = null;

				if (portletResourceAccessor.isPortalResource()) {
					contextPath = PortalUtil.getPathContext();
				}
				else {
					contextPath =
						PortalUtil.getPathProxy() + portlet.getContextPath();
				}

				Collection<String> portletResources =
					portletResourceAccessor.get(portlet);

				for (String portletResource : portletResources) {
					boolean module = false;

					if (portletResource.startsWith("module:")) {
						module = true;

						portletResource = portletResource.substring(7);
					}

					if (!HttpComponentsUtil.hasProtocol(portletResource)) {
						portletResource = PortalUtil.getStaticResourceURL(
							httpServletRequest, contextPath + portletResource,
							rootPortlet.getTimestamp());
					}

					if (!portletResource.contains(Http.PROTOCOL_DELIMITER)) {
						String cdnBaseURL = themeDisplay.getCDNBaseURL();

						portletResource = cdnBaseURL.concat(portletResource);
					}

					if (module) {
						portletResource = "module:" + portletResource;
					}

					if (visitedURLs.contains(portletResource)) {
						continue;
					}

					visitedURLs.add(portletResource);

					urls.add(portletResource);
				}
			}
		}

		return urls;
	}

	private static Collection<String> _getURLs(
		HttpServletRequest httpServletRequest,
		Collection<PortletResourceAccessor> portletResourceAccessors,
		Collection<Portlet> portlets, URLType urlType) {

		boolean fastLoad;
		Predicate<String> predicate = null;

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (urlType == URLType.CSS) {
			fastLoad = themeDisplay.isThemeCssFastLoad();
		}
		else if (urlType == URLType.JAVASCRIPT) {
			fastLoad = themeDisplay.isThemeJsFastLoad();

			predicate = resource -> !themeDisplay.isIncludedJs(resource);
		}
		else {
			throw new UnsupportedOperationException(
				"Unsupported URL type " + urlType);
		}

		Set<String> visitedURLs = (Set<String>)httpServletRequest.getAttribute(
			WebKeys.PORTLET_RESOURCE_STATIC_URLS);

		if (visitedURLs == null) {
			visitedURLs = new LinkedHashSet<>();

			httpServletRequest.setAttribute(
				WebKeys.PORTLET_RESOURCE_STATIC_URLS, visitedURLs);
		}

		if (fastLoad) {
			Theme theme = themeDisplay.getTheme();

			return _getComboServletURLs(
				portletResourceAccessors, portlets, predicate,
				theme.getTimestamp(),
				PortalUtil.getStaticResourceURL(
					httpServletRequest,
					themeDisplay.getCDNDynamicResourcesHost() +
						themeDisplay.getPathContext() + "/combo",
					"minifierType=&themeId=" + themeDisplay.getThemeId(), -1),
				visitedURLs);
		}

		return _getStaticURLs(
			httpServletRequest, portletResourceAccessors, portlets,
			visitedURLs);
	}

	private static void _writeCSSPath(
		PrintWriter printWriter, String cssPath,
		Map<String, String> attributes) {

		printWriter.print("<link href=\"");
		printWriter.print(HtmlUtil.escape(cssPath));
		printWriter.println("\" rel=\"stylesheet\" type=\"text/css\"");

		if (attributes != null) {
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				printWriter.print(StringPool.SPACE);
				printWriter.print(entry.getKey());
				printWriter.print("=\"");
				printWriter.print(HtmlUtil.escapeAttribute(entry.getValue()));
				printWriter.print(StringPool.QUOTE);
			}
		}

		printWriter.println(" />");
	}

	private static void _writeJavaScriptPath(
		PrintWriter printWriter, String javaScriptPath,
		Map<String, String> attributes) {

		String type = "text/javascript";

		if (javaScriptPath.startsWith("module:")) {
			javaScriptPath = javaScriptPath.substring(7);

			type = "module";
		}

		printWriter.print("<script src=\"");
		printWriter.print(HtmlUtil.escapeAttribute(javaScriptPath));
		printWriter.print("\" type=\"");
		printWriter.print(type);
		printWriter.print(StringPool.QUOTE);

		if (attributes != null) {
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				printWriter.print(StringPool.SPACE);
				printWriter.print(entry.getKey());
				printWriter.print("=\"");
				printWriter.print(HtmlUtil.escapeAttribute(entry.getValue()));
				printWriter.print(StringPool.QUOTE);
			}
		}

		printWriter.println("></script>");
	}

	private static void _writePaths(
			HttpServletResponse httpServletResponse,
			Collection<String> cssPaths, Collection<String> javaScriptPaths)
		throws IOException {

		if ((cssPaths == null) || (javaScriptPaths == null) ||
			(cssPaths.isEmpty() && javaScriptPaths.isEmpty())) {

			return;
		}

		PrintWriter printWriter = httpServletResponse.getWriter();

		for (String cssPath : cssPaths) {
			_writeCSSPath(printWriter, cssPath, null);
		}

		for (String javaScriptPath : javaScriptPaths) {
			_writeJavaScriptPath(printWriter, javaScriptPath, null);
		}
	}

	private static final PortletResourceAccessor _FOOTER_PORTAL_CSS =
		new PortletResourceAccessor(true) {

			@Override
			public Collection<String> get(Portlet portlet) {
				return portlet.getFooterPortalCss();
			}

		};

	private static final PortletResourceAccessor _FOOTER_PORTAL_JS =
		new PortletResourceAccessor(true) {

			@Override
			public Collection<String> get(Portlet portlet) {
				return portlet.getFooterPortalJavaScript();
			}

		};

	private static final PortletResourceAccessor _FOOTER_PORTLET_CSS =
		new PortletResourceAccessor(false) {

			@Override
			public Collection<String> get(Portlet portlet) {
				return portlet.getFooterPortletCss();
			}

		};

	private static final PortletResourceAccessor _FOOTER_PORTLET_JS =
		new PortletResourceAccessor(false) {

			@Override
			public Collection<String> get(Portlet portlet) {
				return portlet.getFooterPortletJavaScript();
			}

		};

	private static final PortletResourceAccessor _HEADER_PORTAL_CSS =
		new PortletResourceAccessor(true) {

			@Override
			public Collection<String> get(Portlet portlet) {
				return portlet.getHeaderPortalCss();
			}

		};

	private static final PortletResourceAccessor _HEADER_PORTAL_JS =
		new PortletResourceAccessor(true) {

			@Override
			public Collection<String> get(Portlet portlet) {
				return portlet.getHeaderPortalJavaScript();
			}

		};

	private static final PortletResourceAccessor _HEADER_PORTLET_CSS =
		new PortletResourceAccessor(false) {

			@Override
			public Collection<String> get(Portlet portlet) {
				return portlet.getHeaderPortletCss();
			}

		};

	private static final PortletResourceAccessor _HEADER_PORTLET_JS =
		new PortletResourceAccessor(false) {

			@Override
			public Collection<String> get(Portlet portlet) {
				return portlet.getHeaderPortletJavaScript();
			}

		};

	private enum URLType {

		CSS, JAVASCRIPT

	}

}