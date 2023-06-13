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

package com.liferay.object.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.list.type.entry.util.ListTypeEntryUtil;
import com.liferay.list.type.model.ListTypeDefinition;
import com.liferay.list.type.service.ListTypeDefinitionLocalService;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectRelationshipConstants;
import com.liferay.object.constants.ObjectViewFilterColumnConstants;
import com.liferay.object.exception.DefaultObjectViewException;
import com.liferay.object.exception.ObjectDefinitionModifiableException;
import com.liferay.object.exception.ObjectViewColumnFieldNameException;
import com.liferay.object.exception.ObjectViewFilterColumnException;
import com.liferay.object.exception.ObjectViewSortColumnException;
import com.liferay.object.field.util.ObjectFieldUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.model.ObjectView;
import com.liferay.object.model.ObjectViewColumn;
import com.liferay.object.model.ObjectViewFilterColumn;
import com.liferay.object.model.ObjectViewSortColumn;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.object.service.ObjectViewLocalService;
import com.liferay.object.service.persistence.ObjectViewColumnPersistence;
import com.liferay.object.service.persistence.ObjectViewFilterColumnPersistence;
import com.liferay.object.service.persistence.ObjectViewSortColumnPersistence;
import com.liferay.object.service.test.util.ObjectDefinitionTestUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.FeatureFlags;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Gabriel Albuquerque
 */
