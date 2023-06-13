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

package com.liferay.commerce.product.service;

import com.liferay.commerce.product.model.CommerceChannelAccountEntryRel;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

/**
 * Provides the remote service utility for CommerceChannelAccountEntryRel. This utility wraps
 * <code>com.liferay.commerce.product.service.impl.CommerceChannelAccountEntryRelServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Marco Leo
 * @see CommerceChannelAccountEntryRelService
 * @generated
 */
public class CommerceChannelAccountEntryRelServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.commerce.product.service.impl.CommerceChannelAccountEntryRelServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static CommerceChannelAccountEntryRel
			addCommerceChannelAccountEntryRel(
				long accountEntryId, String className, long classPK,
				long commerceChannelId, boolean overrideEligibility,
				double priority, int type)
		throws PortalException {

		return getService().addCommerceChannelAccountEntryRel(
			accountEntryId, className, classPK, commerceChannelId,
			overrideEligibility, priority, type);
	}

	public static void deleteCommerceChannelAccountEntryRel(
			long commerceChannelAccountEntryRelId)
		throws PortalException {

		getService().deleteCommerceChannelAccountEntryRel(
			commerceChannelAccountEntryRelId);
	}

	public static CommerceChannelAccountEntryRel
			fetchCommerceChannelAccountEntryRel(
				long commerceChannelAccountEntryRelId)
		throws PortalException {

		return getService().fetchCommerceChannelAccountEntryRel(
			commerceChannelAccountEntryRelId);
	}

	public static CommerceChannelAccountEntryRel
			fetchCommerceChannelAccountEntryRel(
				long accountEntryId, long commerceChannelId, int type)
		throws PortalException {

		return getService().fetchCommerceChannelAccountEntryRel(
			accountEntryId, commerceChannelId, type);
	}

	public static CommerceChannelAccountEntryRel
			getCommerceChannelAccountEntryRel(
				long commerceChannelAccountEntryRelId)
		throws PortalException {

		return getService().getCommerceChannelAccountEntryRel(
			commerceChannelAccountEntryRelId);
	}

	public static List<CommerceChannelAccountEntryRel>
			getCommerceChannelAccountEntryRels(
				long accountEntryId, int type, int start, int end,
				OrderByComparator<CommerceChannelAccountEntryRel>
					orderByComparator)
		throws PortalException {

		return getService().getCommerceChannelAccountEntryRels(
			accountEntryId, type, start, end, orderByComparator);
	}

	public static List<CommerceChannelAccountEntryRel>
		getCommerceChannelAccountEntryRels(
			String className, long classPK, long commerceChannelId, int type) {

		return getService().getCommerceChannelAccountEntryRels(
			className, classPK, commerceChannelId, type);
	}

	public static int getCommerceChannelAccountEntryRelsCount(
			long accountEntryId, int type)
		throws PortalException {

		return getService().getCommerceChannelAccountEntryRelsCount(
			accountEntryId, type);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static CommerceChannelAccountEntryRel
			updateCommerceChannelAccountEntryRel(
				long commerceChannelAccountEntryRelId, long commerceChannelId,
				long classPK, boolean overrideEligibility, double priority)
		throws PortalException {

		return getService().updateCommerceChannelAccountEntryRel(
			commerceChannelAccountEntryRelId, commerceChannelId, classPK,
			overrideEligibility, priority);
	}

	public static CommerceChannelAccountEntryRelService getService() {
		return _service;
	}

	private static volatile CommerceChannelAccountEntryRelService _service;

}