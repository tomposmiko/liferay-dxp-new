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

package com.liferay.portal.company.log.web.internal.application.list;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.company.log.web.internal.constants.PortalCompanyLogPortletKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Keven Leone
 */
@Component(
	property = {
		"panel.app.order:Integer=700",
		"panel.category.key=" + PanelCategoryKeys.CONTROL_PANEL_SECURITY
	},
	service = PanelApp.class
)
public class PortalCompanyLogPanelApp extends BasePanelApp {

	@Override
	public Portlet getPortlet() {
		return _portlet;
	}

	@Override
	public String getPortletId() {
		return PortalCompanyLogPortletKeys.PORTAL_COMPANY_LOG;
	}

	@Override
	public boolean isShow(PermissionChecker permissionChecker, Group group)
		throws PortalException {

		if (!GetterUtil.getBoolean(
				PropsUtil.get(PropsKeys.COMPANY_LOG_ENABLED)) ||
			!FeatureFlagManagerUtil.isEnabled("LPS-170670")) {

			return false;
		}

		return super.isShow(permissionChecker, group);
	}

	@Reference(
		target = "(javax.portlet.name=" + PortalCompanyLogPortletKeys.PORTAL_COMPANY_LOG + ")"
	)
	private Portlet _portlet;

}