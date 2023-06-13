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

package com.liferay.message.boards.model.impl;

import com.liferay.message.boards.model.MBSuspiciousActivity;
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
 * The cache model class for representing MBSuspiciousActivity in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class MBSuspiciousActivityCacheModel
	implements CacheModel<MBSuspiciousActivity>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof MBSuspiciousActivityCacheModel)) {
			return false;
		}

		MBSuspiciousActivityCacheModel mbSuspiciousActivityCacheModel =
			(MBSuspiciousActivityCacheModel)object;

		if ((suspiciousActivityId ==
				mbSuspiciousActivityCacheModel.suspiciousActivityId) &&
			(mvccVersion == mbSuspiciousActivityCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, suspiciousActivityId);

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
		StringBundler sb = new StringBundler(31);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", ctCollectionId=");
		sb.append(ctCollectionId);
		sb.append(", uuid=");
		sb.append(uuid);
		sb.append(", suspiciousActivityId=");
		sb.append(suspiciousActivityId);
		sb.append(", groupId=");
		sb.append(groupId);
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
		sb.append(", messageId=");
		sb.append(messageId);
		sb.append(", threadId=");
		sb.append(threadId);
		sb.append(", description=");
		sb.append(description);
		sb.append(", type=");
		sb.append(type);
		sb.append(", validated=");
		sb.append(validated);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public MBSuspiciousActivity toEntityModel() {
		MBSuspiciousActivityImpl mbSuspiciousActivityImpl =
			new MBSuspiciousActivityImpl();

		mbSuspiciousActivityImpl.setMvccVersion(mvccVersion);
		mbSuspiciousActivityImpl.setCtCollectionId(ctCollectionId);

		if (uuid == null) {
			mbSuspiciousActivityImpl.setUuid("");
		}
		else {
			mbSuspiciousActivityImpl.setUuid(uuid);
		}

		mbSuspiciousActivityImpl.setSuspiciousActivityId(suspiciousActivityId);
		mbSuspiciousActivityImpl.setGroupId(groupId);
		mbSuspiciousActivityImpl.setCompanyId(companyId);
		mbSuspiciousActivityImpl.setUserId(userId);

		if (userName == null) {
			mbSuspiciousActivityImpl.setUserName("");
		}
		else {
			mbSuspiciousActivityImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			mbSuspiciousActivityImpl.setCreateDate(null);
		}
		else {
			mbSuspiciousActivityImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			mbSuspiciousActivityImpl.setModifiedDate(null);
		}
		else {
			mbSuspiciousActivityImpl.setModifiedDate(new Date(modifiedDate));
		}

		mbSuspiciousActivityImpl.setMessageId(messageId);
		mbSuspiciousActivityImpl.setThreadId(threadId);

		if (description == null) {
			mbSuspiciousActivityImpl.setDescription("");
		}
		else {
			mbSuspiciousActivityImpl.setDescription(description);
		}

		if (type == null) {
			mbSuspiciousActivityImpl.setType("");
		}
		else {
			mbSuspiciousActivityImpl.setType(type);
		}

		mbSuspiciousActivityImpl.setValidated(validated);

		mbSuspiciousActivityImpl.resetOriginalValues();

		return mbSuspiciousActivityImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();

		ctCollectionId = objectInput.readLong();
		uuid = objectInput.readUTF();

		suspiciousActivityId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		messageId = objectInput.readLong();

		threadId = objectInput.readLong();
		description = objectInput.readUTF();
		type = objectInput.readUTF();

		validated = objectInput.readBoolean();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(ctCollectionId);

		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(suspiciousActivityId);

		objectOutput.writeLong(groupId);

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

		objectOutput.writeLong(messageId);

		objectOutput.writeLong(threadId);

		if (description == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(description);
		}

		if (type == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(type);
		}

		objectOutput.writeBoolean(validated);
	}

	public long mvccVersion;
	public long ctCollectionId;
	public String uuid;
	public long suspiciousActivityId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long messageId;
	public long threadId;
	public String description;
	public String type;
	public boolean validated;

}