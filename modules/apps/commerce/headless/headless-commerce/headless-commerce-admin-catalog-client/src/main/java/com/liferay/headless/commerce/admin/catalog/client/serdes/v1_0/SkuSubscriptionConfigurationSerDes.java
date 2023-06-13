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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.SkuSubscriptionConfiguration;
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
public class SkuSubscriptionConfigurationSerDes {

	public static SkuSubscriptionConfiguration toDTO(String json) {
		SkuSubscriptionConfigurationJSONParser
			skuSubscriptionConfigurationJSONParser =
				new SkuSubscriptionConfigurationJSONParser();

		return skuSubscriptionConfigurationJSONParser.parseToDTO(json);
	}

	public static SkuSubscriptionConfiguration[] toDTOs(String json) {
		SkuSubscriptionConfigurationJSONParser
			skuSubscriptionConfigurationJSONParser =
				new SkuSubscriptionConfigurationJSONParser();

		return skuSubscriptionConfigurationJSONParser.parseToDTOs(json);
	}

	public static String toJSON(
		SkuSubscriptionConfiguration skuSubscriptionConfiguration) {

		if (skuSubscriptionConfiguration == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (skuSubscriptionConfiguration.getDeliverySubscriptionEnable() !=
				null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"deliverySubscriptionEnable\": ");

			sb.append(
				skuSubscriptionConfiguration.getDeliverySubscriptionEnable());
		}

		if (skuSubscriptionConfiguration.getDeliverySubscriptionLength() !=
				null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"deliverySubscriptionLength\": ");

			sb.append(
				skuSubscriptionConfiguration.getDeliverySubscriptionLength());
		}

		if (skuSubscriptionConfiguration.
				getDeliverySubscriptionNumberOfLength() != null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"deliverySubscriptionNumberOfLength\": ");

