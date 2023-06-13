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
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.headless.delivery.dto.v1_0.ContentField;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.BadRequestException;

/**
 * @author Víctor Galán
 */
public class DDMFormValuesUtil {

	public static DDMFormValues toDDMFormValues(
		ContentField[] contentFields, DDMForm ddmForm,
		DLAppService dlAppService, long groupId,
		JournalArticleService journalArticleService,
		LayoutLocalService layoutLocalService, Locale locale,
		List<DDMFormField> rootDDMFormFields) {

		Map<String, List<ContentField>> contentFieldMap = Optional.ofNullable(
			contentFields
		).map(
			fields -> _toContentFieldsMap(Stream.of(fields))
		).orElse(
			new HashMap<>()
		);

		return new DDMFormValues(ddmForm) {
			{
				setAvailableLocales(ddmForm.getAvailableLocales());
				setDDMFormFieldValues(
					_flattenDDMFormFieldValues(
						rootDDMFormFields,
						ddmFormField -> _toDDMFormFieldValues(
							contentFieldMap.get(ddmFormField.getName()),
							ddmFormField, dlAppService, groupId,
							journalArticleService, layoutLocalService,
							locale)));
				setDefaultLocale(ddmForm.getDefaultLocale());
			}
		};
	}

	private static List<DDMFormFieldValue> _flattenDDMFormFieldValues(
		List<DDMFormField> ddmFormFields,
		UnsafeFunction<DDMFormField, List<DDMFormFieldValue>, Exception>
			unsafeFunction) {

		if (ListUtil.isEmpty(ddmFormFields)) {
			return Collections.emptyList();
		}

		Stream<DDMFormField> stream = ddmFormFields.stream();

		return stream.map(
			ddmFormField -> {
				try {
					return unsafeFunction.apply(ddmFormField);
				}
				catch (RuntimeException re) {
					throw re;
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		).flatMap(
			List::stream
		).collect(
			Collectors.toList()
		);
	}

	private static Map<String, List<ContentField>> _toContentFieldsMap(
		Stream<ContentField> stream) {

		return stream.collect(Collectors.groupingBy(ContentField::getName));
	}

	private static DDMFormFieldValue _toDDMFormFieldValue(
		List<ContentField> contentFields, DDMFormField ddmFormField,
		DLAppService dlAppService, long groupId,
		JournalArticleService journalArticleService,
		LayoutLocalService layoutLocalService, Locale locale, Value value) {

		Map<String, List<ContentField>> contentFieldMap = _toContentFieldsMap(
			contentFields.stream());

		return new DDMFormFieldValue() {
			{
				setName(ddmFormField.getName());
				setNestedDDMFormFields(
					_flattenDDMFormFieldValues(
						ddmFormField.getNestedDDMFormFields(),
						field -> _toDDMFormFieldValues(
							contentFieldMap.get(field.getName()), field,
							dlAppService, groupId, journalArticleService,
							layoutLocalService, locale)));
				setValue(value);
			}
		};
	}

	private static List<DDMFormFieldValue> _toDDMFormFieldValues(
		List<ContentField> contentFields, DDMFormField ddmFormField,
		DLAppService dlAppService, long groupId,
		JournalArticleService journalArticleService,
		LayoutLocalService layoutLocalService, Locale locale) {

		if (ListUtil.isEmpty(contentFields)) {
			if (ddmFormField.isRequired()) {
				throw new BadRequestException(
					"No value is specified for field " +
						ddmFormField.getName());
			}

			return Collections.singletonList(
				_toDDMFormFieldValue(
					Collections.emptyList(), ddmFormField, dlAppService,
					groupId, journalArticleService, layoutLocalService, locale,
					_toValue(ddmFormField, locale)));
		}

		return TransformUtil.transform(
			contentFields,
			contentField -> _toDDMFormFieldValue(
				ListUtil.fromArray(contentField.getNestedContentFields()),
				ddmFormField, dlAppService, groupId, journalArticleService,
				layoutLocalService, locale,
				DDMValueUtil.toDDMValue(
					contentField, ddmFormField, dlAppService, groupId,
					journalArticleService, layoutLocalService, locale)));
	}

	private static Value _toValue(DDMFormField ddmFormField, Locale locale) {
		if (Objects.equals(
				DDMFormFieldType.SEPARATOR, ddmFormField.getType())) {

			return null;
		}

		LocalizedValue localizedValue = ddmFormField.getPredefinedValue();

		String valueString = localizedValue.getString(
			localizedValue.getDefaultLocale());

		if (ddmFormField.isLocalizable()) {
			return new LocalizedValue(locale) {
				{
					addString(locale, valueString);
				}
			};
		}

		return new UnlocalizedValue(valueString);
	}

}