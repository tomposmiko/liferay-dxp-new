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

import com.liferay.headless.commerce.admin.account.client.dto.v1_0.AccountChannelEntry;
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
public class AccountChannelEntrySerDes {

	public static AccountChannelEntry toDTO(String json) {
		AccountChannelEntryJSONParser accountChannelEntryJSONParser =
			new AccountChannelEntryJSONParser();

		return accountChannelEntryJSONParser.parseToDTO(json);
	}

	public static AccountChannelEntry[] toDTOs(String json) {
		AccountChannelEntryJSONParser accountChannelEntryJSONParser =
			new AccountChannelEntryJSONParser();

		return accountChannelEntryJSONParser.parseToDTOs(json);
	}

	public static String toJSON(AccountChannelEntry accountChannelEntry) {
		if (accountChannelEntry == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (accountChannelEntry.getAccountExternalReferenceCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"accountExternalReferenceCode\": ");

			sb.append("\"");

			sb.append(
				_escape(accountChannelEntry.getAccountExternalReferenceCode()));

			sb.append("\"");
		}

		if (accountChannelEntry.getAccountId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"accountId\": ");

			sb.append(accountChannelEntry.getAccountId());
		}

		if (accountChannelEntry.getChannelExternalReferenceCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"channelExternalReferenceCode\": ");

			sb.append("\"");

			sb.append(
				_escape(accountChannelEntry.getChannelExternalReferenceCode()));

			sb.append("\"");
		}

		if (accountChannelEntry.getChannelId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"channelId\": ");

			sb.append(accountChannelEntry.getChannelId());
		}

		if (accountChannelEntry.getClassExternalReferenceCode() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"classExternalReferenceCode\": ");

			sb.append("\"");

			sb.append(
				_escape(accountChannelEntry.getClassExternalReferenceCode()));

			sb.append("\"");
		}

		if (accountChannelEntry.getClassPK() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"classPK\": ");

			sb.append(accountChannelEntry.getClassPK());
		}

		if (accountChannelEntry.getId() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"id\": ");

			sb.append(accountChannelEntry.getId());
		}

		if (accountChannelEntry.getOverrideEligibility() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"overrideEligibility\": ");

			sb.append(accountChannelEntry.getOverrideEligibility());
		}

		if (accountChannelEntry.getPriority() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"priority\": ");

			sb.append(accountChannelEntry.getPriority());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		AccountChannelEntryJSONParser accountChannelEntryJSONParser =
			new AccountChannelEntryJSONParser();

		return accountChannelEntryJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(
		AccountChannelEntry accountChannelEntry) {

		if (accountChannelEntry == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (accountChannelEntry.getAccountExternalReferenceCode() == null) {
			map.put("accountExternalReferenceCode", null);
		}
		else {
			map.put(
				"accountExternalReferenceCode",
				String.valueOf(
					accountChannelEntry.getAccountExternalReferenceCode()));
		}

		if (accountChannelEntry.getAccountId() == null) {
			map.put("accountId", null);
		}
		else {
			map.put(
				"accountId",
				String.valueOf(accountChannelEntry.getAccountId()));
		}

		if (accountChannelEntry.getChannelExternalReferenceCode() == null) {
			map.put("channelExternalReferenceCode", null);
		}
		else {
			map.put(
				"channelExternalReferenceCode",
				String.valueOf(
					accountChannelEntry.getChannelExternalReferenceCode()));
		}

		if (accountChannelEntry.getChannelId() == null) {
			map.put("channelId", null);
		}
		else {
			map.put(
				"channelId",
				String.valueOf(accountChannelEntry.getChannelId()));
		}

		if (accountChannelEntry.getClassExternalReferenceCode() == null) {
			map.put("classExternalReferenceCode", null);
		}
		else {
			map.put(
				"classExternalReferenceCode",
				String.valueOf(
					accountChannelEntry.getClassExternalReferenceCode()));
		}

		if (accountChannelEntry.getClassPK() == null) {
			map.put("classPK", null);
		}
		else {
			map.put(
				"classPK", String.valueOf(accountChannelEntry.getClassPK()));
		}

		if (accountChannelEntry.getId() == null) {
			map.put("id", null);
		}
		else {
			map.put("id", String.valueOf(accountChannelEntry.getId()));
		}

		if (accountChannelEntry.getOverrideEligibility() == null) {
			map.put("overrideEligibility", null);
		}
		else {
			map.put(
				"overrideEligibility",
				String.valueOf(accountChannelEntry.getOverrideEligibility()));
		}

		if (accountChannelEntry.getPriority() == null) {
			map.put("priority", null);
		}
		else {
			map.put(
				"priority", String.valueOf(accountChannelEntry.getPriority()));
		}

		return map;
	}

	public static class AccountChannelEntryJSONParser
		extends BaseJSONParser<AccountChannelEntry> {

		@Override
		protected AccountChannelEntry createDTO() {
			return new AccountChannelEntry();
		}

		@Override
		protected AccountChannelEntry[] createDTOArray(int size) {
			return new AccountChannelEntry[size];
		}

		@Override
		protected void setField(
			AccountChannelEntry accountChannelEntry, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(
					jsonParserFieldName, "accountExternalReferenceCode")) {

				if (jsonParserFieldValue != null) {
					accountChannelEntry.setAccountExternalReferenceCode(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "accountId")) {
				if (jsonParserFieldValue != null) {
					accountChannelEntry.setAccountId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "channelExternalReferenceCode")) {

				if (jsonParserFieldValue != null) {
					accountChannelEntry.setChannelExternalReferenceCode(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "channelId")) {
				if (jsonParserFieldValue != null) {
					accountChannelEntry.setChannelId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "classExternalReferenceCode")) {

				if (jsonParserFieldValue != null) {
					accountChannelEntry.setClassExternalReferenceCode(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "classPK")) {
				if (jsonParserFieldValue != null) {
					accountChannelEntry.setClassPK(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					accountChannelEntry.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "overrideEligibility")) {

				if (jsonParserFieldValue != null) {
					accountChannelEntry.setOverrideEligibility(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "priority")) {
				if (jsonParserFieldValue != null) {
					accountChannelEntry.setPriority(
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