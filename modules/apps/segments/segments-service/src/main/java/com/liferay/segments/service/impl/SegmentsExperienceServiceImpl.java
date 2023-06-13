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

package com.liferay.segments.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.segments.constants.SegmentsActionKeys;
import com.liferay.segments.constants.SegmentsConstants;
import com.liferay.segments.model.SegmentsExperience;
import com.liferay.segments.service.base.SegmentsExperienceServiceBaseImpl;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author David Arques
 */
@Component(
	property = {
		"json.web.service.context.name=segments",
		"json.web.service.context.path=SegmentsExperience"
	},
	service = AopService.class
)
public class SegmentsExperienceServiceImpl
	extends SegmentsExperienceServiceBaseImpl {

	@Override
	public SegmentsExperience addSegmentsExperience(
			long segmentsEntryId, long classNameId, long classPK,
			Map<Locale, String> nameMap, boolean active,
			ServiceContext serviceContext)
		throws PortalException {

		if (!_hasUpdateLayoutPermission(_getPublishedLayoutClassPK(classPK))) {
			_portletResourcePermission.check(
				getPermissionChecker(), serviceContext.getScopeGroupId(),
				SegmentsActionKeys.MANAGE_SEGMENTS_ENTRIES);
		}

		return segmentsExperienceLocalService.addSegmentsExperience(
			segmentsEntryId, classNameId, classPK, nameMap, active,
			serviceContext);
	}

	@Override
	public SegmentsExperience deleteSegmentsExperience(
			long segmentsExperienceId)
		throws PortalException {

		_segmentsExperienceResourcePermission.check(
			getPermissionChecker(),
			segmentsExperienceLocalService.getSegmentsExperience(
				segmentsExperienceId),
			ActionKeys.DELETE);

		return segmentsExperienceLocalService.deleteSegmentsExperience(
			segmentsExperienceId);
	}

	@Override
	public SegmentsExperience fetchSegmentsExperience(
			long groupId, String segmentsExperienceKey)
		throws PortalException {

		SegmentsExperience segmentsExperience =
			segmentsExperienceLocalService.getSegmentsExperience(
				groupId, segmentsExperienceKey);

		_segmentsExperienceResourcePermission.check(
			getPermissionChecker(), segmentsExperience, ActionKeys.VIEW);

		return segmentsExperience;
	}

	@Override
	public SegmentsExperience getSegmentsExperience(long segmentsExperienceId)
		throws PortalException {

		SegmentsExperience segmentsExperience =
			segmentsExperienceLocalService.getSegmentsExperience(
				segmentsExperienceId);

		_segmentsExperienceResourcePermission.check(
			getPermissionChecker(), segmentsExperience, ActionKeys.VIEW);

		return segmentsExperience;
	}

	@Override
	public List<SegmentsExperience> getSegmentsExperiences(
			long groupId, long classNameId, long classPK, boolean active)
		throws PortalException {

		long publishedLayoutClassPK = _getPublishedLayoutClassPK(classPK);

		if (_hasUpdateLayoutPermission(publishedLayoutClassPK)) {
			return segmentsExperiencePersistence.findByG_C_C_A(
				groupId, classNameId, publishedLayoutClassPK, active);
		}

		return segmentsExperiencePersistence.filterFindByG_C_C_A(
			groupId, classNameId, publishedLayoutClassPK, active);
	}

	@Override
	public List<SegmentsExperience> getSegmentsExperiences(
			long groupId, long classNameId, long classPK, boolean active,
			int start, int end,
			OrderByComparator<SegmentsExperience> orderByComparator)
		throws PortalException {

		long publishedLayoutClassPK = _getPublishedLayoutClassPK(classPK);

		if (_hasUpdateLayoutPermission(publishedLayoutClassPK)) {
			return segmentsExperiencePersistence.findByG_C_C_A(
				groupId, classNameId, publishedLayoutClassPK, active, start,
				end, orderByComparator);
		}

		return segmentsExperiencePersistence.filterFindByG_C_C_A(
			groupId, classNameId, publishedLayoutClassPK, active, start, end,
			orderByComparator);
	}

	@Override
	public int getSegmentsExperiencesCount(
			long groupId, long classNameId, long classPK, boolean active)
		throws PortalException {

		long publishedLayoutClassPK = _getPublishedLayoutClassPK(classPK);

		if (_hasUpdateLayoutPermission(publishedLayoutClassPK)) {
			return segmentsExperiencePersistence.countByG_C_C_A(
				groupId, classNameId, publishedLayoutClassPK, active);
		}

		return segmentsExperiencePersistence.filterCountByG_C_C_A(
			groupId, classNameId, publishedLayoutClassPK, active);
	}

	@Override
	public SegmentsExperience updateSegmentsExperience(
			long segmentsExperienceId, long segmentsEntryId,
			Map<Locale, String> nameMap, boolean active)
		throws PortalException {

		_segmentsExperienceResourcePermission.check(
			getPermissionChecker(),
			segmentsExperienceLocalService.getSegmentsExperience(
				segmentsExperienceId),
			ActionKeys.UPDATE);

		return segmentsExperienceLocalService.updateSegmentsExperience(
			segmentsExperienceId, segmentsEntryId, nameMap, active);
	}

	@Override
	public void updateSegmentsExperiencePriority(
			long segmentsExperienceId, int newPriority)
		throws PortalException {

		SegmentsExperience segmentsExperience =
			segmentsExperiencePersistence.findByPrimaryKey(
				segmentsExperienceId);

		_segmentsExperienceResourcePermission.check(
			getPermissionChecker(), segmentsExperience, ActionKeys.UPDATE);

		SegmentsExperience swapSegmentsExperience =
			segmentsExperiencePersistence.fetchByG_C_C_P(
				segmentsExperience.getGroupId(),
				segmentsExperience.getClassNameId(),
				segmentsExperience.getClassPK(), newPriority);

		if (swapSegmentsExperience != null) {
			_segmentsExperienceResourcePermission.check(
				getPermissionChecker(), swapSegmentsExperience,
				ActionKeys.UPDATE);
		}

		segmentsExperienceLocalService.updateSegmentsExperiencePriority(
			segmentsExperienceId, newPriority);
	}

	private long _getPublishedLayoutClassPK(long classPK) {
		Layout layout = layoutLocalService.fetchLayout(classPK);

		if ((layout != null) &&
			(layout.getClassNameId() == classNameLocalService.getClassNameId(
				Layout.class)) &&
			(layout.getClassPK() != 0)) {

			return layout.getClassPK();
		}

		return classPK;
	}

	private boolean _hasUpdateLayoutPermission(long plid)
		throws PortalException {

		Layout layout = layoutLocalService.fetchLayout(plid);

		if ((layout != null) &&
			LayoutPermissionUtil.contains(
				getPermissionChecker(), layout, ActionKeys.UPDATE)) {

			return true;
		}

		return false;
	}

	@Reference(
		target = "(resource.name=" + SegmentsConstants.RESOURCE_NAME + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

	@Reference(
		target = "(model.class.name=com.liferay.segments.model.SegmentsExperience)"
	)
	private ModelResourcePermission<SegmentsExperience>
		_segmentsExperienceResourcePermission;

}