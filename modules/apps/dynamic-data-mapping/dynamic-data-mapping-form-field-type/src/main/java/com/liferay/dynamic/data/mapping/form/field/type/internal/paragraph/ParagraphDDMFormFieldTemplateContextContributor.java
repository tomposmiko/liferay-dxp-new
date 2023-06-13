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

package com.liferay.dynamic.data.mapping.form.field.type.internal.paragraph;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.template.soy.data.SoyDataFactory;
import com.liferay.portal.template.soy.util.SoyRawData;

import java.util.Collections;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true, property = "ddm.form.field.type.name=paragraph",
	service = {
		DDMFormFieldTemplateContextContributor.class,
		ParagraphDDMFormFieldTemplateContextContributor.class
	}
)
public class ParagraphDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		SoyRawData soyRawData = _soyDataFactory.createSoyRawData(
			getText(ddmFormField, ddmFormFieldRenderingContext));

		return Collections.singletonMap("text", soyRawData.getValue());
	}

	protected String getText(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		LocalizedValue text = (LocalizedValue)ddmFormField.getProperty("text");

		if (text == null) {
			return StringPool.BLANK;
		}

		return text.getString(ddmFormFieldRenderingContext.getLocale());
	}

	@Reference
	private SoyDataFactory _soyDataFactory;

}