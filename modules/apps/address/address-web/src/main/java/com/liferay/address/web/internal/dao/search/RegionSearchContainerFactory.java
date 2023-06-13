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

package com.liferay.address.web.internal.dao.search;

import com.liferay.address.web.internal.constants.AddressPortletKeys;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.portlet.SearchOrderByUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.service.RegionServiceUtil;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Objects;

/**
 * @author Pei-Jung Lan
 */
public class RegionSearchContainerFactory {

	public static SearchContainer<Region> create(
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws PortalException {

		Boolean active = null;
		String emptyResultsMessage = "there-are-no-regions";

		String navigation = ParamUtil.getString(
			liferayPortletRequest, "navigation", "all");

		if (navigation.equals("active")) {
			active = Boolean.TRUE;
			emptyResultsMessage = "there-are-no-active-regions";
		}
		else if (navigation.equals("inactive")) {
			active = Boolean.FALSE;
			emptyResultsMessage = "there-are-no-inactive-regions";
		}

		SearchContainer<Region> searchContainer = new SearchContainer(
			liferayPortletRequest,
			PortletURLUtil.getCurrent(
				liferayPortletRequest, liferayPortletResponse),
			null, emptyResultsMessage);

		searchContainer.setId("regionSearchContainer");
		searchContainer.setOrderByCol(
			SearchOrderByUtil.getOrderByCol(
				liferayPortletRequest,
				AddressPortletKeys.COUNTRIES_MANAGEMENT_ADMIN, "order-by-col",
				"priority"));
		searchContainer.setOrderByType(
			SearchOrderByUtil.getOrderByType(
				liferayPortletRequest,
				AddressPortletKeys.COUNTRIES_MANAGEMENT_ADMIN, "order-by-type",
				"asc"));

		searchContainer.setOrderByComparator(
			_getOrderByComparator(
				searchContainer.getOrderByCol(),
				searchContainer.getOrderByType()));

		long countryId = ParamUtil.getLong(liferayPortletRequest, "countryId");
		String keywords = ParamUtil.getString(
			liferayPortletRequest, "keywords");

		BaseModelSearchResult<Region> baseModelSearchResult = null;

		if (Validator.isNotNull(keywords)) {
			baseModelSearchResult = RegionServiceUtil.searchRegions(
				PortalUtil.getCompanyId(liferayPortletRequest), active,
				keywords,
				LinkedHashMapBuilder.<String, Object>put(
					"countryId", countryId
				).build(),
				searchContainer.getStart(), searchContainer.getEnd(),
				searchContainer.getOrderByComparator());
		}
		else {
			if (active == null) {
				baseModelSearchResult =
					BaseModelSearchResult.createWithStartAndEnd(
						startAndEnd -> RegionServiceUtil.getRegions(
							countryId, startAndEnd.getStart(),
							startAndEnd.getEnd(),
							searchContainer.getOrderByComparator()),
						RegionServiceUtil.getRegionsCount(countryId),
						searchContainer.getStart(), searchContainer.getEnd());
			}
			else {
				boolean navigationActive = active;

				baseModelSearchResult =
					BaseModelSearchResult.createWithStartAndEnd(
						startAndEnd -> RegionServiceUtil.getRegions(
							countryId, navigationActive, startAndEnd.getStart(),
							startAndEnd.getEnd(),
							searchContainer.getOrderByComparator()),
						RegionServiceUtil.getRegionsCount(
							countryId, navigationActive),
						searchContainer.getStart(), searchContainer.getEnd());
			}
		}

		searchContainer.setResultsAndTotal(baseModelSearchResult);

		searchContainer.setRowChecker(
			new EmptyOnClickRowChecker(liferayPortletResponse));

		return searchContainer;
	}

	private static OrderByComparator<Region> _getOrderByComparator(
		String orderByCol, String orderByType) {

		boolean columnAscending = Objects.equals(orderByType, "asc");

		if (Objects.equals("priority", orderByCol)) {
			return OrderByComparatorFactoryUtil.create(
				"Region", "position", columnAscending, "name", true);
		}

		return OrderByComparatorFactoryUtil.create(
			"Region", orderByCol, columnAscending);
	}

}