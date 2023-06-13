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

package com.liferay.site.navigation.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link SiteNavigationMenuItemLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see SiteNavigationMenuItemLocalService
 * @generated
 */
@ProviderType
public class SiteNavigationMenuItemLocalServiceWrapper
	implements SiteNavigationMenuItemLocalService,
		ServiceWrapper<SiteNavigationMenuItemLocalService> {
	public SiteNavigationMenuItemLocalServiceWrapper(
		SiteNavigationMenuItemLocalService siteNavigationMenuItemLocalService) {
		_siteNavigationMenuItemLocalService = siteNavigationMenuItemLocalService;
	}

	@Override
	public com.liferay.site.navigation.model.SiteNavigationMenuItem addSiteNavigationMenuItem(
		long userId, long groupId, long siteNavigationMenuId,
		long parentSiteNavigationMenuItemId, java.lang.String type, int order,
		java.lang.String typeSettings,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _siteNavigationMenuItemLocalService.addSiteNavigationMenuItem(userId,
			groupId, siteNavigationMenuId, parentSiteNavigationMenuItemId,
			type, order, typeSettings, serviceContext);
	}

	@Override
	public com.liferay.site.navigation.model.SiteNavigationMenuItem addSiteNavigationMenuItem(
		long userId, long groupId, long siteNavigationMenuId,
		long parentSiteNavigationMenuItemId, java.lang.String type,
		java.lang.String typeSettings,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _siteNavigationMenuItemLocalService.addSiteNavigationMenuItem(userId,
			groupId, siteNavigationMenuId, parentSiteNavigationMenuItemId,
			type, typeSettings, serviceContext);
	}

	/**
	* Adds the site navigation menu item to the database. Also notifies the appropriate model listeners.
	*
	* @param siteNavigationMenuItem the site navigation menu item
	* @return the site navigation menu item that was added
	*/
	@Override
	public com.liferay.site.navigation.model.SiteNavigationMenuItem addSiteNavigationMenuItem(
		com.liferay.site.navigation.model.SiteNavigationMenuItem siteNavigationMenuItem) {
		return _siteNavigationMenuItemLocalService.addSiteNavigationMenuItem(siteNavigationMenuItem);
	}

	/**
	* Creates a new site navigation menu item with the primary key. Does not add the site navigation menu item to the database.
	*
	* @param siteNavigationMenuItemId the primary key for the new site navigation menu item
	* @return the new site navigation menu item
	*/
	@Override
	public com.liferay.site.navigation.model.SiteNavigationMenuItem createSiteNavigationMenuItem(
		long siteNavigationMenuItemId) {
		return _siteNavigationMenuItemLocalService.createSiteNavigationMenuItem(siteNavigationMenuItemId);
	}

	/**
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
		com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _siteNavigationMenuItemLocalService.deletePersistedModel(persistedModel);
	}

	/**
	* Deletes the site navigation menu item with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param siteNavigationMenuItemId the primary key of the site navigation menu item
	* @return the site navigation menu item that was removed
	* @throws PortalException if a site navigation menu item with the primary key could not be found
	*/
	@Override
	public com.liferay.site.navigation.model.SiteNavigationMenuItem deleteSiteNavigationMenuItem(
		long siteNavigationMenuItemId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _siteNavigationMenuItemLocalService.deleteSiteNavigationMenuItem(siteNavigationMenuItemId);
	}

	/**
	* Deletes the site navigation menu item from the database. Also notifies the appropriate model listeners.
	*
	* @param siteNavigationMenuItem the site navigation menu item
	* @return the site navigation menu item that was removed
	*/
	@Override
	public com.liferay.site.navigation.model.SiteNavigationMenuItem deleteSiteNavigationMenuItem(
		com.liferay.site.navigation.model.SiteNavigationMenuItem siteNavigationMenuItem) {
		return _siteNavigationMenuItemLocalService.deleteSiteNavigationMenuItem(siteNavigationMenuItem);
	}

	@Override
	public void deleteSiteNavigationMenuItems(long siteNavigationMenuId)
		throws com.liferay.portal.kernel.exception.PortalException {
		_siteNavigationMenuItemLocalService.deleteSiteNavigationMenuItems(siteNavigationMenuId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _siteNavigationMenuItemLocalService.dynamicQuery();
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
		return _siteNavigationMenuItemLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.site.navigation.model.impl.SiteNavigationMenuItemModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return _siteNavigationMenuItemLocalService.dynamicQuery(dynamicQuery,
			start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.site.navigation.model.impl.SiteNavigationMenuItemModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return _siteNavigationMenuItemLocalService.dynamicQuery(dynamicQuery,
			start, end, orderByComparator);
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
		return _siteNavigationMenuItemLocalService.dynamicQueryCount(dynamicQuery);
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
		return _siteNavigationMenuItemLocalService.dynamicQueryCount(dynamicQuery,
			projection);
	}

	@Override
	public com.liferay.site.navigation.model.SiteNavigationMenuItem fetchSiteNavigationMenuItem(
		long siteNavigationMenuItemId) {
		return _siteNavigationMenuItemLocalService.fetchSiteNavigationMenuItem(siteNavigationMenuItemId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return _siteNavigationMenuItemLocalService.getActionableDynamicQuery();
	}

	@Override
	public java.util.List<com.liferay.site.navigation.model.SiteNavigationMenuItem> getChildSiteNavigationMenuItems(
		long parentSiteNavigationMenuItemId) {
		return _siteNavigationMenuItemLocalService.getChildSiteNavigationMenuItems(parentSiteNavigationMenuItemId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		return _siteNavigationMenuItemLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public java.lang.String getOSGiServiceIdentifier() {
		return _siteNavigationMenuItemLocalService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _siteNavigationMenuItemLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	* Returns the site navigation menu item with the primary key.
	*
	* @param siteNavigationMenuItemId the primary key of the site navigation menu item
	* @return the site navigation menu item
	* @throws PortalException if a site navigation menu item with the primary key could not be found
	*/
	@Override
	public com.liferay.site.navigation.model.SiteNavigationMenuItem getSiteNavigationMenuItem(
		long siteNavigationMenuItemId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _siteNavigationMenuItemLocalService.getSiteNavigationMenuItem(siteNavigationMenuItemId);
	}

	/**
	* Returns a range of all the site navigation menu items.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.site.navigation.model.impl.SiteNavigationMenuItemModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of site navigation menu items
	* @param end the upper bound of the range of site navigation menu items (not inclusive)
	* @return the range of site navigation menu items
	*/
	@Override
	public java.util.List<com.liferay.site.navigation.model.SiteNavigationMenuItem> getSiteNavigationMenuItems(
		int start, int end) {
		return _siteNavigationMenuItemLocalService.getSiteNavigationMenuItems(start,
			end);
	}

	@Override
	public java.util.List<com.liferay.site.navigation.model.SiteNavigationMenuItem> getSiteNavigationMenuItems(
		long siteNavigationMenuId) {
		return _siteNavigationMenuItemLocalService.getSiteNavigationMenuItems(siteNavigationMenuId);
	}

	@Override
	public java.util.List<com.liferay.site.navigation.model.SiteNavigationMenuItem> getSiteNavigationMenuItems(
		long siteNavigationMenuId, long parentSiteNavigationMenuItemId) {
		return _siteNavigationMenuItemLocalService.getSiteNavigationMenuItems(siteNavigationMenuId,
			parentSiteNavigationMenuItemId);
	}

	/**
	* Returns the number of site navigation menu items.
	*
	* @return the number of site navigation menu items
	*/
	@Override
	public int getSiteNavigationMenuItemsCount() {
		return _siteNavigationMenuItemLocalService.getSiteNavigationMenuItemsCount();
	}

	@Override
	public com.liferay.site.navigation.model.SiteNavigationMenuItem updateSiteNavigationMenuItem(
		long siteNavigationMenuItemId, long parentSiteNavigationMenuItemId,
		int order) throws com.liferay.portal.kernel.exception.PortalException {
		return _siteNavigationMenuItemLocalService.updateSiteNavigationMenuItem(siteNavigationMenuItemId,
			parentSiteNavigationMenuItemId, order);
	}

	@Override
	public com.liferay.site.navigation.model.SiteNavigationMenuItem updateSiteNavigationMenuItem(
		long userId, long siteNavigationMenuItemId,
		java.lang.String typeSettings,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _siteNavigationMenuItemLocalService.updateSiteNavigationMenuItem(userId,
			siteNavigationMenuItemId, typeSettings, serviceContext);
	}

	/**
	* Updates the site navigation menu item in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param siteNavigationMenuItem the site navigation menu item
	* @return the site navigation menu item that was updated
	*/
	@Override
	public com.liferay.site.navigation.model.SiteNavigationMenuItem updateSiteNavigationMenuItem(
		com.liferay.site.navigation.model.SiteNavigationMenuItem siteNavigationMenuItem) {
		return _siteNavigationMenuItemLocalService.updateSiteNavigationMenuItem(siteNavigationMenuItem);
	}

	@Override
	public SiteNavigationMenuItemLocalService getWrappedService() {
		return _siteNavigationMenuItemLocalService;
	}

	@Override
	public void setWrappedService(
		SiteNavigationMenuItemLocalService siteNavigationMenuItemLocalService) {
		_siteNavigationMenuItemLocalService = siteNavigationMenuItemLocalService;
	}

	private SiteNavigationMenuItemLocalService _siteNavigationMenuItemLocalService;
}