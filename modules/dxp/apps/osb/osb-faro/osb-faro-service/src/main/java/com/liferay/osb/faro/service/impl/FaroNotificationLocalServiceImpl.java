/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.service.impl;

import com.liferay.osb.faro.model.FaroNotification;
import com.liferay.osb.faro.service.base.FaroNotificationLocalServiceBaseImpl;
import com.liferay.osb.faro.util.FaroPermissionChecker;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.List;

/**
 * @author Geyson Silva
 */
public class FaroNotificationLocalServiceImpl
	extends FaroNotificationLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public FaroNotification addFaroNotification(
		long userId, long groupId, long ownerId, String scope, String type,
		String subtype) {

		long faroNotificationId = counterLocalService.increment();

		FaroNotification faroNotification = faroNotificationPersistence.create(
			faroNotificationId);

		faroNotification.setGroupId(groupId);
		faroNotification.setUserId(userId);

		long now = System.currentTimeMillis();

		faroNotification.setCreateTime(now);
		faroNotification.setModifiedTime(now);

		faroNotification.setOwnerId(ownerId);
		faroNotification.setScope(scope);
		faroNotification.setRead(false);
		faroNotification.setType(type);
		faroNotification.setSubtype(subtype);

		return faroNotificationPersistence.update(faroNotification);
	}

	@Override
	public void clearDismissedNotifications() {
		List<FaroNotification> faroNotifications =
			faroNotificationPersistence.findByLtCreateTime(_getDateMillis());

		for (FaroNotification faroNotification : faroNotifications) {
			deleteFaroNotifications(
				faroNotification.getGroupId(), faroNotification.getType(),
				faroNotification.getSubtype(), faroNotification.getUserId());
		}
	}

	@Override
	public void deleteFaroNotifications(
		long groupId, String type, String subtype, long userId) {

		faroNotificationPersistence.removeByG_GtC_O_T_S(
			groupId, _getDateMillis(), userId, type, subtype);
	}

	@Override
	public void deleteUnreadFaroNotifications(
		long groupId, String type, String subtype, long userId) {

		faroNotificationPersistence.removeByG_GtC_O_R_T_S(
			groupId, _getDateMillis(), userId, false, type, subtype);
	}

	@Override
	public List<FaroNotification> findFaroNotificationsLast30Days(
		long groupId, String type, long userId) {

		if (FaroPermissionChecker.isGroupAdmin(groupId)) {
			return faroNotificationPersistence.findByG_GtC_O_T(
				groupId, _getDateMillis(), new long[] {groupId, userId}, type);
		}

		return faroNotificationPersistence.findByG_GtC_O_T(
			groupId, _getDateMillis(), userId, type);
	}

	@Override
	public long getFaroNotificationsLast30DaysCount(
		long groupId, String subtype, String type, long userId) {

		return faroNotificationPersistence.countByG_GtC_O_T_S(
			groupId, _getDateMillis(), userId, type, subtype);
	}

	private long _getDateMillis() {
		LocalDate localDate = LocalDate.now();

		localDate = localDate.minusDays(30);

		ZonedDateTime zonedDateTime = localDate.atStartOfDay(
			ZoneId.systemDefault());

		Instant instant = zonedDateTime.toInstant();

		return instant.toEpochMilli();
	}

}