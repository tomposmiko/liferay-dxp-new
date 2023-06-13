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

import com.liferay.headless.commerce.admin.account.client.dto.v1_0.AccountChannelEntry;
import com.liferay.headless.commerce.admin.account.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.account.client.pagination.Page;
import com.liferay.headless.commerce.admin.account.client.pagination.Pagination;
import com.liferay.headless.commerce.admin.account.client.resource.v1_0.AccountChannelEntryResource;
import com.liferay.headless.commerce.admin.account.client.serdes.v1_0.AccountChannelEntrySerDes;
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
 * @author Alessio Antonio Rendina
 * @generated
 */
@Generated("")
public abstract class BaseAccountChannelEntryResourceTestCase {

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

		_accountChannelEntryResource.setContextCompany(testCompany);

		AccountChannelEntryResource.Builder builder =
			AccountChannelEntryResource.builder();

		accountChannelEntryResource = builder.authentication(
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

		AccountChannelEntry accountChannelEntry1 = randomAccountChannelEntry();

		String json = objectMapper.writeValueAsString(accountChannelEntry1);

		AccountChannelEntry accountChannelEntry2 =
			AccountChannelEntrySerDes.toDTO(json);

		Assert.assertTrue(equals(accountChannelEntry1, accountChannelEntry2));
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

		AccountChannelEntry accountChannelEntry = randomAccountChannelEntry();

		String json1 = objectMapper.writeValueAsString(accountChannelEntry);
		String json2 = AccountChannelEntrySerDes.toJSON(accountChannelEntry);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		AccountChannelEntry accountChannelEntry = randomAccountChannelEntry();

		accountChannelEntry.setAccountExternalReferenceCode(regex);
		accountChannelEntry.setChannelExternalReferenceCode(regex);
		accountChannelEntry.setClassExternalReferenceCode(regex);

		String json = AccountChannelEntrySerDes.toJSON(accountChannelEntry);

		Assert.assertFalse(json.contains(regex));

		accountChannelEntry = AccountChannelEntrySerDes.toDTO(json);

		Assert.assertEquals(
			regex, accountChannelEntry.getAccountExternalReferenceCode());
		Assert.assertEquals(
			regex, accountChannelEntry.getChannelExternalReferenceCode());
		Assert.assertEquals(
			regex, accountChannelEntry.getClassExternalReferenceCode());
	}

	@Test
	public void testDeleteAccountChannelBillingAddressId() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry accountChannelEntry =
			testDeleteAccountChannelBillingAddressId_addAccountChannelEntry();

