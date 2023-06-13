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

package com.liferay.layout.content.page.editor.web.internal.display.context;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.fragment.constants.FragmentActionKeys;
import com.liferay.fragment.constants.FragmentConstants;
import com.liferay.fragment.constants.FragmentEntryLinkConstants;
import com.liferay.fragment.contributor.FragmentCollectionContributor;
import com.liferay.fragment.contributor.FragmentCollectionContributorTracker;
import com.liferay.fragment.model.FragmentCollection;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.renderer.DefaultFragmentRendererContext;
import com.liferay.fragment.renderer.FragmentRenderer;
import com.liferay.fragment.renderer.FragmentRendererController;
import com.liferay.fragment.renderer.FragmentRendererTracker;
import com.liferay.fragment.service.FragmentCollectionServiceUtil;
import com.liferay.fragment.service.FragmentEntryLinkLocalServiceUtil;
import com.liferay.fragment.service.FragmentEntryServiceUtil;
import com.liferay.fragment.util.FragmentEntryConfigUtil;
import com.liferay.fragment.util.comparator.FragmentCollectionContributorNameComparator;
import com.liferay.info.constants.InfoDisplayWebKeys;
import com.liferay.info.display.contributor.InfoDisplayContributor;
import com.liferay.info.display.contributor.InfoDisplayContributorTracker;
import com.liferay.info.item.renderer.InfoItemRenderer;
import com.liferay.info.item.renderer.InfoItemRendererTracker;
import com.liferay.info.item.selector.InfoItemSelector;
import com.liferay.info.item.selector.InfoItemSelectorTracker;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.item.selector.criteria.DownloadFileEntryItemSelectorReturnType;
import com.liferay.item.selector.criteria.URLItemSelectorReturnType;
import com.liferay.item.selector.criteria.image.criterion.ImageItemSelectorCriterion;
import com.liferay.item.selector.criteria.url.criterion.URLItemSelectorCriterion;
import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorWebKeys;
import com.liferay.layout.content.page.editor.sidebar.panel.ContentPageEditorSidebarPanel;
import com.liferay.layout.content.page.editor.web.internal.comment.CommentUtil;
import com.liferay.layout.content.page.editor.web.internal.configuration.util.ContentCreationContentPageEditorConfigurationUtil;
import com.liferay.layout.content.page.editor.web.internal.configuration.util.ContentPageEditorConfigurationUtil;
import com.liferay.layout.content.page.editor.web.internal.util.ContentUtil;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalServiceUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.comment.Comment;
import com.liferay.portal.kernel.comment.CommentManager;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.editor.configuration.EditorConfiguration;
import com.liferay.portal.kernel.editor.configuration.EditorConfigurationFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletApp;
import com.liferay.portal.kernel.model.PortletCategory;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletConfigFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletIdCodec;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.util.comparator.PortletCategoryComparator;
import com.liferay.portal.kernel.util.comparator.PortletTitleComparator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.template.soy.util.SoyContext;
import com.liferay.portal.template.soy.util.SoyContextFactoryUtil;
import com.liferay.portal.util.PortletCategoryUtil;
import com.liferay.portal.util.WebAppPool;
import com.liferay.segments.constants.SegmentsExperienceConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.ActionRequest;
import javax.portlet.PortletConfig;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Eudaldo Alonso
 */
public class ContentPageEditorDisplayContext {

	public ContentPageEditorDisplayContext(
		HttpServletRequest httpServletRequest, RenderResponse renderResponse,
		CommentManager commentManager,
		List<ContentPageEditorSidebarPanel> contentPageEditorSidebarPanels,
		FragmentRendererController fragmentRendererController) {

		request = httpServletRequest;
		_renderResponse = renderResponse;
		_commentManager = commentManager;
		_contentPageEditorSidebarPanels = contentPageEditorSidebarPanels;
		_fragmentRendererController = fragmentRendererController;

		infoDisplayContributorTracker =
			(InfoDisplayContributorTracker)httpServletRequest.getAttribute(
				InfoDisplayWebKeys.INFO_DISPLAY_CONTRIBUTOR_TRACKER);
		themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
		_fragmentCollectionContributorTracker =
			(FragmentCollectionContributorTracker)
				httpServletRequest.getAttribute(
					ContentPageEditorWebKeys.
						FRAGMENT_COLLECTION_CONTRIBUTOR_TRACKER);
		_fragmentRendererTracker =
			(FragmentRendererTracker)httpServletRequest.getAttribute(
				FragmentActionKeys.FRAGMENT_RENDERER_TRACKER);
		_infoItemRendererTracker =
			(InfoItemRendererTracker)httpServletRequest.getAttribute(
				InfoDisplayWebKeys.INFO_ITEM_RENDERER_TRACKER);
		_infoItemSelectorTracker =
			(InfoItemSelectorTracker)httpServletRequest.getAttribute(
				InfoDisplayWebKeys.INFO_ITEM_SELECTOR_TRACKER);
		_itemSelector = (ItemSelector)httpServletRequest.getAttribute(
			ContentPageEditorWebKeys.ITEM_SELECTOR);
	}

