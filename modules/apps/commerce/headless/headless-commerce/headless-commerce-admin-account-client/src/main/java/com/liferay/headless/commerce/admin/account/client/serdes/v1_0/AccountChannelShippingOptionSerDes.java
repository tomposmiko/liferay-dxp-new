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

package com.liferay.headless.commerce.admin.account.client.serdes.v1_0;

import com.liferay.headless.commerce.admin.account.client.dto.v1_0.AccountChannelShippingOption;
import com.liferay.headless.commerce.admin.account.client.json.BaseJSONParser;

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
public class AccountChannelShippingOptionSerDes {

	public static AccountChannelShippingOption toDTO(String json) {
		AccountChannelShippingOptionJSONParser
			accountChannelShippingOptionJSONParser =
				new AccountChannelShippingOptionJSONParser();

		return accountChannelShippingOptionJSONParser.parseToDTO(json);
	}

	public static AccountChannelShippingOption[] toDTOs(String json) {
		AccountChannelShippingOptionJSONParser
			accountChannelShippingOptionJSONParser =
				new AccountChannelShippingOptionJSONParser();

		return accountChannelShippingOptionJSONParser.parseToDTOs(json);
	}

	public static String toJSON(
		AccountChannelShippingOption accountChannelShippingOption) {

		if (accountChannelShippingOption == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (accountChannelShippingOption.getAccountExternalReferenceCode() !=
				null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"accountExternalReferenceCode\": ");

			sb.append("\"");

			sb.append(
				_escape(
					accountChannelShippingOption.
						getAccountExternalReferenceCode()));

			sb.append("\"");
		}

		if (accountChannelShippingOption.getAccountId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"accountId\": ");

			sb.append(accountChannelShippingOption.getAccountId());
		}

		if (accountChannelShippingOption.getActions() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"actions\": ");

			sb.append(_toJSON(accountChannelShippingOption.getActions()));
		}

		if (accountChannelShippingOption.getChannelExternalReferenceCode() !=
				null) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"channelExternalReferenceCode\": ");

			sb.append("\"");

			sb.append(
				_escape(
					accountChannelShippingOption.
						getChannelExternalReferenceCode()));

			sb.append("\"");
		}

		if (accountChannelShippingOption.getChannelId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"channelId\": ");

			sb.append(accountChannelShippingOption.getChannelId());
		}

		if (accountChannelShippingOption.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(accountChannelShippingOption.getId());
		}

		if (accountChannelShippingOption.getShippingMethodId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingMethodId\": ");

			sb.append(accountChannelShippingOption.getShippingMethodId());
		}

		if (accountChannelShippingOption.getShippingMethodKey() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingMethodKey\": ");

			sb.append("\"");

			sb.append(
				_escape(accountChannelShippingOption.getShippingMethodKey()));

			sb.append("\"");
		}

		if (accountChannelShippingOption.getShippingOptionId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingOptionId\": ");

			sb.append(accountChannelShippingOption.getShippingOptionId());
		}

		if (accountChannelShippingOption.getShippingOptionKey() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"shippingOptionKey\": ");

			sb.append("\"");

			sb.append(
				_escape(accountChannelShippingOption.getShippingOptionKey()));

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		AccountChannelShippingOptionJSONParser
			accountChannelShippingOptionJSONParser =
				new AccountChannelShippingOptionJSONParser();

		return accountChannelShippingOptionJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		AccountChannelShippingOption accountChannelShippingOption) {

		if (accountChannelShippingOption == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (accountChannelShippingOption.getAccountExternalReferenceCode() ==
				null) {

			map.put("accountExternalReferenceCode", null);
		}
		else {
			map.put(
				"accountExternalReferenceCode",
				String.valueOf(
					accountChannelShippingOption.
						getAccountExternalReferenceCode()));
		}

		if (accountChannelShippingOption.getAccountId() == null) {
			map.put("accountId", null);
		}
		else {
			map.put(
				"accountId",
				String.valueOf(accountChannelShippingOption.getAccountId()));
		}

		if (accountChannelShippingOption.getActions() == null) {
			map.put("actions", null);
		}
		else {
			map.put(
				"actions",
				String.valueOf(accountChannelShippingOption.getActions()));
		}

		if (accountChannelShippingOption.getChannelExternalReferenceCode() ==
				null) {

			map.put("channelExternalReferenceCode", null);
		}
		else {
			map.put(
				"channelExternalReferenceCode",
				String.valueOf(
					accountChannelShippingOption.
						getChannelExternalReferenceCode()));
		}

		if (accountChannelShippingOption.getChannelId() == null) {
			map.put("channelId", null);
		}
		else {
			map.put(
				"channelId",
				String.valueOf(accountChannelShippingOption.getChannelId()));
		}

		if (accountChannelShippingOption.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(accountChannelShippingOption.getId()));
		}

		if (accountChannelShippingOption.getShippingMethodId() == null) {
			map.put("shippingMethodId", null);
		}
		else {
			map.put(
				"shippingMethodId",
				String.valueOf(
					accountChannelShippingOption.getShippingMethodId()));
		}

		if (accountChannelShippingOption.getShippingMethodKey() == null) {
			map.put("shippingMethodKey", null);
		}
		else {
			map.put(
				"shippingMethodKey",
				String.valueOf(
					accountChannelShippingOption.getShippingMethodKey()));
		}

		if (accountChannelShippingOption.getShippingOptionId() == null) {
			map.put("shippingOptionId", null);
		}
		else {
			map.put(
				"shippingOptionId",
				String.valueOf(
					accountChannelShippingOption.getShippingOptionId()));
		}

		if (accountChannelShippingOption.getShippingOptionKey() == null) {
			map.put("shippingOptionKey", null);
		}
		else {
			map.put(
				"shippingOptionKey",
				String.valueOf(
					accountChannelShippingOption.getShippingOptionKey()));
		}

		return map;
	}

	public static class AccountChannelShippingOptionJSONParser
		extends BaseJSONParser<AccountChannelShippingOption> {

		@Override
		protected AccountChannelShippingOption createDTO() {
			return new AccountChannelShippingOption();
		}

		@Override
		protected AccountChannelShippingOption[] createDTOArray(int size) {
			return new AccountChannelShippingOption[size];
		}

		@Override
		protected void setField(
			AccountChannelShippingOption accountChannelShippingOption,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(
					jsonParserFieldName, "accountExternalReferenceCode")) {

				if (jsonParserFieldValue != null) {
					accountChannelShippingOption.
						setAccountExternalReferenceCode(
							(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "accountId")) {
				if (jsonParserFieldValue != null) {
					accountChannelShippingOption.setAccountId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "actions")) {
				if (jsonParserFieldValue != null) {
					accountChannelShippingOption.setActions(
						(Map)AccountChannelShippingOptionSerDes.toMap(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "channelExternalReferenceCode")) {

				if (jsonParserFieldValue != null) {
					accountChannelShippingOption.
						setChannelExternalReferenceCode(
							(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "channelId")) {
				if (jsonParserFieldValue != null) {
					accountChannelShippingOption.setChannelId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					accountChannelShippingOption.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingMethodId")) {
				if (jsonParserFieldValue != null) {
					accountChannelShippingOption.setShippingMethodId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingMethodKey")) {
				if (jsonParserFieldValue != null) {
					accountChannelShippingOption.setShippingMethodKey(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingOptionId")) {
				if (jsonParserFieldValue != null) {
					accountChannelShippingOption.setShippingOptionId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "shippingOptionKey")) {
				if (jsonParserFieldValue != null) {
					accountChannelShippingOption.setShippingOptionKey(
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