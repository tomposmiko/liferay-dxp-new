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

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.module.configuration.ConfigurationProviderUtil;
import com.liferay.portal.kernel.search.facet.collector.TermCollector;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.search.configuration.CategoryFacetFieldConfiguration;
import com.liferay.portal.search.web.internal.BaseFacetDisplayContextTestCase;
import com.liferay.portal.search.web.internal.category.facet.configuration.CategoryFacetPortletInstanceConfiguration;
import com.liferay.portal.search.web.internal.facet.display.context.builder.AssetCategoriesSearchFacetDisplayContextBuilder;
import com.liferay.portal.search.web.internal.facet.display.context.builder.AssetCategoryPermissionChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.portlet.RenderRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author André de Oliveira
 */
public abstract class BaseCategoriesSearchFacetDisplayContextTestCase
	extends BaseFacetDisplayContextTestCase {

	@Override
	public FacetDisplayContext createFacetDisplayContext(String parameterValue)
		throws ConfigurationException {

		return createFacetDisplayContext(parameterValue, "count:desc");
	}

	@Override
	public FacetDisplayContext createFacetDisplayContext(
			String parameterValue, String order)
		throws ConfigurationException {

		RenderRequest renderRequest = Mockito.mock(RenderRequest.class);

		AssetCategoriesSearchFacetDisplayContextBuilder
			assetCategoriesSearchFacetDisplayContextBuilder =
				new AssetCategoriesSearchFacetDisplayContextBuilder(
					renderRequest);

		assetCategoriesSearchFacetDisplayContextBuilder.
			setAssetCategoryLocalService(_assetCategoryLocalService);
		assetCategoriesSearchFacetDisplayContextBuilder.
			setAssetCategoryPermissionChecker(_assetCategoryPermissionChecker);
		assetCategoriesSearchFacetDisplayContextBuilder.
			setAssetVocabularyLocalService(_assetVocabularyLocalService);
		assetCategoriesSearchFacetDisplayContextBuilder.setDisplayStyle(
			"cloud");
		assetCategoriesSearchFacetDisplayContextBuilder.setFacet(facet);
		assetCategoriesSearchFacetDisplayContextBuilder.setFrequenciesVisible(
			true);
		assetCategoriesSearchFacetDisplayContextBuilder.setFrequencyThreshold(
			0);
		assetCategoriesSearchFacetDisplayContextBuilder.setLocale(
			LocaleUtil.getDefault());
		assetCategoriesSearchFacetDisplayContextBuilder.setMaxTerms(0);
		assetCategoriesSearchFacetDisplayContextBuilder.setOrder(order);
		assetCategoriesSearchFacetDisplayContextBuilder.setParameterName(
			facet.getFieldId());
		assetCategoriesSearchFacetDisplayContextBuilder.setParameterValue(
			parameterValue);
		assetCategoriesSearchFacetDisplayContextBuilder.setPortal(_getPortal());

		if (_excludedGroupId > 0) {
			assetCategoriesSearchFacetDisplayContextBuilder.setExcludedGroupId(
				_excludedGroupId);
		}

		return assetCategoriesSearchFacetDisplayContextBuilder.build();
	}

	@Before
	@Override
	public void setUp() throws Exception {
		setUpAssetVocabularyLocalService();
		setUpConfigurationProvider();
		setUpFacet();
	}

	@Test
	public void testExcludedGroup() throws Exception {
		long assetCategoryId = RandomTestUtil.randomLong();

		long groupId = RandomTestUtil.randomLong();

		long stagingGroupId = RandomTestUtil.randomLong();

		createGroup(groupId, stagingGroupId);

		_setUpAssetCategory(assetCategoryId, stagingGroupId);

		_excludedGroupId = stagingGroupId;

		int frequency = RandomTestUtil.randomInt();

		setUpOneTermCollector(assetCategoryId, frequency);

		String facetParam = StringPool.BLANK;

		FacetDisplayContext facetDisplayContext = createFacetDisplayContext(
			facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			facetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 0, bucketDisplayContexts.size());

		_excludedGroupId = 0;
	}

	@Override
	@Test
	public void testOneTerm() throws Exception {
		long assetCategoryId = RandomTestUtil.randomLong();

		_setUpAssetCategory(assetCategoryId, 0);

		int frequency = RandomTestUtil.randomInt();

		setUpOneTermCollector(assetCategoryId, frequency);

		String facetParam = StringPool.BLANK;

		FacetDisplayContext facetDisplayContext = createFacetDisplayContext(
			facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			facetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(
			String.valueOf(assetCategoryId),
			bucketDisplayContext.getBucketText());
		Assert.assertEquals(
			String.valueOf(assetCategoryId),
			bucketDisplayContext.getFilterValue());
		Assert.assertEquals(frequency, bucketDisplayContext.getFrequency());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());
		Assert.assertFalse(bucketDisplayContext.isSelected());

		Assert.assertEquals(
			facetParam, facetDisplayContext.getParameterValue());
		Assert.assertTrue(facetDisplayContext.isNothingSelected());
		Assert.assertFalse(facetDisplayContext.isRenderNothing());
	}

	@Override
	@Test
	public void testOneTermWithPreviousSelection() throws Exception {
		long assetCategoryId = RandomTestUtil.randomLong();

		_setUpAssetCategory(assetCategoryId, 0);

		int frequency = RandomTestUtil.randomInt();

		setUpOneTermCollector(assetCategoryId, frequency);

		FacetDisplayContext facetDisplayContext = createFacetDisplayContext(
			String.valueOf(assetCategoryId));

		List<BucketDisplayContext> bucketDisplayContexts =
			facetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 1, bucketDisplayContexts.size());

		BucketDisplayContext bucketDisplayContext = bucketDisplayContexts.get(
			0);

		Assert.assertEquals(
			String.valueOf(assetCategoryId),
			bucketDisplayContext.getBucketText());
		Assert.assertEquals(
			String.valueOf(assetCategoryId),
			bucketDisplayContext.getFilterValue());
		Assert.assertEquals(frequency, bucketDisplayContext.getFrequency());
		Assert.assertTrue(bucketDisplayContext.isFrequencyVisible());
		Assert.assertTrue(bucketDisplayContext.isSelected());

		Assert.assertEquals(
			assetCategoryId,
			GetterUtil.getLong(facetDisplayContext.getParameterValue()));
		Assert.assertFalse(facetDisplayContext.isNothingSelected());
		Assert.assertFalse(facetDisplayContext.isRenderNothing());
	}

	@Override
	@Test
	public void testOrderByTermFrequencyAscending() throws Exception {
		long[] assetCategoryIds = {3L, 4L, 2L, 1L};

		List<TermCollector> termCollectors = _getTermCollectors(
			assetCategoryIds, new int[] {6, 5, 5, 4});

		String[] expectedCategoryIds = {"1", "2", "4", "3"};
		int[] expectedFrequencies = {4, 5, 5, 6};

		_testOrderBy(
			assetCategoryIds, termCollectors, "count:asc", expectedCategoryIds,
			expectedFrequencies);
	}

	@Override
	@Test
	public void testOrderByTermFrequencyDescending() throws Exception {
		long[] assetCategoryIds = {3L, 4L, 2L, 1L};

		List<TermCollector> termCollectors = _getTermCollectors(
			assetCategoryIds, new int[] {6, 5, 5, 4});

		String[] expectedCategoryIds = {"3", "2", "4", "1"};
		int[] expectedFrequencies = {6, 5, 5, 4};

		_testOrderBy(
			assetCategoryIds, termCollectors, "count:desc", expectedCategoryIds,
			expectedFrequencies);
	}

	@Override
	@Test
	public void testOrderByTermValueAscending() throws Exception {
		long[] assetCategoryIds = {2L, 1L, 2L, 3L};

		List<TermCollector> termCollectors = _getTermCollectors(
			assetCategoryIds);

		String[] expectedCategoryIds = {"1", "2", "2", "3"};
		int[] expectedFrequencies = {2, 3, 1, 4};

		_testOrderBy(
			assetCategoryIds, termCollectors, "key:asc", expectedCategoryIds,
			expectedFrequencies);
	}

	@Override
	@Test
	public void testOrderByTermValueDescending() throws Exception {
		long[] assetCategoryIds = {2L, 1L, 2L, 3L};

		List<TermCollector> termCollectors = _getTermCollectors(
			assetCategoryIds);

		String[] expectedCategoryIds = {"3", "2", "2", "1"};
		int[] expectedFrequencies = {4, 3, 1, 2};

		_testOrderBy(
			assetCategoryIds, termCollectors, "key:desc", expectedCategoryIds,
			expectedFrequencies);
	}

	@Test
	public void testUnauthorized() throws Exception {
		long assetCategoryId = RandomTestUtil.randomLong();

		_setUpAssetCategoryUnauthorized(assetCategoryId);

		int frequency = RandomTestUtil.randomInt();

		setUpOneTermCollector(assetCategoryId, frequency);

		String facetParam = StringPool.BLANK;

		FacetDisplayContext facetDisplayContext = createFacetDisplayContext(
			facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			facetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 0, bucketDisplayContexts.size());

		Assert.assertEquals(
			facetParam, facetDisplayContext.getParameterValue());
		Assert.assertTrue(facetDisplayContext.isNothingSelected());
		Assert.assertTrue(facetDisplayContext.isRenderNothing());
	}

	@Test
	public void testUnauthorizedWithPreviousSelection() throws Exception {
		long assetCategoryId = RandomTestUtil.randomLong();

		_setUpAssetCategoryUnauthorized(assetCategoryId);

		String facetParam = String.valueOf(assetCategoryId);

		FacetDisplayContext facetDisplayContext = createFacetDisplayContext(
			facetParam);

		List<BucketDisplayContext> bucketDisplayContexts =
			facetDisplayContext.getBucketDisplayContexts();

		Assert.assertEquals(
			bucketDisplayContexts.toString(), 0, bucketDisplayContexts.size());

		Assert.assertEquals(
			facetParam, facetDisplayContext.getParameterValue());
		Assert.assertFalse(facetDisplayContext.isNothingSelected());
		Assert.assertFalse(facetDisplayContext.isRenderNothing());
	}

	protected Group createGroup(long groupId, long stagingGroupId) {
		Group group = Mockito.mock(Group.class);

		Mockito.doReturn(
			groupId
		).when(
			group
		).getGroupId();

		return group;
	}

	@Override
	protected String createTerm() {
		return String.valueOf(RandomTestUtil.randomLong());
	}

	protected TermCollector createTermCollector(
		long assetCategoryId, int frequency) {

		TermCollector termCollector = Mockito.mock(TermCollector.class);

		Mockito.doReturn(
			frequency
		).when(
			termCollector
		).getFrequency();

		if (_isLegacyField()) {
			Mockito.doReturn(
				String.valueOf(assetCategoryId)
			).when(
				termCollector
			).getTerm();
		}
		else {
			Mockito.doReturn(
				"vocabularyId-" + assetCategoryId
			).when(
				termCollector
			).getTerm();
		}

		return termCollector;
	}

	protected abstract String getFacetFieldName();

	protected ThemeDisplay getThemeDisplay() {
		ThemeDisplay themeDisplay = Mockito.mock(ThemeDisplay.class);

		Mockito.doReturn(
			Mockito.mock(PortletDisplay.class)
		).when(
			themeDisplay
		).getPortletDisplay();

		return themeDisplay;
	}

	@Override
	protected void setUpAsset(String assetCategoryId) throws Exception {
		_groupId = RandomTestUtil.randomLong();

		_setUpAssetCategory(GetterUtil.getLong(assetCategoryId), _groupId);
	}

	protected void setUpAssetVocabularyLocalService() {
		AssetVocabulary assetVocabulary = Mockito.mock(AssetVocabulary.class);

		Mockito.doReturn(
			"name"
		).when(
			assetVocabulary
		).getTitle(
			Mockito.any(Locale.class)
		);

		Mockito.doReturn(
			assetVocabulary
		).when(
			_assetVocabularyLocalService
		).fetchAssetVocabulary(
			Mockito.anyLong()
		);
	}

	protected void setUpConfigurationProvider() throws Exception {
		Mockito.doReturn(
			getFacetFieldName()
		).when(
			_categoryFacetFieldConfiguration
		).categoryFacetField();

		Mockito.doReturn(
			_categoryFacetFieldConfiguration
		).when(
			_configurationProvider
		).getSystemConfiguration(
			Mockito.any(Class.class)
		);

		ReflectionTestUtil.setFieldValue(
			ConfigurationProviderUtil.class, "_configurationProvider",
			_configurationProvider);
	}

	protected void setUpFacet() throws Exception {
		super.setUp();

		Mockito.doReturn(
			getFacetFieldName()
		).when(
			facet
		).getFieldName();
	}

	protected void setUpOneTermCollector(long assetCategoryId, int frequency) {
		Mockito.doReturn(
			Collections.singletonList(
				createTermCollector(assetCategoryId, frequency))
		).when(
			facetCollector
		).getTermCollectors();
	}

	private AssetCategory _createAssetCategory(
		long assetCategoryId, long groupId) {

		AssetCategory assetCategory = Mockito.mock(AssetCategory.class);

		Mockito.doReturn(
			assetCategoryId
		).when(
			assetCategory
		).getCategoryId();

		Mockito.doReturn(
			groupId
		).when(
			assetCategory
		).getGroupId();

		Mockito.doReturn(
			String.valueOf(assetCategoryId)
		).when(
			assetCategory
		).getTitle(
			(Locale)Mockito.any()
		);

		Mockito.doReturn(
			assetCategory
		).when(
			_assetCategoryLocalService
		).fetchAssetCategory(
			assetCategoryId
		);

		return assetCategory;
	}

	private Portal _getPortal() throws ConfigurationException {
		Portal portal = Mockito.mock(Portal.class);

		Mockito.doReturn(
			getHttpServletRequest(
				CategoryFacetPortletInstanceConfiguration.class)
		).when(
			portal
		).getHttpServletRequest(
			Mockito.any()
		);

		return portal;
	}

	private List<TermCollector> _getTermCollectors(long... assetCategoryIds) {
		int[] frequencies = new int[assetCategoryIds.length];

		for (int i = 0; i < assetCategoryIds.length; i++) {
			frequencies[i] = i + 1;
		}

		return _getTermCollectors(assetCategoryIds, frequencies);
	}

	private List<TermCollector> _getTermCollectors(
		long[] assetCategoryIds, int[] frequencies) {

		List<TermCollector> termCollectors = new ArrayList<>();

		for (int i = 0; i < assetCategoryIds.length; i++) {
			termCollectors.add(
				createTermCollector(assetCategoryIds[i], frequencies[i]));
		}

		return termCollectors;
	}

	private boolean _isLegacyField() {
		String fieldName = getFacetFieldName();

		return fieldName.equals("assetCategoryIds");
	}

	private void _setUpAssetCategory(long assetCategoryId, long groupId) {
		AssetCategory assetCategory = _createAssetCategory(
			assetCategoryId, groupId);

		Mockito.doReturn(
			true
		).when(
			_assetCategoryPermissionChecker
		).hasPermission(
			assetCategory
		);
	}

	private void _setUpAssetCategoryUnauthorized(long assetCategoryId) {
		AssetCategory assetCategory = _createAssetCategory(assetCategoryId, 0);

		Mockito.doReturn(
			false
		).when(
			_assetCategoryPermissionChecker
		).hasPermission(
			assetCategory
		);
	}

	private void _setUpMultipleAssetCategory(long[] assetCategoryId) {
		for (int i = 0; i < assetCategoryId.length; i++) {
			AssetCategory assetCategory = _createAssetCategory(
				assetCategoryId[i], i);

			Mockito.doReturn(
				true
			).when(
				_assetCategoryPermissionChecker
			).hasPermission(
				assetCategory
			);
		}
	}

	private void _testOrderBy(
			long[] assetCategoryIds, List<TermCollector> termCollectors,
			String order, String[] expectedTerms, int[] expectedFrequencies)
		throws Exception {

		_setUpMultipleAssetCategory(assetCategoryIds);

		setUpTermCollectors(facetCollector, termCollectors);

		FacetDisplayContext facetDisplayContext = createFacetDisplayContext(
			StringPool.BLANK, order);

		assertFacetOrder(
			facetDisplayContext.getBucketDisplayContexts(), expectedTerms,
			expectedFrequencies);
	}

	private final AssetCategoryLocalService _assetCategoryLocalService =
		Mockito.mock(AssetCategoryLocalService.class);
	private final AssetCategoryPermissionChecker
		_assetCategoryPermissionChecker = Mockito.mock(
			AssetCategoryPermissionChecker.class);
	private final AssetVocabularyLocalService _assetVocabularyLocalService =
		Mockito.mock(AssetVocabularyLocalService.class);
	private final CategoryFacetFieldConfiguration
		_categoryFacetFieldConfiguration = Mockito.mock(
			CategoryFacetFieldConfiguration.class);
	private final ConfigurationProvider _configurationProvider = Mockito.mock(
		ConfigurationProvider.class);
	private long _excludedGroupId;
	private long _groupId;

}