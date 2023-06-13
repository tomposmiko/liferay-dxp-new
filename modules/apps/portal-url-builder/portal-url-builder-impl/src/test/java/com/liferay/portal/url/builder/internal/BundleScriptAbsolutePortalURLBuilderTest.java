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

package com.liferay.portal.url.builder.internal;

import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.url.builder.AbsolutePortalURLBuilder;
import com.liferay.portal.url.builder.BundleScriptAbsolutePortalURLBuilder;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author Iván Zaera Avellón
 */
@RunWith(Parameterized.class)
public class BundleScriptAbsolutePortalURLBuilderTest
	extends BaseAbsolutePortalURLBuilderTestCase {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Parameterized.Parameters(name = "{0}: context={1}, proxy={2}, cdnHost={3}")
	public static Collection<Object[]> data() {
		return Arrays.asList(
			new Object[][] {
				{0, false, false, false}, {1, false, false, true},
				{2, false, true, false}, {3, false, true, true},
				{4, true, false, false}, {5, true, false, true},
				{6, true, true, false}, {7, true, true, true}
			});
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();

		_absolutePortalURLBuilder = new AbsolutePortalURLBuilderImpl(
			mockCacheHelper(), mockPortal(context, proxy, cdnHost),
			mockHttpServletRequest());

		_bundleScriptAbsolutePortalURLBuilder =
			_absolutePortalURLBuilder.forBundleScript(mockBundle(), "index.js");
	}

	@Test
	public void test() {
		Assert.assertEquals(
			_RESULTS[index], _bundleScriptAbsolutePortalURLBuilder.build());
	}

	@Test
	public void testIgnoreCDN() {
		_bundleScriptAbsolutePortalURLBuilder.ignoreCDNHost();

		Assert.assertEquals(
			_RESULTS_IGNORE_CDN[index],
			_bundleScriptAbsolutePortalURLBuilder.build());
	}

	@Test
	public void testIgnoreCDNAndProxy() {
		_bundleScriptAbsolutePortalURLBuilder.ignoreCDNHost();
		_bundleScriptAbsolutePortalURLBuilder.ignorePathProxy();

		Assert.assertEquals(
			_RESULTS_IGNORE_CDN_AND_PROXY[index],
			_bundleScriptAbsolutePortalURLBuilder.build());
	}

	@Test
	public void testIgnoreProxy() {
		_bundleScriptAbsolutePortalURLBuilder.ignorePathProxy();

		Assert.assertEquals(
			_RESULTS_IGNORE_PROXY[index],
			_bundleScriptAbsolutePortalURLBuilder.build());
	}

	@Parameterized.Parameter(3)
	public boolean cdnHost;

	@Parameterized.Parameter(1)
	public boolean context;

	@Parameterized.Parameter
	public int index;

	@Parameterized.Parameter(2)
	public boolean proxy;

	private static final String[] _RESULTS = {
		"/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"http://cdn-host/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&" +
			"languageId=es",
		"/proxy/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"http://cdn-host/proxy/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&" +
			"languageId=es",
		"/context/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"http://cdn-host/context/o/wcp/index.js?mac=aG9saQ==&" +
			"browserId=firefox&languageId=es",
		"/proxy/context/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&" +
			"languageId=es",
		"http://cdn-host/proxy/context/o/wcp/index.js?mac=aG9saQ==&" +
			"browserId=firefox&languageId=es"
	};

	private static final String[] _RESULTS_IGNORE_CDN = {
		"/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/proxy/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/proxy/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/context/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/context/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/proxy/context/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&" +
			"languageId=es",
		"/proxy/context/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&" +
			"languageId=es"
	};

	private static final String[] _RESULTS_IGNORE_CDN_AND_PROXY = {
		"/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/context/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/context/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/context/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"/context/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es"
	};

	private static final String[] _RESULTS_IGNORE_PROXY = {
		"/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"http://cdn-host/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&" +
			"languageId=es",
		"/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"http://cdn-host/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&" +
			"languageId=es",
		"/context/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"http://cdn-host/context/o/wcp/index.js?mac=aG9saQ==&" +
			"browserId=firefox&languageId=es",
		"/context/o/wcp/index.js?mac=aG9saQ==&browserId=firefox&languageId=es",
		"http://cdn-host/context/o/wcp/index.js?mac=aG9saQ==&" +
			"browserId=firefox&languageId=es"
	};

	private AbsolutePortalURLBuilder _absolutePortalURLBuilder;
	private BundleScriptAbsolutePortalURLBuilder
		_bundleScriptAbsolutePortalURLBuilder;

}