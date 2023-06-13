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

package com.liferay.dynamic.data.mapping.web.internal.info.field.converter;

import com.liferay.dynamic.data.mapping.info.field.converter.DDMFormFieldInfoFieldConverter;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.info.field.InfoField;
import com.liferay.info.field.type.BooleanInfoFieldType;
import com.liferay.info.field.type.DateInfoFieldType;
import com.liferay.info.field.type.GridInfoFieldType;
import com.liferay.info.field.type.ImageInfoFieldType;
import com.liferay.info.field.type.InfoFieldType;
import com.liferay.info.field.type.NumberInfoFieldType;
import com.liferay.info.field.type.RadioInfoFieldType;
import com.liferay.info.field.type.SelectInfoFieldType;
import com.liferay.info.field.type.TextInfoFieldType;
import com.liferay.info.localized.InfoLocalizedValue;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Objects;

import org.osgi.service.component.annotations.Component;

/**
 * @author Alejandro Tardín
 */
@Component(service = DDMFormFieldInfoFieldConverter.class)
public class DDMFormFieldInfoFieldConverterImpl
	implements DDMFormFieldInfoFieldConverter {

	@Override
	public InfoField convert(DDMFormField ddmFormField) {
		LocalizedValue label = ddmFormField.getLabel();

		return _addAttributes(
			ddmFormField,
			InfoField.builder(
			).infoFieldType(
				_getInfoFieldType(ddmFormField)
			).namespace(
				DDMStructure.class.getSimpleName()
			).name(
				ddmFormField.getName()
			)
		).labelInfoLocalizedValue(
			InfoLocalizedValue.<String>builder(
			).values(
				label.getValues()
			).defaultLocale(
				label.getDefaultLocale()
			).build()
		).localizable(
			ddmFormField.isLocalizable()
		).build();
	}

	private InfoField.FinalStep _addAttributes(
		DDMFormField ddmFormField, InfoField.FinalStep finalStep) {

		if (Objects.equals(
				ddmFormField.getType(), DDMFormFieldType.CHECKBOX_MULTIPLE)) {

			finalStep.attribute(SelectInfoFieldType.MULTIPLE, true);
		}

		if (Objects.equals(ddmFormField.getType(), DDMFormFieldType.DECIMAL)) {
			finalStep.attribute(NumberInfoFieldType.DECIMAL, true);
		}

		if (Objects.equals(ddmFormField.getType(), DDMFormFieldType.SELECT) &&
			GetterUtil.getBoolean(ddmFormField.getProperty("multiple"))) {

			finalStep.attribute(SelectInfoFieldType.MULTIPLE, true);
		}

		if (Objects.equals(ddmFormField.getType(), DDMFormFieldType.TEXT) &&
			Objects.equals(
				ddmFormField.getProperty("displayStyle"), "multiline")) {

			finalStep.attribute(TextInfoFieldType.MULTILINE, true);
		}

		if (Objects.equals(
				ddmFormField.getType(), DDMFormFieldType.TEXT_AREA)) {

			finalStep.attribute(TextInfoFieldType.MULTILINE, true);
		}

		if (Objects.equals(
				ddmFormField.getType(), DDMFormFieldType.TEXT_HTML)) {

			finalStep.attribute(TextInfoFieldType.HTML, true);
			finalStep.attribute(TextInfoFieldType.MULTILINE, true);
		}

		if (Objects.equals(ddmFormField.getType(), "rich_text")) {
			finalStep.attribute(TextInfoFieldType.HTML, true);
			finalStep.attribute(TextInfoFieldType.MULTILINE, true);
		}

		return finalStep;
	}

	private InfoFieldType _getInfoFieldType(DDMFormField ddmFormField) {
		String ddmFormFieldType = ddmFormField.getType();

		if (Objects.equals(ddmFormFieldType, DDMFormFieldType.CHECKBOX)) {
			return BooleanInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(
					ddmFormFieldType, DDMFormFieldType.CHECKBOX_MULTIPLE) ||
				 Objects.equals(ddmFormFieldType, DDMFormFieldType.SELECT)) {

			return SelectInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(ddmFormFieldType, DDMFormFieldType.DATE) ||
				 Objects.equals(ddmFormFieldType, "date")) {

			return DateInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(ddmFormFieldType, "ddm-image") ||
				 Objects.equals(ddmFormFieldType, "image")) {

			return ImageInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(ddmFormFieldType, DDMFormFieldType.DECIMAL) ||
				 Objects.equals(ddmFormFieldType, DDMFormFieldType.INTEGER) ||
				 Objects.equals(ddmFormFieldType, DDMFormFieldType.NUMERIC) ||
				 Objects.equals(ddmFormFieldType, DDMFormFieldType.NUMBER)) {

			return NumberInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(ddmFormFieldType, DDMFormFieldType.GRID)) {
			return GridInfoFieldType.INSTANCE;
		}
		else if (Objects.equals(ddmFormFieldType, DDMFormFieldType.RADIO)) {
			return RadioInfoFieldType.INSTANCE;
		}

		return TextInfoFieldType.INSTANCE;
	}

}