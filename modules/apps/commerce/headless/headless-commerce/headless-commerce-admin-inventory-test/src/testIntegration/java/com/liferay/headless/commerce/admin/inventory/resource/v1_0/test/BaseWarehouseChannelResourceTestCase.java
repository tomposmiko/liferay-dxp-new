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

import com.liferay.headless.commerce.admin.inventory.client.dto.v1_0.WarehouseChannel;
import com.liferay.headless.commerce.admin.inventory.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.inventory.client.pagination.Page;
import com.liferay.headless.commerce.admin.inventory.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.inventory.client.resource.v1_0.WarehouseChannelResource;
import com.liferay.headless.commerce.admin.inventory.client.serdes.v1_0.WarehouseChannelSerDes;
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
public abstract class BaseWarehouseChannelResourceTestCase {

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

		_warehouseChannelResource.setContextCompany(testCompany);

		WarehouseChannelResource.Builder builder =
			WarehouseChannelResource.builder();

		warehouseChannelResource = builder.authentication(
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

		WarehouseChannel warehouseChannel1 = randomWarehouseChannel();

		String json = objectMapper.writeValueAsString(warehouseChannel1);

		WarehouseChannel warehouseChannel2 = WarehouseChannelSerDes.toDTO(json);

		Assert.assertTrue(equals(warehouseChannel1, warehouseChannel2));
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

		WarehouseChannel warehouseChannel = randomWarehouseChannel();

		String json1 = objectMapper.writeValueAsString(warehouseChannel);
		String json2 = WarehouseChannelSerDes.toJSON(warehouseChannel);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		WarehouseChannel warehouseChannel = randomWarehouseChannel();

		warehouseChannel.setChannelExternalReferenceCode(regex);
		warehouseChannel.setWarehouseExternalReferenceCode(regex);

		String json = WarehouseChannelSerDes.toJSON(warehouseChannel);

		Assert.assertFalse(json.contains(regex));

		warehouseChannel = WarehouseChannelSerDes.toDTO(json);

		Assert.assertEquals(
			regex, warehouseChannel.getChannelExternalReferenceCode());
		Assert.assertEquals(
			regex, warehouseChannel.getWarehouseExternalReferenceCode());
	}

