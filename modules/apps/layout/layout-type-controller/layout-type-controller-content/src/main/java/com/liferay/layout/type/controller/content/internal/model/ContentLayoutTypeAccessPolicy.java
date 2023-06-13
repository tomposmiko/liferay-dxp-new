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

package com.liferay.layout.type.controller.content.internal.model;

import com.liferay.layout.type.controller.model.BaseLayoutTypeAccessPolicy;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutTypeAccessPolicy;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jürgen Kappler
 */
@Component(
	property = "layout.type=" + LayoutConstants.TYPE_CONTENT,
	service = LayoutTypeAccessPolicy.class
)
public class ContentLayoutTypeAccessPolicy extends BaseLayoutTypeAccessPolicy {

	@Override
	public boolean isUpdateLayoutAllowed(
			PermissionChecker permissionChecker, Layout layout)
		throws PortalException {

		return LayoutPermissionUtil.containsLayoutRestrictedUpdatePermission(
			permissionChecker, layout);
	}

}