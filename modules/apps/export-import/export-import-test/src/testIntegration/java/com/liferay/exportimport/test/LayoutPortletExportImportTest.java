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

package com.liferay.exportimport.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.exportimport.kernel.lar.PortletDataHandlerKeys;
import com.liferay.exportimport.test.util.lar.BaseExportImportTestCase;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LinkedHashMapBuilder;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portlet.documentlibrary.constants.DLConstants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Noor Najjar
 */
@RunWith(Arquillian.class)
public class LayoutPortletExportImportTest extends BaseExportImportTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		super.setUp();

		UserTestUtil.setUser(TestPropsValues.getUser());
	}

	@Test
	public void testExportImportLayoutWithGloballyScopedPortletDoesNotOverrideCompanySitePermissions()
		throws Exception {

		long companyId = TestPropsValues.getCompanyId();

		Group companyGroup = _groupLocalService.getCompanyGroup(companyId);

		_addResources(
			companyId, companyGroup.getGroupId(), DLConstants.RESOURCE_NAME);

		_addResources(companyId, group.getGroupId(), DLConstants.RESOURCE_NAME);

		Role ownerRole = _roleLocalService.getRole(
			companyId, RoleConstants.OWNER);

		ResourcePermission originalCompanyGroupResourcePermission =
			_getResourcePermission(
				companyId, companyGroup.getGroupId(), DLConstants.RESOURCE_NAME,
				ownerRole.getRoleId());

		try {
			LayoutTestUtil.addPortletToLayout(
				layout, DLPortletKeys.DOCUMENT_LIBRARY,
				HashMapBuilder.put(
					"lfrScopeType", new String[] {"company"}
				).build());

			ResourcePermission groupResourcePermission = _getResourcePermission(
				companyId, group.getGroupId(), DLConstants.RESOURCE_NAME,
				ownerRole.getRoleId());

			groupResourcePermission.setActionIds(0);
			groupResourcePermission.setViewActionId(false);

			_resourcePermissionLocalService.updateResourcePermission(
				groupResourcePermission);

			exportLayouts(
				new long[] {layout.getLayoutId()},
				LinkedHashMapBuilder.putAll(
					getExportParameterMap()
				).put(
					PortletDataHandlerKeys.PERMISSIONS,
					new String[] {Boolean.TRUE.toString()}
				).build());

			importLayouts(
				LinkedHashMapBuilder.putAll(
					getImportParameterMap()
				).put(
					PortletDataHandlerKeys.PERMISSIONS,
					new String[] {Boolean.TRUE.toString()}
				).build());

			ResourcePermission companyGroupResourcePermission =
				_getResourcePermission(
					companyId, companyGroup.getGroupId(),
					DLConstants.RESOURCE_NAME, ownerRole.getRoleId());

			Assert.assertEquals(
				originalCompanyGroupResourcePermission.getActionIds(),
				companyGroupResourcePermission.getActionIds());
			Assert.assertEquals(
				originalCompanyGroupResourcePermission.isViewActionId(),
				companyGroupResourcePermission.isViewActionId());
		}
		finally {
			ResourcePermission resourcePermission =
				_resourcePermissionLocalService.getResourcePermission(
					originalCompanyGroupResourcePermission.
						getResourcePermissionId());

			resourcePermission.setActionIds(
				originalCompanyGroupResourcePermission.getActionIds());
			resourcePermission.setViewActionId(
				originalCompanyGroupResourcePermission.isViewActionId());

			_resourcePermissionLocalService.updateResourcePermission(
				resourcePermission);
		}
	}

	private void _addResources(long companyId, long groupId, String name)
		throws Exception {

		String primaryKey = String.valueOf(groupId);

		int count = _resourcePermissionLocalService.getResourcePermissionsCount(
			companyId, name, ResourceConstants.SCOPE_INDIVIDUAL, primaryKey);

		if (count > 0) {
			return;
		}

		_resourceLocalService.addResources(
			companyId, groupId, 0, name, primaryKey, false, true, true);
	}

	private ResourcePermission _getResourcePermission(
			long companyId, long groupId, String name, long roleId)
		throws PortalException {

		return _resourcePermissionLocalService.getResourcePermission(
			companyId, name, ResourceConstants.SCOPE_INDIVIDUAL,
			String.valueOf(groupId), roleId);
	}

	@Inject
	private GroupLocalService _groupLocalService;

	@Inject
	private ResourceLocalService _resourceLocalService;

	@Inject
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Inject
	private RoleLocalService _roleLocalService;

}