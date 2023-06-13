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

import com.liferay.info.item.InfoItemClassDetails;
import com.liferay.info.item.InfoItemFormVariation;
import com.liferay.info.item.InfoItemServiceRegistry;
import com.liferay.info.item.provider.InfoItemFormVariationsProvider;
import com.liferay.info.localized.InfoLocalizedValue;
import com.liferay.info.permission.provider.InfoPermissionProvider;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Collection;
import java.util.Locale;

/**
 * @author Eudaldo Alonso
 */
public class MappingTypesUtil {

	public static JSONArray getMappingTypesJSONArray(
		InfoItemServiceRegistry infoItemServiceRegistry,
		String itemCapabilityKey, ThemeDisplay themeDisplay) {

		JSONArray mappingTypesJSONArray = JSONFactoryUtil.createJSONArray();

		if (!FeatureFlagManagerUtil.isEnabled("LPS-169923")) {
			for (InfoItemClassDetails infoItemClassDetails :
					infoItemServiceRegistry.getInfoItemClassDetails(
						themeDisplay.getScopeGroupId(), itemCapabilityKey,
						themeDisplay.getPermissionChecker())) {

				mappingTypesJSONArray.put(
					JSONUtil.put(
						"label",
						infoItemClassDetails.getLabel(themeDisplay.getLocale())
					).put(
						"subtypes",
						_getMappingFormVariationsJSONArray(
							infoItemClassDetails, infoItemServiceRegistry,
							themeDisplay.getScopeGroupId(),
							themeDisplay.getLocale())
					).put(
						"value",
						String.valueOf(
							PortalUtil.getClassNameId(
								infoItemClassDetails.getClassName()))
					));
			}

			return mappingTypesJSONArray;
		}

		for (InfoItemClassDetails infoItemClassDetails :
				infoItemServiceRegistry.getInfoItemClassDetails(
					itemCapabilityKey)) {

			mappingTypesJSONArray.put(
				JSONUtil.put(
					"isRestricted",
					() -> {
						InfoPermissionProvider infoPermissionProvider =
							infoItemServiceRegistry.getFirstInfoItemService(
								InfoPermissionProvider.class,
								infoItemClassDetails.getClassName());

						if ((infoPermissionProvider == null) ||
							infoPermissionProvider.hasViewPermission(
								themeDisplay.getPermissionChecker())) {

							return false;
						}

						return true;
					}
				).put(
					"label",
					infoItemClassDetails.getLabel(themeDisplay.getLocale())
				).put(
					"subtypes",
					_getMappingFormVariationsJSONArray(
						infoItemClassDetails, infoItemServiceRegistry,
						themeDisplay.getScopeGroupId(),
						themeDisplay.getLocale())
				).put(
					"value",
					String.valueOf(
						PortalUtil.getClassNameId(
							infoItemClassDetails.getClassName()))
				));
		}

		return mappingTypesJSONArray;
	}

	private static JSONArray _getMappingFormVariationsJSONArray(
		InfoItemClassDetails infoItemClassDetails,
		InfoItemServiceRegistry infoItemServiceRegistry, long groupId,
		Locale locale) {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		InfoItemFormVariationsProvider<?> infoItemFormVariationsProvider =
			infoItemServiceRegistry.getFirstInfoItemService(
				InfoItemFormVariationsProvider.class,
				infoItemClassDetails.getClassName());

		if (infoItemFormVariationsProvider == null) {
			return jsonArray;
		}

		Collection<InfoItemFormVariation> infoItemFormVariations =
			infoItemFormVariationsProvider.getInfoItemFormVariations(groupId);

		for (InfoItemFormVariation infoItemFormVariation :
				infoItemFormVariations) {

			jsonArray.put(
				JSONUtil.put(
					"label",
					() -> {
						InfoLocalizedValue<String> labelInfoLocalizedValue =
							infoItemFormVariation.getLabelInfoLocalizedValue();

						return labelInfoLocalizedValue.getValue(locale);
					}
				).put(
					"value", String.valueOf(infoItemFormVariation.getKey())
				));
		}

		return jsonArray;
	}

}