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

package com.liferay.portal.tools.rest.builder.internal.freemarker.tool.java.parser;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.tools.rest.builder.internal.freemarker.tool.java.JavaMethodParameter;
import com.liferay.portal.tools.rest.builder.internal.freemarker.tool.java.JavaMethodSignature;
import com.liferay.portal.tools.rest.builder.internal.freemarker.tool.java.parser.util.OpenAPIParserUtil;
import com.liferay.portal.vulcan.yaml.config.ConfigYAML;
import com.liferay.portal.vulcan.yaml.openapi.Content;
import com.liferay.portal.vulcan.yaml.openapi.OpenAPIYAML;
import com.liferay.portal.vulcan.yaml.openapi.Operation;
import com.liferay.portal.vulcan.yaml.openapi.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Peter Shin
 */
public class ResourceTestCaseOpenAPIParser {

	public static List<JavaMethodSignature> getJavaMethodSignatures(
		ConfigYAML configYAML, OpenAPIYAML openAPIYAML, String schemaName) {

		List<JavaMethodSignature> javaMethodSignatures = new ArrayList<>();

		List<JavaMethodSignature> resourceJavaMethodSignatures =
			ResourceOpenAPIParser.getJavaMethodSignatures(
				configYAML, openAPIYAML, schemaName);

		for (JavaMethodSignature resourceJavaMethodSignature :
				resourceJavaMethodSignatures) {

			javaMethodSignatures.add(
				new JavaMethodSignature(
					resourceJavaMethodSignature.getPath(),
					resourceJavaMethodSignature.getPathItem(),
					resourceJavaMethodSignature.getOperation(),
					resourceJavaMethodSignature.getRequestBodyMediaTypes(),
					resourceJavaMethodSignature.getSchemaName(),
					resourceJavaMethodSignature.getJavaMethodParameters(),
					_getMethodName(resourceJavaMethodSignature),
					resourceJavaMethodSignature.getReturnType()));
		}

		return javaMethodSignatures;
	}

	public static String getParameters(
		List<JavaMethodParameter> javaMethodParameters, OpenAPIYAML openAPIYAML,
		Operation operation, boolean annotation) {

		return ResourceOpenAPIParser.getParameters(
			javaMethodParameters, openAPIYAML, operation, annotation);
	}

	private static String _getMethodName(
		JavaMethodSignature javaMethodSignature) {

		Operation operation = javaMethodSignature.getOperation();
		Set<String> requestBodyMediaTypes =
			javaMethodSignature.getRequestBodyMediaTypes();

		List<String> mediaTypes = new ArrayList<>();

		if (operation.getRequestBody() != null) {
			RequestBody requestBody = operation.getRequestBody();

			if (requestBody.getContent() != null) {
				Map<String, Content> contents = requestBody.getContent();

				mediaTypes.addAll(contents.keySet());
			}
		}

		if (operation.getOperationId() != null) {
			String operationId = operation.getOperationId();

			if (!requestBodyMediaTypes.contains("multipart/form-data") ||
				(mediaTypes.size() < 2)) {

				return operationId;
			}

			int index = 0;

			for (int i = 0; i < operationId.length(); i++) {
				if (Character.isUpperCase(operationId.charAt(i))) {
					index = i;

					break;
				}
			}

			StringBuilder sb = new StringBuilder();

			sb.append(operationId.substring(0, index));
			sb.append("FormData");
			sb.append(operationId.substring(index));

			return sb.toString();
		}

		String methodName = javaMethodSignature.getMethodName();

		if (requestBodyMediaTypes.contains("multipart/form-data") &&
			(mediaTypes.size() > 1)) {

			String httpMethod = OpenAPIParserUtil.getHTTPMethod(operation);

			return StringUtil.replaceFirst(
				methodName, httpMethod, httpMethod + "FormData");
		}

		return methodName;
	}

}