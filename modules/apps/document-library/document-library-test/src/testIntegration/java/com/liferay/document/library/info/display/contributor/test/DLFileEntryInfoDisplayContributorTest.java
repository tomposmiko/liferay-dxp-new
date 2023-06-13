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

package com.liferay.document.library.info.display.contributor.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.display.page.constants.AssetDisplayPageConstants;
import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.asset.display.page.service.AssetDisplayPageEntryLocalService;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileEntryConstants;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.test.util.DLTestUtil;
import com.liferay.document.library.util.DLURLHelper;
import com.liferay.info.display.contributor.InfoDisplayContributor;
import com.liferay.info.display.contributor.InfoDisplayObjectProvider;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateCollection;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateCollectionService;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryService;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.text.Format;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alejandro Tardín
 */
@RunWith(Arquillian.class)
public class DLFileEntryInfoDisplayContributorTest {

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
	public void testDisplayPageURL() throws Exception {
		_withAndWithoutAssetEntry(
			fileEntry -> {
				_addAssetDisplayPageEntry(fileEntry);

				ThemeDisplay themeDisplay = new ThemeDisplay();

				themeDisplay.setLocale(LocaleUtil.getDefault());
				themeDisplay.setScopeGroupId(_group.getGroupId());
				themeDisplay.setServerName("localhost");
				themeDisplay.setSiteGroupId(_group.getGroupId());

				String expectedURL = StringBundler.concat(
					"/web/", StringUtil.lowerCase(_group.getGroupKey()), "/d/",
					fileEntry.getFileEntryId());

				Assert.assertEquals(
					expectedURL,
					_assetDisplayPageFriendlyURLProvider.getFriendlyURL(
						DLFileEntryConstants.getClassName(),
						fileEntry.getFileEntryId(), themeDisplay));
			});
	}

	@Test
	public void testDLFileEntryInfoDisplayContributor() throws Exception {
		_withAndWithoutAssetEntry(
			fileEntry -> {
				Assert.assertEquals(
					DLFileEntryConstants.getClassName(),
					_infoDisplayContributor.getClassName());

				Assert.assertEquals(
					"/d/", _infoDisplayContributor.getInfoURLSeparator());

				Assert.assertEquals(
					"Document",
					_infoDisplayContributor.getLabel(LocaleUtil.getDefault()));

				Map<String, Object> infoDisplayFieldsValues =
					_infoDisplayContributor.getInfoDisplayFieldsValues(
						fileEntry, LocaleUtil.getDefault());

				Assert.assertEquals(
					fileEntry.getUserName(),
					infoDisplayFieldsValues.get("authorName"));
				Assert.assertEquals(
					StringPool.BLANK,
					infoDisplayFieldsValues.get("authorProfileImage"));
				Assert.assertEquals(
					null, infoDisplayFieldsValues.get("categories"));
				Assert.assertEquals(
					fileEntry.getDescription(),
					infoDisplayFieldsValues.get("description"));
				Assert.assertEquals(
					_dlurlHelper.getDownloadURL(
						fileEntry, fileEntry.getFileVersion(), null,
						StringPool.BLANK),
					infoDisplayFieldsValues.get("downloadURL"));
				Assert.assertEquals(
					fileEntry.getFileName(),
					infoDisplayFieldsValues.get("fileName"));
				Assert.assertEquals(
					fileEntry.getMimeType(),
					infoDisplayFieldsValues.get("mimeType"));

				JSONObject previewImageJSONObject =
					(JSONObject)infoDisplayFieldsValues.get("previewImage");

				Assert.assertEquals(
					StringPool.BLANK, previewImageJSONObject.getString("url"));

				Format dateFormatDateTime =
					FastDateFormatFactoryUtil.getDateTime(
						LocaleUtil.getDefault());

				Assert.assertEquals(
					dateFormatDateTime.format(fileEntry.getModifiedDate()),
					infoDisplayFieldsValues.get("publishDate"));

				Assert.assertEquals(
					TextFormatter.formatStorageSize(
						fileEntry.getSize(), LocaleUtil.getDefault()),
					infoDisplayFieldsValues.get("size"));
				Assert.assertEquals(
					null, infoDisplayFieldsValues.get("tagNames"));
				Assert.assertEquals(
					fileEntry.getTitle(), infoDisplayFieldsValues.get("title"));
				Assert.assertEquals(
					fileEntry.getVersion(),
					infoDisplayFieldsValues.get("version"));
			});
	}

