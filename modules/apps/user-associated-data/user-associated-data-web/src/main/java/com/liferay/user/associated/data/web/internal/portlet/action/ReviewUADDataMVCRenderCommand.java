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

package com.liferay.user.associated.data.web.internal.portlet.action;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.user.associated.data.constants.UserAssociatedDataPortletKeys;
import com.liferay.user.associated.data.display.UADDisplay;
import com.liferay.user.associated.data.web.internal.constants.UADConstants;
import com.liferay.user.associated.data.web.internal.constants.UADWebKeys;
import com.liferay.user.associated.data.web.internal.display.UADApplicationSummaryDisplay;
import com.liferay.user.associated.data.web.internal.display.UADEntity;
import com.liferay.user.associated.data.web.internal.display.ViewUADEntitiesDisplay;
import com.liferay.user.associated.data.web.internal.registry.UADRegistry;
import com.liferay.user.associated.data.web.internal.util.SafeDisplayValueUtil;
import com.liferay.user.associated.data.web.internal.util.SelectedUserHelper;
import com.liferay.user.associated.data.web.internal.util.UADApplicationSummaryHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pei-Jung Lan
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + UserAssociatedDataPortletKeys.USER_ASSOCIATED_DATA,
		"mvc.command.name=/review_uad_data"
	},
	service = MVCRenderCommand.class
)
public class ReviewUADDataMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		try {
			User selectedUser = _selectedUserHelper.getSelectedUser(
				renderRequest);

			String scope = ParamUtil.getString(
				renderRequest, "scope", UADConstants.SCOPE_PERSONAL_SITE);

			long[] groupIds = _getGroupIds(selectedUser, scope);

			List<UADApplicationSummaryDisplay> uadApplicationSummaryDisplays =
				_uadApplicationSummaryHelper.getUADApplicationSummaryDisplays(
					selectedUser.getUserId(), groupIds);

			UADApplicationSummaryDisplay uadApplicationSummaryDisplay =
				uadApplicationSummaryDisplays.get(0);

			for (UADApplicationSummaryDisplay
					currentUadApplicationSummaryDisplay :
						uadApplicationSummaryDisplays) {

				if (currentUadApplicationSummaryDisplay.getCount() > 0) {
					uadApplicationSummaryDisplay =
						currentUadApplicationSummaryDisplay;

					break;
				}
			}

			String applicationKey = ParamUtil.getString(
				renderRequest, "applicationKey");

			if (Validator.isNull(applicationKey)) {
				applicationKey =
					uadApplicationSummaryDisplay.getApplicationKey();
			}

			String uadRegistryKey = ParamUtil.getString(
				renderRequest, "uadRegistryKey");

			if (Validator.isNull(uadRegistryKey)) {
				uadRegistryKey =
					_uadApplicationSummaryHelper.getDefaultUADRegistryKey(
						applicationKey);
			}

			ViewUADEntitiesDisplay viewUADEntitiesDisplay =
				new ViewUADEntitiesDisplay();

			viewUADEntitiesDisplay.setApplicationKey(applicationKey);

			LiferayPortletResponse liferayPortletResponse =
				_portal.getLiferayPortletResponse(renderResponse);

			PortletURL currentURL = PortletURLUtil.getCurrent(
				renderRequest, renderResponse);

			UADDisplay uadDisplay = _uadRegistry.getUADDisplay(uadRegistryKey);

			viewUADEntitiesDisplay.setSearchContainer(
				_getSearchContainer(
					renderRequest, currentURL, uadDisplay, selectedUser,
					groupIds, liferayPortletResponse));
			viewUADEntitiesDisplay.setTypeName(
				uadDisplay.getTypeName(
					LocaleThreadLocal.getThemeDisplayLocale()));

			viewUADEntitiesDisplay.setUADRegistryKey(uadRegistryKey);

