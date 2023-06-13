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

package com.liferay.content.dashboard.journal.internal.item.action.provider;

import com.liferay.content.dashboard.item.action.ContentDashboardItemVersionAction;
import com.liferay.content.dashboard.item.action.provider.ContentDashboardItemVersionActionProvider;
import com.liferay.content.dashboard.journal.internal.item.action.CompareVersionsJournalArticleContentDashboardItemVersionAction;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(service = ContentDashboardItemVersionActionProvider.class)
public class
	CompareVersionsJournalArticleContentDashboardItemVersionActionProvider
		implements ContentDashboardItemVersionActionProvider<JournalArticle> {

	@Override
	public ContentDashboardItemVersionAction
		getContentDashboardItemVersionAction(
			JournalArticle journalArticle,
			HttpServletRequest httpServletRequest) {

		if (!isShow(journalArticle, httpServletRequest)) {
			return null;
		}

		JournalArticle latestJournalArticle = null;

		try {
			latestJournalArticle = _journalArticleService.getLatestArticle(
				journalArticle.getResourcePrimKey());
		}
		catch (PortalException portalException) {
			_log.error(portalException);

			return null;
		}

		return new CompareVersionsJournalArticleContentDashboardItemVersionAction(
			httpServletRequest, journalArticle, _language, latestJournalArticle,
			RequestBackedPortletURLFactoryUtil.create(httpServletRequest));
	}

	@Override
	public boolean isShow(
		JournalArticle journalArticle, HttpServletRequest httpServletRequest) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		User user = themeDisplay.getUser();

		int count = _journalArticleService.getArticlesCountByArticleId(
			journalArticle.getGroupId(), journalArticle.getArticleId());

		if ((count > 1) &&
			permissionChecker.isContentReviewer(
				user.getCompanyId(), themeDisplay.getScopeGroupId())) {

			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CompareVersionsJournalArticleContentDashboardItemVersionActionProvider.
			class);

	@Reference
	private JournalArticleService _journalArticleService;

	@Reference
	private Language _language;

	@Reference(
		target = "(model.class.name=com.liferay.journal.model.JournalArticle)"
	)
	private ModelResourcePermission<JournalArticle> _modelResourcePermission;

}