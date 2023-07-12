/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.data.integration.service;

import com.liferay.commerce.data.integration.model.CommerceDataIntegrationProcess;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;

/**
 * Provides the local service utility for CommerceDataIntegrationProcess. This utility wraps
 * <code>com.liferay.commerce.data.integration.service.impl.CommerceDataIntegrationProcessLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Alessio Antonio Rendina
 * @see CommerceDataIntegrationProcessLocalService
 * @generated
 */
public class CommerceDataIntegrationProcessLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.data.integration.service.impl.CommerceDataIntegrationProcessLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the commerce data integration process to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceDataIntegrationProcessLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceDataIntegrationProcess the commerce data integration process
	 * @return the commerce data integration process that was added
	 */
	public static CommerceDataIntegrationProcess
		addCommerceDataIntegrationProcess(
			CommerceDataIntegrationProcess commerceDataIntegrationProcess) {

		return getService().addCommerceDataIntegrationProcess(
			commerceDataIntegrationProcess);
	}

	public static CommerceDataIntegrationProcess
			addCommerceDataIntegrationProcess(
				long userId, String name, String type,
				com.liferay.portal.kernel.util.UnicodeProperties
					typeSettingsUnicodeProperties,
				boolean system)
		throws PortalException {

		return getService().addCommerceDataIntegrationProcess(
			userId, name, type, typeSettingsUnicodeProperties, system);
	}

	/**
	 * Creates a new commerce data integration process with the primary key. Does not add the commerce data integration process to the database.
	 *
	 * @param commerceDataIntegrationProcessId the primary key for the new commerce data integration process
	 * @return the new commerce data integration process
	 */
	public static CommerceDataIntegrationProcess
		createCommerceDataIntegrationProcess(
			long commerceDataIntegrationProcessId) {

		return getService().createCommerceDataIntegrationProcess(
			commerceDataIntegrationProcessId);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel createPersistedModel(
			Serializable primaryKeyObj)
		throws PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Deletes the commerce data integration process from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceDataIntegrationProcessLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceDataIntegrationProcess the commerce data integration process
	 * @return the commerce data integration process that was removed
	 * @throws PortalException
	 */
	public static CommerceDataIntegrationProcess
			deleteCommerceDataIntegrationProcess(
				CommerceDataIntegrationProcess commerceDataIntegrationProcess)
		throws PortalException {

		return getService().deleteCommerceDataIntegrationProcess(
			commerceDataIntegrationProcess);
	}

	/**
	 * Deletes the commerce data integration process with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceDataIntegrationProcessLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceDataIntegrationProcessId the primary key of the commerce data integration process
	 * @return the commerce data integration process that was removed
	 * @throws PortalException if a commerce data integration process with the primary key could not be found
	 */
	public static CommerceDataIntegrationProcess
			deleteCommerceDataIntegrationProcess(
				long commerceDataIntegrationProcessId)
		throws PortalException {

		return getService().deleteCommerceDataIntegrationProcess(
			commerceDataIntegrationProcessId);
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel deletePersistedModel(
			PersistedModel persistedModel)
		throws PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	public static <T> T dslQuery(DSLQuery dslQuery) {
		return getService().dslQuery(dslQuery);
	}

	public static int dslQueryCount(DSLQuery dslQuery) {
		return getService().dslQueryCount(dslQuery);
	}

	public static DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.data.integration.model.impl.CommerceDataIntegrationProcessModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.data.integration.model.impl.CommerceDataIntegrationProcessModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(DynamicQuery dynamicQuery) {
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
		DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static CommerceDataIntegrationProcess
		fetchCommerceDataIntegrationProcess(
			long commerceDataIntegrationProcessId) {

		return getService().fetchCommerceDataIntegrationProcess(
			commerceDataIntegrationProcessId);
	}

	public static CommerceDataIntegrationProcess
		fetchCommerceDataIntegrationProcess(long companyId, String name) {

		return getService().fetchCommerceDataIntegrationProcess(
			companyId, name);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	/**
	 * Returns the commerce data integration process with the primary key.
	 *
	 * @param commerceDataIntegrationProcessId the primary key of the commerce data integration process
	 * @return the commerce data integration process
	 * @throws PortalException if a commerce data integration process with the primary key could not be found
	 */
	public static CommerceDataIntegrationProcess
			getCommerceDataIntegrationProcess(
				long commerceDataIntegrationProcessId)
		throws PortalException {

		return getService().getCommerceDataIntegrationProcess(
			commerceDataIntegrationProcessId);
	}

	/**
	 * Returns a range of all the commerce data integration processes.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.data.integration.model.impl.CommerceDataIntegrationProcessModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce data integration processes
	 * @param end the upper bound of the range of commerce data integration processes (not inclusive)
	 * @return the range of commerce data integration processes
	 */
	public static List<CommerceDataIntegrationProcess>
		getCommerceDataIntegrationProcesses(int start, int end) {

		return getService().getCommerceDataIntegrationProcesses(start, end);
	}

	public static List<CommerceDataIntegrationProcess>
		getCommerceDataIntegrationProcesses(
			long companyId, int start, int end) {

		return getService().getCommerceDataIntegrationProcesses(
			companyId, start, end);
	}

	/**
	 * Returns the number of commerce data integration processes.
	 *
	 * @return the number of commerce data integration processes
	 */
	public static int getCommerceDataIntegrationProcessesCount() {
		return getService().getCommerceDataIntegrationProcessesCount();
	}

	public static int getCommerceDataIntegrationProcessesCount(long companyId) {
		return getService().getCommerceDataIntegrationProcessesCount(companyId);
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Updates the commerce data integration process in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceDataIntegrationProcessLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceDataIntegrationProcess the commerce data integration process
	 * @return the commerce data integration process that was updated
	 */
	public static CommerceDataIntegrationProcess
		updateCommerceDataIntegrationProcess(
			CommerceDataIntegrationProcess commerceDataIntegrationProcess) {

		return getService().updateCommerceDataIntegrationProcess(
			commerceDataIntegrationProcess);
	}

	public static CommerceDataIntegrationProcess
			updateCommerceDataIntegrationProcess(
				long commerceDataIntegrationProcessId, String name,
				com.liferay.portal.kernel.util.UnicodeProperties
					typeSettingsUnicodeProperties)
		throws PortalException {

		return getService().updateCommerceDataIntegrationProcess(
			commerceDataIntegrationProcessId, name,
			typeSettingsUnicodeProperties);
	}

	public static CommerceDataIntegrationProcess
			updateCommerceDataIntegrationProcessTrigger(
				long commerceDataIntegrationProcessId, boolean active,
				String cronExpression, int startDateMonth, int startDateDay,
				int startDateYear, int startDateHour, int startDateMinute,
				int endDateMonth, int endDateDay, int endDateYear,
				int endDateHour, int endDateMinute, boolean neverEnd)
		throws PortalException {

		return getService().updateCommerceDataIntegrationProcessTrigger(
			commerceDataIntegrationProcessId, active, cronExpression,
			startDateMonth, startDateDay, startDateYear, startDateHour,
			startDateMinute, endDateMonth, endDateDay, endDateYear, endDateHour,
			endDateMinute, neverEnd);
	}

	public static CommerceDataIntegrationProcessLocalService getService() {
		return _service;
	}

	private static volatile CommerceDataIntegrationProcessLocalService _service;

}