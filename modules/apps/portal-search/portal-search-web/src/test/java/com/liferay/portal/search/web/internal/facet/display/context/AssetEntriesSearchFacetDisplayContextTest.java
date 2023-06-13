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

package com.liferay.portal.search.web.internal.facet.display.context;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.collector.FacetCollector;
import com.liferay.portal.kernel.search.facet.collector.TermCollector;
import com.liferay.portal.kernel.security.permission.ResourceActions;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.web.internal.facet.display.context.builder.AssetEntriesSearchFacetDisplayContextBuilder;
import com.liferay.portal.search.web.internal.type.facet.configuration.TypeFacetPortletInstanceConfiguration;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;
import java.util.Locale;

import javax.portlet.RenderRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Gustavo Lima
 */
public class AssetEntriesSearchFacetDisplayContextTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		Mockito.doReturn(
			_facetCollector
		).when(
			_facet
		).getFacetCollector();
	}

	@Test
	public void testOrderByTermFrequencyAscending() throws Exception {
		String type1 = RandomTestUtil.randomString();
		String type2 = RandomTestUtil.randomString();
		String type3 = RandomTestUtil.randomString();

		String[] classNames = {type1, type2, type3};

		_mockResourceActions(classNames);

		_setUpMultipleTermCollectors(classNames);

		AssetEntriesSearchFacetDisplayContext
			assetEntriesSearchFacetDisplayContext = _createDisplayContext(
				classNames, "count:asc");

		List<BucketDisplayContext> bucketDisplayContexts =
			assetEntriesSearchFacetDisplayContext.getBucketDisplayContexts();

		String nameFrequencyString = _buildNameFrequencyString(
			bucketDisplayContexts);

		Assert.assertEquals(
			bucketDisplayContexts.toString(),
			StringBundler.concat(type1, ":1|", type2, ":2|", type3, ":3"),
			nameFrequencyString);
	}

	@Test
	public void testOrderByTermFrequencyDescending() throws Exception {
		String type1 = RandomTestUtil.randomString();
		String type2 = RandomTestUtil.randomString();
		String type3 = RandomTestUtil.randomString();

		String[] classNames = {type1, type2, type3};

		_mockResourceActions(classNames);

		_setUpMultipleTermCollectors(classNames);

		AssetEntriesSearchFacetDisplayContext
			assetEntriesSearchFacetDisplayContext = _createDisplayContext(
				classNames, "count:desc");

		List<BucketDisplayContext> bucketDisplayContexts =
			assetEntriesSearchFacetDisplayContext.getBucketDisplayContexts();

		String nameFrequencyString = _buildNameFrequencyString(
			bucketDisplayContexts);

		Assert.assertEquals(
			bucketDisplayContexts.toString(),
			StringBundler.concat(type3, ":3|", type2, ":2|", type1, ":1"),
			nameFrequencyString);
	}

	@Test
	public void testOrderByTermValueAscending() throws Exception {
		String[] classNames = {"bravo", "delta", "alpha", "charlie"};

		_mockResourceActions(classNames);

		_setUpMultipleTermCollectors(classNames);

		AssetEntriesSearchFacetDisplayContext
			assetEntriesSearchFacetDisplayContext = _createDisplayContext(
				classNames, "key:asc");

		List<BucketDisplayContext> bucketDisplayContexts =
			assetEntriesSearchFacetDisplayContext.getBucketDisplayContexts();

		String nameFrequencyString = _buildNameFrequencyString(
			bucketDisplayContexts);

		Assert.assertEquals(
			bucketDisplayContexts.toString(),
			"alpha:3|bravo:1|charlie:4|delta:2", nameFrequencyString);
	}

	@Test
	public void testOrderByTermValueDescending() throws Exception {
		String[] classNames = {"bravo", "delta", "alpha", "charlie"};

		_mockResourceActions(classNames);

		_setUpMultipleTermCollectors(classNames);

		AssetEntriesSearchFacetDisplayContext
			assetEntriesSearchFacetDisplayContext1 = _createDisplayContext(
				classNames, "key:desc");

		List<BucketDisplayContext> bucketDisplayContexts =
			assetEntriesSearchFacetDisplayContext1.getBucketDisplayContexts();

		String nameFrequencyString = _buildNameFrequencyString(
			bucketDisplayContexts);

		Assert.assertEquals(
			bucketDisplayContexts.toString(),
			"delta:2|charlie:4|bravo:1|alpha:3", nameFrequencyString);
	}

	private String _buildNameFrequencyString(
		List<BucketDisplayContext> bucketDisplayContexts) {

		StringBundler sb = new StringBundler(bucketDisplayContexts.size() * 4);

		for (BucketDisplayContext bucketDisplayContext :
				bucketDisplayContexts) {

			sb.append(bucketDisplayContext.getBucketText());
			sb.append(StringPool.COLON);
			sb.append(bucketDisplayContext.getFrequency());
			sb.append(StringPool.PIPE);
		}

		sb.setIndex(sb.index() - 1);

		return sb.toString();
	}

	private AssetEntriesSearchFacetDisplayContext _createDisplayContext(
			String[] classNames, String order)
		throws Exception {

		AssetEntriesSearchFacetDisplayContextBuilder
			assetEntriesSearchFacetDisplayContextBuilder =
				new AssetEntriesSearchFacetDisplayContextBuilder(
					_getRenderRequest());

		assetEntriesSearchFacetDisplayContextBuilder.setClassNames(classNames);
		assetEntriesSearchFacetDisplayContextBuilder.setFacet(_facet);
		assetEntriesSearchFacetDisplayContextBuilder.setFrequenciesVisible(
			true);
		assetEntriesSearchFacetDisplayContextBuilder.setFrequencyThreshold(0);
		assetEntriesSearchFacetDisplayContextBuilder.setLocale(LocaleUtil.US);
		assetEntriesSearchFacetDisplayContextBuilder.setOrder(order);
		assetEntriesSearchFacetDisplayContextBuilder.setParameterName(
			_facet.getFieldId());
		assetEntriesSearchFacetDisplayContextBuilder.setParameterValue(
			StringPool.BLANK);

		return assetEntriesSearchFacetDisplayContextBuilder.build();
	}

	private TermCollector _createTermCollector(String term, int frequency) {
		TermCollector termCollector = Mockito.mock(TermCollector.class);

		Mockito.doReturn(
			frequency
		).when(
			termCollector
		).getFrequency();

		Mockito.doReturn(
			term
		).when(
			termCollector
		).getTerm();

		return termCollector;
	}

	private PortletDisplay _getPortletDisplay() throws Exception {
		PortletDisplay portletDisplay = Mockito.mock(PortletDisplay.class);

		Mockito.doReturn(
			Mockito.mock(TypeFacetPortletInstanceConfiguration.class)
		).when(
			portletDisplay
		).getPortletInstanceConfiguration(
			Mockito.any()
		);

		return portletDisplay;
	}

	private RenderRequest _getRenderRequest() throws Exception {
		RenderRequest renderRequest = Mockito.mock(RenderRequest.class);

		Mockito.doReturn(
			_getThemeDisplay()
		).when(
			renderRequest
		).getAttribute(
			WebKeys.THEME_DISPLAY
		);

		return renderRequest;
	}

	private ThemeDisplay _getThemeDisplay() throws Exception {
		ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);

		Mockito.doReturn(
			_getPortletDisplay()
		).when(
			themeDisplay
		).getPortletDisplay();

		return themeDisplay;
	}

	private void _mockResourceActions(String[] classNames) {
		ResourceActionsUtil resourceActionsUtil = new ResourceActionsUtil();

		ResourceActions resourceActions = Mockito.mock(ResourceActions.class);

		resourceActionsUtil.setResourceActions(resourceActions);

		for (String className : classNames) {
			Mockito.doReturn(
				className
			).when(
				resourceActions
			).getModelResource(
				Mockito.any(Locale.class), Mockito.eq(className)
			);
		}
	}

	private void _setUpMultipleTermCollectors(String... terms) {
		int frequency = 1;

		for (String term : terms) {
			Mockito.doReturn(
				_createTermCollector(term, frequency)
			).when(
				_facetCollector
			).getTermCollector(
				term
			);

			frequency++;
		}
	}

	private final Facet _facet = Mockito.mock(Facet.class);
	private final FacetCollector _facetCollector = Mockito.mock(
		FacetCollector.class);

}