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

package com.liferay.change.tracking;

import com.liferay.change.tracking.exception.CTException;
import com.liferay.change.tracking.model.CTEntry;
import com.liferay.portal.kernel.dao.orm.QueryDefinition;

import java.util.List;
import java.util.Optional;

/**
 * Provides functionality to register and retrieve model changes from the change
 * tracking framework.
 *
 * @author Daniel Kocsis
 * @review
 */
public interface CTManager {

	/**
	 * Retrieves the latest model change in the context of the current user's
	 * active change collection.
	 *
	 * @param  userId the primary key of the user
	 * @param  resourcePrimKey the primary key of the changed resource model
	 * @return the change tracking entry representing the model change
	 */
	public Optional<CTEntry> getLatestModelChangeCTEntryOptional(
		long userId, long resourcePrimKey);

	/**
	 * Retrieves all model changes in the context of the current user's active
	 * change collection.
	 *
	 * @param  userId the primary key of the user
	 * @param  resourcePrimKey the primary key of the changed resource model
	 * @return a list of change tracking entries representing all the registered
	 *         model changes
	 */
	public List<CTEntry> getModelChangeCTEntries(
		long userId, long resourcePrimKey);

	/**
	 * Retrieves a paginated and ordered list of all model changes in the
	 * context of the current user's active change collection.
	 *
	 * @param  userId the primary key of the user
	 * @param  resourcePrimKey the primary key of the changed resource model
	 * @param  queryDefinition the object contains settings regarding
	 *         pagination, order and filter
	 * @return a list of change tracking entries representing the registered
	 *         model changes
	 */
	public List<CTEntry> getModelChangeCTEntries(
		long userId, long resourcePrimKey,
		QueryDefinition<CTEntry> queryDefinition);

	/**
	 * Retrieves a model change in the context of the current user's active
	 * change collection.
	 *
	 * @param  userId the primary key of the user
	 * @param  classNameId the primary key of the changed version model's class
	 * @param  classPK the primary key of the changed version model
	 * @return the change tracking entry representing the model change
	 */
	public Optional<CTEntry> getModelChangeCTEntryOptional(
		long userId, long classNameId, long classPK);

	/**
	 * Registers a model change into the change tracking framework in the
	 * context of the current user's active change collection.
	 *
	 * @param  userId the primary key of the user
	 * @param  classNameId the primary key of the changed version model's class
	 * @param  classPK the primary key of the changed version model
	 * @param  resourcePrimKey the primary key of the changed resource model
	 * @return the change tracking entry representing the registered model
	 *         change
	 */
	public Optional<CTEntry> registerModelChange(
			long userId, long classNameId, long classPK, long resourcePrimKey)
		throws CTException;

}