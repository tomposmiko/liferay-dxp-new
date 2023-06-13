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

package com.liferay.portal.servlet;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletApp;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.language.LanguageImpl;
import com.liferay.portal.tools.ToolDependencies;
import com.liferay.portal.util.FileImpl;
import com.liferay.portal.util.HttpImpl;
import com.liferay.portal.util.PortalImpl;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsImpl;

import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

/**
 * @author Carlos Sierra Andrés
 * @author Raymond Augé
 */
@PowerMockIgnore("javax.net.ssl.*")
@PrepareForTest({PortletLocalServiceUtil.class, PrefsPropsUtil.class})
@RunWith(PowerMockRunner.class)
public class ComboServletTest extends PowerMockito {

	@BeforeClass
	public static void setUpClass() throws Exception {
		ToolDependencies.wireCaches();

		_http = HttpUtil.getHttp();

		_httpUtil.setHttp(new HttpImpl());

		_portal = PortalUtil.getPortal();

		_portalUtil.setPortal(new PortalImpl());
	}

	@AfterClass
	public static void tearDownClass() {
		_httpUtil.setHttp(_http);
		_portalUtil.setPortal(_portal);
	}

	@Before
	public void setUp() throws ServletException {
		MockitoAnnotations.initMocks(this);

		when(
			_portletLocalService.getPortletById(Matchers.anyString())
		).thenAnswer(
			new Answer<Portlet>() {

				@Override
				public Portlet answer(InvocationOnMock invocation)
					throws Throwable {

					Object[] args = invocation.getArguments();

					if (Objects.equals(_TEST_PORTLET_ID, args[0])) {
						return _testPortlet;
					}
					else if (Objects.equals(PortletKeys.PORTAL, args[0])) {
						return _portalPortlet;
					}
					else if (Objects.equals(_NONEXISTING_PORTLET_ID, args[0])) {
						return null;
					}

					return _portletUndeployed;
				}

			}
		);

		mockStatic(PortletLocalServiceUtil.class, new CallsRealMethods());

		stub(
			method(PortletLocalServiceUtil.class, "getService")
		).toReturn(
			_portletLocalService
		);

		setUpComboServlet();

		setUpPortalServletContext();

		setUpPortalPortlet();

		setUpPluginServletContext();

		setUpTestPortlet();

		when(
			_portletUndeployed.isUndeployedPortlet()
		).thenReturn(
			true
		);

		mockStatic(PrefsPropsUtil.class);

		when(
			PrefsPropsUtil.getStringArray(
				Mockito.eq(PropsKeys.COMBO_ALLOWED_FILE_EXTENSIONS),
				Mockito.anyString())
		).thenReturn(
			new String[] {".css", ".js"}
		);

		_mockHttpServletRequest = new MockHttpServletRequest();

		_mockHttpServletRequest.setLocalAddr("localhost");
		_mockHttpServletRequest.setLocalPort(8080);
		_mockHttpServletRequest.setScheme("http");

		_mockHttpServletResponse = new MockHttpServletResponse();
	}

	@Test
	public void testEmptyParameters() throws Exception {
		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();
		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_comboServlet.service(mockHttpServletRequest, mockHttpServletResponse);

		Assert.assertEquals(
			HttpServletResponse.SC_NOT_FOUND,
			mockHttpServletResponse.getStatus());
	}

	@Test
	public void testGetResourceRequestDispatcherWithNonexistingPortletId()
		throws Exception {

		RequestDispatcher requestDispatcher =
			_comboServlet.getResourceRequestDispatcher(
				_mockHttpServletRequest, _mockHttpServletResponse,
				_NONEXISTING_PORTLET_ID + ":/js/javascript.js");

		Assert.assertNull(requestDispatcher);
	}

	@Test
	public void testGetResourceRequestDispatcherWithoutPortletId()
		throws Exception {

		String path = "/js/javascript.js";

		_comboServlet.getResourceRequestDispatcher(
			_mockHttpServletRequest, _mockHttpServletResponse,
			"/js/javascript.js");

		Mockito.verify(_portalServletContext);

		_portalServletContext.getRequestDispatcher(path);
	}

