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

package com.liferay.remote.app.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link RemoteAppEntryService}.
 *
 * @author Brian Wing Shun Chan
 * @see RemoteAppEntryService
 * @generated
 */
public class RemoteAppEntryServiceWrapper
	implements RemoteAppEntryService, ServiceWrapper<RemoteAppEntryService> {

	public RemoteAppEntryServiceWrapper(
		RemoteAppEntryService remoteAppEntryService) {

		_remoteAppEntryService = remoteAppEntryService;
	}

	@Override
	public com.liferay.remote.app.model.RemoteAppEntry
			addCustomElementRemoteAppEntry(
				String customElementCSSURLs,
				String customElementHTMLElementName, String customElementURLs,
				boolean instanceable,
				java.util.Map<java.util.Locale, String> nameMap,
				String portletCategoryName, String properties)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _remoteAppEntryService.addCustomElementRemoteAppEntry(
			customElementCSSURLs, customElementHTMLElementName,
			customElementURLs, instanceable, nameMap, portletCategoryName,
			properties);
	}

	@Override
	public com.liferay.remote.app.model.RemoteAppEntry addIFrameRemoteAppEntry(
			String iFrameURL, boolean instanceable,
			java.util.Map<java.util.Locale, String> nameMap,
			String portletCategoryName, String properties)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _remoteAppEntryService.addIFrameRemoteAppEntry(
			iFrameURL, instanceable, nameMap, portletCategoryName, properties);
	}

	@Override
	public com.liferay.remote.app.model.RemoteAppEntry deleteRemoteAppEntry(
			long remoteAppEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _remoteAppEntryService.deleteRemoteAppEntry(remoteAppEntryId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _remoteAppEntryService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.remote.app.model.RemoteAppEntry getRemoteAppEntry(
			long remoteAppEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _remoteAppEntryService.getRemoteAppEntry(remoteAppEntryId);
	}

	@Override
	public com.liferay.remote.app.model.RemoteAppEntry
			updateCustomElementRemoteAppEntry(
				long remoteAppEntryId, String customElementCSSURLs,
				String customElementHTMLElementName, String customElementURLs,
				java.util.Map<java.util.Locale, String> nameMap,
				String portletCategoryName, String properties)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _remoteAppEntryService.updateCustomElementRemoteAppEntry(
			remoteAppEntryId, customElementCSSURLs,
			customElementHTMLElementName, customElementURLs, nameMap,
			portletCategoryName, properties);
	}

	@Override
	public com.liferay.remote.app.model.RemoteAppEntry
			updateIFrameRemoteAppEntry(
				long remoteAppEntryId, String iFrameURL,
				java.util.Map<java.util.Locale, String> nameMap,
				String portletCategoryName, String properties)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _remoteAppEntryService.updateIFrameRemoteAppEntry(
			remoteAppEntryId, iFrameURL, nameMap, portletCategoryName,
			properties);
	}

	@Override
	public RemoteAppEntryService getWrappedService() {
		return _remoteAppEntryService;
	}

	@Override
	public void setWrappedService(RemoteAppEntryService remoteAppEntryService) {
		_remoteAppEntryService = remoteAppEntryService;
	}

	private RemoteAppEntryService _remoteAppEntryService;

}