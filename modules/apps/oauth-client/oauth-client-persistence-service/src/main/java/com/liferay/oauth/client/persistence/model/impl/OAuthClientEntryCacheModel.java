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

package com.liferay.oauth.client.persistence.model.impl;

import com.liferay.oauth.client.persistence.model.OAuthClientEntry;
import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing OAuthClientEntry in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class OAuthClientEntryCacheModel
	implements CacheModel<OAuthClientEntry>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof OAuthClientEntryCacheModel)) {
			return false;
		}

		OAuthClientEntryCacheModel oAuthClientEntryCacheModel =
			(OAuthClientEntryCacheModel)object;

		if ((oAuthClientEntryId ==
				oAuthClientEntryCacheModel.oAuthClientEntryId) &&
			(mvccVersion == oAuthClientEntryCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, oAuthClientEntryId);

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
		StringBundler sb = new StringBundler(25);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", oAuthClientEntryId=");
		sb.append(oAuthClientEntryId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", authRequestParametersJSON=");
		sb.append(authRequestParametersJSON);
		sb.append(", authServerWellKnownURI=");
		sb.append(authServerWellKnownURI);
		sb.append(", clientId=");
		sb.append(clientId);
		sb.append(", infoJSON=");
		sb.append(infoJSON);
		sb.append(", tokenRequestParametersJSON=");
		sb.append(tokenRequestParametersJSON);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public OAuthClientEntry toEntityModel() {
		OAuthClientEntryImpl oAuthClientEntryImpl = new OAuthClientEntryImpl();

		oAuthClientEntryImpl.setMvccVersion(mvccVersion);
		oAuthClientEntryImpl.setOAuthClientEntryId(oAuthClientEntryId);
		oAuthClientEntryImpl.setCompanyId(companyId);
		oAuthClientEntryImpl.setUserId(userId);

		if (userName == null) {
			oAuthClientEntryImpl.setUserName("");
		}
		else {
			oAuthClientEntryImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			oAuthClientEntryImpl.setCreateDate(null);
		}
		else {
			oAuthClientEntryImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			oAuthClientEntryImpl.setModifiedDate(null);
		}
		else {
			oAuthClientEntryImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (authRequestParametersJSON == null) {
			oAuthClientEntryImpl.setAuthRequestParametersJSON("");
		}
		else {
			oAuthClientEntryImpl.setAuthRequestParametersJSON(
				authRequestParametersJSON);
		}

		if (authServerWellKnownURI == null) {
			oAuthClientEntryImpl.setAuthServerWellKnownURI("");
		}
		else {
			oAuthClientEntryImpl.setAuthServerWellKnownURI(
				authServerWellKnownURI);
		}

		if (clientId == null) {
			oAuthClientEntryImpl.setClientId("");
		}
		else {
			oAuthClientEntryImpl.setClientId(clientId);
		}

		if (infoJSON == null) {
			oAuthClientEntryImpl.setInfoJSON("");
		}
		else {
			oAuthClientEntryImpl.setInfoJSON(infoJSON);
		}

		if (tokenRequestParametersJSON == null) {
			oAuthClientEntryImpl.setTokenRequestParametersJSON("");
		}
		else {
			oAuthClientEntryImpl.setTokenRequestParametersJSON(
				tokenRequestParametersJSON);
		}

		oAuthClientEntryImpl.resetOriginalValues();

		return oAuthClientEntryImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput)
		throws ClassNotFoundException, IOException {

		mvccVersion = objectInput.readLong();

		oAuthClientEntryId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		authRequestParametersJSON = objectInput.readUTF();
		authServerWellKnownURI = objectInput.readUTF();
		clientId = objectInput.readUTF();
		infoJSON = (String)objectInput.readObject();
		tokenRequestParametersJSON = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(oAuthClientEntryId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (authRequestParametersJSON == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(authRequestParametersJSON);
		}

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

		if (infoJSON == null) {
			objectOutput.writeObject("");
		}
		else {
			objectOutput.writeObject(infoJSON);
		}

		if (tokenRequestParametersJSON == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(tokenRequestParametersJSON);
		}
	}

	public long mvccVersion;
	public long oAuthClientEntryId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public String authRequestParametersJSON;
	public String authServerWellKnownURI;
	public String clientId;
	public String infoJSON;
	public String tokenRequestParametersJSON;

}