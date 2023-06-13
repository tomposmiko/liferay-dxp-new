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

package com.liferay.headless.commerce.delivery.order.client.serdes.v1_0;

import com.liferay.headless.commerce.delivery.order.client.dto.v1_0.PlacedOrderItemShipment;
import com.liferay.headless.commerce.delivery.order.client.json.BaseJSONParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
public class PlacedOrderItemShipmentSerDes {

	public static PlacedOrderItemShipment toDTO(String json) {
		PlacedOrderItemShipmentJSONParser placedOrderItemShipmentJSONParser =
			new PlacedOrderItemShipmentJSONParser();

		return placedOrderItemShipmentJSONParser.parseToDTO(json);
	}

	public static PlacedOrderItemShipment[] toDTOs(String json) {
		PlacedOrderItemShipmentJSONParser placedOrderItemShipmentJSONParser =
			new PlacedOrderItemShipmentJSONParser();

		return placedOrderItemShipmentJSONParser.parseToDTOs(json);
	}

	public static String toJSON(
		PlacedOrderItemShipment placedOrderItemShipment) {

		if (placedOrderItemShipment == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (placedOrderItemShipment.getAccountId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"accountId\": ");

			sb.append(placedOrderItemShipment.getAccountId());
		}

		if (placedOrderItemShipment.getAuthor() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"author\": ");

			sb.append("\"");

			sb.append(_escape(placedOrderItemShipment.getAuthor()));

			sb.append("\"");
		}

		if (placedOrderItemShipment.getCarrier() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"carrier\": ");

			sb.append("\"");

			sb.append(_escape(placedOrderItemShipment.getCarrier()));

