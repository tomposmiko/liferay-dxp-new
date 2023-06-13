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

package com.liferay.document.library.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileEntryMetadata;
import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.document.library.kernel.model.DLFileVersion;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryMetadataLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryTypeLocalServiceUtil;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.kernel.DDMForm;
import com.liferay.dynamic.data.mapping.kernel.DDMFormField;
import com.liferay.dynamic.data.mapping.kernel.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.kernel.DDMFormValues;
import com.liferay.dynamic.data.mapping.kernel.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.dynamic.data.mapping.util.DDMBeanTranslatorUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestDataConstants;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.ByteArrayInputStream;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Michael C. Han
 */
@RunWith(Arquillian.class)
public class DLFileEntryMetadataLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_company = CompanyTestUtil.addCompany();

		_group = GroupTestUtil.addGroup();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group, TestPropsValues.getUserId());

		byte[] testFileBytes = FileUtil.getBytes(
			getClass(), "dependencies/ddmstructure.xml");

		DDMFormDeserializerDeserializeRequest.Builder builder =
			DDMFormDeserializerDeserializeRequest.Builder.newBuilder(
				new String(testFileBytes));

		DDMFormDeserializerDeserializeResponse
			ddmFormDeserializerDeserializeResponse =
				_ddmFormDeserializer.deserialize(builder.build());

		com.liferay.dynamic.data.mapping.model.DDMForm ddmForm =
			ddmFormDeserializerDeserializeResponse.getDDMForm();

		serviceContext.setAttribute(
			"ddmForm", DDMBeanTranslatorUtil.translate(ddmForm));

		User user = TestPropsValues.getUser();

		serviceContext.setLanguageId(LocaleUtil.toLanguageId(user.getLocale()));

		_dlFileEntryType = DLFileEntryTypeLocalServiceUtil.addFileEntryType(
			TestPropsValues.getUserId(), _group.getGroupId(),
			RandomTestUtil.randomString(), StringPool.BLANK, new long[0],
			serviceContext);

		List<com.liferay.dynamic.data.mapping.kernel.DDMStructure>
			ddmStructures = _dlFileEntryType.getDDMStructures();

		com.liferay.dynamic.data.mapping.kernel.DDMStructure ddmStructure =
			ddmStructures.get(0);

		_ddmStructure = DDMStructureLocalServiceUtil.getStructure(
			ddmStructure.getStructureId());

		Map<String, DDMFormValues> ddmFormValuesMap = setUpDDMFormValuesMap(
			_ddmStructure.getStructureKey(), user.getLocale());

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
			TestDataConstants.TEST_BYTE_ARRAY);

		_dlFileEntry = DLFileEntryLocalServiceUtil.addFileEntry(
			TestPropsValues.getUserId(), _group.getGroupId(),
			_group.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			RandomTestUtil.randomString(), null, RandomTestUtil.randomString(),
			null, null, _dlFileEntryType.getFileEntryTypeId(), ddmFormValuesMap,
			null, byteArrayInputStream, byteArrayInputStream.available(),
			serviceContext);
	}

	@Test
	public void testGetMismatchedCompanyIdFileEntryMetadatas()
		throws Exception {

		try {
			DLFileVersion dlFileVersion = _dlFileEntry.getFileVersion();

			DLFileEntryMetadata dlFileEntryMetadata =
				DLFileEntryMetadataLocalServiceUtil.fetchFileEntryMetadata(
					_ddmStructure.getStructureId(),
					dlFileVersion.getFileVersionId());

			_ddmStructure.setCompanyId(_company.getCompanyId());

			_ddmStructure = DDMStructureLocalServiceUtil.updateDDMStructure(
				_ddmStructure);

			List<DLFileEntryMetadata> dlFileEntryMetadatas =
				DLFileEntryMetadataLocalServiceUtil.
					getMismatchedCompanyIdFileEntryMetadatas();

			Assert.assertEquals(
				dlFileEntryMetadatas.toString(), 1,
				dlFileEntryMetadatas.size());
			Assert.assertEquals(
				dlFileEntryMetadata, dlFileEntryMetadatas.get(0));
		}
		finally {
			if (_ddmStructure != null) {
				_ddmStructure.setCompanyId(_dlFileEntry.getCompanyId());

				_ddmStructure = DDMStructureLocalServiceUtil.updateDDMStructure(
					_ddmStructure);
			}
		}
	}

	@Test
	public void testGetNoStructuresFileEntryMetadatas() throws Exception {
		try {
			DLFileVersion dlFileVersion = _dlFileEntry.getFileVersion();

			DLFileEntryMetadata dlFileEntryMetadata =
				DLFileEntryMetadataLocalServiceUtil.fetchFileEntryMetadata(
					_ddmStructure.getStructureId(),
					dlFileVersion.getFileVersionId());

			DDMStructureLocalServiceUtil.deleteDDMStructure(_ddmStructure);

			List<DLFileEntryMetadata> dlFileEntryMetadatas =
				DLFileEntryMetadataLocalServiceUtil.
					getNoStructuresFileEntryMetadatas();

			Assert.assertEquals(
				dlFileEntryMetadatas.toString(), 1,
				dlFileEntryMetadatas.size());
			Assert.assertEquals(
				dlFileEntryMetadata, dlFileEntryMetadatas.get(0));
		}
		finally {
			if (_ddmStructure != null) {
				DDMStructureLocalServiceUtil.addDDMStructure(_ddmStructure);
			}
		}
	}

	protected Map<String, DDMFormValues> setUpDDMFormValuesMap(
		String ddmStructureKey, Locale currentLocale) {

		Set<Locale> availableLocales = DDMFormTestUtil.createAvailableLocales(
			currentLocale);

		DDMForm ddmForm = new DDMForm();

		ddmForm.setAvailableLocales(availableLocales);
		ddmForm.setDefaultLocale(currentLocale);

		DDMFormField ddmFormField = new DDMFormField("date_an", "ddm-date");

		ddmFormField.setDataType("date");

		ddmForm.addDDMFormField(ddmFormField);

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		ddmFormValues.setAvailableLocales(availableLocales);
		ddmFormValues.setDefaultLocale(currentLocale);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("date_an");
		ddmFormFieldValue.setValue(new UnlocalizedValue(""));

		ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);

		Map<String, DDMFormValues> ddmFormValuesMap = new HashMap<>();

		ddmFormValuesMap.put(ddmStructureKey, ddmFormValues);

		return ddmFormValuesMap;
	}

	@DeleteAfterTestRun
	private Company _company;

	@Inject(filter = "ddm.form.deserializer.type=xsd")
	private DDMFormDeserializer _ddmFormDeserializer;

	private DDMStructure _ddmStructure;
	private DLFileEntry _dlFileEntry;
	private DLFileEntryType _dlFileEntryType;

	@DeleteAfterTestRun
	private Group _group;

}