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

package com.liferay.headless.commerce.delivery.catalog.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.delivery.catalog.client.dto.v1_0.WishList;
import com.liferay.headless.commerce.delivery.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Page;
import com.liferay.headless.commerce.delivery.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.delivery.catalog.client.resource.v1_0.WishListResource;
import com.liferay.headless.commerce.delivery.catalog.client.serdes.v1_0.WishListSerDes;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.Method;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Andrea Sbarra
 * @generated
 */
@Generated("")
public abstract class BaseWishListResourceTestCase {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	@Before
	public void setUp() throws Exception {
		irrelevantGroup = GroupTestUtil.addGroup();
		testGroup = GroupTestUtil.addGroup();

		testCompany = CompanyLocalServiceUtil.getCompany(
			testGroup.getCompanyId());

		_wishListResource.setContextCompany(testCompany);

		WishListResource.Builder builder = WishListResource.builder();

		wishListResource = builder.authentication(
			"test@liferay.com", "test"
		).locale(
			LocaleUtil.getDefault()
		).build();
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testClientSerDesToDTO() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				enable(SerializationFeature.INDENT_OUTPUT);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		WishList wishList1 = randomWishList();

		String json = objectMapper.writeValueAsString(wishList1);

		WishList wishList2 = WishListSerDes.toDTO(json);

		Assert.assertTrue(equals(wishList1, wishList2));
	}

