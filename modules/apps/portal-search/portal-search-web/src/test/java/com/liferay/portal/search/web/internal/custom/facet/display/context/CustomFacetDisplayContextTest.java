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

package com.liferay.portal.search.web.internal.custom.facet.display.context;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.search.web.internal.BaseFacetDisplayContextTestCase;
import com.liferay.portal.search.web.internal.custom.facet.configuration.CustomFacetPortletInstanceConfiguration;
import com.liferay.portal.search.web.internal.custom.facet.display.context.builder.CustomFacetDisplayContextBuilder;
import com.liferay.portal.search.web.internal.facet.display.context.BucketDisplayContext;
import com.liferay.portal.search.web.internal.facet.display.context.FacetDisplayContext;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Wade Cao
 */
public class CustomFacetDisplayContextTest
	extends BaseFacetDisplayContextTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Override
	public FacetDisplayContext createFacetDisplayContext(String parameterValue)
		throws Exception {

		return _createDisplayContext(
			"customDisplayCaption", "fieldToAggregate", parameterValue,
			"count:desc");
	}

	@Test
	public void testEmptyCustomDisplayCaption() throws Exception {
		String customDisplayCaption = "";
		String fieldToAggregate = "groupId";

		CustomFacetDisplayContext customFacetDisplayContext =
			_createDisplayContext(
				customDisplayCaption, fieldToAggregate,
				getFacetDisplayContextParameterValue());

		List<BucketDisplayContext> bucketDisplayContexts =
			customFacetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 0, bucketDisplayContexts.size());

		Assert.assertTrue(customFacetDisplayContext.isNothingSelected());
		Assert.assertTrue(customFacetDisplayContext.isRenderNothing());
		Assert.assertEquals(
			fieldToAggregate, customFacetDisplayContext.getDisplayCaption());
	}

	@Override
	@Test
	public void testEmptySearchResults() throws Exception {
	}

	@Override
	protected void testOrderBy(
			int[] expectedFrequencies, String[] expectedTerms,
			int[] frequencies, String order, String[] terms)
		throws Exception {

		setUpTermCollectors(
			facetCollector, getTermCollectors(terms, frequencies));

		CustomFacetDisplayContext customFacetDisplayContext =
			_createDisplayContext(
				"customDisplayCaption", "fieldToAggregate", StringPool.BLANK,
				order);

		assertFacetOrder(
			customFacetDisplayContext.getBucketDisplayContexts(), expectedTerms,
			expectedFrequencies);
	}

	private CustomFacetDisplayContext _createDisplayContext(
			String customDisplayCaption, String fieldToAggregate,
			String parameterValue)
		throws Exception {

		return _createDisplayContext(
			customDisplayCaption, fieldToAggregate, parameterValue,
			"count:desc");
	}

	private CustomFacetDisplayContext _createDisplayContext(
			String customDisplayCaption, String fieldToAggregate,
			String parameterValue, String order)
		throws Exception {

		CustomFacetDisplayContextBuilder customFacetDisplayContextBuilder =
			new CustomFacetDisplayContextBuilder(
				getHttpServletRequest(
					CustomFacetPortletInstanceConfiguration.class));

		customFacetDisplayContextBuilder.setCustomDisplayCaption(
			customDisplayCaption);
		customFacetDisplayContextBuilder.setFacet(facet);
		customFacetDisplayContextBuilder.setFieldToAggregate(fieldToAggregate);
		customFacetDisplayContextBuilder.setFrequenciesVisible(true);
		customFacetDisplayContextBuilder.setFrequencyThreshold(0);
		customFacetDisplayContextBuilder.setMaxTerms(0);
		customFacetDisplayContextBuilder.setOrder(order);
		customFacetDisplayContextBuilder.setParameterName("custom");
		customFacetDisplayContextBuilder.setParameterValue(parameterValue);

		return customFacetDisplayContextBuilder.build();
	}

}