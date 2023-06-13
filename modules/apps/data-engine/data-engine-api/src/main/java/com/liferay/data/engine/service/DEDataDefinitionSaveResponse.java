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

package com.liferay.data.engine.service;

import com.liferay.data.engine.model.DEDataDefinition;

/**
 * @author Leonardo Barros
 */
public final class DEDataDefinitionSaveResponse {

	public DEDataDefinition getDEDataDefinition() {
		return _deDataDefinition;
	}

	public static final class Builder {

		public static Builder newBuilder(DEDataDefinition deDataDefinition) {
			return new Builder(deDataDefinition);
		}

		public static DEDataDefinitionSaveResponse of(
			DEDataDefinition deDataDefinition) {

			return newBuilder(
				deDataDefinition
			).build();
		}

		public DEDataDefinitionSaveResponse build() {
			return _deDataDefinitionSaveResponse;
		}

		private Builder(DEDataDefinition deDataDefinition) {
			_deDataDefinitionSaveResponse._deDataDefinition = deDataDefinition;
		}

		private final DEDataDefinitionSaveResponse
			_deDataDefinitionSaveResponse = new DEDataDefinitionSaveResponse();

	}

	private DEDataDefinitionSaveResponse() {
	}

	private DEDataDefinition _deDataDefinition;

}