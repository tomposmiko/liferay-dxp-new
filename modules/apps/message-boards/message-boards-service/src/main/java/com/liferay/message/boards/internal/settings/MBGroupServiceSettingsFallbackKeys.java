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

package com.liferay.message.boards.internal.settings;

import com.liferay.message.boards.constants.MBConstants;
import com.liferay.portal.kernel.settings.FallbackKeys;
import com.liferay.portal.kernel.util.PropsKeys;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Shuyang Zhou
 */
@Component(
	property = "settingsId=" + MBConstants.SERVICE_NAME,
	service = FallbackKeys.class
)
public class MBGroupServiceSettingsFallbackKeys extends FallbackKeys {

	@Activate
	protected void activate() {
		add(
			"allowAnonymousPosting",
			PropsKeys.MESSAGE_BOARDS_ANONYMOUS_POSTING_ENABLED);
		add(
			"emailFromAddress", PropsKeys.MESSAGE_BOARDS_EMAIL_FROM_ADDRESS,
			PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);
		add(
			"emailFromName", PropsKeys.MESSAGE_BOARDS_EMAIL_FROM_NAME,
			PropsKeys.ADMIN_EMAIL_FROM_NAME);
		add("emailHtmlFormat", PropsKeys.MESSAGE_BOARDS_EMAIL_HTML_FORMAT);
		add(
			"emailMessageAddedBody",
			PropsKeys.MESSAGE_BOARDS_EMAIL_MESSAGE_ADDED_BODY);
		add(
			"emailMessageAddedEnabled",
			PropsKeys.MESSAGE_BOARDS_EMAIL_MESSAGE_ADDED_ENABLED);
		add(
			"emailMessageAddedSubject",
			PropsKeys.MESSAGE_BOARDS_EMAIL_MESSAGE_ADDED_SUBJECT);
		add(
			"emailMessageUpdatedBody",
			PropsKeys.MESSAGE_BOARDS_EMAIL_MESSAGE_UPDATED_BODY);
		add(
			"emailMessageUpdatedEnabled",
			PropsKeys.MESSAGE_BOARDS_EMAIL_MESSAGE_UPDATED_ENABLED);
		add(
			"emailMessageUpdatedSubject",
			PropsKeys.MESSAGE_BOARDS_EMAIL_MESSAGE_UPDATED_SUBJECT);
		add("enableFlags", PropsKeys.MESSAGE_BOARDS_FLAGS_ENABLED);
		add("enableRatings", PropsKeys.MESSAGE_BOARDS_RATINGS_ENABLED);
		add("enableRss", PropsKeys.MESSAGE_BOARDS_RSS_ENABLED);
		add("messageFormat", PropsKeys.MESSAGE_BOARDS_MESSAGE_FORMATS_DEFAULT);
		add("priorities", PropsKeys.MESSAGE_BOARDS_THREAD_PRIORITIES);
		add("ranks", PropsKeys.MESSAGE_BOARDS_USER_RANKS);
		add(
			"recentPostsDateOffset",
			PropsKeys.MESSAGE_BOARDS_RECENT_POSTS_DATE_OFFSET);
		add("rssDelta", PropsKeys.SEARCH_CONTAINER_PAGE_DEFAULT_DELTA);
		add("rssDisplayStyle", PropsKeys.RSS_FEED_DISPLAY_STYLE_DEFAULT);
		add("rssFeedType", PropsKeys.RSS_FEED_TYPE_DEFAULT);
		add(
			"subscribeByDefault",
			PropsKeys.MESSAGE_BOARDS_SUBSCRIBE_BY_DEFAULT);
		add(
			"threadAsQuestionByDefault",
			PropsKeys.MESSAGE_BOARDS_THREAD_AS_QUESTION_BY_DEFAULT);
	}

}