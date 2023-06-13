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

package com.liferay.blogs.web.internal.sharing;

import com.liferay.blogs.model.BlogsEntry;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.osgi.util.service.Snapshot;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.servlet.taglib.ui.MenuItem;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.sharing.configuration.SharingConfiguration;
import com.liferay.sharing.configuration.SharingConfigurationFactory;
import com.liferay.sharing.display.context.util.SharingDropdownItemFactory;
import com.liferay.sharing.display.context.util.SharingMenuItemFactory;
import com.liferay.sharing.security.permission.SharingPermission;
import com.liferay.sharing.service.SharingEntryLocalService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alejandro Tardín
 */
public class BlogsEntrySharingUtil {

	public static boolean containsManageCollaboratorsPermission(
		PermissionChecker permissionChecker, BlogsEntry blogsEntry) {

		try {
			long classNameId = PortalUtil.getClassNameId(BlogsEntry.class);

			SharingEntryLocalService sharingEntryLocalService =
				_sharingEntryLocalServiceSnapshot.get();

			int count = sharingEntryLocalService.getSharingEntriesCount(
				classNameId, blogsEntry.getEntryId());

			if (count == 0) {
				return false;
			}

			SharingPermission sharingPermission =
				_sharingPermissionSnapshot.get();

			return sharingPermission.containsManageCollaboratorsPermission(
				permissionChecker, classNameId, blogsEntry.getEntryId(),
				blogsEntry.getGroupId());
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	public static boolean containsSharePermission(
		PermissionChecker permissionChecker, BlogsEntry blogsEntry) {

		try {
			SharingPermission sharingPermission =
				_sharingPermissionSnapshot.get();

			return sharingPermission.containsSharePermission(
				permissionChecker, PortalUtil.getClassNameId(BlogsEntry.class),
				blogsEntry.getEntryId(), blogsEntry.getGroupId());
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	public static DropdownItem createManageCollaboratorsDropdownItem(
		BlogsEntry blogsEntry, HttpServletRequest httpServletRequest) {

		try {
			SharingDropdownItemFactory sharingDropdownItemFactory =
				_sharingDropdownItemFactorySnapshot.get();

			return sharingDropdownItemFactory.
				createManageCollaboratorsDropdownItem(
					BlogsEntry.class.getName(), blogsEntry.getEntryId(),
					httpServletRequest);
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	public static MenuItem createManageCollaboratorsMenuItem(
		BlogsEntry blogsEntry, HttpServletRequest httpServletRequest) {

		try {
			SharingMenuItemFactory sharingMenuItemFactory =
				_sharingMenuItemFactorySnapshot.get();

			return sharingMenuItemFactory.createManageCollaboratorsMenuItem(
				BlogsEntry.class.getName(), blogsEntry.getEntryId(),
				httpServletRequest);
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	public static DropdownItem createShareDropdownItem(
		BlogsEntry blogsEntry, HttpServletRequest httpServletRequest) {

		try {
			SharingDropdownItemFactory sharingDropdownItemFactory =
				_sharingDropdownItemFactorySnapshot.get();

			return sharingDropdownItemFactory.createShareDropdownItem(
				BlogsEntry.class.getName(), blogsEntry.getEntryId(),
				httpServletRequest);
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	public static MenuItem createShareMenuItem(
		BlogsEntry blogsEntry, HttpServletRequest httpServletRequest) {

		try {
			SharingMenuItemFactory sharingMenuItemFactory =
				_sharingMenuItemFactorySnapshot.get();

			return sharingMenuItemFactory.createShareMenuItem(
				BlogsEntry.class.getName(), blogsEntry.getEntryId(),
				httpServletRequest);
		}
		catch (PortalException portalException) {
			throw new SystemException(portalException);
		}
	}

	public static boolean isSharingEnabled(long groupId)
		throws PortalException {

		SharingConfigurationFactory sharingConfigurationFactory =
			_sharingConfigurationFactorySnapshot.get();

		GroupLocalService groupLocalService = _groupLocalServiceSnapshot.get();

		SharingConfiguration groupSharingConfiguration =
			sharingConfigurationFactory.getGroupSharingConfiguration(
				groupLocalService.getGroup(groupId));

		return groupSharingConfiguration.isEnabled();
	}

	private static final Snapshot<GroupLocalService>
		_groupLocalServiceSnapshot = new Snapshot<>(
			BlogsEntrySharingUtil.class, GroupLocalService.class);
	private static final Snapshot<SharingConfigurationFactory>
		_sharingConfigurationFactorySnapshot = new Snapshot<>(
			BlogsEntrySharingUtil.class, SharingConfigurationFactory.class);
	private static final Snapshot<SharingDropdownItemFactory>
		_sharingDropdownItemFactorySnapshot = new Snapshot<>(
			BlogsEntrySharingUtil.class, SharingDropdownItemFactory.class);
	private static final Snapshot<SharingEntryLocalService>
		_sharingEntryLocalServiceSnapshot = new Snapshot<>(
			BlogsEntrySharingUtil.class, SharingEntryLocalService.class);
	private static final Snapshot<SharingMenuItemFactory>
		_sharingMenuItemFactorySnapshot = new Snapshot<>(
			BlogsEntrySharingUtil.class, SharingMenuItemFactory.class);
	private static final Snapshot<SharingPermission>
		_sharingPermissionSnapshot = new Snapshot<>(
			BlogsEntrySharingUtil.class, SharingPermission.class);

}