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

package com.liferay.fragment.internal.renderer;

import com.liferay.frontend.taglib.clay.servlet.taglib.IconTag;
import com.liferay.info.item.renderer.InfoItemRenderer;
import com.liferay.info.item.renderer.InfoItemRendererRegistry;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

/**
 * @author Eudaldo Alonso
 */
public class FragmentRendererUtil {

	public static List<InfoItemRenderer<?>> getInfoItemRenderers(
		String className, Class<?> clazz,
		InfoItemRendererRegistry infoItemRendererRegistry) {

		if (Validator.isNotNull(className)) {
			List<InfoItemRenderer<?>> infoItemRenderers =
				infoItemRendererRegistry.getInfoItemRenderers(className);

			if (!infoItemRenderers.isEmpty()) {
				return infoItemRenderers;
			}
		}

		Class<?>[] interfaces = clazz.getInterfaces();

		if (interfaces.length != 0) {
			for (Class<?> anInterface : interfaces) {
				List<InfoItemRenderer<?>> infoItemRenderers =
					infoItemRendererRegistry.getInfoItemRenderers(
						anInterface.getName());

				if (!infoItemRenderers.isEmpty()) {
					return infoItemRenderers;
				}
			}
		}

		Class<?> superClass = clazz.getSuperclass();

		if (superClass != null) {
			return getInfoItemRenderers(
				className, superClass, infoItemRendererRegistry);
		}

		return null;
	}

	public static boolean isEditMode(HttpServletRequest httpServletRequest) {
		HttpServletRequest originalHttpServletRequest =
			PortalUtil.getOriginalServletRequest(httpServletRequest);

		String layoutMode = ParamUtil.getString(
			originalHttpServletRequest, "p_l_mode", Constants.VIEW);

		if (layoutMode.equals(Constants.EDIT)) {
			return true;
		}

		return false;
	}

	public static void printPortletMessageInfo(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, String message) {

		try {
			PrintWriter printWriter = httpServletResponse.getWriter();

			StringBundler sb = new StringBundler(3);

			sb.append("<div class=\"portlet-msg-info\">");

			ThemeDisplay themeDisplay =
				(ThemeDisplay)httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
				"content.Language", themeDisplay.getLocale(),
				FragmentRendererUtil.class);

			sb.append(LanguageUtil.get(resourceBundle, message));

			sb.append("</div>");

			printWriter.write(sb.toString());
		}
		catch (IOException ioException) {
			if (_log.isDebugEnabled()) {
				_log.debug(ioException);
			}
		}
	}

	public static void printRestrictedContentMessage(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		try {
			PrintWriter printWriter = httpServletResponse.getWriter();

			printWriter.write(
				"<div class=\"alert alert-secondary align-items-baseline " +
					"bg-light d-flex\"><span class=\"alert-indicator " +
						"flex-shrink-0 mr-2\">");

			IconTag iconTag = new IconTag();

			iconTag.setCssClass("lexicon-icon lexicon-icon-password-policies");

			iconTag.setSymbol("password-policies");

			printWriter.write(
				iconTag.doTagAsString(httpServletRequest, httpServletResponse));

			printWriter.write("</span>");
			printWriter.write(
				LanguageUtil.get(
					httpServletRequest,
					"this-content-cannot-be-displayed-due-to-permission-" +
						"restrictions"));
			printWriter.write("</div>");
		}
		catch (IOException | JspException exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FragmentRendererUtil.class);

}