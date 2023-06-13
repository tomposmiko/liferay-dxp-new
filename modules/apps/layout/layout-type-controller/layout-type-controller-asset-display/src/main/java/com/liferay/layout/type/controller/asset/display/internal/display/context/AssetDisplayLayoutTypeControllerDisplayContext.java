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

package com.liferay.layout.type.controller.asset.display.internal.display.context;

import com.liferay.asset.display.contributor.AssetDisplayContributor;
import com.liferay.asset.display.contributor.AssetDisplayContributorTracker;
import com.liferay.asset.display.contributor.constants.AssetDisplayWebKeys;
import com.liferay.asset.display.page.constants.AssetDisplayPageConstants;
import com.liferay.asset.display.page.model.AssetDisplayPageEntry;
import com.liferay.asset.display.page.service.AssetDisplayPageEntryLocalServiceUtil;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorWebKeys;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class AssetDisplayLayoutTypeControllerDisplayContext {

	public AssetDisplayLayoutTypeControllerDisplayContext(
		HttpServletRequest request) {

		_request = request;

		AssetEntry assetEntry = (AssetEntry)request.getAttribute(
			WebKeys.LAYOUT_ASSET_ENTRY);

		if (assetEntry == null) {
			long assetEntryId = ParamUtil.getLong(request, "assetEntryId");

			assetEntry = AssetEntryLocalServiceUtil.fetchEntry(assetEntryId);

			if (assetEntry != null) {
				request.setAttribute(WebKeys.LAYOUT_ASSET_ENTRY, assetEntry);
			}
		}

		_assetEntry = assetEntry;

		AssetDisplayContributor assetDisplayContributor =
			(AssetDisplayContributor)_request.getAttribute(
				AssetDisplayWebKeys.ASSET_DISPLAY_CONTRIBUTOR);

		if ((assetDisplayContributor == null) && (assetEntry != null)) {
			AssetDisplayContributorTracker assetDisplayContributorTracker =
				(AssetDisplayContributorTracker)request.getAttribute(
					ContentPageEditorWebKeys.ASSET_DISPLAY_CONTRIBUTOR_TRACKER);

			assetDisplayContributor =
				assetDisplayContributorTracker.getAssetDisplayContributor(
					_assetEntry.getClassName());
		}

		_assetDisplayContributor = assetDisplayContributor;
	}

	public Map<String, Object> getAssetDisplayFieldsValues()
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)_request.getAttribute(
			WebKeys.THEME_DISPLAY);

		long versionClassPK = GetterUtil.getLong(
			_request.getAttribute(AssetDisplayWebKeys.VERSION_CLASS_PK));

		if (versionClassPK > 0) {
			return _assetDisplayContributor.getAssetDisplayFieldsValues(
				_assetEntry, versionClassPK, themeDisplay.getLocale());
		}

		return _assetDisplayContributor.getAssetDisplayFieldsValues(
			_assetEntry, themeDisplay.getLocale());
	}

	public AssetEntry getAssetEntry() {
		return _assetEntry;
	}

	public long getLayoutPageTemplateEntryId() {
		AssetDisplayPageEntry assetDisplayPageEntry =
			AssetDisplayPageEntryLocalServiceUtil.fetchAssetDisplayPageEntry(
				_assetEntry.getGroupId(), _assetEntry.getClassNameId(),
				_assetEntry.getClassPK());

		if ((assetDisplayPageEntry == null) ||
			(assetDisplayPageEntry.getType() ==
				AssetDisplayPageConstants.TYPE_NONE)) {

			return 0;
		}

		if (assetDisplayPageEntry.getType() ==
				AssetDisplayPageConstants.TYPE_SPECIFIC) {

			return assetDisplayPageEntry.getLayoutPageTemplateEntryId();
		}

		LayoutPageTemplateEntry defaultLayoutPageTemplateEntry =
			LayoutPageTemplateEntryServiceUtil.
				fetchDefaultLayoutPageTemplateEntry(
					_assetEntry.getGroupId(), _assetEntry.getClassNameId(),
					_assetEntry.getClassTypeId());

		if (defaultLayoutPageTemplateEntry != null) {
			return defaultLayoutPageTemplateEntry.
				getLayoutPageTemplateEntryId();
		}

		return 0;
	}

	private final AssetDisplayContributor _assetDisplayContributor;
	private final AssetEntry _assetEntry;
	private final HttpServletRequest _request;

}