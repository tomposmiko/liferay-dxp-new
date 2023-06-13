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

package com.liferay.asset.list.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.model.StagedGroupedModel;

import java.util.Date;

/**
 * The base model interface for the AssetListEntry service. Represents a row in the &quot;AssetListEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.asset.list.model.impl.AssetListEntryModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.asset.list.model.impl.AssetListEntryImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetListEntry
 * @generated
 */
@ProviderType
public interface AssetListEntryModel extends BaseModel<AssetListEntry>,
	ShardedModel, StagedGroupedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a asset list entry model instance should use the {@link AssetListEntry} interface instead.
	 */

	/**
	 * Returns the primary key of this asset list entry.
	 *
	 * @return the primary key of this asset list entry
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this asset list entry.
	 *
	 * @param primaryKey the primary key of this asset list entry
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this asset list entry.
	 *
	 * @return the uuid of this asset list entry
	 */
	@AutoEscape
	@Override
	public String getUuid();

	/**
	 * Sets the uuid of this asset list entry.
	 *
	 * @param uuid the uuid of this asset list entry
	 */
	@Override
	public void setUuid(String uuid);

	/**
	 * Returns the asset list entry ID of this asset list entry.
	 *
	 * @return the asset list entry ID of this asset list entry
	 */
	public long getAssetListEntryId();

	/**
	 * Sets the asset list entry ID of this asset list entry.
	 *
	 * @param assetListEntryId the asset list entry ID of this asset list entry
	 */
	public void setAssetListEntryId(long assetListEntryId);

	/**
	 * Returns the group ID of this asset list entry.
	 *
	 * @return the group ID of this asset list entry
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this asset list entry.
	 *
	 * @param groupId the group ID of this asset list entry
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this asset list entry.
	 *
	 * @return the company ID of this asset list entry
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this asset list entry.
	 *
	 * @param companyId the company ID of this asset list entry
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this asset list entry.
	 *
	 * @return the user ID of this asset list entry
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this asset list entry.
	 *
	 * @param userId the user ID of this asset list entry
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this asset list entry.
	 *
	 * @return the user uuid of this asset list entry
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this asset list entry.
	 *
	 * @param userUuid the user uuid of this asset list entry
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this asset list entry.
	 *
	 * @return the user name of this asset list entry
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this asset list entry.
	 *
	 * @param userName the user name of this asset list entry
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this asset list entry.
	 *
	 * @return the create date of this asset list entry
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this asset list entry.
	 *
	 * @param createDate the create date of this asset list entry
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this asset list entry.
	 *
	 * @return the modified date of this asset list entry
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this asset list entry.
	 *
	 * @param modifiedDate the modified date of this asset list entry
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the title of this asset list entry.
	 *
	 * @return the title of this asset list entry
	 */
	@AutoEscape
	public String getTitle();

	/**
	 * Sets the title of this asset list entry.
	 *
	 * @param title the title of this asset list entry
	 */
	public void setTitle(String title);

	/**
	 * Returns the type of this asset list entry.
	 *
	 * @return the type of this asset list entry
	 */
	public int getType();

	/**
	 * Sets the type of this asset list entry.
	 *
	 * @param type the type of this asset list entry
	 */
	public void setType(int type);

	/**
	 * Returns the type settings of this asset list entry.
	 *
	 * @return the type settings of this asset list entry
	 */
	@AutoEscape
	public String getTypeSettings();

	/**
	 * Sets the type settings of this asset list entry.
	 *
	 * @param typeSettings the type settings of this asset list entry
	 */
	public void setTypeSettings(String typeSettings);

	/**
	 * Returns the last publish date of this asset list entry.
	 *
	 * @return the last publish date of this asset list entry
	 */
	@Override
	public Date getLastPublishDate();

	/**
	 * Sets the last publish date of this asset list entry.
	 *
	 * @param lastPublishDate the last publish date of this asset list entry
	 */
	@Override
	public void setLastPublishDate(Date lastPublishDate);
}