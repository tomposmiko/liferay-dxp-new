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

package com.liferay.message.boards.web.internal.upgrade.registry;

import com.liferay.message.boards.constants.MBPortletKeys;
import com.liferay.message.boards.web.internal.upgrade.v1_0_0.UpgradePortletSettings;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.settings.SettingsLocatorHelper;
import com.liferay.portal.kernel.upgrade.BaseStagingGroupTypeSettingsUpgradeProcess;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(service = UpgradeStepRegistrator.class)
public class MBWebUpgradeStepRegistrator implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.registerInitialization();

		registry.register(
			"0.0.1", "1.0.0",
			new UpgradePortletSettings(_settingsLocatorHelper));

		registry.register(
			"1.0.0", "1.0.1",
			new BaseStagingGroupTypeSettingsUpgradeProcess(
				_companyLocalService, _groupLocalService,
				MBPortletKeys.MESSAGE_BOARDS,
				MBPortletKeys.MESSAGE_BOARDS_ADMIN));
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private SettingsLocatorHelper _settingsLocatorHelper;

}