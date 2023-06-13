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

package com.liferay.commerce.product.content.web.internal.info.item.provider;

import com.liferay.commerce.product.content.web.internal.info.CPAttachmentFileEntryInfoItemFields;
import com.liferay.commerce.product.model.CPAttachmentFileEntry;
import com.liferay.expando.info.item.provider.ExpandoInfoItemFieldSetProvider;
import com.liferay.info.field.InfoFieldSet;
import com.liferay.info.form.InfoForm;
import com.liferay.info.item.field.reader.InfoItemFieldReaderFieldSetProvider;
import com.liferay.info.item.provider.InfoItemFormProvider;
import com.liferay.info.localized.InfoLocalizedValue;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.template.info.item.provider.TemplateInfoItemFieldSetProvider;

import java.util.Locale;
import java.util.Set;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false, immediate = true,
	property = Constants.SERVICE_RANKING + ":Integer=10",
	service = InfoItemFormProvider.class
)
public class CPAttachmentFileEntryInfoItemFormProvider
	implements InfoItemFormProvider<CPAttachmentFileEntry> {

	@Override
	public InfoForm getInfoForm() {
		return _getInfoForm();
	}

	@Override
	public InfoForm getInfoForm(CPAttachmentFileEntry cpAttachmentFileEntry) {
		return _getInfoForm();
	}

	@Override
	public InfoForm getInfoForm(String formVariationKey, long groupId) {
		return _getInfoForm();
	}

	private InfoFieldSet _getBasicInformationInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.downloadURLInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.thumbnailURLInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.titleInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.URLInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				"com.liferay.commerce.lang", "basic-information")
		).name(
			"basic-information"
		).build();
	}

	private InfoFieldSet _getDetailedInformationInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.CDNEnabledInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.companyIdInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.cpAttachmentFileEntryIdInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.cpDefinitionIdInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.createDateInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.defaultLanguageIdInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.groupIdInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.lastPublishDateInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.mimeTypeInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.modifiedDateInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.optionsInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.priorityInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.sizeInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.stagedModelTypeInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.userIdInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.userNameInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.userUuidInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.uuidInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				"com.liferay.commerce.lang", "detailed-information")
		).name(
			"detailed-information"
		).build();
	}

	private InfoForm _getInfoForm() {
		Set<Locale> availableLocales = _language.getAvailableLocales();

		InfoLocalizedValue.Builder<String> infoLocalizedValueBuilder =
			InfoLocalizedValue.builder();

		for (Locale locale : availableLocales) {
			infoLocalizedValueBuilder.value(
				locale,
				ResourceActionsUtil.getModelResource(
					locale, CPAttachmentFileEntry.class.getName()));
		}

		return InfoForm.builder(
		).infoFieldSetEntry(
			_expandoInfoItemFieldSetProvider.getInfoFieldSet(
				CPAttachmentFileEntry.class.getName())
		).infoFieldSetEntry(
			_templateInfoItemFieldSetProvider.getInfoFieldSet(
				CPAttachmentFileEntry.class.getName())
		).infoFieldSetEntry(
			_infoItemFieldReaderFieldSetProvider.getInfoFieldSet(
				CPAttachmentFileEntry.class.getName())
		).infoFieldSetEntry(
			_getBasicInformationInfoFieldSet()
		).infoFieldSetEntry(
			_getDetailedInformationInfoFieldSet()
		).infoFieldSetEntry(
			_getScheduleInfoFieldSet()
		).infoFieldSetEntry(
			_getStatusInfoFieldSet()
		).labelInfoLocalizedValue(
			infoLocalizedValueBuilder.build()
		).name(
			CPAttachmentFileEntry.class.getName()
		).build();
	}

	private InfoFieldSet _getScheduleInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.displayDateInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.expirationDateInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(getClass(), "schedule")
		).name(
			"schedule"
		).build();
	}

	private InfoFieldSet _getStatusInfoFieldSet() {
		return InfoFieldSet.builder(
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.approvedInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.draftInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.expiredInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.inactiveInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.incompleteInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.scheduledInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.statusByUserIdInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.statusByUserNameInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.statusByUserUuidInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.statusDateInfoField
		).infoFieldSetEntry(
			CPAttachmentFileEntryInfoItemFields.statusInfoField
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(getClass(), "status")
		).name(
			"status"
		).build();
	}

	@Reference
	private ExpandoInfoItemFieldSetProvider _expandoInfoItemFieldSetProvider;

	@Reference
	private InfoItemFieldReaderFieldSetProvider
		_infoItemFieldReaderFieldSetProvider;

	@Reference
	private Language _language;

	@Reference
	private TemplateInfoItemFieldSetProvider _templateInfoItemFieldSetProvider;

}