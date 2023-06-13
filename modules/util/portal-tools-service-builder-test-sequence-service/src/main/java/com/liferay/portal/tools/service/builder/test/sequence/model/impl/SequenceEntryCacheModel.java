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

package com.liferay.portal.tools.service.builder.test.sequence.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.tools.service.builder.test.sequence.model.SequenceEntry;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The cache model class for representing SequenceEntry in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class SequenceEntryCacheModel
	implements CacheModel<SequenceEntry>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof SequenceEntryCacheModel)) {
			return false;
		}

		SequenceEntryCacheModel sequenceEntryCacheModel =
			(SequenceEntryCacheModel)object;

		if ((sequenceEntryId == sequenceEntryCacheModel.sequenceEntryId) &&
			(mvccVersion == sequenceEntryCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, sequenceEntryId);

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
		StringBundler sb = new StringBundler(9);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", uuid=");
		sb.append(uuid);
		sb.append(", sequenceEntryId=");
		sb.append(sequenceEntryId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public SequenceEntry toEntityModel() {
		SequenceEntryImpl sequenceEntryImpl = new SequenceEntryImpl();

		sequenceEntryImpl.setMvccVersion(mvccVersion);

		if (uuid == null) {
			sequenceEntryImpl.setUuid("");
		}
		else {
			sequenceEntryImpl.setUuid(uuid);
		}

		sequenceEntryImpl.setSequenceEntryId(sequenceEntryId);
		sequenceEntryImpl.setCompanyId(companyId);

		sequenceEntryImpl.resetOriginalValues();

		return sequenceEntryImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();
		uuid = objectInput.readUTF();

		sequenceEntryId = objectInput.readLong();

		companyId = objectInput.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(sequenceEntryId);

		objectOutput.writeLong(companyId);
	}

	public long mvccVersion;
	public String uuid;
	public long sequenceEntryId;
	public long companyId;

}