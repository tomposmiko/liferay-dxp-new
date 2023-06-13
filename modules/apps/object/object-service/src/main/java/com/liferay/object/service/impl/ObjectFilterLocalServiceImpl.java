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

package com.liferay.object.service.impl;

import com.liferay.object.model.ObjectFilter;
import com.liferay.object.service.base.ObjectFilterLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(
	property = "model.class.name=com.liferay.object.model.ObjectFilter",
	service = AopService.class
)
public class ObjectFilterLocalServiceImpl
	extends ObjectFilterLocalServiceBaseImpl {

	@Override
	public ObjectFilter addObjectFilter(
			long userId, long objectFieldId, String filterBy, String filterType,
			String json)
		throws PortalException {

		ObjectFilter objectFilter = objectFilterPersistence.create(
			counterLocalService.increment());

		User user = _userLocalService.getUser(userId);

		objectFilter.setCompanyId(user.getCompanyId());
		objectFilter.setUserId(user.getUserId());
		objectFilter.setUserName(user.getFullName());

		objectFilter.setObjectFieldId(objectFieldId);
		objectFilter.setFilterBy(filterBy);
		objectFilter.setFilterType(filterType);
		objectFilter.setJSON(json);

		return objectFilterPersistence.update(objectFilter);
	}

	@Override
	public void deleteObjectFieldObjectFilter(long objectFieldId) {
		objectFilterPersistence.removeByObjectFieldId(objectFieldId);
	}

	@Override
	public List<ObjectFilter> getObjectFieldObjectFilter(long objectFieldId) {
		return objectFilterPersistence.findByObjectFieldId(objectFieldId);
	}

	@Reference
	private UserLocalService _userLocalService;

}