	@Test
	public void testDeleteWarehouseChannel() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLDeleteWarehouseChannel() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage()
		throws Exception {

		String externalReferenceCode =
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_getIrrelevantExternalReferenceCode();

		Page<WarehouseChannel> page =
			warehouseChannelResource.
				getWarehouseByExternalReferenceCodeWarehouseChannelsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			WarehouseChannel irrelevantWarehouseChannel =
				testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_addWarehouseChannel(
					irrelevantExternalReferenceCode,
					randomIrrelevantWarehouseChannel());

			page =
				warehouseChannelResource.
					getWarehouseByExternalReferenceCodeWarehouseChannelsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantWarehouseChannel),
				(List<WarehouseChannel>)page.getItems());
			assertValid(
				page,
				testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		WarehouseChannel warehouseChannel1 =
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_addWarehouseChannel(
				externalReferenceCode, randomWarehouseChannel());

		WarehouseChannel warehouseChannel2 =
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_addWarehouseChannel(
				externalReferenceCode, randomWarehouseChannel());

		page =
			warehouseChannelResource.
				getWarehouseByExternalReferenceCodeWarehouseChannelsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(warehouseChannel1, warehouseChannel2),
			(List<WarehouseChannel>)page.getItems());
		assertValid(
			page,
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_getExpectedActions(
				externalReferenceCode));
	}

	protected Map<String, Map<String, String>>
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetWarehouseByExternalReferenceCodeWarehouseChannelsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_getExternalReferenceCode();

		WarehouseChannel warehouseChannel1 =
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_addWarehouseChannel(
				externalReferenceCode, randomWarehouseChannel());

		WarehouseChannel warehouseChannel2 =
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_addWarehouseChannel(
				externalReferenceCode, randomWarehouseChannel());

		WarehouseChannel warehouseChannel3 =
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_addWarehouseChannel(
				externalReferenceCode, randomWarehouseChannel());

		Page<WarehouseChannel> page1 =
			warehouseChannelResource.
				getWarehouseByExternalReferenceCodeWarehouseChannelsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<WarehouseChannel> warehouseChannels1 =
			(List<WarehouseChannel>)page1.getItems();

		Assert.assertEquals(
			warehouseChannels1.toString(), 2, warehouseChannels1.size());

		Page<WarehouseChannel> page2 =
			warehouseChannelResource.
				getWarehouseByExternalReferenceCodeWarehouseChannelsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<WarehouseChannel> warehouseChannels2 =
			(List<WarehouseChannel>)page2.getItems();

		Assert.assertEquals(
			warehouseChannels2.toString(), 1, warehouseChannels2.size());

		Page<WarehouseChannel> page3 =
			warehouseChannelResource.
				getWarehouseByExternalReferenceCodeWarehouseChannelsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				warehouseChannel1, warehouseChannel2, warehouseChannel3),
			(List<WarehouseChannel>)page3.getItems());
	}

	protected WarehouseChannel
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_addWarehouseChannel(
				String externalReferenceCode, WarehouseChannel warehouseChannel)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetWarehouseByExternalReferenceCodeWarehouseChannelsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostWarehouseByExternalReferenceCodeWarehouseChannel()
		throws Exception {

		WarehouseChannel randomWarehouseChannel = randomWarehouseChannel();

		WarehouseChannel postWarehouseChannel =
			testPostWarehouseByExternalReferenceCodeWarehouseChannel_addWarehouseChannel(
				randomWarehouseChannel);

		assertEquals(randomWarehouseChannel, postWarehouseChannel);
		assertValid(postWarehouseChannel);
	}

	protected WarehouseChannel
			testPostWarehouseByExternalReferenceCodeWarehouseChannel_addWarehouseChannel(
				WarehouseChannel warehouseChannel)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetWarehouseIdWarehouseChannelsPage() throws Exception {
		Long id = testGetWarehouseIdWarehouseChannelsPage_getId();
		Long irrelevantId =
			testGetWarehouseIdWarehouseChannelsPage_getIrrelevantId();

		Page<WarehouseChannel> page =
			warehouseChannelResource.getWarehouseIdWarehouseChannelsPage(
				id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			WarehouseChannel irrelevantWarehouseChannel =
				testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
					irrelevantId, randomIrrelevantWarehouseChannel());

			page = warehouseChannelResource.getWarehouseIdWarehouseChannelsPage(
				irrelevantId, null, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantWarehouseChannel),
				(List<WarehouseChannel>)page.getItems());
			assertValid(
				page,
				testGetWarehouseIdWarehouseChannelsPage_getExpectedActions(
					irrelevantId));
		}

		WarehouseChannel warehouseChannel1 =
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				id, randomWarehouseChannel());

		WarehouseChannel warehouseChannel2 =
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				id, randomWarehouseChannel());

		page = warehouseChannelResource.getWarehouseIdWarehouseChannelsPage(
			id, null, null, Pagination.of(1, 10), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(warehouseChannel1, warehouseChannel2),
			(List<WarehouseChannel>)page.getItems());
		assertValid(
			page,
			testGetWarehouseIdWarehouseChannelsPage_getExpectedActions(id));
	}

	protected Map<String, Map<String, String>>
			testGetWarehouseIdWarehouseChannelsPage_getExpectedActions(Long id)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetWarehouseIdWarehouseChannelsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetWarehouseIdWarehouseChannelsPage_getId();

		WarehouseChannel warehouseChannel1 = randomWarehouseChannel();

		warehouseChannel1 =
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				id, warehouseChannel1);

		for (EntityField entityField : entityFields) {
			Page<WarehouseChannel> page =
				warehouseChannelResource.getWarehouseIdWarehouseChannelsPage(
					id, null,
					getFilterString(entityField, "between", warehouseChannel1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(warehouseChannel1),
				(List<WarehouseChannel>)page.getItems());
		}
	}

	@Test
	public void testGetWarehouseIdWarehouseChannelsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetWarehouseIdWarehouseChannelsPage_getId();

		WarehouseChannel warehouseChannel1 =
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				id, randomWarehouseChannel());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		WarehouseChannel warehouseChannel2 =
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				id, randomWarehouseChannel());

		for (EntityField entityField : entityFields) {
			Page<WarehouseChannel> page =
				warehouseChannelResource.getWarehouseIdWarehouseChannelsPage(
					id, null,
					getFilterString(entityField, "eq", warehouseChannel1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(warehouseChannel1),
				(List<WarehouseChannel>)page.getItems());
		}
	}

	@Test
	public void testGetWarehouseIdWarehouseChannelsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetWarehouseIdWarehouseChannelsPage_getId();

		WarehouseChannel warehouseChannel1 =
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				id, randomWarehouseChannel());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		WarehouseChannel warehouseChannel2 =
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				id, randomWarehouseChannel());

		for (EntityField entityField : entityFields) {
			Page<WarehouseChannel> page =
				warehouseChannelResource.getWarehouseIdWarehouseChannelsPage(
					id, null,
					getFilterString(entityField, "eq", warehouseChannel1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(warehouseChannel1),
				(List<WarehouseChannel>)page.getItems());
		}
	}

	@Test
	public void testGetWarehouseIdWarehouseChannelsPageWithPagination()
		throws Exception {

		Long id = testGetWarehouseIdWarehouseChannelsPage_getId();

		WarehouseChannel warehouseChannel1 =
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				id, randomWarehouseChannel());

		WarehouseChannel warehouseChannel2 =
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				id, randomWarehouseChannel());

		WarehouseChannel warehouseChannel3 =
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				id, randomWarehouseChannel());

		Page<WarehouseChannel> page1 =
			warehouseChannelResource.getWarehouseIdWarehouseChannelsPage(
				id, null, null, Pagination.of(1, 2), null);

		List<WarehouseChannel> warehouseChannels1 =
			(List<WarehouseChannel>)page1.getItems();

		Assert.assertEquals(
			warehouseChannels1.toString(), 2, warehouseChannels1.size());

		Page<WarehouseChannel> page2 =
			warehouseChannelResource.getWarehouseIdWarehouseChannelsPage(
				id, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<WarehouseChannel> warehouseChannels2 =
			(List<WarehouseChannel>)page2.getItems();

		Assert.assertEquals(
			warehouseChannels2.toString(), 1, warehouseChannels2.size());

		Page<WarehouseChannel> page3 =
			warehouseChannelResource.getWarehouseIdWarehouseChannelsPage(
				id, null, null, Pagination.of(1, 3), null);

		assertEqualsIgnoringOrder(
			Arrays.asList(
				warehouseChannel1, warehouseChannel2, warehouseChannel3),
			(List<WarehouseChannel>)page3.getItems());
	}

	@Test
	public void testGetWarehouseIdWarehouseChannelsPageWithSortDateTime()
		throws Exception {

		testGetWarehouseIdWarehouseChannelsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, warehouseChannel1, warehouseChannel2) -> {
				BeanTestUtil.setProperty(
					warehouseChannel1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetWarehouseIdWarehouseChannelsPageWithSortDouble()
		throws Exception {

		testGetWarehouseIdWarehouseChannelsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, warehouseChannel1, warehouseChannel2) -> {
				BeanTestUtil.setProperty(
					warehouseChannel1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					warehouseChannel2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetWarehouseIdWarehouseChannelsPageWithSortInteger()
		throws Exception {

		testGetWarehouseIdWarehouseChannelsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, warehouseChannel1, warehouseChannel2) -> {
				BeanTestUtil.setProperty(
					warehouseChannel1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					warehouseChannel2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetWarehouseIdWarehouseChannelsPageWithSortString()
		throws Exception {

		testGetWarehouseIdWarehouseChannelsPageWithSort(
			EntityField.Type.STRING,
			(entityField, warehouseChannel1, warehouseChannel2) -> {
				Class<?> clazz = warehouseChannel1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						warehouseChannel1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						warehouseChannel2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						warehouseChannel1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						warehouseChannel2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						warehouseChannel1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						warehouseChannel2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetWarehouseIdWarehouseChannelsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, WarehouseChannel, WarehouseChannel, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		Long id = testGetWarehouseIdWarehouseChannelsPage_getId();

		WarehouseChannel warehouseChannel1 = randomWarehouseChannel();
		WarehouseChannel warehouseChannel2 = randomWarehouseChannel();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, warehouseChannel1, warehouseChannel2);
		}

		warehouseChannel1 =
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				id, warehouseChannel1);

		warehouseChannel2 =
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				id, warehouseChannel2);

		for (EntityField entityField : entityFields) {
			Page<WarehouseChannel> ascPage =
				warehouseChannelResource.getWarehouseIdWarehouseChannelsPage(
					id, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(warehouseChannel1, warehouseChannel2),
				(List<WarehouseChannel>)ascPage.getItems());

			Page<WarehouseChannel> descPage =
				warehouseChannelResource.getWarehouseIdWarehouseChannelsPage(
					id, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(warehouseChannel2, warehouseChannel1),
				(List<WarehouseChannel>)descPage.getItems());
		}
	}

	protected WarehouseChannel
			testGetWarehouseIdWarehouseChannelsPage_addWarehouseChannel(
				Long id, WarehouseChannel warehouseChannel)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetWarehouseIdWarehouseChannelsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetWarehouseIdWarehouseChannelsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostWarehouseIdWarehouseChannel() throws Exception {
		WarehouseChannel randomWarehouseChannel = randomWarehouseChannel();

		WarehouseChannel postWarehouseChannel =
			testPostWarehouseIdWarehouseChannel_addWarehouseChannel(
				randomWarehouseChannel);

		assertEquals(randomWarehouseChannel, postWarehouseChannel);
		assertValid(postWarehouseChannel);
	}

	protected WarehouseChannel
			testPostWarehouseIdWarehouseChannel_addWarehouseChannel(
				WarehouseChannel warehouseChannel)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected void assertContains(
		WarehouseChannel warehouseChannel,
		List<WarehouseChannel> warehouseChannels) {

		boolean contains = false;

		for (WarehouseChannel item : warehouseChannels) {
			if (equals(warehouseChannel, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			warehouseChannels + " does not contain " + warehouseChannel,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		WarehouseChannel warehouseChannel1,
		WarehouseChannel warehouseChannel2) {

		Assert.assertTrue(
			warehouseChannel1 + " does not equal " + warehouseChannel2,
			equals(warehouseChannel1, warehouseChannel2));
	}

	protected void assertEquals(
		List<WarehouseChannel> warehouseChannels1,
		List<WarehouseChannel> warehouseChannels2) {

		Assert.assertEquals(
			warehouseChannels1.size(), warehouseChannels2.size());

		for (int i = 0; i < warehouseChannels1.size(); i++) {
			WarehouseChannel warehouseChannel1 = warehouseChannels1.get(i);
			WarehouseChannel warehouseChannel2 = warehouseChannels2.get(i);

			assertEquals(warehouseChannel1, warehouseChannel2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<WarehouseChannel> warehouseChannels1,
		List<WarehouseChannel> warehouseChannels2) {

		Assert.assertEquals(
			warehouseChannels1.size(), warehouseChannels2.size());

		for (WarehouseChannel warehouseChannel1 : warehouseChannels1) {
			boolean contains = false;

			for (WarehouseChannel warehouseChannel2 : warehouseChannels2) {
				if (equals(warehouseChannel1, warehouseChannel2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				warehouseChannels2 + " does not contain " + warehouseChannel1,
				contains);
		}
	}

	protected void assertValid(WarehouseChannel warehouseChannel)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (warehouseChannel.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("channel", additionalAssertFieldName)) {
				if (warehouseChannel.getChannel() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"channelExternalReferenceCode",
					additionalAssertFieldName)) {

				if (warehouseChannel.getChannelExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("channelId", additionalAssertFieldName)) {
				if (warehouseChannel.getChannelId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"warehouseChannelId", additionalAssertFieldName)) {

				if (warehouseChannel.getWarehouseChannelId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"warehouseExternalReferenceCode",
					additionalAssertFieldName)) {

				if (warehouseChannel.getWarehouseExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("warehouseId", additionalAssertFieldName)) {
				if (warehouseChannel.getWarehouseId() == null) {
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

	protected void assertValid(Page<WarehouseChannel> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<WarehouseChannel> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<WarehouseChannel> warehouseChannels =
			page.getItems();

		int size = warehouseChannels.size();

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
					com.liferay.headless.commerce.admin.inventory.dto.v1_0.
						WarehouseChannel.class)) {

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
		WarehouseChannel warehouseChannel1,
		WarehouseChannel warehouseChannel2) {

		if (warehouseChannel1 == warehouseChannel2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)warehouseChannel1.getActions(),
						(Map)warehouseChannel2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("channel", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouseChannel1.getChannel(),
						warehouseChannel2.getChannel())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"channelExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						warehouseChannel1.getChannelExternalReferenceCode(),
						warehouseChannel2.getChannelExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("channelId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouseChannel1.getChannelId(),
						warehouseChannel2.getChannelId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"warehouseChannelId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						warehouseChannel1.getWarehouseChannelId(),
						warehouseChannel2.getWarehouseChannelId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"warehouseExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						warehouseChannel1.getWarehouseExternalReferenceCode(),
						warehouseChannel2.
							getWarehouseExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("warehouseId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						warehouseChannel1.getWarehouseId(),
						warehouseChannel2.getWarehouseId())) {

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

		if (!(_warehouseChannelResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_warehouseChannelResource;

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
		EntityField entityField, String operator,
		WarehouseChannel warehouseChannel) {

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

		if (entityFieldName.equals("channel")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("channelExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					warehouseChannel.getChannelExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("channelId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("warehouseChannelId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("warehouseExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					warehouseChannel.getWarehouseExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("warehouseId")) {
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

	protected WarehouseChannel randomWarehouseChannel() throws Exception {
		return new WarehouseChannel() {
			{
				channelExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				channelId = RandomTestUtil.randomLong();
				warehouseChannelId = RandomTestUtil.randomLong();
				warehouseExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				warehouseId = RandomTestUtil.randomLong();
			}
		};
	}

	protected WarehouseChannel randomIrrelevantWarehouseChannel()
		throws Exception {

		WarehouseChannel randomIrrelevantWarehouseChannel =
			randomWarehouseChannel();

		return randomIrrelevantWarehouseChannel;
	}

	protected WarehouseChannel randomPatchWarehouseChannel() throws Exception {
		return randomWarehouseChannel();
	}

	protected WarehouseChannelResource warehouseChannelResource;
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
		LogFactoryUtil.getLog(BaseWarehouseChannelResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.inventory.resource.v1_0.
		WarehouseChannelResource _warehouseChannelResource;

}