	public SoyContext getEditorSoyContext() throws Exception {
		SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

		soyContext.put(
			"addFragmentEntryLinkCommentURL",
			getFragmentEntryActionURL(
				"/content_layout/add_fragment_entry_link_comment")
		).put(
			"addFragmentEntryLinkURL",
			getFragmentEntryActionURL("/content_layout/add_fragment_entry_link")
		).put(
			"addPortletURL",
			getFragmentEntryActionURL("/content_layout/add_portlet")
		).put(
			"addStructuredContentURL",
			getFragmentEntryActionURL("/content_layout/add_structured_content")
		).put(
			"assetBrowserLinks", _getAssetBrowserLinksSoyContexts()
		).put(
			"availableAssets", _getAvailableAssetsSoyContexts()
		).put(
			"availableLanguages", _getAvailableLanguagesSoyContext()
		).put(
			"classNameId", PortalUtil.getClassNameId(Layout.class.getName())
		).put(
			"classPK", themeDisplay.getPlid()
		).put(
			"contentCreationEnabled",
			ContentCreationContentPageEditorConfigurationUtil.
				isContentCreationEnabled(themeDisplay.getCompanyId())
		).put(
			"defaultEditorConfigurations", _getDefaultConfigurations()
		).put(
			"defaultLanguageId",
			LocaleUtil.toLanguageId(themeDisplay.getSiteDefaultLocale())
		).put(
			"deleteFragmentEntryLinkCommentURL",
			getFragmentEntryActionURL(
				"/content_layout/delete_fragment_entry_link_comment")
		).put(
			"deleteFragmentEntryLinkURL",
			getFragmentEntryActionURL(
				"/content_layout/delete_fragment_entry_link")
		).put(
			"discardDraftRedirectURL", themeDisplay.getURLCurrent()
		).put(
			"discardDraftURL",
			getFragmentEntryActionURL("/content_layout/discard_draft_layout")
		).put(
			"duplicateFragmentEntryLinkURL",
			getFragmentEntryActionURL(
				"/content_layout/duplicate_fragment_entry_link")
		).put(
			"editFragmentEntryLinkCommentURL",
			getFragmentEntryActionURL(
				"/content_layout/edit_fragment_entry_link_comment",
				Constants.UPDATE)
		).put(
			"editFragmentEntryLinkURL",
			getFragmentEntryActionURL(
				"/content_layout/edit_fragment_entry_link")
		).put(
			"elements",
			_getFragmentCollectionsSoyContexts(FragmentConstants.TYPE_COMPONENT)
		).put(
			"fragmentEntryLinks", _getFragmentEntryLinksSoyContext()
		).put(
			"getAssetFieldValueURL",
			_getResourceURL("/content_layout/get_asset_field_value")
		).put(
			"getAssetMappingFieldsURL",
			_getResourceURL("/content_layout/get_asset_mapping_fields")
		).put(
			"getContentStructureMappingFieldsURL",
			_getResourceURL(
				"/content_layout/get_content_structure_mapping_fields")
		).put(
			"getContentStructuresURL",
			_getResourceURL("/content_layout/get_content_structures")
		).put(
			"getExperienceUsedPortletsURL",
			_getResourceURL("/content_layout/get_experience_used_portlets")
		).put(
			"getPageContentsURL",
			_getResourceURL("/content_layout/get_page_contents")
		).put(
			"imageSelectorURL", _getItemSelectorURL()
		).put(
			"languageId", themeDisplay.getLanguageId()
		).put(
			"layoutData", JSONFactoryUtil.createJSONObject(_getLayoutData())
		).put(
			"lookAndFeelURL", _getLookAndFeelURL()
		).put(
			"mappedAssetEntries", _getMappedAssetEntriesSoyContexts()
		).put(
			"pageContents",
			ContentUtil.getPageContentsJSONArray(
				themeDisplay.getPlid(), themeDisplay.getURLCurrent(), request)
		).put(
			"portletNamespace", _renderResponse.getNamespace()
		).put(
			"publishURL",
			getFragmentEntryActionURL("/content_layout/publish_layout")
		).put(
			"redirectURL", _getRedirect()
		).put(
			"renderFragmentEntryURL",
			getFragmentEntryActionURL("/content_layout/render_fragment_entry")
		).put(
			"sections",
			_getFragmentCollectionsSoyContexts(FragmentConstants.TYPE_SECTION)
		).put(
			"spritemap",
			themeDisplay.getPathThemeImages() + "/lexicon/icons.svg"
		).put(
			"themeColorsCssClasses", _getThemeColorsCssClasses()
		).put(
			"updateLayoutPageTemplateDataURL",
			getFragmentEntryActionURL(
				"/content_layout/update_layout_page_template_data")
		).put(
			"widgets", _getWidgetsSoyContexts()
		);

		return soyContext;
	}

	public SoyContext getFragmentsEditorToolbarSoyContext()
		throws PortalException {

		SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

		soyContext.put(
			"availableLanguages", _getAvailableLanguagesSoyContext()
		).put(
			"classPK", themeDisplay.getPlid()
		).put(
			"defaultLanguageId", themeDisplay.getLanguageId()
		);

		Layout draftLayout = themeDisplay.getLayout();

		Layout layout = LayoutLocalServiceUtil.getLayout(
			draftLayout.getClassPK());

		Date modifiedDate = draftLayout.getModifiedDate();

		Date publishDate = layout.getPublishDate();

		if (publishDate == null) {
			publishDate = modifiedDate;
		}

		soyContext.put(
			"draft", modifiedDate.after(publishDate)
		).put(
			"lastSaveDate", StringPool.BLANK
		).put(
			"portletNamespace", _renderResponse.getNamespace()
		).put(
			"spritemap",
			themeDisplay.getPathThemeImages() + "/lexicon/icons.svg"
		);

		return soyContext;
	}

