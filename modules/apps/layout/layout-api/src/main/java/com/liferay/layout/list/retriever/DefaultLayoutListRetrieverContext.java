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

package com.liferay.layout.list.retriever;

import com.liferay.info.pagination.Pagination;

import java.util.Optional;

/**
 * @author Eudaldo Alonso
 */
public class DefaultLayoutListRetrieverContext
	implements LayoutListRetrieverContext {

	@Override
	public Optional<Pagination> getPaginationOptional() {
		return Optional.ofNullable(_pagination);
	}

	@Override
	public Optional<long[]> getSegmentsEntryIdsOptional() {
		return Optional.ofNullable(_segmentsEntryIds);
	}

	@Override
	public Optional<long[]> getSegmentsExperienceIdsOptional() {
		return Optional.ofNullable(_segmentsExperienceIds);
	}

	public void setPagination(Pagination pagination) {
		_pagination = pagination;
	}

	public void setSegmentsEntryIds(long[] segmentsEntryIds) {
		_segmentsEntryIds = segmentsEntryIds;
	}

	public void setSegmentsExperienceIdsOptional(
		long[] segmentsExperienceIdsOptional) {

		_segmentsExperienceIds = segmentsExperienceIdsOptional;
	}

	private Pagination _pagination;
	private long[] _segmentsEntryIds;
	private long[] _segmentsExperienceIds;

}