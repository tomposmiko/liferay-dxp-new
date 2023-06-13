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

package com.liferay.headless.delivery.internal.resource.v1_0;

import com.liferay.client.extension.constants.ClientExtensionEntryConstants;
import com.liferay.client.extension.service.ClientExtensionEntryRelLocalService;
import com.liferay.client.extension.type.CET;
import com.liferay.client.extension.type.manager.CETManager;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureService;
import com.liferay.friendly.url.model.FriendlyURLEntryLocalization;
import com.liferay.friendly.url.service.FriendlyURLEntryLocalService;
import com.liferay.headless.common.spi.service.context.ServiceContextRequestUtil;
import com.liferay.headless.delivery.dto.v1_0.ClientExtension;
import com.liferay.headless.delivery.dto.v1_0.ContentDocument;
import com.liferay.headless.delivery.dto.v1_0.CustomMetaTag;
import com.liferay.headless.delivery.dto.v1_0.MasterPage;
import com.liferay.headless.delivery.dto.v1_0.OpenGraphSettings;
import com.liferay.headless.delivery.dto.v1_0.PageDefinition;
import com.liferay.headless.delivery.dto.v1_0.PagePermission;
import com.liferay.headless.delivery.dto.v1_0.PageSettings;
import com.liferay.headless.delivery.dto.v1_0.ParentSitePage;
import com.liferay.headless.delivery.dto.v1_0.SEOSettings;
import com.liferay.headless.delivery.dto.v1_0.Settings;
import com.liferay.headless.delivery.dto.v1_0.SiteMapSettings;
import com.liferay.headless.delivery.dto.v1_0.SitePage;
import com.liferay.headless.delivery.dto.v1_0.SitePageNavigationMenuSettings;
import com.liferay.headless.delivery.dto.v1_0.StyleBook;
import com.liferay.headless.delivery.dto.v1_0.util.CustomFieldsUtil;
import com.liferay.headless.delivery.internal.odata.entity.v1_0.SitePageEntityModel;
import com.liferay.headless.delivery.resource.v1_0.SitePageResource;
import com.liferay.layout.admin.kernel.model.LayoutTypePortletConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalService;
import com.liferay.layout.seo.model.LayoutSEOEntry;
import com.liferay.layout.seo.service.LayoutSEOEntryService;
import com.liferay.layout.util.LayoutCopyHelper;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.events.ServicePreAction;
import com.liferay.portal.events.ThemeServicePreAction;
import com.liferay.portal.kernel.change.tracking.CTAware;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Team;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.TeamLocalService;
import com.liferay.portal.kernel.service.ThemeLocalService;
import com.liferay.portal.kernel.service.permission.ModelPermissions;
import com.liferay.portal.kernel.service.permission.ModelPermissionsFactory;
import com.liferay.portal.kernel.servlet.DummyHttpServletResponse;
import com.liferay.portal.kernel.servlet.DynamicServletRequest;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.theme.ThemeUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.vulcan.aggregation.Aggregation;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.util.JaxRsLinkUtil;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;
import com.liferay.portal.vulcan.util.SearchUtil;
import com.liferay.segments.SegmentsEntryRetriever;
import com.liferay.segments.constants.SegmentsExperienceConstants;
import com.liferay.segments.constants.SegmentsWebKeys;
import com.liferay.segments.context.RequestContextMapper;
import com.liferay.segments.model.SegmentsExperience;
import com.liferay.segments.processor.SegmentsExperienceRequestProcessorRegistry;
import com.liferay.segments.service.SegmentsExperienceLocalService;
import com.liferay.segments.service.SegmentsExperienceService;
import com.liferay.style.book.model.StyleBookEntry;
import com.liferay.style.book.service.StyleBookEntryLocalService;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.MultivaluedMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/site-page.properties",
	scope = ServiceScope.PROTOTYPE, service = SitePageResource.class
)
@CTAware
public class SitePageResourceImpl extends BaseSitePageResourceImpl {

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap) {
		return _entityModel;
	}

	@Override
	public SitePage getSiteSitePage(Long siteId, String friendlyUrlPath)
		throws Exception {

		return _toSitePage(true, _getLayout(siteId, friendlyUrlPath), null);
	}

	@Override
	public SitePage getSiteSitePageExperienceExperienceKey(
			Long siteId, String friendlyUrlPath, String experienceKey)
		throws Exception {

		return _toSitePage(
			true, _getLayout(siteId, friendlyUrlPath), experienceKey);
	}

	@Override
	public String getSiteSitePageExperienceExperienceKeyRenderedPage(
			Long siteId, String friendlyUrlPath, String experienceKey)
		throws Exception {

		return _toHTML(friendlyUrlPath, siteId, experienceKey);
	}

	@Override
	public String getSiteSitePageRenderedPage(
			Long siteId, String friendlyUrlPath)
		throws Exception {

		return _toHTML(friendlyUrlPath, siteId, null);
	}

	@Override
	public Page<SitePage> getSiteSitePagesExperiencesPage(
			Long siteId, String friendlyUrlPath)
		throws Exception {

		Layout layout = _getLayout(siteId, friendlyUrlPath);

		return Page.of(
			transform(
				_getSegmentsExperiences(layout),
				segmentsExperience -> _toSitePage(
					_isEmbeddedPageDefinition(), layout,
					segmentsExperience.getSegmentsExperienceKey())));
	}

	@Override
	public Page<SitePage> getSiteSitePagesPage(
			Long siteId, String search, Aggregation aggregation, Filter filter,
			Pagination pagination, Sort[] sorts)
		throws Exception {

		return SearchUtil.search(
			HashMapBuilder.<String, Map<String, String>>put(
				"get",
				HashMapBuilder.put(
					"href",
					JaxRsLinkUtil.getJaxRsLink(
						"headless-delivery", BaseSitePageResourceImpl.class,
						"getSiteSitePagesPage", contextUriInfo, siteId)
				).put(
					"method", "GET"
				).build()
			).build(),
			booleanQuery -> {
				BooleanFilter booleanFilter =
					booleanQuery.getPreBooleanFilter();

				booleanFilter.add(
					new TermFilter(Field.GROUP_ID, String.valueOf(siteId)),
					BooleanClauseOccur.MUST);
			},
			filter, Layout.class.getName(), search, pagination,
			queryConfig -> queryConfig.setSelectedFieldNames(
				Field.ENTRY_CLASS_PK),
			searchContext -> {
				searchContext.addVulcanAggregation(aggregation);
				searchContext.setAttribute(Field.TITLE, search);
				searchContext.setAttribute(
					Field.TYPE,
					new String[] {
						LayoutConstants.TYPE_COLLECTION,
						LayoutConstants.TYPE_CONTENT,
						LayoutConstants.TYPE_EMBEDDED,
						LayoutConstants.TYPE_LINK_TO_LAYOUT,
						LayoutConstants.TYPE_FULL_PAGE_APPLICATION,
						LayoutConstants.TYPE_PANEL,
						LayoutConstants.TYPE_PORTLET, LayoutConstants.TYPE_URL
					});
				searchContext.setAttribute(
					"privateLayout", Boolean.FALSE.toString());
				searchContext.setCompanyId(contextCompany.getCompanyId());
				searchContext.setGroupIds(new long[] {siteId});
				searchContext.setKeywords(search);
			},
			sorts,
			document -> {
				long plid = GetterUtil.getLong(
					document.get(Field.ENTRY_CLASS_PK));

				return _toSitePage(
					_isEmbeddedPageDefinition(),
					_layoutLocalService.getLayout(plid), null);
			});
	}

	@Override
	public SitePage postSiteSitePage(Long siteId, SitePage sitePage)
		throws Exception {

		if (!FeatureFlagManagerUtil.isEnabled("LPS-178052")) {
			throw new UnsupportedOperationException();
		}

		Map<Locale, String> titleMap = LocalizedMapUtil.getLocalizedMap(
			contextAcceptLanguage.getPreferredLocale(), sitePage.getTitle(),
			sitePage.getTitle_i18n());

		Layout layout = _addLayout(
			siteId, sitePage, titleMap,
			LocalizedMapUtil.getLocalizedMap(
				contextAcceptLanguage.getPreferredLocale(),
				sitePage.getFriendlyUrlPath(),
				sitePage.getFriendlyUrlPath_i18n(), titleMap));

		DefaultDTOConverterContext dtoConverterContext =
			new DefaultDTOConverterContext(
				contextAcceptLanguage.isAcceptAllLanguages(),
				_getBasicActions(layout), _dtoConverterRegistry,
				contextHttpServletRequest, layout.getPlid(),
				contextAcceptLanguage.getPreferredLocale(), contextUriInfo,
				contextUser);

		return _sitePageDTOConverter.toDTO(dtoConverterContext, layout);
	}

	private void _addClientExtensionEntryRel(
		String cetExternalReferenceCode, Layout layout,
		ServiceContext serviceContext, String type) {

		CET cet = _cetManager.getCET(
			layout.getCompanyId(), cetExternalReferenceCode);

		if ((cet == null) || !Objects.equals(type, cet.getType())) {
			return;
		}

		try {
			_clientExtensionEntryRelLocalService.addClientExtensionEntryRel(
				contextUser.getUserId(), layout.getGroupId(),
				_portal.getClassNameId(Layout.class.getName()),
				layout.getPlid(), cetExternalReferenceCode, type, null,
				serviceContext);
		}
		catch (PortalException portalException) {
			_log.error(portalException);
		}
	}

	private Layout _addLayout(
			Long siteId, SitePage sitePage, Map<Locale, String> nameMap,
			Map<Locale, String> friendlyUrlMap)
		throws Exception {

		long parentLayoutId = 0;

		ParentSitePage parentSitePage = sitePage.getParentSitePage();

		if (parentSitePage != null) {
			Layout layout = _layoutLocalService.fetchLayoutByFriendlyURL(
				siteId, false, parentSitePage.getFriendlyUrlPath());

			if (layout == null) {
				if (_log.isWarnEnabled()) {
					_log.warn("Could not find parent site page");
				}
			}
			else {
				parentLayoutId = layout.getLayoutId();
			}
		}

		Map<Locale, String> titleMap = new HashMap<>();
		Map<Locale, String> descriptionMap = new HashMap<>();
		Map<Locale, String> keywordsMap = new HashMap<>();
		Map<Locale, String> robotsMap = new HashMap<>();
		UnicodeProperties typeSettingsUnicodeProperties =
			new UnicodeProperties();
		boolean hidden = false;

		PageSettings pageSettings = sitePage.getPageSettings();

		if (pageSettings != null) {
			SEOSettings seoSettings = pageSettings.getSeoSettings();

			if (seoSettings != null) {
				titleMap = LocalizedMapUtil.getLocalizedMap(
					contextAcceptLanguage.getPreferredLocale(),
					seoSettings.getHtmlTitle(),
					seoSettings.getHtmlTitle_i18n());
				descriptionMap = LocalizedMapUtil.getLocalizedMap(
					contextAcceptLanguage.getPreferredLocale(),
					seoSettings.getDescription(),
					seoSettings.getDescription_i18n());
				keywordsMap = LocalizedMapUtil.getLocalizedMap(
					contextAcceptLanguage.getPreferredLocale(),
					seoSettings.getSeoKeywords(),
					seoSettings.getSeoKeywords_i18n());
				robotsMap = LocalizedMapUtil.getLocalizedMap(
					contextAcceptLanguage.getPreferredLocale(),
					seoSettings.getRobots(), seoSettings.getRobots_i18n());

				SiteMapSettings siteMapSettings =
					seoSettings.getSiteMapSettings();

				if (siteMapSettings != null) {
					SiteMapSettings.ChangeFrequency changeFrequency =
						siteMapSettings.getChangeFrequency();

					if (changeFrequency != null) {
						typeSettingsUnicodeProperties.setProperty(
							LayoutTypePortletConstants.SITEMAP_CHANGEFREQ,
							StringUtil.toLowerCase(changeFrequency.getValue()));
					}

					Boolean include = siteMapSettings.getInclude();

					if (include != null) {
						String siteMapInclude = "0";

						if (include) {
							siteMapInclude = "1";
						}

						typeSettingsUnicodeProperties.setProperty(
							LayoutTypePortletConstants.SITEMAP_INCLUDE,
							siteMapInclude);
					}

					Double pagePriority = siteMapSettings.getPagePriority();

					if (pagePriority != null) {
						typeSettingsUnicodeProperties.setProperty(
							LayoutTypePortletConstants.SITEMAP_PRIORITY,
							String.valueOf(pagePriority));
					}
				}
			}

			SitePageNavigationMenuSettings sitePageNavigationMenuSettings =
				pageSettings.getSitePageNavigationMenuSettings();

			if (sitePageNavigationMenuSettings != null) {
				String queryString =
					sitePageNavigationMenuSettings.getQueryString();

				if (Validator.isNotNull(queryString)) {
					typeSettingsUnicodeProperties.setProperty(
						LayoutTypePortletConstants.QUERY_STRING, queryString);
				}

				String target = sitePageNavigationMenuSettings.getTarget();
				SitePageNavigationMenuSettings.TargetType targetType =
					sitePageNavigationMenuSettings.getTargetType();

				if (Validator.isNotNull(target)) {
					typeSettingsUnicodeProperties.setProperty(
						LayoutTypePortletConstants.TARGET, target);
				}

				if ((targetType != null) &&
					(targetType ==
						SitePageNavigationMenuSettings.TargetType.NEW_TAB)) {

					typeSettingsUnicodeProperties.setProperty(
						"targetType", "useNewTab");
				}
			}

			hidden = GetterUtil.getBoolean(
				pageSettings.getHiddenFromNavigation());
		}

		Layout layout = _layoutService.addLayout(
			siteId, false, parentLayoutId, nameMap, titleMap, descriptionMap,
			keywordsMap, robotsMap, LayoutConstants.TYPE_CONTENT,
			typeSettingsUnicodeProperties.toString(), hidden, friendlyUrlMap, 0,
			_createServiceContext(siteId, sitePage));

		layout = _updateLayoutSettings(layout, sitePage.getPageDefinition());

		Layout draftLayout = _updateDraftLayout(layout);

		layout.setModifiedDate(draftLayout.getModifiedDate());

		layout.setStatus(WorkflowConstants.STATUS_APPROVED);

		layout = _layoutLocalService.updateLayout(layout);

		_updateModelResourcePermissions(
			layout.getCompanyId(), siteId, layout.getPlid(), sitePage);

		_updateSEOEntry(
			layout.getCompanyId(), siteId, layout.getLayoutId(), sitePage);

		return layout;
	}

	private ServiceContext _createServiceContext(
		long groupId, SitePage sitePage) {

		Long[] assetCategoryIds = {};

		if (sitePage.getTaxonomyCategoryIds() != null) {
			assetCategoryIds = sitePage.getTaxonomyCategoryIds();
		}

		String[] assetTagNames = new String[0];

		if (sitePage.getKeywords() != null) {
			assetTagNames = sitePage.getKeywords();
		}

		return ServiceContextRequestUtil.createServiceContext(
			assetCategoryIds, assetTagNames,
			_getExpandoBridgeAttributes(sitePage), groupId,
			contextHttpServletRequest, null);
	}

	private Map<String, Map<String, String>> _getBasicActions(Layout layout) {
		return HashMapBuilder.<String, Map<String, String>>put(
			"get",
			addAction(
				ActionKeys.VIEW, layout.getPlid(), "getSiteSitePage", null,
				Layout.class.getName(), layout.getGroupId())
		).put(
			"get-experiences",
			() -> {
				if (!layout.isTypeContent()) {
					return null;
				}

				return addAction(
					ActionKeys.VIEW, "getSiteSitePagesExperiencesPage",
					Group.class.getName(), layout.getGroupId());
			}
		).put(
			"get-rendered-page",
			addAction(
				ActionKeys.VIEW, layout.getPlid(),
				"getSiteSitePageRenderedPage", null, Layout.class.getName(),
				layout.getGroupId())
		).build();
	}

	private String _getDDMFormValues(PageSettings pageSettings) {
		CustomMetaTag[] customMetaTags = pageSettings.getCustomMetaTags();

		if (ArrayUtil.isEmpty(customMetaTags)) {
			return null;
		}

		JSONObject ddmFormValuesJSONObject = JSONUtil.put(
			"defaultLanguageId",
			contextAcceptLanguage.getPreferredLanguageId());

		JSONArray fieldValuesJSONArray = _jsonFactory.createJSONArray();

		Set<String> availableLanguageIds = new HashSet<>();

		for (CustomMetaTag customMetaTag : customMetaTags) {
			JSONObject fieldValueJSONObject = JSONUtil.put(
				"instanceId", StringUtil.randomString(8)
			).put(
				"name", "property"
			).put(
				"value", customMetaTag.getKey()
			);

			JSONObject nestedFieldValueJSONObject = JSONUtil.put(
				"instanceId", StringUtil.randomString(8)
			).put(
				"name", "content"
			);

			Map<Locale, String> valuesMap = LocalizedMapUtil.getLocalizedMap(
				contextAcceptLanguage.getPreferredLocale(),
				customMetaTag.getValue(), customMetaTag.getValue_i18n());

			JSONObject valueJSONObject = _jsonFactory.createJSONObject();

			for (Map.Entry<Locale, String> entry : valuesMap.entrySet()) {
				String key = LocaleUtil.toLanguageId(entry.getKey());

				valueJSONObject.put(key, entry.getValue());

				availableLanguageIds.add(key);
			}

			nestedFieldValueJSONObject.put("value", valueJSONObject);

			JSONArray nestedFieldValuesJSONArray = JSONUtil.put(
				nestedFieldValueJSONObject);

			fieldValueJSONObject.put(
				"nestedFieldValues", nestedFieldValuesJSONArray);

			fieldValuesJSONArray.put(fieldValueJSONObject);
		}

		ddmFormValuesJSONObject.put("fieldValues", fieldValuesJSONArray);

		JSONArray availableLanguageIdsJSONArray =
			_jsonFactory.createJSONArray();

		for (String availableLanguage : availableLanguageIds) {
			availableLanguageIdsJSONArray.put(availableLanguage);
		}

		ddmFormValuesJSONObject.put(
			"availableLanguageIds", availableLanguageIdsJSONArray);

		return ddmFormValuesJSONObject.toString();
	}

	private long _getDDMStructurePrimaryKey(long companyId) throws Exception {
		Company company = _companyLocalService.getCompany(companyId);

		DDMStructure ddmStructure = _ddmStructureService.getStructure(
			company.getGroupId(),
			_portal.getClassNameId(LayoutSEOEntry.class.getName()),
			"custom-meta-tags");

		return ddmStructure.getPrimaryKey();
	}

	private Map<String, Serializable> _getExpandoBridgeAttributes(
		SitePage sitePage) {

		return CustomFieldsUtil.toMap(
			Layout.class.getName(), contextCompany.getCompanyId(),
			sitePage.getCustomFields(),
			contextAcceptLanguage.getPreferredLocale());
	}

	private Map<String, Map<String, String>> _getExperienceActions(
		Layout layout) {

		return HashMapBuilder.<String, Map<String, String>>put(
			"get",
			addAction(
				ActionKeys.VIEW, "getSiteSitePageExperienceExperienceKey",
				Group.class.getName(), layout.getGroupId())
		).put(
			"get-rendered-page",
			addAction(
				ActionKeys.VIEW,
				"getSiteSitePageExperienceExperienceKeyRenderedPage",
				Group.class.getName(), layout.getGroupId())
		).build();
	}

	private long _getFileEntryId(long contentDocumentId) {
		try {
			FileEntry fileEntry = _dlAppService.getFileEntry(contentDocumentId);

			return fileEntry.getFileEntryId();
		}
		catch (PortalException portalException) {
			if (_log.isWarnEnabled()) {
				_log.warn(portalException);
			}
		}

		return 0;
	}

	private Layout _getLayout(long groupId, String friendlyUrlPath)
		throws Exception {

		String resourceName = ResourceActionsUtil.getCompositeModelName(
			Layout.class.getName(), "false");

		if (!StringUtil.startsWith(friendlyUrlPath, StringPool.FORWARD_SLASH)) {
			friendlyUrlPath = StringPool.FORWARD_SLASH + friendlyUrlPath;
		}

		FriendlyURLEntryLocalization friendlyURLEntryLocalization =
			_friendlyURLEntryLocalService.getFriendlyURLEntryLocalization(
				groupId, _portal.getClassNameId(resourceName), friendlyUrlPath);

		return _layoutLocalService.getLayout(
			friendlyURLEntryLocalization.getClassPK());
	}

	private SegmentsExperience _getSegmentsExperience(
			Layout layout, String segmentsExperienceKey)
		throws Exception {

		if (Validator.isNull(segmentsExperienceKey)) {
			return _getUserSegmentsExperience(layout);
		}

		return _segmentsExperienceService.fetchSegmentsExperience(
			layout.getGroupId(), segmentsExperienceKey, layout.getPlid());
	}

	private List<SegmentsExperience> _getSegmentsExperiences(Layout layout)
		throws Exception {

		if (!layout.isTypeContent()) {
			return Collections.emptyList();
		}

		return _segmentsExperienceLocalService.getSegmentsExperiences(
			layout.getGroupId(), layout.getPlid(), true);
	}

	private ThemeDisplay _getThemeDisplay(Layout layout) throws Exception {
		ServicePreAction servicePreAction = new ServicePreAction();

		HttpServletResponse httpServletResponse =
			new DummyHttpServletResponse();

		servicePreAction.servicePre(
			contextHttpServletRequest, httpServletResponse, false);

		ThemeServicePreAction themeServicePreAction =
			new ThemeServicePreAction();

		themeServicePreAction.run(
			contextHttpServletRequest, httpServletResponse);

		ThemeDisplay themeDisplay =
			(ThemeDisplay)contextHttpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		themeDisplay.setLayout(layout);
		themeDisplay.setScopeGroupId(layout.getGroupId());
		themeDisplay.setSiteGroupId(layout.getGroupId());

		return themeDisplay;
	}

	private String _getThemeId(long companyId, String themeName) {
		List<Theme> themes = ListUtil.filter(
			_themeLocalService.getThemes(companyId),
			theme -> Objects.equals(theme.getName(), themeName));

		if (ListUtil.isNotEmpty(themes)) {
			Theme theme = themes.get(0);

			return theme.getThemeId();
		}

		return null;
	}

	private SegmentsExperience _getUserSegmentsExperience(Layout layout)
		throws Exception {

		contextHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay(layout));

		long[] segmentsEntryIds = _segmentsEntryRetriever.getSegmentsEntryIds(
			layout.getGroupId(), contextUser.getUserId(),
			_requestContextMapper.map(contextHttpServletRequest));

		long[] segmentsExperienceIds =
			_segmentsExperienceRequestProcessorRegistry.
				getSegmentsExperienceIds(
					contextHttpServletRequest, null, layout.getGroupId(),
					layout.getPlid(), segmentsEntryIds);

		if (ArrayUtil.isEmpty(segmentsExperienceIds)) {
			return _segmentsExperienceLocalService.fetchSegmentsExperience(
				layout.getGroupId(), SegmentsExperienceConstants.KEY_DEFAULT,
				layout.getPlid());
		}

		return _segmentsExperienceLocalService.getSegmentsExperience(
			segmentsExperienceIds[0]);
	}

	private boolean _isEmbeddedPageDefinition() {
		MultivaluedMap<String, String> queryParameters =
			contextUriInfo.getQueryParameters();

		String nestedFields = queryParameters.getFirst("nestedFields");

		if (nestedFields == null) {
			return false;
		}

		return nestedFields.contains("pageDefinition");
	}

	private String _toHTML(
			String friendlyUrlPath, long groupId, String segmentsExperienceKey)
		throws Exception {

		Layout layout = _getLayout(groupId, friendlyUrlPath);

		contextHttpServletRequest = DynamicServletRequest.addQueryString(
			contextHttpServletRequest, "p_l_id=" + layout.getPlid(), false);

		SegmentsExperience segmentsExperience = _getSegmentsExperience(
			layout, segmentsExperienceKey);

		if (segmentsExperience != null) {
			contextHttpServletRequest.setAttribute(
				SegmentsWebKeys.SEGMENTS_EXPERIENCE_IDS,
				new long[] {segmentsExperience.getSegmentsExperienceId()});
		}

		contextHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay(layout));

		ServletContext servletContext = ServletContextPool.get(
			StringPool.BLANK);

		if (contextHttpServletRequest.getAttribute(WebKeys.CTX) == null) {
			contextHttpServletRequest.setAttribute(WebKeys.CTX, servletContext);
		}

		layout.includeLayoutContent(
			contextHttpServletRequest, contextHttpServletResponse);

		StringBundler sb =
			(StringBundler)contextHttpServletRequest.getAttribute(
				WebKeys.LAYOUT_CONTENT);

		LayoutSet layoutSet = layout.getLayoutSet();

		Document document = Jsoup.parse(
			ThemeUtil.include(
				servletContext, contextHttpServletRequest,
				contextHttpServletResponse, "portal_normal.ftl",
				layoutSet.getTheme(), false));

		Element bodyElement = document.body();

		bodyElement.html(sb.toString());

		return document.html();
	}

	private SitePage _toSitePage(
			boolean embeddedPageDefinition, Layout layout,
			String segmentsExperienceKey)
		throws Exception {

		Map<String, Map<String, String>> actions = null;

		if (Validator.isNotNull(segmentsExperienceKey)) {
			actions = _getExperienceActions(layout);
		}
		else {
			actions = _getBasicActions(layout);
		}

		DefaultDTOConverterContext dtoConverterContext =
			new DefaultDTOConverterContext(
				contextAcceptLanguage.isAcceptAllLanguages(), actions,
				_dtoConverterRegistry, contextHttpServletRequest,
				layout.getPlid(), contextAcceptLanguage.getPreferredLocale(),
				contextUriInfo, contextUser);

		dtoConverterContext.setAttribute(
			"embeddedPageDefinition", embeddedPageDefinition);

		if (Validator.isNotNull(segmentsExperienceKey)) {
			dtoConverterContext.setAttribute(
				"segmentsExperience",
				_getSegmentsExperience(layout, segmentsExperienceKey));
			dtoConverterContext.setAttribute("showExperience", Boolean.TRUE);
		}
		else {
			dtoConverterContext.setAttribute(
				"segmentsExperience", _getUserSegmentsExperience(layout));
		}

		return _sitePageDTOConverter.toDTO(dtoConverterContext, layout);
	}

	private Layout _updateDraftLayout(Layout layout) throws Exception {
		Layout draftLayout = layout.fetchDraftLayout();

		draftLayout = _layoutCopyHelper.copyLayoutContent(layout, draftLayout);

		draftLayout.setStatus(WorkflowConstants.STATUS_APPROVED);

		UnicodeProperties typeSettingsUnicodeProperties =
			draftLayout.getTypeSettingsProperties();

		typeSettingsUnicodeProperties.put("published", Boolean.TRUE.toString());

		draftLayout.setTypeSettingsProperties(typeSettingsUnicodeProperties);

		return _layoutLocalService.updateLayout(draftLayout);
	}

	private Layout _updateLayoutSettings(
		Layout layout, PageDefinition pageDefinition) {

		if ((pageDefinition == null) ||
			(pageDefinition.getSettings() == null)) {

			layout.setThemeId(null);

			layout.setColorSchemeId(null);

			return _layoutLocalService.updateLayout(layout);
		}

		ServiceContext serviceContext =
			ServiceContextRequestUtil.createServiceContext(
				null, layout.getGroupId(), contextHttpServletRequest, null);

		Settings settings = pageDefinition.getSettings();

		UnicodeProperties unicodeProperties =
			layout.getTypeSettingsProperties();

		Map<String, String> themeSettings =
			(Map<String, String>)settings.getThemeSettings();

		Set<Map.Entry<String, String>> set = unicodeProperties.entrySet();

		set.removeIf(
			entry -> {
				String key = entry.getKey();

				return key.startsWith("lfr-theme:");
			});

		if (themeSettings != null) {
			for (Map.Entry<String, String> entry : themeSettings.entrySet()) {
				unicodeProperties.put(entry.getKey(), entry.getValue());
			}

			layout.setTypeSettingsProperties(unicodeProperties);
		}

		if (Validator.isNotNull(settings.getThemeName())) {
			String themeId = _getThemeId(
				layout.getCompanyId(), settings.getThemeName());

			layout.setThemeId(themeId);
		}

		if (Validator.isNotNull(settings.getColorSchemeName())) {
			layout.setColorSchemeId(settings.getColorSchemeName());
		}

		if (Validator.isNotNull(settings.getCss())) {
			layout.setCss(settings.getCss());
		}

		Map<String, Serializable> favIconMap =
			(Map<String, Serializable>)settings.getFavIcon();

		if (MapUtil.isNotEmpty(favIconMap)) {
			if (Objects.equals(favIconMap.get("contentType"), "Document")) {
				layout.setFaviconFileEntryId(
					_getFileEntryId(GetterUtil.getLong(favIconMap.get("id"))));
			}
			else if (favIconMap.containsKey("externalReferenceCode")) {
				_addClientExtensionEntryRel(
					String.valueOf(favIconMap.get("externalReferenceCode")),
					layout, serviceContext,
					ClientExtensionEntryConstants.TYPE_THEME_FAVICON);
			}
		}

		MasterPage masterPage = settings.getMasterPage();

		if (masterPage != null) {
			LayoutPageTemplateEntry masterLayoutPageTemplateEntry =
				_layoutPageTemplateEntryLocalService.
					fetchLayoutPageTemplateEntry(
						layout.getGroupId(), masterPage.getKey());

			if (masterLayoutPageTemplateEntry != null) {
				layout.setMasterLayoutPlid(
					masterLayoutPageTemplateEntry.getPlid());
			}
		}

		StyleBook styleBook = settings.getStyleBook();

		if (styleBook != null) {
			StyleBookEntry styleBookEntry =
				_styleBookEntryLocalService.fetchStyleBookEntry(
					layout.getGroupId(), styleBook.getKey());

			if (styleBookEntry != null) {
				layout.setStyleBookEntryId(
					styleBookEntry.getStyleBookEntryId());
			}
		}

		ArrayUtil.isNotEmptyForEach(
			settings.getGlobalCSSClientExtensions(),
			globalCSSClientExtension -> _addClientExtensionEntryRel(
				globalCSSClientExtension.getExternalReferenceCode(), layout,
				serviceContext, ClientExtensionEntryConstants.TYPE_GLOBAL_CSS));
		ArrayUtil.isNotEmptyForEach(
			settings.getGlobalJSClientExtensions(),
			globalJSClientExtension -> _addClientExtensionEntryRel(
				globalJSClientExtension.getExternalReferenceCode(), layout,
				serviceContext, ClientExtensionEntryConstants.TYPE_GLOBAL_JS));

		ClientExtension themeCSSClientExtension =
			settings.getThemeCSSClientExtension();

		if (themeCSSClientExtension != null) {
			_addClientExtensionEntryRel(
				themeCSSClientExtension.getExternalReferenceCode(), layout,
				serviceContext, ClientExtensionEntryConstants.TYPE_THEME_CSS);
		}

		return _layoutLocalService.updateLayout(layout);
	}

	private void _updateModelResourcePermissions(
			long companyId, long groupId, long plid, SitePage sitePage)
		throws Exception {

		PagePermission[] pagePermissions = sitePage.getPagePermissions();

		if (pagePermissions == null) {
			return;
		}

		Map<String, String[]> modelPermissionsParameterMap = new HashMap<>(
			pagePermissions.length);

		for (PagePermission pagePermission : pagePermissions) {
			String roleKey = pagePermission.getRoleKey();

			Team team = _teamLocalService.fetchTeam(groupId, roleKey);

			if (team != null) {
				roleKey = String.valueOf(team.getTeamId());
			}

			modelPermissionsParameterMap.put(
				roleKey, pagePermission.getActionKeys());
		}

		ModelPermissions modelPermissions = ModelPermissionsFactory.create(
			modelPermissionsParameterMap, null);

		_resourcePermissionLocalService.deleteResourcePermissions(
			companyId, Layout.class.getName(),
			ResourceConstants.SCOPE_INDIVIDUAL, String.valueOf(plid));

		_resourcePermissionLocalService.addModelResourcePermissions(
			companyId, groupId, contextUser.getUserId(), Layout.class.getName(),
			String.valueOf(plid), modelPermissions);
	}

	private void _updateSEOEntry(
			long companyId, long groupId, long layoutId, SitePage sitePage)
		throws Exception {

		PageSettings pageSettings = sitePage.getPageSettings();

		if (pageSettings == null) {
			return;
		}

		SEOSettings seoSettings = pageSettings.getSeoSettings();

		boolean canonicalURLEnabled = false;
		Map<Locale, String> canonicalURLMap = new HashMap<>();

		if (seoSettings != null) {
			canonicalURLMap = LocalizedMapUtil.getLocalizedMap(
				contextAcceptLanguage.getPreferredLocale(),
				seoSettings.getCustomCanonicalURL(),
				seoSettings.getCustomCanonicalURL_i18n());

			if (MapUtil.isNotEmpty(canonicalURLMap)) {
				canonicalURLEnabled = true;
			}
		}

		boolean openGraphDescriptionEnabled = false;
		Map<Locale, String> openGraphDescriptionMap = new HashMap<>();
		Map<Locale, String> openGraphImageAltMap = new HashMap<>();
		long openGraphImageFileEntryId = 0;
		boolean openGraphTitleEnabled = false;
		Map<Locale, String> openGraphTitleMap = new HashMap<>();

		OpenGraphSettings openGraphSettings =
			pageSettings.getOpenGraphSettings();

		if (openGraphSettings != null) {
			openGraphDescriptionMap = LocalizedMapUtil.getLocalizedMap(
				contextAcceptLanguage.getPreferredLocale(),
				openGraphSettings.getDescription(),
				openGraphSettings.getDescription_i18n());

			if (MapUtil.isNotEmpty(openGraphDescriptionMap)) {
				openGraphDescriptionEnabled = true;
			}

			openGraphImageAltMap = LocalizedMapUtil.getLocalizedMap(
				contextAcceptLanguage.getPreferredLocale(),
				openGraphSettings.getImageAlt(),
				openGraphSettings.getImageAlt_i18n());

			ContentDocument contentDocument = openGraphSettings.getImage();

			if (contentDocument != null) {
				openGraphImageFileEntryId = _getFileEntryId(
					contentDocument.getId());
			}

			openGraphTitleMap = LocalizedMapUtil.getLocalizedMap(
				contextAcceptLanguage.getPreferredLocale(),
				openGraphSettings.getTitle(),
				openGraphSettings.getTitle_i18n());

			if (MapUtil.isNotEmpty(openGraphTitleMap)) {
				openGraphTitleEnabled = true;
			}
		}

		ServiceContext serviceContext =
			ServiceContextRequestUtil.createServiceContext(
				null, groupId, contextHttpServletRequest, null);

		String ddmFormValues = _getDDMFormValues(pageSettings);

		if (Validator.isNotNull(ddmFormValues)) {
			long ddmStructurePrimaryKey = _getDDMStructurePrimaryKey(companyId);

			serviceContext.setAttribute(
				ddmStructurePrimaryKey + "ddmFormValues", ddmFormValues);
		}

		_layoutSEOEntryService.updateLayoutSEOEntry(
			groupId, false, layoutId, canonicalURLEnabled, canonicalURLMap,
			openGraphDescriptionEnabled, openGraphDescriptionMap,
			openGraphImageAltMap, openGraphImageFileEntryId,
			openGraphTitleEnabled, openGraphTitleMap, serviceContext);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SitePageResourceImpl.class);

	private static final EntityModel _entityModel = new SitePageEntityModel();

	@Reference
	private CETManager _cetManager;

	@Reference
	private ClientExtensionEntryRelLocalService
		_clientExtensionEntryRelLocalService;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private DDMStructureService _ddmStructureService;

	@Reference
	private DLAppService _dlAppService;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

	@Reference
	private FriendlyURLEntryLocalService _friendlyURLEntryLocalService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private LayoutCopyHelper _layoutCopyHelper;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPageTemplateEntryLocalService
		_layoutPageTemplateEntryLocalService;

	@Reference
	private LayoutSEOEntryService _layoutSEOEntryService;

	@Reference
	private LayoutService _layoutService;

	@Reference
	private Portal _portal;

	@Reference
	private RequestContextMapper _requestContextMapper;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private SegmentsEntryRetriever _segmentsEntryRetriever;

	@Reference
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

	@Reference
	private SegmentsExperienceRequestProcessorRegistry
		_segmentsExperienceRequestProcessorRegistry;

	@Reference
	private SegmentsExperienceService _segmentsExperienceService;

	@Reference(
		target = "(component.name=com.liferay.headless.delivery.internal.dto.v1_0.converter.SitePageDTOConverter)"
	)
	private DTOConverter<Layout, SitePage> _sitePageDTOConverter;

	@Reference
	private StyleBookEntryLocalService _styleBookEntryLocalService;

	@Reference
	private TeamLocalService _teamLocalService;

	@Reference
	private ThemeLocalService _themeLocalService;

}