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

package com.liferay.portal.service.permission;

import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.RoleWrapper;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceWrapper;
import com.liferay.portal.kernel.service.permission.ModelPermissions;
import com.liferay.portal.kernel.service.permission.ModelPermissionsFactory;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ListUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Jorge Ferrer
 */
public class ModelPermissionsFactoryTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		ReflectionTestUtil.setFieldValue(
			RoleLocalServiceUtil.class, "_service",
			new RoleLocalServiceWrapper(null) {

				@Override
				public Role getDefaultGroupRole(long groupId) {
					return new RoleWrapper(null) {

						@Override
						public String getName() {
							return RoleConstants.SITE_MEMBER;
						}

					};
				}

				@Override
				public Role getRole(long companyId, String name) {
					return new RoleWrapper(null) {

						@Override
						public String getName() {
							return name;
						}

					};
				}

			});
	}

	@Test
	public void testCreateWithEmptyPermissions() throws Exception {
		String[] groupPermissions = {};
		String[] guestPermissions = {};

		ModelPermissions modelPermissions = ModelPermissionsFactory.create(
			groupPermissions, guestPermissions);

		Collection<String> roleNames = modelPermissions.getRoleNames();

		Assert.assertEquals(roleNames.toString(), 0, roleNames.size());
	}

	@Test
	public void testCreateWithGroupPermissions() throws Exception {
		String[] groupPermissions = {ActionKeys.VIEW};
		String[] guestPermissions = {};

		ModelPermissions modelPermissions = ModelPermissionsFactory.create(
			groupPermissions, guestPermissions);

		Collection<String> roleNames = modelPermissions.getRoleNames();

		Assert.assertEquals(roleNames.toString(), 1, roleNames.size());

		String roleName = roleNames.iterator().next();

		Assert.assertEquals(
			RoleConstants.PLACEHOLDER_DEFAULT_GROUP_ROLE, roleName);
		Assert.assertEquals(
			ListUtil.fromArray(groupPermissions),
			modelPermissions.getActionIdsList(roleName));
	}

	@Test
	public void testCreateWithGuestAndGroupPermissions() throws Exception {
		String[] groupPermissions = {ActionKeys.VIEW};
		String[] guestPermissions = {ActionKeys.VIEW};

		ModelPermissions modelPermissions = ModelPermissionsFactory.create(
			groupPermissions, guestPermissions);

		Collection<String> roleNames = modelPermissions.getRoleNames();

		Assert.assertEquals(roleNames.toString(), 2, roleNames.size());

		Collection<String> viewActionIdRoleNames =
			modelPermissions.getRoleNames(ActionKeys.VIEW);

		Assert.assertEquals(
			viewActionIdRoleNames.toString(), 2, viewActionIdRoleNames.size());
	}

	@Test
	public void testCreateWithGuestPermissions() throws Exception {
		String[] groupPermissions = {};
		String[] guestPermissions = {ActionKeys.VIEW};

		ModelPermissions modelPermissions = ModelPermissionsFactory.create(
			groupPermissions, guestPermissions);

		Collection<String> roleNames = modelPermissions.getRoleNames();

		Assert.assertEquals(roleNames.toString(), 1, roleNames.size());

		String roleName = roleNames.iterator().next();

		Assert.assertEquals(RoleConstants.GUEST, roleName);
		Assert.assertArrayEquals(
			guestPermissions, modelPermissions.getActionIds(roleName));
	}

	@Test
	public void testCreateWithNullPermissions() throws Exception {
		String[] groupPermissions = null;
		String[] guestPermissions = null;

		ModelPermissions modelPermissions = ModelPermissionsFactory.create(
			groupPermissions, guestPermissions);

		Collection<String> roleNames = modelPermissions.getRoleNames();

		Assert.assertTrue(roleNames.toString(), roleNames.isEmpty());
	}

	@Test
	public void testCreateWithoutParameters() throws Exception {
		Map<String, String[]> parameterMap = new HashMap<>();

		ModelPermissions modelPermissions = ModelPermissionsFactory.create(
			parameterMap);

		Collection<String> roleNames = modelPermissions.getRoleNames();

		Assert.assertEquals(roleNames.toString(), 0, roleNames.size());
	}

	@Test
	public void testCreateWithParameterForOneRole() throws Exception {
		Map<String, String[]> parameterMap = new HashMap<>();

		String[] permissions = {ActionKeys.VIEW};

		parameterMap.put(
			ModelPermissionsFactory.MODEL_PERMISSIONS_PREFIX +
				RoleConstants.GUEST,
			permissions);

		ModelPermissions modelPermissions = ModelPermissionsFactory.create(
			parameterMap);

		Collection<String> roleNames = modelPermissions.getRoleNames();

		Assert.assertEquals(roleNames.toString(), 1, roleNames.size());

		String roleName = roleNames.iterator().next();

		Assert.assertEquals(RoleConstants.GUEST, roleName);
		Assert.assertArrayEquals(
			permissions, modelPermissions.getActionIds(roleName));
	}

	@Test
	public void testCreateWithParameterForTwoRoles() throws Exception {
		Map<String, String[]> parameterMap = new HashMap<>();

		String[] permissions = {ActionKeys.VIEW};

		parameterMap.put(
			ModelPermissionsFactory.MODEL_PERMISSIONS_PREFIX +
				RoleConstants.GUEST,
			permissions);
		parameterMap.put(
			ModelPermissionsFactory.MODEL_PERMISSIONS_PREFIX +
				RoleConstants.SITE_MEMBER,
			permissions);

		ModelPermissions modelPermissions = ModelPermissionsFactory.create(
			parameterMap);

		Collection<String> roleNames = modelPermissions.getRoleNames();

		Assert.assertEquals(roleNames.toString(), 2, roleNames.size());

		String roleName = roleNames.iterator().next();

		Assert.assertArrayEquals(
			permissions, modelPermissions.getActionIds(roleName));
	}

}