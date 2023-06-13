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

package com.liferay.user.associated.data.anonymizer;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.user.associated.data.entity.UADEntity;

import java.util.List;

/**
 * @author William Newbury
 */
public abstract class BaseUADEntityAnonymizer implements UADEntityAnonymizer {

	@Override
	public void autoAnonymizeAll(long userId) throws PortalException {
		for (UADEntity uadEntity : getUADEntities(userId)) {
			autoAnonymize(uadEntity);
		}
	}

	@Override
	public void deleteAll(long userId) throws PortalException {
		for (UADEntity uadEntity : getUADEntities(userId)) {
			delete(uadEntity);
		}
	}

	protected List<UADEntity> getUADEntities(long userId) {
		return getUADEntities(userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
	}

	protected abstract List<UADEntity> getUADEntities(
		long userId, int start, int end);

}