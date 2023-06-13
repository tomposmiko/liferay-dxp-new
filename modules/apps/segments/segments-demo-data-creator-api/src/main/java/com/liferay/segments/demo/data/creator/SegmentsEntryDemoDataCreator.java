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

package com.liferay.segments.demo.data.creator;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.segments.model.SegmentsEntry;

import java.io.IOException;

/**
 * @author Eduardo García
 */
@ProviderType
public interface SegmentsEntryDemoDataCreator {

	public SegmentsEntry create(long userId, long groupId)
		throws IOException, PortalException;

	public void delete() throws PortalException;

}