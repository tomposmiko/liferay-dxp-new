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

package com.liferay.change.tracking.rest.internal.resource;

import com.liferay.change.tracking.constants.CTConstants;
import com.liferay.change.tracking.engine.CTEngineManager;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.model.CTProcess;
import com.liferay.change.tracking.rest.internal.model.process.CTProcessModel;
import com.liferay.change.tracking.rest.internal.model.process.CTProcessUserModel;
import com.liferay.change.tracking.rest.internal.util.CTJaxRsUtil;
import com.liferay.change.tracking.service.CTCollectionLocalService;
import com.liferay.change.tracking.service.CTProcessLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.background.task.model.BackgroundTask;
import com.liferay.portal.background.task.service.BackgroundTaskLocalService;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutorRegistry;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManager;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.dao.orm.QueryDefinition;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Máté Thurzó
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(osgi.jaxrs.name=Liferay.Change.Tracking.REST)",
		"osgi.jaxrs.resource=true"
	},
	scope = ServiceScope.PROTOTYPE, service = CTProcessResource.class
)
@Path("/processes")
public class CTProcessResource {

	@GET
	@Path("/{ctProcessId}")
	@Produces
	public CTProcessModel getCtProcessModel(
		@PathParam("ctProcessId") long ctProcessId) {

		return Optional.ofNullable(
			_ctProcessLocalService.fetchCTProcess(ctProcessId)
		).map(
			this::_getCTProcessModel
		).orElse(
			CTProcessModel.emptyCTProcessModel()
		);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<CTProcessModel> getCtProcessModels(
			@QueryParam("companyId") long companyId,
			@QueryParam("userId") long userId,
			@QueryParam("keywords") String keywords,
			@DefaultValue(_TYPE_ALL) @QueryParam("type") String type,
			@QueryParam("offset") int offset, @QueryParam("limit") int limit,
			@QueryParam("sort") String sort)
		throws PortalException {

		CTJaxRsUtil.checkCompany(companyId);

		return _getCTProcessModels(
			_getCTProcesses(
				companyId, userId, keywords, type, offset, limit, sort));
	}

	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CTProcessUserModel> getCtProcessUserModels(
			@QueryParam("companyId") long companyId,
			@QueryParam("keywords") String keywords,
			@DefaultValue(_TYPE_ALL) @QueryParam("type") String type,
			@QueryParam("offset") int offset, @QueryParam("limit") int limit)
		throws PortalException {

		CTJaxRsUtil.checkCompany(companyId);

		Stream<CTProcess> stream = _getCTProcesses(
			companyId, CTConstants.USER_FILTER_ALL, keywords, type, offset,
			limit, null
		).stream();

		return stream.map(
			CTProcess::getUserId
		).distinct(
		).map(
			_userLocalService::fetchUser
		).filter(
			Objects::nonNull
		).map(
			user -> {
				CTProcessUserModel.Builder builder =
					CTProcessUserModel.forUserId(user.getUserId());

				return builder.setUserName(
					user.getFullName()
				).build();
			}
		).collect(
			Collectors.toList()
		);
	}

	private Optional<com.liferay.portal.kernel.backgroundtask.BackgroundTask>
		_fetchPortalKernelBackgroundTaskOptional(
			BackgroundTask backgroundTask) {

		try {
			return Optional.of(
				_backgroundTaskManager.getBackgroundTask(
					backgroundTask.getBackgroundTaskId()));
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to get background task", pe);
			}

			return Optional.empty();
		}
	}

	private Optional<BackgroundTaskDisplay> _getBackgroundTaskDisplayOptional(
		BackgroundTask backgroundTask,
		BackgroundTaskExecutor backgroundTaskExecutor) {

		Optional<com.liferay.portal.kernel.backgroundtask.BackgroundTask>
			portalKernelBackgroundTaskOptional =
				_fetchPortalKernelBackgroundTaskOptional(backgroundTask);

		return portalKernelBackgroundTaskOptional.map(
			backgroundTaskExecutor::getBackgroundTaskDisplay);
	}

	private List<CTProcess> _getCTProcesses(
		long companyId, long userId, String keywords, String type, int offset,
		int limit, String sort) {

		List<CTProcess> ctProcesses = null;

		if (_TYPE_PUBLISHED_LATEST.equals(type)) {
			Optional<CTProcess> latestCTProcessOptional =
				_ctEngineManager.getLatestCTProcessOptional(companyId);

			ctProcesses = latestCTProcessOptional.map(
				Collections::singletonList
			).orElse(
				Collections.emptyList()
			);
		}
		else {
			int status = _getStatus(type);

			ctProcesses = _ctEngineManager.getCTProcesses(
				companyId, userId, keywords,
				_getQueryDefinition(offset, limit, status, sort));
		}

		return ctProcesses;
	}