	protected String getFragmentEntryActionURL(String action) {
		return getFragmentEntryActionURL(action, null);
	}

	protected String getFragmentEntryActionURL(String action, String command) {
		PortletURL actionURL = _renderResponse.createActionURL();

		actionURL.setParameter(ActionRequest.ACTION_NAME, action);

		if (Validator.isNotNull(command)) {
			actionURL.setParameter(Constants.CMD, command);
		}

		return HttpUtil.addParameter(
			actionURL.toString(), "p_l_mode", Constants.EDIT);
	}

	protected long getGroupId() {
		if (_groupId != null) {
			return _groupId;
		}

		_groupId = ParamUtil.getLong(
			request, "groupId", themeDisplay.getScopeGroupId());

		return _groupId;
	}

	protected long getSegmentsExperienceId() {
		return SegmentsExperienceConstants.ID_DEFAULT;
	}

	protected List<SoyContext> getSidebarPanelSoyContexts(
		boolean pageIsDisplayPage) {

		if (_sidebarPanelSoyContexts != null) {
			return _sidebarPanelSoyContexts;
		}

		List<SoyContext> soyContexts = new ArrayList<>();

		for (ContentPageEditorSidebarPanel contentPageEditorSidebarPanel :
				_contentPageEditorSidebarPanels) {

			if (!contentPageEditorSidebarPanel.isVisible(pageIsDisplayPage)) {
				continue;
			}

			if (contentPageEditorSidebarPanel.includeSeparator()) {
				SoyContext availableSoyContext =
					SoyContextFactoryUtil.createSoyContext();

				availableSoyContext.put("sidebarPanelId", "separator");

				soyContexts.add(availableSoyContext);
			}

			SoyContext availableSoyContext =
				SoyContextFactoryUtil.createSoyContext();

			availableSoyContext.put(
				"icon", contentPageEditorSidebarPanel.getIcon()
			).put(
				"label",
				contentPageEditorSidebarPanel.getLabel(themeDisplay.getLocale())
			).put(
				"sidebarPanelId", contentPageEditorSidebarPanel.getId()
			);

			soyContexts.add(availableSoyContext);
		}

		_sidebarPanelSoyContexts = soyContexts;

		return _sidebarPanelSoyContexts;
	}

	protected final InfoDisplayContributorTracker infoDisplayContributorTracker;
	protected final HttpServletRequest request;
	protected final ThemeDisplay themeDisplay;

	private List<SoyContext> _getAssetBrowserLinksSoyContexts()
		throws Exception {

		if (_assetBrowserLinksSoyContexts != null) {
			return _assetBrowserLinksSoyContexts;
		}

		List<SoyContext> soyContexts = new ArrayList<>();

		List<InfoDisplayContributor> infoDisplayContributors =
			infoDisplayContributorTracker.getInfoDisplayContributors();

		for (InfoDisplayContributor infoDisplayContributor :
				infoDisplayContributors) {

			if (infoDisplayContributor == null) {
				continue;
			}

			String assetBrowserURL = _getAssetBrowserURL(
				infoDisplayContributor.getClassName());

			if (assetBrowserURL == null) {
				continue;
			}

			SoyContext assetBrowserSoyContext =
				SoyContextFactoryUtil.createSoyContext();

			assetBrowserSoyContext.put(
				"href", assetBrowserURL
			).put(
				"typeName",
				infoDisplayContributor.getLabel(themeDisplay.getLocale())
			);

			soyContexts.add(assetBrowserSoyContext);
		}

		_assetBrowserLinksSoyContexts = soyContexts;

		return _assetBrowserLinksSoyContexts;
	}

	private String _getAssetBrowserURL(String className) throws Exception {
		PortletURL assetBrowserURL = PortletProviderUtil.getPortletURL(
			request, className, PortletProvider.Action.BROWSE);

		if (assetBrowserURL == null) {
			return null;
		}

		assetBrowserURL.setParameter(
			"groupId", String.valueOf(themeDisplay.getScopeGroupId()));
		assetBrowserURL.setParameter(
			"selectedGroupIds", String.valueOf(themeDisplay.getScopeGroupId()));
		assetBrowserURL.setParameter("typeSelection", className);
		assetBrowserURL.setParameter(
			"showNonindexable", String.valueOf(Boolean.TRUE));
		assetBrowserURL.setParameter(
			"showScheduled", String.valueOf(Boolean.TRUE));
		assetBrowserURL.setParameter(
			"eventName", _renderResponse.getNamespace() + "selectAsset");
		assetBrowserURL.setPortletMode(PortletMode.VIEW);
		assetBrowserURL.setWindowState(LiferayWindowState.POP_UP);

		return assetBrowserURL.toString();
	}

