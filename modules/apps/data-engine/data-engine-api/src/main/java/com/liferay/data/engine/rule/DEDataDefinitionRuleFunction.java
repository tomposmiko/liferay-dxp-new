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

/**
 * This is an interface that a rule function must implements in order to apply
 * rules to values of a list of data definition fields.
 *
 * @author Leonardo Barros
 * @review
 */
public interface DEDataDefinitionRuleFunction {

	/**
	 * It executes the rule function.
	 *
	 * @param deDataDefinitionRuleFunctionApplyRequest
	 * An object with parameters passed to the function.
	 *
	 * @return An object with the output parameters.
	 * @review
	 */
	public DEDataDefinitionRuleFunctionApplyResponse apply(
		DEDataDefinitionRuleFunctionApplyRequest
			deDataDefinitionRuleFunctionApplyRequest);

}