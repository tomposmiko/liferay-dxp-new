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

package com.liferay.apio.architect.exception.mapper.internal;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.liferay.apio.architect.error.APIError;
import com.liferay.apio.architect.exception.mapper.ExceptionMapper;

import javax.ws.rs.NotSupportedException;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class NotSupportedExceptionMapperTest {

	@Test
	public void testConvertReturnsCorrectAPIErrorWithExceptionMessage() {
		ExceptionMapper<NotSupportedException> exceptionMapper =
			new NotSupportedExceptionMapper();

		NotSupportedException notSupportedException = new NotSupportedException(
			"Message");

		APIError apiError = exceptionMapper.map(notSupportedException);

		assertThat(
			apiError.getDescription(),
			is(optionalWithValue(equalTo("Message"))));
		assertThat(apiError.getException(), is(notSupportedException));
		assertThat(apiError.getMessage(), is("Message"));
		assertThat(apiError.getStatusCode(), is(415));
		assertThat(apiError.getTitle(), is("Unsupported Media Type"));
		assertThat(apiError.getType(), is("not-supported"));
	}

	@Test
	public void testConvertReturnsCorrectAPIErrorWithoutExceptionMessage() {
		ExceptionMapper<NotSupportedException> exceptionMapper =
			new NotSupportedExceptionMapper();

		NotSupportedException notSupportedException =
			new NotSupportedException();

		APIError apiError = exceptionMapper.map(notSupportedException);

		assertThat(apiError.getDescription(), is(emptyOptional()));
		assertThat(apiError.getException(), is(notSupportedException));
		assertThat(
			apiError.getMessage(), is("HTTP 415 Unsupported Media Type"));
		assertThat(apiError.getStatusCode(), is(415));
		assertThat(apiError.getTitle(), is("Unsupported Media Type"));
		assertThat(apiError.getType(), is("not-supported"));
	}

}