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

package com.liferay.bulk.rest.client.serdes.v1_0;

import com.liferay.bulk.rest.client.dto.v1_0.GenericError;
import com.liferay.bulk.rest.client.json.BaseJSONParser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Generated;

/**
 * @author Alejandro Tardín
 * @generated
 */
@Generated("")
public class GenericErrorSerDes {

	public static GenericError toDTO(String json) {
		GenericErrorJSONParser genericErrorJSONParser =
			new GenericErrorJSONParser();

		return genericErrorJSONParser.parseToDTO(json);
	}

	public static GenericError[] toDTOs(String json) {
		GenericErrorJSONParser genericErrorJSONParser =
			new GenericErrorJSONParser();

		return genericErrorJSONParser.parseToDTOs(json);
	}

	public static String toJSON(GenericError genericError) {
		if (genericError == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (genericError.getMessage() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"message\": ");

			sb.append("\"");

			sb.append(_escape(genericError.getMessage()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		GenericErrorJSONParser genericErrorJSONParser =
			new GenericErrorJSONParser();

		return genericErrorJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(GenericError genericError) {
		if (genericError == null) {
			return null;
		}

		Map<String, String> map = new HashMap<>();

		if (genericError.getMessage() == null) {
			map.put("message", null);
		}
		else {
			map.put("message", String.valueOf(genericError.getMessage()));
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

	private static class GenericErrorJSONParser
		extends BaseJSONParser<GenericError> {

		@Override
		protected GenericError createDTO() {
			return new GenericError();
		}

		@Override
		protected GenericError[] createDTOArray(int size) {
			return new GenericError[size];
		}

		@Override
		protected void setField(
			GenericError genericError, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "message")) {
				if (jsonParserFieldValue != null) {
					genericError.setMessage((String)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}