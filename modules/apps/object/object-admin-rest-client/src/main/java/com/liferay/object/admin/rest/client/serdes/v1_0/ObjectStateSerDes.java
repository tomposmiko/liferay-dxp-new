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

import com.liferay.object.admin.rest.client.dto.v1_0.NextObjectState;
import com.liferay.object.admin.rest.client.dto.v1_0.ObjectState;
import com.liferay.object.admin.rest.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class ObjectStateSerDes {

	public static ObjectState toDTO(String json) {
		ObjectStateJSONParser objectStateJSONParser =
			new ObjectStateJSONParser();

		return objectStateJSONParser.parseToDTO(json);
	}

	public static ObjectState[] toDTOs(String json) {
		ObjectStateJSONParser objectStateJSONParser =
			new ObjectStateJSONParser();

		return objectStateJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ObjectState objectState) {
		if (objectState == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (objectState.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(objectState.getId());
		}

		if (objectState.getListTypeEntryId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"listTypeEntryId\": ");

			sb.append(objectState.getListTypeEntryId());
		}

		if (objectState.getNextObjectStates() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"nextObjectStates\": ");

			sb.append("[");

			for (int i = 0; i < objectState.getNextObjectStates().length; i++) {
				sb.append(String.valueOf(objectState.getNextObjectStates()[i]));

				if ((i + 1) < objectState.getNextObjectStates().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ObjectStateJSONParser objectStateJSONParser =
			new ObjectStateJSONParser();

		return objectStateJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(ObjectState objectState) {
		if (objectState == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (objectState.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(objectState.getId()));
		}

		if (objectState.getListTypeEntryId() == null) {
			map.put("listTypeEntryId", null);
		}
		else {
			map.put(
				"listTypeEntryId",
				String.valueOf(objectState.getListTypeEntryId()));
		}

		if (objectState.getNextObjectStates() == null) {
			map.put("nextObjectStates", null);
		}
		else {
			map.put(
				"nextObjectStates",
				String.valueOf(objectState.getNextObjectStates()));
		}

		return map;
	}

	public static class ObjectStateJSONParser
		extends BaseJSONParser<ObjectState> {

		@Override
		protected ObjectState createDTO() {
			return new ObjectState();
		}

		@Override
		protected ObjectState[] createDTOArray(int size) {
			return new ObjectState[size];
		}

		@Override
		protected void setField(
			ObjectState objectState, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					objectState.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "listTypeEntryId")) {
				if (jsonParserFieldValue != null) {
					objectState.setListTypeEntryId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "nextObjectStates")) {
				if (jsonParserFieldValue != null) {
					objectState.setNextObjectStates(
						Stream.of(
							toStrings((Object[])jsonParserFieldValue)
						).map(
							object -> NextObjectStateSerDes.toDTO(
								(String)object)
						).toArray(
							size -> new NextObjectState[size]
						));
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