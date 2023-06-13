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

package com.liferay.headless.delivery.client.serdes.v1_0;

import com.liferay.headless.delivery.client.dto.v1_0.EmptyCollectionConfig;
import com.liferay.headless.delivery.client.json.BaseJSONParser;

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
public class EmptyCollectionConfigSerDes {

	public static EmptyCollectionConfig toDTO(String json) {
		EmptyCollectionConfigJSONParser emptyCollectionConfigJSONParser =
			new EmptyCollectionConfigJSONParser();

		return emptyCollectionConfigJSONParser.parseToDTO(json);
	}

	public static EmptyCollectionConfig[] toDTOs(String json) {
		EmptyCollectionConfigJSONParser emptyCollectionConfigJSONParser =
			new EmptyCollectionConfigJSONParser();

		return emptyCollectionConfigJSONParser.parseToDTOs(json);
	}

	public static String toJSON(EmptyCollectionConfig emptyCollectionConfig) {
		if (emptyCollectionConfig == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (emptyCollectionConfig.getDisplayMessage() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"displayMessage\": ");

			sb.append(emptyCollectionConfig.getDisplayMessage());
		}

		if (emptyCollectionConfig.getMessage_i18n() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"message_i18n\": ");

			sb.append(_toJSON(emptyCollectionConfig.getMessage_i18n()));
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		EmptyCollectionConfigJSONParser emptyCollectionConfigJSONParser =
			new EmptyCollectionConfigJSONParser();

		return emptyCollectionConfigJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		EmptyCollectionConfig emptyCollectionConfig) {

		if (emptyCollectionConfig == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (emptyCollectionConfig.getDisplayMessage() == null) {
			map.put("displayMessage", null);
		}
		else {
			map.put(
				"displayMessage",
				String.valueOf(emptyCollectionConfig.getDisplayMessage()));
		}

		if (emptyCollectionConfig.getMessage_i18n() == null) {
			map.put("message_i18n", null);
		}
		else {
			map.put(
				"message_i18n",
				String.valueOf(emptyCollectionConfig.getMessage_i18n()));
		}

		return map;
	}

	public static class EmptyCollectionConfigJSONParser
		extends BaseJSONParser<EmptyCollectionConfig> {

		@Override
		protected EmptyCollectionConfig createDTO() {
			return new EmptyCollectionConfig();
		}

		@Override
		protected EmptyCollectionConfig[] createDTOArray(int size) {
			return new EmptyCollectionConfig[size];
		}

		@Override
		protected void setField(
			EmptyCollectionConfig emptyCollectionConfig,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "displayMessage")) {
				if (jsonParserFieldValue != null) {
					emptyCollectionConfig.setDisplayMessage(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "message_i18n")) {
				if (jsonParserFieldValue != null) {
					emptyCollectionConfig.setMessage_i18n(
						(Map)EmptyCollectionConfigSerDes.toMap(
							(String)jsonParserFieldValue));
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