	private List<SoyContext> _getAvailableAssetsSoyContexts() throws Exception {
		List<SoyContext> soyContexts = new ArrayList<>();

		Set<String> classNames =
			_infoItemSelectorTracker.getInfoItemSelectorsClassNames();

		for (String className : classNames) {
			List<InfoItemRenderer> infoItemRenderers =
				_infoItemRendererTracker.getInfoItemRenderers(className);

			if (ListUtil.isEmpty(infoItemRenderers)) {
				continue;
			}

			List<InfoItemSelector> infoItemSelectors =
				_infoItemSelectorTracker.getInfoItemSelectors(className);

			InfoItemSelector infoItemSelector = infoItemSelectors.get(0);

			PortletURL infoItemSelectorPortletURL =
				infoItemSelector.getInfoItemSelectorPortletURL(request);

			if (infoItemSelectorPortletURL == null) {
				continue;
			}

			SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

			soyContext.put(
				"availableTemplates",
				_getInfoItemRenderersTemplatesSoyContexts(infoItemRenderers)
			).put(
				"className", className
			).put(
				"classNameId", PortalUtil.getClassNameId(className)
			);

			infoItemSelectorPortletURL.setParameter(
				"eventName", _renderResponse.getNamespace() + "selectAsset");

			soyContext.put(
				"href", infoItemSelectorPortletURL.toString()
			).put(
				"typeName",
				ResourceActionsUtil.getModelResource(
					themeDisplay.getLocale(), className)
			);

			soyContexts.add(soyContext);
		}

		return soyContexts;
	}

	private SoyContext _getAvailableLanguagesSoyContext() {
		SoyContext availableLanguagesSoyContext =
			SoyContextFactoryUtil.createSoyContext();

		String[] languageIds = LocaleUtil.toLanguageIds(
			LanguageUtil.getAvailableLocales(themeDisplay.getSiteGroupId()));

		for (String languageId : languageIds) {
			SoyContext languageSoyContext =
				SoyContextFactoryUtil.createSoyContext();

			String languageIcon = StringUtil.toLowerCase(
				StringUtil.replace(
					languageId, CharPool.UNDERLINE, CharPool.DASH));

			languageSoyContext.put("languageIcon", languageIcon);

			String languageLabel = StringUtil.replace(
				languageId, CharPool.UNDERLINE, CharPool.DASH);

			languageSoyContext.put("languageLabel", languageLabel);

			availableLanguagesSoyContext.put(languageId, languageSoyContext);
		}

		return availableLanguagesSoyContext;
	}

	private SoyContext _getContributedFragmentEntrySoyContext(
		String rendererKey) {

		SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

		Map<String, FragmentEntry> fragmentEntries =
			_fragmentCollectionContributorTracker.getFragmentEntries(
				themeDisplay.getLocale());

		FragmentEntry fragmentEntry = fragmentEntries.get(rendererKey);

		if (fragmentEntry != null) {
			soyContext.put(
				"fragmentEntryId", 0
			).put(
				"name", fragmentEntry.getName()
			);
		}

		return soyContext;
	}

	private Map<String, Object> _getDefaultConfigurations() {
		if (_defaultConfigurations != null) {
			return _defaultConfigurations;
		}

		Map<String, Object> configurations = new HashMap<>();

		EditorConfiguration richTextEditorConfiguration =
			EditorConfigurationFactoryUtil.getEditorConfiguration(
				ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
				"fragmenEntryLinkRichTextEditor", StringPool.BLANK,
				Collections.emptyMap(), themeDisplay,
				RequestBackedPortletURLFactoryUtil.create(request));

		configurations.put("rich-text", richTextEditorConfiguration.getData());

		EditorConfiguration editorConfiguration =
			EditorConfigurationFactoryUtil.getEditorConfiguration(
				ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
				"fragmenEntryLinkEditor", StringPool.BLANK,
				Collections.emptyMap(), themeDisplay,
				RequestBackedPortletURLFactoryUtil.create(request));

		configurations.put("text", editorConfiguration.getData());

		_defaultConfigurations = configurations;

		return _defaultConfigurations;
	}

	private List<SoyContext> _getDynamicFragmentsSoyContexts(int type) {
		List<SoyContext> soyContexts = new ArrayList<>();

		Map<String, List<SoyContext>> fragmentCollectionSoyContextsMap =
			new HashMap<>();
		Map<String, FragmentRenderer> fragmentCollectionFragmentRenderers =
			new HashMap<>();

		List<FragmentRenderer> fragmentRenderers =
			_fragmentRendererTracker.getFragmentRenderers(type);

		for (FragmentRenderer fragmentRenderer : fragmentRenderers) {
			if (!fragmentRenderer.isSelectable(request)) {
				continue;
			}

			SoyContext dynamicFragmentSoyContext =
				SoyContextFactoryUtil.createSoyContext();

			dynamicFragmentSoyContext.put(
				"fragmentEntryKey", fragmentRenderer.getKey()
			).put(
				"imagePreviewURL", fragmentRenderer.getImagePreviewURL(request)
			).put(
				"name", fragmentRenderer.getLabel(themeDisplay.getLocale())
			);

			List<SoyContext> fragmentCollectionSoyContexts =
				fragmentCollectionSoyContextsMap.get(
					fragmentRenderer.getCollectionKey());

			if (fragmentCollectionSoyContexts == null) {
				List<SoyContext> dynamicFragmentSoyContexts =
					fragmentCollectionSoyContextsMap.computeIfAbsent(
						fragmentRenderer.getCollectionKey(),
						key -> new ArrayList<>());

				dynamicFragmentSoyContexts.add(dynamicFragmentSoyContext);

				fragmentCollectionSoyContextsMap.put(
					fragmentRenderer.getCollectionKey(),
					dynamicFragmentSoyContexts);

				fragmentCollectionFragmentRenderers.put(
					fragmentRenderer.getCollectionKey(), fragmentRenderer);
			}
			else {
				fragmentCollectionSoyContexts.add(dynamicFragmentSoyContext);
			}
		}

		for (Map.Entry<String, List<SoyContext>> entry :
				fragmentCollectionSoyContextsMap.entrySet()) {

			FragmentRenderer fragmentRenderer =
				fragmentCollectionFragmentRenderers.get(entry.getKey());

			SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

			soyContext.put(
				"fragmentCollectionId", entry.getKey()
			).put(
				"fragmentEntries", entry.getValue()
			).put(
				"name",
				LanguageUtil.get(
					ResourceBundleUtil.getBundle(
						themeDisplay.getLocale(), fragmentRenderer.getClass()),
					"fragment.collection.label." + entry.getKey())
			);

			soyContexts.add(soyContext);
		}

		return soyContexts;
	}

