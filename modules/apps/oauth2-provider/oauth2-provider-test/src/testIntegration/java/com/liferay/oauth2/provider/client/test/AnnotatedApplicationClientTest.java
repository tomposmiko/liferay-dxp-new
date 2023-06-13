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

import com.liferay.oauth2.provider.test.internal.TestAnnotatedApplication;
import com.liferay.oauth2.provider.test.internal.activator.BaseTestPreparatorBundleActivator;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Dictionary;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

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
public class AnnotatedApplicationClientTest extends BaseClientTestCase {

	@Deployment
	public static Archive<?> getDeployment() throws Exception {
		return BaseClientTestCase.getDeployment(
			AnnotatedApplicationTestPreparatorBundleActivator.class);
	}

	@Test
	public void test() throws Exception {
		WebTarget webTarget = getWebTarget("/annotated");

		Invocation.Builder invocationBuilder = authorize(
			webTarget.request(),
			getToken(
				"oauthTestApplicationAfter", null,
				getResourceOwnerPassword("test@liferay.com", "test"),
				this::parseTokenString));

		Assert.assertEquals(
			"everything.read", invocationBuilder.get(String.class));

		invocationBuilder = authorize(
			webTarget.request(), getToken("oauthTestApplicationBefore"));

		Response response = invocationBuilder.get();

		Assert.assertEquals(403, response.getStatus());
	}

	public static class AnnotatedApplicationTestPreparatorBundleActivator
		extends BaseTestPreparatorBundleActivator {

		@Override
		protected void prepareTest() throws Exception {
			long defaultCompanyId = PortalUtil.getDefaultCompanyId();

			User user = UserTestUtil.getAdminUser(defaultCompanyId);

			Dictionary<String, Object> properties = new HashMapDictionary<>();

			properties.put("oauth2.scopechecker.type", "annotations");

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplicationBefore");

			registerJaxRsApplication(
				new TestAnnotatedApplication(), "annotated", properties);

			createOAuth2Application(
				defaultCompanyId, user, "oauthTestApplicationAfter");
		}

	}

}