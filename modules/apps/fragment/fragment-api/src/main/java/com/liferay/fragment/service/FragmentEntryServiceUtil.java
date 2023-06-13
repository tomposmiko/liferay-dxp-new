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

package com.liferay.fragment.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for FragmentEntry. This utility wraps
 * <code>com.liferay.fragment.service.impl.FragmentEntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see FragmentEntryService
 * @generated
 */
public class FragmentEntryServiceUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.fragment.service.impl.FragmentEntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #addFragmentEntry(long, long, String, String, long, int, int,
	 ServiceContext)}
	 */
	@Deprecated
	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String name, int type,
			int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, name, type, status, serviceContext);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #addFragmentEntry(long, long, String, String, long, int, int,
	 ServiceContext)}
	 */
	@Deprecated
	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String name, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, name, status, serviceContext);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #addFragmentEntry(long, long, String, String, long, int, int,
	 ServiceContext)}
	 */
	@Deprecated
	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String fragmentEntryKey,
			String name, int type, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, fragmentEntryKey, name, type, status,
			serviceContext);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #addFragmentEntry(long, long, String, String, long, int, int,
	 ServiceContext)}
	 */
	@Deprecated
	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String fragmentEntryKey,
			String name, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, fragmentEntryKey, name, status,
			serviceContext);
	}

	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String fragmentEntryKey,
			String name, long previewFileEntryId, int type, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, fragmentEntryKey, name,
			previewFileEntryId, type, status, serviceContext);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #addFragmentEntry(long, long, String, String, String, String,
	 String, String, long, int, int, ServiceContext)}
	 */
	@Deprecated
	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String name, String css,
			String html, String js, int type, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, name, css, html, js, type, status,
			serviceContext);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #addFragmentEntry(long, long, String, String, String, String,
	 String, String, long, int, int, ServiceContext)}
	 */
	@Deprecated
	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String name, String css,
			String html, String js, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, name, css, html, js, status,
			serviceContext);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #addFragmentEntry(long, long, String, String, String, String,
	 String, String, long, int, int, ServiceContext)}
	 */
	@Deprecated
	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String fragmentEntryKey,
			String name, String css, String html, String js, int type,
			int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, fragmentEntryKey, name, css, html,
			js, type, status, serviceContext);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #addFragmentEntry(long, long, String, String, String, String,
	 String, String, long, int, int, ServiceContext)}
	 */
	@Deprecated
	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String fragmentEntryKey,
			String name, String css, String html, String js, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, fragmentEntryKey, name, css, html,
			js, status, serviceContext);
	}

	public static com.liferay.fragment.model.FragmentEntry addFragmentEntry(
			long groupId, long fragmentCollectionId, String fragmentEntryKey,
			String name, String css, String html, String js,
			String configuration, long previewFileEntryId, int type, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addFragmentEntry(
			groupId, fragmentCollectionId, fragmentEntryKey, name, css, html,
			js, configuration, previewFileEntryId, type, status,
			serviceContext);
	}

	public static com.liferay.fragment.model.FragmentEntry copyFragmentEntry(
			long groupId, long fragmentEntryId, long fragmentCollectionId,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().copyFragmentEntry(
			groupId, fragmentEntryId, fragmentCollectionId, serviceContext);
	}

	public static void deleteFragmentEntries(long[] fragmentEntriesIds)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteFragmentEntries(fragmentEntriesIds);
	}

	public static com.liferay.fragment.model.FragmentEntry deleteFragmentEntry(
			long fragmentEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteFragmentEntry(fragmentEntryId);
	}

	public static com.liferay.fragment.model.FragmentEntry fetchFragmentEntry(
			long fragmentEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().fetchFragmentEntry(fragmentEntryId);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #getFragmentEntriesCount(Long, Long)}
	 */
	@Deprecated
	public static int getFragmentCollectionsCount(
		long groupId, long fragmentCollectionId) {

		return getService().getFragmentCollectionsCount(
			groupId, fragmentCollectionId);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #getFragmentEntriesCount(Long, Long, Integer)}
	 */
	@Deprecated
	public static int getFragmentCollectionsCount(
		long groupId, long fragmentCollectionId, int status) {

		return getService().getFragmentCollectionsCount(
			groupId, fragmentCollectionId, status);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #getFragmentEntriesCount(Long, Long, String)}
	 */
	@Deprecated
	public static int getFragmentCollectionsCount(
		long groupId, long fragmentCollectionId, String name) {

		return getService().getFragmentCollectionsCount(
			groupId, fragmentCollectionId, name);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #getFragmentEntriesCount(Long, Long, String, Integer)}
	 */
	@Deprecated
	public static int getFragmentCollectionsCount(
		long groupId, long fragmentCollectionId, String name, int status) {

		return getService().getFragmentCollectionsCount(
			groupId, fragmentCollectionId, name, status);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #getFragmentEntriesCount(Long, Long, Integer)}
	 */
	@Deprecated
	public static int getFragmentCollectionsCountByType(
		long groupId, long fragmentCollectionId, int type) {

		return getService().getFragmentCollectionsCountByType(
			groupId, fragmentCollectionId, type);
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntries(long fragmentCollectionId) {

		return getService().getFragmentEntries(fragmentCollectionId);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #getFragmentEntriesByStatus(Long, Long, Integer)}
	 */
	@Deprecated
	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntries(
			long groupId, long fragmentCollectionId, int status) {

		return getService().getFragmentEntries(
			groupId, fragmentCollectionId, status);
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntries(
			long groupId, long fragmentCollectionId, int start, int end) {

		return getService().getFragmentEntries(
			groupId, fragmentCollectionId, start, end);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #getFragmentEntriesByStatus(Long, Long, Integer, Integer, Integer, OrderByComparator)}
	 */
	@Deprecated
	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntries(
			long groupId, long fragmentCollectionId, int status, int start,
			int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.fragment.model.FragmentEntry> orderByComparator) {

		return getService().getFragmentEntries(
			groupId, fragmentCollectionId, status, start, end,
			orderByComparator);
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntries(
			long groupId, long fragmentCollectionId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.fragment.model.FragmentEntry> orderByComparator) {

		return getService().getFragmentEntries(
			groupId, fragmentCollectionId, start, end, orderByComparator);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #getFragmentEntriesByNameAndStatus(Long, Long, String, Integer, Integer, Integer, OrderByComparator)}
	 */
	@Deprecated
	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntries(
			long groupId, long fragmentCollectionId, String name, int status,
			int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.fragment.model.FragmentEntry> orderByComparator) {

		return getService().getFragmentEntries(
			groupId, fragmentCollectionId, name, status, start, end,
			orderByComparator);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #getFragmentEntriesByName(Long, Long, String, Integer, Integer, OrderByComparator)}
	 */
	@Deprecated
	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntries(
			long groupId, long fragmentCollectionId, String name, int start,
			int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.fragment.model.FragmentEntry> orderByComparator) {

		return getService().getFragmentEntries(
			groupId, fragmentCollectionId, name, start, end, orderByComparator);
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntriesByName(
			long groupId, long fragmentCollectionId, String name, int start,
			int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.fragment.model.FragmentEntry> orderByComparator) {

		return getService().getFragmentEntriesByName(
			groupId, fragmentCollectionId, name, start, end, orderByComparator);
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntriesByNameAndStatus(
			long groupId, long fragmentCollectionId, String name, int status,
			int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.fragment.model.FragmentEntry> orderByComparator) {

		return getService().getFragmentEntriesByNameAndStatus(
			groupId, fragmentCollectionId, name, status, start, end,
			orderByComparator);
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntriesByStatus(
			long groupId, long fragmentCollectionId, int status) {

		return getService().getFragmentEntriesByStatus(
			groupId, fragmentCollectionId, status);
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntriesByStatus(
			long groupId, long fragmentCollectionId, int status, int start,
			int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.fragment.model.FragmentEntry> orderByComparator) {

		return getService().getFragmentEntriesByStatus(
			groupId, fragmentCollectionId, status, start, end,
			orderByComparator);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #getFragmentEntriesByTypeAndStatus(Long, Long, Integer, Integer)}
	 */
	@Deprecated
	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntriesByType(
			long groupId, long fragmentCollectionId, int type, int status) {

		return getService().getFragmentEntriesByType(
			groupId, fragmentCollectionId, type, status);
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntriesByType(
			long groupId, long fragmentCollectionId, int type, int start,
			int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.fragment.model.FragmentEntry> orderByComparator) {

		return getService().getFragmentEntriesByType(
			groupId, fragmentCollectionId, type, start, end, orderByComparator);
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntriesByTypeAndStatus(
			long groupId, long fragmentCollectionId, int type, int status) {

		return getService().getFragmentEntriesByTypeAndStatus(
			groupId, fragmentCollectionId, type, status);
	}

	public static java.util.List<com.liferay.fragment.model.FragmentEntry>
		getFragmentEntriesByTypeAndStatus(
			long groupId, long fragmentCollectionId, int type, int status,
			int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.fragment.model.FragmentEntry> orderByComparator) {

		return getService().getFragmentEntriesByTypeAndStatus(
			groupId, fragmentCollectionId, type, status, start, end,
			orderByComparator);
	}

	public static int getFragmentEntriesCount(
		long groupId, long fragmentCollectionId) {

		return getService().getFragmentEntriesCount(
			groupId, fragmentCollectionId);
	}

	public static int getFragmentEntriesCountByName(
		long groupId, long fragmentCollectionId, String name) {

		return getService().getFragmentEntriesCountByName(
			groupId, fragmentCollectionId, name);
	}

	public static int getFragmentEntriesCountByNameAndStatus(
		long groupId, long fragmentCollectionId, String name, int status) {

		return getService().getFragmentEntriesCountByNameAndStatus(
			groupId, fragmentCollectionId, name, status);
	}

	public static int getFragmentEntriesCountByStatus(
		long groupId, long fragmentCollectionId, int status) {

		return getService().getFragmentEntriesCountByStatus(
			groupId, fragmentCollectionId, status);
	}

	public static int getFragmentEntriesCountByType(
		long groupId, long fragmentCollectionId, int type) {

		return getService().getFragmentEntriesCountByType(
			groupId, fragmentCollectionId, type);
	}

	public static int getFragmentEntriesCountByTypeAndStatus(
		long groupId, long fragmentCollectionId, int type, int status) {

		return getService().getFragmentEntriesCountByTypeAndStatus(
			groupId, fragmentCollectionId, type, status);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static String[] getTempFileNames(long groupId, String folderName)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getTempFileNames(groupId, folderName);
	}

	public static com.liferay.fragment.model.FragmentEntry moveFragmentEntry(
			long fragmentEntryId, long fragmentCollectionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().moveFragmentEntry(
			fragmentEntryId, fragmentCollectionId);
	}

	public static com.liferay.fragment.model.FragmentEntry updateFragmentEntry(
			long fragmentEntryId, long previewFileEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateFragmentEntry(
			fragmentEntryId, previewFileEntryId);
	}

	public static com.liferay.fragment.model.FragmentEntry updateFragmentEntry(
			long fragmentEntryId, String name)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateFragmentEntry(fragmentEntryId, name);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #updateFragmentEntry(long, String, String, String,
	 String, String, int)}
	 */
	@Deprecated
	public static com.liferay.fragment.model.FragmentEntry updateFragmentEntry(
			long fragmentEntryId, String name, String css, String html,
			String js, int status)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateFragmentEntry(
			fragmentEntryId, name, css, html, js, status);
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 #updateFragmentEntry(long, String, String, String,
	 String, String, long, int)}
	 */
	@Deprecated
	public static com.liferay.fragment.model.FragmentEntry updateFragmentEntry(
			long fragmentEntryId, String name, String css, String html,
			String js, long previewFileEntryId, int status)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateFragmentEntry(
			fragmentEntryId, name, css, html, js, previewFileEntryId, status);
	}

	public static com.liferay.fragment.model.FragmentEntry updateFragmentEntry(
			long fragmentEntryId, String name, String css, String html,
			String js, String configuration, int status)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateFragmentEntry(
			fragmentEntryId, name, css, html, js, configuration, status);
	}

	public static com.liferay.fragment.model.FragmentEntry updateFragmentEntry(
			long fragmentEntryId, String name, String css, String html,
			String js, String configuration, long previewFileEntryId,
			int status)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateFragmentEntry(
			fragmentEntryId, name, css, html, js, configuration,
			previewFileEntryId, status);
	}

	public static FragmentEntryService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<FragmentEntryService, FragmentEntryService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(FragmentEntryService.class);

		ServiceTracker<FragmentEntryService, FragmentEntryService>
			serviceTracker =
				new ServiceTracker<FragmentEntryService, FragmentEntryService>(
					bundle.getBundleContext(), FragmentEntryService.class,
					null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}