	private List<SoyContext> _getFragmentCollectionContributorSoyContexts(
		int type) {

		List<SoyContext> soyContexts = new ArrayList<>();

		List<FragmentCollectionContributor> fragmentCollectionContributors =
			_fragmentCollectionContributorTracker.
				getFragmentCollectionContributors();

		Collections.sort(
			fragmentCollectionContributors,
			new FragmentCollectionContributorNameComparator(
				themeDisplay.getLocale()));

		for (FragmentCollectionContributor fragmentCollectionContributor :
				fragmentCollectionContributors) {

			List<FragmentEntry> fragmentEntries =
				fragmentCollectionContributor.getFragmentEntries(
					type, themeDisplay.getLocale());

			if (ListUtil.isEmpty(fragmentEntries)) {
				continue;
			}

			SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

			soyContext.put(
				"fragmentCollectionId",
				fragmentCollectionContributor.getFragmentCollectionKey()
			).put(
				"fragmentEntries",
				_getFragmentEntriesSoyContexts(fragmentEntries)
			).put(
				"name",
				fragmentCollectionContributor.getName(themeDisplay.getLocale())
			);

			soyContexts.add(soyContext);
		}

		return soyContexts;
	}

	private List<SoyContext> _getFragmentCollectionsSoyContexts(int type) {
		List<SoyContext> soyContexts =
			_getFragmentCollectionContributorSoyContexts(type);

		soyContexts.addAll(_getDynamicFragmentsSoyContexts(type));

		long[] groupIds = {themeDisplay.getCompanyGroupId(), getGroupId()};

		List<FragmentCollection> fragmentCollections =
			FragmentCollectionServiceUtil.getFragmentCollections(groupIds);

		for (FragmentCollection fragmentCollection : fragmentCollections) {
			List<FragmentEntry> fragmentEntries =
				FragmentEntryServiceUtil.getFragmentEntriesByTypeAndStatus(
					fragmentCollection.getGroupId(),
					fragmentCollection.getFragmentCollectionId(), type,
					WorkflowConstants.STATUS_APPROVED);

			if (ListUtil.isEmpty(fragmentEntries)) {
				continue;
			}

			SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

			soyContext.put(
				"fragmentCollectionId",
				fragmentCollection.getFragmentCollectionId()
			).put(
				"fragmentEntries",
				_getFragmentEntriesSoyContexts(fragmentEntries)
			).put(
				"name", fragmentCollection.getName()
			);

			soyContexts.add(soyContext);
		}

		return soyContexts;
	}

	private List<SoyContext> _getFragmentEntriesSoyContexts(
		List<FragmentEntry> fragmentEntries) {

		List<SoyContext> soyContexts = new ArrayList<>();

		for (FragmentEntry fragmentEntry : fragmentEntries) {
			SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

			soyContext.put(
				"fragmentEntryKey", fragmentEntry.getFragmentEntryKey()
			).put(
				"groupId", fragmentEntry.getGroupId()
			).put(
				"imagePreviewURL",
				fragmentEntry.getImagePreviewURL(themeDisplay)
			).put(
				"name", fragmentEntry.getName()
			);

			soyContexts.add(soyContext);
		}

		return soyContexts;
	}

