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

package com.liferay.data.engine.rest.internal.dto.v2_0.util;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.SetUtil;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import org.powermock.api.mockito.PowerMockito;

/**
 * @author Leonardo Barros
 */
@RunWith(MockitoJUnitRunner.class)
public class DataRecordValuesUtilTest extends PowerMockito {

	@Before
	public void setUp() {
		_setUpJSONFactoryUtil();
		_setUpLanguageUtil();
	}

	@Test
	public void testCreateDDMFormFieldValueInvalidName() {
		DDMFormField ddmFormField = _createDDMFormField("field1", "text", true);

		DDMFormFieldValue ddmFormFieldValue =
			DataRecordValuesUtil.createDDMFormFieldValue(
				HashMapBuilder.<String, Object>put(
					"field2",
					HashMapBuilder.put(
						"en_US", "Value 2"
					).put(
						"pt_BR", "Valor 2"
					).build()
				).build(),
				ddmFormField, null);

		Assert.assertEquals("field1", ddmFormFieldValue.getName());

		Value value = ddmFormFieldValue.getValue();

		Assert.assertNull(value);
	}

	@Test
	public void testCreateDDMFormFieldValueNestedField() {
		DDMFormField ddmFormField = _createDDMFormField("parent", "text", true);

		ddmFormField.addNestedDDMFormField(
			_createDDMFormField("child", "text", true));

		DDMFormFieldValue ddmFormFieldValue =
			DataRecordValuesUtil.createDDMFormFieldValue(
				HashMapBuilder.<String, Object>put(
					"child",
					HashMapBuilder.put(
						"en_US", "Child Value 1"
					).put(
						"pt_BR", "Filho Valor 1"
					).build()
				).put(
					"parent",
					HashMapBuilder.put(
						"en_US", "Parent Value 1"
					).put(
						"pt_BR", "Pai Valor 1"
					).build()
				).build(),
				ddmFormField, null);

		Assert.assertEquals("parent", ddmFormFieldValue.getName());

		Value value = ddmFormFieldValue.getValue();

		Assert.assertTrue(value instanceof LocalizedValue);

		LocalizedValue localizedValue = (LocalizedValue)value;

		Assert.assertEquals(
			"Parent Value 1", localizedValue.getString(LocaleUtil.ENGLISH));
		Assert.assertEquals(
			"Pai Valor 1", localizedValue.getString(LocaleUtil.BRAZIL));

		Map<String, List<DDMFormFieldValue>> nestedDDMFormFieldValuesMap =
			ddmFormFieldValue.getNestedDDMFormFieldValuesMap();

		Assert.assertTrue(nestedDDMFormFieldValuesMap.containsKey("child"));

		List<DDMFormFieldValue> nestedDDMFormFieldValues =
			nestedDDMFormFieldValuesMap.get("child");

		DDMFormFieldValue nestedDDMFormFieldValue =
			nestedDDMFormFieldValues.get(0);

		value = nestedDDMFormFieldValue.getValue();

		Assert.assertTrue(value instanceof LocalizedValue);

		localizedValue = (LocalizedValue)value;

		Assert.assertEquals(
			"Child Value 1", localizedValue.getString(LocaleUtil.ENGLISH));
		Assert.assertEquals(
			"Filho Valor 1", localizedValue.getString(LocaleUtil.BRAZIL));
	}

	@Test
	public void testCreateDDMFormFieldValueNoLocale() {
		DDMFormField ddmFormField = _createDDMFormField("field1", "text", true);

		DDMFormFieldValue ddmFormFieldValue =
			DataRecordValuesUtil.createDDMFormFieldValue(
				HashMapBuilder.<String, Object>put(
					"field1",
					HashMapBuilder.put(
						"en_US", "Value 1"
					).put(
						"pt_BR", "Valor 1"
					).build()
				).build(),
				ddmFormField, null);

		Assert.assertEquals("field1", ddmFormFieldValue.getName());

		Value value = ddmFormFieldValue.getValue();

		Assert.assertTrue(value instanceof LocalizedValue);

		LocalizedValue localizedValue = (LocalizedValue)value;

		Assert.assertEquals(
			"Value 1", localizedValue.getString(LocaleUtil.ENGLISH));
		Assert.assertEquals(
			"Valor 1", localizedValue.getString(LocaleUtil.BRAZIL));
	}

	@Test
	public void testCreateValueWithArray1() {
		DDMFormField ddmFormField = _createDDMFormField("field1", "text", true);

		Value value = DataRecordValuesUtil.createValue(
			ddmFormField, null,
			HashMapBuilder.put(
				"en_US", new Object[] {1, 2}
			).put(
				"pt_BR", new Object[] {3, 4}
			).build());

		Assert.assertTrue(value instanceof LocalizedValue);

		LocalizedValue localizedValue = (LocalizedValue)value;

		Assert.assertEquals(
			"[1,2]", localizedValue.getString(LocaleUtil.ENGLISH));
		Assert.assertEquals(
			"[3,4]", localizedValue.getString(LocaleUtil.BRAZIL));
	}

