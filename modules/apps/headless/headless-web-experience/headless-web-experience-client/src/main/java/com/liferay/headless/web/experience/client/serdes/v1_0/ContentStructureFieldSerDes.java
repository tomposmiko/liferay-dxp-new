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

package com.liferay.headless.web.experience.client.serdes.v1_0;

import com.liferay.headless.web.experience.client.dto.v1_0.ContentStructureField;
import com.liferay.headless.web.experience.client.dto.v1_0.Option;
import com.liferay.headless.web.experience.client.json.BaseJSONParser;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class ContentStructureFieldSerDes {

	public static ContentStructureField toDTO(String json) {
		ContentStructureFieldJSONParser contentStructureFieldJSONParser =
			new ContentStructureFieldJSONParser();

		return contentStructureFieldJSONParser.parseToDTO(json);
	}

	public static ContentStructureField[] toDTOs(String json) {
		ContentStructureFieldJSONParser contentStructureFieldJSONParser =
			new ContentStructureFieldJSONParser();

		return contentStructureFieldJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ContentStructureField contentStructureField) {
		if (contentStructureField == null) {
			return "{}";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"dataType\": ");

		sb.append("\"");
		sb.append(contentStructureField.getDataType());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"inputControl\": ");

		sb.append("\"");
		sb.append(contentStructureField.getInputControl());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"label\": ");

		sb.append("\"");
		sb.append(contentStructureField.getLabel());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"localizable\": ");

		sb.append(contentStructureField.getLocalizable());
		sb.append(", ");

		sb.append("\"multiple\": ");

		sb.append(contentStructureField.getMultiple());
		sb.append(", ");

		sb.append("\"name\": ");

		sb.append("\"");
		sb.append(contentStructureField.getName());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"nestedContentStructureFields\": ");

		if (contentStructureField.getNestedContentStructureFields() == null) {
			sb.append("null");
		}
		else {
			sb.append("[");

			for (int i = 0;
				 i < contentStructureField.
					 getNestedContentStructureFields().length;
				 i++) {

				sb.append(
					contentStructureField.getNestedContentStructureFields()[i]);

				if ((i + 1) < contentStructureField.
						getNestedContentStructureFields().length) {

					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append(", ");

		sb.append("\"options\": ");

		if (contentStructureField.getOptions() == null) {
			sb.append("null");
		}
		else {
			sb.append("[");

			for (int i = 0; i < contentStructureField.getOptions().length;
				 i++) {

				sb.append(contentStructureField.getOptions()[i]);

				if ((i + 1) < contentStructureField.getOptions().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append(", ");

		sb.append("\"predefinedValue\": ");

		sb.append("\"");
		sb.append(contentStructureField.getPredefinedValue());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"repeatable\": ");

		sb.append(contentStructureField.getRepeatable());
		sb.append(", ");

		sb.append("\"required\": ");

		sb.append(contentStructureField.getRequired());
		sb.append(", ");

		sb.append("\"showLabel\": ");

		sb.append(contentStructureField.getShowLabel());

		sb.append("}");

		return sb.toString();
	}

	public static String toJSON(
		Collection<ContentStructureField> contentStructureFields) {

		if (contentStructureFields == null) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("[");

		for (ContentStructureField contentStructureField :
				contentStructureFields) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append(toJSON(contentStructureField));
		}

		sb.append("]");

		return sb.toString();
	}

	private static class ContentStructureFieldJSONParser
		extends BaseJSONParser<ContentStructureField> {

		protected ContentStructureField createDTO() {
			return new ContentStructureField();
		}

		protected ContentStructureField[] createDTOArray(int size) {
			return new ContentStructureField[size];
		}

		protected void setField(
			ContentStructureField contentStructureField,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "dataType")) {
				if (jsonParserFieldValue != null) {
					contentStructureField.setDataType(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "inputControl")) {
				if (jsonParserFieldValue != null) {
					contentStructureField.setInputControl(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "label")) {
				if (jsonParserFieldValue != null) {
					contentStructureField.setLabel(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "localizable")) {
				if (jsonParserFieldValue != null) {
					contentStructureField.setLocalizable(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "multiple")) {
				if (jsonParserFieldValue != null) {
					contentStructureField.setMultiple(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					contentStructureField.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "nestedContentStructureFields")) {

				if (jsonParserFieldValue != null) {
					contentStructureField.setNestedContentStructureFields(
						Stream.of(
							toStrings((Object[])jsonParserFieldValue)
						).map(
							object -> ContentStructureFieldSerDes.toDTO(
								(String)object)
						).toArray(
							size -> new ContentStructureField[size]
						));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "options")) {
				if (jsonParserFieldValue != null) {
					contentStructureField.setOptions(
						Stream.of(
							toStrings((Object[])jsonParserFieldValue)
						).map(
							object -> OptionSerDes.toDTO((String)object)
						).toArray(
							size -> new Option[size]
						));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "predefinedValue")) {
				if (jsonParserFieldValue != null) {
					contentStructureField.setPredefinedValue(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "repeatable")) {
				if (jsonParserFieldValue != null) {
					contentStructureField.setRepeatable(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "required")) {
				if (jsonParserFieldValue != null) {
					contentStructureField.setRequired(
						(Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "showLabel")) {
				if (jsonParserFieldValue != null) {
					contentStructureField.setShowLabel(
						(Boolean)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}