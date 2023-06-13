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
import com.liferay.portal.kernel.change.tracking.CTAware;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jsonwebservice.JSONWebService;
import com.liferay.portal.kernel.security.access.control.AccessControlled;
import com.liferay.portal.kernel.service.BaseService;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the remote service interface for CommerceChannelAccountEntryRel. Methods of this
 * service are expected to have security checks based on the propagated JAAS
 * credentials because this service can be accessed remotely.
 *
 * @author Marco Leo
 * @see CommerceChannelAccountEntryRelServiceUtil
 * @generated
 */
@AccessControlled
@CTAware
@JSONWebService
@ProviderType
@Transactional(
	isolation = Isolation.PORTAL,
	rollbackFor = {PortalException.class, SystemException.class}
)
public interface CommerceChannelAccountEntryRelService extends BaseService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add custom service methods to <code>com.liferay.commerce.product.service.impl.CommerceChannelAccountEntryRelServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface. Consume the commerce channel account entry rel remote service via injection or a <code>org.osgi.util.tracker.ServiceTracker</code>. Use {@link CommerceChannelAccountEntryRelServiceUtil} if injection and service tracking are not available.
	 */
	public CommerceChannelAccountEntryRel addCommerceChannelAccountEntryRel(
			long accountEntryId, String className, long classPK,
			long commerceChannelId, boolean overrideEligibility,
			double priority, int type)
		throws PortalException;

	public void deleteCommerceChannelAccountEntryRel(
			long commerceChannelAccountEntryRelId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CommerceChannelAccountEntryRel fetchCommerceChannelAccountEntryRel(
			long commerceChannelAccountEntryRelId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CommerceChannelAccountEntryRel fetchCommerceChannelAccountEntryRel(
			long accountEntryId, long commerceChannelId, int type)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CommerceChannelAccountEntryRel getCommerceChannelAccountEntryRel(
			long commerceChannelAccountEntryRelId)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<CommerceChannelAccountEntryRel>
			getCommerceChannelAccountEntryRels(
				long accountEntryId, int type, int start, int end,
				OrderByComparator<CommerceChannelAccountEntryRel>
					orderByComparator)
		throws PortalException;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<CommerceChannelAccountEntryRel>
		getCommerceChannelAccountEntryRels(
			String className, long classPK, long commerceChannelId, int type);

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public int getCommerceChannelAccountEntryRelsCount(
			long accountEntryId, int type)
		throws PortalException;

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public String getOSGiServiceIdentifier();

	public CommerceChannelAccountEntryRel updateCommerceChannelAccountEntryRel(
			long commerceChannelAccountEntryRelId, long commerceChannelId,
			long classPK, boolean overrideEligibility, double priority)
		throws PortalException;

}