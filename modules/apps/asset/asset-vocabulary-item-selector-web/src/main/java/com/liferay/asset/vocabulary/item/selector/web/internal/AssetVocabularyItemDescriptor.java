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

package com.liferay.asset.vocabulary.item.selector.web.internal;

import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.item.selector.ItemSelectorViewDescriptor;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class AssetVocabularyItemDescriptor
	implements ItemSelectorViewDescriptor.ItemDescriptor {

	public AssetVocabularyItemDescriptor(
		AssetVocabulary assetVocabulary,
		HttpServletRequest httpServletRequest) {

		_assetVocabulary = assetVocabulary;
		_httpServletRequest = httpServletRequest;
	}

	@Override
	public String getIcon() {
		return "vocabulary";
	}

	@Override
	public String getImageURL() {
		return null;
	}

	@Override
	public Date getModifiedDate() {
		return _assetVocabulary.getModifiedDate();
	}

	@Override
	public String getPayload() {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return JSONUtil.put(
			"groupId", String.valueOf(_assetVocabulary.getGroupId())
		).put(
			"title", _assetVocabulary.getTitle(themeDisplay.getLocale())
		).put(
			"uuid", _assetVocabulary.getUuid()
		).put(
			"vocabularyId", String.valueOf(_assetVocabulary.getVocabularyId())
		).toString();
	}

	@Override
	public String getSubtitle(Locale locale) {
		return _assetVocabulary.getDescription(locale);
	}

	@Override
	public String getTitle(Locale locale) {
		return _assetVocabulary.getTitle(locale);
	}

	@Override
	public long getUserId() {
		return _assetVocabulary.getUserId();
	}

	@Override
	public String getUserName() {
		return _assetVocabulary.getUserName();
	}

	@Override
	public boolean isCompact() {
		return true;
	}

	private final AssetVocabulary _assetVocabulary;
	private final HttpServletRequest _httpServletRequest;

}