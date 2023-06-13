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

package com.liferay.headless.delivery.client.serdes.v1_0;

import com.liferay.headless.delivery.client.dto.v1_0.ContentStructure;
import com.liferay.headless.delivery.client.dto.v1_0.ContentStructureField;
import com.liferay.headless.delivery.client.json.BaseJSONParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class ContentStructureSerDes {

	public static ContentStructure toDTO(String json) {
		ContentStructureJSONParser contentStructureJSONParser =
			new ContentStructureJSONParser();

		return contentStructureJSONParser.parseToDTO(json);
	}

	public static ContentStructure[] toDTOs(String json) {
		ContentStructureJSONParser contentStructureJSONParser =
			new ContentStructureJSONParser();

		return contentStructureJSONParser.parseToDTOs(json);
	}

	public static String toJSON(ContentStructure contentStructure) {
		if (contentStructure == null) {
			return "{}";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"availableLanguages\": ");

		if (contentStructure.getAvailableLanguages() == null) {
			sb.append("null");
		}
		else {
			sb.append("[");

			for (int i = 0; i < contentStructure.getAvailableLanguages().length;
				 i++) {

				sb.append("\"");
				sb.append(contentStructure.getAvailableLanguages()[i]);
				sb.append("\"");

				if ((i + 1) < contentStructure.getAvailableLanguages().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append(", ");

		sb.append("\"contentStructureFields\": ");

		if (contentStructure.getContentStructureFields() == null) {
			sb.append("null");
		}
		else {
			sb.append("[");

			for (int i = 0;
				 i < contentStructure.getContentStructureFields().length; i++) {

				sb.append(contentStructure.getContentStructureFields()[i]);

				if ((i + 1) <
						contentStructure.getContentStructureFields().length) {

					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append(", ");

		sb.append("\"creator\": ");

		if (contentStructure.getCreator() == null) {
			sb.append("null");
		}
		else {
			sb.append(contentStructure.getCreator());
		}

		sb.append(", ");

		sb.append("\"dateCreated\": ");

		if (contentStructure.getDateCreated() == null) {
			sb.append("null");
		}
		else {
			sb.append(contentStructure.getDateCreated());
		}

		sb.append(", ");

		sb.append("\"dateModified\": ");

		if (contentStructure.getDateModified() == null) {
			sb.append("null");
		}
		else {
			sb.append(contentStructure.getDateModified());
		}

		sb.append(", ");

		sb.append("\"description\": ");

		if (contentStructure.getDescription() == null) {
			sb.append("null");
		}
		else {
			sb.append(contentStructure.getDescription());
		}

		sb.append(", ");

		sb.append("\"id\": ");

		if (contentStructure.getId() == null) {
			sb.append("null");
		}
		else {
			sb.append(contentStructure.getId());
		}

		sb.append(", ");

		sb.append("\"name\": ");

		if (contentStructure.getName() == null) {
			sb.append("null");
		}
		else {
			sb.append(contentStructure.getName());
		}

		sb.append(", ");

		sb.append("\"siteId\": ");

		if (contentStructure.getSiteId() == null) {
			sb.append("null");
		}
		else {
			sb.append(contentStructure.getSiteId());
		}

		sb.append("}");

		return sb.toString();
	}

	public static String toJSON(
		Collection<ContentStructure> contentStructures) {

		if (contentStructures == null) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("[");

		for (ContentStructure contentStructure : contentStructures) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append(toJSON(contentStructure));
		}

		sb.append("]");

		return sb.toString();
	}

	private static class ContentStructureJSONParser
		extends BaseJSONParser<ContentStructure> {

		protected ContentStructure createDTO() {
			return new ContentStructure();
		}

		protected ContentStructure[] createDTOArray(int size) {
			return new ContentStructure[size];
		}

		protected void setField(
			ContentStructure contentStructure, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "availableLanguages")) {
				if (jsonParserFieldValue != null) {
					contentStructure.setAvailableLanguages(
						toStrings((Object[])jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "contentStructureFields")) {

				if (jsonParserFieldValue != null) {
					contentStructure.setContentStructureFields(
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
			else if (Objects.equals(jsonParserFieldName, "creator")) {
				if (jsonParserFieldValue != null) {
					contentStructure.setCreator(
						CreatorSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateCreated")) {
				if (jsonParserFieldValue != null) {
					contentStructure.setDateCreated(
						_toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateModified")) {
				if (jsonParserFieldValue != null) {
					contentStructure.setDateModified(
						_toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "description")) {
				if (jsonParserFieldValue != null) {
					contentStructure.setDescription(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					contentStructure.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					contentStructure.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "siteId")) {
				if (jsonParserFieldValue != null) {
					contentStructure.setSiteId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

		private Date _toDate(String string) {
			try {
				DateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss'Z'");

				return dateFormat.parse(string);
			}
			catch (ParseException pe) {
				throw new IllegalArgumentException("Unable to parse " + string);
			}
		}

	}

}