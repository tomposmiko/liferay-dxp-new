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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.GroupedProduct;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.GroupedProductResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.GroupedProductSerDes;
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
public abstract class BaseGroupedProductResourceTestCase {

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

		_groupedProductResource.setContextCompany(testCompany);

		GroupedProductResource.Builder builder =
			GroupedProductResource.builder();

		groupedProductResource = builder.authentication(
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

		GroupedProduct groupedProduct1 = randomGroupedProduct();

		String json = objectMapper.writeValueAsString(groupedProduct1);

		GroupedProduct groupedProduct2 = GroupedProductSerDes.toDTO(json);

		Assert.assertTrue(equals(groupedProduct1, groupedProduct2));
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

		GroupedProduct groupedProduct = randomGroupedProduct();

		String json1 = objectMapper.writeValueAsString(groupedProduct);
		String json2 = GroupedProductSerDes.toJSON(groupedProduct);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		GroupedProduct groupedProduct = randomGroupedProduct();

		groupedProduct.setEntryProductExternalReferenceCode(regex);
		groupedProduct.setProductExternalReferenceCode(regex);

		String json = GroupedProductSerDes.toJSON(groupedProduct);

		Assert.assertFalse(json.contains(regex));

		groupedProduct = GroupedProductSerDes.toDTO(json);

		Assert.assertEquals(
			regex, groupedProduct.getEntryProductExternalReferenceCode());
		Assert.assertEquals(
			regex, groupedProduct.getProductExternalReferenceCode());
	}

	@Test
	public void testDeleteGroupedProduct() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		GroupedProduct groupedProduct =
			testDeleteGroupedProduct_addGroupedProduct();

