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

package com.liferay.asset.display.page.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.AttachedModel;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.model.StagedAuditedModel;

import java.util.Date;

/**
 * The base model interface for the AssetDisplayPageEntry service. Represents a row in the &quot;AssetDisplayPageEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.asset.display.page.model.impl.AssetDisplayPageEntryModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.asset.display.page.model.impl.AssetDisplayPageEntryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetDisplayPageEntry
 * @see com.liferay.asset.display.page.model.impl.AssetDisplayPageEntryImpl
 * @see com.liferay.asset.display.page.model.impl.AssetDisplayPageEntryModelImpl
 * @generated
 */
@ProviderType
public interface AssetDisplayPageEntryModel extends AttachedModel,
	BaseModel<AssetDisplayPageEntry>, GroupedModel, ShardedModel,
	StagedAuditedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a asset display page entry model instance should use the {@link AssetDisplayPageEntry} interface instead.
	 */

	/**
	 * Returns the primary key of this asset display page entry.
	 *
	 * @return the primary key of this asset display page entry
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this asset display page entry.
	 *
	 * @param primaryKey the primary key of this asset display page entry
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this asset display page entry.
	 *
	 * @return the uuid of this asset display page entry
	 */
	@AutoEscape
	@Override
	public String getUuid();

	/**
	 * Sets the uuid of this asset display page entry.
	 *
	 * @param uuid the uuid of this asset display page entry
	 */
	@Override
	public void setUuid(String uuid);

	/**
	 * Returns the asset display page entry ID of this asset display page entry.
	 *
	 * @return the asset display page entry ID of this asset display page entry
	 */
	public long getAssetDisplayPageEntryId();

	/**
	 * Sets the asset display page entry ID of this asset display page entry.
	 *
	 * @param assetDisplayPageEntryId the asset display page entry ID of this asset display page entry
	 */
	public void setAssetDisplayPageEntryId(long assetDisplayPageEntryId);

	/**
	 * Returns the group ID of this asset display page entry.
	 *
	 * @return the group ID of this asset display page entry
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this asset display page entry.
	 *
	 * @param groupId the group ID of this asset display page entry
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this asset display page entry.
	 *
	 * @return the company ID of this asset display page entry
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this asset display page entry.
	 *
	 * @param companyId the company ID of this asset display page entry
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this asset display page entry.
	 *
	 * @return the user ID of this asset display page entry
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this asset display page entry.
	 *
	 * @param userId the user ID of this asset display page entry
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this asset display page entry.
	 *
	 * @return the user uuid of this asset display page entry
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this asset display page entry.
	 *
	 * @param userUuid the user uuid of this asset display page entry
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this asset display page entry.
	 *
	 * @return the user name of this asset display page entry
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this asset display page entry.
	 *
	 * @param userName the user name of this asset display page entry
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this asset display page entry.
	 *
	 * @return the create date of this asset display page entry
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this asset display page entry.
	 *
	 * @param createDate the create date of this asset display page entry
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this asset display page entry.
	 *
	 * @return the modified date of this asset display page entry
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this asset display page entry.
	 *
	 * @param modifiedDate the modified date of this asset display page entry
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the fully qualified class name of this asset display page entry.
	 *
	 * @return the fully qualified class name of this asset display page entry
	 */
	@Override
	public String getClassName();

	public void setClassName(String className);

	/**
	 * Returns the class name ID of this asset display page entry.
	 *
	 * @return the class name ID of this asset display page entry
	 */
	@Override
	public long getClassNameId();

	/**
	 * Sets the class name ID of this asset display page entry.
	 *
	 * @param classNameId the class name ID of this asset display page entry
	 */
	@Override
	public void setClassNameId(long classNameId);

	/**
	 * Returns the class pk of this asset display page entry.
	 *
	 * @return the class pk of this asset display page entry
	 */
	@Override
	public long getClassPK();

	/**
	 * Sets the class pk of this asset display page entry.
	 *
	 * @param classPK the class pk of this asset display page entry
	 */
	@Override
	public void setClassPK(long classPK);

	/**
	 * Returns the layout page template entry ID of this asset display page entry.
	 *
	 * @return the layout page template entry ID of this asset display page entry
	 */
	public long getLayoutPageTemplateEntryId();

	/**
	 * Sets the layout page template entry ID of this asset display page entry.
	 *
	 * @param layoutPageTemplateEntryId the layout page template entry ID of this asset display page entry
	 */
	public void setLayoutPageTemplateEntryId(long layoutPageTemplateEntryId);

	/**
	 * Returns the type of this asset display page entry.
	 *
	 * @return the type of this asset display page entry
	 */
	public int getType();

	/**
	 * Sets the type of this asset display page entry.
	 *
	 * @param type the type of this asset display page entry
	 */
	public void setType(int type);

	/**
	 * Returns the plid of this asset display page entry.
	 *
	 * @return the plid of this asset display page entry
	 */
	public long getPlid();

	/**
	 * Sets the plid of this asset display page entry.
	 *
	 * @param plid the plid of this asset display page entry
	 */
	public void setPlid(long plid);
}