	private JSONArray _getFragmentEntryLinkCommentsJSONArray(
			FragmentEntryLink fragmentEntryLink)
		throws PortalException {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		if (!_commentManager.hasDiscussion(
				FragmentEntryLink.class.getName(),
				fragmentEntryLink.getFragmentEntryLinkId())) {

			return jsonArray;
		}

		List<Comment> rootComments = _commentManager.getRootComments(
			FragmentEntryLink.class.getName(),
			fragmentEntryLink.getFragmentEntryLinkId(),
			WorkflowConstants.STATUS_ANY, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (Comment rootComment : rootComments) {
			JSONObject commentJSONObject = CommentUtil.getCommentJSONObject(
				rootComment, request);

			List<Comment> childComments = _commentManager.getChildComments(
				rootComment.getCommentId(), WorkflowConstants.STATUS_APPROVED,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

			JSONArray childCommentsJSONArray =
				JSONFactoryUtil.createJSONArray();

			for (Comment childComment : childComments) {
				childCommentsJSONArray.put(
					CommentUtil.getCommentJSONObject(childComment, request));
			}

			commentJSONObject.put("children", childCommentsJSONArray);

			jsonArray.put(commentJSONObject);
		}

		return jsonArray;
	}

	private SoyContext _getFragmentEntryLinksSoyContext()
		throws PortalException {

		if (_soyContextFragmentEntryLinksSoyContext != null) {
			return _soyContextFragmentEntryLinksSoyContext;
		}

		SoyContext soyContexts = SoyContextFactoryUtil.createSoyContext();

		List<FragmentEntryLink> fragmentEntryLinks =
			FragmentEntryLinkLocalServiceUtil.getFragmentEntryLinks(
				getGroupId(), PortalUtil.getClassNameId(Layout.class.getName()),
				themeDisplay.getPlid());

		boolean isolated = themeDisplay.isIsolated();

		themeDisplay.setIsolated(true);

		long[] segmentsExperienceIds = {getSegmentsExperienceId()};

		try {
			for (FragmentEntryLink fragmentEntryLink : fragmentEntryLinks) {
				FragmentEntry fragmentEntry =
					FragmentEntryServiceUtil.fetchFragmentEntry(
						fragmentEntryLink.getFragmentEntryId());

				SoyContext soyContext =
					SoyContextFactoryUtil.createSoyContext();

				DefaultFragmentRendererContext fragmentRendererContext =
					new DefaultFragmentRendererContext(fragmentEntryLink);

				fragmentRendererContext.setLocale(themeDisplay.getLocale());
				fragmentRendererContext.setMode(
					FragmentEntryLinkConstants.EDIT);
				fragmentRendererContext.setSegmentsExperienceIds(
					segmentsExperienceIds);

				String content = _fragmentRendererController.render(
					fragmentRendererContext, request,
					PortalUtil.getHttpServletResponse(_renderResponse));

				JSONObject editableValuesJSONObject =
					JSONFactoryUtil.createJSONObject(
						fragmentEntryLink.getEditableValues());

				boolean error = false;

				if (SessionErrors.contains(
						request, "fragmentEntryContentInvalid")) {

					error = true;

					SessionErrors.clear(request);
				}

				if (ContentPageEditorConfigurationUtil.isCommentsEnabled(
						themeDisplay.getCompanyId())) {

					soyContext.put(
						"comments",
						_getFragmentEntryLinkCommentsJSONArray(
							fragmentEntryLink));
				}

				String configuration =
					_fragmentRendererController.getConfiguration(
						fragmentRendererContext);

				soyContext.put(
					"configuration",
					JSONFactoryUtil.createJSONObject(configuration)
				).putHTML(
					"content", content
				).put(
					"defaultConfigurationValues",
					FragmentEntryConfigUtil.
						getConfigurationDefaultValuesJSONObject(configuration)
				).put(
					"editableValues", editableValuesJSONObject
				).put(
					"error", error
				).put(
					"fragmentEntryLinkId",
					String.valueOf(fragmentEntryLink.getFragmentEntryLinkId())
				).putAll(
					_getFragmentEntrySoyContext(
						fragmentEntryLink, fragmentEntry, content)
				);

				soyContexts.put(
					String.valueOf(fragmentEntryLink.getFragmentEntryLinkId()),
					soyContext);
			}
		}
		finally {
			themeDisplay.setIsolated(isolated);
		}

		_soyContextFragmentEntryLinksSoyContext = soyContexts;

		return _soyContextFragmentEntryLinksSoyContext;
	}

	private SoyContext _getFragmentEntrySoyContext(
		FragmentEntryLink fragmentEntryLink, FragmentEntry fragmentEntry,
		String content) {

		if (fragmentEntry != null) {
			SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

			soyContext.put(
				"fragmentEntryId", fragmentEntry.getFragmentEntryId()
			).put(
				"name", fragmentEntry.getName()
			);

			return soyContext;
		}

		String rendererKey = fragmentEntryLink.getRendererKey();

		if (Validator.isNotNull(rendererKey)) {
			SoyContext soyContext = _getContributedFragmentEntrySoyContext(
				rendererKey);

			if (!soyContext.isEmpty()) {
				return soyContext;
			}

			soyContext = SoyContextFactoryUtil.createSoyContext();

			FragmentRenderer fragmentRenderer =
				_fragmentRendererTracker.getFragmentRenderer(
					fragmentEntryLink.getRendererKey());

			if (fragmentRenderer != null) {
				soyContext.put(
					"fragmentEntryId", 0
				).put(
					"name", fragmentRenderer.getLabel(themeDisplay.getLocale())
				);
			}

			if (!soyContext.isEmpty()) {
				return soyContext;
			}
		}

		SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

		soyContext.put(
			"fragmentEntryId", 0
		).put(
			"name", StringPool.BLANK
		);

		String portletId = _getPortletId(content);

		PortletConfig portletConfig = PortletConfigFactoryUtil.get(portletId);

		if (portletConfig == null) {
			return soyContext;
		}

		soyContext.put(
			"name",
			PortalUtil.getPortletTitle(portletId, themeDisplay.getLocale())
		).put(
			"portletId", portletId
		);

		return soyContext;
	}

	private ItemSelectorCriterion _getImageItemSelectorCriterion() {
		if (_imageItemSelectorCriterion != null) {
			return _imageItemSelectorCriterion;
		}

		ItemSelectorCriterion itemSelectorCriterion =
			new ImageItemSelectorCriterion();

		itemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			new DownloadFileEntryItemSelectorReturnType());

		_imageItemSelectorCriterion = itemSelectorCriterion;

		return _imageItemSelectorCriterion;
	}

	private List<SoyContext> _getInfoItemRenderersTemplatesSoyContexts(
		List<InfoItemRenderer> infoItemRenderers) {

		List<SoyContext> soyContexts = new ArrayList<>();

		for (InfoItemRenderer infoItemRenderer : infoItemRenderers) {
			SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

			soyContext.put(
				"key", infoItemRenderer.getKey()
			).put(
				"label", infoItemRenderer.getLabel(themeDisplay.getLocale())
			);

			soyContexts.add(soyContext);
		}

		return soyContexts;
	}

	private String _getItemSelectorURL() {
		PortletURL itemSelectorURL = _itemSelector.getItemSelectorURL(
			RequestBackedPortletURLFactoryUtil.create(request),
			_renderResponse.getNamespace() + "selectImage",
			_getImageItemSelectorCriterion(), _getURLItemSelectorCriterion());

		return itemSelectorURL.toString();
	}

	private String _getLayoutData() throws PortalException {
		if (_layoutData != null) {
			return _layoutData;
		}

		LayoutPageTemplateStructure layoutPageTemplateStructure =
			LayoutPageTemplateStructureLocalServiceUtil.
				fetchLayoutPageTemplateStructure(
					themeDisplay.getScopeGroupId(),
					PortalUtil.getClassNameId(Layout.class.getName()),
					themeDisplay.getPlid(), true);

		_layoutData = layoutPageTemplateStructure.getData(
			getSegmentsExperienceId());

		return _layoutData;
	}

	private String _getLookAndFeelURL() {
		PortletURL lookAndFeelURL = PortalUtil.getControlPanelPortletURL(
			request, LayoutAdminPortletKeys.GROUP_PAGES,
			PortletRequest.RENDER_PHASE);

		lookAndFeelURL.setParameter(
			"mvcRenderCommandName", "/layout/edit_layout");

		lookAndFeelURL.setParameter("redirect", themeDisplay.getURLCurrent());
		lookAndFeelURL.setParameter("backURL", themeDisplay.getURLCurrent());

		Layout layout = themeDisplay.getLayout();

		lookAndFeelURL.setParameter(
			"groupId", String.valueOf(layout.getGroupId()));
		lookAndFeelURL.setParameter(
			"selPlid", String.valueOf(layout.getPlid()));
		lookAndFeelURL.setParameter(
			"privateLayout", String.valueOf(layout.isPrivateLayout()));

		return lookAndFeelURL.toString();
	}

	private Set<SoyContext> _getMappedAssetEntriesSoyContexts()
		throws PortalException {

		Set<SoyContext> mappedAssetEntriesSoyContexts = new HashSet<>();

		Set<AssetEntry> assetEntries = ContentUtil.getMappedAssetEntries(
			_groupId, themeDisplay.getPlid());

		for (AssetEntry assetEntry : assetEntries) {
			SoyContext mappedAssetEntrySoyContext =
				SoyContextFactoryUtil.createSoyContext();

			mappedAssetEntrySoyContext.put(
				"classNameId", assetEntry.getClassNameId()
			).put(
				"classPK", assetEntry.getClassPK()
			).put(
				"title", assetEntry.getTitle(themeDisplay.getLocale())
			);

			mappedAssetEntriesSoyContexts.add(mappedAssetEntrySoyContext);
		}

		return mappedAssetEntriesSoyContexts;
	}

	private String _getPortletCategoryTitle(PortletCategory portletCategory) {
		for (String portletId :
				PortletCategoryUtil.getFirstChildPortletIds(portletCategory)) {

			Portlet portlet = PortletLocalServiceUtil.getPortletById(
				themeDisplay.getCompanyId(), portletId);

			if (portlet == null) {
				continue;
			}

			PortletApp portletApp = portlet.getPortletApp();

			if (!portletApp.isWARFile()) {
				continue;
			}

			PortletConfig portletConfig = PortletConfigFactoryUtil.create(
				portlet, request.getServletContext());

			ResourceBundle portletResourceBundle =
				portletConfig.getResourceBundle(themeDisplay.getLocale());

			String title = ResourceBundleUtil.getString(
				portletResourceBundle, portletCategory.getName());

			if (Validator.isNotNull(title)) {
				return title;
			}
		}

		return LanguageUtil.get(request, portletCategory.getName());
	}

	private String _getPortletId(String content) {
		Document document = Jsoup.parse(content);

		Elements elements = document.getElementsByAttributeValueStarting(
			"id", "portlet_");

		if (elements.size() != 1) {
			return StringPool.BLANK;
		}

		Element element = elements.get(0);

		String id = element.id();

		return PortletIdCodec.decodePortletName(id.substring(8));
	}

	private List<SoyContext> _getPortletsSoyContexts(
		PortletCategory portletCategory) {

		Set<String> portletIds = portletCategory.getPortletIds();

		Stream<String> stream = portletIds.stream();

		HttpSession session = request.getSession();

		ServletContext servletContext = session.getServletContext();

		return stream.map(
			portletId -> PortletLocalServiceUtil.getPortletById(
				themeDisplay.getCompanyId(), portletId)
		).filter(
			portlet -> {
				if (portlet == null) {
					return false;
				}

				if (ArrayUtil.contains(
						_UNSUPPORTED_PORTLETS_NAMES,
						portlet.getPortletName())) {

					return false;
				}

				try {
					return PortletPermissionUtil.contains(
						themeDisplay.getPermissionChecker(),
						themeDisplay.getLayout(), portlet,
						ActionKeys.ADD_TO_PAGE);
				}
				catch (PortalException pe) {
					_log.error(
						"Unable to check portlet permissions for " +
							portlet.getPortletId(),
						pe);

					return false;
				}
			}
		).sorted(
			new PortletTitleComparator(servletContext, themeDisplay.getLocale())
		).map(
			portlet -> {
				SoyContext portletSoyContext =
					SoyContextFactoryUtil.createSoyContext();

				portletSoyContext.put(
					"instanceable", portlet.isInstanceable()
				).put(
					"portletId", portlet.getPortletId()
				).put(
					"title",
					PortalUtil.getPortletTitle(
						portlet, servletContext, themeDisplay.getLocale())
				).put(
					"used", _isUsed(portlet, themeDisplay.getPlid())
				);

				return portletSoyContext;
			}
		).collect(
			Collectors.toList()
		);
	}

	private String _getRedirect() {
		if (Validator.isNotNull(_redirect)) {
			return _redirect;
		}

		_redirect = ParamUtil.getString(request, "redirect");

		if (Validator.isNull(_redirect)) {
			_redirect = PortalUtil.escapeRedirect(
				ParamUtil.getString(
					PortalUtil.getOriginalServletRequest(request),
					"p_l_back_url", themeDisplay.getURLCurrent()));
		}

		return _redirect;
	}

	private String _getResourceURL(String resourceID) {
		ResourceURL resourceURL = _renderResponse.createResourceURL();

		resourceURL.setResourceID(resourceID);

		return resourceURL.toString();
	}

	private String[] _getThemeColorsCssClasses() {
		Theme theme = themeDisplay.getTheme();

		String colorPalette = theme.getSetting("color-palette");

		if (Validator.isNotNull(colorPalette)) {
			return StringUtil.split(colorPalette);
		}

		return new String[] {
			"primary", "success", "danger", "warning", "info", "dark",
			"gray-dark", "secondary", "light", "lighter", "white"
		};
	}

	private ItemSelectorCriterion _getURLItemSelectorCriterion() {
		if (_urlItemSelectorCriterion != null) {
			return _urlItemSelectorCriterion;
		}

		ItemSelectorCriterion itemSelectorCriterion =
			new URLItemSelectorCriterion();

		itemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			new URLItemSelectorReturnType());

		_urlItemSelectorCriterion = itemSelectorCriterion;

		return _urlItemSelectorCriterion;
	}

