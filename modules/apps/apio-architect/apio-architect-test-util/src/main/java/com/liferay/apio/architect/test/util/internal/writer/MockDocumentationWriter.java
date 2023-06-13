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

package com.liferay.apio.architect.test.util.internal.writer;

import static com.liferay.apio.architect.test.util.writer.MockWriterUtil.getRequestInfo;

import com.liferay.apio.architect.documentation.contributor.CustomDocumentation;
import com.liferay.apio.architect.internal.documentation.Documentation;
import com.liferay.apio.architect.internal.documentation.contributor.CustomDocumentationImpl;
import com.liferay.apio.architect.internal.message.json.DocumentationMessageMapper;
import com.liferay.apio.architect.internal.routes.CollectionRoutesImpl;
import com.liferay.apio.architect.internal.routes.ItemRoutesImpl;
import com.liferay.apio.architect.internal.routes.NestedCollectionRoutesImpl;
import com.liferay.apio.architect.internal.writer.DocumentationWriter;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.apio.architect.test.util.model.RootModel;
import com.liferay.apio.architect.test.util.representor.MockRepresentorCreator;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Provides methods that test {@code DocumentationMessageMapper} objects.
 *
 * <p>
 * This class shouldn't be instantiated.
 * </p>
 *
 * @author Alejandro Hernández
 */
public class MockDocumentationWriter {

	/**
	 * Writes a {@code Documentation} object.
	 *
	 * @param  documentationMessageMapper the {@code DocumentationMessageMapper}
	 *         to use for writing the JSON object
	 * @return the string containing the JSON object
	 */
	public static String write(
		DocumentationMessageMapper documentationMessageMapper) {

		CollectionRoutes.Builder<String, Object> collectionBuilder =
			new CollectionRoutesImpl.BuilderImpl<>(
				"name", __ -> null,
				__ -> {
				},
				__ -> null, __ -> null, __ -> Optional.empty());

		CustomDocumentation.Builder customDocumentationBuilder =
			new CustomDocumentationImpl.BuilderImpl();

		customDocumentationBuilder.addDescription(
			"binary1", "binary description");
		customDocumentationBuilder.addLocalizedDescription(
			"root/retrieve", __ -> "retrieve description");

		ItemRoutes.Builder itemBuilder = new ItemRoutesImpl.BuilderImpl<>(
			"name", __ -> null,
			__ -> {
			},
			__ -> null, __ -> Optional.empty(), __ -> Optional.empty());

		NestedCollectionRoutes.Builder nestedBuilder =
			new NestedCollectionRoutesImpl.BuilderImpl<>(
				"name", null, __ -> null,
				__ -> {
				},
				__ -> null, __ -> Optional.empty(), __ -> null);

		Representor<RootModel> rootModelRepresentor =
			MockRepresentorCreator.createRootModelRepresentor(false);

		Map<String, Representor> root = Collections.singletonMap(
			"root", rootModelRepresentor);

		CollectionRoutes<String, Object> collectionRoutes =
			collectionBuilder.build();

		CustomDocumentation customDocumentation =
			customDocumentationBuilder.build();

		ItemRoutes itemRoutes = itemBuilder.build();

		NestedCollectionRoutes nestedCollectionRoutes = nestedBuilder.build();

		Documentation documentation = new Documentation(
			() -> Optional.of(() -> "Title"),
			() -> Optional.of(() -> "Description"),
			() -> Optional.of(() -> "Entrypoint"), () -> root,
			() -> Collections.singletonMap("root", collectionRoutes),
			() -> Collections.singletonMap("root", itemRoutes),
			() -> Collections.singletonMap("root", nestedCollectionRoutes),
			() -> Collections.singletonMap("root", nestedCollectionRoutes),
			() -> customDocumentation);

		DocumentationWriter documentationWriter = DocumentationWriter.create(
			builder -> builder.documentation(
				documentation
			).documentationMessageMapper(
				documentationMessageMapper
			).requestInfo(
				getRequestInfo()
			).build());

		return documentationWriter.write();
	}

	private MockDocumentationWriter() {
		throw new UnsupportedOperationException();
	}

}