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

package com.liferay.portal.security.audit.storage.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for AuditEvent. This utility wraps
 * <code>com.liferay.portal.security.audit.storage.service.impl.AuditEventServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see AuditEventService
 * @generated
 */
public class AuditEventServiceUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.security.audit.storage.service.impl.AuditEventServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link AuditEventServiceUtil} to access the audit event remote service. Add custom service methods to <code>com.liferay.portal.security.audit.storage.service.impl.AuditEventServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static java.util.List
		<com.liferay.portal.security.audit.storage.model.AuditEvent>
				getAuditEvents(long companyId, int start, int end)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditEvents(companyId, start, end);
	}

	public static java.util.List
		<com.liferay.portal.security.audit.storage.model.AuditEvent>
				getAuditEvents(
					long companyId, int start, int end,
					com.liferay.portal.kernel.util.OrderByComparator
						orderByComparator)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditEvents(
			companyId, start, end, orderByComparator);
	}

	public static java.util.List
		<com.liferay.portal.security.audit.storage.model.AuditEvent>
				getAuditEvents(
					long companyId, long userId, String userName,
					java.util.Date createDateGT, java.util.Date createDateLT,
					String eventType, String className, String classPK,
					String clientHost, String clientIP, String serverName,
					int serverPort, String sessionID, boolean andSearch,
					int start, int end)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditEvents(
			companyId, userId, userName, createDateGT, createDateLT, eventType,
			className, classPK, clientHost, clientIP, serverName, serverPort,
			sessionID, andSearch, start, end);
	}

	public static java.util.List
		<com.liferay.portal.security.audit.storage.model.AuditEvent>
				getAuditEvents(
					long companyId, long userId, String userName,
					java.util.Date createDateGT, java.util.Date createDateLT,
					String eventType, String className, String classPK,
					String clientHost, String clientIP, String serverName,
					int serverPort, String sessionID, boolean andSearch,
					int start, int end,
					com.liferay.portal.kernel.util.OrderByComparator
						orderByComparator)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditEvents(
			companyId, userId, userName, createDateGT, createDateLT, eventType,
			className, classPK, clientHost, clientIP, serverName, serverPort,
			sessionID, andSearch, start, end, orderByComparator);
	}

	public static int getAuditEventsCount(long companyId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditEventsCount(companyId);
	}

	public static int getAuditEventsCount(
			long companyId, long userId, String userName,
			java.util.Date createDateGT, java.util.Date createDateLT,
			String eventType, String className, String classPK,
			String clientHost, String clientIP, String serverName,
			int serverPort, String sessionID, boolean andSearch)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAuditEventsCount(
			companyId, userId, userName, createDateGT, createDateLT, eventType,
			className, classPK, clientHost, clientIP, serverName, serverPort,
			sessionID, andSearch);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static AuditEventService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<AuditEventService, AuditEventService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(AuditEventService.class);

		ServiceTracker<AuditEventService, AuditEventService> serviceTracker =
			new ServiceTracker<AuditEventService, AuditEventService>(
				bundle.getBundleContext(), AuditEventService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}