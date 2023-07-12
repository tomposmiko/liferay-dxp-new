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

package com.liferay.headless.commerce.admin.catalog.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.ProductOptionValue;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.ProductOptionValueResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.ProductOptionValueSerDes;
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
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.resource.EntityModelResource;
import com.liferay.portal.vulcan.util.TransformUtil;

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
 * @author Zoltán Takács
 * @generated
 */
@Generated("")
public abstract class BaseProductOptionValueResourceTestCase {

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

		_productOptionValueResource.setContextCompany(testCompany);

		ProductOptionValueResource.Builder builder =
			ProductOptionValueResource.builder();

		productOptionValueResource = builder.authentication(
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

		ProductOptionValue productOptionValue1 = randomProductOptionValue();

		String json = objectMapper.writeValueAsString(productOptionValue1);

		ProductOptionValue productOptionValue2 = ProductOptionValueSerDes.toDTO(
			json);

		Assert.assertTrue(equals(productOptionValue1, productOptionValue2));
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

		ProductOptionValue productOptionValue = randomProductOptionValue();

		String json1 = objectMapper.writeValueAsString(productOptionValue);
		String json2 = ProductOptionValueSerDes.toJSON(productOptionValue);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		ProductOptionValue productOptionValue = randomProductOptionValue();

		productOptionValue.setKey(regex);

		String json = ProductOptionValueSerDes.toJSON(productOptionValue);

		Assert.assertFalse(json.contains(regex));

		productOptionValue = ProductOptionValueSerDes.toDTO(json);

		Assert.assertEquals(regex, productOptionValue.getKey());
	}

