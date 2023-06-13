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

package com.liferay.commerce.shop.by.diagram.admin.web.internal.info.item.provider;

import com.liferay.commerce.shop.by.diagram.admin.web.internal.info.CSDiagramEntryInfoItemFields;
import com.liferay.commerce.shop.by.diagram.model.CSDiagramEntry;
import com.liferay.expando.info.item.provider.ExpandoInfoItemFieldSetProvider;
import com.liferay.info.field.InfoFieldValue;
import com.liferay.info.item.InfoItemFieldValues;
import com.liferay.info.item.InfoItemReference;
import com.liferay.info.item.field.reader.InfoItemFieldReaderFieldSetProvider;
import com.liferay.info.item.provider.InfoItemFieldValuesProvider;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.template.info.item.provider.TemplateInfoItemFieldSetProvider;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Mahmoud Azzam
 * @author Alessio Antonio Rendina
 */
@Component(service = InfoItemFieldValuesProvider.class)
public class CSDiagramEntryInfoItemFieldValuesProvider
	implements InfoItemFieldValuesProvider<CSDiagramEntry> {

	@Override
	public InfoItemFieldValues getInfoItemFieldValues(
		CSDiagramEntry csDiagramEntry) {

		return InfoItemFieldValues.builder(
		).infoFieldValues(
			_getCSDiagramEntryInfoFieldValues(csDiagramEntry)
		).infoFieldValues(
			_expandoInfoItemFieldSetProvider.getInfoFieldValues(
				CSDiagramEntry.class.getName(), csDiagramEntry)
		).infoFieldValues(
			_templateInfoItemFieldSetProvider.getInfoFieldValues(
				CSDiagramEntry.class.getName(), csDiagramEntry)
		).infoFieldValues(
			_infoItemFieldReaderFieldSetProvider.getInfoFieldValues(
				CSDiagramEntry.class.getName(), csDiagramEntry)
		).infoItemReference(
			new InfoItemReference(
				CSDiagramEntry.class.getName(), csDiagramEntry.getPrimaryKey())
		).build();
	}

	private List<InfoFieldValue<Object>> _getCSDiagramEntryInfoFieldValues(
		CSDiagramEntry csDiagramEntry) {

		return ListUtil.fromArray(
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.csDiagramEntryIdInfoField,
				csDiagramEntry.getCSDiagramEntryId()),
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.companyIdInfoField,
				csDiagramEntry.getCompanyId()),
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.userIdInfoField,
				csDiagramEntry.getUserId()),
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.userNameInfoField,
				csDiagramEntry.getUserName()),
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.createDateInfoField,
				csDiagramEntry.getCreateDate()),
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.modifiedDateInfoField,
				csDiagramEntry.getModifiedDate()),
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.cpDefinitionIdInfoField,
				csDiagramEntry.getCPDefinitionId()),
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.cpInstanceIdInfoField,
				csDiagramEntry.getCPInstanceId()),
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.cProductIdInfoField,
				csDiagramEntry.getCProductId()),
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.diagramInfoField,
				csDiagramEntry.isDiagram()),
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.quantityInfoField,
				csDiagramEntry.getQuantity()),
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.sequenceInfoField,
				csDiagramEntry.getSequence()),
			new InfoFieldValue<>(
				CSDiagramEntryInfoItemFields.skuInfoField,
				csDiagramEntry.getSku()));
	}

	@Reference
	private ExpandoInfoItemFieldSetProvider _expandoInfoItemFieldSetProvider;

	@Reference
	private InfoItemFieldReaderFieldSetProvider
		_infoItemFieldReaderFieldSetProvider;

	@Reference
	private TemplateInfoItemFieldSetProvider _templateInfoItemFieldSetProvider;

}