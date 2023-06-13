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

package com.liferay.journal.web.internal.security.permission.resource;

import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.security.permission.DDMPermissionSupport;
import com.liferay.osgi.util.service.Snapshot;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;

/**
 * @author Rafael Praxedes
 */
public class DDMTemplatePermission {

	public static boolean contains(
			PermissionChecker permissionChecker, DDMTemplate ddmTemplate,
			String actionId)
		throws PortalException {

		ModelResourcePermission<DDMTemplate> modelResourcePermission =
			_ddmTemplateModelResourcePermissionSnapshot.get();

		return modelResourcePermission.contains(
			permissionChecker, ddmTemplate, actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long ddmTemplateId,
			String actionId)
		throws PortalException {

		ModelResourcePermission<DDMTemplate> modelResourcePermission =
			_ddmTemplateModelResourcePermissionSnapshot.get();

		return modelResourcePermission.contains(
			permissionChecker, ddmTemplateId, actionId);
	}

	public static boolean containsAddTemplatePermission(
			PermissionChecker permissionChecker, long groupId, long classNameId,
			long resourceClassNameId)
		throws PortalException {

		DDMPermissionSupport ddmPermissionSupport =
			_ddmPermissionSupportSnapshot.get();

		return ddmPermissionSupport.containsAddTemplatePermission(
			permissionChecker, groupId, classNameId, resourceClassNameId);
	}

	public static String getTemplateModelResourceName(long resourceClassNameId)
		throws PortalException {

		DDMPermissionSupport ddmPermissionSupport =
			_ddmPermissionSupportSnapshot.get();

		return ddmPermissionSupport.getTemplateModelResourceName(
			resourceClassNameId);
	}

	private static final Snapshot<DDMPermissionSupport>
		_ddmPermissionSupportSnapshot = new Snapshot<>(
			DDMTemplatePermission.class, DDMPermissionSupport.class);
	private static final Snapshot<ModelResourcePermission<DDMTemplate>>
		_ddmTemplateModelResourcePermissionSnapshot = new Snapshot<>(
			DDMTemplatePermission.class,
			Snapshot.cast(ModelResourcePermission.class),
			"(model.class.name=com.liferay.dynamic.data.mapping.model." +
				"DDMTemplate)");

}