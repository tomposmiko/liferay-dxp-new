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

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for SiteNavigationMenu. This utility wraps
 * <code>com.liferay.site.navigation.service.impl.SiteNavigationMenuServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see SiteNavigationMenuService
 * @generated
 */
public class SiteNavigationMenuServiceUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.site.navigation.service.impl.SiteNavigationMenuServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link SiteNavigationMenuServiceUtil} to access the site navigation menu remote service. Add custom service methods to <code>com.liferay.site.navigation.service.impl.SiteNavigationMenuServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static com.liferay.site.navigation.model.SiteNavigationMenu
			addSiteNavigationMenu(
				long groupId, String name, int type, boolean auto,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addSiteNavigationMenu(
			groupId, name, type, auto, serviceContext);
	}

	public static com.liferay.site.navigation.model.SiteNavigationMenu
			addSiteNavigationMenu(
				long groupId, String name, int type,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addSiteNavigationMenu(
			groupId, name, type, serviceContext);
	}

	public static com.liferay.site.navigation.model.SiteNavigationMenu
			addSiteNavigationMenu(
				long groupId, String name,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addSiteNavigationMenu(
			groupId, name, serviceContext);
	}

	public static com.liferay.site.navigation.model.SiteNavigationMenu
			deleteSiteNavigationMenu(long siteNavigationMenuId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteSiteNavigationMenu(siteNavigationMenuId);
	}

	public static com.liferay.site.navigation.model.SiteNavigationMenu
			fetchSiteNavigationMenu(long siteNavigationMenuId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().fetchSiteNavigationMenu(siteNavigationMenuId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static java.util.List
		<com.liferay.site.navigation.model.SiteNavigationMenu>
			getSiteNavigationMenus(long groupId) {

		return getService().getSiteNavigationMenus(groupId);
	}

	public static java.util.List
		<com.liferay.site.navigation.model.SiteNavigationMenu>
			getSiteNavigationMenus(
				long groupId, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					orderByComparator) {

		return getService().getSiteNavigationMenus(
			groupId, start, end, orderByComparator);
	}

	public static java.util.List
		<com.liferay.site.navigation.model.SiteNavigationMenu>
			getSiteNavigationMenus(
				long groupId, String keywords, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					orderByComparator) {

		return getService().getSiteNavigationMenus(
			groupId, keywords, start, end, orderByComparator);
	}

	public static int getSiteNavigationMenusCount(long groupId) {
		return getService().getSiteNavigationMenusCount(groupId);
	}

	public static int getSiteNavigationMenusCount(
		long groupId, String keywords) {

		return getService().getSiteNavigationMenusCount(groupId, keywords);
	}

	public static com.liferay.site.navigation.model.SiteNavigationMenu
			updateSiteNavigationMenu(
				long siteNavigationMenuId, int type, boolean auto,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateSiteNavigationMenu(
			siteNavigationMenuId, type, auto, serviceContext);
	}

	public static com.liferay.site.navigation.model.SiteNavigationMenu
			updateSiteNavigationMenu(
				long siteNavigationMenuId, String name,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateSiteNavigationMenu(
			siteNavigationMenuId, name, serviceContext);
	}

	public static SiteNavigationMenuService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<SiteNavigationMenuService, SiteNavigationMenuService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			SiteNavigationMenuService.class);

		ServiceTracker<SiteNavigationMenuService, SiteNavigationMenuService>
			serviceTracker =
				new ServiceTracker
					<SiteNavigationMenuService, SiteNavigationMenuService>(
						bundle.getBundleContext(),
						SiteNavigationMenuService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}