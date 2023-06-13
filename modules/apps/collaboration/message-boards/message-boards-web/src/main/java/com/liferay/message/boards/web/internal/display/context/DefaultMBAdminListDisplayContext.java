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

package com.liferay.message.boards.web.internal.display.context;

import com.liferay.message.boards.constants.MBPortletKeys;
import com.liferay.message.boards.display.context.MBAdminListDisplayContext;
import com.liferay.message.boards.model.MBMessage;
import com.liferay.message.boards.service.MBCategoryServiceUtil;
import com.liferay.portal.kernel.dao.orm.QueryDefinition;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchContextFactory;
import com.liferay.portal.kernel.search.SearchResultUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sergio González
 */
public class DefaultMBAdminListDisplayContext
	implements MBAdminListDisplayContext {

	public DefaultMBAdminListDisplayContext(
		HttpServletRequest request, HttpServletResponse response,
		long categoryId) {

		_request = request;

		_categoryId = categoryId;
	}

	@Override
	public int getEntriesDelta() {
		PortalPreferences portalPreferences =
			PortletPreferencesFactoryUtil.getPortalPreferences(_request);

		return GetterUtil.getInteger(
			portalPreferences.getValue(
				MBPortletKeys.MESSAGE_BOARDS_ADMIN, "entriesDelta"),
			SearchContainer.DEFAULT_DELTA);
	}

	@Override
	public UUID getUuid() {
		return _UUID;
	}

	@Override
	public boolean isShowSearch() {
		String keywords = ParamUtil.getString(_request, "keywords");

		if (Validator.isNotNull(keywords)) {
			return true;
		}

		String mvcRenderCommandName = ParamUtil.getString(
			_request, "mvcRenderCommandName");

		if (mvcRenderCommandName.equals("/message_boards/search")) {
			return true;
		}

		return false;
	}

	@Override
	public void populateResultsAndTotal(SearchContainer searchContainer)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (isShowSearch()) {
			long searchCategoryId = ParamUtil.getLong(
				_request, "searchCategoryId");

			long[] categoryIdsArray = null;

			List categoryIds = new ArrayList();

			categoryIds.add(Long.valueOf(searchCategoryId));

			MBCategoryServiceUtil.getSubcategoryIds(
				categoryIds, themeDisplay.getScopeGroupId(), searchCategoryId);

			categoryIdsArray = StringUtil.split(
				StringUtil.merge(categoryIds), 0L);

			Indexer indexer = IndexerRegistryUtil.getIndexer(MBMessage.class);

			SearchContext searchContext = SearchContextFactory.getInstance(
				_request);

			searchContext.setAttribute("paginationType", "more");
			searchContext.setCategoryIds(categoryIdsArray);
			searchContext.setEnd(searchContainer.getEnd());
			searchContext.setIncludeAttachments(true);

			String keywords = ParamUtil.getString(_request, "keywords");

			searchContext.setKeywords(keywords);

			searchContext.setStart(searchContainer.getStart());

			Hits hits = indexer.search(searchContext);

			searchContainer.setResults(
				SearchResultUtil.getSearchResults(hits, _request.getLocale()));

			searchContainer.setSearch(true);
			searchContainer.setTotal(hits.getLength());
		}
		else {
			int status = WorkflowConstants.STATUS_APPROVED;

			PermissionChecker permissionChecker =
				themeDisplay.getPermissionChecker();

			if (permissionChecker.isContentReviewer(
					themeDisplay.getCompanyId(),
					themeDisplay.getScopeGroupId())) {

				status = WorkflowConstants.STATUS_ANY;
			}

			QueryDefinition<?> queryDefinition = new QueryDefinition<>(
				status, themeDisplay.getUserId(), true,
				searchContainer.getStart(), searchContainer.getEnd(),
				searchContainer.getOrderByComparator());

			searchContainer.setTotal(
				MBCategoryServiceUtil.getCategoriesAndThreadsCount(
					themeDisplay.getScopeGroupId(), _categoryId,
					queryDefinition));
			searchContainer.setResults(
				MBCategoryServiceUtil.getCategoriesAndThreads(
					themeDisplay.getScopeGroupId(), _categoryId,
					queryDefinition));
		}
	}

	@Override
	public void setEntriesDelta(SearchContainer searchContainer) {
		int entriesDelta = ParamUtil.getInteger(
			_request, searchContainer.getDeltaParam());

		if (entriesDelta > 0) {
			PortalPreferences portalPreferences =
				PortletPreferencesFactoryUtil.getPortalPreferences(_request);

			portalPreferences.setValue(
				MBPortletKeys.MESSAGE_BOARDS_ADMIN, "entriesDelta",
				String.valueOf(entriesDelta));
		}
	}

	private static final UUID _UUID = UUID.fromString(
		"f3efa0bd-ca31-43c5-bdfe-164ee683b39e");

	private final long _categoryId;
	private final HttpServletRequest _request;

}