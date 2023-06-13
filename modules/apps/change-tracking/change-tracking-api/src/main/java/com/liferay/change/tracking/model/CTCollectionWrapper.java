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

package com.liferay.change.tracking.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link CTCollection}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CTCollection
 * @generated
 */
@ProviderType
public class CTCollectionWrapper extends BaseModelWrapper<CTCollection>
	implements CTCollection, ModelWrapper<CTCollection> {
	public CTCollectionWrapper(CTCollection ctCollection) {
		super(ctCollection);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("ctCollectionId", getCtCollectionId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("name", getName());
		attributes.put("description", getDescription());
		attributes.put("status", getStatus());
		attributes.put("statusByUserId", getStatusByUserId());
		attributes.put("statusByUserName", getStatusByUserName());
		attributes.put("statusDate", getStatusDate());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long ctCollectionId = (Long)attributes.get("ctCollectionId");

		if (ctCollectionId != null) {
			setCtCollectionId(ctCollectionId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		Integer status = (Integer)attributes.get("status");

		if (status != null) {
			setStatus(status);
		}

		Long statusByUserId = (Long)attributes.get("statusByUserId");

		if (statusByUserId != null) {
			setStatusByUserId(statusByUserId);
		}

		String statusByUserName = (String)attributes.get("statusByUserName");

		if (statusByUserName != null) {
			setStatusByUserName(statusByUserName);
		}

		Date statusDate = (Date)attributes.get("statusDate");

		if (statusDate != null) {
			setStatusDate(statusDate);
		}
	}

	/**
	* Returns the company ID of this ct collection.
	*
	* @return the company ID of this ct collection
	*/
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	* Returns the create date of this ct collection.
	*
	* @return the create date of this ct collection
	*/
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	* Returns the ct collection ID of this ct collection.
	*
	* @return the ct collection ID of this ct collection
	*/
	@Override
	public long getCtCollectionId() {
		return model.getCtCollectionId();
	}

	/**
	* Returns the description of this ct collection.
	*
	* @return the description of this ct collection
	*/
	@Override
	public String getDescription() {
		return model.getDescription();
	}

	/**
	* Returns the modified date of this ct collection.
	*
	* @return the modified date of this ct collection
	*/
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	* Returns the name of this ct collection.
	*
	* @return the name of this ct collection
	*/
	@Override
	public String getName() {
		return model.getName();
	}

	/**
	* Returns the primary key of this ct collection.
	*
	* @return the primary key of this ct collection
	*/
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	* Returns the status of this ct collection.
	*
	* @return the status of this ct collection
	*/
	@Override
	public int getStatus() {
		return model.getStatus();
	}

	/**
	* Returns the status by user ID of this ct collection.
	*
	* @return the status by user ID of this ct collection
	*/
	@Override
	public long getStatusByUserId() {
		return model.getStatusByUserId();
	}

	/**
	* Returns the status by user name of this ct collection.
	*
	* @return the status by user name of this ct collection
	*/
	@Override
	public String getStatusByUserName() {
		return model.getStatusByUserName();
	}

	/**
	* Returns the status by user uuid of this ct collection.
	*
	* @return the status by user uuid of this ct collection
	*/
	@Override
	public String getStatusByUserUuid() {
		return model.getStatusByUserUuid();
	}

	/**
	* Returns the status date of this ct collection.
	*
	* @return the status date of this ct collection
	*/
	@Override
	public Date getStatusDate() {
		return model.getStatusDate();
	}

	/**
	* Returns the user ID of this ct collection.
	*
	* @return the user ID of this ct collection
	*/
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	* Returns the user name of this ct collection.
	*
	* @return the user name of this ct collection
	*/
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	* Returns the user uuid of this ct collection.
	*
	* @return the user uuid of this ct collection
	*/
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	* Returns <code>true</code> if this ct collection is approved.
	*
	* @return <code>true</code> if this ct collection is approved; <code>false</code> otherwise
	*/
	@Override
	public boolean isApproved() {
		return model.isApproved();
	}

	/**
	* Returns <code>true</code> if this ct collection is denied.
	*
	* @return <code>true</code> if this ct collection is denied; <code>false</code> otherwise
	*/
	@Override
	public boolean isDenied() {
		return model.isDenied();
	}

	/**
	* Returns <code>true</code> if this ct collection is a draft.
	*
	* @return <code>true</code> if this ct collection is a draft; <code>false</code> otherwise
	*/
	@Override
	public boolean isDraft() {
		return model.isDraft();
	}

	/**
	* Returns <code>true</code> if this ct collection is expired.
	*
	* @return <code>true</code> if this ct collection is expired; <code>false</code> otherwise
	*/
	@Override
	public boolean isExpired() {
		return model.isExpired();
	}

	/**
	* Returns <code>true</code> if this ct collection is inactive.
	*
	* @return <code>true</code> if this ct collection is inactive; <code>false</code> otherwise
	*/
	@Override
	public boolean isInactive() {
		return model.isInactive();
	}

	/**
	* Returns <code>true</code> if this ct collection is incomplete.
	*
	* @return <code>true</code> if this ct collection is incomplete; <code>false</code> otherwise
	*/
	@Override
	public boolean isIncomplete() {
		return model.isIncomplete();
	}

	/**
	* Returns <code>true</code> if this ct collection is pending.
	*
	* @return <code>true</code> if this ct collection is pending; <code>false</code> otherwise
	*/
	@Override
	public boolean isPending() {
		return model.isPending();
	}

	@Override
	public boolean isProduction() {
		return model.isProduction();
	}

	/**
	* Returns <code>true</code> if this ct collection is scheduled.
	*
	* @return <code>true</code> if this ct collection is scheduled; <code>false</code> otherwise
	*/
	@Override
	public boolean isScheduled() {
		return model.isScheduled();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	* Sets the company ID of this ct collection.
	*
	* @param companyId the company ID of this ct collection
	*/
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this ct collection.
	*
	* @param createDate the create date of this ct collection
	*/
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	* Sets the ct collection ID of this ct collection.
	*
	* @param ctCollectionId the ct collection ID of this ct collection
	*/
	@Override
	public void setCtCollectionId(long ctCollectionId) {
		model.setCtCollectionId(ctCollectionId);
	}

	/**
	* Sets the description of this ct collection.
	*
	* @param description the description of this ct collection
	*/
	@Override
	public void setDescription(String description) {
		model.setDescription(description);
	}

	/**
	* Sets the modified date of this ct collection.
	*
	* @param modifiedDate the modified date of this ct collection
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this ct collection.
	*
	* @param name the name of this ct collection
	*/
	@Override
	public void setName(String name) {
		model.setName(name);
	}

	/**
	* Sets the primary key of this ct collection.
	*
	* @param primaryKey the primary key of this ct collection
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	* Sets the status of this ct collection.
	*
	* @param status the status of this ct collection
	*/
	@Override
	public void setStatus(int status) {
		model.setStatus(status);
	}

	/**
	* Sets the status by user ID of this ct collection.
	*
	* @param statusByUserId the status by user ID of this ct collection
	*/
	@Override
	public void setStatusByUserId(long statusByUserId) {
		model.setStatusByUserId(statusByUserId);
	}

	/**
	* Sets the status by user name of this ct collection.
	*
	* @param statusByUserName the status by user name of this ct collection
	*/
	@Override
	public void setStatusByUserName(String statusByUserName) {
		model.setStatusByUserName(statusByUserName);
	}

	/**
	* Sets the status by user uuid of this ct collection.
	*
	* @param statusByUserUuid the status by user uuid of this ct collection
	*/
	@Override
	public void setStatusByUserUuid(String statusByUserUuid) {
		model.setStatusByUserUuid(statusByUserUuid);
	}

	/**
	* Sets the status date of this ct collection.
	*
	* @param statusDate the status date of this ct collection
	*/
	@Override
	public void setStatusDate(Date statusDate) {
		model.setStatusDate(statusDate);
	}

	/**
	* Sets the user ID of this ct collection.
	*
	* @param userId the user ID of this ct collection
	*/
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	* Sets the user name of this ct collection.
	*
	* @param userName the user name of this ct collection
	*/
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	* Sets the user uuid of this ct collection.
	*
	* @param userUuid the user uuid of this ct collection
	*/
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	@Override
	protected CTCollectionWrapper wrap(CTCollection ctCollection) {
		return new CTCollectionWrapper(ctCollection);
	}
}