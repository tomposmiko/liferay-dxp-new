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

package com.liferay.mobile.device.rules.internal.upgrade;

import com.liferay.mobile.device.rules.internal.upgrade.v2_0_0.util.MDRActionTable;
import com.liferay.mobile.device.rules.internal.upgrade.v2_0_0.util.MDRRuleGroupInstanceTable;
import com.liferay.mobile.device.rules.internal.upgrade.v2_0_0.util.MDRRuleGroupTable;
import com.liferay.mobile.device.rules.internal.upgrade.v2_0_0.util.MDRRuleTable;
import com.liferay.portal.kernel.upgrade.BaseUpgradeSQLServerDatetime;
import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.kernel.upgrade.UpgradeMVCCVersion;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Matthew Tambara
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class MDRServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register("0.0.1", "1.0.0", new DummyUpgradeStep());

		registry.register("1.0.0", "1.0.1", new DummyUpgradeStep());

		registry.register(
			"1.0.1", "2.0.0",
			new BaseUpgradeSQLServerDatetime(
				new Class<?>[] {
					MDRActionTable.class, MDRRuleGroupInstanceTable.class,
					MDRRuleGroupTable.class, MDRRuleTable.class
				}));

		registry.register(
			"2.0.0", "2.1.0",
			new UpgradeMVCCVersion() {

				@Override
				protected String[] getModuleTableNames() {
					return new String[] {
						"MDRAction", "MDRRule", "MDRRuleGroup",
						"MDRRuleGroupInstance"
					};
				}

			});
	}

}