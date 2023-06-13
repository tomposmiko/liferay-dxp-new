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

package com.liferay.portal.vulcan.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.jaxrs.impl.UriBuilderImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Carlos Correa
 * @author Raymond Augé
 */
public class UriInfoUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(_portal);

		PropsUtil.setProps(_props);

		Mockito.when(
			_uriInfo.getBaseUriBuilder()
		).thenReturn(
			_uriBuilder
		);
	}

	@Test
	public void testGetBaseUriBuilderHostNoScheme() throws Exception {
		_uriBuilder.host("localhost");

		_assertUriBuilder(
			0, "", 0, 0, _uriBuilder, _uriInfo, "localhost/test-path");
	}

	@Test
	public void testGetBaseUriBuilderHostScheme() throws Exception {
		_uriBuilder.host("localhost");
		_uriBuilder.scheme("http");

		_assertUriBuilder(
			0, "", 0, 0, _uriBuilder, _uriInfo, "http://localhost/test-path");
	}

	@Test
	public void testGetBaseUriBuilderHttpsHostNoScheme() throws Exception {
		_uriBuilder.host("localhost");

		_setProtocol(Http.HTTPS);

		_assertUriBuilder(
			0, "", 0, 1, _uriBuilder, _uriInfo, "https://localhost/test-path");
	}

	@Test
	public void testGetBaseUriBuilderHttpsHostScheme() throws Exception {
		_uriBuilder.host("localhost");
		_uriBuilder.scheme("http");

		_setProtocol(Http.HTTPS);

		_assertUriBuilder(
			0, "", 0, 1, _uriBuilder, _uriInfo, "https://localhost/test-path");
	}

	@Test
	public void testGetBaseUriBuilderHttpsNoHostNoScheme() throws Exception {
		_setProtocol(Http.HTTPS);

		_assertUriBuilder(0, "", 0, 0, _uriBuilder, _uriInfo, "/test-path");
	}

	@Test
	public void testGetBaseUriBuilderNoHostNoScheme() throws Exception {
		_assertUriBuilder(0, "", 0, 0, _uriBuilder, _uriInfo, "/test-path");
	}

	@Test
	public void testGetBaseUriBuilderPathContext() throws Exception {
		String path = StringPool.SLASH + RandomTestUtil.randomString();

		String pathContext = StringPool.SLASH + RandomTestUtil.randomString();

		_setPathContext(path, pathContext);

		_assertUriBuilder(
			1, pathContext + path, 1, 0, _uriBuilder, _uriInfo,
			pathContext + path);
	}

	private void _assertUriBuilder(
			int buildTimes, String path, int replacePathTimes, int schemeTimes,
			UriBuilder uriBuilder, UriInfo uriInfo, String uriString)
		throws Exception {

		Assert.assertSame(uriBuilder, UriInfoUtil.getBaseUriBuilder(uriInfo));

		Mockito.verify(
			uriBuilder, Mockito.times(buildTimes)
		).build();

		Mockito.verify(
			uriBuilder, Mockito.times(replacePathTimes)
		).replacePath(
			path
		);

		Mockito.verify(
			uriBuilder, Mockito.times(schemeTimes)
		).scheme(
			Http.HTTPS
		);

		Mockito.verify(
			uriInfo
		).getBaseUriBuilder();

		Assert.assertEquals(new URI(uriString), uriBuilder.build());
	}

	private void _setPathContext(String path, String pathContext) {
		Mockito.when(
			_portal.getPathContext()
		).thenReturn(
			pathContext
		);

		Mockito.when(
			_portal.getPathContext(Mockito.anyString())
		).thenReturn(
			pathContext + path
		);
	}

	private void _setProtocol(String protocol) {
		Mockito.when(
			_props.get(PropsKeys.WEB_SERVER_PROTOCOL)
		).thenReturn(
			protocol
		);
	}

	private final Portal _portal = Mockito.mock(Portal.class);
	private final Props _props = Mockito.mock(Props.class);
	private final UriBuilder _uriBuilder = Mockito.spy(
		new UriBuilderImpl(
		).path(
			"/test-path"
		));
	private final UriInfo _uriInfo = Mockito.mock(UriInfo.class);

}