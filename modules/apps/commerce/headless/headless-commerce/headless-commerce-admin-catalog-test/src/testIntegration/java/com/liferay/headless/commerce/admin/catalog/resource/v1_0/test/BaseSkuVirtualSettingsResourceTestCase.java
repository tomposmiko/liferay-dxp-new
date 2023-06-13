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

import com.liferay.headless.commerce.admin.catalog.client.dto.v1_0.SkuVirtualSettings;
import com.liferay.headless.commerce.admin.catalog.client.http.HttpInvoker;
import com.liferay.headless.commerce.admin.catalog.client.pagination.Page;
import com.liferay.headless.commerce.admin.catalog.client.resource.v1_0.SkuVirtualSettingsResource;
import com.liferay.headless.commerce.admin.catalog.client.serdes.v1_0.SkuVirtualSettingsSerDes;
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
public abstract class BaseSkuVirtualSettingsResourceTestCase {

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

		_skuVirtualSettingsResource.setContextCompany(testCompany);

		SkuVirtualSettingsResource.Builder builder =
			SkuVirtualSettingsResource.builder();

		skuVirtualSettingsResource = builder.authentication(
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

		SkuVirtualSettings skuVirtualSettings1 = randomSkuVirtualSettings();

		String json = objectMapper.writeValueAsString(skuVirtualSettings1);

		SkuVirtualSettings skuVirtualSettings2 = SkuVirtualSettingsSerDes.toDTO(
			json);

		Assert.assertTrue(equals(skuVirtualSettings1, skuVirtualSettings2));
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

		SkuVirtualSettings skuVirtualSettings = randomSkuVirtualSettings();

		String json1 = objectMapper.writeValueAsString(skuVirtualSettings);
		String json2 = SkuVirtualSettingsSerDes.toJSON(skuVirtualSettings);

		Assert.assertEquals(
			objectMapper.readTree(json1), objectMapper.readTree(json2));
	}

	@Test
	public void testEscapeRegexInStringFields() throws Exception {
		String regex = "^[0-9]+(\\.[0-9]{1,2})\"?";

		SkuVirtualSettings skuVirtualSettings = randomSkuVirtualSettings();

		skuVirtualSettings.setAttachment(regex);
		skuVirtualSettings.setSampleAttachment(regex);
		skuVirtualSettings.setSampleSrc(regex);
		skuVirtualSettings.setSampleUrl(regex);
		skuVirtualSettings.setSrc(regex);
		skuVirtualSettings.setUrl(regex);

		String json = SkuVirtualSettingsSerDes.toJSON(skuVirtualSettings);

		Assert.assertFalse(json.contains(regex));

		skuVirtualSettings = SkuVirtualSettingsSerDes.toDTO(json);

		Assert.assertEquals(regex, skuVirtualSettings.getAttachment());
		Assert.assertEquals(regex, skuVirtualSettings.getSampleAttachment());
		Assert.assertEquals(regex, skuVirtualSettings.getSampleSrc());
		Assert.assertEquals(regex, skuVirtualSettings.getSampleUrl());
		Assert.assertEquals(regex, skuVirtualSettings.getSrc());
		Assert.assertEquals(regex, skuVirtualSettings.getUrl());
	}

