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

package com.liferay.journal.web.internal.portlet.action;

import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.journal.service.JournalFolderService;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	property = {
		"javax.portlet.name=" + JournalPortletKeys.JOURNAL,
		"mvc.command.name=/journal/expire_articles_and_folders"
	},
	service = MVCActionCommand.class
)
public class ExpireArticlesAndFoldersMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long[] expireFolderIds = ParamUtil.getLongValues(
			actionRequest, "rowIdsJournalFolder");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			JournalArticle.class.getName(), actionRequest);

		for (long expireFolderId : expireFolderIds) {
			_expireFolder(
				themeDisplay.getScopeGroupId(), expireFolderId, serviceContext);
		}

		String[] expireArticleIds = ParamUtil.getStringValues(
			actionRequest, "rowIdsJournalArticle");

		for (String expireArticleId : expireArticleIds) {
			ActionUtil.expireArticle(
				actionRequest, HtmlUtil.unescape(expireArticleId));
		}
	}

	private void _expireFolder(
			long groupId, long parentFolderId, ServiceContext serviceContext)
		throws Exception {

		List<JournalFolder> folders = _journalFolderService.getFolders(
			groupId, parentFolderId);

		for (JournalFolder folder : folders) {
			_expireFolder(groupId, folder.getFolderId(), serviceContext);
		}

		List<JournalArticle> articles = _journalArticleService.getArticles(
			groupId, parentFolderId, LocaleUtil.getMostRelevantLocale());

		for (JournalArticle article : articles) {
			_journalArticleService.expireArticle(
				groupId, article.getArticleId(), null, serviceContext);
		}
	}

	@Reference
	private JournalArticleService _journalArticleService;

	@Reference
	private JournalFolderService _journalFolderService;

}