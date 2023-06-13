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

package com.liferay.portal.language.override.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.language.override.constants.PLOActionKeys;
import com.liferay.portal.language.override.model.PLOEntry;
import com.liferay.portal.language.override.service.base.PLOEntryServiceBaseImpl;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = {
		"json.web.service.context.name=portallanguageoverride",
		"json.web.service.context.path=PLOEntry"
	},
	service = AopService.class
)
public class PLOEntryServiceImpl extends PLOEntryServiceBaseImpl {

	@Override
	public void deletePLOEntries(String key) throws PortalException {
		PermissionChecker permissionChecker = getPermissionChecker();

		PortalPermissionUtil.check(
			permissionChecker, PLOActionKeys.MANAGE_LANGUAGE_OVERRIDES);

		ploEntryLocalService.deletePLOEntries(
			permissionChecker.getCompanyId(), key);
	}

	@Override
	public PLOEntry deletePLOEntry(String key, String languageId)
		throws PortalException {

		PermissionChecker permissionChecker = getPermissionChecker();

		PortalPermissionUtil.check(
			permissionChecker, PLOActionKeys.MANAGE_LANGUAGE_OVERRIDES);

		return ploEntryLocalService.deletePLOEntry(
			permissionChecker.getCompanyId(), key, languageId);
	}

	@Override
	public void setPLOEntries(String key, Map<Locale, String> localizationMap)
		throws PortalException {

		PermissionChecker permissionChecker = getPermissionChecker();

		PortalPermissionUtil.check(
			permissionChecker, PLOActionKeys.MANAGE_LANGUAGE_OVERRIDES);

		ploEntryLocalService.setPLOEntries(
			permissionChecker.getCompanyId(), permissionChecker.getUserId(),
			key, localizationMap);
	}

}