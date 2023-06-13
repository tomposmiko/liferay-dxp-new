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

package com.liferay.dynamic.data.mapping.internal.form.renderer.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormTemplateContextFactory;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Marcellus Tavares
 */
@RunWith(Arquillian.class)
public class DDMFormTemplateContextFactoryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_httpServletRequest = new MockHttpServletRequest();

		setUpThemeDisplay();

		_originalSiteDefaultLocale = LocaleThreadLocal.getSiteDefaultLocale();
		_originalThemeDisplayDefaultLocale =
			LocaleThreadLocal.getThemeDisplayLocale();

		LocaleThreadLocal.setSiteDefaultLocale(LocaleUtil.US);
		LocaleThreadLocal.setThemeDisplayLocale(LocaleUtil.US);
	}

	@After
	public void tearDown() {
		LocaleThreadLocal.setSiteDefaultLocale(_originalSiteDefaultLocale);
		LocaleThreadLocal.setThemeDisplayLocale(
			_originalThemeDisplayDefaultLocale);
	}

	@Test
	public void testContainerId() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		String containerId = StringUtil.randomString();

		ddmFormRenderingContext.setContainerId(containerId);

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		Assert.assertEquals(
			containerId, ddmFormTemplateContext.get("containerId"));
	}

	@Test
	public void testContainerIdGeneration() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		Assert.assertNotNull(ddmFormTemplateContext.get("containerId"));
	}

	@Test
	public void testEvaluatorURL() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		Assert.assertEquals(
			"/o/dynamic-data-mapping-form-context-provider/",
			ddmFormTemplateContext.get("evaluatorURL"));
	}

	@Test
	public void testPortletNamespace() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);
		ddmFormRenderingContext.setPortletNamespace("_PORTLET_NAMESPACE_");

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		Assert.assertEquals(
			"_PORTLET_NAMESPACE_",
			ddmFormTemplateContext.get("portletNamespace"));
	}

	@Test
	public void testReadOnly() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);
		ddmFormRenderingContext.setReadOnly(true);

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		Assert.assertTrue((boolean)ddmFormTemplateContext.get("readOnly"));
	}

	@Test
	public void testShowRequiredFieldsWarning() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);
		ddmFormRenderingContext.setShowRequiredFieldsWarning(false);

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		Assert.assertFalse(
			(boolean)ddmFormTemplateContext.get("showRequiredFieldsWarning"));
	}

	@Test
	public void testShowSubmitButton() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);
		ddmFormRenderingContext.setShowSubmitButton(true);

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		Assert.assertTrue(
			(boolean)ddmFormTemplateContext.get("showSubmitButton"));
	}

	@Test
	public void testShowSubmitButtonAndReadOnlyEnabled() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);
		ddmFormRenderingContext.setShowSubmitButton(true);
		ddmFormRenderingContext.setReadOnly(true);

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		Assert.assertFalse(
			(boolean)ddmFormTemplateContext.get("showSubmitButton"));
	}

	@Test
	public void testStrings() throws Exception {
		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				DDMFormTestUtil.createDDMForm(), ddmFormRenderingContext);

		Assert.assertEquals(
			HashMapBuilder.put(
				"next", "Next"
			).put(
				"previous", "Previous"
			).build(),
			ddmFormTemplateContext.get("strings"));
	}

	@Test
	public void testSubmitLabel() throws Exception {
		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		String submitLabel = StringUtil.randomString();

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);
		ddmFormRenderingContext.setSubmitLabel(submitLabel);

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				DDMFormTestUtil.createDDMForm(), ddmFormRenderingContext);

		Assert.assertEquals(
			submitLabel, ddmFormTemplateContext.get("submitLabel"));

		ddmFormRenderingContext = new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);

		ddmFormTemplateContext = _ddmFormTemplateContextFactory.create(
			DDMFormTestUtil.createDDMForm(), ddmFormRenderingContext);

		Assert.assertEquals(
			"Submit", ddmFormTemplateContext.get("submitLabel"));
	}

	@Test
	public void testTemplateNamespace() throws Exception {

		// Settings form

		DDMFormLayout ddmFormLayout = new DDMFormLayout();

		ddmFormLayout.setPaginationMode(DDMFormLayout.SETTINGS_MODE);

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				DDMFormTestUtil.createDDMForm(), ddmFormLayout,
				ddmFormRenderingContext);

		Assert.assertEquals(
			"ddm.settings_form",
			ddmFormTemplateContext.get("templateNamespace"));

		// Simple form

		ddmFormTemplateContext = _ddmFormTemplateContextFactory.create(
			DDMFormTestUtil.createDDMForm(), ddmFormRenderingContext);

		Assert.assertEquals(
			"ddm.simple_form", ddmFormTemplateContext.get("templateNamespace"));

		// Tabbed form

		ddmFormLayout.setPaginationMode(DDMFormLayout.TABBED_MODE);

		ddmFormTemplateContext = _ddmFormTemplateContextFactory.create(
			DDMFormTestUtil.createDDMForm(), ddmFormLayout,
			ddmFormRenderingContext);

		Assert.assertEquals(
			"ddm.tabbed_form", ddmFormTemplateContext.get("templateNamespace"));

		// Paginated form

		ddmFormLayout.setPaginationMode(StringPool.BLANK);

		ddmFormTemplateContext = _ddmFormTemplateContextFactory.create(
			DDMFormTestUtil.createDDMForm(), ddmFormLayout,
			ddmFormRenderingContext);

		Assert.assertEquals(
			"ddm.paginated_form",
			ddmFormTemplateContext.get("templateNamespace"));

		// Wizard form

		ddmFormLayout.setPaginationMode(DDMFormLayout.WIZARD_MODE);

		ddmFormTemplateContext = _ddmFormTemplateContextFactory.create(
			DDMFormTestUtil.createDDMForm(), ddmFormLayout,
			ddmFormRenderingContext);

		Assert.assertEquals(
			"ddm.wizard_form", ddmFormTemplateContext.get("templateNamespace"));
	}

	@Test
	public void testViewMode() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setLocale(LocaleUtil.US);
		ddmFormRenderingContext.setHttpServletRequest(_httpServletRequest);
		ddmFormRenderingContext.setViewMode(true);

		Map<String, Object> ddmFormTemplateContext =
			_ddmFormTemplateContextFactory.create(
				ddmForm, ddmFormRenderingContext);

		Assert.assertTrue((boolean)ddmFormTemplateContext.get("viewMode"));
	}

	protected void setUpThemeDisplay() {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setPathContext("/my/path/context/");
		themeDisplay.setPathThemeImages("/my/theme/images/");

		_httpServletRequest.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);
	}

	@Inject
	private static DDMFormTemplateContextFactory _ddmFormTemplateContextFactory;

	private HttpServletRequest _httpServletRequest;
	private Locale _originalSiteDefaultLocale;
	private Locale _originalThemeDisplayDefaultLocale;

}