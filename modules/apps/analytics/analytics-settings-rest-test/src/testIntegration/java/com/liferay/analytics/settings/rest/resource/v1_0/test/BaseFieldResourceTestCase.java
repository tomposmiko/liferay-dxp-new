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

import com.liferay.analytics.settings.rest.client.dto.v1_0.Field;
import com.liferay.analytics.settings.rest.client.http.HttpInvoker;
import com.liferay.analytics.settings.rest.client.pagination.Page;
import com.liferay.analytics.settings.rest.client.pagination.Pagination;
import com.liferay.analytics.settings.rest.client.resource.v1_0.FieldResource;
import com.liferay.analytics.settings.rest.client.serdes.v1_0.FieldSerDes;
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
public abstract class BaseFieldResourceTestCase {

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

		_fieldResource.setContextCompany(testCompany);

		FieldResource.Builder builder = FieldResource.builder();

		fieldResource = builder.authentication(
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

		Field field1 = randomField();

		String json = objectMapper.writeValueAsString(field1);

		Field field2 = FieldSerDes.toDTO(json);

		Assert.assertTrue(equals(field1, field2));
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

		Field field = randomField();

		String json1 = objectMapper.writeValueAsString(field);
		String json2 = FieldSerDes.toJSON(field);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		Field field = randomField();

		field.setExample(regex);
		field.setName(regex);
		field.setSource(regex);
		field.setType(regex);

		String json = FieldSerDes.toJSON(field);

		Assert.assertFalse(json.contains(regex));

		field = FieldSerDes.toDTO(json);

		Assert.assertEquals(regex, field.getExample());
		Assert.assertEquals(regex, field.getName());
		Assert.assertEquals(regex, field.getSource());
		Assert.assertEquals(regex, field.getType());
	}

