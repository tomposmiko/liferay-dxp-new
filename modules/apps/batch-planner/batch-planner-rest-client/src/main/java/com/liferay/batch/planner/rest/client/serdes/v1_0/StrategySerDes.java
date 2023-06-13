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

package com.liferay.batch.planner.rest.client.serdes.v1_0;

import com.liferay.batch.planner.rest.client.dto.v1_0.Strategy;
import com.liferay.batch.planner.rest.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Matija Petanjek
 * @generated
 */
@Generated("")
public class StrategySerDes {

	public static Strategy toDTO(String json) {
		StrategyJSONParser strategyJSONParser = new StrategyJSONParser();

		return strategyJSONParser.parseToDTO(json);
	}

	public static Strategy[] toDTOs(String json) {
		StrategyJSONParser strategyJSONParser = new StrategyJSONParser();

		return strategyJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Strategy strategy) {
		if (strategy == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (strategy.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(strategy.getName()));

			sb.append("\"");
		}

		if (strategy.getType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"type\": ");

			sb.append("\"");

			sb.append(_escape(strategy.getType()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		StrategyJSONParser strategyJSONParser = new StrategyJSONParser();

		return strategyJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Strategy strategy) {
		if (strategy == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (strategy.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(strategy.getName()));
		}

		if (strategy.getType() == null) {
			map.put("type", null);
		}
		else {
			map.put("type", String.valueOf(strategy.getType()));
		}

		return map;
	}

	public static class StrategyJSONParser extends BaseJSONParser<Strategy> {

		@Override
		protected Strategy createDTO() {
			return new Strategy();
		}

		@Override
		protected Strategy[] createDTOArray(int size) {
			return new Strategy[size];
		}

		@Override
		protected void setField(
			Strategy strategy, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					strategy.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "type")) {
				if (jsonParserFieldValue != null) {
					strategy.setType((String)jsonParserFieldValue);
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