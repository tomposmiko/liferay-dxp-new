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

package com.liferay.frontend.taglib.clay.servlet.taglib.display.context;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Drew Brokke
 */
public class SearchContainerManagementToolbarDisplayContext
	extends BaseManagementToolbarDisplayContext {

	public SearchContainerManagementToolbarDisplayContext(
		HttpServletRequest httpServletRequest,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		SearchContainer<?> searchContainer) {

		super(
			httpServletRequest, liferayPortletRequest, liferayPortletResponse);

		this.searchContainer = searchContainer;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *             #SearchContainerManagementToolbarDisplayContext(
	 *             HttpServletRequest, LiferayPortletRequest,
	 *             LiferayPortletResponse, SearchContainer)}
	 */
	@Deprecated
	public SearchContainerManagementToolbarDisplayContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		HttpServletRequest httpServletRequest,
		SearchContainer<?> searchContainer) {

		this(
			httpServletRequest, liferayPortletRequest, liferayPortletResponse,
			searchContainer);
	}

	@Override
	public int getItemsTotal() {
		return searchContainer.getTotal();
	}

	@Override
	public String getSearchActionURL() {
		PortletURL searchActionURL = getPortletURL();

		return searchActionURL.toString();
	}

	@Override
	public String getSearchContainerId() {
		return searchContainer.getId(httpServletRequest, getNamespace());
	}

	@Override
	public Boolean isSelectable() {
		if (getItemsTotal() == 0) {
			return false;
		}

		return true;
	}

	@Override
	protected String getOrderByCol() {
		return searchContainer.getOrderByCol();
	}

	@Override
	protected String getOrderByColParam() {
		return searchContainer.getOrderByColParam();
	}

	@Override
	protected String getOrderByType() {
		return searchContainer.getOrderByType();
	}

	@Override
	protected String getOrderByTypeParam() {
		return searchContainer.getOrderByTypeParam();
	}

	protected SearchContainer<?> searchContainer;

}