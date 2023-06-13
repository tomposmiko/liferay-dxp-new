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

package com.liferay.commerce.tax.web.internal.display.context;

import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.tax.CommerceTaxEngine;
import com.liferay.commerce.tax.model.CommerceTaxMethod;
import com.liferay.commerce.tax.service.CommerceTaxMethodService;
import com.liferay.commerce.util.CommerceTaxEngineRegistry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Locale;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Alessio Antonio Rendina
 */
public class CommerceTaxMethodsDisplayContext {

	public CommerceTaxMethodsDisplayContext(
		CommerceChannelLocalService commerceChannelLocalService,
		ModelResourcePermission<CommerceChannel>
			commerceChannelModelResourcePermission,
		CommerceTaxEngineRegistry commerceTaxEngineRegistry,
		CommerceTaxMethodService commerceTaxMethodService,
		RenderRequest renderRequest, RenderResponse renderResponse) {

		_commerceChannelLocalService = commerceChannelLocalService;
		_commerceChannelModelResourcePermission =
			commerceChannelModelResourcePermission;
		_commerceTaxEngineRegistry = commerceTaxEngineRegistry;
		_commerceTaxMethodService = commerceTaxMethodService;
		_renderRequest = renderRequest;
		_renderResponse = renderResponse;
	}

	public long getCommerceChannelId() throws PortalException {
		if (_commerceTaxMethod != null) {
			CommerceChannel commerceChannel =
				_commerceChannelLocalService.getCommerceChannelByGroupId(
					_commerceTaxMethod.getGroupId());

			return commerceChannel.getCommerceChannelId();
		}

		return ParamUtil.getLong(_renderRequest, "commerceChannelId");
	}

	public CommerceTaxMethod getCommerceTaxMethod() throws PortalException {
		if (_commerceTaxMethod != null) {
			return _commerceTaxMethod;
		}

		long commerceTaxMethodId = ParamUtil.getLong(
			_renderRequest, "commerceTaxMethodId");

		if (commerceTaxMethodId != 0) {
			return _commerceTaxMethodService.getCommerceTaxMethod(
				commerceTaxMethodId);
		}

		CommerceChannel commerceChannel =
			_commerceChannelLocalService.getCommerceChannel(
				getCommerceChannelId());

		_commerceTaxMethod = _commerceTaxMethodService.fetchCommerceTaxMethod(
			commerceChannel.getGroupId(), getCommerceTaxMethodEngineKey());

		return _commerceTaxMethod;
	}

	public String getCommerceTaxMethodEngineDescription(Locale locale) {
		CommerceTaxEngine commerceTaxEngine =
			_commerceTaxEngineRegistry.getCommerceTaxEngine(
				getCommerceTaxMethodEngineKey());

		return commerceTaxEngine.getDescription(locale);
	}

	public String getCommerceTaxMethodEngineKey() {
		if (_commerceTaxMethod != null) {
			return _commerceTaxMethod.getEngineKey();
		}

		return ParamUtil.getString(
			_renderRequest, "commerceTaxMethodEngineKey");
	}

	public String getCommerceTaxMethodEngineName(Locale locale) {
		CommerceTaxEngine commerceTaxEngine =
			_commerceTaxEngineRegistry.getCommerceTaxEngine(
				getCommerceTaxMethodEngineKey());

		return commerceTaxEngine.getName(locale);
	}

	public PortletURL getPortletURL() {
		PortletURL portletURL = _renderResponse.createRenderURL();

		String delta = ParamUtil.getString(_renderRequest, "delta");

		if (Validator.isNotNull(delta)) {
			portletURL.setParameter("delta", delta);
		}

		portletURL.setParameter("navigation", _getNavigation());

		String screenNavigationEntryKey = getScreenNavigationEntryKey();

		if (Validator.isNotNull(screenNavigationEntryKey)) {
			portletURL.setParameter(
				"screenNavigationEntryKey", screenNavigationEntryKey);
		}

		return portletURL;
	}

	public String getScreenNavigationEntryKey() {
		return ParamUtil.getString(_renderRequest, "screenNavigationEntryKey");
	}

	public boolean hasUpdateCommerceChannelPermission() throws PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay)_renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return _commerceChannelModelResourcePermission.contains(
			themeDisplay.getPermissionChecker(),
			_commerceChannelLocalService.getCommerceChannel(
				getCommerceChannelId()),
			ActionKeys.UPDATE);
	}

	private String _getNavigation() {
		return ParamUtil.getString(_renderRequest, "navigation");
	}

	private final CommerceChannelLocalService _commerceChannelLocalService;
	private final ModelResourcePermission<CommerceChannel>
		_commerceChannelModelResourcePermission;
	private final CommerceTaxEngineRegistry _commerceTaxEngineRegistry;
	private CommerceTaxMethod _commerceTaxMethod;
	private final CommerceTaxMethodService _commerceTaxMethodService;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;

}