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

package com.liferay.portal.upgrade.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.service.ReleaseLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portal.util.PropsValues;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Alberto Chaparro
 */
@RunWith(Arquillian.class)
public class AutoUpgradeProcessTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_upgradeDatabaseAutoRunField = ReflectionUtil.getDeclaredField(
			PropsValues.class, "UPGRADE_DATABASE_AUTO_RUN");

		_upgradeDatabaseAutoRunField.setAccessible(true);

		_modifiersField = Field.class.getDeclaredField("modifiers");

		_modifiersField.setAccessible(true);
		_modifiersField.setInt(
			_upgradeDatabaseAutoRunField,
			_upgradeDatabaseAutoRunField.getModifiers() & ~Modifier.FINAL);
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_upgradeDatabaseAutoRunField.setAccessible(false);

		_modifiersField.setAccessible(false);
	}

	@After
	public void tearDown() throws Exception {
		_upgradeDatabaseAutoRunField.set(
			null, _ORIGINAL_UPGRADE_DATABASE_AUTO_RUN);

		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}

		Release release = _releaseLocalService.fetchRelease(
			_SERVLET_CONTEXT_NAME);

		if (release != null) {
			_releaseLocalService.deleteRelease(release);
		}
	}

	@Test
	public void testNewUpgradeProcessWhenAutoUpgradeDisabled()
		throws Exception {

		_setAutoUpgrade(false);

		Assert.assertEquals(
			"1.0.0", _registerNewUpgradeProcess().getSchemaVersion());
	}

	@Test
	public void testNewUpgradeProcessWhenAutoUpgradeEnabled() throws Exception {
		_setAutoUpgrade(true);

		Assert.assertEquals(
			"2.0.0", _registerNewUpgradeProcess().getSchemaVersion());
	}

	private static void _setAutoUpgrade(boolean value) throws Exception {
		_upgradeDatabaseAutoRunField.set(null, value);
	}

	private Release _registerNewUpgradeProcess() throws Exception {
		_releaseLocalService.addRelease(_SERVLET_CONTEXT_NAME, "1.0.0");

		Bundle bundle = FrameworkUtil.getBundle(AutoUpgradeProcessTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		_serviceRegistration = bundleContext.registerService(
			UpgradeStepRegistrator.class, new TestUpgradeStepRegistrator(),
			null);

		return _releaseLocalService.fetchRelease(_SERVLET_CONTEXT_NAME);
	}

	private static final boolean _ORIGINAL_UPGRADE_DATABASE_AUTO_RUN =
		PropsValues.UPGRADE_DATABASE_AUTO_RUN;

	private static final String _SERVLET_CONTEXT_NAME =
		"com.liferay.portal.upgrade.test";

	private static Field _modifiersField;

	@Inject
	private static ReleaseLocalService _releaseLocalService;

	private static Field _upgradeDatabaseAutoRunField;

	private ServiceRegistration<UpgradeStepRegistrator> _serviceRegistration;

	private static class TestUpgradeStepRegistrator
		implements UpgradeStepRegistrator {

		@Override
		public void register(Registry registry) {
			registry.register("1.0.0", "2.0.0", new DummyUpgradeStep());
		}

	}

}