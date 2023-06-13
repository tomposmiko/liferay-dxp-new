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

package com.liferay.object.rest.internal.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.constants.ObjectRelationshipConstants;
import com.liferay.object.field.util.ObjectFieldUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.rest.internal.resource.v1_0.test.util.HTTPTestUtil;
import com.liferay.object.rest.internal.resource.v1_0.test.util.ObjectDefinitionTestUtil;
import com.liferay.object.rest.internal.resource.v1_0.test.util.ObjectEntryTestUtil;
import com.liferay.object.rest.internal.resource.v1_0.test.util.ObjectRelationshipTestUtil;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectRelationshipLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.util.PropsUtil;

import java.util.Collections;

import javax.ws.rs.NotSupportedException;

import org.hamcrest.CoreMatchers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Luis Miguel Barcos
 */
@RunWith(Arquillian.class)
public class ObjectEntryResourceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@BeforeClass
	public static void setUpClass() throws Exception {
		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-153117", "true"
			).build());
		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-164801", "true"
			).build());
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-153117", "false"
			).build());
		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-164801", "false"
			).build());
	}

	@Before
	public void setUp() throws Exception {
		_objectDefinition1 = ObjectDefinitionTestUtil.publishObjectDefinition(
			Collections.singletonList(
				ObjectFieldUtil.createObjectField(
					"Text", "String", true, true, null,
					RandomTestUtil.randomString(), _OBJECT_FIELD_NAME_1,
					false)));

		_objectEntry1 = ObjectEntryTestUtil.addObjectEntry(
			_objectDefinition1, _OBJECT_FIELD_NAME_1, _OBJECT_FIELD_VALUE_1);

		_objectDefinition2 = ObjectDefinitionTestUtil.publishObjectDefinition(
			Collections.singletonList(
				ObjectFieldUtil.createObjectField(
					"Text", "String", true, true, null,
					RandomTestUtil.randomString(), _OBJECT_FIELD_NAME_2,
					false)));

		_objectEntry2 = ObjectEntryTestUtil.addObjectEntry(
			_objectDefinition2, _OBJECT_FIELD_NAME_2, _OBJECT_FIELD_VALUE_2);
	}

	@After
	public void tearDown() throws Exception {
		_objectRelationshipLocalService.deleteObjectRelationship(
			_objectRelationship);

		_objectDefinitionLocalService.deleteObjectDefinition(
			_objectDefinition1);
		_objectDefinitionLocalService.deleteObjectDefinition(
			_objectDefinition2);
	}

	@Test
	public void testFilterObjectEntriesByRelatedObjectEntries()
		throws Exception {

		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-154672", "true"
			).build());

		for (FilterOperator filterOperator : FilterOperator.values()) {
			_testFilterObjectEntriesByRelatedObjectEntries(filterOperator);
		}

		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-154672", "false"
			).build());
	}

	@Test
	public void testGetNestedFieldDetailsInOneToManyRelationships()
		throws Exception {

		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-161364", "true"
			).build());

		_objectRelationship = _addObjectRelationshipAndRelateObjectsEntries(
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY);

		_testGetNestedFieldDetailsInOneToManyRelationships(
			StringBundler.concat(
				_objectDefinition2.getRESTContextPath(), "?nestedFields=r_",
				_objectRelationship.getName(), "_",
				_objectDefinition1.getPKObjectFieldName()),
			StringBundler.concat(
				"r_", _objectRelationship.getName(), "_",
				StringUtil.replaceLast(
					_objectDefinition1.getPKObjectFieldName(), "Id", "")));

		_testGetNestedFieldDetailsInOneToManyRelationships(
			StringBundler.concat(
				_objectDefinition2.getRESTContextPath(), "?nestedFields=",
				_objectRelationship.getName()),
			_objectRelationship.getName());

		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-161364", "false"
			).build());
	}

	@Test
	public void testGetObjectRelationshipERCFieldNameInOneToManyRelationship()
		throws Exception {

		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-161364", "true"
			).build());

		_objectRelationship = _addObjectRelationshipAndRelateObjectsEntries(
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY);

		JSONObject jsonObject = HTTPTestUtil.invoke(
			null, _objectDefinition2.getRESTContextPath(), Http.Method.GET);

		JSONArray itemsJSONArray = jsonObject.getJSONArray("items");

		Assert.assertEquals(1, itemsJSONArray.length());

		JSONObject itemJSONObject = itemsJSONArray.getJSONObject(0);

		Assert.assertEquals(
			itemJSONObject.getString(_objectRelationship.getName() + "ERC"),
			_objectEntry1.getExternalReferenceCode());

		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-161364", "false"
			).build());
	}

	@Test
	public void testGetObjectRelationshipERCFieldNameInOneToManyRelationshipFromRelatedObjectEntry()
		throws Exception {

		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-161364", "true"
			).build());

		_objectRelationship = _addObjectRelationshipAndRelateObjectsEntries(
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY);

		JSONObject jsonObject = HTTPTestUtil.invoke(
			null,
			StringBundler.concat(
				_objectDefinition1.getRESTContextPath(), "?nestedFields=",
				_objectRelationship.getName()),
			Http.Method.GET);

		JSONArray itemsJSONArray = jsonObject.getJSONArray("items");

		Assert.assertEquals(1, itemsJSONArray.length());

		JSONObject itemJSONObject = itemsJSONArray.getJSONObject(0);

		JSONArray relationshipJSONArray = itemJSONObject.getJSONArray(
			_objectRelationship.getName());

		Assert.assertEquals(1, relationshipJSONArray.length());

		JSONObject relatedObjectEntryJSONObject =
			relationshipJSONArray.getJSONObject(0);

		Assert.assertEquals(
			relatedObjectEntryJSONObject.getString(
				_objectRelationship.getName() + "ERC"),
			_objectEntry1.getExternalReferenceCode());

		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-161364", "false"
			).build());
	}

	@Test
	public void testPostCustomObjectEntryWithNestedCustomObjectEntriesInManyToManyRelationship()
		throws Exception {

		_objectRelationship = ObjectRelationshipTestUtil.addObjectRelationship(
			_objectDefinition1, _objectDefinition2, TestPropsValues.getUserId(),
			ObjectRelationshipConstants.TYPE_MANY_TO_MANY);

		JSONObject objectEntryJSONObject = JSONUtil.put(
			_objectRelationship.getName(),
			_createObjectEntriesJSONArray(
				_OBJECT_FIELD_NAME_2,
				new String[] {
					_NEW_OBJECT_FIELD_VALUE_1, _NEW_OBJECT_FIELD_VALUE_2
				}));

		JSONObject jsonObject = HTTPTestUtil.invoke(
			objectEntryJSONObject.toString(),
			_objectDefinition1.getRESTContextPath(), Http.Method.POST);

		Assert.assertEquals(
			0,
			jsonObject.getJSONObject(
				"status"
			).get(
				"code"
			));

		String objectEntryId = jsonObject.getString("id");

		jsonObject = HTTPTestUtil.invoke(
			null,
			StringBundler.concat(
				_objectDefinition1.getRESTContextPath(), StringPool.SLASH,
				objectEntryId, "?nestedFields=", _objectRelationship.getName()),
			Http.Method.GET);

		JSONArray nestedObjectEntriesJSONArray = jsonObject.getJSONArray(
			_objectRelationship.getName());

		Assert.assertEquals(2, nestedObjectEntriesJSONArray.length());

		_assertObjectEntryField(
			(JSONObject)nestedObjectEntriesJSONArray.get(0),
			_OBJECT_FIELD_NAME_2, _NEW_OBJECT_FIELD_VALUE_1);
		_assertObjectEntryField(
			(JSONObject)nestedObjectEntriesJSONArray.get(1),
			_OBJECT_FIELD_NAME_2, _NEW_OBJECT_FIELD_VALUE_2);
	}

	@Test
	public void testPostCustomObjectEntryWithNestedCustomObjectEntriesInManyToOneRelationship()
		throws Exception {

		_objectRelationship = ObjectRelationshipTestUtil.addObjectRelationship(
			_objectDefinition1, _objectDefinition2, TestPropsValues.getUserId(),
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY);

		JSONObject objectEntryJSONObject = JSONUtil.put(
			_objectRelationship.getName(),
			JSONFactoryUtil.createJSONObject(
				JSONUtil.put(
					_OBJECT_FIELD_NAME_1, _NEW_OBJECT_FIELD_VALUE_1
				).toString()));

		JSONObject jsonObject = HTTPTestUtil.invoke(
			objectEntryJSONObject.toString(),
			_objectDefinition2.getRESTContextPath(), Http.Method.POST);

		Assert.assertEquals(
			0,
			jsonObject.getJSONObject(
				"status"
			).get(
				"code"
			));

		String objectEntryId = jsonObject.getString("id");

		jsonObject = HTTPTestUtil.invoke(
			null,
			StringBundler.concat(
				_objectDefinition2.getRESTContextPath(), StringPool.SLASH,
				objectEntryId, "?nestedFields=",
				StringBundler.concat(
					"r_", _objectRelationship.getName(), "_",
					StringUtil.replaceLast(
						_objectDefinition1.getPKObjectFieldName(), "Id", ""))),
			Http.Method.GET);

		_assertObjectEntryField(
			jsonObject.getJSONObject(
				StringBundler.concat(
					"r_", _objectRelationship.getName(), "_",
					StringUtil.replaceLast(
						_objectDefinition1.getPKObjectFieldName(), "Id", ""))),
			_OBJECT_FIELD_NAME_1, _NEW_OBJECT_FIELD_VALUE_1);
	}

	@Test
	public void testPostCustomObjectEntryWithNestedCustomObjectEntriesInOneToManyRelationship()
		throws Exception {

		_objectRelationship = ObjectRelationshipTestUtil.addObjectRelationship(
			_objectDefinition1, _objectDefinition2, TestPropsValues.getUserId(),
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY);

		JSONObject objectEntryJSONObject = JSONUtil.put(
			_objectRelationship.getName(),
			_createObjectEntriesJSONArray(
				_OBJECT_FIELD_NAME_2,
				new String[] {
					_NEW_OBJECT_FIELD_VALUE_1, _NEW_OBJECT_FIELD_VALUE_2
				}));

		JSONObject jsonObject = HTTPTestUtil.invoke(
			objectEntryJSONObject.toString(),
			_objectDefinition1.getRESTContextPath(), Http.Method.POST);

		Assert.assertEquals(
			0,
			jsonObject.getJSONObject(
				"status"
			).get(
				"code"
			));

		String objectEntryId = jsonObject.getString("id");

		jsonObject = HTTPTestUtil.invoke(
			null,
			StringBundler.concat(
				_objectDefinition1.getRESTContextPath(), StringPool.SLASH,
				objectEntryId, "?nestedFields=", _objectRelationship.getName()),
			Http.Method.GET);

		JSONArray nestedObjectEntriesJSONArray = jsonObject.getJSONArray(
			_objectRelationship.getName());

		Assert.assertEquals(2, nestedObjectEntriesJSONArray.length());

		_assertObjectEntryField(
			(JSONObject)nestedObjectEntriesJSONArray.get(0),
			_OBJECT_FIELD_NAME_2, _NEW_OBJECT_FIELD_VALUE_1);
		_assertObjectEntryField(
			(JSONObject)nestedObjectEntriesJSONArray.get(1),
			_OBJECT_FIELD_NAME_2, _NEW_OBJECT_FIELD_VALUE_2);
	}

	@Test
	public void testPutByExternalReferenceCodeManyToManyRelationship()
		throws Exception {

		_objectRelationship = ObjectRelationshipTestUtil.addObjectRelationship(
			_objectDefinition1, _objectDefinition2, TestPropsValues.getUserId(),
			ObjectRelationshipConstants.TYPE_MANY_TO_MANY);

		JSONObject jsonObject = HTTPTestUtil.invoke(
			null,
			StringBundler.concat(
				_objectDefinition1.getRESTContextPath(),
				"/by-external-reference-code/",
				_objectEntry1.getExternalReferenceCode(), StringPool.SLASH,
				_objectRelationship.getName(), StringPool.SLASH,
				_objectEntry2.getExternalReferenceCode()),
			Http.Method.PUT);

		Assert.assertEquals(
			_objectEntry2.getExternalReferenceCode(),
			jsonObject.getString("externalReferenceCode"));
		Assert.assertEquals(
			_OBJECT_FIELD_VALUE_2, jsonObject.getString(_OBJECT_FIELD_NAME_2));

		jsonObject = HTTPTestUtil.invoke(
			null,
			StringBundler.concat(
				_objectDefinition2.getRESTContextPath(),
				"/by-external-reference-code/",
				_objectEntry2.getExternalReferenceCode(), StringPool.SLASH,
				_objectRelationship.getName(), StringPool.SLASH,
				_objectEntry1.getExternalReferenceCode()),
			Http.Method.PUT);

		Assert.assertEquals(
			_objectEntry1.getExternalReferenceCode(),
			jsonObject.getString("externalReferenceCode"));
		Assert.assertEquals(
			_OBJECT_FIELD_VALUE_1, jsonObject.getString(_OBJECT_FIELD_NAME_1));

		jsonObject = HTTPTestUtil.invoke(
			null,
			StringBundler.concat(
				_objectDefinition2.getRESTContextPath(),
				"/by-external-reference-code/",
				_objectEntry2.getExternalReferenceCode(), StringPool.SLASH,
				_objectRelationship.getName(), StringPool.SLASH,
				RandomTestUtil.randomString()),
			Http.Method.PUT);

		Assert.assertThat(
			jsonObject.getString("title"),
			CoreMatchers.containsString("No ObjectEntry exists with the key"));
	}

	private ObjectRelationship _addObjectRelationshipAndRelateObjectsEntries(
			String type)
		throws Exception {

		ObjectRelationship objectRelationship =
			ObjectRelationshipTestUtil.addObjectRelationship(
				_objectDefinition1, _objectDefinition2,
				TestPropsValues.getUserId(), type);

		ObjectRelationshipTestUtil.relateObjectEntries(
			_objectEntry1.getPrimaryKey(), _objectEntry2.getPrimaryKey(),
			objectRelationship, TestPropsValues.getUserId());

		return objectRelationship;
	}

	private void _assertObjectEntryField(
		JSONObject objectEntryJSONObject, String objectFieldName,
		String objectFieldValue) {

		int objectEntryId = objectEntryJSONObject.getInt("id");

		ObjectEntry objectEntry = _objectEntryLocalService.fetchObjectEntry(
			objectEntryId);

		Assert.assertEquals(
			MapUtil.getString(objectEntry.getValues(), objectFieldName),
			objectFieldValue);
	}

	private JSONArray _createObjectEntriesJSONArray(
			String objectFieldName, String[] objectFieldValues)
		throws Exception {

		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		for (String objectFieldValue : objectFieldValues) {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
				JSONUtil.put(
					objectFieldName, objectFieldValue
				).toString());

			jsonArray.put(jsonObject);
		}

		return jsonArray;
	}

	private void _testFilterByRelatedObjectDefinitionSystemObjectField(
			FilterOperator filterOperator,
			ObjectRelationship objectRelationship)
		throws Exception {

		_testFilterByRelatedObjectDefinitionSystemObjectField(
			_OBJECT_FIELD_NAME_1, _OBJECT_FIELD_VALUE_1, filterOperator,
			_objectDefinition1, objectRelationship,
			_objectEntry2.getObjectEntryId());

		_testFilterByRelatedObjectDefinitionSystemObjectField(
			_OBJECT_FIELD_NAME_2, _OBJECT_FIELD_VALUE_2, filterOperator,
			_objectDefinition2, objectRelationship,
			_objectEntry1.getObjectEntryId());
	}

	private void _testFilterByRelatedObjectDefinitionSystemObjectField(
			String expectedObjectFieldName, String expectedObjectFieldValue,
			FilterOperator filterOperator, ObjectDefinition objectDefinition,
			ObjectRelationship objectRelationship, long relatedObjectEntryId)
		throws Exception {

		String endpoint = StringBundler.concat(
			objectDefinition.getRESTContextPath(), "?filter=",
			objectRelationship.getName(), "/id%20", filterOperator.getValue(),
			"%20'", String.valueOf(relatedObjectEntryId),
			StringPool.APOSTROPHE);

		_testFilterObjectEntriesByRelatedObjectEntriesUsingAFilterOperator(
			endpoint, expectedObjectFieldName, expectedObjectFieldValue);
	}

	private void _testFilterObjectEntriesByRelatedObjectEntries(
			FilterOperator filterOperator)
		throws Exception {

		_objectRelationship = _addObjectRelationshipAndRelateObjectsEntries(
			ObjectRelationshipConstants.TYPE_MANY_TO_MANY);

		_testFilterObjectEntriesByRelatedObjectEntriesInBothSidesOfRelationship(
			_objectRelationship, filterOperator);

		_objectRelationshipLocalService.deleteObjectRelationship(
			_objectRelationship);

		_objectRelationship = _addObjectRelationshipAndRelateObjectsEntries(
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY);

		_testFilterObjectEntriesByRelatedObjectEntriesInBothSidesOfRelationship(
			_objectRelationship, filterOperator);
	}

	private void
			_testFilterObjectEntriesByRelatedObjectEntriesInBothSidesOfRelationship(
				ObjectRelationship objectRelationship,
				FilterOperator filterOperator)
		throws Exception {

		_testFilterObjectEntriesByRelatedObjectEntriesUsingAFilterOperator(
			_OBJECT_FIELD_NAME_1, _OBJECT_FIELD_VALUE_1, filterOperator,
			_objectDefinition1, objectRelationship, _OBJECT_FIELD_NAME_2,
			_OBJECT_FIELD_VALUE_2);
		_testFilterObjectEntriesByRelatedObjectEntriesUsingAFilterOperator(
			_OBJECT_FIELD_NAME_2, _OBJECT_FIELD_VALUE_2, filterOperator,
			_objectDefinition2, objectRelationship, _OBJECT_FIELD_NAME_1,
			_OBJECT_FIELD_VALUE_1);
	}

	private void
			_testFilterObjectEntriesByRelatedObjectEntriesUsingAFilterOperator(
				String expectedObjectFieldName, String expectedObjectFieldValue,
				FilterOperator filterOperator,
				ObjectDefinition objectDefinition,
				ObjectRelationship objectRelationship,
				String relatedObjectFieldName, String relatedObjectFieldValue)
		throws Exception {

		String endpoint = objectDefinition.getRESTContextPath() + "?filter=";

		if (filterOperator == FilterOperator.CONTAINS) {
			endpoint = endpoint.concat(
				StringBundler.concat(
					filterOperator.getValue(), StringPool.OPEN_PARENTHESIS,
					objectRelationship.getName(), StringPool.SLASH,
					relatedObjectFieldName, StringPool.COMMA,
					StringPool.APOSTROPHE,
					relatedObjectFieldValue.substring(1, 2),
					StringPool.APOSTROPHE, StringPool.CLOSE_PARENTHESIS));
		}
		else if (filterOperator == FilterOperator.EQ) {
			_testFilterByRelatedObjectDefinitionSystemObjectField(
				filterOperator, objectRelationship);

			endpoint = endpoint.concat(
				StringBundler.concat(
					objectRelationship.getName(), StringPool.SLASH,
					relatedObjectFieldName, "%20", filterOperator.getValue(),
					"%20'", relatedObjectFieldValue, StringPool.APOSTROPHE));
		}
		else if (filterOperator == FilterOperator.IN) {
			endpoint = endpoint.concat(
				StringBundler.concat(
					objectRelationship.getName(), StringPool.SLASH,
					relatedObjectFieldName, "%20", filterOperator.getValue(),
					"%20('", RandomTestUtil.randomString(),
					StringPool.APOSTROPHE, StringPool.COMMA,
					StringPool.APOSTROPHE, relatedObjectFieldValue,
					StringPool.APOSTROPHE, StringPool.CLOSE_PARENTHESIS));
		}
		else if (filterOperator == FilterOperator.STARTS_WITH) {
			endpoint = endpoint.concat(
				StringBundler.concat(
					filterOperator.getValue(), StringPool.OPEN_PARENTHESIS,
					objectRelationship.getName(), StringPool.SLASH,
					relatedObjectFieldName, StringPool.COMMA,
					StringPool.APOSTROPHE,
					relatedObjectFieldValue.substring(0, 1),
					StringPool.APOSTROPHE, StringPool.CLOSE_PARENTHESIS));
		}
		else {
			throw new NotSupportedException(
				"Filter " + filterOperator.name() + " is not supported");
		}

		_testFilterObjectEntriesByRelatedObjectEntriesUsingAFilterOperator(
			endpoint, expectedObjectFieldName, expectedObjectFieldValue);
	}

	private void
			_testFilterObjectEntriesByRelatedObjectEntriesUsingAFilterOperator(
				String endpoint, String expectedObjectFieldName,
				String expectedObjectFieldValue)
		throws Exception {

		JSONObject jsonObject = HTTPTestUtil.invoke(
			null, endpoint, Http.Method.GET);

		JSONArray itemsJSONArray = jsonObject.getJSONArray("items");

		Assert.assertEquals(1, itemsJSONArray.length());

		JSONObject itemJSONObject = itemsJSONArray.getJSONObject(0);

		Assert.assertEquals(
			expectedObjectFieldValue,
			itemJSONObject.getString(expectedObjectFieldName));
	}

	private void _testGetNestedFieldDetailsInOneToManyRelationships(
			String endpoint, String expectedFieldName)
		throws Exception {

		JSONObject jsonObject = HTTPTestUtil.invoke(
			null, endpoint, Http.Method.GET);

		JSONArray itemsJSONArray = jsonObject.getJSONArray("items");

		Assert.assertEquals(1, itemsJSONArray.length());

		JSONObject itemJSONObject = itemsJSONArray.getJSONObject(0);

		Assert.assertEquals(
			_OBJECT_FIELD_VALUE_2,
			itemJSONObject.getString(_OBJECT_FIELD_NAME_2));

		JSONObject relatedObjectJSONObject = itemJSONObject.getJSONObject(
			expectedFieldName);

		Assert.assertEquals(
			_OBJECT_FIELD_VALUE_1,
			relatedObjectJSONObject.getString(_OBJECT_FIELD_NAME_1));
	}

	private static final String _NEW_OBJECT_FIELD_VALUE_1 =
		RandomTestUtil.randomString();

	private static final String _NEW_OBJECT_FIELD_VALUE_2 =
		RandomTestUtil.randomString();

	private static final String _OBJECT_FIELD_NAME_1 =
		"x" + RandomTestUtil.randomString();

	private static final String _OBJECT_FIELD_NAME_2 =
		"x" + RandomTestUtil.randomString();

	private static final String _OBJECT_FIELD_VALUE_1 =
		RandomTestUtil.randomString();

	private static final String _OBJECT_FIELD_VALUE_2 =
		RandomTestUtil.randomString();

	private ObjectDefinition _objectDefinition1;
	private ObjectDefinition _objectDefinition2;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	private ObjectEntry _objectEntry1;
	private ObjectEntry _objectEntry2;

	@Inject
	private ObjectEntryLocalService _objectEntryLocalService;

	private ObjectRelationship _objectRelationship;

	@Inject
	private ObjectRelationshipLocalService _objectRelationshipLocalService;

	private enum FilterOperator {

		CONTAINS("contains"), EQ("eq"), IN("in"), STARTS_WITH("startswith");

		public String getValue() {
			return _value;
		}

		private FilterOperator(String value) {
			_value = value;
		}

		private final String _value;

	}

}