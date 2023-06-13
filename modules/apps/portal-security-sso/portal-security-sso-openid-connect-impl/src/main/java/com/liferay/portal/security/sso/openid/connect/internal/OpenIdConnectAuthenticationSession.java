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

import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.pkce.CodeVerifier;
import com.nimbusds.openid.connect.sdk.Nonce;

import java.io.Serializable;

/**
 * @author Arthur Chan
 */
public class OpenIdConnectAuthenticationSession implements Serializable {

	public OpenIdConnectAuthenticationSession(
		CodeVerifier codeVerifier, Nonce nonce, long oAuthClientEntryId,
		State state) {

		_codeVerifier = codeVerifier;
		_nonce = nonce;
		_oAuthClientEntryId = oAuthClientEntryId;
		_state = state;
	}

	public CodeVerifier getCodeVerifier() {
		return _codeVerifier;
	}

	public Nonce getNonce() {
		return _nonce;
	}

	public long getOAuthClientEntryId() {
		return _oAuthClientEntryId;
	}

	public State getState() {
		return _state;
	}

	private final CodeVerifier _codeVerifier;
	private final Nonce _nonce;
	private final long _oAuthClientEntryId;
	private final State _state;

}