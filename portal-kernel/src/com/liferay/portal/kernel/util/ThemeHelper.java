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

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateResourceLoaderUtil;

import java.util.Objects;

import javax.servlet.ServletContext;

/**
 * @author     Raymond Augé
 * @deprecated As of Judson (7.1.x), replaced by {@link
 *             com.liferay.portal.model.impl.ThemeImpl}
 */
@Deprecated
public class ThemeHelper {

	public static final String TEMPLATE_EXTENSION_FTL = "ftl";

	public static final String TEMPLATE_EXTENSION_JSP = "jsp";

	public static final String TEMPLATE_EXTENSION_VM = "vm";

	public static String getResourcePath(
		ServletContext servletContext, Theme theme, String portletId,
		String path) {

		StringBundler sb = new StringBundler(11);

		String themeContextName = GetterUtil.getString(
			theme.getServletContextName());

		sb.append(themeContextName);

		String servletContextName = StringPool.BLANK;

		if (!Objects.equals(
				PortalUtil.getPathContext(servletContext.getContextPath()),
				PortalUtil.getPathContext())) {

			servletContextName = GetterUtil.getString(
				servletContext.getServletContextName());
		}

		sb.append(theme.getFreeMarkerTemplateLoader());
		sb.append(theme.getTemplatesPath());

		if (Validator.isNotNull(servletContextName) &&
			!path.startsWith(StringPool.SLASH.concat(servletContextName))) {

			sb.append(StringPool.SLASH);
			sb.append(servletContextName);
		}

		sb.append(StringPool.SLASH);

		int start = 0;

		if (path.startsWith(StringPool.SLASH)) {
			start = 1;
		}

		int end = path.lastIndexOf(CharPool.PERIOD);

		sb.append(path.substring(start, end));

		sb.append(StringPool.PERIOD);

		if (Validator.isNotNull(portletId)) {
			sb.append(portletId);
			sb.append(StringPool.PERIOD);
		}

		sb.append(TEMPLATE_EXTENSION_FTL);

		return sb.toString();
	}

	public static boolean resourceExists(
			ServletContext servletContext, Theme theme, String portletId,
			String path)
		throws Exception {

		Boolean exists = null;

		if (Validator.isNotNull(portletId)) {
			exists = _resourceExists(servletContext, theme, portletId, path);

			if (!exists && PortletIdCodec.hasInstanceId(portletId)) {
				String rootPortletId = PortletIdCodec.decodePortletName(
					portletId);

				exists = _resourceExists(
					servletContext, theme, rootPortletId, path);
			}

			if (!exists) {
				exists = _resourceExists(servletContext, theme, null, path);
			}
		}

		if (exists == null) {
			exists = _resourceExists(servletContext, theme, portletId, path);
		}

		return exists;
	}

	private static boolean _resourceExists(
			ServletContext servletContext, Theme theme, String portletId,
			String path)
		throws Exception {

		if (Validator.isNull(path)) {
			return false;
		}

		return TemplateResourceLoaderUtil.hasTemplateResource(
			TemplateConstants.LANG_TYPE_FTL,
			getResourcePath(servletContext, theme, portletId, path));
	}

}