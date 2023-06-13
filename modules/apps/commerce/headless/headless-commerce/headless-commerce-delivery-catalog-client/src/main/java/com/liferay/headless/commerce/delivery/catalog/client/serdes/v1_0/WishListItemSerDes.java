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

package com.liferay.headless.commerce.delivery.catalog.client.serdes.v1_0;

import com.liferay.headless.commerce.delivery.catalog.client.dto.v1_0.WishListItem;
import com.liferay.headless.commerce.delivery.catalog.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public class WishListItemSerDes {

	public static WishListItem toDTO(String json) {
		WishListItemJSONParser wishListItemJSONParser =
			new WishListItemJSONParser();

		return wishListItemJSONParser.parseToDTO(json);
	}

	public static WishListItem[] toDTOs(String json) {
		WishListItemJSONParser wishListItemJSONParser =
			new WishListItemJSONParser();

		return wishListItemJSONParser.parseToDTOs(json);
	}

	public static String toJSON(WishListItem wishListItem) {
		if (wishListItem == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (wishListItem.getFinalPrice() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"finalPrice\": ");

			sb.append("\"");

			sb.append(_escape(wishListItem.getFinalPrice()));

			sb.append("\"");
		}

		if (wishListItem.getFriendlyURL() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"friendlyURL\": ");

			sb.append("\"");

			sb.append(_escape(wishListItem.getFriendlyURL()));

			sb.append("\"");
		}

		if (wishListItem.getIcon() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"icon\": ");

			sb.append("\"");

			sb.append(_escape(wishListItem.getIcon()));

			sb.append("\"");
		}

		if (wishListItem.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(wishListItem.getId());
		}

		if (wishListItem.getProductId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"productId\": ");

			sb.append(wishListItem.getProductId());
		}

		if (wishListItem.getProductName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"productName\": ");

			sb.append("\"");

			sb.append(_escape(wishListItem.getProductName()));

			sb.append("\"");
		}

		if (wishListItem.getSkuId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"skuId\": ");

			sb.append(wishListItem.getSkuId());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		WishListItemJSONParser wishListItemJSONParser =
			new WishListItemJSONParser();

		return wishListItemJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(WishListItem wishListItem) {
		if (wishListItem == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (wishListItem.getFinalPrice() == null) {
			map.put("finalPrice", null);
		}
		else {
			map.put("finalPrice", String.valueOf(wishListItem.getFinalPrice()));
		}

		if (wishListItem.getFriendlyURL() == null) {
			map.put("friendlyURL", null);
		}
		else {
			map.put(
				"friendlyURL", String.valueOf(wishListItem.getFriendlyURL()));
		}

		if (wishListItem.getIcon() == null) {
			map.put("icon", null);
		}
		else {
			map.put("icon", String.valueOf(wishListItem.getIcon()));
		}

		if (wishListItem.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(wishListItem.getId()));
		}

		if (wishListItem.getProductId() == null) {
			map.put("productId", null);
		}
		else {
			map.put("productId", String.valueOf(wishListItem.getProductId()));
		}

		if (wishListItem.getProductName() == null) {
			map.put("productName", null);
		}
		else {
			map.put(
				"productName", String.valueOf(wishListItem.getProductName()));
		}

		if (wishListItem.getSkuId() == null) {
			map.put("skuId", null);
		}
		else {
			map.put("skuId", String.valueOf(wishListItem.getSkuId()));
		}

		return map;
	}

	public static class WishListItemJSONParser
		extends BaseJSONParser<WishListItem> {

		@Override
		protected WishListItem createDTO() {
			return new WishListItem();
		}

		@Override
		protected WishListItem[] createDTOArray(int size) {
			return new WishListItem[size];
		}

		@Override
		protected void setField(
			WishListItem wishListItem, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "finalPrice")) {
				if (jsonParserFieldValue != null) {
					wishListItem.setFinalPrice((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "friendlyURL")) {
				if (jsonParserFieldValue != null) {
					wishListItem.setFriendlyURL((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "icon")) {
				if (jsonParserFieldValue != null) {
					wishListItem.setIcon((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					wishListItem.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "productId")) {
				if (jsonParserFieldValue != null) {
					wishListItem.setProductId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "productName")) {
				if (jsonParserFieldValue != null) {
					wishListItem.setProductName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "skuId")) {
				if (jsonParserFieldValue != null) {
					wishListItem.setSkuId(
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