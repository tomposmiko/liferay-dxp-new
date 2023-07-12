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

package com.liferay.account.service;

import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * Provides a wrapper for {@link AccountGroupAccountEntryRelLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see AccountGroupAccountEntryRelLocalService
 * @generated
 */
public class AccountGroupAccountEntryRelLocalServiceWrapper
	implements AccountGroupAccountEntryRelLocalService,
			   ServiceWrapper<AccountGroupAccountEntryRelLocalService> {

	public AccountGroupAccountEntryRelLocalServiceWrapper(
		AccountGroupAccountEntryRelLocalService
			accountGroupAccountEntryRelLocalService) {

		_accountGroupAccountEntryRelLocalService =
			accountGroupAccountEntryRelLocalService;
	}

	/**
	 * Adds the account group account entry rel to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AccountGroupAccountEntryRelLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param accountGroupAccountEntryRel the account group account entry rel
	 * @return the account group account entry rel that was added
	 */
	@Override
	public com.liferay.account.model.AccountGroupAccountEntryRel
		addAccountGroupAccountEntryRel(
			com.liferay.account.model.AccountGroupAccountEntryRel
				accountGroupAccountEntryRel) {

		return _accountGroupAccountEntryRelLocalService.
			addAccountGroupAccountEntryRel(accountGroupAccountEntryRel);
	}

	@Override
	public com.liferay.account.model.AccountGroupAccountEntryRel
			addAccountGroupAccountEntryRel(
				long accountGroupId, long accountEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _accountGroupAccountEntryRelLocalService.
			addAccountGroupAccountEntryRel(accountGroupId, accountEntryId);
	}

	@Override
	public void addAccountGroupAccountEntryRels(
			long accountGroupId, long[] accountEntryIds)
		throws com.liferay.portal.kernel.exception.PortalException {

		_accountGroupAccountEntryRelLocalService.
			addAccountGroupAccountEntryRels(accountGroupId, accountEntryIds);
	}

	/**
	 * Creates a new account group account entry rel with the primary key. Does not add the account group account entry rel to the database.
	 *
	 * @param AccountGroupAccountEntryRelId the primary key for the new account group account entry rel
	 * @return the new account group account entry rel
	 */
	@Override
	public com.liferay.account.model.AccountGroupAccountEntryRel
		createAccountGroupAccountEntryRel(long AccountGroupAccountEntryRelId) {

		return _accountGroupAccountEntryRelLocalService.
			createAccountGroupAccountEntryRel(AccountGroupAccountEntryRelId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel createPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _accountGroupAccountEntryRelLocalService.createPersistedModel(
			primaryKeyObj);
	}

	/**
	 * Deletes the account group account entry rel from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AccountGroupAccountEntryRelLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param accountGroupAccountEntryRel the account group account entry rel
	 * @return the account group account entry rel that was removed
	 */
	@Override
	public com.liferay.account.model.AccountGroupAccountEntryRel
		deleteAccountGroupAccountEntryRel(
			com.liferay.account.model.AccountGroupAccountEntryRel
				accountGroupAccountEntryRel) {

		return _accountGroupAccountEntryRelLocalService.
			deleteAccountGroupAccountEntryRel(accountGroupAccountEntryRel);
	}

	/**
	 * Deletes the account group account entry rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AccountGroupAccountEntryRelLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param AccountGroupAccountEntryRelId the primary key of the account group account entry rel
	 * @return the account group account entry rel that was removed
	 * @throws PortalException if a account group account entry rel with the primary key could not be found
	 */
	@Override
	public com.liferay.account.model.AccountGroupAccountEntryRel
			deleteAccountGroupAccountEntryRel(
				long AccountGroupAccountEntryRelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _accountGroupAccountEntryRelLocalService.
			deleteAccountGroupAccountEntryRel(AccountGroupAccountEntryRelId);
	}

	@Override
	public void deleteAccountGroupAccountEntryRels(
			long accountGroupId, long[] accountEntryIds)
		throws com.liferay.portal.kernel.exception.PortalException {

		_accountGroupAccountEntryRelLocalService.
			deleteAccountGroupAccountEntryRels(accountGroupId, accountEntryIds);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _accountGroupAccountEntryRelLocalService.deletePersistedModel(
			persistedModel);
	}

	@Override
	public <T> T dslQuery(com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {
		return _accountGroupAccountEntryRelLocalService.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return _accountGroupAccountEntryRelLocalService.dslQueryCount(dslQuery);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _accountGroupAccountEntryRelLocalService.dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _accountGroupAccountEntryRelLocalService.dynamicQuery(
			dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.account.model.impl.AccountGroupAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return _accountGroupAccountEntryRelLocalService.dynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.account.model.impl.AccountGroupAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return _accountGroupAccountEntryRelLocalService.dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return _accountGroupAccountEntryRelLocalService.dynamicQueryCount(
			dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return _accountGroupAccountEntryRelLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.account.model.AccountGroupAccountEntryRel
		fetchAccountGroupAccountEntryRel(long AccountGroupAccountEntryRelId) {

		return _accountGroupAccountEntryRelLocalService.
			fetchAccountGroupAccountEntryRel(AccountGroupAccountEntryRelId);
	}

	@Override
	public com.liferay.account.model.AccountGroupAccountEntryRel
		fetchAccountGroupAccountEntryRel(
			long accountGroupId, long accountEntryId) {

		return _accountGroupAccountEntryRelLocalService.
			fetchAccountGroupAccountEntryRel(accountGroupId, accountEntryId);
	}

	/**
	 * Returns the account group account entry rel with the primary key.
	 *
	 * @param AccountGroupAccountEntryRelId the primary key of the account group account entry rel
	 * @return the account group account entry rel
	 * @throws PortalException if a account group account entry rel with the primary key could not be found
	 */
	@Override
	public com.liferay.account.model.AccountGroupAccountEntryRel
			getAccountGroupAccountEntryRel(long AccountGroupAccountEntryRelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _accountGroupAccountEntryRelLocalService.
			getAccountGroupAccountEntryRel(AccountGroupAccountEntryRelId);
	}

	/**
	 * Returns a range of all the account group account entry rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.account.model.impl.AccountGroupAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of account group account entry rels
	 * @param end the upper bound of the range of account group account entry rels (not inclusive)
	 * @return the range of account group account entry rels
	 */
	@Override
	public java.util.List<com.liferay.account.model.AccountGroupAccountEntryRel>
		getAccountGroupAccountEntryRels(int start, int end) {

		return _accountGroupAccountEntryRelLocalService.
			getAccountGroupAccountEntryRels(start, end);
	}

	@Override
	public java.util.List<com.liferay.account.model.AccountGroupAccountEntryRel>
		getAccountGroupAccountEntryRelsByAccountEntryId(long accountEntryId) {

		return _accountGroupAccountEntryRelLocalService.
			getAccountGroupAccountEntryRelsByAccountEntryId(accountEntryId);
	}

	@Override
	public java.util.List<com.liferay.account.model.AccountGroupAccountEntryRel>
		getAccountGroupAccountEntryRelsByAccountGroupId(long accountGroupId) {

		return _accountGroupAccountEntryRelLocalService.
			getAccountGroupAccountEntryRelsByAccountGroupId(accountGroupId);
	}

	/**
	 * Returns the number of account group account entry rels.
	 *
	 * @return the number of account group account entry rels
	 */
	@Override
	public int getAccountGroupAccountEntryRelsCount() {
		return _accountGroupAccountEntryRelLocalService.
			getAccountGroupAccountEntryRelsCount();
	}

	@Override
	public long getAccountGroupAccountEntryRelsCountByAccountGroupId(
		long accountGroupId) {

		return _accountGroupAccountEntryRelLocalService.
			getAccountGroupAccountEntryRelsCountByAccountGroupId(
				accountGroupId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _accountGroupAccountEntryRelLocalService.
			getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _accountGroupAccountEntryRelLocalService.
			getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _accountGroupAccountEntryRelLocalService.
			getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _accountGroupAccountEntryRelLocalService.getPersistedModel(
			primaryKeyObj);
	}

	/**
	 * Updates the account group account entry rel in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect AccountGroupAccountEntryRelLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param accountGroupAccountEntryRel the account group account entry rel
	 * @return the account group account entry rel that was updated
	 */
	@Override
	public com.liferay.account.model.AccountGroupAccountEntryRel
		updateAccountGroupAccountEntryRel(
			com.liferay.account.model.AccountGroupAccountEntryRel
				accountGroupAccountEntryRel) {

		return _accountGroupAccountEntryRelLocalService.
			updateAccountGroupAccountEntryRel(accountGroupAccountEntryRel);
	}

	@Override
	public BasePersistence<?> getBasePersistence() {
		return _accountGroupAccountEntryRelLocalService.getBasePersistence();
	}

	@Override
	public AccountGroupAccountEntryRelLocalService getWrappedService() {
		return _accountGroupAccountEntryRelLocalService;
	}

	@Override
	public void setWrappedService(
		AccountGroupAccountEntryRelLocalService
			accountGroupAccountEntryRelLocalService) {

		_accountGroupAccountEntryRelLocalService =
			accountGroupAccountEntryRelLocalService;
	}

	private AccountGroupAccountEntryRelLocalService
		_accountGroupAccountEntryRelLocalService;

}