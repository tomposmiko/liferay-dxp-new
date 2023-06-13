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

package com.liferay.journal.web.internal.portlet;

import com.liferay.asset.display.page.constants.AssetDisplayPageConstants;
import com.liferay.asset.display.page.portlet.AssetDisplayPageEntryFormProcessor;
import com.liferay.asset.display.page.portlet.AssetDisplayPageFriendlyURLProvider;
import com.liferay.asset.kernel.exception.AssetCategoryException;
import com.liferay.asset.kernel.exception.AssetTagException;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.asset.model.AssetEntryUsage;
import com.liferay.asset.service.AssetEntryUsageLocalService;
import com.liferay.document.library.kernel.exception.DuplicateFileEntryException;
import com.liferay.document.library.kernel.exception.FileSizeException;
import com.liferay.dynamic.data.mapping.exception.NoSuchStructureException;
import com.liferay.dynamic.data.mapping.exception.NoSuchTemplateException;
import com.liferay.dynamic.data.mapping.exception.StorageFieldRequiredException;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.dynamic.data.mapping.util.DDMTemplateHelper;
import com.liferay.dynamic.data.mapping.util.DDMUtil;
import com.liferay.exportimport.kernel.exception.ExportImportContentValidationException;
import com.liferay.item.selector.ItemSelector;
import com.liferay.journal.configuration.JournalFileUploadsConfiguration;
import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.constants.JournalWebKeys;
import com.liferay.journal.exception.ArticleContentException;
import com.liferay.journal.exception.ArticleContentSizeException;
import com.liferay.journal.exception.ArticleDisplayDateException;
import com.liferay.journal.exception.ArticleExpirationDateException;
import com.liferay.journal.exception.ArticleIdException;
import com.liferay.journal.exception.ArticleSmallImageNameException;
import com.liferay.journal.exception.ArticleSmallImageSizeException;
import com.liferay.journal.exception.ArticleTitleException;
import com.liferay.journal.exception.ArticleVersionException;
import com.liferay.journal.exception.DuplicateArticleIdException;
import com.liferay.journal.exception.DuplicateFeedIdException;
import com.liferay.journal.exception.DuplicateFolderNameException;
import com.liferay.journal.exception.FeedContentFieldException;
import com.liferay.journal.exception.FeedIdException;
import com.liferay.journal.exception.FeedNameException;
import com.liferay.journal.exception.FeedTargetLayoutFriendlyUrlException;
import com.liferay.journal.exception.FeedTargetPortletIdException;
import com.liferay.journal.exception.FolderNameException;
import com.liferay.journal.exception.InvalidDDMStructureException;
import com.liferay.journal.exception.MaxAddMenuFavItemsException;
import com.liferay.journal.exception.NoSuchArticleException;
import com.liferay.journal.exception.NoSuchFeedException;
import com.liferay.journal.exception.NoSuchFolderException;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalArticleConstants;
import com.liferay.journal.model.JournalFeed;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.model.JournalFolderConstants;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.journal.service.JournalContentSearchLocalService;
import com.liferay.journal.service.JournalFeedService;
import com.liferay.journal.service.JournalFolderService;
import com.liferay.journal.util.JournalContent;
import com.liferay.journal.util.JournalConverter;
import com.liferay.journal.util.JournalHelper;
import com.liferay.journal.web.asset.model.JournalArticleAssetRenderer;
import com.liferay.journal.web.configuration.JournalWebConfiguration;
import com.liferay.journal.web.internal.portlet.action.ActionUtil;
import com.liferay.journal.web.internal.util.JournalDDMTemplateUtil;
import com.liferay.journal.web.util.JournalPortletUtil;
import com.liferay.journal.web.util.JournalUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.diff.CompareVersionsException;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.model.TrashedModel;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletRequestModel;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.MultiSessionMessages;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.LiferayFileItemException;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.util.PropsValues;
import com.liferay.rss.util.RSSUtil;
import com.liferay.trash.TrashHelper;
import com.liferay.trash.kernel.service.TrashEntryService;
import com.liferay.trash.util.TrashWebKeys;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eduardo García
 */
@Component(
	configurationPid = "com.liferay.journal.web.configuration.JournalWebConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.css-class-wrapper=portlet-journal",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.header-portlet-css=/css/ddm_form.css",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.header-portlet-css=/css/tree.css",
		"com.liferay.portlet.icon=/icons/journal.png",
		"com.liferay.portlet.layout-cacheable=true",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.preferences-unique-per-layout=false",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.scopeable=true",
		"com.liferay.portlet.use-default-template=true",
		"com.liferay.portlet.webdav-storage-token=journal",
		"javax.portlet.display-name=Web Content",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.mvc-action-command-package-prefix=com.liferay.journal.web.portlet.action",
		"javax.portlet.init-param.template-path=/META-INF/resources/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + JournalPortletKeys.JOURNAL,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.supports.mime-type=text/html"
	},
	service = {JournalPortlet.class, Portlet.class}
)
public class JournalPortlet extends MVCPortlet {

	public static final String VERSION_SEPARATOR = "_version_";

	public void addAddMenuFavItem(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String ddmStructureKey = ParamUtil.getString(
			actionRequest, "ddmStructureKey");

		PortalPreferences portalPreferences =
			PortletPreferencesFactoryUtil.getPortalPreferences(actionRequest);

		String key = JournalPortletUtil.getAddMenuFavItemKey(
			actionRequest, actionResponse);

		String[] addMenuFavItems = portalPreferences.getValues(
			JournalPortletKeys.JOURNAL, key, new String[0]);

		if (addMenuFavItems.length >=
				_journalWebConfiguration.maxAddMenuItems()) {

			hideDefaultErrorMessage(actionRequest);

			throw new MaxAddMenuFavItemsException();
		}

		portalPreferences.setValues(
			JournalPortletKeys.JOURNAL, key,
			ArrayUtil.append(addMenuFavItems, ddmStructureKey));
	}

