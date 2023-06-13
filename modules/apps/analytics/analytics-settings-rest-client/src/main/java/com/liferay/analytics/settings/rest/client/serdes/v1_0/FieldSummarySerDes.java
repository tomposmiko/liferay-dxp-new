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

package com.liferay.analytics.settings.rest.client.serdes.v1_0;

import com.liferay.analytics.settings.rest.client.dto.v1_0.FieldSummary;
import com.liferay.analytics.settings.rest.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Riccardo Ferrari
 * @generated
 */
@Generated("")
public class FieldSummarySerDes {

	public static FieldSummary toDTO(String json) {
		FieldSummaryJSONParser fieldSummaryJSONParser =
			new FieldSummaryJSONParser();

		return fieldSummaryJSONParser.parseToDTO(json);
	}

	public static FieldSummary[] toDTOs(String json) {
		FieldSummaryJSONParser fieldSummaryJSONParser =
			new FieldSummaryJSONParser();

		return fieldSummaryJSONParser.parseToDTOs(json);
	}

	public static String toJSON(FieldSummary fieldSummary) {
		if (fieldSummary == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (fieldSummary.getAccount() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"account\": ");

			sb.append(fieldSummary.getAccount());
		}

		if (fieldSummary.getOrder() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"order\": ");

			sb.append(fieldSummary.getOrder());
		}

		if (fieldSummary.getPeople() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"people\": ");

			sb.append(fieldSummary.getPeople());
		}

		if (fieldSummary.getProduct() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"product\": ");

			sb.append(fieldSummary.getProduct());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		FieldSummaryJSONParser fieldSummaryJSONParser =
			new FieldSummaryJSONParser();

		return fieldSummaryJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(FieldSummary fieldSummary) {
		if (fieldSummary == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (fieldSummary.getAccount() == null) {
			map.put("account", null);
		}
		else {
			map.put("account", String.valueOf(fieldSummary.getAccount()));
		}

		if (fieldSummary.getOrder() == null) {
			map.put("order", null);
		}
		else {
			map.put("order", String.valueOf(fieldSummary.getOrder()));
		}

		if (fieldSummary.getPeople() == null) {
			map.put("people", null);
		}
		else {
			map.put("people", String.valueOf(fieldSummary.getPeople()));
		}

		if (fieldSummary.getProduct() == null) {
			map.put("product", null);
		}
		else {
			map.put("product", String.valueOf(fieldSummary.getProduct()));
		}

		return map;
	}

	public static class FieldSummaryJSONParser
		extends BaseJSONParser<FieldSummary> {

		@Override
		protected FieldSummary createDTO() {
			return new FieldSummary();
		}

		@Override
		protected FieldSummary[] createDTOArray(int size) {
			return new FieldSummary[size];
		}

		@Override
		protected void setField(
			FieldSummary fieldSummary, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "account")) {
				if (jsonParserFieldValue != null) {
					fieldSummary.setAccount(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "order")) {
				if (jsonParserFieldValue != null) {
					fieldSummary.setOrder(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "people")) {
				if (jsonParserFieldValue != null) {
					fieldSummary.setPeople(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "product")) {
				if (jsonParserFieldValue != null) {
					fieldSummary.setProduct(
						Integer.valueOf((String)jsonParserFieldValue));
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