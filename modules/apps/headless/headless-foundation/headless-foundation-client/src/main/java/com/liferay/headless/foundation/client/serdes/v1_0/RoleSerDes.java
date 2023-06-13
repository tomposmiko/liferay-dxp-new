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

import com.liferay.headless.foundation.client.dto.v1_0.Role;
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
public class RoleSerDes {

	public static Role toDTO(String json) {
		RoleJSONParser roleJSONParser = new RoleJSONParser();

		return roleJSONParser.parseToDTO(json);
	}

	public static Role[] toDTOs(String json) {
		RoleJSONParser roleJSONParser = new RoleJSONParser();

		return roleJSONParser.parseToDTOs(json);
	}

	public static String toJSON(Role role) {
		if (role == null) {
			return "{}";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		sb.append("\"availableLanguages\": ");

		if (role.getAvailableLanguages() == null) {
			sb.append("null");
		}
		else {
			sb.append("[");

			for (int i = 0; i < role.getAvailableLanguages().length; i++) {
				sb.append("\"");
				sb.append(role.getAvailableLanguages()[i]);
				sb.append("\"");

				if ((i + 1) < role.getAvailableLanguages().length) {
					sb.append(", ");
				}
			}

			sb.append("]");
		}

		sb.append(", ");

		sb.append("\"creator\": ");

		sb.append(role.getCreator());
		sb.append(", ");

		sb.append("\"dateCreated\": ");

		sb.append("\"");
		sb.append(role.getDateCreated());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"dateModified\": ");

		sb.append("\"");
		sb.append(role.getDateModified());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"description\": ");

		sb.append("\"");
		sb.append(role.getDescription());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"id\": ");

		sb.append(role.getId());
		sb.append(", ");

		sb.append("\"name\": ");

		sb.append("\"");
		sb.append(role.getName());
		sb.append("\"");
		sb.append(", ");

		sb.append("\"roleType\": ");

		sb.append("\"");
		sb.append(role.getRoleType());
		sb.append("\"");

		sb.append("}");

		return sb.toString();
	}

	public static String toJSON(Collection<Role> roles) {
		if (roles == null) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("[");

		for (Role role : roles) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append(toJSON(role));
		}

		sb.append("]");

		return sb.toString();
	}

	private static class RoleJSONParser extends BaseJSONParser<Role> {

		protected Role createDTO() {
			return new Role();
		}

		protected Role[] createDTOArray(int size) {
			return new Role[size];
		}

		protected void setField(
			Role role, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "availableLanguages")) {
				if (jsonParserFieldValue != null) {
					role.setAvailableLanguages(
						toStrings((Object[])jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "creator")) {
				if (jsonParserFieldValue != null) {
					role.setCreator(
						CreatorSerDes.toDTO((String)jsonParserFieldValue));
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateCreated")) {
				if (jsonParserFieldValue != null) {
					role.setDateCreated((Date)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "dateModified")) {
				if (jsonParserFieldValue != null) {
					role.setDateModified((Date)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "description")) {
				if (jsonParserFieldValue != null) {
					role.setDescription((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "id")) {
				if (jsonParserFieldValue != null) {
					role.setId((Long)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "name")) {
				if (jsonParserFieldValue != null) {
					role.setName((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "roleType")) {
				if (jsonParserFieldValue != null) {
					role.setRoleType((String)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}