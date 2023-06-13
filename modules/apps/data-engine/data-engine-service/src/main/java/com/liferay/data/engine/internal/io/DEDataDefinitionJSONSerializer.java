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

package com.liferay.data.engine.internal.io;

import com.liferay.data.engine.exception.DEDataDefinitionSerializerException;
import com.liferay.data.engine.io.DEDataDefinitionSerializer;
import com.liferay.data.engine.io.DEDataDefinitionSerializerApplyRequest;
import com.liferay.data.engine.io.DEDataDefinitionSerializerApplyResponse;
import com.liferay.data.engine.model.DEDataDefinition;
import com.liferay.data.engine.model.DEDataDefinitionField;
import com.liferay.data.engine.model.DEDataDefinitionRule;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "data.definition.serializer.type=json",
	service = DEDataDefinitionSerializer.class
)
public class DEDataDefinitionJSONSerializer
	implements DEDataDefinitionSerializer {

	@Override
	public DEDataDefinitionSerializerApplyResponse apply(
			DEDataDefinitionSerializerApplyRequest
				deDataDefinitionSerializerApplyRequest)
		throws DEDataDefinitionSerializerException {

		DEDataDefinition deDataDefinition =
			deDataDefinitionSerializerApplyRequest.getDEDataDefinition();

		JSONObject jsonObject = jsonFactory.createJSONObject();

		jsonObject.put(
			"fields",
			getDEDataDefinitionFieldsJSONArray(
				deDataDefinition.getDEDataDefinitionFields()));

		List<DEDataDefinitionRule> deDataDefinitionRules =
			deDataDefinition.getDEDataDefinitionRules();

		if (!deDataDefinitionRules.isEmpty()) {
			jsonObject.put(
				"rules",
				getDEDataDefinitionRulesJSONArray(deDataDefinitionRules));
		}

		return DEDataDefinitionSerializerApplyResponse.Builder.of(
			jsonObject.toJSONString());
	}

	protected JSONObject getDEDataDefinitionFieldJSONObject(
			DEDataDefinitionField deDataDefinitionField)
		throws DEDataDefinitionSerializerException {

		JSONObject jsonObject = jsonFactory.createJSONObject();

		Object defaultValue = deDataDefinitionField.getDefaultValue();

		if (defaultValue != null) {
			jsonObject.put("defaultValue", defaultValue);
		}

		jsonObject.put("indexable", deDataDefinitionField.isIndexable());

		Map<String, String> label = deDataDefinitionField.getLabel();

		if (!label.isEmpty()) {
			setProperty("label", jsonObject, label);
		}

		jsonObject.put("localizable", deDataDefinitionField.isLocalizable());

		String name = deDataDefinitionField.getName();

		if (Validator.isNull(name)) {
			throw new DEDataDefinitionSerializerException(
				"Name property is required");
		}

		jsonObject.put("name", name);

		jsonObject.put("repeatable", deDataDefinitionField.isRepeatable());

		Map<String, String> tip = deDataDefinitionField.getTip();

		if (!tip.isEmpty()) {
			setProperty("tip", jsonObject, tip);
		}

		String type = deDataDefinitionField.getType();

		if ((type == null) || type.isEmpty()) {
			throw new DEDataDefinitionSerializerException(
				"Type property is required");
		}

		jsonObject.put("type", type);

		return jsonObject;
	}

	protected JSONArray getDEDataDefinitionFieldNamesJSONArray(
		List<String> deDataDefinitionFieldNames) {

		JSONArray jsonArray = jsonFactory.createJSONArray();

		for (String deDataDefinitionFieldName : deDataDefinitionFieldNames) {
			jsonArray.put(deDataDefinitionFieldName);
		}

		return jsonArray;
	}

	protected JSONArray getDEDataDefinitionFieldsJSONArray(
			List<DEDataDefinitionField> deDataDefinitionFields)
		throws DEDataDefinitionSerializerException {

		JSONArray jsonArray = jsonFactory.createJSONArray();

		for (DEDataDefinitionField deDataDefinitionField :
				deDataDefinitionFields) {

			jsonArray.put(
				getDEDataDefinitionFieldJSONObject(deDataDefinitionField));
		}

		return jsonArray;
	}

	protected JSONObject getDEDataDefinitionRuleJSONObject(
		DEDataDefinitionRule deDataDefinitionRule) {

		JSONObject jsonObject = jsonFactory.createJSONObject();

		jsonObject.put(
			"fields",
			getDEDataDefinitionFieldNamesJSONArray(
				deDataDefinitionRule.getDEDataDefinitionFieldNames()));
		jsonObject.put("name", deDataDefinitionRule.getName());

		jsonObject.put("ruleType", deDataDefinitionRule.getRuleType());

		Map<String, Object> parameters = deDataDefinitionRule.getParameters();

		if (!parameters.isEmpty()) {
			JSONObject parametersJSONObject = jsonFactory.createJSONObject();

			jsonObject.put("parameters", parametersJSONObject);

			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				parametersJSONObject.put(entry.getKey(), entry.getValue());
			}
		}

		return jsonObject;
	}

	protected JSONArray getDEDataDefinitionRulesJSONArray(
		List<DEDataDefinitionRule> deDataDefinitionRules) {

		JSONArray jsonArray = jsonFactory.createJSONArray();

		Stream<DEDataDefinitionRule> stream = deDataDefinitionRules.stream();

		stream.map(
			this::getDEDataDefinitionRuleJSONObject
		).forEach(
			jsonArray::put
		);

		return jsonArray;
	}

	protected void setProperty(
		String propertyKey, JSONObject jsonObject, Map<String, String> map) {

		JSONObject languageJSONObject = jsonFactory.createJSONObject();

		Set<Map.Entry<String, String>> set = map.entrySet();

		Stream<Map.Entry<String, String>> stream = set.stream();

		stream.forEach(
			entry -> languageJSONObject.put(entry.getKey(), entry.getValue()));

		jsonObject.put(propertyKey, languageJSONObject);
	}

	@Reference
	protected JSONFactory jsonFactory;

}