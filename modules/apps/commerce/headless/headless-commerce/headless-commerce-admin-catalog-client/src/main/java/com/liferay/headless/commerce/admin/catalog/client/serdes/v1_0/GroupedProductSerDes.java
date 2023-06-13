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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.GroupedProduct;
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
public class GroupedProductSerDes {

	public static GroupedProduct toDTO(String json) {
		GroupedProductJSONParser groupedProductJSONParser =
			new GroupedProductJSONParser();

		return groupedProductJSONParser.parseToDTO(json);
	}

	public static GroupedProduct[] toDTOs(String json) {
		GroupedProductJSONParser groupedProductJSONParser =
			new GroupedProductJSONParser();

		return groupedProductJSONParser.parseToDTOs(json);
	}

	public static String toJSON(GroupedProduct groupedProduct) {
		if (groupedProduct == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (groupedProduct.getEntryProductExternalReferenceCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"entryProductExternalReferenceCode\": ");

			sb.append("\"");

			sb.append(
				_escape(groupedProduct.getEntryProductExternalReferenceCode()));

			sb.append("\"");
		}

		if (groupedProduct.getEntryProductId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"entryProductId\": ");

			sb.append(groupedProduct.getEntryProductId());
		}

		if (groupedProduct.getEntryProductName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"entryProductName\": ");

			sb.append(_toJSON(groupedProduct.getEntryProductName()));
		}

		if (groupedProduct.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(groupedProduct.getId());
		}

		if (groupedProduct.getPriority() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"priority\": ");

			sb.append(groupedProduct.getPriority());
		}

		if (groupedProduct.getProductExternalReferenceCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"productExternalReferenceCode\": ");

			sb.append("\"");

			sb.append(
				_escape(groupedProduct.getProductExternalReferenceCode()));

			sb.append("\"");
		}

		if (groupedProduct.getProductId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"productId\": ");

			sb.append(groupedProduct.getProductId());
		}

		if (groupedProduct.getProductName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"productName\": ");

			sb.append(_toJSON(groupedProduct.getProductName()));
		}

		if (groupedProduct.getQuantity() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"quantity\": ");

			sb.append(groupedProduct.getQuantity());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		GroupedProductJSONParser groupedProductJSONParser =
			new GroupedProductJSONParser();

		return groupedProductJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(GroupedProduct groupedProduct) {
		if (groupedProduct == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (groupedProduct.getEntryProductExternalReferenceCode() == null) {
			map.put("entryProductExternalReferenceCode", null);
		}
		else {
			map.put(
				"entryProductExternalReferenceCode",
				String.valueOf(
					groupedProduct.getEntryProductExternalReferenceCode()));
		}

		if (groupedProduct.getEntryProductId() == null) {
			map.put("entryProductId", null);
		}
		else {
			map.put(
				"entryProductId",
				String.valueOf(groupedProduct.getEntryProductId()));
		}

		if (groupedProduct.getEntryProductName() == null) {
			map.put("entryProductName", null);
		}
		else {
			map.put(
				"entryProductName",
				String.valueOf(groupedProduct.getEntryProductName()));
		}

		if (groupedProduct.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(groupedProduct.getId()));
		}

		if (groupedProduct.getPriority() == null) {
			map.put("priority", null);
		}
		else {
			map.put("priority", String.valueOf(groupedProduct.getPriority()));
		}

		if (groupedProduct.getProductExternalReferenceCode() == null) {
			map.put("productExternalReferenceCode", null);
		}
		else {
			map.put(
				"productExternalReferenceCode",
				String.valueOf(
					groupedProduct.getProductExternalReferenceCode()));
		}

		if (groupedProduct.getProductId() == null) {
			map.put("productId", null);
		}
		else {
			map.put("productId", String.valueOf(groupedProduct.getProductId()));
		}

		if (groupedProduct.getProductName() == null) {
			map.put("productName", null);
		}
		else {
			map.put(
				"productName", String.valueOf(groupedProduct.getProductName()));
		}

		if (groupedProduct.getQuantity() == null) {
			map.put("quantity", null);
		}
		else {
			map.put("quantity", String.valueOf(groupedProduct.getQuantity()));
		}

		return map;
	}

	public static class GroupedProductJSONParser
		extends BaseJSONParser<GroupedProduct> {

		@Override
		protected GroupedProduct createDTO() {
			return new GroupedProduct();
		}

		@Override
		protected GroupedProduct[] createDTOArray(int size) {
			return new GroupedProduct[size];
		}

		@Override
		protected void setField(
			GroupedProduct groupedProduct, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(
					jsonParserFieldName, "entryProductExternalReferenceCode")) {

				if (jsonParserFieldValue != null) {
					groupedProduct.setEntryProductExternalReferenceCode(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "entryProductId")) {
				if (jsonParserFieldValue != null) {
					groupedProduct.setEntryProductId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "entryProductName")) {
				if (jsonParserFieldValue != null) {
					groupedProduct.setEntryProductName(
						(Map)GroupedProductSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					groupedProduct.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "priority")) {
				if (jsonParserFieldValue != null) {
					groupedProduct.setPriority(
						Double.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "productExternalReferenceCode")) {

				if (jsonParserFieldValue != null) {
					groupedProduct.setProductExternalReferenceCode(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "productId")) {
				if (jsonParserFieldValue != null) {
					groupedProduct.setProductId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "productName")) {
				if (jsonParserFieldValue != null) {
					groupedProduct.setProductName(
						(Map)GroupedProductSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "quantity")) {
				if (jsonParserFieldValue != null) {
					groupedProduct.setQuantity(
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