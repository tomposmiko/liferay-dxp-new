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

package com.liferay.data.engine.internal.rule;

import com.liferay.data.engine.rule.DEDataDefinitionRuleFunction;
import com.liferay.portal.kernel.util.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * This is a tracker to manage all available rule functions.
 *
 * @author Leonardo Barros
 */
@Component(
	immediate = true, service = DEDataDefinitionRuleFunctionTracker.class
)
public class DEDataDefinitionRuleFunctionTracker {

	public DEDataDefinitionRuleFunction getDEDataDefinitionRuleFunction(
		String name) {

		return _deDataDefinitionRuleFunctions.get(name);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected void addDEDataDefinitionRuleFunction(
		DEDataDefinitionRuleFunction deDataDefinitionRuleFunction,
		Map<String, Object> properties) {

		String name = MapUtil.getString(
			properties, "de.data.definition.rule.function.name");

		_deDataDefinitionRuleFunctions.put(name, deDataDefinitionRuleFunction);

		String type = MapUtil.getString(
			properties, "de.data.definition.rule.function.type");

		List<DEDataDefinitionRuleFunction> deDataDefinitionRuleFunctions =
			_deDataDefinitionRuleFunctionsByType.getOrDefault(
				type, new ArrayList<>());

		deDataDefinitionRuleFunctions.add(deDataDefinitionRuleFunction);

		_deDataDefinitionRuleFunctionsByType.put(
			name, deDataDefinitionRuleFunctions);
	}

	@Deactivate
	protected void deactivate() {
		_deDataDefinitionRuleFunctions.clear();
	}

	protected void removeDEDataDefinitionRuleFunction(
		DEDataDefinitionRuleFunction deDataDefinitionRuleFunction,
		Map<String, Object> properties) {

		String type = MapUtil.getString(
			properties, "de.data.definition.rule.function.type");

		List<DEDataDefinitionRuleFunction> deDataDefinitionRuleFunctions =
			_deDataDefinitionRuleFunctionsByType.get(type);

		if (deDataDefinitionRuleFunctions != null) {
			deDataDefinitionRuleFunctions.remove(deDataDefinitionRuleFunction);

			if (deDataDefinitionRuleFunctions.isEmpty()) {
				_deDataDefinitionRuleFunctionsByType.remove(type);
			}
		}
	}

	private final Map<String, DEDataDefinitionRuleFunction>
		_deDataDefinitionRuleFunctions = new TreeMap<>();
	private final Map<String, List<DEDataDefinitionRuleFunction>>
		_deDataDefinitionRuleFunctionsByType = new TreeMap<>();

}