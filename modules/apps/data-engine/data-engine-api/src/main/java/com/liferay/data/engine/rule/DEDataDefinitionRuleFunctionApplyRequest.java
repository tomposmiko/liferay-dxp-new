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

package com.liferay.data.engine.rule;

import com.liferay.data.engine.model.DEDataDefinitionField;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * It contains references that a rule function may needs during its execution.
 *
 * @author Leonardo Barros
 * @review
 */
public final class DEDataDefinitionRuleFunctionApplyRequest {

	/**
	 * @return A data definition field.
	 * @review
	 */
	public DEDataDefinitionField getDEDataDefinitionField() {
		return _deDataDefinitionField;
	}

	/**
	 * @return A map with parameters that may be used by the rule function.
	 * @review
	 */
	public Map<String, Object> getParameters() {
		return Collections.unmodifiableMap(_parameters);
	}

	/**
	 * @return A value which must be verified.
	 * @review
	 */
	public Object getValue() {
		return _value;
	}

	/**
	 * It sets the data definition field.
	 *
	 * @param deDataDefinitionField The data definition field.
	 * @review
	 */
	public void setDEDataDefinitionField(
		DEDataDefinitionField deDataDefinitionField) {

		_deDataDefinitionField = deDataDefinitionField;
	}

	/**
	 * Set parameters to be used by the rule function.
	 *
	 * @param parameters The parameters
	 * @review
	 */
	public void setParameters(Map<String, Object> parameters) {
		if (parameters != null) {
			_parameters.putAll(parameters);
		}
	}

	/**
	 * It sets the value which must be verified.
	 * @param value The value which must be verified
	 * @review
	 */
	public void setValue(Object value) {
		_value = value;
	}

	private DEDataDefinitionField _deDataDefinitionField;
	private final Map<String, Object> _parameters = new HashMap<>();
	private Object _value;

}