	@Test
	public void testGetSkuByExternalReferenceCodeSkuVirtualSettings()
		throws Exception {

		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetSkuByExternalReferenceCodeSkuVirtualSettings()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testGraphQLGetSkuByExternalReferenceCodeSkuVirtualSettingsNotFound()
		throws Exception {

		Assert.assertTrue(true);
	}

	@Test
	public void testGetSkuIdSkuVirtualSettings() throws Exception {
		Assert.assertTrue(false);
	}

	@Test
	public void testGraphQLGetSkuIdSkuVirtualSettings() throws Exception {
		Assert.assertTrue(true);
	}

	@Test
	public void testGraphQLGetSkuIdSkuVirtualSettingsNotFound()
		throws Exception {

		Assert.assertTrue(true);
	}

	protected void assertContains(
		SkuVirtualSettings skuVirtualSettings,
		List<SkuVirtualSettings> skuVirtualSettingses) {

		boolean contains = false;

		for (SkuVirtualSettings item : skuVirtualSettingses) {
			if (equals(skuVirtualSettings, item)) {
				contains = true;

				break;
			}
		}

		Assert.assertTrue(
			skuVirtualSettingses + " does not contain " + skuVirtualSettings,
			contains);
	}

	protected void assertHttpResponseStatusCode(
		int expectedHttpResponseStatusCode,
		HttpInvoker.HttpResponse actualHttpResponse) {

		Assert.assertEquals(
			expectedHttpResponseStatusCode, actualHttpResponse.getStatusCode());
	}

	protected void assertEquals(
		SkuVirtualSettings skuVirtualSettings1,
		SkuVirtualSettings skuVirtualSettings2) {

		Assert.assertTrue(
			skuVirtualSettings1 + " does not equal " + skuVirtualSettings2,
			equals(skuVirtualSettings1, skuVirtualSettings2));
	}

	protected void assertEquals(
		List<SkuVirtualSettings> skuVirtualSettingses1,
		List<SkuVirtualSettings> skuVirtualSettingses2) {

		Assert.assertEquals(
			skuVirtualSettingses1.size(), skuVirtualSettingses2.size());

		for (int i = 0; i < skuVirtualSettingses1.size(); i++) {
			SkuVirtualSettings skuVirtualSettings1 = skuVirtualSettingses1.get(
				i);
			SkuVirtualSettings skuVirtualSettings2 = skuVirtualSettingses2.get(
				i);

			assertEquals(skuVirtualSettings1, skuVirtualSettings2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<SkuVirtualSettings> skuVirtualSettingses1,
		List<SkuVirtualSettings> skuVirtualSettingses2) {

		Assert.assertEquals(
			skuVirtualSettingses1.size(), skuVirtualSettingses2.size());

		for (SkuVirtualSettings skuVirtualSettings1 : skuVirtualSettingses1) {
			boolean contains = false;

			for (SkuVirtualSettings skuVirtualSettings2 :
					skuVirtualSettingses2) {

				if (equals(skuVirtualSettings1, skuVirtualSettings2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				skuVirtualSettingses2 + " does not contain " +
					skuVirtualSettings1,
				contains);
		}
	}

	protected void assertValid(SkuVirtualSettings skuVirtualSettings)
		throws Exception {

		boolean valid = true;

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("activationStatus", additionalAssertFieldName)) {
				if (skuVirtualSettings.getActivationStatus() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"activationStatusInfo", additionalAssertFieldName)) {

				if (skuVirtualSettings.getActivationStatusInfo() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("attachment", additionalAssertFieldName)) {
				if (skuVirtualSettings.getAttachment() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("duration", additionalAssertFieldName)) {
				if (skuVirtualSettings.getDuration() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("maxUsages", additionalAssertFieldName)) {
				if (skuVirtualSettings.getMaxUsages() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("override", additionalAssertFieldName)) {
				if (skuVirtualSettings.getOverride() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("sampleAttachment", additionalAssertFieldName)) {
				if (skuVirtualSettings.getSampleAttachment() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("sampleSrc", additionalAssertFieldName)) {
				if (skuVirtualSettings.getSampleSrc() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("sampleUrl", additionalAssertFieldName)) {
				if (skuVirtualSettings.getSampleUrl() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("src", additionalAssertFieldName)) {
				if (skuVirtualSettings.getSrc() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"termsOfUseContent", additionalAssertFieldName)) {

				if (skuVirtualSettings.getTermsOfUseContent() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"termsOfUseJournalArticleId", additionalAssertFieldName)) {

				if (skuVirtualSettings.getTermsOfUseJournalArticleId() ==
						null) {

					valid = false;
				}

				continue;
			}

			if (Objects.equals(
					"termsOfUseRequired", additionalAssertFieldName)) {

				if (skuVirtualSettings.getTermsOfUseRequired() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("url", additionalAssertFieldName)) {
				if (skuVirtualSettings.getUrl() == null) {
					valid = false;
				}

				continue;
			}

			if (Objects.equals("useSample", additionalAssertFieldName)) {
				if (skuVirtualSettings.getUseSample() == null) {
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

	protected void assertValid(Page<SkuVirtualSettings> page) {
		assertValid(page, Collections.emptyMap());
	}

	protected void assertValid(
		Page<SkuVirtualSettings> page,
		Map<String, Map<String, String>> expectedActions) {

		boolean valid = false;

		java.util.Collection<SkuVirtualSettings> skuVirtualSettingses =
			page.getItems();

		int size = skuVirtualSettingses.size();

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
						SkuVirtualSettings.class)) {

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
		SkuVirtualSettings skuVirtualSettings1,
		SkuVirtualSettings skuVirtualSettings2) {

		if (skuVirtualSettings1 == skuVirtualSettings2) {
			return true;
		}

		for (String additionalAssertFieldName :
				getAdditionalAssertFieldNames()) {

			if (Objects.equals("activationStatus", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuVirtualSettings1.getActivationStatus(),
						skuVirtualSettings2.getActivationStatus())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"activationStatusInfo", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						skuVirtualSettings1.getActivationStatusInfo(),
						skuVirtualSettings2.getActivationStatusInfo())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("attachment", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuVirtualSettings1.getAttachment(),
						skuVirtualSettings2.getAttachment())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("duration", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuVirtualSettings1.getDuration(),
						skuVirtualSettings2.getDuration())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("maxUsages", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuVirtualSettings1.getMaxUsages(),
						skuVirtualSettings2.getMaxUsages())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("override", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuVirtualSettings1.getOverride(),
						skuVirtualSettings2.getOverride())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("sampleAttachment", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuVirtualSettings1.getSampleAttachment(),
						skuVirtualSettings2.getSampleAttachment())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("sampleSrc", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuVirtualSettings1.getSampleSrc(),
						skuVirtualSettings2.getSampleSrc())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("sampleUrl", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuVirtualSettings1.getSampleUrl(),
						skuVirtualSettings2.getSampleUrl())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("src", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuVirtualSettings1.getSrc(),
						skuVirtualSettings2.getSrc())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"termsOfUseContent", additionalAssertFieldName)) {

				if (!equals(
						(Map)skuVirtualSettings1.getTermsOfUseContent(),
						(Map)skuVirtualSettings2.getTermsOfUseContent())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"termsOfUseJournalArticleId", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						skuVirtualSettings1.getTermsOfUseJournalArticleId(),
						skuVirtualSettings2.getTermsOfUseJournalArticleId())) {

					return false;
				}

				continue;
			}

			if (Objects.equals(
					"termsOfUseRequired", additionalAssertFieldName)) {

				if (!Objects.deepEquals(
						skuVirtualSettings1.getTermsOfUseRequired(),
						skuVirtualSettings2.getTermsOfUseRequired())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("url", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuVirtualSettings1.getUrl(),
						skuVirtualSettings2.getUrl())) {

					return false;
				}

				continue;
			}

			if (Objects.equals("useSample", additionalAssertFieldName)) {
				if (!Objects.deepEquals(
						skuVirtualSettings1.getUseSample(),
						skuVirtualSettings2.getUseSample())) {

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

		if (!(_skuVirtualSettingsResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_skuVirtualSettingsResource;

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
		SkuVirtualSettings skuVirtualSettings) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("activationStatus")) {
			sb.append(String.valueOf(skuVirtualSettings.getActivationStatus()));

			return sb.toString();
		}

		if (entityFieldName.equals("activationStatusInfo")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("attachment")) {
			sb.append("'");
			sb.append(String.valueOf(skuVirtualSettings.getAttachment()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("duration")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("maxUsages")) {
			sb.append(String.valueOf(skuVirtualSettings.getMaxUsages()));

			return sb.toString();
		}

		if (entityFieldName.equals("override")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("sampleAttachment")) {
			sb.append("'");
			sb.append(String.valueOf(skuVirtualSettings.getSampleAttachment()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("sampleSrc")) {
			sb.append("'");
			sb.append(String.valueOf(skuVirtualSettings.getSampleSrc()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("sampleUrl")) {
			sb.append("'");
			sb.append(String.valueOf(skuVirtualSettings.getSampleUrl()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("src")) {
			sb.append("'");
			sb.append(String.valueOf(skuVirtualSettings.getSrc()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("termsOfUseContent")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("termsOfUseJournalArticleId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("termsOfUseRequired")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("url")) {
			sb.append("'");
			sb.append(String.valueOf(skuVirtualSettings.getUrl()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("useSample")) {
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

	protected SkuVirtualSettings randomSkuVirtualSettings() throws Exception {
		return new SkuVirtualSettings() {
			{
				activationStatus = RandomTestUtil.randomInt();
				attachment = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				duration = RandomTestUtil.randomLong();
				maxUsages = RandomTestUtil.randomInt();
				override = RandomTestUtil.randomBoolean();
				sampleAttachment = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				sampleSrc = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				sampleUrl = StringUtil.toLowerCase(
					RandomTestUtil.randomString());
				src = StringUtil.toLowerCase(RandomTestUtil.randomString());
				termsOfUseJournalArticleId = RandomTestUtil.randomLong();
				termsOfUseRequired = RandomTestUtil.randomBoolean();
				url = StringUtil.toLowerCase(RandomTestUtil.randomString());
				useSample = RandomTestUtil.randomBoolean();
			}
		};
	}

	protected SkuVirtualSettings randomIrrelevantSkuVirtualSettings()
		throws Exception {

		SkuVirtualSettings randomIrrelevantSkuVirtualSettings =
			randomSkuVirtualSettings();

		return randomIrrelevantSkuVirtualSettings;
	}

	protected SkuVirtualSettings randomPatchSkuVirtualSettings()
		throws Exception {

		return randomSkuVirtualSettings();
	}

	protected SkuVirtualSettingsResource skuVirtualSettingsResource;
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
		LogFactoryUtil.getLog(BaseSkuVirtualSettingsResourceTestCase.class);

	private static DateFormat _dateFormat;

	@Inject
	private com.liferay.headless.commerce.admin.catalog.resource.v1_0.
		SkuVirtualSettingsResource _skuVirtualSettingsResource;

}