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

package com.liferay.user.associated.data.exporter;

import aQute.bnd.annotation.ProviderType;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.user.associated.data.entity.UADEntity;

/**
 * @author William Newbury
 */
@ProviderType
public interface UADEntityExporter {

	public long count(long userId) throws PortalException;

	public void export(UADEntity uadEntity) throws PortalException;

	public void exportAll(long userId, PortletDataContext portletDataContext)
		throws PortalException;

	public <T extends UADEntity> StagedModelDataHandler<T>
		getStagedModelDataHandler();

	public void prepareManifestSummary(
			long userId, PortletDataContext portletDataContext)
		throws PortalException;

}