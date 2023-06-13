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

package com.liferay.dispatch.web.internal.security.permisison.resource;

import com.liferay.dispatch.model.DispatchTrigger;
import com.liferay.osgi.util.service.Snapshot;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;

/**
 * @author Marco Leo
 */
public class DispatchTriggerPermission {

	public static boolean contains(
			PermissionChecker permissionChecker,
			DispatchTrigger dispatchTrigger, String actionId)
		throws PortalException {

		ModelResourcePermission<DispatchTrigger> modelResourcePermission =
			_dispatchTriggerModelResourcePermissionSnapshot.get();

		return modelResourcePermission.contains(
			permissionChecker, dispatchTrigger.getDispatchTriggerId(),
			actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long dispatchTriggerId,
			String actionId)
		throws PortalException {

		ModelResourcePermission<DispatchTrigger> modelResourcePermission =
			_dispatchTriggerModelResourcePermissionSnapshot.get();

		return modelResourcePermission.contains(
			permissionChecker, dispatchTriggerId, actionId);
	}

	private static final Snapshot<ModelResourcePermission<DispatchTrigger>>
		_dispatchTriggerModelResourcePermissionSnapshot = new Snapshot<>(
			DispatchTriggerPermission.class,
			Snapshot.cast(ModelResourcePermission.class),
			"(model.class.name=com.liferay.dispatch.model.DispatchTrigger)");

}