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

package com.liferay.object.admin.rest.client.serdes.v1_0;

import com.liferay.object.admin.rest.client.dto.v1_0.ObjectState;
import com.liferay.object.admin.rest.client.dto.v1_0.ObjectStateFlow;
import com.liferay.object.admin.rest.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class ObjectStateFlowSerDes {

	public static ObjectStateFlow toDTO(String json) {
		ObjectStateFlowJSONParser objectStateFlowJSONParser =
			new ObjectStateFlowJSONParser();

		return objectStateFlowJSONParser.parseToDTO(json);
	}

	public static ObjectStateFlow[] toDTOs(String json) {
		ObjectStateFlowJSONParser objectStateFlowJSONParser =
			new ObjectStateFlowJSONParser();

		return objectStateFlowJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ObjectStateFlow objectStateFlow) {
		if (objectStateFlow == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (objectStateFlow.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(objectStateFlow.getId());
		}

		if (objectStateFlow.getObjectStates() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"objectStates\": ");

			sb.append("[");

			for (int i = 0; i < objectStateFlow.getObjectStates().length; i++) {
				sb.append(String.valueOf(objectStateFlow.getObjectStates()[i]));

				if ((i + 1) < objectStateFlow.getObjectStates().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ObjectStateFlowJSONParser objectStateFlowJSONParser =
			new ObjectStateFlowJSONParser();

		return objectStateFlowJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(ObjectStateFlow objectStateFlow) {
		if (objectStateFlow == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (objectStateFlow.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(objectStateFlow.getId()));
		}

		if (objectStateFlow.getObjectStates() == null) {
			map.put("objectStates", null);
		}
		else {
			map.put(
				"objectStates",
				String.valueOf(objectStateFlow.getObjectStates()));
		}

		return map;
	}

	public static class ObjectStateFlowJSONParser
		extends BaseJSONParser<ObjectStateFlow> {

		@Override
		protected ObjectStateFlow createDTO() {
			return new ObjectStateFlow();
		}

		@Override
		protected ObjectStateFlow[] createDTOArray(int size) {
			return new ObjectStateFlow[size];
		}

		@Override
		protected void setField(
			ObjectStateFlow objectStateFlow, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					objectStateFlow.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "objectStates")) {
				if (jsonParserFieldValue != null) {
					Object[] jsonParserFieldValues =
						(Object[])jsonParserFieldValue;

					ObjectState[] objectStatesArray =
						new ObjectState[jsonParserFieldValues.length];

					for (int i = 0; i < objectStatesArray.length; i++) {
						objectStatesArray[i] = ObjectStateSerDes.toDTO(
							(String)jsonParserFieldValues[i]);
					}

					objectStateFlow.setObjectStates(objectStatesArray);
				}
			}
		}

	}

	private static String _escape(Object object) {
		String string = String.valueOf(object);

		for (String[] strings : BaseJSONParser.JSON_ESCAPE_STRINGS) {
			string = string.replace(strings[0], strings[1]);
		}

		return string;
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
			sb.append("\": ");

			Object value = entry.getValue();

			Class<?> valueClass = value.getClass();

			if (value instanceof Map) {
				sb.append(_toJSON((Map)value));
			}
			else if (valueClass.isArray()) {
				Object[] values = (Object[])value;

				sb.append("[");

				for (int i = 0; i < values.length; i++) {
					sb.append("\"");
					sb.append(_escape(values[i]));
					sb.append("\"");

					if ((i + 1) < values.length) {
						sb.append(", ");
					}
				}

				sb.append("]");
			}
			else if (value instanceof String) {
				sb.append("\"");
				sb.append(_escape(entry.getValue()));
				sb.append("\"");
			}
			else {
				sb.append(String.valueOf(entry.getValue()));
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("}");

		return sb.toString();
	}

}