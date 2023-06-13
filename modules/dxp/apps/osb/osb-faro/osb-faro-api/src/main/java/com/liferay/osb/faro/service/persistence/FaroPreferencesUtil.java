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

package com.liferay.osb.faro.service.persistence;

import com.liferay.osb.faro.model.FaroPreferences;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence utility for the faro preferences service. This utility wraps <code>com.liferay.osb.faro.service.persistence.impl.FaroPreferencesPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Matthew Kong
 * @see FaroPreferencesPersistence
 * @generated
 */
public class FaroPreferencesUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(FaroPreferences faroPreferences) {
		getPersistence().clearCache(faroPreferences);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, FaroPreferences> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<FaroPreferences> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<FaroPreferences> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<FaroPreferences> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<FaroPreferences> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static FaroPreferences update(FaroPreferences faroPreferences) {
		return getPersistence().update(faroPreferences);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static FaroPreferences update(
		FaroPreferences faroPreferences, ServiceContext serviceContext) {

		return getPersistence().update(faroPreferences, serviceContext);
	}

	/**
	 * Returns all the faro preferenceses where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching faro preferenceses
	 */
	public static List<FaroPreferences> findByGroupId(long groupId) {
		return getPersistence().findByGroupId(groupId);
	}

	/**
	 * Returns a range of all the faro preferenceses where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroPreferencesModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of faro preferenceses
	 * @param end the upper bound of the range of faro preferenceses (not inclusive)
	 * @return the range of matching faro preferenceses
	 */
	public static List<FaroPreferences> findByGroupId(
		long groupId, int start, int end) {

		return getPersistence().findByGroupId(groupId, start, end);
	}

	/**
	 * Returns an ordered range of all the faro preferenceses where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroPreferencesModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of faro preferenceses
	 * @param end the upper bound of the range of faro preferenceses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching faro preferenceses
	 */
	public static List<FaroPreferences> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<FaroPreferences> orderByComparator) {

		return getPersistence().findByGroupId(
			groupId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the faro preferenceses where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroPreferencesModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of faro preferenceses
	 * @param end the upper bound of the range of faro preferenceses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching faro preferenceses
	 */
	public static List<FaroPreferences> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<FaroPreferences> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByGroupId(
			groupId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first faro preferences in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching faro preferences
	 * @throws NoSuchFaroPreferencesException if a matching faro preferences could not be found
	 */
	public static FaroPreferences findByGroupId_First(
			long groupId, OrderByComparator<FaroPreferences> orderByComparator)
		throws com.liferay.osb.faro.exception.NoSuchFaroPreferencesException {

		return getPersistence().findByGroupId_First(groupId, orderByComparator);
	}

	/**
	 * Returns the first faro preferences in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching faro preferences, or <code>null</code> if a matching faro preferences could not be found
	 */
	public static FaroPreferences fetchByGroupId_First(
		long groupId, OrderByComparator<FaroPreferences> orderByComparator) {

		return getPersistence().fetchByGroupId_First(
			groupId, orderByComparator);
	}

	/**
	 * Returns the last faro preferences in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching faro preferences
	 * @throws NoSuchFaroPreferencesException if a matching faro preferences could not be found
	 */
	public static FaroPreferences findByGroupId_Last(
			long groupId, OrderByComparator<FaroPreferences> orderByComparator)
		throws com.liferay.osb.faro.exception.NoSuchFaroPreferencesException {

		return getPersistence().findByGroupId_Last(groupId, orderByComparator);
	}

	/**
	 * Returns the last faro preferences in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching faro preferences, or <code>null</code> if a matching faro preferences could not be found
	 */
	public static FaroPreferences fetchByGroupId_Last(
		long groupId, OrderByComparator<FaroPreferences> orderByComparator) {

		return getPersistence().fetchByGroupId_Last(groupId, orderByComparator);
	}

	/**
	 * Returns the faro preferenceses before and after the current faro preferences in the ordered set where groupId = &#63;.
	 *
	 * @param faroPreferencesId the primary key of the current faro preferences
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next faro preferences
	 * @throws NoSuchFaroPreferencesException if a faro preferences with the primary key could not be found
	 */
	public static FaroPreferences[] findByGroupId_PrevAndNext(
			long faroPreferencesId, long groupId,
			OrderByComparator<FaroPreferences> orderByComparator)
		throws com.liferay.osb.faro.exception.NoSuchFaroPreferencesException {

		return getPersistence().findByGroupId_PrevAndNext(
			faroPreferencesId, groupId, orderByComparator);
	}

	/**
	 * Removes all the faro preferenceses where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	public static void removeByGroupId(long groupId) {
		getPersistence().removeByGroupId(groupId);
	}

	/**
	 * Returns the number of faro preferenceses where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching faro preferenceses
	 */
	public static int countByGroupId(long groupId) {
		return getPersistence().countByGroupId(groupId);
	}

	/**
	 * Returns the faro preferences where groupId = &#63; and ownerId = &#63; or throws a <code>NoSuchFaroPreferencesException</code> if it could not be found.
	 *
	 * @param groupId the group ID
	 * @param ownerId the owner ID
	 * @return the matching faro preferences
	 * @throws NoSuchFaroPreferencesException if a matching faro preferences could not be found
	 */
	public static FaroPreferences findByG_O(long groupId, long ownerId)
		throws com.liferay.osb.faro.exception.NoSuchFaroPreferencesException {

		return getPersistence().findByG_O(groupId, ownerId);
	}

	/**
	 * Returns the faro preferences where groupId = &#63; and ownerId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param groupId the group ID
	 * @param ownerId the owner ID
	 * @return the matching faro preferences, or <code>null</code> if a matching faro preferences could not be found
	 */
	public static FaroPreferences fetchByG_O(long groupId, long ownerId) {
		return getPersistence().fetchByG_O(groupId, ownerId);
	}

	/**
	 * Returns the faro preferences where groupId = &#63; and ownerId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param groupId the group ID
	 * @param ownerId the owner ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching faro preferences, or <code>null</code> if a matching faro preferences could not be found
	 */
	public static FaroPreferences fetchByG_O(
		long groupId, long ownerId, boolean useFinderCache) {

		return getPersistence().fetchByG_O(groupId, ownerId, useFinderCache);
	}

	/**
	 * Removes the faro preferences where groupId = &#63; and ownerId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param ownerId the owner ID
	 * @return the faro preferences that was removed
	 */
	public static FaroPreferences removeByG_O(long groupId, long ownerId)
		throws com.liferay.osb.faro.exception.NoSuchFaroPreferencesException {

		return getPersistence().removeByG_O(groupId, ownerId);
	}

	/**
	 * Returns the number of faro preferenceses where groupId = &#63; and ownerId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param ownerId the owner ID
	 * @return the number of matching faro preferenceses
	 */
	public static int countByG_O(long groupId, long ownerId) {
		return getPersistence().countByG_O(groupId, ownerId);
	}

	/**
	 * Caches the faro preferences in the entity cache if it is enabled.
	 *
	 * @param faroPreferences the faro preferences
	 */
	public static void cacheResult(FaroPreferences faroPreferences) {
		getPersistence().cacheResult(faroPreferences);
	}

	/**
	 * Caches the faro preferenceses in the entity cache if it is enabled.
	 *
	 * @param faroPreferenceses the faro preferenceses
	 */
	public static void cacheResult(List<FaroPreferences> faroPreferenceses) {
		getPersistence().cacheResult(faroPreferenceses);
	}

	/**
	 * Creates a new faro preferences with the primary key. Does not add the faro preferences to the database.
	 *
	 * @param faroPreferencesId the primary key for the new faro preferences
	 * @return the new faro preferences
	 */
	public static FaroPreferences create(long faroPreferencesId) {
		return getPersistence().create(faroPreferencesId);
	}

	/**
	 * Removes the faro preferences with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param faroPreferencesId the primary key of the faro preferences
	 * @return the faro preferences that was removed
	 * @throws NoSuchFaroPreferencesException if a faro preferences with the primary key could not be found
	 */
	public static FaroPreferences remove(long faroPreferencesId)
		throws com.liferay.osb.faro.exception.NoSuchFaroPreferencesException {

		return getPersistence().remove(faroPreferencesId);
	}

	public static FaroPreferences updateImpl(FaroPreferences faroPreferences) {
		return getPersistence().updateImpl(faroPreferences);
	}

	/**
	 * Returns the faro preferences with the primary key or throws a <code>NoSuchFaroPreferencesException</code> if it could not be found.
	 *
	 * @param faroPreferencesId the primary key of the faro preferences
	 * @return the faro preferences
	 * @throws NoSuchFaroPreferencesException if a faro preferences with the primary key could not be found
	 */
	public static FaroPreferences findByPrimaryKey(long faroPreferencesId)
		throws com.liferay.osb.faro.exception.NoSuchFaroPreferencesException {

		return getPersistence().findByPrimaryKey(faroPreferencesId);
	}

	/**
	 * Returns the faro preferences with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param faroPreferencesId the primary key of the faro preferences
	 * @return the faro preferences, or <code>null</code> if a faro preferences with the primary key could not be found
	 */
	public static FaroPreferences fetchByPrimaryKey(long faroPreferencesId) {
		return getPersistence().fetchByPrimaryKey(faroPreferencesId);
	}

	/**
	 * Returns all the faro preferenceses.
	 *
	 * @return the faro preferenceses
	 */
	public static List<FaroPreferences> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the faro preferenceses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroPreferencesModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro preferenceses
	 * @param end the upper bound of the range of faro preferenceses (not inclusive)
	 * @return the range of faro preferenceses
	 */
	public static List<FaroPreferences> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the faro preferenceses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroPreferencesModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro preferenceses
	 * @param end the upper bound of the range of faro preferenceses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of faro preferenceses
	 */
	public static List<FaroPreferences> findAll(
		int start, int end,
		OrderByComparator<FaroPreferences> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the faro preferenceses.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>FaroPreferencesModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of faro preferenceses
	 * @param end the upper bound of the range of faro preferenceses (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of faro preferenceses
	 */
	public static List<FaroPreferences> findAll(
		int start, int end,
		OrderByComparator<FaroPreferences> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the faro preferenceses from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of faro preferenceses.
	 *
	 * @return the number of faro preferenceses
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static FaroPreferencesPersistence getPersistence() {
		return _persistence;
	}

	private static volatile FaroPreferencesPersistence _persistence;

}