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

package com.liferay.asset.publisher.web.internal.display.context;

import com.liferay.asset.publisher.util.AssetPublisherHelper;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public abstract class BaseItemSelectorViewDisplayContext
	implements ItemSelectorViewDisplayContext {

	public BaseItemSelectorViewDisplayContext(
		HttpServletRequest httpServletRequest,
		AssetPublisherHelper assetPublisherHelper, PortletURL portletURL) {

		_assetPublisherHelper = assetPublisherHelper;

		this.httpServletRequest = httpServletRequest;
		this.portletURL = portletURL;
	}

	public long getGroupId() {
		if (_groupId != null) {
			return _groupId;
		}

		_groupId = ParamUtil.getLong(httpServletRequest, "groupId");

		return _groupId;
	}

	@Override
	public long[] getSelectedGroupIds() {
		long plid = ParamUtil.getLong(httpServletRequest, "plid");

		Layout layout = LayoutLocalServiceUtil.fetchLayout(plid);

		if (layout == null) {
			return new long[0];
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		String portletResource = ParamUtil.getString(
			httpServletRequest, "portletResource");

		PortletPreferences portletPreferences =
			themeDisplay.getStrictLayoutPortletSetup(layout, portletResource);

		return _assetPublisherHelper.getGroupIds(
			portletPreferences, themeDisplay.getScopeGroupId(),
			themeDisplay.getLayout());
	}

	@Override
	public boolean isShowSearch() {
		return true;
	}

	protected PortletRequest getPortletRequest() {
		return (PortletRequest)httpServletRequest.getAttribute(
			JavaConstants.JAVAX_PORTLET_REQUEST);
	}

	protected final HttpServletRequest httpServletRequest;
	protected final PortletURL portletURL;

	private final AssetPublisherHelper _assetPublisherHelper;
	private Long _groupId;

}