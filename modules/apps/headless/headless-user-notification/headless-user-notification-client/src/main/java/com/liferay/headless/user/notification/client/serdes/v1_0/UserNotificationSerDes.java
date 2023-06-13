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

package com.liferay.headless.user.notification.client.serdes.v1_0;

import com.liferay.headless.user.notification.client.dto.v1_0.UserNotification;
import com.liferay.headless.user.notification.client.json.BaseJSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Carlos Correa
 * @generated
 */
@Generated("")
public class UserNotificationSerDes {

	public static UserNotification toDTO(String json) {
		UserNotificationJSONParser userNotificationJSONParser =
			new UserNotificationJSONParser();

		return userNotificationJSONParser.parseToDTO(json);
	}

	public static UserNotification[] toDTOs(String json) {
		UserNotificationJSONParser userNotificationJSONParser =
			new UserNotificationJSONParser();

		return userNotificationJSONParser.parseToDTOs(json);
	}

	public static String toJSON(UserNotification userNotification) {
		if (userNotification == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (userNotification.getActions() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"actions\": ");

			sb.append(_toJSON(userNotification.getActions()));
		}

		if (userNotification.getDateCreated() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"dateCreated\": ");

			sb.append("\"");

			sb.append(
				liferayToJSONDateFormat.format(
					userNotification.getDateCreated()));

			sb.append("\"");
		}

		if (userNotification.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(userNotification.getId());
		}

		if (userNotification.getMessage() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"message\": ");

			sb.append("\"");

			sb.append(_escape(userNotification.getMessage()));

			sb.append("\"");
		}

		if (userNotification.getRead() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"read\": ");

			sb.append(userNotification.getRead());
		}

		if (userNotification.getType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"type\": ");

			sb.append(userNotification.getType());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		UserNotificationJSONParser userNotificationJSONParser =
			new UserNotificationJSONParser();

		return userNotificationJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(UserNotification userNotification) {
		if (userNotification == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (userNotification.getActions() == null) {
			map.put("actions", null);
		}
		else {
			map.put("actions", String.valueOf(userNotification.getActions()));
		}

		if (userNotification.getDateCreated() == null) {
			map.put("dateCreated", null);
		}
		else {
			map.put(
				"dateCreated",
				liferayToJSONDateFormat.format(
					userNotification.getDateCreated()));
		}

		if (userNotification.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(userNotification.getId()));
		}

		if (userNotification.getMessage() == null) {
			map.put("message", null);
		}
		else {
			map.put("message", String.valueOf(userNotification.getMessage()));
		}

		if (userNotification.getRead() == null) {
			map.put("read", null);
		}
		else {
			map.put("read", String.valueOf(userNotification.getRead()));
		}

		if (userNotification.getType() == null) {
			map.put("type", null);
		}
		else {
			map.put("type", String.valueOf(userNotification.getType()));
		}

		return map;
	}

	public static class UserNotificationJSONParser
		extends BaseJSONParser<UserNotification> {

		@Override
		protected UserNotification createDTO() {
			return new UserNotification();
		}

		@Override
		protected UserNotification[] createDTOArray(int size) {
			return new UserNotification[size];
		}

		@Override
		protected void setField(
			UserNotification userNotification, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "actions")) {
				if (jsonParserFieldValue != null) {
					userNotification.setActions(
						(Map)UserNotificationSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateCreated")) {
				if (jsonParserFieldValue != null) {
					userNotification.setDateCreated(
						toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					userNotification.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "message")) {
				if (jsonParserFieldValue != null) {
					userNotification.setMessage((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "read")) {
				if (jsonParserFieldValue != null) {
					userNotification.setRead((Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "type")) {
				if (jsonParserFieldValue != null) {
					userNotification.setType(
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