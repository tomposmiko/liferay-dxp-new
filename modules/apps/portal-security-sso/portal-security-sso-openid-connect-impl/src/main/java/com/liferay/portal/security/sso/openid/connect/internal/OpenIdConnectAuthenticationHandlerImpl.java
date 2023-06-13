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

package com.liferay.portal.security.sso.openid.connect.internal;

import com.liferay.oauth.client.persistence.model.OAuthClientEntry;
import com.liferay.oauth.client.persistence.service.OAuthClientEntryLocalService;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectAuthenticationHandler;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectServiceException;
import com.liferay.portal.security.sso.openid.connect.constants.OpenIdConnectConstants;
import com.liferay.portal.security.sso.openid.connect.constants.OpenIdConnectWebKeys;
import com.liferay.portal.security.sso.openid.connect.internal.configuration.admin.service.OpenIdConnectProviderManagedServiceFactory;
import com.liferay.portal.security.sso.openid.connect.internal.session.manager.OfflineOpenIdConnectSessionManager;
import com.liferay.portal.security.sso.openid.connect.internal.util.OpenIdConnectRequestParametersUtil;
import com.liferay.portal.security.sso.openid.connect.internal.util.OpenIdConnectTokenRequestUtil;

import com.nimbusds.jwt.JWT;
import com.nimbusds.langtag.LangTag;
import com.nimbusds.langtag.LangTagException;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.nimbusds.openid.connect.sdk.UserInfoErrorResponse;
import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import com.nimbusds.openid.connect.sdk.UserInfoSuccessResponse;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformation;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.minidev.json.JSONObject;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Thuong Dinh
 * @author Edward C. Han
 * @author Arthur Chan
 */
