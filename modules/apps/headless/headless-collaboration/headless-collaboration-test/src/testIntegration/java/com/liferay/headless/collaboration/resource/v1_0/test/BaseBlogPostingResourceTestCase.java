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

package com.liferay.headless.collaboration.resource.v1_0.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import com.liferay.headless.collaboration.dto.v1_0.BlogPosting;
import com.liferay.headless.collaboration.resource.v1_0.BlogPostingResource;
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
public abstract class BaseBlogPostingResourceTestCase {

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
			"http://localhost:8080/o/headless-collaboration/v1.0");
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(irrelevantGroup);
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testDeleteBlogPosting() throws Exception {
		BlogPosting blogPosting = testDeleteBlogPosting_addBlogPosting();

		assertResponseCode(
			204, invokeDeleteBlogPostingResponse(blogPosting.getId()));

		assertResponseCode(
			404, invokeGetBlogPostingResponse(blogPosting.getId()));
	}

	protected BlogPosting testDeleteBlogPosting_addBlogPosting()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void invokeDeleteBlogPosting(Long blogPostingId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setDelete(true);

		String location =
			_resourceURL +
				_toPath("/blog-postings/{blog-posting-id}", blogPostingId);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}
	}

	protected Http.Response invokeDeleteBlogPostingResponse(Long blogPostingId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setDelete(true);

		String location =
			_resourceURL +
				_toPath("/blog-postings/{blog-posting-id}", blogPostingId);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetBlogPosting() throws Exception {
		BlogPosting postBlogPosting = testGetBlogPosting_addBlogPosting();

		BlogPosting getBlogPosting = invokeGetBlogPosting(
			postBlogPosting.getId());

		assertEquals(postBlogPosting, getBlogPosting);
		assertValid(getBlogPosting);
	}

	protected BlogPosting testGetBlogPosting_addBlogPosting() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected BlogPosting invokeGetBlogPosting(Long blogPostingId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath("/blog-postings/{blog-posting-id}", blogPostingId);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(string, BlogPosting.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokeGetBlogPostingResponse(Long blogPostingId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath("/blog-postings/{blog-posting-id}", blogPostingId);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testPatchBlogPosting() throws Exception {
		BlogPosting postBlogPosting = testPatchBlogPosting_addBlogPosting();

		BlogPosting randomPatchBlogPosting = randomPatchBlogPosting();

		BlogPosting patchBlogPosting = invokePatchBlogPosting(
			postBlogPosting.getId(), randomPatchBlogPosting);

		BlogPosting expectedPatchBlogPosting = (BlogPosting)BeanUtils.cloneBean(
			postBlogPosting);

		_beanUtilsBean.copyProperties(
			expectedPatchBlogPosting, randomPatchBlogPosting);

		BlogPosting getBlogPosting = invokeGetBlogPosting(
			patchBlogPosting.getId());

		assertEquals(expectedPatchBlogPosting, getBlogPosting);
		assertValid(getBlogPosting);
	}

	protected BlogPosting testPatchBlogPosting_addBlogPosting()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected BlogPosting invokePatchBlogPosting(
			Long blogPostingId, BlogPosting blogPosting)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(blogPosting),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath("/blog-postings/{blog-posting-id}", blogPostingId);

		options.setLocation(location);

		options.setPatch(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(string, BlogPosting.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokePatchBlogPostingResponse(
			Long blogPostingId, BlogPosting blogPosting)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(blogPosting),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath("/blog-postings/{blog-posting-id}", blogPostingId);

		options.setLocation(location);

		options.setPatch(true);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testPutBlogPosting() throws Exception {
		BlogPosting postBlogPosting = testPutBlogPosting_addBlogPosting();

		BlogPosting randomBlogPosting = randomBlogPosting();

		BlogPosting putBlogPosting = invokePutBlogPosting(
			postBlogPosting.getId(), randomBlogPosting);

		assertEquals(randomBlogPosting, putBlogPosting);
		assertValid(putBlogPosting);

		BlogPosting getBlogPosting = invokeGetBlogPosting(
			putBlogPosting.getId());

		assertEquals(randomBlogPosting, getBlogPosting);
		assertValid(getBlogPosting);
	}

	protected BlogPosting testPutBlogPosting_addBlogPosting() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected BlogPosting invokePutBlogPosting(
			Long blogPostingId, BlogPosting blogPosting)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(blogPosting),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath("/blog-postings/{blog-posting-id}", blogPostingId);

		options.setLocation(location);

		options.setPut(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(string, BlogPosting.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokePutBlogPostingResponse(
			Long blogPostingId, BlogPosting blogPosting)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(blogPosting),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath("/blog-postings/{blog-posting-id}", blogPostingId);

		options.setLocation(location);

		options.setPut(true);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetContentSpaceBlogPostingsPage() throws Exception {
		Long contentSpaceId =
			testGetContentSpaceBlogPostingsPage_getContentSpaceId();
		Long irrelevantContentSpaceId =
			testGetContentSpaceBlogPostingsPage_getIrrelevantContentSpaceId();

		if ((irrelevantContentSpaceId != null)) {
			BlogPosting irrelevantBlogPosting =
				testGetContentSpaceBlogPostingsPage_addBlogPosting(
					irrelevantContentSpaceId, randomIrrelevantBlogPosting());

			Page<BlogPosting> page = invokeGetContentSpaceBlogPostingsPage(
				irrelevantContentSpaceId, null, null, Pagination.of(1, 2),
				null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantBlogPosting),
				(List<BlogPosting>)page.getItems());
			assertValid(page);
		}

		BlogPosting blogPosting1 =
			testGetContentSpaceBlogPostingsPage_addBlogPosting(
				contentSpaceId, randomBlogPosting());

		BlogPosting blogPosting2 =
			testGetContentSpaceBlogPostingsPage_addBlogPosting(
				contentSpaceId, randomBlogPosting());

		Page<BlogPosting> page = invokeGetContentSpaceBlogPostingsPage(
			contentSpaceId, null, null, Pagination.of(1, 2), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(blogPosting1, blogPosting2),
			(List<BlogPosting>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetContentSpaceBlogPostingsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long contentSpaceId =
			testGetContentSpaceBlogPostingsPage_getContentSpaceId();

		BlogPosting blogPosting1 = randomBlogPosting();
		BlogPosting blogPosting2 = randomBlogPosting();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				blogPosting1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		blogPosting1 = testGetContentSpaceBlogPostingsPage_addBlogPosting(
			contentSpaceId, blogPosting1);

		Thread.sleep(1000);

		blogPosting2 = testGetContentSpaceBlogPostingsPage_addBlogPosting(
			contentSpaceId, blogPosting2);

		for (EntityField entityField : entityFields) {
			Page<BlogPosting> page = invokeGetContentSpaceBlogPostingsPage(
				contentSpaceId, null,
				getFilterString(entityField, "eq", blogPosting1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(blogPosting1),
				(List<BlogPosting>)page.getItems());
		}
	}

	@Test
	public void testGetContentSpaceBlogPostingsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long contentSpaceId =
			testGetContentSpaceBlogPostingsPage_getContentSpaceId();

		BlogPosting blogPosting1 =
			testGetContentSpaceBlogPostingsPage_addBlogPosting(
				contentSpaceId, randomBlogPosting());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		BlogPosting blogPosting2 =
			testGetContentSpaceBlogPostingsPage_addBlogPosting(
				contentSpaceId, randomBlogPosting());

		for (EntityField entityField : entityFields) {
			Page<BlogPosting> page = invokeGetContentSpaceBlogPostingsPage(
				contentSpaceId, null,
				getFilterString(entityField, "eq", blogPosting1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(blogPosting1),
				(List<BlogPosting>)page.getItems());
		}
	}

	@Test
	public void testGetContentSpaceBlogPostingsPageWithPagination()
		throws Exception {

		Long contentSpaceId =
			testGetContentSpaceBlogPostingsPage_getContentSpaceId();

		BlogPosting blogPosting1 =
			testGetContentSpaceBlogPostingsPage_addBlogPosting(
				contentSpaceId, randomBlogPosting());

		BlogPosting blogPosting2 =
			testGetContentSpaceBlogPostingsPage_addBlogPosting(
				contentSpaceId, randomBlogPosting());

		BlogPosting blogPosting3 =
			testGetContentSpaceBlogPostingsPage_addBlogPosting(
				contentSpaceId, randomBlogPosting());

		Page<BlogPosting> page1 = invokeGetContentSpaceBlogPostingsPage(
			contentSpaceId, null, null, Pagination.of(1, 2), null);

		List<BlogPosting> blogPostings1 = (List<BlogPosting>)page1.getItems();

		Assert.assertEquals(blogPostings1.toString(), 2, blogPostings1.size());

		Page<BlogPosting> page2 = invokeGetContentSpaceBlogPostingsPage(
			contentSpaceId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<BlogPosting> blogPostings2 = (List<BlogPosting>)page2.getItems();

		Assert.assertEquals(blogPostings2.toString(), 1, blogPostings2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(blogPosting1, blogPosting2, blogPosting3),
			new ArrayList<BlogPosting>() {
				{
					addAll(blogPostings1);
					addAll(blogPostings2);
				}
			});
	}

	@Test
	public void testGetContentSpaceBlogPostingsPageWithSortDateTime()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long contentSpaceId =
			testGetContentSpaceBlogPostingsPage_getContentSpaceId();

		BlogPosting blogPosting1 = randomBlogPosting();
		BlogPosting blogPosting2 = randomBlogPosting();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				blogPosting1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		blogPosting1 = testGetContentSpaceBlogPostingsPage_addBlogPosting(
			contentSpaceId, blogPosting1);

		Thread.sleep(1000);

		blogPosting2 = testGetContentSpaceBlogPostingsPage_addBlogPosting(
			contentSpaceId, blogPosting2);

		for (EntityField entityField : entityFields) {
			Page<BlogPosting> ascPage = invokeGetContentSpaceBlogPostingsPage(
				contentSpaceId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(blogPosting1, blogPosting2),
				(List<BlogPosting>)ascPage.getItems());

			Page<BlogPosting> descPage = invokeGetContentSpaceBlogPostingsPage(
				contentSpaceId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(blogPosting2, blogPosting1),
				(List<BlogPosting>)descPage.getItems());
		}
	}

	@Test
	public void testGetContentSpaceBlogPostingsPageWithSortString()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long contentSpaceId =
			testGetContentSpaceBlogPostingsPage_getContentSpaceId();

		BlogPosting blogPosting1 = randomBlogPosting();
		BlogPosting blogPosting2 = randomBlogPosting();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(blogPosting1, entityField.getName(), "Aaa");
			BeanUtils.setProperty(blogPosting2, entityField.getName(), "Bbb");
		}

		blogPosting1 = testGetContentSpaceBlogPostingsPage_addBlogPosting(
			contentSpaceId, blogPosting1);

		blogPosting2 = testGetContentSpaceBlogPostingsPage_addBlogPosting(
			contentSpaceId, blogPosting2);

		for (EntityField entityField : entityFields) {
			Page<BlogPosting> ascPage = invokeGetContentSpaceBlogPostingsPage(
				contentSpaceId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(blogPosting1, blogPosting2),
				(List<BlogPosting>)ascPage.getItems());

			Page<BlogPosting> descPage = invokeGetContentSpaceBlogPostingsPage(
				contentSpaceId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(blogPosting2, blogPosting1),
				(List<BlogPosting>)descPage.getItems());
		}
	}

	protected BlogPosting testGetContentSpaceBlogPostingsPage_addBlogPosting(
			Long contentSpaceId, BlogPosting blogPosting)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetContentSpaceBlogPostingsPage_getContentSpaceId()
		throws Exception {

		return testGroup.getGroupId();
	}

	protected Long
			testGetContentSpaceBlogPostingsPage_getIrrelevantContentSpaceId()
		throws Exception {

		return irrelevantGroup.getGroupId();
	}

	protected Page<BlogPosting> invokeGetContentSpaceBlogPostingsPage(
			Long contentSpaceId, String search, String filterString,
			Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/content-spaces/{content-space-id}/blog-postings",
					contentSpaceId);

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
			new TypeReference<Page<BlogPosting>>() {
			});
	}

	protected Http.Response invokeGetContentSpaceBlogPostingsPageResponse(
			Long contentSpaceId, String search, String filterString,
			Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/content-spaces/{content-space-id}/blog-postings",
					contentSpaceId);

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
	public void testPostContentSpaceBlogPosting() throws Exception {
		BlogPosting randomBlogPosting = randomBlogPosting();

		BlogPosting postBlogPosting =
			testPostContentSpaceBlogPosting_addBlogPosting(randomBlogPosting);

		assertEquals(randomBlogPosting, postBlogPosting);
		assertValid(postBlogPosting);
	}

	protected BlogPosting testPostContentSpaceBlogPosting_addBlogPosting(
			BlogPosting blogPosting)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected BlogPosting invokePostContentSpaceBlogPosting(
			Long contentSpaceId, BlogPosting blogPosting)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(blogPosting),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/content-spaces/{content-space-id}/blog-postings",
					contentSpaceId);

		options.setLocation(location);

		options.setPost(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(string, BlogPosting.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokePostContentSpaceBlogPostingResponse(
			Long contentSpaceId, BlogPosting blogPosting)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(blogPosting),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/content-spaces/{content-space-id}/blog-postings",
					contentSpaceId);

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
		BlogPosting blogPosting1, BlogPosting blogPosting2) {

		Assert.assertTrue(
			blogPosting1 + " does not equal " + blogPosting2,
			equals(blogPosting1, blogPosting2));
	}

	protected void assertEquals(
		List<BlogPosting> blogPostings1, List<BlogPosting> blogPostings2) {

		Assert.assertEquals(blogPostings1.size(), blogPostings2.size());

		for (int i = 0; i < blogPostings1.size(); i++) {
			BlogPosting blogPosting1 = blogPostings1.get(i);
			BlogPosting blogPosting2 = blogPostings2.get(i);

			assertEquals(blogPosting1, blogPosting2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<BlogPosting> blogPostings1, List<BlogPosting> blogPostings2) {

		Assert.assertEquals(blogPostings1.size(), blogPostings2.size());

		for (BlogPosting blogPosting1 : blogPostings1) {
			boolean contains = false;

			for (BlogPosting blogPosting2 : blogPostings2) {
				if (equals(blogPosting1, blogPosting2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				blogPostings2 + " does not contain " + blogPosting1, contains);
		}
	}

	protected void assertValid(BlogPosting blogPosting) {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertValid(Page<BlogPosting> page) {
		boolean valid = false;

		Collection<BlogPosting> blogPostings = page.getItems();

		int size = blogPostings.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected boolean equals(
		BlogPosting blogPosting1, BlogPosting blogPosting2) {

		if (blogPosting1 == blogPosting2) {
			return true;
		}

		return false;
	}

	protected Collection<EntityField> getEntityFields() throws Exception {
		if (!(_blogPostingResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_blogPostingResource;

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
		EntityField entityField, String operator, BlogPosting blogPosting) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("aggregateRating")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("alternativeHeadline")) {
			sb.append("'");
			sb.append(String.valueOf(blogPosting.getAlternativeHeadline()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("articleBody")) {
			sb.append("'");
			sb.append(String.valueOf(blogPosting.getArticleBody()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("contentSpaceId")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("creator")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("dateCreated")) {
			sb.append(_dateFormat.format(blogPosting.getDateCreated()));

			return sb.toString();
		}

		if (entityFieldName.equals("dateModified")) {
			sb.append(_dateFormat.format(blogPosting.getDateModified()));

			return sb.toString();
		}

		if (entityFieldName.equals("datePublished")) {
			sb.append(_dateFormat.format(blogPosting.getDatePublished()));

			return sb.toString();
		}

		if (entityFieldName.equals("description")) {
			sb.append("'");
			sb.append(String.valueOf(blogPosting.getDescription()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("encodingFormat")) {
			sb.append("'");
			sb.append(String.valueOf(blogPosting.getEncodingFormat()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("friendlyUrlPath")) {
			sb.append("'");
			sb.append(String.valueOf(blogPosting.getFriendlyUrlPath()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("headline")) {
			sb.append("'");
			sb.append(String.valueOf(blogPosting.getHeadline()));
			sb.append("'");

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("image")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("keywords")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("numberOfComments")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("taxonomyCategories")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("taxonomyCategoryIds")) {
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

	protected BlogPosting randomBlogPosting() {
		return new BlogPosting() {
			{
				alternativeHeadline = RandomTestUtil.randomString();
				articleBody = RandomTestUtil.randomString();
				contentSpaceId = RandomTestUtil.randomLong();
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				datePublished = RandomTestUtil.nextDate();
				description = RandomTestUtil.randomString();
				encodingFormat = RandomTestUtil.randomString();
				friendlyUrlPath = RandomTestUtil.randomString();
				headline = RandomTestUtil.randomString();
				id = RandomTestUtil.randomLong();
			}
		};
	}

	protected BlogPosting randomIrrelevantBlogPosting() {
		return randomBlogPosting();
	}

	protected BlogPosting randomPatchBlogPosting() {
		return randomBlogPosting();
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
		BaseBlogPostingResourceTestCase.class);

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
	private BlogPostingResource _blogPostingResource;

	private URL _resourceURL;

}