	@Test
	public void testCreateValueWithArray2() {
		DDMFormField ddmFormField = _createDDMFormField("field1", "text", true);

		Value value = DataRecordValuesUtil.createValue(
			ddmFormField, LocaleUtil.BRAZIL,
			HashMapBuilder.put(
				"pt_BR", new Object[] {3, 4}
			).build());

		Assert.assertTrue(value instanceof LocalizedValue);

		LocalizedValue localizedValue = (LocalizedValue)value;

		Assert.assertEquals(
			"[3,4]", localizedValue.getString(LocaleUtil.BRAZIL));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateValueWithIllegalArgument() throws Exception {
		DDMFormField ddmFormField = _createDDMFormField(
			"field1", "text", false);

		DataRecordValuesUtil.createValue(
			ddmFormField, LocaleUtil.BRAZIL,
			HashMapBuilder.put(
				"en_US", "Value 1"
			).build());
	}

	@Test
	public void testCreateValueWithLocalizableField1() {
		DDMFormField ddmFormField = _createDDMFormField("field1", "text", true);

		Value value = DataRecordValuesUtil.createValue(
			ddmFormField, null,
			HashMapBuilder.put(
				"en_US", "Value 1"
			).put(
				"pt_BR", "Valor 1"
			).build());

		Assert.assertTrue(value instanceof LocalizedValue);

		LocalizedValue localizedValue = (LocalizedValue)value;

		Assert.assertEquals(
			"Value 1", localizedValue.getString(LocaleUtil.ENGLISH));
		Assert.assertEquals(
			"Valor 1", localizedValue.getString(LocaleUtil.BRAZIL));
	}

	@Test
	public void testCreateValueWithLocalizableField2() {
		DDMFormField ddmFormField = _createDDMFormField("field1", "text", true);

		Value value = DataRecordValuesUtil.createValue(
			ddmFormField, LocaleUtil.BRAZIL,
			HashMapBuilder.put(
				"en_US", "Value 1"
			).put(
				"pt_BR", "Valor 1"
			).build());

		Assert.assertTrue(value instanceof LocalizedValue);

		LocalizedValue localizedValue = (LocalizedValue)value;

		Assert.assertEquals(
			"Valor 1", localizedValue.getString(LocaleUtil.BRAZIL));

		Map<Locale, String> values = localizedValue.getValues();

		Assert.assertEquals(values.toString(), 1, values.size());

		Assert.assertTrue(values.containsKey(LocaleUtil.BRAZIL));
	}

	@Test
	public void testCreateValueWithUnlocalizableField() {
		DDMFormField ddmFormField = _createDDMFormField(
			"field1", "text", false);

		Value value = DataRecordValuesUtil.createValue(
			ddmFormField, LocaleUtil.BRAZIL, "Valor");

		Assert.assertTrue(value instanceof UnlocalizedValue);

		UnlocalizedValue unlocalizedValue = (UnlocalizedValue)value;

		Assert.assertEquals(
			"Valor",
			unlocalizedValue.getString(unlocalizedValue.getDefaultLocale()));
	}

	@Test
	public void testToDDMFormValuesNoLocale() {
		DDMForm ddmForm = new DDMForm();

		ddmForm.setAvailableLocales(
			SetUtil.fromArray(new Locale[] {LocaleUtil.US, LocaleUtil.BRAZIL}));

		ddmForm.addDDMFormField(_createDDMFormField("field1", "text", true));
		ddmForm.addDDMFormField(_createDDMFormField("field2", "text", true));

		DDMFormValues ddmFormValues = DataRecordValuesUtil.toDDMFormValues(
			HashMapBuilder.<String, Object>put(
				"field1",
				HashMapBuilder.put(
					"en_US", "Value 1"
				).put(
					"pt_BR", "Valor 1"
				).build()
			).put(
				"field2",
				HashMapBuilder.put(
					"en_US", "Value 2"
				).put(
					"pt_BR", "Valor 2"
				).build()
			).build(),
			ddmForm, null);

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			ddmFormValues.getDDMFormFieldValuesMap();

		Assert.assertEquals(
			ddmFormFieldValuesMap.toString(), 2, ddmFormFieldValuesMap.size());
		Assert.assertTrue(ddmFormFieldValuesMap.containsKey("field1"));
		Assert.assertTrue(ddmFormFieldValuesMap.containsKey("field2"));

		List<DDMFormFieldValue> ddmFormFieldValues = ddmFormFieldValuesMap.get(
			"field1");

		DDMFormFieldValue ddmFormFieldValue = ddmFormFieldValues.get(0);

		Assert.assertEquals("field1", ddmFormFieldValue.getName());

		Value value = ddmFormFieldValue.getValue();

		Assert.assertTrue(value instanceof LocalizedValue);

		LocalizedValue localizedValue = (LocalizedValue)value;

		Assert.assertEquals(
			"Value 1", localizedValue.getString(LocaleUtil.ENGLISH));
		Assert.assertEquals(
			"Valor 1", localizedValue.getString(LocaleUtil.BRAZIL));

		ddmFormFieldValues = ddmFormFieldValuesMap.get("field2");

		ddmFormFieldValue = ddmFormFieldValues.get(0);

		Assert.assertEquals("field2", ddmFormFieldValue.getName());

		value = ddmFormFieldValue.getValue();

		Assert.assertTrue(value instanceof LocalizedValue);

		localizedValue = (LocalizedValue)value;

		Assert.assertEquals(
			"Value 2", localizedValue.getString(LocaleUtil.ENGLISH));
		Assert.assertEquals(
			"Valor 2", localizedValue.getString(LocaleUtil.BRAZIL));
	}

	@Test
	public void testToDDMFormValuesWithLocale() {
		DDMForm ddmForm = new DDMForm();

		ddmForm.addDDMFormField(_createDDMFormField("field1", "text", true));
		ddmForm.addDDMFormField(_createDDMFormField("field2", "text", true));

		DDMFormValues ddmFormValues = DataRecordValuesUtil.toDDMFormValues(
			HashMapBuilder.<String, Object>put(
				"field1",
				HashMapBuilder.put(
					"en_US", "Value 1"
				).put(
					"pt_BR", "Valor 1"
				).build()
			).put(
				"field2",
				HashMapBuilder.put(
					"en_US", "Value 2"
				).put(
					"pt_BR", "Valor 2"
				).build()
			).build(),
			ddmForm, LocaleUtil.BRAZIL);

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			ddmFormValues.getDDMFormFieldValuesMap();

		Assert.assertEquals(
			ddmFormFieldValuesMap.toString(), 2, ddmFormFieldValuesMap.size());
		Assert.assertTrue(ddmFormFieldValuesMap.containsKey("field1"));
		Assert.assertTrue(ddmFormFieldValuesMap.containsKey("field2"));

		List<DDMFormFieldValue> ddmFormFieldValues = ddmFormFieldValuesMap.get(
			"field1");

		DDMFormFieldValue ddmFormFieldValue = ddmFormFieldValues.get(0);

		Assert.assertEquals("field1", ddmFormFieldValue.getName());

		Value value = ddmFormFieldValue.getValue();

		Assert.assertTrue(value instanceof LocalizedValue);

		LocalizedValue localizedValue = (LocalizedValue)value;

		Set<Locale> availableLocales = localizedValue.getAvailableLocales();

		Assert.assertEquals(
			availableLocales.toString(), 1, availableLocales.size());

		Assert.assertEquals(
			"Valor 1", localizedValue.getString(LocaleUtil.BRAZIL));

		ddmFormFieldValues = ddmFormFieldValuesMap.get("field2");

		ddmFormFieldValue = ddmFormFieldValues.get(0);

		Assert.assertEquals("field2", ddmFormFieldValue.getName());

		value = ddmFormFieldValue.getValue();

		Assert.assertTrue(value instanceof LocalizedValue);

		localizedValue = (LocalizedValue)value;

		Assert.assertEquals(
			availableLocales.toString(), 1, availableLocales.size());
		Assert.assertEquals(
			"Valor 2", localizedValue.getString(LocaleUtil.BRAZIL));
	}

	private DDMFormField _createDDMFormField(
		String name, String type, boolean localizable) {

		DDMFormField ddmFormField = new DDMFormField(name, type);

		ddmFormField.setLocalizable(localizable);

		return ddmFormField;
	}

	private void _setUpJSONFactoryUtil() {
		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());
	}

	private void _setUpLanguageUtil() {
		LanguageUtil languageUtil = new LanguageUtil();

		Language language = mock(Language.class);

		when(
			language.isAvailableLocale(LocaleUtil.BRAZIL)
		).thenReturn(
			true
		);

		when(
			language.isAvailableLocale(LocaleUtil.US)
		).thenReturn(
			true
		);

		when(
			language.getLanguageId(LocaleUtil.BRAZIL)
		).thenReturn(
			"pt_BR"
		);

		when(
			language.getLanguageId(LocaleUtil.US)
		).thenReturn(
			"en_US"
		);

		languageUtil.setLanguage(language);
	}

}