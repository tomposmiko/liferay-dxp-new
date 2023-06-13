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

import com.liferay.commerce.product.exception.NoSuchChannelAccountEntryRelException;
import com.liferay.commerce.product.model.CommerceChannelAccountEntryRel;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.change.tracking.CTPersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the commerce channel account entry rel service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Marco Leo
 * @see CommerceChannelAccountEntryRelUtil
 * @generated
 */
@ProviderType
public interface CommerceChannelAccountEntryRelPersistence
	extends BasePersistence<CommerceChannelAccountEntryRel>,
			CTPersistence<CommerceChannelAccountEntryRel> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link CommerceChannelAccountEntryRelUtil} to access the commerce channel account entry rel persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the commerce channel account entry rels where accountEntryId = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @return the matching commerce channel account entry rels
	 */
	public java.util.List<CommerceChannelAccountEntryRel> findByAccountEntryId(
		long accountEntryId);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByAccountEntryId(
		long accountEntryId, int start, int end);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByAccountEntryId(
		long accountEntryId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByAccountEntryId(
		long accountEntryId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where accountEntryId = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel findByAccountEntryId_First(
			long accountEntryId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where accountEntryId = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByAccountEntryId_First(
		long accountEntryId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where accountEntryId = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel findByAccountEntryId_Last(
			long accountEntryId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where accountEntryId = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByAccountEntryId_Last(
		long accountEntryId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

	/**
	 * Returns the commerce channel account entry rels before and after the current commerce channel account entry rel in the ordered set where accountEntryId = &#63;.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the current commerce channel account entry rel
	 * @param accountEntryId the account entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a commerce channel account entry rel with the primary key could not be found
	 */
	public CommerceChannelAccountEntryRel[] findByAccountEntryId_PrevAndNext(
			long commerceChannelAccountEntryRelId, long accountEntryId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Removes all the commerce channel account entry rels where accountEntryId = &#63; from the database.
	 *
	 * @param accountEntryId the account entry ID
	 */
	public void removeByAccountEntryId(long accountEntryId);

	/**
	 * Returns the number of commerce channel account entry rels where accountEntryId = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @return the number of matching commerce channel account entry rels
	 */
	public int countByAccountEntryId(long accountEntryId);

	/**
	 * Returns all the commerce channel account entry rels where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @return the matching commerce channel account entry rels
	 */
	public java.util.List<CommerceChannelAccountEntryRel>
		findByCommerceChannelId(long commerceChannelId);

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
	public java.util.List<CommerceChannelAccountEntryRel>
		findByCommerceChannelId(long commerceChannelId, int start, int end);

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
	public java.util.List<CommerceChannelAccountEntryRel>
		findByCommerceChannelId(
			long commerceChannelId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator);

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
	public java.util.List<CommerceChannelAccountEntryRel>
		findByCommerceChannelId(
			long commerceChannelId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator,
			boolean useFinderCache);

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel findByCommerceChannelId_First(
			long commerceChannelId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByCommerceChannelId_First(
		long commerceChannelId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel findByCommerceChannelId_Last(
			long commerceChannelId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByCommerceChannelId_Last(
		long commerceChannelId,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

	/**
	 * Returns the commerce channel account entry rels before and after the current commerce channel account entry rel in the ordered set where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the current commerce channel account entry rel
	 * @param commerceChannelId the commerce channel ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a commerce channel account entry rel with the primary key could not be found
	 */
	public CommerceChannelAccountEntryRel[] findByCommerceChannelId_PrevAndNext(
			long commerceChannelAccountEntryRelId, long commerceChannelId,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Removes all the commerce channel account entry rels where commerceChannelId = &#63; from the database.
	 *
	 * @param commerceChannelId the commerce channel ID
	 */
	public void removeByCommerceChannelId(long commerceChannelId);

	/**
	 * Returns the number of commerce channel account entry rels where commerceChannelId = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @return the number of matching commerce channel account entry rels
	 */
	public int countByCommerceChannelId(long commerceChannelId);

	/**
	 * Returns all the commerce channel account entry rels where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @return the matching commerce channel account entry rels
	 */
	public java.util.List<CommerceChannelAccountEntryRel> findByA_T(
		long accountEntryId, int type);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByA_T(
		long accountEntryId, int type, int start, int end);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByA_T(
		long accountEntryId, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByA_T(
		long accountEntryId, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel findByA_T_First(
			long accountEntryId, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByA_T_First(
		long accountEntryId, int type,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel findByA_T_Last(
			long accountEntryId, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByA_T_Last(
		long accountEntryId, int type,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

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
	public CommerceChannelAccountEntryRel[] findByA_T_PrevAndNext(
			long commerceChannelAccountEntryRelId, long accountEntryId,
			int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Removes all the commerce channel account entry rels where accountEntryId = &#63; and type = &#63; from the database.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 */
	public void removeByA_T(long accountEntryId, int type);

	/**
	 * Returns the number of commerce channel account entry rels where accountEntryId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param type the type
	 * @return the number of matching commerce channel account entry rels
	 */
	public int countByA_T(long accountEntryId, int type);

	/**
	 * Returns all the commerce channel account entry rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the matching commerce channel account entry rels
	 */
	public java.util.List<CommerceChannelAccountEntryRel> findByC_C(
		long classNameId, long classPK);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByC_C(
		long classNameId, long classPK, int start, int end);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByC_C(
		long classNameId, long classPK, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByC_C(
		long classNameId, long classPK, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel findByC_C_First(
			long classNameId, long classPK,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByC_C_First(
		long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel findByC_C_Last(
			long classNameId, long classPK,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByC_C_Last(
		long classNameId, long classPK,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

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
	public CommerceChannelAccountEntryRel[] findByC_C_PrevAndNext(
			long commerceChannelAccountEntryRelId, long classNameId,
			long classPK,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Removes all the commerce channel account entry rels where classNameId = &#63; and classPK = &#63; from the database.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 */
	public void removeByC_C(long classNameId, long classPK);

	/**
	 * Returns the number of commerce channel account entry rels where classNameId = &#63; and classPK = &#63;.
	 *
	 * @param classNameId the class name ID
	 * @param classPK the class pk
	 * @return the number of matching commerce channel account entry rels
	 */
	public int countByC_C(long classNameId, long classPK);

	/**
	 * Returns all the commerce channel account entry rels where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @return the matching commerce channel account entry rels
	 */
	public java.util.List<CommerceChannelAccountEntryRel> findByC_T(
		long commerceChannelId, int type);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByC_T(
		long commerceChannelId, int type, int start, int end);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByC_T(
		long commerceChannelId, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByC_T(
		long commerceChannelId, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel findByC_T_First(
			long commerceChannelId, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByC_T_First(
		long commerceChannelId, int type,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel findByC_T_Last(
			long commerceChannelId, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByC_T_Last(
		long commerceChannelId, int type,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

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
	public CommerceChannelAccountEntryRel[] findByC_T_PrevAndNext(
			long commerceChannelAccountEntryRelId, long commerceChannelId,
			int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Removes all the commerce channel account entry rels where commerceChannelId = &#63; and type = &#63; from the database.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 */
	public void removeByC_T(long commerceChannelId, int type);

	/**
	 * Returns the number of commerce channel account entry rels where commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @return the number of matching commerce channel account entry rels
	 */
	public int countByC_T(long commerceChannelId, int type);

	/**
	 * Returns all the commerce channel account entry rels where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @return the matching commerce channel account entry rels
	 */
	public java.util.List<CommerceChannelAccountEntryRel> findByA_C_T(
		long accountEntryId, long commerceChannelId, int type);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByA_C_T(
		long accountEntryId, long commerceChannelId, int type, int start,
		int end);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByA_C_T(
		long accountEntryId, long commerceChannelId, int type, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

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
	public java.util.List<CommerceChannelAccountEntryRel> findByA_C_T(
		long accountEntryId, long commerceChannelId, int type, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache);

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
	public CommerceChannelAccountEntryRel findByA_C_T_First(
			long accountEntryId, long commerceChannelId, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the first commerce channel account entry rel in the ordered set where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByA_C_T_First(
		long accountEntryId, long commerceChannelId, int type,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

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
	public CommerceChannelAccountEntryRel findByA_C_T_Last(
			long accountEntryId, long commerceChannelId, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the last commerce channel account entry rel in the ordered set where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching commerce channel account entry rel, or <code>null</code> if a matching commerce channel account entry rel could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByA_C_T_Last(
		long accountEntryId, long commerceChannelId, int type,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

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
	public CommerceChannelAccountEntryRel[] findByA_C_T_PrevAndNext(
			long commerceChannelAccountEntryRelId, long accountEntryId,
			long commerceChannelId, int type,
			com.liferay.portal.kernel.util.OrderByComparator
				<CommerceChannelAccountEntryRel> orderByComparator)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Removes all the commerce channel account entry rels where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63; from the database.
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 */
	public void removeByA_C_T(
		long accountEntryId, long commerceChannelId, int type);

	/**
	 * Returns the number of commerce channel account entry rels where accountEntryId = &#63; and commerceChannelId = &#63; and type = &#63;.
	 *
	 * @param accountEntryId the account entry ID
	 * @param commerceChannelId the commerce channel ID
	 * @param type the type
	 * @return the number of matching commerce channel account entry rels
	 */
	public int countByA_C_T(
		long accountEntryId, long commerceChannelId, int type);

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
	public CommerceChannelAccountEntryRel findByA_C_C_C_T(
			long accountEntryId, long classNameId, long classPK,
			long commerceChannelId, int type)
		throws NoSuchChannelAccountEntryRelException;

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
	public CommerceChannelAccountEntryRel fetchByA_C_C_C_T(
		long accountEntryId, long classNameId, long classPK,
		long commerceChannelId, int type);

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
	public CommerceChannelAccountEntryRel fetchByA_C_C_C_T(
		long accountEntryId, long classNameId, long classPK,
		long commerceChannelId, int type, boolean useFinderCache);

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
	public CommerceChannelAccountEntryRel removeByA_C_C_C_T(
			long accountEntryId, long classNameId, long classPK,
			long commerceChannelId, int type)
		throws NoSuchChannelAccountEntryRelException;

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
	public int countByA_C_C_C_T(
		long accountEntryId, long classNameId, long classPK,
		long commerceChannelId, int type);

	/**
	 * Caches the commerce channel account entry rel in the entity cache if it is enabled.
	 *
	 * @param commerceChannelAccountEntryRel the commerce channel account entry rel
	 */
	public void cacheResult(
		CommerceChannelAccountEntryRel commerceChannelAccountEntryRel);

	/**
	 * Caches the commerce channel account entry rels in the entity cache if it is enabled.
	 *
	 * @param commerceChannelAccountEntryRels the commerce channel account entry rels
	 */
	public void cacheResult(
		java.util.List<CommerceChannelAccountEntryRel>
			commerceChannelAccountEntryRels);

	/**
	 * Creates a new commerce channel account entry rel with the primary key. Does not add the commerce channel account entry rel to the database.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key for the new commerce channel account entry rel
	 * @return the new commerce channel account entry rel
	 */
	public CommerceChannelAccountEntryRel create(
		long commerceChannelAccountEntryRelId);

	/**
	 * Removes the commerce channel account entry rel with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the commerce channel account entry rel
	 * @return the commerce channel account entry rel that was removed
	 * @throws NoSuchChannelAccountEntryRelException if a commerce channel account entry rel with the primary key could not be found
	 */
	public CommerceChannelAccountEntryRel remove(
			long commerceChannelAccountEntryRelId)
		throws NoSuchChannelAccountEntryRelException;

	public CommerceChannelAccountEntryRel updateImpl(
		CommerceChannelAccountEntryRel commerceChannelAccountEntryRel);

	/**
	 * Returns the commerce channel account entry rel with the primary key or throws a <code>NoSuchChannelAccountEntryRelException</code> if it could not be found.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the commerce channel account entry rel
	 * @return the commerce channel account entry rel
	 * @throws NoSuchChannelAccountEntryRelException if a commerce channel account entry rel with the primary key could not be found
	 */
	public CommerceChannelAccountEntryRel findByPrimaryKey(
			long commerceChannelAccountEntryRelId)
		throws NoSuchChannelAccountEntryRelException;

	/**
	 * Returns the commerce channel account entry rel with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param commerceChannelAccountEntryRelId the primary key of the commerce channel account entry rel
	 * @return the commerce channel account entry rel, or <code>null</code> if a commerce channel account entry rel with the primary key could not be found
	 */
	public CommerceChannelAccountEntryRel fetchByPrimaryKey(
		long commerceChannelAccountEntryRelId);

	/**
	 * Returns all the commerce channel account entry rels.
	 *
	 * @return the commerce channel account entry rels
	 */
	public java.util.List<CommerceChannelAccountEntryRel> findAll();

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
	public java.util.List<CommerceChannelAccountEntryRel> findAll(
		int start, int end);

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
	public java.util.List<CommerceChannelAccountEntryRel> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator);

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
	public java.util.List<CommerceChannelAccountEntryRel> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator
			<CommerceChannelAccountEntryRel> orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the commerce channel account entry rels from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of commerce channel account entry rels.
	 *
	 * @return the number of commerce channel account entry rels
	 */
	public int countAll();

}