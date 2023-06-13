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

package com.liferay.oauth2.provider.client.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.oauth2.provider.internal.test.TestAnnotatedApplication;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.log.Log4JLoggerTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Dictionary;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.log4j.Level;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.BundleActivator;

/**
 * @author Carlos Sierra Andrés
 */
@RunWith(Arquillian.class)
public class IsolationAcrossCompaniesTest extends BaseClientTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void test() throws Exception {
		WebTarget webTarget = getWebTarget("/annotated");

		String tokenString = getToken("oauthTestApplication", "host1.xyz");

		Invocation.Builder builder = authorize(
			webTarget.request(), tokenString);

		builder = builder.header("Host", "host1.xyz");

		Assert.assertEquals("everything.read", builder.get(String.class));

		builder = authorize(webTarget.request(), tokenString);

		builder = builder.header("Host", "host2.xyz");

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					"portal_web.docroot.errors.code_jsp", Level.WARN)) {

			Response response = builder.get();

			Assert.assertEquals(403, response.getStatus());
		}
	}

	public static class IsolationAccrossCompaniesTestPreparatorBundleActivator
		extends BaseTestPreparatorBundleActivator {

		@Override
		protected void prepareTest() throws Exception {
			Dictionary<String, Object> properties = new HashMapDictionary<>();

			properties.put("oauth2.scope.checker.type", "annotations");

			registerJaxRsApplication(
				new TestAnnotatedApplication(), "annotated", properties);

			Company company1 = createCompany("host1");

			createOAuth2Application(
				company1.getCompanyId(),
				UserTestUtil.getAdminUser(company1.getCompanyId()),
				"oauthTestApplication");

			Company company2 = createCompany("host2");

			createOAuth2Application(
				company2.getCompanyId(),
				UserTestUtil.getAdminUser(company2.getCompanyId()),
				"oauthTestApplication");
		}

	}

	@Override
	protected BundleActivator getBundleActivator() {
		return new IsolationAccrossCompaniesTestPreparatorBundleActivator();
	}

}