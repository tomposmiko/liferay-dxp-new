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

package com.liferay.headless.commerce.admin.inventory.client.serdes.v1_0;

import com.liferay.headless.commerce.admin.inventory.client.dto.v1_0.ReplenishmentItem;
import com.liferay.headless.commerce.admin.inventory.client.json.BaseJSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Alessio Antonio Rendina
 * @generated
 */
@Generated("")
public class ReplenishmentItemSerDes {

	public static ReplenishmentItem toDTO(String json) {
		ReplenishmentItemJSONParser replenishmentItemJSONParser =
			new ReplenishmentItemJSONParser();

		return replenishmentItemJSONParser.parseToDTO(json);
	}

	public static ReplenishmentItem[] toDTOs(String json) {
		ReplenishmentItemJSONParser replenishmentItemJSONParser =
			new ReplenishmentItemJSONParser();

		return replenishmentItemJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ReplenishmentItem replenishmentItem) {
		if (replenishmentItem == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (replenishmentItem.getAvailabilityDate() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"availabilityDate\": ");

			sb.append("\"");

			sb.append(
				liferayToJSONDateFormat.format(
					replenishmentItem.getAvailabilityDate()));

			sb.append("\"");
		}

		if (replenishmentItem.getExternalReferenceCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"externalReferenceCode\": ");

			sb.append("\"");

			sb.append(_escape(replenishmentItem.getExternalReferenceCode()));

			sb.append("\"");
		}

		if (replenishmentItem.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(replenishmentItem.getId());
		}

		if (replenishmentItem.getQuantity() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"quantity\": ");

			sb.append(replenishmentItem.getQuantity());
		}

		if (replenishmentItem.getSku() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"sku\": ");

			sb.append("\"");

			sb.append(_escape(replenishmentItem.getSku()));

			sb.append("\"");
		}

		if (replenishmentItem.getWarehouseId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"warehouseId\": ");

			sb.append(replenishmentItem.getWarehouseId());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		ReplenishmentItemJSONParser replenishmentItemJSONParser =
			new ReplenishmentItemJSONParser();

		return replenishmentItemJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		ReplenishmentItem replenishmentItem) {

		if (replenishmentItem == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (replenishmentItem.getAvailabilityDate() == null) {
			map.put("availabilityDate", null);
		}
		else {
			map.put(
				"availabilityDate",
				liferayToJSONDateFormat.format(
					replenishmentItem.getAvailabilityDate()));
		}

		if (replenishmentItem.getExternalReferenceCode() == null) {
			map.put("externalReferenceCode", null);
		}
		else {
			map.put(
				"externalReferenceCode",
				String.valueOf(replenishmentItem.getExternalReferenceCode()));
		}

		if (replenishmentItem.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(replenishmentItem.getId()));
		}

		if (replenishmentItem.getQuantity() == null) {
			map.put("quantity", null);
		}
		else {
			map.put(
				"quantity", String.valueOf(replenishmentItem.getQuantity()));
		}

		if (replenishmentItem.getSku() == null) {
			map.put("sku", null);
		}
		else {
			map.put("sku", String.valueOf(replenishmentItem.getSku()));
		}

		if (replenishmentItem.getWarehouseId() == null) {
			map.put("warehouseId", null);
		}
		else {
			map.put(
				"warehouseId",
				String.valueOf(replenishmentItem.getWarehouseId()));
		}

		return map;
	}

	public static class ReplenishmentItemJSONParser
		extends BaseJSONParser<ReplenishmentItem> {

		@Override
		protected ReplenishmentItem createDTO() {
			return new ReplenishmentItem();
		}

		@Override
		protected ReplenishmentItem[] createDTOArray(int size) {
			return new ReplenishmentItem[size];
		}

		@Override
		protected void setField(
			ReplenishmentItem replenishmentItem, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "availabilityDate")) {
				if (jsonParserFieldValue != null) {
					replenishmentItem.setAvailabilityDate(
						toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "externalReferenceCode")) {

				if (jsonParserFieldValue != null) {
					replenishmentItem.setExternalReferenceCode(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					replenishmentItem.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "quantity")) {
				if (jsonParserFieldValue != null) {
					replenishmentItem.setQuantity(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sku")) {
				if (jsonParserFieldValue != null) {
					replenishmentItem.setSku((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "warehouseId")) {
				if (jsonParserFieldValue != null) {
					replenishmentItem.setWarehouseId(
						Long.valueOf((String)jsonParserFieldValue));
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