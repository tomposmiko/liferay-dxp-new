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
public class ThrowableOctaFunctionTest {

	@Test
	public void testOnInvokingAndThenShouldExecuteBothFunctions()
		throws Exception {

		ThrowableOctaFunction<String, String, String, String, String, String,
			String, String, String> throwableOctaFunction = (
				string1, string2, string3, string4, string5, string6, string7,
				string8) ->
					string1 + string2 + string3 + string4 + string5 + string6 +
						string7 + string8;

		ThrowableFunction<String, String> throwableFunction =
			string -> string + "prosper";

		String string = throwableOctaFunction.andThen(
			throwableFunction
		).apply(
			"Li", "ve", " ", "lo", "ng", " ", "and", " "
		);

		assertThat(string, is("Live long and prosper"));
	}

	@Test(expected = NullPointerException.class)
	public void testOnInvokingAndThenWithNullAfterFunctionThrowsException() {
		ThrowableOctaFunction<String, String, String, String, String, String,
			String, String, String> throwableOctaFunction = (
				string1, string2, string3, string4, string5, string6, string7,
				string8) ->
					string1 + string2 + string3 + string4 + string5 + string6 +
						string7 + string8;

		throwableOctaFunction.andThen(null);
	}

}