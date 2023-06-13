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

package com.liferay.oauth.client.persistence.service.impl;

import com.liferay.oauth.client.persistence.exception.DuplicateOAuthClientAuthServerException;
import com.liferay.oauth.client.persistence.exception.OAuthClientAuthServerMetadataJSONException;
import com.liferay.oauth.client.persistence.exception.OAuthClientAuthServerTypeException;
import com.liferay.oauth.client.persistence.model.OAuthClientAuthServer;
import com.liferay.oauth.client.persistence.service.base.OAuthClientAuthServerLocalServiceBaseImpl;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Validator;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.as.AuthorizationServerMetadata;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;

import java.util.List;

import net.minidev.json.JSONObject;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Arthur Chan
 */
@Component(
	property = "model.class.name=com.liferay.oauth.client.persistence.model.OAuthClientAuthServer",
	service = AopService.class
)
public class OAuthClientAuthServerLocalServiceImpl
	extends OAuthClientAuthServerLocalServiceBaseImpl {

	@Override
	public OAuthClientAuthServer addOAuthClientAuthServer(
			long userId, String discoveryEndpoint, String metadataJSON,
			String type)
		throws PortalException {

		JSONObject metadataJSONObject = _getMetadataJSONObject(metadataJSON);

		_validateMetadataJSON(metadataJSONObject, type);

		User user = _userLocalService.getUser(userId);

		String issuer = metadataJSONObject.getAsString("issuer");

		_validateIssuer(user.getCompanyId(), issuer);

		OAuthClientAuthServer oAuthClientAuthServer =
			oAuthClientAuthServerPersistence.create(
				counterLocalService.increment());

		oAuthClientAuthServer.setCompanyId(user.getCompanyId());
		oAuthClientAuthServer.setUserId(user.getUserId());
		oAuthClientAuthServer.setUserName(user.getFullName());
		oAuthClientAuthServer.setDiscoveryEndpoint(discoveryEndpoint);
		oAuthClientAuthServer.setIssuer(issuer);
		oAuthClientAuthServer.setMetadataJSON(metadataJSON);
		oAuthClientAuthServer.setType(type);

		oAuthClientAuthServer = oAuthClientAuthServerPersistence.update(
			oAuthClientAuthServer);

		_resourceLocalService.addResources(
			oAuthClientAuthServer.getCompanyId(),
			GroupConstants.DEFAULT_LIVE_GROUP_ID,
			oAuthClientAuthServer.getUserId(),
			OAuthClientAuthServer.class.getName(),
			oAuthClientAuthServer.getOAuthClientAuthServerId(), false, false,
			false);

		return oAuthClientAuthServer;
	}

	@Override
	public OAuthClientAuthServer deleteOAuthClientAuthServer(
			long oAuthClientAuthServerId)
		throws PortalException {

		OAuthClientAuthServer oAuthClientAuthServer =
			oAuthClientAuthServerPersistence.findByPrimaryKey(
				oAuthClientAuthServerId);

		return deleteOAuthClientAuthServer(oAuthClientAuthServer);
	}

	@Override
	public OAuthClientAuthServer deleteOAuthClientAuthServer(
			long companyId, String issuer)
		throws PortalException {

		OAuthClientAuthServer oAuthClientAuthServer =
			oAuthClientAuthServerPersistence.findByC_I(companyId, issuer);

		return deleteOAuthClientAuthServer(oAuthClientAuthServer);
	}

	@Override
	public OAuthClientAuthServer deleteOAuthClientAuthServer(
			OAuthClientAuthServer oAuthClientAuthServer)
		throws PortalException {

		oAuthClientAuthServer = oAuthClientAuthServerPersistence.remove(
			oAuthClientAuthServer);

		_resourceLocalService.deleteResource(
			oAuthClientAuthServer.getCompanyId(),
			OAuthClientAuthServer.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL,
			oAuthClientAuthServer.getOAuthClientAuthServerId());

		return oAuthClientAuthServer;
	}

	@Override
	public OAuthClientAuthServer fetchOAuthClientAuthServer(
		long companyId, String issuer) {

		return oAuthClientAuthServerPersistence.fetchByC_I(companyId, issuer);
	}

	@Override
	public List<OAuthClientAuthServer> getCompanyOAuthClientAuthServers(
		long companyId) {

		return oAuthClientAuthServerPersistence.findByCompanyId(companyId);
	}

	@Override
	public List<OAuthClientAuthServer> getCompanyOAuthClientAuthServers(
		long companyId, int start, int end) {

		return oAuthClientAuthServerPersistence.findByCompanyId(
			companyId, start, end);
	}

	@Override
	public OAuthClientAuthServer getOAuthClientAuthServer(
			long companyId, String issuer)
		throws PortalException {

		return oAuthClientAuthServerPersistence.findByC_I(companyId, issuer);
	}

	@Override
	public List<OAuthClientAuthServer> getTypeOAuthClientAuthServers(
		long companyId, String type) {

		return oAuthClientAuthServerPersistence.findByC_T(companyId, type);
	}

	@Override
	public List<OAuthClientAuthServer> getUserOAuthClientAuthServers(
		long userId) {

		return oAuthClientAuthServerPersistence.findByUserId(userId);
	}

	@Override
	public List<OAuthClientAuthServer> getUserOAuthClientAuthServers(
		long userId, int start, int end) {

		return oAuthClientAuthServerPersistence.findByUserId(
			userId, start, end);
	}

	@Override
	public OAuthClientAuthServer updateOAuthClientAuthServer(
			long oAuthClientAuthServerId, String discoveryEndpoint,
			String metadataJSON, String type)
		throws PortalException {

		JSONObject metadataJSONObject = _getMetadataJSONObject(metadataJSON);

		_validateMetadataJSON(metadataJSONObject, type);

		OAuthClientAuthServer oAuthClientAuthServer =
			oAuthClientAuthServerLocalService.getOAuthClientAuthServer(
				oAuthClientAuthServerId);

		String issuer = metadataJSONObject.getAsString("issuer");

		_validateIssuer(oAuthClientAuthServer.getCompanyId(), issuer);

		oAuthClientAuthServer.setDiscoveryEndpoint(discoveryEndpoint);
		oAuthClientAuthServer.setIssuer(issuer);
		oAuthClientAuthServer.setMetadataJSON(metadataJSON);
		oAuthClientAuthServer.setType(type);

		return oAuthClientAuthServerPersistence.update(oAuthClientAuthServer);
	}

	private JSONObject _getMetadataJSONObject(String metadataJSON)
		throws PortalException {

		try {
			return JSONObjectUtils.parse(metadataJSON);
		}
		catch (ParseException parseException) {
			throw new OAuthClientAuthServerMetadataJSONException(
				parseException);
		}
	}

	private void _validateIssuer(long companyId, String issuer)
		throws PortalException {

		if (oAuthClientAuthServerPersistence.fetchByC_I(companyId, issuer) !=
				null) {

			throw new DuplicateOAuthClientAuthServerException(
				StringBundler.concat(
					"Company ID ", companyId, " and issuer ", issuer));
		}
	}

	private void _validateMetadataJSON(
			JSONObject metadataJSONObject, String type)
		throws PortalException {

		if (Validator.isNull(type)) {
			throw new OAuthClientAuthServerTypeException("Type is null");
		}

		if (type.equals("oauth-authorization-server")) {
			try {
				AuthorizationServerMetadata.parse(metadataJSONObject);
			}
			catch (ParseException parseException) {
				throw new OAuthClientAuthServerMetadataJSONException(
					parseException);
			}
		}
		else if (type.equals("openid-configuration")) {
			try {
				OIDCProviderMetadata.parse(metadataJSONObject);
			}
			catch (ParseException parseException) {
				throw new OAuthClientAuthServerMetadataJSONException(
					parseException);
			}
		}
		else {
			throw new OAuthClientAuthServerTypeException(
				"Invalid type " + type);
		}
	}

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private UserLocalService _userLocalService;

}