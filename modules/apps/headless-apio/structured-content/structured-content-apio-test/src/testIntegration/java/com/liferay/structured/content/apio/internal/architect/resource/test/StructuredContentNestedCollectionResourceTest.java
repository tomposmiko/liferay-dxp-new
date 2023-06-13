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

package com.liferay.structured.content.apio.internal.architect.resource.test;

import com.liferay.apio.architect.language.AcceptLanguage;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONDeserializer;
import com.liferay.dynamic.data.mapping.kernel.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.storage.StorageType;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestHelper;
import com.liferay.dynamic.data.mapping.test.util.DDMTemplateTestUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalArticleConstants;
import com.liferay.journal.model.JournalArticleWrapper;
import com.liferay.journal.model.JournalFolderConstants;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.apio.test.util.PaginationRequest;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.test.context.ContextUserReplace;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.odata.filter.Filter;
import com.liferay.portal.odata.sort.Sort;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerTestRule;
import com.liferay.structured.content.apio.architect.resource.StructuredContentField;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.assertj.core.api.Assertions;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Cristina González
 */
@RunWith(Arquillian.class)
public class StructuredContentNestedCollectionResourceTest
	extends BaseStructuredContentNestedCollectionResourceTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testGetDataTypeWithBoolean() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyBoolean");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals("boolean", structuredContentField.getDataType());
	}

	@Test
	public void testGetDataTypeWithURL() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyLinkToPage");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals("url", structuredContentField.getDataType());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithBoolean() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyBoolean");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			StringBundler.concat(
				"_", String.valueOf(ddmStructure.getStructureId()),
				"_MyBoolean"),
			structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithColor() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyColor");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			null, structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithDate() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyDate");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			StringBundler.concat(
				"_", String.valueOf(ddmStructure.getStructureId()), "_MyDate"),
			structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithDecimal() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyDecimal");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			StringBundler.concat(
				"_", String.valueOf(ddmStructure.getStructureId()),
				"_MyDecimal"),
			structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithDocumentsAndMedia()
		throws Exception {

		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyDocumentsAndMedia");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			null, structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithGeolocation()
		throws Exception {

		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyGeolocation");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			null, structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithHTML() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyHTML");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			null, structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithInteger() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyInteger");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			StringBundler.concat(
				"_", String.valueOf(ddmStructure.getStructureId()),
				"_MyInteger"),
			structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithNumber() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyNumber");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			StringBundler.concat(
				"_", String.valueOf(ddmStructure.getStructureId()),
				"_MyNumber"),
			structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithRadio() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyRadio");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			StringBundler.concat(
				"_", String.valueOf(ddmStructure.getStructureId()), "_MyRadio"),
			structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithSelect() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MySelect");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			null, structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithText() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyText");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			StringBundler.concat(
				"_", String.valueOf(ddmStructure.getStructureId()), "_MyText"),
			structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetFilterAndSortIdentifierWithTextBox() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyTextBox");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			null, structuredContentField.getFilterAndSortIdentifier());
	}

	@Test
	public void testGetInputControlWithCheckBox() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyBoolean");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertEquals(
			"checkbox", structuredContentField.getInputControl());
	}

	@Test
	public void testGetInputControlWithNullInteger() throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("MyInteger");

		StructuredContentField structuredContentField =
			createStructuredContentField(ddmFormFieldValue, ddmStructure);

		Assert.assertNull(structuredContentField.getInputControl());
	}

	@Test
	public void testGetJournalArticleWrapper() throws Throwable {
		Map<Locale, String> stringMap = new HashMap<>();

		String title = RandomTestUtil.randomString();

		stringMap.put(LocaleUtil.getDefault(), title);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASSNAME_ID_DEFAULT, title, false,
			stringMap, stringMap, stringMap, null, LocaleUtil.getDefault(),
			null, true, true, serviceContext);

		JournalArticleWrapper journalArticleWrapper = getJournalArticleWrapper(
			journalArticle.getResourcePrimKey(),
			getThemeDisplay(_group, LocaleUtil.getDefault()));

		Assert.assertEquals(
			title, journalArticleWrapper.getTitle(LocaleUtil.getDefault()));
	}

	@Test
	public void testGetJournalArticleWrapperFilterByPermission()
		throws Exception {

		Map<Locale, String> stringMap = new HashMap<>();

		String title = RandomTestUtil.randomString();

		stringMap.put(LocaleUtil.getDefault(), title);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		serviceContext.setAddGuestPermissions(false);
		serviceContext.setAddGroupPermissions(false);

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASSNAME_ID_DEFAULT, title, false,
			stringMap, stringMap, stringMap, null, LocaleUtil.getDefault(),
			null, true, true, serviceContext);

		User user = UserTestUtil.addUser();

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		try (ContextUserReplace contextUserReplace = new ContextUserReplace(
				user, permissionChecker)) {

			Assertions.assertThatThrownBy(
				() -> getJournalArticleWrapper(
					journalArticle.getId(),
					getThemeDisplay(_group, LocaleUtil.getDefault()))
			).isInstanceOf(
				PrincipalException.MustHavePermission.class
			);
		}
		finally {
			_userLocalService.deleteUser(user);
		}
	}

	@Test
	public void testGetPageItems() throws Exception {
		Map<Locale, String> stringMap = new HashMap<>();

		stringMap.put(LocaleUtil.getDefault(), RandomTestUtil.randomString());
		stringMap.put(LocaleUtil.GERMANY, RandomTestUtil.randomString());
		stringMap.put(LocaleUtil.SPAIN, RandomTestUtil.randomString());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASSNAME_ID_DEFAULT,
			RandomTestUtil.randomString(), false, stringMap, stringMap,
			stringMap, null, LocaleUtil.getDefault(), null, true, true,
			serviceContext);

		PageItems<JournalArticle> pageItems = getPageItems(
			PaginationRequest.of(10, 1), _group.getGroupId(), _acceptLanguage,
			getThemeDisplay(_group, LocaleUtil.getDefault()),
			Filter.emptyFilter(), Sort.emptySort());

		Assert.assertEquals(1, pageItems.getTotalCount());

		List<JournalArticle> journalArticles =
			(List<JournalArticle>)pageItems.getItems();

		Assert.assertTrue(
			"Journal articles: " + journalArticles,
			journalArticles.contains(journalArticle));
	}

	@Test
	public void testGetPageItemsFilterByPermission() throws Exception {
		Map<Locale, String> stringMap = new HashMap<>();

		stringMap.put(LocaleUtil.getDefault(), RandomTestUtil.randomString());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		serviceContext.setAddGuestPermissions(false);
		serviceContext.setAddGroupPermissions(false);

		JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASSNAME_ID_DEFAULT,
			RandomTestUtil.randomString(), false, stringMap, stringMap,
			stringMap, null, LocaleUtil.getDefault(), null, true, true,
			serviceContext);

		User user = UserTestUtil.addUser();

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		try (ContextUserReplace contextUserReplace = new ContextUserReplace(
				user, permissionChecker)) {

			PageItems<JournalArticle> pageItems = getPageItems(
				PaginationRequest.of(10, 1), _group.getGroupId(),
				_acceptLanguage,
				getThemeDisplay(_group, LocaleUtil.getDefault()),
				Filter.emptyFilter(), Sort.emptySort());

			Assert.assertEquals(0, pageItems.getTotalCount());
		}
		finally {
			_userLocalService.deleteUser(user);
		}
	}

	@Test
	public void testGetPageItemsWith2VersionsAnd1Scheduled() throws Exception {
		Map<Locale, String> stringMap = new HashMap<>();

		stringMap.put(LocaleUtil.getDefault(), "Version 1");
		stringMap.put(LocaleUtil.GERMANY, RandomTestUtil.randomString());
		stringMap.put(LocaleUtil.SPAIN, RandomTestUtil.randomString());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASSNAME_ID_DEFAULT,
			RandomTestUtil.randomString(), false, stringMap, stringMap,
			stringMap, null, LocaleUtil.getDefault(), null, null, true, true,
			serviceContext);

		// Create new version scheduled for tomorrow

		stringMap.put(LocaleUtil.getDefault(), "Version 2");

		Date displayDate = new Date(
			System.currentTimeMillis() + 24 * 60 * 60 * 1000);

		JournalTestUtil.updateArticle(
			serviceContext.getUserId(), journalArticle, stringMap,
			journalArticle.getContent(), displayDate, true, true,
			serviceContext);

		int journalArticlesCount = _journalArticleLocalService.getArticlesCount(
			journalArticle.getGroupId(), journalArticle.getArticleId());

		Assert.assertEquals(2, journalArticlesCount);

		PageItems<JournalArticle> pageItems = getPageItems(
			PaginationRequest.of(10, 1), _group.getGroupId(), _acceptLanguage,
			getThemeDisplay(_group, LocaleUtil.getDefault()),
			Filter.emptyFilter(), Sort.emptySort());

		Assert.assertEquals(1, pageItems.getTotalCount());

		List<JournalArticle> journalArticles =
			(List<JournalArticle>)pageItems.getItems();

		Assert.assertTrue(
			"Journal articles: " + journalArticles,
			journalArticles.contains(journalArticle));

		JournalArticle foundJournalArticle = journalArticles.get(0);

		Assert.assertEquals(
			"Version 1", foundJournalArticle.getTitle(LocaleUtil.getDefault()));
	}

	@Test
	public void testGetPageItemsWith2VersionsAndOnly1Approved()
		throws Exception {

		Map<Locale, String> stringMap = new HashMap<>();

		stringMap.put(LocaleUtil.getDefault(), "Version 1");
		stringMap.put(LocaleUtil.GERMANY, RandomTestUtil.randomString());
		stringMap.put(LocaleUtil.SPAIN, RandomTestUtil.randomString());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASSNAME_ID_DEFAULT,
			RandomTestUtil.randomString(), false, stringMap, stringMap,
			stringMap, null, LocaleUtil.getDefault(), null, true, true,
			serviceContext);

		// Create new version as a draft

		JournalTestUtil.updateArticle(
			journalArticle, "Version 2", journalArticle.getContent(), true,
			false, serviceContext);

		int journalArticlesCount = _journalArticleLocalService.getArticlesCount(
			journalArticle.getGroupId(), journalArticle.getArticleId());

		Assert.assertEquals(2, journalArticlesCount);

		PageItems<JournalArticle> pageItems = getPageItems(
			PaginationRequest.of(10, 1), _group.getGroupId(), _acceptLanguage,
			getThemeDisplay(_group, LocaleUtil.getDefault()),
			Filter.emptyFilter(), Sort.emptySort());

		Assert.assertEquals(1, pageItems.getTotalCount());

		List<JournalArticle> journalArticles =
			(List<JournalArticle>)pageItems.getItems();

		Assert.assertTrue(
			"Journal articles: " + journalArticles,
			journalArticles.contains(journalArticle));

		JournalArticle foundJournalArticle = journalArticles.get(0);

		Assert.assertEquals(
			"Version 1", foundJournalArticle.getTitle(LocaleUtil.getDefault()));
	}

	@Test
	public void testGetPageItemsWith2VersionsApproved() throws Exception {
		Map<Locale, String> stringMap = new HashMap<>();

		stringMap.put(LocaleUtil.getDefault(), RandomTestUtil.randomString());
		stringMap.put(LocaleUtil.GERMANY, RandomTestUtil.randomString());
		stringMap.put(LocaleUtil.SPAIN, RandomTestUtil.randomString());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASSNAME_ID_DEFAULT,
			RandomTestUtil.randomString(), false, stringMap, stringMap,
			stringMap, null, LocaleUtil.getDefault(), null, null, true, true,
			serviceContext);

		journalArticle = JournalTestUtil.updateArticle(
			journalArticle, "Version 2", journalArticle.getContent(), true,
			true, serviceContext);

		int journalArticlesCount = _journalArticleLocalService.getArticlesCount(
			journalArticle.getGroupId(), journalArticle.getArticleId());

		Assert.assertEquals(2, journalArticlesCount);

		PageItems<JournalArticle> pageItems = getPageItems(
			PaginationRequest.of(10, 1), _group.getGroupId(), _acceptLanguage,
			getThemeDisplay(_group, LocaleUtil.getDefault()),
			Filter.emptyFilter(), Sort.emptySort());

		Assert.assertEquals(1, pageItems.getTotalCount());

		List<JournalArticle> journalArticles =
			(List<JournalArticle>)pageItems.getItems();

		Assert.assertTrue(
			"Journal articles: " + journalArticles,
			journalArticles.contains(journalArticle));

		JournalArticle foundJournalArticle = journalArticles.get(0);

		Assert.assertEquals(
			"Version 2", foundJournalArticle.getTitle(LocaleUtil.getDefault()));
	}

	@Test
	public void testGetPageItemsWithOnlyOneDraftVersion() throws Exception {
		Map<Locale, String> stringMap = new HashMap<>();

		stringMap.put(LocaleUtil.getDefault(), "Version 1");
		stringMap.put(LocaleUtil.GERMANY, RandomTestUtil.randomString());
		stringMap.put(LocaleUtil.SPAIN, RandomTestUtil.randomString());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASSNAME_ID_DEFAULT,
			RandomTestUtil.randomString(), false, stringMap, stringMap,
			stringMap, null, LocaleUtil.getDefault(), null, null, true, false,
			serviceContext);

		int journalArticlesCount = _journalArticleLocalService.getArticlesCount(
			journalArticle.getGroupId(), journalArticle.getArticleId());

		Assert.assertEquals(1, journalArticlesCount);

		PageItems<JournalArticle> pageItems = getPageItems(
			PaginationRequest.of(10, 1), _group.getGroupId(), _acceptLanguage,
			getThemeDisplay(_group, LocaleUtil.getDefault()),
			Filter.emptyFilter(), Sort.emptySort());

		Assert.assertEquals(0, pageItems.getTotalCount());
	}

	@Test
	public void testGetPageItemsWithOnlyOneExpiredVersion() throws Exception {
		Map<Locale, String> stringMap = new HashMap<>();

		stringMap.put(LocaleUtil.getDefault(), "Version 1");
		stringMap.put(LocaleUtil.GERMANY, RandomTestUtil.randomString());
		stringMap.put(LocaleUtil.SPAIN, RandomTestUtil.randomString());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASSNAME_ID_DEFAULT,
			RandomTestUtil.randomString(), false, stringMap, stringMap,
			stringMap, null, LocaleUtil.getDefault(), null, null, true, true,
			serviceContext);

		_journalArticleLocalService.updateStatus(
			serviceContext.getUserId(), journalArticle,
			WorkflowConstants.STATUS_EXPIRED, null, serviceContext,
			new HashMap<>());

		int journalArticlesCount = _journalArticleLocalService.getArticlesCount(
			journalArticle.getGroupId(), journalArticle.getArticleId());

		Assert.assertEquals(1, journalArticlesCount);

		PageItems<JournalArticle> pageItems = getPageItems(
			PaginationRequest.of(10, 1), _group.getGroupId(), _acceptLanguage,
			getThemeDisplay(_group, LocaleUtil.getDefault()),
			Filter.emptyFilter(), Sort.emptySort());

		Assert.assertEquals(0, pageItems.getTotalCount());
	}

	@Test
	public void testGetPageItemsWithOnlyOneSheduledVersion() throws Exception {
		Map<Locale, String> stringMap = new HashMap<>();

		stringMap.put(LocaleUtil.getDefault(), "Version 1");
		stringMap.put(LocaleUtil.GERMANY, RandomTestUtil.randomString());
		stringMap.put(LocaleUtil.SPAIN, RandomTestUtil.randomString());

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		Date displayDate = new Date(
			System.currentTimeMillis() + 24 * 60 * 60 * 1000); // Tomorrow

		JournalArticle journalArticle = JournalTestUtil.addArticle(
			_group.getGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			JournalArticleConstants.CLASSNAME_ID_DEFAULT,
			RandomTestUtil.randomString(), false, stringMap, stringMap,
			stringMap, null, LocaleUtil.getDefault(), displayDate, null, true,
			true, serviceContext);

		int journalArticlesCount = _journalArticleLocalService.getArticlesCount(
			journalArticle.getGroupId(), journalArticle.getArticleId());

		Assert.assertEquals(1, journalArticlesCount);

		PageItems<JournalArticle> pageItems = getPageItems(
			PaginationRequest.of(10, 1), _group.getGroupId(), _acceptLanguage,
			getThemeDisplay(_group, LocaleUtil.getDefault()),
			Filter.emptyFilter(), Sort.emptySort());

		Assert.assertEquals(0, pageItems.getTotalCount());
	}

	@Test
	public void testGetStructuredContentFields() throws Throwable {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-all-fields-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-all-fields-template.xsl"), LocaleUtil.US);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		Map<Locale, String> titleMap = new HashMap<>();

		titleMap.put(LocaleUtil.US, RandomTestUtil.randomString());

		JournalArticle journalArticle =
			JournalArticleLocalServiceUtil.addArticle(
				serviceContext.getUserId(), serviceContext.getScopeGroupId(),
				JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				ClassNameLocalServiceUtil.getClassNameId(DDMStructure.class),
				ddmStructure.getStructureId(), StringPool.BLANK, true, 0,
				titleMap, null, read("test-journal-all-fields-content-1.xml"),
				ddmStructure.getStructureKey(), ddmTemplate.getTemplateKey(),
				null, 1, 1, 1965, 0, 0, 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 0,
				true, true, false, null, null, null, null, serviceContext);

		List<StructuredContentField> structuredContentFields =
			getStructuredContentFields(
				new JournalArticleWrapper(journalArticle));

		boolean found = false;

		for (StructuredContentField structuredContentField :
				structuredContentFields) {

			if (Objects.equals("MyText", structuredContentField.getName())) {
				Assert.assertEquals(
					"string", structuredContentField.getDataType());
				Assert.assertEquals(
					"text", structuredContentField.getInputControl());
				Assert.assertEquals(
					"TextFieldNameLabel_us",
					structuredContentField.getLocalizedLabel(LocaleUtil.US));
				Assert.assertEquals(
					"TextFieldValue_us",
					structuredContentField.getLocalizedValue(LocaleUtil.US));

				found = true;
			}
		}

		Assert.assertTrue("The field MyText was not found", found);
	}

	@Test
	public void testGetStructuredContentFieldsWithSeparator() throws Throwable {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), _group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			deserialize(
				_ddmFormJSONDeserializer,
				read("test-journal-separator-field-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplate ddmTemplate = DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			read("test-journal-separator-field-template.xsl"), LocaleUtil.US);

		String content = read("test-journal-separator-field-content.xml");

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		Map<Locale, String> titleMap = new HashMap<>();

		titleMap.put(LocaleUtil.US, RandomTestUtil.randomString());

		JournalArticle journalArticle = _journalArticleLocalService.addArticle(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			ClassNameLocalServiceUtil.getClassNameId(DDMStructure.class),
			ddmStructure.getStructureId(), StringPool.BLANK, true, 0, titleMap,
			null, content, ddmStructure.getStructureKey(),
			ddmTemplate.getTemplateKey(), null, 1, 1, 1965, 0, 0, 0, 0, 0, 0, 0,
			true, 0, 0, 0, 0, 0, true, true, false, null, null, null, null,
			serviceContext);

		List<StructuredContentField> structuredContentFields =
			getStructuredContentFields(
				new JournalArticleWrapper(journalArticle));

		Assert.assertEquals(
			structuredContentFields.toString(), 0,
			structuredContentFields.size());
	}

	private static final AcceptLanguage _acceptLanguage =
		() -> LocaleUtil.getDefault();

	@Inject
	private DDMFormJSONDeserializer _ddmFormJSONDeserializer;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private JournalArticleLocalService _journalArticleLocalService;

	@Inject
	private UserLocalService _userLocalService;

}