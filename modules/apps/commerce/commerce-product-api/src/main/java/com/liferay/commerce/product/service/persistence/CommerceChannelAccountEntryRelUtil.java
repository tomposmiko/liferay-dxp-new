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

package com.liferay.commerce.product.service.persistence;

import com.liferay.commerce.product.model.CommerceChannelAccountEntryRel;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence utility for the commerce channel account entry rel service. This utility wraps <code>com.liferay.commerce.product.service.persistence.impl.CommerceChannelAccountEntryRelPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Marco Leo
 * @see CommerceChannelAccountEntryRelPersistence
 * @generated
 */
public class CommerceChannelAccountEntryRelUtil {

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
	public static void clearCache(
		CommerceChannelAccountEntryRel commerceChannelAccountEntryRel) {

		getPersistence().clearCache(commerceChannelAccountEntryRel);
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
	public static Map<Serializable, CommerceChannelAccountEntryRel>
		fetchByPrimaryKeys(Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<CommerceChannelAccountEntryRel> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {

		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<CommerceChannelAccountEntryRel> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<CommerceChannelAccountEntryRel> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static CommerceChannelAccountEntryRel update(
		CommerceChannelAccountEntryRel commerceChannelAccountEntryRel) {

		return getPersistence().update(commerceChannelAccountEntryRel);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static CommerceChannelAccountEntryRel update(
		CommerceChannelAccountEntryRel commerceChannelAccountEntryRel,
		ServiceContext serviceContext) {

		return getPersistence().update(
			commerceChannelAccountEntryRel, serviceContext);
	}

	/**
	 * Returns all the commerce channel account entry rels where accountEntryId = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @return the matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByAccountEntryId(
		long accountEntryId) {

		return getPersistence().findByAccountEntryId(accountEntryId);
	}

	/**
	 * Returns a range of all the commerce channel account entry rels where accountEntryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param accountEntryId the account entry ID
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @return the range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByAccountEntryId(
		long accountEntryId, int start, int end) {

		return getPersistence().findByAccountEntryId(
			accountEntryId, start, end);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels where accountEntryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param accountEntryId the account entry ID
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByAccountEntryId(
		long accountEntryId, int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().findByAccountEntryId(
			accountEntryId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels where accountEntryId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param accountEntryId the account entry ID
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByAccountEntryId(
		long accountEntryId, int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByAccountEntryId(
			accountEntryId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where accountEntryId = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByAccountEntryId_First(
			long accountEntryId,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByAccountEntryId_First(
			accountEntryId, orderByComparator);
	}

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where accountEntryId = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByAccountEntryId_First(
		long accountEntryId,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().fetchByAccountEntryId_First(
			accountEntryId, orderByComparator);
	}

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where accountEntryId = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByAccountEntryId_Last(
			long accountEntryId,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByAccountEntryId_Last(
			accountEntryId, orderByComparator);
	}

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where accountEntryId = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByAccountEntryId_Last(
		long accountEntryId,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().fetchByAccountEntryId_Last(
			accountEntryId, orderByComparator);
	}

	/**
	 * Returns the commerce channel account entry rels before and after the current commerce channel account entry rel in the ordered set where accountEntryId = &#63;.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the current commerce channel account entry rel
	 * @param accountEntryId the account entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a commerce channel account entry rel with the primary key could not be found
	 */
	public static CommerceChannelAccountEntryRel[]
			findByAccountEntryId_PrevAndNext(
				long commerceChannelAccountEntryRelId, long accountEntryId,
				OrderByComparator<CommerceChannelAccountEntryRel>
					orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByAccountEntryId_PrevAndNext(
			commerceChannelAccountEntryRelId, accountEntryId,
			orderByComparator);
	}

	/**
	 * Removes all the commerce channel account entry rels where accountEntryId = &#63; from the database.
	 *
	 * @param accountEntryId the account entry ID
	 */
	public static void removeByAccountEntryId(long accountEntryId) {
		getPersistence().removeByAccountEntryId(accountEntryId);
	}

	/**
	 * Returns the number of commerce channel account entry rels where accountEntryId = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @return the number of matching commerce channel account entry rels
	 */
	public static int countByAccountEntryId(long accountEntryId) {
		return getPersistence().countByAccountEntryId(accountEntryId);
	}

	/**
	 * Returns all the commerce channel account entry rels where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @return the matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByCommerceChannelId(
		long commerceChannelId) {

		return getPersistence().findByCommerceChannelId(commerceChannelId);
	}

	/**
	 * Returns a range of all the commerce channel account entry rels where commerceChannelId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @return the range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByCommerceChannelId(
		long commerceChannelId, int start, int end) {

		return getPersistence().findByCommerceChannelId(
			commerceChannelId, start, end);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels where commerceChannelId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByCommerceChannelId(
		long commerceChannelId, int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().findByCommerceChannelId(
			commerceChannelId, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels where commerceChannelId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByCommerceChannelId(
		long commerceChannelId, int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByCommerceChannelId(
			commerceChannelId, start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByCommerceChannelId_First(
			long commerceChannelId,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByCommerceChannelId_First(
			commerceChannelId, orderByComparator);
	}

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByCommerceChannelId_First(
		long commerceChannelId,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().fetchByCommerceChannelId_First(
			commerceChannelId, orderByComparator);
	}

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByCommerceChannelId_Last(
			long commerceChannelId,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByCommerceChannelId_Last(
			commerceChannelId, orderByComparator);
	}

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByCommerceChannelId_Last(
		long commerceChannelId,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().fetchByCommerceChannelId_Last(
			commerceChannelId, orderByComparator);
	}

	/**
	 * Returns the commerce channel account entry rels before and after the current commerce channel account entry rel in the ordered set where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the current commerce channel account entry rel
	 * @param commerceChannelId the commerce channel ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a commerce channel account entry rel with the primary key could not be found
	 */
	public static CommerceChannelAccountEntryRel[]
			findByCommerceChannelId_PrevAndNext(
				long commerceChannelAccountEntryRelId, long commerceChannelId,
				OrderByComparator<CommerceChannelAccountEntryRel>
					orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByCommerceChannelId_PrevAndNext(
			commerceChannelAccountEntryRelId, commerceChannelId,
			orderByComparator);
	}

	/**
	 * Removes all the commerce channel account entry rels where commerceChannelId = &#63; from the database.
	 *
	 * @param commerceChannelId the commerce channel ID
	 */
	public static void removeByCommerceChannelId(long commerceChannelId) {
		getPersistence().removeByCommerceChannelId(commerceChannelId);
	}

	/**
	 * Returns the number of commerce channel account entry rels where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @return the number of matching commerce channel account entry rels
	 */
	public static int countByCommerceChannelId(long commerceChannelId) {
		return getPersistence().countByCommerceChannelId(commerceChannelId);
	}

	/**
	 * Returns all the commerce channel account entry rels where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @return the matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByA_T(
		long accountEntryId, int type) {

		return getPersistence().findByA_T(accountEntryId, type);
	}

	/**
	 * Returns a range of all the commerce channel account entry rels where accountEntryId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @return the range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByA_T(
		long accountEntryId, int type, int start, int end) {

		return getPersistence().findByA_T(accountEntryId, type, start, end);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels where accountEntryId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByA_T(
		long accountEntryId, int type, int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().findByA_T(
			accountEntryId, type, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels where accountEntryId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByA_T(
		long accountEntryId, int type, int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByA_T(
			accountEntryId, type, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByA_T_First(
			long accountEntryId, int type,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByA_T_First(
			accountEntryId, type, orderByComparator);
	}

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByA_T_First(
		long accountEntryId, int type,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().fetchByA_T_First(
			accountEntryId, type, orderByComparator);
	}

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByA_T_Last(
			long accountEntryId, int type,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByA_T_Last(
			accountEntryId, type, orderByComparator);
	}

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByA_T_Last(
		long accountEntryId, int type,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().fetchByA_T_Last(
			accountEntryId, type, orderByComparator);
	}

	/**
	 * Returns the commerce channel account entry rels before and after the current commerce channel account entry rel in the ordered set where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the current commerce channel account entry rel
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a commerce channel account entry rel with the primary key could not be found
	 */
	public static CommerceChannelAccountEntryRel[] findByA_T_PrevAndNext(
			long commerceChannelAccountEntryRelId, long accountEntryId,
			int type,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByA_T_PrevAndNext(
			commerceChannelAccountEntryRelId, accountEntryId, type,
			orderByComparator);
	}

	/**
	 * Removes all the commerce channel account entry rels where accountEntryId = &#63; and type = &#63; from the database.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 */
	public static void removeByA_T(long accountEntryId, int type) {
		getPersistence().removeByA_T(accountEntryId, type);
	}

	/**
	 * Returns the number of commerce channel account entry rels where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @return the number of matching commerce channel account entry rels
	 */
	public static int countByA_T(long accountEntryId, int type) {
		return getPersistence().countByA_T(accountEntryId, type);
	}

	/**
	 * Returns all the commerce channel account entry rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByC_C(
		long classNameId, long classPK) {

		return getPersistence().findByC_C(classNameId, classPK);
	}

	/**
	 * Returns a range of all the commerce channel account entry rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @return the range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByC_C(
		long classNameId, long classPK, int start, int end) {

		return getPersistence().findByC_C(classNameId, classPK, start, end);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByC_C(
		long classNameId, long classPK, int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().findByC_C(
			classNameId, classPK, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByC_C(
		long classNameId, long classPK, int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByC_C(
			classNameId, classPK, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByC_C_First(
			long classNameId, long classPK,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByC_C_First(
			classNameId, classPK, orderByComparator);
	}

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByC_C_First(
		long classNameId, long classPK,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().fetchByC_C_First(
			classNameId, classPK, orderByComparator);
	}

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByC_C_Last(
			long classNameId, long classPK,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByC_C_Last(
			classNameId, classPK, orderByComparator);
	}

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByC_C_Last(
		long classNameId, long classPK,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().fetchByC_C_Last(
			classNameId, classPK, orderByComparator);
	}

	/**
	 * Returns the commerce channel account entry rels before and after the current commerce channel account entry rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the current commerce channel account entry rel
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a commerce channel account entry rel with the primary key could not be found
	 */
	public static CommerceChannelAccountEntryRel[] findByC_C_PrevAndNext(
			long commerceChannelAccountEntryRelId, long classNameId,
			long classPK,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByC_C_PrevAndNext(
			commerceChannelAccountEntryRelId, classNameId, classPK,
			orderByComparator);
	}

	/**
	 * Removes all the commerce channel account entry rels where classNameId = &#63; and classPK = &#63; from the database.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 */
	public static void removeByC_C(long classNameId, long classPK) {
		getPersistence().removeByC_C(classNameId, classPK);
	}

	/**
	 * Returns the number of commerce channel account entry rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the number of matching commerce channel account entry rels
	 */
	public static int countByC_C(long classNameId, long classPK) {
		return getPersistence().countByC_C(classNameId, classPK);
	}

	/**
	 * Returns all the commerce channel account entry rels where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @return the matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByC_T(
		long commerceChannelId, int type) {

		return getPersistence().findByC_T(commerceChannelId, type);
	}

	/**
	 * Returns a range of all the commerce channel account entry rels where commerceChannelId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @return the range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByC_T(
		long commerceChannelId, int type, int start, int end) {

		return getPersistence().findByC_T(commerceChannelId, type, start, end);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels where commerceChannelId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByC_T(
		long commerceChannelId, int type, int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().findByC_T(
			commerceChannelId, type, start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels where commerceChannelId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByC_T(
		long commerceChannelId, int type, int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByC_T(
			commerceChannelId, type, start, end, orderByComparator,
			useFinderCache);
	}

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByC_T_First(
			long commerceChannelId, int type,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByC_T_First(
			commerceChannelId, type, orderByComparator);
	}

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByC_T_First(
		long commerceChannelId, int type,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().fetchByC_T_First(
			commerceChannelId, type, orderByComparator);
	}

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByC_T_Last(
			long commerceChannelId, int type,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByC_T_Last(
			commerceChannelId, type, orderByComparator);
	}

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByC_T_Last(
		long commerceChannelId, int type,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().fetchByC_T_Last(
			commerceChannelId, type, orderByComparator);
	}

	/**
	 * Returns the commerce channel account entry rels before and after the current commerce channel account entry rel in the ordered set where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the current commerce channel account entry rel
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a commerce channel account entry rel with the primary key could not be found
	 */
	public static CommerceChannelAccountEntryRel[] findByC_T_PrevAndNext(
			long commerceChannelAccountEntryRelId, long commerceChannelId,
			int type,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByC_T_PrevAndNext(
			commerceChannelAccountEntryRelId, commerceChannelId, type,
			orderByComparator);
	}

	/**
	 * Removes all the commerce channel account entry rels where commerceChannelId = &#63; and type = &#63; from the database.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 */
	public static void removeByC_T(long commerceChannelId, int type) {
		getPersistence().removeByC_T(commerceChannelId, type);
	}

	/**
	 * Returns the number of commerce channel account entry rels where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @return the number of matching commerce channel account entry rels
	 */
	public static int countByC_T(long commerceChannelId, int type) {
		return getPersistence().countByC_T(commerceChannelId, type);
	}

	/**
	 * Returns all the commerce channel account entry rels where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @return the matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByA_C_T(
		long accountEntryId, long commerceChannelId, int type) {

		return getPersistence().findByA_C_T(
			accountEntryId, commerceChannelId, type);
	}

	/**
	 * Returns a range of all the commerce channel account entry rels where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @return the range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByA_C_T(
		long accountEntryId, long commerceChannelId, int type, int start,
		int end) {

		return getPersistence().findByA_C_T(
			accountEntryId, commerceChannelId, type, start, end);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByA_C_T(
		long accountEntryId, long commerceChannelId, int type, int start,
		int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().findByA_C_T(
			accountEntryId, commerceChannelId, type, start, end,
			orderByComparator);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findByA_C_T(
		long accountEntryId, long commerceChannelId, int type, int start,
		int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findByA_C_T(
			accountEntryId, commerceChannelId, type, start, end,
			orderByComparator, useFinderCache);
	}

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByA_C_T_First(
			long accountEntryId, long commerceChannelId, int type,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByA_C_T_First(
			accountEntryId, commerceChannelId, type, orderByComparator);
	}

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByA_C_T_First(
		long accountEntryId, long commerceChannelId, int type,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().fetchByA_C_T_First(
			accountEntryId, commerceChannelId, type, orderByComparator);
	}

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByA_C_T_Last(
			long accountEntryId, long commerceChannelId, int type,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByA_C_T_Last(
			accountEntryId, commerceChannelId, type, orderByComparator);
	}

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByA_C_T_Last(
		long accountEntryId, long commerceChannelId, int type,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().fetchByA_C_T_Last(
			accountEntryId, commerceChannelId, type, orderByComparator);
	}

	/**
	 * Returns the commerce channel account entry rels before and after the current commerce channel account entry rel in the ordered set where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the current commerce channel account entry rel
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a commerce channel account entry rel with the primary key could not be found
	 */
	public static CommerceChannelAccountEntryRel[] findByA_C_T_PrevAndNext(
			long commerceChannelAccountEntryRelId, long accountEntryId,
			long commerceChannelId, int type,
			OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByA_C_T_PrevAndNext(
			commerceChannelAccountEntryRelId, accountEntryId, commerceChannelId,
			type, orderByComparator);
	}

	/**
	 * Removes all the commerce channel account entry rels where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63; from the database.
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 */
	public static void removeByA_C_T(
		long accountEntryId, long commerceChannelId, int type) {

		getPersistence().removeByA_C_T(accountEntryId, commerceChannelId, type);
	}

	/**
	 * Returns the number of commerce channel account entry rels where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @return the number of matching commerce channel account entry rels
	 */
	public static int countByA_C_T(
		long accountEntryId, long commerceChannelId, int type) {

		return getPersistence().countByA_C_T(
			accountEntryId, commerceChannelId, type);
	}

	/**
	 * Returns the commerce channel account entry rel where accountEntryId = &#63; and classNameId = &#63; and classPK = &#63; and commerceChannelId = &#63; and type = &#63; or throws a <code>NoSuchChannelAccountEntryRelException</code> if it could not be found.
	 *
	 * @param accountEntryId the account entry ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @return the matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel findByA_C_C_C_T(
			long accountEntryId, long classNameId, long classPK,
			long commerceChannelId, int type)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByA_C_C_C_T(
			accountEntryId, classNameId, classPK, commerceChannelId, type);
	}

	/**
	 * Returns the commerce channel account entry rel where accountEntryId = &#63; and classNameId = &#63; and classPK = &#63; and commerceChannelId = &#63; and type = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param accountEntryId the account entry ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @return the matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByA_C_C_C_T(
		long accountEntryId, long classNameId, long classPK,
		long commerceChannelId, int type) {

		return getPersistence().fetchByA_C_C_C_T(
			accountEntryId, classNameId, classPK, commerceChannelId, type);
	}

	/**
	 * Returns the commerce channel account entry rel where accountEntryId = &#63; and classNameId = &#63; and classPK = &#63; and commerceChannelId = &#63; and type = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param accountEntryId the account entry ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByA_C_C_C_T(
		long accountEntryId, long classNameId, long classPK,
		long commerceChannelId, int type, boolean useFinderCache) {

		return getPersistence().fetchByA_C_C_C_T(
			accountEntryId, classNameId, classPK, commerceChannelId, type,
			useFinderCache);
	}

	/**
	 * Removes the commerce channel account entry rel where accountEntryId = &#63; and classNameId = &#63; and classPK = &#63; and commerceChannelId = &#63; and type = &#63; from the database.
	 *
	 * @param accountEntryId the account entry ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @return the commerce channel account entry rel that was removed
	 */
	public static CommerceChannelAccountEntryRel removeByA_C_C_C_T(
			long accountEntryId, long classNameId, long classPK,
			long commerceChannelId, int type)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().removeByA_C_C_C_T(
			accountEntryId, classNameId, classPK, commerceChannelId, type);
	}

	/**
	 * Returns the number of commerce channel account entry rels where accountEntryId = &#63; and classNameId = &#63; and classPK = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @return the number of matching commerce channel account entry rels
	 */
	public static int countByA_C_C_C_T(
		long accountEntryId, long classNameId, long classPK,
		long commerceChannelId, int type) {

		return getPersistence().countByA_C_C_C_T(
			accountEntryId, classNameId, classPK, commerceChannelId, type);
	}

	/**
	 * Caches the commerce channel account entry rel in the entity cache if it is enabled.
	 *
	 * @param commerceChannelAccountEntryRel the commerce channel account entry rel
	 */
	public static void cacheResult(
		CommerceChannelAccountEntryRel commerceChannelAccountEntryRel) {

		getPersistence().cacheResult(commerceChannelAccountEntryRel);
	}

	/**
	 * Caches the commerce channel account entry rels in the entity cache if it is enabled.
	 *
	 * @param commerceChannelAccountEntryRels the commerce channel account entry rels
	 */
	public static void cacheResult(
		List<CommerceChannelAccountEntryRel> commerceChannelAccountEntryRels) {

		getPersistence().cacheResult(commerceChannelAccountEntryRels);
	}

	/**
	 * Creates a new commerce channel account entry rel with the primary key. Does not add the commerce channel account entry rel to the database.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key for the new commerce channel account entry rel
	 * @return the new commerce channel account entry rel
	 */
	public static CommerceChannelAccountEntryRel create(
		long commerceChannelAccountEntryRelId) {

		return getPersistence().create(commerceChannelAccountEntryRelId);
	}

	/**
	 * Removes the commerce channel account entry rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the commerce channel account entry rel
	 * @return the commerce channel account entry rel that was removed
	 * @throws NoSuchChannelAccountEntryRelException if a commerce channel account entry rel with the primary key could not be found
	 */
	public static CommerceChannelAccountEntryRel remove(
			long commerceChannelAccountEntryRelId)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().remove(commerceChannelAccountEntryRelId);
	}

	public static CommerceChannelAccountEntryRel updateImpl(
		CommerceChannelAccountEntryRel commerceChannelAccountEntryRel) {

		return getPersistence().updateImpl(commerceChannelAccountEntryRel);
	}

	/**
	 * Returns the commerce channel account entry rel with the primary key or throws a <code>NoSuchChannelAccountEntryRelException</code> if it could not be found.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the commerce channel account entry rel
	 * @return the commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a commerce channel account entry rel with the primary key could not be found
	 */
	public static CommerceChannelAccountEntryRel findByPrimaryKey(
			long commerceChannelAccountEntryRelId)
		throws com.liferay.commerce.product.exception.
			NoSuchChannelAccountEntryRelException {

		return getPersistence().findByPrimaryKey(
			commerceChannelAccountEntryRelId);
	}

	/**
	 * Returns the commerce channel account entry rel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the commerce channel account entry rel
	 * @return the commerce channel account entry rel, or <code>null</code> if a commerce channel account entry rel with the primary key could not be found
	 */
	public static CommerceChannelAccountEntryRel fetchByPrimaryKey(
		long commerceChannelAccountEntryRelId) {

		return getPersistence().fetchByPrimaryKey(
			commerceChannelAccountEntryRelId);
	}

	/**
	 * Returns all the commerce channel account entry rels.
	 *
	 * @return the commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findAll() {
		return getPersistence().findAll();
	}

	/**
	 * Returns a range of all the commerce channel account entry rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @return the range of commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findAll(
		int start, int end) {

		return getPersistence().findAll(start, end);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findAll(
		int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	 * Returns an ordered range of all the commerce channel account entry rels.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>CommerceChannelAccountEntryRelModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce channel account entry rels
	 * @param end the upper bound of the range of commerce channel account entry rels (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of commerce channel account entry rels
	 */
	public static List<CommerceChannelAccountEntryRel> findAll(
		int start, int end,
		OrderByComparator<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the commerce channel account entry rels from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of commerce channel account entry rels.
	 *
	 * @return the number of commerce channel account entry rels
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static CommerceChannelAccountEntryRelPersistence getPersistence() {
		return _persistence;
	}

	private static volatile CommerceChannelAccountEntryRelPersistence
		_persistence;

}