	public void addArticle(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		updateArticle(actionRequest, actionResponse);
	}

	public void addFeed(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		updateFeed(actionRequest, actionResponse);
	}

	public void addFolder(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		updateFolder(actionRequest, actionResponse);
	}

	public void deleteArticle(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		deleteArticles(actionRequest, actionResponse, false);
	}

	public void deleteArticles(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		deleteArticles(actionRequest, actionResponse, false);
	}

	public void deleteEntries(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		deleteEntries(actionRequest, actionResponse, false);
	}

	public void deleteFeeds(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long[] deleteFeedIds = null;

		long deleteFeedId = ParamUtil.getLong(actionRequest, "deleteFeedId");

		if (deleteFeedId > 0) {
			deleteFeedIds = new long[] {deleteFeedId};
		}
		else {
			deleteFeedIds = ParamUtil.getLongValues(actionRequest, "rowIds");
		}

		for (long curDeleteFeedId : deleteFeedIds) {
			_journalFeedService.deleteFeed(
				themeDisplay.getScopeGroupId(),
				String.valueOf(curDeleteFeedId));
		}
	}

	public void deleteFolder(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		deleteFolder(actionRequest, actionResponse, false);
	}

	public void expireArticles(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String articleId = ParamUtil.getString(actionRequest, "articleId");

		if (Validator.isNotNull(articleId)) {
			ActionUtil.expireArticle(actionRequest, articleId);
		}
		else {
			String[] expireArticleIds = ParamUtil.getParameterValues(
				actionRequest, "rowIds");

			for (String expireArticleId : expireArticleIds) {
				ActionUtil.expireArticle(
					actionRequest, HtmlUtil.unescape(expireArticleId));
			}
		}

		sendEditArticleRedirect(actionRequest, actionResponse);
	}

	public void expireEntries(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long[] expireFolderIds = ParamUtil.getLongValues(
			actionRequest, "rowIdsJournalFolder");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			JournalArticle.class.getName(), actionRequest);

		for (long expireFolderId : expireFolderIds) {
			ActionUtil.expireFolder(
				themeDisplay.getScopeGroupId(), expireFolderId, serviceContext);
		}

		String[] expireArticleIds = ParamUtil.getStringValues(
			actionRequest, "rowIdsJournalArticle");

		for (String expireArticleId : expireArticleIds) {
			ActionUtil.expireArticle(
				actionRequest, HtmlUtil.unescape(expireArticleId));
		}