			sb.append("\"");
		}

		if (placedOrderItemShipment.getCreateDate() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"createDate\": ");

			sb.append("\"");

			sb.append(
				liferayToJSONDateFormat.format(
					placedOrderItemShipment.getCreateDate()));

			sb.append("\"");
		}

		if (placedOrderItemShipment.getEstimatedDeliveryDate() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"estimatedDeliveryDate\": ");

			sb.append("\"");

			sb.append(
				liferayToJSONDateFormat.format(
					placedOrderItemShipment.getEstimatedDeliveryDate()));

			sb.append("\"");
		}

		if (placedOrderItemShipment.getEstimatedShippingDate() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"estimatedShippingDate\": ");

			sb.append("\"");

			sb.append(
				liferayToJSONDateFormat.format(
					placedOrderItemShipment.getEstimatedShippingDate()));

			sb.append("\"");
		}

		if (placedOrderItemShipment.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(placedOrderItemShipment.getId());
		}

		if (placedOrderItemShipment.getModifiedDate() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"modifiedDate\": ");

			sb.append("\"");

			sb.append(
				liferayToJSONDateFormat.format(
					placedOrderItemShipment.getModifiedDate()));

			sb.append("\"");
		}

		if (placedOrderItemShipment.getOrderId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"orderId\": ");

			sb.append(placedOrderItemShipment.getOrderId());
		}

		if (placedOrderItemShipment.getQuantity() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"quantity\": ");

			sb.append(placedOrderItemShipment.getQuantity());
		}

		if (placedOrderItemShipment.getShippingAddressId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingAddressId\": ");

			sb.append(placedOrderItemShipment.getShippingAddressId());
		}

		if (placedOrderItemShipment.getShippingMethodId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingMethodId\": ");

			sb.append(placedOrderItemShipment.getShippingMethodId());
		}

		if (placedOrderItemShipment.getShippingOptionName() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingOptionName\": ");

			sb.append("\"");

			sb.append(_escape(placedOrderItemShipment.getShippingOptionName()));

			sb.append("\"");
		}

		if (placedOrderItemShipment.getStatus() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"status\": ");

			sb.append(String.valueOf(placedOrderItemShipment.getStatus()));
		}

		if (placedOrderItemShipment.getTrackingNumber() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"trackingNumber\": ");

			sb.append("\"");

			sb.append(_escape(placedOrderItemShipment.getTrackingNumber()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		PlacedOrderItemShipmentJSONParser placedOrderItemShipmentJSONParser =
			new PlacedOrderItemShipmentJSONParser();

		return placedOrderItemShipmentJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		PlacedOrderItemShipment placedOrderItemShipment) {

		if (placedOrderItemShipment == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		DateFormat liferayToJSONDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssXX");

		if (placedOrderItemShipment.getAccountId() == null) {
			map.put("accountId", null);
		}
		else {
			map.put(
				"accountId",
				String.valueOf(placedOrderItemShipment.getAccountId()));
		}

		if (placedOrderItemShipment.getAuthor() == null) {
			map.put("author", null);
		}
		else {
			map.put(
				"author", String.valueOf(placedOrderItemShipment.getAuthor()));
		}

		if (placedOrderItemShipment.getCarrier() == null) {
			map.put("carrier", null);
		}
		else {
			map.put(
				"carrier",
				String.valueOf(placedOrderItemShipment.getCarrier()));
		}

		if (placedOrderItemShipment.getCreateDate() == null) {
			map.put("createDate", null);
		}
		else {
			map.put(
				"createDate",
				liferayToJSONDateFormat.format(
					placedOrderItemShipment.getCreateDate()));
		}

		if (placedOrderItemShipment.getEstimatedDeliveryDate() == null) {
			map.put("estimatedDeliveryDate", null);
		}
		else {
			map.put(
				"estimatedDeliveryDate",
				liferayToJSONDateFormat.format(
					placedOrderItemShipment.getEstimatedDeliveryDate()));
		}

		if (placedOrderItemShipment.getEstimatedShippingDate() == null) {
			map.put("estimatedShippingDate", null);
		}
		else {
			map.put(
				"estimatedShippingDate",
				liferayToJSONDateFormat.format(
					placedOrderItemShipment.getEstimatedShippingDate()));
		}

		if (placedOrderItemShipment.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(placedOrderItemShipment.getId()));
		}

		if (placedOrderItemShipment.getModifiedDate() == null) {
			map.put("modifiedDate", null);
		}
		else {
			map.put(
				"modifiedDate",
				liferayToJSONDateFormat.format(
					placedOrderItemShipment.getModifiedDate()));
		}

		if (placedOrderItemShipment.getOrderId() == null) {
			map.put("orderId", null);
		}
		else {
			map.put(
				"orderId",
				String.valueOf(placedOrderItemShipment.getOrderId()));
		}

		if (placedOrderItemShipment.getQuantity() == null) {
			map.put("quantity", null);
		}
		else {
			map.put(
				"quantity",
				String.valueOf(placedOrderItemShipment.getQuantity()));
		}

		if (placedOrderItemShipment.getShippingAddressId() == null) {
			map.put("shippingAddressId", null);
		}
		else {
			map.put(
				"shippingAddressId",
				String.valueOf(placedOrderItemShipment.getShippingAddressId()));
		}

		if (placedOrderItemShipment.getShippingMethodId() == null) {
			map.put("shippingMethodId", null);
		}
		else {
			map.put(
				"shippingMethodId",
				String.valueOf(placedOrderItemShipment.getShippingMethodId()));
		}

		if (placedOrderItemShipment.getShippingOptionName() == null) {
			map.put("shippingOptionName", null);
		}
		else {
			map.put(
				"shippingOptionName",
				String.valueOf(
					placedOrderItemShipment.getShippingOptionName()));
		}

		if (placedOrderItemShipment.getStatus() == null) {
			map.put("status", null);
		}
		else {
			map.put(
				"status", String.valueOf(placedOrderItemShipment.getStatus()));
		}

		if (placedOrderItemShipment.getTrackingNumber() == null) {
			map.put("trackingNumber", null);
		}
		else {
			map.put(
				"trackingNumber",
				String.valueOf(placedOrderItemShipment.getTrackingNumber()));
		}

		return map;
	}

	public static class PlacedOrderItemShipmentJSONParser
		extends BaseJSONParser<PlacedOrderItemShipment> {

		@Override
		protected PlacedOrderItemShipment createDTO() {
			return new PlacedOrderItemShipment();
		}

		@Override
		protected PlacedOrderItemShipment[] createDTOArray(int size) {
			return new PlacedOrderItemShipment[size];
		}

		@Override
		protected void setField(
			PlacedOrderItemShipment placedOrderItemShipment,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "accountId")) {
				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setAccountId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "author")) {
				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setAuthor(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "carrier")) {
				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setCarrier(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "createDate")) {
				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setCreateDate(
						toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "estimatedDeliveryDate")) {

				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setEstimatedDeliveryDate(
						toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "estimatedShippingDate")) {

				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setEstimatedShippingDate(
						toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "modifiedDate")) {
				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setModifiedDate(
						toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "orderId")) {
				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setOrderId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "quantity")) {
				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setQuantity(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingAddressId")) {
				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setShippingAddressId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingMethodId")) {
				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setShippingMethodId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "shippingOptionName")) {

				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setShippingOptionName(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "status")) {
				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setStatus(
						StatusSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "trackingNumber")) {
				if (jsonParserFieldValue != null) {
					placedOrderItemShipment.setTrackingNumber(
						(String)jsonParserFieldValue);
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