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

package com.liferay.fragment.service.impl;

import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.base.FragmentEntryLinkServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.BaseModelPermissionCheckerUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Portal;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(
	property = {
		"json.web.service.context.name=fragment",
		"json.web.service.context.path=FragmentEntryLink"
	},
	service = AopService.class
)
public class FragmentEntryLinkServiceImpl
	extends FragmentEntryLinkServiceBaseImpl {

	@Override
	public FragmentEntryLink addFragmentEntryLink(
			long groupId, long originalFragmentEntryLinkId,
			long fragmentEntryId, long classNameId, long classPK, String css,
			String html, String js, String editableValues, String namespace,
			int position, String rendererKey, ServiceContext serviceContext)
		throws PortalException {

		_checkPermission(groupId, _portal.getClassName(classNameId), classPK);

		return fragmentEntryLinkLocalService.addFragmentEntryLink(
			getUserId(), groupId, originalFragmentEntryLinkId, fragmentEntryId,
			classNameId, classPK, css, html, js, editableValues, namespace,
			position, rendererKey, serviceContext);
	}

	@Override
	public FragmentEntryLink deleteFragmentEntryLink(long fragmentEntryLinkId)
		throws PortalException {

		FragmentEntryLink fragmentEntryLink =
			fragmentEntryLinkPersistence.findByPrimaryKey(fragmentEntryLinkId);

		_checkPermission(
			fragmentEntryLink.getGroupId(), fragmentEntryLink.getClassName(),
			fragmentEntryLink.getClassPK());

		return fragmentEntryLinkLocalService.deleteFragmentEntryLink(
			fragmentEntryLinkId);
	}

	@Override
	public FragmentEntryLink updateFragmentEntryLink(
			long fragmentEntryLinkId, String editableValues)
		throws PortalException {

		FragmentEntryLink fragmentEntryLink =
			fragmentEntryLinkPersistence.findByPrimaryKey(fragmentEntryLinkId);

		_checkPermission(
			fragmentEntryLink.getGroupId(), fragmentEntryLink.getClassName(),
			fragmentEntryLink.getClassPK());

		return fragmentEntryLinkLocalService.updateFragmentEntryLink(
			fragmentEntryLinkId, editableValues);
	}

	@Override
	public void updateFragmentEntryLinks(
			long groupId, long classNameId, long classPK,
			long[] fragmentEntryIds, String editableValues,
			ServiceContext serviceContext)
		throws PortalException {

		_checkPermission(groupId, _portal.getClassName(classNameId), classPK);

		fragmentEntryLinkLocalService.updateFragmentEntryLinks(
			getUserId(), groupId, classNameId, classNameId, fragmentEntryIds,
			editableValues, serviceContext);
	}

	private void _checkPermission(long groupId, String className, long classPK)
		throws PortalException {

		Boolean containsPermission =
			BaseModelPermissionCheckerUtil.containsBaseModelPermission(
				getPermissionChecker(), groupId, className, classPK,
				ActionKeys.UPDATE);

		if ((containsPermission == null) || !containsPermission) {
			throw new PrincipalException.MustHavePermission(
				getUserId(), className, classPK, ActionKeys.UPDATE);
		}
	}

	@Reference
	private Portal _portal;

}