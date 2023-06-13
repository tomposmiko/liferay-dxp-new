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

package com.liferay.external.reference.service;

import aQute.bnd.annotation.ProviderType;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for ERUserGroup. This utility wraps
 * {@link com.liferay.external.reference.service.impl.ERUserGroupLocalServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see ERUserGroupLocalService
 * @see com.liferay.external.reference.service.base.ERUserGroupLocalServiceBaseImpl
 * @see com.liferay.external.reference.service.impl.ERUserGroupLocalServiceImpl
 * @generated
 */
@ProviderType
public class ERUserGroupLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.external.reference.service.impl.ERUserGroupLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.kernel.model.UserGroup addOrUpdateUserGroup(
		String externalReferenceCode, long userId, long companyId, String name,
		String description,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addOrUpdateUserGroup(externalReferenceCode, userId,
			companyId, name, description, serviceContext);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static ERUserGroupLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<ERUserGroupLocalService, ERUserGroupLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(ERUserGroupLocalService.class);

		ServiceTracker<ERUserGroupLocalService, ERUserGroupLocalService> serviceTracker =
			new ServiceTracker<ERUserGroupLocalService, ERUserGroupLocalService>(bundle.getBundleContext(),
				ERUserGroupLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
}