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

import com.liferay.data.engine.exception.DEDataDefinitionDeserializerException;
import com.liferay.data.engine.io.DEDataDefinitionDeserializer;
import com.liferay.data.engine.io.DEDataDefinitionDeserializerApplyRequest;
import com.liferay.data.engine.io.DEDataDefinitionDeserializerApplyResponse;
import com.liferay.data.engine.model.DEDataDefinition;
import com.liferay.data.engine.model.DEDataDefinitionField;
import com.liferay.data.engine.model.DEDataDefinitionRule;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "data.definition.deserializer.type=json",
	service = DEDataDefinitionDeserializer.class
)
public class DEDataDefinitionJSONDeserializer
	implements DEDataDefinitionDeserializer {

	@Override
	public DEDataDefinitionDeserializerApplyResponse apply(
			DEDataDefinitionDeserializerApplyRequest
				deDataDefinitionDeserializerApplyRequest)
		throws DEDataDefinitionDeserializerException {

		DEDataDefinition deDataDefinition = new DEDataDefinition();

		try {
			JSONObject jsonObject = jsonFactory.createJSONObject(
				deDataDefinitionDeserializerApplyRequest.getContent());

			deDataDefinition.setDEDataDefinitionFields(
				getDEDataDefinitionFields(jsonObject.getJSONArray("fields")));

			if (jsonObject.has("rules")) {
				deDataDefinition.setDEDataDefinitionRules(
					getDEDataDefinitionRules(jsonObject.getJSONArray("rules")));
			}

			return DEDataDefinitionDeserializerApplyResponse.Builder.of(
				deDataDefinition);
		}
		catch (JSONException jsone) {
			throw new DEDataDefinitionDeserializerException(
				"Invalid JSON format");
		}
		catch (DEDataDefinitionDeserializerException deddde) {
			throw deddde;
		}
	}

	protected DEDataDefinitionField getDEDataDefinitionField(
			JSONObject jsonObject)
		throws DEDataDefinitionDeserializerException {

		Map<String, String> labels = new TreeMap<>();

		if (jsonObject.has("label")) {
			JSONObject labelJSONObject = jsonObject.getJSONObject("label");

			if (labelJSONObject == null) {
				throw new DEDataDefinitionDeserializerException(
					"Label property must contain localized values");
			}

			Iterator<String> keys = labelJSONObject.keys();

			while (keys.hasNext()) {
				String key = keys.next();

				labels.put(key, labelJSONObject.getString(key));
			}
		}

		if (!jsonObject.has("name")) {
			throw new DEDataDefinitionDeserializerException(
				"Name property is required");
		}

		if (!jsonObject.has("type")) {
			throw new DEDataDefinitionDeserializerException(
				"Type property is required");
		}

		Map<String, String> tips = new TreeMap<>();

		if (jsonObject.has("tip")) {
			JSONObject tipJSONObject = jsonObject.getJSONObject("tip");

			if (tipJSONObject == null) {
				throw new DEDataDefinitionDeserializerException(
					"Tip property must contain localized values");
			}

			Iterator<String> keys = tipJSONObject.keys();

			while (keys.hasNext()) {
				String key = keys.next();

				tips.put(key, tipJSONObject.getString(key));
			}
		}

		DEDataDefinitionField deDataDefinitionField = new DEDataDefinitionField(
			jsonObject.getString("name"), jsonObject.getString("type"));

		deDataDefinitionField.setDefaultValue(jsonObject.get("defaultValue"));
		deDataDefinitionField.setIndexable(
			jsonObject.getBoolean("indexable", true));
		deDataDefinitionField.addLabels(labels);
		deDataDefinitionField.setLocalizable(
			jsonObject.getBoolean("localizable", false));
		deDataDefinitionField.setRepeatable(
			jsonObject.getBoolean("repeatable", false));
		deDataDefinitionField.addTips(tips);

		return deDataDefinitionField;
	}

	protected List<String> getDEDataDefinitionFieldNames(JSONArray jsonArray) {
		List<String> fieldNames = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			fieldNames.add(jsonArray.getString(i));
		}

		return fieldNames;
	}

	protected List<DEDataDefinitionField> getDEDataDefinitionFields(
			JSONArray jsonArray)
		throws DEDataDefinitionDeserializerException {

		List<DEDataDefinitionField> deDataDefinitionFields = new ArrayList<>(
			jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			DEDataDefinitionField deDataDefinitionField =
				getDEDataDefinitionField(jsonArray.getJSONObject(i));

			deDataDefinitionFields.add(deDataDefinitionField);
		}

		return deDataDefinitionFields;
	}

	protected DEDataDefinitionRule getDEDataDefinitionRule(
		JSONObject jsonObject) {

		DEDataDefinitionRule deDataDefinitionRule = new DEDataDefinitionRule(
			jsonObject.getString("name"), jsonObject.getString("ruleType"),
			getDEDataDefinitionFieldNames(jsonObject.getJSONArray("fields")));

		if (jsonObject.has("parameters")) {
			JSONObject parametersJSONObject = jsonObject.getJSONObject(
				"parameters");

			Iterator<String> keys = parametersJSONObject.keys();

			while (keys.hasNext()) {
				String key = keys.next();

				deDataDefinitionRule.addParameter(
					key, parametersJSONObject.get(key));
			}
		}

		return deDataDefinitionRule;
	}

	protected List<DEDataDefinitionRule> getDEDataDefinitionRules(
		JSONArray jsonArray) {

		List<DEDataDefinitionRule> deDataDefinitionRules = new ArrayList<>(
			jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			DEDataDefinitionRule deDataDefinitionRule = getDEDataDefinitionRule(
				jsonArray.getJSONObject(i));

			deDataDefinitionRules.add(deDataDefinitionRule);
		}

		return deDataDefinitionRules;
	}

	@Reference
	protected JSONFactory jsonFactory;

}