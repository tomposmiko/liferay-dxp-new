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

package com.liferay.message.boards.model;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link MBStatsUser}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see MBStatsUser
 * @generated
 */
public class MBStatsUserWrapper
	extends BaseModelWrapper<MBStatsUser>
	implements MBStatsUser, ModelWrapper<MBStatsUser> {

	public MBStatsUserWrapper(MBStatsUser mbStatsUser) {
		super(mbStatsUser);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("statsUserId", getStatsUserId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("messageCount", getMessageCount());
		attributes.put("lastPostDate", getLastPostDate());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long statsUserId = (Long)attributes.get("statsUserId");

		if (statsUserId != null) {
			setStatsUserId(statsUserId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		Integer messageCount = (Integer)attributes.get("messageCount");

		if (messageCount != null) {
			setMessageCount(messageCount);
		}

		Date lastPostDate = (Date)attributes.get("lastPostDate");

		if (lastPostDate != null) {
			setLastPostDate(lastPostDate);
		}
	}

	/**
	 * Returns the company ID of this message boards stats user.
	 *
	 * @return the company ID of this message boards stats user
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the group ID of this message boards stats user.
	 *
	 * @return the group ID of this message boards stats user
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the last post date of this message boards stats user.
	 *
	 * @return the last post date of this message boards stats user
	 */
	@Override
	public Date getLastPostDate() {
		return model.getLastPostDate();
	}

	/**
	 * Returns the message count of this message boards stats user.
	 *
	 * @return the message count of this message boards stats user
	 */
	@Override
	public int getMessageCount() {
		return model.getMessageCount();
	}

	/**
	 * Returns the primary key of this message boards stats user.
	 *
	 * @return the primary key of this message boards stats user
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the stats user ID of this message boards stats user.
	 *
	 * @return the stats user ID of this message boards stats user
	 */
	@Override
	public long getStatsUserId() {
		return model.getStatsUserId();
	}

	/**
	 * Returns the stats user uuid of this message boards stats user.
	 *
	 * @return the stats user uuid of this message boards stats user
	 */
	@Override
	public String getStatsUserUuid() {
		return model.getStatsUserUuid();
	}

	/**
	 * Returns the user ID of this message boards stats user.
	 *
	 * @return the user ID of this message boards stats user
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user uuid of this message boards stats user.
	 *
	 * @return the user uuid of this message boards stats user
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a message boards stats user model instance should use the <code>MBStatsUser</code> interface instead.
	 */
	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the company ID of this message boards stats user.
	 *
	 * @param companyId the company ID of this message boards stats user
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the group ID of this message boards stats user.
	 *
	 * @param groupId the group ID of this message boards stats user
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the last post date of this message boards stats user.
	 *
	 * @param lastPostDate the last post date of this message boards stats user
	 */
	@Override
	public void setLastPostDate(Date lastPostDate) {
		model.setLastPostDate(lastPostDate);
	}

	/**
	 * Sets the message count of this message boards stats user.
	 *
	 * @param messageCount the message count of this message boards stats user
	 */
	@Override
	public void setMessageCount(int messageCount) {
		model.setMessageCount(messageCount);
	}

	/**
	 * Sets the primary key of this message boards stats user.
	 *
	 * @param primaryKey the primary key of this message boards stats user
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the stats user ID of this message boards stats user.
	 *
	 * @param statsUserId the stats user ID of this message boards stats user
	 */
	@Override
	public void setStatsUserId(long statsUserId) {
		model.setStatsUserId(statsUserId);
	}

	/**
	 * Sets the stats user uuid of this message boards stats user.
	 *
	 * @param statsUserUuid the stats user uuid of this message boards stats user
	 */
	@Override
	public void setStatsUserUuid(String statsUserUuid) {
		model.setStatsUserUuid(statsUserUuid);
	}

	/**
	 * Sets the user ID of this message boards stats user.
	 *
	 * @param userId the user ID of this message boards stats user
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user uuid of this message boards stats user.
	 *
	 * @param userUuid the user uuid of this message boards stats user
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	@Override
	protected MBStatsUserWrapper wrap(MBStatsUser mbStatsUser) {
		return new MBStatsUserWrapper(mbStatsUser);
	}

}