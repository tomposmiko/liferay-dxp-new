/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.app.builder.workflow.rest.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.app.builder.workflow.rest.client.dto.v1_0.AppWorkflow;
import com.liferay.app.builder.workflow.rest.client.http.HttpInvoker;
import com.liferay.app.builder.workflow.rest.client.pagination.Page;
import com.liferay.app.builder.workflow.rest.client.resource.v1_0.AppWorkflowResource;
import com.liferay.app.builder.workflow.rest.client.serdes.v1_0.AppWorkflowSerDes;
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
 * @author Rafael Praxedes
 * @generated
 */
@Generated("")
public abstract class BaseAppWorkflowResourceTestCase {

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

		_appWorkflowResource.setContextCompany(testCompany);

		AppWorkflowResource.Builder builder = AppWorkflowResource.builder();

		appWorkflowResource = builder.authentication(
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

		AppWorkflow appWorkflow1 = randomAppWorkflow();

		String json = objectMapper.writeValueAsString(appWorkflow1);

		AppWorkflow appWorkflow2 = AppWorkflowSerDes.toDTO(json);

		Assert.assertTrue(equals(appWorkflow1, appWorkflow2));
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

		AppWorkflow appWorkflow = randomAppWorkflow();

		String json1 = objectMapper.writeValueAsString(appWorkflow);
		String json2 = AppWorkflowSerDes.toJSON(appWorkflow);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		AppWorkflow appWorkflow = randomAppWorkflow();

		appWorkflow.setAppVersion(regex);

		String json = AppWorkflowSerDes.toJSON(appWorkflow);

		Assert.assertFalse(json.contains(regex));

		appWorkflow = AppWorkflowSerDes.toDTO(json);

		Assert.assertEquals(regex, appWorkflow.getAppVersion());
	}

	@Test
	public void testDeleteAppWorkflow() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLDeleteAppWorkflow() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetAppWorkflow() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetAppWorkflow() throws Exception {
		Assert.assertTrue(true);
	}

	@Test
	public void testGraphQLGetAppWorkflowNotFound() throws Exception {
		Assert.assertTrue(true);
	}

	@Test
	public void testPostAppWorkflow() throws Exception {
		AppWorkflow randomAppWorkflow = randomAppWorkflow();

		AppWorkflow postAppWorkflow = testPostAppWorkflow_addAppWorkflow(
			randomAppWorkflow);

		assertEquals(randomAppWorkflow, postAppWorkflow);
		assertValid(postAppWorkflow);
	}

	protected AppWorkflow testPostAppWorkflow_addAppWorkflow(
			AppWorkflow appWorkflow)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPutAppWorkflow() throws Exception {
		Assert.assertTrue(false);
	}

	protected void assertContains(
		AppWorkflow appWorkflow, List<AppWorkflow> appWorkflows) {

		boolean contains = false;

		for (AppWorkflow item : appWorkflows) {
			if (equals(appWorkflow, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			appWorkflows + " does not contain " + appWorkflow, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		AppWorkflow appWorkflow1, AppWorkflow appWorkflow2) {

		Assert.assertTrue(
			appWorkflow1 + " does not equal " + appWorkflow2,
			equals(appWorkflow1, appWorkflow2));
	}

	protected void assertEquals(
		List<AppWorkflow> appWorkflows1, List<AppWorkflow> appWorkflows2) {

		Assert.assertEquals(appWorkflows1.size(), appWorkflows2.size());

		for (int i = 0; i < appWorkflows1.size(); i++) {
			AppWorkflow appWorkflow1 = appWorkflows1.get(i);
			AppWorkflow appWorkflow2 = appWorkflows2.get(i);

			assertEquals(appWorkflow1, appWorkflow2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<AppWorkflow> appWorkflows1, List<AppWorkflow> appWorkflows2) {

		Assert.assertEquals(appWorkflows1.size(), appWorkflows2.size());

		for (AppWorkflow appWorkflow1 : appWorkflows1) {
			boolean contains = false;

			for (AppWorkflow appWorkflow2 : appWorkflows2) {
				if (equals(appWorkflow1, appWorkflow2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				appWorkflows2 + " does not contain " + appWorkflow1, contains);
		}
	}

	protected void assertValid(AppWorkflow appWorkflow) throws Exception {
		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("appId", additionalAssertFieldName)) {
				if (appWorkflow.getAppId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("appVersion", additionalAssertFieldName)) {
				if (appWorkflow.getAppVersion() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"appWorkflowDefinitionId", additionalAssertFieldName)) {

				if (appWorkflow.getAppWorkflowDefinitionId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"appWorkflowStates", additionalAssertFieldName)) {

				if (appWorkflow.getAppWorkflowStates() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("appWorkflowTasks", additionalAssertFieldName)) {
				if (appWorkflow.getAppWorkflowTasks() == null) {
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

	protected void assertValid(Page<AppWorkflow> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<AppWorkflow> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<AppWorkflow> appWorkflows = page.getItems();

		int size = appWorkflows.size();

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
					com.liferay.app.builder.workflow.rest.dto.v1_0.AppWorkflow.
						class)) {

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
		AppWorkflow appWorkflow1, AppWorkflow appWorkflow2) {

		if (appWorkflow1 == appWorkflow2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("appId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						appWorkflow1.getAppId(), appWorkflow2.getAppId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("appVersion", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						appWorkflow1.getAppVersion(),
						appWorkflow2.getAppVersion())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"appWorkflowDefinitionId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						appWorkflow1.getAppWorkflowDefinitionId(),
						appWorkflow2.getAppWorkflowDefinitionId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"appWorkflowStates", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						appWorkflow1.getAppWorkflowStates(),
						appWorkflow2.getAppWorkflowStates())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("appWorkflowTasks", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						appWorkflow1.getAppWorkflowTasks(),
						appWorkflow2.getAppWorkflowTasks())) {

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

		if (!(_appWorkflowResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_appWorkflowResource;

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
		EntityField entityField, String operator, AppWorkflow appWorkflow) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("appId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("appVersion")) {
			sb.append("'");
			sb.append(String.valueOf(appWorkflow.getAppVersion()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("appWorkflowDefinitionId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("appWorkflowStates")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("appWorkflowTasks")) {
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

	protected AppWorkflow randomAppWorkflow() throws Exception {
		return new AppWorkflow() {
			{
				appId = RandomTestUtil.randomLong();
				appVersion = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				appWorkflowDefinitionId = RandomTestUtil.randomLong();
			}
		};
	}

	protected AppWorkflow randomIrrelevantAppWorkflow() throws Exception {
		AppWorkflow randomIrrelevantAppWorkflow = randomAppWorkflow();

		return randomIrrelevantAppWorkflow;
	}

	protected AppWorkflow randomPatchAppWorkflow() throws Exception {
		return randomAppWorkflow();
	}

	protected AppWorkflowResource appWorkflowResource;
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
		LogFactoryUtil.getLog(BaseAppWorkflowResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private
		com.liferay.app.builder.workflow.rest.resource.v1_0.AppWorkflowResource
			_appWorkflowResource;

}