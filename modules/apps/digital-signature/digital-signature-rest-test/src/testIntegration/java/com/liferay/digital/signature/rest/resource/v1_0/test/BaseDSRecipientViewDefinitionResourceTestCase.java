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

package com.liferay.digital.signature.rest.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.digital.signature.rest.client.dto.v1_0.DSEnvelopeSignatureURL;
import com.liferay.digital.signature.rest.client.dto.v1_0.DSRecipientViewDefinition;
import com.liferay.digital.signature.rest.client.http.HttpInvoker;
import com.liferay.digital.signature.rest.client.pagination.Page;
import com.liferay.digital.signature.rest.client.resource.v1_0.DSRecipientViewDefinitionResource;
import com.liferay.digital.signature.rest.client.serdes.v1_0.DSRecipientViewDefinitionSerDes;
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
 * @author José Abelenda
 * @generated
 */
@Generated("")
public abstract class BaseDSRecipientViewDefinitionResourceTestCase {

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

		_dsRecipientViewDefinitionResource.setContextCompany(testCompany);

		DSRecipientViewDefinitionResource.Builder builder =
			DSRecipientViewDefinitionResource.builder();

		dsRecipientViewDefinitionResource = builder.authentication(
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

		DSRecipientViewDefinition dsRecipientViewDefinition1 =
			randomDSRecipientViewDefinition();

		String json = objectMapper.writeValueAsString(
			dsRecipientViewDefinition1);

		DSRecipientViewDefinition dsRecipientViewDefinition2 =
			DSRecipientViewDefinitionSerDes.toDTO(json);

		Assert.assertTrue(
			equals(dsRecipientViewDefinition1, dsRecipientViewDefinition2));
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

		DSRecipientViewDefinition dsRecipientViewDefinition =
			randomDSRecipientViewDefinition();

		String json1 = objectMapper.writeValueAsString(
			dsRecipientViewDefinition);
		String json2 = DSRecipientViewDefinitionSerDes.toJSON(
			dsRecipientViewDefinition);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		DSRecipientViewDefinition dsRecipientViewDefinition =
			randomDSRecipientViewDefinition();

		dsRecipientViewDefinition.setAuthenticationMethod(regex);
		dsRecipientViewDefinition.setDsClientUserId(regex);
		dsRecipientViewDefinition.setEmailAddress(regex);
		dsRecipientViewDefinition.setReturnURL(regex);
		dsRecipientViewDefinition.setUserName(regex);

		String json = DSRecipientViewDefinitionSerDes.toJSON(
			dsRecipientViewDefinition);

		Assert.assertFalse(json.contains(regex));

		dsRecipientViewDefinition = DSRecipientViewDefinitionSerDes.toDTO(json);

		Assert.assertEquals(
			regex, dsRecipientViewDefinition.getAuthenticationMethod());
		Assert.assertEquals(
			regex, dsRecipientViewDefinition.getDsClientUserId());
		Assert.assertEquals(regex, dsRecipientViewDefinition.getEmailAddress());
		Assert.assertEquals(regex, dsRecipientViewDefinition.getReturnURL());
		Assert.assertEquals(regex, dsRecipientViewDefinition.getUserName());
	}

	@Test
	public void testPostSiteDSRecipientViewDefinition() throws Exception {
		Assert.assertTrue(true);
	}

