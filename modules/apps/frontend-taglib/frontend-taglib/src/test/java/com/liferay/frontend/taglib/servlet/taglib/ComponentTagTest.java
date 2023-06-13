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

package com.liferay.frontend.taglib.servlet.taglib;

import com.liferay.frontend.js.loader.modules.extender.npm.NPMResolvedPackageNameUtil;
import com.liferay.frontend.js.web.internal.servlet.taglib.aui.PortletDataRendererImpl;
import com.liferay.frontend.taglib.internal.util.ServicesProvider;
import com.liferay.petra.lang.ClassLoaderPool;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletConfig;
import com.liferay.portal.kernel.servlet.taglib.aui.ScriptData;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.util.PortalImpl;
import com.liferay.portal.uuid.PortalUUIDImpl;

import java.io.StringWriter;

import java.lang.reflect.Field;

import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPageContext;

/**
 * @author Cristina González
 */
public class ComponentTagTest {

	@ClassRule
	public static LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		ClassLoaderPool.register(
			"ShieldedContainerClassLoader", PortalImpl.class.getClassLoader());

		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(new PortalImpl());

		PortalUUIDUtil portalUUIDUtil = new PortalUUIDUtil();

		portalUUIDUtil.setPortalUUID(new PortalUUIDImpl());

		Field portletDataRendererField = ReflectionUtil.getDeclaredField(
			ScriptData.class, "_portletDataRenderer");

		portletDataRendererField.set(null, new PortletDataRendererImpl());

		BundleContext bundleContext = SystemBundleUtil.getBundleContext();

		Mockito.when(
			FrameworkUtil.getBundle(Mockito.any())
		).thenReturn(
			bundleContext.getBundle()
		);
	}

	@After
	public void tearDown() {
		_servicesProviderMockedStatic.close();
		_frameworkUtilMockedStatic.close();
	}

	@Test
	public void testDoEndTag() throws Exception {
		ComponentTag componentTag = new ComponentTag();

		HttpServletRequest httpServletRequest = _getHttpServletRequest();

		componentTag.setComponentId("componentId");
		componentTag.setContext(Collections.singletonMap("name", "value"));
		componentTag.setModule("module");
		componentTag.setPageContext(
			new MockPageContext(_getServletContext(), httpServletRequest));

		componentTag.doEndTag();

		Assert.assertEquals(
			_read("dependencies/component_tag.html"),
			_toString(
				(ScriptData)httpServletRequest.getAttribute(
					WebKeys.AUI_SCRIPT_DATA)));
	}

	private HttpServletRequest _getHttpServletRequest() {
		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setPathThemeSpritemap("/clay/icons.svg");

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		portletDisplay.setNamespace("namespace");

		LiferayPortletConfig liferayPortletConfig = Mockito.mock(
			LiferayPortletConfig.class);

		Mockito.when(
			liferayPortletConfig.getPortletId()
		).thenReturn(
			"portletId"
		);

		mockHttpServletRequest.setAttribute(
			JavaConstants.JAVAX_PORTLET_CONFIG, liferayPortletConfig);

		mockHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, themeDisplay);

		return mockHttpServletRequest;
	}

	private ServletContext _getServletContext() {
		ServletContext servletContext = Mockito.mock(ServletContext.class);

		servletContext.setAttribute(
			NPMResolvedPackageNameUtil.class.getName(),
			"NPMResolvedPackageNameUtil");

		return servletContext;
	}

	private String _read(String fileName) throws Exception {
		Class<?> clazz = getClass();

		URL url = clazz.getResource(fileName);

		return new String(Files.readAllBytes(Paths.get(url.toURI())));
	}

	private String _toString(ScriptData scriptData) throws Exception {
		StringWriter stringWriter = new StringWriter();

		scriptData.writeTo(stringWriter);

		StringBuffer stringBuffer = stringWriter.getBuffer();

		return stringBuffer.toString();
	}

	private static final MockedStatic<FrameworkUtil>
		_frameworkUtilMockedStatic = Mockito.mockStatic(FrameworkUtil.class);

	private final MockedStatic<ServicesProvider> _servicesProviderMockedStatic =
		Mockito.mockStatic(ServicesProvider.class);

}