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
import com.liferay.headless.admin.user.dto.v1_0.UserAccount;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectRelationshipConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectRelationship;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalServiceUtil;
import com.liferay.object.service.ObjectRelationshipLocalServiceUtil;
import com.liferay.object.system.SystemObjectDefinitionMetadata;
import com.liferay.object.system.SystemObjectDefinitionMetadataTracker;
import com.liferay.object.util.LocalizedMapUtil;
import com.liferay.object.util.ObjectFieldUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PropsUtil;

import java.io.Serializable;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.http.HttpStatus;

/**
 * @author Carlos Correa
 */
@RunWith(Arquillian.class)
public class SystemObjectRelatedObjectsTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-162964", "true"
			).build());
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		PropsUtil.addProperties(
			UnicodePropertiesBuilder.setProperty(
				"feature.flag.LPS-162964", "false"
			).build());
	}

	@Before
	public void setUp() throws Exception {
		_objectDefinition = _publishObjectDefinition(
			Collections.singletonList(
				ObjectFieldUtil.createObjectField(
					"Text", "String", true, true, null,
					RandomTestUtil.randomString(), _OBJECT_FIELD_NAME, false)));

		_objectEntry = _addObjectEntry(_OBJECT_FIELD_VALUE);

		_user = TestPropsValues.getUser();

		_userSystemObjectDefinitionMetadata =
			_systemObjectDefinitionMetadataTracker.
				getSystemObjectDefinitionMetadata("User");

		_userSystemObjectDefinition =
			_objectDefinitionLocalService.fetchSystemObjectDefinition(
				_userSystemObjectDefinitionMetadata.getName());
	}

	@After
	public void tearDown() throws Exception {
		for (ObjectRelationship objectRelationship : _objectRelationships) {
			ObjectRelationshipLocalServiceUtil.
				deleteObjectRelationshipMappingTableValues(
					objectRelationship.getObjectRelationshipId(),
					_objectEntry.getPrimaryKey(), _user.getUserId());

			ObjectRelationshipLocalServiceUtil.deleteObjectRelationship(
				objectRelationship);
		}

		_objectDefinitionLocalService.deleteObjectDefinition(
			_objectDefinition.getObjectDefinitionId());
	}

	@Test
	public void testGetManyToManySystemObjectRelatedObjects() throws Exception {
		String name = StringUtil.randomId();

		_addObjectRelationship(
			name, _objectDefinition.getObjectDefinitionId(),
			_userSystemObjectDefinition.getObjectDefinitionId(),
			_objectEntry.getPrimaryKey(), _user.getUserId(),
			ObjectRelationshipConstants.TYPE_MANY_TO_MANY);

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			_invokeGet(_getLocation(name)));

		JSONArray jsonArray = jsonObject.getJSONArray(name);

		_assertEquals(jsonArray, _objectEntry);

		name = StringUtil.randomId();

		_addObjectRelationship(
			name, _userSystemObjectDefinition.getObjectDefinitionId(),
			_objectDefinition.getObjectDefinitionId(), _user.getUserId(),
			_objectEntry.getPrimaryKey(),
			ObjectRelationshipConstants.TYPE_MANY_TO_MANY);

		jsonObject = JSONFactoryUtil.createJSONObject(
			_invokeGet(_getLocation(name)));

		jsonArray = jsonObject.getJSONArray(name);

		_assertEquals(jsonArray, _objectEntry);
	}

	@Test
	public void testGetManyToOneSystemObjectRelatedObjects() throws Exception {
		String name = StringUtil.randomId();

		_addObjectRelationship(
			name, _objectDefinition.getObjectDefinitionId(),
			_userSystemObjectDefinition.getObjectDefinitionId(),
			_objectEntry.getPrimaryKey(), _user.getUserId(),
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY);

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			_invokeGet(_getLocation(name)));

		Assert.assertNull(jsonObject.get(name));
	}

	@Test
	public void testGetNotFoundSystemObjectRelatedObjects() throws Exception {
		String name = StringUtil.randomId();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			_invokeGet(_getLocation(name)));

		Assert.assertNull(jsonObject.getJSONArray(name));
	}

	@Test
	public void testGetOneToManySystemObjectRelatedObjects() throws Exception {
		String name = StringUtil.randomId();

		_addObjectRelationship(
			name, _userSystemObjectDefinition.getObjectDefinitionId(),
			_objectDefinition.getObjectDefinitionId(), _user.getUserId(),
			_objectEntry.getPrimaryKey(),
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY);

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			_invokeGet(_getLocation(name)));

		_assertEquals(jsonObject.getJSONArray(name), _objectEntry);
	}

	@Test
	public void testPostSystemObjectWithObjectRelationshipName()
		throws Exception {

		String name = StringUtil.randomId();

		_addObjectRelationship(
			name, _objectDefinition.getObjectDefinitionId(),
			_userSystemObjectDefinition.getObjectDefinitionId(),
			_objectEntry.getPrimaryKey(), _user.getUserId(),
			ObjectRelationshipConstants.TYPE_ONE_TO_MANY);

		UserAccount userAccount = new UserAccount() {
			{
				name = RandomTestUtil.randomString();
			}
		};

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			userAccount.toString());

		jsonObject.put(name, JSONFactoryUtil.createJSONArray());

		Assert.assertEquals(
			HttpStatus.BAD_REQUEST.value(),
			_invokePost(
				jsonObject.toString(),
				_userSystemObjectDefinitionMetadata.getRESTContextPath()));
	}

	private ObjectEntry _addObjectEntry(String objectFieldValue)
		throws Exception {

		return ObjectEntryLocalServiceUtil.addObjectEntry(
			TestPropsValues.getUserId(), 0,
			_objectDefinition.getObjectDefinitionId(),
			HashMapBuilder.<String, Serializable>put(
				_OBJECT_FIELD_NAME, objectFieldValue
			).build(),
			ServiceContextTestUtil.getServiceContext());
	}

	private ObjectRelationship _addObjectRelationship(
			String name, long objectDefinitionId1, long objectDefinitionId2,
			long primaryKey1, long primaryKey2, String type)
		throws Exception {

		ObjectRelationship objectRelationship =
			ObjectRelationshipLocalServiceUtil.addObjectRelationship(
				_user.getUserId(), objectDefinitionId1, objectDefinitionId2, 0,
				ObjectRelationshipConstants.DELETION_TYPE_PREVENT,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				name, type);

		ObjectRelationshipLocalServiceUtil.
			addObjectRelationshipMappingTableValues(
				_user.getUserId(), objectRelationship.getObjectRelationshipId(),
				primaryKey1, primaryKey2,
				ServiceContextTestUtil.getServiceContext());

		_objectRelationships.add(objectRelationship);

		return objectRelationship;
	}

	private void _assertEquals(JSONArray jsonArray, ObjectEntry objectEntry) {
		Assert.assertEquals(jsonArray.toString(), 1, jsonArray.length());

		JSONObject jsonObject = (JSONObject)jsonArray.get(0);

		Assert.assertEquals(
			objectEntry.getObjectEntryId(), jsonObject.getLong("id"));
	}

	private Http.Options _createOptions(
		String body, Http.Method httpMethod, String location) {

		Http.Options options = new Http.Options();

		options.addHeader(
			HttpHeaders.CONTENT_TYPE, ContentTypes.APPLICATION_JSON);
		options.addHeader(
			"Authorization",
			"Basic " + Base64.encode("test@liferay.com:test".getBytes()));

		if (body != null) {
			options.setBody(
				body, ContentTypes.APPLICATION_JSON,
				StandardCharsets.UTF_8.name());
		}

		options.setLocation(location);
		options.setMethod(httpMethod);

		return options;
	}

	private String _getLocation(String name) {
		return StringBundler.concat(
			"http://localhost:8080/o/",
			_userSystemObjectDefinitionMetadata.getRESTContextPath(),
			StringPool.SLASH, _user.getUserId(), "?nestedFields=", name);
	}

	private String _invokeGet(String location) throws Exception {
		Http.Options options = _createOptions(null, Http.Method.GET, location);

		return HttpUtil.URLtoString(options);
	}

	private int _invokePost(String body, String location) throws Exception {
		Http.Options options = _createOptions(body, Http.Method.POST, location);

		HttpUtil.URLtoString(options);

		Http.Response response = options.getResponse();

		return response.getResponseCode();
	}

	private ObjectDefinition _publishObjectDefinition(
			List<ObjectField> objectFields)
		throws Exception {

		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.addCustomObjectDefinition(
				TestPropsValues.getUserId(),
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				"A" + RandomTestUtil.randomString(), null, null,
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
				ObjectDefinitionConstants.SCOPE_COMPANY,
				ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT, objectFields);

		return _objectDefinitionLocalService.publishCustomObjectDefinition(
			TestPropsValues.getUserId(),
			objectDefinition.getObjectDefinitionId());
	}

	private static final String _OBJECT_FIELD_NAME =
		"x" + RandomTestUtil.randomString();

	private static final String _OBJECT_FIELD_VALUE =
		RandomTestUtil.randomString();

	private ObjectDefinition _objectDefinition;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	private ObjectEntry _objectEntry;
	private final List<ObjectRelationship> _objectRelationships =
		new ArrayList<>();

	@Inject
	private SystemObjectDefinitionMetadataTracker
		_systemObjectDefinitionMetadataTracker;

	private User _user;
	private ObjectDefinition _userSystemObjectDefinition;
	private SystemObjectDefinitionMetadata _userSystemObjectDefinitionMetadata;

}