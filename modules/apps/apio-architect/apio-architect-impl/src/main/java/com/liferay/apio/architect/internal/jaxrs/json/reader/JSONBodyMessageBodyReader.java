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

package com.liferay.apio.architect.internal.jaxrs.json.reader;

import static java.nio.charset.StandardCharsets.UTF_8;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.internal.form.JSONBodyImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;

/**
 * Reads JSON objects as a {@link Body}.
 *
 * @author Alejandro Hernández
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(liferay.apio.architect.application=true)",
		"osgi.jaxrs.extension=true"
	},
	service = MessageBodyReader.class
)
@Consumes(APPLICATION_JSON)
@Provider
public class JSONBodyMessageBodyReader implements MessageBodyReader<Body> {

	@Override
	public boolean isReadable(
		Class<?> type, Type genericType, Annotation[] annotations,
		MediaType mediaType) {

		return true;
	}

	@Override
	public Body readFrom(
			Class<Body> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream)
		throws IOException {

		return _getBody(entityStream);
	}

	private static Body _getBody(InputStream entityStream) {
		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonNode = Try.fromFallibleWithResources(
			() -> new InputStreamReader(entityStream, UTF_8),
			objectMapper::readTree
		).filter(
			node -> node.isObject() || node.isArray()
		).orElseThrow(
			() -> new BadRequestException("Body is not a valid JSON")
		);

		if (jsonNode.isObject()) {
			return _getBody((ObjectNode)jsonNode);
		}

		return _getListBody((ArrayNode)jsonNode);
	}

	private static Body _getBody(ObjectNode objectNode) {
		return new JSONBodyImpl(objectNode);
	}

	private static Body _getListBody(ArrayNode arrayNode) {
		return new JSONBodyImpl(arrayNode);
	}

}