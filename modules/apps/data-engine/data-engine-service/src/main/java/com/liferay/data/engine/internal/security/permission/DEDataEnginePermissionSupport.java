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

package com.liferay.data.engine.internal.security.permission;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalService;

/**
 * @author Leonardo Barros
 */
public class DEDataEnginePermissionSupport {

	public static final String RESOURCE_NAME = "com.liferay.data.engine";

	public DEDataEnginePermissionSupport(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	public boolean contains(
		PermissionChecker permissionChecker, String name, long classPK,
		String actionId) {

		Group group = _groupLocalService.fetchGroup(classPK);

		if ((group != null) && group.isStagingGroup()) {
			group = group.getLiveGroup();
		}

		return permissionChecker.hasPermission(group, name, classPK, actionId);
	}

	private final GroupLocalService _groupLocalService;

}