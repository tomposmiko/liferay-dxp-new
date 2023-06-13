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

import com.liferay.account.admin.web.internal.display.AccountGroupDisplay;
import com.liferay.account.model.AccountGroup;
import com.liferay.account.service.AccountGroupLocalServiceUtil;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.List;

/**
 * @author Albert Lee
 */
public class AccountGroupDisplaySearchContainerFactory {

	public static SearchContainer<AccountGroupDisplay> create(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		SearchContainer<AccountGroupDisplay>
			accountGroupDisplaySearchContainer = new SearchContainer(
				liferayPortletRequest,
				PortletURLUtil.getCurrent(
					liferayPortletRequest, liferayPortletResponse),
				null, "no-account-groups-were-found");

		accountGroupDisplaySearchContainer.setId("accountGroups");

		String orderByType = ParamUtil.getString(
			liferayPortletRequest, "orderByType", "asc");

		accountGroupDisplaySearchContainer.setOrderByType(orderByType);

		accountGroupDisplaySearchContainer.setRowChecker(
			new EmptyOnClickRowChecker(liferayPortletResponse));

		String keywords = ParamUtil.getString(
			liferayPortletRequest, "keywords");

		ThemeDisplay themeDisplay =
			(ThemeDisplay)liferayPortletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		BaseModelSearchResult<AccountGroup> baseModelSearchResult =
			AccountGroupLocalServiceUtil.searchAccountGroups(
				themeDisplay.getCompanyId(), keywords,
				accountGroupDisplaySearchContainer.getStart(),
				accountGroupDisplaySearchContainer.getEnd(),
				OrderByComparatorFactoryUtil.create(
					"AccountGroup", "name", orderByType.equals("asc")));

		List<AccountGroupDisplay> accountGroupDisplays =
			TransformUtil.transform(
				baseModelSearchResult.getBaseModels(), AccountGroupDisplay::of);

		accountGroupDisplaySearchContainer.setResults(accountGroupDisplays);

		accountGroupDisplaySearchContainer.setTotal(
			baseModelSearchResult.getLength());

		return accountGroupDisplaySearchContainer;
	}

}