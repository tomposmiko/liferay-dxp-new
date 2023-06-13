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

package com.liferay.fragment.internal.upgrade;

import com.liferay.fragment.internal.upgrade.v1_1_0.UpgradePortletPreferences;
import com.liferay.fragment.internal.upgrade.v2_0_0.util.FragmentCollectionTable;
import com.liferay.fragment.internal.upgrade.v2_0_0.util.FragmentEntryLinkTable;
import com.liferay.fragment.internal.upgrade.v2_0_0.util.FragmentEntryTable;
import com.liferay.fragment.internal.upgrade.v2_1_0.UpgradeSchema;
import com.liferay.fragment.internal.upgrade.v2_2_1.UpgradeFragmentEntry;
import com.liferay.fragment.internal.upgrade.v2_4_0.UpgradeFragmentEntryLink;
import com.liferay.fragment.internal.upgrade.v2_6_0.UpgradeFragmentEntryVersion;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.upgrade.BaseUpgradeSQLServerDatetime;
import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.kernel.upgrade.UpgradeCTModel;
import com.liferay.portal.kernel.upgrade.UpgradeMVCCVersion;
import com.liferay.portal.kernel.view.count.ViewCountManager;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portal.upgrade.step.util.UpgradeStepFactory;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author José Ángel Jiménez
 */
@Component(immediate = true, service = UpgradeStepRegistrator.class)
public class FragmentServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"1.0.0", "1.0.1",
			UpgradeStepFactory.alterColumnTypes(
				com.liferay.fragment.internal.upgrade.v1_0_1.util.
					FragmentEntryTable.class,
				"TEXT null", "css", "html", "js"),
			UpgradeStepFactory.alterColumnTypes(
				com.liferay.fragment.internal.upgrade.v1_0_1.util.
					FragmentEntryLinkTable.class,
				"TEXT null", "css", "html", "js", "editableValues"));

		registry.register("1.0.1", "1.0.2", new DummyUpgradeStep());

		registry.register(
			"1.0.2", "1.1.0",
			new UpgradePortletPreferences(_layoutLocalService));

		registry.register(
			"1.1.0", "2.0.0",
			new BaseUpgradeSQLServerDatetime(
				new Class<?>[] {
					FragmentCollectionTable.class, FragmentEntryLinkTable.class,
					FragmentEntryTable.class
				}));

		registry.register("2.0.0", "2.1.0", new UpgradeSchema());

		registry.register("2.1.0", "2.1.1", new DummyUpgradeStep());

		registry.register(
			"2.1.1", "2.1.2",
			new com.liferay.fragment.internal.upgrade.v2_1_2.UpgradeSchema());

		registry.register(
			"2.1.2", "2.1.3",
			new com.liferay.fragment.internal.upgrade.v2_1_3.UpgradeSchema());

		registry.register(
			"2.1.3", "2.2.0",
			new UpgradeMVCCVersion() {

				@Override
				protected String[] getModuleTableNames() {
					return new String[] {
						"FragmentCollection", "FragmentEntry",
						"FragmentEntryLink"
					};
				}

			});

		registry.register(
			"2.2.0", "2.2.1", new UpgradeFragmentEntry(),
			new com.liferay.fragment.internal.upgrade.v2_2_1.UpgradeSchema());

		registry.register(
			"2.2.1", "2.3.0",
			new com.liferay.fragment.internal.upgrade.v2_3_0.
				UpgradeFragmentEntry(),
			new com.liferay.fragment.internal.upgrade.v2_3_0.UpgradeSchema());

		registry.register("2.3.0", "2.4.0", new UpgradeFragmentEntryLink());

		registry.register(
			"2.4.0", "2.5.0",
			new com.liferay.fragment.internal.upgrade.v2_5_0.
				UpgradeFragmentEntryLink());

		registry.register(
			"2.5.0", "2.6.0",
			new com.liferay.fragment.internal.upgrade.v2_6_0.
				UpgradeFragmentEntry(),
			new UpgradeFragmentEntryVersion());

		registry.register(
			"2.6.0", "2.7.0",
			new UpgradeCTModel(
				"FragmentCollection", "FragmentComposition", "FragmentEntry",
				"FragmentEntryLink", "FragmentEntryVersion"),
			new UpgradeMVCCVersion() {

				@Override
				protected String[] getModuleTableNames() {
					return new String[] {"FragmentEntryVersion"};
				}

			});
	}

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private ViewCountManager _viewCountManager;

}