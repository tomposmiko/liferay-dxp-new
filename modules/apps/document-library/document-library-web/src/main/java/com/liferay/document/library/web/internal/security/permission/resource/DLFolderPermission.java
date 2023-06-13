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

package com.liferay.document.library.web.internal.security.permission.resource;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.osgi.util.service.Snapshot;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionUtil;

/**
 * @author Preston Crary
 */
public class DLFolderPermission {

	public static void check(
			PermissionChecker permissionChecker, DLFolder dlFolder,
			String actionId)
		throws PortalException {

		ModelResourcePermission<DLFolder> modelResourcePermission =
			_dlFolderModelResourcePermissionSnapshot.get();

		modelResourcePermission.check(permissionChecker, dlFolder, actionId);
	}

	public static void check(
			PermissionChecker permissionChecker, Folder folder, String actionId)
		throws PortalException {

		ModelResourcePermission<Folder> modelResourcePermission =
			_folderModelResourcePermissionSnapshot.get();

		modelResourcePermission.check(permissionChecker, folder, actionId);
	}

	public static void check(
			PermissionChecker permissionChecker, long groupId, long folderId,
			String actionId)
		throws PortalException {

		ModelResourcePermission<Folder> modelResourcePermission =
			_folderModelResourcePermissionSnapshot.get();

		ModelResourcePermissionUtil.check(
			modelResourcePermission, permissionChecker, groupId, folderId,
			actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, DLFolder dlFolder,
			String actionId)
		throws PortalException {

		ModelResourcePermission<DLFolder> modelResourcePermission =
			_dlFolderModelResourcePermissionSnapshot.get();

		return modelResourcePermission.contains(
			permissionChecker, dlFolder, actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, Folder folder, String actionId)
		throws PortalException {

		ModelResourcePermission<Folder> modelResourcePermission =
			_folderModelResourcePermissionSnapshot.get();

		return modelResourcePermission.contains(
			permissionChecker, folder, actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long groupId, long folderId,
			String actionId)
		throws PortalException {

		ModelResourcePermission<Folder> modelResourcePermission =
			_folderModelResourcePermissionSnapshot.get();

		return ModelResourcePermissionUtil.contains(
			modelResourcePermission, permissionChecker, groupId, folderId,
			actionId);
	}

	private static final Snapshot<ModelResourcePermission<DLFolder>>
		_dlFolderModelResourcePermissionSnapshot = new Snapshot<>(
			DLFolderPermission.class,
			Snapshot.cast(ModelResourcePermission.class),
			"(model.class.name=com.liferay.document.library.kernel.model." +
				"DLFolder)");
	private static final Snapshot<ModelResourcePermission<Folder>>
		_folderModelResourcePermissionSnapshot = new Snapshot<>(
			DLFolderPermission.class,
			Snapshot.cast(ModelResourcePermission.class),
			"(model.class.name=com.liferay.portal.kernel.repository.model." +
				"Folder)");

}