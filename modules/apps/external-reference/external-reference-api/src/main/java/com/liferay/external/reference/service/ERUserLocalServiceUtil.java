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
 * Provides the local service utility for ERUser. This utility wraps
 * {@link com.liferay.external.reference.service.impl.ERUserLocalServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see ERUserLocalService
 * @see com.liferay.external.reference.service.base.ERUserLocalServiceBaseImpl
 * @see com.liferay.external.reference.service.impl.ERUserLocalServiceImpl
 * @generated
 */
@ProviderType
public class ERUserLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.external.reference.service.impl.ERUserLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.kernel.model.User addOrUpdateUser(
		String externalReferenceCode, long creatorUserId, long companyId,
		boolean autoPassword, String password1, String password2,
		boolean autoScreenName, String screenName, String emailAddress,
		java.util.Locale locale, String firstName, String middleName,
		String lastName, long prefixId, long suffixId, boolean male,
		int birthdayMonth, int birthdayDay, int birthdayYear, String jobTitle,
		long[] groupIds, long[] organizationIds, long[] roleIds,
		java.util.List<com.liferay.portal.kernel.model.UserGroupRole> userGroupRoles,
		long[] userGroupIds, boolean sendEmail,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addOrUpdateUser(externalReferenceCode, creatorUserId,
			companyId, autoPassword, password1, password2, autoScreenName,
			screenName, emailAddress, locale, firstName, middleName, lastName,
			prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear,
			jobTitle, groupIds, organizationIds, roleIds, userGroupRoles,
			userGroupIds, sendEmail, serviceContext);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static ERUserLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<ERUserLocalService, ERUserLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(ERUserLocalService.class);

		ServiceTracker<ERUserLocalService, ERUserLocalService> serviceTracker = new ServiceTracker<ERUserLocalService, ERUserLocalService>(bundle.getBundleContext(),
				ERUserLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
}