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

package com.liferay.marketplace.internal.upgrade.registry;

import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.kernel.upgrade.UpgradeProcessFactory;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Joan Kim
 * @author Ryan Park
 */
@Component(service = UpgradeStepRegistrator.class)
public class MarketplaceServiceUpgradeStepRegistrator
	implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"0.0.1", "1.0.0",
			new com.liferay.marketplace.internal.upgrade.v0_0_1.
				ExpandoUpgradeProcess(
					_companyLocalService, _expandoColumnLocalService,
					_expandoTableLocalService, _expandoValueLocalService));

		registry.register(
			"1.0.0", "1.0.1",
			UpgradeProcessFactory.addColumns(
				"Marketplace_App", "title VARCHAR(75)", "description STRING",
				"category VARCHAR(75)", "iconURL STRING",
				"version VARCHAR(75)"),
			new com.liferay.marketplace.internal.upgrade.v1_0_0.
				ModuleUpgradeProcess());

		registry.register("1.0.1", "1.0.2", new DummyUpgradeStep());

		registry.register(
			"1.0.2", "2.0.0",
			new com.liferay.marketplace.internal.upgrade.v2_0_0.
				AppUpgradeProcess());

		registry.register(
			"2.0.0", "2.0.1",
			new com.liferay.marketplace.internal.upgrade.v2_0_1.
				UpgradeCompanyId());

		registry.register(
			"2.0.1", "2.0.2",
			new com.liferay.marketplace.internal.upgrade.v2_0_2.
				AppUpgradeProcess());

		registry.register(
			"2.0.2", "2.0.3",
			UpgradeProcessFactory.alterColumnType(
				"Marketplace_App", "title", "VARCHAR(255) null"),
			UpgradeProcessFactory.alterColumnType(
				"Marketplace_App", "category", "VARCHAR(255) null"));
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ExpandoColumnLocalService _expandoColumnLocalService;

	@Reference
	private ExpandoTableLocalService _expandoTableLocalService;

	@Reference
	private ExpandoValueLocalService _expandoValueLocalService;

}