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

package com.liferay.commerce.theme.minium.internal.helper;

import com.liferay.application.list.PanelAppRegistry;
import com.liferay.application.list.PanelCategoryRegistry;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.application.list.display.context.logic.PanelCategoryHelper;
import com.liferay.commerce.account.util.CommerceAccountHelper;
import com.liferay.commerce.constants.CommercePortletKeys;
import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(service = CommerceThemeMiniumHttpHelper.class)
public class CommerceThemeMiniumHttpHelper {

	public String getAccountManagementPortletURL(
			HttpServletRequest httpServletRequest)
		throws PortalException {

		return _commerceAccountHelper.getAccountManagementPortletURL(
			httpServletRequest);
	}

	public String getMyListsLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return _language.get(resourceBundle, "my-lists");
	}

	public int getNotificationsCount(ThemeDisplay themeDisplay) {
		PanelCategoryHelper panelCategoryHelper = new PanelCategoryHelper(
			_panelAppRegistry, _panelCategoryRegistry);

		return panelCategoryHelper.getNotificationsCount(
			PanelCategoryKeys.USER_MY_ACCOUNT,
			themeDisplay.getPermissionChecker(), themeDisplay.getScopeGroup(),
			themeDisplay.getUser());
	}

	public String getNotificationsURL(HttpServletRequest httpServletRequest)
		throws PortalException {

		return String.valueOf(
			PortletProviderUtil.getPortletURL(
				httpServletRequest, UserNotificationEvent.class.getName(),
				PortletProvider.Action.VIEW));
	}

	public String getRedirectURL(HttpServletRequest httpServletRequest)
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		String portletURL = _getPortletURL(
			"/dashboard", httpServletRequest, themeDisplay,
			CommercePortletKeys.COMMERCE_DASHBOARD_FORECASTS_CHART);

		if (Validator.isBlank(portletURL)) {
			portletURL = _getPortletURL(
				"/catalog", httpServletRequest, themeDisplay,
				CPPortletKeys.CP_SEARCH_RESULTS);
		}

		List<Layout> layouts = themeDisplay.getLayouts();

		if (Validator.isBlank(portletURL) && ListUtil.isNotEmpty(layouts)) {
			return _portal.getLayoutURL(layouts.get(0), themeDisplay);
		}

		if (!Validator.isBlank(portletURL) &&
			portletURL.contains(StringPool.QUESTION)) {

			portletURL = portletURL.substring(
				0, portletURL.lastIndexOf(StringPool.QUESTION));
		}

		if (!Validator.isBlank(portletURL) &&
			Validator.isNotNull(themeDisplay.getDoAsUserId())) {

			portletURL = _portal.addPreservedParameters(
				themeDisplay, portletURL, false, true);
		}

		return portletURL;
	}

	private String _getPortletURL(
			String friendlyURL, HttpServletRequest httpServletRequest,
			ThemeDisplay themeDisplay, String portletId)
		throws PortalException {

		List<Layout> layouts = themeDisplay.getLayouts();

		if (ListUtil.isEmpty(layouts)) {
			return StringPool.BLANK;
		}

		for (Layout layout : layouts) {
			if (Objects.equals(friendlyURL, layout.getFriendlyURL())) {
				return _portal.getLayoutURL(layout, themeDisplay);
			}
		}

		long plid = _portal.getPlidFromPortletId(
			themeDisplay.getScopeGroupId(), portletId);

		if (plid == 0) {
			return StringPool.BLANK;
		}

		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			httpServletRequest, portletId, PortletProvider.Action.VIEW);

		if (portletURL == null) {
			return StringPool.BLANK;
		}

		return String.valueOf(portletURL);
	}

	@Reference
	private CommerceAccountHelper _commerceAccountHelper;

	@Reference
	private Language _language;

	@Reference
	private PanelAppRegistry _panelAppRegistry;

	@Reference
	private PanelCategoryRegistry _panelCategoryRegistry;

	@Reference
	private Portal _portal;

}