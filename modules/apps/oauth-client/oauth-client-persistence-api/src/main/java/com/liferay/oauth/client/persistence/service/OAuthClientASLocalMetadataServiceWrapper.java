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

package com.liferay.oauth.client.persistence.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link OAuthClientASLocalMetadataService}.
 *
 * @author Brian Wing Shun Chan
 * @see OAuthClientASLocalMetadataService
 * @generated
 */
public class OAuthClientASLocalMetadataServiceWrapper
	implements OAuthClientASLocalMetadataService,
			   ServiceWrapper<OAuthClientASLocalMetadataService> {

	public OAuthClientASLocalMetadataServiceWrapper() {
		this(null);
	}

	public OAuthClientASLocalMetadataServiceWrapper(
		OAuthClientASLocalMetadataService oAuthClientASLocalMetadataService) {

		_oAuthClientASLocalMetadataService = oAuthClientASLocalMetadataService;
	}

	@Override
	public com.liferay.oauth.client.persistence.model.OAuthClientASLocalMetadata
			addOAuthClientASLocalMetadata(
				long userId, String metadataJSON, String wellKnownURISuffix)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _oAuthClientASLocalMetadataService.addOAuthClientASLocalMetadata(
			userId, metadataJSON, wellKnownURISuffix);
	}

	@Override
	public com.liferay.oauth.client.persistence.model.OAuthClientASLocalMetadata
			deleteOAuthClientASLocalMetadata(long oAuthClientASLocalMetadataId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _oAuthClientASLocalMetadataService.
			deleteOAuthClientASLocalMetadata(oAuthClientASLocalMetadataId);
	}

	@Override
	public com.liferay.oauth.client.persistence.model.OAuthClientASLocalMetadata
			deleteOAuthClientASLocalMetadata(String localWellKnownURI)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _oAuthClientASLocalMetadataService.
			deleteOAuthClientASLocalMetadata(localWellKnownURI);
	}

	@Override
	public java.util.List
		<com.liferay.oauth.client.persistence.model.OAuthClientASLocalMetadata>
			getCompanyOAuthClientASLocalMetadata(long companyId) {

		return _oAuthClientASLocalMetadataService.
			getCompanyOAuthClientASLocalMetadata(companyId);
	}

	@Override
	public java.util.List
		<com.liferay.oauth.client.persistence.model.OAuthClientASLocalMetadata>
			getCompanyOAuthClientASLocalMetadata(
				long companyId, int start, int end) {

		return _oAuthClientASLocalMetadataService.
			getCompanyOAuthClientASLocalMetadata(companyId, start, end);
	}

	@Override
	public com.liferay.oauth.client.persistence.model.OAuthClientASLocalMetadata
			getOAuthClientASLocalMetadata(String localWellKnownURI)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _oAuthClientASLocalMetadataService.getOAuthClientASLocalMetadata(
			localWellKnownURI);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _oAuthClientASLocalMetadataService.getOSGiServiceIdentifier();
	}

	@Override
	public java.util.List
		<com.liferay.oauth.client.persistence.model.OAuthClientASLocalMetadata>
			getUserOAuthClientASLocalMetadata(long userId) {

		return _oAuthClientASLocalMetadataService.
			getUserOAuthClientASLocalMetadata(userId);
	}

	@Override
	public java.util.List
		<com.liferay.oauth.client.persistence.model.OAuthClientASLocalMetadata>
			getUserOAuthClientASLocalMetadata(long userId, int start, int end) {

		return _oAuthClientASLocalMetadataService.
			getUserOAuthClientASLocalMetadata(userId, start, end);
	}

	@Override
	public com.liferay.oauth.client.persistence.model.OAuthClientASLocalMetadata
			updateOAuthClientASLocalMetadata(
				long oAuthClientASLocalMetadataId, String metadataJSON,
				String wellKnownURISuffix)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _oAuthClientASLocalMetadataService.
			updateOAuthClientASLocalMetadata(
				oAuthClientASLocalMetadataId, metadataJSON, wellKnownURISuffix);
	}

	@Override
	public OAuthClientASLocalMetadataService getWrappedService() {
		return _oAuthClientASLocalMetadataService;
	}

	@Override
	public void setWrappedService(
		OAuthClientASLocalMetadataService oAuthClientASLocalMetadataService) {

		_oAuthClientASLocalMetadataService = oAuthClientASLocalMetadataService;
	}

	private OAuthClientASLocalMetadataService
		_oAuthClientASLocalMetadataService;

}