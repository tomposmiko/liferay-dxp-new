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

package com.liferay.headless.foundation.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import com.liferay.headless.foundation.dto.v1_0.TaxonomyCategory;
import com.liferay.headless.foundation.resource.v1_0.TaxonomyCategoryResource;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.portal.vulcan.resource.EntityModelResource;

import java.lang.reflect.InvocationTargetException;

import java.net.URL;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.time.DateUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseTaxonomyCategoryResourceTestCase {

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

		_resourceURL = new URL(
			"http://localhost:8080/o/headless-foundation/v1.0");
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testDeleteTaxonomyCategory() throws Exception {
		TaxonomyCategory taxonomyCategory =
			testDeleteTaxonomyCategory_addTaxonomyCategory();

		assertResponseCode(
			204,
			invokeDeleteTaxonomyCategoryResponse(taxonomyCategory.getId()));

		assertResponseCode(
			404, invokeGetTaxonomyCategoryResponse(taxonomyCategory.getId()));
	}

	protected TaxonomyCategory testDeleteTaxonomyCategory_addTaxonomyCategory()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void invokeDeleteTaxonomyCategory(Long taxonomyCategoryId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setDelete(true);

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-categories/{taxonomy-category-id}",
					taxonomyCategoryId);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}
	}

	protected Http.Response invokeDeleteTaxonomyCategoryResponse(
			Long taxonomyCategoryId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setDelete(true);

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-categories/{taxonomy-category-id}",
					taxonomyCategoryId);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetTaxonomyCategory() throws Exception {
		TaxonomyCategory postTaxonomyCategory =
			testGetTaxonomyCategory_addTaxonomyCategory();

		TaxonomyCategory getTaxonomyCategory = invokeGetTaxonomyCategory(
			postTaxonomyCategory.getId());

		assertEquals(postTaxonomyCategory, getTaxonomyCategory);
		assertValid(getTaxonomyCategory);
	}

	protected TaxonomyCategory testGetTaxonomyCategory_addTaxonomyCategory()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected TaxonomyCategory invokeGetTaxonomyCategory(
			Long taxonomyCategoryId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-categories/{taxonomy-category-id}",
					taxonomyCategoryId);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(
				string, TaxonomyCategory.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokeGetTaxonomyCategoryResponse(
			Long taxonomyCategoryId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-categories/{taxonomy-category-id}",
					taxonomyCategoryId);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testPatchTaxonomyCategory() throws Exception {
		TaxonomyCategory postTaxonomyCategory =
			testPatchTaxonomyCategory_addTaxonomyCategory();

		TaxonomyCategory randomPatchTaxonomyCategory =
			randomPatchTaxonomyCategory();

		TaxonomyCategory patchTaxonomyCategory = invokePatchTaxonomyCategory(
			postTaxonomyCategory.getId(), randomPatchTaxonomyCategory);

		TaxonomyCategory expectedPatchTaxonomyCategory =
			(TaxonomyCategory)BeanUtils.cloneBean(postTaxonomyCategory);

		_beanUtilsBean.copyProperties(
			expectedPatchTaxonomyCategory, randomPatchTaxonomyCategory);

		TaxonomyCategory getTaxonomyCategory = invokeGetTaxonomyCategory(
			patchTaxonomyCategory.getId());

		assertEquals(expectedPatchTaxonomyCategory, getTaxonomyCategory);
		assertValid(getTaxonomyCategory);
	}

	protected TaxonomyCategory testPatchTaxonomyCategory_addTaxonomyCategory()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected TaxonomyCategory invokePatchTaxonomyCategory(
			Long taxonomyCategoryId, TaxonomyCategory taxonomyCategory)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(taxonomyCategory),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-categories/{taxonomy-category-id}",
					taxonomyCategoryId);

		options.setLocation(location);

		options.setPatch(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(
				string, TaxonomyCategory.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokePatchTaxonomyCategoryResponse(
			Long taxonomyCategoryId, TaxonomyCategory taxonomyCategory)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(taxonomyCategory),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-categories/{taxonomy-category-id}",
					taxonomyCategoryId);

		options.setLocation(location);

		options.setPatch(true);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testPutTaxonomyCategory() throws Exception {
		TaxonomyCategory postTaxonomyCategory =
			testPutTaxonomyCategory_addTaxonomyCategory();

		TaxonomyCategory randomTaxonomyCategory = randomTaxonomyCategory();

		TaxonomyCategory putTaxonomyCategory = invokePutTaxonomyCategory(
			postTaxonomyCategory.getId(), randomTaxonomyCategory);

		assertEquals(randomTaxonomyCategory, putTaxonomyCategory);
		assertValid(putTaxonomyCategory);

		TaxonomyCategory getTaxonomyCategory = invokeGetTaxonomyCategory(
			putTaxonomyCategory.getId());

		assertEquals(randomTaxonomyCategory, getTaxonomyCategory);
		assertValid(getTaxonomyCategory);
	}

	protected TaxonomyCategory testPutTaxonomyCategory_addTaxonomyCategory()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected TaxonomyCategory invokePutTaxonomyCategory(
			Long taxonomyCategoryId, TaxonomyCategory taxonomyCategory)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(taxonomyCategory),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-categories/{taxonomy-category-id}",
					taxonomyCategoryId);

		options.setLocation(location);

		options.setPut(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(
				string, TaxonomyCategory.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokePutTaxonomyCategoryResponse(
			Long taxonomyCategoryId, TaxonomyCategory taxonomyCategory)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(taxonomyCategory),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-categories/{taxonomy-category-id}",
					taxonomyCategoryId);

		options.setLocation(location);

		options.setPut(true);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetTaxonomyCategoryTaxonomyCategoriesPage()
		throws Exception {

		Long taxonomyCategoryId =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_getTaxonomyCategoryId();
		Long irrelevantTaxonomyCategoryId =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_getIrrelevantTaxonomyCategoryId();

		if ((irrelevantTaxonomyCategoryId != null)) {
			TaxonomyCategory irrelevantTaxonomyCategory =
				testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
					irrelevantTaxonomyCategoryId,
					randomIrrelevantTaxonomyCategory());

			Page<TaxonomyCategory> page =
				invokeGetTaxonomyCategoryTaxonomyCategoriesPage(
					irrelevantTaxonomyCategoryId, null, null,
					Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantTaxonomyCategory),
				(List<TaxonomyCategory>)page.getItems());
			assertValid(page);
		}

		TaxonomyCategory taxonomyCategory1 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, randomTaxonomyCategory());

		TaxonomyCategory taxonomyCategory2 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, randomTaxonomyCategory());

		Page<TaxonomyCategory> page =
			invokeGetTaxonomyCategoryTaxonomyCategoriesPage(
				taxonomyCategoryId, null, null, Pagination.of(1, 2), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(taxonomyCategory1, taxonomyCategory2),
			(List<TaxonomyCategory>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetTaxonomyCategoryTaxonomyCategoriesPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long taxonomyCategoryId =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_getTaxonomyCategoryId();

		TaxonomyCategory taxonomyCategory1 = randomTaxonomyCategory();
		TaxonomyCategory taxonomyCategory2 = randomTaxonomyCategory();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				taxonomyCategory1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		taxonomyCategory1 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, taxonomyCategory1);

		Thread.sleep(1000);

		taxonomyCategory2 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, taxonomyCategory2);

		for (EntityField entityField : entityFields) {
			Page<TaxonomyCategory> page =
				invokeGetTaxonomyCategoryTaxonomyCategoriesPage(
					taxonomyCategoryId, null,
					getFilterString(entityField, "eq", taxonomyCategory1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(taxonomyCategory1),
				(List<TaxonomyCategory>)page.getItems());
		}
	}

	@Test
	public void testGetTaxonomyCategoryTaxonomyCategoriesPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long taxonomyCategoryId =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_getTaxonomyCategoryId();

		TaxonomyCategory taxonomyCategory1 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, randomTaxonomyCategory());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		TaxonomyCategory taxonomyCategory2 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, randomTaxonomyCategory());

		for (EntityField entityField : entityFields) {
			Page<TaxonomyCategory> page =
				invokeGetTaxonomyCategoryTaxonomyCategoriesPage(
					taxonomyCategoryId, null,
					getFilterString(entityField, "eq", taxonomyCategory1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(taxonomyCategory1),
				(List<TaxonomyCategory>)page.getItems());
		}
	}

	@Test
	public void testGetTaxonomyCategoryTaxonomyCategoriesPageWithPagination()
		throws Exception {

		Long taxonomyCategoryId =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_getTaxonomyCategoryId();

		TaxonomyCategory taxonomyCategory1 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, randomTaxonomyCategory());

		TaxonomyCategory taxonomyCategory2 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, randomTaxonomyCategory());

		TaxonomyCategory taxonomyCategory3 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, randomTaxonomyCategory());

		Page<TaxonomyCategory> page1 =
			invokeGetTaxonomyCategoryTaxonomyCategoriesPage(
				taxonomyCategoryId, null, null, Pagination.of(1, 2), null);

		List<TaxonomyCategory> taxonomyCategories1 =
			(List<TaxonomyCategory>)page1.getItems();

		Assert.assertEquals(
			taxonomyCategories1.toString(), 2, taxonomyCategories1.size());

		Page<TaxonomyCategory> page2 =
			invokeGetTaxonomyCategoryTaxonomyCategoriesPage(
				taxonomyCategoryId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<TaxonomyCategory> taxonomyCategories2 =
			(List<TaxonomyCategory>)page2.getItems();

		Assert.assertEquals(
			taxonomyCategories2.toString(), 1, taxonomyCategories2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(
				taxonomyCategory1, taxonomyCategory2, taxonomyCategory3),
			new ArrayList<TaxonomyCategory>() {
				{
					addAll(taxonomyCategories1);
					addAll(taxonomyCategories2);
				}
			});
	}

	@Test
	public void testGetTaxonomyCategoryTaxonomyCategoriesPageWithSortDateTime()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long taxonomyCategoryId =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_getTaxonomyCategoryId();

		TaxonomyCategory taxonomyCategory1 = randomTaxonomyCategory();
		TaxonomyCategory taxonomyCategory2 = randomTaxonomyCategory();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				taxonomyCategory1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		taxonomyCategory1 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, taxonomyCategory1);

		Thread.sleep(1000);

		taxonomyCategory2 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, taxonomyCategory2);

		for (EntityField entityField : entityFields) {
			Page<TaxonomyCategory> ascPage =
				invokeGetTaxonomyCategoryTaxonomyCategoriesPage(
					taxonomyCategoryId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(taxonomyCategory1, taxonomyCategory2),
				(List<TaxonomyCategory>)ascPage.getItems());

			Page<TaxonomyCategory> descPage =
				invokeGetTaxonomyCategoryTaxonomyCategoriesPage(
					taxonomyCategoryId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(taxonomyCategory2, taxonomyCategory1),
				(List<TaxonomyCategory>)descPage.getItems());
		}
	}

	@Test
	public void testGetTaxonomyCategoryTaxonomyCategoriesPageWithSortString()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long taxonomyCategoryId =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_getTaxonomyCategoryId();

		TaxonomyCategory taxonomyCategory1 = randomTaxonomyCategory();
		TaxonomyCategory taxonomyCategory2 = randomTaxonomyCategory();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				taxonomyCategory1, entityField.getName(), "Aaa");
			BeanUtils.setProperty(
				taxonomyCategory2, entityField.getName(), "Bbb");
		}

		taxonomyCategory1 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, taxonomyCategory1);

		taxonomyCategory2 =
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyCategoryId, taxonomyCategory2);

		for (EntityField entityField : entityFields) {
			Page<TaxonomyCategory> ascPage =
				invokeGetTaxonomyCategoryTaxonomyCategoriesPage(
					taxonomyCategoryId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(taxonomyCategory1, taxonomyCategory2),
				(List<TaxonomyCategory>)ascPage.getItems());

			Page<TaxonomyCategory> descPage =
				invokeGetTaxonomyCategoryTaxonomyCategoriesPage(
					taxonomyCategoryId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(taxonomyCategory2, taxonomyCategory1),
				(List<TaxonomyCategory>)descPage.getItems());
		}
	}

	protected TaxonomyCategory
			testGetTaxonomyCategoryTaxonomyCategoriesPage_addTaxonomyCategory(
				Long taxonomyCategoryId, TaxonomyCategory taxonomyCategory)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetTaxonomyCategoryTaxonomyCategoriesPage_getTaxonomyCategoryId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetTaxonomyCategoryTaxonomyCategoriesPage_getIrrelevantTaxonomyCategoryId()
		throws Exception {

		return null;
	}

	protected Page<TaxonomyCategory>
			invokeGetTaxonomyCategoryTaxonomyCategoriesPage(
				Long taxonomyCategoryId, String search, String filterString,
				Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-categories/{taxonomy-category-id}/taxonomy-categories",
					taxonomyCategoryId);

		location = HttpUtil.addParameter(location, "filter", filterString);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		location = HttpUtil.addParameter(location, "sort", sortString);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		return _outputObjectMapper.readValue(
			string,
			new TypeReference<Page<TaxonomyCategory>>() {
			});
	}

	protected Http.Response
			invokeGetTaxonomyCategoryTaxonomyCategoriesPageResponse(
				Long taxonomyCategoryId, String search, String filterString,
				Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-categories/{taxonomy-category-id}/taxonomy-categories",
					taxonomyCategoryId);

		location = HttpUtil.addParameter(location, "filter", filterString);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		location = HttpUtil.addParameter(location, "sort", sortString);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testPostTaxonomyCategoryTaxonomyCategory() throws Exception {
		TaxonomyCategory randomTaxonomyCategory = randomTaxonomyCategory();

		TaxonomyCategory postTaxonomyCategory =
			testPostTaxonomyCategoryTaxonomyCategory_addTaxonomyCategory(
				randomTaxonomyCategory);

		assertEquals(randomTaxonomyCategory, postTaxonomyCategory);
		assertValid(postTaxonomyCategory);
	}

	protected TaxonomyCategory
			testPostTaxonomyCategoryTaxonomyCategory_addTaxonomyCategory(
				TaxonomyCategory taxonomyCategory)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected TaxonomyCategory invokePostTaxonomyCategoryTaxonomyCategory(
			Long taxonomyCategoryId, TaxonomyCategory taxonomyCategory)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(taxonomyCategory),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-categories/{taxonomy-category-id}/taxonomy-categories",
					taxonomyCategoryId);

		options.setLocation(location);

		options.setPost(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(
				string, TaxonomyCategory.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokePostTaxonomyCategoryTaxonomyCategoryResponse(
			Long taxonomyCategoryId, TaxonomyCategory taxonomyCategory)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(taxonomyCategory),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-categories/{taxonomy-category-id}/taxonomy-categories",
					taxonomyCategoryId);

		options.setLocation(location);

		options.setPost(true);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetTaxonomyVocabularyTaxonomyCategoriesPage()
		throws Exception {

		Long taxonomyVocabularyId =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_getTaxonomyVocabularyId();
		Long irrelevantTaxonomyVocabularyId =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_getIrrelevantTaxonomyVocabularyId();

		if ((irrelevantTaxonomyVocabularyId != null)) {
			TaxonomyCategory irrelevantTaxonomyCategory =
				testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
					irrelevantTaxonomyVocabularyId,
					randomIrrelevantTaxonomyCategory());

			Page<TaxonomyCategory> page =
				invokeGetTaxonomyVocabularyTaxonomyCategoriesPage(
					irrelevantTaxonomyVocabularyId, null, null,
					Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantTaxonomyCategory),
				(List<TaxonomyCategory>)page.getItems());
			assertValid(page);
		}

		TaxonomyCategory taxonomyCategory1 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, randomTaxonomyCategory());

		TaxonomyCategory taxonomyCategory2 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, randomTaxonomyCategory());

		Page<TaxonomyCategory> page =
			invokeGetTaxonomyVocabularyTaxonomyCategoriesPage(
				taxonomyVocabularyId, null, null, Pagination.of(1, 2), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(taxonomyCategory1, taxonomyCategory2),
			(List<TaxonomyCategory>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetTaxonomyVocabularyTaxonomyCategoriesPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long taxonomyVocabularyId =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_getTaxonomyVocabularyId();

		TaxonomyCategory taxonomyCategory1 = randomTaxonomyCategory();
		TaxonomyCategory taxonomyCategory2 = randomTaxonomyCategory();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				taxonomyCategory1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		taxonomyCategory1 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, taxonomyCategory1);

		Thread.sleep(1000);

		taxonomyCategory2 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, taxonomyCategory2);

		for (EntityField entityField : entityFields) {
			Page<TaxonomyCategory> page =
				invokeGetTaxonomyVocabularyTaxonomyCategoriesPage(
					taxonomyVocabularyId, null,
					getFilterString(entityField, "eq", taxonomyCategory1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(taxonomyCategory1),
				(List<TaxonomyCategory>)page.getItems());
		}
	}

	@Test
	public void testGetTaxonomyVocabularyTaxonomyCategoriesPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long taxonomyVocabularyId =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_getTaxonomyVocabularyId();

		TaxonomyCategory taxonomyCategory1 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, randomTaxonomyCategory());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		TaxonomyCategory taxonomyCategory2 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, randomTaxonomyCategory());

		for (EntityField entityField : entityFields) {
			Page<TaxonomyCategory> page =
				invokeGetTaxonomyVocabularyTaxonomyCategoriesPage(
					taxonomyVocabularyId, null,
					getFilterString(entityField, "eq", taxonomyCategory1),
					Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(taxonomyCategory1),
				(List<TaxonomyCategory>)page.getItems());
		}
	}

	@Test
	public void testGetTaxonomyVocabularyTaxonomyCategoriesPageWithPagination()
		throws Exception {

		Long taxonomyVocabularyId =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_getTaxonomyVocabularyId();

		TaxonomyCategory taxonomyCategory1 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, randomTaxonomyCategory());

		TaxonomyCategory taxonomyCategory2 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, randomTaxonomyCategory());

		TaxonomyCategory taxonomyCategory3 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, randomTaxonomyCategory());

		Page<TaxonomyCategory> page1 =
			invokeGetTaxonomyVocabularyTaxonomyCategoriesPage(
				taxonomyVocabularyId, null, null, Pagination.of(1, 2), null);

		List<TaxonomyCategory> taxonomyCategories1 =
			(List<TaxonomyCategory>)page1.getItems();

		Assert.assertEquals(
			taxonomyCategories1.toString(), 2, taxonomyCategories1.size());

		Page<TaxonomyCategory> page2 =
			invokeGetTaxonomyVocabularyTaxonomyCategoriesPage(
				taxonomyVocabularyId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<TaxonomyCategory> taxonomyCategories2 =
			(List<TaxonomyCategory>)page2.getItems();

		Assert.assertEquals(
			taxonomyCategories2.toString(), 1, taxonomyCategories2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(
				taxonomyCategory1, taxonomyCategory2, taxonomyCategory3),
			new ArrayList<TaxonomyCategory>() {
				{
					addAll(taxonomyCategories1);
					addAll(taxonomyCategories2);
				}
			});
	}

	@Test
	public void testGetTaxonomyVocabularyTaxonomyCategoriesPageWithSortDateTime()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long taxonomyVocabularyId =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_getTaxonomyVocabularyId();

		TaxonomyCategory taxonomyCategory1 = randomTaxonomyCategory();
		TaxonomyCategory taxonomyCategory2 = randomTaxonomyCategory();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				taxonomyCategory1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		taxonomyCategory1 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, taxonomyCategory1);

		Thread.sleep(1000);

		taxonomyCategory2 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, taxonomyCategory2);

		for (EntityField entityField : entityFields) {
			Page<TaxonomyCategory> ascPage =
				invokeGetTaxonomyVocabularyTaxonomyCategoriesPage(
					taxonomyVocabularyId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(taxonomyCategory1, taxonomyCategory2),
				(List<TaxonomyCategory>)ascPage.getItems());

			Page<TaxonomyCategory> descPage =
				invokeGetTaxonomyVocabularyTaxonomyCategoriesPage(
					taxonomyVocabularyId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(taxonomyCategory2, taxonomyCategory1),
				(List<TaxonomyCategory>)descPage.getItems());
		}
	}

	@Test
	public void testGetTaxonomyVocabularyTaxonomyCategoriesPageWithSortString()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long taxonomyVocabularyId =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_getTaxonomyVocabularyId();

		TaxonomyCategory taxonomyCategory1 = randomTaxonomyCategory();
		TaxonomyCategory taxonomyCategory2 = randomTaxonomyCategory();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				taxonomyCategory1, entityField.getName(), "Aaa");
			BeanUtils.setProperty(
				taxonomyCategory2, entityField.getName(), "Bbb");
		}

		taxonomyCategory1 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, taxonomyCategory1);

		taxonomyCategory2 =
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				taxonomyVocabularyId, taxonomyCategory2);

		for (EntityField entityField : entityFields) {
			Page<TaxonomyCategory> ascPage =
				invokeGetTaxonomyVocabularyTaxonomyCategoriesPage(
					taxonomyVocabularyId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(taxonomyCategory1, taxonomyCategory2),
				(List<TaxonomyCategory>)ascPage.getItems());

			Page<TaxonomyCategory> descPage =
				invokeGetTaxonomyVocabularyTaxonomyCategoriesPage(
					taxonomyVocabularyId, null, null, Pagination.of(1, 2),
					entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(taxonomyCategory2, taxonomyCategory1),
				(List<TaxonomyCategory>)descPage.getItems());
		}
	}

	protected TaxonomyCategory
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_addTaxonomyCategory(
				Long taxonomyVocabularyId, TaxonomyCategory taxonomyCategory)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_getTaxonomyVocabularyId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long
			testGetTaxonomyVocabularyTaxonomyCategoriesPage_getIrrelevantTaxonomyVocabularyId()
		throws Exception {

		return null;
	}

	protected Page<TaxonomyCategory>
			invokeGetTaxonomyVocabularyTaxonomyCategoriesPage(
				Long taxonomyVocabularyId, String search, String filterString,
				Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-vocabularies/{taxonomy-vocabulary-id}/taxonomy-categories",
					taxonomyVocabularyId);

		location = HttpUtil.addParameter(location, "filter", filterString);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		location = HttpUtil.addParameter(location, "sort", sortString);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		return _outputObjectMapper.readValue(
			string,
			new TypeReference<Page<TaxonomyCategory>>() {
			});
	}

	protected Http.Response
			invokeGetTaxonomyVocabularyTaxonomyCategoriesPageResponse(
				Long taxonomyVocabularyId, String search, String filterString,
				Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-vocabularies/{taxonomy-vocabulary-id}/taxonomy-categories",
					taxonomyVocabularyId);

		location = HttpUtil.addParameter(location, "filter", filterString);

		location = HttpUtil.addParameter(
			location, "page", pagination.getPage());
		location = HttpUtil.addParameter(
			location, "pageSize", pagination.getPageSize());

		location = HttpUtil.addParameter(location, "sort", sortString);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testPostTaxonomyVocabularyTaxonomyCategory() throws Exception {
		TaxonomyCategory randomTaxonomyCategory = randomTaxonomyCategory();

		TaxonomyCategory postTaxonomyCategory =
			testPostTaxonomyVocabularyTaxonomyCategory_addTaxonomyCategory(
				randomTaxonomyCategory);

		assertEquals(randomTaxonomyCategory, postTaxonomyCategory);
		assertValid(postTaxonomyCategory);
	}

	protected TaxonomyCategory
			testPostTaxonomyVocabularyTaxonomyCategory_addTaxonomyCategory(
				TaxonomyCategory taxonomyCategory)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected TaxonomyCategory invokePostTaxonomyVocabularyTaxonomyCategory(
			Long taxonomyVocabularyId, TaxonomyCategory taxonomyCategory)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(taxonomyCategory),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-vocabularies/{taxonomy-vocabulary-id}/taxonomy-categories",
					taxonomyVocabularyId);

		options.setLocation(location);

		options.setPost(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(
				string, TaxonomyCategory.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response
			invokePostTaxonomyVocabularyTaxonomyCategoryResponse(
				Long taxonomyVocabularyId, TaxonomyCategory taxonomyCategory)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(taxonomyCategory),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/taxonomy-vocabularies/{taxonomy-vocabulary-id}/taxonomy-categories",
					taxonomyVocabularyId);

		options.setLocation(location);

		options.setPost(true);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	protected void assertResponseCode(
		int expectedResponseCode, Http.Response actualResponse) {

		Assert.assertEquals(
			expectedResponseCode, actualResponse.getResponseCode());
	}

	protected void assertEquals(
		TaxonomyCategory taxonomyCategory1,
		TaxonomyCategory taxonomyCategory2) {

		Assert.assertTrue(
			taxonomyCategory1 + " does not equal " + taxonomyCategory2,
			equals(taxonomyCategory1, taxonomyCategory2));
	}

	protected void assertEquals(
		List<TaxonomyCategory> taxonomyCategories1,
		List<TaxonomyCategory> taxonomyCategories2) {

		Assert.assertEquals(
			taxonomyCategories1.size(), taxonomyCategories2.size());

		for (int i = 0; i < taxonomyCategories1.size(); i++) {
			TaxonomyCategory taxonomyCategory1 = taxonomyCategories1.get(i);
			TaxonomyCategory taxonomyCategory2 = taxonomyCategories2.get(i);

			assertEquals(taxonomyCategory1, taxonomyCategory2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<TaxonomyCategory> taxonomyCategories1,
		List<TaxonomyCategory> taxonomyCategories2) {

		Assert.assertEquals(
			taxonomyCategories1.size(), taxonomyCategories2.size());

		for (TaxonomyCategory taxonomyCategory1 : taxonomyCategories1) {
			boolean contains = false;

			for (TaxonomyCategory taxonomyCategory2 : taxonomyCategories2) {
				if (equals(taxonomyCategory1, taxonomyCategory2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				taxonomyCategories2 + " does not contain " + taxonomyCategory1,
				contains);
		}
	}

	protected void assertValid(TaxonomyCategory taxonomyCategory) {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertValid(Page<TaxonomyCategory> page) {
		boolean valid = false;

		Collection<TaxonomyCategory> taxonomyCategories = page.getItems();

		int size = taxonomyCategories.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected boolean equals(
		TaxonomyCategory taxonomyCategory1,
		TaxonomyCategory taxonomyCategory2) {

		if (taxonomyCategory1 == taxonomyCategory2) {
			return true;
		}

		return false;
	}

	protected Collection<EntityField> getEntityFields() throws Exception {
		if (!(_taxonomyCategoryResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_taxonomyCategoryResource;

		EntityModel entityModel = entityModelResource.getEntityModel(
			new MultivaluedHashMap());

		Map<String, EntityField> entityFieldsMap =
			entityModel.getEntityFieldsMap();

		return entityFieldsMap.values();
	}

	protected List<EntityField> getEntityFields(EntityField.Type type)
		throws Exception {

		Collection<EntityField> entityFields = getEntityFields();

		Stream<EntityField> stream = entityFields.stream();

		return stream.filter(
			entityField -> Objects.equals(entityField.getType(), type)
		).collect(
			Collectors.toList()
		);
	}

	protected String getFilterString(
		EntityField entityField, String operator,
		TaxonomyCategory taxonomyCategory) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("availableLanguages")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("creator")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("dateCreated")) {
			sb.append(_dateFormat.format(taxonomyCategory.getDateCreated()));

			return sb.toString();
		}

		if (entityFieldName.equals("dateModified")) {
			sb.append(_dateFormat.format(taxonomyCategory.getDateModified()));

			return sb.toString();
		}

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(taxonomyCategory.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("name")) {
			sb.append("'");
			sb.append(String.valueOf(taxonomyCategory.getName()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("numberOfTaxonomyCategories")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("parentTaxonomyCategory")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("parentTaxonomyVocabulary")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("parentVocabularyId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("viewableBy")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected TaxonomyCategory randomTaxonomyCategory() {
		return new TaxonomyCategory() {
			{
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				description = RandomTestUtil.randomString();
				id = RandomTestUtil.randomLong();
				name = RandomTestUtil.randomString();
				parentVocabularyId = RandomTestUtil.randomLong();
			}
		};
	}

	protected TaxonomyCategory randomIrrelevantTaxonomyCategory() {
		return randomTaxonomyCategory();
	}

	protected TaxonomyCategory randomPatchTaxonomyCategory() {
		return randomTaxonomyCategory();
	}

	protected Group irrelevantGroup;
	protected Group testGroup;

	protected static class Page<T> {

		public Collection<T> getItems() {
			return new ArrayList<>(items);
		}

		public long getLastPage() {
			return lastPage;
		}

		public long getPage() {
			return page;
		}

		public long getPageSize() {
			return pageSize;
		}

		public long getTotalCount() {
			return totalCount;
		}

		@JsonProperty
		protected Collection<T> items;

		@JsonProperty
		protected long lastPage;

		@JsonProperty
		protected long page;

		@JsonProperty
		protected long pageSize;

		@JsonProperty
		protected long totalCount;

	}

	private Http.Options _createHttpOptions() {
		Http.Options options = new Http.Options();

		options.addHeader("Accept", "application/json");

		String userNameAndPassword = "test@liferay.com:test";

		String encodedUserNameAndPassword = Base64.encode(
			userNameAndPassword.getBytes());

		options.addHeader(
			"Authorization", "Basic " + encodedUserNameAndPassword);

		options.addHeader("Content-Type", "application/json");

		return options;
	}

	private String _toPath(String template, Object... values) {
		if (ArrayUtil.isEmpty(values)) {
			return template;
		}

		for (int i = 0; i < values.length; i++) {
			template = template.replaceFirst(
				"\\{.*?\\}", String.valueOf(values[i]));
		}

		return template;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseTaxonomyCategoryResourceTestCase.class);

	private static BeanUtilsBean _beanUtilsBean = new BeanUtilsBean() {

		@Override
		public void copyProperty(Object bean, String name, Object value)
			throws IllegalAccessException, InvocationTargetException {

			if (value != null) {
				super.copyProperty(bean, name, value);
			}
		}

	};
	private static DateFormat _dateFormat;
	private final static ObjectMapper _inputObjectMapper = new ObjectMapper() {
		{
			setFilterProvider(
				new SimpleFilterProvider() {
					{
						addFilter(
							"Liferay.Vulcan",
							SimpleBeanPropertyFilter.serializeAll());
					}
				});
			setSerializationInclusion(JsonInclude.Include.NON_NULL);
		}
	};
	private final static ObjectMapper _outputObjectMapper = new ObjectMapper() {
		{
			setFilterProvider(
				new SimpleFilterProvider() {
					{
						addFilter(
							"Liferay.Vulcan",
							SimpleBeanPropertyFilter.serializeAll());
					}
				});
		}
	};

	@Inject
	private TaxonomyCategoryResource _taxonomyCategoryResource;

	private URL _resourceURL;

}