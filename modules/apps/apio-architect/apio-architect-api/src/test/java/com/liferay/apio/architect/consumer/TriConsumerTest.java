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

package com.liferay.apio.architect.consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class TriConsumerTest {

	@Test
	public void testOnInvokingAndThenShouldExecuteBothFunctions() {
		List<String> list = new ArrayList<>();

		TriConsumer<String, String, String> triConsumer =
			(string1, string2, string3) -> {
				list.add(string1);
				list.add(string2);
				list.add(string3);
			};

		triConsumer.andThen(
			triConsumer
		).accept(
			"| Live", " long and", " prosper |"
		);

		String string = String.join("", list);

		assertThat(
			string, is("| Live long and prosper || Live long and prosper |"));
	}

	@Test(expected = NullPointerException.class)
	public void testOnInvokingAndThenWithNullAfterThrowsException() {
		TriConsumer.empty().andThen(null);
	}

}