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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * It represents a rule that may be applied to a data definition.
 *
 * @author Leonardo Barros
 * @see com.liferay.data.engine.rule.DEDataDefinitionRuleFunction
 * @review
 */
public final class DEDataDefinitionRule implements Serializable {

	/**
	 * Default constructor.
	 * @review
	 */
	public DEDataDefinitionRule() {
	}

	/**
	 * It creates a rule.
	 *
	 * @param name A name of rule function.
	 * The name must match the value of the property
	 * "de.data.definition.rule.function.name" of a registered OSGI
	 * component which implements {@link
	 * com.liferay.data.engine.rule.DEDataDefinitionRuleFunction}.
	 *
	 * @param ruleType The rule function type.
	 * The type must match the value of the property
	 * "de.data.definition.rule.function.type" of a registered OSGI
	 * component which implements {@link
	 * com.liferay.data.engine.rule.DEDataDefinitionRuleFunction}.
	 *
	 * @param deDataDefinitionFieldNames A list of data definition field's name.
	 * The rule must be applied to this list of fields.
	 * @review
	 */
	public DEDataDefinitionRule(
		String name, String ruleType, List<String> deDataDefinitionFieldNames) {

		_name = name;
		_ruleType = ruleType;

		setDEDataDefinitionFieldNames(deDataDefinitionFieldNames);
	}

	/**
	 * Add parameters to be used by the rule function.
	 *
	 * @param name The parameter's name.
	 * @param value The parameter's value.
	 * @review
	 */
	public void addParameter(String name, Object value) {
		_parameters.put(name, value);
	}

	/**
	 * @return A list of data definition field's name.
	 * @review
	 */
	public List<String> getDEDataDefinitionFieldNames() {
		return _deDataDefinitionFieldNames;
	}

	/**
	 * @return A name of the rule function.
	 * @review
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @return Additional parameters.
	 * @review
	 */
	public Map<String, Object> getParameters() {
		return Collections.unmodifiableMap(_parameters);
	}

	/**
	 * @return The rule type of the rule function
	 * @review
	 */
	public String getRuleType() {
		return _ruleType;
	}

	/**
	 * It sets a list of data definition field's name.
	 *
	 * @param deDataDefinitionFieldNames A list of data definition field's name.
	 * @review
	 */
	public void setDEDataDefinitionFieldNames(
		List<String> deDataDefinitionFieldNames) {

		_deDataDefinitionFieldNames = new ArrayList<>();

		if (deDataDefinitionFieldNames != null) {
			_deDataDefinitionFieldNames.addAll(deDataDefinitionFieldNames);
		}
	}

	/**
	 * It sets the name of the rule function to be used.
	 *
	 * @param name A name of a rule function.
	 * @review
	 */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * It sets the type of the rule function to be used.
	 *
	 * @param ruleType A type of a rule function
	 * @review
	 */
	public void setRuleType(String ruleType) {
		_ruleType = ruleType;
	}

	private List<String> _deDataDefinitionFieldNames = new ArrayList<>();
	private String _name;
	private Map<String, Object> _parameters = new HashMap<>();
	private String _ruleType;

}