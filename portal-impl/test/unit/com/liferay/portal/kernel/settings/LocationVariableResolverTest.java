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

package com.liferay.portal.kernel.settings;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * @author Iván Zaera
 */
public class LocationVariableResolverTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_mockResourceManager = new MockResourceManager(
			"En un lugar de la Mancha...");

		_mockSettingsLocatorHelper = Mockito.mock(SettingsLocatorHelper.class);

		_locationVariableResolver = new LocationVariableResolver(
			_mockResourceManager, _mockSettingsLocatorHelper);
	}

	@Test
	public void testIsLocationVariableWithNonvariable() {
		Assert.assertFalse(
			_locationVariableResolver.isLocationVariable(
				"this is obviously not a location variable"));
	}

	@Test
	public void testIsLocationVariableWithProtocol() {
		Assert.assertTrue(
			_locationVariableResolver.isLocationVariable(
				"${resource:location}", LocationVariableProtocol.RESOURCE));
		Assert.assertFalse(
			_locationVariableResolver.isLocationVariable(
				"${resource:location}", LocationVariableProtocol.FILE));
	}

	@Test
	public void testIsLocationVariableWithVariable() {
		Assert.assertTrue(
			_locationVariableResolver.isLocationVariable(
				"${resource:location}"));
	}

	@Test
	public void testIsLocationVariableWithVariableWithInvalidProtocol() {
		Assert.assertFalse(
			_locationVariableResolver.isLocationVariable(
				"${protocol:location}"));
	}

	@Test
	public void testResolveVariableWithFile() throws IOException {
		String expectedValue = "En un lugar de la Mancha...";

		File file = File.createTempFile("testResolveVariableForFile", "txt");

		try (OutputStream outputStream = new FileOutputStream(file)) {
			outputStream.write(expectedValue.getBytes());

			outputStream.flush();

			String value = _locationVariableResolver.resolve(
				"${file://" + file.getAbsolutePath() + "}");

			Assert.assertEquals(expectedValue, value);
		}
		finally {
			file.delete();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testResolveVariableWithInvalidFile() {
		_locationVariableResolver.resolve(
			"${file:bad_file_uri_without_slashes.txt}");
	}

	@Test
	public void testResolveVariableWithLanguage() throws Exception {
		JSONObject expectedValueJSONObject = JSONFactoryUtil.createJSONObject();
		String invalidKey = "invalid-key";
		String validKey = "valid-key";

		LanguageUtil languageUtil = new LanguageUtil();

		Language language = Mockito.mock(Language.class);

		Set<Locale> locales = SetUtil.fromArray(
			LocaleUtil.ENGLISH, LocaleUtil.FRENCH, LocaleUtil.GERMAN);

		Mockito.when(
			language.getCompanyAvailableLocales(Matchers.anyLong())
		).thenReturn(
			locales
		);

		Mockito.when(
			language.get((Locale)Matchers.any(), Matchers.eq(invalidKey))
		).thenReturn(
			invalidKey
		);

		for (Locale locale : locales) {
			String value = validKey + "_" + locale.getLanguage();

			Mockito.when(
				language.get(Matchers.eq(locale), Matchers.eq(validKey))
			).thenReturn(
				value
			);

			expectedValueJSONObject.put(LocaleUtil.toLanguageId(locale), value);
		}

		languageUtil.setLanguage(language);

		String json = _locationVariableResolver.resolve(
			String.format("${language:%s}", validKey));

		Assert.assertTrue(JSONUtil.isValid(json));
		Assert.assertEquals(expectedValueJSONObject.toString(), json);

		json = _locationVariableResolver.resolve(
			String.format("${language:%s}", invalidKey));

		Assert.assertTrue(JSONUtil.isValid(json));
		Assert.assertEquals("{}", json);
	}

	@Test
	public void testResolveVariableWithResource() {
		String value = _locationVariableResolver.resolve(
			"${resource:template.ftl}");

		Assert.assertEquals(_mockResourceManager.getContent(), value);

		List<String> requestedLocations =
			_mockResourceManager.getRequestedLocations();

		Assert.assertEquals(
			requestedLocations.toString(), 1, requestedLocations.size());
		Assert.assertEquals("template.ftl", requestedLocations.get(0));
	}

	@Test
	public void testResolveVariableWithServerProperty() {
		String expectedValue = "test@liferay.com";

		MemorySettings memorySettings = new MemorySettings();

		memorySettings.setValue("admin.email.from.address", expectedValue);

		Mockito.when(
			_mockSettingsLocatorHelper.getServerSettings("com.liferay.portal")
		).thenReturn(
			memorySettings
		);

		Assert.assertEquals(
			expectedValue,
			_locationVariableResolver.resolve(
				"${server-property://com.liferay.portal" +
					"/admin.email.from.address}"));
	}

	private LocationVariableResolver _locationVariableResolver;
	private MockResourceManager _mockResourceManager;
	private SettingsLocatorHelper _mockSettingsLocatorHelper;

}