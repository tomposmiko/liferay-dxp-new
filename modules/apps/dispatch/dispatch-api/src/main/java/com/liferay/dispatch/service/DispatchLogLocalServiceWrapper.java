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

package com.liferay.dispatch.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link DispatchLogLocalService}.
 *
 * @author Alessio Antonio Rendina
 * @see DispatchLogLocalService
 * @generated
 */
public class DispatchLogLocalServiceWrapper
	implements DispatchLogLocalService,
			   ServiceWrapper<DispatchLogLocalService> {

	public DispatchLogLocalServiceWrapper(
		DispatchLogLocalService dispatchLogLocalService) {

		_dispatchLogLocalService = dispatchLogLocalService;
	}

	/**
	 * Adds the dispatch log to the database. Also notifies the appropriate model listeners.
	 *
	 * @param dispatchLog the dispatch log
	 * @return the dispatch log that was added
	 */
	@Override
	public com.liferay.dispatch.model.DispatchLog addDispatchLog(
		com.liferay.dispatch.model.DispatchLog dispatchLog) {

		return _dispatchLogLocalService.addDispatchLog(dispatchLog);
	}

	/**
	 * Creates a new dispatch log with the primary key. Does not add the dispatch log to the database.
	 *
	 * @param dispatchLogId the primary key for the new dispatch log
	 * @return the new dispatch log
	 */
	@Override
	public com.liferay.dispatch.model.DispatchLog createDispatchLog(
		long dispatchLogId) {

		return _dispatchLogLocalService.createDispatchLog(dispatchLogId);
	}

	/**
	 * Deletes the dispatch log from the database. Also notifies the appropriate model listeners.
	 *
	 * @param dispatchLog the dispatch log
	 * @return the dispatch log that was removed
	 */
	@Override
	public com.liferay.dispatch.model.DispatchLog deleteDispatchLog(
		com.liferay.dispatch.model.DispatchLog dispatchLog) {

		return _dispatchLogLocalService.deleteDispatchLog(dispatchLog);
	}

	/**
	 * Deletes the dispatch log with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param dispatchLogId the primary key of the dispatch log
	 * @return the dispatch log that was removed
	 * @throws PortalException if a dispatch log with the primary key could not be found
	 */
	@Override
	public com.liferay.dispatch.model.DispatchLog deleteDispatchLog(
			long dispatchLogId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _dispatchLogLocalService.deleteDispatchLog(dispatchLogId);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _dispatchLogLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _dispatchLogLocalService.dynamicQuery();
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

		return _dispatchLogLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.dispatch.model.impl.DispatchLogModelImpl</code>.
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

		return _dispatchLogLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.dispatch.model.impl.DispatchLogModelImpl</code>.
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

		return _dispatchLogLocalService.dynamicQuery(
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

		return _dispatchLogLocalService.dynamicQueryCount(dynamicQuery);
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

		return _dispatchLogLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.dispatch.model.DispatchLog fetchDispatchLog(
		long dispatchLogId) {

		return _dispatchLogLocalService.fetchDispatchLog(dispatchLogId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _dispatchLogLocalService.getActionableDynamicQuery();
	}

	/**
	 * Returns the dispatch log with the primary key.
	 *
	 * @param dispatchLogId the primary key of the dispatch log
	 * @return the dispatch log
	 * @throws PortalException if a dispatch log with the primary key could not be found
	 */
	@Override
	public com.liferay.dispatch.model.DispatchLog getDispatchLog(
			long dispatchLogId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _dispatchLogLocalService.getDispatchLog(dispatchLogId);
	}

	/**
	 * Returns a range of all the dispatch logs.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.dispatch.model.impl.DispatchLogModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of dispatch logs
	 * @param end the upper bound of the range of dispatch logs (not inclusive)
	 * @return the range of dispatch logs
	 */
	@Override
	public java.util.List<com.liferay.dispatch.model.DispatchLog>
		getDispatchLogs(int start, int end) {

		return _dispatchLogLocalService.getDispatchLogs(start, end);
	}

	/**
	 * Returns the number of dispatch logs.
	 *
	 * @return the number of dispatch logs
	 */
	@Override
	public int getDispatchLogsCount() {
		return _dispatchLogLocalService.getDispatchLogsCount();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _dispatchLogLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _dispatchLogLocalService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _dispatchLogLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the dispatch log in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param dispatchLog the dispatch log
	 * @return the dispatch log that was updated
	 */
	@Override
	public com.liferay.dispatch.model.DispatchLog updateDispatchLog(
		com.liferay.dispatch.model.DispatchLog dispatchLog) {

		return _dispatchLogLocalService.updateDispatchLog(dispatchLog);
	}

	@Override
	public DispatchLogLocalService getWrappedService() {
		return _dispatchLogLocalService;
	}

	@Override
	public void setWrappedService(
		DispatchLogLocalService dispatchLogLocalService) {

		_dispatchLogLocalService = dispatchLogLocalService;
	}

	private DispatchLogLocalService _dispatchLogLocalService;

}