@Component(immediate = true, service = OpenIdConnectAuthenticationHandler.class)
public class OpenIdConnectAuthenticationHandlerImpl
	implements OpenIdConnectAuthenticationHandler {

	@Override
	public void processAuthenticationResponse(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			UnsafeConsumer<Long, Exception> userIdUnsafeConsumer)
		throws Exception {

		HttpSession httpSession = httpServletRequest.getSession();

		OpenIdConnectAuthenticationSession openIdConnectAuthenticationSession =
			(OpenIdConnectAuthenticationSession)httpSession.getAttribute(
				_OPEN_ID_CONNECT_AUTHENTICATION_SESSION);

		httpSession.removeAttribute(_OPEN_ID_CONNECT_AUTHENTICATION_SESSION);

		if (openIdConnectAuthenticationSession == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"OpenId Connect authentication was not requested or " +
						"removed");
			}

			return;
		}

		AuthenticationSuccessResponse authenticationSuccessResponse =
			_getAuthenticationSuccessResponse(httpServletRequest);

		_validateState(
			openIdConnectAuthenticationSession.getState(),
			authenticationSuccessResponse.getState());

		OAuthClientEntry oAuthClientEntry =
			_oAuthClientEntryLocalService.getOAuthClientEntry(
				openIdConnectAuthenticationSession.getOAuthClientEntryId());

		OIDCClientInformation oidcClientInformation =
			OIDCClientInformation.parse(
				JSONObjectUtils.parse(oAuthClientEntry.getInfoJSON()));

		OIDCProviderMetadata oidcProviderMetadata =
			_authorizationServerMetadataResolver.resolveOIDCProviderMetadata(
				oAuthClientEntry.getAuthServerWellKnownURI());

		OIDCTokens oidcTokens = OpenIdConnectTokenRequestUtil.request(
			authenticationSuccessResponse,
			openIdConnectAuthenticationSession.getNonce(),
			oidcClientInformation, oidcProviderMetadata,
			_getLoginRedirectURI(httpServletRequest),
			oAuthClientEntry.getTokenRequestParametersJSON());

		UserInfo userInfo = _requestUserInfo(
			oidcTokens.getAccessToken(), oidcProviderMetadata);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			httpServletRequest);

		long userId = _openIdConnectUserInfoProcessor.processUserInfo(
			userInfo, _portal.getCompanyId(httpServletRequest),
			String.valueOf(oidcProviderMetadata.getIssuer()),
			serviceContext.getPathMain(), serviceContext.getPortalURL());

		userIdUnsafeConsumer.accept(userId);

		httpSession = httpServletRequest.getSession();

		long openIdConnectSessionId =
			_offlineOpenIdConnectSessionManager.startOpenIdConnectSession(
				oAuthClientEntry.getAuthServerWellKnownURI(),
				String.valueOf(oidcClientInformation.getID()), oidcTokens,
				userId);

		httpSession.setAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_SESSION,
			new OpenIdConnectSessionImpl(
				openIdConnectSessionId,
				oAuthClientEntry.getAuthServerWellKnownURI(),
				openIdConnectAuthenticationSession.getNonce(),
				openIdConnectAuthenticationSession.getState(), userId));
		httpSession.setAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_SESSION_ID,
			openIdConnectSessionId);
	}

	@Override
	public void requestAuthentication(
			long oAuthClientEntryId, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws PortalException {

		HttpSession httpSession = httpServletRequest.getSession();

		Long openIdConnectSessionId = (Long)httpSession.getAttribute(
			OpenIdConnectWebKeys.OPEN_ID_CONNECT_SESSION_ID);

		if (openIdConnectSessionId != null) {
			httpSession.removeAttribute(
				OpenIdConnectWebKeys.OPEN_ID_CONNECT_SESSION_ID);
		}

		OAuthClientEntry oAuthClientEntry =
			_oAuthClientEntryLocalService.getOAuthClientEntry(
				oAuthClientEntryId);

		Map<String, Object> runtimeRequestParameters =
			HashMapBuilder.<String, Object>put(
				"nonce", new Nonce()
			).put(
				"redirect_uri", _getLoginRedirectURI(httpServletRequest)
			).put(
				"state", new State()
			).put(
				"ui_Locals", _getLangTags(httpServletRequest)
			).build();

		try {
			OIDCProviderMetadata oidcProviderMetadata =
				_authorizationServerMetadataResolver.
					resolveOIDCProviderMetadata(
						oAuthClientEntry.getAuthServerWellKnownURI());

			URI authenticationRequestURI = _getAuthenticationRequestURI(
				oidcProviderMetadata.getAuthorizationEndpointURI(),
				oAuthClientEntry.getAuthRequestParametersJSON(),
				oAuthClientEntry.getClientId(), runtimeRequestParameters);

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Authentication request query: " +
						authenticationRequestURI.getQuery());
			}

			httpServletResponse.sendRedirect(
				authenticationRequestURI.toString());

			httpSession.setAttribute(
				_OPEN_ID_CONNECT_AUTHENTICATION_SESSION,
				new OpenIdConnectAuthenticationSession(
					(Nonce)runtimeRequestParameters.get("nonce"),
					oAuthClientEntryId,
					(State)runtimeRequestParameters.get("state")));
		}
		catch (Exception exception) {
			throw new PortalException(exception);
		}
	}

	@Override
	public void requestAuthentication(
			String openIdConnectProviderName,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws PortalException {

		requestAuthentication(
			_openIdConnectProviderManagedServiceFactory.getOAuthClientEntryId(
				_portal.getCompanyId(httpServletRequest),
				openIdConnectProviderName),
			httpServletRequest, httpServletResponse);
	}

	private URI _getAuthenticationRequestURI(
			URI authenticationEndpointURI,
			String authenticationRequestParametersJSON, String clientId,
			Map<String, Object> runtimeRequestParameters)
		throws Exception {

		JSONObject authenticationRequestParametersJSONObject =
			JSONObjectUtils.parse(authenticationRequestParametersJSON);

		AuthenticationRequest.Builder builder =
			new AuthenticationRequest.Builder(
				OpenIdConnectRequestParametersUtil.getResponseType(
					authenticationRequestParametersJSONObject),
				OpenIdConnectRequestParametersUtil.getScope(
					authenticationRequestParametersJSONObject),
				new ClientID(clientId),
				(URI)runtimeRequestParameters.get("redirect_uri"));

		builder = builder.endpointURI(
			authenticationEndpointURI
		).nonce(
			(Nonce)runtimeRequestParameters.get("nonce")
		).resources(
			OpenIdConnectRequestParametersUtil.getResourceURIs(
				authenticationRequestParametersJSONObject)
		).state(
			(State)runtimeRequestParameters.get("state")
		).uiLocales(
			(List<LangTag>)runtimeRequestParameters.get("ui_locales")
		);

		OpenIdConnectRequestParametersUtil.consumeCustomRequestParameters(
			builder::customParameter,
			authenticationRequestParametersJSONObject);

		return builder.build(
		).toURI();
	}

	private AuthenticationSuccessResponse _getAuthenticationSuccessResponse(
			HttpServletRequest httpServletRequest)
		throws OpenIdConnectServiceException.AuthenticationException {

		StringBuffer requestURL = httpServletRequest.getRequestURL();

		if (Validator.isNotNull(httpServletRequest.getQueryString())) {
			requestURL.append(StringPool.QUESTION);
			requestURL.append(httpServletRequest.getQueryString());
		}

		try {
			URI requestURI = new URI(requestURL.toString());

			AuthenticationResponse authenticationResponse =
				AuthenticationResponseParser.parse(requestURI);

			if (authenticationResponse instanceof AuthenticationErrorResponse) {
				AuthenticationErrorResponse authenticationErrorResponse =
					(AuthenticationErrorResponse)authenticationResponse;

				ErrorObject errorObject =
					authenticationErrorResponse.getErrorObject();

				JSONObject jsonObject = errorObject.toJSONObject();

				throw new OpenIdConnectServiceException.AuthenticationException(
					jsonObject.toString());
			}

			return (AuthenticationSuccessResponse)authenticationResponse;
		}
		catch (ParseException | URISyntaxException exception) {
			throw new OpenIdConnectServiceException.AuthenticationException(
				StringBundler.concat(
					"Unable to process response from ", requestURL.toString(),
					": ", exception.getMessage()),
				exception);
		}
	}

	private List<LangTag> _getLangTags(HttpServletRequest httpServletRequest) {
		Locale locale = _portal.getLocale(httpServletRequest);

		if (locale == null) {
			return null;
		}

		try {
			return Collections.singletonList(new LangTag(locale.getLanguage()));
		}
		catch (LangTagException langTagException) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Unable to create a lang tag with locale " +
						locale.getLanguage(),
					langTagException);
			}

			return null;
		}
	}

	private URI _getLoginRedirectURI(HttpServletRequest httpServletRequest) {
		try {
			return new URI(
				StringBundler.concat(
					_portal.getPortalURL(httpServletRequest),
					_portal.getPathContext(),
					OpenIdConnectConstants.REDIRECT_URL_PATTERN));
		}
		catch (URISyntaxException uriSyntaxException) {
			throw new SystemException(
				"Unable to generate OpenId Connect login redirect URI: " +
					uriSyntaxException.getMessage(),
				uriSyntaxException);
		}
	}

	private UserInfo _requestUserInfo(
			AccessToken accessToken, OIDCProviderMetadata oidcProviderMetadata)
		throws OpenIdConnectServiceException.UserInfoException {

		UserInfoRequest userInfoRequest = new UserInfoRequest(
			oidcProviderMetadata.getUserInfoEndpointURI(),
			(BearerAccessToken)accessToken);

		HTTPRequest httpRequest = userInfoRequest.toHTTPRequest();

		httpRequest.setAccept(
			"text/html, image/gif, image/jpeg, */*; q=0.2, */*; q=0.2");

		try {
			HTTPResponse httpResponse = httpRequest.send();

			UserInfoResponse userInfoResponse = UserInfoResponse.parse(
				httpResponse);

			if (userInfoResponse instanceof UserInfoErrorResponse) {
				UserInfoErrorResponse userInfoErrorResponse =
					(UserInfoErrorResponse)userInfoResponse;

				ErrorObject errorObject =
					userInfoErrorResponse.getErrorObject();

				JSONObject jsonObject = errorObject.toJSONObject();

				throw new OpenIdConnectServiceException.UserInfoException(
					jsonObject.toString());
			}

			UserInfoSuccessResponse userInfoSuccessResponse =
				(UserInfoSuccessResponse)userInfoResponse;

			UserInfo userInfo = userInfoSuccessResponse.getUserInfo();

			if (userInfo != null) {
				return userInfo;
			}

			JWT userInfoJWT = userInfoSuccessResponse.getUserInfoJWT();

			return new UserInfo(userInfoJWT.getJWTClaimsSet());
		}
		catch (IOException ioException) {
			throw new OpenIdConnectServiceException.UserInfoException(
				StringBundler.concat(
					"Unable to get user information from ",
					oidcProviderMetadata.getUserInfoEndpointURI(), ": ",
					ioException.getMessage()),
				ioException);
		}
		catch (java.text.ParseException | ParseException exception) {
			throw new OpenIdConnectServiceException.UserInfoException(
				StringBundler.concat(
					"Unable to parse user information response from ",
					oidcProviderMetadata.getUserInfoEndpointURI(), ": ",
					exception.getMessage()),
				exception);
		}
	}

	private void _validateState(State requestedState, State state)
		throws Exception {

		if (!state.equals(requestedState)) {
			throw new OpenIdConnectServiceException.AuthenticationException(
				StringBundler.concat(
					"Requested value \"", requestedState.getValue(),
					"\" and approved state \"", state.getValue(),
					"\" do not match"));
		}
	}

	private static final String _OPEN_ID_CONNECT_AUTHENTICATION_SESSION =
		OpenIdConnectAuthenticationHandlerImpl.class.getName() +
			"#OPEN_ID_CONNECT_AUTHENTICATION_SESSION";

	private static final Log _log = LogFactoryUtil.getLog(
		OpenIdConnectAuthenticationHandlerImpl.class);

	@Reference
	private AuthorizationServerMetadataResolver
		_authorizationServerMetadataResolver;

	@Reference
	private OAuthClientEntryLocalService _oAuthClientEntryLocalService;

	@Reference
	private OfflineOpenIdConnectSessionManager
		_offlineOpenIdConnectSessionManager;

	@Reference
	private OpenIdConnectProviderManagedServiceFactory
		_openIdConnectProviderManagedServiceFactory;

	@Reference
	private OpenIdConnectUserInfoProcessor _openIdConnectUserInfoProcessor;

	@Reference
	private Portal _portal;

}