@FeatureFlags("LPS-167253")
@RunWith(Arquillian.class)
public class ObjectViewLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_objectDefinition = _addObjectDefinition();
	}

	@Test
	public void testAddObjectView() throws Exception {
		_objectViewLocalService.addObjectView(
			TestPropsValues.getUserId(),
			_objectDefinition.getObjectDefinitionId(), true,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			Arrays.asList(_createObjectViewColumn("Able", "able")),
			Collections.emptyList(), Collections.emptyList());

		_assertFailureAddOrUpdateObjectView(
			DefaultObjectViewException.class, true,
			"There can only be one default object view", null,
			Collections.emptyList(), Collections.emptyList(),
			Collections.emptyList());

		_objectDefinitionLocalService.deleteObjectDefinition(
			_objectDefinition.getObjectDefinitionId());

		_objectDefinition =
			ObjectDefinitionTestUtil.addUnmodifiableSystemObjectDefinition(
				TestPropsValues.getUserId(), "Test", null,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				"Test", null, null,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				ObjectDefinitionConstants.SCOPE_COMPANY, null, 1,
				_objectDefinitionLocalService,
				Arrays.asList(
					ObjectFieldUtil.createObjectField(
						ObjectFieldConstants.BUSINESS_TYPE_TEXT,
						ObjectFieldConstants.DB_TYPE_STRING,
						RandomTestUtil.randomString(), StringUtil.randomId())));

		_assertFailureAddOrUpdateObjectView(
			ObjectDefinitionModifiableException.class, true,
			"A modifiable object definition is required", null, null,
			Collections.emptyList(), null);

		_objectDefinitionLocalService.deleteObjectDefinition(
			_objectDefinition.getObjectDefinitionId());

		_objectDefinition = _addObjectDefinition();

		_objectViewLocalService.addObjectView(
			TestPropsValues.getUserId(),
			_objectDefinition.getObjectDefinitionId(), true,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			Arrays.asList(
				_createObjectViewColumn("Able", "able"),
				_createObjectViewColumn("Baker", "baker")),
			Arrays.asList(
				_createObjectViewFilterColumn(
					ObjectViewFilterColumnConstants.FILTER_TYPE_INCLUDES,
					"{\"includes\": [\"brazil\"]}", "country"),
				_createObjectViewFilterColumn(null, null, "createDate")),
			Arrays.asList(
				_createObjectViewSortColumn("able", "asc"),
				_createObjectViewSortColumn("baker", "asc")));

		_assertFailureAddOrUpdateObjectView(
			ObjectViewColumnFieldNameException.class, false,
			"There is no object field with the name: zebra", null,
			Arrays.asList(
				_createObjectViewColumnWithNonexistentObjectFieldName()),
			Collections.emptyList(), Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewColumnFieldNameException.class, false,
			"There is already an object view column with the object field " +
				"name: roger",
			null, _createObjectViewColumnsWithDuplicateObjectFieldName(),
			Collections.emptyList(), Collections.emptyList());

		_objectViewLocalService.addObjectView(
			TestPropsValues.getUserId(),
			_objectDefinition.getObjectDefinitionId(), false,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			_createObjectViewColumnsWithoutLabel(), Collections.emptyList(),
			Collections.emptyList());

		_assertFailureAddOrUpdateObjectView(
			ObjectViewFilterColumnException.class, false,
			"Object field name is null", null, Collections.emptyList(),
			Arrays.asList(_createObjectViewFilterColumn(null, null, null)),
			Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewFilterColumnException.class, false,
			"Object field name \"creator\" is not filterable", null,
			Collections.emptyList(),
			Arrays.asList(_createObjectViewFilterColumn(null, null, "creator")),
			Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewFilterColumnException.class, false,
			"Object field name \"country\" needs to have the filter type and " +
				"JSON specified",
			null, Collections.emptyList(),
			Arrays.asList(
				_createObjectViewFilterColumn(
					null, RandomTestUtil.randomString(), "country")),
			Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewFilterColumnException.class, false,
			"Object field name \"country\" needs to have the filter type and " +
				"JSON specified",
			null, Collections.emptyList(),
			Arrays.asList(
				_createObjectViewFilterColumn(
					RandomTestUtil.randomString(), null, "country")),
			Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewFilterColumnException.class, false,
			"Object field name \"name\" is not filterable", null,
			Collections.emptyList(),
			Arrays.asList(_createObjectViewFilterColumn(null, null, "name")),
			Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewSortColumnException.class, false,
			"There is no object view column with the name: zulu", null,
			Arrays.asList(_createObjectViewColumn("Item", "item")),
			Collections.emptyList(),
			Arrays.asList(
				_createObjectViewSortColumnWithWrongObjectFieldName()));
		_assertFailureAddOrUpdateObjectView(
			ObjectViewSortColumnException.class, false,
			"There is no sort order of type: zulu", null,
			Arrays.asList(_createObjectViewColumn("King", "king")),
			Collections.emptyList(),
			Arrays.asList(_createObjectViewSortColumn("king", "zulu")));

		_deleteObjectFields();

		_testAddObjectViewRelationshipFilterColumn();

		_objectDefinitionLocalService.deleteObjectDefinition(
			_objectDefinition.getObjectDefinitionId());

		_objectDefinition =
			ObjectDefinitionTestUtil.addModifiableSystemObjectDefinition(
				TestPropsValues.getUserId(), null,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				"Test", null, null,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				ObjectDefinitionConstants.SCOPE_SITE, null, 1,
				_objectDefinitionLocalService,
				Arrays.asList(
					ObjectFieldUtil.createObjectField(
						ObjectFieldConstants.BUSINESS_TYPE_TEXT,
						ObjectFieldConstants.DB_TYPE_STRING,
						RandomTestUtil.randomString(), StringUtil.randomId())));

		ObjectView objectView = _addObjectView();

		_assertObjectView(objectView);

		_deleteObjectFields();

		_objectViewLocalService.deleteObjectView(objectView.getObjectViewId());
	}

	@Test
	public void testGetObjectView() throws Exception {
		ObjectView objectView = _addObjectView();

		objectView = _objectViewLocalService.getObjectView(
			objectView.getObjectViewId());

		_assertObjectView(objectView);

		_deleteObjectFields();

		_objectViewLocalService.deleteObjectView(objectView.getObjectViewId());
	}

	@Test
	public void testUpdateObjectView() throws Exception {
		ObjectView objectView = _addObjectView();

		objectView = _objectViewLocalService.updateObjectView(
			objectView.getObjectViewId(), objectView.isDefaultObjectView(),
			objectView.getNameMap(),
			Collections.singletonList(_createObjectViewColumn("Fox", "fox")),
			Collections.emptyList(),
			Collections.singletonList(
				_createObjectViewSortColumn("fox", "desc")));

		List<ObjectViewColumn> objectViewColumns =
			objectView.getObjectViewColumns();

		Assert.assertEquals(
			objectViewColumns.toString(), 1, objectViewColumns.size());

		List<ObjectViewSortColumn> objectViewSortColumns =
			objectView.getObjectViewSortColumns();

		Assert.assertEquals(
			objectViewSortColumns.toString(), 1, objectViewSortColumns.size());

		_assertFailureAddOrUpdateObjectView(
			ObjectViewColumnFieldNameException.class,
			objectView.isDefaultObjectView(),
			"There is already an object view column with the object field " +
				"name: roger",
			objectView, _createObjectViewColumnsWithDuplicateObjectFieldName(),
			Collections.emptyList(), Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewColumnFieldNameException.class,
			objectView.isDefaultObjectView(),
			"There is no object field with the name: zebra", objectView,
			Collections.singletonList(
				_createObjectViewColumnWithNonexistentObjectFieldName()),
			Collections.emptyList(), Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewFilterColumnException.class,
			objectView.isDefaultObjectView(), "Object field name is null",
			objectView, Collections.emptyList(),
			Arrays.asList(_createObjectViewFilterColumn(null, null, null)),
			Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewFilterColumnException.class,
			objectView.isDefaultObjectView(),
			"Object field name \"creator\" is not filterable", objectView,
			Collections.emptyList(),
			Arrays.asList(_createObjectViewFilterColumn(null, null, "creator")),
			Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewFilterColumnException.class,
			objectView.isDefaultObjectView(),
			"Object field name \"country\" needs to have the filter type and " +
				"JSON specified",
			objectView, Collections.emptyList(),
			Arrays.asList(
				_createObjectViewFilterColumn(
					null, RandomTestUtil.randomString(), "country")),
			Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewFilterColumnException.class,
			objectView.isDefaultObjectView(),
			"Object field name \"country\" needs to have the filter type and " +
				"JSON specified",
			objectView, Collections.emptyList(),
			Arrays.asList(
				_createObjectViewFilterColumn(
					RandomTestUtil.randomString(), null, "country")),
			Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewFilterColumnException.class,
			objectView.isDefaultObjectView(),
			"Object field name \"name\" is not filterable", objectView,
			Collections.emptyList(),
			Arrays.asList(_createObjectViewFilterColumn(null, null, "name")),
			Collections.emptyList());
		_assertFailureAddOrUpdateObjectView(
			ObjectViewSortColumnException.class,
			objectView.isDefaultObjectView(),
			"There is no object view column with the name: king", objectView,
			Collections.singletonList(_createObjectViewColumn("Jig", "jig")),
			Collections.emptyList(),
			Collections.singletonList(
				_createObjectViewSortColumn("king", "desc")));
		_assertFailureAddOrUpdateObjectView(
			ObjectViewSortColumnException.class,
			objectView.isDefaultObjectView(),
			"There is no sort order of type: zulu", objectView,
			Collections.singletonList(_createObjectViewColumn("Love", "love")),
			Collections.emptyList(),
			Collections.singletonList(
				_createObjectViewSortColumn("love", "zulu")));

		objectView = _objectViewLocalService.updateObjectView(
			objectView.getObjectViewId(), objectView.isDefaultObjectView(),
			objectView.getNameMap(), Collections.emptyList(),
			Collections.emptyList(), Collections.emptyList());

		objectViewColumns = objectView.getObjectViewColumns();

		Assert.assertEquals(
			objectViewColumns.toString(), 0, objectViewColumns.size());

		objectViewSortColumns = objectView.getObjectViewSortColumns();

		Assert.assertEquals(
			objectViewSortColumns.toString(), 0, objectViewSortColumns.size());

		_objectViewLocalService.updateObjectView(
			objectView.getObjectViewId(), objectView.isDefaultObjectView(),
			objectView.getNameMap(), _createObjectViewColumnsWithoutLabel(),
			Collections.emptyList(), Collections.emptyList());

		_deleteObjectFields();

		_objectViewLocalService.deleteObjectView(objectView.getObjectViewId());
	}

	private ObjectDefinition _addObjectDefinition() throws Exception {
		ListTypeDefinition listTypeDefinition =
			_listTypeDefinitionLocalService.addListTypeDefinition(
				null, TestPropsValues.getUserId(),
				Collections.singletonMap(LocaleUtil.US, "Countries"),
				Collections.singletonList(
					ListTypeEntryUtil.createListTypeEntry(
						StringUtil.randomId(),
						Collections.singletonMap(LocaleUtil.US, "Brazil"))));

		ObjectField objectField = ObjectFieldUtil.createObjectField(
			ObjectFieldConstants.BUSINESS_TYPE_PICKLIST,
			ObjectFieldConstants.DB_TYPE_STRING, "country");

		objectField.setListTypeDefinitionId(
			listTypeDefinition.getListTypeDefinitionId());

		return ObjectDefinitionTestUtil.addObjectDefinition(
			_objectDefinitionLocalService,
			Arrays.asList(
				objectField,
				ObjectFieldUtil.createObjectField(
					ObjectFieldConstants.BUSINESS_TYPE_TEXT,
					ObjectFieldConstants.DB_TYPE_STRING, "name")));
	}

	private String _addObjectField(
			String objectFieldLabel, String objectFieldName)
		throws Exception {

		ObjectField objectField = _objectFieldLocalService.addCustomObjectField(
			null, TestPropsValues.getUserId(), 0,
			_objectDefinition.getObjectDefinitionId(),
			ObjectFieldConstants.BUSINESS_TYPE_TEXT,
			ObjectFieldConstants.DB_TYPE_STRING, false, false, null,
			LocalizedMapUtil.getLocalizedMap(objectFieldLabel), false,
			objectFieldName, true, false, Collections.emptyList());

		return objectField.getName();
	}

	private ObjectView _addObjectView() throws Exception {
		ListTypeDefinition listTypeDefinition =
			_listTypeDefinitionLocalService.addListTypeDefinition(
				null, TestPropsValues.getUserId(),
				Collections.singletonMap(LocaleUtil.US, "Countries"),
				Collections.singletonList(
					ListTypeEntryUtil.createListTypeEntry(
						StringUtil.randomId(),
						Collections.singletonMap(LocaleUtil.US, "Brazil"))));

		ObjectField objectField = ObjectFieldUtil.createObjectField(
			ObjectFieldConstants.BUSINESS_TYPE_PICKLIST,
			ObjectFieldConstants.DB_TYPE_STRING, "country");

		objectField.setListTypeDefinitionId(
			listTypeDefinition.getListTypeDefinitionId());
		objectField.setObjectDefinitionId(
			_objectDefinition.getObjectDefinitionId());

		_objectFieldLocalService.addObjectField(objectField);

		return _objectViewLocalService.addObjectView(
			TestPropsValues.getUserId(),
			_objectDefinition.getObjectDefinitionId(), true,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			Arrays.asList(
				_createObjectViewColumn("Able", "able"),
				_createObjectViewColumn("Baker", "baker")),
			Arrays.asList(
				_createObjectViewFilterColumn(
					ObjectViewFilterColumnConstants.FILTER_TYPE_EXCLUDES,
					"{\"excludes\": [3, 4]}", "status"),
				_createObjectViewFilterColumn(
					ObjectViewFilterColumnConstants.FILTER_TYPE_INCLUDES,
					"{\"includes\": [\"brazil\"]}", "country"),
				_createObjectViewFilterColumn(null, null, "createDate")),
			Arrays.asList(
				_createObjectViewSortColumn("able", "asc"),
				_createObjectViewSortColumn("baker", "asc")));
	}

	private void _assertFailureAddOrUpdateObjectView(
		Class<?> clazz, boolean defaultObjectView, String message,
		ObjectView objectView, List<ObjectViewColumn> objectViewColumns,
		List<ObjectViewFilterColumn> objectViewFilterColumns,
		List<ObjectViewSortColumn> objectViewSortColumns) {

		try {
			if (objectView != null) {
				_objectViewLocalService.updateObjectView(
					objectView.getObjectViewId(), defaultObjectView,
					objectView.getNameMap(), objectViewColumns,
					objectViewFilterColumns, objectViewSortColumns);
			}
			else {
				_objectViewLocalService.addObjectView(
					TestPropsValues.getUserId(),
					_objectDefinition.getObjectDefinitionId(),
					defaultObjectView,
					LocalizedMapUtil.getLocalizedMap(
						RandomTestUtil.randomString()),
					objectViewColumns, objectViewFilterColumns,
					objectViewSortColumns);
			}

			Assert.fail();
		}
		catch (PortalException portalException) {
			Assert.assertTrue(clazz.isInstance(portalException));
			Assert.assertEquals(message, portalException.getMessage());
		}
	}

	private void _assertObjectView(ObjectView objectView) {
		List<ObjectViewColumn> objectViewColumns =
			objectView.getObjectViewColumns();

		Assert.assertEquals(
			objectViewColumns.toString(), 2, objectViewColumns.size());

		List<ObjectViewFilterColumn> objectViewFilterColumns =
			objectView.getObjectViewFilterColumns();

		Assert.assertEquals(
			objectViewFilterColumns.toString(), 3,
			objectViewFilterColumns.size());

		List<ObjectViewSortColumn> objectViewSortColumns =
			objectView.getObjectViewSortColumns();

		Assert.assertEquals(
			objectViewSortColumns.toString(), 2, objectViewSortColumns.size());
	}

	private ObjectRelationship _createObjectRelationship(
			long objectDefinitionId1, long objectDefinitionId2)
		throws Exception {

		return _objectRelationshipLocalService.addObjectRelationship(
			TestPropsValues.getUserId(), objectDefinitionId1,
			objectDefinitionId2, 0,
			ObjectRelationshipConstants.DELETION_TYPE_PREVENT,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			StringUtil.randomId(),
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY);
	}

	private ObjectViewColumn _createObjectViewColumn(
			String objectFieldLabel, String objectFieldName)
		throws Exception {

		ObjectViewColumn objectViewColumn = _objectViewColumnPersistence.create(
			0);

		objectViewColumn.setLabelMap(
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()));
		objectViewColumn.setObjectFieldName(
			_addObjectField(objectFieldLabel, objectFieldName));
		objectViewColumn.setPriority(0);

		return objectViewColumn;
	}

	private List<ObjectViewColumn>
			_createObjectViewColumnsWithDuplicateObjectFieldName()
		throws Exception {

		ObjectViewColumn objectViewColumn1 = _createObjectViewColumn(
			"Roger", "roger");

		ObjectViewColumn objectViewColumn2 = _createObjectViewColumn(
			RandomTestUtil.randomString(),
			StringUtil.toLowerCase(RandomStringUtils.randomAlphabetic(5)));

		objectViewColumn2.setObjectFieldName("roger");

		return ListUtil.fromArray(objectViewColumn1, objectViewColumn2);
	}

	private List<ObjectViewColumn> _createObjectViewColumnsWithoutLabel()
		throws Exception {

		ObjectViewColumn objectViewColumn = _createObjectViewColumn(
			RandomTestUtil.randomString(),
			StringUtil.toLowerCase(RandomStringUtils.randomAlphabetic(5)));

		objectViewColumn.setLabelMap(LocalizedMapUtil.getLocalizedMap(""));

		return ListUtil.fromArray(objectViewColumn);
	}

	private ObjectViewColumn
			_createObjectViewColumnWithNonexistentObjectFieldName()
		throws Exception {

		ObjectViewColumn objectViewColumn = _createObjectViewColumn(
			RandomTestUtil.randomString(),
			StringUtil.toLowerCase(RandomStringUtils.randomAlphabetic(5)));

		objectViewColumn.setObjectFieldName("zebra");

		return objectViewColumn;
	}

	private ObjectViewFilterColumn _createObjectViewFilterColumn(
		String filterType, String json, String objectFieldName) {

		ObjectViewFilterColumn objectViewFilterColumn =
			_objectViewFilterColumnPersistence.create(0);

		objectViewFilterColumn.setFilterType(filterType);
		objectViewFilterColumn.setJSON(json);
		objectViewFilterColumn.setObjectFieldName(objectFieldName);

		return objectViewFilterColumn;
	}

	private ObjectViewSortColumn _createObjectViewSortColumn(
		String objectFieldName, String sortOrder) {

		ObjectViewSortColumn objectViewSortColumn =
			_objectViewSortColumnPersistence.create(0);

		objectViewSortColumn.setObjectFieldName(objectFieldName);
		objectViewSortColumn.setPriority(0);
		objectViewSortColumn.setSortOrder(sortOrder);

		return objectViewSortColumn;
	}

	private ObjectViewSortColumn
		_createObjectViewSortColumnWithWrongObjectFieldName() {

		ObjectViewSortColumn objectViewSortColumn = _createObjectViewSortColumn(
			"item", "asc");

		objectViewSortColumn.setObjectFieldName("zulu");

		return objectViewSortColumn;
	}

	private void _deleteObjectFields() throws Exception {
		List<ObjectField> objectFields =
			_objectFieldLocalService.getObjectFields(
				_objectDefinition.getObjectDefinitionId(), false);

		for (ObjectField objectField : objectFields) {
			_objectFieldLocalService.deleteObjectField(objectField);
		}
	}

	private void _testAddObjectViewRelationshipFilterColumn() throws Exception {
		String externalReferenceCode = RandomTestUtil.randomString();

		ObjectDefinition objectDefinition1 =
			ObjectDefinitionTestUtil.addObjectDefinition(
				_objectDefinitionLocalService,
				Arrays.asList(
					ObjectFieldUtil.createObjectField(
						ObjectFieldConstants.BUSINESS_TYPE_TEXT,
						ObjectFieldConstants.DB_TYPE_STRING, "title")));

		ObjectRelationship objectRelationship = _createObjectRelationship(
			objectDefinition1.getObjectDefinitionId(),
			_objectDefinition.getObjectDefinitionId());

		ObjectField objectField = _objectFieldLocalService.getObjectField(
			objectRelationship.getObjectFieldId2());

		try {
			_objectViewLocalService.addObjectView(
				TestPropsValues.getUserId(),
				_objectDefinition.getObjectDefinitionId(), false,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				Arrays.asList(_createObjectViewColumn("Golf", "golf")),
				Arrays.asList(
					_createObjectViewFilterColumn(
						ObjectViewFilterColumnConstants.FILTER_TYPE_INCLUDES,
						StringBundler.concat(
							"{\"includes\": [\"", externalReferenceCode,
							"\"]}"),
						objectField.getName())),
				Collections.emptyList());
		}
		catch (ObjectViewFilterColumnException
					objectViewFilterColumnException) {

			Assert.assertEquals(
				StringBundler.concat(
					"No ", objectDefinition1.getShortName(),
					" exists with the external reference code ",
					externalReferenceCode),
				objectViewFilterColumnException.getMessage());
		}

		_objectDefinitionLocalService.publishCustomObjectDefinition(
			TestPropsValues.getUserId(),
			objectDefinition1.getObjectDefinitionId());

		ObjectEntry objectEntry1 = _objectEntryLocalService.addObjectEntry(
			TestPropsValues.getUserId(), 0,
			objectDefinition1.getObjectDefinitionId(),
			HashMapBuilder.<String, Serializable>put(
				"title", "Roger"
			).build(),
			ServiceContextTestUtil.getServiceContext());

		_objectViewLocalService.addObjectView(
			TestPropsValues.getUserId(),
			_objectDefinition.getObjectDefinitionId(), false,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			Arrays.asList(_createObjectViewColumn("How", "how")),
			Arrays.asList(
				_createObjectViewFilterColumn(
					ObjectViewFilterColumnConstants.FILTER_TYPE_INCLUDES,
					StringBundler.concat(
						"{\"includes\": [\"",
						objectEntry1.getExternalReferenceCode(), "\"]}"),
					objectField.getName())),
			Collections.emptyList());

		_objectRelationshipLocalService.deleteObjectRelationship(
			objectRelationship.getObjectRelationshipId());

		ObjectDefinition systemObjectDefinition = null;

		for (ObjectDefinition objectDefinition :
				_objectDefinitionLocalService.getSystemObjectDefinitions()) {

			if (StringUtil.equals(objectDefinition.getName(), "User")) {
				systemObjectDefinition = objectDefinition;

				break;
			}
		}

		Assert.assertNotNull(systemObjectDefinition);

		objectRelationship = _createObjectRelationship(
			systemObjectDefinition.getObjectDefinitionId(),
			_objectDefinition.getObjectDefinitionId());

		objectField = _objectFieldLocalService.getObjectField(
			objectRelationship.getObjectFieldId2());

		long randomId = RandomTestUtil.randomLong();

		try {
			_objectViewLocalService.addObjectView(
				TestPropsValues.getUserId(),
				_objectDefinition.getObjectDefinitionId(), false,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				Arrays.asList(_createObjectViewColumn("India", "india")),
				Arrays.asList(
					_createObjectViewFilterColumn(
						ObjectViewFilterColumnConstants.FILTER_TYPE_INCLUDES,
						StringBundler.concat(
							"{\"includes\": [\"", randomId, "\"]}"),
						objectField.getName())),
				Collections.emptyList());
		}
		catch (ObjectViewFilterColumnException
					objectViewFilterColumnException) {

			Assert.assertEquals(
				"No User exists with the primary key " + randomId,
				objectViewFilterColumnException.getMessage());
		}

		long[] userIds = new long[2];

		for (int i = 0; i < 2; i++) {
			User user = UserTestUtil.addUser();

			userIds[i] = user.getUserId();
		}

		_objectViewLocalService.addObjectView(
			TestPropsValues.getUserId(),
			_objectDefinition.getObjectDefinitionId(), false,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			Arrays.asList(_createObjectViewColumn("York", "york")),
			Arrays.asList(
				_createObjectViewFilterColumn(
					ObjectViewFilterColumnConstants.FILTER_TYPE_INCLUDES,
					StringBundler.concat(
						"{\"includes\": [\"", userIds[0], "\",\"", userIds[1],
						"\"]}"),
					objectField.getName())),
			Collections.emptyList());

		_objectRelationshipLocalService.deleteObjectRelationship(
			objectRelationship.getObjectRelationshipId());

		_objectDefinitionLocalService.deleteObjectDefinition(objectDefinition1);
	}

	@Inject
	private ListTypeDefinitionLocalService _listTypeDefinitionLocalService;

	@DeleteAfterTestRun
	private ObjectDefinition _objectDefinition;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Inject
	private ObjectEntryLocalService _objectEntryLocalService;

	@Inject
	private ObjectFieldLocalService _objectFieldLocalService;

	@Inject
	private ObjectRelationshipLocalService _objectRelationshipLocalService;

	@Inject
	private ObjectViewColumnPersistence _objectViewColumnPersistence;

	@Inject
	private ObjectViewFilterColumnPersistence
		_objectViewFilterColumnPersistence;

	@Inject
	private ObjectViewLocalService _objectViewLocalService;

	@Inject
	private ObjectViewSortColumnPersistence _objectViewSortColumnPersistence;

}