		assertHttpResponseStatusCode(
			204,
			accountChannelEntryResource.
				deleteAccountChannelBillingAddressIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.
				getAccountChannelBillingAddressIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.
				getAccountChannelBillingAddressIdHttpResponse(
					accountChannelEntry.getId()));
	}

	protected AccountChannelEntry
			testDeleteAccountChannelBillingAddressId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountChannelBillingAddressId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testGetAccountChannelBillingAddressId_addAccountChannelEntry();

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelBillingAddressId(
				postAccountChannelEntry.getId());

		assertEquals(postAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testGetAccountChannelBillingAddressId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetAccountChannelBillingAddressId()
		throws Exception {

		AccountChannelEntry accountChannelEntry =
			testGraphQLGetAccountChannelBillingAddressId_addAccountChannelEntry();

		Assert.assertTrue(
			equals(
				accountChannelEntry,
				AccountChannelEntrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"accountChannelBillingAddressId",
								new HashMap<String, Object>() {
									{
										put("id", accountChannelEntry.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/accountChannelBillingAddressId"))));
	}

	@Test
	public void testGraphQLGetAccountChannelBillingAddressIdNotFound()
		throws Exception {

		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"accountChannelBillingAddressId",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected AccountChannelEntry
			testGraphQLGetAccountChannelBillingAddressId_addAccountChannelEntry()
		throws Exception {

		return testGraphQLAccountChannelEntry_addAccountChannelEntry();
	}

	@Test
	public void testPatchAccountChannelBillingAddressId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testPatchAccountChannelBillingAddressId_addAccountChannelEntry();

		AccountChannelEntry randomPatchAccountChannelEntry =
			randomPatchAccountChannelEntry();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry patchAccountChannelEntry =
			accountChannelEntryResource.patchAccountChannelBillingAddressId(
				postAccountChannelEntry.getId(),
				randomPatchAccountChannelEntry);

		AccountChannelEntry expectedPatchAccountChannelEntry =
			postAccountChannelEntry.clone();

		BeanTestUtil.copyProperties(
			randomPatchAccountChannelEntry, expectedPatchAccountChannelEntry);

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelBillingAddressId(
				patchAccountChannelEntry.getId());

		assertEquals(expectedPatchAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPatchAccountChannelBillingAddressId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteAccountChannelCurrencyId() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry accountChannelEntry =
			testDeleteAccountChannelCurrencyId_addAccountChannelEntry();

		assertHttpResponseStatusCode(
			204,
			accountChannelEntryResource.
				deleteAccountChannelCurrencyIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.getAccountChannelCurrencyIdHttpResponse(
				accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.getAccountChannelCurrencyIdHttpResponse(
				accountChannelEntry.getId()));
	}

	protected AccountChannelEntry
			testDeleteAccountChannelCurrencyId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountChannelCurrencyId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testGetAccountChannelCurrencyId_addAccountChannelEntry();

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelCurrencyId(
				postAccountChannelEntry.getId());

		assertEquals(postAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testGetAccountChannelCurrencyId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetAccountChannelCurrencyId() throws Exception {
		AccountChannelEntry accountChannelEntry =
			testGraphQLGetAccountChannelCurrencyId_addAccountChannelEntry();

		Assert.assertTrue(
			equals(
				accountChannelEntry,
				AccountChannelEntrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"accountChannelCurrencyId",
								new HashMap<String, Object>() {
									{
										put("id", accountChannelEntry.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/accountChannelCurrencyId"))));
	}

	@Test
	public void testGraphQLGetAccountChannelCurrencyIdNotFound()
		throws Exception {

		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"accountChannelCurrencyId",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected AccountChannelEntry
			testGraphQLGetAccountChannelCurrencyId_addAccountChannelEntry()
		throws Exception {

		return testGraphQLAccountChannelEntry_addAccountChannelEntry();
	}

	@Test
	public void testPatchAccountChannelCurrencyId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testPatchAccountChannelCurrencyId_addAccountChannelEntry();

		AccountChannelEntry randomPatchAccountChannelEntry =
			randomPatchAccountChannelEntry();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry patchAccountChannelEntry =
			accountChannelEntryResource.patchAccountChannelCurrencyId(
				postAccountChannelEntry.getId(),
				randomPatchAccountChannelEntry);

		AccountChannelEntry expectedPatchAccountChannelEntry =
			postAccountChannelEntry.clone();

		BeanTestUtil.copyProperties(
			randomPatchAccountChannelEntry, expectedPatchAccountChannelEntry);

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelCurrencyId(
				patchAccountChannelEntry.getId());

		assertEquals(expectedPatchAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPatchAccountChannelCurrencyId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteAccountChannelDeliveryTermId() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry accountChannelEntry =
			testDeleteAccountChannelDeliveryTermId_addAccountChannelEntry();

		assertHttpResponseStatusCode(
			204,
			accountChannelEntryResource.
				deleteAccountChannelDeliveryTermIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.
				getAccountChannelDeliveryTermIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.
				getAccountChannelDeliveryTermIdHttpResponse(
					accountChannelEntry.getId()));
	}

	protected AccountChannelEntry
			testDeleteAccountChannelDeliveryTermId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountChannelDeliveryTermId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testGetAccountChannelDeliveryTermId_addAccountChannelEntry();

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelDeliveryTermId(
				postAccountChannelEntry.getId());

		assertEquals(postAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testGetAccountChannelDeliveryTermId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetAccountChannelDeliveryTermId() throws Exception {
		AccountChannelEntry accountChannelEntry =
			testGraphQLGetAccountChannelDeliveryTermId_addAccountChannelEntry();

		Assert.assertTrue(
			equals(
				accountChannelEntry,
				AccountChannelEntrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"accountChannelDeliveryTermId",
								new HashMap<String, Object>() {
									{
										put("id", accountChannelEntry.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/accountChannelDeliveryTermId"))));
	}

	@Test
	public void testGraphQLGetAccountChannelDeliveryTermIdNotFound()
		throws Exception {

		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"accountChannelDeliveryTermId",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected AccountChannelEntry
			testGraphQLGetAccountChannelDeliveryTermId_addAccountChannelEntry()
		throws Exception {

		return testGraphQLAccountChannelEntry_addAccountChannelEntry();
	}

	@Test
	public void testPatchAccountChannelDeliveryTermId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testPatchAccountChannelDeliveryTermId_addAccountChannelEntry();

		AccountChannelEntry randomPatchAccountChannelEntry =
			randomPatchAccountChannelEntry();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry patchAccountChannelEntry =
			accountChannelEntryResource.patchAccountChannelDeliveryTermId(
				postAccountChannelEntry.getId(),
				randomPatchAccountChannelEntry);

		AccountChannelEntry expectedPatchAccountChannelEntry =
			postAccountChannelEntry.clone();

		BeanTestUtil.copyProperties(
			randomPatchAccountChannelEntry, expectedPatchAccountChannelEntry);

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelDeliveryTermId(
				patchAccountChannelEntry.getId());

		assertEquals(expectedPatchAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPatchAccountChannelDeliveryTermId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteAccountChannelDiscountId() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry accountChannelEntry =
			testDeleteAccountChannelDiscountId_addAccountChannelEntry();

		assertHttpResponseStatusCode(
			204,
			accountChannelEntryResource.
				deleteAccountChannelDiscountIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.getAccountChannelDiscountIdHttpResponse(
				accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.getAccountChannelDiscountIdHttpResponse(
				accountChannelEntry.getId()));
	}

	protected AccountChannelEntry
			testDeleteAccountChannelDiscountId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountChannelDiscountId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testGetAccountChannelDiscountId_addAccountChannelEntry();

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelDiscountId(
				postAccountChannelEntry.getId());

		assertEquals(postAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testGetAccountChannelDiscountId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetAccountChannelDiscountId() throws Exception {
		AccountChannelEntry accountChannelEntry =
			testGraphQLGetAccountChannelDiscountId_addAccountChannelEntry();

		Assert.assertTrue(
			equals(
				accountChannelEntry,
				AccountChannelEntrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"accountChannelDiscountId",
								new HashMap<String, Object>() {
									{
										put("id", accountChannelEntry.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/accountChannelDiscountId"))));
	}

	@Test
	public void testGraphQLGetAccountChannelDiscountIdNotFound()
		throws Exception {

		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"accountChannelDiscountId",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected AccountChannelEntry
			testGraphQLGetAccountChannelDiscountId_addAccountChannelEntry()
		throws Exception {

		return testGraphQLAccountChannelEntry_addAccountChannelEntry();
	}

	@Test
	public void testPatchAccountChannelDiscountId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testPatchAccountChannelDiscountId_addAccountChannelEntry();

		AccountChannelEntry randomPatchAccountChannelEntry =
			randomPatchAccountChannelEntry();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry patchAccountChannelEntry =
			accountChannelEntryResource.patchAccountChannelDiscountId(
				postAccountChannelEntry.getId(),
				randomPatchAccountChannelEntry);

		AccountChannelEntry expectedPatchAccountChannelEntry =
			postAccountChannelEntry.clone();

		BeanTestUtil.copyProperties(
			randomPatchAccountChannelEntry, expectedPatchAccountChannelEntry);

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelDiscountId(
				patchAccountChannelEntry.getId());

		assertEquals(expectedPatchAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPatchAccountChannelDiscountId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteAccountChannelPaymentMethodId() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry accountChannelEntry =
			testDeleteAccountChannelPaymentMethodId_addAccountChannelEntry();

		assertHttpResponseStatusCode(
			204,
			accountChannelEntryResource.
				deleteAccountChannelPaymentMethodIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.
				getAccountChannelPaymentMethodIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.
				getAccountChannelPaymentMethodIdHttpResponse(
					accountChannelEntry.getId()));
	}

	protected AccountChannelEntry
			testDeleteAccountChannelPaymentMethodId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountChannelPaymentMethodId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testGetAccountChannelPaymentMethodId_addAccountChannelEntry();

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelPaymentMethodId(
				postAccountChannelEntry.getId());

		assertEquals(postAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testGetAccountChannelPaymentMethodId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetAccountChannelPaymentMethodId() throws Exception {
		AccountChannelEntry accountChannelEntry =
			testGraphQLGetAccountChannelPaymentMethodId_addAccountChannelEntry();

		Assert.assertTrue(
			equals(
				accountChannelEntry,
				AccountChannelEntrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"accountChannelPaymentMethodId",
								new HashMap<String, Object>() {
									{
										put("id", accountChannelEntry.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/accountChannelPaymentMethodId"))));
	}

	@Test
	public void testGraphQLGetAccountChannelPaymentMethodIdNotFound()
		throws Exception {

		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"accountChannelPaymentMethodId",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected AccountChannelEntry
			testGraphQLGetAccountChannelPaymentMethodId_addAccountChannelEntry()
		throws Exception {

		return testGraphQLAccountChannelEntry_addAccountChannelEntry();
	}

	@Test
	public void testPatchAccountChannelPaymentMethodId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testPatchAccountChannelPaymentMethodId_addAccountChannelEntry();

		AccountChannelEntry randomPatchAccountChannelEntry =
			randomPatchAccountChannelEntry();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry patchAccountChannelEntry =
			accountChannelEntryResource.patchAccountChannelPaymentMethodId(
				postAccountChannelEntry.getId(),
				randomPatchAccountChannelEntry);

		AccountChannelEntry expectedPatchAccountChannelEntry =
			postAccountChannelEntry.clone();

		BeanTestUtil.copyProperties(
			randomPatchAccountChannelEntry, expectedPatchAccountChannelEntry);

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelPaymentMethodId(
				patchAccountChannelEntry.getId());

		assertEquals(expectedPatchAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPatchAccountChannelPaymentMethodId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteAccountChannelPaymentTermId() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry accountChannelEntry =
			testDeleteAccountChannelPaymentTermId_addAccountChannelEntry();

		assertHttpResponseStatusCode(
			204,
			accountChannelEntryResource.
				deleteAccountChannelPaymentTermIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.
				getAccountChannelPaymentTermIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.
				getAccountChannelPaymentTermIdHttpResponse(
					accountChannelEntry.getId()));
	}

	protected AccountChannelEntry
			testDeleteAccountChannelPaymentTermId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountChannelPaymentTermId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testGetAccountChannelPaymentTermId_addAccountChannelEntry();

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelPaymentTermId(
				postAccountChannelEntry.getId());

		assertEquals(postAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testGetAccountChannelPaymentTermId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetAccountChannelPaymentTermId() throws Exception {
		AccountChannelEntry accountChannelEntry =
			testGraphQLGetAccountChannelPaymentTermId_addAccountChannelEntry();

		Assert.assertTrue(
			equals(
				accountChannelEntry,
				AccountChannelEntrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"accountChannelPaymentTermId",
								new HashMap<String, Object>() {
									{
										put("id", accountChannelEntry.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/accountChannelPaymentTermId"))));
	}

	@Test
	public void testGraphQLGetAccountChannelPaymentTermIdNotFound()
		throws Exception {

		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"accountChannelPaymentTermId",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected AccountChannelEntry
			testGraphQLGetAccountChannelPaymentTermId_addAccountChannelEntry()
		throws Exception {

		return testGraphQLAccountChannelEntry_addAccountChannelEntry();
	}

	@Test
	public void testPatchAccountChannelPaymentTermId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testPatchAccountChannelPaymentTermId_addAccountChannelEntry();

		AccountChannelEntry randomPatchAccountChannelEntry =
			randomPatchAccountChannelEntry();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry patchAccountChannelEntry =
			accountChannelEntryResource.patchAccountChannelPaymentTermId(
				postAccountChannelEntry.getId(),
				randomPatchAccountChannelEntry);

		AccountChannelEntry expectedPatchAccountChannelEntry =
			postAccountChannelEntry.clone();

		BeanTestUtil.copyProperties(
			randomPatchAccountChannelEntry, expectedPatchAccountChannelEntry);

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelPaymentTermId(
				patchAccountChannelEntry.getId());

		assertEquals(expectedPatchAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPatchAccountChannelPaymentTermId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteAccountChannelPriceListId() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry accountChannelEntry =
			testDeleteAccountChannelPriceListId_addAccountChannelEntry();

		assertHttpResponseStatusCode(
			204,
			accountChannelEntryResource.
				deleteAccountChannelPriceListIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.
				getAccountChannelPriceListIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.
				getAccountChannelPriceListIdHttpResponse(
					accountChannelEntry.getId()));
	}

	protected AccountChannelEntry
			testDeleteAccountChannelPriceListId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountChannelPriceListId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testGetAccountChannelPriceListId_addAccountChannelEntry();

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelPriceListId(
				postAccountChannelEntry.getId());

		assertEquals(postAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testGetAccountChannelPriceListId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetAccountChannelPriceListId() throws Exception {
		AccountChannelEntry accountChannelEntry =
			testGraphQLGetAccountChannelPriceListId_addAccountChannelEntry();

		Assert.assertTrue(
			equals(
				accountChannelEntry,
				AccountChannelEntrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"accountChannelPriceListId",
								new HashMap<String, Object>() {
									{
										put("id", accountChannelEntry.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/accountChannelPriceListId"))));
	}

	@Test
	public void testGraphQLGetAccountChannelPriceListIdNotFound()
		throws Exception {

		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"accountChannelPriceListId",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected AccountChannelEntry
			testGraphQLGetAccountChannelPriceListId_addAccountChannelEntry()
		throws Exception {

		return testGraphQLAccountChannelEntry_addAccountChannelEntry();
	}

	@Test
	public void testPatchAccountChannelPriceListId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testPatchAccountChannelPriceListId_addAccountChannelEntry();

		AccountChannelEntry randomPatchAccountChannelEntry =
			randomPatchAccountChannelEntry();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry patchAccountChannelEntry =
			accountChannelEntryResource.patchAccountChannelPriceListId(
				postAccountChannelEntry.getId(),
				randomPatchAccountChannelEntry);

		AccountChannelEntry expectedPatchAccountChannelEntry =
			postAccountChannelEntry.clone();

		BeanTestUtil.copyProperties(
			randomPatchAccountChannelEntry, expectedPatchAccountChannelEntry);

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelPriceListId(
				patchAccountChannelEntry.getId());

		assertEquals(expectedPatchAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPatchAccountChannelPriceListId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteAccountChannelShippingAddressId() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry accountChannelEntry =
			testDeleteAccountChannelShippingAddressId_addAccountChannelEntry();

		assertHttpResponseStatusCode(
			204,
			accountChannelEntryResource.
				deleteAccountChannelShippingAddressIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.
				getAccountChannelShippingAddressIdHttpResponse(
					accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.
				getAccountChannelShippingAddressIdHttpResponse(
					accountChannelEntry.getId()));
	}

	protected AccountChannelEntry
			testDeleteAccountChannelShippingAddressId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountChannelShippingAddressId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testGetAccountChannelShippingAddressId_addAccountChannelEntry();

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelShippingAddressId(
				postAccountChannelEntry.getId());

		assertEquals(postAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testGetAccountChannelShippingAddressId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetAccountChannelShippingAddressId()
		throws Exception {

		AccountChannelEntry accountChannelEntry =
			testGraphQLGetAccountChannelShippingAddressId_addAccountChannelEntry();

		Assert.assertTrue(
			equals(
				accountChannelEntry,
				AccountChannelEntrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"accountChannelShippingAddressId",
								new HashMap<String, Object>() {
									{
										put("id", accountChannelEntry.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data",
						"Object/accountChannelShippingAddressId"))));
	}

	@Test
	public void testGraphQLGetAccountChannelShippingAddressIdNotFound()
		throws Exception {

		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"accountChannelShippingAddressId",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected AccountChannelEntry
			testGraphQLGetAccountChannelShippingAddressId_addAccountChannelEntry()
		throws Exception {

		return testGraphQLAccountChannelEntry_addAccountChannelEntry();
	}

	@Test
	public void testPatchAccountChannelShippingAddressId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testPatchAccountChannelShippingAddressId_addAccountChannelEntry();

		AccountChannelEntry randomPatchAccountChannelEntry =
			randomPatchAccountChannelEntry();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry patchAccountChannelEntry =
			accountChannelEntryResource.patchAccountChannelShippingAddressId(
				postAccountChannelEntry.getId(),
				randomPatchAccountChannelEntry);

		AccountChannelEntry expectedPatchAccountChannelEntry =
			postAccountChannelEntry.clone();

		BeanTestUtil.copyProperties(
			randomPatchAccountChannelEntry, expectedPatchAccountChannelEntry);

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelShippingAddressId(
				patchAccountChannelEntry.getId());

		assertEquals(expectedPatchAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPatchAccountChannelShippingAddressId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testDeleteAccountChannelUserId() throws Exception {
		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry accountChannelEntry =
			testDeleteAccountChannelUserId_addAccountChannelEntry();

		assertHttpResponseStatusCode(
			204,
			accountChannelEntryResource.deleteAccountChannelUserIdHttpResponse(
				accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.getAccountChannelUserIdHttpResponse(
				accountChannelEntry.getId()));

		assertHttpResponseStatusCode(
			404,
			accountChannelEntryResource.getAccountChannelUserIdHttpResponse(
				accountChannelEntry.getId()));
	}

	protected AccountChannelEntry
			testDeleteAccountChannelUserId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountChannelUserId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testGetAccountChannelUserId_addAccountChannelEntry();

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelUserId(
				postAccountChannelEntry.getId());

		assertEquals(postAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testGetAccountChannelUserId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGraphQLGetAccountChannelUserId() throws Exception {
		AccountChannelEntry accountChannelEntry =
			testGraphQLGetAccountChannelUserId_addAccountChannelEntry();

		Assert.assertTrue(
			equals(
				accountChannelEntry,
				AccountChannelEntrySerDes.toDTO(
					JSONUtil.getValueAsString(
						invokeGraphQLQuery(
							new GraphQLField(
								"accountChannelUserId",
								new HashMap<String, Object>() {
									{
										put("id", accountChannelEntry.getId());
									}
								},
								getGraphQLFields())),
						"JSONObject/data", "Object/accountChannelUserId"))));
	}

	@Test
	public void testGraphQLGetAccountChannelUserIdNotFound() throws Exception {
		Long irrelevantId = RandomTestUtil.randomLong();

		Assert.assertEquals(
			"Not Found",
			JSONUtil.getValueAsString(
				invokeGraphQLQuery(
					new GraphQLField(
						"accountChannelUserId",
						new HashMap<String, Object>() {
							{
								put("id", irrelevantId);
							}
						},
						getGraphQLFields())),
				"JSONArray/errors", "Object/0", "JSONObject/extensions",
				"Object/code"));
	}

	protected AccountChannelEntry
			testGraphQLGetAccountChannelUserId_addAccountChannelEntry()
		throws Exception {

		return testGraphQLAccountChannelEntry_addAccountChannelEntry();
	}

	@Test
	public void testPatchAccountChannelUserId() throws Exception {
		AccountChannelEntry postAccountChannelEntry =
			testPatchAccountChannelUserId_addAccountChannelEntry();

		AccountChannelEntry randomPatchAccountChannelEntry =
			randomPatchAccountChannelEntry();

		@SuppressWarnings("PMD.UnusedLocalVariable")
		AccountChannelEntry patchAccountChannelEntry =
			accountChannelEntryResource.patchAccountChannelUserId(
				postAccountChannelEntry.getId(),
				randomPatchAccountChannelEntry);

		AccountChannelEntry expectedPatchAccountChannelEntry =
			postAccountChannelEntry.clone();

		BeanTestUtil.copyProperties(
			randomPatchAccountChannelEntry, expectedPatchAccountChannelEntry);

		AccountChannelEntry getAccountChannelEntry =
			accountChannelEntryResource.getAccountChannelUserId(
				patchAccountChannelEntry.getId());

		assertEquals(expectedPatchAccountChannelEntry, getAccountChannelEntry);
		assertValid(getAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPatchAccountChannelUserId_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_getIrrelevantExternalReferenceCode();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelBillingAddressesPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_addAccountChannelEntry(
					irrelevantExternalReferenceCode,
					randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountByExternalReferenceCodeAccountChannelBillingAddressesPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelBillingAddressesPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_getExpectedActions(
				externalReferenceCode));
	}

	protected Map<String, Map>
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_getExternalReferenceCode();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelBillingAddressesPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelBillingAddressesPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelBillingAddressesPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_addAccountChannelEntry(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelBillingAddressesPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountByExternalReferenceCodeAccountChannelBillingAddress()
		throws Exception {

		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountByExternalReferenceCodeAccountChannelBillingAddress_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountByExternalReferenceCodeAccountChannelBillingAddress_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_getIrrelevantExternalReferenceCode();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelCurrenciesPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_addAccountChannelEntry(
					irrelevantExternalReferenceCode,
					randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountByExternalReferenceCodeAccountChannelCurrenciesPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelCurrenciesPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_getExpectedActions(
				externalReferenceCode));
	}

	protected Map<String, Map>
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_getExternalReferenceCode();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelCurrenciesPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelCurrenciesPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelCurrenciesPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_addAccountChannelEntry(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelCurrenciesPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountByExternalReferenceCodeAccountChannelCurrency()
		throws Exception {

		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountByExternalReferenceCodeAccountChannelCurrency_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountByExternalReferenceCodeAccountChannelCurrency_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_getIrrelevantExternalReferenceCode();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_addAccountChannelEntry(
					irrelevantExternalReferenceCode,
					randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_getExpectedActions(
				externalReferenceCode));
	}

	protected Map<String, Map>
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_getExternalReferenceCode();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_addAccountChannelEntry(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelDeliveryTermsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountByExternalReferenceCodeAccountChannelDeliveryTerm()
		throws Exception {

		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountByExternalReferenceCodeAccountChannelDeliveryTerm_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountByExternalReferenceCodeAccountChannelDeliveryTerm_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_getIrrelevantExternalReferenceCode();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelDiscountsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_addAccountChannelEntry(
					irrelevantExternalReferenceCode,
					randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountByExternalReferenceCodeAccountChannelDiscountsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelDiscountsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_getExpectedActions(
				externalReferenceCode));
	}

	protected Map<String, Map>
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelDiscountsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_getExternalReferenceCode();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelDiscountsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelDiscountsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelDiscountsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_addAccountChannelEntry(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelDiscountsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountByExternalReferenceCodeAccountChannelDiscount()
		throws Exception {

		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountByExternalReferenceCodeAccountChannelDiscount_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountByExternalReferenceCodeAccountChannelDiscount_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_getIrrelevantExternalReferenceCode();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_addAccountChannelEntry(
					irrelevantExternalReferenceCode,
					randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_getExpectedActions(
				externalReferenceCode));
	}

	protected Map<String, Map>
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_getExternalReferenceCode();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_addAccountChannelEntry(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelPaymentMethodsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountByExternalReferenceCodeAccountChannelPaymentMethod()
		throws Exception {

		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountByExternalReferenceCodeAccountChannelPaymentMethod_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountByExternalReferenceCodeAccountChannelPaymentMethod_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_getIrrelevantExternalReferenceCode();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPaymentTermsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_addAccountChannelEntry(
					irrelevantExternalReferenceCode,
					randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountByExternalReferenceCodeAccountChannelPaymentTermsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPaymentTermsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_getExpectedActions(
				externalReferenceCode));
	}

	protected Map<String, Map>
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_getExternalReferenceCode();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPaymentTermsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPaymentTermsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPaymentTermsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_addAccountChannelEntry(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelPaymentTermsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountByExternalReferenceCodeAccountChannelPaymentTerm()
		throws Exception {

		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountByExternalReferenceCodeAccountChannelPaymentTerm_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountByExternalReferenceCodeAccountChannelPaymentTerm_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_getIrrelevantExternalReferenceCode();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPriceListsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_addAccountChannelEntry(
					irrelevantExternalReferenceCode,
					randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountByExternalReferenceCodeAccountChannelPriceListsPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPriceListsPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_getExpectedActions(
				externalReferenceCode));
	}

	protected Map<String, Map>
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelPriceListsPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_getExternalReferenceCode();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPriceListsPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPriceListsPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelPriceListsPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_addAccountChannelEntry(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelPriceListsPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountByExternalReferenceCodeAccountChannelPriceList()
		throws Exception {

		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountByExternalReferenceCodeAccountChannelPriceList_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountByExternalReferenceCodeAccountChannelPriceList_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_getIrrelevantExternalReferenceCode();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelShippingAddressesPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_addAccountChannelEntry(
					irrelevantExternalReferenceCode,
					randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountByExternalReferenceCodeAccountChannelShippingAddressesPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelShippingAddressesPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_getExpectedActions(
				externalReferenceCode));
	}

	protected Map<String, Map>
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_getExternalReferenceCode();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelShippingAddressesPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelShippingAddressesPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelShippingAddressesPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_addAccountChannelEntry(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelShippingAddressesPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountByExternalReferenceCodeAccountChannelShippingAddress()
		throws Exception {

		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountByExternalReferenceCodeAccountChannelShippingAddress_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountByExternalReferenceCodeAccountChannelShippingAddress_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelUsersPage()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_getExternalReferenceCode();
		String irrelevantExternalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_getIrrelevantExternalReferenceCode();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelUsersPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantExternalReferenceCode != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountByExternalReferenceCodeAccountChannelUsersPage_addAccountChannelEntry(
					irrelevantExternalReferenceCode,
					randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountByExternalReferenceCodeAccountChannelUsersPage(
						irrelevantExternalReferenceCode, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountByExternalReferenceCodeAccountChannelUsersPage_getExpectedActions(
					irrelevantExternalReferenceCode));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelUsersPage(
					externalReferenceCode, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_getExpectedActions(
				externalReferenceCode));
	}

	protected Map<String, Map>
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_getExpectedActions(
				String externalReferenceCode)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountByExternalReferenceCodeAccountChannelUsersPageWithPagination()
		throws Exception {

		String externalReferenceCode =
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_getExternalReferenceCode();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_addAccountChannelEntry(
				externalReferenceCode, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelUsersPage(
					externalReferenceCode, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelUsersPage(
					externalReferenceCode, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountByExternalReferenceCodeAccountChannelUsersPage(
					externalReferenceCode, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_addAccountChannelEntry(
				String externalReferenceCode,
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_getExternalReferenceCode()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected String
			testGetAccountByExternalReferenceCodeAccountChannelUsersPage_getIrrelevantExternalReferenceCode()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountByExternalReferenceCodeAccountChannelUser()
		throws Exception {

		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountByExternalReferenceCodeAccountChannelUser_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountByExternalReferenceCodeAccountChannelUser_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountIdAccountChannelBillingAddressesPage()
		throws Exception {

		Long id = testGetAccountIdAccountChannelBillingAddressesPage_getId();
		Long irrelevantId =
			testGetAccountIdAccountChannelBillingAddressesPage_getIrrelevantId();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountIdAccountChannelBillingAddressesPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountIdAccountChannelBillingAddressesPage_addAccountChannelEntry(
					irrelevantId, randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountIdAccountChannelBillingAddressesPage(
						irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountIdAccountChannelBillingAddressesPage_getExpectedActions(
					irrelevantId));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelBillingAddressesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelBillingAddressesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountIdAccountChannelBillingAddressesPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountIdAccountChannelBillingAddressesPage_getExpectedActions(
				id));
	}

	protected Map<String, Map>
			testGetAccountIdAccountChannelBillingAddressesPage_getExpectedActions(
				Long id)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountIdAccountChannelBillingAddressesPageWithPagination()
		throws Exception {

		Long id = testGetAccountIdAccountChannelBillingAddressesPage_getId();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelBillingAddressesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelBillingAddressesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountIdAccountChannelBillingAddressesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountIdAccountChannelBillingAddressesPage(
					id, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountIdAccountChannelBillingAddressesPage(
					id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountIdAccountChannelBillingAddressesPage(
					id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountIdAccountChannelBillingAddressesPage_addAccountChannelEntry(
				Long id, AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountChannelBillingAddressesPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetAccountIdAccountChannelBillingAddressesPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountIdAccountChannelBillingAddress()
		throws Exception {

		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountIdAccountChannelBillingAddress_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountIdAccountChannelBillingAddress_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountIdAccountChannelCurrenciesPage()
		throws Exception {

		Long id = testGetAccountIdAccountChannelCurrenciesPage_getId();
		Long irrelevantId =
			testGetAccountIdAccountChannelCurrenciesPage_getIrrelevantId();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountIdAccountChannelCurrenciesPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountIdAccountChannelCurrenciesPage_addAccountChannelEntry(
					irrelevantId, randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountIdAccountChannelCurrenciesPage(
						irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountIdAccountChannelCurrenciesPage_getExpectedActions(
					irrelevantId));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelCurrenciesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelCurrenciesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountIdAccountChannelCurrenciesPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountIdAccountChannelCurrenciesPage_getExpectedActions(
				id));
	}

	protected Map<String, Map>
			testGetAccountIdAccountChannelCurrenciesPage_getExpectedActions(
				Long id)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountIdAccountChannelCurrenciesPageWithPagination()
		throws Exception {

		Long id = testGetAccountIdAccountChannelCurrenciesPage_getId();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelCurrenciesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelCurrenciesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountIdAccountChannelCurrenciesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountIdAccountChannelCurrenciesPage(
					id, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountIdAccountChannelCurrenciesPage(
					id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountIdAccountChannelCurrenciesPage(
					id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountIdAccountChannelCurrenciesPage_addAccountChannelEntry(
				Long id, AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountChannelCurrenciesPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetAccountIdAccountChannelCurrenciesPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountIdAccountChannelCurrency() throws Exception {
		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountIdAccountChannelCurrency_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountIdAccountChannelCurrency_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountIdAccountChannelDeliveryTermsPage()
		throws Exception {

		Long id = testGetAccountIdAccountChannelDeliveryTermsPage_getId();
		Long irrelevantId =
			testGetAccountIdAccountChannelDeliveryTermsPage_getIrrelevantId();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountIdAccountChannelDeliveryTermsPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountIdAccountChannelDeliveryTermsPage_addAccountChannelEntry(
					irrelevantId, randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountIdAccountChannelDeliveryTermsPage(
						irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountIdAccountChannelDeliveryTermsPage_getExpectedActions(
					irrelevantId));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelDeliveryTermsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelDeliveryTermsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountIdAccountChannelDeliveryTermsPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountIdAccountChannelDeliveryTermsPage_getExpectedActions(
				id));
	}

	protected Map<String, Map>
			testGetAccountIdAccountChannelDeliveryTermsPage_getExpectedActions(
				Long id)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountIdAccountChannelDeliveryTermsPageWithPagination()
		throws Exception {

		Long id = testGetAccountIdAccountChannelDeliveryTermsPage_getId();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelDeliveryTermsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelDeliveryTermsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountIdAccountChannelDeliveryTermsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountIdAccountChannelDeliveryTermsPage(
					id, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountIdAccountChannelDeliveryTermsPage(
					id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountIdAccountChannelDeliveryTermsPage(
					id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountIdAccountChannelDeliveryTermsPage_addAccountChannelEntry(
				Long id, AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountChannelDeliveryTermsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetAccountIdAccountChannelDeliveryTermsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountIdAccountChannelDeliveryTerm() throws Exception {
		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountIdAccountChannelDeliveryTerm_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountIdAccountChannelDeliveryTerm_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountIdAccountChannelDiscountsPage() throws Exception {
		Long id = testGetAccountIdAccountChannelDiscountsPage_getId();
		Long irrelevantId =
			testGetAccountIdAccountChannelDiscountsPage_getIrrelevantId();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.getAccountIdAccountChannelDiscountsPage(
				id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountIdAccountChannelDiscountsPage_addAccountChannelEntry(
					irrelevantId, randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountIdAccountChannelDiscountsPage(
						irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountIdAccountChannelDiscountsPage_getExpectedActions(
					irrelevantId));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelDiscountsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelDiscountsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.getAccountIdAccountChannelDiscountsPage(
				id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountIdAccountChannelDiscountsPage_getExpectedActions(id));
	}

	protected Map<String, Map>
			testGetAccountIdAccountChannelDiscountsPage_getExpectedActions(
				Long id)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountIdAccountChannelDiscountsPageWithPagination()
		throws Exception {

		Long id = testGetAccountIdAccountChannelDiscountsPage_getId();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelDiscountsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelDiscountsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountIdAccountChannelDiscountsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.getAccountIdAccountChannelDiscountsPage(
				id, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.getAccountIdAccountChannelDiscountsPage(
				id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.getAccountIdAccountChannelDiscountsPage(
				id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountIdAccountChannelDiscountsPage_addAccountChannelEntry(
				Long id, AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountChannelDiscountsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountChannelDiscountsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountIdAccountChannelDiscount() throws Exception {
		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountIdAccountChannelDiscount_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountIdAccountChannelDiscount_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountIdAccountChannelPaymentMethodsPage()
		throws Exception {

		Long id = testGetAccountIdAccountChannelPaymentMethodsPage_getId();
		Long irrelevantId =
			testGetAccountIdAccountChannelPaymentMethodsPage_getIrrelevantId();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountIdAccountChannelPaymentMethodsPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountIdAccountChannelPaymentMethodsPage_addAccountChannelEntry(
					irrelevantId, randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountIdAccountChannelPaymentMethodsPage(
						irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountIdAccountChannelPaymentMethodsPage_getExpectedActions(
					irrelevantId));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelPaymentMethodsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelPaymentMethodsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountIdAccountChannelPaymentMethodsPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountIdAccountChannelPaymentMethodsPage_getExpectedActions(
				id));
	}

	protected Map<String, Map>
			testGetAccountIdAccountChannelPaymentMethodsPage_getExpectedActions(
				Long id)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountIdAccountChannelPaymentMethodsPageWithPagination()
		throws Exception {

		Long id = testGetAccountIdAccountChannelPaymentMethodsPage_getId();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelPaymentMethodsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelPaymentMethodsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountIdAccountChannelPaymentMethodsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountIdAccountChannelPaymentMethodsPage(
					id, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountIdAccountChannelPaymentMethodsPage(
					id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountIdAccountChannelPaymentMethodsPage(
					id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountIdAccountChannelPaymentMethodsPage_addAccountChannelEntry(
				Long id, AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountChannelPaymentMethodsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetAccountIdAccountChannelPaymentMethodsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountIdAccountChannelPaymentMethod()
		throws Exception {

		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountIdAccountChannelPaymentMethod_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountIdAccountChannelPaymentMethod_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountIdAccountChannelPaymentTermsPage()
		throws Exception {

		Long id = testGetAccountIdAccountChannelPaymentTermsPage_getId();
		Long irrelevantId =
			testGetAccountIdAccountChannelPaymentTermsPage_getIrrelevantId();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountIdAccountChannelPaymentTermsPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountIdAccountChannelPaymentTermsPage_addAccountChannelEntry(
					irrelevantId, randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountIdAccountChannelPaymentTermsPage(
						irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountIdAccountChannelPaymentTermsPage_getExpectedActions(
					irrelevantId));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelPaymentTermsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelPaymentTermsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountIdAccountChannelPaymentTermsPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountIdAccountChannelPaymentTermsPage_getExpectedActions(
				id));
	}

	protected Map<String, Map>
			testGetAccountIdAccountChannelPaymentTermsPage_getExpectedActions(
				Long id)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountIdAccountChannelPaymentTermsPageWithPagination()
		throws Exception {

		Long id = testGetAccountIdAccountChannelPaymentTermsPage_getId();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelPaymentTermsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelPaymentTermsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountIdAccountChannelPaymentTermsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountIdAccountChannelPaymentTermsPage(
					id, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountIdAccountChannelPaymentTermsPage(
					id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountIdAccountChannelPaymentTermsPage(
					id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountIdAccountChannelPaymentTermsPage_addAccountChannelEntry(
				Long id, AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountChannelPaymentTermsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetAccountIdAccountChannelPaymentTermsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountIdAccountChannelPaymentTerm() throws Exception {
		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountIdAccountChannelPaymentTerm_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountIdAccountChannelPaymentTerm_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountIdAccountChannelPriceListsPage()
		throws Exception {

		Long id = testGetAccountIdAccountChannelPriceListsPage_getId();
		Long irrelevantId =
			testGetAccountIdAccountChannelPriceListsPage_getIrrelevantId();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountIdAccountChannelPriceListsPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountIdAccountChannelPriceListsPage_addAccountChannelEntry(
					irrelevantId, randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountIdAccountChannelPriceListsPage(
						irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountIdAccountChannelPriceListsPage_getExpectedActions(
					irrelevantId));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelPriceListsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelPriceListsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountIdAccountChannelPriceListsPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountIdAccountChannelPriceListsPage_getExpectedActions(
				id));
	}

	protected Map<String, Map>
			testGetAccountIdAccountChannelPriceListsPage_getExpectedActions(
				Long id)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountIdAccountChannelPriceListsPageWithPagination()
		throws Exception {

		Long id = testGetAccountIdAccountChannelPriceListsPage_getId();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelPriceListsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelPriceListsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountIdAccountChannelPriceListsPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountIdAccountChannelPriceListsPage(
					id, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountIdAccountChannelPriceListsPage(
					id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountIdAccountChannelPriceListsPage(
					id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountIdAccountChannelPriceListsPage_addAccountChannelEntry(
				Long id, AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountChannelPriceListsPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetAccountIdAccountChannelPriceListsPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountIdAccountChannelPriceList() throws Exception {
		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountIdAccountChannelPriceList_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountIdAccountChannelPriceList_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountIdAccountChannelShippingAddressesPage()
		throws Exception {

		Long id = testGetAccountIdAccountChannelShippingAddressesPage_getId();
		Long irrelevantId =
			testGetAccountIdAccountChannelShippingAddressesPage_getIrrelevantId();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.
				getAccountIdAccountChannelShippingAddressesPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountIdAccountChannelShippingAddressesPage_addAccountChannelEntry(
					irrelevantId, randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.
					getAccountIdAccountChannelShippingAddressesPage(
						irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountIdAccountChannelShippingAddressesPage_getExpectedActions(
					irrelevantId));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelShippingAddressesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelShippingAddressesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		page =
			accountChannelEntryResource.
				getAccountIdAccountChannelShippingAddressesPage(
					id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountIdAccountChannelShippingAddressesPage_getExpectedActions(
				id));
	}

	protected Map<String, Map>
			testGetAccountIdAccountChannelShippingAddressesPage_getExpectedActions(
				Long id)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountIdAccountChannelShippingAddressesPageWithPagination()
		throws Exception {

		Long id = testGetAccountIdAccountChannelShippingAddressesPage_getId();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelShippingAddressesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelShippingAddressesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountIdAccountChannelShippingAddressesPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.
				getAccountIdAccountChannelShippingAddressesPage(
					id, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.
				getAccountIdAccountChannelShippingAddressesPage(
					id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.
				getAccountIdAccountChannelShippingAddressesPage(
					id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountIdAccountChannelShippingAddressesPage_addAccountChannelEntry(
				Long id, AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountChannelShippingAddressesPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetAccountIdAccountChannelShippingAddressesPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountIdAccountChannelShippingAddress()
		throws Exception {

		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountIdAccountChannelShippingAddress_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountIdAccountChannelShippingAddress_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	@Test
	public void testGetAccountIdAccountChannelUsersPage() throws Exception {
		Long id = testGetAccountIdAccountChannelUsersPage_getId();
		Long irrelevantId =
			testGetAccountIdAccountChannelUsersPage_getIrrelevantId();

		Page<AccountChannelEntry> page =
			accountChannelEntryResource.getAccountIdAccountChannelUsersPage(
				id, Pagination.of(1, 10));

		Assert.assertEquals(0, page.getTotalCount());

		if (irrelevantId != null) {
			AccountChannelEntry irrelevantAccountChannelEntry =
				testGetAccountIdAccountChannelUsersPage_addAccountChannelEntry(
					irrelevantId, randomIrrelevantAccountChannelEntry());

			page =
				accountChannelEntryResource.getAccountIdAccountChannelUsersPage(
					irrelevantId, Pagination.of(1, 2));

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantAccountChannelEntry),
				(List<AccountChannelEntry>)page.getItems());
			assertValid(
				page,
				testGetAccountIdAccountChannelUsersPage_getExpectedActions(
					irrelevantId));
		}

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelUsersPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelUsersPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		page = accountChannelEntryResource.getAccountIdAccountChannelUsersPage(
			id, Pagination.of(1, 10));

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(accountChannelEntry1, accountChannelEntry2),
			(List<AccountChannelEntry>)page.getItems());
		assertValid(
			page,
			testGetAccountIdAccountChannelUsersPage_getExpectedActions(id));
	}

	protected Map<String, Map>
			testGetAccountIdAccountChannelUsersPage_getExpectedActions(Long id)
		throws Exception {

		Map<String, Map> expectedActions = new HashMap<>();

		return expectedActions;
	}

	@Test
	public void testGetAccountIdAccountChannelUsersPageWithPagination()
		throws Exception {

		Long id = testGetAccountIdAccountChannelUsersPage_getId();

		AccountChannelEntry accountChannelEntry1 =
			testGetAccountIdAccountChannelUsersPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry2 =
			testGetAccountIdAccountChannelUsersPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		AccountChannelEntry accountChannelEntry3 =
			testGetAccountIdAccountChannelUsersPage_addAccountChannelEntry(
				id, randomAccountChannelEntry());

		Page<AccountChannelEntry> page1 =
			accountChannelEntryResource.getAccountIdAccountChannelUsersPage(
				id, Pagination.of(1, 2));

		List<AccountChannelEntry> accountChannelEntries1 =
			(List<AccountChannelEntry>)page1.getItems();

		Assert.assertEquals(
			accountChannelEntries1.toString(), 2,
			accountChannelEntries1.size());

		Page<AccountChannelEntry> page2 =
			accountChannelEntryResource.getAccountIdAccountChannelUsersPage(
				id, Pagination.of(2, 2));

		Assert.assertEquals(3, page2.getTotalCount());

		List<AccountChannelEntry> accountChannelEntries2 =
			(List<AccountChannelEntry>)page2.getItems();

		Assert.assertEquals(
			accountChannelEntries2.toString(), 1,
			accountChannelEntries2.size());

		Page<AccountChannelEntry> page3 =
			accountChannelEntryResource.getAccountIdAccountChannelUsersPage(
				id, Pagination.of(1, 3));

		assertEqualsIgnoringOrder(
			Arrays.asList(
				accountChannelEntry1, accountChannelEntry2,
				accountChannelEntry3),
			(List<AccountChannelEntry>)page3.getItems());
	}

	protected AccountChannelEntry
			testGetAccountIdAccountChannelUsersPage_addAccountChannelEntry(
				Long id, AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountChannelUsersPage_getId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetAccountIdAccountChannelUsersPage_getIrrelevantId()
		throws Exception {

		return null;
	}

	@Test
	public void testPostAccountIdAccountChannelUser() throws Exception {
		AccountChannelEntry randomAccountChannelEntry =
			randomAccountChannelEntry();

		AccountChannelEntry postAccountChannelEntry =
			testPostAccountIdAccountChannelUser_addAccountChannelEntry(
				randomAccountChannelEntry);

		assertEquals(randomAccountChannelEntry, postAccountChannelEntry);
		assertValid(postAccountChannelEntry);
	}

	protected AccountChannelEntry
			testPostAccountIdAccountChannelUser_addAccountChannelEntry(
				AccountChannelEntry accountChannelEntry)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected AccountChannelEntry
			testGraphQLAccountChannelEntry_addAccountChannelEntry()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertContains(
		AccountChannelEntry accountChannelEntry,
		List<AccountChannelEntry> accountChannelEntries) {

		boolean contains = false;

		for (AccountChannelEntry item : accountChannelEntries) {
			if (equals(accountChannelEntry, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			accountChannelEntries + " does not contain " + accountChannelEntry,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		AccountChannelEntry accountChannelEntry1,
		AccountChannelEntry accountChannelEntry2) {

		Assert.assertTrue(
			accountChannelEntry1 + " does not equal " + accountChannelEntry2,
			equals(accountChannelEntry1, accountChannelEntry2));
	}

	protected void assertEquals(
		List<AccountChannelEntry> accountChannelEntries1,
		List<AccountChannelEntry> accountChannelEntries2) {

		Assert.assertEquals(
			accountChannelEntries1.size(), accountChannelEntries2.size());

		for (int i = 0; i < accountChannelEntries1.size(); i++) {
			AccountChannelEntry accountChannelEntry1 =
				accountChannelEntries1.get(i);
			AccountChannelEntry accountChannelEntry2 =
				accountChannelEntries2.get(i);

			assertEquals(accountChannelEntry1, accountChannelEntry2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<AccountChannelEntry> accountChannelEntries1,
		List<AccountChannelEntry> accountChannelEntries2) {

		Assert.assertEquals(
			accountChannelEntries1.size(), accountChannelEntries2.size());

		for (AccountChannelEntry accountChannelEntry1 :
				accountChannelEntries1) {

			boolean contains = false;

			for (AccountChannelEntry accountChannelEntry2 :
					accountChannelEntries2) {

				if (equals(accountChannelEntry1, accountChannelEntry2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				accountChannelEntries2 + " does not contain " +
					accountChannelEntry1,
				contains);
		}
	}

	protected void assertValid(AccountChannelEntry accountChannelEntry)
		throws Exception {

		boolean valid = true;

		if (accountChannelEntry.getId() == null) {
			valid = false;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"accountExternalReferenceCode",
					additionalAssertFieldName)) {

				if (accountChannelEntry.getAccountExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("accountId", additionalAssertFieldName)) {
				if (accountChannelEntry.getAccountId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (accountChannelEntry.getActions() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"channelExternalReferenceCode",
					additionalAssertFieldName)) {

				if (accountChannelEntry.getChannelExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("channelId", additionalAssertFieldName)) {
				if (accountChannelEntry.getChannelId() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"classExternalReferenceCode", additionalAssertFieldName)) {

				if (accountChannelEntry.getClassExternalReferenceCode() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals("classPK", additionalAssertFieldName)) {
				if (accountChannelEntry.getClassPK() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"overrideEligibility", additionalAssertFieldName)) {

				if (accountChannelEntry.getOverrideEligibility() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (accountChannelEntry.getPriority() == null) {
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

	protected void assertValid(Page<AccountChannelEntry> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<AccountChannelEntry> page, Map<String, Map> expectedActions) {

		boolean valid = false;

		java.util.Collection<AccountChannelEntry> accountChannelEntries =
			page.getItems();

		int size = accountChannelEntries.size();

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
					com.liferay.headless.commerce.admin.account.dto.v1_0.
						AccountChannelEntry.class)) {

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
		AccountChannelEntry accountChannelEntry1,
		AccountChannelEntry accountChannelEntry2) {

		if (accountChannelEntry1 == accountChannelEntry2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals(
					"accountExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						accountChannelEntry1.getAccountExternalReferenceCode(),
						accountChannelEntry2.
							getAccountExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("accountId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						accountChannelEntry1.getAccountId(),
						accountChannelEntry2.getAccountId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("actions", additionalAssertFieldName)) {
				if (!equals(
						(Map)accountChannelEntry1.getActions(),
						(Map)accountChannelEntry2.getActions())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"channelExternalReferenceCode",
					additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						accountChannelEntry1.getChannelExternalReferenceCode(),
						accountChannelEntry2.
							getChannelExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("channelId", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						accountChannelEntry1.getChannelId(),
						accountChannelEntry2.getChannelId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"classExternalReferenceCode", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						accountChannelEntry1.getClassExternalReferenceCode(),
						accountChannelEntry2.getClassExternalReferenceCode())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("classPK", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						accountChannelEntry1.getClassPK(),
						accountChannelEntry2.getClassPK())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("id", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						accountChannelEntry1.getId(),
						accountChannelEntry2.getId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"overrideEligibility", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						accountChannelEntry1.getOverrideEligibility(),
						accountChannelEntry2.getOverrideEligibility())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("priority", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						accountChannelEntry1.getPriority(),
						accountChannelEntry2.getPriority())) {

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

		if (!(_accountChannelEntryResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_accountChannelEntryResource;

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
		AccountChannelEntry accountChannelEntry) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("accountExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					accountChannelEntry.getAccountExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("accountId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("actions")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("channelExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					accountChannelEntry.getChannelExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("channelId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("classExternalReferenceCode")) {
			sb.append("'");
			sb.append(
				String.valueOf(
					accountChannelEntry.getClassExternalReferenceCode()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("classPK")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("overrideEligibility")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("priority")) {
			sb.append(String.valueOf(accountChannelEntry.getPriority()));

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

	protected AccountChannelEntry randomAccountChannelEntry() throws Exception {
		return new AccountChannelEntry() {
			{
				accountExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				accountId = RandomTestUtil.randomLong();
				channelExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				channelId = RandomTestUtil.randomLong();
				classExternalReferenceCode = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				classPK = RandomTestUtil.randomLong();
				id = RandomTestUtil.randomLong();
				overrideEligibility = RandomTestUtil.randomBoolean();
				priority = RandomTestUtil.randomDouble();
			}
		};
	}

	protected AccountChannelEntry randomIrrelevantAccountChannelEntry()
		throws Exception {

		AccountChannelEntry randomIrrelevantAccountChannelEntry =
			randomAccountChannelEntry();

		return randomIrrelevantAccountChannelEntry;
	}

	protected AccountChannelEntry randomPatchAccountChannelEntry()
		throws Exception {

		return randomAccountChannelEntry();
	}

	protected AccountChannelEntryResource accountChannelEntryResource;
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
		LogFactoryUtil.getLog(BaseAccountChannelEntryResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.account.resource.v1_0.
		AccountChannelEntryResource _accountChannelEntryResource;

}