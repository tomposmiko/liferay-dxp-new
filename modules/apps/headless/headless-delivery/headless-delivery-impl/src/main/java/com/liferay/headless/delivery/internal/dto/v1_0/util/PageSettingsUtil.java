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

package com.liferay.headless.delivery.internal.dto.v1_0.util;

import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.util.DLURLHelper;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.DDMStorageEngineManager;
import com.liferay.headless.delivery.dto.v1_0.CustomMetaTag;
import com.liferay.headless.delivery.dto.v1_0.PageSettings;
import com.liferay.headless.delivery.dto.v1_0.SitePageNavigationMenuSettings;
import com.liferay.layout.admin.kernel.model.LayoutTypePortletConstants;
import com.liferay.layout.seo.model.LayoutSEOEntry;
import com.liferay.layout.seo.service.LayoutSEOEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * @author Jürgen Kappler
 * @author Javier de Arcos
 */
public class PageSettingsUtil {

	public static PageSettings getPageSettings(
		DDMStorageEngineManager ddmStorageEngineManager,
		DLAppService dlAppService, DLURLHelper dlURLHelper,
		DTOConverterContext dtoConverterContext,
		LayoutSEOEntryLocalService layoutSEOEntryLocalService, Layout layout) {

		return new PageSettings() {
			{
				hiddenFromNavigation = layout.isHidden();
				openGraphSettings = OpenGraphSettingsUtil.getOpenGraphSettings(
					dlAppService, dlURLHelper, dtoConverterContext,
					layoutSEOEntryLocalService, layout);
				seoSettings = SEOSettingsUtil.getSeoSettings(
					dtoConverterContext, layoutSEOEntryLocalService, layout);
				sitePageNavigationMenuSettings =
					_toSitePageNavigationMenuSettings(
						layout.getTypeSettingsProperties());

				setCustomMetaTags(
					() -> _getCustomMetaTags(
						dtoConverterContext, layout, layoutSEOEntryLocalService,
						dtoConverterContext.getLocale(),
						ddmStorageEngineManager));
			}
		};
	}

	private static CustomMetaTag[] _getCustomMetaTags(
			DTOConverterContext dtoConverterContext, Layout layout,
			LayoutSEOEntryLocalService layoutSEOEntryLocalService,
			Locale locale, DDMStorageEngineManager ddmStorageEngineManager)
		throws PortalException {

		LayoutSEOEntry layoutSEOEntry =
			layoutSEOEntryLocalService.fetchLayoutSEOEntry(
				layout.getGroupId(), layout.isPrivateLayout(),
				layout.getLayoutId());

		if ((layoutSEOEntry == null) ||
			(layoutSEOEntry.getDDMStorageId() == 0)) {

			return null;
		}

		List<CustomMetaTag> customMetaTags = new ArrayList<>();

		DDMFormValues ddmFormValues = ddmStorageEngineManager.getDDMFormValues(
			layoutSEOEntry.getDDMStorageId());

		List<DDMFormFieldValue> ddmFormFieldValues =
			ddmFormValues.getDDMFormFieldValues();

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			Value value = ddmFormFieldValue.getValue();

			String valueString = value.getString(LocaleUtil.ROOT);

			List<DDMFormFieldValue> nestedDDMFormFieldValues =
				ddmFormFieldValue.getNestedDDMFormFieldValues();

			DDMFormFieldValue nestedDDMFormFieldValue =
				nestedDDMFormFieldValues.get(0);

			value = nestedDDMFormFieldValue.getValue();

			String nestedValueString = value.getString(locale);

			Map<Locale, String> nestedValuesLocaleMap = value.getValues();

			customMetaTags.add(
				new CustomMetaTag() {
					{
						key = valueString;
						value = nestedValueString;
						value_i18n = LocalizedMapUtil.getI18nMap(
							dtoConverterContext.isAcceptAllLanguages(),
							nestedValuesLocaleMap);
					}
				});
		}

		return customMetaTags.toArray(new CustomMetaTag[0]);
	}

	private static SitePageNavigationMenuSettings
		_toSitePageNavigationMenuSettings(UnicodeProperties unicodeProperties) {

		String queryStringPropertyValue = unicodeProperties.getProperty(
			LayoutTypePortletConstants.QUERY_STRING);
		String targetPropertyValue = unicodeProperties.getProperty(
			LayoutTypePortletConstants.TARGET);
		String targetTypePropertyValue = unicodeProperties.getProperty(
			"targetType");

		if ((queryStringPropertyValue == null) &&
			(targetPropertyValue == null) &&
			(targetTypePropertyValue == null)) {

			return null;
		}

		return new SitePageNavigationMenuSettings() {
			{
				queryString = queryStringPropertyValue;
				target = targetPropertyValue;

				setTargetType(
					() -> {
						if (targetTypePropertyValue == null) {
							return null;
						}

						if (Objects.equals(
								targetTypePropertyValue, "useNewTab")) {

							return TargetType.NEW_TAB;
						}

						return TargetType.SPECIFIC_FRAME;
					});
			}
		};
	}

}