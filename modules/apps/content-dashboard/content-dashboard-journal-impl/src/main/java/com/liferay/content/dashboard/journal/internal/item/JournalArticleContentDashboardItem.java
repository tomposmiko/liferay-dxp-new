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

package com.liferay.content.dashboard.journal.internal.item;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.content.dashboard.item.ContentDashboardItemVersion;
import com.liferay.content.dashboard.item.VersionableContentDashboardItem;
import com.liferay.content.dashboard.item.action.ContentDashboardItemAction;
import com.liferay.content.dashboard.item.action.ContentDashboardItemActionProviderRegistry;
import com.liferay.content.dashboard.item.action.ContentDashboardItemVersionAction;
import com.liferay.content.dashboard.item.action.ContentDashboardItemVersionActionProviderRegistry;
import com.liferay.content.dashboard.item.action.exception.ContentDashboardItemActionException;
import com.liferay.content.dashboard.item.action.exception.ContentDashboardItemVersionActionException;
import com.liferay.content.dashboard.item.action.provider.ContentDashboardItemActionProvider;
import com.liferay.content.dashboard.item.action.provider.ContentDashboardItemVersionActionProvider;
import com.liferay.content.dashboard.item.type.ContentDashboardItemSubtype;
import com.liferay.info.field.InfoFieldValue;
import com.liferay.info.item.InfoItemClassDetails;
import com.liferay.info.item.InfoItemFieldValues;
import com.liferay.info.item.InfoItemReference;
import com.liferay.info.item.provider.InfoItemFieldValuesProvider;
import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.journal.util.comparator.ArticleVersionComparator;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Cristina González
 */
