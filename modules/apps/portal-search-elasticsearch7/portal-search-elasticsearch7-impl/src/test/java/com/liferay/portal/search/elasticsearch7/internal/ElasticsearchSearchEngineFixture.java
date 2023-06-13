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

package com.liferay.portal.search.elasticsearch7.internal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConfiguration;
import com.liferay.portal.search.elasticsearch7.internal.configuration.ElasticsearchConfigurationWrapper;
import com.liferay.portal.search.elasticsearch7.internal.configuration.OperationModeResolver;
import com.liferay.portal.search.elasticsearch7.internal.connection.ElasticsearchClientResolver;
import com.liferay.portal.search.elasticsearch7.internal.connection.ElasticsearchConnectionFixture;
import com.liferay.portal.search.elasticsearch7.internal.connection.ElasticsearchConnectionManager;
import com.liferay.portal.search.elasticsearch7.internal.index.CompanyIdIndexNameBuilder;
import com.liferay.portal.search.elasticsearch7.internal.index.CompanyIndexFactory;
import com.liferay.portal.search.elasticsearch7.internal.search.engine.adapter.ElasticsearchEngineAdapterFixture;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.portal.search.test.util.search.engine.SearchEngineFixture;

import java.util.Map;
import java.util.Objects;

import org.osgi.framework.BundleContext;

/**
 * @author Adam Brandizzi
 */
public class ElasticsearchSearchEngineFixture implements SearchEngineFixture {

	public ElasticsearchSearchEngineFixture(
		ElasticsearchConnectionFixture elasticsearchConnectionFixture) {

		_elasticsearchConnectionFixture = elasticsearchConnectionFixture;
	}

	public ElasticsearchConnectionManager getElasticsearchConnectionManager() {
		return _elasticsearchConnectionManager;
	}

	public ElasticsearchSearchEngine getElasticsearchSearchEngine() {
		return _elasticsearchSearchEngine;
	}

	@Override
	public IndexNameBuilder getIndexNameBuilder() {
		return _indexNameBuilder;
	}

	@Override
	public SearchEngine getSearchEngine() {
		return getElasticsearchSearchEngine();
	}

	@Override
	public void setUp() throws Exception {
		ElasticsearchConnectionFixture elasticsearchConnectionFixture =
			Objects.requireNonNull(_elasticsearchConnectionFixture);

		CompanyIdIndexNameBuilder indexNameBuilder = _createIndexNameBuilder();

		ElasticsearchConnectionManager elasticsearchConnectionManager =
			_createElasticsearchConnectionManager(
				elasticsearchConnectionFixture);

		_elasticsearchConnectionManager = elasticsearchConnectionManager;
		_elasticsearchSearchEngine = _createElasticsearchSearchEngine(
			elasticsearchConnectionFixture, elasticsearchConnectionManager,
			indexNameBuilder,
			elasticsearchConnectionFixture.
				getElasticsearchConfigurationProperties());

		_indexNameBuilder = indexNameBuilder;
	}

	@Override
	public void tearDown() throws Exception {
		_elasticsearchConnectionFixture.destroyNode();

		_elasticsearchEngineAdapterFixture.tearDown();

		if (_companyIndexFactory != null) {
			ReflectionTestUtil.invoke(
				_companyIndexFactory, "deactivate", new Class<?>[0]);

			_companyIndexFactory = null;
		}
	}

	protected static ElasticsearchConfigurationWrapper
		createElasticsearchConfigurationWrapper(
			Map<String, Object> properties) {

		return new ElasticsearchConfigurationWrapper() {
			{
				setElasticsearchConfiguration(
					ConfigurableUtil.createConfigurable(
						ElasticsearchConfiguration.class, properties));
			}
		};
	}

