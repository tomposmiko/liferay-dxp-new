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

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationConfiguration;
import com.liferay.portal.kernel.messaging.DestinationFactory;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.search.IndexSearcher;
import com.liferay.portal.kernel.search.IndexWriter;
import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.kernel.search.SearchEngineProxyWrapper;
import com.liferay.portal.kernel.search.messaging.BaseSearchEngineMessageListener;
import com.liferay.portal.kernel.search.messaging.SearchReaderMessageListener;
import com.liferay.portal.kernel.search.messaging.SearchWriterMessageListener;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.PortalRunMode;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = ElasticsearchEngineConfigurator.class)
public class ElasticsearchEngineConfigurator {

	public void configure(SearchEngine searchEngine) {
		_registerSearchEngineMessageListener(
			searchEngine, _getSearchReaderDestination(),
			new SearchReaderMessageListener(), searchEngine.getIndexSearcher());

		_registerSearchEngineMessageListener(
			searchEngine, _getSearchWriterDestination(),
			new SearchWriterMessageListener(), searchEngine.getIndexWriter());

		SearchEngineProxyWrapper searchEngineProxyWrapper =
			new SearchEngineProxyWrapper(
				searchEngine, _indexSearcher, _indexWriter);

		for (Company company : _companyLocalService.getCompanies()) {
			searchEngineProxyWrapper.initialize(company.getCompanyId());
		}

		searchEngineProxyWrapper.initialize(CompanyConstants.SYSTEM);
	}

	public void unconfigure() {
		for (ServiceRegistration<?> serviceRegistration :
				_destinationServiceRegistrations) {

			serviceRegistration.unregister();
		}

		_destinationServiceRegistrations.clear();
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	private Destination _createSearchReaderDestination(
		String searchReaderDestinationName) {

		DestinationConfiguration destinationConfiguration =
			DestinationConfiguration.createSynchronousDestinationConfiguration(
				searchReaderDestinationName);

		return _destinationFactory.createDestination(destinationConfiguration);
	}

	private Destination _createSearchWriterDestination(
		String searchWriterDestinationName) {

		DestinationConfiguration destinationConfiguration = null;

		if (PortalRunMode.isTestMode()) {
			destinationConfiguration =
				DestinationConfiguration.
					createSynchronousDestinationConfiguration(
						searchWriterDestinationName);
		}
		else {
			destinationConfiguration =
				DestinationConfiguration.createParallelDestinationConfiguration(
					searchWriterDestinationName);
		}

		if (_INDEX_SEARCH_WRITER_MAX_QUEUE_SIZE > 0) {
			destinationConfiguration.setMaximumQueueSize(
				_INDEX_SEARCH_WRITER_MAX_QUEUE_SIZE);

			RejectedExecutionHandler rejectedExecutionHandler =
				new ThreadPoolExecutor.CallerRunsPolicy() {

					@Override
					public void rejectedExecution(
						Runnable runnable,
						ThreadPoolExecutor threadPoolExecutor) {

						if (_log.isWarnEnabled()) {
							_log.warn(
								StringBundler.concat(
									"The search index writer's task queue is ",
									"at its maximum capacity. The current ",
									"thread will handle the request."));
						}

						super.rejectedExecution(runnable, threadPoolExecutor);
					}

				};

			destinationConfiguration.setRejectedExecutionHandler(
				rejectedExecutionHandler);
		}

		return _destinationFactory.createDestination(destinationConfiguration);
	}

	private Destination _getSearchReaderDestination() {
		Destination searchReaderDestination = _messageBus.getDestination(
			DestinationNames.SEARCH_READER);

		if (searchReaderDestination == null) {
			searchReaderDestination = _createSearchReaderDestination(
				DestinationNames.SEARCH_READER);

			_destinationServiceRegistrations.add(
				_bundleContext.registerService(
					Destination.class, searchReaderDestination,
					MapUtil.singletonDictionary(
						"destination.name",
						searchReaderDestination.getName())));
		}

		return searchReaderDestination;
	}

	private Destination _getSearchWriterDestination() {
		Destination searchWriterDestination = _messageBus.getDestination(
			DestinationNames.SEARCH_WRITER);

		if (searchWriterDestination == null) {
			searchWriterDestination = _createSearchWriterDestination(
				DestinationNames.SEARCH_WRITER);

			_destinationServiceRegistrations.add(
				_bundleContext.registerService(
					Destination.class, searchWriterDestination,
					MapUtil.singletonDictionary(
						"destination.name",
						searchWriterDestination.getName())));
		}

		return searchWriterDestination;
	}

	private void _registerSearchEngineMessageListener(
		SearchEngine searchEngine, Destination destination,
		BaseSearchEngineMessageListener baseSearchEngineMessageListener,
		Object manager) {

		baseSearchEngineMessageListener.setManager(manager);
		baseSearchEngineMessageListener.setMessageBus(_messageBus);
		baseSearchEngineMessageListener.setSearchEngine(searchEngine);

		destination.register(
			baseSearchEngineMessageListener,
			ElasticsearchEngineConfigurator.class.getClassLoader());
	}

	private static final int _INDEX_SEARCH_WRITER_MAX_QUEUE_SIZE =
		GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.INDEX_SEARCH_WRITER_MAX_QUEUE_SIZE));

	private static final Log _log = LogFactoryUtil.getLog(
		ElasticsearchEngineConfigurator.class);

	private BundleContext _bundleContext;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private DestinationFactory _destinationFactory;

	private final List<ServiceRegistration<?>>
		_destinationServiceRegistrations = new ArrayList<>();

	@Reference(target = "(!(search.engine.impl=*))")
	private IndexSearcher _indexSearcher;

	@Reference(target = "(!(search.engine.impl=*))")
	private IndexWriter _indexWriter;

	@Reference
	private MessageBus _messageBus;

}