	@Test
	public void testInfoDisplayObjectProvider() throws Exception {
		_withAndWithoutAssetEntry(
			fileEntry -> {
				InfoDisplayObjectProvider infoDisplayObjectProvider =
					_infoDisplayContributor.getInfoDisplayObjectProvider(
						fileEntry.getFileEntryId());

				Assert.assertEquals(
					fileEntry.getTitle(),
					infoDisplayObjectProvider.getTitle(
						LocaleUtil.getDefault()));

				Assert.assertEquals(
					fileEntry.getDescription(),
					infoDisplayObjectProvider.getDescription(
						LocaleUtil.getDefault()));

				Assert.assertEquals(
					String.valueOf(fileEntry.getFileEntryId()),
					infoDisplayObjectProvider.getURLTitle(
						LocaleUtil.getDefault()));

				Assert.assertEquals(
					StringPool.BLANK,
					infoDisplayObjectProvider.getKeywords(
						LocaleUtil.getDefault()));

				Assert.assertEquals(
					_portal.getClassNameId(DLFileEntryConstants.getClassName()),
					infoDisplayObjectProvider.getClassNameId());

				Assert.assertEquals(
					fileEntry.getFileEntryId(),
					infoDisplayObjectProvider.getClassPK());

				Assert.assertEquals(
					fileEntry.getGroupId(),
					infoDisplayObjectProvider.getGroupId());

				Assert.assertEquals(
					0, infoDisplayObjectProvider.getClassTypeId());

				Assert.assertEquals(
					fileEntry, infoDisplayObjectProvider.getDisplayObject());
			});
	}

	private void _addAssetDisplayPageEntry(FileEntry dlFileEntry)
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		LayoutPageTemplateCollection layoutPageTemplateCollection =
			_layoutPageTemplateCollectionService.
				addLayoutPageTemplateCollection(
					_group.getGroupId(), RandomTestUtil.randomString(), null,
					serviceContext);

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_layoutPageTemplateEntryService.addLayoutPageTemplateEntry(
				_group.getGroupId(),
				layoutPageTemplateCollection.
					getLayoutPageTemplateCollectionId(),
				RandomTestUtil.randomString(),
				LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE,
				WorkflowConstants.STATUS_DRAFT, serviceContext);

		_assetDisplayPageEntryLocalService.addAssetDisplayPageEntry(
			dlFileEntry.getUserId(), _group.getGroupId(),
			_portal.getClassNameId(DLFileEntryConstants.getClassName()),
			dlFileEntry.getFileEntryId(),
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
			AssetDisplayPageConstants.TYPE_SPECIFIC, serviceContext);
	}

	private void _withAndWithoutAssetEntry(
			UnsafeConsumer<FileEntry, PortalException> testFunction)
		throws Exception {

		DLFolder dlFolder = DLTestUtil.addDLFolder(_group.getGroupId());

		DLFileEntry dlFileEntry = DLTestUtil.addDLFileEntry(
			dlFolder.getFolderId());

		testFunction.accept(
			_dlAppLocalService.getFileEntry(dlFileEntry.getFileEntryId()));

		dlFileEntry = DLTestUtil.addDLFileEntry(dlFolder.getFolderId());

		AssetEntryLocalServiceUtil.deleteEntry(
			DLFileEntryConstants.getClassName(), dlFileEntry.getFileEntryId());

		testFunction.accept(
			_dlAppLocalService.getFileEntry(dlFileEntry.getFileEntryId()));
	}

	@Inject
	private AssetDisplayPageEntryLocalService
		_assetDisplayPageEntryLocalService;

	@Inject
	private AssetDisplayPageFriendlyURLProvider
		_assetDisplayPageFriendlyURLProvider;

	@Inject
	private DLAppLocalService _dlAppLocalService;

	@Inject
	private DLURLHelper _dlurlHelper;

	@DeleteAfterTestRun
	private Group _group;

	@Inject(filter = "component.name=*.DLFileEntryInfoDisplayContributor")
	private InfoDisplayContributor<FileEntry> _infoDisplayContributor;

	@Inject
	private LayoutPageTemplateCollectionService
		_layoutPageTemplateCollectionService;

	@Inject
	private LayoutPageTemplateEntryService _layoutPageTemplateEntryService;

	@Inject
	private Portal _portal;

}