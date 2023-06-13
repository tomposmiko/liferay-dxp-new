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

package com.liferay.client.extension.util.spring.boot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Raymond Augé
 * @author Gregory Amerson
 * @author Brian Wing Shun Chan
 */
public class LiferayOAuth2Util {

	public static String getClientId(
		String externalReferenceCode, String lxcMainDomain,
		String lxcServerProtocol) {

		try {
			String baseURL =
				lxcServerProtocol + "://" + lxcMainDomain +
					"/o/oauth2/application";

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Get client ID from " + baseURL + " using " +
						externalReferenceCode);
			}

			return WebClient.create(
				baseURL
			).get(
			).uri(
				uriBuilder -> uriBuilder.queryParam(
					"externalReferenceCode", externalReferenceCode
				).build()
			).retrieve(
			).bodyToMono(
				ApplicationInfo.class
			).block(
			).client_id;
		}
		catch (Throwable throwable) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to get client ID: " + throwable.getMessage());
			}

			return null;
		}
	}

	public static OAuth2AccessToken getOAuth2AccessToken(
		AuthorizedClientServiceOAuth2AuthorizedClientManager
			authorizedClientServiceOAuth2AuthorizedClientManager,
		String externalReferenceCode) {

		OAuth2AuthorizeRequest.Builder oAuth2AuthorizeRequestBuilder =
			OAuth2AuthorizeRequest.withClientRegistrationId(
				externalReferenceCode
			).principal(
				externalReferenceCode
			);

		OAuth2AuthorizedClient oAuth2AuthorizedClient =
			authorizedClientServiceOAuth2AuthorizedClientManager.authorize(
				oAuth2AuthorizeRequestBuilder.build());

		if (oAuth2AuthorizedClient == null) {
			_log.error("Unable to get OAuth 2 authorized client");

			return null;
		}

		OAuth2AccessToken oAuth2AccessToken =
			oAuth2AuthorizedClient.getAccessToken();

		if (oAuth2AccessToken == null) {
			_log.error("Unable to get OAuth 2 access token");

			return null;
		}

		return oAuth2AccessToken;
	}

	private static final Log _log = LogFactory.getLog(LiferayOAuth2Util.class);

	private static class ApplicationInfo {

		public String client_id;

	}

}