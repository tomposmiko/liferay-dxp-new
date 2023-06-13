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

package com.liferay.segments.configuration.provider.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.test.util.CompanyConfigurationTemporarySwapper;
import com.liferay.portal.configuration.test.util.ConfigurationTemporarySwapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.settings.SettingsFactoryUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.segments.configuration.SegmentsCompanyConfiguration;
import com.liferay.segments.configuration.SegmentsConfiguration;
import com.liferay.segments.configuration.provider.SegmentsConfigurationProvider;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Cristina González
 */
@RunWith(Arquillian.class)
public class SegmentsConfigurationProviderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testGetConfigurationURL() throws Exception {
		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(
			_companyLocalService.fetchCompany(TestPropsValues.getCompanyId()));
		themeDisplay.setSiteGroupId(TestPropsValues.getGroupId());
		themeDisplay.setUser(TestPropsValues.getUser());

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, themeDisplay);

		mockHttpServletRequest.setAttribute(
			WebKeys.USER_ID, TestPropsValues.getUserId());

		String configurationURL =
			_segmentsConfigurationProvider.getConfigurationURL(
				mockHttpServletRequest);

		Assert.assertTrue(
			configurationURL.contains(
				"factoryPid=com.liferay.segments.configuration." +
					"SegmentsCompanyConfiguration"));
	}

	@Test
	public void testGetConfigurationURLWithoutPermission() throws Exception {
		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(
			_companyLocalService.fetchCompany(TestPropsValues.getCompanyId()));

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, themeDisplay);

		User user = UserTestUtil.addUser(TestPropsValues.getGroupId());

		mockHttpServletRequest.setAttribute(WebKeys.USER_ID, user.getUserId());

		Assert.assertNull(
			_segmentsConfigurationProvider.getConfigurationURL(
				mockHttpServletRequest));

		_userLocalService.deleteUser(user);
	}

	@Test
	public void testIsRoleSegmentationEnabled() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"roleSegmentationEnabled", true
					).build())) {

			Assert.assertTrue(
				_segmentsConfigurationProvider.isRoleSegmentationEnabled());
		}
	}

	@Test
	public void testIsRoleSegmentationEnabledWithCompanyId() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"roleSegmentationEnabled", true
					).build())) {

			Assert.assertTrue(
				_segmentsConfigurationProvider.isRoleSegmentationEnabled(
					TestPropsValues.getCompanyId()));
		}
	}

	@Test
	public void testIsRoleSegmentationEnabledWithCompanyIdAndCompanyConfigurationDisabled()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"roleSegmentationEnabled", true
					).build())) {

			Configuration configuration =
				_configurationAdmin.createFactoryConfiguration(
					SegmentsCompanyConfiguration.class.getName() + ".scoped",
					StringPool.QUESTION);

			configuration.update(
				HashMapDictionaryBuilder.<String, Object>put(
					"companyId", TestPropsValues.getCompanyId()
				).put(
					"roleSegmentationEnabled", false
				).build());

			try (CompanyConfigurationTemporarySwapper
					companyConfigurationTemporarySwapper =
						new CompanyConfigurationTemporarySwapper(
							TestPropsValues.getCompanyId(),
							SegmentsCompanyConfiguration.class.getName(),
							HashMapDictionaryBuilder.<String, Object>put(
								"roleSegmentationEnabled", false
							).build(),
							SettingsFactoryUtil.getSettingsFactory())) {

				Assert.assertFalse(
					_segmentsConfigurationProvider.isRoleSegmentationEnabled(
						TestPropsValues.getCompanyId()));
			}
			finally {
				configuration.delete();
			}
		}
	}

	@Test
	public void testIsRoleSegmentationEnabledWithCompanyIdAndCompanyConfigurationEnabled()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"roleSegmentationEnabled", true
					).build())) {

			try (CompanyConfigurationTemporarySwapper
					companyConfigurationTemporarySwapper =
						new CompanyConfigurationTemporarySwapper(
							TestPropsValues.getCompanyId(),
							SegmentsCompanyConfiguration.class.getName(),
							HashMapDictionaryBuilder.<String, Object>put(
								"roleSegmentationEnabled", true
							).build(),
							SettingsFactoryUtil.getSettingsFactory())) {

				Assert.assertTrue(
					_segmentsConfigurationProvider.isRoleSegmentationEnabled(
						TestPropsValues.getCompanyId()));
			}
		}
	}

	@Test
	public void testIsRoleSegmentationEnabledWithCompanyIdAndRoleSegmentationDisabled()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"roleSegmentationEnabled", false
					).build())) {

			Assert.assertFalse(
				_segmentsConfigurationProvider.isRoleSegmentationEnabled(
					TestPropsValues.getCompanyId()));
		}
	}

	@Test
	public void testIsRoleSegmentationEnabledWithConfigurationDisabled()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"roleSegmentationEnabled", false
					).build())) {

			Assert.assertFalse(
				_segmentsConfigurationProvider.isRoleSegmentationEnabled());
		}
	}

	@Test
	public void testIsSegmentationEnabled() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"segmentationEnabled", true
					).build())) {

			Assert.assertTrue(
				_segmentsConfigurationProvider.isSegmentationEnabled());
		}
	}

	@Test
	public void testIsSegmentationEnabledWithCompanyId() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"segmentationEnabled", true
					).build())) {

			try (CompanyConfigurationTemporarySwapper
					companyConfigurationTemporarySwapper =
						new CompanyConfigurationTemporarySwapper(
							TestPropsValues.getCompanyId(),
							SegmentsCompanyConfiguration.class.getName(),
							HashMapDictionaryBuilder.<String, Object>put(
								"segmentationEnabled", true
							).build(),
							SettingsFactoryUtil.getSettingsFactory())) {

				Assert.assertTrue(
					_segmentsConfigurationProvider.isSegmentationEnabled(
						TestPropsValues.getCompanyId()));
			}
		}
	}

	@Test
	public void testIsSegmentationEnabledWithCompanyIdAndCompanyConfigurationDisabled()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"segmentationEnabled", true
					).build())) {

			Configuration configuration =
				_configurationAdmin.createFactoryConfiguration(
					SegmentsCompanyConfiguration.class.getName() + ".scoped",
					StringPool.QUESTION);

			configuration.update(
				HashMapDictionaryBuilder.<String, Object>put(
					"companyId", TestPropsValues.getCompanyId()
				).put(
					"roleSegmentationEnabled", false
				).build());

			try (CompanyConfigurationTemporarySwapper
					companyConfigurationTemporarySwapper =
						new CompanyConfigurationTemporarySwapper(
							TestPropsValues.getCompanyId(),
							SegmentsCompanyConfiguration.class.getName(),
							HashMapDictionaryBuilder.<String, Object>put(
								"segmentationEnabled", false
							).build(),
							SettingsFactoryUtil.getSettingsFactory())) {

				Assert.assertFalse(
					_segmentsConfigurationProvider.isSegmentationEnabled(
						TestPropsValues.getCompanyId()));
			}
			finally {
				configuration.delete();
			}
		}
	}

	@Test
	public void testIsSegmentationEnabledWithCompanyIdAndCompanyConfigurationEnabled()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"segmentationEnabled", true
					).build())) {

			try (CompanyConfigurationTemporarySwapper
					companyConfigurationTemporarySwapper =
						new CompanyConfigurationTemporarySwapper(
							TestPropsValues.getCompanyId(),
							SegmentsCompanyConfiguration.class.getName(),
							HashMapDictionaryBuilder.<String, Object>put(
								"segmentationEnabled", true
							).build(),
							SettingsFactoryUtil.getSettingsFactory())) {

				Assert.assertTrue(
					_segmentsConfigurationProvider.isSegmentationEnabled(
						TestPropsValues.getCompanyId()));
			}
		}
	}

	@Test
	public void testIsSegmentationEnabledWithCompanyIdAndSegmentationDisabled()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"segmentationEnabled", false
					).build())) {

			Assert.assertFalse(
				_segmentsConfigurationProvider.isSegmentationEnabled(
					TestPropsValues.getCompanyId()));
		}
	}

	@Test
	public void testIsSegmentationEnabledWithConfigurationDisabled()
		throws Exception {

		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"segmentationEnabled", false
					).build())) {

			Assert.assertFalse(
				_segmentsConfigurationProvider.isSegmentationEnabled());
		}
	}

	@Test
	public void testIsSegmentsCompanyConfigurationDefined() throws Exception {
		Configuration configuration =
			_configurationAdmin.createFactoryConfiguration(
				SegmentsCompanyConfiguration.class.getName() + ".scoped",
				StringPool.QUESTION);

		try {
			configuration.update(
				HashMapDictionaryBuilder.<String, Object>put(
					"companyId", TestPropsValues.getCompanyId()
				).put(
					"roleSegmentationEnabled", false
				).build());

			Assert.assertTrue(
				_segmentsConfigurationProvider.
					isSegmentsCompanyConfigurationDefined(
						TestPropsValues.getCompanyId()));
		}
		finally {
			configuration.delete();
		}
	}

	@Test
	public void testResetSegmentsCompanyConfiguration() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"segmentationEnabled", false
					).build())) {

			Configuration configuration =
				_configurationAdmin.createFactoryConfiguration(
					SegmentsCompanyConfiguration.class.getName() + ".scoped",
					StringPool.QUESTION);

			configuration.update(
				HashMapDictionaryBuilder.<String, Object>put(
					"companyId", TestPropsValues.getCompanyId()
				).put(
					"segmentationEnabled", true
				).build());

			_segmentsConfigurationProvider.resetSegmentsCompanyConfiguration(
				TestPropsValues.getCompanyId());

			Assert.assertFalse(
				_segmentsConfigurationProvider.
					isSegmentsCompanyConfigurationDefined(
						TestPropsValues.getCompanyId()));
		}
	}

	@Test
	public void testUpdateSegmentsCompanyConfiguration() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					SegmentsConfiguration.class.getName(),
					HashMapDictionaryBuilder.<String, Object>put(
						"roleSegmentationEnabled", true
					).put(
						"segmentationEnabled", false
					).build())) {

			_segmentsConfigurationProvider.updateSegmentsCompanyConfiguration(
				TestPropsValues.getCompanyId(),
				new SegmentsCompanyConfiguration() {

					@Override
					public boolean roleSegmentationEnabled() {
						return true;
					}

					@Override
					public boolean segmentationEnabled() {
						return false;
					}

				});

			Assert.assertTrue(
				_segmentsConfigurationProvider.isRoleSegmentationEnabled());
			Assert.assertFalse(
				_segmentsConfigurationProvider.isSegmentationEnabled());
		}
	}

	@Inject
	private CompanyLocalService _companyLocalService;

	@Inject
	private ConfigurationAdmin _configurationAdmin;

	@Inject
	private SegmentsConfigurationProvider _segmentsConfigurationProvider;

	@Inject
	private UserLocalService _userLocalService;

}