	@Test
	public void testGetFieldsAccountsPage() throws Exception {
		Page<Field> page = fieldResource.getFieldsAccountsPage(
			RandomTestUtil.randomString(), Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		Field field1 = testGetFieldsAccountsPage_addField(randomField());

		Field field2 = testGetFieldsAccountsPage_addField(randomField());

		page = fieldResource.getFieldsAccountsPage(
			null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(field1, (List<Field>)page.getItems());
		assertContains(field2, (List<Field>)page.getItems());
		assertValid(page, testGetFieldsAccountsPage_getExpectedActions());
	}

	protected Map<String, Map> testGetFieldsAccountsPage_getExpectedActions()
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetFieldsAccountsPageWithPagination() throws Exception {
		Page<Field> totalPage = fieldResource.getFieldsAccountsPage(
			null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		Field field1 = testGetFieldsAccountsPage_addField(randomField());

		Field field2 = testGetFieldsAccountsPage_addField(randomField());

		Field field3 = testGetFieldsAccountsPage_addField(randomField());

		Page<Field> page1 = fieldResource.getFieldsAccountsPage(
			null, Pagination.of(1, totalCount + 2), null);

		List<Field> fields1 = (List<Field>)page1.getItems();

		Assert.assertEquals(fields1.toString(), totalCount + 2, fields1.size());

		Page<Field> page2 = fieldResource.getFieldsAccountsPage(
			null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<Field> fields2 = (List<Field>)page2.getItems();

		Assert.assertEquals(fields2.toString(), 1, fields2.size());

		Page<Field> page3 = fieldResource.getFieldsAccountsPage(
			null, Pagination.of(1, totalCount + 3), null);

		assertContains(field1, (List<Field>)page3.getItems());
		assertContains(field2, (List<Field>)page3.getItems());
		assertContains(field3, (List<Field>)page3.getItems());
	}

	@Test
	public void testGetFieldsAccountsPageWithSortDateTime() throws Exception {
		testGetFieldsAccountsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, field1, field2) -> {
				BeanTestUtil.setProperty(
					field1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetFieldsAccountsPageWithSortDouble() throws Exception {
		testGetFieldsAccountsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, field1, field2) -> {
				BeanTestUtil.setProperty(field1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(field2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetFieldsAccountsPageWithSortInteger() throws Exception {
		testGetFieldsAccountsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, field1, field2) -> {
				BeanTestUtil.setProperty(field1, entityField.getName(), 0);
				BeanTestUtil.setProperty(field2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetFieldsAccountsPageWithSortString() throws Exception {
		testGetFieldsAccountsPageWithSort(
			EntityField.Type.STRING,
			(entityField, field1, field2) -> {
				Class<?> clazz = field1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						field1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						field2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						field1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						field2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						field1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						field2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetFieldsAccountsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Field, Field, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Field field1 = randomField();
		Field field2 = randomField();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, field1, field2);
		}

		field1 = testGetFieldsAccountsPage_addField(field1);

		field2 = testGetFieldsAccountsPage_addField(field2);

		for (EntityField entityField : entityFields) {
			Page<Field> ascPage = fieldResource.getFieldsAccountsPage(
				null, Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(field1, field2), (List<Field>)ascPage.getItems());

			Page<Field> descPage = fieldResource.getFieldsAccountsPage(
				null, Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(field2, field1),
				(List<Field>)descPage.getItems());
		}
	}

	protected Field testGetFieldsAccountsPage_addField(Field field)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPatchFieldAccount() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetFieldsOrdersPage() throws Exception {
		Page<Field> page = fieldResource.getFieldsOrdersPage(
			RandomTestUtil.randomString(), Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		Field field1 = testGetFieldsOrdersPage_addField(randomField());

		Field field2 = testGetFieldsOrdersPage_addField(randomField());

		page = fieldResource.getFieldsOrdersPage(
			null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(field1, (List<Field>)page.getItems());
		assertContains(field2, (List<Field>)page.getItems());
		assertValid(page, testGetFieldsOrdersPage_getExpectedActions());
	}

	protected Map<String, Map> testGetFieldsOrdersPage_getExpectedActions()
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetFieldsOrdersPageWithPagination() throws Exception {
		Page<Field> totalPage = fieldResource.getFieldsOrdersPage(
			null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		Field field1 = testGetFieldsOrdersPage_addField(randomField());

		Field field2 = testGetFieldsOrdersPage_addField(randomField());

		Field field3 = testGetFieldsOrdersPage_addField(randomField());

		Page<Field> page1 = fieldResource.getFieldsOrdersPage(
			null, Pagination.of(1, totalCount + 2), null);

		List<Field> fields1 = (List<Field>)page1.getItems();

		Assert.assertEquals(fields1.toString(), totalCount + 2, fields1.size());

		Page<Field> page2 = fieldResource.getFieldsOrdersPage(
			null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<Field> fields2 = (List<Field>)page2.getItems();

		Assert.assertEquals(fields2.toString(), 1, fields2.size());

		Page<Field> page3 = fieldResource.getFieldsOrdersPage(
			null, Pagination.of(1, totalCount + 3), null);

		assertContains(field1, (List<Field>)page3.getItems());
		assertContains(field2, (List<Field>)page3.getItems());
		assertContains(field3, (List<Field>)page3.getItems());
	}

	@Test
	public void testGetFieldsOrdersPageWithSortDateTime() throws Exception {
		testGetFieldsOrdersPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, field1, field2) -> {
				BeanTestUtil.setProperty(
					field1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetFieldsOrdersPageWithSortDouble() throws Exception {
		testGetFieldsOrdersPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, field1, field2) -> {
				BeanTestUtil.setProperty(field1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(field2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetFieldsOrdersPageWithSortInteger() throws Exception {
		testGetFieldsOrdersPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, field1, field2) -> {
				BeanTestUtil.setProperty(field1, entityField.getName(), 0);
				BeanTestUtil.setProperty(field2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetFieldsOrdersPageWithSortString() throws Exception {
		testGetFieldsOrdersPageWithSort(
			EntityField.Type.STRING,
			(entityField, field1, field2) -> {
				Class<?> clazz = field1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						field1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						field2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						field1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						field2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						field1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						field2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetFieldsOrdersPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Field, Field, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Field field1 = randomField();
		Field field2 = randomField();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, field1, field2);
		}

		field1 = testGetFieldsOrdersPage_addField(field1);

		field2 = testGetFieldsOrdersPage_addField(field2);

		for (EntityField entityField : entityFields) {
			Page<Field> ascPage = fieldResource.getFieldsOrdersPage(
				null, Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(field1, field2), (List<Field>)ascPage.getItems());

			Page<Field> descPage = fieldResource.getFieldsOrdersPage(
				null, Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(field2, field1),
				(List<Field>)descPage.getItems());
		}
	}

	protected Field testGetFieldsOrdersPage_addField(Field field)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPatchFieldOrder() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetFieldsPeoplePage() throws Exception {
		Page<Field> page = fieldResource.getFieldsPeoplePage(
			RandomTestUtil.randomString(), Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		Field field1 = testGetFieldsPeoplePage_addField(randomField());

		Field field2 = testGetFieldsPeoplePage_addField(randomField());

		page = fieldResource.getFieldsPeoplePage(
			null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(field1, (List<Field>)page.getItems());
		assertContains(field2, (List<Field>)page.getItems());
		assertValid(page, testGetFieldsPeoplePage_getExpectedActions());
	}

	protected Map<String, Map> testGetFieldsPeoplePage_getExpectedActions()
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetFieldsPeoplePageWithPagination() throws Exception {
		Page<Field> totalPage = fieldResource.getFieldsPeoplePage(
			null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		Field field1 = testGetFieldsPeoplePage_addField(randomField());

		Field field2 = testGetFieldsPeoplePage_addField(randomField());

		Field field3 = testGetFieldsPeoplePage_addField(randomField());

		Page<Field> page1 = fieldResource.getFieldsPeoplePage(
			null, Pagination.of(1, totalCount + 2), null);

		List<Field> fields1 = (List<Field>)page1.getItems();

		Assert.assertEquals(fields1.toString(), totalCount + 2, fields1.size());

		Page<Field> page2 = fieldResource.getFieldsPeoplePage(
			null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<Field> fields2 = (List<Field>)page2.getItems();

		Assert.assertEquals(fields2.toString(), 1, fields2.size());

		Page<Field> page3 = fieldResource.getFieldsPeoplePage(
			null, Pagination.of(1, totalCount + 3), null);

		assertContains(field1, (List<Field>)page3.getItems());
		assertContains(field2, (List<Field>)page3.getItems());
		assertContains(field3, (List<Field>)page3.getItems());
	}

	@Test
	public void testGetFieldsPeoplePageWithSortDateTime() throws Exception {
		testGetFieldsPeoplePageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, field1, field2) -> {
				BeanTestUtil.setProperty(
					field1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetFieldsPeoplePageWithSortDouble() throws Exception {
		testGetFieldsPeoplePageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, field1, field2) -> {
				BeanTestUtil.setProperty(field1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(field2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetFieldsPeoplePageWithSortInteger() throws Exception {
		testGetFieldsPeoplePageWithSort(
			EntityField.Type.INTEGER,
			(entityField, field1, field2) -> {
				BeanTestUtil.setProperty(field1, entityField.getName(), 0);
				BeanTestUtil.setProperty(field2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetFieldsPeoplePageWithSortString() throws Exception {
		testGetFieldsPeoplePageWithSort(
			EntityField.Type.STRING,
			(entityField, field1, field2) -> {
				Class<?> clazz = field1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						field1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						field2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						field1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						field2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						field1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						field2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetFieldsPeoplePageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Field, Field, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Field field1 = randomField();
		Field field2 = randomField();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, field1, field2);
		}

		field1 = testGetFieldsPeoplePage_addField(field1);

		field2 = testGetFieldsPeoplePage_addField(field2);

		for (EntityField entityField : entityFields) {
			Page<Field> ascPage = fieldResource.getFieldsPeoplePage(
				null, Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(field1, field2), (List<Field>)ascPage.getItems());

			Page<Field> descPage = fieldResource.getFieldsPeoplePage(
				null, Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(field2, field1),
				(List<Field>)descPage.getItems());
		}
	}

	protected Field testGetFieldsPeoplePage_addField(Field field)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPatchFieldPeople() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetFieldsProductsPage() throws Exception {
		Page<Field> page = fieldResource.getFieldsProductsPage(
			RandomTestUtil.randomString(), Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		Field field1 = testGetFieldsProductsPage_addField(randomField());

		Field field2 = testGetFieldsProductsPage_addField(randomField());

		page = fieldResource.getFieldsProductsPage(
			null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(field1, (List<Field>)page.getItems());
		assertContains(field2, (List<Field>)page.getItems());
		assertValid(page, testGetFieldsProductsPage_getExpectedActions());
	}

	protected Map<String, Map> testGetFieldsProductsPage_getExpectedActions()
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetFieldsProductsPageWithPagination() throws Exception {
		Page<Field> totalPage = fieldResource.getFieldsProductsPage(
			null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		Field field1 = testGetFieldsProductsPage_addField(randomField());

		Field field2 = testGetFieldsProductsPage_addField(randomField());

		Field field3 = testGetFieldsProductsPage_addField(randomField());

		Page<Field> page1 = fieldResource.getFieldsProductsPage(
			null, Pagination.of(1, totalCount + 2), null);

		List<Field> fields1 = (List<Field>)page1.getItems();

		Assert.assertEquals(fields1.toString(), totalCount + 2, fields1.size());

		Page<Field> page2 = fieldResource.getFieldsProductsPage(
			null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<Field> fields2 = (List<Field>)page2.getItems();

		Assert.assertEquals(fields2.toString(), 1, fields2.size());

		Page<Field> page3 = fieldResource.getFieldsProductsPage(
			null, Pagination.of(1, totalCount + 3), null);

		assertContains(field1, (List<Field>)page3.getItems());
		assertContains(field2, (List<Field>)page3.getItems());
		assertContains(field3, (List<Field>)page3.getItems());
	}

	@Test
	public void testGetFieldsProductsPageWithSortDateTime() throws Exception {
		testGetFieldsProductsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, field1, field2) -> {
				BeanTestUtil.setProperty(
					field1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetFieldsProductsPageWithSortDouble() throws Exception {
		testGetFieldsProductsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, field1, field2) -> {
				BeanTestUtil.setProperty(field1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(field2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetFieldsProductsPageWithSortInteger() throws Exception {
		testGetFieldsProductsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, field1, field2) -> {
				BeanTestUtil.setProperty(field1, entityField.getName(), 0);
				BeanTestUtil.setProperty(field2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetFieldsProductsPageWithSortString() throws Exception {
		testGetFieldsProductsPageWithSort(
			EntityField.Type.STRING,
			(entityField, field1, field2) -> {
				Class<?> clazz = field1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						field1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						field2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						field1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						field2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						field1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						field2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetFieldsProductsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer<EntityField, Field, Field, Exception>
				unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Field field1 = randomField();
		Field field2 = randomField();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(entityField, field1, field2);
		}

		field1 = testGetFieldsProductsPage_addField(field1);

		field2 = testGetFieldsProductsPage_addField(field2);

		for (EntityField entityField : entityFields) {
			Page<Field> ascPage = fieldResource.getFieldsProductsPage(
				null, Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(field1, field2), (List<Field>)ascPage.getItems());

			Page<Field> descPage = fieldResource.getFieldsProductsPage(
				null, Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(field2, field1),
				(List<Field>)descPage.getItems());
		}
	}

	protected Field testGetFieldsProductsPage_addField(Field field)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPatchFieldProduct() throws Exception {
		Assert.assertTrue(false);
	}

	protected void assertContains(Field field, List<Field> fields) {
		boolean contains = false;

		for (Field item : fields) {
			if (equals(field, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(fields + " does not contain " + field, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(Field field1, Field field2) {
		Assert.assertTrue(
			field1 + " does not equal " + field2, equals(field1, field2));
	}

	protected void assertEquals(List<Field> fields1, List<Field> fields2) {
		Assert.assertEquals(fields1.size(), fields2.size());

		for (int i = 0; i < fields1.size(); i++) {
			Field field1 = fields1.get(i);
			Field field2 = fields2.get(i);

			assertEquals(field1, field2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<Field> fields1, List<Field> fields2) {

		Assert.assertEquals(fields1.size(), fields2.size());

		for (Field field1 : fields1) {
			boolean contains = false;

			for (Field field2 : fields2) {
				if (equals(field1, field2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				fields2 + " does not contain " + field1, contains);
		}
	}

	protected void assertValid(Field field) throws Exception {
		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("example", additionalAssertFieldName)) {
				if (field.getExample() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (field.getName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("required", additionalAssertFieldName)) {
				if (field.getRequired() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("selected", additionalAssertFieldName)) {
				if (field.getSelected() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("source", additionalAssertFieldName)) {
				if (field.getSource() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (field.getType() == null) {
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

	protected void assertValid(Page<Field> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<Field> page, Map<String, Map> expectedActions) {

		boolean valid = false;

		java.util.Collection<Field> fields = page.getItems();

		int size = fields.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);

		Map<String, Map> actions = page.getActions();

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
					com.liferay.analytics.settings.rest.dto.v1_0.Field.class)) {

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

	protected boolean equals(Field field1, Field field2) {
		if (field1 == field2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("example", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						field1.getExample(), field2.getExample())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(field1.getName(), field2.getName())) {
					return false;
				}

				continue;
			}

			if (Objects.equals("required", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						field1.getRequired(), field2.getRequired())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("selected", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						field1.getSelected(), field2.getSelected())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("source", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						field1.getSource(), field2.getSource())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("type", additionalAssertFieldName)) {
				if (!Objects.deepEquals(field1.getType(), field2.getType())) {
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

		if (!(_fieldResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_fieldResource;

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
		EntityField entityField, String operator, Field field) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("example")) {
			sb.append("'");
			sb.append(String.valueOf(field.getExample()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(field.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("required")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("selected")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("source")) {
			sb.append("'");
			sb.append(String.valueOf(field.getSource()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("type")) {
			sb.append("'");
			sb.append(String.valueOf(field.getType()));
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

	protected Field randomField() throws Exception {
		return new Field() {
			{
				example = StringUtil.toLowerCase(RandomTestUtil.randomString());
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
				required = RandomTestUtil.randomBoolean();
				selected = RandomTestUtil.randomBoolean();
				source = StringUtil.toLowerCase(RandomTestUtil.randomString());
				type = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected Field randomIrrelevantField() throws Exception {
		Field randomIrrelevantField = randomField();

		return randomIrrelevantField;
	}

	protected Field randomPatchField() throws Exception {
		return randomField();
	}

	protected FieldResource fieldResource;
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
		LogFactoryUtil.getLog(BaseFieldResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.analytics.settings.rest.resource.v1_0.FieldResource
		_fieldResource;

}