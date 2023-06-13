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

import com.liferay.headless.delivery.client.dto.v1_0.KnowledgeBaseFolder;
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
public class KnowledgeBaseFolderSerDes {

	public static KnowledgeBaseFolder toDTO(String json) {
		KnowledgeBaseFolderJSONParser knowledgeBaseFolderJSONParser =
			new KnowledgeBaseFolderJSONParser();

		return knowledgeBaseFolderJSONParser.parseToDTO(json);
	}

	public static KnowledgeBaseFolder[] toDTOs(String json) {
		KnowledgeBaseFolderJSONParser knowledgeBaseFolderJSONParser =
			new KnowledgeBaseFolderJSONParser();

		return knowledgeBaseFolderJSONParser.parseToDTOs(json);
	}

	public static String toJSON(KnowledgeBaseFolder knowledgeBaseFolder) {
		if (knowledgeBaseFolder == null) {
			return "{}";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"creator\": ");

		if (knowledgeBaseFolder.getCreator() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseFolder.getCreator());
		}

		sb.append(", ");

		sb.append("\"dateCreated\": ");

		if (knowledgeBaseFolder.getDateCreated() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseFolder.getDateCreated());
		}

		sb.append(", ");

		sb.append("\"dateModified\": ");

		if (knowledgeBaseFolder.getDateModified() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseFolder.getDateModified());
		}

		sb.append(", ");

		sb.append("\"description\": ");

		if (knowledgeBaseFolder.getDescription() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseFolder.getDescription());
		}

		sb.append(", ");

		sb.append("\"id\": ");

		if (knowledgeBaseFolder.getId() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseFolder.getId());
		}

		sb.append(", ");

		sb.append("\"name\": ");

		if (knowledgeBaseFolder.getName() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseFolder.getName());
		}

		sb.append(", ");

		sb.append("\"numberOfKnowledgeBaseArticles\": ");

		if (knowledgeBaseFolder.getNumberOfKnowledgeBaseArticles() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseFolder.getNumberOfKnowledgeBaseArticles());
		}

		sb.append(", ");

		sb.append("\"numberOfKnowledgeBaseFolders\": ");

		if (knowledgeBaseFolder.getNumberOfKnowledgeBaseFolders() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseFolder.getNumberOfKnowledgeBaseFolders());
		}

		sb.append(", ");

		sb.append("\"parentKnowledgeBaseFolder\": ");

		if (knowledgeBaseFolder.getParentKnowledgeBaseFolder() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseFolder.getParentKnowledgeBaseFolder());
		}

		sb.append(", ");

		sb.append("\"parentKnowledgeBaseFolderId\": ");

		if (knowledgeBaseFolder.getParentKnowledgeBaseFolderId() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseFolder.getParentKnowledgeBaseFolderId());
		}

		sb.append(", ");

		sb.append("\"siteId\": ");

		if (knowledgeBaseFolder.getSiteId() == null) {
			sb.append("null");
		}
		else {
			sb.append(knowledgeBaseFolder.getSiteId());
		}

		sb.append(", ");

		sb.append("\"viewableBy\": ");

		if (knowledgeBaseFolder.getViewableBy() == null) {
			sb.append("null");
		}
		else {
			sb.append("\"");
			sb.append(knowledgeBaseFolder.getViewableBy());
			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static String toJSON(
		Collection<KnowledgeBaseFolder> knowledgeBaseFolders) {

		if (knowledgeBaseFolders == null) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("[");

		for (KnowledgeBaseFolder knowledgeBaseFolder : knowledgeBaseFolders) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append(toJSON(knowledgeBaseFolder));
		}

		sb.append("]");

		return sb.toString();
	}

	private static class KnowledgeBaseFolderJSONParser
		extends BaseJSONParser<KnowledgeBaseFolder> {

		protected KnowledgeBaseFolder createDTO() {
			return new KnowledgeBaseFolder();
		}

		protected KnowledgeBaseFolder[] createDTOArray(int size) {
			return new KnowledgeBaseFolder[size];
		}

		protected void setField(
			KnowledgeBaseFolder knowledgeBaseFolder, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "creator")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseFolder.setCreator(
						CreatorSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateCreated")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseFolder.setDateCreated(
						_toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateModified")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseFolder.setDateModified(
						_toDate((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "description")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseFolder.setDescription(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseFolder.setId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseFolder.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "numberOfKnowledgeBaseArticles")) {

				if (jsonParserFieldValue != null) {
					knowledgeBaseFolder.setNumberOfKnowledgeBaseArticles(
						(Number)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "numberOfKnowledgeBaseFolders")) {

				if (jsonParserFieldValue != null) {
					knowledgeBaseFolder.setNumberOfKnowledgeBaseFolders(
						(Number)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "parentKnowledgeBaseFolder")) {

				if (jsonParserFieldValue != null) {
					knowledgeBaseFolder.setParentKnowledgeBaseFolder(
						ParentKnowledgeBaseFolderSerDes.toDTO(
							(String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(
						jsonParserFieldName, "parentKnowledgeBaseFolderId")) {

				if (jsonParserFieldValue != null) {
					knowledgeBaseFolder.setParentKnowledgeBaseFolderId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "siteId")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseFolder.setSiteId(
						Long.valueOf((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "viewableBy")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseFolder.setViewableBy(
						KnowledgeBaseFolder.ViewableBy.create(
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