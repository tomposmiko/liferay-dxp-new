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

package com.liferay.counter.kernel.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;

import java.util.List;

/**
 * Provides the local service interface for Counter. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see CounterLocalServiceUtil
 * @see com.liferay.counter.service.base.CounterLocalServiceBaseImpl
 * @see com.liferay.counter.service.impl.CounterLocalServiceImpl
 * @generated
 */
@ProviderType
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
	PortalException.class, SystemException.class})
public interface CounterLocalService extends BaseLocalService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link CounterLocalServiceUtil} to access the counter local service. Add custom service methods to {@link com.liferay.counter.service.impl.CounterLocalServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<String> getNames();

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public String getOSGiServiceIdentifier();

	@Transactional(isolation = Isolation.COUNTER, propagation = Propagation.REQUIRES_NEW)
	public long increment();

	@Transactional(isolation = Isolation.COUNTER, propagation = Propagation.REQUIRES_NEW)
	public long increment(String name);

	@Transactional(isolation = Isolation.COUNTER, propagation = Propagation.REQUIRES_NEW)
	public long increment(String name, int size);

	@Transactional(isolation = Isolation.COUNTER, propagation = Propagation.REQUIRES_NEW)
	public void rename(String oldName, String newName);

	@Transactional(isolation = Isolation.COUNTER, propagation = Propagation.REQUIRES_NEW)
	public void reset(String name);

	@Transactional(isolation = Isolation.COUNTER, propagation = Propagation.REQUIRES_NEW)
	public void reset(String name, long size);
}