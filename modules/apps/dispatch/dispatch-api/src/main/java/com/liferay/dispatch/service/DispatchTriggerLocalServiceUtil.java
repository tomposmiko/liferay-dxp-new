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

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for DispatchTrigger. This utility wraps
 * <code>com.liferay.dispatch.service.impl.DispatchTriggerLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Alessio Antonio Rendina
 * @see DispatchTriggerLocalService
 * @generated
 */
public class DispatchTriggerLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.dispatch.service.impl.DispatchTriggerLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the dispatch trigger to the database. Also notifies the appropriate model listeners.
	 *
	 * @param dispatchTrigger the dispatch trigger
	 * @return the dispatch trigger that was added
	 */
	public static com.liferay.dispatch.model.DispatchTrigger addDispatchTrigger(
		com.liferay.dispatch.model.DispatchTrigger dispatchTrigger) {

		return getService().addDispatchTrigger(dispatchTrigger);
	}

	public static com.liferay.dispatch.model.DispatchTrigger addDispatchTrigger(
			long userId, String name, boolean system, String type,
			com.liferay.portal.kernel.util.UnicodeProperties
				typeSettingsProperties)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addDispatchTrigger(
			userId, name, system, type, typeSettingsProperties);
	}

	/**
	 * Creates a new dispatch trigger with the primary key. Does not add the dispatch trigger to the database.
	 *
	 * @param dispatchTriggerId the primary key for the new dispatch trigger
	 * @return the new dispatch trigger
	 */
	public static com.liferay.dispatch.model.DispatchTrigger
		createDispatchTrigger(long dispatchTriggerId) {

		return getService().createDispatchTrigger(dispatchTriggerId);
	}

	/**
	 * Deletes the dispatch trigger from the database. Also notifies the appropriate model listeners.
	 *
	 * @param dispatchTrigger the dispatch trigger
	 * @return the dispatch trigger that was removed
	 * @throws PortalException
	 */
	public static com.liferay.dispatch.model.DispatchTrigger
			deleteDispatchTrigger(
				com.liferay.dispatch.model.DispatchTrigger dispatchTrigger)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteDispatchTrigger(dispatchTrigger);
	}

	/**
	 * Deletes the dispatch trigger with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param dispatchTriggerId the primary key of the dispatch trigger
	 * @return the dispatch trigger that was removed
	 * @throws PortalException if a dispatch trigger with the primary key could not be found
	 */
	public static com.liferay.dispatch.model.DispatchTrigger
			deleteDispatchTrigger(long dispatchTriggerId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteDispatchTrigger(dispatchTriggerId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			deletePersistedModel(
				com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.dispatch.model.impl.DispatchTriggerModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.dispatch.model.impl.DispatchTriggerModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.dispatch.model.DispatchTrigger
		fetchDispatchTrigger(long dispatchTriggerId) {

		return getService().fetchDispatchTrigger(dispatchTriggerId);
	}

	public static com.liferay.dispatch.model.DispatchTrigger
		fetchDispatchTrigger(long companyId, String name) {

		return getService().fetchDispatchTrigger(companyId, name);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	/**
	 * Returns the dispatch trigger with the primary key.
	 *
	 * @param dispatchTriggerId the primary key of the dispatch trigger
	 * @return the dispatch trigger
	 * @throws PortalException if a dispatch trigger with the primary key could not be found
	 */
	public static com.liferay.dispatch.model.DispatchTrigger getDispatchTrigger(
			long dispatchTriggerId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getDispatchTrigger(dispatchTriggerId);
	}

	/**
	 * Returns a range of all the dispatch triggers.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.dispatch.model.impl.DispatchTriggerModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of dispatch triggers
	 * @param end the upper bound of the range of dispatch triggers (not inclusive)
	 * @return the range of dispatch triggers
	 */
	public static java.util.List<com.liferay.dispatch.model.DispatchTrigger>
		getDispatchTriggers(int start, int end) {

		return getService().getDispatchTriggers(start, end);
	}

	public static java.util.List<com.liferay.dispatch.model.DispatchTrigger>
		getDispatchTriggers(long companyId, int start, int end) {

		return getService().getDispatchTriggers(companyId, start, end);
	}

	/**
	 * Returns the number of dispatch triggers.
	 *
	 * @return the number of dispatch triggers
	 */
	public static int getDispatchTriggersCount() {
		return getService().getDispatchTriggersCount();
	}

	public static int getDispatchTriggersCount(long companyId) {
		return getService().getDispatchTriggersCount(companyId);
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	public static java.util.Date getNextFireDate(long dispatchTriggerId) {
		return getService().getNextFireDate(dispatchTriggerId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	public static java.util.Date getPreviousFireDate(long dispatchTriggerId) {
		return getService().getPreviousFireDate(dispatchTriggerId);
	}

	/**
	 * Updates the dispatch trigger in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param dispatchTrigger the dispatch trigger
	 * @return the dispatch trigger that was updated
	 */
	public static com.liferay.dispatch.model.DispatchTrigger
		updateDispatchTrigger(
			com.liferay.dispatch.model.DispatchTrigger dispatchTrigger) {

		return getService().updateDispatchTrigger(dispatchTrigger);
	}

	public static com.liferay.dispatch.model.DispatchTrigger
			updateDispatchTrigger(
				long dispatchTriggerId, boolean active, String cronExpression,
				int endDateMonth, int endDateDay, int endDateYear,
				int endDateHour, int endDateMinute, boolean neverEnd,
				int startDateMonth, int startDateDay, int startDateYear,
				int startDateHour, int startDateMinute)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateDispatchTrigger(
			dispatchTriggerId, active, cronExpression, endDateMonth, endDateDay,
			endDateYear, endDateHour, endDateMinute, neverEnd, startDateMonth,
			startDateDay, startDateYear, startDateHour, startDateMinute);
	}

	public static com.liferay.dispatch.model.DispatchTrigger
			updateDispatchTrigger(
				long dispatchTriggerId, String name,
				com.liferay.portal.kernel.util.UnicodeProperties
					typeSettingsProperties)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateDispatchTrigger(
			dispatchTriggerId, name, typeSettingsProperties);
	}

	public static DispatchTriggerLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<DispatchTriggerLocalService, DispatchTriggerLocalService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			DispatchTriggerLocalService.class);

		ServiceTracker<DispatchTriggerLocalService, DispatchTriggerLocalService>
			serviceTracker =
				new ServiceTracker
					<DispatchTriggerLocalService, DispatchTriggerLocalService>(
						bundle.getBundleContext(),
						DispatchTriggerLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}