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

package com.liferay.journal.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.data.engine.rest.dto.v2_0.DataDefinition;
import com.liferay.data.engine.rest.resource.v2_0.DataDefinitionResource;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.journal.constants.JournalArticleConstants;
import com.liferay.journal.constants.JournalFolderConstants;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.File;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eudaldo Alonso
 */
@RunWith(Arquillian.class)
public class JournalArticleDDMFormConverterTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testUpdateDataDefinitionMoveFieldFromRootToNestedField()
		throws Exception {

		DataDefinition dataDefinition = DataDefinition.toDTO(
			_readFileToString("dependencies/data_definition.json"));

		dataDefinition.setName(
			HashMapBuilder.<String, Object>put(
				String.valueOf(LocaleUtil.US), "Structure"
			).build());

		DataDefinitionResource.Builder dataDefinitionResourcedBuilder =
			_dataDefinitionResourceFactory.create();

		DataDefinitionResource dataDefinitionResource =
			dataDefinitionResourcedBuilder.user(
				TestPropsValues.getUser()
			).build();

		dataDefinition =
			dataDefinitionResource.postSiteDataDefinitionByContentType(
				_group.getGroupId(), "journal", dataDefinition);

		JournalArticle journalArticle =
			JournalTestUtil.addArticleWithXMLContent(
				_group.getGroupId(),
				JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				JournalArticleConstants.CLASS_NAME_ID_DEFAULT,
				_readFileToString("dependencies/journal_content.xml"),
				dataDefinition.getDataDefinitionKey(), null, LocaleUtil.US);

		DDMFormValues ddmFormValues = journalArticle.getDDMFormValues();

		Assert.assertEquals(
			"Test Content", _getValue(ddmFormValues.getDDMFormFieldValues()));

		DataDefinition updatedDataDefinition = DataDefinition.toDTO(
			_readFileToString("dependencies/updated_data_definition.json"));

		dataDefinitionResource.putDataDefinition(
			dataDefinition.getId(), updatedDataDefinition);

		journalArticle = _journalArticleLocalService.updateArticle(
			journalArticle.getUserId(), journalArticle.getGroupId(),
			journalArticle.getFolderId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), journalArticle.getContent(),
			ServiceContextTestUtil.getServiceContext());

		ddmFormValues = journalArticle.getDDMFormValues();

		Assert.assertEquals(
			"Test Content", _getValue(ddmFormValues.getDDMFormFieldValues()));
	}

	@Test
	public void testUpdateDataDefinitionMoveFieldFromRootToNestedFieldWithMultipleNestedFields()
		throws Exception {

		DataDefinition dataDefinition = DataDefinition.toDTO(
			_readFileToString("dependencies/data_definition.json"));

		dataDefinition.setName(
			HashMapBuilder.<String, Object>put(
				String.valueOf(LocaleUtil.US), "Structure"
			).build());

		DataDefinitionResource.Builder dataDefinitionResourcedBuilder =
			_dataDefinitionResourceFactory.create();

		DataDefinitionResource dataDefinitionResource =
			dataDefinitionResourcedBuilder.user(
				TestPropsValues.getUser()
			).build();

		dataDefinition =
			dataDefinitionResource.postSiteDataDefinitionByContentType(
				_group.getGroupId(), "journal", dataDefinition);

		JournalArticle journalArticle =
			JournalTestUtil.addArticleWithXMLContent(
				_group.getGroupId(),
				JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				JournalArticleConstants.CLASS_NAME_ID_DEFAULT,
				_readFileToString("dependencies/journal_content.xml"),
				dataDefinition.getDataDefinitionKey(), null, LocaleUtil.US);

		DDMFormValues ddmFormValues = journalArticle.getDDMFormValues();

		Assert.assertEquals(
			"Test Content", _getValue(ddmFormValues.getDDMFormFieldValues()));

		DataDefinition updatedDataDefinition = DataDefinition.toDTO(
			_readFileToString("dependencies/updated_data_definition.json"));

		dataDefinitionResource.putDataDefinition(
			dataDefinition.getId(), updatedDataDefinition);

		journalArticle = _journalArticleLocalService.updateArticle(
			journalArticle.getUserId(), journalArticle.getGroupId(),
			journalArticle.getFolderId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), journalArticle.getContent(),
			ServiceContextTestUtil.getServiceContext());

		ddmFormValues = journalArticle.getDDMFormValues();

		Assert.assertEquals(
			"Test Content", _getValue(ddmFormValues.getDDMFormFieldValues()));
	}

	@Test
	public void testUpdateDataDefinitionMovesFieldFromNestedFieldToRoot()
		throws Exception {

		DataDefinition dataDefinition = DataDefinition.toDTO(
			_readFileToString("dependencies/updated_data_definition.json"));

		dataDefinition.setName(
			HashMapBuilder.<String, Object>put(
				String.valueOf(LocaleUtil.US), "Structure"
			).build());

		DataDefinitionResource.Builder dataDefinitionResourcedBuilder =
			_dataDefinitionResourceFactory.create();

		DataDefinitionResource dataDefinitionResource =
			dataDefinitionResourcedBuilder.user(
				TestPropsValues.getUser()
			).build();

		dataDefinition =
			dataDefinitionResource.postSiteDataDefinitionByContentType(
				_group.getGroupId(), "journal", dataDefinition);

		JournalArticle journalArticle =
			JournalTestUtil.addArticleWithXMLContent(
				_group.getGroupId(),
				JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				JournalArticleConstants.CLASS_NAME_ID_DEFAULT,
				_readFileToString("dependencies/updated_journal_content.xml"),
				dataDefinition.getDataDefinitionKey(), null, LocaleUtil.US);

		DDMFormValues ddmFormValues = journalArticle.getDDMFormValues();

		Assert.assertEquals(
			"Test Content", _getValue(ddmFormValues.getDDMFormFieldValues()));

		DataDefinition updatedDataDefinition = DataDefinition.toDTO(
			_readFileToString("dependencies/data_definition.json"));

		dataDefinitionResource.putDataDefinition(
			dataDefinition.getId(), updatedDataDefinition);

		journalArticle = _journalArticleLocalService.updateArticle(
			journalArticle.getUserId(), journalArticle.getGroupId(),
			journalArticle.getFolderId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), journalArticle.getContent(),
			ServiceContextTestUtil.getServiceContext());

		ddmFormValues = journalArticle.getDDMFormValues();

		Assert.assertEquals(
			"Test Content", _getValue(ddmFormValues.getDDMFormFieldValues()));
	}

	private String _getValue(List<DDMFormFieldValue> ddmFormFieldValues) {
		String valueString = StringPool.BLANK;

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			if (Objects.equals(
					ddmFormFieldValue.getFieldReference(), "Text45440928")) {

				Value value = ddmFormFieldValue.getValue();

				return value.getString(LocaleUtil.US);
			}

			if (ListUtil.isNotEmpty(
					ddmFormFieldValue.getNestedDDMFormFieldValues())) {

				valueString = _getValue(
					ddmFormFieldValue.getNestedDDMFormFieldValues());
			}
		}

		return valueString;
	}

	private String _readFileToString(String s) throws Exception {
		return new String(_file.getBytes(getClass(), s));
	}

	@Inject
	private DataDefinitionResource.Factory _dataDefinitionResourceFactory;

	@Inject
	private File _file;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private JournalArticleLocalService _journalArticleLocalService;

}