	@Test
	public void testGetResourceWithPortletId() throws Exception {
		_comboServlet.getResourceRequestDispatcher(
			_mockHttpServletRequest, _mockHttpServletResponse,
			_TEST_PORTLET_ID + ":/js/javascript.js");

		Mockito.verify(_pluginServletContext);

		_pluginServletContext.getRequestDispatcher("/js/javascript.js");
	}

	@Test
	public void testMixedExtensionsRequest() throws Exception {
		FileUtil fileUtil = new FileUtil();

		fileUtil.setFile(new FileImpl());

		LanguageUtil languageUtil = new LanguageUtil();

		languageUtil.setLanguage(new LanguageImpl());

		PropsUtil.setProps(new PropsImpl());

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setQueryString(
			"/js/javascript.js&/css/styles.css");

		MockHttpServletResponse mockHttpServletResponse =
			new MockHttpServletResponse();

		_comboServlet.service(mockHttpServletRequest, mockHttpServletResponse);

		Assert.assertEquals(
			HttpServletResponse.SC_BAD_REQUEST,
			mockHttpServletResponse.getStatus());
	}

	@Test
	public void testValidateInValidModuleExtension() throws Exception {
		boolean valid = _comboServlet.validateModuleExtension(
			_TEST_PORTLET_ID +
				"_INSTANCE_.js:/api/jsonws?discover=true&callback=aaa");

		Assert.assertFalse(valid);
	}

	@Test
	public void testValidateModuleExtensionWithParameterPath()
		throws Exception {

		boolean valid = _comboServlet.validateModuleExtension(
			_TEST_PORTLET_ID +
				"_INSTANCE_.js:/api/jsonws;.js?discover=true&callback=aaa");

		Assert.assertFalse(valid);
	}

	@Test
	public void testValidateValidModuleExtension() throws Exception {
		boolean valid = _comboServlet.validateModuleExtension(
			_TEST_PORTLET_ID + "_INSTANCE_.js:/js/javascript.js");

		Assert.assertTrue(valid);
	}

	protected ServletConfig getServletConfig() {
		ServletConfig servletConfig = new MockServletConfig(
			_portalServletContext);

		return servletConfig;
	}

	protected void setUpComboServlet() throws ServletException {
		_comboServlet = new ComboServlet();

		ServletConfig servletConfig = getServletConfig();

		_comboServlet.init(servletConfig);
	}

	protected void setUpPluginServletContext() {
		_pluginServletContext = spy(new MockServletContext());
	}

	protected void setUpPortalPortlet() {
		when(
			_portalPortlet.getContextPath()
		).thenReturn(
			"portal"
		);

		when(
			_portalPortletApp.getServletContext()
		).thenReturn(
			_portalServletContext
		);

		when(
			_portalPortlet.getPortletApp()
		).thenReturn(
			_portalPortletApp
		);

		when(
			_portalPortlet.getRootPortletId()
		).thenReturn(
			PortletKeys.PORTAL
		);
	}

	protected void setUpPortalServletContext() {
		_portalServletContext = spy(new MockServletContext());

		_portalServletContext.setContextPath("portal");
	}

	protected void setUpTestPortlet() {
		when(
			_testPortletApp.getServletContext()
		).thenReturn(
			_pluginServletContext
		);

		when(
			_testPortlet.getPortletApp()
		).thenReturn(
			_testPortletApp
		);

		when(
			_testPortlet.getRootPortletId()
		).thenReturn(
			_TEST_PORTLET_ID
		);
	}

	private static final String _NONEXISTING_PORTLET_ID = "2345678";

	private static final String _TEST_PORTLET_ID = "TEST_PORTLET_ID";

	private static Http _http;
	private static final HttpUtil _httpUtil = new HttpUtil();
	private static Portal _portal;
	private static final PortalUtil _portalUtil = new PortalUtil();

	private ComboServlet _comboServlet;
	private MockHttpServletRequest _mockHttpServletRequest;
	private MockHttpServletResponse _mockHttpServletResponse;
	private MockServletContext _pluginServletContext;

	@Mock
	private Portlet _portalPortlet;

	@Mock
	private PortletApp _portalPortletApp;

	private MockServletContext _portalServletContext;

	@Mock
	private PortletLocalService _portletLocalService;

	@Mock
	private Portlet _portletUndeployed;

	@Mock
	private Portlet _testPortlet;

	@Mock
	private PortletApp _testPortletApp;

}