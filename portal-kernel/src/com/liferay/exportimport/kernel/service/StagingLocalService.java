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

package com.liferay.exportimport.kernel.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.exportimport.kernel.lar.MissingReferences;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.BaseLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Transactional;

import java.util.Map;

import javax.portlet.PortletRequest;

/**
 * Provides the local service interface for Staging. Methods of this
 * service will not have security checks based on the propagated JAAS
 * credentials because this service can only be accessed from within the same
 * VM.
 *
 * @author Brian Wing Shun Chan
 * @see StagingLocalServiceUtil
 * @see com.liferay.portlet.exportimport.service.base.StagingLocalServiceBaseImpl
 * @see com.liferay.portlet.exportimport.service.impl.StagingLocalServiceImpl
 * @generated
 */
@ProviderType
@Transactional(isolation = Isolation.PORTAL, rollbackFor =  {
	PortalException.class, SystemException.class})
public interface StagingLocalService extends BaseLocalService {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link StagingLocalServiceUtil} to access the staging local service. Add custom service methods to {@link com.liferay.portlet.exportimport.service.impl.StagingLocalServiceImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public void checkDefaultLayoutSetBranches(long userId, Group liveGroup,
		boolean branchingPublic, boolean branchingPrivate, boolean remote,
		ServiceContext serviceContext) throws PortalException;

	public void cleanUpStagingRequest(long stagingRequestId)
		throws PortalException;

	public long createStagingRequest(long userId, long groupId, String checksum)
		throws PortalException;

	public void disableStaging(Group liveGroup, ServiceContext serviceContext)
		throws PortalException;

	public void disableStaging(PortletRequest portletRequest, Group liveGroup,
		ServiceContext serviceContext) throws PortalException;

	public void enableLocalStaging(long userId, Group liveGroup,
		boolean branchingPublic, boolean branchingPrivate,
		ServiceContext serviceContext) throws PortalException;

	public void enableRemoteStaging(long userId, Group stagingGroup,
		boolean branchingPublic, boolean branchingPrivate,
		String remoteAddress, int remotePort, String remotePathContext,
		boolean secureConnection, long remoteGroupId,
		ServiceContext serviceContext) throws PortalException;

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public String getOSGiServiceIdentifier();

	/**
	* @deprecated As of Wilberforce (7.0.x), with no direct replacement
	*/
	@Deprecated
	public MissingReferences publishStagingRequest(long userId,
		long stagingRequestId, boolean privateLayout,
		Map<String, String[]> parameterMap) throws PortalException;

	public MissingReferences publishStagingRequest(long userId,
		long stagingRequestId,
		ExportImportConfiguration exportImportConfiguration)
		throws PortalException;

	public void updateStagingRequest(long userId, long stagingRequestId,
		String fileName, byte[] bytes) throws PortalException;

	/**
	* @deprecated As of Wilberforce (7.0.x), replaced by {@link
	#publishStagingRequest(long, long, boolean, Map)}
	*/
	@Deprecated
	public MissingReferences validateStagingRequest(long userId,
		long stagingRequestId, boolean privateLayout,
		Map<String, String[]> parameterMap);
}