			sb.append(
				skuSubscriptionConfiguration.
					getDeliverySubscriptionNumberOfLength());
		}

		if (skuSubscriptionConfiguration.getDeliverySubscriptionType() !=
				null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"deliverySubscriptionType\": ");

			sb.append("\"");

			sb.append(
				skuSubscriptionConfiguration.getDeliverySubscriptionType());

			sb.append("\"");
		}

		if (skuSubscriptionConfiguration.
				getDeliverySubscriptionTypeSettings() != null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"deliverySubscriptionTypeSettings\": ");

			sb.append(
				_toJSON(
					skuSubscriptionConfiguration.
						getDeliverySubscriptionTypeSettings()));
		}

		if (skuSubscriptionConfiguration.getEnable() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"enable\": ");

			sb.append(skuSubscriptionConfiguration.getEnable());
		}

		if (skuSubscriptionConfiguration.getLength() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"length\": ");

			sb.append(skuSubscriptionConfiguration.getLength());
		}

		if (skuSubscriptionConfiguration.getNumberOfLength() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"numberOfLength\": ");

			sb.append(skuSubscriptionConfiguration.getNumberOfLength());
		}

		if (skuSubscriptionConfiguration.getOverrideSubscriptionInfo() !=
				null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"overrideSubscriptionInfo\": ");

			sb.append(
				skuSubscriptionConfiguration.getOverrideSubscriptionInfo());
		}

		if (skuSubscriptionConfiguration.getSubscriptionType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subscriptionType\": ");

			sb.append("\"");

			sb.append(skuSubscriptionConfiguration.getSubscriptionType());

			sb.append("\"");
		}

		if (skuSubscriptionConfiguration.getSubscriptionTypeSettings() !=
				null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subscriptionTypeSettings\": ");

			sb.append(
				_toJSON(
					skuSubscriptionConfiguration.
						getSubscriptionTypeSettings()));
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		SkuSubscriptionConfigurationJSONParser
			skuSubscriptionConfigurationJSONParser =
				new SkuSubscriptionConfigurationJSONParser();

		return skuSubscriptionConfigurationJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		SkuSubscriptionConfiguration skuSubscriptionConfiguration) {

		if (skuSubscriptionConfiguration == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (skuSubscriptionConfiguration.getDeliverySubscriptionEnable() ==
				null) {

			map.put("deliverySubscriptionEnable", null);
		}
		else {
			map.put(
				"deliverySubscriptionEnable",
				String.valueOf(
					skuSubscriptionConfiguration.
						getDeliverySubscriptionEnable()));
		}

		if (skuSubscriptionConfiguration.getDeliverySubscriptionLength() ==
				null) {

			map.put("deliverySubscriptionLength", null);
		}
		else {
			map.put(
				"deliverySubscriptionLength",
				String.valueOf(
					skuSubscriptionConfiguration.
						getDeliverySubscriptionLength()));
		}

		if (skuSubscriptionConfiguration.
				getDeliverySubscriptionNumberOfLength() == null) {

			map.put("deliverySubscriptionNumberOfLength", null);
		}
		else {
			map.put(
				"deliverySubscriptionNumberOfLength",
				String.valueOf(
					skuSubscriptionConfiguration.
						getDeliverySubscriptionNumberOfLength()));
		}

		if (skuSubscriptionConfiguration.getDeliverySubscriptionType() ==
				null) {

			map.put("deliverySubscriptionType", null);
		}
		else {
			map.put(
				"deliverySubscriptionType",
				String.valueOf(
					skuSubscriptionConfiguration.
						getDeliverySubscriptionType()));
		}

		if (skuSubscriptionConfiguration.
				getDeliverySubscriptionTypeSettings() == null) {

			map.put("deliverySubscriptionTypeSettings", null);
		}
		else {
			map.put(
				"deliverySubscriptionTypeSettings",
				String.valueOf(
					skuSubscriptionConfiguration.
						getDeliverySubscriptionTypeSettings()));
		}

		if (skuSubscriptionConfiguration.getEnable() == null) {
			map.put("enable", null);
		}
		else {
			map.put(
				"enable",
				String.valueOf(skuSubscriptionConfiguration.getEnable()));
		}

		if (skuSubscriptionConfiguration.getLength() == null) {
			map.put("length", null);
		}
		else {
			map.put(
				"length",
				String.valueOf(skuSubscriptionConfiguration.getLength()));
		}

		if (skuSubscriptionConfiguration.getNumberOfLength() == null) {
			map.put("numberOfLength", null);
		}
		else {
			map.put(
				"numberOfLength",
				String.valueOf(
					skuSubscriptionConfiguration.getNumberOfLength()));
		}

		if (skuSubscriptionConfiguration.getOverrideSubscriptionInfo() ==
				null) {

			map.put("overrideSubscriptionInfo", null);
		}
		else {
			map.put(
				"overrideSubscriptionInfo",
				String.valueOf(
					skuSubscriptionConfiguration.
						getOverrideSubscriptionInfo()));
		}

		if (skuSubscriptionConfiguration.getSubscriptionType() == null) {
			map.put("subscriptionType", null);
		}
		else {
			map.put(
				"subscriptionType",
				String.valueOf(
					skuSubscriptionConfiguration.getSubscriptionType()));
		}

		if (skuSubscriptionConfiguration.getSubscriptionTypeSettings() ==
				null) {

			map.put("subscriptionTypeSettings", null);
		}
		else {
			map.put(
				"subscriptionTypeSettings",
				String.valueOf(
					skuSubscriptionConfiguration.
						getSubscriptionTypeSettings()));
		}

		return map;
	}

	public static class SkuSubscriptionConfigurationJSONParser
		extends BaseJSONParser<SkuSubscriptionConfiguration> {

		@Override
		protected SkuSubscriptionConfiguration createDTO() {
			return new SkuSubscriptionConfiguration();
		}

		@Override
		protected SkuSubscriptionConfiguration[] createDTOArray(int size) {
			return new SkuSubscriptionConfiguration[size];
		}

		@Override
		protected void setField(
			SkuSubscriptionConfiguration skuSubscriptionConfiguration,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(
					jsonParserFieldName, "deliverySubscriptionEnable")) {

				if (jsonParserFieldValue != null) {
					skuSubscriptionConfiguration.setDeliverySubscriptionEnable(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "deliverySubscriptionLength")) {

				if (jsonParserFieldValue != null) {
					skuSubscriptionConfiguration.setDeliverySubscriptionLength(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName,
						"deliverySubscriptionNumberOfLength")) {

				if (jsonParserFieldValue != null) {
					skuSubscriptionConfiguration.
						setDeliverySubscriptionNumberOfLength(
							Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "deliverySubscriptionType")) {

				if (jsonParserFieldValue != null) {
					skuSubscriptionConfiguration.setDeliverySubscriptionType(
						SkuSubscriptionConfiguration.DeliverySubscriptionType.
							create((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName,
						"deliverySubscriptionTypeSettings")) {

				if (jsonParserFieldValue != null) {
					skuSubscriptionConfiguration.
						setDeliverySubscriptionTypeSettings(
							(Map)SkuSubscriptionConfigurationSerDes.toMap(
								(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "enable")) {
				if (jsonParserFieldValue != null) {
					skuSubscriptionConfiguration.setEnable(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "length")) {
				if (jsonParserFieldValue != null) {
					skuSubscriptionConfiguration.setLength(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "numberOfLength")) {
				if (jsonParserFieldValue != null) {
					skuSubscriptionConfiguration.setNumberOfLength(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "overrideSubscriptionInfo")) {

				if (jsonParserFieldValue != null) {
					skuSubscriptionConfiguration.setOverrideSubscriptionInfo(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "subscriptionType")) {
				if (jsonParserFieldValue != null) {
					skuSubscriptionConfiguration.setSubscriptionType(
						SkuSubscriptionConfiguration.SubscriptionType.create(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "subscriptionTypeSettings")) {

				if (jsonParserFieldValue != null) {
					skuSubscriptionConfiguration.setSubscriptionTypeSettings(
						(Map)SkuSubscriptionConfigurationSerDes.toMap(
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