	@Test
	public void testClientSerDesToJSON() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper() {
			{
				configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
				configure(
					SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
				setDateFormat(new ISO8601DateFormat());
				setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
				setSerializationInclusion(JsonInclude.Include.NON_NULL);
				setVisibility(
					PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
				setVisibility(
					PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
			}
		};

		WishList wishList = randomWishList();

		String json1 = objectMapper.writeValueAsString(wishList);
		String json2 = WishListSerDes.toJSON(wishList);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		WishList wishList = randomWishList();

		wishList.setName(regex);

		String json = WishListSerDes.toJSON(wishList);

		Assert.assertFalse(json.contains(regex));

		wishList = WishListSerDes.toDTO(json);

		Assert.assertEquals(regex, wishList.getName());
	}

	@Test
	public void testGetChannelWishListsPage() throws Exception {
		Long channelId = testGetChannelWishListsPage_getChannelId();
		Long irrelevantChannelId =
			testGetChannelWishListsPage_getIrrelevantChannelId();

		Page<WishList> page = wishListResource.getChannelWishListsPage(
			channelId, null, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantChannelId != null) {
			WishList irrelevantWishList =
				testGetChannelWishListsPage_addWishList(
					irrelevantChannelId, randomIrrelevantWishList());

			page = wishListResource.getChannelWishListsPage(
				irrelevantChannelId, null, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantWishList),
				(List<WishList>)page.getItems());
			assertValid(
				page,
				testGetChannelWishListsPage_getExpectedActions(
					irrelevantChannelId));
		}

		WishList wishList1 = testGetChannelWishListsPage_addWishList(
			channelId, randomWishList());

		WishList wishList2 = testGetChannelWishListsPage_addWishList(
			channelId, randomWishList());

		page = wishListResource.getChannelWishListsPage(
			channelId, null, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(wishList1, wishList2),
			(List<WishList>)page.getItems());
		assertValid(
			page, testGetChannelWishListsPage_getExpectedActions(channelId));

		wishListResource.deleteWishList(wishList1.getId());

		wishListResource.deleteWishList(wishList2.getId());
	}

	protected Map<String, Map<String, String>>
			testGetChannelWishListsPage_getExpectedActions(Long channelId)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetChannelWishListsPageWithPagination() throws Exception {
		Long channelId = testGetChannelWishListsPage_getChannelId();

		WishList wishList1 = testGetChannelWishListsPage_addWishList(
			channelId, randomWishList());

		WishList wishList2 = testGetChannelWishListsPage_addWishList(
			channelId, randomWishList());

		WishList wishList3 = testGetChannelWishListsPage_addWishList(
			channelId, randomWishList());

		Page<WishList> page1 = wishListResource.getChannelWishListsPage(
			channelId, null, Pagination.of(1, 2));

		List<WishList> wishLists1 = (List<WishList>)page1.getItems();

		Assert.assertEquals(wishLists1.toString(), 2, wishLists1.size());

		Page<WishList> page2 = wishListResource.getChannelWishListsPage(
			channelId, null, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<WishList> wishLists2 = (List<WishList>)page2.getItems();

		Assert.assertEquals(wishLists2.toString(), 1, wishLists2.size());

		Page<WishList> page3 = wishListResource.getChannelWishListsPage(
			channelId, null, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(wishList1, wishList2, wishList3),
			(List<WishList>)page3.getItems());
	}

	protected WishList testGetChannelWishListsPage_addWishList(
			Long channelId, WishList wishList)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelWishListsPage_getChannelId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetChannelWishListsPage_getIrrelevantChannelId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostChannelWishList() throws Exception {
		WishList randomWishList = randomWishList();

		WishList postWishList = testPostChannelWishList_addWishList(
			randomWishList);

		assertEquals(randomWishList, postWishList);
		assertValid(postWishList);
	}

	protected WishList testPostChannelWishList_addWishList(WishList wishList)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteWishList() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		WishList wishList = testDeleteWishList_addWishList();

		assertHttpResponseStatusCode(
			204, wishListResource.deleteWishListHttpResponse(wishList.getId()));

		assertHttpResponseStatusCode(
			404, wishListResource.getWishListHttpResponse(wishList.getId()));

		assertHttpResponseStatusCode(
			404, wishListResource.getWishListHttpResponse(0L));
	}

	protected WishList testDeleteWishList_addWishList() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteWishList() throws Exception {
		WishList wishList = testGraphQLDeleteWishList_addWishList();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteWishList",
						new HashMap<String, Object>() {
							{
								put("wishListId", wishList.getId());
							}
						})),
				"JSONObject/data", "Object/deleteWishList"));
		JSONArray errorsJSONArray = JSONUtil.getValueAsJSONArray(
			invokeGraphQLQuery(
				new GraphQLField(
					"wishList",
					new HashMap<String, Object>() {
						{
							put("wishListId", wishList.getId());
						}
					},
					new GraphQLField("id"))),
			"JSONArray/errors");

		Assert.assertTrue(errorsJSONArray.length() > 0);
	}

	protected WishList testGraphQLDeleteWishList_addWishList()
		throws Exception {

		return testGraphQLWishList_addWishList();
	}

	@Test
	public void testGetWishList() throws Exception {
		WishList postWishList = testGetWishList_addWishList();

		WishList getWishList = wishListResource.getWishList(
			postWishList.getId());

		assertEquals(postWishList, getWishList);
		assertValid(getWishList);
	}

	protected WishList testGetWishList_addWishList() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetWishList() throws Exception {
		WishList wishList = testGraphQLGetWishList_addWishList();

		Assert.assertTrue(
			equals(
				wishList,
				WishListSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"wishList",
								new HashMap<String, Object>() {
									{
										put("wishListId", wishList.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/wishList"))));
	}

	@Test
	public void testGraphQLGetWishListNotFound() throws Exception {
		Long irrelevantWishListId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"wishList",
						new HashMap<String, Object>() {
							{
								put("wishListId", irrelevantWishListId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected WishList testGraphQLGetWishList_addWishList() throws Exception {
		return testGraphQLWishList_addWishList();
	}

	@Test
	public void testPatchChannelWishList() throws Exception {
		Assert.assertTrue(false);
	}

	protected WishList testGraphQLWishList_addWishList() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(WishList wishList, List<WishList> wishLists) {
		boolean contains = false;

		for (WishList item : wishLists) {
			if (equals(wishList, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			wishLists + " does not contain " + wishList, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(WishList wishList1, WishList wishList2) {
		Assert.assertTrue(
			wishList1 + " does not equal " + wishList2,
			equals(wishList1, wishList2));
	}

	protected void assertEquals(
		List<WishList> wishLists1, List<WishList> wishLists2) {

		Assert.assertEquals(wishLists1.size(), wishLists2.size());

		for (int i = 0; i < wishLists1.size(); i++) {
			WishList wishList1 = wishLists1.get(i);
			WishList wishList2 = wishLists2.get(i);

			assertEquals(wishList1, wishList2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<WishList> wishLists1, List<WishList> wishLists2) {

		Assert.assertEquals(wishLists1.size(), wishLists2.size());

		for (WishList wishList1 : wishLists1) {
			boolean contains = false;

			for (WishList wishList2 : wishLists2) {
				if (equals(wishList1, wishList2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				wishLists2 + " does not contain " + wishList1, contains);
		}
	}

	protected void assertValid(WishList wishList) throws Exception {
		boolean valid = true;

		if (wishList.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("defaultWishList", additionalAssertFieldName)) {
				if (wishList.getDefaultWishList() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (wishList.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("wishListItems", additionalAssertFieldName)) {
				if (wishList.getWishListItems() == null) {
					valid = false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		Assert.assertTrue(valid);
	}

	protected void assertValid(Page<WishList> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<WishList> page, Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<WishList> wishLists = page.getItems();

		int size = wishLists.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);

		Map<String, Map<String, String>> actions = page.getActions();

		for (String key : expectedActions.keySet()) {
			Map action = actions.get(key);

			Assert.assertNotNull(key + " does not contain an action", action);

			Map expectedAction = expectedActions.get(key);

			Assert.assertEquals(
				expectedAction.get("method"), action.get("method"));
			Assert.assertEquals(expectedAction.get("href"), action.get("href"));
		}
	}

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	protected List<GraphQLField> getGraphQLFields() throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (java.lang.reflect.Field field :
				getDeclaredFields(
					com.liferay.headless.commerce.delivery.catalog.dto.v1_0.
						WishList.class)) {

			if (!ArrayUtil.contains(
					getAdditionalAssertFieldNames(), field.getName())) {

				continue;
			}

			graphQLFields.addAll(getGraphQLFields(field));
		}

		return graphQLFields;
	}

	protected List<GraphQLField> getGraphQLFields(
			java.lang.reflect.Field... fields)
		throws Exception {

		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (java.lang.reflect.Field field : fields) {
			com.liferay.portal.vulcan.graphql.annotation.GraphQLField
				vulcanGraphQLField = field.getAnnotation(
					com.liferay.portal.vulcan.graphql.annotation.GraphQLField.
						class);

			if (vulcanGraphQLField != null) {
				Class<?> clazz = field.getType();

				if (clazz.isArray()) {
					clazz = clazz.getComponentType();
				}

				List<GraphQLField> childrenGraphQLFields = getGraphQLFields(
					getDeclaredFields(clazz));

				graphQLFields.add(
					new GraphQLField(field.getName(), childrenGraphQLFields));
			}
		}

		return graphQLFields;
	}

	protected String[] getIgnoredEntityFieldNames() {
		return new String[0];
	}

	protected boolean equals(WishList wishList1, WishList wishList2) {
		if (wishList1 == wishList2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("defaultWishList", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						wishList1.getDefaultWishList(),
						wishList2.getDefaultWishList())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(wishList1.getId(), wishList2.getId())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						wishList1.getName(), wishList2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("wishListItems", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						wishList1.getWishListItems(),
						wishList2.getWishListItems())) {

					return false;
				}

				continue;
			}

			throw new IllegalArgumentException(
				"Invalid additional assert field name " +
					additionalAssertFieldName);
		}

		return true;
	}

	protected boolean equals(
		Map<String, Object> map1, Map<String, Object> map2) {

		if (Objects.equals(map1.keySet(), map2.keySet())) {
			for (Map.Entry<String, Object> entry : map1.entrySet()) {
				if (entry.getValue() instanceof Map) {
					if (!equals(
							(Map)entry.getValue(),
							(Map)map2.get(entry.getKey()))) {

						return false;
					}
				}
				else if (!Objects.deepEquals(
							entry.getValue(), map2.get(entry.getKey()))) {

					return false;
				}
			}

			return true;
		}

		return false;
	}

	protected java.lang.reflect.Field[] getDeclaredFields(Class clazz)
		throws Exception {

		Stream<java.lang.reflect.Field> stream = Stream.of(
			ReflectionUtil.getDeclaredFields(clazz));

		return stream.filter(
			field -> !field.isSynthetic()
		).toArray(
			java.lang.reflect.Field[]::new
		);
	}

	protected java.util.Collection<EntityField> getEntityFields()
		throws Exception {

		if (!(_wishListResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_wishListResource;

		EntityModel entityModel = entityModelResource.getEntityModel(
			new MultivaluedHashMap());

		if (entityModel == null) {
			return Collections.emptyList();
		}

		Map<String, EntityField> entityFieldsMap =
			entityModel.getEntityFieldsMap();

		return entityFieldsMap.values();
	}

	protected List<EntityField> getEntityFields(EntityField.Type type)
		throws Exception {

		java.util.Collection<EntityField> entityFields = getEntityFields();

		Stream<EntityField> stream = entityFields.stream();

		return stream.filter(
			entityField ->
				Objects.equals(entityField.getType(), type) &&
				!ArrayUtil.contains(
					getIgnoredEntityFieldNames(), entityField.getName())
		).collect(
			Collectors.toList()
		);
	}

	protected String getFilterString(
		EntityField entityField, String operator, WishList wishList) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("defaultWishList")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(wishList.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("wishListItems")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected String invoke(String query) throws Exception {
		HttpInvoker httpInvoker = HttpInvoker.newHttpInvoker();

		httpInvoker.body(
			JSONUtil.put(
				"query", query
			).toString(),
			"application/json");
		httpInvoker.httpMethod(HttpInvoker.HttpMethod.POST);
		httpInvoker.path("http://localhost:8080/o/graphql");
		httpInvoker.userNameAndPassword("test@liferay.com:test");

		HttpInvoker.HttpResponse httpResponse = httpInvoker.invoke();

		return httpResponse.getContent();
	}

	protected JSONObject invokeGraphQLMutation(GraphQLField graphQLField)
		throws Exception {

		GraphQLField mutationGraphQLField = new GraphQLField(
			"mutation", graphQLField);

		return JSONFactoryUtil.createJSONObject(
			invoke(mutationGraphQLField.toString()));
	}

	protected JSONObject invokeGraphQLQuery(GraphQLField graphQLField)
		throws Exception {

		GraphQLField queryGraphQLField = new GraphQLField(
			"query", graphQLField);

		return JSONFactoryUtil.createJSONObject(
			invoke(queryGraphQLField.toString()));
	}

	protected WishList randomWishList() throws Exception {
		return new WishList() {
			{
				defaultWishList = RandomTestUtil.randomBoolean();
				id = RandomTestUtil.randomLong();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected WishList randomIrrelevantWishList() throws Exception {
		WishList randomIrrelevantWishList = randomWishList();

		return randomIrrelevantWishList;
	}

	protected WishList randomPatchWishList() throws Exception {
		return randomWishList();
	}

	protected WishListResource wishListResource;
	protected Group irrelevantGroup;
	protected Company testCompany;
	protected Group testGroup;

	protected static class BeanTestUtil {

		public static void copyProperties(Object source, Object target)
			throws Exception {

			Class<?> sourceClass = _getSuperClass(source.getClass());

			Class<?> targetClass = target.getClass();

			for (java.lang.reflect.Field field :
					sourceClass.getDeclaredFields()) {

				if (field.isSynthetic()) {
					continue;
				}

				Method getMethod = _getMethod(
					sourceClass, field.getName(), "get");

				Method setMethod = _getMethod(
					targetClass, field.getName(), "set",
					getMethod.getReturnType());

				setMethod.invoke(target, getMethod.invoke(source));
			}
		}

		public static boolean hasProperty(Object bean, String name) {
			Method setMethod = _getMethod(
				bean.getClass(), "set" + StringUtil.upperCaseFirstLetter(name));

			if (setMethod != null) {
				return true;
			}

			return false;
		}

		public static void setProperty(Object bean, String name, Object value)
			throws Exception {

			Class<?> clazz = bean.getClass();

			Method setMethod = _getMethod(
				clazz, "set" + StringUtil.upperCaseFirstLetter(name));

			if (setMethod == null) {
				throw new NoSuchMethodException();
			}

			Class<?>[] parameterTypes = setMethod.getParameterTypes();

			setMethod.invoke(bean, _translateValue(parameterTypes[0], value));
		}

		private static Method _getMethod(Class<?> clazz, String name) {
			for (Method method : clazz.getMethods()) {
				if (name.equals(method.getName()) &&
					(method.getParameterCount() == 1) &&
					_parameterTypes.contains(method.getParameterTypes()[0])) {

					return method;
				}
			}

			return null;
		}

		private static Method _getMethod(
				Class<?> clazz, String fieldName, String prefix,
				Class<?>... parameterTypes)
			throws Exception {

			return clazz.getMethod(
				prefix + StringUtil.upperCaseFirstLetter(fieldName),
				parameterTypes);
		}

		private static Class<?> _getSuperClass(Class<?> clazz) {
			Class<?> superClass = clazz.getSuperclass();

			if ((superClass == null) || (superClass == Object.class)) {
				return clazz;
			}

			return superClass;
		}

		private static Object _translateValue(
			Class<?> parameterType, Object value) {

			if ((value instanceof Integer) &&
				parameterType.equals(Long.class)) {

				Integer intValue = (Integer)value;

				return intValue.longValue();
			}

			return value;
		}

		private static final Set<Class<?>> _parameterTypes = new HashSet<>(
			Arrays.asList(
				Boolean.class, Date.class, Double.class, Integer.class,
				Long.class, Map.class, String.class));

	}

	protected class GraphQLField {

		public GraphQLField(String key, GraphQLField... graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(String key, List<GraphQLField> graphQLFields) {
			this(key, new HashMap<>(), graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			GraphQLField... graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = Arrays.asList(graphQLFields);
		}

		public GraphQLField(
			String key, Map<String, Object> parameterMap,
			List<GraphQLField> graphQLFields) {

			_key = key;
			_parameterMap = parameterMap;
			_graphQLFields = graphQLFields;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(_key);

			if (!_parameterMap.isEmpty()) {
				sb.append("(");

				for (Map.Entry<String, Object> entry :
						_parameterMap.entrySet()) {

					sb.append(entry.getKey());
					sb.append(": ");
					sb.append(entry.getValue());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append(")");
			}

			if (!_graphQLFields.isEmpty()) {
				sb.append("{");

				for (GraphQLField graphQLField : _graphQLFields) {
					sb.append(graphQLField.toString());
					sb.append(", ");
				}

				sb.setLength(sb.length() - 2);

				sb.append("}");
			}

			return sb.toString();
		}

		private final List<GraphQLField> _graphQLFields;
		private final String _key;
		private final Map<String, Object> _parameterMap;

	}

	private static final com.liferay.portal.kernel.log.Log _log =
		LogFactoryUtil.getLog(BaseWishListResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.delivery.catalog.resource.v1_0.
		WishListResource _wishListResource;

}