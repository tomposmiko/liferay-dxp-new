/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.saml.admin.rest.client.serdes.v1_0;

import com.liferay.saml.admin.rest.client.dto.v1_0.Idp;
import com.liferay.saml.admin.rest.client.json.BaseJSONParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * @author Stian Sigvartsen
 * @generated
 */
@Generated("")
public class IdpSerDes {

	public static Idp toDTO(String json) {
		IdpJSONParser idpJSONParser = new IdpJSONParser();

		return idpJSONParser.parseToDTO(json);
	}

	public static Idp[] toDTOs(String json) {
		IdpJSONParser idpJSONParser = new IdpJSONParser();

		return idpJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Idp idp) {
		if (idp == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (idp.getAuthnRequestSignatureRequired() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"authnRequestSignatureRequired\": ");

			sb.append(idp.getAuthnRequestSignatureRequired());
		}

		if (idp.getDefaultAssertionLifetime() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"defaultAssertionLifetime\": ");

			sb.append(idp.getDefaultAssertionLifetime());
		}

		if (idp.getSessionMaximumAge() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"sessionMaximumAge\": ");

			sb.append(idp.getSessionMaximumAge());
		}

		if (idp.getSessionTimeout() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"sessionTimeout\": ");

			sb.append(idp.getSessionTimeout());
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, Object> toMap(String json) {
		IdpJSONParser idpJSONParser = new IdpJSONParser();

		return idpJSONParser.parseToMap(json);
	}

	public static Map<String, String> toMap(Idp idp) {
		if (idp == null) {
			return null;
		}

		Map<String, String> map = new TreeMap<>();

		if (idp.getAuthnRequestSignatureRequired() == null) {
			map.put("authnRequestSignatureRequired", null);
		}
		else {
			map.put(
				"authnRequestSignatureRequired",
				String.valueOf(idp.getAuthnRequestSignatureRequired()));
		}

		if (idp.getDefaultAssertionLifetime() == null) {
			map.put("defaultAssertionLifetime", null);
		}
		else {
			map.put(
				"defaultAssertionLifetime",
				String.valueOf(idp.getDefaultAssertionLifetime()));
		}

		if (idp.getSessionMaximumAge() == null) {
			map.put("sessionMaximumAge", null);
		}
		else {
			map.put(
				"sessionMaximumAge",
				String.valueOf(idp.getSessionMaximumAge()));
		}

		if (idp.getSessionTimeout() == null) {
			map.put("sessionTimeout", null);
		}
		else {
			map.put("sessionTimeout", String.valueOf(idp.getSessionTimeout()));
		}

		return map;
	}

	public static class IdpJSONParser extends BaseJSONParser<Idp> {

		@Override
		protected Idp createDTO() {
			return new Idp();
		}

		@Override
		protected Idp[] createDTOArray(int size) {
			return new Idp[size];
		}

		@Override
		protected void setField(
			Idp idp, String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(
					jsonParserFieldName, "authnRequestSignatureRequired")) {

				if (jsonParserFieldValue != null) {
					idp.setAuthnRequestSignatureRequired(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "defaultAssertionLifetime")) {

				if (jsonParserFieldValue != null) {
					idp.setDefaultAssertionLifetime(
						Integer.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sessionMaximumAge")) {
				if (jsonParserFieldValue != null) {
					idp.setSessionMaximumAge(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sessionTimeout")) {
				if (jsonParserFieldValue != null) {
					idp.setSessionTimeout(
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