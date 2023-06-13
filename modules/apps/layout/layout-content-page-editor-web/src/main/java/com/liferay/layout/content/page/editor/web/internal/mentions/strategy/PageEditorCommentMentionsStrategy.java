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

package com.liferay.layout.content.page.editor.web.internal.mentions.strategy;

import com.liferay.mentions.constants.MentionsPortletKeys;
import com.liferay.mentions.strategy.MentionsStrategy;
import com.liferay.mentions.util.MentionsUserFinder;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.service.permission.LayoutPermission;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.social.kernel.util.SocialInteractionsConfigurationUtil;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Cristina González
 */
@Component(
	property = "mentions.strategy=pageEditorCommentStrategy",
	service = MentionsStrategy.class
)
public class PageEditorCommentMentionsStrategy implements MentionsStrategy {

	@Override
	public List<User> getUsers(
			long companyId, long groupId, long userId, String query,
			JSONObject jsonObject)
		throws PortalException {

		long plid = jsonObject.getLong("plid");

		return ListUtil.filter(
			_mentionsUserFinder.getUsers(
				companyId, groupId, userId, query,
				SocialInteractionsConfigurationUtil.
					getSocialInteractionsConfiguration(
						companyId, MentionsPortletKeys.MENTIONS)),
			user -> {
				try {
					return _layoutPermission.contains(
						_permissionCheckerFactory.create(user), plid,
						ActionKeys.UPDATE);
				}
				catch (PortalException portalException) {
					_log.error(
						"Unable to check permission for " + user,
						portalException);

					return false;
				}
			});
	}

	@Override
	public List<User> getUsers(
			long companyId, long userId, String query, JSONObject jsonObject)
		throws PortalException {

		return getUsers(
			companyId, GroupConstants.DEFAULT_PARENT_GROUP_ID, userId, query,
			jsonObject);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PageEditorCommentMentionsStrategy.class);

	@Reference
	private LayoutPermission _layoutPermission;

	@Reference
	private MentionsUserFinder _mentionsUserFinder;

	@Reference
	private PermissionCheckerFactory _permissionCheckerFactory;

}