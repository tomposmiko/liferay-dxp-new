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

import com.liferay.petra.lang.HashUtil;

import java.util.Objects;

/**
 * @author Leonardo Barros
 */
public class DDMFormFieldValidationExpression {

	public DDMFormFieldValidationExpression() {
	}

	public DDMFormFieldValidationExpression(
		DDMFormFieldValidationExpression ddmFormFieldValidationExpression) {

		_name = ddmFormFieldValidationExpression._name;
		_value = ddmFormFieldValidationExpression._value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DDMFormFieldValidationExpression)) {
			return false;
		}

		DDMFormFieldValidationExpression ddmFormFieldValidationExpression =
			(DDMFormFieldValidationExpression)obj;

		if (Objects.equals(_name, ddmFormFieldValidationExpression._name)) {
			return true;
		}

		return false;
	}

	public String getName() {
		return _name;
	}

	public String getValue() {
		return _value;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, _name);
	}

	public void setName(String name) {
		_name = name;
	}

	public void setValue(String value) {
		_value = value;
	}

	private String _name;
	private String _value;

}