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

package com.liferay.analytics.message.storage.service.persistence;

import com.liferay.analytics.message.storage.exception.NoSuchAssociationException;
import com.liferay.analytics.message.storage.model.AnalyticsAssociation;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import java.util.Date;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the analytics association service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AnalyticsAssociationUtil
 * @generated
 */
@ProviderType
public interface AnalyticsAssociationPersistence
	extends BasePersistence<AnalyticsAssociation> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link AnalyticsAssociationUtil} to access the analytics association persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the analytics associations where companyId = &#63; and associationClassName = &#63;.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @return the matching analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findByC_A(
		long companyId, String associationClassName);

	/**
	 * Returns a range of all the analytics associations where companyId = &#63; and associationClassName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsAssociationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param start the lower bound of the range of analytics associations
	 * @param end the upper bound of the range of analytics associations (not inclusive)
	 * @return the range of matching analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findByC_A(
		long companyId, String associationClassName, int start, int end);

	/**
	 * Returns an ordered range of all the analytics associations where companyId = &#63; and associationClassName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsAssociationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param start the lower bound of the range of analytics associations
	 * @param end the upper bound of the range of analytics associations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findByC_A(
		long companyId, String associationClassName, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator);

	/**
	 * Returns an ordered range of all the analytics associations where companyId = &#63; and associationClassName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsAssociationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param start the lower bound of the range of analytics associations
	 * @param end the upper bound of the range of analytics associations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findByC_A(
		long companyId, String associationClassName, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first analytics association in the ordered set where companyId = &#63; and associationClassName = &#63;.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics association
	 * @throws NoSuchAssociationException if a matching analytics association could not be found
	 */
	public AnalyticsAssociation findByC_A_First(
			long companyId, String associationClassName,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsAssociation> orderByComparator)
		throws NoSuchAssociationException;

	/**
	 * Returns the first analytics association in the ordered set where companyId = &#63; and associationClassName = &#63;.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics association, or <code>null</code> if a matching analytics association could not be found
	 */
	public AnalyticsAssociation fetchByC_A_First(
		long companyId, String associationClassName,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator);

	/**
	 * Returns the last analytics association in the ordered set where companyId = &#63; and associationClassName = &#63;.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics association
	 * @throws NoSuchAssociationException if a matching analytics association could not be found
	 */
	public AnalyticsAssociation findByC_A_Last(
			long companyId, String associationClassName,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsAssociation> orderByComparator)
		throws NoSuchAssociationException;

	/**
	 * Returns the last analytics association in the ordered set where companyId = &#63; and associationClassName = &#63;.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics association, or <code>null</code> if a matching analytics association could not be found
	 */
	public AnalyticsAssociation fetchByC_A_Last(
		long companyId, String associationClassName,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator);

	/**
	 * Returns the analytics associations before and after the current analytics association in the ordered set where companyId = &#63; and associationClassName = &#63;.
	 *
	 * @param analyticsAssociationId the primary key of the current analytics association
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics association
	 * @throws NoSuchAssociationException if a analytics association with the primary key could not be found
	 */
	public AnalyticsAssociation[] findByC_A_PrevAndNext(
			long analyticsAssociationId, long companyId,
			String associationClassName,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsAssociation> orderByComparator)
		throws NoSuchAssociationException;

	/**
	 * Removes all the analytics associations where companyId = &#63; and associationClassName = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 */
	public void removeByC_A(long companyId, String associationClassName);

	/**
	 * Returns the number of analytics associations where companyId = &#63; and associationClassName = &#63;.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @return the number of matching analytics associations
	 */
	public int countByC_A(long companyId, String associationClassName);

	/**
	 * Returns all the analytics associations where companyId = &#63; and modifiedDate &gt; &#63; and associationClassName = &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param associationClassName the association class name
	 * @return the matching analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findByC_GtM_A(
		long companyId, Date modifiedDate, String associationClassName);

	/**
	 * Returns a range of all the analytics associations where companyId = &#63; and modifiedDate &gt; &#63; and associationClassName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsAssociationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param associationClassName the association class name
	 * @param start the lower bound of the range of analytics associations
	 * @param end the upper bound of the range of analytics associations (not inclusive)
	 * @return the range of matching analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findByC_GtM_A(
		long companyId, Date modifiedDate, String associationClassName,
		int start, int end);

	/**
	 * Returns an ordered range of all the analytics associations where companyId = &#63; and modifiedDate &gt; &#63; and associationClassName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsAssociationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param associationClassName the association class name
	 * @param start the lower bound of the range of analytics associations
	 * @param end the upper bound of the range of analytics associations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findByC_GtM_A(
		long companyId, Date modifiedDate, String associationClassName,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator);

	/**
	 * Returns an ordered range of all the analytics associations where companyId = &#63; and modifiedDate &gt; &#63; and associationClassName = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsAssociationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param associationClassName the association class name
	 * @param start the lower bound of the range of analytics associations
	 * @param end the upper bound of the range of analytics associations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findByC_GtM_A(
		long companyId, Date modifiedDate, String associationClassName,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first analytics association in the ordered set where companyId = &#63; and modifiedDate &gt; &#63; and associationClassName = &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param associationClassName the association class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics association
	 * @throws NoSuchAssociationException if a matching analytics association could not be found
	 */
	public AnalyticsAssociation findByC_GtM_A_First(
			long companyId, Date modifiedDate, String associationClassName,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsAssociation> orderByComparator)
		throws NoSuchAssociationException;

	/**
	 * Returns the first analytics association in the ordered set where companyId = &#63; and modifiedDate &gt; &#63; and associationClassName = &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param associationClassName the association class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics association, or <code>null</code> if a matching analytics association could not be found
	 */
	public AnalyticsAssociation fetchByC_GtM_A_First(
		long companyId, Date modifiedDate, String associationClassName,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator);

	/**
	 * Returns the last analytics association in the ordered set where companyId = &#63; and modifiedDate &gt; &#63; and associationClassName = &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param associationClassName the association class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics association
	 * @throws NoSuchAssociationException if a matching analytics association could not be found
	 */
	public AnalyticsAssociation findByC_GtM_A_Last(
			long companyId, Date modifiedDate, String associationClassName,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsAssociation> orderByComparator)
		throws NoSuchAssociationException;

	/**
	 * Returns the last analytics association in the ordered set where companyId = &#63; and modifiedDate &gt; &#63; and associationClassName = &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param associationClassName the association class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics association, or <code>null</code> if a matching analytics association could not be found
	 */
	public AnalyticsAssociation fetchByC_GtM_A_Last(
		long companyId, Date modifiedDate, String associationClassName,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator);

	/**
	 * Returns the analytics associations before and after the current analytics association in the ordered set where companyId = &#63; and modifiedDate &gt; &#63; and associationClassName = &#63;.
	 *
	 * @param analyticsAssociationId the primary key of the current analytics association
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param associationClassName the association class name
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics association
	 * @throws NoSuchAssociationException if a analytics association with the primary key could not be found
	 */
	public AnalyticsAssociation[] findByC_GtM_A_PrevAndNext(
			long analyticsAssociationId, long companyId, Date modifiedDate,
			String associationClassName,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsAssociation> orderByComparator)
		throws NoSuchAssociationException;

	/**
	 * Removes all the analytics associations where companyId = &#63; and modifiedDate &gt; &#63; and associationClassName = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param associationClassName the association class name
	 */
	public void removeByC_GtM_A(
		long companyId, Date modifiedDate, String associationClassName);

	/**
	 * Returns the number of analytics associations where companyId = &#63; and modifiedDate &gt; &#63; and associationClassName = &#63;.
	 *
	 * @param companyId the company ID
	 * @param modifiedDate the modified date
	 * @param associationClassName the association class name
	 * @return the number of matching analytics associations
	 */
	public int countByC_GtM_A(
		long companyId, Date modifiedDate, String associationClassName);

	/**
	 * Returns all the analytics associations where companyId = &#63; and associationClassName = &#63; and associationClassPK = &#63;.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param associationClassPK the association class pk
	 * @return the matching analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findByC_A_A(
		long companyId, String associationClassName, long associationClassPK);

	/**
	 * Returns a range of all the analytics associations where companyId = &#63; and associationClassName = &#63; and associationClassPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsAssociationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param associationClassPK the association class pk
	 * @param start the lower bound of the range of analytics associations
	 * @param end the upper bound of the range of analytics associations (not inclusive)
	 * @return the range of matching analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findByC_A_A(
		long companyId, String associationClassName, long associationClassPK,
		int start, int end);

	/**
	 * Returns an ordered range of all the analytics associations where companyId = &#63; and associationClassName = &#63; and associationClassPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsAssociationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param associationClassPK the association class pk
	 * @param start the lower bound of the range of analytics associations
	 * @param end the upper bound of the range of analytics associations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findByC_A_A(
		long companyId, String associationClassName, long associationClassPK,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator);

	/**
	 * Returns an ordered range of all the analytics associations where companyId = &#63; and associationClassName = &#63; and associationClassPK = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsAssociationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param associationClassPK the association class pk
	 * @param start the lower bound of the range of analytics associations
	 * @param end the upper bound of the range of analytics associations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findByC_A_A(
		long companyId, String associationClassName, long associationClassPK,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first analytics association in the ordered set where companyId = &#63; and associationClassName = &#63; and associationClassPK = &#63;.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param associationClassPK the association class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics association
	 * @throws NoSuchAssociationException if a matching analytics association could not be found
	 */
	public AnalyticsAssociation findByC_A_A_First(
			long companyId, String associationClassName,
			long associationClassPK,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsAssociation> orderByComparator)
		throws NoSuchAssociationException;

	/**
	 * Returns the first analytics association in the ordered set where companyId = &#63; and associationClassName = &#63; and associationClassPK = &#63;.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param associationClassPK the association class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching analytics association, or <code>null</code> if a matching analytics association could not be found
	 */
	public AnalyticsAssociation fetchByC_A_A_First(
		long companyId, String associationClassName, long associationClassPK,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator);

	/**
	 * Returns the last analytics association in the ordered set where companyId = &#63; and associationClassName = &#63; and associationClassPK = &#63;.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param associationClassPK the association class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics association
	 * @throws NoSuchAssociationException if a matching analytics association could not be found
	 */
	public AnalyticsAssociation findByC_A_A_Last(
			long companyId, String associationClassName,
			long associationClassPK,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsAssociation> orderByComparator)
		throws NoSuchAssociationException;

	/**
	 * Returns the last analytics association in the ordered set where companyId = &#63; and associationClassName = &#63; and associationClassPK = &#63;.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param associationClassPK the association class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching analytics association, or <code>null</code> if a matching analytics association could not be found
	 */
	public AnalyticsAssociation fetchByC_A_A_Last(
		long companyId, String associationClassName, long associationClassPK,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator);

	/**
	 * Returns the analytics associations before and after the current analytics association in the ordered set where companyId = &#63; and associationClassName = &#63; and associationClassPK = &#63;.
	 *
	 * @param analyticsAssociationId the primary key of the current analytics association
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param associationClassPK the association class pk
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next analytics association
	 * @throws NoSuchAssociationException if a analytics association with the primary key could not be found
	 */
	public AnalyticsAssociation[] findByC_A_A_PrevAndNext(
			long analyticsAssociationId, long companyId,
			String associationClassName, long associationClassPK,
			com.liferay.portal.kernel.util.OrderByComparator
				<AnalyticsAssociation> orderByComparator)
		throws NoSuchAssociationException;

	/**
	 * Removes all the analytics associations where companyId = &#63; and associationClassName = &#63; and associationClassPK = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param associationClassPK the association class pk
	 */
	public void removeByC_A_A(
		long companyId, String associationClassName, long associationClassPK);

	/**
	 * Returns the number of analytics associations where companyId = &#63; and associationClassName = &#63; and associationClassPK = &#63;.
	 *
	 * @param companyId the company ID
	 * @param associationClassName the association class name
	 * @param associationClassPK the association class pk
	 * @return the number of matching analytics associations
	 */
	public int countByC_A_A(
		long companyId, String associationClassName, long associationClassPK);

	/**
	 * Caches the analytics association in the entity cache if it is enabled.
	 *
	 * @param analyticsAssociation the analytics association
	 */
	public void cacheResult(AnalyticsAssociation analyticsAssociation);

	/**
	 * Caches the analytics associations in the entity cache if it is enabled.
	 *
	 * @param analyticsAssociations the analytics associations
	 */
	public void cacheResult(
		java.util.List<AnalyticsAssociation> analyticsAssociations);

	/**
	 * Creates a new analytics association with the primary key. Does not add the analytics association to the database.
	 *
	 * @param analyticsAssociationId the primary key for the new analytics association
	 * @return the new analytics association
	 */
	public AnalyticsAssociation create(long analyticsAssociationId);

	/**
	 * Removes the analytics association with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param analyticsAssociationId the primary key of the analytics association
	 * @return the analytics association that was removed
	 * @throws NoSuchAssociationException if a analytics association with the primary key could not be found
	 */
	public AnalyticsAssociation remove(long analyticsAssociationId)
		throws NoSuchAssociationException;

	public AnalyticsAssociation updateImpl(
		AnalyticsAssociation analyticsAssociation);

	/**
	 * Returns the analytics association with the primary key or throws a <code>NoSuchAssociationException</code> if it could not be found.
	 *
	 * @param analyticsAssociationId the primary key of the analytics association
	 * @return the analytics association
	 * @throws NoSuchAssociationException if a analytics association with the primary key could not be found
	 */
	public AnalyticsAssociation findByPrimaryKey(long analyticsAssociationId)
		throws NoSuchAssociationException;

	/**
	 * Returns the analytics association with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param analyticsAssociationId the primary key of the analytics association
	 * @return the analytics association, or <code>null</code> if a analytics association with the primary key could not be found
	 */
	public AnalyticsAssociation fetchByPrimaryKey(long analyticsAssociationId);

	/**
	 * Returns all the analytics associations.
	 *
	 * @return the analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findAll();

	/**
	 * Returns a range of all the analytics associations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsAssociationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of analytics associations
	 * @param end the upper bound of the range of analytics associations (not inclusive)
	 * @return the range of analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findAll(int start, int end);

	/**
	 * Returns an ordered range of all the analytics associations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsAssociationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of analytics associations
	 * @param end the upper bound of the range of analytics associations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator);

	/**
	 * Returns an ordered range of all the analytics associations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>AnalyticsAssociationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of analytics associations
	 * @param end the upper bound of the range of analytics associations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of analytics associations
	 */
	public java.util.List<AnalyticsAssociation> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<AnalyticsAssociation>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the analytics associations from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of analytics associations.
	 *
	 * @return the number of analytics associations
	 */
	public int countAll();

}