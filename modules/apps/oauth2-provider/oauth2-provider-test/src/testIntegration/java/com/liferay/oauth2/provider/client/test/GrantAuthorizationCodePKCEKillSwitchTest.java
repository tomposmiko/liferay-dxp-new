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
import com.liferay.oauth2.provider.test.internal.TestAnnotatedApplication;
import com.liferay.oauth2.provider.test.internal.activator.BaseTestPreparatorBundleActivator;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Collections;
import java.util.Dictionary;

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
public class GrantAuthorizationCodePKCEKillSwitchTest
	extends BaseClientTestCase {

	@Deployment
	public static Archive<?> getDeployment() throws Exception {
		return BaseClientTestCase.getDeployment(
			GrantKillClientCredentialsSwitchTestPreparatorBundleActivator.
				class);
	}

	@Test
	public void test() throws Exception {
		Assert.assertEquals(
			"unauthorized_client",
			getToken(
				"oauthTestApplicationCodePKCE", null,
				getAuthorizationCodePKCE("test@liferay.com", "test", null),
				this::parseError));
	}

	public static class
		GrantKillClientCredentialsSwitchTestPreparatorBundleActivator
			extends BaseTestPreparatorBundleActivator {

		@Override
		protected void prepareTest() throws Exception {
			executeAndWaitForReadiness(
				() -> {
					Dictionary<String, Object> properties =
						new HashMapDictionary<>();

					properties.put(
						"oauth2.allow.authorization.code.pkce.grant", false);

					Runnable runnable = updateOrCreateConfiguration(
						"com.liferay.oauth2.provider.configuration." +
							"OAuth2ProviderConfiguration",
						properties);

					autoCloseables.add(
						() -> executeAndWaitForReadiness(runnable));
				});

			long defaultCompanyId = PortalUtil.getDefaultCompanyId();

			User user = UserTestUtil.getAdminUser(defaultCompanyId);

			Dictionary<String, Object> properties = new HashMapDictionary<>();

			properties.put("oauth2.scopechecker.type", "annotations");

			registerJaxRsApplication(
				new TestAnnotatedApplication(), "annotated", properties);

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplicationCodePKCE", null,
				Collections.singletonList(GrantType.AUTHORIZATION_CODE_PKCE),
				Collections.singletonList("everything"));
		}

	}

}