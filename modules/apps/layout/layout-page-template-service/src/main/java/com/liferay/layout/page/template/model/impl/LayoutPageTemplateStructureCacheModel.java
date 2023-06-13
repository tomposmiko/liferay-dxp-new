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

package com.liferay.layout.page.template.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;

import com.liferay.portal.kernel.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing LayoutPageTemplateStructure in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see LayoutPageTemplateStructure
 * @generated
 */
@ProviderType
public class LayoutPageTemplateStructureCacheModel implements CacheModel<LayoutPageTemplateStructure>,
	Externalizable {
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof LayoutPageTemplateStructureCacheModel)) {
			return false;
		}

		LayoutPageTemplateStructureCacheModel layoutPageTemplateStructureCacheModel =
			(LayoutPageTemplateStructureCacheModel)obj;

		if (layoutPageTemplateStructureId == layoutPageTemplateStructureCacheModel.layoutPageTemplateStructureId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, layoutPageTemplateStructureId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(23);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", layoutPageTemplateStructureId=");
		sb.append(layoutPageTemplateStructureId);
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
		sb.append(", classNameId=");
		sb.append(classNameId);
		sb.append(", classPK=");
		sb.append(classPK);
		sb.append(", data=");
		sb.append(data);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public LayoutPageTemplateStructure toEntityModel() {
		LayoutPageTemplateStructureImpl layoutPageTemplateStructureImpl = new LayoutPageTemplateStructureImpl();

		if (uuid == null) {
			layoutPageTemplateStructureImpl.setUuid("");
		}
		else {
			layoutPageTemplateStructureImpl.setUuid(uuid);
		}

		layoutPageTemplateStructureImpl.setLayoutPageTemplateStructureId(layoutPageTemplateStructureId);
		layoutPageTemplateStructureImpl.setGroupId(groupId);
		layoutPageTemplateStructureImpl.setCompanyId(companyId);
		layoutPageTemplateStructureImpl.setUserId(userId);

		if (userName == null) {
			layoutPageTemplateStructureImpl.setUserName("");
		}
		else {
			layoutPageTemplateStructureImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			layoutPageTemplateStructureImpl.setCreateDate(null);
		}
		else {
			layoutPageTemplateStructureImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			layoutPageTemplateStructureImpl.setModifiedDate(null);
		}
		else {
			layoutPageTemplateStructureImpl.setModifiedDate(new Date(
					modifiedDate));
		}

		layoutPageTemplateStructureImpl.setClassNameId(classNameId);
		layoutPageTemplateStructureImpl.setClassPK(classPK);

		if (data == null) {
			layoutPageTemplateStructureImpl.setData("");
		}
		else {
			layoutPageTemplateStructureImpl.setData(data);
		}

		layoutPageTemplateStructureImpl.resetOriginalValues();

		return layoutPageTemplateStructureImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();

		layoutPageTemplateStructureId = objectInput.readLong();

		groupId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		classNameId = objectInput.readLong();

		classPK = objectInput.readLong();
		data = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(layoutPageTemplateStructureId);

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

		objectOutput.writeLong(classNameId);

		objectOutput.writeLong(classPK);

		if (data == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(data);
		}
	}

	public String uuid;
	public long layoutPageTemplateStructureId;
	public long groupId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long classNameId;
	public long classPK;
	public String data;
}