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

package com.liferay.analytics.message.storage.model.impl;

import com.liferay.analytics.message.storage.model.AnalyticsAssociation;
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
 * The cache model class for representing AnalyticsAssociation in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class AnalyticsAssociationCacheModel
	implements CacheModel<AnalyticsAssociation>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof AnalyticsAssociationCacheModel)) {
			return false;
		}

		AnalyticsAssociationCacheModel analyticsAssociationCacheModel =
			(AnalyticsAssociationCacheModel)object;

		if ((analyticsAssociationId ==
				analyticsAssociationCacheModel.analyticsAssociationId) &&
			(mvccVersion == analyticsAssociationCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, analyticsAssociationId);

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
		StringBundler sb = new StringBundler(21);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", analyticsAssociationId=");
		sb.append(analyticsAssociationId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", associationClassName=");
		sb.append(associationClassName);
		sb.append(", associationClassPK=");
		sb.append(associationClassPK);
		sb.append(", className=");
		sb.append(className);
		sb.append(", classPK=");
		sb.append(classPK);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public AnalyticsAssociation toEntityModel() {
		AnalyticsAssociationImpl analyticsAssociationImpl =
			new AnalyticsAssociationImpl();

		analyticsAssociationImpl.setMvccVersion(mvccVersion);
		analyticsAssociationImpl.setAnalyticsAssociationId(
			analyticsAssociationId);
		analyticsAssociationImpl.setCompanyId(companyId);

		if (createDate == Long.MIN_VALUE) {
			analyticsAssociationImpl.setCreateDate(null);
		}
		else {
			analyticsAssociationImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			analyticsAssociationImpl.setModifiedDate(null);
		}
		else {
			analyticsAssociationImpl.setModifiedDate(new Date(modifiedDate));
		}

		analyticsAssociationImpl.setUserId(userId);

		if (associationClassName == null) {
			analyticsAssociationImpl.setAssociationClassName("");
		}
		else {
			analyticsAssociationImpl.setAssociationClassName(
				associationClassName);
		}

		analyticsAssociationImpl.setAssociationClassPK(associationClassPK);

		if (className == null) {
			analyticsAssociationImpl.setClassName("");
		}
		else {
			analyticsAssociationImpl.setClassName(className);
		}

		analyticsAssociationImpl.setClassPK(classPK);

		analyticsAssociationImpl.resetOriginalValues();

		return analyticsAssociationImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();

		analyticsAssociationId = objectInput.readLong();

		companyId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		userId = objectInput.readLong();
		associationClassName = objectInput.readUTF();

		associationClassPK = objectInput.readLong();
		className = objectInput.readUTF();

		classPK = objectInput.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(analyticsAssociationId);

		objectOutput.writeLong(companyId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeLong(userId);

		if (associationClassName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(associationClassName);
		}

		objectOutput.writeLong(associationClassPK);

		if (className == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(className);
		}

		objectOutput.writeLong(classPK);
	}

	public long mvccVersion;
	public long analyticsAssociationId;
	public long companyId;
	public long createDate;
	public long modifiedDate;
	public long userId;
	public String associationClassName;
	public long associationClassPK;
	public String className;
	public long classPK;

}