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

package com.liferay.client.extension.web.internal.portlet;

import com.liferay.client.extension.type.CustomElementCET;
import com.liferay.frontend.js.loader.modules.extender.npm.NPMResolver;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Dictionary;
import java.util.Map;
import java.util.Properties;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Iván Zaera Avellón
 */
public class CustomElementCETPortlet extends BaseCETPortlet<CustomElementCET> {

	public CustomElementCETPortlet(
		CustomElementCET customElementCET, NPMResolver npmResolver,
		String portletId) {

		super(customElementCET, npmResolver);

		_portletId = portletId;
	}

	@Override
	public Dictionary<String, Object> getDictionary() {
		Dictionary<String, Object> dictionary =
			HashMapDictionaryBuilder.<String, Object>put(
				"com.liferay.portlet.company", cet.getCompanyId()
			).put(
				"com.liferay.portlet.css-class-wrapper",
				"portlet-client-extension"
			).put(
				"com.liferay.portlet.display-category",
				cet.getPortletCategoryName()
			).put(
				"com.liferay.portlet.instanceable", cet.isInstanceable()
			).put(
				"javax.portlet.display-name", cet.getName(LocaleUtil.US)
			).put(
				"javax.portlet.name", _portletId
			).put(
				"javax.portlet.security-role-ref", "power-user,user"
			).put(
				"javax.portlet.version", "3.0"
			).build();

		long lastModified = System.currentTimeMillis();

		String cssURLs = cet.getCSSURLs();

		if (Validator.isNotNull(cssURLs)) {
			dictionary.put(
				"com.liferay.portlet.header-portal-css",
				_prepareURLs(lastModified, cssURLs.split(StringPool.NEW_LINE)));
		}

		String urls = cet.getURLs();

		String[] urlsArray = urls.split(StringPool.NEW_LINE);

		if (cet.isUseESM()) {
			for (int i = 0; i < urlsArray.length; i++) {
				urlsArray[i] = "module:" + urlsArray[i];
			}
		}

		dictionary.put(
			"com.liferay.portlet.header-portal-javascript",
			_prepareURLs(lastModified, urlsArray));

		return dictionary;
	}

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException {

		PrintWriter printWriter = renderResponse.getWriter();

		printWriter.print(StringPool.LESS_THAN);
		printWriter.print(cet.getHTMLElementName());

		Properties properties = getProperties(renderRequest);

		try {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

			Group group = GroupLocalServiceUtil.getGroup(
				themeDisplay.getScopeGroupId());

			StringBundler webDavURLSB = new StringBundler(4);

			webDavURLSB.append(themeDisplay.getPortalURL());
			webDavURLSB.append("/webdav");
			webDavURLSB.append(group.getFriendlyURL());
			webDavURLSB.append("/document_library");

			properties.put("liferaywebdavurl", webDavURLSB.toString());
		}
		catch (PortalException portalException) {
			if (_log.isWarnEnabled()) {
				_log.warn(portalException);
			}
		}

		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			printWriter.print(StringPool.SPACE);
			printWriter.print(entry.getKey());
			printWriter.print("=\"");
			printWriter.print(
				StringUtil.replace(
					(String)entry.getValue(), CharPool.QUOTE, "&quot;"));
			printWriter.print(StringPool.QUOTE);
		}

		printWriter.print("></");
		printWriter.print(cet.getHTMLElementName());
		printWriter.print(StringPool.GREATER_THAN);

		printWriter.flush();
	}

	private String[] _prepareURLs(long lastModified, String[] urls) {
		for (int i = 0; i < urls.length; i++) {
			urls[i] = HttpComponentsUtil.addParameter(
				urls[i], "t", lastModified);
		}

		return urls;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CustomElementCETPortlet.class);

	private final String _portletId;

}