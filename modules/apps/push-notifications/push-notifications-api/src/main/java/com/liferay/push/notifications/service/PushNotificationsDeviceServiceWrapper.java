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

package com.liferay.push.notifications.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link PushNotificationsDeviceService}.
 *
 * @author Bruno Farache
 * @see PushNotificationsDeviceService
 * @generated
 */
public class PushNotificationsDeviceServiceWrapper
	implements PushNotificationsDeviceService,
			   ServiceWrapper<PushNotificationsDeviceService> {

	public PushNotificationsDeviceServiceWrapper(
		PushNotificationsDeviceService pushNotificationsDeviceService) {

		_pushNotificationsDeviceService = pushNotificationsDeviceService;
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link PushNotificationsDeviceServiceUtil} to access the push notifications device remote service. Add custom service methods to <code>com.liferay.push.notifications.service.impl.PushNotificationsDeviceServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	@Override
	public com.liferay.push.notifications.model.PushNotificationsDevice
			addPushNotificationsDevice(String token, String platform)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _pushNotificationsDeviceService.addPushNotificationsDevice(
			token, platform);
	}

	@Override
	public com.liferay.push.notifications.model.PushNotificationsDevice
			deletePushNotificationsDevice(long pushNotificationsDeviceId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _pushNotificationsDeviceService.deletePushNotificationsDevice(
			pushNotificationsDeviceId);
	}

	@Override
	public com.liferay.push.notifications.model.PushNotificationsDevice
			deletePushNotificationsDevice(String token)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _pushNotificationsDeviceService.deletePushNotificationsDevice(
			token);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _pushNotificationsDeviceService.getOSGiServiceIdentifier();
	}

	@Override
	public void sendPushNotification(long[] toUserIds, String payload)
		throws com.liferay.portal.kernel.exception.PortalException {

		_pushNotificationsDeviceService.sendPushNotification(
			toUserIds, payload);
	}

	@Override
	public void sendPushNotification(
			String platform, java.util.List<String> tokens, String payload)
		throws com.liferay.portal.kernel.exception.PortalException {

		_pushNotificationsDeviceService.sendPushNotification(
			platform, tokens, payload);
	}

	@Override
	public PushNotificationsDeviceService getWrappedService() {
		return _pushNotificationsDeviceService;
	}

	@Override
	public void setWrappedService(
		PushNotificationsDeviceService pushNotificationsDeviceService) {

		_pushNotificationsDeviceService = pushNotificationsDeviceService;
	}

	private PushNotificationsDeviceService _pushNotificationsDeviceService;

}