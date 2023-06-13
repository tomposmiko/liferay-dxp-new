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

package com.liferay.mobile.device.rules.service;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the remote service utility for MDRRuleGroupInstance. This utility wraps
 * <code>com.liferay.mobile.device.rules.service.impl.MDRRuleGroupInstanceServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Edward C. Han
 * @see MDRRuleGroupInstanceService
 * @generated
 */
public class MDRRuleGroupInstanceServiceUtil {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.mobile.device.rules.service.impl.MDRRuleGroupInstanceServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link MDRRuleGroupInstanceServiceUtil} to access the mdr rule group instance remote service. Add custom service methods to <code>com.liferay.mobile.device.rules.service.impl.MDRRuleGroupInstanceServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static com.liferay.mobile.device.rules.model.MDRRuleGroupInstance
			addRuleGroupInstance(
				long groupId, String className, long classPK, long ruleGroupId,
				int priority,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addRuleGroupInstance(
			groupId, className, classPK, ruleGroupId, priority, serviceContext);
	}

	public static com.liferay.mobile.device.rules.model.MDRRuleGroupInstance
			addRuleGroupInstance(
				long groupId, String className, long classPK, long ruleGroupId,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addRuleGroupInstance(
			groupId, className, classPK, ruleGroupId, serviceContext);
	}

	public static void deleteRuleGroupInstance(long ruleGroupInstanceId)
		throws com.liferay.portal.kernel.exception.PortalException {

		getService().deleteRuleGroupInstance(ruleGroupInstanceId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static java.util.List
		<com.liferay.mobile.device.rules.model.MDRRuleGroupInstance>
			getRuleGroupInstances(
				String className, long classPK, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.mobile.device.rules.model.MDRRuleGroupInstance>
						orderByComparator) {

		return getService().getRuleGroupInstances(
			className, classPK, start, end, orderByComparator);
	}

	public static int getRuleGroupInstancesCount(
		String className, long classPK) {

		return getService().getRuleGroupInstancesCount(className, classPK);
	}

	public static com.liferay.mobile.device.rules.model.MDRRuleGroupInstance
			updateRuleGroupInstance(long ruleGroupInstanceId, int priority)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateRuleGroupInstance(
			ruleGroupInstanceId, priority);
	}

	public static MDRRuleGroupInstanceService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker
		<MDRRuleGroupInstanceService, MDRRuleGroupInstanceService>
			_serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(
			MDRRuleGroupInstanceService.class);

		ServiceTracker<MDRRuleGroupInstanceService, MDRRuleGroupInstanceService>
			serviceTracker =
				new ServiceTracker
					<MDRRuleGroupInstanceService, MDRRuleGroupInstanceService>(
						bundle.getBundleContext(),
						MDRRuleGroupInstanceService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}

}