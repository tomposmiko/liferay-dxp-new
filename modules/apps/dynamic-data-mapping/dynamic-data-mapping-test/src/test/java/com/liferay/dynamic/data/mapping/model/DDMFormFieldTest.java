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

package com.liferay.dynamic.data.mapping.model;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactoryUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class DDMFormFieldTest {

	@Before
	public void setUp() throws Exception {
		setUpJSONFactoryUtil();
	}

	@Test
	public void testGetDataSourceTypeInvalid() {
		DDMFormField ddmFormField = new DDMFormField("select1", "select");

		ddmFormField.setProperty("dataSourceType", 1);

		Assert.assertEquals(StringPool.BLANK, ddmFormField.getDataSourceType());
	}

	@Test
	public void testGetDataSourceTypeValid() {
		DDMFormField ddmFormField1 = new DDMFormField("select1", "select");

		ddmFormField1.setProperty(
			"dataSourceType",
			JSONFactoryUtil.createJSONArray(
				new String[] {"manual", "data-provider", "from-autofill"}));

		DDMFormField ddmFormField2 = new DDMFormField("select2", "select");

		ddmFormField2.setProperty("dataSourceType", "manual");

		Assert.assertEquals(
			ddmFormField1.getDataSourceType(),
			ddmFormField2.getDataSourceType());
	}

	protected void setUpJSONFactoryUtil() {
		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());
	}

}