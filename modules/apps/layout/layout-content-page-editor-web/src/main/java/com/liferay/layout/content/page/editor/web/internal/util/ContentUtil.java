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
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.fragment.model.FragmentEntryLink;
import com.liferay.fragment.service.FragmentEntryLinkLocalServiceUtil;
import com.liferay.info.display.contributor.InfoDisplayContributor;
import com.liferay.info.display.contributor.InfoDisplayObjectProvider;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorPortletKeys;
import com.liferay.layout.content.page.editor.web.internal.security.permission.resource.ModelResourcePermissionUtil;
import com.liferay.layout.model.LayoutClassedModelUsage;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalServiceUtil;
import com.liferay.layout.service.LayoutClassedModelUsageLocalServiceUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.segments.constants.SegmentsExperienceConstants;
import com.liferay.taglib.security.PermissionsURLTag;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Víctor Galán
 */
public class ContentUtil {

	public static Set<InfoDisplayObjectProvider>
		getFragmentEntryLinkMappedInfoDisplayObjectProviders(
			FragmentEntryLink fragmentEntryLink) {

		return _getFragmentEntryLinkMappedInfoDisplayObjectProviders(
			fragmentEntryLink, new HashSet<>());
	}

	public static Set<InfoDisplayObjectProvider>
			getLayoutMappedInfoDisplayObjectProviders(String layoutData)
		throws PortalException {

		return _getLayoutMappedInfoDisplayObjectProviders(
			layoutData, new HashSet<>());
	}

	public static Set<InfoDisplayObjectProvider>
			getMappedInfoDisplayObjectProviders(long groupId, long plid)
		throws PortalException {

		Set<Long> mappedClassPKs = new HashSet<>();

		Set<InfoDisplayObjectProvider> infoDisplayObjectProviders =
			_getFragmentEntryLinksMappedInfoDisplayObjectProviders(
				groupId, plid, mappedClassPKs);

		infoDisplayObjectProviders.addAll(
			_getLayoutMappedInfoDisplayObjectProviders(
				groupId, plid, mappedClassPKs));

		return infoDisplayObjectProviders;
	}

