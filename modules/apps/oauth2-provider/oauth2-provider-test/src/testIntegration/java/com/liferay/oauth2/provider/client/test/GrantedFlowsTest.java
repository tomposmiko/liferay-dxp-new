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

import com.liferay.oauth2.provider.constants.GrantType;
import com.liferay.oauth2.provider.test.internal.activator.BaseTestPreparatorBundleActivator;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Collections;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Carlos Sierra Andrés
 */
@RunAsClient
@RunWith(Arquillian.class)
public class GrantedFlowsTest extends BaseClientTestCase {

	@Deployment
	public static Archive<?> getArchive() throws Exception {
		return BaseClientTestCase.getArchive(
			AnnotatedApplicationTestPreparatorBundleActivator.class);
	}

	@Test
	public void test() throws Exception {
		String errorString = getToken(
			"oauthTestApplicationPassword", null,
			this::getClientCredentialsResponse, this::parseError);

		Assert.assertEquals("unauthorized_client", errorString);

		String tokenString = getToken(
			"oauthTestApplicationPassword", null,
			getResourceOwnerPasswordBiFunction("test@liferay.com", "test"),
			this::parseTokenString);

		Assert.assertNotNull(tokenString);

		errorString = getToken(
			"oauthTestApplicationClient", null,
			getResourceOwnerPasswordBiFunction("test@liferay.com", "test"),
			this::parseError);

		Assert.assertEquals("unauthorized_client", errorString);

		tokenString = getToken(
			"oauthTestApplicationClient", null,
			this::getClientCredentialsResponse, this::parseTokenString);

		Assert.assertNotNull(tokenString);

		errorString = getToken(
			"oauthTestApplicationNoGrants", null,
			getAuthorizationCodePKCEBiFunction(
				"test@liferay.com", "test", null),
			this::parseError);

		Assert.assertEquals("unauthorized_client", errorString);

		tokenString = getToken(
			"oauthTestApplicationCode", null,
			getAuthorizationCodeBiFunction("test@liferay.com", "test", null),
			this::parseTokenString);

		Assert.assertNotNull(tokenString);

		errorString = getToken(
			"oauthTestApplicationPassword", null,
			getAuthorizationCodeBiFunction("test@liferay.com", "test", null),
			this::parseError);

		Assert.assertEquals("unauthorized_client", errorString);

		tokenString = getToken(
			"oauthTestApplicationCodePKCE", null,
			getAuthorizationCodePKCEBiFunction(
				"test@liferay.com", "test", null),
			this::parseTokenString);

		Assert.assertNotNull(tokenString);
	}

	public static class AnnotatedApplicationTestPreparatorBundleActivator
		extends BaseTestPreparatorBundleActivator {

		@Override
		protected void prepareTest() throws Exception {
			long defaultCompanyId = PortalUtil.getDefaultCompanyId();

			User user = UserTestUtil.getAdminUser(defaultCompanyId);

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplicationCode",
				Collections.singletonList(GrantType.AUTHORIZATION_CODE),
				Collections.singletonList("everything"));

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplicationCodePKCE", null,
				Collections.singletonList(GrantType.AUTHORIZATION_CODE_PKCE),
				Collections.singletonList("everything"),
				Collections.singletonList("http://redirecturi:8080"));

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplicationClient",
				Collections.singletonList(GrantType.CLIENT_CREDENTIALS),
				Collections.singletonList("everything"));

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplicationNoGrants", null,
				Collections.emptyList(),
				Collections.singletonList("everything"),
				Collections.singletonList("http://redirecturi:8080"));

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplicationPassword",
				Collections.singletonList(GrantType.RESOURCE_OWNER_PASSWORD),
				Collections.singletonList("everything"));
		}

	}

}