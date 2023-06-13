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

import com.liferay.headless.delivery.dto.v1_0.SEOSettings;
import com.liferay.headless.delivery.dto.v1_0.SiteMapSettings;
import com.liferay.layout.seo.model.LayoutSEOEntry;
import com.liferay.layout.seo.service.LayoutSEOEntryLocalService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.vulcan.dto.converter.DTOConverterContext;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;

/**
 * @author Jürgen Kappler
 * @author Javier de Arcos
 */
public class SEOSettingsUtil {

	public static SEOSettings getSeoSettings(
		DTOConverterContext dtoConverterContext,
		LayoutSEOEntryLocalService layoutSEOEntryLocalService, Layout layout) {

		LayoutSEOEntry layoutSEOEntry =
			layoutSEOEntryLocalService.fetchLayoutSEOEntry(
				layout.getGroupId(), layout.isPrivateLayout(),
				layout.getLayoutId());

		SEOSettings seoSettings = new SEOSettings() {
			{
				description = layout.getDescription(
					dtoConverterContext.getLocale());
				description_i18n = LocalizedMapUtil.getI18nMap(
					dtoConverterContext.isAcceptAllLanguages(),
					layout.getDescriptionMap());
				htmlTitle = layout.getTitle(dtoConverterContext.getLocale());
				htmlTitle_i18n = LocalizedMapUtil.getI18nMap(
					dtoConverterContext.isAcceptAllLanguages(),
					layout.getTitleMap());
				robots = layout.getRobots(dtoConverterContext.getLocale());
				robots_i18n = LocalizedMapUtil.getI18nMap(
					dtoConverterContext.isAcceptAllLanguages(),
					layout.getRobotsMap());
				seoKeywords = layout.getKeywords(
					dtoConverterContext.getLocale());
				seoKeywords_i18n = LocalizedMapUtil.getI18nMap(
					dtoConverterContext.isAcceptAllLanguages(),
					layout.getKeywordsMap());
				siteMapSettings = _toSiteMapSettings(
					layout.getTypeSettingsProperties());
			}
		};

		if ((layoutSEOEntry != null) &&
			layoutSEOEntry.isCanonicalURLEnabled()) {

			seoSettings.setCustomCanonicalURL(
				layoutSEOEntry.getCanonicalURL(
					dtoConverterContext.getLocale()));

			seoSettings.setCustomCanonicalURL_i18n(
				LocalizedMapUtil.getI18nMap(
					dtoConverterContext.isAcceptAllLanguages(),
					layoutSEOEntry.getCanonicalURLMap()));
		}

		return seoSettings;
	}

	private static SiteMapSettings _toSiteMapSettings(
		UnicodeProperties unicodeProperties) {

		String siteMapChangeFreq = unicodeProperties.getProperty(
			"sitemap-changefreq");
		String siteMapInclude = unicodeProperties.getProperty(
			"sitemap-include");
		String siteMapPriority = unicodeProperties.getProperty(
			"sitemap-priority");

		if ((siteMapChangeFreq == null) && (siteMapInclude == null) &&
			(siteMapPriority == null)) {

			return null;
		}

		return new SiteMapSettings() {
			{
				setChangeFrequency(
					() -> {
						if (siteMapChangeFreq == null) {
							return null;
						}

						return ChangeFrequency.create(
							StringUtil.upperCaseFirstLetter(siteMapChangeFreq));
					});

				setInclude(
					() -> {
						if (siteMapInclude == null) {
							return null;
						}

						if (siteMapInclude.equals("0")) {
							return false;
						}

						if (siteMapInclude.equals("1")) {
							return true;
						}

						return null;
					});

				setPagePriority(
					() -> {
						if (siteMapPriority == null) {
							return null;
						}

						try {
							return Double.parseDouble(siteMapPriority);
						}
						catch (NumberFormatException numberFormatException) {
							if (_log.isWarnEnabled()) {
								_log.warn(numberFormatException);
							}

							return null;
						}
					});
			}
		};
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SEOSettingsUtil.class);

}