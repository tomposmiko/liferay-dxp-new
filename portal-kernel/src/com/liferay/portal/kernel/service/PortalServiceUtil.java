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

package com.liferay.portal.kernel.service;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;

/**
 * Provides the remote service utility for Portal. This utility wraps
 * <code>com.liferay.portal.service.impl.PortalServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see PortalService
 * @generated
 */
public class PortalServiceUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.service.impl.PortalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link PortalServiceUtil} to access the portal remote service. Add custom service methods to <code>com.liferay.portal.service.impl.PortalServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static String getAutoDeployDirectory() {
		return getService().getAutoDeployDirectory();
	}

	public static int getBuildNumber() {
		return getService().getBuildNumber();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static String getVersion() {
		return getService().getVersion();
	}

	public static void testAddClassName_Rollback(String classNameValue) {
		getService().testAddClassName_Rollback(classNameValue);
	}

	public static void testAddClassName_Success(String classNameValue) {
		getService().testAddClassName_Success(classNameValue);
	}

	public static void
		testAddClassNameAndTestTransactionPortletBar_PortalRollback(
			String transactionPortletBarText) {

		getService().
			testAddClassNameAndTestTransactionPortletBar_PortalRollback(
				transactionPortletBarText);
	}

	public static void
		testAddClassNameAndTestTransactionPortletBar_PortletRollback(
			String transactionPortletBarText) {

		getService().
			testAddClassNameAndTestTransactionPortletBar_PortletRollback(
				transactionPortletBarText);
	}

	public static void testAddClassNameAndTestTransactionPortletBar_Success(
		String transactionPortletBarText) {

		getService().testAddClassNameAndTestTransactionPortletBar_Success(
			transactionPortletBarText);
	}

	public static void testAutoSyncHibernateSessionStateOnTxCreation() {
		getService().testAutoSyncHibernateSessionStateOnTxCreation();
	}

	public static void testDeleteClassName()
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().testDeleteClassName();
	}

	public static int testGetBuildNumber() {
		return getService().testGetBuildNumber();
	}

	public static void testGetUserId() {
		getService().testGetUserId();
	}

	public static boolean testHasClassName() {
		return getService().testHasClassName();
	}

	public static PortalService getService() {
		if (_service == null) {
			_service = (PortalService)PortalBeanLocatorUtil.locate(
				PortalService.class.getName());
		}

		return _service;
	}

	private static PortalService _service;

}