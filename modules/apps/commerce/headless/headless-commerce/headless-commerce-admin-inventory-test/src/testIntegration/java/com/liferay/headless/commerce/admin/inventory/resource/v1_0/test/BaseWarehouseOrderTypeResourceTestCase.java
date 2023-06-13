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

package com.liferay.headless.commerce.admin.inventory.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.inventory.client.dto.v1_0.WarehouseOrderType;
import com.liferay.headless.commerce.admin.inventory.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.inventory.client.pagination.Page;
import com.liferay.headless.commerce.admin.inventory.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.inventory.client.resource.v1_0.WarehouseOrderTypeResource;
import com.liferay.headless.commerce.admin.inventory.client.serdes.v1_0.WarehouseOrderTypeSerDes;
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
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.search.test.util.SearchTestRule;
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
 * @author Alessio Antonio Rendina
 * @generated
 */
@Generated("")
public abstract class BaseWarehouseOrderTypeResourceTestCase {

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

		_warehouseOrderTypeResource.setContextCompany(testCompany);

		WarehouseOrderTypeResource.Builder builder =
			WarehouseOrderTypeResource.builder();

		warehouseOrderTypeResource = builder.authentication(
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

		WarehouseOrderType warehouseOrderType1 = randomWarehouseOrderType();

		String json = objectMapper.writeValueAsString(warehouseOrderType1);

		WarehouseOrderType warehouseOrderType2 = WarehouseOrderTypeSerDes.toDTO(
			json);

		Assert.assertTrue(equals(warehouseOrderType1, warehouseOrderType2));
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

		WarehouseOrderType warehouseOrderType = randomWarehouseOrderType();

		String json1 = objectMapper.writeValueAsString(warehouseOrderType);
		String json2 = WarehouseOrderTypeSerDes.toJSON(warehouseOrderType);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		WarehouseOrderType warehouseOrderType = randomWarehouseOrderType();

		warehouseOrderType.setOrderTypeExternalReferenceCode(regex);
		warehouseOrderType.setWarehouseExternalReferenceCode(regex);

		String json = WarehouseOrderTypeSerDes.toJSON(warehouseOrderType);

		Assert.assertFalse(json.contains(regex));

		warehouseOrderType = WarehouseOrderTypeSerDes.toDTO(json);

		Assert.assertEquals(
			regex, warehouseOrderType.getOrderTypeExternalReferenceCode());
		Assert.assertEquals(
			regex, warehouseOrderType.getWarehouseExternalReferenceCode());
	}

