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

import com.liferay.change.tracking.spi.constants.CTTimelineKeys;
import com.liferay.dynamic.data.mapping.exception.TemplateScriptException;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.journal.constants.JournalArticleConstants;
import com.liferay.journal.exception.NoSuchArticleException;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFeed;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalArticleServiceUtil;
import com.liferay.journal.service.JournalFeedServiceUtil;
import com.liferay.journal.web.internal.portlet.JournalPortlet;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.File;

import javax.portlet.ActionRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 */
public class ActionUtil {

	public static void deleteArticle(
			ActionRequest actionRequest, String deleteArticleId)
		throws Exception {

		long groupId = ParamUtil.getLong(actionRequest, "groupId");
		String articleURL = ParamUtil.getString(actionRequest, "articleURL");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			JournalArticle.class.getName(), actionRequest);

		int pos = deleteArticleId.lastIndexOf(JournalPortlet.VERSION_SEPARATOR);

		if (pos == -1) {
			JournalArticleServiceUtil.deleteArticle(
				groupId, deleteArticleId, articleURL, serviceContext);
		}
		else {
			String articleId = deleteArticleId.substring(0, pos);
			double version = GetterUtil.getDouble(
				deleteArticleId.substring(
					pos + JournalPortlet.VERSION_SEPARATOR.length()));

			JournalArticleServiceUtil.deleteArticle(
				groupId, articleId, version, articleURL, serviceContext);
		}
	}

	public static void expireArticle(
			ActionRequest actionRequest, String expireArticleId)
		throws Exception {

		long groupId = ParamUtil.getLong(actionRequest, "groupId");
		String articleURL = ParamUtil.getString(actionRequest, "articleURL");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			JournalArticle.class.getName(), actionRequest);

		int pos = expireArticleId.lastIndexOf(JournalPortlet.VERSION_SEPARATOR);

		if (pos == -1) {
			JournalArticleServiceUtil.expireArticle(
				groupId, expireArticleId, articleURL, serviceContext);
		}
		else {
			String articleId = expireArticleId.substring(0, pos);
			double version = GetterUtil.getDouble(
				expireArticleId.substring(
					pos + JournalPortlet.VERSION_SEPARATOR.length()));

			JournalArticleServiceUtil.expireArticle(
				groupId, articleId, version, articleURL, serviceContext);
		}
	}

	public static JournalArticle getArticle(
			HttpServletRequest httpServletRequest)
		throws PortalException {

		String actionName = ParamUtil.getString(
			httpServletRequest, ActionRequest.ACTION_NAME);

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		long resourcePrimKey = ParamUtil.getLong(
			httpServletRequest, "resourcePrimKey");
		long groupId = ParamUtil.getLong(
			httpServletRequest, "groupId", themeDisplay.getScopeGroupId());
		long classNameId = ParamUtil.getLong(httpServletRequest, "classNameId");
		long classPK = ParamUtil.getLong(httpServletRequest, "classPK");
		String articleId = ParamUtil.getString(httpServletRequest, "articleId");
		int status = ParamUtil.getInteger(
			httpServletRequest, "status", WorkflowConstants.STATUS_ANY);

		JournalArticle article = null;

		if (actionName.equals("/journal/add_article") &&
			(resourcePrimKey != 0)) {

			article = JournalArticleLocalServiceUtil.getLatestArticle(
				resourcePrimKey, status, false);
		}
		else if (!actionName.equals("/journal/add_article") &&
				 Validator.isNotNull(articleId)) {

			article = JournalArticleServiceUtil.getLatestArticle(
				groupId, articleId, status);
		}
		else if ((classNameId > 0) &&
				 (classPK > JournalArticleConstants.CLASS_NAME_ID_DEFAULT)) {

			String className = PortalUtil.getClassName(classNameId);

			try {
				article = JournalArticleServiceUtil.getLatestArticle(
					groupId, className, classPK);
			}
			catch (NoSuchArticleException noSuchArticleException) {
				if (_log.isDebugEnabled()) {
					_log.debug(noSuchArticleException);
				}

				return null;
			}
		}
		else {
			long ddmStructureId = ParamUtil.getLong(
				httpServletRequest, "ddmStructureId");

			DDMStructure ddmStructure =
				DDMStructureLocalServiceUtil.fetchStructure(ddmStructureId);

			if (ddmStructure == null) {
				return null;
			}

			try {
				article = JournalArticleServiceUtil.getArticle(
					ddmStructure.getGroupId(), DDMStructure.class.getName(),
					ddmStructure.getStructureId());

				article.getDescriptionMap();
				article.getTitleMap();

				article.setNew(true);
				article.setId(0);
				article.setGroupId(groupId);
				article.setClassNameId(
					JournalArticleConstants.CLASS_NAME_ID_DEFAULT);
				article.setClassPK(0);
				article.setArticleId(null);
				article.setVersion(0);
			}
			catch (NoSuchArticleException noSuchArticleException) {
				if (_log.isDebugEnabled()) {
					_log.debug(noSuchArticleException);
				}

				return null;
			}
		}

		httpServletRequest.setAttribute(
			CTTimelineKeys.CLASS_NAME, JournalArticle.class.getName());
		httpServletRequest.setAttribute(
			CTTimelineKeys.CLASS_PK, article.getPrimaryKey());

		return article;
	}

	public static JournalFeed getFeed(HttpServletRequest httpServletRequest)
		throws Exception {

		String feedId = ParamUtil.getString(httpServletRequest, "feedId");

		JournalFeed feed = null;

		if (Validator.isNotNull(feedId)) {
			long groupId = ParamUtil.getLong(httpServletRequest, "groupId");

			feed = JournalFeedServiceUtil.getFeed(groupId, feedId);
		}

		return feed;
	}

	public static String getScript(UploadPortletRequest uploadPortletRequest)
		throws Exception {

		String fileScriptContent = _getFileScriptContent(uploadPortletRequest);

		if (Validator.isNotNull(fileScriptContent)) {
			return fileScriptContent;
		}

		return FileUtil.read(uploadPortletRequest.getFile("scriptContent"));
	}

	private static String _getFileScriptContent(
			UploadPortletRequest uploadPortletRequest)
		throws Exception {

		File file = uploadPortletRequest.getFile("script");

		if (file == null) {
			return null;
		}

		String fileScriptContent = FileUtil.read(file);

		String contentType = MimeTypesUtil.getContentType(file);

		if (Validator.isNotNull(fileScriptContent) &&
			!_isValidContentType(contentType)) {

			throw new TemplateScriptException(
				"Invalid contentType " + contentType);
		}

		return fileScriptContent;
	}

	private static boolean _isValidContentType(String contentType) {
		if (contentType.equals(ContentTypes.APPLICATION_XSLT_XML) ||
			contentType.startsWith(ContentTypes.TEXT)) {

			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(ActionUtil.class);

}