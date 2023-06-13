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

package com.liferay.depot.staging.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.DepotEntryLocalService;
import com.liferay.depot.test.util.DepotStagingTestUtil;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.model.DLVersionNumberIncrease;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.friendly.url.model.FriendlyURLEntry;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.test.util.ConfigurationTemporarySwapper;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FriendlyURLNormalizer;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alicia García
 */
@RunWith(Arquillian.class)
public class DepotEntryFileEntryFriendlyURLTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_liveDepotEntry = _addDepotEntry();
	}

	@Test
	public void testDepotEntryFileEntryFriendlyURLEntries() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					_FF_FRIENDLY_URL_ENTRY_FILE_ENTRY_CONFIGURATION_PID,
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", true
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, TestPropsValues.getUserId(), _liveDepotEntry.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				StringUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				StringUtil.randomString(), "urltitle",
				StringUtil.randomString(), StringUtil.randomString(),
				new byte[0], null, null,
				ServiceContextTestUtil.getServiceContext(
					_liveDepotEntry.getGroupId()));

			FriendlyURLEntry friendlyURLEntry =
				_friendlyURLEntryLocalService.getMainFriendlyURLEntry(
					_portal.getClassNameId(FileEntry.class),
					fileEntry.getFileEntryId());

			Assert.assertNotNull(friendlyURLEntry);
			Assert.assertEquals("urltitle", friendlyURLEntry.getUrlTitle());
		}
	}

	@Test
	public void testDepotEntryFileEntryFriendlyURLEntriesNormalizedTitle()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					_FF_FRIENDLY_URL_ENTRY_FILE_ENTRY_CONFIGURATION_PID,
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", true
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, TestPropsValues.getUserId(), _liveDepotEntry.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				StringUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				StringUtil.randomString(), StringPool.BLANK,
				StringUtil.randomString(), StringUtil.randomString(),
				new byte[0], null, null,
				ServiceContextTestUtil.getServiceContext(
					_liveDepotEntry.getGroupId()));

			FriendlyURLEntry friendlyURLEntry =
				_friendlyURLEntryLocalService.getMainFriendlyURLEntry(
					_portal.getClassNameId(FileEntry.class),
					fileEntry.getFileEntryId());

			Assert.assertNotNull(friendlyURLEntry);
			Assert.assertEquals(
				_friendlyURLNormalizer.normalizeWithEncoding(
					fileEntry.getTitle()),
				friendlyURLEntry.getUrlTitle());
		}
	}

	@Test
	public void testDepotEntryFileEntryFriendlyURLEntriesUpdatingFileEntry()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					_FF_FRIENDLY_URL_ENTRY_FILE_ENTRY_CONFIGURATION_PID,
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", true
					).build())) {

			ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(
					_liveDepotEntry.getGroupId());

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, TestPropsValues.getUserId(), _liveDepotEntry.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				StringUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				StringUtil.randomString(), "urltitle",
				StringUtil.randomString(), StringUtil.randomString(),
				new byte[0], null, null, serviceContext);

			FriendlyURLEntry friendlyURLEntry =
				_friendlyURLEntryLocalService.getMainFriendlyURLEntry(
					_portal.getClassNameId(FileEntry.class),
					fileEntry.getFileEntryId());

			Assert.assertNotNull(friendlyURLEntry);
			Assert.assertEquals("urltitle", friendlyURLEntry.getUrlTitle());

			fileEntry = DLAppServiceUtil.updateFileEntry(
				fileEntry.getFileEntryId(), StringPool.BLANK,
				ContentTypes.TEXT_PLAIN, fileEntry.getTitle(),
				"modifiedurltitle", StringPool.BLANK, StringPool.BLANK,
				DLVersionNumberIncrease.MINOR, (byte[])null, null, null,
				serviceContext);

			friendlyURLEntry =
				_friendlyURLEntryLocalService.getMainFriendlyURLEntry(
					_portal.getClassNameId(FileEntry.class),
					fileEntry.getFileEntryId());

			Assert.assertNotNull(friendlyURLEntry);
			Assert.assertEquals(
				"modifiedurltitle", friendlyURLEntry.getUrlTitle());
		}
	}

	@Test
	public void testRemoteStagedDepotEntryFileEntryFriendlyURLEntriesNormalizedTitle()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					_FF_FRIENDLY_URL_ENTRY_FILE_ENTRY_CONFIGURATION_PID,
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", true
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, TestPropsValues.getUserId(), _liveDepotEntry.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				StringUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				StringUtil.randomString(), StringPool.BLANK,
				StringUtil.randomString(), StringUtil.randomString(),
				new byte[0], null, null,
				ServiceContextTestUtil.getServiceContext(
					_liveDepotEntry.getGroupId()));

			_stagingDepotEntry = _addDepotEntry();

			Group stagingDepotEntryGroup = _stagingDepotEntry.getGroup();

			stagingDepotEntryGroup.setLiveGroupId(_liveDepotEntry.getGroupId());

			GroupLocalServiceUtil.updateGroup(stagingDepotEntryGroup);

			_liveDepotEntry = DepotStagingTestUtil.enableRemoteStaging(
				_liveDepotEntry, _stagingDepotEntry);

			FileEntry stagedFileEntry =
				_dlAppLocalService.getFileEntryByUuidAndGroupId(
					fileEntry.getUuid(), _liveDepotEntry.getGroupId());

			FriendlyURLEntry friendlyURLEntry =
				_friendlyURLEntryLocalService.getMainFriendlyURLEntry(
					_portal.getClassNameId(FileEntry.class),
					stagedFileEntry.getFileEntryId());

			Assert.assertNotNull(friendlyURLEntry);
			Assert.assertEquals(
				_friendlyURLNormalizer.normalizeWithEncoding(
					fileEntry.getTitle()),
				friendlyURLEntry.getUrlTitle());
		}
	}

	@Test
	public void testStagedDepotEntryFileEntryFriendlyURLEntriesNormalizedTitle()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					_FF_FRIENDLY_URL_ENTRY_FILE_ENTRY_CONFIGURATION_PID,
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", true
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, TestPropsValues.getUserId(), _liveDepotEntry.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				StringUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				StringUtil.randomString(), StringPool.BLANK,
				StringUtil.randomString(), StringUtil.randomString(),
				new byte[0], null, null,
				ServiceContextTestUtil.getServiceContext(
					_liveDepotEntry.getGroupId()));

			_stagingDepotEntry = DepotStagingTestUtil.enableLocalStaging(
				_liveDepotEntry);

			FileEntry stagedFileEntry =
				_dlAppLocalService.getFileEntryByUuidAndGroupId(
					fileEntry.getUuid(), _liveDepotEntry.getGroupId());

			FriendlyURLEntry friendlyURLEntry =
				_friendlyURLEntryLocalService.getMainFriendlyURLEntry(
					_portal.getClassNameId(FileEntry.class),
					stagedFileEntry.getFileEntryId());

			Assert.assertNotNull(friendlyURLEntry);
			Assert.assertEquals(
				_friendlyURLNormalizer.normalizeWithEncoding(
					fileEntry.getTitle()),
				friendlyURLEntry.getUrlTitle());
		}
	}

	@Test
	public void testStageDepotEntryFileEntryFriendlyURLEntries()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					_FF_FRIENDLY_URL_ENTRY_FILE_ENTRY_CONFIGURATION_PID,
					HashMapDictionaryBuilder.<String, Object>put(
						"enabled", true
					).build())) {

			FileEntry fileEntry = _dlAppLocalService.addFileEntry(
				null, TestPropsValues.getUserId(), _liveDepotEntry.getGroupId(),
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				StringUtil.randomString(),
				ContentTypes.APPLICATION_OCTET_STREAM,
				StringUtil.randomString(), "urltitle",
				StringUtil.randomString(), StringUtil.randomString(),
				new byte[0], null, null,
				ServiceContextTestUtil.getServiceContext(
					_liveDepotEntry.getGroupId()));

			_stagingDepotEntry = DepotStagingTestUtil.enableLocalStaging(
				_liveDepotEntry);

			FileEntry stagedFileEntry =
				_dlAppLocalService.getFileEntryByUuidAndGroupId(
					fileEntry.getUuid(), _liveDepotEntry.getGroupId());

			FriendlyURLEntry friendlyURLEntry =
				_friendlyURLEntryLocalService.getMainFriendlyURLEntry(
					_portal.getClassNameId(FileEntry.class),
					stagedFileEntry.getFileEntryId());

			Assert.assertNotNull(friendlyURLEntry);
			Assert.assertEquals("urltitle", friendlyURLEntry.getUrlTitle());
		}
	}

	private DepotEntry _addDepotEntry() throws Exception {
		return _depotEntryLocalService.addDepotEntry(
			Collections.singletonMap(
				LocaleUtil.getDefault(), RandomTestUtil.randomString()),
			Collections.singletonMap(
				LocaleUtil.getDefault(), RandomTestUtil.randomString()),
			ServiceContextTestUtil.getServiceContext());
	}

	private static final String
		_FF_FRIENDLY_URL_ENTRY_FILE_ENTRY_CONFIGURATION_PID =
			"com.liferay.document.library.configuration." +
				"FFFriendlyURLEntryFileEntryConfiguration";

	@Inject
	private DepotEntryLocalService _depotEntryLocalService;

	@Inject
	private DLAppLocalService _dlAppLocalService;

	@Inject
	private FriendlyURLEntryLocalService _friendlyURLEntryLocalService;

	@Inject
	private FriendlyURLNormalizer _friendlyURLNormalizer;

	@DeleteAfterTestRun
	private DepotEntry _liveDepotEntry;

	@Inject
	private Portal _portal;

	@DeleteAfterTestRun
	private DepotEntry _stagingDepotEntry;

}