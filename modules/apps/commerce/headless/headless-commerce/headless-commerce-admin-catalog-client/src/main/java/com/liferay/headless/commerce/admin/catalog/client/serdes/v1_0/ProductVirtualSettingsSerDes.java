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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.ProductVirtualSettings;
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
public class ProductVirtualSettingsSerDes {

	public static ProductVirtualSettings toDTO(String json) {
		ProductVirtualSettingsJSONParser productVirtualSettingsJSONParser =
			new ProductVirtualSettingsJSONParser();

		return productVirtualSettingsJSONParser.parseToDTO(json);
	}

	public static ProductVirtualSettings[] toDTOs(String json) {
		ProductVirtualSettingsJSONParser productVirtualSettingsJSONParser =
			new ProductVirtualSettingsJSONParser();

		return productVirtualSettingsJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ProductVirtualSettings productVirtualSettings) {
		if (productVirtualSettings == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (productVirtualSettings.getActivationStatus() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"activationStatus\": ");

			sb.append(productVirtualSettings.getActivationStatus());
		}

		if (productVirtualSettings.getActivationStatusInfo() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"activationStatusInfo\": ");

			sb.append(
				String.valueOf(
					productVirtualSettings.getActivationStatusInfo()));
		}

		if (productVirtualSettings.getAttachment() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"attachment\": ");

			sb.append("\"");

			sb.append(_escape(productVirtualSettings.getAttachment()));

