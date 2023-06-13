/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.internal.search.spi.model.index.contributor;

import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import com.liferay.search.experiences.model.SXPElement;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	enabled = false,
	property = "indexer.class.name=com.liferay.search.experiences.model.SXPElement",
	service = ModelDocumentContributor.class
)
public class SXPElementModelDocumentContributor
	implements ModelDocumentContributor<SXPElement> {

	@Override
	public void contribute(Document document, SXPElement sxpElement) {
		document.addDate(Field.MODIFIED_DATE, sxpElement.getModifiedDate());
		document.addKeyword(Field.HIDDEN, sxpElement.isHidden());
		document.addKeyword(Field.TYPE, sxpElement.getType());
		document.addKeyword("readOnly", sxpElement.isReadOnly());

		for (Locale locale :
				_language.getCompanyAvailableLocales(
					sxpElement.getCompanyId())) {

			String languageId = LocaleUtil.toLanguageId(locale);

			document.addKeyword(
				Field.getSortableFieldName(
					_localization.getLocalizedName(
						Field.DESCRIPTION, languageId)),
				sxpElement.getDescription(locale), true);
			document.addKeyword(
				Field.getSortableFieldName(
					_localization.getLocalizedName(Field.TITLE, languageId)),
				sxpElement.getTitle(locale), true);
			document.addText(
				_localization.getLocalizedName(Field.DESCRIPTION, languageId),
				sxpElement.getDescription(locale));
			document.addText(
				_localization.getLocalizedName(Field.TITLE, languageId),
				sxpElement.getTitle(locale));
		}
	}

	@Reference
	private Language _language;

	@Reference
	private Localization _localization;

}