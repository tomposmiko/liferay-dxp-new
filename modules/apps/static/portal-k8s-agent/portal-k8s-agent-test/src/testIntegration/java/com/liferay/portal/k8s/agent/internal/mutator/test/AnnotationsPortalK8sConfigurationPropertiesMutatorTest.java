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

package com.liferay.portal.k8s.agent.internal.mutator.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.k8s.agent.mutator.PortalK8sConfigurationPropertiesMutator;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Dictionary;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Raymond Augé
 */
@RunWith(Arquillian.class)
public class AnnotationsPortalK8sConfigurationPropertiesMutatorTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testMutateConfigurationProperties() throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(
			AnnotationsPortalK8sConfigurationPropertiesMutatorTest.class);
		String filterString = StringBundler.concat(
			"(&(component.name=*.",
			"AnnotationsPortalK8sConfigurationPropertiesMutator)(objectClass=",
			PortalK8sConfigurationPropertiesMutator.class.getName(), "))");

		ServiceTracker
			<PortalK8sConfigurationPropertiesMutator,
			 PortalK8sConfigurationPropertiesMutator> serviceTracker =
				new ServiceTracker<>(
					bundle.getBundleContext(),
					FrameworkUtil.createFilter(filterString), null);

		try {
			serviceTracker.open();

			PortalK8sConfigurationPropertiesMutator
				portalK8sConfigurationPropertiesMutator =
					serviceTracker.waitForService(4000);

			Dictionary<String, Object> properties = new HashMapDictionary<>();

			portalK8sConfigurationPropertiesMutator.
				mutateConfigurationProperties(
					HashMapBuilder.put(
						"cloud.liferay.com/context-data",
						JSONUtil.put(
							"domains", JSONUtil.putAll("foo")
						).put(
							"environment", "uat"
						).toString()
					).build(),
					new HashMap<>(), properties);

			Assert.assertArrayEquals(
				new String[] {"foo"},
				(String[])properties.get("com.liferay.lxc.ext.domains"));
			Assert.assertEquals(
				"uat", (String)properties.get("k8s.lxc.environment"));
		}
		finally {
			serviceTracker.close();
		}
	}

}