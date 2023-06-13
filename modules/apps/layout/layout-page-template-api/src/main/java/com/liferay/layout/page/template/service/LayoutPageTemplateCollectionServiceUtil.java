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

package com.liferay.layout.page.template.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for LayoutPageTemplateCollection. This utility wraps
 * <code>com.liferay.layout.page.template.service.impl.LayoutPageTemplateCollectionServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see LayoutPageTemplateCollectionService
 * @generated
 */
public class LayoutPageTemplateCollectionServiceUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.layout.page.template.service.impl.LayoutPageTemplateCollectionServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link LayoutPageTemplateCollectionServiceUtil} to access the layout page template collection remote service. Add custom service methods to <code>com.liferay.layout.page.template.service.impl.LayoutPageTemplateCollectionServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static
		com.liferay.layout.page.template.model.LayoutPageTemplateCollection
				addLayoutPageTemplateCollection(
					long groupId, String name, String description,
					com.liferay.portal.kernel.service.ServiceContext
						serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addLayoutPageTemplateCollection(
			groupId, name, description, serviceContext);
	}

	public static
		com.liferay.layout.page.template.model.LayoutPageTemplateCollection
				deleteLayoutPageTemplateCollection(
					long layoutPageTemplateCollectionId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteLayoutPageTemplateCollection(
			layoutPageTemplateCollectionId);
	}

	public static void deleteLayoutPageTemplateCollections(
			long[] layoutPageTemplateCollectionIds)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteLayoutPageTemplateCollections(
			layoutPageTemplateCollectionIds);
	}

	public static
		com.liferay.layout.page.template.model.LayoutPageTemplateCollection
				fetchLayoutPageTemplateCollection(
					long layoutPageTemplateCollectionId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().fetchLayoutPageTemplateCollection(
			layoutPageTemplateCollectionId);
	}

	public static java.util.List
		<com.liferay.layout.page.template.model.LayoutPageTemplateCollection>
			getLayoutPageTemplateCollections(long groupId) {

		return getService().getLayoutPageTemplateCollections(groupId);
	}

	public static java.util.List
		<com.liferay.layout.page.template.model.LayoutPageTemplateCollection>
			getLayoutPageTemplateCollections(long groupId, int start, int end) {

		return getService().getLayoutPageTemplateCollections(
			groupId, start, end);
	}

	public static java.util.List
		<com.liferay.layout.page.template.model.LayoutPageTemplateCollection>
			getLayoutPageTemplateCollections(
				long groupId, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.layout.page.template.model.
						LayoutPageTemplateCollection> orderByComparator) {

		return getService().getLayoutPageTemplateCollections(
			groupId, start, end, orderByComparator);
	}

	public static java.util.List
		<com.liferay.layout.page.template.model.LayoutPageTemplateCollection>
			getLayoutPageTemplateCollections(
				long groupId, String name, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.layout.page.template.model.
						LayoutPageTemplateCollection> orderByComparator) {

		return getService().getLayoutPageTemplateCollections(
			groupId, name, start, end, orderByComparator);
	}

	public static int getLayoutPageTemplateCollectionsCount(long groupId) {
		return getService().getLayoutPageTemplateCollectionsCount(groupId);
	}

	public static int getLayoutPageTemplateCollectionsCount(
		long groupId, String name) {

		return getService().getLayoutPageTemplateCollectionsCount(
			groupId, name);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static
		com.liferay.layout.page.template.model.LayoutPageTemplateCollection
				updateLayoutPageTemplateCollection(
					long layoutPageTemplateCollectionId, String name,
					String description)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateLayoutPageTemplateCollection(
			layoutPageTemplateCollectionId, name, description);
	}

	public static LayoutPageTemplateCollectionService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<LayoutPageTemplateCollectionService,
		 LayoutPageTemplateCollectionService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			LayoutPageTemplateCollectionService.class);

		ServiceTracker
			<LayoutPageTemplateCollectionService,
			 LayoutPageTemplateCollectionService> serviceTracker =
				new ServiceTracker
					<LayoutPageTemplateCollectionService,
					 LayoutPageTemplateCollectionService>(
						 bundle.getBundleContext(),
						 LayoutPageTemplateCollectionService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}