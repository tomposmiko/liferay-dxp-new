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

package com.liferay.user.associated.data.exporter;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.user.associated.data.component.UADComponent;

import java.io.File;

/**
 * Handles converting the entities of type {@code T} related to a user into a
 * format that can be written to a file and downloaded.
 *
 * @author William Newbury
 * @review
 */
@ProviderType
public interface UADExporter<T> extends UADComponent<T> {

	/**
	 * Returns the number of entities of type {@code T} associated with a user.
	 *
	 * @param userId the primary key of the user whose data to count
	 * @return the number of entities associated with the user
	 * @review
	 */
	public long count(long userId) throws PortalException;

	/**
	 * Returns a byte array representing the entity, ready to be written to a
	 * file.
	 *
	 * @param t the entity of type {@code T} to convert into a byte array
	 * @return a byte array representing the given entity
	 * @throws PortalException
	 * @review
	 */
	public byte[] export(T t) throws PortalException;

	/**
	 * Returns a file object containing the data from all entities of type
	 * {@code T} related to a user.
	 *
	 * @param userId the primary key of the user whose data to export
	 * @return a {@link File} object containing the exported data
	 * @throws PortalException
	 * @review
	 */
	public File exportAll(long userId) throws PortalException;

}