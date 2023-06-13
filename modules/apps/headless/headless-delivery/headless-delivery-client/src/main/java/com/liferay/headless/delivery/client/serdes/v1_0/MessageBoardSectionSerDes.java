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

import com.liferay.headless.delivery.client.dto.v1_0.MessageBoardSection;
import com.liferay.headless.delivery.client.json.BaseJSONParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class MessageBoardSectionSerDes {

	public static MessageBoardSection toDTO(String json) {
		MessageBoardSectionJSONParser messageBoardSectionJSONParser =
			new MessageBoardSectionJSONParser();

		return messageBoardSectionJSONParser.parseToDTO(json);
	}

	public static MessageBoardSection[] toDTOs(String json) {
		MessageBoardSectionJSONParser messageBoardSectionJSONParser =
			new MessageBoardSectionJSONParser();

		return messageBoardSectionJSONParser.parseToDTOs(json);
	}

	public static String toJSON(MessageBoardSection messageBoardSection) {
		if (messageBoardSection == null) {
			return "{}";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"creator\": ");

		if (messageBoardSection.getCreator() == null) {
			sb.append("null");
		}
		else {
			sb.append(messageBoardSection.getCreator());
		}

		sb.append(", ");

		sb.append("\"dateCreated\": ");

		if (messageBoardSection.getDateCreated() == null) {
			sb.append("null");
		}
		else {
			sb.append(messageBoardSection.getDateCreated());
		}

		sb.append(", ");

		sb.append("\"dateModified\": ");

		if (messageBoardSection.getDateModified() == null) {
			sb.append("null");
		}
		else {
			sb.append(messageBoardSection.getDateModified());
		}

		sb.append(", ");

		sb.append("\"description\": ");

		if (messageBoardSection.getDescription() == null) {
			sb.append("null");
		}
		else {
			sb.append(messageBoardSection.getDescription());
		}

		sb.append(", ");

		sb.append("\"id\": ");

		if (messageBoardSection.getId() == null) {
			sb.append("null");
		}
		else {
			sb.append(messageBoardSection.getId());
		}

		sb.append(", ");

		sb.append("\"numberOfMessageBoardSections\": ");

		if (messageBoardSection.getNumberOfMessageBoardSections() == null) {
			sb.append("null");
		}
		else {
			sb.append(messageBoardSection.getNumberOfMessageBoardSections());
		}

		sb.append(", ");

		sb.append("\"numberOfMessageBoardThreads\": ");

		if (messageBoardSection.getNumberOfMessageBoardThreads() == null) {
			sb.append("null");
		}
		else {
			sb.append(messageBoardSection.getNumberOfMessageBoardThreads());
		}

		sb.append(", ");

		sb.append("\"siteId\": ");

		if (messageBoardSection.getSiteId() == null) {
			sb.append("null");
		}
		else {
			sb.append(messageBoardSection.getSiteId());
		}

		sb.append(", ");

		sb.append("\"title\": ");

		if (messageBoardSection.getTitle() == null) {
			sb.append("null");
		}
		else {
			sb.append(messageBoardSection.getTitle());
		}

		sb.append(", ");

		sb.append("\"viewableBy\": ");

		if (messageBoardSection.getViewableBy() == null) {
			sb.append("null");
		}
		else {
			sb.append("\"");
			sb.append(messageBoardSection.getViewableBy());
			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static String toJSON(
		Collection<MessageBoardSection> messageBoardSections) {

		if (messageBoardSections == null) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("[");

		for (MessageBoardSection messageBoardSection : messageBoardSections) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append(toJSON(messageBoardSection));
		}

		sb.append("]");

		return sb.toString();
	}

	private static class MessageBoardSectionJSONParser
		extends BaseJSONParser<MessageBoardSection> {

		protected MessageBoardSection createDTO() {
			return new MessageBoardSection();
		}

		protected MessageBoardSection[] createDTOArray(int size) {
			return new MessageBoardSection[size];
		}

		protected void setField(
			MessageBoardSection messageBoardSection, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "creator")) {
				if (jsonParserFieldValue != null) {
					messageBoardSection.setCreator(
						CreatorSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateCreated")) {
				if (jsonParserFieldValue != null) {
					messageBoardSection.setDateCreated(
						_toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateModified")) {
				if (jsonParserFieldValue != null) {
					messageBoardSection.setDateModified(
						_toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "description")) {
				if (jsonParserFieldValue != null) {
					messageBoardSection.setDescription(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					messageBoardSection.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "numberOfMessageBoardSections")) {

				if (jsonParserFieldValue != null) {
					messageBoardSection.setNumberOfMessageBoardSections(
						(Integer)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "numberOfMessageBoardThreads")) {

				if (jsonParserFieldValue != null) {
					messageBoardSection.setNumberOfMessageBoardThreads(
						(Integer)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "siteId")) {
				if (jsonParserFieldValue != null) {
					messageBoardSection.setSiteId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "title")) {
				if (jsonParserFieldValue != null) {
					messageBoardSection.setTitle((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "viewableBy")) {
				if (jsonParserFieldValue != null) {
					messageBoardSection.setViewableBy(
						MessageBoardSection.ViewableBy.create(
							(String)jsonParserFieldValue));
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