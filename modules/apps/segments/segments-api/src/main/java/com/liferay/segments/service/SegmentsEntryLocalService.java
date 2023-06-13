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

package com.liferay.segments.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;

import com.liferay.segments.model.SegmentsEntry;

import java.io.Serializable;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Provides the local service interface for SegmentsEntry. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Eduardo Garcia
 * @see SegmentsEntryLocalServiceUtil
 * @see com.liferay.segments.service.base.SegmentsEntryLocalServiceBaseImpl
 * @see com.liferay.segments.service.impl.SegmentsEntryLocalServiceImpl
 * @generated
 */
@ProviderType
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
	PortalException.class, SystemException.class})
public interface SegmentsEntryLocalService extends BaseLocalService,
	PersistedModelLocalService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link SegmentsEntryLocalServiceUtil} to access the segments entry local service. Add custom service methods to {@link com.liferay.segments.service.impl.SegmentsEntryLocalServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	@Indexable(type = IndexableType.REINDEX)
	public SegmentsEntry addSegmentsEntry(Map<Locale, String> nameMap,
		Map<Locale, String> descriptionMap, boolean active, String criteria,
		String key, String source, String type, ServiceContext serviceContext)
		throws PortalException;

	/**
	* Adds the segments entry to the database. Also notifies the appropriate model listeners.
	*
	* @param segmentsEntry the segments entry
	* @return the segments entry that was added
	*/
	@Indexable(type = IndexableType.REINDEX)
	public SegmentsEntry addSegmentsEntry(SegmentsEntry segmentsEntry);

	/**
	* Creates a new segments entry with the primary key. Does not add the segments entry to the database.
	*
	* @param segmentsEntryId the primary key for the new segments entry
	* @return the new segments entry
	*/
	@Transactional(enabled = false)
	public SegmentsEntry createSegmentsEntry(long segmentsEntryId);

	/**
	* @throws PortalException
	*/
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException;

	public void deleteSegmentsEntries(long groupId) throws PortalException;

	public void deleteSegmentsEntries(String source) throws PortalException;

	/**
	* Deletes the segments entry with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param segmentsEntryId the primary key of the segments entry
	* @return the segments entry that was removed
	* @throws PortalException if a segments entry with the primary key could not be found
	*/
	@Indexable(type = IndexableType.DELETE)
	public SegmentsEntry deleteSegmentsEntry(long segmentsEntryId)
		throws PortalException;

	/**
	* Deletes the segments entry from the database. Also notifies the appropriate model listeners.
	*
	* @param segmentsEntry the segments entry
	* @return the segments entry that was removed
	* @throws PortalException
	*/
	@Indexable(type = IndexableType.DELETE)
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public SegmentsEntry deleteSegmentsEntry(SegmentsEntry segmentsEntry)
		throws PortalException;

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
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.segments.model.impl.SegmentsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.segments.model.impl.SegmentsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
	public SegmentsEntry fetchSegmentsEntry(long segmentsEntryId);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SegmentsEntry fetchSegmentsEntry(long groupId, String key);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ActionableDynamicQuery getActionableDynamicQuery();

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

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SegmentsEntry> getSegmentsEntries(boolean active, String type,
		int start, int end, OrderByComparator<SegmentsEntry> orderByComparator);

	/**
	* Returns a range of all the segments entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.segments.model.impl.SegmentsEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of segments entries
	* @param end the upper bound of the range of segments entries (not inclusive)
	* @return the range of segments entries
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SegmentsEntry> getSegmentsEntries(int start, int end);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SegmentsEntry> getSegmentsEntries(long groupId, boolean active,
		String type, int start, int end,
		OrderByComparator<SegmentsEntry> orderByComparator);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SegmentsEntry> getSegmentsEntries(long groupId, int start,
		int end, OrderByComparator<SegmentsEntry> orderByComparator);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SegmentsEntry> getSegmentsEntries(String type, int start,
		int end, OrderByComparator<SegmentsEntry> orderByComparator);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SegmentsEntry> getSegmentsEntriesBySource(String source,
		int start, int end, OrderByComparator<SegmentsEntry> orderByComparator);

	/**
	* Returns the number of segments entries.
	*
	* @return the number of segments entries
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getSegmentsEntriesCount();

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getSegmentsEntriesCount(long groupId);

	/**
	* Returns the segments entry with the primary key.
	*
	* @param segmentsEntryId the primary key of the segments entry
	* @return the segments entry
	* @throws PortalException if a segments entry with the primary key could not be found
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SegmentsEntry getSegmentsEntry(long segmentsEntryId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SegmentsEntry getSegmentsEntry(long groupId, String key)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public BaseModelSearchResult<SegmentsEntry> searchSegmentsEntries(
		long companyId, long groupId, String keywords, int start, int end,
		Sort sort) throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public BaseModelSearchResult<SegmentsEntry> searchSegmentsEntries(
		SearchContext searchContext) throws PortalException;

	@Indexable(type = IndexableType.REINDEX)
	public SegmentsEntry updateSegmentsEntry(long segmentsEntryId,
		Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
		boolean active, String criteria, String key,
		ServiceContext serviceContext) throws PortalException;

	/**
	* Updates the segments entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param segmentsEntry the segments entry
	* @return the segments entry that was updated
	*/
	@Indexable(type = IndexableType.REINDEX)
	public SegmentsEntry updateSegmentsEntry(SegmentsEntry segmentsEntry);
}