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

package com.liferay.layout.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link LayoutClassedModelUsageLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see LayoutClassedModelUsageLocalService
 * @generated
 */
public class LayoutClassedModelUsageLocalServiceWrapper
	implements LayoutClassedModelUsageLocalService,
			   ServiceWrapper<LayoutClassedModelUsageLocalService> {

	public LayoutClassedModelUsageLocalServiceWrapper(
		LayoutClassedModelUsageLocalService
			layoutClassedModelUsageLocalService) {

		_layoutClassedModelUsageLocalService =
			layoutClassedModelUsageLocalService;
	}

	@Override
	public com.liferay.layout.model.LayoutClassedModelUsage
		addDefaultLayoutClassedModelUsage(
			long groupId, long classNameId, long classPK,
			com.liferay.portal.kernel.service.ServiceContext serviceContext) {

		return _layoutClassedModelUsageLocalService.
			addDefaultLayoutClassedModelUsage(
				groupId, classNameId, classPK, serviceContext);
	}

	/**
	 * Adds the layout classed model usage to the database. Also notifies the appropriate model listeners.
	 *
	 * @param layoutClassedModelUsage the layout classed model usage
	 * @return the layout classed model usage that was added
	 */
	@Override
	public com.liferay.layout.model.LayoutClassedModelUsage
		addLayoutClassedModelUsage(
			com.liferay.layout.model.LayoutClassedModelUsage
				layoutClassedModelUsage) {

		return _layoutClassedModelUsageLocalService.addLayoutClassedModelUsage(
			layoutClassedModelUsage);
	}

	@Override
	public com.liferay.layout.model.LayoutClassedModelUsage
		addLayoutClassedModelUsage(
			long groupId, long classNameId, long classPK, String containerKey,
			long containerType, long plid,
			com.liferay.portal.kernel.service.ServiceContext serviceContext) {

		return _layoutClassedModelUsageLocalService.addLayoutClassedModelUsage(
			groupId, classNameId, classPK, containerKey, containerType, plid,
			serviceContext);
	}

	/**
	 * Creates a new layout classed model usage with the primary key. Does not add the layout classed model usage to the database.
	 *
	 * @param layoutClassedModelUsageId the primary key for the new layout classed model usage
	 * @return the new layout classed model usage
	 */
	@Override
	public com.liferay.layout.model.LayoutClassedModelUsage
		createLayoutClassedModelUsage(long layoutClassedModelUsageId) {

		return _layoutClassedModelUsageLocalService.
			createLayoutClassedModelUsage(layoutClassedModelUsageId);
	}

	/**
	 * Deletes the layout classed model usage from the database. Also notifies the appropriate model listeners.
	 *
	 * @param layoutClassedModelUsage the layout classed model usage
	 * @return the layout classed model usage that was removed
	 */
	@Override
	public com.liferay.layout.model.LayoutClassedModelUsage
		deleteLayoutClassedModelUsage(
			com.liferay.layout.model.LayoutClassedModelUsage
				layoutClassedModelUsage) {

		return _layoutClassedModelUsageLocalService.
			deleteLayoutClassedModelUsage(layoutClassedModelUsage);
	}

	/**
	 * Deletes the layout classed model usage with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param layoutClassedModelUsageId the primary key of the layout classed model usage
	 * @return the layout classed model usage that was removed
	 * @throws PortalException if a layout classed model usage with the primary key could not be found
	 */
	@Override
	public com.liferay.layout.model.LayoutClassedModelUsage
			deleteLayoutClassedModelUsage(long layoutClassedModelUsageId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _layoutClassedModelUsageLocalService.
			deleteLayoutClassedModelUsage(layoutClassedModelUsageId);
	}

	@Override
	public void deleteLayoutClassedModelUsages(long classNameId, long classPK) {
		_layoutClassedModelUsageLocalService.deleteLayoutClassedModelUsages(
			classNameId, classPK);
	}

	@Override
	public void deleteLayoutClassedModelUsages(
		String containerKey, long containerType, long plid) {

		_layoutClassedModelUsageLocalService.deleteLayoutClassedModelUsages(
			containerKey, containerType, plid);
	}

	@Override
	public void deleteLayoutClassedModelUsagesByPlid(long plid) {
		_layoutClassedModelUsageLocalService.
			deleteLayoutClassedModelUsagesByPlid(plid);
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
			com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _layoutClassedModelUsageLocalService.deletePersistedModel(
			persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _layoutClassedModelUsageLocalService.dynamicQuery();
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

		return _layoutClassedModelUsageLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.layout.model.impl.LayoutClassedModelUsageModelImpl</code>.
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

		return _layoutClassedModelUsageLocalService.dynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.layout.model.impl.LayoutClassedModelUsageModelImpl</code>.
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

		return _layoutClassedModelUsageLocalService.dynamicQuery(
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

		return _layoutClassedModelUsageLocalService.dynamicQueryCount(
			dynamicQuery);
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

		return _layoutClassedModelUsageLocalService.dynamicQueryCount(
			dynamicQuery, projection);
	}

	@Override
	public com.liferay.layout.model.LayoutClassedModelUsage
		fetchLayoutClassedModelUsage(long layoutClassedModelUsageId) {

		return _layoutClassedModelUsageLocalService.
			fetchLayoutClassedModelUsage(layoutClassedModelUsageId);
	}

	@Override
	public com.liferay.layout.model.LayoutClassedModelUsage
		fetchLayoutClassedModelUsage(
			long classNameId, long classPK, String containerKey,
			long containerType, long plid) {

		return _layoutClassedModelUsageLocalService.
			fetchLayoutClassedModelUsage(
				classNameId, classPK, containerKey, containerType, plid);
	}

	/**
	 * Returns the layout classed model usage matching the UUID and group.
	 *
	 * @param uuid the layout classed model usage's UUID
	 * @param groupId the primary key of the group
	 * @return the matching layout classed model usage, or <code>null</code> if a matching layout classed model usage could not be found
	 */
	@Override
	public com.liferay.layout.model.LayoutClassedModelUsage
		fetchLayoutClassedModelUsageByUuidAndGroupId(
			String uuid, long groupId) {

		return _layoutClassedModelUsageLocalService.
			fetchLayoutClassedModelUsageByUuidAndGroupId(uuid, groupId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return _layoutClassedModelUsageLocalService.getActionableDynamicQuery();
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		return _layoutClassedModelUsageLocalService.
			getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the layout classed model usage with the primary key.
	 *
	 * @param layoutClassedModelUsageId the primary key of the layout classed model usage
	 * @return the layout classed model usage
	 * @throws PortalException if a layout classed model usage with the primary key could not be found
	 */
	@Override
	public com.liferay.layout.model.LayoutClassedModelUsage
			getLayoutClassedModelUsage(long layoutClassedModelUsageId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _layoutClassedModelUsageLocalService.getLayoutClassedModelUsage(
			layoutClassedModelUsageId);
	}

	/**
	 * Returns the layout classed model usage matching the UUID and group.
	 *
	 * @param uuid the layout classed model usage's UUID
	 * @param groupId the primary key of the group
	 * @return the matching layout classed model usage
	 * @throws PortalException if a matching layout classed model usage could not be found
	 */
	@Override
	public com.liferay.layout.model.LayoutClassedModelUsage
			getLayoutClassedModelUsageByUuidAndGroupId(
				String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _layoutClassedModelUsageLocalService.
			getLayoutClassedModelUsageByUuidAndGroupId(uuid, groupId);
	}

	/**
	 * Returns a range of all the layout classed model usages.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.layout.model.impl.LayoutClassedModelUsageModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of layout classed model usages
	 * @param end the upper bound of the range of layout classed model usages (not inclusive)
	 * @return the range of layout classed model usages
	 */
	@Override
	public java.util.List<com.liferay.layout.model.LayoutClassedModelUsage>
		getLayoutClassedModelUsages(int start, int end) {

		return _layoutClassedModelUsageLocalService.getLayoutClassedModelUsages(
			start, end);
	}

	@Override
	public java.util.List<com.liferay.layout.model.LayoutClassedModelUsage>
		getLayoutClassedModelUsages(long classNameId, long classPK) {

		return _layoutClassedModelUsageLocalService.getLayoutClassedModelUsages(
			classNameId, classPK);
	}

	@Override
	public java.util.List<com.liferay.layout.model.LayoutClassedModelUsage>
		getLayoutClassedModelUsages(
			long classNameId, long classPK, int type, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.layout.model.LayoutClassedModelUsage>
					orderByComparator) {

		return _layoutClassedModelUsageLocalService.getLayoutClassedModelUsages(
			classNameId, classPK, type, start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.model.LayoutClassedModelUsage>
		getLayoutClassedModelUsages(
			long classNameId, long classPK, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.layout.model.LayoutClassedModelUsage>
					orderByComparator) {

		return _layoutClassedModelUsageLocalService.getLayoutClassedModelUsages(
			classNameId, classPK, start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.model.LayoutClassedModelUsage>
		getLayoutClassedModelUsagesByPlid(long plid) {

		return _layoutClassedModelUsageLocalService.
			getLayoutClassedModelUsagesByPlid(plid);
	}

	/**
	 * Returns the number of layout classed model usages.
	 *
	 * @return the number of layout classed model usages
	 */
	@Override
	public int getLayoutClassedModelUsagesCount() {
		return _layoutClassedModelUsageLocalService.
			getLayoutClassedModelUsagesCount();
	}

	@Override
	public int getLayoutClassedModelUsagesCount(
		long classNameId, long classPK) {

		return _layoutClassedModelUsageLocalService.
			getLayoutClassedModelUsagesCount(classNameId, classPK);
	}

	@Override
	public int getLayoutClassedModelUsagesCount(
		long classNameId, long classPK, int type) {

		return _layoutClassedModelUsageLocalService.
			getLayoutClassedModelUsagesCount(classNameId, classPK, type);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _layoutClassedModelUsageLocalService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
			java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _layoutClassedModelUsageLocalService.getPersistedModel(
			primaryKeyObj);
	}

	@Override
	public int getUniqueLayoutClassedModelUsagesCount(
		long classNameId, long classPK) {

		return _layoutClassedModelUsageLocalService.
			getUniqueLayoutClassedModelUsagesCount(classNameId, classPK);
	}

	@Override
	public boolean hasDefaultLayoutClassedModelUsage(
		long classNameId, long classPK) {

		return _layoutClassedModelUsageLocalService.
			hasDefaultLayoutClassedModelUsage(classNameId, classPK);
	}

	/**
	 * Updates the layout classed model usage in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param layoutClassedModelUsage the layout classed model usage
	 * @return the layout classed model usage that was updated
	 */
	@Override
	public com.liferay.layout.model.LayoutClassedModelUsage
		updateLayoutClassedModelUsage(
			com.liferay.layout.model.LayoutClassedModelUsage
				layoutClassedModelUsage) {

		return _layoutClassedModelUsageLocalService.
			updateLayoutClassedModelUsage(layoutClassedModelUsage);
	}

	@Override
	public LayoutClassedModelUsageLocalService getWrappedService() {
		return _layoutClassedModelUsageLocalService;
	}

	@Override
	public void setWrappedService(
		LayoutClassedModelUsageLocalService
			layoutClassedModelUsageLocalService) {

		_layoutClassedModelUsageLocalService =
			layoutClassedModelUsageLocalService;
	}

	private LayoutClassedModelUsageLocalService
		_layoutClassedModelUsageLocalService;

}