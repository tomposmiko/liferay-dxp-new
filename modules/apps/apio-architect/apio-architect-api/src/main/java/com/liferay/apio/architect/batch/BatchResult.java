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

package com.liferay.apio.architect.batch;

import java.util.List;

/**
 * Represents the result of a batch operation.
 *
 * @author Alejandro Hernández
 * @author Zoltán Takács
 */
public class BatchResult<T> {

	public BatchResult(List<T> identifiers, String resourceName) {
		_identifiers = identifiers;

		this.resourceName = resourceName;
	}

	/**
	 * Returns the list of identifiers created in the batch operation.
	 *
	 * @return the list of identifiers
	 */
	public List<T> getIdentifiers() {
		return _identifiers;
	}

	/**
	 * The name of the elements' resource created in the batch operation.
	 */
	public final String resourceName;

	private final List<T> _identifiers;

}