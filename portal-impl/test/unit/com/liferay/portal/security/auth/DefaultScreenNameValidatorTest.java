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

package com.liferay.portal.security.auth;

import com.liferay.portal.kernel.security.auth.DefaultScreenNameValidator;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.util.HtmlImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Drew Brokke
 */
public class DefaultScreenNameValidatorTest extends DefaultScreenNameValidator {

	@Before
	public void setUp() {
		HtmlUtil htmlUtil = new HtmlUtil();

		htmlUtil.setHtml(new HtmlImpl());
	}

	@Test
	public void testGetJSEscapedSpecialChars() {
		_specialCharacters = "-._'";

		Assert.assertEquals(
			HtmlUtil.escapeJS(_specialCharacters), getJSEscapedSpecialChars());
	}

	@Override
	protected String getSpecialChars() {
		return _specialCharacters;
	}

	private String _specialCharacters = "-._";

}