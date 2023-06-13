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

package com.liferay.analytics.layout.page.template.web.internal.servlet.taglib.test;

import com.liferay.analytics.layout.page.template.web.internal.MockObject;
import com.liferay.analytics.layout.page.template.web.internal.layout.display.page.MockObjectLayoutDisplayPageObjectProvider;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.service.BlogsEntryLocalService;
import com.liferay.info.item.InfoItemReference;
import com.liferay.journal.constants.JournalFolderConstants;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.layout.display.page.LayoutDisplayPageProvider;
import com.liferay.layout.display.page.constants.LayoutDisplayPageWebKeys;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.IOException;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Cristina González
 */
@RunWith(Arquillian.class)
public class AnalyticsRenderFragmentLayoutPostDynamicIncludeTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup(
			TestPropsValues.getCompanyId(), TestPropsValues.getUserId(), 0);
	}

	@Test
	public void testIncludeWithBlog() throws Exception {
		BlogsEntry blogsEntry = _blogsEntryLocalService.addEntry(
			TestPropsValues.getUserId(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(),
			ServiceContextTestUtil.getServiceContext(_group.getGroupId()));

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			LayoutDisplayPageWebKeys.LAYOUT_DISPLAY_PAGE_OBJECT_PROVIDER,
			_blogsLayoutDisplayPageProvider.getLayoutDisplayPageObjectProvider(
				_group.getGroupId(), blogsEntry.getUrlTitle()));

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_dynamicInclude.include(
			mockHttpServletRequest, mockHttpServletResponse,
			RandomTestUtil.randomString());

		Assert.assertEquals(
			"</div >", mockHttpServletResponse.getContentAsString());
	}

	@Test
	public void testIncludeWithJournalArticle() throws Exception {
		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			Collections.emptyMap());

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			LayoutDisplayPageWebKeys.LAYOUT_DISPLAY_PAGE_OBJECT_PROVIDER,
			_journalArticlesLayoutDisplayPageProvider.
				getLayoutDisplayPageObjectProvider(
					new InfoItemReference(
						JournalArticle.class.getName(),
						journalArticle.getResourcePrimKey())));

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_dynamicInclude.include(
			mockHttpServletRequest, mockHttpServletResponse,
			RandomTestUtil.randomString());

		Assert.assertEquals(
			"</div >", mockHttpServletResponse.getContentAsString());
	}

	@Test
	public void testIncludeWithoutLayoutDisplayPageObjectProvider()
		throws IOException {

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_dynamicInclude.include(
			mockHttpServletRequest, mockHttpServletResponse,
			RandomTestUtil.randomString());

		Assert.assertEquals(
			StringPool.BLANK, mockHttpServletResponse.getContentAsString());
	}

	@Test
	public void testIncludeWithUnregisteredClass() throws IOException {
		ClassName className = _classNameLocalService.addClassName(
			MockObject.class.getName());

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setAttribute(
			LayoutDisplayPageWebKeys.LAYOUT_DISPLAY_PAGE_OBJECT_PROVIDER,
			new MockObjectLayoutDisplayPageObjectProvider(
				className.getClassNameId()));

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_dynamicInclude.include(
			mockHttpServletRequest, mockHttpServletResponse,
			RandomTestUtil.randomString());

		Assert.assertEquals(
			StringPool.BLANK, mockHttpServletResponse.getContentAsString());
	}

	@Inject
	private BlogsEntryLocalService _blogsEntryLocalService;

	@Inject(filter = "component.name=*.BlogsLayoutDisplayPageProvider")
	private LayoutDisplayPageProvider _blogsLayoutDisplayPageProvider;

	@Inject
	private ClassNameLocalService _classNameLocalService;

	@Inject(
		filter = "component.name=com.liferay.analytics.layout.page.template.web.internal.servlet.taglib.AnalyticsRenderFragmentLayoutPostDynamicInclude"
	)
	private DynamicInclude _dynamicInclude;

	@DeleteAfterTestRun
	private Group _group;

	@Inject(filter = "component.name=*.JournalArticleLayoutDisplayPageProvider")
	private LayoutDisplayPageProvider _journalArticlesLayoutDisplayPageProvider;

}