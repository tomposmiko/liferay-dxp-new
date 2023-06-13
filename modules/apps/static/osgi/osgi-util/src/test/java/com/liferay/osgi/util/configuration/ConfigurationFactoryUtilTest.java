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

package com.liferay.osgi.util.configuration;

import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Raymond Augé
 */
public class ConfigurationFactoryUtilTest {

	@Test
	public void testGetExternalReferenceCode() {
		Map<String, HashMap<String, Object>> map =
			HashMapBuilder.<String, HashMap<String, Object>>put(
				"foo",
				HashMapBuilder.<String, Object>put(
					"service.factoryPid", "com.bar"
				).put(
					"service.pid", "com.bar~foo"
				).build()
			).put(
				"fum",
				HashMapBuilder.<String, Object>put(
					"service.factoryPid", "com.bar"
				).put(
					"service.pid", "com.bar~fum/liferay.com"
				).build()
			).build();

		for (Map.Entry<String, HashMap<String, Object>> entry :
				map.entrySet()) {

			Assert.assertEquals(
				entry.getKey(),
				ConfigurationFactoryUtil.getExternalReferenceCode(
					entry.getValue()));
		}

		_testGetExternalReferenceCode("foo,com.bar,com.bar~foo");
		_testGetExternalReferenceCode("foo,com.bar,com.bar~foo/liferay.com");
	}

	@Test
	public void testGetFactoryPidFromPid() {
		Assert.assertNull(ConfigurationFactoryUtil.getFactoryPidFromPid("foo"));
		Assert.assertEquals(
			"com.foo",
			ConfigurationFactoryUtil.getFactoryPidFromPid("com.foo~bar"));
	}

	@Rule
	public LiferayUnitTestRule liferayUnitTestRule = new LiferayUnitTestRule();

	private void _testGetExternalReferenceCode(String string) {
		String[] parts = StringUtil.split(string);

		Assert.assertEquals(
			parts[0],
			ConfigurationFactoryUtil.getExternalReferenceCode(
				parts[1], Arrays.asList(parts[2])));
	}

}