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

package com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0;

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.SkuVirtualSettings;
import com.liferay.headless.commerce.admin.catalog.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
public class SkuVirtualSettingsSerDes {

	public static SkuVirtualSettings toDTO(String json) {
		SkuVirtualSettingsJSONParser skuVirtualSettingsJSONParser =
			new SkuVirtualSettingsJSONParser();

		return skuVirtualSettingsJSONParser.parseToDTO(json);
	}

	public static SkuVirtualSettings[] toDTOs(String json) {
		SkuVirtualSettingsJSONParser skuVirtualSettingsJSONParser =
			new SkuVirtualSettingsJSONParser();

		return skuVirtualSettingsJSONParser.parseToDTOs(json);
	}

	public static String toJSON(SkuVirtualSettings skuVirtualSettings) {
		if (skuVirtualSettings == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (skuVirtualSettings.getActivationStatus() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"activationStatus\": ");

			sb.append(skuVirtualSettings.getActivationStatus());
		}

		if (skuVirtualSettings.getActivationStatusInfo() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"activationStatusInfo\": ");

			sb.append(
				String.valueOf(skuVirtualSettings.getActivationStatusInfo()));
		}

		if (skuVirtualSettings.getAttachment() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"attachment\": ");

			sb.append("\"");

			sb.append(_escape(skuVirtualSettings.getAttachment()));

