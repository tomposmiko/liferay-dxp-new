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

package com.liferay.headless.web.experience.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeRequest;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerDeserializeResponse;
import com.liferay.dynamic.data.mapping.io.DDMFormDeserializerTracker;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.storage.StorageType;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestHelper;
import com.liferay.dynamic.data.mapping.test.util.DDMTemplateTestUtil;
import com.liferay.headless.web.experience.dto.v1_0.StructuredContent;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.test.util.JournalTestUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;

import java.io.InputStream;

import java.util.Objects;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * @author Javier Gamarra
 */
@RunWith(Arquillian.class)
public class StructuredContentResourceTest
	extends BaseStructuredContentResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		Registry registry = RegistryUtil.getRegistry();

		_serviceReference = registry.getServiceReference(
			DDMFormDeserializerTracker.class);

		_ddmFormDeserializerTracker = registry.getService(_serviceReference);

		_ddmStructure = _addDDMStructure(testGroup);
		_irrelevantDDMStructure = _addDDMStructure(irrelevantGroup);

		_journalFolder = JournalTestUtil.addFolder(
			testGroup.getGroupId(), RandomTestUtil.randomString());
		_irrelevantJournalFolder = JournalTestUtil.addFolder(
			irrelevantGroup.getGroupId(), RandomTestUtil.randomString());
	}

	@After
	@Override
	public void tearDown() throws Exception {
		Registry registry = RegistryUtil.getRegistry();

		_ddmFormDeserializerTracker = null;

		registry.ungetService(_serviceReference);

		super.tearDown();
	}

	protected void assertValid(StructuredContent structuredContent) {
		boolean valid = false;

		if (Objects.equals(
				structuredContent.getContentSpaceId(),
				testGroup.getGroupId()) &&
			(structuredContent.getDateCreated() != null) &&
			(structuredContent.getDateModified() != null) &&
			(structuredContent.getId() != null)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	@Override
	protected boolean equals(
		StructuredContent structuredContent1,
		StructuredContent structuredContent2) {

		if (Objects.equals(
				structuredContent1.getContentSpaceId(),
				structuredContent2.getContentSpaceId()) &&
			Objects.equals(
				structuredContent1.getContentStructureId(),
				structuredContent2.getContentStructureId()) &&
			Objects.equals(
				structuredContent1.getDescription(),
				structuredContent2.getDescription()) &&
			Objects.equals(
				structuredContent1.getTitle(), structuredContent2.getTitle())) {

			return true;
		}

		return false;
	}

	@Override
	protected StructuredContent randomIrrelevantStructuredContent() {
		StructuredContent structuredContent = randomStructuredContent();

		structuredContent.setContentSpaceId(irrelevantGroup.getGroupId());
		structuredContent.setContentStructureId(
			_irrelevantDDMStructure.getStructureId());

		return structuredContent;
	}

	@Override
	protected StructuredContent randomStructuredContent() {
		return new StructuredContent() {
			{
				contentSpaceId = testGroup.getGroupId();
				contentStructureId = _ddmStructure.getStructureId();
				datePublished = RandomTestUtil.nextDate();
				description = RandomTestUtil.randomString();
				lastReviewed = RandomTestUtil.nextDate();
				title = RandomTestUtil.randomString();
			}
		};
	}

	@Override
	protected StructuredContent
			testDeleteStructuredContent_addStructuredContent()
		throws Exception {

		return invokePostContentSpaceStructuredContent(
			testGroup.getGroupId(), randomStructuredContent());
	}

	@Override
	protected StructuredContent
			testGetContentSpaceStructuredContentByKey_addStructuredContent()
		throws Exception {

		return invokePostContentSpaceStructuredContent(
			testGroup.getGroupId(), randomStructuredContent());
	}

	@Override
	protected StructuredContent
			testGetContentSpaceStructuredContentByUuid_addStructuredContent()
		throws Exception {

		return invokePostContentSpaceStructuredContent(
			testGroup.getGroupId(), randomStructuredContent());
	}

	@Override
	protected StructuredContent
			testGetContentSpaceStructuredContentsPage_addStructuredContent(
				Long contentSpaceId, StructuredContent structuredContent)
		throws Exception {

		return invokePostContentSpaceStructuredContent(
			contentSpaceId, structuredContent);
	}

	@Override
	protected StructuredContent
			testGetContentStructureStructuredContentsPage_addStructuredContent(
				Long contentStructureId, StructuredContent structuredContent)
		throws Exception {

		return invokePostContentSpaceStructuredContent(
			testGroup.getGroupId(), structuredContent);
	}

	@Override
	protected Long
			testGetContentStructureStructuredContentsPage_getContentStructureId()
		throws Exception {

		return _ddmStructure.getStructureId();
	}

	@Override
	protected StructuredContent testGetStructuredContent_addStructuredContent()
		throws Exception {

		return invokePostContentSpaceStructuredContent(
			testGroup.getGroupId(), randomStructuredContent());
	}

	@Override
	protected StructuredContent
			testGetStructuredContentFolderStructuredContentsPage_addStructuredContent(
				Long structuredContentFolderId,
				StructuredContent structuredContent)
		throws Exception {

		return invokePostStructuredContentFolderStructuredContent(
			structuredContentFolderId, structuredContent);
	}

	@Override
	protected Long
		testGetStructuredContentFolderStructuredContentsPage_getIrrelevantStructuredContentFolderId() {

		return _irrelevantJournalFolder.getFolderId();
	}

	@Override
	protected Long
		testGetStructuredContentFolderStructuredContentsPage_getStructuredContentFolderId() {

		return _journalFolder.getFolderId();
	}

	@Override
	protected StructuredContent
			testPatchStructuredContent_addStructuredContent()
		throws Exception {

		return invokePostContentSpaceStructuredContent(
			testGroup.getGroupId(), randomStructuredContent());
	}

	@Override
	protected StructuredContent
			testPostContentSpaceStructuredContent_addStructuredContent(
				StructuredContent structuredContent)
		throws Exception {

		return invokePostContentSpaceStructuredContent(
			testGroup.getGroupId(), structuredContent);
	}

	@Override
	protected StructuredContent
			testPostStructuredContentFolderStructuredContent_addStructuredContent(
				StructuredContent structuredContent)
		throws Exception {

		return invokePostContentSpaceStructuredContent(
			testGroup.getGroupId(), structuredContent);
	}

	@Override
	protected StructuredContent testPutStructuredContent_addStructuredContent()
		throws Exception {

		return invokePostContentSpaceStructuredContent(
			testGroup.getGroupId(), randomStructuredContent());
	}

	private DDMStructure _addDDMStructure(Group group) throws Exception {
		DDMStructureTestHelper ddmStructureTestHelper =
			new DDMStructureTestHelper(
				PortalUtil.getClassNameId(JournalArticle.class), group);

		DDMStructure ddmStructure = ddmStructureTestHelper.addStructure(
			PortalUtil.getClassNameId(JournalArticle.class),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			_deserialize(_read("test-structured-content-structure.json")),
			StorageType.JSON.getValue(), DDMStructureConstants.TYPE_DEFAULT);

		DDMTemplateTestUtil.addTemplate(
			ddmStructure.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(JournalArticle.class),
			TemplateConstants.LANG_TYPE_VM,
			_read("test-structured-content-template.xsl"), LocaleUtil.US);

		return ddmStructure;
	}

	private DDMForm _deserialize(String content) {
		DDMFormDeserializer ddmFormDeserializer =
			_ddmFormDeserializerTracker.getDDMFormDeserializer("json");

		DDMFormDeserializerDeserializeRequest.Builder builder =
			DDMFormDeserializerDeserializeRequest.Builder.newBuilder(content);

		DDMFormDeserializerDeserializeResponse
			ddmFormDeserializerDeserializeResponse =
				ddmFormDeserializer.deserialize(builder.build());

		return ddmFormDeserializerDeserializeResponse.getDDMForm();
	}

	private String _read(String fileName) throws Exception {
		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/" + fileName);

		return StringUtil.read(inputStream);
	}

	private DDMFormDeserializerTracker _ddmFormDeserializerTracker;
	private DDMStructure _ddmStructure;
	private DDMStructure _irrelevantDDMStructure;
	private JournalFolder _irrelevantJournalFolder;
	private JournalFolder _journalFolder;
	private ServiceReference<DDMFormDeserializerTracker> _serviceReference;

}