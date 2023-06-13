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

package com.liferay.document.library.internal.lock.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.petra.function.UnsafeRunnable;
import com.liferay.petra.lang.SafeCloseable;
import com.liferay.portal.kernel.lock.LockListener;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.test.constants.TestDataConstants;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.PropsValuesTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Cristina González
 */
@RunWith(Arquillian.class)
public class DLFileEntryLockListenerTest {

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
	public void testOnAfterExpireWithCancelCheckOutPolicy() throws Exception {
		_testWithCancelCheckOutAsPolicy(
			() -> {
				FileEntry fileEntry = _dlAppLocalService.addFileEntry(
					null, TestPropsValues.getUserId(), _group.getGroupId(),
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
					RandomTestUtil.randomString(), ContentTypes.TEXT_PLAIN,
					TestDataConstants.TEST_BYTE_ARRAY, null, null,
					ServiceContextTestUtil.getServiceContext(
						_group, TestPropsValues.getUserId()));

				_dlFileEntryLocalService.checkOutFileEntry(
					TestPropsValues.getUserId(), fileEntry.getFileEntryId(),
					ServiceContextTestUtil.getServiceContext());

				_lockListener.onAfterExpire(
					String.valueOf(fileEntry.getFileEntryId()));

				FileEntry finalFileEntry = _dlAppLocalService.getFileEntry(
					fileEntry.getFileEntryId());

				Assert.assertFalse(finalFileEntry.isCheckedOut());

				FileVersion latestFileVersion =
					finalFileEntry.getLatestFileVersion();

				Assert.assertEquals(
					fileEntry.getVersion(), latestFileVersion.getVersion());
			});
	}

	@Test
	public void testOnAfterExpireWithCheckInPolicy() throws Exception {
		_testWithCheckInAsPolicy(
			() -> {
				FileEntry fileEntry = _dlAppLocalService.addFileEntry(
					null, TestPropsValues.getUserId(), _group.getGroupId(),
					DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
					RandomTestUtil.randomString(), ContentTypes.TEXT_PLAIN,
					TestDataConstants.TEST_BYTE_ARRAY, null, null,
					ServiceContextTestUtil.getServiceContext(
						_group, TestPropsValues.getUserId()));

				fileEntry.getVersion();

				_dlFileEntryLocalService.checkOutFileEntry(
					TestPropsValues.getUserId(), fileEntry.getFileEntryId(),
					ServiceContextTestUtil.getServiceContext());

				_lockListener.onAfterExpire(
					String.valueOf(fileEntry.getFileEntryId()));

				FileEntry finalFileEntry = _dlAppLocalService.getFileEntry(
					fileEntry.getFileEntryId());

				Assert.assertFalse(finalFileEntry.isCheckedOut());

				FileVersion latestFileVersion =
					finalFileEntry.getLatestFileVersion();

				Assert.assertNotEquals(
					fileEntry.getVersion(), latestFileVersion.getVersion());
			});
	}

	private void _testWithCancelCheckOutAsPolicy(
			UnsafeRunnable<Exception> unsafeRunnable)
		throws Exception {

		try (SafeCloseable safeCloseable =
				PropsValuesTestUtil.swapWithSafeCloseable(
					"DL_FILE_ENTRY_LOCK_POLICY", 0)) {

			unsafeRunnable.run();
		}
	}

	private void _testWithCheckInAsPolicy(
			UnsafeRunnable<Exception> unsafeRunnable)
		throws Exception {

		try (SafeCloseable safeCloseable =
				PropsValuesTestUtil.swapWithSafeCloseable(
					"DL_FILE_ENTRY_LOCK_POLICY", 1)) {

			unsafeRunnable.run();
		}
	}

	@Inject
	private DLAppLocalService _dlAppLocalService;

	@Inject
	private DLFileEntryLocalService _dlFileEntryLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject(
		filter = "component.name=com.liferay.document.library.internal.lock.DLFileEntryLockListener"
	)
	private LockListener _lockListener;

}