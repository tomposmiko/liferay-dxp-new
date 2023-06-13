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
 * Provides the local service utility for ERAssetVocabulary. This utility wraps
 * {@link com.liferay.external.reference.service.impl.ERAssetVocabularyLocalServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see ERAssetVocabularyLocalService
 * @see com.liferay.external.reference.service.base.ERAssetVocabularyLocalServiceBaseImpl
 * @see com.liferay.external.reference.service.impl.ERAssetVocabularyLocalServiceImpl
 * @generated
 */
@ProviderType
public class ERAssetVocabularyLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.external.reference.service.impl.ERAssetVocabularyLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.asset.kernel.model.AssetVocabulary addOrUpdateVocabulary(
		String externalReferenceCode, long userId, long groupId, String title,
		java.util.Map<java.util.Locale, String> titleMap,
		java.util.Map<java.util.Locale, String> descriptionMap,
		String settings,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addOrUpdateVocabulary(externalReferenceCode, userId,
			groupId, title, titleMap, descriptionMap, settings, serviceContext);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static ERAssetVocabularyLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<ERAssetVocabularyLocalService, ERAssetVocabularyLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(ERAssetVocabularyLocalService.class);

		ServiceTracker<ERAssetVocabularyLocalService, ERAssetVocabularyLocalService> serviceTracker =
			new ServiceTracker<ERAssetVocabularyLocalService, ERAssetVocabularyLocalService>(bundle.getBundleContext(),
				ERAssetVocabularyLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
}