/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.search.tuning.rankings.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.engine.SearchEngineInformation;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.sort.Sorts;
import com.liferay.portal.search.tuning.rankings.web.internal.BaseRankingsWebTestCase;
import com.liferay.portal.search.tuning.rankings.web.internal.index.DocumentToRankingTranslator;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Wade Cao
 */
public class RankingPortletDisplayBuilderTest extends BaseRankingsWebTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		setUpPortletPreferencesFactoryUtil();

		_rankingPortletDisplayBuilder = new RankingPortletDisplayBuilder(
			_documentToRankingTranslator, _httpServletRequest, language, portal,
			queries, rankingIndexNameBuilder, _sorts, _renderRequest,
			_renderResponse, searchEngineAdapter, _searchEngineInformation);
	}

	@Test
	public void testBuild() throws Exception {
		setUpHttpServletRequestParamValue(
			_httpServletRequest, "displayStyle", "displayStyle");
		setUpHttpServletRequestParamValue(
			_httpServletRequest, "orderByType", "desc");
		setUpHttpServletRequestAttribute(
			_httpServletRequest, WebKeys.THEME_DISPLAY,
			Mockito.mock(ThemeDisplay.class));

		setUpLanguageUtil("");
		setUpPortal();
		setUpPortalUtil();
		setUpRankingIndexNameBuilder();
		setUpRenderResponse(_renderResponse);
		setUpSearchEngineAdapter(Mockito.mock(SearchHits.class));

		RankingPortletDisplayContext rankingPortletDisplayContext =
			_rankingPortletDisplayBuilder.build();

		Assert.assertEquals(
			"", rankingPortletDisplayContext.getClearResultsURL());
		Assert.assertNotNull(rankingPortletDisplayContext.getCreationMenu());
		Assert.assertEquals(
			"displayStyle", rankingPortletDisplayContext.getDisplayStyle());
		Assert.assertEquals(
			"desc", rankingPortletDisplayContext.getOrderByType());
		Assert.assertEquals(
			"", rankingPortletDisplayContext.getSearchActionURL());
		Assert.assertNotNull(rankingPortletDisplayContext.getSearchContainer());
		Assert.assertEquals("", rankingPortletDisplayContext.getSortingURL());
		Assert.assertEquals(3, rankingPortletDisplayContext.getTotalItems());
		Assert.assertFalse(
			rankingPortletDisplayContext.isDisabledManagementBar());
		Assert.assertTrue(rankingPortletDisplayContext.isShowCreationMenu());

		List<DropdownItem> dropdownItems =
			rankingPortletDisplayContext.getActionDropdownItems();

		Assert.assertEquals(dropdownItems.toString(), 3, dropdownItems.size());

		dropdownItems =
			rankingPortletDisplayContext.getFilterItemsDropdownItems();

		Assert.assertEquals(dropdownItems.toString(), 2, dropdownItems.size());
	}

	@Override
	protected HttpServletRequest setUpPortalGetHttpServletRequest() {
		Mockito.doReturn(
			_httpServletRequest
		).when(
			portal
		).getHttpServletRequest(
			Mockito.any(PortletRequest.class)
		);

		return _httpServletRequest;
	}

	private final DocumentToRankingTranslator _documentToRankingTranslator =
		Mockito.mock(DocumentToRankingTranslator.class);
	private final HttpServletRequest _httpServletRequest = Mockito.mock(
		HttpServletRequest.class);
	private RankingPortletDisplayBuilder _rankingPortletDisplayBuilder;
	private final RenderRequest _renderRequest = Mockito.mock(
		RenderRequest.class);
	private final RenderResponse _renderResponse = Mockito.mock(
		RenderResponse.class);
	private final SearchEngineInformation _searchEngineInformation =
		Mockito.mock(SearchEngineInformation.class);
	private final Sorts _sorts = Mockito.mock(Sorts.class);

}