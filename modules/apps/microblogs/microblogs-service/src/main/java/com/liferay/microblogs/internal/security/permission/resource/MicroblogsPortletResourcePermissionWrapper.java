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

package com.liferay.microblogs.internal.security.permission.resource;

import com.liferay.exportimport.kernel.staging.permission.StagingPermission;
import com.liferay.microblogs.constants.MicroblogsConstants;
import com.liferay.microblogs.constants.MicroblogsPortletKeys;
import com.liferay.portal.kernel.security.permission.resource.BasePortletResourcePermissionWrapper;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermissionFactory;
import com.liferay.portal.kernel.security.permission.resource.StagedPortletPermissionLogic;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Preston Crary
 */
@Component(
	property = "resource.name=" + MicroblogsConstants.RESOURCE_NAME,
	service = PortletResourcePermission.class
)
public class MicroblogsPortletResourcePermissionWrapper
	extends BasePortletResourcePermissionWrapper {

	@Override
	protected PortletResourcePermission doGetPortletResourcePermission() {
		return PortletResourcePermissionFactory.create(
			MicroblogsConstants.RESOURCE_NAME,
			new StagedPortletPermissionLogic(
				_stagingPermission, MicroblogsPortletKeys.MICROBLOGS));
	}

	@Reference
	private StagingPermission _stagingPermission;

}