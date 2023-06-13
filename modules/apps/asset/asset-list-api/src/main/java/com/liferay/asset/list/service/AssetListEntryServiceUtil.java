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

package com.liferay.asset.list.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for AssetListEntry. This utility wraps
 * <code>com.liferay.asset.list.service.impl.AssetListEntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see AssetListEntryService
 * @generated
 */
public class AssetListEntryServiceUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.asset.list.service.impl.AssetListEntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link AssetListEntryServiceUtil} to access the asset list entry remote service. Add custom service methods to <code>com.liferay.asset.list.service.impl.AssetListEntryServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static void addAssetEntrySelection(
			long assetListEntryId, long assetEntryId, long segmentsEntryId,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().addAssetEntrySelection(
			assetListEntryId, assetEntryId, segmentsEntryId, serviceContext);
	}

	public static void addAssetEntrySelections(
			long assetListEntryId, long[] assetEntryIds, long segmentsEntryId,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().addAssetEntrySelections(
			assetListEntryId, assetEntryIds, segmentsEntryId, serviceContext);
	}

	public static com.liferay.asset.list.model.AssetListEntry addAssetListEntry(
			long groupId, String title, int type,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addAssetListEntry(
			groupId, title, type, serviceContext);
	}

	public static com.liferay.asset.list.model.AssetListEntry
			addDynamicAssetListEntry(
				long userId, long groupId, String title, String typeSettings,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addDynamicAssetListEntry(
			userId, groupId, title, typeSettings, serviceContext);
	}

	public static com.liferay.asset.list.model.AssetListEntry
			addManualAssetListEntry(
				long userId, long groupId, String title, long[] assetEntryIds,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addManualAssetListEntry(
			userId, groupId, title, assetEntryIds, serviceContext);
	}

	public static void deleteAssetEntrySelection(
			long assetListEntryId, long segmentsEntryId, int position)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteAssetEntrySelection(
			assetListEntryId, segmentsEntryId, position);
	}

	public static void deleteAssetListEntries(long[] assetListEntriesIds)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteAssetListEntries(assetListEntriesIds);
	}

	public static com.liferay.asset.list.model.AssetListEntry
			deleteAssetListEntry(long assetListEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteAssetListEntry(assetListEntryId);
	}

	public static void deleteAssetListEntry(
			long assetListEntryId, long segmentsEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteAssetListEntry(assetListEntryId, segmentsEntryId);
	}

	public static com.liferay.asset.list.model.AssetListEntry
			fetchAssetListEntry(long assetListEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().fetchAssetListEntry(assetListEntryId);
	}

	public static java.util.List<com.liferay.asset.list.model.AssetListEntry>
		getAssetListEntries(
			long groupId, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.asset.list.model.AssetListEntry>
					orderByComparator) {

		return getService().getAssetListEntries(
			groupId, start, end, orderByComparator);
	}

	public static java.util.List<com.liferay.asset.list.model.AssetListEntry>
		getAssetListEntries(
			long groupId, String title, int start, int end,
			com.liferay.portal.kernel.util.OrderByComparator
				<com.liferay.asset.list.model.AssetListEntry>
					orderByComparator) {

		return getService().getAssetListEntries(
			groupId, title, start, end, orderByComparator);
	}

	public static int getAssetListEntriesCount(long groupId) {
		return getService().getAssetListEntriesCount(groupId);
	}

	public static int getAssetListEntriesCount(long groupId, String title) {
		return getService().getAssetListEntriesCount(groupId, title);
	}

	public static com.liferay.asset.list.model.AssetListEntry getAssetListEntry(
			long assetListEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAssetListEntry(assetListEntryId);
	}

	public static com.liferay.asset.list.model.AssetListEntry getAssetListEntry(
			long groupId, String assetListEntryKey)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAssetListEntry(groupId, assetListEntryKey);
	}

	public static com.liferay.asset.list.model.AssetListEntry
			getAssetListEntryByUuidAndGroupId(String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getAssetListEntryByUuidAndGroupId(uuid, groupId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static void moveAssetEntrySelection(
			long assetListEntryId, long segmentsEntryId, int position,
			int newPosition)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().moveAssetEntrySelection(
			assetListEntryId, segmentsEntryId, position, newPosition);
	}

	public static void updateAssetListEntry(
			long assetListEntryId, long segmentsEntryId, String typeSettings,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().updateAssetListEntry(
			assetListEntryId, segmentsEntryId, typeSettings, serviceContext);
	}

	public static com.liferay.asset.list.model.AssetListEntry
			updateAssetListEntry(long assetListEntryId, String title)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateAssetListEntry(assetListEntryId, title);
	}

	public static void updateAssetListEntryTypeSettings(
			long assetListEntryId, long segmentsEntryId, String typeSettings)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().updateAssetListEntryTypeSettings(
			assetListEntryId, segmentsEntryId, typeSettings);
	}

	public static AssetListEntryService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<AssetListEntryService, AssetListEntryService>
		_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(AssetListEntryService.class);

		ServiceTracker<AssetListEntryService, AssetListEntryService>
			serviceTracker =
				new ServiceTracker
					<AssetListEntryService, AssetListEntryService>(
						bundle.getBundleContext(), AssetListEntryService.class,
						null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}