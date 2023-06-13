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

package com.liferay.headless.foundation.client.serdes.v1_0;

import com.liferay.headless.foundation.client.dto.v1_0.Segment;
import com.liferay.headless.foundation.client.json.BaseJSONParser;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class SegmentSerDes {

	public static Segment toDTO(String json) {
		SegmentJSONParser segmentJSONParser = new SegmentJSONParser();

		return segmentJSONParser.parseToDTO(json);
	}

	public static Segment[] toDTOs(String json) {
		SegmentJSONParser segmentJSONParser = new SegmentJSONParser();

		return segmentJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Segment segment) {
		if (segment == null) {
			return "{}";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"active\": ");

		sb.append(segment.getActive());
		sb.append(", ");

		sb.append("\"contentSpaceId\": ");

		sb.append(segment.getContentSpaceId());
		sb.append(", ");

		sb.append("\"criteria\": ");

		sb.append("\"");
		sb.append(segment.getCriteria());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"dateCreated\": ");

		sb.append("\"");
		sb.append(segment.getDateCreated());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"dateModified\": ");

		sb.append("\"");
		sb.append(segment.getDateModified());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"id\": ");

		sb.append(segment.getId());
		sb.append(", ");

		sb.append("\"name\": ");

		sb.append("\"");
		sb.append(segment.getName());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"source\": ");

		sb.append("\"");
		sb.append(segment.getSource());
		sb.append("\"");

		sb.append("}");

		return sb.toString();
	}

	public static String toJSON(Collection<Segment> segments) {
		if (segments == null) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("[");

		for (Segment segment : segments) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append(toJSON(segment));
		}

		sb.append("]");

		return sb.toString();
	}

	private static class SegmentJSONParser extends BaseJSONParser<Segment> {

		protected Segment createDTO() {
			return new Segment();
		}

		protected Segment[] createDTOArray(int size) {
			return new Segment[size];
		}

		protected void setField(
			Segment segment, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "active")) {
				if (jsonParserFieldValue != null) {
					segment.setActive((Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "contentSpaceId")) {
				if (jsonParserFieldValue != null) {
					segment.setContentSpaceId((Long)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "criteria")) {
				if (jsonParserFieldValue != null) {
					segment.setCriteria((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateCreated")) {
				if (jsonParserFieldValue != null) {
					segment.setDateCreated((Date)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateModified")) {
				if (jsonParserFieldValue != null) {
					segment.setDateModified((Date)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					segment.setId((Long)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					segment.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "source")) {
				if (jsonParserFieldValue != null) {
					segment.setSource((String)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}