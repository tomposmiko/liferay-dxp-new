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

package com.liferay.object.admin.rest.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.admin.rest.client.dto.v1_0.ObjectField;
import com.liferay.object.admin.rest.client.pagination.Page;
import com.liferay.object.admin.rest.client.pagination.Pagination;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.util.LocalizedMapUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.Inject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Javier Gamarra
 */
@RunWith(Arquillian.class)
public class ObjectFieldResourceTest extends BaseObjectFieldResourceTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		String value = "A" + RandomTestUtil.randomString();

		_objectDefinition =
			_objectDefinitionLocalService.addCustomObjectDefinition(
				TestPropsValues.getUserId(),
				LocalizedMapUtil.getLocalizedMap(value), value, null, null,
				LocalizedMapUtil.getLocalizedMap(value),
				ObjectDefinitionConstants.SCOPE_COMPANY,
				ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT,
				Collections.<com.liferay.object.model.ObjectField>emptyList());
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();

		if (_objectDefinition != null) {
			_objectDefinitionLocalService.deleteObjectDefinition(
				_objectDefinition.getObjectDefinitionId());
		}
	}

	@Override
	@Test
	public void testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage()
		throws Exception {

		String objectDefinitionExternalReferenceCode =
			testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage_getObjectDefinitionExternalReferenceCode();
		String irrelevantObjectDefinitionExternalReferenceCode =
			testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage_getIrrelevantObjectDefinitionExternalReferenceCode();

		Page<ObjectField> page =
			objectFieldResource.
				getObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage(
					objectDefinitionExternalReferenceCode, null, null,
					Pagination.of(1, 10), null);

		Assert.assertEquals(6, page.getTotalCount());

		if (irrelevantObjectDefinitionExternalReferenceCode != null) {
			ObjectField irrelevantObjectField =
				testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage_addObjectField(
					irrelevantObjectDefinitionExternalReferenceCode,
					randomIrrelevantObjectField());

			page =
				objectFieldResource.
					getObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage(
						irrelevantObjectDefinitionExternalReferenceCode, null,
						null, Pagination.of(1, 10), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantObjectField),
				(List<ObjectField>)page.getItems());
			assertValid(page);
		}

		ObjectField objectField1 =
			testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage_addObjectField(
				objectDefinitionExternalReferenceCode, randomObjectField());
		ObjectField objectField2 =
			testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage_addObjectField(
				objectDefinitionExternalReferenceCode, randomObjectField());

		page =
			objectFieldResource.
				getObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage(
					objectDefinitionExternalReferenceCode, null, null,
					Pagination.of(1, 10), null);

		Assert.assertEquals(8, page.getTotalCount());

		assertContains(objectField1, (List<ObjectField>)page.getItems());
		assertContains(objectField2, (List<ObjectField>)page.getItems());
		assertValid(page);

		objectFieldResource.deleteObjectField(objectField1.getId());
		objectFieldResource.deleteObjectField(objectField2.getId());
	}

	@Override
	@Test
	public void testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPageWithPagination()
		throws Exception {

		String objectDefinitionExternalReferenceCode =
			testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage_getObjectDefinitionExternalReferenceCode();

		Page<ObjectField> totalPage =
			objectFieldResource.
				getObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage(
					objectDefinitionExternalReferenceCode, null, null, null,
					null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		ObjectField objectField1 =
			testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage_addObjectField(
				objectDefinitionExternalReferenceCode, randomObjectField());

		ObjectField objectField2 =
			testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage_addObjectField(
				objectDefinitionExternalReferenceCode, randomObjectField());

		ObjectField objectField3 =
			testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage_addObjectField(
				objectDefinitionExternalReferenceCode, randomObjectField());

		Page<ObjectField> page1 =
			objectFieldResource.
				getObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage(
					objectDefinitionExternalReferenceCode, null, null,
					Pagination.of(1, totalCount + 2), null);

		List<ObjectField> objectFields1 = (List<ObjectField>)page1.getItems();

		Assert.assertEquals(
			objectFields1.toString(), totalCount + 2, objectFields1.size());

		Page<ObjectField> page2 =
			objectFieldResource.
				getObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage(
					objectDefinitionExternalReferenceCode, null, null,
					Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<ObjectField> objectFields2 = (List<ObjectField>)page2.getItems();

		Assert.assertEquals(objectFields2.toString(), 1, objectFields2.size());

		Page<ObjectField> page3 =
			objectFieldResource.
				getObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage(
					objectDefinitionExternalReferenceCode, null, null,
					Pagination.of(1, totalCount + 3), null);

		assertContains(objectField1, (List<ObjectField>)page3.getItems());
		assertContains(objectField2, (List<ObjectField>)page3.getItems());
		assertContains(objectField3, (List<ObjectField>)page3.getItems());
	}

	@Override
	@Test
	public void testGetObjectDefinitionObjectFieldsPage() throws Exception {

		// TODO Fix REST builder

		Long objectDefinitionId =
			testGetObjectDefinitionObjectFieldsPage_getObjectDefinitionId();
		Long irrelevantObjectDefinitionId =
			testGetObjectDefinitionObjectFieldsPage_getIrrelevantObjectDefinitionId();

		Page<ObjectField> page =
			objectFieldResource.getObjectDefinitionObjectFieldsPage(
				objectDefinitionId, null, null, Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		if (irrelevantObjectDefinitionId != null) {
			ObjectField irrelevantObjectField =
				testGetObjectDefinitionObjectFieldsPage_addObjectField(
					irrelevantObjectDefinitionId,
					randomIrrelevantObjectField());

			page = objectFieldResource.getObjectDefinitionObjectFieldsPage(
				irrelevantObjectDefinitionId, null, null, Pagination.of(1, 2),
				null);

			Assert.assertEquals(totalCount + 1, page.getTotalCount());

			assertContains(
				irrelevantObjectField, (List<ObjectField>)page.getItems());

			assertValid(page);
		}

		ObjectField objectField1 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		ObjectField objectField2 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		page = objectFieldResource.getObjectDefinitionObjectFieldsPage(
			objectDefinitionId, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(objectField1, (List<ObjectField>)page.getItems());
		assertContains(objectField2, (List<ObjectField>)page.getItems());
		assertValid(page);

		objectFieldResource.deleteObjectField(objectField1.getId());

		objectFieldResource.deleteObjectField(objectField2.getId());
	}

	@Override
	@Test
	public void testGetObjectDefinitionObjectFieldsPageWithPagination()
		throws Exception {

		// TODO Fix REST builder

		Long objectDefinitionId =
			testGetObjectDefinitionObjectFieldsPage_getObjectDefinitionId();

		Page<ObjectField> totalPage =
			objectFieldResource.getObjectDefinitionObjectFieldsPage(
				objectDefinitionId, null, null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		ObjectField objectField1 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		ObjectField objectField2 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		ObjectField objectField3 =
			testGetObjectDefinitionObjectFieldsPage_addObjectField(
				objectDefinitionId, randomObjectField());

		Page<ObjectField> page1 =
			objectFieldResource.getObjectDefinitionObjectFieldsPage(
				objectDefinitionId, null, null,
				Pagination.of(1, totalCount + 2), null);

		List<ObjectField> objectFields1 = (List<ObjectField>)page1.getItems();

		Assert.assertEquals(
			objectFields1.toString(), totalCount + 2, objectFields1.size());

		Page<ObjectField> page2 =
			objectFieldResource.getObjectDefinitionObjectFieldsPage(
				objectDefinitionId, null, null,
				Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<ObjectField> objectFields2 = (List<ObjectField>)page2.getItems();

		Assert.assertEquals(objectFields2.toString(), 1, objectFields2.size());

		Page<ObjectField> page3 =
			objectFieldResource.getObjectDefinitionObjectFieldsPage(
				objectDefinitionId, null, null,
				Pagination.of(1, totalCount + 3), null);

		assertContains(objectField1, (List<ObjectField>)page3.getItems());
		assertContains(objectField2, (List<ObjectField>)page3.getItems());
		assertContains(objectField3, (List<ObjectField>)page3.getItems());
	}

	@Ignore
	@Override
	@Test
	public void testGraphQLGetObjectFieldNotFound() {
	}

	@Override
	protected String[] getAdditionalAssertFieldNames() {
		return new String[] {"defaultValue", "label", "state"};
	}

	@Override
	protected ObjectField randomObjectField() throws Exception {
		ObjectField objectField = super.randomObjectField();

		objectField.setBusinessType(ObjectField.BusinessType.create("Text"));
		objectField.setDBType(ObjectField.DBType.create("String"));
		objectField.setDefaultValue(StringPool.BLANK);
		objectField.setIndexedAsKeyword(false);
		objectField.setLabel(
			Collections.singletonMap(
				LocaleUtil.US.toString(), "a" + objectField.getName()));
		objectField.setName("a" + objectField.getName());
		objectField.setState(false);

		return objectField;
	}

	@Override
	protected ObjectField testDeleteObjectField_addObjectField()
		throws Exception {

		return _addObjectField();
	}

	@Override
	protected ObjectField
			testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage_addObjectField(
				String objectDefinitionExternalReferenceCode,
				ObjectField objectField)
		throws Exception {

		return objectFieldResource.
			postObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectField(
				objectDefinitionExternalReferenceCode, objectField);
	}

	@Override
	protected String
			testGetObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectFieldsPage_getObjectDefinitionExternalReferenceCode()
		throws Exception {

		return _objectDefinition.getExternalReferenceCode();
	}

	@Override
	protected Long
		testGetObjectDefinitionObjectFieldsPage_getObjectDefinitionId() {

		return _objectDefinition.getObjectDefinitionId();
	}

	@Override
	protected ObjectField testGetObjectField_addObjectField() throws Exception {
		return _addObjectField();
	}

	@Override
	protected ObjectField testGraphQLObjectField_addObjectField()
		throws Exception {

		return _addObjectField();
	}

	@Override
	protected ObjectField testPatchObjectField_addObjectField()
		throws Exception {

		return _addObjectField();
	}

	@Override
	protected ObjectField
			testPostObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectField_addObjectField(
				ObjectField objectField)
		throws Exception {

		return objectFieldResource.
			postObjectDefinitionByExternalReferenceCodeObjectDefinitionExternalReferenceCodeObjectField(
				_objectDefinition.getExternalReferenceCode(), objectField);
	}

	@Override
	protected ObjectField testPutObjectField_addObjectField() throws Exception {
		return _addObjectField();
	}

	private ObjectField _addObjectField() throws Exception {
		_objectField = objectFieldResource.postObjectDefinitionObjectField(
			_objectDefinition.getObjectDefinitionId(), randomObjectField());

		return _objectField;
	}

	private ObjectDefinition _objectDefinition;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	private ObjectField _objectField;

}