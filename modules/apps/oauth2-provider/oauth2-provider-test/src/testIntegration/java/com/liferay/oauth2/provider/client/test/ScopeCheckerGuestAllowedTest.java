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
import com.liferay.oauth2.provider.constants.GrantType;
import com.liferay.oauth2.provider.constants.OAuth2ProviderConstants;
import com.liferay.oauth2.provider.internal.test.TestAnnotatedApplication;
import com.liferay.oauth2.provider.internal.test.TestApplication;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.log.Log4JLoggerTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.net.URISyntaxException;

import java.util.Arrays;
import java.util.Collections;
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
 * @author Tomas Polesovsky
 */
@RunWith(Arquillian.class)
public class ScopeCheckerGuestAllowedTest extends BaseClientTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void test() throws Exception {
		testApplication("/annotated-guest-allowed/", "everything.read", 200);

		testApplication("/annotated-guest-allowed/no-scope", "no-scope", 200);

		testApplication("/annotated-guest-default/", "everything.read", 200);

		testApplication("/annotated-guest-default/no-scope", "no-scope", 200);

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					"portal_web.docroot.errors.code_jsp", Level.WARN)) {

			testApplication(
				"/annotated-guest-not-allowed/", "everything.read", 403);

			testApplication(
				"/annotated-guest-not-allowed/no-scope", "no-scope", 403);
		}

		testApplication("/default-jaxrs-app-guest-allowed/", "get", 200);

		testApplication("/default-jaxrs-app-guest-default/", "get", 200);

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					"portal_web.docroot.errors.code_jsp", Level.WARN)) {

			testApplication(
				"/default-jaxrs-app-guest-not-allowed/", "get", 403);
		}

		testApplication("/methods-guest-allowed/", "get", 200);

		testApplication("/methods-guest-default/", "get", 200);

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					"portal_web.docroot.errors.code_jsp", Level.WARN)) {

			testApplication("/methods-guest-not-allowed/", "get", 403);
		}
	}

	public static class ScopeCheckerGuestAllowedTestPreparatorBundleActivator
		extends BaseTestPreparatorBundleActivator {

		@Override
		protected void prepareTest() throws Exception {
			Dictionary<String, Object> properties = new HashMapDictionary<>();

			properties.put("auth.verifier.guest.allowed", true);
			properties.put("oauth2.scope.checker.type", "annotations");

			registerJaxRsApplication(
				new TestAnnotatedApplication(), "annotated-guest-allowed",
				properties);

			properties = new HashMapDictionary<>();

			properties.put("oauth2.scope.checker.type", "annotations");

			registerJaxRsApplication(
				new TestAnnotatedApplication(), "annotated-guest-default",
				properties);

			properties = new HashMapDictionary<>();

			properties.put("auth.verifier.guest.allowed", false);
			properties.put("oauth2.scope.checker.type", "annotations");

			registerJaxRsApplication(
				new TestAnnotatedApplication(), "annotated-guest-not-allowed",
				properties);

			properties = new HashMapDictionary<>();

			properties.put("auth.verifier.guest.allowed", true);

			registerJaxRsApplication(
				new TestApplication(), "default-jaxrs-app-guest-allowed",
				properties);

			registerJaxRsApplication(
				new TestApplication(), "default-jaxrs-app-guest-default",
				new HashMapDictionary<>());

			properties = new HashMapDictionary<>();

			properties.put("auth.verifier.guest.allowed", false);

			registerJaxRsApplication(
				new TestApplication(), "default-jaxrs-app-guest-not-allowed",
				properties);

			properties = new HashMapDictionary<>();

			properties.put("auth.verifier.guest.allowed", true);
			properties.put("oauth2.scope.checker.type", "http.method");

			registerJaxRsApplication(
				new TestApplication(), "methods-guest-allowed", properties);

			properties = new HashMapDictionary<>();

			properties.put("oauth2.scope.checker.type", "http.method");

			registerJaxRsApplication(
				new TestApplication(), "methods-guest-default", properties);

			properties = new HashMapDictionary<>();

			properties.put("auth.verifier.guest.allowed", false);
			properties.put("oauth2.scope.checker.type", "http.method");

			registerJaxRsApplication(
				new TestApplication(), "methods-guest-not-allowed", properties);

			long defaultCompanyId = PortalUtil.getDefaultCompanyId();

			User user = UserTestUtil.getAdminUser(defaultCompanyId);

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplication",
				Collections.singletonList(GrantType.CLIENT_CREDENTIALS),
				Arrays.asList("everything.read", "GET"));

			createServiceAccessProfile(
				user.getUserId(), "#get*", true, true, "GUEST_OAUTH2_TEST");
		}

	}

	@Override
	protected BundleActivator getBundleActivator() {
		return new ScopeCheckerGuestAllowedTestPreparatorBundleActivator();
	}

	protected void testApplication(
			String path, String expectedValidTokenResponse,
			int expectedInvalidTokenStatus)
		throws URISyntaxException {

		WebTarget webTarget = getWebTarget(path);

		for (String invalidToken : _INVALID_TOKENS) {
			Invocation.Builder invocationBuilder = webTarget.request();

			if (invalidToken != null) {
				invocationBuilder = authorize(invocationBuilder, invalidToken);
			}

			Response response = invocationBuilder.get();

			int status = response.getStatus();

			Assert.assertEquals(
				"Token: " + invalidToken, expectedInvalidTokenStatus, status);
		}

		Invocation.Builder invocationBuilder = authorize(
			webTarget.request(), getToken("oauthTestApplication"));

		Assert.assertEquals(
			expectedValidTokenResponse, invocationBuilder.get(String.class));
	}

	private static final String[] _INVALID_TOKENS = {
		OAuth2ProviderConstants.EXPIRED_TOKEN, StringPool.BLANK,
		StringPool.NULL, "Invalid Token", null
	};

}