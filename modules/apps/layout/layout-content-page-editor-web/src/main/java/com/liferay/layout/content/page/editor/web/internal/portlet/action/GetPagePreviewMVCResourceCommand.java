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

package com.liferay.layout.content.page.editor.web.internal.portlet.action;

import com.liferay.info.constants.InfoDisplayWebKeys;
import com.liferay.info.item.ClassPKInfoItemIdentifier;
import com.liferay.info.item.InfoItemReference;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.item.provider.InfoItemDetailsProvider;
import com.liferay.info.item.provider.InfoItemObjectProvider;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.layout.display.page.LayoutDisplayPageObjectProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProviderRegistry;
import com.liferay.layout.display.page.constants.LayoutDisplayPageWebKeys;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.theme.ThemeUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.segments.constants.SegmentsWebKeys;
import com.liferay.segments.service.SegmentsExperienceLocalService;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	property = {
		"javax.portlet.name=" + ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
		"mvc.command.name=/layout_content_page_editor/get_page_preview"
	},
	service = MVCResourceCommand.class
)
public class GetPagePreviewMVCResourceCommand extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay currentThemeDisplay =
			(ThemeDisplay)resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

		ThemeDisplay themeDisplay = (ThemeDisplay)currentThemeDisplay.clone();

		long selPlid = ParamUtil.getLong(resourceRequest, "selPlid");

		if (selPlid > 0) {
			Layout layout = _layoutLocalService.fetchLayout(selPlid);

			themeDisplay.setLayout(layout);

			LayoutSet layoutSet = layout.getLayoutSet();

			themeDisplay.setLayoutSet(layoutSet);
			themeDisplay.setLookAndFeel(
				layoutSet.getTheme(), layoutSet.getColorScheme());

			themeDisplay.setPlid(layout.getPlid());
		}

		if (!LayoutPermissionUtil.containsLayoutUpdatePermission(
				PermissionCheckerFactoryUtil.create(themeDisplay.getRealUser()),
				themeDisplay.getLayout())) {

			resourceResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);

			return;
		}

		long[] currentSegmentsExperienceIds = GetterUtil.getLongValues(
			resourceRequest.getAttribute(
				SegmentsWebKeys.SEGMENTS_EXPERIENCE_IDS));
		boolean currentPortletDecorate = GetterUtil.getBoolean(
			resourceRequest.getAttribute(WebKeys.PORTLET_DECORATE));

		try {
			long segmentsExperienceId = ParamUtil.getLong(
				resourceRequest, "segmentsExperienceId",
				_segmentsExperienceLocalService.
					fetchDefaultSegmentsExperienceId(selPlid));

			resourceRequest.setAttribute(
				SegmentsWebKeys.SEGMENTS_EXPERIENCE_IDS,
				new long[] {segmentsExperienceId});

			resourceRequest.setAttribute(
				WebKeys.PORTLET_DECORATE, Boolean.FALSE);

			String languageId = ParamUtil.getString(
				resourceRequest, "languageId",
				LocaleUtil.toLanguageId(themeDisplay.getLocale()));

			themeDisplay.setLocale(LocaleUtil.fromLanguageId(languageId));

			themeDisplay.setSignedIn(false);

			User defaultUser = _userLocalService.getDefaultUser(
				themeDisplay.getCompanyId());

			themeDisplay.setUser(defaultUser);

			Layout layout = themeDisplay.getLayout();

			layout.setClassNameId(0);

			String className = ParamUtil.getString(
				resourceRequest, "className");
			long classPK = ParamUtil.getLong(resourceRequest, "classPK");

			if (layout.isTypeAssetDisplay() &&
				(Validator.isNull(className) || (classPK <= 0))) {

				layout.setType(LayoutConstants.TYPE_CONTENT);
			}

			HttpServletRequest httpServletRequest =
				_portal.getHttpServletRequest(resourceRequest);

			httpServletRequest.setAttribute(
				WebKeys.THEME_DISPLAY, themeDisplay);

			if (Validator.isNotNull(className) && (classPK > 0)) {
				_includeInfoItemObjects(className, classPK, httpServletRequest);
			}

			HttpServletResponse httpServletResponse =
				_portal.getHttpServletResponse(resourceResponse);

			layout.includeLayoutContent(
				httpServletRequest, httpServletResponse);

			ServletContext servletContext = ServletContextPool.get(
				StringPool.BLANK);
			LayoutSet layoutSet = themeDisplay.getLayoutSet();

			Document document = Jsoup.parse(
				ThemeUtil.include(
					servletContext, httpServletRequest, httpServletResponse,
					"portal_normal.ftl", layoutSet.getTheme(), false));

			Element contentElement = document.getElementById("content");

			StringBundler sb = (StringBundler)httpServletRequest.getAttribute(
				WebKeys.LAYOUT_CONTENT);

			contentElement.html(sb.toString());

			ServletResponseUtil.write(httpServletResponse, document.toString());
		}
		finally {
			resourceRequest.setAttribute(
				SegmentsWebKeys.SEGMENTS_EXPERIENCE_IDS,
				currentSegmentsExperienceIds);
			resourceRequest.setAttribute(
				WebKeys.PORTLET_DECORATE, currentPortletDecorate);
			resourceRequest.setAttribute(
				WebKeys.THEME_DISPLAY, currentThemeDisplay);
		}
	}

	private void _includeInfoItemObjects(
			String className, long classPK,
			HttpServletRequest httpServletRequest)
		throws Exception {

		InfoItemObjectProvider<?> infoItemObjectProvider =
			_infoItemServiceRegistry.getFirstInfoItemService(
				InfoItemObjectProvider.class, className);

		ClassPKInfoItemIdentifier classPKInfoItemIdentifier =
			new ClassPKInfoItemIdentifier(classPK);

		String version = ParamUtil.getString(httpServletRequest, "version");

		if (Validator.isNotNull(version)) {
			classPKInfoItemIdentifier.setVersion(version);
		}

		Object infoItem = infoItemObjectProvider.getInfoItem(
			classPKInfoItemIdentifier);

		httpServletRequest.setAttribute(InfoDisplayWebKeys.INFO_ITEM, infoItem);

		InfoItemDetailsProvider infoItemDetailsProvider =
			_infoItemServiceRegistry.getFirstInfoItemService(
				InfoItemDetailsProvider.class, className);

		httpServletRequest.setAttribute(
			InfoDisplayWebKeys.INFO_ITEM_DETAILS,
			infoItemDetailsProvider.getInfoItemDetails(infoItem));

		LayoutDisplayPageProvider<?> layoutDisplayPageProvider =
			_layoutDisplayPageProviderRegistry.
				getLayoutDisplayPageProviderByClassName(className);

		httpServletRequest.setAttribute(
			LayoutDisplayPageWebKeys.LAYOUT_DISPLAY_PAGE_PROVIDER,
			layoutDisplayPageProvider);

		LayoutDisplayPageObjectProvider<?> layoutDisplayPageObjectProvider =
			layoutDisplayPageProvider.getLayoutDisplayPageObjectProvider(
				new InfoItemReference(className, classPK));

		if (layoutDisplayPageObjectProvider != null) {
			httpServletRequest.setAttribute(
				LayoutDisplayPageWebKeys.LAYOUT_DISPLAY_PAGE_OBJECT_PROVIDER,
				layoutDisplayPageObjectProvider);
		}
	}

	@Reference
	private InfoItemServiceRegistry _infoItemServiceRegistry;

	@Reference
	private LayoutDisplayPageProviderRegistry
		_layoutDisplayPageProviderRegistry;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

	@Reference
	private UserLocalService _userLocalService;

}