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

package com.liferay.portal.security.sso.openid.connect.persistence.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.security.sso.openid.connect.persistence.model.OpenIdConnectSession;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing OpenIdConnectSession in entity cache.
 *
 * @author Arthur Chan
 * @generated
 */
public class OpenIdConnectSessionCacheModel
	implements CacheModel<OpenIdConnectSession>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof OpenIdConnectSessionCacheModel)) {
			return false;
		}

		OpenIdConnectSessionCacheModel openIdConnectSessionCacheModel =
			(OpenIdConnectSessionCacheModel)object;

		if ((openIdConnectSessionId ==
				openIdConnectSessionCacheModel.openIdConnectSessionId) &&
			(mvccVersion == openIdConnectSessionCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, openIdConnectSessionId);

		return HashUtil.hash(hashCode, mvccVersion);
	}

	@Override
	public long getMvccVersion() {
		return mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		this.mvccVersion = mvccVersion;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(23);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", openIdConnectSessionId=");
		sb.append(openIdConnectSessionId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", accessToken=");
		sb.append(accessToken);
		sb.append(", accessTokenExpirationDate=");
		sb.append(accessTokenExpirationDate);
		sb.append(", authServerWellKnownURI=");
		sb.append(authServerWellKnownURI);
		sb.append(", clientId=");
		sb.append(clientId);
		sb.append(", idToken=");
		sb.append(idToken);
		sb.append(", refreshToken=");
		sb.append(refreshToken);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public OpenIdConnectSession toEntityModel() {
		OpenIdConnectSessionImpl openIdConnectSessionImpl =
			new OpenIdConnectSessionImpl();

		openIdConnectSessionImpl.setMvccVersion(mvccVersion);
		openIdConnectSessionImpl.setOpenIdConnectSessionId(
			openIdConnectSessionId);
		openIdConnectSessionImpl.setCompanyId(companyId);
		openIdConnectSessionImpl.setUserId(userId);

		if (modifiedDate == Long.MIN_VALUE) {
			openIdConnectSessionImpl.setModifiedDate(null);
		}
		else {
			openIdConnectSessionImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (accessToken == null) {
			openIdConnectSessionImpl.setAccessToken("");
		}
		else {
			openIdConnectSessionImpl.setAccessToken(accessToken);
		}

		if (accessTokenExpirationDate == Long.MIN_VALUE) {
			openIdConnectSessionImpl.setAccessTokenExpirationDate(null);
		}
		else {
			openIdConnectSessionImpl.setAccessTokenExpirationDate(
				new Date(accessTokenExpirationDate));
		}

		if (authServerWellKnownURI == null) {
			openIdConnectSessionImpl.setAuthServerWellKnownURI("");
		}
		else {
			openIdConnectSessionImpl.setAuthServerWellKnownURI(
				authServerWellKnownURI);
		}

		if (clientId == null) {
			openIdConnectSessionImpl.setClientId("");
		}
		else {
			openIdConnectSessionImpl.setClientId(clientId);
		}

		if (idToken == null) {
			openIdConnectSessionImpl.setIdToken("");
		}
		else {
			openIdConnectSessionImpl.setIdToken(idToken);
		}

		if (refreshToken == null) {
			openIdConnectSessionImpl.setRefreshToken("");
		}
		else {
			openIdConnectSessionImpl.setRefreshToken(refreshToken);
		}

		openIdConnectSessionImpl.resetOriginalValues();

		return openIdConnectSessionImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();

		openIdConnectSessionId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		accessToken = objectInput.readUTF();
		accessTokenExpirationDate = objectInput.readLong();
		authServerWellKnownURI = objectInput.readUTF();
		clientId = objectInput.readUTF();
		idToken = objectInput.readUTF();
		refreshToken = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(openIdConnectSessionId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);
		objectOutput.writeLong(modifiedDate);

		if (accessToken == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(accessToken);
		}

		objectOutput.writeLong(accessTokenExpirationDate);

		if (authServerWellKnownURI == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(authServerWellKnownURI);
		}

		if (clientId == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(clientId);
		}

		if (idToken == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(idToken);
		}

		if (refreshToken == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(refreshToken);
		}
	}

	public long mvccVersion;
	public long openIdConnectSessionId;
	public long companyId;
	public long userId;
	public long modifiedDate;
	public String accessToken;
	public long accessTokenExpirationDate;
	public String authServerWellKnownURI;
	public String clientId;
	public String idToken;
	public String refreshToken;

}