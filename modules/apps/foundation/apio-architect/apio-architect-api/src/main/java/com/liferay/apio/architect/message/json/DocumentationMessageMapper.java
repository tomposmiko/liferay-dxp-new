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

package com.liferay.apio.architect.message.json;

import aQute.bnd.annotation.ConsumerType;

import com.liferay.apio.architect.documentation.Documentation;

import javax.ws.rs.core.HttpHeaders;

/**
 * Maps the API {@link Documentation} to its JSON object representation.
 * Instances of this interface work like events. The {@code
 * javax.ws.rs.ext.MessageBodyWriter} of the {@code Documentation} calls the
 * {@code DocumentationMessageMapper} methods. In each method, developers should
 * only map the provided part of the resource to its representation in a JSON
 * object. To enable this, each method receives a {@link JSONObjectBuilder}.
 *
 * <p>
 * The methods {@link #onStart(JSONObjectBuilder, Documentation, HttpHeaders)}
 * and {@link #onFinish(JSONObjectBuilder, Documentation, HttpHeaders)} are
 * called when the writer starts and finishes the page, respectively. Otherwise,
 * the page message mapper's methods aren't called in a particular order.
 * </p>
 *
 * @author Alejandro Hernández
 */
@ConsumerType
@SuppressWarnings("unused")
public interface DocumentationMessageMapper {

	/**
	 * Returns the media type the mapper represents.
	 *
	 * @return the media type the mapper represents
	 */
	public String getMediaType();

	/**
	 * Maps the API description to its JSON object representation.
	 *
	 * @param jsonObjectBuilder the JSON object builder for the documentation
	 * @param description the API description
	 */
	public default void mapDescription(
		JSONObjectBuilder jsonObjectBuilder, String description) {
	}

	/**
	 * Maps the API title to its JSON object representation.
	 *
	 * @param jsonObjectBuilder the JSON object builder for the documentation
	 * @param title the API title
	 */
	public default void mapTitle(
		JSONObjectBuilder jsonObjectBuilder, String title) {
	}

	/**
	 * Finishes the documentation. This is the final documentation message
	 * mapper method the writer calls.
	 *
	 * @param jsonObjectBuilder the JSON object builder for the documentation
	 * @param documentation the documentation
	 * @param httpHeaders the current request's HTTP headers
	 */
	public default void onFinish(
		JSONObjectBuilder jsonObjectBuilder, Documentation documentation,
		HttpHeaders httpHeaders) {
	}

	/**
	 * Starts the documentation. This is the first documentation message mapper
	 * method the writer calls for the documentation.
	 *
	 * @param jsonObjectBuilder the JSON object builder for the documentation
	 * @param documentation the documentation
	 * @param httpHeaders the current request's HTTP headers
	 */
	public default void onStart(
		JSONObjectBuilder jsonObjectBuilder, Documentation documentation,
		HttpHeaders httpHeaders) {
	}

	/**
	 * Returns {@code true} if the mapper can map all things related to the
	 * current request.
	 *
	 * @param  documentation the documentation
	 * @param  httpHeaders the current request's HTTP headers
	 * @return {@code true} if the mapper can map the request; {@code false}
	 *         otherwise
	 */
	public default boolean supports(
		Documentation documentation, HttpHeaders httpHeaders) {

		return true;
	}

}