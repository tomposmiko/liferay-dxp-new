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

package com.liferay.oauth2.provider.service.persistence;

import com.liferay.oauth2.provider.exception.NoSuchOAuth2ApplicationException;
import com.liferay.oauth2.provider.model.OAuth2Application;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the o auth2 application service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2ApplicationUtil
 * @generated
 */
@ProviderType
public interface OAuth2ApplicationPersistence
	extends BasePersistence<OAuth2Application> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link OAuth2ApplicationUtil} to access the o auth2 application persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns all the o auth2 applications where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByUuid(String uuid);

	/**
	 * Returns a range of all the o auth2 applications where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @return the range of matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByUuid(
		String uuid, int start, int end);

	/**
	 * Returns an ordered range of all the o auth2 applications where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns an ordered range of all the o auth2 applications where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first o auth2 application in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a matching o auth2 application could not be found
	 */
	public OAuth2Application findByUuid_First(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the first o auth2 application in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching o auth2 application, or <code>null</code> if a matching o auth2 application could not be found
	 */
	public OAuth2Application fetchByUuid_First(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns the last o auth2 application in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a matching o auth2 application could not be found
	 */
	public OAuth2Application findByUuid_Last(
			String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the last o auth2 application in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching o auth2 application, or <code>null</code> if a matching o auth2 application could not be found
	 */
	public OAuth2Application fetchByUuid_Last(
		String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns the o auth2 applications before and after the current o auth2 application in the ordered set where uuid = &#63;.
	 *
	 * @param oAuth2ApplicationId the primary key of the current o auth2 application
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a o auth2 application with the primary key could not be found
	 */
	public OAuth2Application[] findByUuid_PrevAndNext(
			long oAuth2ApplicationId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns all the o auth2 applications that the user has permission to view where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching o auth2 applications that the user has permission to view
	 */
	public java.util.List<OAuth2Application> filterFindByUuid(String uuid);

	/**
	 * Returns a range of all the o auth2 applications that the user has permission to view where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @return the range of matching o auth2 applications that the user has permission to view
	 */
	public java.util.List<OAuth2Application> filterFindByUuid(
		String uuid, int start, int end);

	/**
	 * Returns an ordered range of all the o auth2 applications that the user has permissions to view where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching o auth2 applications that the user has permission to view
	 */
	public java.util.List<OAuth2Application> filterFindByUuid(
		String uuid, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns the o auth2 applications before and after the current o auth2 application in the ordered set of o auth2 applications that the user has permission to view where uuid = &#63;.
	 *
	 * @param oAuth2ApplicationId the primary key of the current o auth2 application
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a o auth2 application with the primary key could not be found
	 */
	public OAuth2Application[] filterFindByUuid_PrevAndNext(
			long oAuth2ApplicationId, String uuid,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Removes all the o auth2 applications where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	public void removeByUuid(String uuid);

	/**
	 * Returns the number of o auth2 applications where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching o auth2 applications
	 */
	public int countByUuid(String uuid);

	/**
	 * Returns the number of o auth2 applications that the user has permission to view where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching o auth2 applications that the user has permission to view
	 */
	public int filterCountByUuid(String uuid);

	/**
	 * Returns all the o auth2 applications where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByUuid_C(
		String uuid, long companyId);

	/**
	 * Returns a range of all the o auth2 applications where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @return the range of matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByUuid_C(
		String uuid, long companyId, int start, int end);

	/**
	 * Returns an ordered range of all the o auth2 applications where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns an ordered range of all the o auth2 applications where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first o auth2 application in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a matching o auth2 application could not be found
	 */
	public OAuth2Application findByUuid_C_First(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the first o auth2 application in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching o auth2 application, or <code>null</code> if a matching o auth2 application could not be found
	 */
	public OAuth2Application fetchByUuid_C_First(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns the last o auth2 application in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a matching o auth2 application could not be found
	 */
	public OAuth2Application findByUuid_C_Last(
			String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the last o auth2 application in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching o auth2 application, or <code>null</code> if a matching o auth2 application could not be found
	 */
	public OAuth2Application fetchByUuid_C_Last(
		String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns the o auth2 applications before and after the current o auth2 application in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param oAuth2ApplicationId the primary key of the current o auth2 application
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a o auth2 application with the primary key could not be found
	 */
	public OAuth2Application[] findByUuid_C_PrevAndNext(
			long oAuth2ApplicationId, String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns all the o auth2 applications that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching o auth2 applications that the user has permission to view
	 */
	public java.util.List<OAuth2Application> filterFindByUuid_C(
		String uuid, long companyId);

	/**
	 * Returns a range of all the o auth2 applications that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @return the range of matching o auth2 applications that the user has permission to view
	 */
	public java.util.List<OAuth2Application> filterFindByUuid_C(
		String uuid, long companyId, int start, int end);

	/**
	 * Returns an ordered range of all the o auth2 applications that the user has permissions to view where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching o auth2 applications that the user has permission to view
	 */
	public java.util.List<OAuth2Application> filterFindByUuid_C(
		String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns the o auth2 applications before and after the current o auth2 application in the ordered set of o auth2 applications that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param oAuth2ApplicationId the primary key of the current o auth2 application
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a o auth2 application with the primary key could not be found
	 */
	public OAuth2Application[] filterFindByUuid_C_PrevAndNext(
			long oAuth2ApplicationId, String uuid, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Removes all the o auth2 applications where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	public void removeByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of o auth2 applications where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching o auth2 applications
	 */
	public int countByUuid_C(String uuid, long companyId);

	/**
	 * Returns the number of o auth2 applications that the user has permission to view where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching o auth2 applications that the user has permission to view
	 */
	public int filterCountByUuid_C(String uuid, long companyId);

	/**
	 * Returns all the o auth2 applications where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByCompanyId(long companyId);

	/**
	 * Returns a range of all the o auth2 applications where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @return the range of matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByCompanyId(
		long companyId, int start, int end);

	/**
	 * Returns an ordered range of all the o auth2 applications where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns an ordered range of all the o auth2 applications where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first o auth2 application in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a matching o auth2 application could not be found
	 */
	public OAuth2Application findByCompanyId_First(
			long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the first o auth2 application in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching o auth2 application, or <code>null</code> if a matching o auth2 application could not be found
	 */
	public OAuth2Application fetchByCompanyId_First(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns the last o auth2 application in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a matching o auth2 application could not be found
	 */
	public OAuth2Application findByCompanyId_Last(
			long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the last o auth2 application in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching o auth2 application, or <code>null</code> if a matching o auth2 application could not be found
	 */
	public OAuth2Application fetchByCompanyId_Last(
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns the o auth2 applications before and after the current o auth2 application in the ordered set where companyId = &#63;.
	 *
	 * @param oAuth2ApplicationId the primary key of the current o auth2 application
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a o auth2 application with the primary key could not be found
	 */
	public OAuth2Application[] findByCompanyId_PrevAndNext(
			long oAuth2ApplicationId, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns all the o auth2 applications that the user has permission to view where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching o auth2 applications that the user has permission to view
	 */
	public java.util.List<OAuth2Application> filterFindByCompanyId(
		long companyId);

	/**
	 * Returns a range of all the o auth2 applications that the user has permission to view where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @return the range of matching o auth2 applications that the user has permission to view
	 */
	public java.util.List<OAuth2Application> filterFindByCompanyId(
		long companyId, int start, int end);

	/**
	 * Returns an ordered range of all the o auth2 applications that the user has permissions to view where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching o auth2 applications that the user has permission to view
	 */
	public java.util.List<OAuth2Application> filterFindByCompanyId(
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns the o auth2 applications before and after the current o auth2 application in the ordered set of o auth2 applications that the user has permission to view where companyId = &#63;.
	 *
	 * @param oAuth2ApplicationId the primary key of the current o auth2 application
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a o auth2 application with the primary key could not be found
	 */
	public OAuth2Application[] filterFindByCompanyId_PrevAndNext(
			long oAuth2ApplicationId, long companyId,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Removes all the o auth2 applications where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	public void removeByCompanyId(long companyId);

	/**
	 * Returns the number of o auth2 applications where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching o auth2 applications
	 */
	public int countByCompanyId(long companyId);

	/**
	 * Returns the number of o auth2 applications that the user has permission to view where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching o auth2 applications that the user has permission to view
	 */
	public int filterCountByCompanyId(long companyId);

	/**
	 * Returns the o auth2 application where companyId = &#63; and clientId = &#63; or throws a <code>NoSuchOAuth2ApplicationException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param clientId the client ID
	 * @return the matching o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a matching o auth2 application could not be found
	 */
	public OAuth2Application findByC_C(long companyId, String clientId)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the o auth2 application where companyId = &#63; and clientId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param clientId the client ID
	 * @return the matching o auth2 application, or <code>null</code> if a matching o auth2 application could not be found
	 */
	public OAuth2Application fetchByC_C(long companyId, String clientId);

	/**
	 * Returns the o auth2 application where companyId = &#63; and clientId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param clientId the client ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching o auth2 application, or <code>null</code> if a matching o auth2 application could not be found
	 */
	public OAuth2Application fetchByC_C(
		long companyId, String clientId, boolean useFinderCache);

	/**
	 * Removes the o auth2 application where companyId = &#63; and clientId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param clientId the client ID
	 * @return the o auth2 application that was removed
	 */
	public OAuth2Application removeByC_C(long companyId, String clientId)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the number of o auth2 applications where companyId = &#63; and clientId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param clientId the client ID
	 * @return the number of matching o auth2 applications
	 */
	public int countByC_C(long companyId, String clientId);

	/**
	 * Returns all the o auth2 applications where companyId = &#63; and clientProfile = &#63;.
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @return the matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByC_CP(
		long companyId, int clientProfile);

	/**
	 * Returns a range of all the o auth2 applications where companyId = &#63; and clientProfile = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @return the range of matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByC_CP(
		long companyId, int clientProfile, int start, int end);

	/**
	 * Returns an ordered range of all the o auth2 applications where companyId = &#63; and clientProfile = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByC_CP(
		long companyId, int clientProfile, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns an ordered range of all the o auth2 applications where companyId = &#63; and clientProfile = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching o auth2 applications
	 */
	public java.util.List<OAuth2Application> findByC_CP(
		long companyId, int clientProfile, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Returns the first o auth2 application in the ordered set where companyId = &#63; and clientProfile = &#63;.
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a matching o auth2 application could not be found
	 */
	public OAuth2Application findByC_CP_First(
			long companyId, int clientProfile,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the first o auth2 application in the ordered set where companyId = &#63; and clientProfile = &#63;.
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching o auth2 application, or <code>null</code> if a matching o auth2 application could not be found
	 */
	public OAuth2Application fetchByC_CP_First(
		long companyId, int clientProfile,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns the last o auth2 application in the ordered set where companyId = &#63; and clientProfile = &#63;.
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a matching o auth2 application could not be found
	 */
	public OAuth2Application findByC_CP_Last(
			long companyId, int clientProfile,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the last o auth2 application in the ordered set where companyId = &#63; and clientProfile = &#63;.
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching o auth2 application, or <code>null</code> if a matching o auth2 application could not be found
	 */
	public OAuth2Application fetchByC_CP_Last(
		long companyId, int clientProfile,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns the o auth2 applications before and after the current o auth2 application in the ordered set where companyId = &#63; and clientProfile = &#63;.
	 *
	 * @param oAuth2ApplicationId the primary key of the current o auth2 application
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a o auth2 application with the primary key could not be found
	 */
	public OAuth2Application[] findByC_CP_PrevAndNext(
			long oAuth2ApplicationId, long companyId, int clientProfile,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns all the o auth2 applications that the user has permission to view where companyId = &#63; and clientProfile = &#63;.
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @return the matching o auth2 applications that the user has permission to view
	 */
	public java.util.List<OAuth2Application> filterFindByC_CP(
		long companyId, int clientProfile);

	/**
	 * Returns a range of all the o auth2 applications that the user has permission to view where companyId = &#63; and clientProfile = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @return the range of matching o auth2 applications that the user has permission to view
	 */
	public java.util.List<OAuth2Application> filterFindByC_CP(
		long companyId, int clientProfile, int start, int end);

	/**
	 * Returns an ordered range of all the o auth2 applications that the user has permissions to view where companyId = &#63; and clientProfile = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching o auth2 applications that the user has permission to view
	 */
	public java.util.List<OAuth2Application> filterFindByC_CP(
		long companyId, int clientProfile, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns the o auth2 applications before and after the current o auth2 application in the ordered set of o auth2 applications that the user has permission to view where companyId = &#63; and clientProfile = &#63;.
	 *
	 * @param oAuth2ApplicationId the primary key of the current o auth2 application
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a o auth2 application with the primary key could not be found
	 */
	public OAuth2Application[] filterFindByC_CP_PrevAndNext(
			long oAuth2ApplicationId, long companyId, int clientProfile,
			com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
				orderByComparator)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Removes all the o auth2 applications where companyId = &#63; and clientProfile = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 */
	public void removeByC_CP(long companyId, int clientProfile);

	/**
	 * Returns the number of o auth2 applications where companyId = &#63; and clientProfile = &#63;.
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @return the number of matching o auth2 applications
	 */
	public int countByC_CP(long companyId, int clientProfile);

	/**
	 * Returns the number of o auth2 applications that the user has permission to view where companyId = &#63; and clientProfile = &#63;.
	 *
	 * @param companyId the company ID
	 * @param clientProfile the client profile
	 * @return the number of matching o auth2 applications that the user has permission to view
	 */
	public int filterCountByC_CP(long companyId, int clientProfile);

	/**
	 * Returns the o auth2 application where companyId = &#63; and externalReferenceCode = &#63; or throws a <code>NoSuchOAuth2ApplicationException</code> if it could not be found.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a matching o auth2 application could not be found
	 */
	public OAuth2Application findByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the o auth2 application where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the matching o auth2 application, or <code>null</code> if a matching o auth2 application could not be found
	 */
	public OAuth2Application fetchByC_ERC(
		long companyId, String externalReferenceCode);

	/**
	 * Returns the o auth2 application where companyId = &#63; and externalReferenceCode = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching o auth2 application, or <code>null</code> if a matching o auth2 application could not be found
	 */
	public OAuth2Application fetchByC_ERC(
		long companyId, String externalReferenceCode, boolean useFinderCache);

	/**
	 * Removes the o auth2 application where companyId = &#63; and externalReferenceCode = &#63; from the database.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the o auth2 application that was removed
	 */
	public OAuth2Application removeByC_ERC(
			long companyId, String externalReferenceCode)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the number of o auth2 applications where companyId = &#63; and externalReferenceCode = &#63;.
	 *
	 * @param companyId the company ID
	 * @param externalReferenceCode the external reference code
	 * @return the number of matching o auth2 applications
	 */
	public int countByC_ERC(long companyId, String externalReferenceCode);

	/**
	 * Caches the o auth2 application in the entity cache if it is enabled.
	 *
	 * @param oAuth2Application the o auth2 application
	 */
	public void cacheResult(OAuth2Application oAuth2Application);

	/**
	 * Caches the o auth2 applications in the entity cache if it is enabled.
	 *
	 * @param oAuth2Applications the o auth2 applications
	 */
	public void cacheResult(
		java.util.List<OAuth2Application> oAuth2Applications);

	/**
	 * Creates a new o auth2 application with the primary key. Does not add the o auth2 application to the database.
	 *
	 * @param oAuth2ApplicationId the primary key for the new o auth2 application
	 * @return the new o auth2 application
	 */
	public OAuth2Application create(long oAuth2ApplicationId);

	/**
	 * Removes the o auth2 application with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param oAuth2ApplicationId the primary key of the o auth2 application
	 * @return the o auth2 application that was removed
	 * @throws NoSuchOAuth2ApplicationException if a o auth2 application with the primary key could not be found
	 */
	public OAuth2Application remove(long oAuth2ApplicationId)
		throws NoSuchOAuth2ApplicationException;

	public OAuth2Application updateImpl(OAuth2Application oAuth2Application);

	/**
	 * Returns the o auth2 application with the primary key or throws a <code>NoSuchOAuth2ApplicationException</code> if it could not be found.
	 *
	 * @param oAuth2ApplicationId the primary key of the o auth2 application
	 * @return the o auth2 application
	 * @throws NoSuchOAuth2ApplicationException if a o auth2 application with the primary key could not be found
	 */
	public OAuth2Application findByPrimaryKey(long oAuth2ApplicationId)
		throws NoSuchOAuth2ApplicationException;

	/**
	 * Returns the o auth2 application with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param oAuth2ApplicationId the primary key of the o auth2 application
	 * @return the o auth2 application, or <code>null</code> if a o auth2 application with the primary key could not be found
	 */
	public OAuth2Application fetchByPrimaryKey(long oAuth2ApplicationId);

	/**
	 * Returns all the o auth2 applications.
	 *
	 * @return the o auth2 applications
	 */
	public java.util.List<OAuth2Application> findAll();

	/**
	 * Returns a range of all the o auth2 applications.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @return the range of o auth2 applications
	 */
	public java.util.List<OAuth2Application> findAll(int start, int end);

	/**
	 * Returns an ordered range of all the o auth2 applications.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of o auth2 applications
	 */
	public java.util.List<OAuth2Application> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator);

	/**
	 * Returns an ordered range of all the o auth2 applications.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>OAuth2ApplicationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of o auth2 applications
	 * @param end the upper bound of the range of o auth2 applications (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of o auth2 applications
	 */
	public java.util.List<OAuth2Application> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<OAuth2Application>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the o auth2 applications from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of o auth2 applications.
	 *
	 * @return the number of o auth2 applications
	 */
	public int countAll();

}