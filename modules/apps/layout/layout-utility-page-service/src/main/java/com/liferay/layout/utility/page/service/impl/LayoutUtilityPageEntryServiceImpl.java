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

package com.liferay.layout.utility.page.service.impl;

import com.liferay.layout.utility.page.constants.LayoutUtilityPageActionKeys;
import com.liferay.layout.utility.page.constants.LayoutUtilityPageConstants;
import com.liferay.layout.utility.page.model.LayoutUtilityPageEntry;
import com.liferay.layout.utility.page.service.base.LayoutUtilityPageEntryServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = {
		"json.web.service.context.name=layoututilitypage",
		"json.web.service.context.path=LayoutUtilityPageEntry"
	},
	service = AopService.class
)
public class LayoutUtilityPageEntryServiceImpl
	extends LayoutUtilityPageEntryServiceBaseImpl {

	@Override
	public LayoutUtilityPageEntry addLayoutUtilityPageEntry(
			String externalReferenceCode, long groupId, long plid, String name,
			int type)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), groupId,
			LayoutUtilityPageActionKeys.ADD_LAYOUT_UTILITY_PAGE_ENTRY);

		return layoutUtilityPageEntryLocalService.addLayoutUtilityPageEntry(
			externalReferenceCode, getUserId(), groupId, plid, name, type);
	}

	@Override
	public LayoutUtilityPageEntry deleteLayoutUtilityPageEntry(
			long layoutUtilityPageEntryId)
		throws PortalException {

		return layoutUtilityPageEntryLocalService.deleteLayoutUtilityPageEntry(
			layoutUtilityPageEntryId);
	}

	@Override
	public LayoutUtilityPageEntry fetchLayoutUtilityPageEntry(
		long layoutUtilityPageEntryId) {

		return layoutUtilityPageEntryLocalService.fetchLayoutUtilityPageEntry(
			layoutUtilityPageEntryId);
	}

	@Override
	public LayoutUtilityPageEntry getDefaultLayoutUtilityPageEntry(
			long groupId, int type)
		throws PortalException {

		return layoutUtilityPageEntryLocalService.
			getDefaultLayoutUtilityPageEntry(groupId, type);
	}

	@Override
	public List<LayoutUtilityPageEntry> getLayoutUtilityPageEntries(
		long groupId) {

		return layoutUtilityPageEntryLocalService.getLayoutUtilityPageEntries(
			groupId);
	}

	@Override
	public List<LayoutUtilityPageEntry> getLayoutUtilityPageEntries(
		long groupId, int type, int start, int end,
		OrderByComparator<LayoutUtilityPageEntry> orderByComparator) {

		return layoutUtilityPageEntryLocalService.getLayoutUtilityPageEntries(
			groupId, type, start, end, orderByComparator);
	}

	@Override
	public List<LayoutUtilityPageEntry> getLayoutUtilityPageEntries(
		long groupId, int start, int end,
		OrderByComparator<LayoutUtilityPageEntry> orderByComparator) {

		return layoutUtilityPageEntryLocalService.getLayoutUtilityPageEntries(
			groupId, start, end, orderByComparator);
	}

	@Override
	public int getLayoutUtilityPageEntriesCount(long groupId) {
		return layoutUtilityPageEntryLocalService.
			getLayoutUtilityPageEntriesCount(groupId);
	}

	@Override
	public LayoutUtilityPageEntry setDefaultLayoutUtilityPageEntry(
			long layoutUtilityPageEntryId)
		throws PortalException {

		return layoutUtilityPageEntryLocalService.
			setDefaultLayoutUtilityPageEntry(layoutUtilityPageEntryId);
	}

	@Override
	public LayoutUtilityPageEntry updateLayoutUtilityPageEntry(
			long layoutUtilityPageEntryId, long plid, String name, int type)
		throws PortalException {

		return layoutUtilityPageEntryLocalService.updateLayoutUtilityPageEntry(
			layoutUtilityPageEntryId, plid, name, type);
	}

	@Reference(
		target = "(model.class.name=com.liferay.layout.utility.page.model.LayoutUtilityPageEntry)"
	)
	private ModelResourcePermission<LayoutUtilityPageEntry>
		_layoutUtilityPageEntryModelResourcePermission;

	@Reference(
		target = "(resource.name=" + LayoutUtilityPageConstants.RESOURCE_NAME + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

}