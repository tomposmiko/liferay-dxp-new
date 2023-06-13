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

import aQute.bnd.annotation.ProviderType;

import com.liferay.change.tracking.exception.CTException;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.model.CTEntry;
import com.liferay.change.tracking.model.CTEntryAggregate;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.portal.kernel.dao.orm.QueryDefinition;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModel;

import java.util.List;
import java.util.Optional;

/**
 * Provides functionality to register and retrieve model changes from the change
 * tracking framework.
 *
 * @author Daniel Kocsis
 */
@ProviderType
public interface CTManager {

	/**
	 * Assigns a model change to the change entry aggregate associated with the
	 * owner model change. If there is no change aggregate associated with the
	 * owner, it creates a new one. Also, a new aggregate is created if the
	 * related entry was already part of the aggregate and is being changed.
	 *
	 * @param  userId the primary key of the user
	 * @param  ownerCTEntry the change bag's owner
	 * @param  relatedCTEntry the change to add to the bag
	 * @return the created or updated change entry aggregate
	 */
	public Optional<CTEntryAggregate> addRelatedCTEntry(
		long userId, CTEntry ownerCTEntry, CTEntry relatedCTEntry);

	/**
	 * Assigns a model change to the change entry aggregate associated with the
	 * owner model change. If there is no change aggregate associated with the
	 * owner, it creates a new one. Also, a new aggregate is created if the
	 * related entry was already part of the aggregate and is being changed,
	 * unless you force the override of the existing change entry aggregate.
	 *
	 * @param  userId the primary key of the user
	 * @param  ownerCTEntry the owner of the change bag
	 * @param  relatedCTEntry the change to add to the bag
	 * @param  force whether to override the existing change entry aggregate
	 * @return the created or updated change entry aggregate
	 */
	public Optional<CTEntryAggregate> addRelatedCTEntry(
		long userId, CTEntry ownerCTEntry, CTEntry relatedCTEntry,
		boolean force);

	/**
	 * Assigns a model change to the change entry aggregate associated with the
	 * owner model change. If there is no change aggregate associated with the
	 * owner, it creates a new one. Also, a new aggregate is created if the
	 * related entry was already part of the aggregate and is being changed.
	 *
	 * @param  userId the primary key of the user
	 * @param  ownerCTEntryId the primary key of the change bag's owner
	 * @param  relatedCTEntryId the primary key of the change to add to the bag
	 * @return the created or updated change entry aggregate
	 */
	public Optional<CTEntryAggregate> addRelatedCTEntry(
		long userId, long ownerCTEntryId, long relatedCTEntryId);

	/**
	 * Executes a model addition or update using the given supplier, toggling
	 * the flag that indicates the update before and after the operation.
	 * Therefore, during the execution, {@link #isModelUpdateInProgress()} will
	 * return <code>true</code>.
	 *
	 * @param  modelUpdateSupplier the supplier that performs the add or update
	 *         and supplies the resulting model
	 * @return the created or updated model
	 */
	public <T> T executeModelUpdate(
			UnsafeSupplier<T, PortalException> modelUpdateSupplier)
		throws PortalException;

	/**
	 * Returns the model change of the current user's active change collection.
	 *
	 * @param  userId the primary key of the user
	 * @param  modelClassNameId the primary key of the changed version model's
	 *         class
	 * @param  modelClassPK the primary key of the changed version model
	 * @return the change tracking entry representing the model change
	 */
	public Optional<CTEntry> getActiveCTCollectionCTEntryOptional(
		long userId, long modelClassNameId, long modelClassPK);

	/**
	 * Returns the active change tracking collection associated with the user in
	 * the scope of the company.
	 *
	 * @param  userId the primary key of the user
	 * @return the selected change tracking collection
	 */
	public Optional<CTCollection> getActiveCTCollectionOptional(long userId);

