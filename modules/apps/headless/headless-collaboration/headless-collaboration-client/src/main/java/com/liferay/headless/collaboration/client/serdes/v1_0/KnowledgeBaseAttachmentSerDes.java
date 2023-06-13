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

package com.liferay.headless.collaboration.client.serdes.v1_0;

import com.liferay.headless.collaboration.client.dto.v1_0.KnowledgeBaseAttachment;
import com.liferay.headless.collaboration.client.json.BaseJSONParser;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class KnowledgeBaseAttachmentSerDes {

	public static KnowledgeBaseAttachment toDTO(String json) {
		KnowledgeBaseAttachmentJSONParser knowledgeBaseAttachmentJSONParser =
			new KnowledgeBaseAttachmentJSONParser();

		return knowledgeBaseAttachmentJSONParser.parseToDTO(json);
	}

	public static KnowledgeBaseAttachment[] toDTOs(String json) {
		KnowledgeBaseAttachmentJSONParser knowledgeBaseAttachmentJSONParser =
			new KnowledgeBaseAttachmentJSONParser();

		return knowledgeBaseAttachmentJSONParser.parseToDTOs(json);
	}

	public static String toJSON(
		KnowledgeBaseAttachment knowledgeBaseAttachment) {

		if (knowledgeBaseAttachment == null) {
			return "{}";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"contentUrl\": ");

		sb.append("\"");
		sb.append(knowledgeBaseAttachment.getContentUrl());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"encodingFormat\": ");

		sb.append("\"");
		sb.append(knowledgeBaseAttachment.getEncodingFormat());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"fileExtension\": ");

		sb.append("\"");
		sb.append(knowledgeBaseAttachment.getFileExtension());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"id\": ");

		sb.append(knowledgeBaseAttachment.getId());
		sb.append(", ");

		sb.append("\"sizeInBytes\": ");

		sb.append(knowledgeBaseAttachment.getSizeInBytes());
		sb.append(", ");

		sb.append("\"title\": ");

		sb.append("\"");
		sb.append(knowledgeBaseAttachment.getTitle());
		sb.append("\"");

		sb.append("}");

		return sb.toString();
	}

	public static String toJSON(
		Collection<KnowledgeBaseAttachment> knowledgeBaseAttachments) {

		if (knowledgeBaseAttachments == null) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("[");

		for (KnowledgeBaseAttachment knowledgeBaseAttachment :
				knowledgeBaseAttachments) {

			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append(toJSON(knowledgeBaseAttachment));
		}

		sb.append("]");

		return sb.toString();
	}

	private static class KnowledgeBaseAttachmentJSONParser
		extends BaseJSONParser<KnowledgeBaseAttachment> {

		protected KnowledgeBaseAttachment createDTO() {
			return new KnowledgeBaseAttachment();
		}

		protected KnowledgeBaseAttachment[] createDTOArray(int size) {
			return new KnowledgeBaseAttachment[size];
		}

		protected void setField(
			KnowledgeBaseAttachment knowledgeBaseAttachment,
			String jsonParserFieldName, Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "contentUrl")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseAttachment.setContentUrl(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "encodingFormat")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseAttachment.setEncodingFormat(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "fileExtension")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseAttachment.setFileExtension(
						(String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseAttachment.setId((Long)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "sizeInBytes")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseAttachment.setSizeInBytes(
						(Number)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "title")) {
				if (jsonParserFieldValue != null) {
					knowledgeBaseAttachment.setTitle(
						(String)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}