			sb.append("\"");
		}

		if (productVirtualSettings.getDuration() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"duration\": ");

			sb.append(productVirtualSettings.getDuration());
		}

		if (productVirtualSettings.getMaxUsages() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"maxUsages\": ");

			sb.append(productVirtualSettings.getMaxUsages());
		}

		if (productVirtualSettings.getSampleAttachment() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"sampleAttachment\": ");

			sb.append("\"");

			sb.append(_escape(productVirtualSettings.getSampleAttachment()));

			sb.append("\"");
		}

		if (productVirtualSettings.getSampleSrc() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"sampleSrc\": ");

			sb.append("\"");

			sb.append(_escape(productVirtualSettings.getSampleSrc()));

			sb.append("\"");
		}

		if (productVirtualSettings.getSampleUrl() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"sampleUrl\": ");

			sb.append("\"");

			sb.append(_escape(productVirtualSettings.getSampleUrl()));

			sb.append("\"");
		}

		if (productVirtualSettings.getSrc() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"src\": ");

			sb.append("\"");

			sb.append(_escape(productVirtualSettings.getSrc()));

			sb.append("\"");
		}

		if (productVirtualSettings.getTermsOfUseContent() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"termsOfUseContent\": ");

			sb.append(_toJSON(productVirtualSettings.getTermsOfUseContent()));
		}

		if (productVirtualSettings.getTermsOfUseJournalArticleId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"termsOfUseJournalArticleId\": ");

			sb.append(productVirtualSettings.getTermsOfUseJournalArticleId());
		}

		if (productVirtualSettings.getTermsOfUseRequired() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"termsOfUseRequired\": ");

			sb.append(productVirtualSettings.getTermsOfUseRequired());
		}

		if (productVirtualSettings.getUrl() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"url\": ");

			sb.append("\"");

			sb.append(_escape(productVirtualSettings.getUrl()));

			sb.append("\"");
		}

		if (productVirtualSettings.getUseSample() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"useSample\": ");

			sb.append(productVirtualSettings.getUseSample());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ProductVirtualSettingsJSONParser productVirtualSettingsJSONParser =
			new ProductVirtualSettingsJSONParser();

		return productVirtualSettingsJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		ProductVirtualSettings productVirtualSettings) {

		if (productVirtualSettings == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (productVirtualSettings.getActivationStatus() == null) {
			map.put("activationStatus", null);
		}
		else {
			map.put(
				"activationStatus",
				String.valueOf(productVirtualSettings.getActivationStatus()));
		}

		if (productVirtualSettings.getActivationStatusInfo() == null) {
			map.put("activationStatusInfo", null);
		}
		else {
			map.put(
				"activationStatusInfo",
				String.valueOf(
					productVirtualSettings.getActivationStatusInfo()));
		}

		if (productVirtualSettings.getAttachment() == null) {
			map.put("attachment", null);
		}
		else {
			map.put(
				"attachment",
				String.valueOf(productVirtualSettings.getAttachment()));
		}

		if (productVirtualSettings.getDuration() == null) {
			map.put("duration", null);
		}
		else {
			map.put(
				"duration",
				String.valueOf(productVirtualSettings.getDuration()));
		}

		if (productVirtualSettings.getMaxUsages() == null) {
			map.put("maxUsages", null);
		}
		else {
			map.put(
				"maxUsages",
				String.valueOf(productVirtualSettings.getMaxUsages()));
		}

		if (productVirtualSettings.getSampleAttachment() == null) {
			map.put("sampleAttachment", null);
		}
		else {
			map.put(
				"sampleAttachment",
				String.valueOf(productVirtualSettings.getSampleAttachment()));
		}

		if (productVirtualSettings.getSampleSrc() == null) {
			map.put("sampleSrc", null);
		}
		else {
			map.put(
				"sampleSrc",
				String.valueOf(productVirtualSettings.getSampleSrc()));
		}

		if (productVirtualSettings.getSampleUrl() == null) {
			map.put("sampleUrl", null);
		}
		else {
			map.put(
				"sampleUrl",
				String.valueOf(productVirtualSettings.getSampleUrl()));
		}

		if (productVirtualSettings.getSrc() == null) {
			map.put("src", null);
		}
		else {
			map.put("src", String.valueOf(productVirtualSettings.getSrc()));
		}

		if (productVirtualSettings.getTermsOfUseContent() == null) {
			map.put("termsOfUseContent", null);
		}
		else {
			map.put(
				"termsOfUseContent",
				String.valueOf(productVirtualSettings.getTermsOfUseContent()));
		}

		if (productVirtualSettings.getTermsOfUseJournalArticleId() == null) {
			map.put("termsOfUseJournalArticleId", null);
		}
		else {
			map.put(
				"termsOfUseJournalArticleId",
				String.valueOf(
					productVirtualSettings.getTermsOfUseJournalArticleId()));
		}

		if (productVirtualSettings.getTermsOfUseRequired() == null) {
			map.put("termsOfUseRequired", null);
		}
		else {
			map.put(
				"termsOfUseRequired",
				String.valueOf(productVirtualSettings.getTermsOfUseRequired()));
		}

		if (productVirtualSettings.getUrl() == null) {
			map.put("url", null);
		}
		else {
			map.put("url", String.valueOf(productVirtualSettings.getUrl()));
		}

		if (productVirtualSettings.getUseSample() == null) {
			map.put("useSample", null);
		}
		else {
			map.put(
				"useSample",
				String.valueOf(productVirtualSettings.getUseSample()));
		}

		return map;
	}

	public static class ProductVirtualSettingsJSONParser
		extends BaseJSONParser<ProductVirtualSettings> {

		@Override
		protected ProductVirtualSettings createDTO() {
			return new ProductVirtualSettings();
		}

		@Override
		protected ProductVirtualSettings[] createDTOArray(int size) {
			return new ProductVirtualSettings[size];
		}

		@Override
		protected void setField(
			ProductVirtualSettings productVirtualSettings,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "activationStatus")) {
				if (jsonParserFieldValue != null) {
					productVirtualSettings.setActivationStatus(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "activationStatusInfo")) {

				if (jsonParserFieldValue != null) {
					productVirtualSettings.setActivationStatusInfo(
						StatusSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "attachment")) {
				if (jsonParserFieldValue != null) {
					productVirtualSettings.setAttachment(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "duration")) {
				if (jsonParserFieldValue != null) {
					productVirtualSettings.setDuration(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "maxUsages")) {
				if (jsonParserFieldValue != null) {
					productVirtualSettings.setMaxUsages(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sampleAttachment")) {
				if (jsonParserFieldValue != null) {
					productVirtualSettings.setSampleAttachment(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sampleSrc")) {
				if (jsonParserFieldValue != null) {
					productVirtualSettings.setSampleSrc(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sampleUrl")) {
				if (jsonParserFieldValue != null) {
					productVirtualSettings.setSampleUrl(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "src")) {
				if (jsonParserFieldValue != null) {
					productVirtualSettings.setSrc((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "termsOfUseContent")) {
				if (jsonParserFieldValue != null) {
					productVirtualSettings.setTermsOfUseContent(
						(Map)ProductVirtualSettingsSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "termsOfUseJournalArticleId")) {

				if (jsonParserFieldValue != null) {
					productVirtualSettings.setTermsOfUseJournalArticleId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "termsOfUseRequired")) {

				if (jsonParserFieldValue != null) {
					productVirtualSettings.setTermsOfUseRequired(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "url")) {
				if (jsonParserFieldValue != null) {
					productVirtualSettings.setUrl((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "useSample")) {
				if (jsonParserFieldValue != null) {
					productVirtualSettings.setUseSample(
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