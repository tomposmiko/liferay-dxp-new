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

package com.liferay.segments.internal.security.permission.resource;

import com.liferay.exportimport.kernel.staging.permission.StagingPermission;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.BaseModelResourcePermissionWrapper;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionLogic;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.segments.constants.SegmentsConstants;
import com.liferay.segments.constants.SegmentsEntryConstants;
import com.liferay.segments.constants.SegmentsPortletKeys;
import com.liferay.segments.model.SegmentsEntry;
import com.liferay.segments.service.SegmentsEntryLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo García
 */
@Component(
	property = "model.class.name=com.liferay.segments.model.SegmentsEntry",
	service = ModelResourcePermission.class
)
public class SegmentsEntryModelResourcePermissionWrapper
	extends BaseModelResourcePermissionWrapper<SegmentsEntry> {

	@Override
	protected ModelResourcePermission<SegmentsEntry>
		doGetModelResourcePermission() {

		return ModelResourcePermissionFactory.create(
			SegmentsEntry.class, SegmentsEntry::getSegmentsEntryId,
			_segmentsEntryLocalService::getSegmentsEntry,
			_portletResourcePermission,
			(modelResourcePermission, consumer) -> consumer.accept(
				new StagedModelResourcePermissionLogic(_stagingPermission)));
	}

	@Reference(
		target = "(resource.name=" + SegmentsConstants.RESOURCE_NAME + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

	@Reference
	private SegmentsEntryLocalService _segmentsEntryLocalService;

	@Reference
	private StagingPermission _stagingPermission;

	private static class StagedModelResourcePermissionLogic
		implements ModelResourcePermissionLogic<SegmentsEntry> {

		@Override
		public Boolean contains(
				PermissionChecker permissionChecker, String name,
				SegmentsEntry segmentsEntry, String actionId)
			throws PortalException {

			if (actionId.equals(ActionKeys.UPDATE) &&
				SegmentsEntryConstants.SOURCE_ASAH_FARO_BACKEND.equals(
					segmentsEntry.getSource()) &&
				(!FeatureFlagManagerUtil.isEnabled("LPS-172194") ||
				 Validator.isNull(segmentsEntry.getCriteria()))) {

				return false;
			}

			return _stagingPermission.hasPermission(
				permissionChecker, segmentsEntry.getGroupId(),
				SegmentsEntry.class.getName(),
				segmentsEntry.getSegmentsEntryId(),
				SegmentsPortletKeys.SEGMENTS, actionId);
		}

		private StagedModelResourcePermissionLogic(
			StagingPermission stagingPermission) {

			_stagingPermission = stagingPermission;
		}

		private final StagingPermission _stagingPermission;

	}

}