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

package com.liferay.data.engine.rest.client.dto.v1_0;

import com.liferay.data.engine.rest.client.function.UnsafeSupplier;

import javax.annotation.Generated;

/**
 * @author Jeyvison Nascimento
 * @generated
 */
@Generated("")
public class DataDefinitionRule {

	public String[] getDataDefinitionFieldNames() {
		return dataDefinitionFieldNames;
	}

	public void setDataDefinitionFieldNames(String[] dataDefinitionFieldNames) {
		this.dataDefinitionFieldNames = dataDefinitionFieldNames;
	}

	public void setDataDefinitionFieldNames(
		UnsafeSupplier<String[], Exception>
			dataDefinitionFieldNamesUnsafeSupplier) {

		try {
			dataDefinitionFieldNames =
				dataDefinitionFieldNamesUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String[] dataDefinitionFieldNames;

	public DataDefinitionRuleParameter[] getDataDefinitionRuleParameters() {
		return dataDefinitionRuleParameters;
	}

	public void setDataDefinitionRuleParameters(
		DataDefinitionRuleParameter[] dataDefinitionRuleParameters) {

		this.dataDefinitionRuleParameters = dataDefinitionRuleParameters;
	}

	public void setDataDefinitionRuleParameters(
		UnsafeSupplier<DataDefinitionRuleParameter[], Exception>
			dataDefinitionRuleParametersUnsafeSupplier) {

		try {
			dataDefinitionRuleParameters =
				dataDefinitionRuleParametersUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected DataDefinitionRuleParameter[] dataDefinitionRuleParameters;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setName(UnsafeSupplier<String, Exception> nameUnsafeSupplier) {
		try {
			name = nameUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String name;

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public void setRuleType(
		UnsafeSupplier<String, Exception> ruleTypeUnsafeSupplier) {

		try {
			ruleType = ruleTypeUnsafeSupplier.get();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String ruleType;

}