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

package com.liferay.data.engine.model;

import com.liferay.data.engine.constants.DEDataDefinitionRuleConstants;

import java.util.List;

/**
 * This is a factory to create rules to be added to a data
 * definition.
 *
 * @author Leonardo
 *
 * @review
 */
public class DEDataDefinitionRuleFactory {

	/**
	 * It creates a custom validation rule.
	 *
	 * @param name A name of validation rule function.
	 * The name must match the value of the property
	 * "de.data.definition.rule.function.name" of a registered OSGI
	 * component which implements {@link
	 * com.liferay.data.engine.rule.DEDataDefinitionRuleFunction}.
	 *
	 * @param deDataDefinitionFieldNames A list of data definition field's name.
	 * @return The rule function created.
	 * @review
	 */
	public static DEDataDefinitionRule customValidation(
		String name, List<String> deDataDefinitionFieldNames) {

		return new DEDataDefinitionRule(
			name, DEDataDefinitionRuleConstants.VALIDATION_RULE_TYPE,
			deDataDefinitionFieldNames);
	}

	/**
	 * It creates a validation rule to verify if a value is a decimal number.
	 *
	 * @param deDataDefinitionFieldNames A list of data definition field's name.
	 * @return The rule function created.
	 * @review
	 */
	public static DEDataDefinitionRule decimalLiteral(
		List<String> deDataDefinitionFieldNames) {

		return new DEDataDefinitionRule(
			DEDataDefinitionRuleConstants.DECIMAL_LITERAL_RULE,
			DEDataDefinitionRuleConstants.VALIDATION_RULE_TYPE,
			deDataDefinitionFieldNames);
	}

	/**
	 * It creates a validation rule to verify if a value is a valid email
	 * address.
	 *
	 * @param deDataDefinitionFieldNames A list of data definition field's name.
	 * @return The rule function created.
	 * @review
	 */
	public static DEDataDefinitionRule emailAddress(
		List<String> deDataDefinitionFieldNames) {

		return new DEDataDefinitionRule(
			DEDataDefinitionRuleConstants.EMAIL_ADDRESS_RULE,
			DEDataDefinitionRuleConstants.VALIDATION_RULE_TYPE,
			deDataDefinitionFieldNames);
	}

	/**
	 * It crated a validation rule to verify if a value is empty.
	 *
	 * @param deDataDefinitionFieldNames A list of data definition field's name.
	 * @return The rule function created.
	 * @review
	 */
	public static DEDataDefinitionRule empty(
		List<String> deDataDefinitionFieldNames) {

		return new DEDataDefinitionRule(
			DEDataDefinitionRuleConstants.EMPTY_RULE,
			DEDataDefinitionRuleConstants.VALIDATION_RULE_TYPE,
			deDataDefinitionFieldNames);
	}

	/**
	 * It creates a validation rule to verify if a value is an integer number.
	 * @param deDataDefinitionFieldNames A list of data definition field's name.
	 * @return The rule function created.
	 * @review
	 */
	public static DEDataDefinitionRule integerLiteral(
		List<String> deDataDefinitionFieldNames) {

		return new DEDataDefinitionRule(
			DEDataDefinitionRuleConstants.INTEGER_LITERAL_RULE,
			DEDataDefinitionRuleConstants.VALIDATION_RULE_TYPE,
			deDataDefinitionFieldNames);
	}

	/**
	 * It creates a validation rule to verify if a value matches a regular
	 * expression.
	 *
	 * @param expression A regular expression.
	 * @param deDataDefinitionFieldNames A list of data definition field's name.
	 * @return The rule function created.
	 * @review
	 */
	public static DEDataDefinitionRule matchExpression(
		String expression, List<String> deDataDefinitionFieldNames) {

		DEDataDefinitionRule deDataDefinitionValidationRule =
			new DEDataDefinitionRule(
				DEDataDefinitionRuleConstants.MATCH_EXPRESSION_RULE,
				DEDataDefinitionRuleConstants.VALIDATION_RULE_TYPE,
				deDataDefinitionFieldNames);

		deDataDefinitionValidationRule.addParameter(
			DEDataDefinitionRuleConstants.EXPRESSION_PARAMETER, expression);

		return deDataDefinitionValidationRule;
	}

	/**
	 * It creates a validation rule to verify if a value is not null or empty.
	 *
	 * @param deDataDefinitionFieldNames A list of data definition field's name.
	 * @return The rule function created.
	 * @review
	 */
	public static DEDataDefinitionRule required(
		List<String> deDataDefinitionFieldNames) {

		return new DEDataDefinitionRule(
			DEDataDefinitionRuleConstants.REQUIRED_RULE,
			DEDataDefinitionRuleConstants.VALIDATION_RULE_TYPE,
			deDataDefinitionFieldNames);
	}

	/**
	 * It creates a validation rule to verify if a value is a valid URL.
	 *
	 * @param deDataDefinitionFieldNames A list of data definition field's name.
	 * @return The rule function created.
	 * @review
	 */
	public static DEDataDefinitionRule url(
		List<String> deDataDefinitionFieldNames) {

		return new DEDataDefinitionRule(
			DEDataDefinitionRuleConstants.URL_RULE,
			DEDataDefinitionRuleConstants.VALIDATION_RULE_TYPE,
			deDataDefinitionFieldNames);
	}

}