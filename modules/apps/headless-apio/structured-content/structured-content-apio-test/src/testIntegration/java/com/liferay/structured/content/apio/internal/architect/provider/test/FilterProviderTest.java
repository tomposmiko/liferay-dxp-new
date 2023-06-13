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

package com.liferay.structured.content.apio.internal.architect.provider.test;

import com.liferay.apio.architect.provider.Provider;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.odata.filter.Filter;
import com.liferay.portal.odata.filter.InvalidFilterException;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Cristina González
 */
@RunWith(Arquillian.class)
public class FilterProviderTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testCreateContextWithInvalidPropertyInFilter() {
		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		String filterString = "unknownProperty eq 'some value'";

		mockHttpServletRequest.setParameter("filter", filterString);

		AbstractThrowableAssert exception = Assertions.assertThatThrownBy(
			() -> _provider.createContext(mockHttpServletRequest)
		).isInstanceOf(
			InvalidFilterException.class
		);

		exception.hasMessage(
			String.format(
				"Invalid query computed from filter '%s': Unknown property.",
				filterString));
	}

	@Inject(
		filter = "component.name=com.liferay.structured.content.apio.internal.architect.provider.FilterProvider"
	)
	private Provider<Filter> _provider;

}