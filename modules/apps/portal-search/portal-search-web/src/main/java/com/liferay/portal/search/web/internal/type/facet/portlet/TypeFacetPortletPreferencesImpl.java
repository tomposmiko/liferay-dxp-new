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

package com.liferay.portal.search.web.internal.type.facet.portlet;

import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.asset.SearchableAssetClassNamesProvider;
import com.liferay.portal.search.web.internal.portlet.preferences.BasePortletPreferences;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.portlet.PortletPreferences;

/**
 * @author Lino Alves
 */
public class TypeFacetPortletPreferencesImpl
	extends BasePortletPreferences implements TypeFacetPortletPreferences {

	public TypeFacetPortletPreferencesImpl(
		ObjectDefinitionLocalService objectDefinitionLocalService,
		Optional<PortletPreferences> portletPreferencesOptional,
		SearchableAssetClassNamesProvider searchableAssetClassNamesProvider) {

		super(portletPreferencesOptional.orElse(null));

		_objectDefinitionLocalService = objectDefinitionLocalService;
		_searchableAssetClassNamesProvider = searchableAssetClassNamesProvider;
	}

	@Override
	public String getAssetTypes() {
		return getString(
			TypeFacetPortletPreferences.PREFERENCE_KEY_ASSET_TYPES,
			StringPool.BLANK);
	}

	@Override
	public List<KeyValuePair> getAvailableAssetTypes(
		long companyId, Locale locale) {

		String[] assetTypes = getCurrentAssetTypesArray(companyId);

		return TransformUtil.transformToList(
			getAllAssetTypes(companyId),
			assetType -> {
				if (ArrayUtil.contains(assetTypes, assetType)) {
					return null;
				}

				return _getKeyValuePair(locale, assetType);
			});
	}

	@Override
	public List<KeyValuePair> getCurrentAssetTypes(
		long companyId, Locale locale) {

		return TransformUtil.transformToList(
			getCurrentAssetTypesArray(companyId),
			assetType -> _getKeyValuePair(locale, assetType));
	}

	@Override
	public String[] getCurrentAssetTypesArray(long companyId) {
		String assetTypes = getString(
			TypeFacetPortletPreferences.PREFERENCE_KEY_ASSET_TYPES, null);

		if (assetTypes != null) {
			return StringUtil.split(assetTypes);
		}

		return getAllAssetTypes(companyId);
	}

	@Override
	public int getFrequencyThreshold() {
		return getInteger(
			TypeFacetPortletPreferences.PREFERENCE_KEY_FREQUENCY_THRESHOLD, 1);
	}

	@Override
	public String getOrder() {
		return getString(
			TypeFacetPortletPreferences.PREFERENCE_KEY_ORDER, "count:desc");
	}

	@Override
	public String getParameterName() {
		return getString(
			TypeFacetPortletPreferences.PREFERENCE_KEY_PARAMETER_NAME, "type");
	}

	@Override
	public boolean isFrequenciesVisible() {
		return getBoolean(
			TypeFacetPortletPreferences.PREFERENCE_KEY_FREQUENCIES_VISIBLE,
			true);
	}

	protected String[] getAllAssetTypes(long companyId) {
		return _searchableAssetClassNamesProvider.getClassNames(companyId);
	}

	private KeyValuePair _getKeyValuePair(Locale locale, String className) {
		String modelResource = ResourceActionsUtil.getModelResource(
			locale, className);

		if (className.startsWith(ObjectDefinition.class.getName() + "#")) {
			String[] parts = StringUtil.split(className, "#");

			ObjectDefinition objectDefinition =
				_objectDefinitionLocalService.fetchObjectDefinition(
					Long.valueOf(parts[1]));

			modelResource = objectDefinition.getLabel(locale);
		}

		return new KeyValuePair(className, modelResource);
	}

	private final ObjectDefinitionLocalService _objectDefinitionLocalService;
	private final SearchableAssetClassNamesProvider
		_searchableAssetClassNamesProvider;

}