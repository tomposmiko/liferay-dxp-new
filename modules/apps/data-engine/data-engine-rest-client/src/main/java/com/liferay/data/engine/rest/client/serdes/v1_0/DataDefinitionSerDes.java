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

package com.liferay.data.engine.rest.client.serdes.v1_0;

import com.liferay.data.engine.rest.client.dto.v1_0.DataDefinition;
import com.liferay.data.engine.rest.client.dto.v1_0.DataDefinitionField;
import com.liferay.data.engine.rest.client.dto.v1_0.DataDefinitionRule;
import com.liferay.data.engine.rest.client.json.BaseJSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Generated;

/**
 * @author Jeyvison Nascimento
 * @generated
 */
@Generated("")
public class DataDefinitionSerDes {

	public static DataDefinition toDTO(String json) {
		DataDefinitionJSONParser dataDefinitionJSONParser =
			new DataDefinitionJSONParser();

		return dataDefinitionJSONParser.parseToDTO(json);
	}

	public static DataDefinition[] toDTOs(String json) {
		DataDefinitionJSONParser dataDefinitionJSONParser =
			new DataDefinitionJSONParser();

		return dataDefinitionJSONParser.parseToDTOs(json);
	}

	public static String toJSON(DataDefinition dataDefinition) {
		if (dataDefinition == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

		if (dataDefinition.getDataDefinitionFields() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"dataDefinitionFields\": ");

			sb.append("[");

			for (int i = 0; i < dataDefinition.getDataDefinitionFields().length;
				 i++) {

				sb.append(
					String.valueOf(
						dataDefinition.getDataDefinitionFields()[i]));

				if ((i + 1) < dataDefinition.getDataDefinitionFields().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (dataDefinition.getDataDefinitionRules() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"dataDefinitionRules\": ");

			sb.append("[");

			for (int i = 0; i < dataDefinition.getDataDefinitionRules().length;
				 i++) {

				sb.append(
					String.valueOf(dataDefinition.getDataDefinitionRules()[i]));

				if ((i + 1) < dataDefinition.getDataDefinitionRules().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (dataDefinition.getDateCreated() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"dateCreated\": ");

			sb.append("\"");

			sb.append(
				liferayToJSONDateFormat.format(
					dataDefinition.getDateCreated()));

			sb.append("\"");
		}

		if (dataDefinition.getDateModified() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"dateModified\": ");

			sb.append("\"");

			sb.append(
				liferayToJSONDateFormat.format(
					dataDefinition.getDateModified()));

			sb.append("\"");
		}

		if (dataDefinition.getDescription() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"description\": ");

			sb.append(_toJSON(dataDefinition.getDescription()));
		}

		if (dataDefinition.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(dataDefinition.getId());
		}

		if (dataDefinition.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append(_toJSON(dataDefinition.getName()));
		}

		if (dataDefinition.getSiteId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"siteId\": ");

			sb.append(dataDefinition.getSiteId());
		}

		if (dataDefinition.getStorageType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"storageType\": ");

			sb.append("\"");

			sb.append(_escape(dataDefinition.getStorageType()));

			sb.append("\"");
		}

		if (dataDefinition.getUserId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"userId\": ");

			sb.append(dataDefinition.getUserId());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		DataDefinitionJSONParser dataDefinitionJSONParser =
			new DataDefinitionJSONParser();

		return dataDefinitionJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(DataDefinition dataDefinition) {
		if (dataDefinition == null) {
			return null;
		}

		Map<String, String> map = new HashMap<>();

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

		if (dataDefinition.getDataDefinitionFields() == null) {
			map.put("dataDefinitionFields", null);
		}
		else {
			map.put(
				"dataDefinitionFields",
				String.valueOf(dataDefinition.getDataDefinitionFields()));
		}

		if (dataDefinition.getDataDefinitionRules() == null) {
			map.put("dataDefinitionRules", null);
		}
		else {
			map.put(
				"dataDefinitionRules",
				String.valueOf(dataDefinition.getDataDefinitionRules()));
		}

		map.put(
			"dateCreated",
			liferayToJSONDateFormat.format(dataDefinition.getDateCreated()));

		map.put(
			"dateModified",
			liferayToJSONDateFormat.format(dataDefinition.getDateModified()));

		if (dataDefinition.getDescription() == null) {
			map.put("description", null);
		}
		else {
			map.put(
				"description", String.valueOf(dataDefinition.getDescription()));
		}

		if (dataDefinition.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(dataDefinition.getId()));
		}

		if (dataDefinition.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(dataDefinition.getName()));
		}

		if (dataDefinition.getSiteId() == null) {
			map.put("siteId", null);
		}
		else {
			map.put("siteId", String.valueOf(dataDefinition.getSiteId()));
		}

		if (dataDefinition.getStorageType() == null) {
			map.put("storageType", null);
		}
		else {
			map.put(
				"storageType", String.valueOf(dataDefinition.getStorageType()));
		}

		if (dataDefinition.getUserId() == null) {
			map.put("userId", null);
		}
		else {
			map.put("userId", String.valueOf(dataDefinition.getUserId()));
		}

		return map;
	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		string = string.replace("\\", "\\\\");

		return string.replace("\"", "\\\"");
	}

	private static String _toJSON(Map<String, ?> map) {
		StringBuilder sb = new StringBuilder("{");

		@SuppressWarnings("unchecked")
		Set set = map.entrySet();

		@SuppressWarnings("unchecked")
		Iterator<Map.Entry<String, ?>> iterator = set.iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, ?> entry = iterator.next();

			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\":");
			sb.append("\"");
			sb.append(entry.getValue());
			sb.append("\"");

			if (iterator.hasNext()) {
				sb.append(",");
			}
		}

		sb.append("}");

		return sb.toString();
	}

	private static class DataDefinitionJSONParser
		extends BaseJSONParser<DataDefinition> {

		@Override
		protected DataDefinition createDTO() {
			return new DataDefinition();
		}

		@Override
		protected DataDefinition[] createDTOArray(int size) {
			return new DataDefinition[size];
		}

		@Override
		protected void setField(
			DataDefinition dataDefinition, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "dataDefinitionFields")) {
				if (jsonParserFieldValue != null) {
					dataDefinition.setDataDefinitionFields(
						Stream.of(
							toStrings((Object[])jsonParserFieldValue)
						).map(
							object -> DataDefinitionFieldSerDes.toDTO(
								(String)object)
						).toArray(
							size -> new DataDefinitionField[size]
						));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "dataDefinitionRules")) {

				if (jsonParserFieldValue != null) {
					dataDefinition.setDataDefinitionRules(
						Stream.of(
							toStrings((Object[])jsonParserFieldValue)
						).map(
							object -> DataDefinitionRuleSerDes.toDTO(
								(String)object)
						).toArray(
							size -> new DataDefinitionRule[size]
						));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateCreated")) {
				if (jsonParserFieldValue != null) {
					dataDefinition.setDateCreated(
						toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateModified")) {
				if (jsonParserFieldValue != null) {
					dataDefinition.setDateModified(
						toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "description")) {
				if (jsonParserFieldValue != null) {
					dataDefinition.setDescription(
						(Map)DataDefinitionSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					dataDefinition.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					dataDefinition.setName(
						(Map)DataDefinitionSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "siteId")) {
				if (jsonParserFieldValue != null) {
					dataDefinition.setSiteId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "storageType")) {
				if (jsonParserFieldValue != null) {
					dataDefinition.setStorageType((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "userId")) {
				if (jsonParserFieldValue != null) {
					dataDefinition.setUserId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}