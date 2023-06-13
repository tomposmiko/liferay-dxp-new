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

package com.liferay.headless.commerce.admin.account.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import com.liferay.headless.commerce.admin.account.client.dto.v1_0.AdminAccountGroup;
import com.liferay.headless.commerce.admin.account.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.account.client.pagination.Page;
import com.liferay.headless.commerce.admin.account.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.account.client.resource.v1_0.AdminAccountGroupResource;
import com.liferay.headless.commerce.admin.account.client.serdes.v1_0.AdminAccountGroupSerDes;
import com.liferay.petra.function.UnsafeTriConsumer;
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
import com.liferay.portal.kernel.util.GetterUtil;
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
public abstract class BaseAdminAccountGroupResourceTestCase {

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

		_adminAccountGroupResource.setContextCompany(testCompany);

		AdminAccountGroupResource.Builder builder =
			AdminAccountGroupResource.builder();

		adminAccountGroupResource = builder.authentication(
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

		AdminAccountGroup adminAccountGroup1 = randomAdminAccountGroup();

		String json = objectMapper.writeValueAsString(adminAccountGroup1);

		AdminAccountGroup adminAccountGroup2 = AdminAccountGroupSerDes.toDTO(
			json);

		Assert.assertTrue(equals(adminAccountGroup1, adminAccountGroup2));
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

		AdminAccountGroup adminAccountGroup = randomAdminAccountGroup();

		String json1 = objectMapper.writeValueAsString(adminAccountGroup);
		String json2 = AdminAccountGroupSerDes.toJSON(adminAccountGroup);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		AdminAccountGroup adminAccountGroup = randomAdminAccountGroup();

		adminAccountGroup.setDescription(regex);
		adminAccountGroup.setExternalReferenceCode(regex);
		adminAccountGroup.setName(regex);

		String json = AdminAccountGroupSerDes.toJSON(adminAccountGroup);

		Assert.assertFalse(json.contains(regex));

		adminAccountGroup = AdminAccountGroupSerDes.toDTO(json);

		Assert.assertEquals(regex, adminAccountGroup.getDescription());
		Assert.assertEquals(
			regex, adminAccountGroup.getExternalReferenceCode());
		Assert.assertEquals(regex, adminAccountGroup.getName());
	}

	@Test
	public void testGetAccountGroupsPage() throws Exception {
		Page<AdminAccountGroup> page =
			adminAccountGroupResource.getAccountGroupsPage(
				null, Pagination.of(1, 10), null);

		long totalCount = page.getTotalCount();

		AdminAccountGroup adminAccountGroup1 =
			testGetAccountGroupsPage_addAdminAccountGroup(
				randomAdminAccountGroup());

		AdminAccountGroup adminAccountGroup2 =
			testGetAccountGroupsPage_addAdminAccountGroup(
				randomAdminAccountGroup());

		page = adminAccountGroupResource.getAccountGroupsPage(
			null, Pagination.of(1, 10), null);

		Assert.assertEquals(totalCount + 2, page.getTotalCount());

		assertContains(
			adminAccountGroup1, (List<AdminAccountGroup>)page.getItems());
		assertContains(
			adminAccountGroup2, (List<AdminAccountGroup>)page.getItems());
		assertValid(page, testGetAccountGroupsPage_getExpectedActions());
	}

	protected Map<String, Map<String, String>>
			testGetAccountGroupsPage_getExpectedActions()
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountGroupsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		AdminAccountGroup adminAccountGroup1 = randomAdminAccountGroup();

		adminAccountGroup1 = testGetAccountGroupsPage_addAdminAccountGroup(
			adminAccountGroup1);

		for (EntityField entityField : entityFields) {
			Page<AdminAccountGroup> page =
				adminAccountGroupResource.getAccountGroupsPage(
					getFilterString(entityField, "between", adminAccountGroup1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(adminAccountGroup1),
				(List<AdminAccountGroup>)page.getItems());
		}
	}

