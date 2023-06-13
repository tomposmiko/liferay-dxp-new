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

package com.liferay.search.experiences.internal.suggestions.spi;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.search.document.Document;
import com.liferay.portal.search.hits.SearchHit;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.internal.suggestions.SuggestionBuilderFactoryImpl;
import com.liferay.portal.search.internal.suggestions.SuggestionsContributorResultsBuilderFactoryImpl;
import com.liferay.portal.search.rest.dto.v1_0.SuggestionsContributorConfiguration;
import com.liferay.portal.search.searcher.SearchRequest;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.portal.search.searcher.SearchRequestBuilderFactory;
import com.liferay.portal.search.searcher.SearchResponse;
import com.liferay.portal.search.searcher.Searcher;
import com.liferay.portal.search.suggestions.Suggestion;
import com.liferay.portal.search.suggestions.SuggestionsContributorResults;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.portlet.MutableRenderParameters;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Wade Cao
 * @author Gustavo Lima
 */
public class SXPBlueprintSuggestionsContributorTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		_setUpAssetEntryLocalService();
		_setUpLayoutLocalService();
		_setUpLiferayPortletRequest();
		_setUpLiferayPortletResponse();
		_setUpPortalUtil();
		_setUpSearchContext();
		_setUpSearchRequestBuilderFactory();
		_setUpSuggestionsContributorConfiguration();
		_setUpSXPBlueprintSuggestionsContributor();
	}

	@Test
	public void testGetSuggestionsContributorResults() throws Exception {
		int totalHits = 1;

		_setUpAssetRendererFactoryRegistryUtil(
			"Asset Renderer Title", "Asset Renderer Summary");
		_setUpSearcher(totalHits);
		_setUpSuggestionsContributorConfiguration("testField");

		SuggestionsContributorResults suggestionsContributorResults =
			_getSuggestionsContributorResults();

		Assert.assertEquals(
			"testGetSuggestionsContributorResults",
			suggestionsContributorResults.getDisplayGroupName());

		List<Suggestion> suggestions =
			suggestionsContributorResults.getSuggestions();

		for (int i = 0; i < totalHits; i++) {
			Suggestion suggestion = suggestions.get(i);

			Assert.assertEquals(
				"Asset Renderer Summary",
				suggestion.getAttribute("assetSearchSummary"));
			Assert.assertEquals(i, suggestion.getScore(), i);
			Assert.assertEquals("Document Text " + i, suggestion.getText());
		}
	}

	@Test
	public void testGetSuggestionsContributorResultsWithNullTextFieldName()
		throws Exception {

		int totalHits = 1;

		_setUpAssetRendererFactoryRegistryUtil(
			"Asset Renderer Title", "Asset Renderer Summary");
		_setUpSearcher(totalHits);
		_setUpSuggestionsContributorConfiguration(null);

		SuggestionsContributorResults suggestionsContributorResults =
			_getSuggestionsContributorResults();

		List<Suggestion> suggestions =
			suggestionsContributorResults.getSuggestions();

		for (int i = 0; i < totalHits; i++) {
			Suggestion suggestion = suggestions.get(i);

			Assert.assertEquals(
				"Asset Renderer Summary",
				suggestion.getAttribute("assetSearchSummary"));
			Assert.assertEquals(i, suggestion.getScore(), i);
			Assert.assertEquals("Asset Renderer Title", suggestion.getText());
		}
	}

	@Test
	public void testSearchHitsWithZeroTotalHits() throws Exception {
		_setUpSearcher(0);

		Assert.assertNull(_getSuggestionsContributorResults());

		Mockito.verify(
			_liferayPortletRequest, Mockito.never()
		).getAttribute(
			Mockito.anyString()
		);
	}

	@Test
	public void testSuggestionsContributorConfigurationWithAssetRendererNull() {
		int totalHits = 1;

		_setUpSearcher(totalHits);

		_setUpAssetRendererFactoryRegistryUtilNull();

		_setUpSuggestionsContributorConfiguration("testField");

		SuggestionsContributorResults suggestionsContributorResults =
			_getSuggestionsContributorResults();

		Assert.assertEquals(
			"testSuggestionsContributorConfigurationWithAssetRendererNull",
			suggestionsContributorResults.getDisplayGroupName());

		List<Suggestion> suggestions =
			suggestionsContributorResults.getSuggestions();

		for (int i = 0; i < totalHits; i++) {
			Suggestion suggestion = suggestions.get(i);

			Assert.assertNull(suggestion.getAttribute("assetSearchSummary"));
			Assert.assertEquals(i, suggestion.getScore(), i);
			Assert.assertEquals("Document Text " + i, suggestion.getText());
		}
	}

	@Test
	public void testSuggestionsContributorConfigurationWithNullAttributes() {
		Assert.assertNull(_getSuggestionsContributorResults());

		Mockito.verify(
			_searcher, Mockito.never()
		).search(
			Mockito.any()
		);
	}

	@Rule
	public TestName testName = new TestName();

	private SuggestionsContributorResults _getSuggestionsContributorResults() {
		return _sxpBlueprintSuggestionsContributor.
			getSuggestionsContributorResults(
				_liferayPortletRequest, _liferayPortletResponse, _searchContext,
				_suggestionsContributorConfiguration);
	}

	private void _setUpAssetEntryLocalService() throws Exception {
		AssetEntry assetEntry = Mockito.mock(AssetEntry.class);

		Mockito.doReturn(
			RandomTestUtil.randomLong()
		).when(
			assetEntry
		).getEntryId();

		Mockito.doReturn(
			assetEntry
		).when(
			_assetEntryLocalService
		).getEntry(
			Mockito.anyString(), Mockito.anyLong()
		);
	}

	private void _setUpAssetRendererFactoryRegistryUtil(
			String title, String summary)
		throws Exception {

		AssetRenderer<?> assetRenderer = Mockito.mock(AssetRenderer.class);

		Mockito.doReturn(
			summary
		).when(
			assetRenderer
		).getSearchSummary(
			Mockito.any()
		);

		Mockito.doReturn(
			title
		).when(
			assetRenderer
		).getTitle(
			Mockito.any()
		);

		Mockito.doReturn(
			assetRenderer
		).when(
			_assetRendererFactory
		).getAssetRenderer(
			Mockito.anyLong()
		);

		Mockito.doReturn(
			_assetRendererFactory
		).when(
			_serviceTrackerMap
		).getService(
			Mockito.anyString()
		);

		ReflectionTestUtil.setFieldValue(
			AssetRendererFactoryRegistryUtil.class,
			"_classNameAssetRenderFactoriesServiceTrackerMap",
			_serviceTrackerMap);
	}

	private void _setUpAssetRendererFactoryRegistryUtilNull() {
		Mockito.doReturn(
			null
		).when(
			_serviceTrackerMap
		).getService(
			Mockito.anyString()
		);

		ReflectionTestUtil.setFieldValue(
			AssetRendererFactoryRegistryUtil.class,
			"_classNameAssetRenderFactoriesServiceTrackerMap",
			_serviceTrackerMap);
	}

	private void _setUpLayoutLocalService() {
		Mockito.doReturn(
			Mockito.mock(Layout.class)
		).when(
			_layoutLocalService
		).fetchLayoutByFriendlyURL(
			Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyString()
		);
	}

	private void _setUpLiferayPortletRequest() {
		ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);

		Mockito.doReturn(
			themeDisplay
		).when(
			_liferayPortletRequest
		).getAttribute(
			Mockito.anyString()
		);
	}

	private void _setUpLiferayPortletResponse() {
		LiferayPortletURL liferayPortletURL = Mockito.mock(
			LiferayPortletURL.class);

		Mockito.doReturn(
			liferayPortletURL
		).when(
			_liferayPortletResponse
		).createLiferayPortletURL(
			Mockito.anyLong(), Mockito.anyString(), Mockito.anyString()
		);

		Mockito.doReturn(
			Mockito.mock(MutableRenderParameters.class)
		).when(
			liferayPortletURL
		).getRenderParameters();
	}

	private void _setUpPortalUtil() {
		Portal portal = Mockito.mock(Portal.class);

		Mockito.doAnswer(
			invocation -> new String[] {
				invocation.getArgument(0, String.class), StringPool.BLANK
			}
		).when(
			portal
		).stripURLAnchor(
			Mockito.anyString(), Mockito.anyString()
		);

		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(portal);
	}

	private void _setUpSearchContext() {
		Mockito.doReturn(
			"test"
		).when(
			_searchContext
		).getKeywords();

		Mockito.doReturn(
			"test"
		).when(
			_searchContext
		).getAttribute(
			Mockito.anyString()
		);
	}

	private void _setUpSearcher(long totalHits) {
		SearchResponse searchResponse = Mockito.mock(SearchResponse.class);

		SearchHits searchHits = Mockito.mock(SearchHits.class);

		List<SearchHit> searchHitsList = new ArrayList<>();

		for (int i = 0; i < totalHits; i++) {
			SearchHit searchHit = Mockito.mock(SearchHit.class);

			Document document = Mockito.mock(Document.class);

			Mockito.doReturn(
				"Class Name " + i
			).when(
				document
			).getString(
				Mockito.eq(Field.ENTRY_CLASS_NAME)
			);

			Mockito.doReturn(
				"Class Name " + i
			).when(
				document
			).getString(
				Mockito.eq(Field.ENTRY_CLASS_NAME)
			);

			Mockito.doReturn(
				ListUtil.fromArray("Document Text " + i)
			).when(
				document
			).getStrings(
				Mockito.anyString()
			);

			Mockito.doReturn(
				"Document Title " + i
			).when(
				document
			).getString(
				Mockito.startsWith("title")
			);

			Mockito.doReturn(
				document
			).when(
				searchHit
			).getDocument();

			Mockito.doReturn(
				Float.valueOf(i)
			).when(
				searchHit
			).getScore();

			searchHitsList.add(searchHit);
		}

		Mockito.doReturn(
			searchHitsList
		).when(
			searchHits
		).getSearchHits();

		Mockito.doReturn(
			totalHits
		).when(
			searchHits
		).getTotalHits();

		Mockito.doReturn(
			searchHits
		).when(
			searchResponse
		).getSearchHits();

		Mockito.doReturn(
			searchResponse
		).when(
			_searcher
		).search(
			Mockito.any()
		);
	}

	private void _setUpSearchRequestBuilderFactory() {
		SearchRequestBuilder searchRequestBuilder = Mockito.mock(
			SearchRequestBuilder.class);

		Mockito.doReturn(
			Mockito.mock(SearchRequest.class)
		).when(
			searchRequestBuilder
		).build();

		Mockito.doReturn(
			searchRequestBuilder
		).when(
			searchRequestBuilder
		).from(
			Mockito.anyInt()
		);

		Mockito.doReturn(
			searchRequestBuilder
		).when(
			searchRequestBuilder
		).queryString(
			Mockito.anyString()
		);

		Mockito.doReturn(
			searchRequestBuilder
		).when(
			searchRequestBuilder
		).size(
			Mockito.anyInt()
		);

		Mockito.doReturn(
			searchRequestBuilder
		).when(
			searchRequestBuilder
		).withSearchContext(
			Mockito.any(Consumer.class)
		);

		Mockito.doReturn(
			searchRequestBuilder
		).when(
			_searchRequestBuilderFactory
		).builder();
	}

	private void _setUpSuggestionsContributorConfiguration() {
		_suggestionsContributorConfiguration =
			new SuggestionsContributorConfiguration();

		_suggestionsContributorConfiguration.setDisplayGroupName(
			testName.getMethodName());
	}

	private void _setUpSuggestionsContributorConfiguration(String textField) {
		_suggestionsContributorConfiguration.setAttributes(
			HashMapBuilder.<String, Object>put(
				"fields", ListUtil.fromArray("field")
			).put(
				"sxpBlueprintId", RandomTestUtil.randomLong()
			).put(
				"textField", textField
			).build());

		_suggestionsContributorConfiguration.setSize(
			RandomTestUtil.randomInt());
	}

	private void _setUpSXPBlueprintSuggestionsContributor() {
		_sxpBlueprintSuggestionsContributor =
			new SXPBlueprintSuggestionsContributor();

		ReflectionTestUtil.setFieldValue(
			_sxpBlueprintSuggestionsContributor, "_assetEntryLocalService",
			_assetEntryLocalService);
		ReflectionTestUtil.setFieldValue(
			_sxpBlueprintSuggestionsContributor, "_layoutLocalService",
			_layoutLocalService);
		ReflectionTestUtil.setFieldValue(
			_sxpBlueprintSuggestionsContributor, "_searcher", _searcher);
		ReflectionTestUtil.setFieldValue(
			_sxpBlueprintSuggestionsContributor, "_searchRequestBuilderFactory",
			_searchRequestBuilderFactory);
		ReflectionTestUtil.setFieldValue(
			_sxpBlueprintSuggestionsContributor,
			"_suggestionsContributorResultsBuilderFactory",
			new SuggestionsContributorResultsBuilderFactoryImpl());
		ReflectionTestUtil.setFieldValue(
			_sxpBlueprintSuggestionsContributor, "_suggestionBuilderFactory",
			new SuggestionBuilderFactoryImpl());
	}

	@Mock
	private AssetEntryLocalService _assetEntryLocalService;

	@Mock
	private AssetRendererFactory<?> _assetRendererFactory;

	@Mock
	private LayoutLocalService _layoutLocalService;

	@Mock
	private LiferayPortletRequest _liferayPortletRequest;

	@Mock
	private LiferayPortletResponse _liferayPortletResponse;

	@Mock
	private SearchContext _searchContext;

	@Mock
	private Searcher _searcher;

	@Mock
	private SearchRequestBuilderFactory _searchRequestBuilderFactory;

	@Mock
	private ServiceTrackerMap<String, AssetRendererFactory<?>>
		_serviceTrackerMap;

	private SuggestionsContributorConfiguration
		_suggestionsContributorConfiguration;
	private SXPBlueprintSuggestionsContributor
		_sxpBlueprintSuggestionsContributor;

}