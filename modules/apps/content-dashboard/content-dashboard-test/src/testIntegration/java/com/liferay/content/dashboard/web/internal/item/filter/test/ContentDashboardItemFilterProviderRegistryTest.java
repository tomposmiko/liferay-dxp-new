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

package com.liferay.content.dashboard.web.internal.item.filter.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.content.dashboard.item.action.exception.ContentDashboardItemActionException;
import com.liferay.content.dashboard.item.filter.ContentDashboardItemFilter;
import com.liferay.content.dashboard.item.filter.provider.ContentDashboardItemFilterProvider;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * @author Cristina González
 */
@RunWith(Arquillian.class)
public class ContentDashboardItemFilterProviderRegistryTest {

	@Before
	public void setUp() throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(
			ContentDashboardItemFilterProviderRegistryTest.class);

		_bundleContext = bundle.getBundleContext();

		_serviceReference = _bundleContext.getServiceReference(
			"com.liferay.content.dashboard.web.internal.item.filter." +
				"ContentDashboardItemFilterProviderRegistry");

		_contentDashboardItemFilterProviderRegistry = _bundleContext.getService(
			_serviceReference);
	}

	@After
	public void tearDown() {
		_bundleContext.ungetService(_serviceReference);
	}

	@Test
	public void testGetContentDashboardItemFilterProviders() {
		_bundleContext.registerService(
			ContentDashboardItemFilterProvider.class,
			new ContentDashboardItemFilterProvider() {

				@Override
				public ContentDashboardItemFilter getContentDashboardItemFilter(
						HttpServletRequest httpServletRequest)
					throws ContentDashboardItemActionException {

					return null;
				}

				@Override
				public String getKey() {
					return "mockContentDashboardItemFilter";
				}

				@Override
				public ContentDashboardItemFilter.Type getType() {
					return ContentDashboardItemFilter.Type.ITEM_SELECTOR;
				}

				@Override
				public boolean isShow(HttpServletRequest httpServletRequest) {
					return false;
				}

			},
			new HashMapDictionary<>());

		boolean found = false;

		List<ContentDashboardItemFilterProvider>
			contentDashboardItemFilterProviders = ReflectionTestUtil.invoke(
				_contentDashboardItemFilterProviderRegistry,
				"getContentDashboardItemFilterProviders", new Class<?>[0]);

		for (ContentDashboardItemFilterProvider
				contentDashboardItemFilterProvider :
					contentDashboardItemFilterProviders) {

			if (Objects.equals(
					contentDashboardItemFilterProvider.getKey(),
					"mockContentDashboardItemFilter")) {

				found = true;

				break;
			}
		}

		Assert.assertTrue(found);
	}

	private BundleContext _bundleContext;
	private Object _contentDashboardItemFilterProviderRegistry;
	private ServiceReference<?> _serviceReference;

}