	/**
	 * Returns change tracking collections associated with
	 * the given company, optionally including production or active change lists.
	 *
	 * @param  companyId the primary key of the company
	 * @param  includeProduction whether to return the production change list
	 * @param  includeActive whether to return the active change lists
	 * @param  queryDefinition the settings regarding
	 *         pagination, order and filter
	 * @return the change tracking collections
	 */
	public List<CTCollection> getCTCollections(
		long companyId, long userId, boolean includeProduction,
		boolean includeActive, QueryDefinition<CTCollection> queryDefinition);

	/**
	 * Returns the change entry aggregate containing the change entry and change
	 * entry collection.
	 *
	 * @param  ctEntry the model change entry
	 * @param  ctCollection the model change entry collection
	 * @return the change entry aggregate containing the change entry and change
	 *         entry collection
	 */
	public Optional<CTEntryAggregate> getCTEntryAggregateOptional(
		CTEntry ctEntry, CTCollection ctCollection);

	/**
	 * Returns the latest model change for the current user's active change
	 * collection.
	 *
	 * @param  userId the primary key of the user
	 * @param  modelResourcePrimKey the primary key of the changed resource
	 *         model
	 * @return the change tracking entry representing the model change
	 */
	public Optional<CTEntry> getLatestModelChangeCTEntryOptional(
		long userId, long modelResourcePrimKey);

	/**
	 * Returns all model changes for the current user's active change
	 * collection.
	 *
	 * @param  userId the primary key of the user
	 * @param  modelResourcePrimKey the primary key of the changed resource
	 *         model
	 * @return a list of change tracking entries representing all the registered
	 *         model changes
	 */
	public List<CTEntry> getModelChangeCTEntries(
		long userId, long modelResourcePrimKey);

	/**
	 * Returns a paginated and ordered list of all model changes in the context
	 * of the current user's active change collection.
	 *
	 * @param  userId the primary key of the user
	 * @param  modelResourcePrimKey the primary key of the changed resource
	 *         model
	 * @param  queryDefinition the settings regarding pagination, order, and
	 *         filter
	 * @return a list of change tracking entries representing the registered
	 *         model changes
	 */
	public List<CTEntry> getModelChangeCTEntries(
		long userId, long modelResourcePrimKey,
		QueryDefinition<CTEntry> queryDefinition);

	/**
	 * Returns a model change's bag, first searching for it in the current
	 * user's active change collection; if it doesn't exist there, the
	 * production change collection is searched.
	 *
	 * @param  userId the primary key of the user
	 * @param  modelClassNameId the primary key of the changed version model's
	 *         class
	 * @param  modelClassPK the primary key of the changed version model
	 * @return the change tracking entry representing the model change
	 */
	public Optional<CTEntryAggregate> getModelChangeCTEntryAggregateOptional(
		long userId, long modelClassNameId, long modelClassPK);

	/**
	 * Returns a model change, first searching for it in the current user's
	 * active change collection; if it doesn't exist there, the production
	 * change collection is searched.
	 *
	 * @param  userId the primary key of the user
	 * @param  modelClassNameId the primary key of the changed version model's
	 *         class
	 * @param  modelClassPK the primary key of the changed version model
	 * @return the change tracking entry representing the model change
	 */
	public Optional<CTEntry> getModelChangeCTEntryOptional(
		long userId, long modelClassNameId, long modelClassPK);

	/**
	 * Retrurns a model change from the production change collection.
	 *
	 * @param  userId the primary key of the user
	 * @param  modelClassNameId the primary key of the changed version model's
	 *         class
	 * @param  modelClassPK the primary key of the changed version model
	 * @return the change tracking entry representing the model change
	 */
	public Optional<CTEntry> getProductionCTCollectionCTEntryOptional(
		long userId, long modelClassNameId, long modelClassPK);

	/**
	 * Returns the change related change entries associated with the given
	 * change entry collection.
	 *
	 * @param  ctEntry the model change entry
	 * @param  ctCollection the model change entry collection
	 * @return the change related change entries associated with the given
	 *         change entry collection
	 */
	public List<CTEntry> getRelatedCTEntries(
		CTEntry ctEntry, CTCollection ctCollection);

