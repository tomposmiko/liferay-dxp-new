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

package com.liferay.dynamic.data.mapping.form.field.type.internal.text;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldOptionsFactory;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.form.field.type.internal.util.DDMFormFieldTypeUtil;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true, property = "ddm.form.field.type.name=text",
	service = {
		DDMFormFieldTemplateContextContributor.class,
		TextDDMFormFieldTemplateContextContributor.class
	}
)
public class TextDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, Object> parameters = new HashMap<>();

		if (ddmFormFieldRenderingContext.isReturnFullContext()) {
			parameters.put(
				"autocompleteEnabled", isAutocompleteEnabled(ddmFormField));
			parameters.put("displayStyle", getDisplayStyle(ddmFormField));
			parameters.put(
				"placeholder",
				getPlaceholder(ddmFormField, ddmFormFieldRenderingContext));
			parameters.put(
				"tooltip",
				getTooltip(ddmFormField, ddmFormFieldRenderingContext));
		}

		String value = getValue(ddmFormFieldRenderingContext);

		if (Validator.isNotNull(value)) {
			parameters.put("value", value);
		}

		return HashMapBuilder.<String, Object>put(
			"options", getOptions(ddmFormField, ddmFormFieldRenderingContext)
		).put(
			"predefinedValue",
			DDMFormFieldTypeUtil.getPropertyValue(
				ddmFormField, ddmFormFieldRenderingContext.getLocale(),
				"predefinedValue")
		).putAll(
			parameters
		).build();
	}

	protected String getDisplayStyle(DDMFormField ddmFormField) {
		return GetterUtil.getString(
			ddmFormField.getProperty("displayStyle"), "singleline");
	}

	protected List<Object> getOptions(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		List<Object> options = new ArrayList<>();

		DDMFormFieldOptions ddmFormFieldOptions =
			ddmFormFieldOptionsFactory.create(
				ddmFormField, ddmFormFieldRenderingContext);

		for (String optionValue : ddmFormFieldOptions.getOptionsValues()) {
			options.add(
				HashMapBuilder.put(
					"label",
					() -> {
						LocalizedValue optionLabel =
							ddmFormFieldOptions.getOptionLabels(optionValue);

						return optionLabel.getString(
							ddmFormFieldRenderingContext.getLocale());
					}
				).put(
					"reference",
					ddmFormFieldOptions.getOptionReference(optionValue)
				).put(
					"value", optionValue
				).build());
		}

		return options;
	}

	protected String getPlaceholder(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		LocalizedValue placeholder = (LocalizedValue)ddmFormField.getProperty(
			"placeholder");

		return getValueString(
			placeholder, ddmFormFieldRenderingContext.getLocale(),
			ddmFormFieldRenderingContext);
	}

	protected String getTooltip(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		LocalizedValue tooltip = (LocalizedValue)ddmFormField.getProperty(
			"tooltip");

		return getValueString(
			tooltip, ddmFormFieldRenderingContext.getLocale(),
			ddmFormFieldRenderingContext);
	}

	protected String getValue(
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		String value = String.valueOf(
			ddmFormFieldRenderingContext.getProperty("value"));

		if (ddmFormFieldRenderingContext.isViewMode()) {
			value = HtmlUtil.extractText(value);
		}

		return value;
	}

	protected String getValueString(
		Value value, Locale locale,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		if (value == null) {
			return StringPool.BLANK;
		}

		return value.getString(locale);
	}

	protected boolean isAutocompleteEnabled(DDMFormField ddmFormField) {
		return GetterUtil.getBoolean(ddmFormField.getProperty("autocomplete"));
	}

	@Reference
	protected DDMFormFieldOptionsFactory ddmFormFieldOptionsFactory;

	@Reference
	protected JSONFactory jsonFactory;

}