			sb.append("\"");
		}

		if (skuVirtualSettings.getDuration() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"duration\": ");

			sb.append(skuVirtualSettings.getDuration());
		}

		if (skuVirtualSettings.getMaxUsages() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"maxUsages\": ");

			sb.append(skuVirtualSettings.getMaxUsages());
		}

		if (skuVirtualSettings.getOverride() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"override\": ");

			sb.append(skuVirtualSettings.getOverride());
		}

		if (skuVirtualSettings.getSampleAttachment() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"sampleAttachment\": ");

			sb.append("\"");

			sb.append(_escape(skuVirtualSettings.getSampleAttachment()));

			sb.append("\"");
		}

		if (skuVirtualSettings.getSampleSrc() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"sampleSrc\": ");

			sb.append("\"");

			sb.append(_escape(skuVirtualSettings.getSampleSrc()));

			sb.append("\"");
		}

		if (skuVirtualSettings.getSampleUrl() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"sampleUrl\": ");

			sb.append("\"");

			sb.append(_escape(skuVirtualSettings.getSampleUrl()));

			sb.append("\"");
		}

		if (skuVirtualSettings.getSrc() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"src\": ");

			sb.append("\"");

			sb.append(_escape(skuVirtualSettings.getSrc()));

			sb.append("\"");
		}

		if (skuVirtualSettings.getTermsOfUseContent() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"termsOfUseContent\": ");

			sb.append(_toJSON(skuVirtualSettings.getTermsOfUseContent()));
		}

		if (skuVirtualSettings.getTermsOfUseJournalArticleId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"termsOfUseJournalArticleId\": ");

			sb.append(skuVirtualSettings.getTermsOfUseJournalArticleId());
		}

		if (skuVirtualSettings.getTermsOfUseRequired() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"termsOfUseRequired\": ");

			sb.append(skuVirtualSettings.getTermsOfUseRequired());
		}

		if (skuVirtualSettings.getUrl() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"url\": ");

			sb.append("\"");

			sb.append(_escape(skuVirtualSettings.getUrl()));

			sb.append("\"");
		}

		if (skuVirtualSettings.getUseSample() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"useSample\": ");

			sb.append(skuVirtualSettings.getUseSample());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		SkuVirtualSettingsJSONParser skuVirtualSettingsJSONParser =
			new SkuVirtualSettingsJSONParser();

		return skuVirtualSettingsJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		SkuVirtualSettings skuVirtualSettings) {

		if (skuVirtualSettings == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (skuVirtualSettings.getActivationStatus() == null) {
			map.put("activationStatus", null);
		}
		else {
			map.put(
				"activationStatus",
				String.valueOf(skuVirtualSettings.getActivationStatus()));
		}

		if (skuVirtualSettings.getActivationStatusInfo() == null) {
			map.put("activationStatusInfo", null);
		}
		else {
			map.put(
				"activationStatusInfo",
				String.valueOf(skuVirtualSettings.getActivationStatusInfo()));
		}

		if (skuVirtualSettings.getAttachment() == null) {
			map.put("attachment", null);
		}
		else {
			map.put(
				"attachment",
				String.valueOf(skuVirtualSettings.getAttachment()));
		}

		if (skuVirtualSettings.getDuration() == null) {
			map.put("duration", null);
		}
		else {
			map.put(
				"duration", String.valueOf(skuVirtualSettings.getDuration()));
		}

		if (skuVirtualSettings.getMaxUsages() == null) {
			map.put("maxUsages", null);
		}
		else {
			map.put(
				"maxUsages", String.valueOf(skuVirtualSettings.getMaxUsages()));
		}

		if (skuVirtualSettings.getOverride() == null) {
			map.put("override", null);
		}
		else {
			map.put(
				"override", String.valueOf(skuVirtualSettings.getOverride()));
		}

		if (skuVirtualSettings.getSampleAttachment() == null) {
			map.put("sampleAttachment", null);
		}
		else {
			map.put(
				"sampleAttachment",
				String.valueOf(skuVirtualSettings.getSampleAttachment()));
		}

		if (skuVirtualSettings.getSampleSrc() == null) {
			map.put("sampleSrc", null);
		}
		else {
			map.put(
				"sampleSrc", String.valueOf(skuVirtualSettings.getSampleSrc()));
		}

		if (skuVirtualSettings.getSampleUrl() == null) {
			map.put("sampleUrl", null);
		}
		else {
			map.put(
				"sampleUrl", String.valueOf(skuVirtualSettings.getSampleUrl()));
		}

		if (skuVirtualSettings.getSrc() == null) {
			map.put("src", null);
		}
		else {
			map.put("src", String.valueOf(skuVirtualSettings.getSrc()));
		}

		if (skuVirtualSettings.getTermsOfUseContent() == null) {
			map.put("termsOfUseContent", null);
		}
		else {
			map.put(
				"termsOfUseContent",
				String.valueOf(skuVirtualSettings.getTermsOfUseContent()));
		}

		if (skuVirtualSettings.getTermsOfUseJournalArticleId() == null) {
			map.put("termsOfUseJournalArticleId", null);
		}
		else {
			map.put(
				"termsOfUseJournalArticleId",
				String.valueOf(
					skuVirtualSettings.getTermsOfUseJournalArticleId()));
		}

		if (skuVirtualSettings.getTermsOfUseRequired() == null) {
			map.put("termsOfUseRequired", null);
		}
		else {
			map.put(
				"termsOfUseRequired",
				String.valueOf(skuVirtualSettings.getTermsOfUseRequired()));
		}

		if (skuVirtualSettings.getUrl() == null) {
			map.put("url", null);
		}
		else {
			map.put("url", String.valueOf(skuVirtualSettings.getUrl()));
		}

		if (skuVirtualSettings.getUseSample() == null) {
			map.put("useSample", null);
		}
		else {
			map.put(
				"useSample", String.valueOf(skuVirtualSettings.getUseSample()));
		}

		return map;
	}

	public static class SkuVirtualSettingsJSONParser
		extends BaseJSONParser<SkuVirtualSettings> {

		@Override
		protected SkuVirtualSettings createDTO() {
			return new SkuVirtualSettings();
		}

		@Override
		protected SkuVirtualSettings[] createDTOArray(int size) {
			return new SkuVirtualSettings[size];
		}

		@Override
		protected void setField(
			SkuVirtualSettings skuVirtualSettings, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "activationStatus")) {
				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setActivationStatus(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "activationStatusInfo")) {

				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setActivationStatusInfo(
						StatusSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "attachment")) {
				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setAttachment(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "duration")) {
				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setDuration(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "maxUsages")) {
				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setMaxUsages(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "override")) {
				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setOverride(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sampleAttachment")) {
				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setSampleAttachment(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sampleSrc")) {
				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setSampleSrc(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sampleUrl")) {
				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setSampleUrl(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "src")) {
				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setSrc((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "termsOfUseContent")) {
				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setTermsOfUseContent(
						(Map)SkuVirtualSettingsSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "termsOfUseJournalArticleId")) {

				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setTermsOfUseJournalArticleId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "termsOfUseRequired")) {

				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setTermsOfUseRequired(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "url")) {
				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setUrl((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "useSample")) {
				if (jsonParserFieldValue != null) {
					skuVirtualSettings.setUseSample(
						(Boolean)jsonParserFieldValue);
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