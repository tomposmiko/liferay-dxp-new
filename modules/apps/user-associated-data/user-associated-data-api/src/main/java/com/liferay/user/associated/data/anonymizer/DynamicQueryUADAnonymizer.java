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

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.user.associated.data.util.UADDynamicQueryUtil;

/**
 * @author William Newbury
 */
public abstract class DynamicQueryUADAnonymizer<T extends BaseModel>
	implements UADAnonymizer<T> {

	@Override
	public void autoAnonymizeAll(long userId, User anonymousUser)
		throws PortalException {

		ActionableDynamicQuery actionableDynamicQuery =
			getActionableDynamicQuery(userId);

		actionableDynamicQuery.setPerformActionMethod(
			(T baseModel) -> autoAnonymize(baseModel, userId, anonymousUser));

		actionableDynamicQuery.performActions();
	}

	@Override
	public long count(long userId) throws PortalException {
		return getActionableDynamicQuery(userId).performCount();
	}

	@Override
	public void deleteAll(long userId) throws PortalException {
		ActionableDynamicQuery actionableDynamicQuery =
			getActionableDynamicQuery(userId);

		actionableDynamicQuery.setPerformActionMethod(
			(T baseModel) -> delete(baseModel));

		actionableDynamicQuery.performActions();
	}

	protected abstract ActionableDynamicQuery doGetActionableDynamicQuery();

	protected abstract String[] doGetUserIdFieldNames();

	protected ActionableDynamicQuery getActionableDynamicQuery(long userId) {
		return UADDynamicQueryUtil.addActionableDynamicQueryCriteria(
			doGetActionableDynamicQuery(), doGetUserIdFieldNames(), userId);
	}

}