	@Test
	public void testGetAccountGroupsPageWithFilterDoubleEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DOUBLE);

		if (entityFields.isEmpty()) {
			return;
		}

		AdminAccountGroup adminAccountGroup1 =
			testGetAccountGroupsPage_addAdminAccountGroup(
				randomAdminAccountGroup());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		AdminAccountGroup adminAccountGroup2 =
			testGetAccountGroupsPage_addAdminAccountGroup(
				randomAdminAccountGroup());

		for (EntityField entityField : entityFields) {
			Page<AdminAccountGroup> page =
				adminAccountGroupResource.getAccountGroupsPage(
					getFilterString(entityField, "eq", adminAccountGroup1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(adminAccountGroup1),
				(List<AdminAccountGroup>)page.getItems());
		}
	}

	@Test
	public void testGetAccountGroupsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		AdminAccountGroup adminAccountGroup1 =
			testGetAccountGroupsPage_addAdminAccountGroup(
				randomAdminAccountGroup());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		AdminAccountGroup adminAccountGroup2 =
			testGetAccountGroupsPage_addAdminAccountGroup(
				randomAdminAccountGroup());

		for (EntityField entityField : entityFields) {
			Page<AdminAccountGroup> page =
				adminAccountGroupResource.getAccountGroupsPage(
					getFilterString(entityField, "eq", adminAccountGroup1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(adminAccountGroup1),
				(List<AdminAccountGroup>)page.getItems());
		}
	}

	@Test
	public void testGetAccountGroupsPageWithPagination() throws Exception {
		Page<AdminAccountGroup> totalPage =
			adminAccountGroupResource.getAccountGroupsPage(null, null, null);

		int totalCount = GetterUtil.getInteger(totalPage.getTotalCount());

		AdminAccountGroup adminAccountGroup1 =
			testGetAccountGroupsPage_addAdminAccountGroup(
				randomAdminAccountGroup());

		AdminAccountGroup adminAccountGroup2 =
			testGetAccountGroupsPage_addAdminAccountGroup(
				randomAdminAccountGroup());

		AdminAccountGroup adminAccountGroup3 =
			testGetAccountGroupsPage_addAdminAccountGroup(
				randomAdminAccountGroup());

		Page<AdminAccountGroup> page1 =
			adminAccountGroupResource.getAccountGroupsPage(
				null, Pagination.of(1, totalCount + 2), null);

		List<AdminAccountGroup> adminAccountGroups1 =
			(List<AdminAccountGroup>)page1.getItems();

		Assert.assertEquals(
			adminAccountGroups1.toString(), totalCount + 2,
			adminAccountGroups1.size());

		Page<AdminAccountGroup> page2 =
			adminAccountGroupResource.getAccountGroupsPage(
				null, Pagination.of(2, totalCount + 2), null);

		Assert.assertEquals(totalCount + 3, page2.getTotalCount());

		List<AdminAccountGroup> adminAccountGroups2 =
			(List<AdminAccountGroup>)page2.getItems();

		Assert.assertEquals(
			adminAccountGroups2.toString(), 1, adminAccountGroups2.size());

		Page<AdminAccountGroup> page3 =
			adminAccountGroupResource.getAccountGroupsPage(
				null, Pagination.of(1, totalCount + 3), null);

		assertContains(
			adminAccountGroup1, (List<AdminAccountGroup>)page3.getItems());
		assertContains(
			adminAccountGroup2, (List<AdminAccountGroup>)page3.getItems());
		assertContains(
			adminAccountGroup3, (List<AdminAccountGroup>)page3.getItems());
	}

	@Test
	public void testGetAccountGroupsPageWithSortDateTime() throws Exception {
		testGetAccountGroupsPageWithSort(
			EntityField.Type.DATE_TIME,
			(entityField, adminAccountGroup1, adminAccountGroup2) -> {
				BeanTestUtil.setProperty(
					adminAccountGroup1, entityField.getName(),
					DateUtils.addMinutes(new Date(), -2));
			});
	}

	@Test
	public void testGetAccountGroupsPageWithSortDouble() throws Exception {
		testGetAccountGroupsPageWithSort(
			EntityField.Type.DOUBLE,
			(entityField, adminAccountGroup1, adminAccountGroup2) -> {
				BeanTestUtil.setProperty(
					adminAccountGroup1, entityField.getName(), 0.1);
				BeanTestUtil.setProperty(
					adminAccountGroup2, entityField.getName(), 0.5);
			});
	}

	@Test
	public void testGetAccountGroupsPageWithSortInteger() throws Exception {
		testGetAccountGroupsPageWithSort(
			EntityField.Type.INTEGER,
			(entityField, adminAccountGroup1, adminAccountGroup2) -> {
				BeanTestUtil.setProperty(
					adminAccountGroup1, entityField.getName(), 0);
				BeanTestUtil.setProperty(
					adminAccountGroup2, entityField.getName(), 1);
			});
	}

	@Test
	public void testGetAccountGroupsPageWithSortString() throws Exception {
		testGetAccountGroupsPageWithSort(
			EntityField.Type.STRING,
			(entityField, adminAccountGroup1, adminAccountGroup2) -> {
				Class<?> clazz = adminAccountGroup1.getClass();

				String entityFieldName = entityField.getName();

				Method method = clazz.getMethod(
					"get" + StringUtil.upperCaseFirstLetter(entityFieldName));

				Class<?> returnType = method.getReturnType();

				if (returnType.isAssignableFrom(Map.class)) {
					BeanTestUtil.setProperty(
						adminAccountGroup1, entityFieldName,
						Collections.singletonMap("Aaa", "Aaa"));
					BeanTestUtil.setProperty(
						adminAccountGroup2, entityFieldName,
						Collections.singletonMap("Bbb", "Bbb"));
				}
				else if (entityFieldName.contains("email")) {
					BeanTestUtil.setProperty(
						adminAccountGroup1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
					BeanTestUtil.setProperty(
						adminAccountGroup2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()) +
									"@liferay.com");
				}
				else {
					BeanTestUtil.setProperty(
						adminAccountGroup1, entityFieldName,
						"aaa" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
					BeanTestUtil.setProperty(
						adminAccountGroup2, entityFieldName,
						"bbb" +
							StringUtil.toLowerCase(
								RandomTestUtil.randomString()));
				}
			});
	}

	protected void testGetAccountGroupsPageWithSort(
			EntityField.Type type,
			UnsafeTriConsumer
				<EntityField, AdminAccountGroup, AdminAccountGroup, Exception>
					unsafeTriConsumer)
		throws Exception {

		List<EntityField> entityFields = getEntityFields(type);

		if (entityFields.isEmpty()) {
			return;
		}

		AdminAccountGroup adminAccountGroup1 = randomAdminAccountGroup();
		AdminAccountGroup adminAccountGroup2 = randomAdminAccountGroup();

		for (EntityField entityField : entityFields) {
			unsafeTriConsumer.accept(
				entityField, adminAccountGroup1, adminAccountGroup2);
		}

		adminAccountGroup1 = testGetAccountGroupsPage_addAdminAccountGroup(
			adminAccountGroup1);

		adminAccountGroup2 = testGetAccountGroupsPage_addAdminAccountGroup(
			adminAccountGroup2);

		for (EntityField entityField : entityFields) {
			Page<AdminAccountGroup> ascPage =
				adminAccountGroupResource.getAccountGroupsPage(
					null, Pagination.of(1, 2), entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(adminAccountGroup1, adminAccountGroup2),
				(List<AdminAccountGroup>)ascPage.getItems());

			Page<AdminAccountGroup> descPage =
				adminAccountGroupResource.getAccountGroupsPage(
					null, Pagination.of(1, 2), entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(adminAccountGroup2, adminAccountGroup1),
				(List<AdminAccountGroup>)descPage.getItems());
		}
	}

	protected AdminAccountGroup testGetAccountGroupsPage_addAdminAccountGroup(
			AdminAccountGroup adminAccountGroup)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testPostAccountGroup() throws Exception {
		AdminAccountGroup randomAdminAccountGroup = randomAdminAccountGroup();

		AdminAccountGroup postAdminAccountGroup =
			testPostAccountGroup_addAdminAccountGroup(randomAdminAccountGroup);

		assertEquals(randomAdminAccountGroup, postAdminAccountGroup);
		assertValid(postAdminAccountGroup);
	}

	protected AdminAccountGroup testPostAccountGroup_addAdminAccountGroup(
			AdminAccountGroup adminAccountGroup)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteAccountGroupByExternalReferenceCode()
		throws Exception {

		@SuppressWarnings("PMD.UnusedLocalVariable")
		AdminAccountGroup adminAccountGroup =
			testDeleteAccountGroupByExternalReferenceCode_addAdminAccountGroup();

		assertHttpResponseStatusCode(
			204,
			adminAccountGroupResource.
				deleteAccountGroupByExternalReferenceCodeHttpResponse(
					adminAccountGroup.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			adminAccountGroupResource.
				getAccountGroupByExternalReferenceCodeHttpResponse(
					adminAccountGroup.getExternalReferenceCode()));

		assertHttpResponseStatusCode(
			404,
			adminAccountGroupResource.
				getAccountGroupByExternalReferenceCodeHttpResponse(
					adminAccountGroup.getExternalReferenceCode()));
	}

	protected AdminAccountGroup
			testDeleteAccountGroupByExternalReferenceCode_addAdminAccountGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountGroupByExternalReferenceCode() throws Exception {
		AdminAccountGroup postAdminAccountGroup =
			testGetAccountGroupByExternalReferenceCode_addAdminAccountGroup();

		AdminAccountGroup getAdminAccountGroup =
			adminAccountGroupResource.getAccountGroupByExternalReferenceCode(
				postAdminAccountGroup.getExternalReferenceCode());

		assertEquals(postAdminAccountGroup, getAdminAccountGroup);
		assertValid(getAdminAccountGroup);
	}

	protected AdminAccountGroup
			testGetAccountGroupByExternalReferenceCode_addAdminAccountGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetAccountGroupByExternalReferenceCode()
		throws Exception {

		AdminAccountGroup adminAccountGroup =
			testGraphQLGetAccountGroupByExternalReferenceCode_addAdminAccountGroup();

		Assert.assertTrue(
			equals(
				adminAccountGroup,
				AdminAccountGroupSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"accountGroupByExternalReferenceCode",
								new HashMap<String, Object>() {
									{
										put(
											"externalReferenceCode",
											"\"" +
												adminAccountGroup.
													getExternalReferenceCode() +
														"\"");
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/accountGroupByExternalReferenceCode"))));
	}

	@Test
	public void testGraphQLGetAccountGroupByExternalReferenceCodeNotFound()
		throws Exception {

		String irrelevantExternalReferenceCode =
			"\"" + RandomTestUtil.randomString() + "\"";

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"accountGroupByExternalReferenceCode",
						new HashMap<String, Object>() {
							{
								put(
									"externalReferenceCode",
									irrelevantExternalReferenceCode);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected AdminAccountGroup
			testGraphQLGetAccountGroupByExternalReferenceCode_addAdminAccountGroup()
		throws Exception {

		return testGraphQLAdminAccountGroup_addAdminAccountGroup();
	}

	@Test
	public void testPatchAccountGroupByExternalReferenceCode()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testDeleteAccountGroup() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		AdminAccountGroup adminAccountGroup =
			testDeleteAccountGroup_addAdminAccountGroup();

		assertHttpResponseStatusCode(
			204,
			adminAccountGroupResource.deleteAccountGroupHttpResponse(
				adminAccountGroup.getId()));

		assertHttpResponseStatusCode(
			404,
			adminAccountGroupResource.getAccountGroupHttpResponse(
				adminAccountGroup.getId()));

		assertHttpResponseStatusCode(
			404,
			adminAccountGroupResource.getAccountGroupHttpResponse(
				adminAccountGroup.getId()));
	}

	protected AdminAccountGroup testDeleteAccountGroup_addAdminAccountGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountGroup() throws Exception {
		AdminAccountGroup postAdminAccountGroup =
			testGetAccountGroup_addAdminAccountGroup();

		AdminAccountGroup getAdminAccountGroup =
			adminAccountGroupResource.getAccountGroup(
				postAdminAccountGroup.getId());

		assertEquals(postAdminAccountGroup, getAdminAccountGroup);
		assertValid(getAdminAccountGroup);
	}

	protected AdminAccountGroup testGetAccountGroup_addAdminAccountGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetAccountGroup() throws Exception {
		AdminAccountGroup adminAccountGroup =
			testGraphQLGetAccountGroup_addAdminAccountGroup();

		Assert.assertTrue(
			equals(
				adminAccountGroup,
				AdminAccountGroupSerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"accountGroup",
								new HashMap<String, Object>() {
									{
										put("id", adminAccountGroup.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/accountGroup"))));
	}

	@Test
	public void testGraphQLGetAccountGroupNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"accountGroup",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected AdminAccountGroup
			testGraphQLGetAccountGroup_addAdminAccountGroup()
		throws Exception {

		return testGraphQLAdminAccountGroup_addAdminAccountGroup();
	}

	@Test
	public void testPatchAccountGroup() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountGroupsPage()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountGroupsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountGroupsPage_getIrrelevantExternalReferenceCode();

		Page<AdminAccountGroup> page =
			adminAccountGroupResource.
				getAccountByExternalReferenceCodeAccountGroupsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			AdminAccountGroup irrelevantAdminAccountGroup =
				testGetAccountByExternalReferenceCodeAccountGroupsPage_addAdminAccountGroup(
					irrelevantExternalReferenceCode,
					randomIrrelevantAdminAccountGroup());

			page =
				adminAccountGroupResource.
					getAccountByExternalReferenceCodeAccountGroupsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAdminAccountGroup),
				(List<AdminAccountGroup>)page.getItems());
			assertValid(
				page,
				testGetAccountByExternalReferenceCodeAccountGroupsPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		AdminAccountGroup adminAccountGroup1 =
			testGetAccountByExternalReferenceCodeAccountGroupsPage_addAdminAccountGroup(
				externalReferenceCode, randomAdminAccountGroup());

		AdminAccountGroup adminAccountGroup2 =
			testGetAccountByExternalReferenceCodeAccountGroupsPage_addAdminAccountGroup(
				externalReferenceCode, randomAdminAccountGroup());

		page =
			adminAccountGroupResource.
				getAccountByExternalReferenceCodeAccountGroupsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(adminAccountGroup1, adminAccountGroup2),
			(List<AdminAccountGroup>)page.getItems());
		assertValid(
			page,
			testGetAccountByExternalReferenceCodeAccountGroupsPage_getExpectedActions(
				externalReferenceCode));
	}

	protected Map<String, Map<String, String>>
			testGetAccountByExternalReferenceCodeAccountGroupsPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountGroupsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountGroupsPage_getExternalReferenceCode();

		AdminAccountGroup adminAccountGroup1 =
			testGetAccountByExternalReferenceCodeAccountGroupsPage_addAdminAccountGroup(
				externalReferenceCode, randomAdminAccountGroup());

		AdminAccountGroup adminAccountGroup2 =
			testGetAccountByExternalReferenceCodeAccountGroupsPage_addAdminAccountGroup(
				externalReferenceCode, randomAdminAccountGroup());

		AdminAccountGroup adminAccountGroup3 =
			testGetAccountByExternalReferenceCodeAccountGroupsPage_addAdminAccountGroup(
				externalReferenceCode, randomAdminAccountGroup());

		Page<AdminAccountGroup> page1 =
			adminAccountGroupResource.
				getAccountByExternalReferenceCodeAccountGroupsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<AdminAccountGroup> adminAccountGroups1 =
			(List<AdminAccountGroup>)page1.getItems();

		Assert.assertEquals(
			adminAccountGroups1.toString(), 2, adminAccountGroups1.size());

		Page<AdminAccountGroup> page2 =
			adminAccountGroupResource.
				getAccountByExternalReferenceCodeAccountGroupsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AdminAccountGroup> adminAccountGroups2 =
			(List<AdminAccountGroup>)page2.getItems();

		Assert.assertEquals(
			adminAccountGroups2.toString(), 1, adminAccountGroups2.size());

		Page<AdminAccountGroup> page3 =
			adminAccountGroupResource.
				getAccountByExternalReferenceCodeAccountGroupsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				adminAccountGroup1, adminAccountGroup2, adminAccountGroup3),
			(List<AdminAccountGroup>)page3.getItems());
	}

	protected AdminAccountGroup
			testGetAccountByExternalReferenceCodeAccountGroupsPage_addAdminAccountGroup(
				String externalReferenceCode,
				AdminAccountGroup adminAccountGroup)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountGroupsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountGroupsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testGetAccountIdAccountGroupsPage() throws Exception {
		Long id = testGetAccountIdAccountGroupsPage_getId();
		Long irrelevantId = testGetAccountIdAccountGroupsPage_getIrrelevantId();

		Page<AdminAccountGroup> page =
			adminAccountGroupResource.getAccountIdAccountGroupsPage(
				id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			AdminAccountGroup irrelevantAdminAccountGroup =
				testGetAccountIdAccountGroupsPage_addAdminAccountGroup(
					irrelevantId, randomIrrelevantAdminAccountGroup());

			page = adminAccountGroupResource.getAccountIdAccountGroupsPage(
				irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAdminAccountGroup),
				(List<AdminAccountGroup>)page.getItems());
			assertValid(
				page,
				testGetAccountIdAccountGroupsPage_getExpectedActions(
					irrelevantId));
		}

		AdminAccountGroup adminAccountGroup1 =
			testGetAccountIdAccountGroupsPage_addAdminAccountGroup(
				id, randomAdminAccountGroup());

		AdminAccountGroup adminAccountGroup2 =
			testGetAccountIdAccountGroupsPage_addAdminAccountGroup(
				id, randomAdminAccountGroup());

		page = adminAccountGroupResource.getAccountIdAccountGroupsPage(
			id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(adminAccountGroup1, adminAccountGroup2),
			(List<AdminAccountGroup>)page.getItems());
		assertValid(
			page, testGetAccountIdAccountGroupsPage_getExpectedActions(id));
	}

	protected Map<String, Map<String, String>>
			testGetAccountIdAccountGroupsPage_getExpectedActions(Long id)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountIdAccountGroupsPageWithPagination()
		throws Exception {

		Long id = testGetAccountIdAccountGroupsPage_getId();

		AdminAccountGroup adminAccountGroup1 =
			testGetAccountIdAccountGroupsPage_addAdminAccountGroup(
				id, randomAdminAccountGroup());

		AdminAccountGroup adminAccountGroup2 =
			testGetAccountIdAccountGroupsPage_addAdminAccountGroup(
				id, randomAdminAccountGroup());

		AdminAccountGroup adminAccountGroup3 =
			testGetAccountIdAccountGroupsPage_addAdminAccountGroup(
				id, randomAdminAccountGroup());

		Page<AdminAccountGroup> page1 =
			adminAccountGroupResource.getAccountIdAccountGroupsPage(
				id, Pagination.of(1, 2));

		List<AdminAccountGroup> adminAccountGroups1 =
			(List<AdminAccountGroup>)page1.getItems();

		Assert.assertEquals(
			adminAccountGroups1.toString(), 2, adminAccountGroups1.size());

		Page<AdminAccountGroup> page2 =
			adminAccountGroupResource.getAccountIdAccountGroupsPage(
				id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AdminAccountGroup> adminAccountGroups2 =
			(List<AdminAccountGroup>)page2.getItems();

		Assert.assertEquals(
			adminAccountGroups2.toString(), 1, adminAccountGroups2.size());

		Page<AdminAccountGroup> page3 =
			adminAccountGroupResource.getAccountIdAccountGroupsPage(
				id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				adminAccountGroup1, adminAccountGroup2, adminAccountGroup3),
			(List<AdminAccountGroup>)page3.getItems());
	}

	protected AdminAccountGroup
			testGetAccountIdAccountGroupsPage_addAdminAccountGroup(
				Long id, AdminAccountGroup adminAccountGroup)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountGroupsPage_getId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountGroupsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

	protected AdminAccountGroup
			testGraphQLAdminAccountGroup_addAdminAccountGroup()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		AdminAccountGroup adminAccountGroup,
		List<AdminAccountGroup> adminAccountGroups) {

		boolean contains = false;

		for (AdminAccountGroup item : adminAccountGroups) {
			if (equals(adminAccountGroup, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			adminAccountGroups + " does not contain " + adminAccountGroup,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		AdminAccountGroup adminAccountGroup1,
		AdminAccountGroup adminAccountGroup2) {

		Assert.assertTrue(
			adminAccountGroup1 + " does not equal " + adminAccountGroup2,
			equals(adminAccountGroup1, adminAccountGroup2));
	}

	protected void assertEquals(
		List<AdminAccountGroup> adminAccountGroups1,
		List<AdminAccountGroup> adminAccountGroups2) {

		Assert.assertEquals(
			adminAccountGroups1.size(), adminAccountGroups2.size());

		for (int i = 0; i < adminAccountGroups1.size(); i++) {
			AdminAccountGroup adminAccountGroup1 = adminAccountGroups1.get(i);
			AdminAccountGroup adminAccountGroup2 = adminAccountGroups2.get(i);

			assertEquals(adminAccountGroup1, adminAccountGroup2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<AdminAccountGroup> adminAccountGroups1,
		List<AdminAccountGroup> adminAccountGroups2) {

		Assert.assertEquals(
			adminAccountGroups1.size(), adminAccountGroups2.size());

		for (AdminAccountGroup adminAccountGroup1 : adminAccountGroups1) {
			boolean contains = false;

			for (AdminAccountGroup adminAccountGroup2 : adminAccountGroups2) {
				if (equals(adminAccountGroup1, adminAccountGroup2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				adminAccountGroups2 + " does not contain " + adminAccountGroup1,
				contains);
		}
	}

	protected void assertValid(AdminAccountGroup adminAccountGroup)
		throws Exception {

		boolean valid = true;

		if (adminAccountGroup.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (adminAccountGroup.getCustomFields() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (adminAccountGroup.getDescription() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (adminAccountGroup.getExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (adminAccountGroup.getName() == null) {
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

	protected void assertValid(Page<AdminAccountGroup> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<AdminAccountGroup> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<AdminAccountGroup> adminAccountGroups =
			page.getItems();

		int size = adminAccountGroups.size();

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
					com.liferay.headless.commerce.admin.account.dto.v1_0.
						AdminAccountGroup.class)) {

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
		AdminAccountGroup adminAccountGroup1,
		AdminAccountGroup adminAccountGroup2) {

		if (adminAccountGroup1 == adminAccountGroup2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("customFields", additionalAssertFieldName)) {
				if (!equals(
						(Map)adminAccountGroup1.getCustomFields(),
						(Map)adminAccountGroup2.getCustomFields())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("description", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						adminAccountGroup1.getDescription(),
						adminAccountGroup2.getDescription())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"externalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						adminAccountGroup1.getExternalReferenceCode(),
						adminAccountGroup2.getExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						adminAccountGroup1.getId(),
						adminAccountGroup2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("name", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						adminAccountGroup1.getName(),
						adminAccountGroup2.getName())) {

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

		if (!(_adminAccountGroupResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_adminAccountGroupResource;

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
		AdminAccountGroup adminAccountGroup) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("customFields")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(adminAccountGroup.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("externalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(adminAccountGroup.getExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(adminAccountGroup.getName()));
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

	protected AdminAccountGroup randomAdminAccountGroup() throws Exception {
		return new AdminAccountGroup() {
			{
				description = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				externalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				id = RandomTestUtil.randomLong();
				name = StringUtil.toLowerCase(RandomTestUtil.randomString());
			}
		};
	}

	protected AdminAccountGroup randomIrrelevantAdminAccountGroup()
		throws Exception {

		AdminAccountGroup randomIrrelevantAdminAccountGroup =
			randomAdminAccountGroup();

		return randomIrrelevantAdminAccountGroup;
	}

	protected AdminAccountGroup randomPatchAdminAccountGroup()
		throws Exception {

		return randomAdminAccountGroup();
	}

	protected AdminAccountGroupResource adminAccountGroupResource;
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
		LogFactoryUtil.getLog(BaseAdminAccountGroupResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.account.resource.v1_0.
		AdminAccountGroupResource _adminAccountGroupResource;

}