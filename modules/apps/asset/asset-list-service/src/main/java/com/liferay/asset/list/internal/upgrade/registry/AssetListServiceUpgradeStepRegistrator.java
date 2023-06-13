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

package com.liferay.asset.list.internal.upgrade.registry;

import com.liferay.asset.list.internal.upgrade.v1_3_0.AssetListEntryUpgradeProcess;
import com.liferay.asset.list.internal.upgrade.v1_4_0.AssetListEntryUsageUpgradeProcess;
import com.liferay.asset.list.internal.upgrade.v1_5_0.AssetListEntrySegmentsEntryRelUpgradeProcess;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.upgrade.CTModelUpgradeProcess;
import com.liferay.portal.kernel.upgrade.MVCCVersionUpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeProcessFactory;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Preston Crary
 */
@Component(service = UpgradeStepRegistrator.class)
public class AssetListServiceUpgradeStepRegistrator
	implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register(
			"1.0.0", "1.1.0",
			new MVCCVersionUpgradeProcess() {

				@Override
				protected String[] getTableNames() {
					return new String[] {
						"AssetListEntry", "AssetListEntryAssetEntryRel",
						"AssetListEntrySegmentsEntryRel", "AssetListEntryUsage"
					};
				}

			});

		registry.register(
			"1.1.0", "1.2.0",
			new CTModelUpgradeProcess(
				"AssetListEntry", "AssetListEntryAssetEntryRel",
				"AssetListEntrySegmentsEntryRel", "AssetListEntryUsage"));

		registry.register("1.2.0", "1.3.0", new AssetListEntryUpgradeProcess());

		registry.register(
			"1.3.0", "1.4.0", new AssetListEntryUsageUpgradeProcess());

		registry.register(
			"1.4.0", "1.5.0",
			new AssetListEntrySegmentsEntryRelUpgradeProcess());

		registry.register(
			"1.5.0", "1.6.0",
			UpgradeProcessFactory.alterColumnType(
				"AssetListEntrySegmentsEntryRel", "priority", "INTEGER"));

		registry.register(
			"1.6.0", "1.7.0",
			new com.liferay.asset.list.internal.upgrade.v1_7_0.
				AssetListEntrySegmentsEntryRelUpgradeProcess());

		registry.register(
			"1.7.0", "1.8.0",
			new com.liferay.asset.list.internal.upgrade.v1_8_0.
				AssetListEntrySegmentsEntryRelUpgradeProcess());

		registry.register(
			"1.8.0", "1.9.0",
			new com.liferay.asset.list.internal.upgrade.v1_9_0.
				AssetListEntryUsageUpgradeProcess(
					_layoutLocalService, _layoutPageTemplateEntryLocalService,
					_layoutPageTemplateStructureLocalService, _portal));
	}

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Reference
	private Portal _portal;

}