	public static JSONArray getPageContentsJSONArray(
		long plid, HttpServletRequest httpServletRequest) {

		JSONArray mappedContentsJSONArray = JSONFactoryUtil.createJSONArray();

		List<LayoutClassedModelUsage> layoutClassedModelUsages =
			LayoutClassedModelUsageLocalServiceUtil.
				getLayoutClassedModelUsagesByPlid(plid);

		Set<String> uniqueLayoutClassedModelUsageKeys = new HashSet<>();

		for (LayoutClassedModelUsage layoutClassedModelUsage :
				layoutClassedModelUsages) {

			if (uniqueLayoutClassedModelUsageKeys.contains(
					_generateUniqueLayoutClassedModelUsageKey(
						layoutClassedModelUsage))) {

				continue;
			}

			try {
				mappedContentsJSONArray.put(
					_getPageContentJSONObject(
						layoutClassedModelUsage, httpServletRequest));
			}
			catch (Exception exception) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						StringBundler.concat(
							"An error occurred while getting mapped content ",
							"with class PK ",
							layoutClassedModelUsage.getClassPK(),
							" and class name ID ",
							layoutClassedModelUsage.getClassNameId()),
						exception);
				}
			}

			uniqueLayoutClassedModelUsageKeys.add(
				_generateUniqueLayoutClassedModelUsageKey(
					layoutClassedModelUsage));
		}

		return mappedContentsJSONArray;
	}

	private static String _generateUniqueLayoutClassedModelUsageKey(
		LayoutClassedModelUsage layoutClassedModelUsage) {

		return layoutClassedModelUsage.getClassNameId() + StringPool.DASH +
			layoutClassedModelUsage.getClassPK();
	}

	private static JSONObject _getActionsJSONObject(
			LayoutClassedModelUsage layoutClassedModelUsage,
			ThemeDisplay themeDisplay, HttpServletRequest httpServletRequest)
		throws Exception {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		InfoDisplayContributor infoDisplayContributor =
			InfoDisplayContributorTrackerUtil.getInfoDisplayContributor(
				layoutClassedModelUsage.getClassName());

		InfoDisplayObjectProvider infoDisplayObjectProvider =
			infoDisplayContributor.getInfoDisplayObjectProvider(
				layoutClassedModelUsage.getClassPK());

		if (ModelResourcePermissionUtil.contains(
				themeDisplay.getPermissionChecker(),
				layoutClassedModelUsage.getClassName(),
				layoutClassedModelUsage.getClassPK(), ActionKeys.UPDATE)) {

			String editURL = InfoEditURLProviderUtil.getURLEdit(
				layoutClassedModelUsage.getClassName(),
				infoDisplayObjectProvider.getDisplayObject(),
				httpServletRequest);

			if (editURL != null) {
				jsonObject.put("editURL", editURL);
			}
		}

		if (ModelResourcePermissionUtil.contains(
				themeDisplay.getPermissionChecker(),
				layoutClassedModelUsage.getClassName(),
				layoutClassedModelUsage.getClassPK(), ActionKeys.PERMISSIONS)) {

			String permissionsURL = PermissionsURLTag.doTag(
				StringPool.BLANK, layoutClassedModelUsage.getClassName(),
				HtmlUtil.escape(
					infoDisplayObjectProvider.getTitle(
						themeDisplay.getLocale())),
				null, String.valueOf(layoutClassedModelUsage.getClassPK()),
				LiferayWindowState.POP_UP.toString(), null, httpServletRequest);

			if (Validator.isNotNull(permissionsURL)) {
				jsonObject.put("permissionsURL", permissionsURL);
			}
		}

		PortletURL viewUsagesURL = PortletURLFactoryUtil.create(
			httpServletRequest,
			ContentPageEditorPortletKeys.CONTENT_PAGE_EDITOR_PORTLET,
			PortletRequest.RENDER_PHASE);

		viewUsagesURL.setParameter(
			"mvcPath", "/view_layout_classed_model_usages.jsp");
		viewUsagesURL.setParameter(
			"className", layoutClassedModelUsage.getClassName());
		viewUsagesURL.setParameter(
			"classPK", String.valueOf(layoutClassedModelUsage.getClassPK()));
		viewUsagesURL.setWindowState(LiferayWindowState.POP_UP);

		return jsonObject.put("viewUsagesURL", viewUsagesURL.toString());
	}

	private static Set<InfoDisplayObjectProvider>
		_getFragmentEntryLinkMappedInfoDisplayObjectProviders(
			FragmentEntryLink fragmentEntryLink, Set<Long> mappedClassPKs) {

		JSONObject editableValuesJSONObject = null;

		try {
			editableValuesJSONObject = JSONFactoryUtil.createJSONObject(
				fragmentEntryLink.getEditableValues());
		}
		catch (JSONException jsonException) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Unable to create JSON object from " +
						fragmentEntryLink.getEditableValues(),
					jsonException);
			}

			return Collections.emptySet();
		}

		Set<InfoDisplayObjectProvider> infoDisplayObjectProviders =
			new HashSet<>();

		Iterator<String> keysIterator = editableValuesJSONObject.keys();

		while (keysIterator.hasNext()) {
			String key = keysIterator.next();

			JSONObject editableProcessorJSONObject =
				editableValuesJSONObject.getJSONObject(key);

			if (editableProcessorJSONObject == null) {
				continue;
			}

			Iterator<String> editableKeysIterator =
				editableProcessorJSONObject.keys();

			while (editableKeysIterator.hasNext()) {
				String editableKey = editableKeysIterator.next();

				JSONObject editableJSONObject =
					editableProcessorJSONObject.getJSONObject(editableKey);

				if (editableJSONObject == null) {
					continue;
				}

				JSONObject configJSONObject = editableJSONObject.getJSONObject(
					"config");

				if ((configJSONObject != null) &&
					(configJSONObject.length() > 0)) {

					InfoDisplayObjectProvider infoDisplayObjectProvider =
						_getInfoDisplayObjectProvider(
							configJSONObject, mappedClassPKs);

					if (infoDisplayObjectProvider != null) {
						infoDisplayObjectProviders.add(
							infoDisplayObjectProvider);
					}
				}

				JSONObject itemSelectorJSONObject =
					editableJSONObject.getJSONObject("itemSelector");

				if ((itemSelectorJSONObject != null) &&
					(itemSelectorJSONObject.length() > 0)) {

					InfoDisplayObjectProvider infoDisplayObjectProvider =
						_getInfoDisplayObjectProvider(
							itemSelectorJSONObject, mappedClassPKs);

					if (infoDisplayObjectProvider != null) {
						infoDisplayObjectProviders.add(
							infoDisplayObjectProvider);
					}
				}

				InfoDisplayObjectProvider infoDisplayObjectProvider =
					_getInfoDisplayObjectProvider(
						editableJSONObject, mappedClassPKs);

				if (infoDisplayObjectProvider == null) {
					continue;
				}

				infoDisplayObjectProviders.add(infoDisplayObjectProvider);
			}
		}

		return infoDisplayObjectProviders;
	}

	private static Set<InfoDisplayObjectProvider>
		_getFragmentEntryLinksMappedInfoDisplayObjectProviders(
			long groupId, long plid, Set<Long> mappedClassPKs) {

		Set<InfoDisplayObjectProvider> infoDisplayObjectProviders =
			new HashSet<>();

		List<FragmentEntryLink> fragmentEntryLinks =
			FragmentEntryLinkLocalServiceUtil.getFragmentEntryLinks(
				groupId, PortalUtil.getClassNameId(Layout.class.getName()),
				plid);

		for (FragmentEntryLink fragmentEntryLink : fragmentEntryLinks) {
			infoDisplayObjectProviders.addAll(
				_getFragmentEntryLinkMappedInfoDisplayObjectProviders(
					fragmentEntryLink, mappedClassPKs));
		}

		return infoDisplayObjectProviders;
	}

	private static InfoDisplayObjectProvider _getInfoDisplayObjectProvider(
		JSONObject jsonObject, Set<Long> mappedClassPKs) {

		if (!jsonObject.has("classNameId") || !jsonObject.has("classPK")) {
			return null;
		}

		long classPK = jsonObject.getLong("classPK");

		if (classPK <= 0) {
			return null;
		}

		if (mappedClassPKs.contains(classPK)) {
			return null;
		}

		mappedClassPKs.add(classPK);

		long classNameId = jsonObject.getLong("classNameId");

		if (classNameId <= 0) {
			return null;
		}

		try {
			InfoDisplayContributor infoDisplayContributor =
				InfoDisplayContributorTrackerUtil.getInfoDisplayContributor(
					PortalUtil.getClassName(classNameId));

			if (infoDisplayContributor == null) {
				return null;
			}

			return infoDisplayContributor.getInfoDisplayObjectProvider(classPK);
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						"Unable to get info display object provider for class ",
						"name ID ", classNameId, " with primary key ", classPK),
					portalException);
			}
		}

		return null;
	}

	private static Set<InfoDisplayObjectProvider>
			_getLayoutMappedInfoDisplayObjectProviders(
				long groupId, long plid, Set<Long> mappedClassPKs)
		throws PortalException {

		LayoutPageTemplateStructure layoutPageTemplateStructure =
			LayoutPageTemplateStructureLocalServiceUtil.
				fetchLayoutPageTemplateStructure(
					groupId, PortalUtil.getClassNameId(Layout.class.getName()),
					plid, false);

		return _getLayoutMappedInfoDisplayObjectProviders(
			layoutPageTemplateStructure.getData(
				SegmentsExperienceConstants.ID_DEFAULT),
			mappedClassPKs);
	}

	private static Set<InfoDisplayObjectProvider>
			_getLayoutMappedInfoDisplayObjectProviders(
				String layoutData, Set<Long> mappedClassPKs)
		throws PortalException {

		JSONObject layoutDataJSONObject = JSONFactoryUtil.createJSONObject(
			layoutData);

		JSONArray structureJSONArray = layoutDataJSONObject.getJSONArray(
			"structure");

		if (structureJSONArray == null) {
			return Collections.emptySet();
		}

		Set<InfoDisplayObjectProvider> infoDisplayObjectProviders =
			new HashSet<>();

		Iterator<JSONObject> iteratorStructure = structureJSONArray.iterator();

		iteratorStructure.forEachRemaining(
			structureJSONObject -> {
				JSONObject configJSONObject = structureJSONObject.getJSONObject(
					"config");

				if (configJSONObject != null) {
					JSONObject backgroundImageJSONObject =
						configJSONObject.getJSONObject("backgroundImage");

					if (backgroundImageJSONObject != null) {
						InfoDisplayObjectProvider infoDisplayObjectProvider =
							_getInfoDisplayObjectProvider(
								backgroundImageJSONObject, mappedClassPKs);

						if (infoDisplayObjectProvider != null) {
							infoDisplayObjectProviders.add(
								infoDisplayObjectProvider);
						}
					}
				}
			});

		return infoDisplayObjectProviders;
	}

	private static JSONObject _getPageContentJSONObject(
			LayoutClassedModelUsage layoutClassedModelUsage,
			HttpServletRequest httpServletRequest)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		JSONObject mappedContentJSONObject = JSONUtil.put(
			"actions",
			_getActionsJSONObject(
				layoutClassedModelUsage, themeDisplay, httpServletRequest)
		).put(
			"className", layoutClassedModelUsage.getClassName()
		).put(
			"classNameId", layoutClassedModelUsage.getClassNameId()
		).put(
			"classPK", layoutClassedModelUsage.getClassPK()
		);

		InfoDisplayContributor infoDisplayContributor =
			InfoDisplayContributorTrackerUtil.getInfoDisplayContributor(
				layoutClassedModelUsage.getClassName());

		InfoDisplayObjectProvider infoDisplayObjectProvider =
			infoDisplayContributor.getInfoDisplayObjectProvider(
				layoutClassedModelUsage.getClassPK());

		return mappedContentJSONObject.put(
			"name",
			ResourceActionsUtil.getModelResource(
				themeDisplay.getLocale(),
				layoutClassedModelUsage.getClassName())
		).put(
			"status", _getStatusJSONObject(layoutClassedModelUsage)
		).put(
			"title",
			infoDisplayObjectProvider.getTitle(themeDisplay.getLocale())
		).put(
			"usagesCount",
			LayoutClassedModelUsageLocalServiceUtil.
				getUniqueLayoutClassedModelUsagesCount(
					layoutClassedModelUsage.getClassNameId(),
					layoutClassedModelUsage.getClassPK())
		);
	}

	private static JSONObject _getStatusJSONObject(
			LayoutClassedModelUsage layoutClassedModelUsage)
		throws PortalException {

		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.
				getAssetRendererFactoryByClassNameId(
					layoutClassedModelUsage.getClassNameId());

		if (assetRendererFactory == null) {
			return JSONUtil.put(
				"hasApprovedVersion", false
			).put(
				"label",
				WorkflowConstants.getStatusLabel(
					WorkflowConstants.STATUS_APPROVED)
			).put(
				"style",
				WorkflowConstants.getStatusStyle(
					WorkflowConstants.STATUS_APPROVED)
			);
		}

		AssetRenderer latestAssetRenderer =
			assetRendererFactory.getAssetRenderer(
				layoutClassedModelUsage.getClassPK(),
				AssetRendererFactory.TYPE_LATEST);

		boolean hasApprovedVersion = false;

		if (latestAssetRenderer.getStatus() !=
				WorkflowConstants.STATUS_APPROVED) {

			AssetRenderer assetRenderer = assetRendererFactory.getAssetRenderer(
				layoutClassedModelUsage.getClassPK(),
				AssetRendererFactory.TYPE_LATEST_APPROVED);

			if (assetRenderer.getStatus() ==
					WorkflowConstants.STATUS_APPROVED) {

				hasApprovedVersion = true;
			}
		}

		return JSONUtil.put(
			"hasApprovedVersion", hasApprovedVersion
		).put(
			"label",
			WorkflowConstants.getStatusLabel(latestAssetRenderer.getStatus())
		).put(
			"style",
			WorkflowConstants.getStatusStyle(latestAssetRenderer.getStatus())
		);
	}

	private static final Log _log = LogFactoryUtil.getLog(ContentUtil.class);

}