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

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for PushNotificationsDevice. This utility wraps
 * <code>com.liferay.push.notifications.service.impl.PushNotificationsDeviceServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Bruno Farache
 * @see PushNotificationsDeviceService
 * @generated
 */
public class PushNotificationsDeviceServiceUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.push.notifications.service.impl.PushNotificationsDeviceServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link PushNotificationsDeviceServiceUtil} to access the push notifications device remote service. Add custom service methods to <code>com.liferay.push.notifications.service.impl.PushNotificationsDeviceServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static com.liferay.push.notifications.model.PushNotificationsDevice
			addPushNotificationsDevice(String token, String platform)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addPushNotificationsDevice(token, platform);
	}

	public static com.liferay.push.notifications.model.PushNotificationsDevice
			deletePushNotificationsDevice(long pushNotificationsDeviceId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePushNotificationsDevice(
			pushNotificationsDeviceId);
	}

	public static com.liferay.push.notifications.model.PushNotificationsDevice
			deletePushNotificationsDevice(String token)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePushNotificationsDevice(token);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static void sendPushNotification(long[] toUserIds, String payload)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().sendPushNotification(toUserIds, payload);
	}

	public static void sendPushNotification(
			String platform, java.util.List<String> tokens, String payload)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().sendPushNotification(platform, tokens, payload);
	}

	public static PushNotificationsDeviceService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<PushNotificationsDeviceService, PushNotificationsDeviceService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			PushNotificationsDeviceService.class);

		ServiceTracker
			<PushNotificationsDeviceService, PushNotificationsDeviceService>
				serviceTracker =
					new ServiceTracker
						<PushNotificationsDeviceService,
						 PushNotificationsDeviceService>(
							 bundle.getBundleContext(),
							 PushNotificationsDeviceService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}