	@Test
	public void testGetProductOptionIdProductOptionValuesPage()
		throws Exception {

		Long id = testGetProductOptionIdProductOptionValuesPage_getId();
		Long irrelevantId =
			testGetProductOptionIdProductOptionValuesPage_getIrrelevantId();

		Page<ProductOptionValue> page =
			productOptionValueResource.
				getProductOptionIdProductOptionValuesPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			ProductOptionValue irrelevantProductOptionValue =
				testGetProductOptionIdProductOptionValuesPage_addProductOptionValue(
					irrelevantId, randomIrrelevantProductOptionValue());

			page =
				productOptionValueResource.
					getProductOptionIdProductOptionValuesPage(
						irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantProductOptionValue),
				(List<ProductOptionValue>)page.getItems());
			assertValid(
				page,
				testGetProductOptionIdProductOptionValuesPage_getExpectedActions(
					irrelevantId));
		}

		ProductOptionValue productOptionValue1 =
			testGetProductOptionIdProductOptionValuesPage_addProductOptionValue(
				id, randomProductOptionValue());

		ProductOptionValue productOptionValue2 =
			testGetProductOptionIdProductOptionValuesPage_addProductOptionValue(
				id, randomProductOptionValue());

		page =
			productOptionValueResource.
				getProductOptionIdProductOptionValuesPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(productOptionValue1, productOptionValue2),
			(List<ProductOptionValue>)page.getItems());
		assertValid(
			page,
			testGetProductOptionIdProductOptionValuesPage_getExpectedActions(
				id));
	}

	protected Map<String, Map<String, String>>
			testGetProductOptionIdProductOptionValuesPage_getExpectedActions(
				Long id)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetProductOptionIdProductOptionValuesPageWithPagination()
		throws Exception {

		Long id = testGetProductOptionIdProductOptionValuesPage_getId();

		ProductOptionValue productOptionValue1 =
			testGetProductOptionIdProductOptionValuesPage_addProductOptionValue(
				id, randomProductOptionValue());

		ProductOptionValue productOptionValue2 =
			testGetProductOptionIdProductOptionValuesPage_addProductOptionValue(
				id, randomProductOptionValue());

		ProductOptionValue productOptionValue3 =
			testGetProductOptionIdProductOptionValuesPage_addProductOptionValue(
				id, randomProductOptionValue());

		Page<ProductOptionValue> page1 =
			productOptionValueResource.
				getProductOptionIdProductOptionValuesPage(
					id, Pagination.of(1, 2));

		List<ProductOptionValue> productOptionValues1 =
			(List<ProductOptionValue>)page1.getItems();

		Assert.assertEquals(
			productOptionValues1.toString(), 2, productOptionValues1.size());

		Page<ProductOptionValue> page2 =
			productOptionValueResource.
				getProductOptionIdProductOptionValuesPage(
					id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<ProductOptionValue> productOptionValues2 =
			(List<ProductOptionValue>)page2.getItems();

		Assert.assertEquals(
			productOptionValues2.toString(), 1, productOptionValues2.size());

		Page<ProductOptionValue> page3 =
			productOptionValueResource.
				getProductOptionIdProductOptionValuesPage(
					id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				productOptionValue1, productOptionValue2, productOptionValue3),
			(List<ProductOptionValue>)page3.getItems());
	}

	protected ProductOptionValue
			testGetProductOptionIdProductOptionValuesPage_addProductOptionValue(
				Long id, ProductOptionValue productOptionValue)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductOptionIdProductOptionValuesPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetProductOptionIdProductOptionValuesPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostProductOptionIdProductOptionValue() throws Exception {
		ProductOptionValue randomProductOptionValue =
			randomProductOptionValue();

		ProductOptionValue postProductOptionValue =
			testPostProductOptionIdProductOptionValue_addProductOptionValue(
				randomProductOptionValue);

		assertEquals(randomProductOptionValue, postProductOptionValue);
		assertValid(postProductOptionValue);
	}

	protected ProductOptionValue
			testPostProductOptionIdProductOptionValue_addProductOptionValue(
				ProductOptionValue productOptionValue)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected ProductOptionValue
			testGraphQLProductOptionValue_addProductOptionValue()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		ProductOptionValue productOptionValue,
		List<ProductOptionValue> productOptionValues) {

		boolean contains = false;

		for (ProductOptionValue item : productOptionValues) {
			if (equals(productOptionValue, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			productOptionValues + " does not contain " + productOptionValue,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		ProductOptionValue productOptionValue1,
		ProductOptionValue productOptionValue2) {

		Assert.assertTrue(
			productOptionValue1 + " does not equal " + productOptionValue2,
			equals(productOptionValue1, productOptionValue2));
	}

	protected void assertEquals(
		List<ProductOptionValue> productOptionValues1,
		List<ProductOptionValue> productOptionValues2) {

		Assert.assertEquals(
			productOptionValues1.size(), productOptionValues2.size());

		for (int i = 0; i < productOptionValues1.size(); i++) {
			ProductOptionValue productOptionValue1 = productOptionValues1.get(
				i);
			ProductOptionValue productOptionValue2 = productOptionValues2.get(
				i);

			assertEquals(productOptionValue1, productOptionValue2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<ProductOptionValue> productOptionValues1,
		List<ProductOptionValue> productOptionValues2) {

		Assert.assertEquals(
			productOptionValues1.size(), productOptionValues2.size());

		for (ProductOptionValue productOptionValue1 : productOptionValues1) {
			boolean contains = false;

			for (ProductOptionValue productOptionValue2 :
					productOptionValues2) {

				if (equals(productOptionValue1, productOptionValue2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				productOptionValues2 + " does not contain " +
					productOptionValue1,
				contains);
		}
	}

	protected void assertValid(ProductOptionValue productOptionValue)
		throws Exception {

		boolean valid = true;

		if (productOptionValue.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (productOptionValue.getKey() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (productOptionValue.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (productOptionValue.getPriority() == null) {
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

	protected void assertValid(Page<ProductOptionValue> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<ProductOptionValue> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<ProductOptionValue> productOptionValues =
			page.getItems();

		int size = productOptionValues.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);

		assertValid(page.getActions(), expectedActions);
	}

	protected void assertValid(
		Map<String, Map<String, String>> actions1,
		Map<String, Map<String, String>> actions2) {

		for (String key : actions2.keySet()) {
			Map action = actions1.get(key);

			Assert.assertNotNull(key + " does not contain an action", action);

			Map<String, String> expectedAction = actions2.get(key);

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
					com.liferay.headless.commerce.admin.catalog.dto.v1_0.
						ProductOptionValue.class)) {

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
		ProductOptionValue productOptionValue1,
		ProductOptionValue productOptionValue2) {

		if (productOptionValue1 == productOptionValue2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productOptionValue1.getId(),
						productOptionValue2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("key", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productOptionValue1.getKey(),
						productOptionValue2.getKey())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!equals(
						(Map)productOptionValue1.getName(),
						(Map)productOptionValue2.getName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						productOptionValue1.getPriority(),
						productOptionValue2.getPriority())) {

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

		return TransformUtil.transform(
			ReflectionUtil.getDeclaredFields(clazz),
			field -> {
				if (field.isSynthetic()) {
					return null;
				}

				return field;
			},
			java.lang.reflect.Field.class);
	}

	protected java.util.Collection<EntityField> getEntityFields()
		throws Exception {

		if (!(_productOptionValueResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_productOptionValueResource;

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

		return TransformUtil.transform(
			getEntityFields(),
			entityField -> {
				if (!Objects.equals(entityField.getType(), type) ||
					ArrayUtil.contains(
						getIgnoredEntityFieldNames(), entityField.getName())) {

					return null;
				}

				return entityField;
			});
	}

	protected String getFilterString(
		EntityField entityField, String operator,
		ProductOptionValue productOptionValue) {

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

		if (entityFieldName.equals("key")) {
			sb.append("'");
			sb.append(String.valueOf(productOptionValue.getKey()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("name")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("priority")) {
			sb.append(String.valueOf(productOptionValue.getPriority()));

			return sb.toString();
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

	protected ProductOptionValue randomProductOptionValue() throws Exception {
		return new ProductOptionValue() {
			{
				id = RandomTestUtil.randomLong();
				key = StringUtil.toLowerCase(RandomTestUtil.randomString());
				priority = RandomTestUtil.randomDouble();
			}
		};
	}

	protected ProductOptionValue randomIrrelevantProductOptionValue()
		throws Exception {

		ProductOptionValue randomIrrelevantProductOptionValue =
			randomProductOptionValue();

		return randomIrrelevantProductOptionValue;
	}

	protected ProductOptionValue randomPatchProductOptionValue()
		throws Exception {

		return randomProductOptionValue();
	}

	protected ProductOptionValueResource productOptionValueResource;
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
		LogFactoryUtil.getLog(BaseProductOptionValueResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		ProductOptionValueResource _productOptionValueResource;

}