	private List<SoyContext> _getWidgetCategoriesSoyContexts(
		PortletCategory portletCategory) {

		Collection<PortletCategory> portletCategories =
			portletCategory.getCategories();

		Stream<PortletCategory> stream = portletCategories.stream();

		return stream.sorted(
			new PortletCategoryComparator(themeDisplay.getLocale())
		).filter(
			category -> !category.isHidden()
		).map(
			category -> {
				SoyContext categoryContext =
					SoyContextFactoryUtil.createSoyContext();

				categoryContext.put(
					"categories", _getWidgetCategoriesSoyContexts(category)
				).put(
					"path",
					StringUtil.replace(
						category.getPath(), new String[] {"/", "."},
						new String[] {"-", "-"})
				).put(
					"portlets", _getPortletsSoyContexts(category)
				).put(
					"title", _getPortletCategoryTitle(category)
				);

				return categoryContext;
			}
		).collect(
			Collectors.toList()
		);
	}

	private List<SoyContext> _getWidgetsSoyContexts() throws Exception {
		PortletCategory portletCategory = (PortletCategory)WebAppPool.get(
			themeDisplay.getCompanyId(), WebKeys.PORTLET_CATEGORY);

		portletCategory = PortletCategoryUtil.getRelevantPortletCategory(
			themeDisplay.getPermissionChecker(), themeDisplay.getCompanyId(),
			themeDisplay.getLayout(), portletCategory,
			themeDisplay.getLayoutTypePortlet());

		return _getWidgetCategoriesSoyContexts(portletCategory);
	}

