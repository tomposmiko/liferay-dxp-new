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

package com.liferay.portal.security.service.access.policy.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for SAPEntry. This utility wraps
 * <code>com.liferay.portal.security.service.access.policy.service.impl.SAPEntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see SAPEntryService
 * @generated
 */
public class SAPEntryServiceUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.portal.security.service.access.policy.service.impl.SAPEntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link SAPEntryServiceUtil} to access the sap entry remote service. Add custom service methods to <code>com.liferay.portal.security.service.access.policy.service.impl.SAPEntryServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static
		com.liferay.portal.security.service.access.policy.model.SAPEntry
				addSAPEntry(
					String allowedServiceSignatures, boolean defaultSAPEntry,
					boolean enabled, String name,
					java.util.Map<java.util.Locale, String> titleMap,
					com.liferay.portal.kernel.service.ServiceContext
						serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addSAPEntry(
			allowedServiceSignatures, defaultSAPEntry, enabled, name, titleMap,
			serviceContext);
	}

	public static
		com.liferay.portal.security.service.access.policy.model.SAPEntry
				deleteSAPEntry(long sapEntryId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteSAPEntry(sapEntryId);
	}

	public static
		com.liferay.portal.security.service.access.policy.model.SAPEntry
				deleteSAPEntry(
					com.liferay.portal.security.service.access.policy.model.
						SAPEntry sapEntry)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteSAPEntry(sapEntry);
	}

	public static
		com.liferay.portal.security.service.access.policy.model.SAPEntry
				fetchSAPEntry(long companyId, String name)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().fetchSAPEntry(companyId, name);
	}

	public static java.util.List
		<com.liferay.portal.security.service.access.policy.model.SAPEntry>
			getCompanySAPEntries(long companyId, int start, int end) {

		return getService().getCompanySAPEntries(companyId, start, end);
	}

	public static java.util.List
		<com.liferay.portal.security.service.access.policy.model.SAPEntry>
			getCompanySAPEntries(
				long companyId, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.portal.security.service.access.policy.model.
						SAPEntry> obc) {

		return getService().getCompanySAPEntries(companyId, start, end, obc);
	}

	public static int getCompanySAPEntriesCount(long companyId) {
		return getService().getCompanySAPEntriesCount(companyId);
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
		com.liferay.portal.security.service.access.policy.model.SAPEntry
				getSAPEntry(long sapEntryId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getSAPEntry(sapEntryId);
	}

	public static
		com.liferay.portal.security.service.access.policy.model.SAPEntry
				getSAPEntry(long companyId, String name)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getSAPEntry(companyId, name);
	}

	public static
		com.liferay.portal.security.service.access.policy.model.SAPEntry
				updateSAPEntry(
					long sapEntryId, String allowedServiceSignatures,
					boolean defaultSAPEntry, boolean enabled, String name,
					java.util.Map<java.util.Locale, String> titleMap,
					com.liferay.portal.kernel.service.ServiceContext
						serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateSAPEntry(
			sapEntryId, allowedServiceSignatures, defaultSAPEntry, enabled,
			name, titleMap, serviceContext);
	}

	public static SAPEntryService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<SAPEntryService, SAPEntryService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(SAPEntryService.class);

		ServiceTracker<SAPEntryService, SAPEntryService> serviceTracker =
			new ServiceTracker<SAPEntryService, SAPEntryService>(
				bundle.getBundleContext(), SAPEntryService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}