public class JournalArticleContentDashboardItem
	implements VersionableContentDashboardItem<JournalArticle> {

	public JournalArticleContentDashboardItem(
		List<AssetCategory> assetCategories, List<AssetTag> assetTags,
		ContentDashboardItemActionProviderRegistry
			contentDashboardItemActionProviderRegistry,
		ContentDashboardItemVersionActionProviderRegistry
			contentDashboardItemVersionActionProviderRegistry,
		ContentDashboardItemSubtype contentDashboardItemSubtype, Group group,
		InfoItemFieldValuesProvider<JournalArticle> infoItemFieldValuesProvider,
		JournalArticle journalArticle,
		JournalArticleService journalArticleService, Language language,
		JournalArticle latestApprovedJournalArticle, Portal portal) {

		if (ListUtil.isEmpty(assetCategories)) {
			_assetCategories = Collections.emptyList();
		}
		else {
			_assetCategories = Collections.unmodifiableList(assetCategories);
		}

		if (ListUtil.isEmpty(assetTags)) {
			_assetTags = Collections.emptyList();
		}
		else {
			_assetTags = Collections.unmodifiableList(assetTags);
		}

		_contentDashboardItemActionProviderRegistry =
			contentDashboardItemActionProviderRegistry;
		_contentDashboardItemVersionActionProviderRegistry =
			contentDashboardItemVersionActionProviderRegistry;
		_contentDashboardItemSubtype = contentDashboardItemSubtype;
		_group = group;
		_infoItemFieldValuesProvider = infoItemFieldValuesProvider;
		_journalArticle = journalArticle;
		_journalArticleService = journalArticleService;
		_language = language;

		if (!journalArticle.equals(latestApprovedJournalArticle)) {
			_latestApprovedJournalArticle = latestApprovedJournalArticle;
		}
		else {
			_latestApprovedJournalArticle = null;
		}

		_portal = portal;
	}

	@Override
	public List<ContentDashboardItemVersion> getAllContentDashboardItemVersions(
		HttpServletRequest httpServletRequest) {

		int status = WorkflowConstants.STATUS_APPROVED;

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		User user = themeDisplay.getUser();

		if ((user.getUserId() == _journalArticle.getUserId()) ||
			permissionChecker.isContentReviewer(
				user.getCompanyId(), themeDisplay.getScopeGroupId())) {

			status = WorkflowConstants.STATUS_ANY;
		}

		List<JournalArticle> journalArticles =
			_journalArticleService.getArticlesByArticleId(
				_journalArticle.getGroupId(), _journalArticle.getArticleId(),
				status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				new ArticleVersionComparator());

		return ListUtil.toList(
			journalArticles,
			journalArticle -> new ContentDashboardItemVersion(
				null,
				_getContentDashboardItemVersionActions(
					httpServletRequest, journalArticle),
				journalArticle.getStatusDate(),
				_language.get(
					themeDisplay.getLocale(),
					WorkflowConstants.getStatusLabel(
						journalArticle.getStatus())),
				themeDisplay.getLocale(),
				WorkflowConstants.getStatusStyle(journalArticle.getStatus()),
				journalArticle.getUserName(),
				String.valueOf(journalArticle.getVersion())));
	}

	@Override
	public List<AssetCategory> getAssetCategories() {
		return _assetCategories;
	}

	@Override
	public List<AssetCategory> getAssetCategories(long assetVocabularyId) {
		return ListUtil.filter(
			_assetCategories,
			assetCategory ->
				assetCategory.getVocabularyId() == assetVocabularyId);
	}

	@Override
	public List<AssetTag> getAssetTags() {
		return _assetTags;
	}

	@Override
	public List<Locale> getAvailableLocales() {
		return TransformUtil.transformToList(
			_journalArticle.getAvailableLanguageIds(),
			LocaleUtil::fromLanguageId);
	}

	@Override
	public List<ContentDashboardItemAction> getContentDashboardItemActions(
		HttpServletRequest httpServletRequest,
		ContentDashboardItemAction.Type... types) {

		return TransformUtil.transform(
			_contentDashboardItemActionProviderRegistry.
				getContentDashboardItemActionProviders(
					JournalArticle.class.getName(), types),
			contentDashboardItemActionProvider -> {
				try {
					return contentDashboardItemActionProvider.
						getContentDashboardItemAction(
							_journalArticle, httpServletRequest);
				}
				catch (ContentDashboardItemActionException
							contentDashboardItemActionException) {

					_log.error(contentDashboardItemActionException);
				}

				return null;
			});
	}

	@Override
	public ContentDashboardItemSubtype getContentDashboardItemSubtype() {
		return _contentDashboardItemSubtype;
	}

	@Override
	public Date getCreateDate() {
		return _journalArticle.getCreateDate();
	}

	@Override
	public ContentDashboardItemAction getDefaultContentDashboardItemAction(
		HttpServletRequest httpServletRequest) {

		long userId = _portal.getUserId(httpServletRequest);

		Locale locale = _portal.getLocale(httpServletRequest);

		ContentDashboardItemVersion contentDashboardItemVersion =
			_getLastContentDashboardItemVersion(locale);

		if ((getUserId() == userId) &&
			Objects.equals(
				contentDashboardItemVersion.getLabel(),
				_language.get(
					locale,
					WorkflowConstants.getStatusLabel(
						WorkflowConstants.STATUS_DRAFT)))) {

			ContentDashboardItemActionProvider
				contentDashboardItemActionProvider =
					_contentDashboardItemActionProviderRegistry.
						getContentDashboardItemActionProvider(
							JournalArticle.class.getName(),
							ContentDashboardItemAction.Type.EDIT);

			if (contentDashboardItemActionProvider == null) {
				return null;
			}

			ContentDashboardItemAction contentDashboardItemAction =
				_toContentDashboardItemAction(
					contentDashboardItemActionProvider, httpServletRequest);

			if (contentDashboardItemAction == null) {
				return null;
			}

			return contentDashboardItemAction;
		}

		ContentDashboardItemActionProvider
			viewContentDashboardItemActionProvider =
				_contentDashboardItemActionProviderRegistry.
					getContentDashboardItemActionProvider(
						JournalArticle.class.getName(),
						ContentDashboardItemAction.Type.VIEW);

		if (viewContentDashboardItemActionProvider == null) {
			return _getContentDashboardItemAction(httpServletRequest);
		}

		ContentDashboardItemAction contentDashboardItemAction =
			_toContentDashboardItemAction(
				viewContentDashboardItemActionProvider, httpServletRequest);

		if (contentDashboardItemAction == null) {
			return _getContentDashboardItemAction(httpServletRequest);
		}

		return contentDashboardItemAction;
	}

	@Override
	public Locale getDefaultLocale() {
		return LocaleUtil.fromLanguageId(
			_journalArticle.getDefaultLanguageId());
	}

	@Override
	public String getDescription(Locale locale) {
		return _getStringValue("description", locale);
	}

	@Override
	public long getId() {
		return _journalArticle.getResourcePrimKey();
	}

	@Override
	public InfoItemReference getInfoItemReference() {
		return new InfoItemReference(
			JournalArticle.class.getName(),
			_journalArticle.getResourcePrimKey());
	}

	@Override
	public List<ContentDashboardItemVersion>
		getLatestContentDashboardItemVersions(Locale locale) {

		List<ContentDashboardItemVersion> contentDashboardItemVersions =
			new ArrayList<>();

		ContentDashboardItemVersion contentDashboardItemVersion = _toVersion(
			_journalArticle, locale);

		if (contentDashboardItemVersion != null) {
			contentDashboardItemVersions.add(contentDashboardItemVersion);
		}

		contentDashboardItemVersion = _toVersion(
			_latestApprovedJournalArticle, locale);

		if (contentDashboardItemVersion != null) {
			contentDashboardItemVersions.add(contentDashboardItemVersion);
		}

		contentDashboardItemVersions.sort(
			Comparator.comparing(ContentDashboardItemVersion::getVersion));

		return contentDashboardItemVersions;
	}

	@Override
	public Date getModifiedDate() {
		return _journalArticle.getModifiedDate();
	}

	@Override
	public Date getReviewDate() {
		return _journalArticle.getReviewDate();
	}

	@Override
	public String getScopeName(Locale locale) {
		if (_group == null) {
			return StringPool.BLANK;
		}

		String scopeName = null;

		try {
			scopeName = _group.getDescriptiveName(locale);
		}
		catch (PortalException portalException) {
			_log.error(portalException);
		}

		if (scopeName == null) {
			return _group.getName(locale);
		}

		return scopeName;
	}

	@Override
	public List<SpecificInformation<?>> getSpecificInformationList(
		Locale locale) {

		return Arrays.asList(
			new SpecificInformation<>(
				"display-date", SpecificInformation.Type.DATE,
				_journalArticle.getDisplayDate()),
			new SpecificInformation<>(
				"expiration-date", SpecificInformation.Type.DATE,
				_journalArticle.getExpirationDate()),
			new SpecificInformation<>(
				"review-date", SpecificInformation.Type.DATE,
				_journalArticle.getReviewDate()));
	}

	@Override
	public String getTitle(Locale locale) {
		return _journalArticle.getTitle(locale);
	}

	@Override
	public String getTypeLabel(Locale locale) {
		InfoItemClassDetails infoItemClassDetails = new InfoItemClassDetails(
			JournalArticle.class.getName());

		return infoItemClassDetails.getLabel(locale);
	}

	@Override
	public long getUserId() {
		if (_latestApprovedJournalArticle != null) {
			return _latestApprovedJournalArticle.getUserId();
		}

		return _journalArticle.getUserId();
	}

	@Override
	public String getUserName() {
		if (_latestApprovedJournalArticle != null) {
			return _latestApprovedJournalArticle.getUserName();
		}

		return _journalArticle.getUserName();
	}

	@Override
	public String getViewVersionsURL(HttpServletRequest httpServletRequest) {
		return PortletURLBuilder.create(
			_portal.getControlPanelPortletURL(
				httpServletRequest,
				GroupLocalServiceUtil.fetchGroup(_journalArticle.getGroupId()),
				JournalPortletKeys.JOURNAL, 0, 0, PortletRequest.RENDER_PHASE)
		).setMVCPath(
			"/view_article_history.jsp"
		).setBackURL(
			() -> {
				LiferayPortletResponse liferayPortletResponse =
					_portal.getLiferayPortletResponse(
						(PortletResponse)httpServletRequest.getAttribute(
							JavaConstants.JAVAX_PORTLET_RESPONSE));

				return liferayPortletResponse.createRenderURL();
			}
		).setParameter(
			"articleId", _journalArticle.getArticleId()
		).buildString();
	}

	@Override
	public boolean isViewable(HttpServletRequest httpServletRequest) {
		if (!_journalArticle.hasApprovedVersion()) {
			return false;
		}

		ContentDashboardItemActionProvider contentDashboardItemActionProvider =
			_contentDashboardItemActionProviderRegistry.
				getContentDashboardItemActionProvider(
					JournalArticle.class.getName(),
					ContentDashboardItemAction.Type.VIEW);

		if (contentDashboardItemActionProvider == null) {
			return false;
		}

		return contentDashboardItemActionProvider.isShow(
			_journalArticle, httpServletRequest);
	}

	private ContentDashboardItemAction _getContentDashboardItemAction(
		HttpServletRequest httpServletRequest) {

		ContentDashboardItemActionProvider
			editContentDashboardItemActionProvider =
				_contentDashboardItemActionProviderRegistry.
					getContentDashboardItemActionProvider(
						JournalArticle.class.getName(),
						ContentDashboardItemAction.Type.EDIT);

		if (editContentDashboardItemActionProvider == null) {
			return null;
		}

		ContentDashboardItemAction contentDashboardItemAction =
			_toContentDashboardItemAction(
				editContentDashboardItemActionProvider, httpServletRequest);

		if (contentDashboardItemAction == null) {
			return null;
		}

		return contentDashboardItemAction;
	}

	private List<ContentDashboardItemVersionAction>
		_getContentDashboardItemVersionActions(
			HttpServletRequest httpServletRequest,
			JournalArticle journalArticleVersion) {

		List<ContentDashboardItemVersionAction>
			contentDashboardItemVersionActions = new ArrayList<>();

		List<ContentDashboardItemVersionActionProvider>
			contentDashboardItemVersionActionProviders =
				_contentDashboardItemVersionActionProviderRegistry.
					getContentDashboardItemVersionActionProviders(
						JournalArticle.class.getName());

		for (ContentDashboardItemVersionActionProvider
				contentDashboardItemVersionActionProvider :
					contentDashboardItemVersionActionProviders) {

			if (!contentDashboardItemVersionActionProvider.isShow(
					journalArticleVersion, httpServletRequest)) {

				continue;
			}

			try {
				ContentDashboardItemVersionAction
					contentDashboardItemVersionAction =
						contentDashboardItemVersionActionProvider.
							getContentDashboardItemVersionAction(
								journalArticleVersion, httpServletRequest);

				if (contentDashboardItemVersionAction != null) {
					contentDashboardItemVersionActions.add(
						contentDashboardItemVersionAction);
				}
			}
			catch (ContentDashboardItemVersionActionException
						contentDashboardItemVersionActionException) {

				_log.error(contentDashboardItemVersionActionException);
			}
		}

		return contentDashboardItemVersionActions;
	}

	private ContentDashboardItemVersion _getLastContentDashboardItemVersion(
		Locale locale) {

		List<ContentDashboardItemVersion> contentDashboardItemVersions =
			getLatestContentDashboardItemVersions(locale);

		return contentDashboardItemVersions.get(
			contentDashboardItemVersions.size() - 1);
	}

	private String _getStringValue(String infoFieldName, Locale locale) {
		InfoItemFieldValues infoItemFieldValues =
			_infoItemFieldValuesProvider.getInfoItemFieldValues(
				_journalArticle);

		InfoFieldValue<Object> infoFieldValue =
			infoItemFieldValues.getInfoFieldValue(infoFieldName);

		if (infoFieldValue == null) {
			return StringPool.BLANK;
		}

		Object value = infoFieldValue.getValue(locale);

		if (value == null) {
			return StringPool.BLANK;
		}

		return value.toString();
	}

	private ContentDashboardItemAction _toContentDashboardItemAction(
		ContentDashboardItemActionProvider contentDashboardItemActionProvider,
		HttpServletRequest httpServletRequest) {

		try {
			return contentDashboardItemActionProvider.
				getContentDashboardItemAction(
					_journalArticle, httpServletRequest);
		}
		catch (ContentDashboardItemActionException
					contentDashboardItemActionException) {

			_log.error(contentDashboardItemActionException);

			return null;
		}
	}

	private ContentDashboardItemVersion _toVersion(
		JournalArticle journalArticle, Locale locale) {

		if (journalArticle == null) {
			return null;
		}

		return new ContentDashboardItemVersion(
			null, null, journalArticle.getCreateDate(),
			_language.get(
				locale,
				WorkflowConstants.getStatusLabel(journalArticle.getStatus())),
			null, WorkflowConstants.getStatusStyle(journalArticle.getStatus()),
			journalArticle.getUserName(),
			String.valueOf(journalArticle.getVersion()));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JournalArticleContentDashboardItem.class);

	private final List<AssetCategory> _assetCategories;
	private final List<AssetTag> _assetTags;
	private final ContentDashboardItemActionProviderRegistry
		_contentDashboardItemActionProviderRegistry;
	private final ContentDashboardItemSubtype _contentDashboardItemSubtype;
	private final ContentDashboardItemVersionActionProviderRegistry
		_contentDashboardItemVersionActionProviderRegistry;
	private final Group _group;
	private final InfoItemFieldValuesProvider<JournalArticle>
		_infoItemFieldValuesProvider;
	private final JournalArticle _journalArticle;
	private final JournalArticleService _journalArticleService;
	private final Language _language;
	private final JournalArticle _latestApprovedJournalArticle;
	private final Portal _portal;

}