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

package com.liferay.portal.tools.rest.builder.internal.freemarker.util;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.tools.rest.builder.internal.freemarker.tool.java.JavaMethodSignature;
import com.liferay.portal.tools.rest.builder.internal.freemarker.tool.java.parser.GraphQLOpenAPIParser;
import com.liferay.portal.tools.rest.builder.internal.freemarker.tool.java.parser.util.OpenAPIParserUtil;
import com.liferay.portal.vulcan.yaml.config.ConfigYAML;
import com.liferay.portal.vulcan.yaml.openapi.Components;
import com.liferay.portal.vulcan.yaml.openapi.Info;
import com.liferay.portal.vulcan.yaml.openapi.Items;
import com.liferay.portal.vulcan.yaml.openapi.OpenAPIYAML;
import com.liferay.portal.vulcan.yaml.openapi.Operation;
import com.liferay.portal.vulcan.yaml.openapi.Schema;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Peter Shin
 */
public class OpenAPIUtil {

	public static String escapeVersion(OpenAPIYAML openAPIYAML) {
		Info info = openAPIYAML.getInfo();

		String version = info.getVersion();

		if (Validator.isNull(version)) {
			return null;
		}

		Matcher matcher = _nondigitPattern.matcher(version);

		String escapedVersion = matcher.replaceAll("_");

		matcher = _leadingUnderscorePattern.matcher(escapedVersion);

		return "v" + matcher.replaceFirst("");
	}

	public static String formatSingular(String s) {
		if (s.endsWith("ases")) {
			s = s.substring(0, s.length() - 1);
		}
		else if (s.endsWith("ses")) {
			s = s.substring(0, s.length() - 2);
		}
		else if (s.endsWith("ies")) {
			s = s.substring(0, s.length() - 3) + "y";
		}
		else if (s.endsWith("s")) {
			s = s.substring(0, s.length() - 1);
		}

		return s;
	}

	public static Map<String, Schema> getAllSchemas(OpenAPIYAML openAPIYAML) {
		Map<String, Schema> allSchemas = new TreeMap<>();

		Queue<Map<String, Schema>> queue = new LinkedList<>();

		Components components = openAPIYAML.getComponents();

		queue.add(components.getSchemas());

		Map<String, Schema> map = null;

		while ((map = queue.poll()) != null) {
			for (Map.Entry<String, Schema> entry : map.entrySet()) {
				Schema schema = entry.getValue();

				Map<String, Schema> propertySchemas = null;

				Items items = schema.getItems();

				if (items != null) {
					propertySchemas = items.getPropertySchemas();
				}
				else if (schema.getAllOfSchemas() != null) {
					propertySchemas = OpenAPIParserUtil.getAllOfPropertySchemas(
						schema);
				}
				else {
					propertySchemas = schema.getPropertySchemas();
				}

				if (propertySchemas == null) {
					continue;
				}

				String schemaName = StringUtil.upperCaseFirstLetter(
					entry.getKey());

				if (items != null) {
					schemaName = formatSingular(schemaName);
				}

				allSchemas.put(schemaName, schema);

				if (schema.getOneOfSchemas() != null) {
					for (Schema oneOfSchema : schema.getOneOfSchemas()) {
						Map<String, Schema> schemas =
							oneOfSchema.getPropertySchemas();

						Set<String> keys = schemas.keySet();

						Iterator<String> iterator = keys.iterator();

						String schemaKey = StringUtil.upperCaseFirstLetter(
							iterator.next());

						if (!allSchemas.containsKey(schemaKey)) {
							allSchemas.put(schemaKey, oneOfSchema);

							queue.add(schemas);
						}
					}
				}

				queue.add(propertySchemas);
			}
		}

		return allSchemas;
	}

	public static Map<String, Schema> getGlobalEnumSchemas(
		OpenAPIYAML openAPIYAML) {

		Map<String, Schema> globalEnumSchemas = new TreeMap<>();

		Components components = openAPIYAML.getComponents();

		Map<String, Schema> schemas = components.getSchemas();

		for (Map.Entry<String, Schema> entry : schemas.entrySet()) {
			Schema schema = entry.getValue();

			Map<String, Schema> propertySchemas = null;

			Items items = schema.getItems();

			if (items != null) {
				propertySchemas = items.getPropertySchemas();
			}
			else if (schema.getAllOfSchemas() != null) {
				propertySchemas = OpenAPIParserUtil.getAllOfPropertySchemas(
					schema);
			}
			else {
				propertySchemas = schema.getPropertySchemas();
			}

			if ((propertySchemas == null) && (schema.getEnumValues() != null)) {
				String schemaName = StringUtil.upperCaseFirstLetter(
					entry.getKey());

				if (items != null) {
					schemaName = formatSingular(schemaName);
				}

				globalEnumSchemas.put(schemaName, schema);
			}
		}

		return globalEnumSchemas;
	}

	public static List<JavaMethodSignature> getJavaMethodSignatures(
		ConfigYAML configYAML, OpenAPIYAML openAPIYAML,
		Predicate<Operation> predicate) {

		return GraphQLOpenAPIParser.getJavaMethodSignatures(
			configYAML, openAPIYAML, predicate);
	}

	private static final Pattern _leadingUnderscorePattern = Pattern.compile(
		"^_+");
	private static final Pattern _nondigitPattern = Pattern.compile("\\D");

}