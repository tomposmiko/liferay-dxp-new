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

package com.liferay.message.boards.internal.util;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.SubscriptionSender;

/**
 * @author Roberto Díaz
 */
public class MBDiscussionSubcriptionSender extends SubscriptionSender {

	protected void sendEmailNotification(User user) throws Exception {
		if (PrefsPropsUtil.getBoolean(
				companyId, PropsKeys.DISCUSSION_EMAIL_COMMENTS_ADDED_ENABLED)) {

			super.sendEmailNotification(user);
		}
	}

}