		assertHttpResponseStatusCode(
			204,
			groupedProductResource.deleteGroupedProductHttpResponse(
				groupedProduct.getId()));
	}

	protected GroupedProduct testDeleteGroupedProduct_addGroupedProduct()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLDeleteGroupedProduct() throws Exception {
		GroupedProduct groupedProduct =
			testGraphQLDeleteGroupedProduct_addGroupedProduct();

		Assert.assertTrue(
			JSONUtil.getValueAsBoolean(
				invokeGraphQLMutation(
					new GraphQLField(
						"deleteGroupedProduct",
						new HashMap<String, Object>() {
							{
								put("groupedProductId", groupedProduct.getId());
							}
						})),
				"JSONObject/data", "Object/deleteGroupedProduct"));
	}

	protected GroupedProduct testGraphQLDeleteGroupedProduct_addGroupedProduct()
		throws Exception {

		return testGraphQLGroupedProduct_addGroupedProduct();
	}

	@Test
	public void testPatchGroupedProduct() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGetProductByExternalReferenceCodeGroupedProductsPage()
		throws Exception {

		String externalReferenceCode =
			testGetProductByExternalReferenceCodeGroupedProductsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetProductByExternalReferenceCodeGroupedProductsPage_getIrrelevantExternalReferenceCode();

		Page<GroupedProduct> page =
			groupedProductResource.
				getProductByExternalReferenceCodeGroupedProductsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			GroupedProduct irrelevantGroupedProduct =
				testGetProductByExternalReferenceCodeGroupedProductsPage_addGroupedProduct(
					irrelevantExternalReferenceCode,
					randomIrrelevantGroupedProduct());

			page =
				groupedProductResource.
					getProductByExternalReferenceCodeGroupedProductsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantGroupedProduct),
				(List<GroupedProduct>)page.getItems());
			assertValid(
				page,
				testGetProductByExternalReferenceCodeGroupedProductsPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		GroupedProduct groupedProduct1 =
			testGetProductByExternalReferenceCodeGroupedProductsPage_addGroupedProduct(
				externalReferenceCode, randomGroupedProduct());

		GroupedProduct groupedProduct2 =
			testGetProductByExternalReferenceCodeGroupedProductsPage_addGroupedProduct(
				externalReferenceCode, randomGroupedProduct());

		page =
			groupedProductResource.
				getProductByExternalReferenceCodeGroupedProductsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(groupedProduct1, groupedProduct2),
			(List<GroupedProduct>)page.getItems());
		assertValid(
			page,
			testGetProductByExternalReferenceCodeGroupedProductsPage_getExpectedActions(
				externalReferenceCode));

		groupedProductResource.deleteGroupedProduct(groupedProduct1.getId());

		groupedProductResource.deleteGroupedProduct(groupedProduct2.getId());
	}

	protected Map<String, Map<String, String>>
			testGetProductByExternalReferenceCodeGroupedProductsPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetProductByExternalReferenceCodeGroupedProductsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetProductByExternalReferenceCodeGroupedProductsPage_getExternalReferenceCode();

		GroupedProduct groupedProduct1 =
			testGetProductByExternalReferenceCodeGroupedProductsPage_addGroupedProduct(
				externalReferenceCode, randomGroupedProduct());

		GroupedProduct groupedProduct2 =
			testGetProductByExternalReferenceCodeGroupedProductsPage_addGroupedProduct(
				externalReferenceCode, randomGroupedProduct());

		GroupedProduct groupedProduct3 =
			testGetProductByExternalReferenceCodeGroupedProductsPage_addGroupedProduct(
				externalReferenceCode, randomGroupedProduct());

		Page<GroupedProduct> page1 =
			groupedProductResource.
				getProductByExternalReferenceCodeGroupedProductsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<GroupedProduct> groupedProducts1 =
			(List<GroupedProduct>)page1.getItems();

		Assert.assertEquals(
			groupedProducts1.toString(), 2, groupedProducts1.size());

		Page<GroupedProduct> page2 =
			groupedProductResource.
				getProductByExternalReferenceCodeGroupedProductsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<GroupedProduct> groupedProducts2 =
			(List<GroupedProduct>)page2.getItems();

		Assert.assertEquals(
			groupedProducts2.toString(), 1, groupedProducts2.size());

		Page<GroupedProduct> page3 =
			groupedProductResource.
				getProductByExternalReferenceCodeGroupedProductsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(groupedProduct1, groupedProduct2, groupedProduct3),
			(List<GroupedProduct>)page3.getItems());
	}

	protected GroupedProduct
			testGetProductByExternalReferenceCodeGroupedProductsPage_addGroupedProduct(
				String externalReferenceCode, GroupedProduct groupedProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetProductByExternalReferenceCodeGroupedProductsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetProductByExternalReferenceCodeGroupedProductsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostProductByExternalReferenceCodeGroupedProduct()
		throws Exception {

		GroupedProduct randomGroupedProduct = randomGroupedProduct();

		GroupedProduct postGroupedProduct =
			testPostProductByExternalReferenceCodeGroupedProduct_addGroupedProduct(
				randomGroupedProduct);

		assertEquals(randomGroupedProduct, postGroupedProduct);
		assertValid(postGroupedProduct);
	}

	protected GroupedProduct
			testPostProductByExternalReferenceCodeGroupedProduct_addGroupedProduct(
				GroupedProduct groupedProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetProductIdGroupedProductsPage() throws Exception {
		Long productId = testGetProductIdGroupedProductsPage_getProductId();
		Long irrelevantProductId =
			testGetProductIdGroupedProductsPage_getIrrelevantProductId();

		Page<GroupedProduct> page =
			groupedProductResource.getProductIdGroupedProductsPage(
				productId, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantProductId != null) {
			GroupedProduct irrelevantGroupedProduct =
				testGetProductIdGroupedProductsPage_addGroupedProduct(
					irrelevantProductId, randomIrrelevantGroupedProduct());

			page = groupedProductResource.getProductIdGroupedProductsPage(
				irrelevantProductId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantGroupedProduct),
				(List<GroupedProduct>)page.getItems());
			assertValid(
				page,
				testGetProductIdGroupedProductsPage_getExpectedActions(
					irrelevantProductId));
		}

		GroupedProduct groupedProduct1 =
			testGetProductIdGroupedProductsPage_addGroupedProduct(
				productId, randomGroupedProduct());

		GroupedProduct groupedProduct2 =
			testGetProductIdGroupedProductsPage_addGroupedProduct(
				productId, randomGroupedProduct());

		page = groupedProductResource.getProductIdGroupedProductsPage(
			productId, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(groupedProduct1, groupedProduct2),
			(List<GroupedProduct>)page.getItems());
		assertValid(
			page,
			testGetProductIdGroupedProductsPage_getExpectedActions(productId));

		groupedProductResource.deleteGroupedProduct(groupedProduct1.getId());

		groupedProductResource.deleteGroupedProduct(groupedProduct2.getId());
	}

	protected Map<String, Map<String, String>>
			testGetProductIdGroupedProductsPage_getExpectedActions(
				Long productId)
		throws Exception {

		Map<String, Map<String, String>> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetProductIdGroupedProductsPageWithPagination()
		throws Exception {

		Long productId = testGetProductIdGroupedProductsPage_getProductId();

		GroupedProduct groupedProduct1 =
			testGetProductIdGroupedProductsPage_addGroupedProduct(
				productId, randomGroupedProduct());

		GroupedProduct groupedProduct2 =
			testGetProductIdGroupedProductsPage_addGroupedProduct(
				productId, randomGroupedProduct());

		GroupedProduct groupedProduct3 =
			testGetProductIdGroupedProductsPage_addGroupedProduct(
				productId, randomGroupedProduct());

		Page<GroupedProduct> page1 =
			groupedProductResource.getProductIdGroupedProductsPage(
				productId, Pagination.of(1, 2));

		List<GroupedProduct> groupedProducts1 =
			(List<GroupedProduct>)page1.getItems();

		Assert.assertEquals(
			groupedProducts1.toString(), 2, groupedProducts1.size());

		Page<GroupedProduct> page2 =
			groupedProductResource.getProductIdGroupedProductsPage(
				productId, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<GroupedProduct> groupedProducts2 =
			(List<GroupedProduct>)page2.getItems();

		Assert.assertEquals(
			groupedProducts2.toString(), 1, groupedProducts2.size());

		Page<GroupedProduct> page3 =
			groupedProductResource.getProductIdGroupedProductsPage(
				productId, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(groupedProduct1, groupedProduct2, groupedProduct3),
			(List<GroupedProduct>)page3.getItems());
	}

	protected GroupedProduct
			testGetProductIdGroupedProductsPage_addGroupedProduct(
				Long productId, GroupedProduct groupedProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductIdGroupedProductsPage_getProductId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetProductIdGroupedProductsPage_getIrrelevantProductId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostProductIdGroupedProduct() throws Exception {
		GroupedProduct randomGroupedProduct = randomGroupedProduct();

		GroupedProduct postGroupedProduct =
			testPostProductIdGroupedProduct_addGroupedProduct(
				randomGroupedProduct);

		assertEquals(randomGroupedProduct, postGroupedProduct);
		assertValid(postGroupedProduct);
	}

	protected GroupedProduct testPostProductIdGroupedProduct_addGroupedProduct(
			GroupedProduct groupedProduct)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected GroupedProduct testGraphQLGroupedProduct_addGroupedProduct()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		GroupedProduct groupedProduct, List<GroupedProduct> groupedProducts) {

		boolean contains = false;

		for (GroupedProduct item : groupedProducts) {
			if (equals(groupedProduct, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			groupedProducts + " does not contain " + groupedProduct, contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		GroupedProduct groupedProduct1, GroupedProduct groupedProduct2) {

		Assert.assertTrue(
			groupedProduct1 + " does not equal " + groupedProduct2,
			equals(groupedProduct1, groupedProduct2));
	}

	protected void assertEquals(
		List<GroupedProduct> groupedProducts1,
		List<GroupedProduct> groupedProducts2) {

		Assert.assertEquals(groupedProducts1.size(), groupedProducts2.size());

		for (int i = 0; i < groupedProducts1.size(); i++) {
			GroupedProduct groupedProduct1 = groupedProducts1.get(i);
			GroupedProduct groupedProduct2 = groupedProducts2.get(i);

			assertEquals(groupedProduct1, groupedProduct2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<GroupedProduct> groupedProducts1,
		List<GroupedProduct> groupedProducts2) {

		Assert.assertEquals(groupedProducts1.size(), groupedProducts2.size());

		for (GroupedProduct groupedProduct1 : groupedProducts1) {
			boolean contains = false;

			for (GroupedProduct groupedProduct2 : groupedProducts2) {
				if (equals(groupedProduct1, groupedProduct2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				groupedProducts2 + " does not contain " + groupedProduct1,
				contains);
		}
	}

	protected void assertValid(GroupedProduct groupedProduct) throws Exception {
		boolean valid = true;

		if (groupedProduct.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"entryProductExternalReferenceCode",
					additionalAssertFieldName)) {

				if (groupedProduct.getEntryProductExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("entryProductId", additionalAssertFieldName)) {
				if (groupedProduct.getEntryProductId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("entryProductName", additionalAssertFieldName)) {
				if (groupedProduct.getEntryProductName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (groupedProduct.getPriority() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"productExternalReferenceCode",
					additionalAssertFieldName)) {

				if (groupedProduct.getProductExternalReferenceCode() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (groupedProduct.getProductId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("productName", additionalAssertFieldName)) {
				if (groupedProduct.getProductName() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("quantity", additionalAssertFieldName)) {
				if (groupedProduct.getQuantity() == null) {
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

	protected void assertValid(Page<GroupedProduct> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<GroupedProduct> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<GroupedProduct> groupedProducts = page.getItems();

		int size = groupedProducts.size();

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
						GroupedProduct.class)) {

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
		GroupedProduct groupedProduct1, GroupedProduct groupedProduct2) {

		if (groupedProduct1 == groupedProduct2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"entryProductExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						groupedProduct1.getEntryProductExternalReferenceCode(),
						groupedProduct2.
							getEntryProductExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("entryProductId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						groupedProduct1.getEntryProductId(),
						groupedProduct2.getEntryProductId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("entryProductName", additionalAssertFieldName)) {
				if (!equals(
						(Map)groupedProduct1.getEntryProductName(),
						(Map)groupedProduct2.getEntryProductName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						groupedProduct1.getId(), groupedProduct2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						groupedProduct1.getPriority(),
						groupedProduct2.getPriority())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"productExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						groupedProduct1.getProductExternalReferenceCode(),
						groupedProduct2.getProductExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						groupedProduct1.getProductId(),
						groupedProduct2.getProductId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("productName", additionalAssertFieldName)) {
				if (!equals(
						(Map)groupedProduct1.getProductName(),
						(Map)groupedProduct2.getProductName())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("quantity", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						groupedProduct1.getQuantity(),
						groupedProduct2.getQuantity())) {

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

		if (!(_groupedProductResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_groupedProductResource;

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
		GroupedProduct groupedProduct) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("entryProductExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					groupedProduct.getEntryProductExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("entryProductId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("entryProductName")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("priority")) {
			sb.append(String.valueOf(groupedProduct.getPriority()));

			return sb.toString();
		}

		if (entityFieldName.equals("productExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					groupedProduct.getProductExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("productId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("productName")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("quantity")) {
			sb.append(String.valueOf(groupedProduct.getQuantity()));

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

	protected GroupedProduct randomGroupedProduct() throws Exception {
		return new GroupedProduct() {
			{
				entryProductExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				entryProductId = RandomTestUtil.randomLong();
				id = RandomTestUtil.randomLong();
				priority = RandomTestUtil.randomDouble();
				productExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				productId = RandomTestUtil.randomLong();
				quantity = RandomTestUtil.randomInt();
			}
		};
	}

	protected GroupedProduct randomIrrelevantGroupedProduct() throws Exception {
		GroupedProduct randomIrrelevantGroupedProduct = randomGroupedProduct();

		return randomIrrelevantGroupedProduct;
	}

	protected GroupedProduct randomPatchGroupedProduct() throws Exception {
		return randomGroupedProduct();
	}

	protected GroupedProductResource groupedProductResource;
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
		LogFactoryUtil.getLog(BaseGroupedProductResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		GroupedProductResource _groupedProductResource;

}