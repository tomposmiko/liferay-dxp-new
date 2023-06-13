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

import com.liferay.analytics.settings.rest.client.dto.v1_0.CommerceChannel;
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
public class CommerceChannelSerDes {

	public static CommerceChannel toDTO(String json) {
		CommerceChannelJSONParser commerceChannelJSONParser =
			new CommerceChannelJSONParser();

		return commerceChannelJSONParser.parseToDTO(json);
	}

	public static CommerceChannel[] toDTOs(String json) {
		CommerceChannelJSONParser commerceChannelJSONParser =
			new CommerceChannelJSONParser();

		return commerceChannelJSONParser.parseToDTOs(json);
	}

	public static String toJSON(CommerceChannel commerceChannel) {
		if (commerceChannel == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (commerceChannel.getChannelName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"channelName\": ");

			sb.append("\"");

			sb.append(_escape(commerceChannel.getChannelName()));

			sb.append("\"");
		}

		if (commerceChannel.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(commerceChannel.getId());
		}

		if (commerceChannel.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(commerceChannel.getName()));

			sb.append("\"");
		}

		if (commerceChannel.getSiteName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"siteName\": ");

			sb.append("\"");

			sb.append(_escape(commerceChannel.getSiteName()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		CommerceChannelJSONParser commerceChannelJSONParser =
			new CommerceChannelJSONParser();

		return commerceChannelJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(CommerceChannel commerceChannel) {
		if (commerceChannel == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (commerceChannel.getChannelName() == null) {
			map.put("channelName", null);
		}
		else {
			map.put(
				"channelName",
				String.valueOf(commerceChannel.getChannelName()));
		}

		if (commerceChannel.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(commerceChannel.getId()));
		}

		if (commerceChannel.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(commerceChannel.getName()));
		}

		if (commerceChannel.getSiteName() == null) {
			map.put("siteName", null);
		}
		else {
			map.put("siteName", String.valueOf(commerceChannel.getSiteName()));
		}

		return map;
	}

	public static class CommerceChannelJSONParser
		extends BaseJSONParser<CommerceChannel> {

		@Override
		protected CommerceChannel createDTO() {
			return new CommerceChannel();
		}

		@Override
		protected CommerceChannel[] createDTOArray(int size) {
			return new CommerceChannel[size];
		}

		@Override
		protected void setField(
			CommerceChannel commerceChannel, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "channelName")) {
				if (jsonParserFieldValue != null) {
					commerceChannel.setChannelName(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					commerceChannel.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					commerceChannel.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "siteName")) {
				if (jsonParserFieldValue != null) {
					commerceChannel.setSiteName((String)jsonParserFieldValue);
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