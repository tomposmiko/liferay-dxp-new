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

package com.liferay.portal.search.internal.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.blogs.service.BlogsEntryLocalService;
import com.liferay.blogs.test.util.BlogsTestUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.instances.service.PortalInstancesLocalService;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.kernel.search.SearchEngineHelper;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.uuid.PortalUUID;
import com.liferay.portal.search.engine.SearchEngineInformation;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.CountSearchRequest;
import com.liferay.portal.search.engine.adapter.search.CountSearchResponse;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.query.TermQuery;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Bryan Engler
 */
@RunWith(Arquillian.class)
public class IndexWriterHelperImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		Assume.assumeTrue(!isSearchEngine("Solr"));

		reindex(null);

		Thread.sleep(10000);
	}

	@Test
	public void testReindexWithClassName() throws Exception {
		Map<Long, Long> originalCounts = new HashMap<>();

		addBlogEntry();

		populateOriginalCounts(originalCounts, _CLASS_NAME_BLOGS_ENTRY, false);

		reindex(_CLASS_NAME_BLOGS_ENTRY);

		assertReindexedCounts(originalCounts, _CLASS_NAME_BLOGS_ENTRY);
	}

	@Test
	public void testReindexWithoutClassName() throws Exception {
		Map<Long, Long> originalBlogEntryCounts = new HashMap<>();
		Map<Long, Long> originalConfigurationModelCounts = new HashMap<>();

		addBlogEntry();

		populateOriginalCounts(
			originalBlogEntryCounts, _CLASS_NAME_BLOGS_ENTRY, false);

		populateOriginalCounts(
			originalConfigurationModelCounts, _CLASS_NAME_CONFIGURATION_MODEL,
			true);

		reindex(null);

		assertReindexedCounts(originalBlogEntryCounts, _CLASS_NAME_BLOGS_ENTRY);

		assertReindexedCounts(
			originalConfigurationModelCounts, _CLASS_NAME_CONFIGURATION_MODEL);
	}

	@Test
	public void testReindexWithSystemIndexerClassName() throws Exception {
		Map<Long, Long> originalConfigurationModelCounts = new HashMap<>();

		populateOriginalCounts(
			originalConfigurationModelCounts, _CLASS_NAME_CONFIGURATION_MODEL,
			true);

		reindex(_CLASS_NAME_CONFIGURATION_MODEL_INDEXER);

		assertReindexedCounts(
			originalConfigurationModelCounts, _CLASS_NAME_CONFIGURATION_MODEL);
	}

	protected void addBlogEntry() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				TestPropsValues.getGroupId());

		BlogsTestUtil.addEntryWithWorkflow(
			TestPropsValues.getUserId(), RandomTestUtil.randomString(), false,
			serviceContext);
	}

	protected void assertCountEqualsZero(
		String className, long companyId, long count) {

		Assert.assertTrue(
			StringBundler.concat(
				className, " companyId=", companyId, " count=", count),
			count == 0);
	}

	protected void assertCountGreaterThanZero(
		String className, long companyId, long count) {

		Assert.assertTrue(
			StringBundler.concat(
				className, " companyId=", companyId, " count=", count),
			count > 0);
	}

	protected void assertReindexedCounts(
		Map<Long, Long> originalCounts, String className) {

		for (long companyId : getCompanyIds()) {
			CountSearchRequest countSearchRequest = new CountSearchRequest();

			countSearchRequest.setIndexNames("liferay-" + companyId);

			TermQuery termQuery = _queries.term(
				Field.ENTRY_CLASS_NAME, className);

			countSearchRequest.setQuery(termQuery);

			CountSearchResponse countSearchResponse =
				_searchEngineAdapter.execute(countSearchRequest);

			Long originalCount = originalCounts.get(companyId);

			Long newCount = Long.valueOf(countSearchResponse.getCount());

			Assert.assertEquals(
				className + " companyId=" + companyId, originalCount, newCount);
		}
	}

	protected long[] getCompanyIds() {
		long[] companyIds = _portalInstancesLocalService.getCompanyIds();

		if (!ArrayUtil.contains(companyIds, CompanyConstants.SYSTEM)) {
			companyIds = ArrayUtil.append(
				new long[] {CompanyConstants.SYSTEM}, companyIds);
		}

		return companyIds;
	}

	protected boolean isSearchEngine(String engine) {
		SearchEngine searchEngine = _searchEngineHelper.getSearchEngine(
			_searchEngineHelper.getDefaultSearchEngineId());

		String vendor = searchEngine.getVendor();

		if (engine.equals("Elasticsearch7")) {
			String version = _searchEngineInformation.getClientVersionString();

			if (vendor.equals("Elasticsearch") && version.startsWith("7")) {
				return true;
			}

			return false;
		}

		return vendor.equals(engine);
	}

	protected void populateOriginalCounts(
		Map<Long, Long> originalCounts, String className,
		boolean systemIndexer) {

		for (long companyId : getCompanyIds()) {
			CountSearchRequest countSearchRequest = new CountSearchRequest();

			countSearchRequest.setIndexNames("liferay-" + companyId);

			TermQuery termQuery = _queries.term(
				Field.ENTRY_CLASS_NAME, className);

			countSearchRequest.setQuery(termQuery);

			CountSearchResponse countSearchResponse =
				_searchEngineAdapter.execute(countSearchRequest);

			if (systemIndexer) {
				if (companyId == CompanyConstants.SYSTEM) {
					assertCountGreaterThanZero(
						className, companyId, countSearchResponse.getCount());
				}
				else {
					assertCountEqualsZero(
						className, companyId, countSearchResponse.getCount());
				}
			}
			else {
				if (companyId == CompanyConstants.SYSTEM) {
					assertCountEqualsZero(
						className, companyId, countSearchResponse.getCount());
				}
				else {
					assertCountGreaterThanZero(
						className, companyId, countSearchResponse.getCount());
				}
			}

			originalCounts.put(companyId, countSearchResponse.getCount());
		}
	}

	protected void reindex(String className) throws Exception {
		String jobName = "reindex-".concat(_portalUUID.generate());

		_indexWriterHelper.reindex(
			UserConstants.USER_ID_DEFAULT, jobName, getCompanyIds(), className,
			null);
	}

	private static final String _CLASS_NAME_BLOGS_ENTRY =
		"com.liferay.blogs.model.BlogsEntry";

	private static final String _CLASS_NAME_CONFIGURATION_MODEL =
		"com.liferay.configuration.admin.web.internal.model.ConfigurationModel";

	private static final String _CLASS_NAME_CONFIGURATION_MODEL_INDEXER =
		"com.liferay.configuration.admin.web.internal.search." +
			"ConfigurationModelIndexer";

	@Inject
	private static SearchEngineHelper _searchEngineHelper;

	@Inject
	private static SearchEngineInformation _searchEngineInformation;

	@Inject
	private BlogsEntryLocalService _blogsEntryLocalService;

	@Inject
	private IndexWriterHelper _indexWriterHelper;

	@Inject
	private PortalInstancesLocalService _portalInstancesLocalService;

	@Inject
	private PortalUUID _portalUUID;

	@Inject
	private Queries _queries;

	@Inject
	private SearchEngineAdapter _searchEngineAdapter;

}