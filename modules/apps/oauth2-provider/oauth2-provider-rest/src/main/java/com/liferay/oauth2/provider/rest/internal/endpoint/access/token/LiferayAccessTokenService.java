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

package com.liferay.oauth2.provider.rest.internal.endpoint.access.token;

import com.liferay.oauth2.provider.rest.internal.endpoint.constants.OAuth2ProviderRestEndpointConstants;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.services.AccessTokenService;

/**
 * @author Tomas Polesovsky
 */
@Path("/token")
public class LiferayAccessTokenService extends AccessTokenService {

	@Override
	protected Client authenticateClientIfNeeded(
		MultivaluedMap<String, String> params) {

		Client client = super.authenticateClientIfNeeded(params);

		Map<String, String> properties = client.getProperties();

		MessageContext messageContext = getMessageContext();

		HttpServletRequest httpServletRequest =
			messageContext.getHttpServletRequest();

		String remoteAddr = httpServletRequest.getRemoteAddr();

		String remoteHost = httpServletRequest.getRemoteHost();

		try {
			InetAddress inetAddress = InetAddress.getByName(remoteAddr);

			remoteHost = inetAddress.getCanonicalHostName();
		}
		catch (UnknownHostException uhe) {
		}

		properties.put(
			OAuth2ProviderRestEndpointConstants.PROPERTY_KEY_CLIENT_REMOTE_ADDR,
			remoteAddr);
		properties.put(
			OAuth2ProviderRestEndpointConstants.PROPERTY_KEY_CLIENT_REMOTE_HOST,
			remoteHost);

		return client;
	}

}