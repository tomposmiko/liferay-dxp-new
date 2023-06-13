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

package com.liferay.change.tracking.internal.background.task;

import com.liferay.change.tracking.CTEngineManager;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.model.CTEntry;
import com.liferay.change.tracking.service.CTCollectionLocalServiceUtil;
import com.liferay.change.tracking.service.CTEntryLocalServiceUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BaseBackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Zoltan Csaszi
 */
public class CTPublishBackgroundTaskExecutor
	extends BaseBackgroundTaskExecutor {

	public CTPublishBackgroundTaskExecutor() {
		setBackgroundTaskStatusMessageTranslator(
			new CTPublishBackgroundTaskStatusMessageTranslator());
		setIsolationLevel(BackgroundTaskConstants.ISOLATION_LEVEL_COMPANY);

		Bundle bundle = FrameworkUtil.getBundle(
			CTPublishBackgroundTaskExecutor.class);

		ServiceTracker<CTEngineManager, CTEngineManager> serviceTracker =
			new ServiceTracker<>(
				bundle.getBundleContext(), CTEngineManager.class, null);

		serviceTracker.open();

		_ctEngineManager = serviceTracker.getService();
	}

	@Override
	public BackgroundTaskExecutor clone() {
		CTPublishBackgroundTaskExecutor ctPublishBackgroundTaskExecutor =
			new CTPublishBackgroundTaskExecutor();

		ctPublishBackgroundTaskExecutor.
			setBackgroundTaskStatusMessageTranslator(
				getBackgroundTaskStatusMessageTranslator());
		ctPublishBackgroundTaskExecutor.setIsolationLevel(getIsolationLevel());

		return ctPublishBackgroundTaskExecutor;
	}

	@Override
	public BackgroundTaskResult execute(BackgroundTask backgroundTask) {
		Map<String, Serializable> taskContextMap =
			backgroundTask.getTaskContextMap();

		long ctCollectionId = GetterUtil.getLong(
			taskContextMap.get("ctCollectionId"));

		List<CTEntry> ctEntries = _ctEngineManager.getCTEntries(ctCollectionId);

		if (ListUtil.isEmpty(ctEntries)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to find change tracking entries with change " +
						"tracking collection ID " + ctCollectionId);
			}
		}
		else {
			_publishCTEntries(
				backgroundTask.getUserId(), ctCollectionId, ctEntries);
		}

		return BackgroundTaskResult.SUCCESS;
	}

	@Override
	public BackgroundTaskDisplay getBackgroundTaskDisplay(
		BackgroundTask backgroundTask) {

		return null;
	}

	private void _publishCTEntries(
		long userId, long ctCollectionId, List<CTEntry> ctEntries) {

		User user = UserLocalServiceUtil.fetchUser(userId);

		if (user == null) {
			_log.error("Unable to get user " + userId);

			return;
		}

		Optional<CTCollection> productionCTCollectionOptional =
			_ctEngineManager.getProductionCTCollectionOptional(
				user.getCompanyId());

		if (!productionCTCollectionOptional.isPresent()) {
			return;
		}

		long productionCTCollectionId = productionCTCollectionOptional.map(
			CTCollection::getCtCollectionId
		).get();

		CTEntryLocalServiceUtil.addCTCollectionCTEntries(
			productionCTCollectionId, ctEntries);

		Optional<CTCollection> ctCollectionOptional =
			_ctEngineManager.getCTCollectionOptional(ctCollectionId);

		if (!ctCollectionOptional.isPresent()) {
			return;
		}

		try {
			CTCollectionLocalServiceUtil.updateStatus(
				userId, ctCollectionOptional.get(),
				WorkflowConstants.STATUS_APPROVED, new ServiceContext());
		}
		catch (PortalException pe) {
			_log.error(
				"Unable to update the status of the published change " +
					"tracking collection",
				pe);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CTPublishBackgroundTaskExecutor.class);

	private final CTEngineManager _ctEngineManager;

}