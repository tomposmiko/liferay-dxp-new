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

package com.liferay.journal.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.display.page.constants.AssetDisplayPageConstants;
import com.liferay.asset.display.page.service.AssetDisplayPageEntryLocalService;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.data.engine.rest.dto.v2_0.DataDefinition;
import com.liferay.data.engine.rest.resource.v2_0.DataDefinitionResource;
import com.liferay.data.engine.rest.test.util.DataDefinitionTestUtil;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLinkLocalService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.Field;
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMTemplateTestUtil;
import com.liferay.exportimport.kernel.service.StagingLocalService;
import com.liferay.journal.constants.JournalArticleConstants;
import com.liferay.journal.constants.JournalFolderConstants;
import com.liferay.journal.exception.ArticleFriendlyURLException;
import com.liferay.journal.exception.DuplicateArticleExternalReferenceCodeException;
import com.liferay.journal.exception.DuplicateArticleIdException;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalArticleDisplay;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.journal.util.JournalConverter;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.FriendlyURLNormalizer;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TempFileEntryUtil;
import com.liferay.portal.kernel.util.TimeZoneUtil;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.InputStream;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Michael C. Han
 */
@RunWith(Arquillian.class)
public class JournalArticleLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		Layout layout = LayoutTestUtil.addTypePortletLayout(_group);

		_themeDisplay = new ThemeDisplay();

		_themeDisplay.setCompany(
			_companyLocalService.getCompany(_group.getCompanyId()));
		_themeDisplay.setLayout(layout);
		_themeDisplay.setLookAndFeel(
			layout.getTheme(), layout.getColorScheme());

		User user = _userLocalService.getUser(_group.getCreatorUserId());

		_themeDisplay.setRealUser(user);

		HttpServletRequest httpServletRequest = new MockHttpServletRequest();

		httpServletRequest.setAttribute(WebKeys.THEME_DISPLAY, _themeDisplay);

		_themeDisplay.setRequest(httpServletRequest);

		_themeDisplay.setResponse(new MockHttpServletResponse());
		_themeDisplay.setScopeGroupId(_group.getGroupId());
		_themeDisplay.setSiteGroupId(_group.getGroupId());
		_themeDisplay.setUser(user);
	}

	@Test
	public void testArticleFriendlyURLValidation() throws Exception {
		_assertArticleFriendlyURLMap(_group);

		Group companyGroup = _groupLocalService.getCompanyGroup(
			_group.getCompanyId());

		_assertArticleFriendlyURLMap(companyGroup);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				companyGroup.getGroupId(), TestPropsValues.getUserId());

		PermissionChecker originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		try {
			PermissionThreadLocal.setPermissionChecker(
				PermissionCheckerFactoryUtil.create(TestPropsValues.getUser()));

			_stagingLocalService.enableLocalStaging(
				TestPropsValues.getUserId(), companyGroup, false, false,
				serviceContext);

			_assertArticleFriendlyURLMap(companyGroup.getStagingGroup());
		}
		finally {
			try {
				_stagingLocalService.disableStaging(
					companyGroup, serviceContext);
			}
			catch (Exception exception) {
			}

			PermissionThreadLocal.setPermissionChecker(
				originalPermissionChecker);
		}
	}

	@Test(expected = ArticleFriendlyURLException.class)
	public void testArticleFriendlyURLValidationCompanyGroup()
		throws Exception {

		Group companyGroup = _groupLocalService.getCompanyGroup(
			_group.getCompanyId());

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			companyGroup.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			Collections.emptyMap());

		_updateJournalArticle(Collections.emptyMap(), journalArticle);
	}

	@Test(expected = ArticleFriendlyURLException.class)
	public void testArticleFriendlyURLValidationCompanyGroupStagingEnabled()
		throws Exception {

		Group companyGroup = _groupLocalService.getCompanyGroup(
			_group.getCompanyId());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				companyGroup.getGroupId(), TestPropsValues.getUserId());

		PermissionChecker originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		try {
			PermissionThreadLocal.setPermissionChecker(
				PermissionCheckerFactoryUtil.create(TestPropsValues.getUser()));

			_stagingLocalService.enableLocalStaging(
				TestPropsValues.getUserId(), companyGroup, false, false,
				serviceContext);

			Group stagingGroup = companyGroup.getStagingGroup();

			JournalArticle journalArticle = JournalTestUtil.addArticle(
				stagingGroup.getGroupId(),
				JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				Collections.emptyMap());

			_updateJournalArticle(Collections.emptyMap(), journalArticle);
		}
		finally {
			try {
				_stagingLocalService.disableStaging(
					companyGroup, serviceContext);
			}
			catch (Exception exception) {
			}

			PermissionThreadLocal.setPermissionChecker(
				originalPermissionChecker);
		}
	}

	@Test(expected = ArticleFriendlyURLException.class)
	public void testArticleFriendlyURLValidationThrowsArticleFriendlyURLException()
		throws Exception {

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			Collections.emptyMap());

		_updateJournalArticle(Collections.emptyMap(), journalArticle);
	}

	@Test
	public void testCopyArticle() throws Exception {
		JournalArticle oldArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, "Test-1",
			RandomTestUtil.randomString());

		JournalArticle thirdArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, "Test-2",
			oldArticle.getContent());

		JournalArticle newArticle = _journalArticleLocalService.copyArticle(
			oldArticle.getUserId(), oldArticle.getGroupId(),
			oldArticle.getArticleId(), null, true, oldArticle.getVersion());

		Assert.assertNotEquals(oldArticle, newArticle);
		Assert.assertNotEquals(
			thirdArticle.getUrlTitle(), newArticle.getUrlTitle());

		List<ResourcePermission> oldResourcePermissions =
			_resourcePermissionLocalService.getResourcePermissions(
				oldArticle.getCompanyId(), JournalArticle.class.getName(),
				ResourceConstants.SCOPE_INDIVIDUAL,
				String.valueOf(oldArticle.getResourcePrimKey()));

		List<ResourcePermission> newResourcePermissions =
			_resourcePermissionLocalService.getResourcePermissions(
				newArticle.getCompanyId(), JournalArticle.class.getName(),
				ResourceConstants.SCOPE_INDIVIDUAL,
				String.valueOf(newArticle.getResourcePrimKey()));

		Assert.assertEquals(
			StringBundler.concat(
				"Old resource permissions: ", oldResourcePermissions,
				", new resource permissions: ", newResourcePermissions),
			oldResourcePermissions.size(), newResourcePermissions.size());

		for (int i = 0; i < oldResourcePermissions.size(); i++) {
			ResourcePermission oldResourcePermission =
				oldResourcePermissions.get(i);
			ResourcePermission newResourcePermission =
				newResourcePermissions.get(i);

			Assert.assertNotEquals(
				oldResourcePermission, newResourcePermission);

			Assert.assertEquals(
				oldResourcePermission.getRoleId(),
				newResourcePermission.getRoleId());
			Assert.assertEquals(
				oldResourcePermission.getOwnerId(),
				newResourcePermission.getOwnerId());
			Assert.assertEquals(
				oldResourcePermission.getActionIds(),
				newResourcePermission.getActionIds());
			Assert.assertEquals(
				oldResourcePermission.isViewActionId(),
				newResourcePermission.isViewActionId());
		}
	}

	@Test
	public void testCopyArticleWithImages() throws Exception {
		DataDefinition dataDefinition =
			DataDefinitionTestUtil.addDataDefinition(
				"journal", _dataDefinitionResourceFactory, _group.getGroupId(),
				_readFileToString("ddm_form_with_images.json"),
				TestPropsValues.getUser());

		JournalArticle oldArticle = JournalTestUtil.addArticleWithXMLContent(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASS_NAME_ID_DEFAULT,
			StringUtil.replace(
				_readFileToString("journal_content_with_images.xml"),
				new String[] {"[$IMAGE_JSON_1$]", "[$IMAGE_JSON_2$]"},
				new String[] {
					_toJSON(_addTempFileEntry("test_01.jpg")),
					_toJSON(_addTempFileEntry("test_02.jpg"))
				}),
			dataDefinition.getDataDefinitionKey(), null, LocaleUtil.US);

		JournalArticle newArticle = _journalArticleLocalService.copyArticle(
			oldArticle.getUserId(), oldArticle.getGroupId(),
			oldArticle.getArticleId(), null, true, oldArticle.getVersion());

		Assert.assertEquals(
			oldArticle.getImagesFileEntriesCount(),
			newArticle.getImagesFileEntriesCount());

		_validateDDMFormValuesImages(newArticle);
	}

	@Test
	public void testCopyArticleWithImagesAndNestedFields() throws Exception {
		DataDefinition dataDefinition =
			DataDefinitionTestUtil.addDataDefinition(
				"journal", _dataDefinitionResourceFactory, _group.getGroupId(),
				_readFileToString(
					"ddm_form_with_images_and_nested_fields.json"),
				TestPropsValues.getUser());

		JournalArticle oldArticle = JournalTestUtil.addArticleWithXMLContent(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASS_NAME_ID_DEFAULT,
			StringUtil.replace(
				_readFileToString(
					"journal_content_with_images_and_nested_fields.xml"),
				new String[] {"[$IMAGE_JSON_1$]", "[$IMAGE_JSON_2$]"},
				new String[] {
					_toJSON(_addTempFileEntry("test_01.jpg")),
					_toJSON(_addTempFileEntry("test_02.jpg"))
				}),
			dataDefinition.getDataDefinitionKey(), null, LocaleUtil.US);

		JournalArticle newArticle = _journalArticleLocalService.copyArticle(
			oldArticle.getUserId(), oldArticle.getGroupId(),
			oldArticle.getArticleId(), null, true, oldArticle.getVersion());

		Assert.assertEquals(
			oldArticle.getImagesFileEntriesCount(),
			newArticle.getImagesFileEntriesCount());

		_validateDDMFormValuesImages(newArticle);
	}

	@Test
	public void testCopyArticleWithImagesAndRepeatableFields()
		throws Exception {

		DataDefinition dataDefinition =
			DataDefinitionTestUtil.addDataDefinition(
				"journal", _dataDefinitionResourceFactory, _group.getGroupId(),
				_readFileToString(
					"ddm_form_with_images_and_repeatable_fields.json"),
				TestPropsValues.getUser());

		JournalArticle oldArticle = JournalTestUtil.addArticleWithXMLContent(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASS_NAME_ID_DEFAULT,
			StringUtil.replace(
				_readFileToString(
					"journal_content_with_images_and_repeatable_fields.xml"),
				new String[] {
					"[$IMAGE_JSON_1$]", "[$IMAGE_JSON_2$]", "[$IMAGE_JSON_3$]",
					"[$IMAGE_JSON_4$]", "[$IMAGE_JSON_5$]", "[$IMAGE_JSON_6$]",
					"[$IMAGE_JSON_7$]", "[$IMAGE_JSON_8$]"
				},
				new String[] {
					_toJSON(_addTempFileEntry("test_01.jpg")),
					_toJSON(_addTempFileEntry("test_02.jpg")),
					_toJSON(_addTempFileEntry("test_03.jpg")),
					_toJSON(_addTempFileEntry("test_04.jpg")),
					_toJSON(_addTempFileEntry("test_05.jpg")),
					_toJSON(_addTempFileEntry("test_06.jpg")),
					_toJSON(_addTempFileEntry("test_07.jpg")),
					_toJSON(_addTempFileEntry("test_08.jpg"))
				}),
			dataDefinition.getDataDefinitionKey(), null, LocaleUtil.US);

		JournalArticle newArticle = _journalArticleLocalService.copyArticle(
			oldArticle.getUserId(), oldArticle.getGroupId(),
			oldArticle.getArticleId(), null, true, oldArticle.getVersion());

		Assert.assertEquals(
			oldArticle.getImagesFileEntriesCount(),
			newArticle.getImagesFileEntriesCount());

		_validateDDMFormValuesImages(newArticle);
	}

	@Test
	public void testDeleteDDMStructurePredefinedValues() throws Exception {
		Tuple tuple = _createJournalArticleWithPredefinedValues("Test Article");

		JournalArticle journalArticle = (JournalArticle)tuple.getObject(0);
		DDMStructure ddmStructure = (DDMStructure)tuple.getObject(1);

		DDMStructure actualDDMStructure = journalArticle.getDDMStructure();

		List<DDMFormField> ddmFormFields = actualDDMStructure.getDDMFormFields(
			false);

		for (DDMFormField ddmFormField : ddmFormFields) {
			LocalizedValue localizedValue = ddmFormField.getPredefinedValue();

			Map<Locale, String> values = localizedValue.getValues();

			for (Map.Entry<Locale, String> entry : values.entrySet()) {
				Assert.assertNotEquals(StringPool.BLANK, entry.getValue());
			}
		}

		Assert.assertEquals(
			actualDDMStructure.getStructureId(), ddmStructure.getStructureId());

		JournalArticle defaultJournalArticle =
			_journalArticleLocalService.getArticle(
				ddmStructure.getGroupId(), DDMStructure.class.getName(),
				ddmStructure.getStructureId());

		Assert.assertEquals(
			defaultJournalArticle.getTitle(), journalArticle.getTitle());

		ServiceContextThreadLocal.pushServiceContext(
			ServiceContextTestUtil.getServiceContext());

		_journalArticleLocalService.deleteArticleDefaultValues(
			journalArticle.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getDDMStructureKey());

		Assert.assertNull(
			_journalArticleLocalService.fetchLatestArticle(
				defaultJournalArticle.getResourcePrimKey()));

		actualDDMStructure = _ddmStructureLocalService.getDDMStructure(
			actualDDMStructure.getStructureId());

		ddmFormFields = actualDDMStructure.getDDMFormFields(false);

		for (DDMFormField ddmFormField : ddmFormFields) {
			LocalizedValue localizedValue = ddmFormField.getPredefinedValue();

			Map<Locale, String> values = localizedValue.getValues();

			for (Map.Entry<Locale, String> entry : values.entrySet()) {
				Assert.assertEquals(StringPool.BLANK, entry.getValue());
			}
		}
	}

	@Test(expected = DuplicateArticleIdException.class)
	public void testDuplicatedArticleId() throws Exception {
		JournalArticle article = JournalTestUtil.addArticle(
			RandomTestUtil.randomString(), _group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, null, true);

		JournalTestUtil.addArticle(
			RandomTestUtil.randomString(), _group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			article.getArticleId(), false);
	}

	@Test
	public void testDuplicatedAutoGeneratedArticleId() throws Exception {
		JournalArticle article = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			article.getArticleId(), true);
	}

	@Test(expected = DuplicateArticleExternalReferenceCodeException.class)
	public void testDuplicatedExternalReferenceCode() throws Exception {
		JournalArticle article = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		JournalTestUtil.addArticle(
			article.getExternalReferenceCode(), _group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			article.getArticleId(), true);
	}

	@Test
	public void testGetArticleDisplayFriendlyURLDisplayPageExists()
		throws Exception {

		String defaultLanguageId = LocaleUtil.toLanguageId(
			LocaleUtil.getSiteDefault());

		JournalArticle article = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		String script = StringBundler.concat(
			"${friendlyURLs[\"", defaultLanguageId, "\"]}");

		DDMStructure ddmStructure = article.getDDMStructure();

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			_portal.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_FTL, script,
			LocaleUtil.getSiteDefault());

		article.setDDMTemplateKey(ddmTemplate.getTemplateKey());

		_journalArticleLocalService.updateJournalArticle(article);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_layoutPageTemplateEntryLocalService.addLayoutPageTemplateEntry(
				_group.getCreatorUserId(), _group.getGroupId(), 0,
				_portal.getClassNameId(JournalArticle.class.getName()),
				ddmStructure.getStructureId(), RandomTestUtil.randomString(),
				LayoutPageTemplateEntryTypeConstants.TYPE_DISPLAY_PAGE, 0, true,
				0, 0, 0, 0, serviceContext);

		_assetDisplayPageEntryLocalService.addAssetDisplayPageEntry(
			article.getUserId(), _group.getGroupId(),
			_portal.getClassNameId(JournalArticle.class.getName()),
			article.getResourcePrimKey(),
			layoutPageTemplateEntry.getLayoutPageTemplateEntryId(),
			AssetDisplayPageConstants.TYPE_DEFAULT, serviceContext);

		JournalArticleDisplay articleDisplay =
			_journalArticleLocalService.getArticleDisplay(
				_group.getGroupId(), article.getArticleId(), Constants.VIEW,
				defaultLanguageId, _themeDisplay);

		String content = articleDisplay.getContent();

		Assert.assertTrue(content.contains(_group.getFriendlyURL()));
		Assert.assertTrue(
			content.contains(article.getUrlTitle(LocaleUtil.getSiteDefault())));
	}

	@Test
	public void testGetArticleDisplayFriendlyURLWithoutDisplayPage()
		throws Exception {

		String defaultLanguageId = LocaleUtil.toLanguageId(
			LocaleUtil.getSiteDefault());

		JournalArticle article = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		String script = StringBundler.concat(
			"${friendlyURLs[\"", defaultLanguageId, "\"]!\"no-friendly-url\"}");

		DDMStructure ddmStructure = article.getDDMStructure();

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			_portal.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_FTL, script,
			LocaleUtil.getSiteDefault());

		article.setDDMTemplateKey(ddmTemplate.getTemplateKey());

		_journalArticleLocalService.updateJournalArticle(article);

		JournalArticleDisplay articleDisplay =
			_journalArticleLocalService.getArticleDisplay(
				_group.getGroupId(), article.getArticleId(), Constants.VIEW,
				defaultLanguageId, _themeDisplay);

		Assert.assertEquals("no-friendly-url", articleDisplay.getContent());
	}

	@Test
	public void testGetArticleDisplayWithComplexData() throws Exception {
		DataDefinition dataDefinition =
			DataDefinitionTestUtil.addDataDefinition(
				"journal", _dataDefinitionResourceFactory, _group.getGroupId(),
				_readFileToString("complex_data_definition.json"),
				TestPropsValues.getUser());

		FileEntry fileEntry = _dlAppLocalService.addFileEntry(
			null, TestPropsValues.getUserId(), _group.getGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			StringUtil.randomString(), ContentTypes.IMAGE_JPEG,
			FileUtil.getBytes(getClass(), "dependencies/image.jpg"), null, null,
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		JournalArticle journalArticle =
			JournalTestUtil.addArticleWithXMLContent(
				_group.getGroupId(),
				JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				JournalArticleConstants.CLASS_NAME_ID_DEFAULT,
				StringUtil.replace(
					_readFileToString("complex_journal_content.xml"),
					"[$DOCUMENT_JSON$]", _toJSON(fileEntry)),
				dataDefinition.getDataDefinitionKey(), null, LocaleUtil.SPAIN);

		DDMStructure ddmStructure = DDMStructureLocalServiceUtil.fetchStructure(
			_group.getGroupId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			dataDefinition.getDataDefinitionKey());

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_FTL, "<p>Web Content Render</p>",
			LocaleUtil.US);

		JournalArticleDisplay journalArticleDisplay = null;

		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			journalArticleDisplay =
				_journalArticleLocalService.getArticleDisplay(
					journalArticle.getGroupId(), journalArticle.getArticleId(),
					ddmTemplate.getTemplateKey(), null, null,
					_getThemeDisplay());
		}

		String content = journalArticleDisplay.getContent();

		Assert.assertTrue(content.contains("Web Content Render"));
	}

	@Test
	public void testGetArticleDisplayWithSimpleData() throws Exception {
		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		JournalArticleDisplay journalArticleDisplay =
			_journalArticleLocalService.getArticleDisplay(
				journalArticle.getGroupId(), journalArticle.getArticleId(),
				null, null, _getThemeDisplay());

		String content = journalArticleDisplay.getContent();

		Assert.assertFalse(content.contains("Web Content Render"));
	}

	@Test
	public void testGetNoAssetArticles() throws Exception {
		JournalArticle article = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		AssetEntry assetEntry = _assetEntryLocalService.fetchEntry(
			JournalArticle.class.getName(), article.getResourcePrimKey());

		Assert.assertNotNull(assetEntry);

		_assetEntryLocalService.deleteAssetEntry(assetEntry);

		List<JournalArticle> articles =
			_journalArticleLocalService.getNoAssetArticles();

		for (JournalArticle curArticle : articles) {
			assetEntry = _assetEntryLocalService.fetchEntry(
				JournalArticle.class.getName(),
				curArticle.getResourcePrimKey());

			Assert.assertNull(assetEntry);

			_journalArticleLocalService.deleteJournalArticle(
				curArticle.getPrimaryKey());

			_resourceLocalService.deleteResource(
				curArticle.getCompanyId(), JournalArticle.class.getName(),
				ResourceConstants.SCOPE_INDIVIDUAL,
				curArticle.getResourcePrimKey());

			_ddmTemplateLinkLocalService.deleteTemplateLink(
				PortalUtil.getClassNameId(JournalArticle.class),
				curArticle.getPrimaryKey());
		}
	}

	@Test
	public void testGetNoPermissionArticles() throws Exception {
		JournalArticle article = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		List<ResourcePermission> resourcePermissions =
			_resourcePermissionLocalService.getResourcePermissions(
				article.getCompanyId(), JournalArticle.class.getName(),
				ResourceConstants.SCOPE_INDIVIDUAL,
				String.valueOf(article.getResourcePrimKey()));

		for (ResourcePermission resourcePermission : resourcePermissions) {
			_resourcePermissionLocalService.deleteResourcePermission(
				resourcePermission.getResourcePermissionId());
		}

		List<JournalArticle> articles =
			_journalArticleLocalService.getNoPermissionArticles();

		Assert.assertEquals(articles.toString(), 1, articles.size());
		Assert.assertEquals(article, articles.get(0));
	}

	@Test
	public void testRemoveArticleLocale() throws Exception {
		DataDefinition dataDefinition =
			DataDefinitionTestUtil.addDataDefinition(
				"journal", _dataDefinitionResourceFactory, _group.getGroupId(),
				_readFileToString("ddm_form.json"), TestPropsValues.getUser());

		FileEntry fileEntry = _dlAppLocalService.addFileEntry(
			null, TestPropsValues.getUserId(), _group.getGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			StringUtil.randomString(), ContentTypes.IMAGE_JPEG,
			FileUtil.getBytes(getClass(), "dependencies/image.jpg"), null, null,
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		JournalArticle journalArticle =
			JournalArticleLocalServiceUtil.addArticle(
				null, TestPropsValues.getUserId(), _group.getGroupId(), 0, 0,
				PortalUtil.getClassNameId(JournalArticle.class),
				StringPool.BLANK, true, 0,
				HashMapBuilder.put(
					LocaleUtil.SPAIN, "title-es"
				).put(
					LocaleUtil.US, "title"
				).build(),
				HashMapBuilder.put(
					LocaleUtil.SPAIN, "description-es"
				).put(
					LocaleUtil.US, "description"
				).build(),
				HashMapBuilder.put(
					LocaleUtil.SPAIN, "friendly-url-es"
				).put(
					LocaleUtil.US, "friendly-url"
				).build(),
				StringUtil.replace(
					_readFileToString(
						"journal_content_with_different_locales.xml"),
					"[$DOCUMENT_JSON$]", _toJSON(fileEntry)),
				dataDefinition.getDataDefinitionKey(), null, null, 1, 1, 1965,
				0, 0, 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 0, true, true, false,
				null, null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), TestPropsValues.getUserId()));

		journalArticle = _journalArticleLocalService.removeArticleLocale(
			_group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(),
			LocaleUtil.toLanguageId(LocaleUtil.SPAIN));

		Assert.assertEquals("title", journalArticle.getTitle(LocaleUtil.SPAIN));
		Assert.assertEquals(
			"description", journalArticle.getDescription(LocaleUtil.SPAIN));

		Map<Locale, String> friendlyURLMap = journalArticle.getFriendlyURLMap();

		Assert.assertNull(friendlyURLMap.get(LocaleUtil.SPAIN));

		DDMFormValues ddmFormValues = journalArticle.getDDMFormValues();

		Set<Locale> availableLocales = ddmFormValues.getAvailableLocales();

		Assert.assertEquals(
			availableLocales.toString(), 1, availableLocales.size());
		Assert.assertFalse(availableLocales.contains(LocaleUtil.SPAIN));

		_validateDDMFormFieldValues(ddmFormValues.getDDMFormFieldValues());
	}

	@Test
	public void testRemoveArticleLocaleWithNestedFields() throws Exception {
		DataDefinition dataDefinition =
			DataDefinitionTestUtil.addDataDefinition(
				"journal", _dataDefinitionResourceFactory, _group.getGroupId(),
				_readFileToString("ddm_form_nested_fields.json"),
				TestPropsValues.getUser());

		FileEntry fileEntry = _dlAppLocalService.addFileEntry(
			null, TestPropsValues.getUserId(), _group.getGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			StringUtil.randomString(), ContentTypes.IMAGE_JPEG,
			FileUtil.getBytes(getClass(), "dependencies/image.jpg"), null, null,
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		JournalArticle journalArticle =
			JournalArticleLocalServiceUtil.addArticle(
				null, TestPropsValues.getUserId(), _group.getGroupId(), 0, 0,
				PortalUtil.getClassNameId(JournalArticle.class),
				StringPool.BLANK, true, 0,
				HashMapBuilder.put(
					LocaleUtil.SPAIN, "title-es"
				).put(
					LocaleUtil.US, "title"
				).build(),
				HashMapBuilder.put(
					LocaleUtil.SPAIN, "description-es"
				).put(
					LocaleUtil.US, "description"
				).build(),
				HashMapBuilder.put(
					LocaleUtil.SPAIN, "friendly-url-es"
				).put(
					LocaleUtil.US, "friendly-url"
				).build(),
				StringUtil.replace(
					_readFileToString(
						"journal_content_nested_fields_with_different_" +
							"locales.xml"),
					"[$DOCUMENT_JSON$]", _toJSON(fileEntry)),
				dataDefinition.getDataDefinitionKey(), null, null, 1, 1, 1965,
				0, 0, 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 0, true, true, false,
				null, null, null, null,
				ServiceContextTestUtil.getServiceContext(
					_group.getGroupId(), TestPropsValues.getUserId()));

		journalArticle = _journalArticleLocalService.removeArticleLocale(
			_group.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(),
			LocaleUtil.toLanguageId(LocaleUtil.SPAIN));

		Assert.assertEquals("title", journalArticle.getTitle(LocaleUtil.SPAIN));
		Assert.assertEquals(
			"description", journalArticle.getDescription(LocaleUtil.SPAIN));

		Map<Locale, String> friendlyURLMap = journalArticle.getFriendlyURLMap();

		Assert.assertNull(friendlyURLMap.get(LocaleUtil.SPAIN));

		DDMFormValues ddmFormValues = journalArticle.getDDMFormValues();

		Set<Locale> availableLocales = ddmFormValues.getAvailableLocales();

		Assert.assertEquals(
			availableLocales.toString(), 1, availableLocales.size());
		Assert.assertFalse(availableLocales.contains(LocaleUtil.SPAIN));

		_validateDDMFormFieldValues(ddmFormValues.getDDMFormFieldValues());
	}

	@Test
	public void testUpdateArticleByNonownerUser() throws Exception {
		User ownerUser = UserTestUtil.addGroupUser(
			_group, RoleConstants.ADMINISTRATOR);

		PermissionChecker ownerPermissionChecker =
			PermissionCheckerFactoryUtil.create(ownerUser);

		PermissionChecker originalPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		try {
			PermissionThreadLocal.setPermissionChecker(ownerPermissionChecker);

			ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(
					_group.getCompanyId(), _group.getGroupId(),
					ownerUser.getUserId());

			JournalArticle journalArticle =
				JournalTestUtil.addArticleWithWorkflow(
					_group.getGroupId(), 0, RandomTestUtil.randomString(),
					RandomTestUtil.randomString(), true, serviceContext);

			_assertArticleUser(journalArticle, ownerUser, ownerUser);

			Role siteMemberRole = RoleLocalServiceUtil.getRole(
				_group.getCompanyId(), RoleConstants.SITE_MEMBER);

			_resourcePermissionLocalService.setResourcePermissions(
				_group.getCompanyId(), JournalArticle.class.getName(),
				ResourceConstants.SCOPE_INDIVIDUAL,
				String.valueOf(journalArticle.getResourcePrimKey()),
				siteMemberRole.getRoleId(),
				new String[] {
					ActionKeys.ADD_DISCUSSION, ActionKeys.VIEW,
					ActionKeys.UPDATE
				});

			User nonownerUser = UserTestUtil.addGroupUser(
				_group, RoleConstants.SITE_MEMBER);

			PermissionChecker nonownerPermissionChecker =
				PermissionCheckerFactoryUtil.create(nonownerUser);

			Assert.assertFalse(
				nonownerPermissionChecker.hasPermission(
					_group.getGroupId(), JournalArticle.class.getName(),
					String.valueOf(journalArticle.getResourcePrimKey()),
					ActionKeys.PERMISSIONS));
			Assert.assertTrue(
				nonownerPermissionChecker.hasPermission(
					_group.getGroupId(), JournalArticle.class.getName(),
					String.valueOf(journalArticle.getResourcePrimKey()),
					ActionKeys.UPDATE));

			PermissionThreadLocal.setPermissionChecker(
				nonownerPermissionChecker);

			serviceContext = ServiceContextTestUtil.getServiceContext(
				_group.getCompanyId(), _group.getGroupId(),
				nonownerPermissionChecker.getUserId());

			Double originalArticleVersion = journalArticle.getVersion();

			journalArticle = _journalArticleLocalService.updateArticle(
				nonownerUser.getUserId(), journalArticle.getGroupId(),
				journalArticle.getFolderId(), journalArticle.getArticleId(),
				journalArticle.getVersion(), journalArticle.getTitleMap(),
				journalArticle.getDescriptionMap(), journalArticle.getContent(),
				journalArticle.getLayoutUuid(), serviceContext);

			int versionComparison = Double.compare(
				originalArticleVersion, journalArticle.getVersion());

			Assert.assertTrue(versionComparison < 0);

			_assertArticleUser(journalArticle, ownerUser, nonownerUser);

			Assert.assertFalse(
				nonownerPermissionChecker.hasPermission(
					_group.getGroupId(), JournalArticle.class.getName(),
					String.valueOf(journalArticle.getResourcePrimKey()),
					ActionKeys.PERMISSIONS));
			Assert.assertTrue(
				nonownerPermissionChecker.hasPermission(
					_group.getGroupId(), JournalArticle.class.getName(),
					String.valueOf(journalArticle.getResourcePrimKey()),
					ActionKeys.UPDATE));
		}
		finally {
			PermissionThreadLocal.setPermissionChecker(
				originalPermissionChecker);
		}
	}

	@Test
	public void testUpdateDDMStructurePredefinedValues() throws Exception {
		Tuple tuple = _createJournalArticleWithPredefinedValues("Test Article");

		JournalArticle journalArticle = (JournalArticle)tuple.getObject(0);
		DDMStructure ddmStructure = (DDMStructure)tuple.getObject(1);

		DDMStructure actualDDMStructure = journalArticle.getDDMStructure();

		Assert.assertEquals(
			actualDDMStructure.getStructureId(), ddmStructure.getStructureId());

		Fields fields = _journalConverter.getDDMFields(
			ddmStructure, journalArticle.getContent());

		Field field = fields.get("name");

		Assert.assertNotNull(field);

		Assert.assertEquals(
			"Valor Predefinido", field.getValue(LocaleUtil.BRAZIL));
		Assert.assertEquals(
			"Valeur Prédéfinie", field.getValue(LocaleUtil.FRENCH));
		Assert.assertEquals(
			"Valor Predefinido", field.getValue(LocaleUtil.SPAIN));
		Assert.assertEquals("Predefined Value", field.getValue(LocaleUtil.US));

		Locale unavailableLocale = LocaleUtil.ITALY;

		Assert.assertEquals(
			"Predefined Value", field.getValue(unavailableLocale));
	}

	private FileEntry _addTempFileEntry(String fileName) throws Exception {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(
			"/com/liferay/journal/service/test/dependencies/images/" +
				fileName);

		return TempFileEntryUtil.addTempFileEntry(
			_group.getGroupId(), TestPropsValues.getUserId(),
			JournalArticle.class.getName(), fileName, inputStream,
			ContentTypes.IMAGE_JPEG);
	}

	private void _assertArticleFriendlyURLMap(Group group) throws Exception {
		JournalArticle journalArticle = JournalTestUtil.addArticle(
			group.getGroupId(), JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			Collections.emptyMap());

		Locale locale = _portal.getSiteDefaultLocale(group.getGroupId());

		String friendlyURL = _friendlyURLNormalizer.normalizeWithPeriods(
			StringBundler.concat(
				RandomTestUtil.randomString(5), StringPool.PERIOD,
				RandomTestUtil.randomString(5), StringPool.SLASH,
				RandomTestUtil.randomString(5)));

		Assert.assertTrue(friendlyURL.contains(StringPool.DASH));
		Assert.assertFalse(friendlyURL.contains(StringPool.PERIOD));
		Assert.assertTrue(friendlyURL.contains(StringPool.SLASH));

		Map<Locale, String> friendlyURLMap = journalArticle.getFriendlyURLMap();

		journalArticle = _updateJournalArticle(
			HashMapBuilder.put(
				locale, friendlyURL
			).build(),
			journalArticle);

		friendlyURLMap.put(locale, friendlyURL);

		Assert.assertEquals(friendlyURLMap, journalArticle.getFriendlyURLMap());
	}

	private void _assertArticleUser(
		JournalArticle journalArticle, User expectedOwnerUser,
		User expectedStatusByUser) {

		Assert.assertEquals(
			expectedOwnerUser.getUserId(), journalArticle.getUserId());
		Assert.assertEquals(
			expectedOwnerUser.getFullName(), journalArticle.getUserName());

		Assert.assertEquals(
			expectedStatusByUser.getUserId(),
			journalArticle.getStatusByUserId());
		Assert.assertEquals(
			expectedStatusByUser.getFullName(),
			journalArticle.getStatusByUserName());
	}

	private Tuple _createJournalArticleWithPredefinedValues(String title)
		throws Exception {

		Set<Locale> availableLocales = DDMFormTestUtil.createAvailableLocales(
			LocaleUtil.BRAZIL, LocaleUtil.FRENCH, LocaleUtil.SPAIN,
			LocaleUtil.US);

		DDMForm ddmForm = DDMFormTestUtil.createDDMForm(
			availableLocales, LocaleUtil.US);

		DDMFormField ddmFormField =
			DDMFormTestUtil.createLocalizableTextDDMFormField("name");

		LocalizedValue label = new LocalizedValue(LocaleUtil.US);

		label.addString(LocaleUtil.BRAZIL, "rótulo");
		label.addString(LocaleUtil.FRENCH, "étiquette");
		label.addString(LocaleUtil.SPAIN, "etiqueta");
		label.addString(LocaleUtil.US, "label");

		ddmFormField.setLabel(label);

		ddmForm.addDDMFormField(ddmFormField);

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), JournalArticle.class.getName(), ddmForm);

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_FTL,
			JournalTestUtil.getSampleTemplateFTL(), LocaleUtil.US);

		String content = DDMStructureTestUtil.getSampleStructuredContent(
			HashMapBuilder.put(
				LocaleUtil.BRAZIL, "Valor Predefinido"
			).put(
				LocaleUtil.FRENCH, "Valeur Prédéfinie"
			).put(
				LocaleUtil.SPAIN, "Valor Predefinido"
			).put(
				LocaleUtil.US, "Predefined Value"
			).build(),
			LocaleUtil.US.toString());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		JournalArticle article =
			_journalArticleLocalService.addArticleDefaultValues(
				serviceContext.getUserId(), serviceContext.getScopeGroupId(),
				_classNameLocalService.getClassNameId(DDMStructure.class),
				ddmStructure.getStructureId(),
				HashMapBuilder.put(
					LocaleUtil.US, title
				).build(),
				null, content, ddmStructure.getStructureKey(),
				ddmTemplate.getTemplateKey(), null, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, true, 0, 0, 0, 0, 0, true, true, false, null, null,
				serviceContext);

		return new Tuple(article, ddmStructure);
	}

	private ThemeDisplay _getThemeDisplay() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(
			_companyLocalService.getCompany(_group.getCompanyId()));

		Layout layout = _layoutLocalService.addLayout(
			TestPropsValues.getUserId(), _group.getGroupId(), false,
			LayoutConstants.DEFAULT_PARENT_LAYOUT_ID,
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			StringPool.BLANK, LayoutConstants.TYPE_PORTLET, false,
			StringPool.BLANK,
			ServiceContextTestUtil.getServiceContext(
				TestPropsValues.getGroupId(), TestPropsValues.getUserId()));

		themeDisplay.setLayout(layout);

		LayoutSet layoutSet = layout.getLayoutSet();

		themeDisplay.setLayoutSet(layoutSet);

		themeDisplay.setLayoutTypePortlet(
			(LayoutTypePortlet)layout.getLayoutType());
		themeDisplay.setLocale(LocaleUtil.getSiteDefault());
		themeDisplay.setLookAndFeel(
			layoutSet.getTheme(), layoutSet.getColorScheme());
		themeDisplay.setPermissionChecker(
			PermissionThreadLocal.getPermissionChecker());
		themeDisplay.setRealUser(TestPropsValues.getUser());

		HttpServletRequest httpServletRequest = new MockHttpServletRequest();

		httpServletRequest.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);

		themeDisplay.setRequest(httpServletRequest);

		themeDisplay.setResponse(new MockHttpServletResponse());
		themeDisplay.setScopeGroupId(_group.getGroupId());
		themeDisplay.setSiteGroupId(_group.getGroupId());
		themeDisplay.setTimeZone(TimeZoneUtil.getDefault());
		themeDisplay.setUser(TestPropsValues.getUser());

		return themeDisplay;
	}

	private String _readFileToString(String fileName) throws Exception {
		return new String(
			FileUtil.getBytes(getClass(), "dependencies/" + fileName));
	}

	private String _toJSON(FileEntry fileEntry) {
		return JSONUtil.put(
			"alt", StringPool.BLANK
		).put(
			"description", StringPool.BLANK
		).put(
			"fileEntryId", fileEntry.getFileEntryId()
		).put(
			"groupId", fileEntry.getGroupId()
		).put(
			"name", fileEntry.getFileName()
		).put(
			"title", fileEntry.getTitle()
		).put(
			"type", "journal"
		).put(
			"uuid", fileEntry.getUuid()
		).toString();
	}

	private JournalArticle _updateJournalArticle(
			Map<Locale, String> friendlyURLMap, JournalArticle journalArticle)
		throws Exception {

		Calendar calendar = CalendarFactoryUtil.getCalendar();

		calendar.setTime(journalArticle.getDisplayDate());

		int displayDateMonth = calendar.get(Calendar.MONTH);
		int displayDateDay = calendar.get(Calendar.DATE);
		int displayDateYear = calendar.get(Calendar.YEAR);
		int displayDateHour = calendar.get(Calendar.HOUR);
		int displayDateMinute = calendar.get(Calendar.MINUTE);

		if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
			displayDateHour += 12;
		}

		return _journalArticleLocalService.updateArticle(
			TestPropsValues.getUserId(), journalArticle.getGroupId(),
			journalArticle.getFolderId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), journalArticle.getTitleMap(),
			journalArticle.getDescriptionMap(), friendlyURLMap,
			journalArticle.getContent(), journalArticle.getDDMStructureKey(),
			journalArticle.getDDMTemplateKey(), journalArticle.getLayoutUuid(),
			displayDateMonth, displayDateDay, displayDateYear, displayDateHour,
			displayDateMinute, 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 0, true,
			journalArticle.isIndexable(), false, null, null, null, null,
			ServiceContextTestUtil.getServiceContext(
				journalArticle.getGroupId(), TestPropsValues.getUserId()));
	}

	private void _validateDDMFormFieldValues(
		List<DDMFormFieldValue> ddmFormFieldValues) {

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			Value value = ddmFormFieldValue.getValue();

			if (value != null) {
				Set<Locale> valueAvailableLocales = value.getAvailableLocales();

				Assert.assertEquals(
					valueAvailableLocales.toString(), 1,
					valueAvailableLocales.size());
				Assert.assertFalse(
					valueAvailableLocales.contains(LocaleUtil.SPAIN));

				Assert.assertEquals(
					value.getString(value.getDefaultLocale()),
					value.getString(LocaleUtil.SPAIN));
			}

			if (ListUtil.isNotEmpty(
					ddmFormFieldValue.getNestedDDMFormFieldValues())) {

				_validateDDMFormFieldValues(
					ddmFormFieldValue.getNestedDDMFormFieldValues());
			}
		}
	}

	private void _validateDDMFormValuesImages(JournalArticle journalArticle)
		throws Exception {

		DDMFormValues ddmFormValues = journalArticle.getDDMFormValues();

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			ddmFormValues.getDDMFormFieldValuesMap(true);

		for (Map.Entry<String, List<DDMFormFieldValue>> entry :
				ddmFormFieldValuesMap.entrySet()) {

			List<DDMFormFieldValue> ddmFormFieldValues = entry.getValue();

			for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
				if (!Objects.equals(
						DDMFormFieldTypeConstants.IMAGE,
						ddmFormFieldValue.getType())) {

					continue;
				}

				Value value = ddmFormFieldValue.getValue();

				for (Locale locale : value.getAvailableLocales()) {
					JSONObject jsonObject = _jsonFactory.createJSONObject(
						value.getString(locale));

					Assert.assertEquals(
						journalArticle.getResourcePrimKey(),
						jsonObject.getLong("resourcePrimKey"));
				}
			}
		}
	}

	@Inject(
		filter = "model.class.name=com.liferay.journal.model.JournalArticle"
	)
	private static ModelResourcePermission<JournalArticle>
		_journalArticleModelResourcePermission;

	@Inject
	private AssetDisplayPageEntryLocalService
		_assetDisplayPageEntryLocalService;

	@Inject
	private AssetEntryLocalService _assetEntryLocalService;

	@Inject
	private ClassNameLocalService _classNameLocalService;

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private DataDefinitionResource.Factory _dataDefinitionResourceFactory;

	@Inject
	private DDMStructureLocalService _ddmStructureLocalService;

	@Inject
	private DDMTemplateLinkLocalService _ddmTemplateLinkLocalService;

	@Inject
	private DLAppLocalService _dlAppLocalService;

	@Inject
	private FriendlyURLNormalizer _friendlyURLNormalizer;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private GroupLocalService _groupLocalService;

	@Inject
	private JournalArticleLocalService _journalArticleLocalService;

	@Inject
	private JournalConverter _journalConverter;

	@Inject
	private JSONFactory _jsonFactory;

	@Inject
	private LayoutLocalService _layoutLocalService;

	@Inject
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Inject
	private Portal _portal;

	@Inject
	private ResourceLocalService _resourceLocalService;

	@Inject
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Inject
	private ResourcePermissionService _resourcePermissionService;

	@Inject
	private RoleLocalService _roleLocalService;

	@Inject
	private StagingLocalService _stagingLocalService;

	private ThemeDisplay _themeDisplay;

	@Inject
	private UserLocalService _userLocalService;

}