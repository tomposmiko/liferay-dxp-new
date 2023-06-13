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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.SkuSubscriptionConfiguration;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.SkuSubscriptionConfigurationResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.SkuSubscriptionConfigurationSerDes;
import com.liferay.petra.function.transform.TransformUtil;
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
public abstract class BaseSkuSubscriptionConfigurationResourceTestCase {

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

		_skuSubscriptionConfigurationResource.setContextCompany(testCompany);

		SkuSubscriptionConfigurationResource.Builder builder =
			SkuSubscriptionConfigurationResource.builder();

		skuSubscriptionConfigurationResource = builder.authentication(
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

		SkuSubscriptionConfiguration skuSubscriptionConfiguration1 =
			randomSkuSubscriptionConfiguration();

		String json = objectMapper.writeValueAsString(
			skuSubscriptionConfiguration1);

		SkuSubscriptionConfiguration skuSubscriptionConfiguration2 =
			SkuSubscriptionConfigurationSerDes.toDTO(json);

		Assert.assertTrue(
			equals(
				skuSubscriptionConfiguration1, skuSubscriptionConfiguration2));
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

		SkuSubscriptionConfiguration skuSubscriptionConfiguration =
			randomSkuSubscriptionConfiguration();

		String json1 = objectMapper.writeValueAsString(
			skuSubscriptionConfiguration);
		String json2 = SkuSubscriptionConfigurationSerDes.toJSON(
			skuSubscriptionConfiguration);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		SkuSubscriptionConfiguration skuSubscriptionConfiguration =
			randomSkuSubscriptionConfiguration();

		String json = SkuSubscriptionConfigurationSerDes.toJSON(
			skuSubscriptionConfiguration);

		Assert.assertFalse(json.contains(regex));

		skuSubscriptionConfiguration = SkuSubscriptionConfigurationSerDes.toDTO(
			json);
	}

	@Test
	public void testGetSkuByExternalReferenceCodeSkuSubscriptionConfiguration()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetSkuByExternalReferenceCodeSkuSubscriptionConfiguration()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testGraphQLGetSkuByExternalReferenceCodeSkuSubscriptionConfigurationNotFound()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testGetIdSkuSubscriptionConfiguration() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetIdSkuSubscriptionConfiguration()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testGraphQLGetIdSkuSubscriptionConfigurationNotFound()
		throws Exception {

		Assert.assertTrue(true);
	}

