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

package com.liferay.layout.admin.web.internal.display.context;

import com.liferay.client.extension.constants.ClientExtensionEntryConstants;
import com.liferay.client.extension.model.ClientExtensionEntryRel;
import com.liferay.client.extension.service.ClientExtensionEntryRelLocalServiceUtil;
import com.liferay.client.extension.type.CET;
import com.liferay.client.extension.type.manager.CETManager;
import com.liferay.exportimport.kernel.staging.StagingUtil;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.TabsItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.TabsItemListBuilder;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.criteria.UUIDItemSelectorReturnType;
import com.liferay.layout.admin.web.internal.item.selector.MasterLayoutPageTemplateEntryItemSelectorCriterion;
import com.liferay.layout.admin.web.internal.item.selector.StyleBookEntryItemSelectorCriterion;
import com.liferay.layout.page.template.constants.LayoutPageTemplateEntryTypeConstants;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.style.book.model.StyleBookEntry;
import com.liferay.style.book.service.StyleBookEntryLocalServiceUtil;
import com.liferay.style.book.util.DefaultStyleBookEntryUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Víctor Galán
 */
public class LayoutLookAndFeelDisplayContext {

	public LayoutLookAndFeelDisplayContext(
		HttpServletRequest httpServletRequest,
		LayoutsAdminDisplayContext layoutsAdminDisplayContext,
		LiferayPortletResponse liferayPortletResponse) {

		_httpServletRequest = httpServletRequest;
		_layoutsAdminDisplayContext = layoutsAdminDisplayContext;
		_liferayPortletResponse = liferayPortletResponse;

		_itemSelector = (ItemSelector)httpServletRequest.getAttribute(
			ItemSelector.class.getName());
		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);
	}

	public Map<String, Object> getGlobalCSSCETsConfigurationProps(
		String className, long classPK) {

		return HashMapBuilder.<String, Object>put(
			"globalCSSCETs",
			_getClientExtensionEntryRelsJSONArray(
				className, classPK,
				ClientExtensionEntryConstants.TYPE_GLOBAL_CSS)
		).put(
			"globalCSSCETSelectorURL",
			() -> PortletURLBuilder.create(
				_layoutsAdminDisplayContext.getCETItemSelectorURL(
					true, "selectGlobalCSSCETs",
					ClientExtensionEntryConstants.TYPE_GLOBAL_CSS)
			).buildString()
		).put(
			"selectGlobalCSSCETsEventName", "selectGlobalCSSCETs"
		).build();
	}

	public Map<String, Object> getGlobalJSCETsConfigurationProps(
		String className, long classPK) {

		return HashMapBuilder.<String, Object>put(
			"globalJSCETs",
			_getClientExtensionEntryRelsJSONArray(
				className, classPK,
				ClientExtensionEntryConstants.TYPE_GLOBAL_JS)
		).put(
			"globalJSCETSelectorURL",
			() -> PortletURLBuilder.create(
				_layoutsAdminDisplayContext.getCETItemSelectorURL(
					true, "selectGlobalJSCETs",
					ClientExtensionEntryConstants.TYPE_GLOBAL_JS)
			).buildString()
		).put(
			"selectGlobalJSCETsEventName", "selectGlobalJSCETs"
		).build();
	}

	public Map<String, Object> getMasterLayoutConfigurationProps() {
		return HashMapBuilder.<String, Object>put(
			"changeMasterLayoutURL",
			() -> {
				RequestBackedPortletURLFactory requestBackedPortletURLFactory =
					RequestBackedPortletURLFactoryUtil.create(
						_httpServletRequest);

				MasterLayoutPageTemplateEntryItemSelectorCriterion
					masterLayoutPageTemplateEntryItemSelectorCriterion =
						new MasterLayoutPageTemplateEntryItemSelectorCriterion();

				masterLayoutPageTemplateEntryItemSelectorCriterion.
					setDesiredItemSelectorReturnTypes(
						new UUIDItemSelectorReturnType());

				return String.valueOf(
					_itemSelector.getItemSelectorURL(
						requestBackedPortletURLFactory,
						_liferayPortletResponse.getNamespace() +
							"selectMasterLayout",
						masterLayoutPageTemplateEntryItemSelectorCriterion));
			}
		).put(
			"editMasterLayoutURL",
			() -> {
				if (!hasMasterLayout()) {
					return StringPool.BLANK;
				}

				Layout selLayout = _layoutsAdminDisplayContext.getSelLayout();

				Layout masterLayout = LayoutLocalServiceUtil.getLayout(
					selLayout.getMasterLayoutPlid());

				String editLayoutURL = HttpComponentsUtil.addParameter(
					HttpComponentsUtil.addParameter(
						PortalUtil.getLayoutFullURL(selLayout, _themeDisplay),
						"p_l_mode", Constants.EDIT),
					"p_l_back_url",
					ParamUtil.getString(_httpServletRequest, "redirect"));

				return HttpComponentsUtil.addParameter(
					HttpComponentsUtil.addParameter(
						PortalUtil.getLayoutFullURL(
							masterLayout.fetchDraftLayout(), _themeDisplay),
						"p_l_mode", Constants.EDIT),
					"p_l_back_url", editLayoutURL);
			}
		).put(
			"masterLayoutName", getMasterLayoutName()
		).put(
			"masterLayoutPlid",
			() -> {
				if (hasMasterLayout()) {
					Layout selLayout =
						_layoutsAdminDisplayContext.getSelLayout();

					return String.valueOf(selLayout.getMasterLayoutPlid());
				}

				return StringPool.BLANK;
			}
		).build();
	}

	public String getMasterLayoutName() {
		if (_masterLayoutName != null) {
			return _masterLayoutName;
		}

		String masterLayoutName = LanguageUtil.get(
			_httpServletRequest, "blank");

		Layout selLayout = _layoutsAdminDisplayContext.getSelLayout();

		if (selLayout.getMasterLayoutPlid() > 0) {
			LayoutPageTemplateEntry layoutPageTemplateEntry =
				LayoutPageTemplateEntryLocalServiceUtil.
					fetchLayoutPageTemplateEntryByPlid(
						selLayout.getMasterLayoutPlid());

			if (layoutPageTemplateEntry != null) {
				masterLayoutName = layoutPageTemplateEntry.getName();
			}
		}

		_masterLayoutName = masterLayoutName;

		return _masterLayoutName;
	}

	public Map<String, Object> getStyleBookConfigurationProps() {
		return HashMapBuilder.<String, Object>put(
			"changeStyleBookURL",
			() -> {
				RequestBackedPortletURLFactory requestBackedPortletURLFactory =
					RequestBackedPortletURLFactoryUtil.create(
						_httpServletRequest);

				StyleBookEntryItemSelectorCriterion
					styleBookEntryItemSelectorCriterion =
						new StyleBookEntryItemSelectorCriterion();

				styleBookEntryItemSelectorCriterion.
					setDesiredItemSelectorReturnTypes(
						new UUIDItemSelectorReturnType());
				styleBookEntryItemSelectorCriterion.setSelPlid(
					_layoutsAdminDisplayContext.getSelPlid());

				return String.valueOf(
					_itemSelector.getItemSelectorURL(
						requestBackedPortletURLFactory,
						_liferayPortletResponse.getNamespace() +
							"selectStyleBook",
						styleBookEntryItemSelectorCriterion));
			}
		).put(
			"styleBookEntryId",
			() -> {
				Layout selLayout = _layoutsAdminDisplayContext.getSelLayout();

				return String.valueOf(selLayout.getStyleBookEntryId());
			}
		).put(
			"styleBookEntryName", getStyleBookEntryName()
		).build();
	}

	public String getStyleBookEntryName() {
		Layout selLayout = _layoutsAdminDisplayContext.getSelLayout();

		StyleBookEntry defaultStyleBookEntry =
			DefaultStyleBookEntryUtil.getDefaultStyleBookEntry(selLayout);

		if (selLayout.getStyleBookEntryId() > 0) {
			return defaultStyleBookEntry.getName();
		}

		if (defaultStyleBookEntry == null) {
			return LanguageUtil.get(_httpServletRequest, "styles-from-theme");
		}

		if (hasEditableMasterLayout() &&
			(selLayout.getMasterLayoutPlid() > 0)) {

			return LanguageUtil.get(_httpServletRequest, "styles-from-master");
		}

		return LanguageUtil.get(_httpServletRequest, "styles-by-default");
	}

	public List<TabsItem> getTabsItems() {
		return TabsItemListBuilder.add(
			tabsItem -> {
				tabsItem.setActive(true);
				tabsItem.setLabel(LanguageUtil.get(_httpServletRequest, "css"));
				tabsItem.setPanelId("css");
			}
		).add(
			tabsItem -> {
				tabsItem.setLabel(
					LanguageUtil.get(_httpServletRequest, "javascript"));
				tabsItem.setPanelId("javascript");
			}
		).build();
	}

	public Map<String, Object> getThemeSpritemapCETConfigurationProps(
		String className, long classPK) {

		return HashMapBuilder.<String, Object>put(
			"selectThemeSpritemapCETEventName", "selectThemeSpritemapCET"
		).put(
			"themeSpritemapCET",
			() -> {
				ClientExtensionEntryRel clientExtensionEntryRel =
					ClientExtensionEntryRelLocalServiceUtil.
						fetchClientExtensionEntryRel(
							PortalUtil.getClassNameId(className), classPK,
							ClientExtensionEntryConstants.TYPE_THEME_SPRITEMAP);

				if (clientExtensionEntryRel == null) {
					return null;
				}

				return _getCETJSONObject(
					clientExtensionEntryRel, true,
					LanguageUtil.format(
						_themeDisplay.getLocale(), "from-x",
						_getLayoutRootNodeName(), false));
			}
		).put(
			"themeSpritemapCETSelectorURL",
			() -> PortletURLBuilder.create(
				_layoutsAdminDisplayContext.getCETItemSelectorURL(
					false, "selectThemeSpritemapCET",
					ClientExtensionEntryConstants.TYPE_THEME_SPRITEMAP)
			).buildString()
		).build();
	}

	public boolean hasEditableMasterLayout() {
		if (_hasEditableMasterLayout != null) {
			return _hasEditableMasterLayout;
		}

		boolean hasEditableMasterLayout = false;

		Layout selLayout = _layoutsAdminDisplayContext.getSelLayout();

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			LayoutPageTemplateEntryLocalServiceUtil.
				fetchLayoutPageTemplateEntryByPlid(selLayout.getPlid());

		if (layoutPageTemplateEntry == null) {
			layoutPageTemplateEntry =
				LayoutPageTemplateEntryLocalServiceUtil.
					fetchLayoutPageTemplateEntryByPlid(selLayout.getClassPK());
		}

		if ((layoutPageTemplateEntry == null) ||
			!Objects.equals(
				layoutPageTemplateEntry.getType(),
				LayoutPageTemplateEntryTypeConstants.TYPE_MASTER_LAYOUT)) {

			hasEditableMasterLayout = true;
		}

		_hasEditableMasterLayout = hasEditableMasterLayout;

		return _hasEditableMasterLayout;
	}

	public boolean hasMasterLayout() {
		if (_hasMasterLayout != null) {
			return _hasMasterLayout;
		}

		boolean hasMasterLayout = false;

		Layout selLayout = _layoutsAdminDisplayContext.getSelLayout();

		if (selLayout.getMasterLayoutPlid() > 0) {
			LayoutPageTemplateEntry layoutPageTemplateEntry =
				LayoutPageTemplateEntryLocalServiceUtil.
					fetchLayoutPageTemplateEntryByPlid(
						selLayout.getMasterLayoutPlid());

			if (layoutPageTemplateEntry != null) {
				hasMasterLayout = true;
			}
		}

		_hasMasterLayout = hasMasterLayout;

		return _hasMasterLayout;
	}

	public boolean hasStyleBooks() {
		if (_hasStyleBooks != null) {
			return _hasStyleBooks;
		}

		boolean hasStyleBooks = false;

		Group liveGroup = StagingUtil.getLiveGroup(
			_layoutsAdminDisplayContext.getGroup());

		int styleBookEntriesCount =
			StyleBookEntryLocalServiceUtil.getStyleBookEntriesCount(
				liveGroup.getGroupId());

		if (styleBookEntriesCount > 0) {
			hasStyleBooks = true;
		}

		_hasStyleBooks = hasStyleBooks;

		return _hasStyleBooks;
	}

	private JSONObject _getCETJSONObject(
		ClientExtensionEntryRel clientExtensionEntryRel, boolean inherited,
		String inheritedLabel) {

		CETManager cetManager = (CETManager)_httpServletRequest.getAttribute(
			CETManager.class.getName());

		CET cet = cetManager.getCET(
			_themeDisplay.getCompanyId(),
			clientExtensionEntryRel.getCETExternalReferenceCode());

		if (cet == null) {
			return null;
		}

		UnicodeProperties typeSettingsUnicodeProperties =
			UnicodePropertiesBuilder.create(
				true
			).fastLoad(
				clientExtensionEntryRel.getTypeSettings()
			).build();

		return JSONUtil.put(
			"cetExternalReferenceCode",
			clientExtensionEntryRel.getCETExternalReferenceCode()
		).put(
			"inherited", inherited
		).put(
			"inheritedLabel", inheritedLabel
		).put(
			"loadType",
			() -> typeSettingsUnicodeProperties.getProperty("loadType", null)
		).put(
			"name", cet.getName(_themeDisplay.getLocale())
		).put(
			"scriptLocation",
			() -> typeSettingsUnicodeProperties.getProperty(
				"scriptLocation", null)
		);
	}

	private JSONArray _getClientExtensionEntryRelsJSONArray(
		String className, long classPK, String type) {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		if (Objects.equals(className, Layout.class.getName())) {
			LayoutSet layoutSet = _layoutsAdminDisplayContext.getSelLayoutSet();

			List<ClientExtensionEntryRel> clientExtensionEntryRels =
				ClientExtensionEntryRelLocalServiceUtil.
					getClientExtensionEntryRels(
						PortalUtil.getClassNameId(LayoutSet.class),
						layoutSet.getLayoutSetId(), type);

			for (ClientExtensionEntryRel clientExtensionEntryRel :
					clientExtensionEntryRels) {

				jsonArray.put(
					() -> _getCETJSONObject(
						clientExtensionEntryRel, true,
						LanguageUtil.format(
							_themeDisplay.getLocale(), "from-x",
							_getLayoutRootNodeName(), false)));
			}

			Layout layout = _layoutsAdminDisplayContext.getSelLayout();

			if ((layout != null) && (layout.getMasterLayoutPlid() > 0)) {
				clientExtensionEntryRels =
					ClientExtensionEntryRelLocalServiceUtil.
						getClientExtensionEntryRels(
							PortalUtil.getClassNameId(Layout.class),
							layout.getMasterLayoutPlid(), type);

				for (ClientExtensionEntryRel clientExtensionEntryRel :
						clientExtensionEntryRels) {

					jsonArray.put(
						() -> _getCETJSONObject(
							clientExtensionEntryRel, true,
							LanguageUtil.format(
								_themeDisplay.getLocale(), "from-x", "master",
								true)));
				}
			}
		}

		List<ClientExtensionEntryRel> clientExtensionEntryRels =
			ClientExtensionEntryRelLocalServiceUtil.getClientExtensionEntryRels(
				PortalUtil.getClassNameId(className), classPK, type);

		for (ClientExtensionEntryRel clientExtensionEntryRel :
				clientExtensionEntryRels) {

			jsonArray.put(
				() -> _getCETJSONObject(
					clientExtensionEntryRel, false, StringPool.DASH));
		}

		return jsonArray;
	}

	private String _getLayoutRootNodeName() {
		LayoutSet layoutSet = _layoutsAdminDisplayContext.getSelLayoutSet();

		Group group = GroupLocalServiceUtil.fetchGroup(layoutSet.getGroupId());

		if (group == null) {
			return StringPool.DASH;
		}

		return group.getLayoutRootNodeName(
			layoutSet.isPrivateLayout(), _themeDisplay.getLocale());
	}

	private Boolean _hasEditableMasterLayout;
	private Boolean _hasMasterLayout;
	private Boolean _hasStyleBooks;
	private final HttpServletRequest _httpServletRequest;
	private final ItemSelector _itemSelector;
	private final LayoutsAdminDisplayContext _layoutsAdminDisplayContext;
	private final LiferayPortletResponse _liferayPortletResponse;
	private String _masterLayoutName;
	private final ThemeDisplay _themeDisplay;

}