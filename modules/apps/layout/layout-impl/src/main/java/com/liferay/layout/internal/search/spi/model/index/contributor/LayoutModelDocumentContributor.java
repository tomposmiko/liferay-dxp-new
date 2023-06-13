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

package com.liferay.layout.internal.search.spi.model.index.contributor;

import com.liferay.fragment.constants.FragmentEntryLinkConstants;
import com.liferay.layout.internal.search.util.LayoutPageTemplateStructureRenderUtil;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructure;
import com.liferay.layout.page.template.service.LayoutPageTemplateStructureLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.servlet.DynamicServletRequest;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import com.liferay.segments.model.SegmentsExperience;
import com.liferay.segments.service.SegmentsExperienceLocalService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Vagner B.C
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.liferay.portal.kernel.model.Layout",
	service = ModelDocumentContributor.class
)
public class LayoutModelDocumentContributor
	implements ModelDocumentContributor<Layout> {

	public static final String CLASS_NAME = Layout.class.getName();

	@Override
	public void contribute(Document document, Layout layout) {
		if (layout.isSystem()) {
			return;
		}

		document.addUID(CLASS_NAME, layout.getPlid());
		document.addText(
			Field.DEFAULT_LANGUAGE_ID, layout.getDefaultLanguageId());
		document.addLocalizedText(Field.NAME, layout.getNameMap());
		document.addNumberSortable("leftPlid", layout.getLeftPlid());
		document.addText(
			"privateLayout", String.valueOf(layout.isPrivateLayout()));
		document.addText(Field.TYPE, layout.getType());

		LayoutPageTemplateStructure layoutPageTemplateStructure =
			_layoutPageTemplateStructureLocalService.
				fetchLayoutPageTemplateStructure(
					layout.getGroupId(), _portal.getClassNameId(Layout.class),
					layout.getPlid());

		for (String languageId : layout.getAvailableLanguageIds()) {
			Locale locale = LocaleUtil.fromLanguageId(languageId);

			document.addText(
				Field.getLocalizedName(locale, Field.TITLE),
				layout.getName(locale));
		}

		if (layoutPageTemplateStructure == null) {
			return;
		}

		HttpServletRequest request = null;
		HttpServletResponse response = null;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		if (serviceContext != null) {
			request = DynamicServletRequest.addQueryString(
				serviceContext.getRequest(), "p_l_id=" + layout.getPlid(),
				false);
			response = serviceContext.getResponse();
		}

		long[] segmentsExperienceIds = _getSegmentsExperienceIds(
			layout.getGroupId(),
			_classNameLocalService.getClassNameId(Layout.class.getName()),
			layout.getPrimaryKey());

		Set<Locale> locales = LanguageUtil.getAvailableLocales(
			layout.getGroupId());

		for (Locale locale : locales) {
			try {
				if ((request == null) || (response == null)) {
					break;
				}

				String content =
					LayoutPageTemplateStructureRenderUtil.renderLayoutContent(
						request, response, layoutPageTemplateStructure,
						FragmentEntryLinkConstants.VIEW, new HashMap<>(),
						locale, segmentsExperienceIds);

				document.addText(
					Field.getLocalizedName(locale, Field.CONTENT), content);
			}
			catch (PortalException pe) {
				throw new SystemException(pe);
			}
		}
	}

	private long[] _getSegmentsExperienceIds(
		long groupId, long classNameId, long classPK) {

		try {
			SegmentsExperience defaultSegmentsExperience =
				_segmentsExperienceLocalService.getDefaultSegmentsExperience(
					groupId, classNameId, classPK);

			return new long[] {
				defaultSegmentsExperience.getSegmentsExperienceId()
			};
		}
		catch (PortalException pe) {
			throw new SystemException(pe);
		}
	}

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private LayoutPageTemplateStructureLocalService
		_layoutPageTemplateStructureLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private SegmentsExperienceLocalService _segmentsExperienceLocalService;

}