	protected void assertContains(
		SkuSubscriptionConfiguration skuSubscriptionConfiguration,
		List<SkuSubscriptionConfiguration> skuSubscriptionConfigurations) {

		boolean contains = false;

		for (SkuSubscriptionConfiguration item :
				skuSubscriptionConfigurations) {

			if (equals(skuSubscriptionConfiguration, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			skuSubscriptionConfigurations + " does not contain " +
				skuSubscriptionConfiguration,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		SkuSubscriptionConfiguration skuSubscriptionConfiguration1,
		SkuSubscriptionConfiguration skuSubscriptionConfiguration2) {

		Assert.assertTrue(
			skuSubscriptionConfiguration1 + " does not equal " +
				skuSubscriptionConfiguration2,
			equals(
				skuSubscriptionConfiguration1, skuSubscriptionConfiguration2));
	}

	protected void assertEquals(
		List<SkuSubscriptionConfiguration> skuSubscriptionConfigurations1,
		List<SkuSubscriptionConfiguration> skuSubscriptionConfigurations2) {

		Assert.assertEquals(
			skuSubscriptionConfigurations1.size(),
			skuSubscriptionConfigurations2.size());

		for (int i = 0; i < skuSubscriptionConfigurations1.size(); i++) {
			SkuSubscriptionConfiguration skuSubscriptionConfiguration1 =
				skuSubscriptionConfigurations1.get(i);
			SkuSubscriptionConfiguration skuSubscriptionConfiguration2 =
				skuSubscriptionConfigurations2.get(i);

			assertEquals(
				skuSubscriptionConfiguration1, skuSubscriptionConfiguration2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<SkuSubscriptionConfiguration> skuSubscriptionConfigurations1,
		List<SkuSubscriptionConfiguration> skuSubscriptionConfigurations2) {

		Assert.assertEquals(
			skuSubscriptionConfigurations1.size(),
			skuSubscriptionConfigurations2.size());

		for (SkuSubscriptionConfiguration skuSubscriptionConfiguration1 :
				skuSubscriptionConfigurations1) {

			boolean contains = false;

			for (SkuSubscriptionConfiguration skuSubscriptionConfiguration2 :
					skuSubscriptionConfigurations2) {

				if (equals(
						skuSubscriptionConfiguration1,
						skuSubscriptionConfiguration2)) {

					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				skuSubscriptionConfigurations2 + " does not contain " +
					skuSubscriptionConfiguration1,
				contains);
		}
	}

	protected void assertValid(
			SkuSubscriptionConfiguration skuSubscriptionConfiguration)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"deliverySubscriptionEnable", additionalAssertFieldName)) {

				if (skuSubscriptionConfiguration.
						getDeliverySubscriptionEnable() == null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"deliverySubscriptionLength", additionalAssertFieldName)) {

				if (skuSubscriptionConfiguration.
						getDeliverySubscriptionLength() == null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"deliverySubscriptionNumberOfLength",
					additionalAssertFieldName)) {

				if (skuSubscriptionConfiguration.
						getDeliverySubscriptionNumberOfLength() == null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"deliverySubscriptionType", additionalAssertFieldName)) {

				if (skuSubscriptionConfiguration.
						getDeliverySubscriptionType() == null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"deliverySubscriptionTypeSettings",
					additionalAssertFieldName)) {

				if (skuSubscriptionConfiguration.
						getDeliverySubscriptionTypeSettings() == null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("enable", additionalAssertFieldName)) {
				if (skuSubscriptionConfiguration.getEnable() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("length", additionalAssertFieldName)) {
				if (skuSubscriptionConfiguration.getLength() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("numberOfLength", additionalAssertFieldName)) {
				if (skuSubscriptionConfiguration.getNumberOfLength() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"overrideSubscriptionInfo", additionalAssertFieldName)) {

				if (skuSubscriptionConfiguration.
						getOverrideSubscriptionInfo() == null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("subscriptionType", additionalAssertFieldName)) {
				if (skuSubscriptionConfiguration.getSubscriptionType() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"subscriptionTypeSettings", additionalAssertFieldName)) {

				if (skuSubscriptionConfiguration.
						getSubscriptionTypeSettings() == null) {

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

	protected void assertValid(Page<SkuSubscriptionConfiguration> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<SkuSubscriptionConfiguration> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<SkuSubscriptionConfiguration>
			skuSubscriptionConfigurations = page.getItems();

		int size = skuSubscriptionConfigurations.size();

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
					com.liferay.headless.commerce.admin.catalog.dto.v1_0.
						SkuSubscriptionConfiguration.class)) {

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
		SkuSubscriptionConfiguration skuSubscriptionConfiguration1,
		SkuSubscriptionConfiguration skuSubscriptionConfiguration2) {

		if (skuSubscriptionConfiguration1 == skuSubscriptionConfiguration2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"deliverySubscriptionEnable", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						skuSubscriptionConfiguration1.
							getDeliverySubscriptionEnable(),
						skuSubscriptionConfiguration2.
							getDeliverySubscriptionEnable())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"deliverySubscriptionLength", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						skuSubscriptionConfiguration1.
							getDeliverySubscriptionLength(),
						skuSubscriptionConfiguration2.
							getDeliverySubscriptionLength())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"deliverySubscriptionNumberOfLength",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						skuSubscriptionConfiguration1.
							getDeliverySubscriptionNumberOfLength(),
						skuSubscriptionConfiguration2.
							getDeliverySubscriptionNumberOfLength())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"deliverySubscriptionType", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						skuSubscriptionConfiguration1.
							getDeliverySubscriptionType(),
						skuSubscriptionConfiguration2.
							getDeliverySubscriptionType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"deliverySubscriptionTypeSettings",
					additionalAssertFieldName)) {

				if (!equals(
						(Map)
							skuSubscriptionConfiguration1.
								getDeliverySubscriptionTypeSettings(),
						(Map)
							skuSubscriptionConfiguration2.
								getDeliverySubscriptionTypeSettings())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("enable", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuSubscriptionConfiguration1.getEnable(),
						skuSubscriptionConfiguration2.getEnable())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("length", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuSubscriptionConfiguration1.getLength(),
						skuSubscriptionConfiguration2.getLength())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("numberOfLength", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuSubscriptionConfiguration1.getNumberOfLength(),
						skuSubscriptionConfiguration2.getNumberOfLength())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"overrideSubscriptionInfo", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						skuSubscriptionConfiguration1.
							getOverrideSubscriptionInfo(),
						skuSubscriptionConfiguration2.
							getOverrideSubscriptionInfo())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("subscriptionType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuSubscriptionConfiguration1.getSubscriptionType(),
						skuSubscriptionConfiguration2.getSubscriptionType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"subscriptionTypeSettings", additionalAssertFieldName)) {

				if (!equals(
						(Map)
							skuSubscriptionConfiguration1.
								getSubscriptionTypeSettings(),
						(Map)
							skuSubscriptionConfiguration2.
								getSubscriptionTypeSettings())) {

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

		if (!(_skuSubscriptionConfigurationResource instanceof
				EntityModelResource)) {

			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_skuSubscriptionConfigurationResource;

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
		SkuSubscriptionConfiguration skuSubscriptionConfiguration) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("deliverySubscriptionEnable")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("deliverySubscriptionLength")) {
			sb.append(
				String.valueOf(
					skuSubscriptionConfiguration.
						getDeliverySubscriptionLength()));

			return sb.toString();
		}

		if (entityFieldName.equals("deliverySubscriptionNumberOfLength")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("deliverySubscriptionType")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("deliverySubscriptionTypeSettings")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("enable")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("length")) {
			sb.append(String.valueOf(skuSubscriptionConfiguration.getLength()));

			return sb.toString();
		}

		if (entityFieldName.equals("numberOfLength")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("overrideSubscriptionInfo")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("subscriptionType")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("subscriptionTypeSettings")) {
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

	protected SkuSubscriptionConfiguration randomSkuSubscriptionConfiguration()
		throws Exception {

		return new SkuSubscriptionConfiguration() {
			{
				deliverySubscriptionEnable = RandomTestUtil.randomBoolean();
				deliverySubscriptionLength = RandomTestUtil.randomInt();
				deliverySubscriptionNumberOfLength =
					RandomTestUtil.randomLong();
				enable = RandomTestUtil.randomBoolean();
				length = RandomTestUtil.randomInt();
				numberOfLength = RandomTestUtil.randomLong();
				overrideSubscriptionInfo = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected SkuSubscriptionConfiguration
			randomIrrelevantSkuSubscriptionConfiguration()
		throws Exception {

		SkuSubscriptionConfiguration
			randomIrrelevantSkuSubscriptionConfiguration =
				randomSkuSubscriptionConfiguration();

		return randomIrrelevantSkuSubscriptionConfiguration;
	}

	protected SkuSubscriptionConfiguration
			randomPatchSkuSubscriptionConfiguration()
		throws Exception {

		return randomSkuSubscriptionConfiguration();
	}

	protected SkuSubscriptionConfigurationResource
		skuSubscriptionConfigurationResource;
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
		LogFactoryUtil.getLog(
			BaseSkuSubscriptionConfigurationResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		SkuSubscriptionConfigurationResource
			_skuSubscriptionConfigurationResource;

}