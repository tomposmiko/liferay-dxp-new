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

package com.liferay.content.dashboard.web.internal.portlet.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMTemplateTestUtil;
import com.liferay.journal.constants.JournalArticleConstants;
import com.liferay.journal.constants.JournalFolderConstants;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.constants.LanguageConstants;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.portlet.bridges.mvc.constants.MVCRenderConstants;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletRenderRequest;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletRenderResponse;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletURL;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import javax.portlet.Portlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Cristina González
 */
@RunWith(Arquillian.class)
@Sync
public class ContentDashboardAdminPortletTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_company = CompanyTestUtil.addCompany();

		_permissionChecker = PermissionThreadLocal.getPermissionChecker();

		_user = UserTestUtil.getAdminUser(_company.getCompanyId());

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_user));
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		PermissionThreadLocal.setPermissionChecker(_permissionChecker);

		_companyLocalService.deleteCompany(_company);
	}

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup(
			_company.getCompanyId(), _user.getUserId(), 0);
	}

	@Test
	public void testGetAuditGraphTitle() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _company.getGroupId(),
				_user.getUserId());

		AssetVocabulary assetVocabulary =
			_assetVocabularyLocalService.fetchGroupVocabulary(
				serviceContext.getScopeGroupId(), "audience");

		AssetCategory assetCategory = _assetCategoryLocalService.addCategory(
			_user.getUserId(), _company.getGroupId(),
			RandomTestUtil.randomString(), assetVocabulary.getVocabularyId(),
			serviceContext);

		AssetVocabulary childAssetVocabulary =
			_assetVocabularyLocalService.fetchGroupVocabulary(
				serviceContext.getScopeGroupId(), "stage");

		AssetCategory childAssetCategory =
			_assetCategoryLocalService.addCategory(
				_user.getUserId(), _company.getGroupId(),
				RandomTestUtil.randomString(),
				childAssetVocabulary.getVocabularyId(), serviceContext);

		try {
			JournalArticle journalArticle = JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);

			_journalArticleLocalService.updateAsset(
				_user.getUserId(), journalArticle,
				new long[] {
					assetCategory.getCategoryId(),
					childAssetCategory.getCategoryId()
				},
				new String[0], new long[0], null);

			Assert.assertEquals(
				String.format(
					"Content per %s and %s",
					assetVocabulary.getTitle(LocaleUtil.US),
					childAssetVocabulary.getTitle(LocaleUtil.US)),
				_getAuditGraphTitle(_getMockLiferayPortletRenderRequest()));
		}
		finally {
			_assetCategoryLocalService.deleteAssetCategory(assetCategory);
			_assetCategoryLocalService.deleteAssetCategory(childAssetCategory);
		}
	}

	@Test
	public void testGetAuditGraphTitleWithMissingChildAssetVocabularies()
		throws Exception {

		JournalTestUtil.addArticle(_user.getUserId(), _group.getGroupId(), 0);

		Assert.assertEquals(
			"Content",
			_getAuditGraphTitle(_getMockLiferayPortletRenderRequest()));
	}

	@Test
	public void testGetAuditGraphTitleWithMissingChildAssetVocabulary()
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _company.getGroupId(),
				_user.getUserId());

		AssetVocabulary assetVocabulary =
			_assetVocabularyLocalService.fetchGroupVocabulary(
				serviceContext.getScopeGroupId(), "audience");

		AssetCategory assetCategory = _assetCategoryLocalService.addCategory(
			_user.getUserId(), _company.getGroupId(),
			RandomTestUtil.randomString(), assetVocabulary.getVocabularyId(),
			serviceContext);

		try {
			JournalArticle journalArticle = JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);

			_journalArticleLocalService.updateAsset(
				_user.getUserId(), journalArticle,
				new long[] {assetCategory.getCategoryId()}, new String[0],
				new long[0], null);

			Assert.assertEquals(
				String.format(
					"Content per %s", assetVocabulary.getTitle(LocaleUtil.US)),
				_getAuditGraphTitle(_getMockLiferayPortletRenderRequest()));
		}
		finally {
			_assetCategoryLocalService.deleteAssetCategory(assetCategory);
		}
	}

	@Test
	public void testGetContextWithLtrLanguageDirection() throws Exception {
		Map<String, Object> data = _getData(
			_getMockLiferayPortletRenderRequest());

		Map<String, Object> context = (Map<String, Object>)data.get("context");

		Assert.assertNotNull(context);
		Assert.assertEquals(
			LanguageConstants.VALUE_LTR, context.get("languageDirection"));
	}

	@Test
	public void testGetContextWithRtlLanguageDirection() throws Exception {
		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			_getMockLiferayPortletRenderRequest(
				LocaleUtil.fromLanguageId("ar_SA"));

		Map<String, Object> data = _getData(mockLiferayPortletRenderRequest);

		Map<String, Object> context = (Map<String, Object>)data.get("context");

		Assert.assertNotNull(context);
		Assert.assertEquals(
			LanguageConstants.VALUE_RTL, context.get("languageDirection"));
	}

	@Test
	public void testGetOnClickConfiguration() throws Exception {
		MVCPortlet mvcPortlet = (MVCPortlet)_portlet;

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			_getMockLiferayPortletRenderRequest();

		mvcPortlet.render(
			mockLiferayPortletRenderRequest,
			new MockLiferayPortletRenderResponse());

		String onClickConfiguration = ReflectionTestUtil.invoke(
			mockLiferayPortletRenderRequest.getAttribute(
				"CONTENT_DASHBOARD_ADMIN_DISPLAY_CONTEXT"),
			"getOnClickConfiguration", new Class<?>[0]);

		Assert.assertTrue(
			onClickConfiguration.contains(
				HtmlUtil.escapeJS(
					"mvcRenderCommandName=" +
						"/edit_content_dashboard_configuration")));
	}

	@Test
	public void testGetProps() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _company.getGroupId(),
				_user.getUserId());

		AssetVocabulary assetVocabulary =
			_assetVocabularyLocalService.fetchGroupVocabulary(
				serviceContext.getScopeGroupId(), "audience");

		AssetCategory assetCategory = _assetCategoryLocalService.addCategory(
			_user.getUserId(), _company.getGroupId(),
			RandomTestUtil.randomString(), assetVocabulary.getVocabularyId(),
			serviceContext);

		AssetVocabulary childAssetVocabulary =
			_assetVocabularyLocalService.fetchGroupVocabulary(
				serviceContext.getScopeGroupId(), "stage");

		AssetCategory childAssetCategory =
			_assetCategoryLocalService.addCategory(
				_user.getUserId(), _company.getGroupId(),
				RandomTestUtil.randomString(),
				childAssetVocabulary.getVocabularyId(), serviceContext);

		try {
			JournalArticle journalArticle = JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);

			_journalArticleLocalService.updateAsset(
				_user.getUserId(), journalArticle,
				new long[] {
					assetCategory.getCategoryId(),
					childAssetCategory.getCategoryId()
				},
				new String[0], new long[0], null);

			Map<String, Object> data = _getData(
				_getMockLiferayPortletRenderRequest());

			Map<String, Object> props = (Map<String, Object>)data.get("props");

			Assert.assertNotNull(props);

			JSONArray vocabulariesJSONArray = (JSONArray)props.get(
				"vocabularies");

			Assert.assertEquals(
				JSONUtil.put(
					JSONUtil.put(
						"categories",
						JSONUtil.put(
							JSONUtil.put(
								"key",
								String.valueOf(
									childAssetCategory.getCategoryId())
							).put(
								"name",
								childAssetCategory.getTitle(LocaleUtil.US)
							).put(
								"value", 1L
							).put(
								"vocabularyName",
								childAssetVocabulary.getTitle(LocaleUtil.US)
							))
					).put(
						"key", String.valueOf(assetCategory.getCategoryId())
					).put(
						"name", assetCategory.getTitle(LocaleUtil.US)
					).put(
						"value", 1L
					).put(
						"vocabularyName",
						assetVocabulary.getTitle(LocaleUtil.US)
					)
				).toString(),
				vocabulariesJSONArray.toString());
		}
		finally {
			_assetCategoryLocalService.deleteAssetCategory(assetCategory);
			_assetCategoryLocalService.deleteAssetCategory(childAssetCategory);
		}
	}

	@Test
	public void testGetPropsWithMissingCategorizedJournalArticle()
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _company.getGroupId(),
				_user.getUserId());

		AssetVocabulary assetVocabulary =
			_assetVocabularyLocalService.fetchGroupVocabulary(
				serviceContext.getScopeGroupId(), "audience");

		AssetCategory assetCategory = _assetCategoryLocalService.addCategory(
			_user.getUserId(), _company.getGroupId(),
			RandomTestUtil.randomString(), assetVocabulary.getVocabularyId(),
			serviceContext);

		AssetVocabulary childAssetVocabulary =
			_assetVocabularyLocalService.fetchGroupVocabulary(
				serviceContext.getScopeGroupId(), "stage");

		AssetCategory childAssetCategory =
			_assetCategoryLocalService.addCategory(
				_user.getUserId(), _company.getGroupId(),
				RandomTestUtil.randomString(),
				childAssetVocabulary.getVocabularyId(), serviceContext);

		try {
			JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);

			Map<String, Object> data = _getData(
				_getMockLiferayPortletRenderRequest());

			Map<String, Object> props = (Map<String, Object>)data.get("props");

			Assert.assertNotNull(props);

			JSONArray vocabulariesJSONArray = (JSONArray)props.get(
				"vocabularies");

			Assert.assertEquals(
				String.valueOf(JSONFactoryUtil.createJSONArray()),
				vocabulariesJSONArray.toString());
		}
		finally {
			_assetCategoryLocalService.deleteAssetCategory(assetCategory);
			_assetCategoryLocalService.deleteAssetCategory(childAssetCategory);
		}
	}

	@Test
	public void testGetPropsWithMissingCategorizedJournalArticleWithAssetCategory()
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _company.getGroupId(),
				_user.getUserId());

		AssetVocabulary assetVocabulary =
			_assetVocabularyLocalService.fetchGroupVocabulary(
				serviceContext.getScopeGroupId(), "audience");

		AssetCategory assetCategory = _assetCategoryLocalService.addCategory(
			_user.getUserId(), _company.getGroupId(),
			RandomTestUtil.randomString(), assetVocabulary.getVocabularyId(),
			serviceContext);

		AssetVocabulary childAssetVocabulary =
			_assetVocabularyLocalService.fetchGroupVocabulary(
				serviceContext.getScopeGroupId(), "stage");

		AssetCategory childAssetCategory =
			_assetCategoryLocalService.addCategory(
				_user.getUserId(), _company.getGroupId(),
				RandomTestUtil.randomString(),
				childAssetVocabulary.getVocabularyId(), serviceContext);

		try {
			JournalArticle journalArticle = JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);

			_journalArticleLocalService.updateAsset(
				_user.getUserId(), journalArticle,
				new long[] {childAssetCategory.getCategoryId()}, new String[0],
				new long[0], null);

			Map<String, Object> data = _getData(
				_getMockLiferayPortletRenderRequest());

			Map<String, Object> props = (Map<String, Object>)data.get("props");

			Assert.assertNotNull(props);

			JSONArray vocabulariesJSONArray = (JSONArray)props.get(
				"vocabularies");

			Assert.assertEquals(
				JSONUtil.put(
					JSONUtil.put(
						"key",
						String.valueOf(childAssetCategory.getCategoryId())
					).put(
						"name", childAssetCategory.getTitle(LocaleUtil.US)
					).put(
						"value", 1L
					).put(
						"vocabularyName",
						childAssetVocabulary.getTitle(LocaleUtil.US)
					)
				).toString(),
				vocabulariesJSONArray.toString());
		}
		finally {
			_assetCategoryLocalService.deleteAssetCategory(assetCategory);
			_assetCategoryLocalService.deleteAssetCategory(childAssetCategory);
		}
	}

	@Test
	public void testGetPropsWithMissingChildAssetVocabularies()
		throws Exception {

		JournalTestUtil.addArticle(_user.getUserId(), _group.getGroupId(), 0);

		Map<String, Object> data = _getData(
			_getMockLiferayPortletRenderRequest());

		Map<String, Object> props = (Map<String, Object>)data.get("props");

		Assert.assertNotNull(props);

		JSONArray vocabulariesJSONArray = (JSONArray)props.get("vocabularies");

		Assert.assertEquals(
			String.valueOf(JSONFactoryUtil.createJSONArray()),
			vocabulariesJSONArray.toString());
	}

	@Test
	public void testGetPropsWithMissingChildAssetVocabulary() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _company.getGroupId(),
				_user.getUserId());

		AssetVocabulary assetVocabulary =
			_assetVocabularyLocalService.fetchGroupVocabulary(
				serviceContext.getScopeGroupId(), "audience");

		AssetCategory assetCategory = _assetCategoryLocalService.addCategory(
			_user.getUserId(), _company.getGroupId(),
			RandomTestUtil.randomString(), assetVocabulary.getVocabularyId(),
			serviceContext);

		try {
			JournalArticle journalArticle = JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);

			_journalArticleLocalService.updateAsset(
				_user.getUserId(), journalArticle,
				new long[] {assetCategory.getCategoryId()}, new String[0],
				new long[0], null);

			Map<String, Object> data = _getData(
				_getMockLiferayPortletRenderRequest());

			Map<String, Object> props = (Map<String, Object>)data.get("props");

			Assert.assertNotNull(props);

			JSONArray vocabulariesJSONArray = (JSONArray)props.get(
				"vocabularies");

			Assert.assertEquals(
				JSONUtil.put(
					JSONUtil.put(
						"key", String.valueOf(assetCategory.getCategoryId())
					).put(
						"name", assetCategory.getTitle(LocaleUtil.US)
					).put(
						"value", 1L
					).put(
						"vocabularyName",
						assetVocabulary.getTitle(LocaleUtil.US)
					)
				).toString(),
				vocabulariesJSONArray.toString());
		}
		finally {
			_assetCategoryLocalService.deleteAssetCategory(assetCategory);
		}
	}

	@Test
	public void testGetSearchContainer() throws Exception {
		User user = UserTestUtil.addGroupAdminUser(_group);

		Group group = GroupTestUtil.addGroup(
			_company.getCompanyId(), _user.getUserId(), 0);

		try {
			MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
				_getMockLiferayPortletRenderRequest();

			SearchContainer<Object> searchContainer = _getSearchContainer(
				mockLiferayPortletRenderRequest);

			int initialCount = searchContainer.getTotal();

			JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);
			JournalTestUtil.addArticle(
				user.getUserId(), _group.getGroupId(), 0);
			JournalTestUtil.addArticle(
				_user.getUserId(), group.getGroupId(), 0);

			searchContainer = _getSearchContainer(
				mockLiferayPortletRenderRequest);

			int actualCount = searchContainer.getTotal();

			Assert.assertEquals(initialCount + 3, actualCount);
		}
		finally {
			GroupTestUtil.deleteGroup(group);
			_userLocalService.deleteUser(user);
		}
	}

	@Test
	public void testGetSearchContainerWithAssetCategory() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _company.getGroupId(),
				_user.getUserId());

		AssetVocabulary assetVocabulary =
			_assetVocabularyLocalService.fetchGroupVocabulary(
				serviceContext.getScopeGroupId(), "audience");

		AssetCategory assetCategory = _assetCategoryLocalService.addCategory(
			_user.getUserId(), _company.getGroupId(),
			RandomTestUtil.randomString(), assetVocabulary.getVocabularyId(),
			serviceContext);

		try {
			JournalArticle journalArticle = JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);

			_journalArticleLocalService.updateAsset(
				_user.getUserId(), journalArticle,
				new long[] {assetCategory.getCategoryId()}, new String[0],
				new long[0], null);

			JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);

			MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
				_getMockLiferayPortletRenderRequest();

			mockLiferayPortletRenderRequest.setParameter(
				"assetCategoryId",
				String.valueOf(assetCategory.getCategoryId()));

			SearchContainer<Object> searchContainer = _getSearchContainer(
				mockLiferayPortletRenderRequest);

			Assert.assertEquals(1, searchContainer.getTotal());

			List<Object> results = searchContainer.getResults();

			Assert.assertEquals(
				journalArticle.getTitle(LocaleUtil.US),
				ReflectionTestUtil.invoke(
					results.get(0), "getTitle", new Class<?>[] {Locale.class},
					LocaleUtil.US));
		}
		finally {
			_assetCategoryLocalService.deleteAssetCategory(
				assetCategory.getCategoryId());
		}
	}

	@Test
	public void testGetSearchContainerWithAssetTag() throws Exception {
		JournalArticle journalArticle1 = JournalTestUtil.addArticle(
			_user.getUserId(), _group.getGroupId(), 0);

		_journalArticleLocalService.updateAsset(
			_user.getUserId(), journalArticle1, new long[0],
			new String[] {"tag1"}, new long[0], null);

		JournalArticle journalArticle2 = JournalTestUtil.addArticle(
			_user.getUserId(), _group.getGroupId(), 0);

		_journalArticleLocalService.updateAsset(
			_user.getUserId(), journalArticle2, new long[0],
			new String[] {"tag2"}, new long[0], null);

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			_getMockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setParameter("assetTagId", "tag1");

		SearchContainer<Object> searchContainer = _getSearchContainer(
			mockLiferayPortletRenderRequest);

		Assert.assertEquals(1, searchContainer.getTotal());

		List<Object> results = searchContainer.getResults();

		Assert.assertEquals(
			journalArticle1.getTitle(LocaleUtil.US),
			ReflectionTestUtil.invoke(
				results.get(0), "getTitle", new Class<?>[] {Locale.class},
				LocaleUtil.US));
	}

	@Test
	public void testGetSearchContainerWithAuthor() throws Exception {
		User user = UserTestUtil.addGroupAdminUser(_group);

		try {
			JournalArticle journalArticle = JournalTestUtil.addArticle(
				user.getUserId(), _group.getGroupId(), 0);

			JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);

			MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
				_getMockLiferayPortletRenderRequest();

			mockLiferayPortletRenderRequest.setParameter(
				"authorIds", String.valueOf(user.getUserId()));

			SearchContainer<Object> searchContainer = _getSearchContainer(
				mockLiferayPortletRenderRequest);

			Assert.assertEquals(1, searchContainer.getTotal());

			List<Object> results = searchContainer.getResults();

			Assert.assertEquals(
				journalArticle.getTitle(LocaleUtil.US),
				ReflectionTestUtil.invoke(
					results.get(0), "getTitle", new Class<?>[] {Locale.class},
					LocaleUtil.US));
		}
		finally {
			_userLocalService.deleteUser(user);
		}
	}

	@Test
	public void testGetSearchContainerWithAuthors() throws Exception {
		User user = UserTestUtil.addGroupAdminUser(_group);

		try {
			JournalArticle journalArticle1 = JournalTestUtil.addArticle(
				user.getUserId(), _group.getGroupId(), 0);
			JournalArticle journalArticle2 = JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);

			MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
				_getMockLiferayPortletRenderRequest();

			mockLiferayPortletRenderRequest.setParameter(
				"authorIds",
				new String[] {
					String.valueOf(user.getUserId()),
					String.valueOf(_user.getUserId())
				});

			SearchContainer<Object> searchContainer = _getSearchContainer(
				mockLiferayPortletRenderRequest);

			Assert.assertEquals(2, searchContainer.getTotal());

			List<Object> results = searchContainer.getResults();

			Stream<Object> stream = results.stream();

			Assert.assertTrue(
				stream.anyMatch(
					result -> Objects.equals(
						journalArticle1.getTitle(LocaleUtil.US),
						ReflectionTestUtil.invoke(
							result, "getTitle", new Class<?>[] {Locale.class},
							LocaleUtil.US))));

			stream = results.stream();

			Assert.assertTrue(
				stream.anyMatch(
					result -> Objects.equals(
						journalArticle2.getTitle(LocaleUtil.US),
						ReflectionTestUtil.invoke(
							result, "getTitle", new Class<?>[] {Locale.class},
							LocaleUtil.US))));
		}
		finally {
			_userLocalService.deleteUser(user);
		}
	}

	@Test
	public void testGetSearchContainerWithContentDashboardItemType()
		throws Exception {

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), JournalArticle.class.getName(), 0,
			DDMStructureTestUtil.getSampleDDMForm(),
			LocaleUtil.getSiteDefault(),
			ServiceContextTestUtil.getServiceContext());

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			JournalTestUtil.getSampleTemplateXSL(),
			LocaleUtil.getSiteDefault());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _group.getGroupId(),
				_user.getUserId());

		JournalArticle journalArticle =
			JournalTestUtil.addArticleWithXMLContent(
				JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				JournalArticleConstants.CLASS_NAME_ID_DEFAULT, 0,
				DDMStructureTestUtil.getSampleStructuredContent(),
				ddmStructure.getStructureKey(), ddmTemplate.getTemplateKey(),
				LocaleUtil.getSiteDefault(), null, serviceContext);

		JournalTestUtil.addArticle(_user.getUserId(), _group.getGroupId(), 0);

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			_getMockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setParameter(
			"contentDashboardItemTypePayload",
			JSONUtil.put(
				"className", DDMStructure.class.getName()
			).put(
				"classPK", ddmStructure.getStructureId()
			).toString());

		SearchContainer<Object> searchContainer = _getSearchContainer(
			mockLiferayPortletRenderRequest);

		Assert.assertEquals(1, searchContainer.getTotal());

		List<Object> results = searchContainer.getResults();

		Assert.assertEquals(
			journalArticle.getTitle(LocaleUtil.US),
			ReflectionTestUtil.invoke(
				results.get(0), "getTitle", new Class<?>[] {Locale.class},
				LocaleUtil.US));
	}

	@Test
	public void testGetSearchContainerWithDefaultOrder() throws Exception {
		JournalArticle journalArticle1 = JournalTestUtil.addArticle(
			_user.getUserId(), _group.getGroupId(), 0);
		JournalArticle journalArticle2 = JournalTestUtil.addArticle(
			_user.getUserId(), _group.getGroupId(), 0);

		SearchContainer<Object> searchContainer = _getSearchContainer(
			_getMockLiferayPortletRenderRequest());

		Assert.assertEquals(2, searchContainer.getTotal());

		List<Object> results = searchContainer.getResults();

		Assert.assertEquals(
			journalArticle2.getTitle(LocaleUtil.US),
			ReflectionTestUtil.invoke(
				results.get(0), "getTitle", new Class<?>[] {Locale.class},
				LocaleUtil.US));
		Assert.assertEquals(
			journalArticle1.getTitle(LocaleUtil.US),
			ReflectionTestUtil.invoke(
				results.get(1), "getTitle", new Class<?>[] {Locale.class},
				LocaleUtil.US));
	}

	@Test
	public void testGetSearchContainerWithExpiredJournalArticle()
		throws Exception {

		User user = UserTestUtil.addGroupAdminUser(_group);

		Group group = GroupTestUtil.addGroup(
			_company.getCompanyId(), _user.getUserId(), 0);

		try {
			MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
				_getMockLiferayPortletRenderRequest();

			SearchContainer<Object> searchContainer = _getSearchContainer(
				mockLiferayPortletRenderRequest);

			int initialCount = searchContainer.getTotal();

			ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(
					group.getGroupId(), user.getUserId());

			DDMForm ddmForm = DDMStructureTestUtil.getSampleDDMForm(
				"content", "string", "text", true, "textarea",
				new Locale[] {LocaleUtil.getSiteDefault()},
				LocaleUtil.getSiteDefault());

			DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
				group.getGroupId(), JournalArticle.class.getName(), 0, ddmForm,
				LocaleUtil.getSiteDefault(),
				ServiceContextTestUtil.getServiceContext());

			DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
				group.getGroupId(),
				PortalUtil.getClassNameId(DDMStructure.class),
				ddmStructure.getStructureId(),
				PortalUtil.getClassNameId(JournalArticle.class));

			JournalArticle journalArticle =
				_journalArticleLocalService.addArticle(
					serviceContext.getUserId(),
					serviceContext.getScopeGroupId(), 0,
					JournalArticleConstants.CLASS_NAME_ID_DEFAULT, 0,
					StringPool.BLANK, true, 2.0,
					Collections.singletonMap(
						LocaleUtil.getSiteDefault(), "Test Article"),
					null,
					DDMStructureTestUtil.getSampleStructuredContent(
						Collections.singletonMap(
							LocaleUtil.getSiteDefault(),
							RandomTestUtil.randomString()),
						LocaleUtil.toLanguageId(LocaleUtil.getSiteDefault())),
					ddmStructure.getStructureKey(),
					ddmTemplate.getTemplateKey(), null, 1, 1, 1965, 0, 0, 0, 0,
					0, 0, 0, true, 0, 0, 0, 0, 0, true, true, false, null, null,
					null, null, serviceContext);

			JournalTestUtil.expireArticle(
				_group.getGroupId(), journalArticle, 2.0);

			searchContainer = _getSearchContainer(
				mockLiferayPortletRenderRequest);

			int actualCount = searchContainer.getTotal();

			Assert.assertEquals(initialCount + 1, actualCount);
		}
		finally {
			GroupTestUtil.deleteGroup(group);
			_userLocalService.deleteUser(user);
		}
	}

	@Test
	public void testGetSearchContainerWithKeywords() throws Exception {
		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_user.getUserId(), _group.getGroupId(), 0);

		JournalTestUtil.addArticle(_user.getUserId(), _group.getGroupId(), 0);

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			_getMockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setParameter(
			"keywords", journalArticle.getTitle(LocaleUtil.getDefault()));

		SearchContainer<Object> searchContainer = _getSearchContainer(
			mockLiferayPortletRenderRequest);

		Assert.assertEquals(1, searchContainer.getTotal());

		List<Object> results = searchContainer.getResults();

		Assert.assertEquals(
			journalArticle.getTitle(LocaleUtil.US),
			ReflectionTestUtil.invoke(
				results.get(0), "getTitle", new Class<?>[] {Locale.class},
				LocaleUtil.US));
	}

	@Test
	public void testGetSearchContainerWithMultipleContentDashboardItemType()
		throws Exception {

		DDMStructure ddmStructure1 = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), JournalArticle.class.getName(), 0,
			DDMStructureTestUtil.getSampleDDMForm(),
			LocaleUtil.getSiteDefault(),
			ServiceContextTestUtil.getServiceContext());

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure1.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			JournalTestUtil.getSampleTemplateXSL(),
			LocaleUtil.getSiteDefault());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _group.getGroupId(),
				_user.getUserId());

		JournalArticle journalArticle1 =
			JournalTestUtil.addArticleWithXMLContent(
				JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				JournalArticleConstants.CLASS_NAME_ID_DEFAULT, 0,
				DDMStructureTestUtil.getSampleStructuredContent(),
				ddmStructure1.getStructureKey(), ddmTemplate.getTemplateKey(),
				LocaleUtil.getSiteDefault(), null, serviceContext);

		JournalArticle journalArticle2 = JournalTestUtil.addArticle(
			_user.getUserId(), _group.getGroupId(), 0);

		DDMStructure ddmStructure2 = journalArticle2.getDDMStructure();

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			_getMockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setParameter(
			"contentDashboardItemTypePayload",
			new String[] {
				JSONUtil.put(
					"className", DDMStructure.class.getName()
				).put(
					"classPK", ddmStructure1.getStructureId()
				).toString(),
				JSONUtil.put(
					"className", DDMStructure.class.getName()
				).put(
					"classPK", ddmStructure2.getStructureId()
				).toString()
			});

		SearchContainer<Object> searchContainer = _getSearchContainer(
			mockLiferayPortletRenderRequest);

		Assert.assertEquals(2, searchContainer.getTotal());

		List<Object> results = searchContainer.getResults();

		Stream<Object> stream = results.stream();

		Assert.assertTrue(
			stream.anyMatch(
				result -> Objects.equals(
					journalArticle1.getTitle(LocaleUtil.US),
					ReflectionTestUtil.invoke(
						result, "getTitle", new Class<?>[] {Locale.class},
						LocaleUtil.US))));

		stream = results.stream();

		Assert.assertTrue(
			stream.anyMatch(
				result -> Objects.equals(
					journalArticle2.getTitle(LocaleUtil.US),
					ReflectionTestUtil.invoke(
						result, "getTitle", new Class<?>[] {Locale.class},
						LocaleUtil.US))));
	}

	@Test
	public void testGetSearchContainerWithPagination() throws Exception {
		for (int i = 0; i <= SearchContainer.DEFAULT_DELTA; i++) {
			JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);
		}

		SearchContainer<Object> searchContainer = _getSearchContainer(
			_getMockLiferayPortletRenderRequest());

		Assert.assertEquals(
			SearchContainer.DEFAULT_DELTA + 1, searchContainer.getTotal());

		List<Object> objects = searchContainer.getResults();

		Assert.assertEquals(
			objects.toString(), SearchContainer.DEFAULT_DELTA, objects.size());
	}

	@Test
	public void testGetSearchContainerWithScope() throws Exception {
		Group group = GroupTestUtil.addGroup(
			_company.getCompanyId(), _user.getUserId(), 0);

		try {
			JournalArticle journalArticle = JournalTestUtil.addArticle(
				_user.getUserId(), group.getGroupId(), 0);

			JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);

			MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
				_getMockLiferayPortletRenderRequest();

			mockLiferayPortletRenderRequest.setParameter(
				"scopeId", String.valueOf(group.getGroupId()));

			SearchContainer<Object> searchContainer = _getSearchContainer(
				mockLiferayPortletRenderRequest);

			Assert.assertEquals(1, searchContainer.getTotal());

			List<Object> results = searchContainer.getResults();

			Assert.assertEquals(
				journalArticle.getTitle(LocaleUtil.US),
				ReflectionTestUtil.invoke(
					results.get(0), "getTitle", new Class<?>[] {Locale.class},
					LocaleUtil.US));
		}
		finally {
			GroupTestUtil.deleteGroup(group);
		}
	}

	@Test
	public void testGetSearchContainerWithStatusAny() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _group.getGroupId(),
				_user.getUserId());

		JournalArticle journalArticle1 = JournalTestUtil.addArticleWithWorkflow(
			_group.getGroupId(), 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), true, serviceContext);
		JournalArticle journalArticle2 = JournalTestUtil.addArticleWithWorkflow(
			_group.getGroupId(), 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), false, serviceContext);

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			_getMockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setParameter(
			"status", String.valueOf(WorkflowConstants.STATUS_ANY));

		SearchContainer<Object> searchContainer = _getSearchContainer(
			mockLiferayPortletRenderRequest);

		Assert.assertEquals(2, searchContainer.getTotal());

		List<Object> results = searchContainer.getResults();

		Stream<Object> stream = results.stream();

		Assert.assertTrue(
			stream.anyMatch(
				result -> Objects.equals(
					journalArticle1.getTitle(LocaleUtil.US),
					ReflectionTestUtil.invoke(
						result, "getTitle", new Class<?>[] {Locale.class},
						LocaleUtil.US))));

		stream = results.stream();

		Assert.assertTrue(
			stream.anyMatch(
				result -> Objects.equals(
					journalArticle2.getTitle(LocaleUtil.US),
					ReflectionTestUtil.invoke(
						result, "getTitle", new Class<?>[] {Locale.class},
						LocaleUtil.US))));
	}

	@Test
	public void testGetSearchContainerWithStatusApproved() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _group.getGroupId(),
				_user.getUserId());

		JournalArticle journalArticle = JournalTestUtil.addArticleWithWorkflow(
			_group.getGroupId(), 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), true, serviceContext);

		JournalTestUtil.addArticleWithWorkflow(
			_group.getGroupId(), 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), false, serviceContext);

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			_getMockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setParameter(
			"status", String.valueOf(WorkflowConstants.STATUS_APPROVED));

		SearchContainer<Object> searchContainer = _getSearchContainer(
			mockLiferayPortletRenderRequest);

		Assert.assertEquals(1, searchContainer.getTotal());

		List<Object> results = searchContainer.getResults();

		Assert.assertEquals(
			journalArticle.getTitle(LocaleUtil.US),
			ReflectionTestUtil.invoke(
				results.get(0), "getTitle", new Class<?>[] {Locale.class},
				LocaleUtil.US));
	}

	@Test
	public void testGetSearchContainerWithStatusDraft() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _group.getGroupId(),
				_user.getUserId());

		JournalArticle journalArticle = JournalTestUtil.addArticleWithWorkflow(
			_group.getGroupId(), 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), false, serviceContext);

		JournalTestUtil.addArticleWithWorkflow(
			_group.getGroupId(), 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), true, serviceContext);

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			_getMockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setParameter(
			"status", String.valueOf(WorkflowConstants.STATUS_DRAFT));

		SearchContainer<Object> searchContainer = _getSearchContainer(
			mockLiferayPortletRenderRequest);

		Assert.assertEquals(1, searchContainer.getTotal());

		List<Object> results = searchContainer.getResults();

		Assert.assertEquals(
			journalArticle.getTitle(LocaleUtil.US),
			ReflectionTestUtil.invoke(
				results.get(0), "getTitle", new Class<?>[] {Locale.class},
				LocaleUtil.US));
	}

	@Test
	public void testGetSearchContainerWithStatusDraftAndAssetCategory()
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _company.getGroupId(),
				_user.getUserId());

		AssetVocabulary assetVocabulary =
			_assetVocabularyLocalService.fetchGroupVocabulary(
				serviceContext.getScopeGroupId(), "audience");

		AssetCategory assetCategory = _assetCategoryLocalService.addCategory(
			_user.getUserId(), _company.getGroupId(),
			RandomTestUtil.randomString(), assetVocabulary.getVocabularyId(),
			serviceContext);

		try {
			JournalArticle journalArticle = JournalTestUtil.addArticle(
				_user.getUserId(), _group.getGroupId(), 0);

			serviceContext = ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _group.getGroupId(),
				_user.getUserId());

			serviceContext.setAssetCategoryIds(
				new long[] {assetCategory.getCategoryId()});

			JournalTestUtil.updateArticle(
				journalArticle, RandomTestUtil.randomString(),
				journalArticle.getContent(), true, false, serviceContext);

			SearchContainer<Object> searchContainer = _getSearchContainer(
				_getMockLiferayPortletRenderRequest());

			Assert.assertEquals(1, searchContainer.getTotal());

			List<Object> results = searchContainer.getResults();

			List<AssetCategory> assetCategories = ReflectionTestUtil.invoke(
				results.get(0), "getAssetCategories", new Class<?>[0]);

			Assert.assertEquals(
				String.valueOf(assetCategories), 1, assetCategories.size());
			Assert.assertEquals(assetCategory, assetCategories.get(0));
		}
		finally {
			_assetVocabularyLocalService.deleteAssetVocabulary(
				assetVocabulary.getVocabularyId());
		}
	}

	@Test
	public void testGetSearchContainerWithStatusDraftAndHasApprovedVersion()
		throws Exception {

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_user.getUserId(), _group.getGroupId(), 0);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _group.getGroupId(),
				_user.getUserId());

		JournalTestUtil.updateArticle(
			journalArticle, RandomTestUtil.randomString(),
			journalArticle.getContent(), true, false, serviceContext);

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			_getMockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setParameter(
			"status", String.valueOf(WorkflowConstants.STATUS_APPROVED));

		SearchContainer<Object> searchContainer = _getSearchContainer(
			mockLiferayPortletRenderRequest);

		Assert.assertEquals(1, searchContainer.getTotal());

		List<Object> results = searchContainer.getResults();

		List<Object> versions = ReflectionTestUtil.invoke(
			results.get(0), "getVersions", new Class<?>[] {Locale.class},
			LocaleUtil.US);

		Assert.assertEquals(versions.toString(), 2, versions.size());
		Assert.assertEquals(
			"Approved",
			ReflectionTestUtil.invoke(
				versions.get(0), "getLabel", new Class<?>[0]));
		Assert.assertEquals(
			"Draft",
			ReflectionTestUtil.invoke(
				versions.get(1), "getLabel", new Class<?>[0]));
	}

	@Test
	public void testGetSearchContainerWithStatusScheduled() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_company.getCompanyId(), _group.getGroupId(),
				_user.getUserId());

		LocalDateTime localDateTime = LocalDateTime.now();

		localDateTime = localDateTime.plusDays(1);

		ZonedDateTime zonedDateTime = localDateTime.atZone(
			ZoneId.systemDefault());

		Date displayDate = Date.from(zonedDateTime.toInstant());

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(), 0,
			JournalArticleConstants.CLASS_NAME_ID_DEFAULT, StringPool.BLANK,
			true, RandomTestUtil.randomLocaleStringMap(),
			RandomTestUtil.randomLocaleStringMap(),
			RandomTestUtil.randomLocaleStringMap(), null,
			LocaleUtil.getSiteDefault(), displayDate, null, true, true,
			serviceContext);

		JournalTestUtil.addArticleWithWorkflow(
			_group.getGroupId(), 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), true, serviceContext);

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			_getMockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setParameter(
			"status", String.valueOf(WorkflowConstants.STATUS_SCHEDULED));

		SearchContainer<Object> searchContainer = _getSearchContainer(
			mockLiferayPortletRenderRequest);

		Assert.assertEquals(1, searchContainer.getTotal());

		List<Object> results = searchContainer.getResults();

		Assert.assertEquals(
			journalArticle.getTitle(LocaleUtil.US),
			ReflectionTestUtil.invoke(
				results.get(0), "getTitle", new Class<?>[] {Locale.class},
				LocaleUtil.US));
	}

	private String _getAuditGraphTitle(
			MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest)
		throws Exception {

		MVCPortlet mvcPortlet = (MVCPortlet)_portlet;

		mvcPortlet.render(
			mockLiferayPortletRenderRequest,
			new MockLiferayPortletRenderResponse());

		return ReflectionTestUtil.invoke(
			mockLiferayPortletRenderRequest.getAttribute(
				"CONTENT_DASHBOARD_ADMIN_DISPLAY_CONTEXT"),
			"getAuditGraphTitle", new Class<?>[0]);
	}

	private Map<String, Object> _getData(
			MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest)
		throws Exception {

		MVCPortlet mvcPortlet = (MVCPortlet)_portlet;

		mvcPortlet.render(
			mockLiferayPortletRenderRequest,
			new MockLiferayPortletRenderResponse());

		return ReflectionTestUtil.invoke(
			mockLiferayPortletRenderRequest.getAttribute(
				"CONTENT_DASHBOARD_ADMIN_DISPLAY_CONTEXT"),
			"getData", new Class<?>[0]);
	}

	private MockLiferayPortletRenderRequest
			_getMockLiferayPortletRenderRequest()
		throws Exception {

		return _getMockLiferayPortletRenderRequest(LocaleUtil.US);
	}

	private MockLiferayPortletRenderRequest _getMockLiferayPortletRenderRequest(
			Locale locale)
		throws Exception {

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			new MockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setAttribute(
			WebKeys.COMPANY_ID, _company.getCompanyId());

		mockLiferayPortletRenderRequest.setAttribute(
			StringBundler.concat(
				mockLiferayPortletRenderRequest.getPortletName(), "-",
				WebKeys.CURRENT_PORTLET_URL),
			new MockLiferayPortletURL());

		String path = "/view.jsp";

		mockLiferayPortletRenderRequest.setParameter("mvcPath", path);

		mockLiferayPortletRenderRequest.setAttribute(
			MVCRenderConstants.
				PORTLET_CONTEXT_OVERRIDE_REQUEST_ATTIBUTE_NAME_PREFIX + path,
			ProxyUtil.newProxyInstance(
				PortletContext.class.getClassLoader(),
				new Class<?>[] {PortletContext.class},
				(PortletContextProxy, portletContextMethod,
				 portletContextArgs) -> {

					if (Objects.equals(
							portletContextMethod.getName(),
							"getRequestDispatcher") &&
						Objects.equals(portletContextArgs[0], path)) {

						return ProxyUtil.newProxyInstance(
							PortletRequestDispatcher.class.getClassLoader(),
							new Class<?>[] {PortletRequestDispatcher.class},
							(portletRequestDispatcherProxy,
							 portletRequestDispatcherMethod,
							 portletRequestDispatcherArgs) -> null);
					}

					throw new UnsupportedOperationException();
				}));

		mockLiferayPortletRenderRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay(locale));

		PortletPreferences portletPreferences =
			mockLiferayPortletRenderRequest.getPreferences();

		portletPreferences.setValues(
			"assetVocabularyNames", "audience", "stage");

		return mockLiferayPortletRenderRequest;
	}

	private SearchContainer<Object> _getSearchContainer(
			MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest)
		throws Exception {

		MVCPortlet mvcPortlet = (MVCPortlet)_portlet;

		mvcPortlet.render(
			mockLiferayPortletRenderRequest,
			new MockLiferayPortletRenderResponse());

		return ReflectionTestUtil.invoke(
			mockLiferayPortletRenderRequest.getAttribute(
				"CONTENT_DASHBOARD_ADMIN_DISPLAY_CONTEXT"),
			"getSearchContainer", new Class<?>[0]);
	}

	private ThemeDisplay _getThemeDisplay(Locale locale) throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(_company);
		themeDisplay.setLocale(locale);
		themeDisplay.setPermissionChecker(
			PermissionThreadLocal.getPermissionChecker());
		themeDisplay.setUser(_company.getDefaultUser());

		return themeDisplay;
	}

	private static Company _company;

	@Inject
	private static CompanyLocalService _companyLocalService;

	private static PermissionChecker _permissionChecker;
	private static User _user;

	@Inject
	private AssetCategoryLocalService _assetCategoryLocalService;

	@Inject
	private AssetVocabularyLocalService _assetVocabularyLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private JournalArticleLocalService _journalArticleLocalService;

	@Inject(
		filter = "component.name=com.liferay.content.dashboard.web.internal.portlet.ContentDashboardAdminPortlet"
	)
	private Portlet _portlet;

	@Inject
	private UserLocalService _userLocalService;

}