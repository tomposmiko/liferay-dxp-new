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

package com.liferay.message.boards.internal.upgrade.registry;

import com.liferay.message.boards.internal.upgrade.v1_0_0.UpgradeClassNames;
import com.liferay.message.boards.internal.upgrade.v1_0_1.UpgradeUnsupportedGuestPermissions;
import com.liferay.message.boards.internal.upgrade.v1_1_0.MBThreadUpgradeProcess;
import com.liferay.message.boards.internal.upgrade.v2_0_0.util.MBBanTable;
import com.liferay.message.boards.internal.upgrade.v2_0_0.util.MBCategoryTable;
import com.liferay.message.boards.internal.upgrade.v2_0_0.util.MBDiscussionTable;
import com.liferay.message.boards.internal.upgrade.v2_0_0.util.MBMailingListTable;
import com.liferay.message.boards.internal.upgrade.v2_0_0.util.MBMessageTable;
import com.liferay.message.boards.internal.upgrade.v2_0_0.util.MBStatsUserTable;
import com.liferay.message.boards.internal.upgrade.v2_0_0.util.MBThreadFlagTable;
import com.liferay.message.boards.internal.upgrade.v2_0_0.util.MBThreadTable;
import com.liferay.message.boards.internal.upgrade.v3_0_0.MBMessageTreePathUpgradeProcess;
import com.liferay.message.boards.internal.upgrade.v3_1_0.UrlSubjectUpgradeProcess;
import com.liferay.message.boards.internal.upgrade.v6_0_0.MBStatsUserUpgradeProcess;
import com.liferay.message.boards.internal.upgrade.v6_3_0.util.MBSuspiciousActivityTable;
import com.liferay.message.boards.model.MBThread;
import com.liferay.portal.kernel.service.ResourceActionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.upgrade.BaseExternalReferenceCodeUpgradeProcess;
import com.liferay.portal.kernel.upgrade.BaseSQLServerDatetimeUpgradeProcess;
import com.liferay.portal.kernel.upgrade.CTModelUpgradeProcess;
import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.kernel.upgrade.MVCCVersionUpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeProcessFactory;
import com.liferay.portal.kernel.upgrade.ViewCountUpgradeProcess;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.view.count.service.ViewCountEntryLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(service = UpgradeStepRegistrator.class)
public class MBServiceUpgradeStepRegistrator implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register("0.0.1", "1.0.0", new UpgradeClassNames());

		registry.register(
			"1.0.0", "1.0.1",
			new UpgradeUnsupportedGuestPermissions(
				_resourceActionLocalService, _resourcePermissionLocalService,
				_roleLocalService));

		registry.register("1.0.1", "1.1.0", new MBThreadUpgradeProcess());

		registry.register(
			"1.1.0", "2.0.0",
			new BaseSQLServerDatetimeUpgradeProcess(
				new Class<?>[] {
					MBBanTable.class, MBCategoryTable.class,
					MBDiscussionTable.class, MBMailingListTable.class,
					MBMessageTable.class, MBStatsUserTable.class,
					MBThreadFlagTable.class, MBThreadTable.class
				}));

		registry.register(
			"2.0.0", "3.0.0",
			new ViewCountUpgradeProcess(
				"MBThread", MBThread.class, "threadId", "viewCount"),
			new MBMessageTreePathUpgradeProcess());

		registry.register("3.0.0", "3.1.0", new UrlSubjectUpgradeProcess());

		registry.register(
			"3.1.0", "4.0.0",
			UpgradeProcessFactory.dropColumns(
				"MBCategory", "lastPostDate", "messageCount", "threadCount"));

		registry.register(
			"4.0.0", "5.0.0",
			UpgradeProcessFactory.dropColumns("MBThread", "messageCount"),
			new MVCCVersionUpgradeProcess() {

				@Override
				protected String[] getTableNames() {
					return new String[] {
						"MBBan", "MBCategory", "MBDiscussion", "MBMailingList",
						"MBMessage", "MBStatsUser", "MBThread", "MBThreadFlag"
					};
				}

			});

		registry.register(
			"5.0.0", "5.1.0",
			new CTModelUpgradeProcess(
				"MBBan", "MBCategory", "MBDiscussion", "MBMailingList",
				"MBMessage", "MBStatsUser", "MBThread", "MBThreadFlag"));

		registry.register("5.1.0", "5.2.0", new DummyUpgradeStep());

		registry.register("5.2.0", "6.0.0", new MBStatsUserUpgradeProcess());

		registry.register(
			"6.0.0", "6.1.0",
			UpgradeProcessFactory.alterColumnType(
				"MBThread", "title", "VARCHAR(75) null"));

		registry.register(
			"6.1.0", "6.1.1",
			UpgradeProcessFactory.alterColumnType(
				"MBMessage", "subject", "VARCHAR(255) null"),
			UpgradeProcessFactory.alterColumnType(
				"MBThread", "title", "VARCHAR(255) null"));

		registry.register(
			"6.1.1", "6.2.0",
			new BaseExternalReferenceCodeUpgradeProcess() {

				@Override
				protected String[][] getTableAndPrimaryKeyColumnNames() {
					return new String[][] {{"MBMessage", "messageId"}};
				}

			});

		registry.register("6.2.0", "6.3.0", MBSuspiciousActivityTable.create());

		registry.register(
			"6.3.0", "6.4.0",
			UpgradeProcessFactory.alterColumnName(
				"MBSuspiciousActivity", "type_", "reason VARCHAR(75) null"),
			UpgradeProcessFactory.alterColumnType(
				"MBSuspiciousActivity", "reason", "VARCHAR(255)"),
			UpgradeProcessFactory.dropColumns(
				"MBSuspiciousActivity", "description"));

		registry.register(
			"6.4.0", "6.4.1",
			UpgradeProcessFactory.alterColumnType(
				"MBSuspiciousActivity", "reason", "VARCHAR(255) null"));
	}

	@Reference
	private ResourceActionLocalService _resourceActionLocalService;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	/**
	 * See LPS-101086. The ViewCount table needs to exist.
	 */
	@Reference
	private ViewCountEntryLocalService _viewCountEntryLocalService;

}