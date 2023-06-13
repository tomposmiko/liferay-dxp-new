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

package com.liferay.headless.admin.taxonomy.client.serdes.v1_0;

import com.liferay.headless.admin.taxonomy.client.dto.v1_0.AssetType;
import com.liferay.headless.admin.taxonomy.client.json.BaseJSONParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class AssetTypeSerDes {

	public static AssetType toDTO(String json) {
		AssetTypeJSONParser assetTypeJSONParser = new AssetTypeJSONParser();

		return assetTypeJSONParser.parseToDTO(json);
	}

	public static AssetType[] toDTOs(String json) {
		AssetTypeJSONParser assetTypeJSONParser = new AssetTypeJSONParser();

		return assetTypeJSONParser.parseToDTOs(json);
	}

	public static String toJSON(AssetType assetType) {
		if (assetType == null) {
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{");

		if (assetType.getRequired() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"required\":");

			sb.append(assetType.getRequired());
		}

		if (assetType.getSubtype() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"subtype\":");

			sb.append("\"");

			sb.append(assetType.getSubtype());

			sb.append("\"");
		}

		if (assetType.getType() != null) {
			if (sb.length() > 1) {
				sb.append(", ");
			}

			sb.append("\"type\":");

			sb.append("\"");

			sb.append(assetType.getType());

			sb.append("\"");
		}

		sb.append("}");

		return sb.toString();
	}

	public static Map<String, String> toMap(AssetType assetType) {
		if (assetType == null) {
			return null;
		}

		Map<String, String> map = new HashMap<>();

		if (assetType.getRequired() == null) {
			map.put("required", null);
		}
		else {
			map.put("required", String.valueOf(assetType.getRequired()));
		}

		if (assetType.getSubtype() == null) {
			map.put("subtype", null);
		}
		else {
			map.put("subtype", String.valueOf(assetType.getSubtype()));
		}

		if (assetType.getType() == null) {
			map.put("type", null);
		}
		else {
			map.put("type", String.valueOf(assetType.getType()));
		}

		return map;
	}

	private static class AssetTypeJSONParser extends BaseJSONParser<AssetType> {

		@Override
		protected AssetType createDTO() {
			return new AssetType();
		}

		@Override
		protected AssetType[] createDTOArray(int size) {
			return new AssetType[size];
		}

		@Override
		protected void setField(
			AssetType assetType, String jsonParserFieldName,
			Object jsonParserFieldValue) {

			if (Objects.equals(jsonParserFieldName, "required")) {
				if (jsonParserFieldValue != null) {
					assetType.setRequired((Boolean)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "subtype")) {
				if (jsonParserFieldValue != null) {
					assetType.setSubtype((String)jsonParserFieldValue);
				}
			}
			else if (Objects.equals(jsonParserFieldName, "type")) {
				if (jsonParserFieldValue != null) {
					assetType.setType((String)jsonParserFieldValue);
				}
			}
			else {
				throw new IllegalArgumentException(
					"Unsupported field name " + jsonParserFieldName);
			}
		}

	}

}