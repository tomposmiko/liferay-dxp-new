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

package com.liferay.asset.categories.internal.search.spi.model.index.contributor;

import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.localization.SearchLocalizationHelper;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luan Maoski
 * @author Lucas Marques
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.liferay.asset.kernel.model.AssetVocabulary",
	service = ModelDocumentContributor.class
)
public class AssetVocabularyModelDocumentContributor
	implements ModelDocumentContributor<AssetVocabulary> {

	@Override
	public void contribute(Document document, AssetVocabulary assetVocabulary) {
		document.addKeyword(
			Field.ASSET_VOCABULARY_ID, assetVocabulary.getVocabularyId());

		Locale siteDefaultLocale = _getSiteDefaultLocale(
			assetVocabulary.getGroupId());

		_searchLocalizationHelper.addLocalizedField(
			document, Field.DESCRIPTION, siteDefaultLocale,
			assetVocabulary.getDescriptionMap());

		document.addText(Field.NAME, assetVocabulary.getName());
		document.addText(
			Field.TITLE,
			assetVocabulary.getTitle(assetVocabulary.getDefaultLanguageId()));
		document.addLocalizedKeyword(
			Field.TITLE,
			_populateMap(assetVocabulary, assetVocabulary.getTitleMap()), true,
			true);
	}

	private Locale _getSiteDefaultLocale(long groupId) {
		try {
			return _portal.getSiteDefaultLocale(groupId);
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

	private Map<Locale, String> _populateMap(
		AssetVocabulary assetVocabulary, Map<Locale, String> map) {

		String defaultValue = map.get(
			LocaleUtil.fromLanguageId(assetVocabulary.getDefaultLanguageId()));

		for (Locale availableLocale :
				LanguageUtil.getAvailableLocales(
					assetVocabulary.getGroupId())) {

			if (!map.containsKey(availableLocale) ||
				Validator.isNull(map.get(availableLocale))) {

				map.put(availableLocale, defaultValue);
			}
		}

		return map;
	}

	@Reference
	private Portal _portal;

	@Reference
	private SearchLocalizationHelper _searchLocalizationHelper;

}