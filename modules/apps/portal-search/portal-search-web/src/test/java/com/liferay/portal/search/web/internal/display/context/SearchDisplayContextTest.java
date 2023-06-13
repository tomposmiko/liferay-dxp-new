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

package com.liferay.portal.search.web.internal.display.context;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.test.util.PropsTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upgrade.MockPortletPreferences;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.context.SearchContextFactory;
import com.liferay.portal.search.internal.legacy.searcher.SearchRequestBuilderFactoryImpl;
import com.liferay.portal.search.legacy.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.legacy.searcher.SearchResponseBuilderFactory;
import com.liferay.portal.search.searcher.SearchResponse;
import com.liferay.portal.search.searcher.SearchResponseBuilder;
import com.liferay.portal.search.searcher.Searcher;
import com.liferay.portal.search.summary.SummaryBuilderFactory;
import com.liferay.portal.search.web.constants.SearchPortletParameterNames;
import com.liferay.portal.search.web.internal.facet.SearchFacetTracker;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portlet.portletconfiguration.util.ConfigurationRenderRequest;

import java.util.Collections;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author André de Oliveira
 */
public class SearchDisplayContextTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		themeDisplay = createThemeDisplay();

		setUpHttpServletRequest();
		setUpPortletURLFactory();
		setUpRenderRequest();
		setUpSearchContextFactory();
		setUpSearcher();
		setUpSearchResponseBuilderFactory();
	}

	@Test
	public void testConfigurationKeywordsEmptySkipsSearch() throws Exception {
		assertSearchSkippedAndNullResults(
			null,
			new ConfigurationRenderRequest(renderRequest, portletPreferences));
	}

	@Test
	public void testNoScopeParameter() throws Exception {
		portletPreferences.setValue("searchScope", "let-the-user-choose");

		assertSearchKeywords(StringPool.DOUBLE_SPACE, StringPool.BLANK);
	}

	@Test
	public void testSearchKeywordsBlank() throws Exception {
		assertSearchKeywords(StringPool.BLANK, StringPool.BLANK);
	}

	@Test
	public void testSearchKeywordsNullWord() throws Exception {
		assertSearchKeywords(StringPool.NULL, StringPool.NULL);
	}

	@Test
	public void testSearchKeywordsSpaces() throws Exception {
		assertSearchKeywords(StringPool.DOUBLE_SPACE, StringPool.BLANK);
	}

	protected void assertSearchKeywords(
			String requestKeywords, String searchDisplayContextKeywords)
		throws Exception {

		SearchDisplayContext searchDisplayContext = createSearchDisplayContext(
			requestKeywords, renderRequest);

		Assert.assertEquals(
			searchDisplayContextKeywords, searchDisplayContext.getKeywords());

		Assert.assertNotNull(searchDisplayContext.getHits());
		Assert.assertNotNull(searchDisplayContext.getSearchContainer());
		Assert.assertNotNull(searchDisplayContext.getSearchContext());

		SearchContext searchContext = searchDisplayContext.getSearchContext();

		Mockito.verify(
			searcher
		).search(
			Mockito.any()
		);

		Assert.assertEquals(
			searchDisplayContextKeywords, searchContext.getKeywords());
	}

	protected void assertSearchSkippedAndNullResults(
			String requestKeywords, RenderRequest renderRequest)
		throws Exception {

		SearchDisplayContext searchDisplayContext = createSearchDisplayContext(
			requestKeywords, renderRequest);

		Assert.assertNull(searchDisplayContext.getHits());
		Assert.assertNull(searchDisplayContext.getKeywords());
		Assert.assertNull(searchDisplayContext.getSearchContainer());
		Assert.assertNull(searchDisplayContext.getSearchContext());

		Mockito.verifyZeroInteractions(searcher);
	}

	protected JSONArray createJSONArray() {
		JSONArray jsonArray = Mockito.mock(JSONArray.class);

		Mockito.doReturn(
			1
		).when(
			jsonArray
		).length();

		Mockito.doReturn(
			RandomTestUtil.randomString()
		).when(
			jsonArray
		).getString(
			0
		);

		return jsonArray;
	}

	protected JSONFactory createJSONFactory() {
		JSONFactory jsonFactory = Mockito.mock(JSONFactory.class);

		Mockito.doReturn(
			createJSONObject()
		).when(
			jsonFactory
		).createJSONObject();

		return jsonFactory;
	}

	protected JSONObject createJSONObject() {
		JSONObject jsonObject = Mockito.mock(JSONObject.class);

		Mockito.doReturn(
			true
		).when(
			jsonObject
		).has(
			"values"
		);

		Mockito.doReturn(
			createJSONArray()
		).when(
			jsonObject
		).getJSONArray(
			"values"
		);

		return jsonObject;
	}

	protected Portal createPortal(
			ThemeDisplay themeDisplay, RenderRequest renderRequest)
		throws Exception {

		Portal portal = Mockito.mock(Portal.class);

		Mockito.doReturn(
			httpServletRequest
		).when(
			portal
		).getHttpServletRequest(
			renderRequest
		);

		return portal;
	}

	protected SearchDisplayContext createSearchDisplayContext(
			String keywords, RenderRequest renderRequest)
		throws Exception {

		setUpRequestKeywords(keywords);

		PropsTestUtil.setProps(Collections.emptyMap());

		return new SearchDisplayContext(
			renderRequest, portletPreferences,
			createPortal(themeDisplay, renderRequest), Mockito.mock(Html.class),
			Mockito.mock(Language.class), searcher,
			Mockito.mock(IndexSearchPropsValues.class), portletURLFactory,
			Mockito.mock(SummaryBuilderFactory.class), searchContextFactory,
			searchRequestBuilderFactory, new SearchFacetTracker());
	}

	protected ThemeDisplay createThemeDisplay() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(Mockito.mock(Company.class));
		themeDisplay.setUser(Mockito.mock(User.class));

		return themeDisplay;
	}

	protected void setUpHttpServletRequest() throws Exception {
		Mockito.doReturn(
			themeDisplay
		).when(
			httpServletRequest
		).getAttribute(
			WebKeys.THEME_DISPLAY
		);
	}

	protected void setUpPortletURLFactory() throws Exception {
		Mockito.doReturn(
			Mockito.mock(PortletURL.class)
		).when(
			portletURLFactory
		).getPortletURL();
	}

	protected void setUpRenderRequest() throws Exception {
		Mockito.doReturn(
			themeDisplay
		).when(
			renderRequest
		).getAttribute(
			WebKeys.THEME_DISPLAY
		);
	}

	protected void setUpRequestKeywords(String keywords) {
		Mockito.doReturn(
			keywords
		).when(
			httpServletRequest
		).getParameter(
			SearchPortletParameterNames.KEYWORDS
		);

		Mockito.doReturn(
			keywords
		).when(
			renderRequest
		).getParameter(
			SearchPortletParameterNames.KEYWORDS
		);
	}

	protected void setUpSearchContextFactory() throws Exception {
		Mockito.doReturn(
			new SearchContext()
		).when(
			searchContextFactory
		).getSearchContext(
			Mockito.any(), Mockito.any(), Mockito.anyLong(), Mockito.any(),
			Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyLong(),
			Mockito.any(), Mockito.anyLong()
		);
	}

	protected void setUpSearcher() throws Exception {
		Mockito.doReturn(
			Mockito.mock(Hits.class)
		).when(
			searchResponse
		).withHitsGet(
			Mockito.any()
		);

		Mockito.doReturn(
			searchResponse
		).when(
			searcher
		).search(
			Mockito.any()
		);
	}

	protected void setUpSearchResponseBuilderFactory() {
		Mockito.doReturn(
			searchResponseBuilder
		).when(
			searchResponseBuilderFactory
		).builder(
			Mockito.any()
		);

		Mockito.doReturn(
			searchResponse
		).when(
			searchResponseBuilder
		).build();
	}

	@Mock
	protected HttpServletRequest httpServletRequest;

	protected PortletPreferences portletPreferences =
		new MockPortletPreferences();

	@Mock
	protected PortletURLFactory portletURLFactory;

	@Mock
	protected RenderRequest renderRequest;

	@Mock
	protected SearchContextFactory searchContextFactory;

	@Mock
	protected Searcher searcher;

	protected SearchRequestBuilderFactory searchRequestBuilderFactory =
		new SearchRequestBuilderFactoryImpl();

	@Mock
	protected SearchResponse searchResponse;

	@Mock
	protected SearchResponseBuilder searchResponseBuilder;

	@Mock
	protected SearchResponseBuilderFactory searchResponseBuilderFactory;

	protected ThemeDisplay themeDisplay;

}