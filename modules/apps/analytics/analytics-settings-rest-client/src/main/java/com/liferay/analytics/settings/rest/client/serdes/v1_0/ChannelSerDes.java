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

import com.liferay.analytics.settings.rest.client.dto.v1_0.Channel;
import com.liferay.analytics.settings.rest.client.dto.v1_0.DataSource;
import com.liferay.analytics.settings.rest.client.json.BaseJSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
public class ChannelSerDes {

	public static Channel toDTO(String json) {
		ChannelJSONParser channelJSONParser = new ChannelJSONParser();

		return channelJSONParser.parseToDTO(json);
	}

	public static Channel[] toDTOs(String json) {
		ChannelJSONParser channelJSONParser = new ChannelJSONParser();

		return channelJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Channel channel) {
		if (channel == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (channel.getChannelId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"channelId\": ");

			sb.append("\"");

			sb.append(_escape(channel.getChannelId()));

			sb.append("\"");
		}

		if (channel.getCommerceSyncEnabled() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"commerceSyncEnabled\": ");

			sb.append(channel.getCommerceSyncEnabled());
		}

		if (channel.getCreateDate() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"createDate\": ");

			sb.append("\"");

			sb.append(liferayToJSONDateFormat.format(channel.getCreateDate()));

			sb.append("\"");
		}

		if (channel.getDataSources() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"dataSources\": ");

			sb.append("[");

			for (int i = 0; i < channel.getDataSources().length; i++) {
				sb.append(String.valueOf(channel.getDataSources()[i]));

				if ((i + 1) < channel.getDataSources().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		if (channel.getName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"name\": ");

			sb.append("\"");

			sb.append(_escape(channel.getName()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ChannelJSONParser channelJSONParser = new ChannelJSONParser();

		return channelJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Channel channel) {
		if (channel == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (channel.getChannelId() == null) {
			map.put("channelId", null);
		}
		else {
			map.put("channelId", String.valueOf(channel.getChannelId()));
		}

		if (channel.getCommerceSyncEnabled() == null) {
			map.put("commerceSyncEnabled", null);
		}
		else {
			map.put(
				"commerceSyncEnabled",
				String.valueOf(channel.getCommerceSyncEnabled()));
		}

		if (channel.getCreateDate() == null) {
			map.put("createDate", null);
		}
		else {
			map.put(
				"createDate",
				liferayToJSONDateFormat.format(channel.getCreateDate()));
		}

		if (channel.getDataSources() == null) {
			map.put("dataSources", null);
		}
		else {
			map.put("dataSources", String.valueOf(channel.getDataSources()));
		}

		if (channel.getName() == null) {
			map.put("name", null);
		}
		else {
			map.put("name", String.valueOf(channel.getName()));
		}

		return map;
	}

	public static class ChannelJSONParser extends BaseJSONParser<Channel> {

		@Override
		protected Channel createDTO() {
			return new Channel();
		}

		@Override
		protected Channel[] createDTOArray(int size) {
			return new Channel[size];
		}

		@Override
		protected void setField(
			Channel channel, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "channelId")) {
				if (jsonParserFieldValue != null) {
					channel.setChannelId((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "commerceSyncEnabled")) {

				if (jsonParserFieldValue != null) {
					channel.setCommerceSyncEnabled(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "createDate")) {
				if (jsonParserFieldValue != null) {
					channel.setCreateDate(toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dataSources")) {
				if (jsonParserFieldValue != null) {
					Object[] jsonParserFieldValues =
						(Object[])jsonParserFieldValue;

					DataSource[] dataSourcesArray =
						new DataSource[jsonParserFieldValues.length];

					for (int i = 0; i < dataSourcesArray.length; i++) {
						dataSourcesArray[i] = DataSourceSerDes.toDTO(
							(String)jsonParserFieldValues[i]);
					}

					channel.setDataSources(dataSourcesArray);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					channel.setName((String)jsonParserFieldValue);
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