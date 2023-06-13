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

package com.liferay.jethr0.entity.repository;

import com.liferay.jethr0.entity.Entity;

import java.util.List;

/**
 * @author Michael Hashimoto
 */
public interface EntityRepository<T extends Entity> {

	public void add(List<T> entities);

	public void add(T entity);

	public List<T> get();

	public T getById(long id);

	public void remove(List<T> entities);

	public void remove(T entity);

	public T update(T entity);

}