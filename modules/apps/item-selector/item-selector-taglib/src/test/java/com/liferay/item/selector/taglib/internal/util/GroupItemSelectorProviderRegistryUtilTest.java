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

package com.liferay.item.selector.taglib.internal.util;

import com.liferay.item.selector.provider.GroupItemSelectorProvider;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Cristina González
 */
public class GroupItemSelectorProviderRegistryUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testGetGroupItemSelectorProviderOptionalWithoutRegisteredGroupItemSelectorProvider() {
		Assert.assertNull(
			GroupItemSelectorProviderRegistryUtil.getGroupItemSelectorProvider(
				RandomTestUtil.randomString()));
	}

	@Test
	public void testGetGroupItemSelectorProviderOptionalWithRegisteredGroupItemSelectorProvider() {
		ReflectionTestUtil.setFieldValue(
			GroupItemSelectorProviderRegistryUtil.class, "_serviceTrackerMap",
			new MockServiceTrackerMap(
				Collections.singletonMap(
					"test", new MockGroupItemSelectorProvider())));

		GroupItemSelectorProvider groupItemSelectorProvider =
			GroupItemSelectorProviderRegistryUtil.getGroupItemSelectorProvider(
				"test");

		Assert.assertNotNull(groupItemSelectorProvider);
		Assert.assertEquals("icon", groupItemSelectorProvider.getIcon());

		ReflectionTestUtil.setFieldValue(
			GroupItemSelectorProviderRegistryUtil.class, "_serviceTrackerMap",
			null);
	}

	@Test
	public void testGetGroupItemSelectorProviderOptionalWithRegisteredInactiveGroupItemSelectorProvider() {
		ReflectionTestUtil.setFieldValue(
			GroupItemSelectorProviderRegistryUtil.class, "_serviceTrackerMap",
			new MockServiceTrackerMap(
				Collections.singletonMap(
					"test", new MockGroupItemSelectorProvider(false))));

		Assert.assertNull(
			GroupItemSelectorProviderRegistryUtil.getGroupItemSelectorProvider(
				"test"));
		Assert.assertTrue(
			SetUtil.isEmpty(
				GroupItemSelectorProviderRegistryUtil.
					getGroupItemSelectorProviderTypes()));

		ReflectionTestUtil.setFieldValue(
			GroupItemSelectorProviderRegistryUtil.class, "_serviceTrackerMap",
			null);
	}

	@Test
	public void testGetGroupItemSelectorProviderTypesWithoutRegisteredGroupItemSelectorProvider() {
		Assert.assertEquals(
			Collections.emptySet(),
			GroupItemSelectorProviderRegistryUtil.
				getGroupItemSelectorProviderTypes());
	}

	@Test
	public void testGetGroupItemSelectorProviderTypesWithRegisteredGroupItemSelectorProvider() {
		ReflectionTestUtil.setFieldValue(
			GroupItemSelectorProviderRegistryUtil.class, "_serviceTrackerMap",
			new MockServiceTrackerMap(
				Collections.singletonMap(
					"test", new MockGroupItemSelectorProvider())));

		Assert.assertEquals(
			Collections.singleton("test"),
			GroupItemSelectorProviderRegistryUtil.
				getGroupItemSelectorProviderTypes());

		ReflectionTestUtil.setFieldValue(
			GroupItemSelectorProviderRegistryUtil.class, "_serviceTrackerMap",
			null);
	}

	private static class MockGroupItemSelectorProvider
		implements GroupItemSelectorProvider {

		public MockGroupItemSelectorProvider() {
			this(true);
		}

		public MockGroupItemSelectorProvider(boolean active) {
			_active = active;
		}

		@Override
		public String getEmptyResultsMessage() {
			return null;
		}

		@Override
		public List<Group> getGroups(
			long companyId, long groupId, String keywords, int start, int end) {

			return Collections.singletonList(Mockito.mock(Group.class));
		}

		@Override
		public int getGroupsCount(
			long companyId, long groupId, String keywords) {

			return 3;
		}

		@Override
		public String getGroupType() {
			return "test";
		}

		@Override
		public String getIcon() {
			return "icon";
		}

		@Override
		public String getLabel(Locale locale) {
			return RandomTestUtil.randomString();
		}

		@Override
		public boolean isEnabled() {
			return _active;
		}

		private boolean _active;

	}

	private static class MockServiceTrackerMap
		implements ServiceTrackerMap<String, GroupItemSelectorProvider> {

		public MockServiceTrackerMap(
			Map<String, GroupItemSelectorProvider>
				groupItemSelectorProviderMap) {

			_groupItemSelectorProviderMap = groupItemSelectorProviderMap;
		}

		@Override
		public void close() {
		}

		@Override
		public boolean containsKey(String key) {
			return _groupItemSelectorProviderMap.containsKey(key);
		}

		@Override
		public GroupItemSelectorProvider getService(String key) {
			return _groupItemSelectorProviderMap.get(key);
		}

		@Override
		public Set<String> keySet() {
			return _groupItemSelectorProviderMap.keySet();
		}

		@Override
		public Collection<GroupItemSelectorProvider> values() {
			return _groupItemSelectorProviderMap.values();
		}

		private final Map<String, GroupItemSelectorProvider>
			_groupItemSelectorProviderMap;

	}

}