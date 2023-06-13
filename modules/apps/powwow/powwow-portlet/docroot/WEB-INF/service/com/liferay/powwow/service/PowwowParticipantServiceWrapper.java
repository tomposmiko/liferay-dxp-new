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

package com.liferay.powwow.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link PowwowParticipantService}.
 *
 * @author Shinn Lok
 * @see PowwowParticipantService
 * @generated
 */
public class PowwowParticipantServiceWrapper
	implements PowwowParticipantService,
			   ServiceWrapper<PowwowParticipantService> {

	public PowwowParticipantServiceWrapper(
		PowwowParticipantService powwowParticipantService) {

		_powwowParticipantService = powwowParticipantService;
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link PowwowParticipantServiceUtil} to access the powwow participant remote service. Add custom service methods to <code>com.liferay.powwow.service.impl.PowwowParticipantServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	@Override
	public com.liferay.powwow.model.PowwowParticipant deletePowwowParticipant(
			com.liferay.powwow.model.PowwowParticipant powwowParticipant)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _powwowParticipantService.deletePowwowParticipant(
			powwowParticipant);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _powwowParticipantService.getOSGiServiceIdentifier();
	}

	@Override
	public java.util.List<com.liferay.powwow.model.PowwowParticipant>
			getPowwowParticipants(long powwowMeetingId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _powwowParticipantService.getPowwowParticipants(powwowMeetingId);
	}

	@Override
	public int getPowwowParticipantsCount(long powwowMeetingId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _powwowParticipantService.getPowwowParticipantsCount(
			powwowMeetingId);
	}

	@Override
	public com.liferay.powwow.model.PowwowParticipant updatePowwowParticipant(
			long powwowParticipantId, long powwowMeetingId, String name,
			long participantUserId, String emailAddress, int type, int status,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _powwowParticipantService.updatePowwowParticipant(
			powwowParticipantId, powwowMeetingId, name, participantUserId,
			emailAddress, type, status, serviceContext);
	}

	@Override
	public PowwowParticipantService getWrappedService() {
		return _powwowParticipantService;
	}

	@Override
	public void setWrappedService(
		PowwowParticipantService powwowParticipantService) {

		_powwowParticipantService = powwowParticipantService;
	}

	private PowwowParticipantService _powwowParticipantService;

}