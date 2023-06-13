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

package com.liferay.analytics.settings.rest.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.analytics.settings.rest.client.dto.v1_0.ContactOrganization;
import com.liferay.analytics.settings.rest.client.http.HttpInvoker;
import com.liferay.analytics.settings.rest.client.pagination.Page;
import com.liferay.analytics.settings.rest.client.pagination.Pagination;
import com.liferay.analytics.settings.rest.client.resource.v1_0.ContactOrganizationResource;
import com.liferay.analytics.settings.rest.client.serdes.v1_0.ContactOrganizationSerDes;
import com.liferay.petra.function.UnsafeTriConsumer;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
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
import com.liferay.portal.kernel.util.GetterUtil;
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

import org.apache.commons.lang.time.DateUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Riccardo Ferrari
 * @generated
 */
@Generated("")
public abstract class BaseContactOrganizationResourceTestCase {

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

		_contactOrganizationResource.setContextCompany(testCompany);

		ContactOrganizationResource.Builder builder =
			ContactOrganizationResource.builder();

		contactOrganizationResource = builder.authentication(
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

		ContactOrganization contactOrganization1 = randomContactOrganization();

		String json = objectMapper.writeValueAsString(contactOrganization1);

		ContactOrganization contactOrganization2 =
			ContactOrganizationSerDes.toDTO(json);

		Assert.assertTrue(equals(contactOrganization1, contactOrganization2));
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

		ContactOrganization contactOrganization = randomContactOrganization();

		String json1 = objectMapper.writeValueAsString(contactOrganization);
		String json2 = ContactOrganizationSerDes.toJSON(contactOrganization);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ContactOrganization contactOrganization = randomContactOrganization();

		contactOrganization.setName(regex);

		String json = ContactOrganizationSerDes.toJSON(contactOrganization);

		Assert.assertFalse(json.contains(regex));

		contactOrganization = ContactOrganizationSerDes.toDTO(json);

		Assert.assertEquals(regex, contactOrganization.getName());
	}

