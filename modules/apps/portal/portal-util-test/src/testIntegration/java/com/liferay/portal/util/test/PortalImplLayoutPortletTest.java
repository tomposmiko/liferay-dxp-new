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
import com.liferay.layout.test.util.ContentLayoutTestUtil;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.util.ArrayList;
import java.util.Collection;

import javax.portlet.Portlet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Dante Wang
 * @author Evan Thibodeau
 */
@RunWith(Arquillian.class)
public class PortalImplLayoutPortletTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() {
		Bundle bundle = FrameworkUtil.getBundle(
			PortalImplLayoutPortletTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		_serviceRegistrations.add(
			bundleContext.registerService(
				Portlet.class, new MVCPortlet(),
				HashMapDictionaryBuilder.<String, Object>put(
					"com.liferay.portlet.instanceable", "true"
				).put(
					"com.liferay.portlet.preferences-owned-by-group", "true"
				).put(
					"javax.portlet.name",
					_TEST_PORTLET_NAME_PREF_OWNER_TYPE_LAYOUT
				).build()));

		_serviceRegistrations.add(
			bundleContext.registerService(
				Portlet.class, new MVCPortlet(),
				HashMapDictionaryBuilder.<String, Object>put(
					"com.liferay.portlet.instanceable", "true"
				).put(
					"com.liferay.portlet.preferences-owned-by-group", "true"
				).put(
					"com.liferay.portlet.preferences-unique-per-layout", "false"
				).put(
					"javax.portlet.name",
					_TEST_PORTLET_NAME_PREF_OWNER_TYPE_GROUP
				).build()));
	}

	@After
	public void tearDown() {
		if (_serviceRegistrations != null) {
			_serviceRegistrations.forEach(ServiceRegistration::unregister);
		}
	}

	@Test
	public void testGetPlidFromPortletIdContentLayoutPrefOwnerTypeGroup()
		throws Exception {

		_testGetPlidFromPortletIdContentLayout(
			_TEST_PORTLET_NAME_PREF_OWNER_TYPE_GROUP);
	}

	@Test
	public void testGetPlidFromPortletIdContentLayoutPrefOwnerTypeLayout()
		throws Exception {

		_testGetPlidFromPortletIdContentLayout(
			_TEST_PORTLET_NAME_PREF_OWNER_TYPE_LAYOUT);
	}

	@Test
	public void testGetPlidFromPortletIdPortletLayoutPrefOwnerTypeGroup()
		throws Exception {

		_testGetPlidFromPortletIdPortletLayout(
			_TEST_PORTLET_NAME_PREF_OWNER_TYPE_GROUP);
	}

	@Test
	public void testGetPlidFromPortletIdPortletLayoutPrefOwnerTypeLayout()
		throws Exception {

		_testGetPlidFromPortletIdPortletLayout(
			_TEST_PORTLET_NAME_PREF_OWNER_TYPE_LAYOUT);
	}

	private void _testGetPlidFromPortletIdContentLayout(String portletId)
		throws Exception {

		_group = GroupTestUtil.addGroup();

		Layout layout = LayoutTestUtil.addTypeContentLayout(_group);

		Layout draftLayout = layout.fetchDraftLayout();

		JSONObject layoutDataJSONObject =
			ContentLayoutTestUtil.addPortletToLayout(draftLayout, portletId);

		ContentLayoutTestUtil.publishLayout(draftLayout, layout);

		Assert.assertEquals(
			layout.getPlid(),
			_portal.getPlidFromPortletId(_group.getGroupId(), portletId));

		ContentLayoutTestUtil.markItemForDeletionFromLayout(
			draftLayout, portletId,
			layoutDataJSONObject.getString("addedItemId"));

		ContentLayoutTestUtil.publishLayout(draftLayout, layout);

		Assert.assertNotEquals(
			layout.getPlid(),
			_portal.getPlidFromPortletId(_group.getGroupId(), portletId));
	}

	private void _testGetPlidFromPortletIdPortletLayout(String portletId)
		throws Exception {

		_group = GroupTestUtil.addGroup();

		Layout layout = LayoutTestUtil.addTypePortletLayout(_group);

		LayoutTestUtil.addPortletToLayout(layout, portletId);

		Assert.assertEquals(
			layout.getPlid(),
			_portal.getPlidFromPortletId(_group.getGroupId(), portletId));
	}

	private static final String _TEST_PORTLET_NAME_PREF_OWNER_TYPE_GROUP =
		"com_liferay_test_portlet_TestPortletOwnerTypeGroup";

	private static final String _TEST_PORTLET_NAME_PREF_OWNER_TYPE_LAYOUT =
		"com_liferay_test_portlet_TestPortletOwnerTypeLayout";

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private Portal _portal;

	private final Collection<ServiceRegistration<Portlet>>
		_serviceRegistrations = new ArrayList<>();

}