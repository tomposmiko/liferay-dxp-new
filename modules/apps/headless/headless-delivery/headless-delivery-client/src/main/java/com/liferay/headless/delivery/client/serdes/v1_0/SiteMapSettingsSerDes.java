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

import com.liferay.headless.delivery.client.dto.v1_0.SiteMapSettings;
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
public class SiteMapSettingsSerDes {

	public static SiteMapSettings toDTO(String json) {
		SiteMapSettingsJSONParser siteMapSettingsJSONParser =
			new SiteMapSettingsJSONParser();

		return siteMapSettingsJSONParser.parseToDTO(json);
	}

	public static SiteMapSettings[] toDTOs(String json) {
		SiteMapSettingsJSONParser siteMapSettingsJSONParser =
			new SiteMapSettingsJSONParser();

		return siteMapSettingsJSONParser.parseToDTOs(json);
	}

	public static String toJSON(SiteMapSettings siteMapSettings) {
		if (siteMapSettings == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (siteMapSettings.getChangeFrequency() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"changeFrequency\": ");

			sb.append("\"");

			sb.append(siteMapSettings.getChangeFrequency());

			sb.append("\"");
		}

		if (siteMapSettings.getInclude() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"include\": ");

			sb.append(siteMapSettings.getInclude());
		}

		if (siteMapSettings.getPagePriority() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"pagePriority\": ");

			sb.append(siteMapSettings.getPagePriority());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		SiteMapSettingsJSONParser siteMapSettingsJSONParser =
			new SiteMapSettingsJSONParser();

		return siteMapSettingsJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(SiteMapSettings siteMapSettings) {
		if (siteMapSettings == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (siteMapSettings.getChangeFrequency() == null) {
			map.put("changeFrequency", null);
		}
		else {
			map.put(
				"changeFrequency",
				String.valueOf(siteMapSettings.getChangeFrequency()));
		}

		if (siteMapSettings.getInclude() == null) {
			map.put("include", null);
		}
		else {
			map.put("include", String.valueOf(siteMapSettings.getInclude()));
		}

		if (siteMapSettings.getPagePriority() == null) {
			map.put("pagePriority", null);
		}
		else {
			map.put(
				"pagePriority",
				String.valueOf(siteMapSettings.getPagePriority()));
		}

		return map;
	}

	public static class SiteMapSettingsJSONParser
		extends BaseJSONParser<SiteMapSettings> {

		@Override
		protected SiteMapSettings createDTO() {
			return new SiteMapSettings();
		}

		@Override
		protected SiteMapSettings[] createDTOArray(int size) {
			return new SiteMapSettings[size];
		}

		@Override
		protected void setField(
			SiteMapSettings siteMapSettings, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "changeFrequency")) {
				if (jsonParserFieldValue != null) {
					siteMapSettings.setChangeFrequency(
						SiteMapSettings.ChangeFrequency.create(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "include")) {
				if (jsonParserFieldValue != null) {
					siteMapSettings.setInclude((Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "pagePriority")) {
				if (jsonParserFieldValue != null) {
					siteMapSettings.setPagePriority(
						Double.valueOf((String)jsonParserFieldValue));
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