	protected void assertContains(
		DSRecipientViewDefinition dsRecipientViewDefinition,
		List<DSRecipientViewDefinition> dsRecipientViewDefinitions) {

		boolean contains = false;

		for (DSRecipientViewDefinition item : dsRecipientViewDefinitions) {
			if (equals(dsRecipientViewDefinition, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			dsRecipientViewDefinitions + " does not contain " +
				dsRecipientViewDefinition,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		DSRecipientViewDefinition dsRecipientViewDefinition1,
		DSRecipientViewDefinition dsRecipientViewDefinition2) {

		Assert.assertTrue(
			dsRecipientViewDefinition1 + " does not equal " +
				dsRecipientViewDefinition2,
			equals(dsRecipientViewDefinition1, dsRecipientViewDefinition2));
	}

	protected void assertEquals(
		List<DSRecipientViewDefinition> dsRecipientViewDefinitions1,
		List<DSRecipientViewDefinition> dsRecipientViewDefinitions2) {

		Assert.assertEquals(
			dsRecipientViewDefinitions1.size(),
			dsRecipientViewDefinitions2.size());

		for (int i = 0; i < dsRecipientViewDefinitions1.size(); i++) {
			DSRecipientViewDefinition dsRecipientViewDefinition1 =
				dsRecipientViewDefinitions1.get(i);
			DSRecipientViewDefinition dsRecipientViewDefinition2 =
				dsRecipientViewDefinitions2.get(i);

			assertEquals(
				dsRecipientViewDefinition1, dsRecipientViewDefinition2);
		}
	}

	protected void assertEquals(
		DSEnvelopeSignatureURL dsEnvelopeSignatureURL1,
		DSEnvelopeSignatureURL dsEnvelopeSignatureURL2) {

		Assert.assertTrue(
			dsEnvelopeSignatureURL1 + " does not equal " +
				dsEnvelopeSignatureURL2,
			equals(dsEnvelopeSignatureURL1, dsEnvelopeSignatureURL2));
	}

	protected void assertEqualsIgnoringOrder(
		List<DSRecipientViewDefinition> dsRecipientViewDefinitions1,
		List<DSRecipientViewDefinition> dsRecipientViewDefinitions2) {

		Assert.assertEquals(
			dsRecipientViewDefinitions1.size(),
			dsRecipientViewDefinitions2.size());

		for (DSRecipientViewDefinition dsRecipientViewDefinition1 :
				dsRecipientViewDefinitions1) {

			boolean contains = false;

			for (DSRecipientViewDefinition dsRecipientViewDefinition2 :
					dsRecipientViewDefinitions2) {

				if (equals(
						dsRecipientViewDefinition1,
						dsRecipientViewDefinition2)) {

					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				dsRecipientViewDefinitions2 + " does not contain " +
					dsRecipientViewDefinition1,
				contains);
		}
	}

	protected void assertValid(
			DSRecipientViewDefinition dsRecipientViewDefinition)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"authenticationMethod", additionalAssertFieldName)) {

				if (dsRecipientViewDefinition.getAuthenticationMethod() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("dsClientUserId", additionalAssertFieldName)) {
				if (dsRecipientViewDefinition.getDsClientUserId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("emailAddress", additionalAssertFieldName)) {
				if (dsRecipientViewDefinition.getEmailAddress() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("returnURL", additionalAssertFieldName)) {
				if (dsRecipientViewDefinition.getReturnURL() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("userName", additionalAssertFieldName)) {
				if (dsRecipientViewDefinition.getUserName() == null) {
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

	protected void assertValid(Page<DSRecipientViewDefinition> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<DSRecipientViewDefinition> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<DSRecipientViewDefinition>
			dsRecipientViewDefinitions = page.getItems();

		int size = dsRecipientViewDefinitions.size();

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

	protected void assertValid(DSEnvelopeSignatureURL dsEnvelopeSignatureURL) {
		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalDSEnvelopeSignatureURLAssertFieldNames()) {

			if (Objects.equals("url", additionalAssertFieldName)) {
				if (dsEnvelopeSignatureURL.getUrl() == null) {
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

	protected String[] getAdditionalAssertFieldNames() {
		return new String[0];
	}

	protected String[] getAdditionalDSEnvelopeSignatureURLAssertFieldNames() {
		return new String[0];
	}

	protected List<GraphQLField> getGraphQLFields() throws Exception {
		List<GraphQLField> graphQLFields = new ArrayList<>();

		for (java.lang.reflect.Field field :
				getDeclaredFields(
					com.liferay.digital.signature.rest.dto.v1_0.
						DSRecipientViewDefinition.class)) {

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
		DSRecipientViewDefinition dsRecipientViewDefinition1,
		DSRecipientViewDefinition dsRecipientViewDefinition2) {

		if (dsRecipientViewDefinition1 == dsRecipientViewDefinition2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"authenticationMethod", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						dsRecipientViewDefinition1.getAuthenticationMethod(),
						dsRecipientViewDefinition2.getAuthenticationMethod())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("dsClientUserId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsRecipientViewDefinition1.getDsClientUserId(),
						dsRecipientViewDefinition2.getDsClientUserId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("emailAddress", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsRecipientViewDefinition1.getEmailAddress(),
						dsRecipientViewDefinition2.getEmailAddress())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("returnURL", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsRecipientViewDefinition1.getReturnURL(),
						dsRecipientViewDefinition2.getReturnURL())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("userName", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsRecipientViewDefinition1.getUserName(),
						dsRecipientViewDefinition2.getUserName())) {

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

	protected boolean equals(
		DSEnvelopeSignatureURL dsEnvelopeSignatureURL1,
		DSEnvelopeSignatureURL dsEnvelopeSignatureURL2) {

		if (dsEnvelopeSignatureURL1 == dsEnvelopeSignatureURL2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalDSEnvelopeSignatureURLAssertFieldNames()) {

			if (Objects.equals("url", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						dsEnvelopeSignatureURL1.getUrl(),
						dsEnvelopeSignatureURL2.getUrl())) {

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

		if (!(_dsRecipientViewDefinitionResource instanceof
				EntityModelResource)) {

			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_dsRecipientViewDefinitionResource;

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
		DSRecipientViewDefinition dsRecipientViewDefinition) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("authenticationMethod")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					dsRecipientViewDefinition.getAuthenticationMethod()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("dsClientUserId")) {
			sb.append("'");
			sb.append(
				String.valueOf(dsRecipientViewDefinition.getDsClientUserId()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("emailAddress")) {
			sb.append("'");
			sb.append(
				String.valueOf(dsRecipientViewDefinition.getEmailAddress()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("returnURL")) {
			sb.append("'");
			sb.append(String.valueOf(dsRecipientViewDefinition.getReturnURL()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("userName")) {
			sb.append("'");
			sb.append(String.valueOf(dsRecipientViewDefinition.getUserName()));
			sb.append("'");

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

	protected DSRecipientViewDefinition randomDSRecipientViewDefinition()
		throws Exception {

		return new DSRecipientViewDefinition() {
			{
				authenticationMethod = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				dsClientUserId = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				emailAddress =
					StringUtil.toLowerCase(RandomTestUtil.randomString()) +
						"@liferay.com";
				returnURL = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				userName = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
			}
		};
	}

	protected DSRecipientViewDefinition
			randomIrrelevantDSRecipientViewDefinition()
		throws Exception {

		DSRecipientViewDefinition randomIrrelevantDSRecipientViewDefinition =
			randomDSRecipientViewDefinition();

		return randomIrrelevantDSRecipientViewDefinition;
	}

	protected DSRecipientViewDefinition randomPatchDSRecipientViewDefinition()
		throws Exception {

		return randomDSRecipientViewDefinition();
	}

	protected DSEnvelopeSignatureURL randomDSEnvelopeSignatureURL()
		throws Exception {

		return new DSEnvelopeSignatureURL() {
			{
				url = RandomTestUtil.randomString();
			}
		};
	}

	protected DSRecipientViewDefinitionResource
		dsRecipientViewDefinitionResource;
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
			BaseDSRecipientViewDefinitionResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.digital.signature.rest.resource.v1_0.
		DSRecipientViewDefinitionResource _dsRecipientViewDefinitionResource;

}