	private CTProcessModel _getCTProcessModel(CTProcess ctProcess) {
		CTCollection ctCollection = _ctCollectionLocalService.fetchCTCollection(
			ctProcess.getCtCollectionId());

		BackgroundTask backgroundTask =
			_backgroundTaskLocalService.fetchBackgroundTask(
				ctProcess.getBackgroundTaskId());

		Optional<BackgroundTask> backgroundTaskOptional = Optional.ofNullable(
			backgroundTask);

		User user = _userLocalService.fetchUser(ctProcess.getUserId());

		Optional<User> userOptional = Optional.ofNullable(user);

		CTProcessModel.Builder builder = CTProcessModel.forCompany(
			ctProcess.getCompanyId());

		return builder.setCTCollection(
			ctCollection
		).setCTProcessId(
			ctProcess.getCtProcessId()
		).setDate(
			ctProcess.getCreateDate()
		).setStatus(
			backgroundTaskOptional.map(
				BackgroundTask::getStatusLabel
			).orElse(
				StringPool.BLANK
			)
		).setPercentage(
			backgroundTaskOptional.flatMap(
				this::_fetchPortalKernelBackgroundTaskOptional
			).map(
				com.liferay.portal.kernel.backgroundtask.BackgroundTask::
					getTaskExecutorClassName
			).map(
				_backgroundTaskExecutorRegistry::getBackgroundTaskExecutor
			).flatMap(
				backgroundTaskExecutor -> _getBackgroundTaskDisplayOptional(
					backgroundTask, backgroundTaskExecutor)
			).map(
				BackgroundTaskDisplay::getPercentage
			).orElse(
				100
			)
		).setUserInitials(
			userOptional.map(
				User::getInitials
			).orElse(
				StringPool.BLANK
			)
		).setUserName(
			userOptional.map(
				User::getFullName
			).orElse(
				StringPool.BLANK
			)
		).setUserPortraitURL(
			userOptional.map(
				this::_getPortraitURL
			).orElse(
				StringPool.BLANK
			)
		).build();
	}

	private List<CTProcessModel> _getCTProcessModels(
		List<CTProcess> ctProcesses) {

		Stream<CTProcess> stream = ctProcesses.stream();

		return stream.map(
			this::_getCTProcessModel
		).collect(
			Collectors.toList()
		);
	}

	private String _getPortraitURL(User user) {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (themeDisplay == null) {
			return StringPool.BLANK;
		}

		if (user != null) {
			return user.fetchPortraitURL(themeDisplay);
		}

		return UserConstants.getPortraitURL(
			themeDisplay.getPathImage(), true, 0, StringPool.BLANK);
	}

	@SuppressWarnings("unchecked")
	private QueryDefinition _getQueryDefinition(
		int offset, int limit, int status, String sort) {

		QueryDefinition queryDefinition = new QueryDefinition();

		int end = CTJaxRsUtil.checkLimit(limit);

		if (end > 0) {
			queryDefinition.setEnd(CTJaxRsUtil.checkLimit(limit));
		}

		int start = CTJaxRsUtil.checkLimit(offset);

		if (start > 0) {
			queryDefinition.setStart(start);
		}

		queryDefinition.setStatus(status);

		Object[] sortColumns = CTJaxRsUtil.checkSortColumns(
			sort, _orderByColumnNames);

		OrderByComparator orderByComparator = null;

		if (ArrayUtil.isNotEmpty(sortColumns)) {
			String sortColumn = GetterUtil.getString(sortColumns[0]);

			if (Objects.equals(sortColumn, "name")) {
				orderByComparator = OrderByComparatorFactoryUtil.create(
					"CTCollection", sortColumns);
			}
			else if (Objects.equals(sortColumn, "publishDate")) {
				orderByComparator = OrderByComparatorFactoryUtil.create(
					"CTProcess", "createDate", sortColumns[1]);
			}
		}

		queryDefinition.setOrderByComparator(orderByComparator);

		return queryDefinition;
	}

	private int _getStatus(String type) {
		int status = 0;

		if (_TYPE_ALL.equals(type)) {
			status = WorkflowConstants.STATUS_ANY;
		}
		else if (_TYPE_FAILED.equals(type)) {
			status = BackgroundTaskConstants.STATUS_FAILED;
		}
		else if (_TYPE_IN_PROGRESS.equals(type)) {
			status = BackgroundTaskConstants.STATUS_IN_PROGRESS;
		}
		else if (_TYPE_PUBLISHED.equals(type)) {
			status = BackgroundTaskConstants.STATUS_SUCCESSFUL;
		}

		return status;
	}

	private static final String _TYPE_ALL = "all";

	private static final String _TYPE_FAILED = "failed";

	private static final String _TYPE_IN_PROGRESS = "in-progress";

	private static final String _TYPE_PUBLISHED = "published";

	private static final String _TYPE_PUBLISHED_LATEST = "published-latest";

	private static final Log _log = LogFactoryUtil.getLog(
		CTProcessResource.class);

	private static final Set<String> _orderByColumnNames = new HashSet<>(
		Arrays.asList("publishDate", "name"));

	@Reference
	private BackgroundTaskExecutorRegistry _backgroundTaskExecutorRegistry;

	@Reference
	private BackgroundTaskLocalService _backgroundTaskLocalService;

	@Reference
	private BackgroundTaskManager _backgroundTaskManager;

	@Reference
	private CTCollectionLocalService _ctCollectionLocalService;

	@Reference
	private CTEngineManager _ctEngineManager;

	@Reference
	private CTProcessLocalService _ctProcessLocalService;

	@Context
	private HttpServletRequest _httpServletRequest;

	@Reference
	private UserLocalService _userLocalService;

}