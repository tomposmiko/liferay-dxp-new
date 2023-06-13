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

package com.liferay.change.tracking.web.internal.portlet.action;

import com.liferay.change.tracking.constants.CTPortletKeys;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.model.CTProcess;
import com.liferay.change.tracking.service.CTCollectionLocalService;
import com.liferay.change.tracking.service.CTProcessLocalService;
import com.liferay.change.tracking.web.internal.constants.CTWebConstants;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.background.task.model.BackgroundTask;
import com.liferay.portal.background.task.service.BackgroundTaskLocalService;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.text.Format;

import java.util.List;
import java.util.Objects;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Samuel Trong Tran
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CTPortletKeys.CHANGE_LISTS_HISTORY,
		"mvc.command.name=/change_lists_history/get_ct_processes"
	},
	service = MVCResourceCommand.class
)
public class GetCTProcessesMVCResourceCommand
	extends BaseCTProcessMVCResourceCommand {

	@Override
	protected void doServeResource(
		ResourceRequest resourceRequest, ResourceResponse resourceResponse) {

		try {
			HttpServletResponse httpServletResponse =
				_portal.getHttpServletResponse(resourceResponse);

			httpServletResponse.setContentType(ContentTypes.APPLICATION_JSON);

			JSONArray jsonArray = _getProcessEntriesJSONArray(resourceRequest);

			ServletResponseUtil.write(
				httpServletResponse, jsonArray.toString());
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	private String _getCTProcessStatusLabel(CTProcess ctProcess) {
		BackgroundTask backgroundTask =
			_backgroundTaskLocalService.fetchBackgroundTask(
				ctProcess.getBackgroundTaskId());

		if (backgroundTask == null) {
			return StringPool.BLANK;
		}

		int status = backgroundTask.getStatus();

		if (status == BackgroundTaskConstants.STATUS_SUCCESSFUL) {
			return "published";
		}

		return BackgroundTaskConstants.getStatusLabel(status);
	}

	private OrderByComparator<CTProcess> _getOrderByComparator(String sort) {
		OrderByComparator<CTProcess> orderByComparator = null;

		String[] sortColumns = StringUtil.split(sort, ":");

		if (sortColumns.length == 2) {
			boolean asc = true;

			if (sortColumns[1].equals("desc")) {
				asc = false;
			}

			if (Objects.equals(sortColumns[0], "name")) {
				orderByComparator = OrderByComparatorFactoryUtil.create(
					"CTCollection", new Object[] {sortColumns[0], asc});
			}
			else if (Objects.equals(sortColumns[0], "publishDate")) {
				orderByComparator = OrderByComparatorFactoryUtil.create(
					"CTProcess", "createDate", asc);
			}
		}

		return orderByComparator;
	}

	private JSONArray _getProcessEntriesJSONArray(
			ResourceRequest resourceRequest)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String keywords = ParamUtil.getString(
			resourceRequest, "keywords", null);
		String sort = ParamUtil.getString(resourceRequest, "sort");
		String type = ParamUtil.getString(resourceRequest, "type");
		long userId = ParamUtil.getLong(
			resourceRequest, "user", CTWebConstants.USER_FILTER_ALL);

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		List<CTProcess> ctProcesses = _ctProcessLocalService.getCTProcesses(
			themeDisplay.getCompanyId(), userId, keywords, getStatus(type), 0,
			5, _getOrderByComparator(sort));

		PortletURL detailsURL = PortletURLFactoryUtil.create(
			resourceRequest, CTPortletKeys.CHANGE_LISTS_HISTORY,
			PortletRequest.RENDER_PHASE);

		detailsURL.setParameter(
			"mvcRenderCommandName", "/change_lists_history/view_details");
		detailsURL.setParameter(
			"backURL", _portal.getCurrentURL(resourceRequest));
		detailsURL.setParameter("orderByCol", "modifiedDate");
		detailsURL.setParameter("orderByType", "desc");

		for (CTProcess ctProcess : ctProcesses) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			CTCollection ctCollection =
				_ctCollectionLocalService.getCTCollection(
					ctProcess.getCtCollectionId());
			detailsURL.setParameter(
				"ctCollectionId",
				String.valueOf(ctProcess.getCtCollectionId()));
			Format format = FastDateFormatFactoryUtil.getDateTime(
				themeDisplay.getLocale());
			User user = _userLocalService.getUser(ctProcess.getUserId());

			PortletURL undoURL = PortletURLFactoryUtil.create(
				resourceRequest, CTPortletKeys.CHANGE_LISTS_HISTORY,
				PortletRequest.ACTION_PHASE);

			undoURL.setParameter(
				ActionRequest.ACTION_NAME,
				"/change_lists_history/undo_ct_collection");

			undoURL.setParameter(
				"ctCollectionId",
				String.valueOf(ctCollection.getCtCollectionId()));

			jsonArray.put(
				jsonObject.put(
					"description", ctCollection.getDescription()
				).put(
					"detailsLink", detailsURL.toString()
				).put(
					"name", ctCollection.getName()
				).put(
					"state", _getCTProcessStatusLabel(ctProcess)
				).put(
					"timestamp", format.format(ctProcess.getCreateDate())
				).put(
					"undoURL", undoURL.toString()
				).put(
					"userInitials", user.getInitials()
				).put(
					"userName", user.getFullName()
				));
		}

		return jsonArray;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		GetCTProcessesMVCResourceCommand.class);

	@Reference
	private BackgroundTaskLocalService _backgroundTaskLocalService;

	@Reference
	private CTCollectionLocalService _ctCollectionLocalService;

	@Reference
	private CTProcessLocalService _ctProcessLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private UserLocalService _userLocalService;

}