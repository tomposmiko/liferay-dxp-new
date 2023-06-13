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

/**
 * It represents the outcome of a rule function execution.
 *
 * @author Leonardo Barros
 * @review
 */
public final class DEDataDefinitionRuleFunctionApplyResponse {

	/**
	 * @return The data definition field.
	 * @review
	 */
	public DEDataDefinitionField getDEDataDefinitionField() {
		return _deDataDefinitionField;
	}

	/**
	 * @return The error code in case of a failure.
	 * @review
	 */
	public String getErrorCode() {
		return _errorCode;
	}

	/**
	 * @return True if the rule function executed successfully.
	 * @review
	 */
	public boolean isValid() {
		return _valid;
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
	 * It sets the error code in case of a failure.
	 * @param errorCode The error code.
	 * @review
	 */
	public void setErrorCode(String errorCode) {
		_errorCode = errorCode;
	}

	/**
	 * It sets the result of the rule function
	 *
	 * @param valid True if the rule function executed successfully.
	 * @review
	 */
	public void setValid(boolean valid) {
		_valid = valid;
	}

	private DEDataDefinitionField _deDataDefinitionField;
	private String _errorCode;
	private boolean _valid;

}