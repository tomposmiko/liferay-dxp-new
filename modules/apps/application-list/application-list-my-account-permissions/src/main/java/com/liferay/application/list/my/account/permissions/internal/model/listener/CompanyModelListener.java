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

package com.liferay.application.list.my.account.permissions.internal.model.listener;

import com.liferay.application.list.PanelApp;
import com.liferay.application.list.PanelAppRegistry;
import com.liferay.application.list.PanelCategoryRegistry;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.application.list.display.context.logic.PanelCategoryHelper;
import com.liferay.application.list.my.account.permissions.internal.PanelAppMyAccountPermissions;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Drew Brokke
 */
@Component(service = ModelListener.class)
public class CompanyModelListener extends BaseModelListener<Company> {

	@Override
	public void onAfterCreate(Company company) {
		TransactionCommitCallbackUtil.registerCallback(
			() -> {
				PanelCategoryHelper panelCategoryHelper =
					new PanelCategoryHelper(
						_panelAppRegistry, _panelCategoryRegistry);

				List<PanelApp> panelApps = panelCategoryHelper.getAllPanelApps(
					PanelCategoryKeys.USER_MY_ACCOUNT);

				List<Portlet> portlets = new ArrayList<>(panelApps.size());

				for (PanelApp panelApp : panelApps) {
					Portlet portlet = _portletLocalService.getPortletById(
						panelApp.getPortletId());

					portlets.add(portlet);
				}

				_panelAppMyAccountPermissions.initPermissions(
					company.getCompanyId(), portlets);

				return null;
			});
	}

	@Reference
	private PanelAppMyAccountPermissions _panelAppMyAccountPermissions;

	@Reference
	private PanelAppRegistry _panelAppRegistry;

	@Reference
	private PanelCategoryRegistry _panelCategoryRegistry;

	@Reference
	private PortletLocalService _portletLocalService;

}