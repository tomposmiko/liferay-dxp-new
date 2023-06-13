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

package com.liferay.portal.util.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.TreeMapBuilder;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PropsValues;

import java.util.Map;
import java.util.TreeMap;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Christopher Kian
 */
@RunWith(Arquillian.class)
public class PortalImplGroupFriendlyURLTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_company = CompanyTestUtil.addCompany();

		_group = _groupLocalService.fetchGroup(
			_company.getCompanyId(),
			PropsValues.VIRTUAL_HOSTS_DEFAULT_SITE_NAME);

		if (_group == null) {
			User user = UserTestUtil.getAdminUser(_company.getCompanyId());

			_group = GroupTestUtil.addGroup(
				_company.getCompanyId(), user.getUserId(), 0);

			_group.setGroupKey(PropsValues.VIRTUAL_HOSTS_DEFAULT_SITE_NAME);

			_groupLocalService.updateGroup(_group);
		}

		LayoutTestUtil.addLayout(_group, true);

		_privateLayout = LayoutTestUtil.addLayout(_group.getGroupId(), true);

		_updateLayoutSetVirtualHostname(
			_privateLayout, _PRIVATE_LAYOUT_HOSTNAME);

		_publicLayout = LayoutTestUtil.addLayout(_group.getGroupId(), false);

		_updateLayoutSetVirtualHostname(_publicLayout, _PUBLIC_LAYOUT_HOSTNAME);
	}

	@AfterClass
	public static void tearDownClass() throws PortalException {
		_companyLocalService.deleteCompany(_company);
	}

	@Test
	public void testGetGroupFriendlyURLFromPrivateLayout() throws Exception {

		// Tests for LPS-70980

		String expectedURL =
			PropsValues.LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING +
				_group.getFriendlyURL();

		_testGroupFriendlyURL(
			_PRIVATE_LAYOUT_HOSTNAME, expectedURL, _group, _privateLayout);
	}

	@Test
	public void testGetGroupFriendlyURLFromPublicLayout() throws Exception {
		String expectedURL = StringPool.BLANK;

		_testGroupFriendlyURL(
			_PUBLIC_LAYOUT_HOSTNAME, expectedURL, _group, _publicLayout);
	}

	@Test
	public void testGetGroupFriendlyURLFromPublicLayoutLocalhost()
		throws Exception {

		_testGroupFriendlyURL(
			"localhost", StringPool.BLANK, _group, _publicLayout);

		User user = UserTestUtil.getAdminUser(_company.getCompanyId());

		Group group = GroupTestUtil.addGroup(
			_company.getCompanyId(), user.getUserId(), 0);

		Layout layout = LayoutTestUtil.addLayout(group);

		_testGroupFriendlyURL(
			"localhost",
			PropsValues.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING +
				group.getFriendlyURL(),
			group, layout);
	}

	private static void _updateLayoutSetVirtualHostname(
		Layout layout, String layoutHostname) {

		LayoutSet layoutSet = layout.getLayoutSet();

		Map<String, String> virtualHostnames = TreeMapBuilder.put(
			layoutHostname, StringPool.BLANK
		).build();

		layoutSet.setVirtualHostnames(
			(TreeMap<String, String>)virtualHostnames);

		layout.setLayoutSet(layoutSet);
	}

	private void _testGroupFriendlyURL(
			String virtualHostname, String expectedURL, Group group,
			Layout layout)
		throws Exception {

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(_company);
		themeDisplay.setLayout(layout);
		themeDisplay.setLayoutSet(layout.getLayoutSet());
		themeDisplay.setPortalDomain(virtualHostname);
		themeDisplay.setServerName(virtualHostname);
		themeDisplay.setSiteGroupId(group.getGroupId());
		themeDisplay.setUser(TestPropsValues.getUser());

		Assert.assertEquals(
			expectedURL,
			_portal.getGroupFriendlyURL(
				layout.getLayoutSet(), themeDisplay, false, true));
	}

	private static final String _PRIVATE_LAYOUT_HOSTNAME =
		"privateLayoutHostname";

	private static final String _PUBLIC_LAYOUT_HOSTNAME =
		"publicLayoutHostname";

	private static Company _company;

	@Inject
	private static CompanyLocalService _companyLocalService;

	private static Group _group;

	@Inject
	private static GroupLocalService _groupLocalService;

	private static Layout _privateLayout;
	private static Layout _publicLayout;

	@Inject
	private Portal _portal;

}