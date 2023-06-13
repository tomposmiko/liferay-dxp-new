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

package com.liferay.apio.architect.internal.documentation;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.hamcrest.collection.IsMapWithSize.anEmptyMap;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import com.liferay.apio.architect.documentation.contributor.CustomDocumentation;
import com.liferay.apio.architect.internal.documentation.contributor.CustomDocumentationImpl;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class DocumentationTest {

	@Test
	public void testDocumentationWithEmptyValuesReturnEmpty() {
		Documentation documentation = new Documentation(
			Optional::empty, Optional::empty, Optional::empty,
			Collections::emptyMap, null, null, null);

		Optional<String> optionalTitle = documentation.getAPITitleOptional();

		Optional<String> optionalDescription =
			documentation.getAPIDescriptionOptional();

		assertThat(optionalTitle, is(emptyOptional()));
		assertThat(optionalDescription, is(emptyOptional()));

		assertThat(documentation.getRepresentors(), is(anEmptyMap()));
	}

	@Test
	public void testDocumentationWithNonemptyValuesReturnThem() {
		CustomDocumentation.Builder dummyCustomDocumentationBuilder =
			new CustomDocumentationImpl.BuilderImpl();

		dummyCustomDocumentationBuilder.addDescription("key", "value");

		Documentation documentation = new Documentation(
			() -> Optional.of(() -> "A"), () -> Optional.of(() -> "B"),
			() -> Optional.of(() -> "C"),
			() -> Collections.singletonMap("r", null), null, null,
			dummyCustomDocumentationBuilder::build);

		Optional<String> optionalTitle = documentation.getAPITitleOptional();
		Optional<String> optionalDescription =
			documentation.getAPIDescriptionOptional();
		Optional<String> optionalApplicationURL =
			documentation.getEntryPointOptional();

		CustomDocumentation customDocumentation =
			documentation.getCustomDocumentation();

		Function<Locale, String> descriptionFunction =
			customDocumentation.getDescriptionFunction("key");

		assertThat(optionalTitle, is(optionalWithValue(equalTo("A"))));
		assertThat(optionalDescription, is(optionalWithValue(equalTo("B"))));
		assertThat(optionalApplicationURL, is(optionalWithValue(equalTo("C"))));
		assertThat(documentation.getRepresentors(), is(aMapWithSize(1)));
		assertThat(documentation.getRepresentors(), hasKey("r"));
		assertThat(descriptionFunction.apply(null), is("value"));
	}

}