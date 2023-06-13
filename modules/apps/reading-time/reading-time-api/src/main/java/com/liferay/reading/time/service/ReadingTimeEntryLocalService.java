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

package com.liferay.reading.time.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.exportimport.kernel.lar.PortletDataContext;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;

import com.liferay.reading.time.model.ReadingTimeEntry;

import java.io.Serializable;

import java.time.Duration;

import java.util.List;

/**
 * Provides the local service interface for ReadingTimeEntry. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see ReadingTimeEntryLocalServiceUtil
 * @see com.liferay.reading.time.service.base.ReadingTimeEntryLocalServiceBaseImpl
 * @see com.liferay.reading.time.service.impl.ReadingTimeEntryLocalServiceImpl
 * @generated
 */
@ProviderType
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
	PortalException.class, SystemException.class})
public interface ReadingTimeEntryLocalService extends BaseLocalService,
	PersistedModelLocalService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link ReadingTimeEntryLocalServiceUtil} to access the reading time entry local service. Add custom service methods to {@link com.liferay.reading.time.service.impl.ReadingTimeEntryLocalServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public ReadingTimeEntry addReadingTimeEntry(GroupedModel groupedModel,
		Duration readingTimeDuration);

	public ReadingTimeEntry addReadingTimeEntry(long groupId, long classNameId,
		long classPK, Duration readingTimeDuration);

	/**
	* Adds the reading time entry to the database. Also notifies the appropriate model listeners.
	*
	* @param readingTimeEntry the reading time entry
	* @return the reading time entry that was added
	*/
	@Indexable(type = IndexableType.REINDEX)
	public ReadingTimeEntry addReadingTimeEntry(
		ReadingTimeEntry readingTimeEntry);

	/**
	* Creates a new reading time entry with the primary key. Does not add the reading time entry to the database.
	*
	* @param readingTimeEntryId the primary key for the new reading time entry
	* @return the new reading time entry
	*/
	@Transactional(enabled = false)
	public ReadingTimeEntry createReadingTimeEntry(long readingTimeEntryId);

	/**
	* @throws PortalException
	*/
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException;

	public ReadingTimeEntry deleteReadingTimeEntry(GroupedModel groupedModel);

	/**
	* Deletes the reading time entry with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param readingTimeEntryId the primary key of the reading time entry
	* @return the reading time entry that was removed
	* @throws PortalException if a reading time entry with the primary key could not be found
	*/
	@Indexable(type = IndexableType.DELETE)
	public ReadingTimeEntry deleteReadingTimeEntry(long readingTimeEntryId)
		throws PortalException;

	public ReadingTimeEntry deleteReadingTimeEntry(long groupId,
		long classNameId, long classPK);

	/**
	* Deletes the reading time entry from the database. Also notifies the appropriate model listeners.
	*
	* @param readingTimeEntry the reading time entry
	* @return the reading time entry that was removed
	*/
	@Indexable(type = IndexableType.DELETE)
	public ReadingTimeEntry deleteReadingTimeEntry(
		ReadingTimeEntry readingTimeEntry);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public DynamicQuery dynamicQuery();

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery);

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.reading.time.model.impl.ReadingTimeEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end);

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.reading.time.model.impl.ReadingTimeEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery, int start,
		int end, OrderByComparator<T> orderByComparator);

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows matching the dynamic query
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long dynamicQueryCount(DynamicQuery dynamicQuery);

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows matching the dynamic query
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public long dynamicQueryCount(DynamicQuery dynamicQuery,
		Projection projection);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ReadingTimeEntry fetchOrAddReadingTimeEntry(
		GroupedModel groupedModel);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ReadingTimeEntry fetchReadingTimeEntry(GroupedModel groupedModel);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ReadingTimeEntry fetchReadingTimeEntry(long readingTimeEntryId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ReadingTimeEntry fetchReadingTimeEntry(long groupId,
		long classNameId, long classPK);

	/**
	* Returns the reading time entry matching the UUID and group.
	*
	* @param uuid the reading time entry's UUID
	* @param groupId the primary key of the group
	* @return the matching reading time entry, or <code>null</code> if a matching reading time entry could not be found
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ReadingTimeEntry fetchReadingTimeEntryByUuidAndGroupId(String uuid,
		long groupId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		PortletDataContext portletDataContext);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public IndexableActionableDynamicQuery getIndexableActionableDynamicQuery();

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public String getOSGiServiceIdentifier();

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException;

	/**
	* Returns a range of all the reading time entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.reading.time.model.impl.ReadingTimeEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of reading time entries
	* @param end the upper bound of the range of reading time entries (not inclusive)
	* @return the range of reading time entries
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ReadingTimeEntry> getReadingTimeEntries(int start, int end);

	/**
	* Returns all the reading time entries matching the UUID and company.
	*
	* @param uuid the UUID of the reading time entries
	* @param companyId the primary key of the company
	* @return the matching reading time entries, or an empty list if no matches were found
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ReadingTimeEntry> getReadingTimeEntriesByUuidAndCompanyId(
		String uuid, long companyId);

	/**
	* Returns a range of reading time entries matching the UUID and company.
	*
	* @param uuid the UUID of the reading time entries
	* @param companyId the primary key of the company
	* @param start the lower bound of the range of reading time entries
	* @param end the upper bound of the range of reading time entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the range of matching reading time entries, or an empty list if no matches were found
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ReadingTimeEntry> getReadingTimeEntriesByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<ReadingTimeEntry> orderByComparator);

	/**
	* Returns the number of reading time entries.
	*
	* @return the number of reading time entries
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getReadingTimeEntriesCount();

	/**
	* Returns the reading time entry with the primary key.
	*
	* @param readingTimeEntryId the primary key of the reading time entry
	* @return the reading time entry
	* @throws PortalException if a reading time entry with the primary key could not be found
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ReadingTimeEntry getReadingTimeEntry(long readingTimeEntryId)
		throws PortalException;

	/**
	* Returns the reading time entry matching the UUID and group.
	*
	* @param uuid the reading time entry's UUID
	* @param groupId the primary key of the group
	* @return the matching reading time entry
	* @throws PortalException if a matching reading time entry could not be found
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ReadingTimeEntry getReadingTimeEntryByUuidAndGroupId(String uuid,
		long groupId) throws PortalException;

	public ReadingTimeEntry updateReadingTimeEntry(GroupedModel groupedModel);

	public ReadingTimeEntry updateReadingTimeEntry(long groupId,
		long classNameId, long classPK, Duration readingTimeDuration);

	/**
	* Updates the reading time entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param readingTimeEntry the reading time entry
	* @return the reading time entry that was updated
	*/
	@Indexable(type = IndexableType.REINDEX)
	public ReadingTimeEntry updateReadingTimeEntry(
		ReadingTimeEntry readingTimeEntry);
}