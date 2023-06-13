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

package com.liferay.apio.architect.internal.wiring.osgi.manager.uri.mapper;

import com.liferay.apio.architect.internal.wiring.osgi.manager.base.ClassNameBaseManager;
import com.liferay.apio.architect.uri.Path;

import java.util.Optional;

/**
 * Provides methods to map a {@link Path} to an identifier, and vice versa.
 *
 * @author Alejandro Hernández
 */
public interface PathIdentifierMapperManager {

	/**
	 * Returns {@code true} if a {@link
	 * com.liferay.apio.architect.uri.mapper.PathIdentifierMapper} for a
	 * resource has been published.
	 *
	 * @param  name the resource's name
	 * @return {@code true} if a {@code PathIdentifierMapper} for a resource is
	 *         present; {@code false} otherwise
	 */
	public boolean hasPathIdentifierMapper(String name);

	/**
	 * Converts a {@code Path} to its equivalent identifier of type {@code T},
	 * if a valid {@link
	 * com.liferay.apio.architect.uri.mapper.PathIdentifierMapper} can be found;
	 * throws a {@code MustHavePathIdentifierMapper} exception otherwise.
	 *
	 * @param  path the {@code Path}
	 * @return the identifier
	 */
	public <T> T mapToIdentifierOrFail(Path path);

	/**
	 * Converts a {@code Path} to its equivalent identifier of type {@code T},
	 * if a valid {@link
	 * com.liferay.apio.architect.uri.mapper.PathIdentifierMapper} can be found;
	 * throws a {@code MustHavePathIdentifierMapper} exception otherwise.
	 *
	 * @param  path the {@code Path}
	 * @param  classNameBaseManager the classNameBaseManager to look for
	 *         reusable routes
	 * @return the identifier
	 * @review
	 */
	public default <T, R> T mapToIdentifierOrFail(
		Path path, ClassNameBaseManager<R> classNameBaseManager) {

		return mapToIdentifierOrFail(path);
	}

	/**
	 * Converts an identifier to its equivalent {@code Path}, if a valid {@code
	 * PathIdentifierMapper} can be found. Returns {@code Optional#empty()}
	 * otherwise.
	 *
	 * @param  name the resource's name
	 * @param  identifier the identifier
	 * @return the {@code Path}, if a valid {@code PathIdentifierMapper} is
	 *         present; {@code Optional#empty()} otherwise
	 */
	public <T> Optional<Path> mapToPath(String name, T identifier);

	/**
	 * Converts an identifier to its equivalent {@code Path}, if a valid {@code
	 * PathIdentifierMapper} can be found. Returns {@code Optional#empty()}
	 * otherwise.
	 *
	 * @param  name the resource's name
	 * @param  identifier the identifier
	 * @param  classNameBaseManager the classNameBaseManager to look for
	 *         reusable routes
	 * @return the {@code Path}, if a valid {@code PathIdentifierMapper} is
	 *         present; {@code Optional#empty()} otherwise
	 * @review
	 */
	public default <R, T> Optional<Path> mapToPath(
		String name, T identifier,
		ClassNameBaseManager<R> classNameBaseManager) {

		return mapToPath(name, identifier);
	}

}