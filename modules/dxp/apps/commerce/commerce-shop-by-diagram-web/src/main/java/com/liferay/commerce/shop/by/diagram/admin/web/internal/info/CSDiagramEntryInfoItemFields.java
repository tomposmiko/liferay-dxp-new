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

package com.liferay.commerce.shop.by.diagram.admin.web.internal.info;

import com.liferay.commerce.shop.by.diagram.model.CSDiagramEntry;
import com.liferay.info.field.InfoField;
import com.liferay.info.field.type.BooleanInfoFieldType;
import com.liferay.info.field.type.DateInfoFieldType;
import com.liferay.info.field.type.NumberInfoFieldType;
import com.liferay.info.field.type.TextInfoFieldType;
import com.liferay.info.localized.InfoLocalizedValue;

/**
 * @author Mahmoud Azzam
 * @author Alessio Antonio Rendina
 */
public class CSDiagramEntryInfoItemFields {

	public static final InfoField<NumberInfoFieldType> companyIdInfoField =
		CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
			NumberInfoFieldType.INSTANCE
		).name(
			"companyId"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				CSDiagramEntryInfoItemFields.class, "company-id")
		).build();
	public static final InfoField<NumberInfoFieldType> cpDefinitionIdInfoField =
		CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
			NumberInfoFieldType.INSTANCE
		).name(
			"cpDefinitionId"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				CSDiagramEntryInfoItemFields.class, "cpDefinitionId")
		).build();
	public static final InfoField<NumberInfoFieldType> cpInstanceIdInfoField =
		CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
			NumberInfoFieldType.INSTANCE
		).name(
			"cpInstanceId"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				CSDiagramEntryInfoItemFields.class, "cpInstanceId")
		).build();
	public static final InfoField<NumberInfoFieldType> cProductIdInfoField =
		CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
			NumberInfoFieldType.INSTANCE
		).name(
			"cProductId"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				CSDiagramEntryInfoItemFields.class, "cProductId")
		).build();
	public static final InfoField<DateInfoFieldType> createDateInfoField =
		CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
			DateInfoFieldType.INSTANCE
		).name(
			"createDate"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				CSDiagramEntryInfoItemFields.class, "create-date")
		).build();
	public static final InfoField<NumberInfoFieldType>
		csDiagramEntryIdInfoField =
			CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
				NumberInfoFieldType.INSTANCE
			).name(
				"csDiagramEntryId"
			).labelInfoLocalizedValue(
				InfoLocalizedValue.localize(
					CSDiagramEntryInfoItemFields.class, "csDiagramEntryId")
			).build();
	public static final InfoField<BooleanInfoFieldType> diagramInfoField =
		CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
			BooleanInfoFieldType.INSTANCE
		).name(
			"diagram"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				CSDiagramEntryInfoItemFields.class, "diagram")
		).build();
	public static final InfoField<DateInfoFieldType> modifiedDateInfoField =
		CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
			DateInfoFieldType.INSTANCE
		).name(
			"modifiedDate"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				CSDiagramEntryInfoItemFields.class, "modified-date")
		).build();
	public static final InfoField<NumberInfoFieldType> quantityInfoField =
		CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
			NumberInfoFieldType.INSTANCE
		).name(
			"quantity"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				CSDiagramEntryInfoItemFields.class, "quantity")
		).build();
	public static final InfoField<TextInfoFieldType> sequenceInfoField =
		CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
			TextInfoFieldType.INSTANCE
		).name(
			"sequence"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				CSDiagramEntryInfoItemFields.class, "sequence")
		).build();
	public static final InfoField<TextInfoFieldType> skuInfoField =
		CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
			TextInfoFieldType.INSTANCE
		).name(
			"sku"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				CSDiagramEntryInfoItemFields.class, "sku")
		).build();
	public static final InfoField<NumberInfoFieldType> userIdInfoField =
		CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
			NumberInfoFieldType.INSTANCE
		).name(
			"userId"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				CSDiagramEntryInfoItemFields.class, "user-id")
		).build();
	public static final InfoField<TextInfoFieldType> userNameInfoField =
		CSDiagramEntryInfoItemFields.BuilderHolder._builder.infoFieldType(
			TextInfoFieldType.INSTANCE
		).name(
			"userName"
		).labelInfoLocalizedValue(
			InfoLocalizedValue.localize(
				CSDiagramEntryInfoItemFields.class, "user-name")
		).build();

	private static class BuilderHolder {

		private static final InfoField.NamespacedBuilder _builder =
			InfoField.builder(CSDiagramEntry.class.getSimpleName());

	}

}