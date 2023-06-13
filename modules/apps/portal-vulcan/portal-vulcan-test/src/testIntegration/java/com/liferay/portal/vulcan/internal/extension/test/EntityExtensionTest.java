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

package com.liferay.portal.vulcan.internal.extension.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.extension.ExtensionProvider;
import com.liferay.portal.vulcan.extension.ExtensionProviderRegistry;
import com.liferay.portal.vulcan.extension.PropertyDefinition;
import com.liferay.portal.vulcan.internal.test.util.URLConnectionUtil;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;

import java.net.HttpURLConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

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
 * @author Carlos Correa
 */
@RunWith(Arquillian.class)
public class EntityExtensionTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(EntityExtensionTest.class);

		_bundleContext = bundle.getBundleContext();

		_group = GroupTestUtil.addGroup();

		_serviceRegistrations.add(
			_bundleContext.registerService(
				Application.class, new TestApplication(),
				HashMapDictionaryBuilder.<String, Object>put(
					"liferay.auth.verifier", true
				).put(
					"liferay.jackson", false
				).put(
					"liferay.oauth2", false
				).put(
					"osgi.jaxrs.application.base", "/test-vulcan"
				).put(
					"osgi.jaxrs.extension.select",
					"(osgi.jaxrs.name=Liferay.Vulcan)"
				).build()));
	}

	@After
	public void tearDown() {
		for (ServiceRegistration<?> serviceRegistration :
				_serviceRegistrations) {

			serviceRegistration.unregister();
		}

		_serviceRegistrations.clear();
	}

	@Test
	public void testProcessCommitedRequest() throws Exception {
		_serviceRegistrations.add(
			_bundleContext.registerService(
				ExtensionProvider.class, new TestExtensionProvider(false),
				null));

		int usersCount = UserLocalServiceUtil.getUsersCount();

		Assert.assertEquals(
			200, _getResponseCode("http://localhost:8080/o/test-vulcan/test"));

		Assert.assertEquals(
			usersCount + 1, UserLocalServiceUtil.getUsersCount());
	}

	@Test
	public void testProcessRollbackedRequest() throws Exception {
		_serviceRegistrations.add(
			_bundleContext.registerService(
				ExtensionProvider.class, new TestExtensionProvider(true),
				null));

		int usersCount = UserLocalServiceUtil.getUsersCount();

		try (LogCapture logCapture1 = LoggerTestUtil.configureLog4JLogger(
				"com.liferay.portal.vulcan.internal.jaxrs.exception.mapper." +
					"WebApplicationExceptionMapper",
				LoggerTestUtil.ERROR);
			LogCapture logCapture2 = LoggerTestUtil.configureLog4JLogger(
				"com.liferay.portal.vulcan.internal.jaxrs.container.response." +
					"filter.EntityExtensionContainerResponseFilter",
				LoggerTestUtil.ERROR)) {

			Assert.assertEquals(
				500,
				_getResponseCode("http://localhost:8080/o/test-vulcan/test"));
		}

		Assert.assertEquals(usersCount, UserLocalServiceUtil.getUsersCount());
	}

	public static class TestApplication extends Application {

		@Override
		public Set<Object> getSingletons() {
			return Collections.singleton(this);
		}

		@Consumes(MediaType.APPLICATION_JSON)
		@Path("/test")
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		public TestClass test(TestClass testClass) throws Exception {
			UserTestUtil.addUser(testClass.getGroupId());

			return testClass;
		}

	}

	private int _getResponseCode(String urlString) throws Exception {
		HttpURLConnection httpURLConnection =
			(HttpURLConnection)URLConnectionUtil.createURLConnection(urlString);

		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod(HttpMethod.POST);
		httpURLConnection.setRequestProperty(
			HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
		httpURLConnection.setRequestProperty(
			HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

		OutputStream outputStream = httpURLConnection.getOutputStream();

		try (PrintWriter printWriter = new PrintWriter(
				new OutputStreamWriter(outputStream, "UTF-8"), true)) {

			printWriter.append("{\"groupId\": " + _group.getGroupId() + "}");

			printWriter.flush();
		}

		return httpURLConnection.getResponseCode();
	}

	private BundleContext _bundleContext;

	@Inject
	private ExtensionProviderRegistry _extensionProviderRegistry;

	private Group _group;
	private final List<ServiceRegistration<?>> _serviceRegistrations =
		new ArrayList<>();

	private static class TestClass {

		public long getGroupId() {
			return groupId;
		}

		public void setGroupId(long groupId) {
			this.groupId = groupId;
		}

		protected long groupId;

	}

	private static class TestExtensionProvider implements ExtensionProvider {

		public TestExtensionProvider(boolean fail) {
			_fail = fail;

			_propertyName = RandomTestUtil.randomString();
		}

		@Override
		public Map<String, Serializable> getExtendedProperties(
			long companyId, String className, Object entity) {

			return HashMapBuilder.<String, Serializable>put(
				_propertyName, RandomTestUtil.randomString()
			).build();
		}

		@Override
		public Map<String, PropertyDefinition> getExtendedPropertyDefinitions(
			long companyId, String className) {

			return HashMapBuilder.<String, PropertyDefinition>put(
				_propertyName,
				new PropertyDefinition(
					_propertyName, _propertyName,
					PropertyDefinition.PropertyType.TEXT, false)
			).build();
		}

		@Override
		public Collection<String> getFilteredPropertyNames(
			long companyId, Object entity) {

			return Collections.emptyList();
		}

		@Override
		public boolean isApplicableExtension(long companyId, String className) {
			if (className.equals(TestClass.class.getName())) {
				return true;
			}

			return false;
		}

		@Override
		public void setExtendedProperties(
				long companyId, long userId, String className, Object entity,
				Map<String, Serializable> extendedProperties)
			throws Exception {

			if (_fail) {
				throw new Exception();
			}
		}

		private final boolean _fail;
		private final String _propertyName;

	}

}