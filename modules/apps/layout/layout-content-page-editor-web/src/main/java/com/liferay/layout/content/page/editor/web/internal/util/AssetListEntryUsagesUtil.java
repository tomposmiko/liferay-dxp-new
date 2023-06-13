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

package com.liferay.layout.content.page.editor.web.internal.util;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.ClassType;
import com.liferay.asset.kernel.model.ClassTypeReader;
import com.liferay.asset.list.model.AssetListEntry;
import com.liferay.asset.list.model.AssetListEntryUsage;
import com.liferay.asset.list.service.AssetListEntryLocalService;
import com.liferay.asset.list.service.AssetListEntryUsageLocalService;
import com.liferay.asset.util.AssetPublisherAddItemHolder;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentEntryLinkLocalService;
import com.liferay.info.collection.provider.InfoCollectionProvider;
import com.liferay.info.collection.provider.RelatedInfoItemCollectionProvider;
import com.liferay.info.collection.provider.SingleFormVariationInfoCollectionProvider;
import com.liferay.info.item.InfoItemFormVariation;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.item.provider.InfoItemFormVariationsProvider;
import com.liferay.info.list.provider.item.selector.criterion.InfoListProviderItemSelectorReturnType;
import com.liferay.info.search.InfoSearchClassMapperRegistry;
import com.liferay.item.selector.criteria.InfoListItemSelectorReturnType;
import com.liferay.layout.security.permission.resource.LayoutContentModelResourcePermission;
import com.liferay.layout.util.structure.CollectionStyledLayoutStructureItem;
import com.liferay.layout.util.structure.LayoutStructure;
import com.liferay.layout.util.structure.LayoutStructureItem;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.ResourceActions;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.security.PermissionsURLTag;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Víctor Galán
 */
@Component(service = {})
public class AssetListEntryUsagesUtil {

	public static JSONArray getPageContentsJSONArray(
			List<String> hiddenItemIds, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			LayoutStructure layoutStructure, long plid,
			List<String> restrictedItemIds)
		throws PortalException {

		JSONArray mappedContentsJSONArray = _jsonFactory.createJSONArray();

		Set<String> uniqueAssetListEntryUsagesKeys = new HashSet<>();

		List<AssetListEntryUsage> assetListEntryUsages =
			_assetListEntryUsageLocalService.getAssetEntryListUsagesByPlid(
				plid);

		String redirect = _getRedirect(httpServletRequest);

		for (AssetListEntryUsage assetListEntryUsage : assetListEntryUsages) {
			String uniqueKey = _generateUniqueLayoutClassedModelUsageKey(
				assetListEntryUsage);

			if (uniqueAssetListEntryUsagesKeys.contains(uniqueKey) ||
				_isCollectionStyledLayoutStructureItemDeletedOrHidden(
					assetListEntryUsage, hiddenItemIds, layoutStructure) ||
				_isFragmentEntryLinkDeletedOrHidden(
					assetListEntryUsage, hiddenItemIds, layoutStructure)) {

				continue;
			}

			mappedContentsJSONArray.put(
				_getPageContentJSONObject(
					assetListEntryUsage, httpServletRequest,
					httpServletResponse, redirect, restrictedItemIds));

			uniqueAssetListEntryUsagesKeys.add(uniqueKey);
		}

		return mappedContentsJSONArray;
	}

	@Reference(unbind = "-")
	protected void setAssetListEntryLocalService(
		AssetListEntryLocalService assetListEntryLocalService) {

		_assetListEntryLocalService = assetListEntryLocalService;
	}

	@Reference(unbind = "-")
	protected void setAssetListEntryUsageLocalService(
		AssetListEntryUsageLocalService assetListEntryUsageLocalService) {

		_assetListEntryUsageLocalService = assetListEntryUsageLocalService;
	}

	@Reference(unbind = "-")
	protected void setFragmentEntryLinkLocalService(
		FragmentEntryLinkLocalService fragmentEntryLinkLocalService) {

		_fragmentEntryLinkLocalService = fragmentEntryLinkLocalService;
	}

	@Reference(unbind = "-")
	protected void setInfoItemServiceRegistry(
		InfoItemServiceRegistry infoItemServiceRegistry) {

		_infoItemServiceRegistry = infoItemServiceRegistry;
	}