	/**
	 * Returns the number of change related change entries associated with the
	 * given change entry.
	 *
	 * @param  ctEntryId a model change entry
	 * @return the number of change related change entries associated with the
	 *         given change entry
	 */
	public int getRelatedOwnerCTEntriesCount(long ctEntryId);

	/**
	 * Returns <code>true</code> if a model addition or update is in progress.
	 * This only returns <code>true</code> if the addition or update is being
	 * executed with {@link #executeModelUpdate(UnsafeSupplier)} and the
	 * execution is in progress. It's useful to bypass change tracking
	 * consideration when a get or fetch is executed for a model during its own
	 * addition or update.
	 *
	 * @return <code>true</code> if a model addition or update is in progress;
	 *         <code>false</code> otherwise
	 */
	public boolean isModelUpdateInProgress();

	/**
	 * Registers the model change into the change tracking framework for the
	 * current user's active change collection. A
	 * <code>DuplicateCTEntryException</code> is thrown if the change tracking
	 * entry already exists with the same model class name ID and model class
	 * primary key.
	 *
	 * @param  userId the primary key of the user
	 * @param  modelClassNameId the primary key of the changed version model's
	 *         class
	 * @param  modelClassPK the primary key of the changed version model
	 * @param  modelResourcePrimKey the primary key of the changed resource
	 *         model
	 * @param  changeType the model change's type
	 * @return the change tracking entry representing the registered model
	 *         change
	 */
	public Optional<CTEntry> registerModelChange(
			long userId, long modelClassNameId, long modelClassPK,
			long modelResourcePrimKey, int changeType)
		throws CTException;

	/**
	 * Registers a model change into the change tracking framework for the
	 * current user's active change collection. A
	 * <code>DuplicateCTEntryException</code> is thrown if a change tracking
	 * entry already exists with the same model class name ID and model class
	 * primary key, unless you force the override of the existing change entry.
	 *
	 * @param  userId the primary key of the user
	 * @param  modelClassNameId the primary key of the changed version model's
	 *         class
	 * @param  modelClassPK the primary key of the changed version model
	 * @param  modelResourcePrimKey the primary key of the changed resource
	 *         model
	 * @param  changeType the model change's type
	 * @param  force whether to override an existing change entry
	 * @return the change tracking entry representing the registered model
	 *         change
	 */
	public Optional<CTEntry> registerModelChange(
			long userId, long modelClassNameId, long modelClassPK,
			long modelResourcePrimKey, int changeType, boolean force)
		throws CTException;

	/**
	 * Assigns all related model changes to a change entry aggregate associated
	 * with the owner model change. A new aggregate is created if the related
	 * entry was already part of the aggregate.
	 *
	 * @param userId the primary key of the user
	 * @param classNameId the primary key of the owner version model's class
	 * @param classPK the primary key of the owner version model
	 */
	public <V extends BaseModel> void registerRelatedChanges(
		long userId, long classNameId, long classPK);

	/**
	 * Assigns all related model changes to a change entry aggregate associated
	 * with the owner model change. A new aggregate is created if the related
	 * entry was already part of the aggregate, unless you force the override of
	 * the existing change entry aggregate.
	 *
	 * @param userId the primary key of the user
	 * @param classNameId the primary key of the owner version model's class
	 * @param classPK the primary key of the owner version model
	 * @param force whether to override the existing change entry aggregate
	 */
	public <V extends BaseModel> void registerRelatedChanges(
		long userId, long classNameId, long classPK, boolean force);

	/**
	 * Unregisters a model change from the change tracking framework.
	 *
	 * @param  userId the primary key of the user
	 * @param  modelClassNameId the primary key of the changed version model's
	 *         class
	 * @param  modelClassPK the primary key of the changed version model
	 * @return the change tracking entry that was deleted
	 */
	public Optional<CTEntry> unregisterModelChange(
		long userId, long modelClassNameId, long modelClassPK);

}