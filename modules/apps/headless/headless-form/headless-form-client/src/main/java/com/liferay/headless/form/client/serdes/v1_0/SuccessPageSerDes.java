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

package com.liferay.headless.form.client.serdes.v1_0;

import com.liferay.headless.form.client.dto.v1_0.SuccessPage;
import com.liferay.headless.form.client.json.BaseJSONParser;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class SuccessPageSerDes {

	public static SuccessPage toDTO(String json) {
		SuccessPageJSONParser successPageJSONParser =
			new SuccessPageJSONParser();

		return successPageJSONParser.parseToDTO(json);
	}

	public static SuccessPage[] toDTOs(String json) {
		SuccessPageJSONParser successPageJSONParser =
			new SuccessPageJSONParser();

		return successPageJSONParser.parseToDTOs(json);
	}

	public static String toJSON(SuccessPage successPage) {
		if (successPage == null) {
			return "{}";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"description\": ");

		sb.append("\"");
		sb.append(successPage.getDescription());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"headline\": ");

		sb.append("\"");
		sb.append(successPage.getHeadline());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"id\": ");

		sb.append(successPage.getId());

		sb.append("}");

		return sb.toString();
	}

	public static String toJSON(Collection<SuccessPage> successPages) {
		if (successPages == null) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("[");

		for (SuccessPage successPage : successPages) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append(toJSON(successPage));
		}

		sb.append("]");

		return sb.toString();
	}

	private static class SuccessPageJSONParser
		extends BaseJSONParser<SuccessPage> {

		protected SuccessPage createDTO() {
			return new SuccessPage();
		}

		protected SuccessPage[] createDTOArray(int size) {
			return new SuccessPage[size];
		}

		protected void setField(
			SuccessPage successPage, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "description")) {
				if (jsonParserFieldValue != null) {
					successPage.setDescription((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "headline")) {
				if (jsonParserFieldValue != null) {
					successPage.setHeadline((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					successPage.setId((Long)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}