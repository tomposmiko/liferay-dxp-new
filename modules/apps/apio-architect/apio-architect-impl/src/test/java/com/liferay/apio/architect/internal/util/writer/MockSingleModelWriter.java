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

package com.liferay.apio.architect.internal.util.writer;

import static com.liferay.apio.architect.internal.util.writer.MockWriterUtil.getRequestInfo;

import com.liferay.apio.architect.internal.message.json.SingleModelMessageMapper;
import com.liferay.apio.architect.internal.single.model.SingleModelImpl;
import com.liferay.apio.architect.internal.util.model.RootModel;
import com.liferay.apio.architect.internal.writer.SingleModelWriter;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.Optional;

/**
 * Provides methods that test {@code SingleModelMessageMapper} objects.
 *
 * <p>
 * This class shouldn't be instantiated.
 * </p>
 *
 * @author Alejandro Hernández
 */
public class MockSingleModelWriter {

	/**
	 * Writes a {@link RootModel} with the hierarchy of embedded models and
	 * multiple fields.
	 *
	 * @param  singleModelMessageMapper the {@code SingleModelMessageMapper} to
	 *         use for writing the JSON object
	 * @return the string containing the JSON object
	 */
	public static String write(
		SingleModelMessageMapper<RootModel> singleModelMessageMapper) {

		SingleModel<RootModel> singleModel = new SingleModelImpl<>(
			() -> "first", "root");

		SingleModelWriter<RootModel> singleModelWriter =
			SingleModelWriter.create(
				builder -> builder.singleModel(
					singleModel
				).modelMessageMapper(
					singleModelMessageMapper
				).pathFunction(
					MockWriterUtil::identifierToPath
				).resourceNameFunction(
					__ -> Optional.of("models")
				).representorFunction(
					MockWriterUtil::getRepresentorOptional
				).requestInfo(
					getRequestInfo()
				).singleModelFunction(
					MockWriterUtil::getSingleModel
				).actionSemanticsFunction(
					MockWriterUtil::getActionSemantics
				).build());

		Optional<String> optional = singleModelWriter.write();

		return optional.orElseThrow(
			() -> new AssertionError("Unable to write"));
	}

	private MockSingleModelWriter() {
		throw new UnsupportedOperationException();
	}

}