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
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.dynamic.data.mapping.exception.RequiredTemplateException;
import com.liferay.dynamic.data.mapping.exception.StorageFieldRequiredException;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMTemplateTestUtil;
import com.liferay.journal.constants.JournalArticleConstants;
import com.liferay.journal.constants.JournalFolderConstants;
import com.liferay.journal.exception.NoSuchArticleException;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.journal.util.comparator.ArticleVersionComparator;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactory;
import com.liferay.portal.kernel.service.PortalPreferencesLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Juan Fernández
 * @author Roberto Díaz
 */
@RunWith(Arquillian.class)
public class JournalArticleServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_article = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, "Version 1",
			"This is a test article.");

		PortalPreferences portalPreferences =
			_portletPreferencesFactory.getPortalPreferences(
				TestPropsValues.getUserId(), true);

		_originalPortalPreferencesXML = _portletPreferencesFactory.toXML(
			portalPreferences);

		portalPreferences.setValue(
			"", "expireAllArticleVersionsEnabled", "true");

		_portalPreferencesLocalService.updatePreferences(
			TestPropsValues.getCompanyId(),
			PortletKeys.PREFS_OWNER_TYPE_COMPANY,
			_portletPreferencesFactory.toXML(portalPreferences));
	}

	@After
	public void tearDown() throws Exception {
		if (_article != null) {
			_journalArticleLocalService.deleteArticle(
				_group.getGroupId(), _article.getArticleId(),
				new ServiceContext());
		}

		_portalPreferencesLocalService.updatePreferences(
			TestPropsValues.getCompanyId(),
			PortletKeys.PREFS_OWNER_TYPE_COMPANY,
			_originalPortalPreferencesXML);
	}

	@Test
	public void testAddArticle() {
		Assert.assertEquals(
			"Version 1", _article.getTitle(LocaleUtil.getDefault()));
		Assert.assertTrue(_article.isApproved());
		Assert.assertEquals(1.0, _article.getVersion(), 0);
	}

	@Test(expected = StorageFieldRequiredException.class)
	public void testAddArticleWithEmptyRequiredHTMLField() throws Exception {
		testAddArticleRequiredFields(
			"test-ddm-structure-html-required-field.xml",
			"test-journal-content-html-empty-required-field.xml",
			HashMapBuilder.put(
				"HTML2030", ""
			).build());
	}

	@Test
	public void testAddArticleWithExternalReferenceCode() throws Exception {
		String externalReferenceCode = RandomTestUtil.randomString();

		_article = JournalTestUtil.addArticle(
			externalReferenceCode, _group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, StringPool.BLANK,
			true);

		Assert.assertNotNull(_article);
		Assert.assertEquals(
			externalReferenceCode, _article.getExternalReferenceCode());

		_latestArticle =
			_journalArticleService.fetchLatestArticleByExternalReferenceCode(
				_group.getGroupId(), externalReferenceCode);

		Assert.assertNotNull(_latestArticle);
		Assert.assertEquals(
			externalReferenceCode, _latestArticle.getExternalReferenceCode());
	}

	@Test
	public void testAddArticleWithNotEmptyRequiredHTMLField() throws Exception {
		testAddArticleRequiredFields(
			"test-ddm-structure-html-required-field.xml",
			"test-journal-content-html-required-field.xml",
			HashMapBuilder.put(
				"HTML2030", "<p>Hello.</p>"
			).build());
	}

	@Test
	public void testAddArticleWithoutExternalReferenceCode() throws Exception {
		String articleId = StringUtil.toUpperCase(
			RandomTestUtil.randomString());

		_article = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, articleId, false);

		String externalReferenceCode = _article.getExternalReferenceCode();

		Assert.assertNotNull(_article);
		Assert.assertEquals(articleId, _article.getArticleId());
		Assert.assertEquals(externalReferenceCode, _article.getUuid());

		_latestArticle =
			_journalArticleService.fetchLatestArticleByExternalReferenceCode(
				_group.getGroupId(), externalReferenceCode);

		Assert.assertNotNull(_latestArticle);
		Assert.assertEquals(_article, _latestArticle);
	}

	@Test
	public void testDeleteTemplateReferencedByJournalArticles()
		throws Exception {

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), JournalArticle.class.getName());

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class));

		JournalTestUtil.addArticleWithXMLContent(
			_group.getGroupId(), "<title>Test Article</title>",
			ddmStructure.getStructureKey(), ddmTemplate.getTemplateKey());

		try {
			_ddmTemplateLocalService.deleteTemplate(
				ddmTemplate.getTemplateId());

			Assert.fail();
		}
		catch (RequiredTemplateException requiredTemplateException) {
		}
	}

	@Test
	public void testExpireArticle() throws Exception {
		updateAndExpireLatestArticle("Version 2");

		Assert.assertEquals(
			"Version 2", _article.getTitle(LocaleUtil.getDefault()));
		Assert.assertTrue(_article.isExpired());
		Assert.assertEquals(1.1, _article.getVersion(), 0);
	}

	@Test
	public void testFetchLatestArticleByApprovedStatusWhenArticleApproved()
		throws Exception {

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		_latestArticle = fetchLatestArticle(WorkflowConstants.STATUS_APPROVED);

		Assert.assertEquals(
			"Version 2", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertTrue(_latestArticle.isApproved());
		Assert.assertTrue(_latestArticle.getVersion() == 1.1);
	}

	@Test
	public void testFetchLatestArticleByApprovedStatusWhenArticleExpired()
		throws Exception {

		updateAndExpireArticle();

		_latestArticle = fetchLatestArticle(WorkflowConstants.STATUS_APPROVED);

		Assert.assertNull(_latestArticle);
	}

	@Test
	public void testFetchLatestArticleByApprovedStatusWhenFirstArticleExpired()
		throws Exception {

		JournalTestUtil.updateArticle(_article, "Version 2");

		_article = JournalTestUtil.expireArticle(
			_group.getGroupId(), _article, 1.0);

		_latestArticle = fetchLatestArticle(WorkflowConstants.STATUS_APPROVED);

		Assert.assertEquals(
			"Version 2", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertTrue(_latestArticle.isApproved());
		Assert.assertTrue(_latestArticle.getVersion() == 1.1);
	}

	@Test
	public void testFetchLatestArticleByDraftStatusWhenNoDraftArticle()
		throws Exception {

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		_latestArticle = fetchLatestArticle(WorkflowConstants.STATUS_DRAFT);

		Assert.assertNull(_latestArticle);
	}

	@Test
	public void testFetchLatestArticleByExternalReferenceCode()
		throws Exception {

		long groupId = _article.getGroupId();
		String externalReferenceCode = _article.getExternalReferenceCode();

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		_latestArticle =
			_journalArticleService.fetchLatestArticleByExternalReferenceCode(
				groupId, externalReferenceCode);

		Assert.assertEquals(
			"Version 2", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertEquals(1.1, _latestArticle.getVersion(), 0);
		Assert.assertEquals(
			externalReferenceCode, _latestArticle.getExternalReferenceCode());
	}

	@Test
	public void testFetchLatestArticleByNonexistentExternalReferenceCode()
		throws Exception {

		_latestArticle =
			_journalArticleService.fetchLatestArticleByExternalReferenceCode(
				_article.getGroupId(), RandomTestUtil.randomString());

		Assert.assertNull(_latestArticle);
	}

	@Test
	public void testFetchLatestArticleExpiredWithStatusAny() throws Exception {
		updateAndExpireLatestArticle("Version 2");

		_latestArticle = fetchLatestArticle(
			WorkflowConstants.STATUS_ANY, false);

		Assert.assertTrue(_latestArticle.isExpired());
		Assert.assertEquals(
			"Version 2", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertEquals(1.1, _latestArticle.getVersion(), 0);
	}

	@Test
	public void testFetchLatestArticleExpiredWithStatusApproved()
		throws Exception {

		updateAndExpireLatestArticle("Version 2");

		_latestArticle = fetchLatestArticle(
			WorkflowConstants.STATUS_APPROVED, false);

		Assert.assertTrue(_latestArticle.isApproved());
		Assert.assertEquals(
			"Version 1", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertEquals(1.0, _latestArticle.getVersion(), 0);
	}

	@Test
	public void testFetchLatestArticleExpiredWithStatusExpired()
		throws Exception {

		updateAndExpireLatestArticle("Version 2");

		_latestArticle = fetchLatestArticle(
			WorkflowConstants.STATUS_EXPIRED, false);

		Assert.assertTrue(_latestArticle.isExpired());
		Assert.assertEquals(
			"Version 2", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertEquals(1.1, _latestArticle.getVersion(), 0);
	}

	@Test
	public void testFetchLatestArticleNotExpiredWithStatusAny()
		throws Exception {

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		_latestArticle = fetchLatestArticle(
			WorkflowConstants.STATUS_ANY, false);

		Assert.assertTrue(_latestArticle.isApproved());
		Assert.assertEquals(
			"Version 2", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertEquals(1.1, _latestArticle.getVersion(), 0);
	}

	@Test
	public void testFetchLatestArticleNotExpiredWithStatusApproved()
		throws Exception {

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		_latestArticle = fetchLatestArticle(
			WorkflowConstants.STATUS_APPROVED, false);

		Assert.assertTrue(_latestArticle.isApproved());
		Assert.assertEquals(
			"Version 2", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertEquals(1.1, _latestArticle.getVersion(), 0);
	}

	@Test
	public void testFetchLatestArticleNotExpiredWithStatusExpired()
		throws Exception {

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		_latestArticle = fetchLatestArticle(
			WorkflowConstants.STATUS_EXPIRED, false);

		Assert.assertNull(_latestArticle);
	}

	@Test
	public void testGetArticlesById() throws Exception {
		List<JournalArticle> expectedArticles = new ArrayList<>();

		JournalArticle article = JournalTestUtil.addArticleWithWorkflow(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			RandomTestUtil.randomString(), RandomTestUtil.randomString(), true);

		expectedArticles.add(article);

		article = updateArticleStatus(
			article, WorkflowConstants.STATUS_APPROVED);

		expectedArticles.add(article);

		article = updateArticleStatus(article, WorkflowConstants.STATUS_DRAFT);

		expectedArticles.add(article);

		int actualCount = _journalArticleService.getArticlesCountByArticleId(
			_group.getGroupId(), article.getArticleId());

		Assert.assertEquals(expectedArticles.size(), actualCount);

		List<JournalArticle> articles =
			_journalArticleService.getArticlesByArticleId(
				_group.getGroupId(), article.getArticleId(), QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, new ArticleVersionComparator(true));

		Assert.assertEquals(
			articles.toString(), expectedArticles.size(), articles.size());

		Assert.assertEquals(expectedArticles, articles);
	}

	@Test
	public void testGetArticlesByIdAndStatus() throws Exception {
		List<JournalArticle> expectedArticles = new ArrayList<>();

		JournalArticle article = JournalTestUtil.addArticleWithWorkflow(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			RandomTestUtil.randomString(), RandomTestUtil.randomString(), true);

		expectedArticles.add(article);

		article = updateArticleStatus(
			article, WorkflowConstants.STATUS_APPROVED);

		expectedArticles.add(article);

		updateArticleStatus(article, WorkflowConstants.STATUS_DRAFT);

		int actualCount = _journalArticleService.getArticlesCountByArticleId(
			_group.getGroupId(), article.getArticleId(),
			WorkflowConstants.STATUS_APPROVED);

		Assert.assertEquals(expectedArticles.size(), actualCount);

		List<JournalArticle> articles =
			_journalArticleService.getArticlesByArticleId(
				_group.getGroupId(), article.getArticleId(),
				WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, new ArticleVersionComparator(true));

		Assert.assertEquals(
			articles.toString(), expectedArticles.size(), articles.size());

		Assert.assertEquals(expectedArticles, articles);
	}

	@Test
	public void testGetGroupArticlesWhenUserNotNullAndStatusAny()
		throws Exception {

		List<JournalArticle> expectedArticles = addArticles(
			2, RandomTestUtil.randomString());

		_article = updateArticleStatus(
			_article, WorkflowConstants.STATUS_DRAFT);

		expectedArticles.add(_article);

		int count = _journalArticleService.getGroupArticlesCount(
			_group.getGroupId(), _article.getUserId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		Assert.assertEquals(3, count);

		List<JournalArticle> articles = _journalArticleService.getGroupArticles(
			_group.getGroupId(), _article.getUserId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);

		Assert.assertEquals(expectedArticles, articles);
	}

	@Test
	public void testGetGroupArticlesWhenUserNotNullAndStatusApproved()
		throws Exception {

		List<JournalArticle> expectedArticles = addArticles(
			2, RandomTestUtil.randomString());

		expectedArticles.add(0, _article);

		_article = updateArticleStatus(
			_article, WorkflowConstants.STATUS_DRAFT);

		int count = _journalArticleService.getGroupArticlesCount(
			_group.getGroupId(), _article.getUserId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			WorkflowConstants.STATUS_APPROVED);

		Assert.assertEquals(3, count);

		List<JournalArticle> articles = _journalArticleService.getGroupArticles(
			_group.getGroupId(), _article.getUserId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);

		Assert.assertEquals(expectedArticles, articles);
	}

	@Test
	public void testGetGroupArticlesWhenUserNullAndStatusAny()
		throws Exception {

		List<JournalArticle> expectedArticles = addArticles(
			2, RandomTestUtil.randomString());

		_article = updateArticleStatus(
			_article, WorkflowConstants.STATUS_DRAFT);

		expectedArticles.add(_article);

		int count = _journalArticleService.getGroupArticlesCount(
			_group.getGroupId(), 0,
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		Assert.assertEquals(3, count);

		List<JournalArticle> articles = _journalArticleService.getGroupArticles(
			_group.getGroupId(), 0,
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);

		Assert.assertEquals(expectedArticles, articles);
	}

	@Test
	public void testGetGroupArticlesWhenUserNullAndStatusApproved()
		throws Exception {

		List<JournalArticle> expectedArticles = addArticles(
			2, RandomTestUtil.randomString());

		expectedArticles.add(0, _article);

		_article = updateArticleStatus(
			_article, WorkflowConstants.STATUS_DRAFT);

		int count = _journalArticleService.getGroupArticlesCount(
			_group.getGroupId(), 0,
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			WorkflowConstants.STATUS_APPROVED);

		Assert.assertEquals(3, count);

		List<JournalArticle> articles = _journalArticleService.getGroupArticles(
			_group.getGroupId(), 0,
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);

		Assert.assertEquals(expectedArticles, articles);
	}

	@Test
	public void testGetLatestArticleByExternalReferenceCode() throws Exception {
		long groupId = _article.getGroupId();
		String externalReferenceCode = _article.getExternalReferenceCode();

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		_latestArticle =
			_journalArticleService.getLatestArticleByExternalReferenceCode(
				groupId, externalReferenceCode);

		Assert.assertEquals(
			"Version 2", _latestArticle.getTitle(LocaleUtil.getDefault()));
		Assert.assertEquals(1.1, _latestArticle.getVersion(), 0);
		Assert.assertEquals(
			externalReferenceCode, _latestArticle.getExternalReferenceCode());
	}

	@Test(expected = NoSuchArticleException.class)
	public void testGetLatestArticleByNonexistentExternalReferenceCode()
		throws Exception {

		_journalArticleService.getLatestArticleByExternalReferenceCode(
			_article.getGroupId(), RandomTestUtil.randomString());
	}

	@Test
	public void testGetLatestArticlesByStatus() throws Exception {
		List<JournalArticle> articles = addArticles(
			1, RandomTestUtil.randomString());

		articles.add(0, _article);

		int count = _journalArticleService.getLatestArticlesCount(
			_group.getGroupId(), WorkflowConstants.STATUS_APPROVED);

		Assert.assertEquals(2, count);

		Assert.assertEquals(
			_journalArticleService.getLatestArticles(
				_group.getGroupId(), WorkflowConstants.STATUS_APPROVED,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null),
			articles);

		_article = updateArticleStatus(
			_article, WorkflowConstants.STATUS_DRAFT);

		int draftCount = _journalArticleService.getLatestArticlesCount(
			_group.getGroupId(), WorkflowConstants.STATUS_DRAFT);

		Assert.assertEquals(1, draftCount);

		List<JournalArticle> draftArticles =
			_journalArticleService.getLatestArticles(
				_group.getGroupId(), WorkflowConstants.STATUS_DRAFT,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		Assert.assertEquals(_article, draftArticles.get(0));
	}

	@Test
	public void testUpdateArticle() throws Exception {
		_article.setDisplayDate(new Date());

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		Assert.assertEquals(
			"Version 2", _article.getTitle(LocaleUtil.getDefault()));
		Assert.assertTrue(_article.isApproved());
		Assert.assertEquals(1.1, _article.getVersion(), 0);

		AssetEntry assetEntry = _assetEntryLocalService.getEntry(
			_article.getModelClassName(), _article.getResourcePrimKey());

		Assert.assertEquals(
			_article.getDisplayDate(), assetEntry.getPublishDate());
	}

	@Test
	public void testUpdateExpiredArticle() throws Exception {
		_article = JournalTestUtil.expireArticle(
			_group.getGroupId(), _article, _article.getVersion());

		Assert.assertTrue(_article.isExpired());

		_article.setDisplayDate(new Date());

		_article = JournalTestUtil.updateArticle(_article, "Version 2");

		Assert.assertEquals(
			"Version 2", _article.getTitle(LocaleUtil.getDefault()));
		Assert.assertTrue(_article.isApproved());
		Assert.assertEquals(1.1, _article.getVersion(), 0);

		AssetEntry assetEntry = _assetEntryLocalService.getEntry(
			_article.getModelClassName(), _article.getResourcePrimKey());

		Assert.assertEquals(
			_article.getDisplayDate(), assetEntry.getPublishDate());
	}

	protected List<JournalArticle> addArticles(int count, String content)
		throws Exception {

		List<JournalArticle> articles = new ArrayList<>(count);

		for (int i = 0; i < count; i++) {
			JournalArticle article = JournalTestUtil.addArticle(
				_group.getGroupId(),
				JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				RandomTestUtil.randomString(), content);

			articles.add(article);
		}

		return articles;
	}

	protected JournalArticle fetchLatestArticle(int status) {
		return _journalArticleLocalService.fetchLatestArticle(
			_group.getGroupId(), _article.getArticleId(), status);
	}

	protected JournalArticle fetchLatestArticle(
		int status, boolean preferApproved) {

		return _journalArticleLocalService.fetchLatestArticle(
			_article.getResourcePrimKey(), status, preferApproved);
	}

	protected String readText(String fileName) throws Exception {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(
			"com/liferay/journal/dependencies/" + fileName);

		return StringUtil.read(inputStream);
	}

	protected void testAddArticleRequiredFields(
			String ddmStructureDefinition, String journalArticleContent,
			Map<String, String> requiredFields)
		throws Exception {

		String definition = readText(ddmStructureDefinition);

		DDMFormDeserializerDeserializeRequest.Builder builder =
			DDMFormDeserializerDeserializeRequest.Builder.newBuilder(
				definition);

		DDMFormDeserializerDeserializeResponse
			ddmFormDeserializerDeserializeResponse =
				_ddmFormDeserializer.deserialize(builder.build());

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), JournalArticle.class.getName(),
			ddmFormDeserializerDeserializeResponse.getDDMForm());

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class));

		String xmlContent = readText(journalArticleContent);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		for (Map.Entry<String, String> entry : requiredFields.entrySet()) {
			Assert.assertTrue(ddmStructure.getFieldRequired(entry.getKey()));

			serviceContext.setAttribute(entry.getKey(), entry.getValue());
		}

		JournalTestUtil.addArticleWithXMLContent(
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASS_NAME_ID_DEFAULT, xmlContent,
			ddmStructure.getStructureKey(), ddmTemplate.getTemplateKey(),
			LocaleUtil.fromLanguageId(ddmStructure.getDefaultLanguageId()),
			serviceContext);
	}

	protected void updateAndExpireArticle() throws Exception {
		JournalTestUtil.updateArticle(_article, "Version 2");

		JournalTestUtil.expireArticle(_group.getGroupId(), _article);
	}

	protected void updateAndExpireLatestArticle(String title) throws Exception {
		JournalTestUtil.updateArticle(_article, title);

		_article = JournalTestUtil.expireArticle(
			_group.getGroupId(), _article, 1.1);
	}

	protected JournalArticle updateArticleStatus(
			JournalArticle article, int status)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext();

		if (status == WorkflowConstants.STATUS_DRAFT) {
			serviceContext.setWorkflowAction(
				WorkflowConstants.ACTION_SAVE_DRAFT);
		}
		else {
			serviceContext.setWorkflowAction(WorkflowConstants.ACTION_PUBLISH);
		}

		return JournalTestUtil.updateArticle(
			article, "Version 2", article.getContent(), false, true,
			serviceContext);
	}

	@Inject
	private static JournalArticleLocalService _journalArticleLocalService;

	private JournalArticle _article;

	@Inject
	private AssetEntryLocalService _assetEntryLocalService;

	@Inject(filter = "ddm.form.deserializer.type=xsd")
	private DDMFormDeserializer _ddmFormDeserializer;

	@Inject
	private DDMTemplateLocalService _ddmTemplateLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private JournalArticleService _journalArticleService;

	private JournalArticle _latestArticle;
	private String _originalPortalPreferencesXML;

	@Inject
	private PortalPreferencesLocalService _portalPreferencesLocalService;

	@Inject
	private PortletPreferencesFactory _portletPreferencesFactory;

}