	@Test
	public void testGetContactOrganizationsPage() throws Exception {
		Page<ContactOrganization> page =
			contactOrganizationResource.getContactOrganizationsPage(
				RandomTestUtil.randomString(), Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		ContactOrganization contactOrganization1 =
			testGetContactOrganizationsPage_addContactOrganization(
				randomContactOrganization());

		ContactOrganization contactOrganization2 =
			testGetContactOrganizationsPage_addContactOrganization(
				randomContactOrganization());

		page = contactOrganizationResource.getContactOrganizationsPage(
			null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(
			contactOrganization1, (List<ContactOrganization>)page.getItems());
		assertContains(
			contactOrganization2, (List<ContactOrganization>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetContactOrganizationsPageWithPagination()
		throws Exception {

		Page<ContactOrganization> totalPage =
			contactOrganizationResource.getContactOrganizationsPage(
				null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		ContactOrganization contactOrganization1 =
			testGetContactOrganizationsPage_addContactOrganization(
				randomContactOrganization());

		ContactOrganization contactOrganization2 =
			testGetContactOrganizationsPage_addContactOrganization(
				randomContactOrganization());

		ContactOrganization contactOrganization3 =
			testGetContactOrganizationsPage_addContactOrganization(
				randomContactOrganization());

		Page<ContactOrganization> page1 =
			contactOrganizationResource.getContactOrganizationsPage(
				null, Pagination.of(1, totalCount + 2), null);

		List<ContactOrganization> contactOrganizations1 =
			(List<ContactOrganization>)page1.getItems();

		Assert.assertEquals(
			contactOrganizations1.toString(), totalCount + 2,
			contactOrganizations1.size());

		Page<ContactOrganization> page2 =
			contactOrganizationResource.getContactOrganizationsPage(
				null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<ContactOrganization> contactOrganizations2 =
			(List<ContactOrganization>)page2.getItems();

		Assert.assertEquals(
			contactOrganizations2.toString(), 1, contactOrganizations2.size());

		Page<ContactOrganization> page3 =
			contactOrganizationResource.getContactOrganizationsPage(
				null, Pagination.of(1, totalCount + 3), null);

		assertContains(
			contactOrganization1, (List<ContactOrganization>)page3.getItems());
		assertContains(
			contactOrganization2, (List<ContactOrganization>)page3.getItems());
		assertContains(
			contactOrganization3, (List<ContactOrganization>)page3.getItems());
	}

	@Test
	public void testGetContactOrganizationsPageWithSortDateTime()
		throws Exception {

		testGetContactOrganizationsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, contactOrganization1, contactOrganization2) -> {
				BeanTestUtil.setProperty(
					contactOrganization1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetContactOrganizationsPageWithSortDouble()
		throws Exception {

		testGetContactOrganizationsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, contactOrganization1, contactOrganization2) -> {
				BeanTestUtil.setProperty(
					contactOrganization1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					contactOrganization2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetContactOrganizationsPageWithSortInteger()
		throws Exception {

		testGetContactOrganizationsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, contactOrganization1, contactOrganization2) -> {
				BeanTestUtil.setProperty(
					contactOrganization1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					contactOrganization2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetContactOrganizationsPageWithSortString()
		throws Exception {

		testGetContactOrganizationsPageWithSort(
			EntityField.Type.STRING,
			(entityField, contactOrganization1, contactOrganization2) -> {
				Class<?> clazz = contactOrganization1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						contactOrganization1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						contactOrganization2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						contactOrganization1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						contactOrganization2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						contactOrganization1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						contactOrganization2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetContactOrganizationsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, ContactOrganization, ContactOrganization,
				 Exception> unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		ContactOrganization contactOrganization1 = randomContactOrganization();
		ContactOrganization contactOrganization2 = randomContactOrganization();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, contactOrganization1, contactOrganization2);
		}

		contactOrganization1 =
			testGetContactOrganizationsPage_addContactOrganization(
				contactOrganization1);

		contactOrganization2 =
			testGetContactOrganizationsPage_addContactOrganization(
				contactOrganization2);

		for (EntityField entityField : entityFields) {
			Page<ContactOrganization> ascPage =
				contactOrganizationResource.getContactOrganizationsPage(
					null, Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(contactOrganization1, contactOrganization2),
				(List<ContactOrganization>)ascPage.getItems());

			Page<ContactOrganization> descPage =
				contactOrganizationResource.getContactOrganizationsPage(
					null, Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(contactOrganization2, contactOrganization1),
				(List<ContactOrganization>)descPage.getItems());
		}
	}

	protected ContactOrganization
			testGetContactOrganizationsPage_addContactOrganization(
				ContactOrganization contactOrganization)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetContactOrganizationsPage() throws Exception {
		GraphQLField graphQLField = new GraphQLField(
			"contactOrganizations",
			new HashMap<String, Object>() {
				{
					put("page", 1);
					put("pageSize", 10);
				}
			},
			new GraphQLField("items", getGraphQLFields()),
			new GraphQLField("page"), new GraphQLField("totalCount"));

		JSONObject contactOrganizationsJSONObject =
			JSONUtil.getValueAsJSONObject(
				invokeGraphQLQuery(graphQLField), "JSONObject/data",
				"JSONObject/contactOrganizations");

		long totalCount = contactOrganizationsJSONObject.getLong("totalCount");

		ContactOrganization contactOrganization1 =
			testGraphQLGetContactOrganizationsPage_addContactOrganization();
		ContactOrganization contactOrganization2 =
			testGraphQLGetContactOrganizationsPage_addContactOrganization();

		contactOrganizationsJSONObject = JSONUtil.getValueAsJSONObject(
			invokeGraphQLQuery(graphQLField), "JSONObject/data",
			"JSONObject/contactOrganizations");

		Assert.assertEquals(
			totalCount + 2,
			contactOrganizationsJSONObject.getLong("totalCount"));

		assertContains(
			contactOrganization1,
			Arrays.asList(
				ContactOrganizationSerDes.toDTOs(
					contactOrganizationsJSONObject.getString("items"))));
		assertContains(
			contactOrganization2,
			Arrays.asList(
				ContactOrganizationSerDes.toDTOs(
					contactOrganizationsJSONObject.getString("items"))));
	}

	protected ContactOrganization
			testGraphQLGetContactOrganizationsPage_addContactOrganization()
		throws Exception {

		return testGraphQLContactOrganization_addContactOrganization();
	}

	protected ContactOrganization
			testGraphQLContactOrganization_addContactOrganization()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		ContactOrganization contactOrganization,
		List<ContactOrganization> contactOrganizations) {

		boolean contains = false;

		for (ContactOrganization item : contactOrganizations) {
			if (equals(contactOrganization, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			contactOrganizations + " does not contain " + contactOrganization,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ContactOrganization contactOrganization1,
		ContactOrganization contactOrganization2) {

		Assert.assertTrue(
			contactOrganization1 + " does not equal " + contactOrganization2,
			equals(contactOrganization1, contactOrganization2));
	}

	protected void assertEquals(
		List<ContactOrganization> contactOrganizations1,
		List<ContactOrganization> contactOrganizations2) {

		Assert.assertEquals(
			contactOrganizations1.size(), contactOrganizations2.size());

		for (int i = 0; i < contactOrganizations1.size(); i++) {
			ContactOrganization contactOrganization1 =
				contactOrganizations1.get(i);
			ContactOrganization contactOrganization2 =
				contactOrganizations2.get(i);

			assertEquals(contactOrganization1, contactOrganization2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ContactOrganization> contactOrganizations1,
		List<ContactOrganization> contactOrganizations2) {

		Assert.assertEquals(
			contactOrganizations1.size(), contactOrganizations2.size());

		for (ContactOrganization contactOrganization1 : contactOrganizations1) {
			boolean contains = false;

			for (ContactOrganization contactOrganization2 :
					contactOrganizations2) {

				if (equals(contactOrganization1, contactOrganization2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				contactOrganizations2 + " does not contain " +
					contactOrganization1,
				contains);
		}
	}

	protected void assertValid(ContactOrganization contactOrganization)
		throws Exception {

		boolean valid = true;

		if (contactOrganization.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (contactOrganization.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("selected", additionalAssertFieldName)) {
				if (contactOrganization.getSelected() == null) {
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

	protected void assertValid(Page<ContactOrganization> page) {
		boolean valid = false;

		java.util.Collection<ContactOrganization> contactOrganizations =
			page.getItems();

		int size = contactOrganizations.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	protected List<GraphQLField> getGraphQLFields() throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (java.lang.reflect.Field field :
				getDeclaredFields(
					com.liferay.analytics.settings.rest.dto.v1_0.
						ContactOrganization.class)) {

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

	protected boolean equals(
		ContactOrganization contactOrganization1,
		ContactOrganization contactOrganization2) {

		if (contactOrganization1 == contactOrganization2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						contactOrganization1.getId(),
						contactOrganization2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						contactOrganization1.getName(),
						contactOrganization2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("selected", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						contactOrganization1.getSelected(),
						contactOrganization2.getSelected())) {

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

		if (!(_contactOrganizationResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_contactOrganizationResource;

		EntityModel entityModel = entityModelResource.getEntityModel(
			new MultivaluedHashMap());

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
		EntityField entityField, String operator,
		ContactOrganization contactOrganization) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(contactOrganization.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("selected")) {
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

	protected ContactOrganization randomContactOrganization() throws Exception {
		return new ContactOrganization() {
			{
				id = RandomTestUtil.randomLong();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				selected = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected ContactOrganization randomIrrelevantContactOrganization()
		throws Exception {

		ContactOrganization randomIrrelevantContactOrganization =
			randomContactOrganization();

		return randomIrrelevantContactOrganization;
	}

	protected ContactOrganization randomPatchContactOrganization()
		throws Exception {

		return randomContactOrganization();
	}

	protected ContactOrganizationResource contactOrganizationResource;
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
		LogFactoryUtil.getLog(BaseContactOrganizationResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.analytics.settings.rest.resource.v1_0.
		ContactOrganizationResource _contactOrganizationResource;

}