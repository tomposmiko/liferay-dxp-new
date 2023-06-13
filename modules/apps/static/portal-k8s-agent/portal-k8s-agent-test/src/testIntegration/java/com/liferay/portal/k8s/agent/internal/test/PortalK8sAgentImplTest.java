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

package com.liferay.portal.k8s.agent.internal.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.k8s.agent.PortalK8sConfigMapModifier;
import com.liferay.portal.k8s.agent.configuration.v1.PortalK8sAgentConfiguration;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Raymond Augé
 */
@RunWith(Arquillian.class)
public class PortalK8sAgentImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@ClassRule
	@Rule
	public static final KubernetesServer kubernetesServer =
		new KubernetesServer(false, true);

	@Test
	public void testK8sAgentInitialization() throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(PortalK8sAgentImplTest.class);

		ServiceTracker<PortalK8sConfigMapModifier, PortalK8sConfigMapModifier>
			serviceTracker = new ServiceTracker<>(
				bundle.getBundleContext(), PortalK8sConfigMapModifier.class,
				null);

		Configuration configuration = null;

		try {
			serviceTracker.open();

			PortalK8sConfigMapModifier portalK8sConfigMapModifier =
				serviceTracker.waitForService(2000);

			Assert.assertNull(portalK8sConfigMapModifier);

			configuration = _configurationAdmin.getConfiguration(
				PortalK8sAgentConfiguration.class.getName(), "?");

			KubernetesMockServer kubernetesMockServer =
				kubernetesServer.getKubernetesMockServer();

			configuration.update(
				HashMapDictionaryBuilder.<String, Object>put(
					"apiServerHost", kubernetesMockServer.getHostName()
				).put(
					"apiServerPort", kubernetesMockServer.getPort()
				).put(
					"apiServerSSL", Boolean.FALSE
				).put(
					"caCertData",
					StringUtil.read(
						PortalK8sAgentImplTest.class, "dependencies/ca.crt")
				).put(
					"namespace", "default"
				).put(
					"saToken", "saToken"
				).build());

			portalK8sConfigMapModifier = serviceTracker.waitForService(2000);

			Assert.assertNotNull(portalK8sConfigMapModifier);
		}
		finally {
			serviceTracker.close();

			if (configuration != null) {
				configuration.delete();
			}
		}
	}

	@Inject
	private static ConfigurationAdmin _configurationAdmin;

}