		sendEditEntryRedirect(actionRequest, actionResponse);
	}

	public void moveEntries(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long newFolderId = ParamUtil.getLong(actionRequest, "newFolderId");

		long[] folderIds = ParamUtil.getLongValues(
			actionRequest, "rowIdsJournalFolder");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			JournalArticle.class.getName(), actionRequest);

		for (long folderId : folderIds) {
			_journalFolderService.moveFolder(
				folderId, newFolderId, serviceContext);
		}

		List<String> invalidArticleIds = new ArrayList<>();

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String[] articleIds = ParamUtil.getStringValues(
			actionRequest, "rowIdsJournalArticle");

		for (String articleId : articleIds) {
			try {
				_journalArticleService.moveArticle(
					themeDisplay.getScopeGroupId(),
					HtmlUtil.unescape(articleId), newFolderId, serviceContext);
			}
			catch (InvalidDDMStructureException iddmse) {
				if (_log.isWarnEnabled()) {
					_log.warn(iddmse.getMessage());
				}

				invalidArticleIds.add(articleId);
			}
		}

		if (!invalidArticleIds.isEmpty()) {
			StringBundler sb = new StringBundler(4);

			sb.append("Folder ");
			sb.append(newFolderId);
			sb.append(" does not allow the structures for articles: ");
			sb.append(StringUtil.merge(invalidArticleIds));

			throw new InvalidDDMStructureException(sb.toString());
		}

		sendEditEntryRedirect(actionRequest, actionResponse);
	}

	public void moveEntriesToTrash(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		deleteEntries(actionRequest, actionResponse, true);
	}

	public void moveFolderToTrash(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		deleteFolder(actionRequest, actionResponse, true);
	}

	public void moveToTrash(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		deleteArticles(actionRequest, actionResponse, true);
	}

	public void previewArticle(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		updateArticle(actionRequest, actionResponse);
	}

	public void removeAddMenuFavItem(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String ddmStructureKey = ParamUtil.getString(
			actionRequest, "ddmStructureKey");

		PortalPreferences portalPreferences =
			PortletPreferencesFactoryUtil.getPortalPreferences(actionRequest);

		String key = JournalPortletUtil.getAddMenuFavItemKey(
			actionRequest, actionResponse);

		String[] addMenuFavItems = portalPreferences.getValues(
			JournalPortletKeys.JOURNAL, key);

		portalPreferences.setValues(
			JournalPortletKeys.JOURNAL, key,
			ArrayUtil.remove(addMenuFavItems, ddmStructureKey));
	}

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		renderRequest.setAttribute(
			AssetDisplayPageFriendlyURLProvider.class.getName(),
			_assetDisplayPageFriendlyURLProvider);

		renderRequest.setAttribute(TrashWebKeys.TRASH_HELPER, _trashHelper);

		String path = getPath(renderRequest, renderResponse);

		if (Objects.equals(path, "/edit_article.jsp")) {
			renderRequest.setAttribute(
				JournalWebKeys.ITEM_SELECTOR, _itemSelector);
		}

		if (Objects.equals(path, "/edit_ddm_template.jsp")) {
			renderRequest.setAttribute(
				DDMTemplateHelper.class.getName(), _ddmTemplateHelper);
			renderRequest.setAttribute(
				JournalDDMTemplateUtil.class.getName(),
				_journalDDMTemplateUtil);
		}

		renderRequest.setAttribute(
			JournalFileUploadsConfiguration.class.getName(),
			_journalFileUploadsConfiguration);

		renderRequest.setAttribute(
			JournalWebConfiguration.class.getName(), _journalWebConfiguration);

		renderRequest.setAttribute(
			JournalWebKeys.JOURNAL_CONTENT, _journalContent);

		renderRequest.setAttribute(
			JournalWebKeys.JOURNAL_CONVERTER, _journalConverter);

		super.render(renderRequest, renderResponse);
	}

	public void restoreTrashEntries(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long[] restoreTrashEntryIds = StringUtil.split(
			ParamUtil.getString(actionRequest, "restoreTrashEntryIds"), 0L);

		for (long restoreTrashEntryId : restoreTrashEntryIds) {
			_trashEntryService.restoreEntry(restoreTrashEntryId);
		}
	}

	@Override
	public void serveResource(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IOException, PortletException {

		resourceRequest.setAttribute(
			AssetDisplayPageFriendlyURLProvider.class.getName(),
			_assetDisplayPageFriendlyURLProvider);

		resourceRequest.setAttribute(
			DDMTemplateHelper.class.getName(), _ddmTemplateHelper);

		resourceRequest.setAttribute(TrashWebKeys.TRASH_HELPER, _trashHelper);

		resourceRequest.setAttribute(
			JournalWebConfiguration.class.getName(), _journalWebConfiguration);

		String resourceID = GetterUtil.getString(
			resourceRequest.getResourceID());

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			resourceRequest);

		HttpServletResponse httpServletResponse =
			_portal.getHttpServletResponse(resourceResponse);

		if (resourceID.equals("compareVersions")) {
			ThemeDisplay themeDisplay =
				(ThemeDisplay)resourceRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			long groupId = ParamUtil.getLong(resourceRequest, "groupId");
			String articleId = ParamUtil.getString(
				resourceRequest, "articleId");
			double sourceVersion = ParamUtil.getDouble(
				resourceRequest, "filterSourceVersion");
			double targetVersion = ParamUtil.getDouble(
				resourceRequest, "filterTargetVersion");
			String languageId = ParamUtil.getString(
				resourceRequest, "languageId");

			String diffHtmlResults = null;

			try {
				diffHtmlResults = _journalHelper.diffHtml(
					groupId, articleId, sourceVersion, targetVersion,
					languageId,
					new PortletRequestModel(resourceRequest, resourceResponse),
					themeDisplay);
			}
			catch (CompareVersionsException cve) {
				resourceRequest.setAttribute(
					WebKeys.DIFF_VERSION, cve.getVersion());
			}
			catch (Exception e) {
				try {
					_portal.sendError(
						e, httpServletRequest, httpServletResponse);
				}
				catch (ServletException se) {
				}
			}

			resourceRequest.setAttribute(
				WebKeys.DIFF_HTML_RESULTS, diffHtmlResults);

			PortletSession portletSession = resourceRequest.getPortletSession();

			PortletContext portletContext = portletSession.getPortletContext();

			PortletRequestDispatcher portletRequestDispatcher =
				portletContext.getRequestDispatcher(
					"/compare_versions_diff_html.jsp");

			portletRequestDispatcher.include(resourceRequest, resourceResponse);
		}
		else {
			super.serveResource(resourceRequest, resourceResponse);
		}
	}

	public void subscribeArticle(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long articleId = ParamUtil.getLong(actionRequest, "articleId");

		_journalArticleService.subscribe(
			themeDisplay.getScopeGroupId(), articleId);
	}

	public void subscribeFolder(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long folderId = ParamUtil.getLong(actionRequest, "folderId");

		_journalFolderService.subscribe(
			themeDisplay.getScopeGroupId(), folderId);
	}

	public void subscribeStructure(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long ddmStructureId = ParamUtil.getLong(
			actionRequest, "ddmStructureId");

		_journalArticleService.subscribeStructure(
			themeDisplay.getScopeGroupId(), themeDisplay.getUserId(),
			ddmStructureId);

		sendEditArticleRedirect(actionRequest, actionResponse);
	}

	public void unsubscribeArticle(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long articleId = ParamUtil.getLong(actionRequest, "articleId");

		_journalArticleService.unsubscribe(
			themeDisplay.getScopeGroupId(), articleId);
	}

	public void unsubscribeFolder(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long folderId = ParamUtil.getLong(actionRequest, "folderId");

		_journalFolderService.unsubscribe(
			themeDisplay.getScopeGroupId(), folderId);
	}

	public void unsubscribeStructure(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long ddmStructureId = ParamUtil.getLong(
			actionRequest, "ddmStructureId");

		_journalArticleService.unsubscribeStructure(
			themeDisplay.getScopeGroupId(), themeDisplay.getUserId(),
			ddmStructureId);

		sendEditArticleRedirect(actionRequest, actionResponse);
	}

	public void updateArticle(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		UploadException uploadException =
			(UploadException)actionRequest.getAttribute(
				WebKeys.UPLOAD_EXCEPTION);

		if (uploadException != null) {
			Throwable cause = uploadException.getCause();

			if (uploadException.isExceededLiferayFileItemSizeLimit()) {
				throw new LiferayFileItemException(cause);
			}

			if (uploadException.isExceededFileSizeLimit() ||
				uploadException.isExceededUploadRequestSizeLimit()) {

				throw new ArticleContentSizeException(cause);
			}

			throw new PortalException(cause);
		}

		UploadPortletRequest uploadPortletRequest =
			_portal.getUploadPortletRequest(actionRequest);

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Updating article " +
					MapUtil.toString(uploadPortletRequest.getParameterMap()));
		}

		String actionName = ParamUtil.getString(
			actionRequest, ActionRequest.ACTION_NAME);

		long groupId = ParamUtil.getLong(uploadPortletRequest, "groupId");
		long folderId = ParamUtil.getLong(uploadPortletRequest, "folderId");
		long classNameId = ParamUtil.getLong(
			uploadPortletRequest, "classNameId");
		long classPK = ParamUtil.getLong(uploadPortletRequest, "classPK");
		String articleId = ParamUtil.getString(
			uploadPortletRequest, "articleId");
		boolean autoArticleId = ParamUtil.getBoolean(
			uploadPortletRequest, "autoArticleId");
		double version = ParamUtil.getDouble(uploadPortletRequest, "version");
		Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(
			actionRequest, "titleMapAsXML");

		String ddmStructureKey = ParamUtil.getString(
			uploadPortletRequest, "ddmStructureKey");

		DDMStructure ddmStructure = _ddmStructureLocalService.getStructure(
			_portal.getSiteGroupId(groupId),
			_portal.getClassNameId(JournalArticle.class), ddmStructureKey,
			true);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			JournalArticle.class.getName(), uploadPortletRequest);

		Fields fields = DDMUtil.getFields(
			ddmStructure.getStructureId(), serviceContext);

		String content = _journalConverter.getContent(ddmStructure, fields);

		Locale articleDefaultLocale = LocaleUtil.fromLanguageId(
			LocalizationUtil.getDefaultLanguageId(content));

		if ((classNameId == JournalArticleConstants.CLASSNAME_ID_DEFAULT) &&
			!_hasDefaultLocale(titleMap, articleDefaultLocale)) {

			titleMap.put(
				articleDefaultLocale,
				LanguageUtil.format(
					_portal.getHttpServletRequest(actionRequest), "untitled-x",
					HtmlUtil.escape(
						ddmStructure.getName(themeDisplay.getLocale()))));
		}

		Map<Locale, String> descriptionMap =
			LocalizationUtil.getLocalizationMap(
				actionRequest, "descriptionMapAsXML");
		Map<Locale, String> friendlyURLMap =
			LocalizationUtil.getLocalizationMap(actionRequest, "friendlyURL");

		String ddmTemplateKey = ParamUtil.getString(
			uploadPortletRequest, "ddmTemplateKey");
		int displayPageType = ParamUtil.getInteger(
			uploadPortletRequest, "displayPageType");

		String layoutUuid = ParamUtil.getString(
			uploadPortletRequest, "layoutUuid");

		if ((displayPageType == AssetDisplayPageConstants.TYPE_DEFAULT) ||
			(displayPageType == AssetDisplayPageConstants.TYPE_SPECIFIC)) {

			Layout targetLayout = _journalHelper.getArticleLayout(
				layoutUuid, groupId);

			JournalArticle latestArticle = _journalArticleService.fetchArticle(
				groupId, articleId);

			if ((displayPageType == AssetDisplayPageConstants.TYPE_SPECIFIC) &&
				(targetLayout == null) && (latestArticle != null) &&
				Validator.isNotNull(latestArticle.getLayoutUuid())) {

				Layout latestTargetLayout = _journalHelper.getArticleLayout(
					latestArticle.getLayoutUuid(), groupId);

				if (latestTargetLayout == null) {
					layoutUuid = latestArticle.getLayoutUuid();
				}
			}
			else if (targetLayout == null) {
				layoutUuid = null;
			}
		}
		else {
			layoutUuid = null;
		}

		int displayDateMonth = ParamUtil.getInteger(
			uploadPortletRequest, "displayDateMonth");
		int displayDateDay = ParamUtil.getInteger(
			uploadPortletRequest, "displayDateDay");
		int displayDateYear = ParamUtil.getInteger(
			uploadPortletRequest, "displayDateYear");
		int displayDateHour = ParamUtil.getInteger(
			uploadPortletRequest, "displayDateHour");
		int displayDateMinute = ParamUtil.getInteger(
			uploadPortletRequest, "displayDateMinute");
		int displayDateAmPm = ParamUtil.getInteger(
			uploadPortletRequest, "displayDateAmPm");

		if (displayDateAmPm == Calendar.PM) {
			displayDateHour += 12;
		}

		int expirationDateMonth = ParamUtil.getInteger(
			uploadPortletRequest, "expirationDateMonth");
		int expirationDateDay = ParamUtil.getInteger(
			uploadPortletRequest, "expirationDateDay");
		int expirationDateYear = ParamUtil.getInteger(
			uploadPortletRequest, "expirationDateYear");
		int expirationDateHour = ParamUtil.getInteger(
			uploadPortletRequest, "expirationDateHour");
		int expirationDateMinute = ParamUtil.getInteger(
			uploadPortletRequest, "expirationDateMinute");
		int expirationDateAmPm = ParamUtil.getInteger(
			uploadPortletRequest, "expirationDateAmPm");

		boolean neverExpire = ParamUtil.getBoolean(
			uploadPortletRequest, "neverExpire", displayDateYear == 0);

		if (!PropsValues.SCHEDULER_ENABLED) {
			neverExpire = true;
		}

		if (expirationDateAmPm == Calendar.PM) {
			expirationDateHour += 12;
		}

		int reviewDateMonth = ParamUtil.getInteger(
			uploadPortletRequest, "reviewDateMonth");
		int reviewDateDay = ParamUtil.getInteger(
			uploadPortletRequest, "reviewDateDay");
		int reviewDateYear = ParamUtil.getInteger(
			uploadPortletRequest, "reviewDateYear");
		int reviewDateHour = ParamUtil.getInteger(
			uploadPortletRequest, "reviewDateHour");
		int reviewDateMinute = ParamUtil.getInteger(
			uploadPortletRequest, "reviewDateMinute");
		int reviewDateAmPm = ParamUtil.getInteger(
			uploadPortletRequest, "reviewDateAmPm");

		boolean neverReview = ParamUtil.getBoolean(
			uploadPortletRequest, "neverReview", displayDateYear == 0);

		if (!PropsValues.SCHEDULER_ENABLED) {
			neverReview = true;
		}

		if (reviewDateAmPm == Calendar.PM) {
			reviewDateHour += 12;
		}

		boolean indexable = ParamUtil.getBoolean(
			uploadPortletRequest, "indexable");

		String smallImageSource = ParamUtil.getString(
			uploadPortletRequest, "smallImageSource", "none");

		boolean smallImage = !Objects.equals(smallImageSource, "none");

		String smallImageURL = StringPool.BLANK;
		File smallFile = null;

		if (Objects.equals(smallImageSource, "url")) {
			smallImageURL = ParamUtil.getString(
				uploadPortletRequest, "smallImageURL");
		}
		else if (Objects.equals(smallImageSource, "file")) {
			smallFile = uploadPortletRequest.getFile("smallFile");
		}

		String articleURL = ParamUtil.getString(
			uploadPortletRequest, "articleURL");

		JournalArticle article = null;
		String oldUrlTitle = StringPool.BLANK;

		if (actionName.equals("addArticle")) {

			// Add article

			article = _journalArticleService.addArticle(
				groupId, folderId, classNameId, classPK, articleId,
				autoArticleId, titleMap, descriptionMap, friendlyURLMap,
				content, ddmStructureKey, ddmTemplateKey, layoutUuid,
				displayDateMonth, displayDateDay, displayDateYear,
				displayDateHour, displayDateMinute, expirationDateMonth,
				expirationDateDay, expirationDateYear, expirationDateHour,
				expirationDateMinute, neverExpire, reviewDateMonth,
				reviewDateDay, reviewDateYear, reviewDateHour, reviewDateMinute,
				neverReview, indexable, smallImage, smallImageURL, smallFile,
				null, articleURL, serviceContext);
		}
		else {

			// Update article

			article = _journalArticleService.getArticle(
				groupId, articleId, version);

			String tempOldUrlTitle = article.getUrlTitle();

			if (actionName.equals("previewArticle") ||
				actionName.equals("updateArticle")) {

				article = _journalArticleService.updateArticle(
					groupId, folderId, articleId, version, titleMap,
					descriptionMap, friendlyURLMap, content, ddmStructureKey,
					ddmTemplateKey, layoutUuid, displayDateMonth,
					displayDateDay, displayDateYear, displayDateHour,
					displayDateMinute, expirationDateMonth, expirationDateDay,
					expirationDateYear, expirationDateHour,
					expirationDateMinute, neverExpire, reviewDateMonth,
					reviewDateDay, reviewDateYear, reviewDateHour,
					reviewDateMinute, neverReview, indexable, smallImage,
					smallImageURL, smallFile, null, articleURL, serviceContext);
			}

			if (!tempOldUrlTitle.equals(article.getUrlTitle())) {
				oldUrlTitle = tempOldUrlTitle;
			}
		}

		// Recent articles

		JournalUtil.addRecentArticle(actionRequest, article);

		// Journal content

		String portletResource = ParamUtil.getString(
			actionRequest, "portletResource");

		long refererPlid = ParamUtil.getLong(actionRequest, "refererPlid");

		if (Validator.isNotNull(portletResource) && (refererPlid > 0)) {
			AssetEntry assetEntry = _assetEntryLocalService.fetchEntry(
				JournalArticle.class.getName(), article.getResourcePrimKey());

			Layout layout = _layoutLocalService.getLayout(refererPlid);

			PortletPreferences portletPreferences =
				PortletPreferencesFactoryUtil.getStrictPortletSetup(
					layout, portletResource);

			if (portletPreferences != null) {
				portletPreferences.setValue(
					"groupId", String.valueOf(article.getGroupId()));
				portletPreferences.setValue(
					"articleId", article.getArticleId());

				if (assetEntry != null) {
					portletPreferences.setValue(
						"assetEntryId",
						String.valueOf(assetEntry.getEntryId()));
				}

				portletPreferences.store();

				updateContentSearch(
					refererPlid, portletResource, article.getArticleId());
			}

			if (assetEntry != null) {
				_updateAssetEntryUsage(
					groupId, assetEntry, portletResource, refererPlid,
					serviceContext);
			}
		}

		// Asset display page

		_assetDisplayPageEntryFormProcessor.process(
			JournalArticle.class.getName(), article.getResourcePrimKey(),
			actionRequest);

		int workflowAction = ParamUtil.getInteger(
			actionRequest, "workflowAction", WorkflowConstants.ACTION_PUBLISH);

		if (Validator.isNotNull(portletResource) &&
			(workflowAction != WorkflowConstants.ACTION_SAVE_DRAFT)) {

			MultiSessionMessages.add(
				actionRequest, portletResource + "requestProcessed");
		}

		sendEditArticleRedirect(
			actionRequest, actionResponse, article, oldUrlTitle);

		boolean hideDefaultSuccessMessage = ParamUtil.getBoolean(
			actionRequest, "hideDefaultSuccessMessage");

		if (hideDefaultSuccessMessage) {
			hideDefaultSuccessMessage(actionRequest);
		}
	}

	public void updateFeed(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String actionName = ParamUtil.getString(
			actionRequest, ActionRequest.ACTION_NAME);

		long groupId = ParamUtil.getLong(actionRequest, "groupId");

		String feedId = ParamUtil.getString(actionRequest, "feedId");

		String name = ParamUtil.getString(actionRequest, "name");
		String description = ParamUtil.getString(actionRequest, "description");
		String ddmStructureKey = ParamUtil.getString(
			actionRequest, "ddmStructureKey");
		String ddmTemplateKey = ParamUtil.getString(
			actionRequest, "ddmTemplateKey");
		String ddmRendererTemplateKey = ParamUtil.getString(
			actionRequest, "ddmRendererTemplateKey");
		int delta = ParamUtil.getInteger(actionRequest, "delta");
		String orderByCol = ParamUtil.getString(actionRequest, "orderByCol");
		String orderByType = ParamUtil.getString(actionRequest, "orderByType");
		String targetLayoutFriendlyUrl = ParamUtil.getString(
			actionRequest, "targetLayoutFriendlyUrl");
		String targetPortletId = ParamUtil.getString(
			actionRequest, "targetPortletId");
		String contentField = ParamUtil.getString(
			actionRequest, "contentField");

		String feedType = ParamUtil.getString(
			actionRequest, "feedType", RSSUtil.FEED_TYPE_DEFAULT);

		String feedFormat = RSSUtil.getFeedTypeFormat(feedType);
		double feedVersion = RSSUtil.getFeedTypeVersion(feedType);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			JournalFeed.class.getName(), actionRequest);

		if (actionName.equals("addFeed")) {

			// Add feed

			boolean autoFeedId = ParamUtil.getBoolean(
				actionRequest, "autoFeedId");

			_journalFeedService.addFeed(
				groupId, feedId, autoFeedId, name, description, ddmStructureKey,
				ddmTemplateKey, ddmRendererTemplateKey, delta, orderByCol,
				orderByType, targetLayoutFriendlyUrl, targetPortletId,
				contentField, feedFormat, feedVersion, serviceContext);
		}
		else {

			// Update feed

			_journalFeedService.updateFeed(
				groupId, feedId, name, description, ddmStructureKey,
				ddmTemplateKey, ddmRendererTemplateKey, delta, orderByCol,
				orderByType, targetLayoutFriendlyUrl, targetPortletId,
				contentField, feedFormat, feedVersion, serviceContext);
		}
	}

	public void updateFolder(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long folderId = ParamUtil.getLong(actionRequest, "folderId");

		long parentFolderId = ParamUtil.getLong(
			actionRequest, "parentFolderId");
		String name = ParamUtil.getString(actionRequest, "name");
		String description = ParamUtil.getString(actionRequest, "description");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			JournalFolder.class.getName(), actionRequest);

		if (folderId <= 0) {

			// Add folder

			_journalFolderService.addFolder(
				serviceContext.getScopeGroupId(), parentFolderId, name,
				description, serviceContext);
		}
		else {

			// Update folder

			long[] ddmStructureIds = StringUtil.split(
				ParamUtil.getString(
					actionRequest, "ddmStructuresSearchContainerPrimaryKeys"),
				0L);
			int restrinctionType = ParamUtil.getInteger(
				actionRequest, "restrictionType");
			boolean mergeWithParentFolder = ParamUtil.getBoolean(
				actionRequest, "mergeWithParentFolder");

			_journalFolderService.updateFolder(
				serviceContext.getScopeGroupId(), folderId, parentFolderId,
				name, description, ddmStructureIds, restrinctionType,
				mergeWithParentFolder, serviceContext);
		}
	}

	public void updateWorkflowDefinitions(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long[] ddmStructureIds = StringUtil.split(
			ParamUtil.getString(
				actionRequest, "ddmStructuresSearchContainerPrimaryKeys"),
			0L);
		int restrinctionType = ParamUtil.getInteger(
			actionRequest, "restrictionType");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			JournalFolder.class.getName(), actionRequest);

		_journalFolderService.updateFolder(
			serviceContext.getScopeGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, null, null,
			ddmStructureIds, restrinctionType, false, serviceContext);
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_journalFileUploadsConfiguration = ConfigurableUtil.createConfigurable(
			JournalFileUploadsConfiguration.class, properties);

		_journalWebConfiguration = ConfigurableUtil.createConfigurable(
			JournalWebConfiguration.class, properties);
	}

	protected void deleteArticles(
			ActionRequest actionRequest, ActionResponse actionResponse,
			boolean moveToTrash)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String[] deleteArticleIds = null;

		String articleId = ParamUtil.getString(actionRequest, "articleId");

		if (Validator.isNotNull(articleId)) {
			deleteArticleIds = new String[] {articleId};
		}
		else {
			deleteArticleIds = ParamUtil.getParameterValues(
				actionRequest, "rowIds");
		}

		List<TrashedModel> trashedModels = new ArrayList<>();

		for (String deleteArticleId : deleteArticleIds) {
			if (moveToTrash) {
				JournalArticle article =
					_journalArticleService.moveArticleToTrash(
						themeDisplay.getScopeGroupId(),
						HtmlUtil.unescape(deleteArticleId));

				trashedModels.add(article);
			}
			else {
				ActionUtil.deleteArticle(
					actionRequest, HtmlUtil.unescape(deleteArticleId));
			}
		}

		if (moveToTrash && !trashedModels.isEmpty()) {
			Map<String, Object> data = new HashMap<>();

			data.put("trashedModels", trashedModels);

			SessionMessages.add(
				actionRequest,
				_portal.getPortletId(actionRequest) +
					SessionMessages.KEY_SUFFIX_DELETE_SUCCESS_DATA,
				data);

			hideDefaultSuccessMessage(actionRequest);
		}

		sendEditArticleRedirect(actionRequest, actionResponse);
	}

	protected void deleteEntries(
			ActionRequest actionRequest, ActionResponse actionResponse,
			boolean moveToTrash)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		List<TrashedModel> trashedModels = new ArrayList<>();

		long[] deleteFolderIds = ParamUtil.getLongValues(
			actionRequest, "rowIdsJournalFolder");

		for (long deleteFolderId : deleteFolderIds) {
			if (moveToTrash) {
				JournalFolder folder = _journalFolderService.moveFolderToTrash(
					deleteFolderId);

				trashedModels.add(folder);
			}
			else {
				_journalFolderService.deleteFolder(deleteFolderId);
			}
		}

		String[] deleteArticleIds = ParamUtil.getStringValues(
			actionRequest, "rowIdsJournalArticle");

		for (String deleteArticleId : deleteArticleIds) {
			if (moveToTrash) {
				JournalArticle article =
					_journalArticleService.moveArticleToTrash(
						themeDisplay.getScopeGroupId(),
						HtmlUtil.unescape(deleteArticleId));

				trashedModels.add(article);
			}
			else {
				ActionUtil.deleteArticle(
					actionRequest, HtmlUtil.unescape(deleteArticleId));
			}
		}

		if (moveToTrash && !trashedModels.isEmpty()) {
			Map<String, Object> data = new HashMap<>();

			data.put("trashedModels", trashedModels);

			SessionMessages.add(
				actionRequest,
				_portal.getPortletId(actionRequest) +
					SessionMessages.KEY_SUFFIX_DELETE_SUCCESS_DATA,
				data);

			hideDefaultSuccessMessage(actionRequest);
		}

		sendEditEntryRedirect(actionRequest, actionResponse);
	}

	protected void deleteFolder(
			ActionRequest actionRequest, ActionResponse actionResponse,
			boolean moveToTrash)
		throws Exception {

		long folderId = ParamUtil.getLong(actionRequest, "folderId");

		List<TrashedModel> trashedModels = new ArrayList<>();

		if (moveToTrash) {
			JournalFolder folder = _journalFolderService.moveFolderToTrash(
				folderId);

			trashedModels.add(folder);
		}
		else {
			_journalFolderService.deleteFolder(folderId);
		}

		if (moveToTrash && !trashedModels.isEmpty()) {
			Map<String, Object> data = new HashMap<>();

			data.put("trashedModels", trashedModels);

			SessionMessages.add(
				actionRequest,
				_portal.getPortletId(actionRequest) +
					SessionMessages.KEY_SUFFIX_DELETE_SUCCESS_DATA,
				data);

			hideDefaultSuccessMessage(actionRequest);
		}
	}

	@Override
	protected void doDispatch(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		try {
			String path = getPath(renderRequest, renderResponse);

			if (Objects.equals(path, "/edit_article.jsp") ||
				Objects.equals(path, "/view_article_history.jsp")) {

				ActionUtil.getArticle(renderRequest);
			}
			else {
				ActionUtil.getFolder(renderRequest);
			}
		}
		catch (Exception e) {
			_log.error(e.getMessage());

			SessionErrors.add(renderRequest, e.getClass());
		}

		if (SessionErrors.contains(
				renderRequest, NoSuchArticleException.class.getName()) ||
			SessionErrors.contains(
				renderRequest, NoSuchFeedException.class.getName()) ||
			SessionErrors.contains(
				renderRequest, NoSuchFolderException.class.getName()) ||
			SessionErrors.contains(
				renderRequest, NoSuchStructureException.class.getName()) ||
			SessionErrors.contains(
				renderRequest, NoSuchTemplateException.class.getName()) ||
			SessionErrors.contains(
				renderRequest, PrincipalException.getNestedClasses())) {

			include("/error.jsp", renderRequest, renderResponse);
		}
		else {
			super.doDispatch(renderRequest, renderResponse);
		}
	}

	protected String getSaveAndContinueRedirect(
			ActionRequest actionRequest, JournalArticle article,
			String redirect)
		throws Exception {

		String portletResource = ParamUtil.getString(
			actionRequest, "portletResource");

		String referringPortletResource = ParamUtil.getString(
			actionRequest, "referringPortletResource");

		PortletURL portletURL = PortletURLFactoryUtil.create(
			actionRequest, JournalPortletKeys.JOURNAL,
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter("mvcPath", "/edit_article.jsp");
		portletURL.setParameter("redirect", redirect);
		portletURL.setParameter("portletResource", portletResource);
		portletURL.setParameter(
			"referringPortletResource", referringPortletResource);
		portletURL.setParameter(
			"resourcePrimKey", String.valueOf(article.getResourcePrimKey()));
		portletURL.setParameter(
			"groupId", String.valueOf(article.getGroupId()));
		portletURL.setParameter(
			"folderId", String.valueOf(article.getFolderId()));
		portletURL.setParameter("articleId", article.getArticleId());
		portletURL.setParameter(
			"version", String.valueOf(article.getVersion()));

		portletURL.setWindowState(actionRequest.getWindowState());

		return portletURL.toString();
	}

	@Override
	protected boolean isAlwaysSendRedirect() {
		return true;
	}

	@Override
	protected boolean isSessionErrorException(Throwable cause) {
		if (cause instanceof ArticleContentException ||
			cause instanceof ArticleContentSizeException ||
			cause instanceof ArticleDisplayDateException ||
			cause instanceof ArticleExpirationDateException ||
			cause instanceof ArticleIdException ||
			cause instanceof ArticleSmallImageNameException ||
			cause instanceof ArticleSmallImageSizeException ||
			cause instanceof ArticleTitleException ||
			cause instanceof ArticleVersionException ||
			cause instanceof AssetCategoryException ||
			cause instanceof AssetTagException ||
			cause instanceof DuplicateArticleIdException ||
			cause instanceof DuplicateFeedIdException ||
			cause instanceof DuplicateFileEntryException ||
			cause instanceof DuplicateFolderNameException ||
			cause instanceof ExportImportContentValidationException ||
			cause instanceof FeedContentFieldException ||
			cause instanceof FeedIdException ||
			cause instanceof FeedNameException ||
			cause instanceof FeedTargetLayoutFriendlyUrlException ||
			cause instanceof FeedTargetPortletIdException ||
			cause instanceof FileSizeException ||
			cause instanceof FolderNameException ||
			cause instanceof InvalidDDMStructureException ||
			cause instanceof LiferayFileItemException ||
			cause instanceof LocaleException ||
			cause instanceof MaxAddMenuFavItemsException ||
			cause instanceof StorageFieldRequiredException ||
			cause instanceof SystemException ||
			super.isSessionErrorException(cause)) {

			return true;
		}

		return false;
	}

	protected void sendEditArticleRedirect(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		sendEditArticleRedirect(
			actionRequest, actionResponse, null, StringPool.BLANK);
	}

	protected void sendEditArticleRedirect(
			ActionRequest actionRequest, ActionResponse actionResponse,
			JournalArticle article, String oldUrlTitle)
		throws Exception {

		String actionName = ParamUtil.getString(
			actionRequest, ActionRequest.ACTION_NAME);

		String redirect = ParamUtil.getString(actionRequest, "redirect");

		int workflowAction = ParamUtil.getInteger(
			actionRequest, "workflowAction", WorkflowConstants.ACTION_PUBLISH);

		String portletId = _http.getParameter(
			redirect, "portletResource", false);

		String namespace = _portal.getPortletNamespace(portletId);

		if (Validator.isNotNull(oldUrlTitle) &&
			Validator.isNotNull(portletId)) {

			String oldRedirectParam = namespace + "redirect";

			String oldRedirect = _http.getParameter(
				redirect, oldRedirectParam, false);

			if (Validator.isNotNull(oldRedirect)) {
				String newRedirect = _http.decodeURL(oldRedirect);

				newRedirect = StringUtil.replace(
					newRedirect, oldUrlTitle, article.getUrlTitle());
				newRedirect = StringUtil.replace(
					newRedirect, oldRedirectParam, "redirect");

				redirect = StringUtil.replace(
					redirect, oldRedirect, newRedirect);
			}
		}

		if ((actionName.equals("deleteArticle") ||
			 actionName.equals("deleteArticles")) &&
			!ActionUtil.hasArticle(actionRequest)) {

			ThemeDisplay themeDisplay =
				(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

			PortletURL portletURL = PortletURLFactoryUtil.create(
				actionRequest, themeDisplay.getPpid(),
				PortletRequest.RENDER_PHASE);

			redirect = portletURL.toString();
		}

		if ((article != null) &&
			(workflowAction == WorkflowConstants.ACTION_SAVE_DRAFT)) {

			redirect = getSaveAndContinueRedirect(
				actionRequest, article, redirect);

			if (actionName.equals("previewArticle")) {
				SessionMessages.add(actionRequest, "previewRequested");

				hideDefaultSuccessMessage(actionRequest);
			}
		}
		else {
			redirect = _portal.escapeRedirect(redirect);

			if (Validator.isNotNull(redirect) &&
				Validator.isNotNull(portletId)) {

				if (actionName.equals("addArticle") && (article != null) &&
					Validator.isNotNull(namespace)) {

					redirect = _http.addParameter(
						redirect, namespace + "className",
						JournalArticle.class.getName());
					redirect = _http.addParameter(
						redirect, namespace + "classPK",
						JournalArticleAssetRenderer.getClassPK(article));
				}
			}
		}

		actionRequest.setAttribute(WebKeys.REDIRECT, redirect);
	}

	protected void sendEditEntryRedirect(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String redirect = _portal.escapeRedirect(
			ParamUtil.getString(actionRequest, "redirect"));

		WindowState windowState = actionRequest.getWindowState();

		if (!windowState.equals(LiferayWindowState.POP_UP)) {
			sendRedirect(actionRequest, actionResponse);
		}
		else if (Validator.isNotNull(redirect)) {
			actionResponse.sendRedirect(redirect);
		}
	}

	@Reference(
		target = "(&(release.bundle.symbolic.name=com.liferay.journal.web)(&(release.schema.version>=1.0.0)(!(release.schema.version>=1.1.0))))",
		unbind = "-"
	)
	protected void setRelease(Release release) {
	}

	protected void updateContentSearch(
			long plid, String portletResource, String articleId)
		throws Exception {

		Layout layout = _layoutLocalService.fetchLayout(plid);

		_journalContentSearchLocalService.updateContentSearch(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			portletResource, articleId, true);
	}

	private boolean _hasDefaultLocale(Map<Locale, String> map, Locale locale) {
		if (MapUtil.isEmpty(map)) {
			return false;
		}

		if (Validator.isNull(map.get(locale))) {
			return false;
		}

		return true;
	}

	private void _updateAssetEntryUsage(
		long groupId, AssetEntry assetEntry, String portletResource, long plid,
		ServiceContext serviceContext) {

		AssetEntryUsage assetEntryUsage =
			_assetEntryUsageLocalService.fetchAssetEntryUsage(
				assetEntry.getEntryId(),
				_portal.getClassNameId(
					com.liferay.portal.kernel.model.Portlet.class),
				portletResource, plid);

		if (assetEntryUsage != null) {
			return;
		}

		_assetEntryUsageLocalService.addAssetEntryUsage(
			groupId, assetEntry.getEntryId(),
			_portal.getClassNameId(
				com.liferay.portal.kernel.model.Portlet.class),
			portletResource, plid, serviceContext);
	}

	private static final Log _log = LogFactoryUtil.getLog(JournalPortlet.class);

	@Reference
	private AssetDisplayPageEntryFormProcessor
		_assetDisplayPageEntryFormProcessor;

	@Reference
	private AssetDisplayPageFriendlyURLProvider
		_assetDisplayPageFriendlyURLProvider;

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private AssetEntryUsageLocalService _assetEntryUsageLocalService;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private DDMTemplateHelper _ddmTemplateHelper;

	@Reference
	private Http _http;

	@Reference
	private ItemSelector _itemSelector;

	@Reference
	private JournalArticleService _journalArticleService;

	@Reference
	private JournalContent _journalContent;

	@Reference
	private JournalContentSearchLocalService _journalContentSearchLocalService;

	@Reference
	private JournalConverter _journalConverter;

	@Reference
	private JournalDDMTemplateUtil _journalDDMTemplateUtil;

	@Reference
	private JournalFeedService _journalFeedService;

	private volatile JournalFileUploadsConfiguration
		_journalFileUploadsConfiguration;

	@Reference
	private JournalFolderService _journalFolderService;

	@Reference
	private JournalHelper _journalHelper;

	private volatile JournalWebConfiguration _journalWebConfiguration;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private TrashEntryService _trashEntryService;

	@Reference
	private TrashHelper _trashHelper;

}