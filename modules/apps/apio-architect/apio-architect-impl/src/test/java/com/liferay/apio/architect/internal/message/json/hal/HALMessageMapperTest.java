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

package com.liferay.apio.architect.internal.message.json.hal;

import com.liferay.apio.architect.internal.unsafe.Unsafe;
import com.liferay.apio.architect.internal.util.json.MessageMapperTesterBuilder;
import com.liferay.apio.architect.internal.util.model.RootModel;
import com.liferay.apio.architect.internal.util.writer.MockWriterUtil;
import com.liferay.apio.architect.internal.wiring.osgi.manager.representable.RepresentableManager;
import com.liferay.apio.architect.representor.Representor;

import java.nio.file.Paths;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

/**
 * @author Javier Gamarra
 * @author Alejandro Hernández
 */
public class HALMessageMapperTest implements RepresentableManager {

	@Override
	public <T> Optional<Representor<T>> getRepresentorOptional(String name) {
		Optional<Representor<?>> optional =
			MockWriterUtil.getRepresentorOptional(name);

		return optional.map(Unsafe::unsafeCast);
	}

	@Override
	public Map<String, Representor> getRepresentors() {
		return Collections.emptyMap();
	}

	@Test
	public void testHALMessageMappers() {
		HALPageMessageMapper<RootModel> pageMessageMapper =
			new HALPageMessageMapper<>();

		pageMessageMapper.representableManager = this;

		MessageMapperTesterBuilder.path(
			Paths.get("src", "test", "resources", "hal")
		).mediaType(
			"application/hal+json"
		).validateErrorMessageMapper(
			new HALErrorMessageMapper()
		).validateEntryPointMessageMapper(
			new HALEntryPointMessageMapper()
		).validatePageMessageMapper(
			pageMessageMapper
		).validateSingleModelMessageMapper(
			new HALSingleModelMessageMapper<>()
		);
	}

}