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

package com.liferay.apio.architect.function.throwable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class ThrowableEnneaFunctionTest {

	@Test
	public void testOnInvokingAndThenShouldExecuteBothFunctions()
		throws Exception {

		ThrowableEnneaFunction
			<String, String, String, String, String, String,
			 String, String, String, String> throwableEnneaFunction = (
				string1, string2, string3, string4, string5, string6,
				string7, string8, string9) ->
					string1 + string2 + string3 + string4 + string5 + string6 +
						string7 + string8 + string9;

		ThrowableFunction<String, String> throwableFunction =
			string -> string + "prosper";

		String string = throwableEnneaFunction.andThen(
			throwableFunction
		).apply(
			"Li", "ve", " ", "lo", "ng", " ", "an", "d", " "
		);

		assertThat(string, is("Live long and prosper"));
	}

	@Test(expected = NullPointerException.class)
	public void testOnInvokingAndThenWithNullAfterFunctionThrowsException() {
		ThrowableEnneaFunction
			<String, String, String, String, String, String,
			 String, String, String, String> throwableEnneaFunction = (
				string1, string2, string3, string4, string5, string6,
				string7, string8, string9) ->
					string1 + string2 + string3 + string4 + string5 + string6 +
						string7 + string8 + string9;

		throwableEnneaFunction.andThen(null);
	}

}