	private boolean _isUsed(Portlet portlet, long plid) {
		if (portlet.isInstanceable()) {
			return false;
		}

		long count =
			PortletPreferencesLocalServiceUtil.getPortletPreferencesCount(
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, plid,
				PortletIdCodec.encode(
					portlet.getPortletId(), String.valueOf(CharPool.NUMBER_0)));

		if (count > 0) {
			return true;
		}

		return false;
	}

	private static final String[] _UNSUPPORTED_PORTLETS_NAMES = {
		"com_liferay_nested_portlets_web_portlet_NestedPortletsPortlet"
	};

	private static final Log _log = LogFactoryUtil.getLog(
		ContentPageEditorDisplayContext.class);

	private List<SoyContext> _assetBrowserLinksSoyContexts;
	private final CommentManager _commentManager;
	private final List<ContentPageEditorSidebarPanel>
		_contentPageEditorSidebarPanels;
	private Map<String, Object> _defaultConfigurations;
	private final FragmentCollectionContributorTracker
		_fragmentCollectionContributorTracker;
	private final FragmentRendererController _fragmentRendererController;
	private final FragmentRendererTracker _fragmentRendererTracker;
	private Long _groupId;
	private ItemSelectorCriterion _imageItemSelectorCriterion;
	private final InfoItemRendererTracker _infoItemRendererTracker;
	private final InfoItemSelectorTracker _infoItemSelectorTracker;
	private final ItemSelector _itemSelector;
	private String _layoutData;
	private String _redirect;
	private final RenderResponse _renderResponse;
	private List<SoyContext> _sidebarPanelSoyContexts;
	private SoyContext _soyContextFragmentEntryLinksSoyContext;
	private ItemSelectorCriterion _urlItemSelectorCriterion;

}