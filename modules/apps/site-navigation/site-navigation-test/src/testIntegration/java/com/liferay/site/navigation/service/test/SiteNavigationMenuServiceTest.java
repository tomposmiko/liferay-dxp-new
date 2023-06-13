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

package com.liferay.site.navigation.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.site.navigation.constants.SiteNavigationConstants;
import com.liferay.site.navigation.model.SiteNavigationMenu;
import com.liferay.site.navigation.service.SiteNavigationMenuLocalServiceUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Pavel Savinov
 */
@RunWith(Arquillian.class)
public class SiteNavigationMenuServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testUpdateSiteNavigationMenuType() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), TestPropsValues.getUserId());

		SiteNavigationMenu siteNavigationMenu =
			SiteNavigationMenuLocalServiceUtil.addSiteNavigationMenu(
				TestPropsValues.getUserId(), _group.getGroupId(), "Primary",
				SiteNavigationConstants.TYPE_PRIMARY, false, serviceContext);

		SiteNavigationMenuLocalServiceUtil.addSiteNavigationMenu(
			TestPropsValues.getUserId(), _group.getGroupId(), "New Primary",
			SiteNavigationConstants.TYPE_PRIMARY, false, serviceContext);

		siteNavigationMenu =
			SiteNavigationMenuLocalServiceUtil.fetchSiteNavigationMenu(
				siteNavigationMenu.getSiteNavigationMenuId());

		Assert.assertEquals(
			SiteNavigationConstants.TYPE_DEFAULT, siteNavigationMenu.getType());
	}

	@DeleteAfterTestRun
	private Group _group;

}