	@Test
	public void testDeleteWarehouseOrderType() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLDeleteWarehouseOrderType() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage()
		throws Exception {

		String externalReferenceCode =
			testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage_getIrrelevantExternalReferenceCode();

		Page<WarehouseOrderType> page =
			warehouseOrderTypeResource.
				getWarehouseByExternalReferenceCodeWarehouseOrderTypesPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			WarehouseOrderType irrelevantWarehouseOrderType =
				testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage_addWarehouseOrderType(
					irrelevantExternalReferenceCode,
					randomIrrelevantWarehouseOrderType());

			page =
				warehouseOrderTypeResource.
					getWarehouseByExternalReferenceCodeWarehouseOrderTypesPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantWarehouseOrderType),
				(List<WarehouseOrderType>)page.getItems());
			assertValid(page);
		}

		WarehouseOrderType warehouseOrderType1 =
			testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage_addWarehouseOrderType(
				externalReferenceCode, randomWarehouseOrderType());

		WarehouseOrderType warehouseOrderType2 =
			testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage_addWarehouseOrderType(
				externalReferenceCode, randomWarehouseOrderType());

		page =
			warehouseOrderTypeResource.
				getWarehouseByExternalReferenceCodeWarehouseOrderTypesPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(warehouseOrderType1, warehouseOrderType2),
			(List<WarehouseOrderType>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage_getExternalReferenceCode();

		WarehouseOrderType warehouseOrderType1 =
			testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage_addWarehouseOrderType(
				externalReferenceCode, randomWarehouseOrderType());

		WarehouseOrderType warehouseOrderType2 =
			testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage_addWarehouseOrderType(
				externalReferenceCode, randomWarehouseOrderType());

		WarehouseOrderType warehouseOrderType3 =
			testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage_addWarehouseOrderType(
				externalReferenceCode, randomWarehouseOrderType());

		Page<WarehouseOrderType> page1 =
			warehouseOrderTypeResource.
				getWarehouseByExternalReferenceCodeWarehouseOrderTypesPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<WarehouseOrderType> warehouseOrderTypes1 =
			(List<WarehouseOrderType>)page1.getItems();

		Assert.assertEquals(
			warehouseOrderTypes1.toString(), 2, warehouseOrderTypes1.size());

		Page<WarehouseOrderType> page2 =
			warehouseOrderTypeResource.
				getWarehouseByExternalReferenceCodeWarehouseOrderTypesPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<WarehouseOrderType> warehouseOrderTypes2 =
			(List<WarehouseOrderType>)page2.getItems();

		Assert.assertEquals(
			warehouseOrderTypes2.toString(), 1, warehouseOrderTypes2.size());

		Page<WarehouseOrderType> page3 =
			warehouseOrderTypeResource.
				getWarehouseByExternalReferenceCodeWarehouseOrderTypesPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				warehouseOrderType1, warehouseOrderType2, warehouseOrderType3),
			(List<WarehouseOrderType>)page3.getItems());
	}

	protected WarehouseOrderType
			testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage_addWarehouseOrderType(
				String externalReferenceCode,
				WarehouseOrderType warehouseOrderType)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetWarehouseByExternalReferenceCodeWarehouseOrderTypesPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostWarehouseByExternalReferenceCodeWarehouseOrderType()
		throws Exception {

		WarehouseOrderType randomWarehouseOrderType =
			randomWarehouseOrderType();

		WarehouseOrderType postWarehouseOrderType =
			testPostWarehouseByExternalReferenceCodeWarehouseOrderType_addWarehouseOrderType(
				randomWarehouseOrderType);

		assertEquals(randomWarehouseOrderType, postWarehouseOrderType);
		assertValid(postWarehouseOrderType);
	}

	protected WarehouseOrderType
			testPostWarehouseByExternalReferenceCodeWarehouseOrderType_addWarehouseOrderType(
				WarehouseOrderType warehouseOrderType)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetWarehouseIdWarehouseOrderTypesPage() throws Exception {
		Long id = testGetWarehouseIdWarehouseOrderTypesPage_getId();
		Long irrelevantId =
			testGetWarehouseIdWarehouseOrderTypesPage_getIrrelevantId();

		Page<WarehouseOrderType> page =
			warehouseOrderTypeResource.getWarehouseIdWarehouseOrderTypesPage(
				id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			WarehouseOrderType irrelevantWarehouseOrderType =
				testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
					irrelevantId, randomIrrelevantWarehouseOrderType());

			page =
				warehouseOrderTypeResource.
					getWarehouseIdWarehouseOrderTypesPage(
						irrelevantId, null, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantWarehouseOrderType),
				(List<WarehouseOrderType>)page.getItems());
			assertValid(page);
		}

		WarehouseOrderType warehouseOrderType1 =
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				id, randomWarehouseOrderType());

		WarehouseOrderType warehouseOrderType2 =
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				id, randomWarehouseOrderType());

		page = warehouseOrderTypeResource.getWarehouseIdWarehouseOrderTypesPage(
			id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(warehouseOrderType1, warehouseOrderType2),
			(List<WarehouseOrderType>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetWarehouseIdWarehouseOrderTypesPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetWarehouseIdWarehouseOrderTypesPage_getId();

		WarehouseOrderType warehouseOrderType1 = randomWarehouseOrderType();

		warehouseOrderType1 =
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				id, warehouseOrderType1);

		for (EntityField entityField : entityFields) {
			Page<WarehouseOrderType> page =
				warehouseOrderTypeResource.
					getWarehouseIdWarehouseOrderTypesPage(
						id, null,
						getFilterString(
							entityField, "between", warehouseOrderType1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(warehouseOrderType1),
				(List<WarehouseOrderType>)page.getItems());
		}
	}

	@Test
	public void testGetWarehouseIdWarehouseOrderTypesPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetWarehouseIdWarehouseOrderTypesPage_getId();

		WarehouseOrderType warehouseOrderType1 =
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				id, randomWarehouseOrderType());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		WarehouseOrderType warehouseOrderType2 =
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				id, randomWarehouseOrderType());

		for (EntityField entityField : entityFields) {
			Page<WarehouseOrderType> page =
				warehouseOrderTypeResource.
					getWarehouseIdWarehouseOrderTypesPage(
						id, null,
						getFilterString(entityField, "eq", warehouseOrderType1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(warehouseOrderType1),
				(List<WarehouseOrderType>)page.getItems());
		}
	}

	@Test
	public void testGetWarehouseIdWarehouseOrderTypesPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetWarehouseIdWarehouseOrderTypesPage_getId();

		WarehouseOrderType warehouseOrderType1 =
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				id, randomWarehouseOrderType());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		WarehouseOrderType warehouseOrderType2 =
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				id, randomWarehouseOrderType());

		for (EntityField entityField : entityFields) {
			Page<WarehouseOrderType> page =
				warehouseOrderTypeResource.
					getWarehouseIdWarehouseOrderTypesPage(
						id, null,
						getFilterString(entityField, "eq", warehouseOrderType1),
						Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(warehouseOrderType1),
				(List<WarehouseOrderType>)page.getItems());
		}
	}

	@Test
	public void testGetWarehouseIdWarehouseOrderTypesPageWithPagination()
		throws Exception {

		Long id = testGetWarehouseIdWarehouseOrderTypesPage_getId();

		WarehouseOrderType warehouseOrderType1 =
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				id, randomWarehouseOrderType());

		WarehouseOrderType warehouseOrderType2 =
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				id, randomWarehouseOrderType());

		WarehouseOrderType warehouseOrderType3 =
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				id, randomWarehouseOrderType());

		Page<WarehouseOrderType> page1 =
			warehouseOrderTypeResource.getWarehouseIdWarehouseOrderTypesPage(
				id, null, null, Pagination.of(1, 2), null);

		List<WarehouseOrderType> warehouseOrderTypes1 =
			(List<WarehouseOrderType>)page1.getItems();

		Assert.assertEquals(
			warehouseOrderTypes1.toString(), 2, warehouseOrderTypes1.size());

		Page<WarehouseOrderType> page2 =
			warehouseOrderTypeResource.getWarehouseIdWarehouseOrderTypesPage(
				id, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<WarehouseOrderType> warehouseOrderTypes2 =
			(List<WarehouseOrderType>)page2.getItems();

		Assert.assertEquals(
			warehouseOrderTypes2.toString(), 1, warehouseOrderTypes2.size());

		Page<WarehouseOrderType> page3 =
			warehouseOrderTypeResource.getWarehouseIdWarehouseOrderTypesPage(
				id, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(
				warehouseOrderType1, warehouseOrderType2, warehouseOrderType3),
			(List<WarehouseOrderType>)page3.getItems());
	}

	@Test
	public void testGetWarehouseIdWarehouseOrderTypesPageWithSortDateTime()
		throws Exception {

		testGetWarehouseIdWarehouseOrderTypesPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, warehouseOrderType1, warehouseOrderType2) -> {
				BeanTestUtil.setProperty(
					warehouseOrderType1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetWarehouseIdWarehouseOrderTypesPageWithSortDouble()
		throws Exception {

		testGetWarehouseIdWarehouseOrderTypesPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, warehouseOrderType1, warehouseOrderType2) -> {
				BeanTestUtil.setProperty(
					warehouseOrderType1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					warehouseOrderType2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetWarehouseIdWarehouseOrderTypesPageWithSortInteger()
		throws Exception {

		testGetWarehouseIdWarehouseOrderTypesPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, warehouseOrderType1, warehouseOrderType2) -> {
				BeanTestUtil.setProperty(
					warehouseOrderType1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					warehouseOrderType2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetWarehouseIdWarehouseOrderTypesPageWithSortString()
		throws Exception {

		testGetWarehouseIdWarehouseOrderTypesPageWithSort(
			EntityField.Type.STRING,
			(entityField, warehouseOrderType1, warehouseOrderType2) -> {
				Class<?> clazz = warehouseOrderType1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						warehouseOrderType1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						warehouseOrderType2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						warehouseOrderType1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						warehouseOrderType2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						warehouseOrderType1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						warehouseOrderType2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetWarehouseIdWarehouseOrderTypesPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, WarehouseOrderType, WarehouseOrderType, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetWarehouseIdWarehouseOrderTypesPage_getId();

		WarehouseOrderType warehouseOrderType1 = randomWarehouseOrderType();
		WarehouseOrderType warehouseOrderType2 = randomWarehouseOrderType();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, warehouseOrderType1, warehouseOrderType2);
		}

		warehouseOrderType1 =
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				id, warehouseOrderType1);

		warehouseOrderType2 =
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				id, warehouseOrderType2);

		for (EntityField entityField : entityFields) {
			Page<WarehouseOrderType> ascPage =
				warehouseOrderTypeResource.
					getWarehouseIdWarehouseOrderTypesPage(
						id, null, null, Pagination.of(1, 2),
						entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(warehouseOrderType1, warehouseOrderType2),
				(List<WarehouseOrderType>)ascPage.getItems());

			Page<WarehouseOrderType> descPage =
				warehouseOrderTypeResource.
					getWarehouseIdWarehouseOrderTypesPage(
						id, null, null, Pagination.of(1, 2),
						entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(warehouseOrderType2, warehouseOrderType1),
				(List<WarehouseOrderType>)descPage.getItems());
		}
	}

	protected WarehouseOrderType
			testGetWarehouseIdWarehouseOrderTypesPage_addWarehouseOrderType(
				Long id, WarehouseOrderType warehouseOrderType)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetWarehouseIdWarehouseOrderTypesPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetWarehouseIdWarehouseOrderTypesPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostWarehouseIdWarehouseOrderType() throws Exception {
		WarehouseOrderType randomWarehouseOrderType =
			randomWarehouseOrderType();

		WarehouseOrderType postWarehouseOrderType =
			testPostWarehouseIdWarehouseOrderType_addWarehouseOrderType(
				randomWarehouseOrderType);

		assertEquals(randomWarehouseOrderType, postWarehouseOrderType);
		assertValid(postWarehouseOrderType);
	}

	protected WarehouseOrderType
			testPostWarehouseIdWarehouseOrderType_addWarehouseOrderType(
				WarehouseOrderType warehouseOrderType)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected void assertContains(
		WarehouseOrderType warehouseOrderType,
		List<WarehouseOrderType> warehouseOrderTypes) {

		boolean contains = false;

		for (WarehouseOrderType item : warehouseOrderTypes) {
			if (equals(warehouseOrderType, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			warehouseOrderTypes + " does not contain " + warehouseOrderType,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		WarehouseOrderType warehouseOrderType1,
		WarehouseOrderType warehouseOrderType2) {

		Assert.assertTrue(
			warehouseOrderType1 + " does not equal " + warehouseOrderType2,
			equals(warehouseOrderType1, warehouseOrderType2));
	}

	protected void assertEquals(
		List<WarehouseOrderType> warehouseOrderTypes1,
		List<WarehouseOrderType> warehouseOrderTypes2) {

		Assert.assertEquals(
			warehouseOrderTypes1.size(), warehouseOrderTypes2.size());

		for (int i = 0; i < warehouseOrderTypes1.size(); i++) {
			WarehouseOrderType warehouseOrderType1 = warehouseOrderTypes1.get(
				i);
			WarehouseOrderType warehouseOrderType2 = warehouseOrderTypes2.get(
				i);

			assertEquals(warehouseOrderType1, warehouseOrderType2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<WarehouseOrderType> warehouseOrderTypes1,
		List<WarehouseOrderType> warehouseOrderTypes2) {

		Assert.assertEquals(
			warehouseOrderTypes1.size(), warehouseOrderTypes2.size());

		for (WarehouseOrderType warehouseOrderType1 : warehouseOrderTypes1) {
			boolean contains = false;

			for (WarehouseOrderType warehouseOrderType2 :
					warehouseOrderTypes2) {

				if (equals(warehouseOrderType1, warehouseOrderType2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				warehouseOrderTypes2 + " does not contain " +
					warehouseOrderType1,
				contains);
		}
	}

	protected void assertValid(WarehouseOrderType warehouseOrderType)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (warehouseOrderType.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("orderType", additionalAssertFieldName)) {
				if (warehouseOrderType.getOrderType() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"orderTypeExternalReferenceCode",
					additionalAssertFieldName)) {

				if (warehouseOrderType.getOrderTypeExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("orderTypeId", additionalAssertFieldName)) {
				if (warehouseOrderType.getOrderTypeId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (warehouseOrderType.getPriority() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"warehouseExternalReferenceCode",
					additionalAssertFieldName)) {

				if (warehouseOrderType.getWarehouseExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("warehouseId", additionalAssertFieldName)) {
				if (warehouseOrderType.getWarehouseId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"warehouseOrderTypeId", additionalAssertFieldName)) {

				if (warehouseOrderType.getWarehouseOrderTypeId() == null) {
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

	protected void assertValid(Page<WarehouseOrderType> page) {
		boolean valid = false;

		java.util.Collection<WarehouseOrderType> warehouseOrderTypes =
			page.getItems();

		int size = warehouseOrderTypes.size();

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
					com.liferay.headless.commerce.admin.inventory.dto.v1_0.
						WarehouseOrderType.class)) {

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
		WarehouseOrderType warehouseOrderType1,
		WarehouseOrderType warehouseOrderType2) {

		if (warehouseOrderType1 == warehouseOrderType2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)warehouseOrderType1.getActions(),
						(Map)warehouseOrderType2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("orderType", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouseOrderType1.getOrderType(),
						warehouseOrderType2.getOrderType())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"orderTypeExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						warehouseOrderType1.getOrderTypeExternalReferenceCode(),
						warehouseOrderType2.
							getOrderTypeExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("orderTypeId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouseOrderType1.getOrderTypeId(),
						warehouseOrderType2.getOrderTypeId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouseOrderType1.getPriority(),
						warehouseOrderType2.getPriority())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"warehouseExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						warehouseOrderType1.getWarehouseExternalReferenceCode(),
						warehouseOrderType2.
							getWarehouseExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("warehouseId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouseOrderType1.getWarehouseId(),
						warehouseOrderType2.getWarehouseId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"warehouseOrderTypeId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						warehouseOrderType1.getWarehouseOrderTypeId(),
						warehouseOrderType2.getWarehouseOrderTypeId())) {

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

		if (!(_warehouseOrderTypeResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_warehouseOrderTypeResource;

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
		WarehouseOrderType warehouseOrderType) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("actions")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("orderType")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("orderTypeExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					warehouseOrderType.getOrderTypeExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("orderTypeId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("priority")) {
			sb.append(String.valueOf(warehouseOrderType.getPriority()));

			return sb.toString();
		}

		if (entityFieldName.equals("warehouseExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					warehouseOrderType.getWarehouseExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("warehouseId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("warehouseOrderTypeId")) {
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

	protected WarehouseOrderType randomWarehouseOrderType() throws Exception {
		return new WarehouseOrderType() {
			{
				orderTypeExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				orderTypeId = RandomTestUtil.randomLong();
				priority = RandomTestUtil.randomInt();
				warehouseExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				warehouseId = RandomTestUtil.randomLong();
				warehouseOrderTypeId = RandomTestUtil.randomLong();
			}
		};
	}

	protected WarehouseOrderType randomIrrelevantWarehouseOrderType()
		throws Exception {

		WarehouseOrderType randomIrrelevantWarehouseOrderType =
			randomWarehouseOrderType();

		return randomIrrelevantWarehouseOrderType;
	}

	protected WarehouseOrderType randomPatchWarehouseOrderType()
		throws Exception {

		return randomWarehouseOrderType();
	}

	protected WarehouseOrderTypeResource warehouseOrderTypeResource;
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
		LogFactoryUtil.getLog(BaseWarehouseOrderTypeResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.inventory.resource.v1_0.
		WarehouseOrderTypeResource _warehouseOrderTypeResource;

}