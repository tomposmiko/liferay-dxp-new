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

package com.liferay.portal.search.engine.adapter.search;

import com.liferay.portal.search.engine.adapter.ccr.CrossClusterRequest;

/**
 * @author Bryan Engler
 */
public class OpenPointInTimeRequest
	extends CrossClusterRequest
	implements SearchRequest<OpenPointInTimeResponse> {

	public OpenPointInTimeRequest(long keepAliveMinutes) {
		_keepAliveMinutes = keepAliveMinutes;

		setPreferLocalCluster(true);
	}

	@Override
	public OpenPointInTimeResponse accept(
		SearchRequestExecutor searchRequestExecutor) {

		return searchRequestExecutor.executeSearchRequest(this);
	}

	public String[] getIndices() {
		return _indices;
	}

	public long getKeepAliveMinutes() {
		return _keepAliveMinutes;
	}

	public void setIndices(String... indices) {
		_indices = indices;
	}

	private String[] _indices;
	private final long _keepAliveMinutes;

}