	@Reference(unbind = "-")
	protected void setInfoSearchClassMapperRegistry(
		InfoSearchClassMapperRegistry infoSearchClassMapperRegistry) {

		_infoSearchClassMapperRegistry = infoSearchClassMapperRegistry;
	}

	@Reference(unbind = "-")
	protected void setJSONFactory(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	@Reference(unbind = "-")
	protected void setLanguage(Language language) {
		_language = language;
	}

	@Reference(unbind = "-")
	protected void setLayoutContentModelResourcePermission(
		LayoutContentModelResourcePermission
			layoutContentModelResourcePermission) {

		_layoutContentModelResourcePermission =
			layoutContentModelResourcePermission;
	}

	@Reference(unbind = "-")
	protected void setPortal(Portal portal) {
		_portal = portal;
	}

	@Reference(unbind = "-")
	protected void setResourceActions(ResourceActions resourceActions) {
		_resourceActions = resourceActions;
	}

	private static String _generateUniqueLayoutClassedModelUsageKey(
		AssetListEntryUsage assetListEntryUsage) {

		return StringBundler.concat(
			assetListEntryUsage.getClassNameId(), StringPool.DASH,
			assetListEntryUsage.getContainerKey(), StringPool.DASH,
			assetListEntryUsage.getKey());
	}

	private static String _getAssetEntryListSubtypeLabel(
		AssetListEntry assetListEntry, Locale locale) {

		String typeLabel = _resourceActions.getModelResource(
			locale, assetListEntry.getAssetEntryType());

		String subtypeLabel = _getSubtypeLabel(assetListEntry, locale);

		if (Validator.isNull(subtypeLabel)) {
			return typeLabel;
		}

		return typeLabel + " - " + subtypeLabel;
	}

	private static JSONObject _getAssetListEntryActionsJSONObject(
		AssetListEntry assetListEntry, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, String redirect) {

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		String editURL = _getAssetListEntryEditURL(
			assetListEntry, httpServletRequest, redirect);

		if (Validator.isNotNull(editURL)) {
			jsonObject.put("editURL", editURL);
		}

		String permissionsURL = _getAssetListEntryPermissionsURL(
			assetListEntry, httpServletRequest);

		if (Validator.isNotNull(permissionsURL)) {
			jsonObject.put("permissionsURL", permissionsURL);
		}

		String viewItemsURL = _getAssetListEntryViewItemsURL(
			assetListEntry, httpServletRequest, redirect);

		if (Validator.isNotNull(viewItemsURL)) {
			jsonObject.put("viewItemsURL", viewItemsURL);
		}

		try {
			JSONArray addItemsJSONArray = _getAssetListEntryAddItemsJSONArray(
				assetListEntry, httpServletRequest, httpServletResponse);

			if ((addItemsJSONArray != null) &&
				(addItemsJSONArray.length() > 0)) {

				jsonObject.put("addItems", addItemsJSONArray);
			}
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return jsonObject;
	}

	private static JSONArray _getAssetListEntryAddItemsJSONArray(
			AssetListEntry assetListEntry,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		JSONArray addItemsJSONArray = _jsonFactory.createJSONArray();

		List<AssetPublisherAddItemHolder> assetPublisherAddItemHolders =
			AssetHelperUtil.getAssetPublisherAddItemHolders(
				assetListEntry, httpServletRequest, httpServletResponse);

		for (AssetPublisherAddItemHolder assetPublisherAddItemHolder :
				assetPublisherAddItemHolders) {

			addItemsJSONArray.put(
				JSONUtil.put(
					"href", assetPublisherAddItemHolder.getPortletURL()
				).put(
					"label", assetPublisherAddItemHolder.getModelResource()
				));
		}

		return addItemsJSONArray;
	}

	private static String _getAssetListEntryEditURL(
		AssetListEntry assetListEntry, HttpServletRequest httpServletRequest,
		String redirect) {

		PortletURL portletURL = null;

		try {
			portletURL = PortletProviderUtil.getPortletURL(
				httpServletRequest, AssetListEntry.class.getName(),
				PortletProvider.Action.EDIT);
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}
		}

		if (portletURL == null) {
			return StringPool.BLANK;
		}

		portletURL.setParameter("redirect", redirect);
		portletURL.setParameter("backURL", redirect);

		portletURL.setParameter(
			"assetListEntryId",
			String.valueOf(assetListEntry.getAssetListEntryId()));

		return portletURL.toString();
	}

	private static String _getAssetListEntryPermissionsURL(
		AssetListEntry assetListEntry, HttpServletRequest httpServletRequest) {

		String permissionsURL = null;

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (_layoutContentModelResourcePermission.contains(
				themeDisplay.getPermissionChecker(),
				AssetListEntry.class.getName(),
				assetListEntry.getAssetListEntryId(), ActionKeys.PERMISSIONS)) {

			try {
				permissionsURL = PermissionsURLTag.doTag(
					StringPool.BLANK, AssetListEntry.class.getName(),
					HtmlUtil.escape(assetListEntry.getTitle()), null,
					String.valueOf(assetListEntry.getAssetListEntryId()),
					LiferayWindowState.POP_UP.toString(), null,
					httpServletRequest);
			}
			catch (Exception exception) {
				if (_log.isDebugEnabled()) {
					_log.debug(exception);
				}
			}
		}

		return permissionsURL;
	}

	private static String _getAssetListEntryViewItemsURL(
		AssetListEntry assetListEntry, HttpServletRequest httpServletRequest,
		String redirect) {

		PortletURL portletURL = null;

		try {
			portletURL = PortletProviderUtil.getPortletURL(
				httpServletRequest, AssetListEntry.class.getName(),
				PortletProvider.Action.BROWSE);

			if (portletURL == null) {
				return StringPool.BLANK;
			}

			portletURL.setParameter("redirect", redirect);

			portletURL.setParameter(
				"collectionPK",
				String.valueOf(assetListEntry.getAssetListEntryId()));
			portletURL.setParameter(
				"collectionType",
				InfoListItemSelectorReturnType.class.getName());
			portletURL.setParameter(
				"showActions", String.valueOf(Boolean.TRUE));
			portletURL.setWindowState(LiferayWindowState.POP_UP);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return portletURL.toString();
	}

	private static AssetRendererFactory<?> _getAssetRendererFactory(
		String className) {

		return AssetRendererFactoryRegistryUtil.
			getAssetRendererFactoryByClassName(
				_infoSearchClassMapperRegistry.getSearchClassName(className));
	}

	private static long _getCollectionStyledLayoutStructureItemClassNameId() {
		if (_collectionStyledLayoutStructureItemClassNameId != null) {
			return _collectionStyledLayoutStructureItemClassNameId;
		}

		_collectionStyledLayoutStructureItemClassNameId =
			_portal.getClassNameId(
				CollectionStyledLayoutStructureItem.class.getName());

		return _collectionStyledLayoutStructureItemClassNameId;
	}

	private static long _getFragmentEntryLinkClassNameId() {
		if (_fragmentEntryLinkClassNameId != null) {
			return _fragmentEntryLinkClassNameId;
		}

		_fragmentEntryLinkClassNameId = _portal.getClassNameId(
			FragmentEntryLink.class.getName());

		return _fragmentEntryLinkClassNameId;
	}

	private static JSONObject _getInfoCollectionProviderActionsJSONObject(
		InfoCollectionProvider<?> infoCollectionProvider,
		HttpServletRequest httpServletRequest, String redirect) {

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		String viewItemsURL = _getInfoCollectionProviderViewItemsURL(
			infoCollectionProvider, httpServletRequest, redirect);

		if (Validator.isNotNull(viewItemsURL)) {
			jsonObject.put("viewItemsURL", viewItemsURL);
		}

		return jsonObject;
	}

	private static String _getInfoCollectionProviderSubtypeLabel(
		long groupId, InfoCollectionProvider<?> infoCollectionProvider,
		Locale locale) {

		String className = infoCollectionProvider.getCollectionItemClassName();

		if (Validator.isNull(className)) {
			return StringPool.BLANK;
		}

		if (!(infoCollectionProvider instanceof
				SingleFormVariationInfoCollectionProvider)) {

			return _resourceActions.getModelResource(locale, className);
		}

		InfoItemFormVariationsProvider<?> infoItemFormVariationsProvider =
			_infoItemServiceRegistry.getFirstInfoItemService(
				InfoItemFormVariationsProvider.class, className);

		if (infoItemFormVariationsProvider == null) {
			return _resourceActions.getModelResource(locale, className);
		}

		SingleFormVariationInfoCollectionProvider<?>
			singleFormVariationInfoCollectionProvider =
				(SingleFormVariationInfoCollectionProvider<?>)
					infoCollectionProvider;

		InfoItemFormVariation infoItemFormVariation =
			infoItemFormVariationsProvider.getInfoItemFormVariation(
				groupId,
				singleFormVariationInfoCollectionProvider.
					getFormVariationKey());

		if (infoItemFormVariation == null) {
			return _resourceActions.getModelResource(locale, className);
		}

		return _resourceActions.getModelResource(locale, className) + " - " +
			infoItemFormVariation.getLabel(locale);
	}

	private static String _getInfoCollectionProviderViewItemsURL(
		InfoCollectionProvider<?> infoCollectionProvider,
		HttpServletRequest httpServletRequest, String redirect) {

		PortletURL portletURL = null;

		try {
			portletURL = PortletProviderUtil.getPortletURL(
				httpServletRequest, AssetListEntry.class.getName(),
				PortletProvider.Action.BROWSE);

			if (portletURL == null) {
				return StringPool.BLANK;
			}

			portletURL.setParameter("redirect", redirect);

			portletURL.setParameter(
				"collectionPK",
				String.valueOf(infoCollectionProvider.getKey()));
			portletURL.setParameter(
				"collectionType",
				InfoListProviderItemSelectorReturnType.class.getName());
			portletURL.setParameter(
				"showActions", String.valueOf(Boolean.TRUE));

			portletURL.setWindowState(LiferayWindowState.POP_UP);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return portletURL.toString();
	}

	private static JSONObject _getPageContentJSONObject(
		AssetListEntryUsage assetListEntryUsage,
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse, String redirect,
		List<String> restrictedItemIds) {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		JSONObject mappedContentJSONObject = JSONUtil.put(
			"className", assetListEntryUsage.getClassName()
		).put(
			"classNameId", assetListEntryUsage.getClassNameId()
		).put(
			"classPK", assetListEntryUsage.getKey()
		).put(
			"icon", "list-ul"
		).put(
			"isRestricted",
			() -> {
				if ((assetListEntryUsage.getContainerType() ==
						_getCollectionStyledLayoutStructureItemClassNameId()) &&
					restrictedItemIds.contains(
						assetListEntryUsage.getContainerKey())) {

					return true;
				}

				return false;
			}
		).put(
			"type", _language.get(httpServletRequest, "collection")
		);

		if (Objects.equals(
				assetListEntryUsage.getClassName(),
				AssetListEntry.class.getName())) {

			AssetListEntry assetListEntry =
				_assetListEntryLocalService.fetchAssetListEntry(
					GetterUtil.getLong(assetListEntryUsage.getKey()));

			if (assetListEntry != null) {
				mappedContentJSONObject.put(
					"actions",
					_getAssetListEntryActionsJSONObject(
						assetListEntry, httpServletRequest, httpServletResponse,
						redirect)
				).put(
					"subtype",
					_getAssetEntryListSubtypeLabel(
						assetListEntry, themeDisplay.getLocale())
				).put(
					"title", assetListEntry.getTitle()
				);
			}
		}

		if (Objects.equals(
				assetListEntryUsage.getClassName(),
				InfoCollectionProvider.class.getName())) {

			InfoCollectionProvider<?> infoCollectionProvider =
				_infoItemServiceRegistry.getInfoItemService(
					InfoCollectionProvider.class, assetListEntryUsage.getKey());

			if (infoCollectionProvider == null) {
				infoCollectionProvider =
					_infoItemServiceRegistry.getInfoItemService(
						RelatedInfoItemCollectionProvider.class,
						assetListEntryUsage.getKey());
			}

			if (infoCollectionProvider != null) {
				if (!(infoCollectionProvider instanceof
						RelatedInfoItemCollectionProvider)) {

					mappedContentJSONObject.put(
						"actions",
						_getInfoCollectionProviderActionsJSONObject(
							infoCollectionProvider, httpServletRequest,
							redirect));
				}

				mappedContentJSONObject.put(
					"subtype",
					_getInfoCollectionProviderSubtypeLabel(
						themeDisplay.getScopeGroupId(), infoCollectionProvider,
						themeDisplay.getLocale())
				).put(
					"title",
					infoCollectionProvider.getLabel(themeDisplay.getLocale())
				);
			}
		}

		return mappedContentJSONObject;
	}

	private static String _getRedirect(HttpServletRequest httpServletRequest) {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		try {
			return HttpComponentsUtil.setParameter(
				_portal.getLayoutRelativeURL(layout, themeDisplay), "p_l_mode",
				Constants.EDIT);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}
		}

		return themeDisplay.getURLCurrent();
	}

	private static String _getSubtypeLabel(
		AssetListEntry assetListEntry, Locale locale) {

		long classTypeId = GetterUtil.getLong(
			assetListEntry.getAssetEntrySubtype(), -1);

		if (classTypeId < 0) {
			return StringPool.BLANK;
		}

		AssetRendererFactory<?> assetRendererFactory = _getAssetRendererFactory(
			assetListEntry.getAssetEntryType());

		if ((assetRendererFactory == null) ||
			!assetRendererFactory.isSupportsClassTypes()) {

			return StringPool.BLANK;
		}

		ClassTypeReader classTypeReader =
			assetRendererFactory.getClassTypeReader();

		try {
			ClassType classType = classTypeReader.getClassType(
				classTypeId, locale);

			return classType.getName();
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return StringPool.BLANK;
		}
	}

	private static boolean
		_isCollectionStyledLayoutStructureItemDeletedOrHidden(
			AssetListEntryUsage assetListEntryUsage, List<String> hiddenItemIds,
			LayoutStructure layoutStructure) {

		if (assetListEntryUsage.getContainerType() !=
				_getCollectionStyledLayoutStructureItemClassNameId()) {

			return false;
		}

		LayoutStructureItem layoutStructureItem =
			layoutStructure.getLayoutStructureItem(
				assetListEntryUsage.getContainerKey());

		if (layoutStructureItem == null) {
			_assetListEntryUsageLocalService.deleteAssetListEntryUsage(
				assetListEntryUsage);

			return true;
		}

		if (layoutStructure.isItemMarkedForDeletion(
				layoutStructureItem.getItemId()) ||
			hiddenItemIds.contains(layoutStructureItem.getItemId())) {

			return true;
		}

		return false;
	}

	private static boolean _isFragmentEntryLinkDeletedOrHidden(
		AssetListEntryUsage assetListEntryUsage, List<String> hiddenItemIds,
		LayoutStructure layoutStructure) {

		if (assetListEntryUsage.getContainerType() !=
				_getFragmentEntryLinkClassNameId()) {

			return false;
		}

		FragmentEntryLink fragmentEntryLink =
			_fragmentEntryLinkLocalService.fetchFragmentEntryLink(
				GetterUtil.getLong(assetListEntryUsage.getContainerKey()));

		if (fragmentEntryLink == null) {
			_assetListEntryUsageLocalService.deleteAssetListEntryUsage(
				assetListEntryUsage);

			return true;
		}

		if (fragmentEntryLink.isDeleted()) {
			return true;
		}

		LayoutStructureItem layoutStructureItem =
			layoutStructure.getLayoutStructureItemByFragmentEntryLinkId(
				fragmentEntryLink.getFragmentEntryLinkId());

		if ((layoutStructureItem == null) ||
			layoutStructure.isItemMarkedForDeletion(
				layoutStructureItem.getItemId()) ||
			hiddenItemIds.contains(layoutStructureItem.getItemId())) {

			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AssetListEntryUsagesUtil.class);

	private static AssetListEntryLocalService _assetListEntryLocalService;
	private static AssetListEntryUsageLocalService
		_assetListEntryUsageLocalService;
	private static Long _collectionStyledLayoutStructureItemClassNameId;
	private static Long _fragmentEntryLinkClassNameId;
	private static FragmentEntryLinkLocalService _fragmentEntryLinkLocalService;
	private static InfoItemServiceRegistry _infoItemServiceRegistry;
	private static InfoSearchClassMapperRegistry _infoSearchClassMapperRegistry;
	private static JSONFactory _jsonFactory;
	private static Language _language;
	private static LayoutContentModelResourcePermission
		_layoutContentModelResourcePermission;
	private static Portal _portal;
	private static ResourceActions _resourceActions;

}