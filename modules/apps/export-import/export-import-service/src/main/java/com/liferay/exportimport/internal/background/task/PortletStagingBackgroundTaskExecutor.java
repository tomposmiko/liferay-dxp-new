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

package com.liferay.exportimport.internal.background.task;

import com.liferay.exportimport.internal.background.task.display.PortletExportImportBackgroundTaskDisplay;
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.exportimport.kernel.lar.MissingReferences;
import com.liferay.exportimport.kernel.lifecycle.ExportImportLifecycleManagerUtil;
import com.liferay.exportimport.kernel.lifecycle.constants.ExportImportLifecycleConstants;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.exportimport.kernel.service.ExportImportLocalService;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;

import java.io.File;

import java.util.concurrent.Callable;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Julio Camarero
 * @author Daniel Kocsis
 * @author Akos Thurzo
 */
@Component(
	property = "background.task.executor.class.name=com.liferay.exportimport.internal.background.task.PortletStagingBackgroundTaskExecutor",
	service = BackgroundTaskExecutor.class
)
public class PortletStagingBackgroundTaskExecutor
	extends BaseStagingBackgroundTaskExecutor {

	public PortletStagingBackgroundTaskExecutor() {
		setBackgroundTaskStatusMessageTranslator(
			new PortletStagingBackgroundTaskStatusMessageTranslator());
	}

	@Override
	public BackgroundTaskExecutor clone() {
		return this;
	}

	@Override
	public BackgroundTaskResult execute(BackgroundTask backgroundTask)
		throws Exception {

		ExportImportConfiguration exportImportConfiguration =
			getExportImportConfiguration(backgroundTask);

		File file = null;
		MissingReferences missingReferences;

		try {
			ExportImportThreadLocal.setPortletStagingInProcess(true);

			ExportImportLifecycleManagerUtil.fireExportImportLifecycleEvent(
				ExportImportLifecycleConstants.
					EVENT_PUBLICATION_PORTLET_LOCAL_STARTED,
				ExportImportLifecycleConstants.
					PROCESS_FLAG_PORTLET_STAGING_IN_PROCESS,
				String.valueOf(
					exportImportConfiguration.getExportImportConfigurationId()),
				exportImportConfiguration);

			file = _exportImportLocalService.exportPortletInfoAsFile(
				exportImportConfiguration);

			markBackgroundTask(
				backgroundTask.getBackgroundTaskId(), "exported");

			missingReferences = TransactionInvokerUtil.invoke(
				transactionConfig,
				new PortletStagingCallable(
					backgroundTask.getBackgroundTaskId(),
					exportImportConfiguration, file));

			deleteExportedChangesetEntries();

			ExportImportThreadLocal.setPortletStagingInProcess(false);

			ExportImportLifecycleManagerUtil.fireExportImportLifecycleEvent(
				ExportImportLifecycleConstants.
					EVENT_PUBLICATION_PORTLET_LOCAL_SUCCEEDED,
				ExportImportLifecycleConstants.
					PROCESS_FLAG_PORTLET_STAGING_IN_PROCESS,
				String.valueOf(
					exportImportConfiguration.getExportImportConfigurationId()),
				exportImportConfiguration);
		}
		catch (Throwable throwable) {
			ExportImportThreadLocal.setPortletStagingInProcess(false);

			ExportImportLifecycleManagerUtil.fireExportImportLifecycleEvent(
				ExportImportLifecycleConstants.
					EVENT_PUBLICATION_PORTLET_LOCAL_FAILED,
				ExportImportLifecycleConstants.
					PROCESS_FLAG_PORTLET_STAGING_IN_PROCESS,
				String.valueOf(
					exportImportConfiguration.getExportImportConfigurationId()),
				exportImportConfiguration);

			deleteTempLarOnFailure(file);

			throw new SystemException(throwable);
		}

		deleteTempLarOnSuccess(file);

		return processMissingReferences(
			backgroundTask.getBackgroundTaskId(), missingReferences);
	}

	@Override
	public BackgroundTaskDisplay getBackgroundTaskDisplay(
		BackgroundTask backgroundTask) {

		return new PortletExportImportBackgroundTaskDisplay(backgroundTask);
	}

	@Reference
	private ExportImportLocalService _exportImportLocalService;

	private class PortletStagingCallable
		implements Callable<MissingReferences> {

		public PortletStagingCallable(
			long backgroundTaskId,
			ExportImportConfiguration exportImportConfiguration, File file) {

			_backgroundTaskId = backgroundTaskId;
			_exportImportConfiguration = exportImportConfiguration;
			_file = file;
		}

		@Override
		public MissingReferences call() throws PortalException {
			_exportImportLocalService.importPortletDataDeletions(
				_exportImportConfiguration, _file);

			MissingReferences missingReferences =
				_exportImportLocalService.validateImportPortletInfo(
					_exportImportConfiguration, _file);

			markBackgroundTask(_backgroundTaskId, "validated");

			_exportImportLocalService.importPortletInfo(
				_exportImportConfiguration, _file);

			return missingReferences;
		}

		private final long _backgroundTaskId;
		private final ExportImportConfiguration _exportImportConfiguration;
		private final File _file;

	}

}