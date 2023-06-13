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

package com.liferay.portal.search.internal;

import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.petra.executor.PortalExecutorManager;
import com.liferay.petra.lang.SafeCloseable;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskThreadLocal;
import com.liferay.portal.kernel.change.tracking.sql.CTSQLModeThreadLocal;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.search.IndexWriterHelperUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchEngineHelperUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.search.index.ConcurrentReindexManager;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang.time.StopWatch;

import org.osgi.framework.BundleContext;

/**
 * @author Brian Wing Shun Chan
 */
public class SearchEngineInitializer implements Runnable {

	public SearchEngineInitializer(
		BundleContext bundleContext, long companyId,
		ConcurrentReindexManager concurrentReindexManager, String executionMode,
		PortalExecutorManager portalExecutorManager) {

		_bundleContext = bundleContext;
		_companyId = companyId;
		_concurrentReindexManager = concurrentReindexManager;
		_executionMode = executionMode;
		_portalExecutorManager = portalExecutorManager;
	}

	public void halt() {
	}

	public boolean isFinished() {
		return _finished;
	}

	public void reindex() {
		reindex(0);
	}

	public void reindex(int delay) {
		_reindex(delay);
	}

	@Override
	public void run() {
		reindex(PropsValues.INDEX_ON_STARTUP_DELAY);
	}

	protected void reindex(Indexer<?> indexer) throws Exception {
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();

		if (_log.isInfoEnabled()) {
			_log.info(
				"Reindexing of " + indexer.getClassName() +
					" entities started");
		}

		indexer.reindex(new String[] {String.valueOf(_companyId)});

		if (_log.isInfoEnabled()) {
			_log.info(
				StringBundler.concat(
					"Reindexing of ", indexer.getClassName(),
					" entities completed in ",
					stopWatch.getTime() / Time.SECOND, " seconds"));
		}
	}

	private boolean _isExecuteConcurrentReindex() {
		if (FeatureFlagManagerUtil.isEnabled("LPS-177664") &&
			(_concurrentReindexManager != null) && (_executionMode != null) &&
			_executionMode.equals("concurrent") &&
			(_companyId != CompanyConstants.SYSTEM)) {

			return true;
		}

		return false;
	}

	private void _reindex(int delay) {
		if (IndexWriterHelperUtil.isIndexReadOnly()) {
			return;
		}

		if (_log.isInfoEnabled()) {
			_log.info("Reindexing started");
		}

		if (delay < 0) {
			delay = 0;
		}

		try {
			if (delay > 0) {
				Thread.sleep(Time.SECOND * delay);
			}
		}
		catch (InterruptedException interruptedException) {
			if (_log.isDebugEnabled()) {
				_log.debug(interruptedException);
			}
		}

		ExecutorService executorService =
			_portalExecutorManager.getPortalExecutor(
				SearchEngineInitializer.class.getName());

		StopWatch stopWatch = new StopWatch();

		stopWatch.start();

		try {
			if (_isExecuteConcurrentReindex()) {
				_concurrentReindexManager.createNextIndex(_companyId);
			}
			else {
				SearchEngineHelperUtil.removeCompany(_companyId);

				SearchEngineHelperUtil.initialize(_companyId);
			}

			long backgroundTaskId =
				BackgroundTaskThreadLocal.getBackgroundTaskId();
			List<FutureTask<Void>> futureTasks = new ArrayList<>();

			if (_companyId == CompanyConstants.SYSTEM) {
				_indexers = ServiceTrackerListFactory.open(
					_bundleContext, (Class<Indexer<?>>)(Class<?>)Indexer.class,
					"(system.index=true)");
			}
			else {
				_indexers = ServiceTrackerListFactory.open(
					_bundleContext, (Class<Indexer<?>>)(Class<?>)Indexer.class,
					"(!(system.index=true))");
			}

			for (Indexer<?> indexer : _indexers) {
				FutureTask<Void> futureTask = new FutureTask<>(
					new Callable<Void>() {

						@Override
						public Void call() throws Exception {
							CTSQLModeThreadLocal.CTSQLMode ctSQLMode =
								CTSQLModeThreadLocal.getCTSQLMode();

							try (SafeCloseable safeCloseable =
									BackgroundTaskThreadLocal.
										setBackgroundTaskIdWithSafeCloseable(
											backgroundTaskId)) {

								CTSQLModeThreadLocal.
									setCTSQLModeWithSafeCloseable(
										CTSQLModeThreadLocal.CTSQLMode.CT_ALL);

								reindex(indexer);

								return null;
							}
							finally {
								CTSQLModeThreadLocal.
									setCTSQLModeWithSafeCloseable(ctSQLMode);
							}
						}

					});

				executorService.submit(futureTask);

				futureTasks.add(futureTask);
			}

			_indexers.close();

			for (FutureTask<Void> futureTask : futureTasks) {
				futureTask.get();
			}

			if (_isExecuteConcurrentReindex()) {
				_concurrentReindexManager.replaceCurrentIndexWithNextIndex(
					_companyId);
			}

			if (_log.isInfoEnabled()) {
				_log.info(
					"Reindexing completed in " +
						(stopWatch.getTime() / Time.SECOND) + " seconds");
			}
		}
		catch (Exception exception) {
			if (_isExecuteConcurrentReindex()) {
				_concurrentReindexManager.deleteNextIndex(_companyId);
			}

			_log.error("Error encountered while reindexing", exception);

			if (_log.isInfoEnabled()) {
				_log.info("Reindexing failed");
			}
		}

		_finished = true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SearchEngineInitializer.class);

	private final BundleContext _bundleContext;
	private final long _companyId;
	private final ConcurrentReindexManager _concurrentReindexManager;
	private final String _executionMode;
	private boolean _finished;
	private ServiceTrackerList<Indexer<?>> _indexers;
	private final PortalExecutorManager _portalExecutorManager;

}