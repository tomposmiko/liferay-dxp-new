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

import com.liferay.apio.architect.internal.message.json.PageMessageMapper;
import com.liferay.apio.architect.internal.pagination.PageImpl;
import com.liferay.apio.architect.internal.pagination.PaginationImpl;
import com.liferay.apio.architect.internal.util.model.RootModel;
import com.liferay.apio.architect.internal.writer.PageWriter;
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.resource.Resource.Paged;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * Provides methods that test {@code PageMessageMapper} objects.
 *
 * <p>
 * This class shouldn't be instantiated.
 * </p>
 *
 * @author Alejandro Hernández
 */
public class MockPageWriter {

	/**
	 * Writes a {@link RootModel} collection with the hierarchy of embedded
	 * models and multiple fields.
	 *
	 * @param  pageMessageMapper the {@code PageMessageMapper} to use for
	 *         writing the JSON object
	 * @return the string containing the JSON object
	 */
	public static String write(PageMessageMapper<RootModel> pageMessageMapper) {
		Collection<RootModel> items = Arrays.asList(
			() -> "1", () -> "2", () -> "3");

		PageItems<RootModel> pageItems = new PageItems<>(items, 9);

		Pagination pagination = new PaginationImpl(3, 2);

		Page<RootModel> page = new PageImpl<>(
			Paged.of("root"), pageItems, pagination);

		PageWriter<RootModel> pageWriter = PageWriter.create(
			builder -> builder.page(
				page
			).pageMessageMapper(
				pageMessageMapper
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

		return pageWriter.write();
	}

	private MockPageWriter() {
		throw new UnsupportedOperationException();
	}

}