			renderRequest.setAttribute(
				UADWebKeys.APPLICATION_UAD_DISPLAYS,
				_uadRegistry.getApplicationUADDisplays(applicationKey));
			renderRequest.setAttribute(UADWebKeys.GROUP_IDS, groupIds);
			renderRequest.setAttribute(
				UADWebKeys.INFO_PANEL_UAD_DISPLAY, uadDisplay);
			renderRequest.setAttribute(
				UADWebKeys.TOTAL_UAD_ENTITIES_COUNT,
				_uadApplicationSummaryHelper.getTotalReviewableUADEntitiesCount(
					selectedUser.getUserId()));
			renderRequest.setAttribute(
				UADWebKeys.UAD_APPLICATION_SUMMARY_DISPLAY_LIST,
				uadApplicationSummaryDisplays);
			renderRequest.setAttribute(
				UADWebKeys.VIEW_UAD_ENTITIES_DISPLAY, viewUADEntitiesDisplay);
		}
		catch (Exception e) {
			throw new PortletException(e);
		}

		return "/review_uad_data.jsp";
	}

	private <T> UADEntity<T> _constructUADEntity(
			T entity, UADDisplay<T> uadDisplay,
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		UADEntity<T> uadEntity = new UADEntity(
			entity, uadDisplay.getPrimaryKey(entity),
			uadDisplay.getEditURL(
				entity, liferayPortletRequest, liferayPortletResponse));

		Map<String, Object> columnFieldValues = uadDisplay.getFieldValues(
			entity, uadDisplay.getColumnFieldNames());

		for (String columnFieldName : uadDisplay.getColumnFieldNames()) {
			uadEntity.addColumnEntry(
				columnFieldName,
				SafeDisplayValueUtil.get(
					columnFieldValues.get(columnFieldName)));
		}

		return uadEntity;
	}

	private long[] _getGroupIds(User user, String scope) {
		try {
			if (scope.equals(UADConstants.SCOPE_PERSONAL_SITE)) {
				Group userGroup = _groupLocalService.getUserGroup(
					user.getCompanyId(), user.getUserId());

				return new long[] {userGroup.getGroupId()};
			}

			if (scope.equals(UADConstants.SCOPE_REGULAR_SITES)) {
				List<Group> groups = _groupLocalService.getGroups(
					user.getCompanyId(), GroupConstants.ANY_PARENT_GROUP_ID,
					true);

				int size = groups.size();

				long[] groupIds = new long[size];

				for (int i = 0; i < size; i++) {
					Group group = groups.get(i);

					groupIds[i] = group.getGroupId();
				}

				return groupIds;
			}
		}
		catch (PortalException pe) {
			_log.error(pe, pe);
		}

		return null;
	}

	private SearchContainer<UADEntity> _getSearchContainer(
			RenderRequest renderRequest, PortletURL currentURL,
			UADDisplay uadDisplay, User selectedUser, long[] groupIds,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		LiferayPortletRequest liferayPortletRequest =
			_portal.getLiferayPortletRequest(renderRequest);

		DisplayTerms displayTerms = new DisplayTerms(renderRequest);

		int cur = ParamUtil.getInteger(
			renderRequest, SearchContainer.DEFAULT_CUR_PARAM,
			SearchContainer.DEFAULT_CUR);

		SearchContainer<UADEntity> searchContainer = new SearchContainer<>(
			renderRequest, displayTerms, displayTerms,
			SearchContainer.DEFAULT_CUR_PARAM, cur,
			SearchContainer.DEFAULT_DELTA, currentURL, null,
			"no-entities-remain-of-this-type", null);

		searchContainer.setId("UADEntities");

		String orderByCol = ParamUtil.getString(
			renderRequest, SearchContainer.DEFAULT_ORDER_BY_COL_PARAM);

		if (!ArrayUtil.contains(
				uadDisplay.getSortingFieldNames(), orderByCol)) {

			orderByCol = "modifiedDate";
		}

		searchContainer.setOrderByCol(orderByCol);

		String orderByType = ParamUtil.getString(
			renderRequest, SearchContainer.DEFAULT_ORDER_BY_TYPE_PARAM, "asc");

		searchContainer.setOrderByType(orderByType);

		Map<String, String> orderableHeaders = new LinkedHashMap<>();

		for (String orderByColumn : uadDisplay.getSortingFieldNames()) {
			orderableHeaders.put(
				TextFormatter.format(orderByColumn, TextFormatter.K),
				orderByColumn);
		}

		searchContainer.setOrderableHeaders(orderableHeaders);

		try {
			List entities = uadDisplay.search(
				selectedUser.getUserId(), groupIds, displayTerms.getKeywords(),
				searchContainer.getOrderByCol(),
				searchContainer.getOrderByType(), searchContainer.getStart(),
				searchContainer.getEnd());

			List<UADEntity> uadEntities = new ArrayList<>();

			for (Object entity : entities) {
				uadEntities.add(
					_constructUADEntity(
						entity, uadDisplay, liferayPortletRequest,
						liferayPortletResponse));
			}

			searchContainer.setResults(uadEntities);

			searchContainer.setTotal(
				(int)uadDisplay.searchCount(
					selectedUser.getUserId(), groupIds,
					displayTerms.getKeywords()));
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}

			searchContainer.setResults(Collections.emptyList());
			searchContainer.setTotal(0);
		}

		RowChecker rowChecker = new EmptyOnClickRowChecker(
			liferayPortletResponse);

		Class<?> uadClass = uadDisplay.getTypeClass();

		rowChecker.setRememberCheckBoxStateURLRegex(
			"uadRegistryKey=" + uadClass.getName());

		searchContainer.setRowChecker(rowChecker);

		return searchContainer;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ReviewUADDataMVCRenderCommand.class);

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private SelectedUserHelper _selectedUserHelper;

	@Reference
	private UADApplicationSummaryHelper _uadApplicationSummaryHelper;

	@Reference
	private UADRegistry _uadRegistry;

}