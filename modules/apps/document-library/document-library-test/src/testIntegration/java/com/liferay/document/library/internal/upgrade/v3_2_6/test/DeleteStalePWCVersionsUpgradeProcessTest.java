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

package com.liferay.document.library.internal.upgrade.v3_2_6.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileEntryConstants;
import com.liferay.document.library.kernel.model.DLFileVersion;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.model.DLVersionNumberIncrease;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.store.DLStoreRequest;
import com.liferay.document.library.kernel.store.DLStoreUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import java.util.Objects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Adolfo Pérez
 */
@RunWith(Arquillian.class)
public class DeleteStalePWCVersionsUpgradeProcessTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testDeletePWCVersionsPreservesCheckedOutDocuments()
		throws PortalException {

		FileEntry fileEntry1 = _addFileEntry();
		FileEntry fileEntry2 = _addFileEntry();
		FileEntry fileEntry3 = _addFileEntry();

		_checkOutFileEntry(fileEntry1);
		_checkOutFileEntry(fileEntry2);
		_checkOutFileEntry(fileEntry3);

		Assert.assertTrue(_hasPWCVersion(fileEntry1));
		Assert.assertTrue(_hasPWCVersion(fileEntry2));
		Assert.assertTrue(_hasPWCVersion(fileEntry3));

		_checkInFileEntry(fileEntry1);

		Assert.assertFalse(_hasPWCVersion(fileEntry1));

		_createStalePWCVersion(fileEntry1);

		Assert.assertTrue(_hasPWCVersion(fileEntry1));

		_runUpgrade();

		Assert.assertFalse(_hasPWCVersion(fileEntry1));
		Assert.assertTrue(_hasPWCVersion(fileEntry2));
		Assert.assertTrue(_hasPWCVersion(fileEntry3));
	}

	private FileEntry _addFileEntry() throws PortalException {
		return _dlAppService.addFileEntry(
			null, _group.getGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			StringUtil.randomString(), ContentTypes.APPLICATION_OCTET_STREAM,
			StringUtil.randomString(), StringUtil.randomString(),
			StringUtil.randomString(), StringUtil.randomString(), new byte[0],
			null, null,
			ServiceContextTestUtil.getServiceContext(
				_group, TestPropsValues.getUserId()));
	}

	private void _checkInFileEntry(FileEntry fileEntry) throws PortalException {
		_dlAppService.checkInFileEntry(
			fileEntry.getFileEntryId(), DLVersionNumberIncrease.MAJOR,
			StringUtil.randomString(),
			ServiceContextTestUtil.getServiceContext(
				_group, TestPropsValues.getUserId()));
	}

	private void _checkOutFileEntry(FileEntry fileEntry)
		throws PortalException {

		_dlAppService.checkOutFileEntry(
			fileEntry.getFileEntryId(),
			ServiceContextTestUtil.getServiceContext(
				_group, TestPropsValues.getUserId()));
	}

	private void _createStalePWCVersion(FileEntry fileEntry)
		throws PortalException {

		DLFileEntry dlFileEntry = (DLFileEntry)fileEntry.getModel();

		DLStoreUtil.addFile(
			DLStoreRequest.builder(
				dlFileEntry.getCompanyId(), dlFileEntry.getRepositoryId(),
				dlFileEntry.getName()
			).versionLabel(
				DLFileEntryConstants.PRIVATE_WORKING_COPY_VERSION
			).build(),
			new byte[0]);
	}

	private UpgradeProcess _getUpgradeProcess() {
		UpgradeProcess[] upgradeProcesses = new UpgradeProcess[1];

		_upgradeStepRegistrator.register(
			(fromSchemaVersionString, toSchemaVersionString, upgradeSteps) -> {
				for (UpgradeStep upgradeStep : upgradeSteps) {
					Class<? extends UpgradeStep> clazz = upgradeStep.getClass();

					if (Objects.equals(clazz.getName(), _CLASS_NAME)) {
						upgradeProcesses[0] = (UpgradeProcess)upgradeStep;

						break;
					}
				}
			});

		return upgradeProcesses[0];
	}

	private boolean _hasPWCVersion(FileEntry fileEntry) throws PortalException {
		DLFileEntry dlFileEntry = (DLFileEntry)fileEntry.getModel();

		DLFileVersion dlFileVersion = dlFileEntry.getLatestFileVersion(true);

		if (Objects.equals(
				dlFileVersion.getVersion(),
				DLFileEntryConstants.PRIVATE_WORKING_COPY_VERSION)) {

			return DLStoreUtil.hasFile(
				dlFileEntry.getCompanyId(), dlFileEntry.getRepositoryId(),
				dlFileEntry.getName(), dlFileVersion.getStoreFileName());
		}

		return DLStoreUtil.hasFile(
			dlFileEntry.getCompanyId(), dlFileEntry.getRepositoryId(),
			dlFileEntry.getName(),
			DLFileEntryConstants.PRIVATE_WORKING_COPY_VERSION);
	}

	private void _runUpgrade() throws UpgradeException {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				_CLASS_NAME, LoggerTestUtil.OFF)) {

			UpgradeProcess upgradeProcess = _getUpgradeProcess();

			upgradeProcess.upgrade();
		}
	}

	private static final String _CLASS_NAME =
		"com.liferay.document.library.internal.upgrade.v3_2_6." +
			"DeleteStalePWCVersionsUpgradeProcess";

	@Inject(
		filter = "(&(component.name=com.liferay.document.library.internal.upgrade.registry.DLServiceUpgradeStepRegistrator))"
	)
	private static UpgradeStepRegistrator _upgradeStepRegistrator;

	@Inject
	private DLAppService _dlAppService;

	@DeleteAfterTestRun
	private Group _group;

}