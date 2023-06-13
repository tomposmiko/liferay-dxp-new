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

package com.liferay.analytics.settings.rest.internal.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author Riccardo Ferrari
 */
public class AnalyticsChannel {

	@JsonProperty("dataSources")
	public AnalyticsDataSource[] getAnalyticsDataSources() {
		return _analyticsDataSources;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public Long getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public void setAnalyticsDataSources(
		AnalyticsDataSource[] analyticsDataSources) {

		_analyticsDataSources = analyticsDataSources;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setName(String name) {
		_name = name;
	}

	private AnalyticsDataSource[] _analyticsDataSources;
	private Date _createDate;
	private Long _id;
	private String _name;

}