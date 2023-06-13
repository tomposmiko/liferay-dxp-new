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

package com.liferay.account.admin.web.internal.dao.search;

import com.liferay.account.constants.AccountConstants;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.retriever.AccountUserRetriever;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pei-Jung Lan
 */
@Component(service = {})
public class AssignableAccountUserDisplaySearchContainerFactory {

	public static SearchContainer create(
			long accountEntryId, LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws PortalException {

		SearchContainer searchContainer = new SearchContainer(
			liferayPortletRequest,
			PortletURLUtil.getCurrent(
				liferayPortletRequest, liferayPortletResponse),
			null, "no-users-were-found");

		searchContainer.setId("accountUsers");

		String orderByCol = ParamUtil.getString(
			liferayPortletRequest, "orderByCol", "last-name");

		searchContainer.setOrderByCol(orderByCol);

		String orderByType = ParamUtil.getString(
			liferayPortletRequest, "orderByType", "asc");

		searchContainer.setOrderByType(orderByType);

		searchContainer.setRowChecker(
			new SelectAccountUserRowChecker(
				liferayPortletResponse, accountEntryId));

		String navigation = ParamUtil.getString(
			liferayPortletRequest, "navigation", "current-account-users");

		String keywords = ParamUtil.getString(
			liferayPortletRequest, "keywords", null);

		BaseModelSearchResult<User> baseModelSearchResult =
			_accountUserRetriever.searchAccountUsers(
				AccountConstants.ACCOUNT_ENTRY_ID_ANY,
				_getEmailAddressDomains(accountEntryId, navigation), keywords,
				WorkflowConstants.STATUS_APPROVED, searchContainer.getStart(),
				searchContainer.getDelta(), orderByCol,
				_isReverseOrder(orderByType));

		searchContainer.setResults(baseModelSearchResult.getBaseModels());
		searchContainer.setTotal(baseModelSearchResult.getLength());

		return searchContainer;
	}

	@Reference(unbind = "-")
	protected void setAccountEntryLocalService(
		AccountEntryLocalService accountEntryLocalService) {

		_accountEntryLocalService = accountEntryLocalService;
	}

	@Reference(unbind = "-")
	protected void setAccountUserRetriever(
		AccountUserRetriever accountUserRetriever) {

		_accountUserRetriever = accountUserRetriever;
	}

	private static String[] _getEmailAddressDomains(
		long accountEntryId, String navigation) {

		if (Objects.equals(navigation, "current-account-users")) {
			AccountEntry accountEntry =
				_accountEntryLocalService.fetchAccountEntry(accountEntryId);

			return StringUtil.split(accountEntry.getDomains());
		}

		return null;
	}

	private static boolean _isReverseOrder(String orderByType) {
		if (Objects.equals(orderByType, "desc")) {
			return true;
		}

		return false;
	}

	private static AccountEntryLocalService _accountEntryLocalService;
	private static AccountUserRetriever _accountUserRetriever;

}