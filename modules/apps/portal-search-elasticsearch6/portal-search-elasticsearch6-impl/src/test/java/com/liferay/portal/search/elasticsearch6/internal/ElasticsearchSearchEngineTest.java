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

package com.liferay.portal.search.elasticsearch6.internal;

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnection;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchFixture;
import com.liferay.portal.search.elasticsearch6.internal.connection.TestElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch6.internal.facet.DefaultFacetProcessor;
import com.liferay.portal.search.elasticsearch6.internal.facet.FacetProcessor;
import com.liferay.portal.search.elasticsearch6.internal.index.CompanyIdIndexNameBuilder;
import com.liferay.portal.search.elasticsearch6.internal.index.CompanyIndexFactory;
import com.liferay.portal.search.elasticsearch6.internal.index.IndexNameBuilder;
import com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.ElasticsearchEngineAdapterFixture;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;

import java.util.List;

import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotAction;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotRequestBuilder;
import org.elasticsearch.action.admin.cluster.snapshots.delete.DeleteSnapshotAction;
import org.elasticsearch.action.admin.cluster.snapshots.delete.DeleteSnapshotRequestBuilder;
import org.elasticsearch.action.admin.cluster.snapshots.get.GetSnapshotsAction;
import org.elasticsearch.action.admin.cluster.snapshots.get.GetSnapshotsRequestBuilder;
import org.elasticsearch.action.admin.cluster.snapshots.get.GetSnapshotsResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.snapshots.SnapshotInfo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author André de Oliveira
 */
public class ElasticsearchSearchEngineTest {

	@Before
	public void setUp() throws Exception {
		_elasticsearchFixture = new ElasticsearchFixture(
			ElasticsearchSearchEngineTest.class.getSimpleName());

		_elasticsearchFixture.setUp();

		_elasticsearchConnectionManager =
			new TestElasticsearchConnectionManager(_elasticsearchFixture);

		ElasticsearchEngineAdapterFixture elasticsearchEngineAdapterFixture =
			new ElasticsearchEngineAdapterFixture(
				_elasticsearchConnectionManager, _facetProcessor);

		_searchEngineAdapter =
			elasticsearchEngineAdapterFixture.getSearchEngineAdapter();
	}

	@After
	public void tearDown() throws Exception {
		_elasticsearchFixture.tearDown();
	}

	@Test
	public void testBackup() throws SearchException {
		ElasticsearchSearchEngine elasticsearchSearchEngine =
			createElasticsearchSearchEngine(
				_elasticsearchConnectionManager, _searchEngineAdapter);

		long companyId = RandomTestUtil.randomLong();

		elasticsearchSearchEngine.initialize(companyId);

		elasticsearchSearchEngine.backup(companyId, "backup_test");

		GetSnapshotsRequestBuilder getSnapshotsRequestBuilder =
			GetSnapshotsAction.INSTANCE.newRequestBuilder(
				_elasticsearchFixture.getClient());

		getSnapshotsRequestBuilder.setIgnoreUnavailable(true);
		getSnapshotsRequestBuilder.setRepository("liferay_backup");
		getSnapshotsRequestBuilder.setSnapshots("backup_test");

		GetSnapshotsResponse getSnapshotsResponse =
			getSnapshotsRequestBuilder.get();

		List<SnapshotInfo> snapshotInfos = getSnapshotsResponse.getSnapshots();

		Assert.assertTrue(snapshotInfos.size() == 1);

		DeleteSnapshotRequestBuilder deleteSnapshotRequestBuilder =
			DeleteSnapshotAction.INSTANCE.newRequestBuilder(
				_elasticsearchFixture.getClient());

		deleteSnapshotRequestBuilder.setRepository("liferay_backup");
		deleteSnapshotRequestBuilder.setSnapshot("backup_test");

		deleteSnapshotRequestBuilder.get();
	}

	@Test
	public void testInitializeAfterReconnect() {
		ElasticsearchSearchEngine elasticsearchSearchEngine =
			createElasticsearchSearchEngine(
				_elasticsearchConnectionManager, _searchEngineAdapter);

		long companyId = RandomTestUtil.randomLong();

		elasticsearchSearchEngine.initialize(companyId);

		reconnect(_elasticsearchConnectionManager);

		elasticsearchSearchEngine.initialize(companyId);
	}

	@Test
	public void testRestore() throws SearchException {
		ElasticsearchSearchEngine elasticsearchSearchEngine =
			createElasticsearchSearchEngine(
				_elasticsearchConnectionManager, _searchEngineAdapter);

		long companyId = RandomTestUtil.randomLong();

		elasticsearchSearchEngine.initialize(companyId);

		elasticsearchSearchEngine.createBackupRepository();

		CreateSnapshotRequestBuilder createSnapshotRequestBuilder =
			CreateSnapshotAction.INSTANCE.newRequestBuilder(
				_elasticsearchFixture.getClient());

		createSnapshotRequestBuilder.setIndices(String.valueOf(companyId));
		createSnapshotRequestBuilder.setRepository("liferay_backup");
		createSnapshotRequestBuilder.setSnapshot("restore_test");
		createSnapshotRequestBuilder.setWaitForCompletion(true);

		createSnapshotRequestBuilder.get();

		elasticsearchSearchEngine.restore(companyId, "restore_test");

		DeleteSnapshotRequestBuilder deleteSnapshotRequestBuilder =
			DeleteSnapshotAction.INSTANCE.newRequestBuilder(
				_elasticsearchFixture.getClient());

		deleteSnapshotRequestBuilder.setRepository("liferay_backup");
		deleteSnapshotRequestBuilder.setSnapshot("restore_test");

		deleteSnapshotRequestBuilder.get();
	}

	protected CompanyIndexFactory createCompanyIndexFactory() {
		return new CompanyIndexFactory() {
			{
				indexNameBuilder = createIndexNameBuilder();
				jsonFactory = new JSONFactoryImpl();
			}
		};
	}

	protected ElasticsearchConnectionManager
		createElasticsearchConnectionManager() {

		return new TestElasticsearchConnectionManager(_elasticsearchFixture);
	}

	protected ElasticsearchSearchEngine createElasticsearchSearchEngine(
		final ElasticsearchConnectionManager elasticsearchConnectionManager2,
		final SearchEngineAdapter searchEngineAdapter1) {

		return new ElasticsearchSearchEngine() {
			{
				indexFactory = createCompanyIndexFactory();
				indexNameBuilder = String::valueOf;
				elasticsearchConnectionManager =
					elasticsearchConnectionManager2;
				searchEngineAdapter = searchEngineAdapter1;
			}
		};
	}

	protected IndexNameBuilder createIndexNameBuilder() {
		return new CompanyIdIndexNameBuilder() {
			{
				setIndexNamePrefix(null);
			}
		};
	}

	protected void reconnect(
		ElasticsearchConnectionManager elasticsearchConnectionManager) {

		ElasticsearchConnection elasticsearchConnection =
			elasticsearchConnectionManager.getElasticsearchConnection();

		elasticsearchConnection.close();

		elasticsearchConnectionManager.connect();
	}

	private ElasticsearchConnectionManager _elasticsearchConnectionManager;
	private ElasticsearchFixture _elasticsearchFixture;
	private final FacetProcessor<SearchRequestBuilder> _facetProcessor =
		new DefaultFacetProcessor();
	private SearchEngineAdapter _searchEngineAdapter;

}