	private CompanyIndexFactory _createCompanyIndexFactory(
		IndexNameBuilder indexNameBuilder, Map<String, Object> properites) {

		_companyIndexFactory = new CompanyIndexFactory();

		ReflectionTestUtil.setFieldValue(
			_companyIndexFactory, "_elasticsearchConfigurationWrapper",
			createElasticsearchConfigurationWrapper(properites));
		ReflectionTestUtil.setFieldValue(
			_companyIndexFactory, "_indexNameBuilder", indexNameBuilder);
		ReflectionTestUtil.setFieldValue(
			_companyIndexFactory, "_jsonFactory", new JSONFactoryImpl());

		ReflectionTestUtil.invoke(
			_companyIndexFactory, "activate",
			new Class<?>[] {BundleContext.class},
			SystemBundleUtil.getBundleContext());

		return _companyIndexFactory;
	}

	private ElasticsearchConnectionManager
		_createElasticsearchConnectionManager(
			ElasticsearchConnectionFixture elasticsearchConnectionFixture) {

		return new ElasticsearchConnectionManager() {
			{
				elasticsearchConfigurationWrapper =
					createElasticsearchConfigurationWrapper(
						elasticsearchConnectionFixture.
							getElasticsearchConfigurationProperties());

				operationModeResolver = _createOperationModeResolver(
					elasticsearchConfigurationWrapper);

				addElasticsearchConnection(
					elasticsearchConnectionFixture.
						createElasticsearchConnection());
			}
		};
	}

	private ElasticsearchSearchEngine _createElasticsearchSearchEngine(
		ElasticsearchClientResolver elasticsearchClientResolver,
		ElasticsearchConnectionManager elasticsearchConnectionManager,
		IndexNameBuilder indexNameBuilder, Map<String, Object> properites) {

		ElasticsearchSearchEngine elasticsearchSearchEngine =
			new ElasticsearchSearchEngine();

		ReflectionTestUtil.setFieldValue(
			elasticsearchSearchEngine, "_elasticsearchConnectionManager",
			elasticsearchConnectionManager);
		ReflectionTestUtil.setFieldValue(
			elasticsearchSearchEngine, "_indexFactory",
			_createCompanyIndexFactory(indexNameBuilder, properites));
		ReflectionTestUtil.setFieldValue(
			elasticsearchSearchEngine, "_indexNameBuilder",
			(IndexNameBuilder)String::valueOf);
		ReflectionTestUtil.setFieldValue(
			elasticsearchSearchEngine, "_searchEngineAdapter",
			_createSearchEngineAdapter(elasticsearchClientResolver));

		return elasticsearchSearchEngine;
	}

	private CompanyIdIndexNameBuilder _createIndexNameBuilder() {
		return new CompanyIdIndexNameBuilder() {
			{
				setIndexNamePrefix(null);
			}
		};
	}

	private OperationModeResolver _createOperationModeResolver(
		ElasticsearchConfigurationWrapper elasticsearchConfigurationWrapper1) {

		return new OperationModeResolver() {
			{
				elasticsearchConfigurationWrapper =
					elasticsearchConfigurationWrapper1;
			}
		};
	}

	private SearchEngineAdapter _createSearchEngineAdapter(
		ElasticsearchClientResolver elasticsearchClientResolver) {

		_elasticsearchEngineAdapterFixture =
			new ElasticsearchEngineAdapterFixture() {
				{
					setElasticsearchClientResolver(elasticsearchClientResolver);
				}
			};

		_elasticsearchEngineAdapterFixture.setUp();

		return _elasticsearchEngineAdapterFixture.getSearchEngineAdapter();
	}

	private CompanyIndexFactory _companyIndexFactory;
	private final ElasticsearchConnectionFixture
		_elasticsearchConnectionFixture;
	private ElasticsearchConnectionManager _elasticsearchConnectionManager;
	private ElasticsearchEngineAdapterFixture
		_elasticsearchEngineAdapterFixture;
	private ElasticsearchSearchEngine _elasticsearchSearchEngine;
	private IndexNameBuilder _indexNameBuilder;

}