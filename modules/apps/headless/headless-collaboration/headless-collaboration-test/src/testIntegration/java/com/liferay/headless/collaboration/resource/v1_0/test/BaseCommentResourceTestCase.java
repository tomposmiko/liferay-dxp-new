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

import com.liferay.headless.collaboration.dto.v1_0.Comment;
import com.liferay.headless.collaboration.resource.v1_0.CommentResource;
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
public abstract class BaseCommentResourceTestCase {

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
	public void testGetBlogPostingCommentsPage() throws Exception {
		Long blogPostingId = testGetBlogPostingCommentsPage_getBlogPostingId();
		Long irrelevantBlogPostingId =
			testGetBlogPostingCommentsPage_getIrrelevantBlogPostingId();

		if ((irrelevantBlogPostingId != null)) {
			Comment irrelevantComment =
				testGetBlogPostingCommentsPage_addComment(
					irrelevantBlogPostingId, randomIrrelevantComment());

			Page<Comment> page = invokeGetBlogPostingCommentsPage(
				irrelevantBlogPostingId, null, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantComment),
				(List<Comment>)page.getItems());
			assertValid(page);
		}

		Comment comment1 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		Comment comment2 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		Page<Comment> page = invokeGetBlogPostingCommentsPage(
			blogPostingId, null, null, Pagination.of(1, 2), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(comment1, comment2), (List<Comment>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetBlogPostingCommentsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long blogPostingId = testGetBlogPostingCommentsPage_getBlogPostingId();

		Comment comment1 = randomComment();
		Comment comment2 = randomComment();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				comment1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		comment1 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, comment1);

		Thread.sleep(1000);

		comment2 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, comment2);

		for (EntityField entityField : entityFields) {
			Page<Comment> page = invokeGetBlogPostingCommentsPage(
				blogPostingId, null,
				getFilterString(entityField, "eq", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetBlogPostingCommentsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long blogPostingId = testGetBlogPostingCommentsPage_getBlogPostingId();

		Comment comment1 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment2 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		for (EntityField entityField : entityFields) {
			Page<Comment> page = invokeGetBlogPostingCommentsPage(
				blogPostingId, null,
				getFilterString(entityField, "eq", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetBlogPostingCommentsPageWithPagination()
		throws Exception {

		Long blogPostingId = testGetBlogPostingCommentsPage_getBlogPostingId();

		Comment comment1 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		Comment comment2 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		Comment comment3 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, randomComment());

		Page<Comment> page1 = invokeGetBlogPostingCommentsPage(
			blogPostingId, null, null, Pagination.of(1, 2), null);

		List<Comment> comments1 = (List<Comment>)page1.getItems();

		Assert.assertEquals(comments1.toString(), 2, comments1.size());

		Page<Comment> page2 = invokeGetBlogPostingCommentsPage(
			blogPostingId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<Comment> comments2 = (List<Comment>)page2.getItems();

		Assert.assertEquals(comments2.toString(), 1, comments2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(comment1, comment2, comment3),
			new ArrayList<Comment>() {
				{
					addAll(comments1);
					addAll(comments2);
				}
			});
	}

	@Test
	public void testGetBlogPostingCommentsPageWithSortDateTime()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long blogPostingId = testGetBlogPostingCommentsPage_getBlogPostingId();

		Comment comment1 = randomComment();
		Comment comment2 = randomComment();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				comment1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		comment1 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, comment1);

		Thread.sleep(1000);

		comment2 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, comment2);

		for (EntityField entityField : entityFields) {
			Page<Comment> ascPage = invokeGetBlogPostingCommentsPage(
				blogPostingId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(comment1, comment2),
				(List<Comment>)ascPage.getItems());

			Page<Comment> descPage = invokeGetBlogPostingCommentsPage(
				blogPostingId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(comment2, comment1),
				(List<Comment>)descPage.getItems());
		}
	}

	@Test
	public void testGetBlogPostingCommentsPageWithSortString()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long blogPostingId = testGetBlogPostingCommentsPage_getBlogPostingId();

		Comment comment1 = randomComment();
		Comment comment2 = randomComment();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(comment1, entityField.getName(), "Aaa");
			BeanUtils.setProperty(comment2, entityField.getName(), "Bbb");
		}

		comment1 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, comment1);

		comment2 = testGetBlogPostingCommentsPage_addComment(
			blogPostingId, comment2);

		for (EntityField entityField : entityFields) {
			Page<Comment> ascPage = invokeGetBlogPostingCommentsPage(
				blogPostingId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(comment1, comment2),
				(List<Comment>)ascPage.getItems());

			Page<Comment> descPage = invokeGetBlogPostingCommentsPage(
				blogPostingId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(comment2, comment1),
				(List<Comment>)descPage.getItems());
		}
	}

	protected Comment testGetBlogPostingCommentsPage_addComment(
			Long blogPostingId, Comment comment)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetBlogPostingCommentsPage_getBlogPostingId()
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetBlogPostingCommentsPage_getIrrelevantBlogPostingId()
		throws Exception {

		return null;
	}

	protected Page<Comment> invokeGetBlogPostingCommentsPage(
			Long blogPostingId, String search, String filterString,
			Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/blog-postings/{blog-posting-id}/comments", blogPostingId);

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
			new TypeReference<Page<Comment>>() {
			});
	}

	protected Http.Response invokeGetBlogPostingCommentsPageResponse(
			Long blogPostingId, String search, String filterString,
			Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath(
					"/blog-postings/{blog-posting-id}/comments", blogPostingId);

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
	public void testPostBlogPostingComment() throws Exception {
		Comment randomComment = randomComment();

		Comment postComment = testPostBlogPostingComment_addComment(
			randomComment);

		assertEquals(randomComment, postComment);
		assertValid(postComment);
	}

	protected Comment testPostBlogPostingComment_addComment(Comment comment)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment invokePostBlogPostingComment(
			Long blogPostingId, Comment comment)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(comment),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/blog-postings/{blog-posting-id}/comments", blogPostingId);

		options.setLocation(location);

		options.setPost(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(string, Comment.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokePostBlogPostingCommentResponse(
			Long blogPostingId, Comment comment)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(comment),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath(
					"/blog-postings/{blog-posting-id}/comments", blogPostingId);

		options.setLocation(location);

		options.setPost(true);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testDeleteComment() throws Exception {
		Comment comment = testDeleteComment_addComment();

		assertResponseCode(204, invokeDeleteCommentResponse(comment.getId()));

		assertResponseCode(404, invokeGetCommentResponse(comment.getId()));
	}

	protected Comment testDeleteComment_addComment() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void invokeDeleteComment(Long commentId) throws Exception {
		Http.Options options = _createHttpOptions();

		options.setDelete(true);

		String location =
			_resourceURL + _toPath("/comments/{comment-id}", commentId);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}
	}

	protected Http.Response invokeDeleteCommentResponse(Long commentId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setDelete(true);

		String location =
			_resourceURL + _toPath("/comments/{comment-id}", commentId);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetComment() throws Exception {
		Comment postComment = testGetComment_addComment();

		Comment getComment = invokeGetComment(postComment.getId());

		assertEquals(postComment, getComment);
		assertValid(getComment);
	}

	protected Comment testGetComment_addComment() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment invokeGetComment(Long commentId) throws Exception {
		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL + _toPath("/comments/{comment-id}", commentId);

		options.setLocation(location);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(string, Comment.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokeGetCommentResponse(Long commentId)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL + _toPath("/comments/{comment-id}", commentId);

		options.setLocation(location);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testPutComment() throws Exception {
		Comment postComment = testPutComment_addComment();

		Comment randomComment = randomComment();

		Comment putComment = invokePutComment(
			postComment.getId(), randomComment);

		assertEquals(randomComment, putComment);
		assertValid(putComment);

		Comment getComment = invokeGetComment(putComment.getId());

		assertEquals(randomComment, getComment);
		assertValid(getComment);
	}

	protected Comment testPutComment_addComment() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment invokePutComment(Long commentId, Comment comment)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(comment),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL + _toPath("/comments/{comment-id}", commentId);

		options.setLocation(location);

		options.setPut(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(string, Comment.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokePutCommentResponse(
			Long commentId, Comment comment)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(comment),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL + _toPath("/comments/{comment-id}", commentId);

		options.setLocation(location);

		options.setPut(true);

		HttpUtil.URLtoByteArray(options);

		return options.getResponse();
	}

	@Test
	public void testGetCommentCommentsPage() throws Exception {
		Long commentId = testGetCommentCommentsPage_getCommentId();
		Long irrelevantCommentId =
			testGetCommentCommentsPage_getIrrelevantCommentId();

		if ((irrelevantCommentId != null)) {
			Comment irrelevantComment = testGetCommentCommentsPage_addComment(
				irrelevantCommentId, randomIrrelevantComment());

			Page<Comment> page = invokeGetCommentCommentsPage(
				irrelevantCommentId, null, null, Pagination.of(1, 2), null);

			Assert.assertEquals(1, page.getTotalCount());

			assertEquals(
				Arrays.asList(irrelevantComment),
				(List<Comment>)page.getItems());
			assertValid(page);
		}

		Comment comment1 = testGetCommentCommentsPage_addComment(
			commentId, randomComment());

		Comment comment2 = testGetCommentCommentsPage_addComment(
			commentId, randomComment());

		Page<Comment> page = invokeGetCommentCommentsPage(
			commentId, null, null, Pagination.of(1, 2), null);

		Assert.assertEquals(2, page.getTotalCount());

		assertEqualsIgnoringOrder(
			Arrays.asList(comment1, comment2), (List<Comment>)page.getItems());
		assertValid(page);
	}

	@Test
	public void testGetCommentCommentsPageWithFilterDateTimeEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long commentId = testGetCommentCommentsPage_getCommentId();

		Comment comment1 = randomComment();
		Comment comment2 = randomComment();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				comment1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		comment1 = testGetCommentCommentsPage_addComment(commentId, comment1);

		Thread.sleep(1000);

		comment2 = testGetCommentCommentsPage_addComment(commentId, comment2);

		for (EntityField entityField : entityFields) {
			Page<Comment> page = invokeGetCommentCommentsPage(
				commentId, null, getFilterString(entityField, "eq", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetCommentCommentsPageWithFilterStringEquals()
		throws Exception {

		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long commentId = testGetCommentCommentsPage_getCommentId();

		Comment comment1 = testGetCommentCommentsPage_addComment(
			commentId, randomComment());

		@SuppressWarnings("PMD.UnusedLocalVariable")
		Comment comment2 = testGetCommentCommentsPage_addComment(
			commentId, randomComment());

		for (EntityField entityField : entityFields) {
			Page<Comment> page = invokeGetCommentCommentsPage(
				commentId, null, getFilterString(entityField, "eq", comment1),
				Pagination.of(1, 2), null);

			assertEquals(
				Collections.singletonList(comment1),
				(List<Comment>)page.getItems());
		}
	}

	@Test
	public void testGetCommentCommentsPageWithPagination() throws Exception {
		Long commentId = testGetCommentCommentsPage_getCommentId();

		Comment comment1 = testGetCommentCommentsPage_addComment(
			commentId, randomComment());

		Comment comment2 = testGetCommentCommentsPage_addComment(
			commentId, randomComment());

		Comment comment3 = testGetCommentCommentsPage_addComment(
			commentId, randomComment());

		Page<Comment> page1 = invokeGetCommentCommentsPage(
			commentId, null, null, Pagination.of(1, 2), null);

		List<Comment> comments1 = (List<Comment>)page1.getItems();

		Assert.assertEquals(comments1.toString(), 2, comments1.size());

		Page<Comment> page2 = invokeGetCommentCommentsPage(
			commentId, null, null, Pagination.of(2, 2), null);

		Assert.assertEquals(3, page2.getTotalCount());

		List<Comment> comments2 = (List<Comment>)page2.getItems();

		Assert.assertEquals(comments2.toString(), 1, comments2.size());

		assertEqualsIgnoringOrder(
			Arrays.asList(comment1, comment2, comment3),
			new ArrayList<Comment>() {
				{
					addAll(comments1);
					addAll(comments2);
				}
			});
	}

	@Test
	public void testGetCommentCommentsPageWithSortDateTime() throws Exception {
		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.DATE_TIME);

		if (entityFields.isEmpty()) {
			return;
		}

		Long commentId = testGetCommentCommentsPage_getCommentId();

		Comment comment1 = randomComment();
		Comment comment2 = randomComment();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(
				comment1, entityField.getName(),
				DateUtils.addMinutes(new Date(), -2));
		}

		comment1 = testGetCommentCommentsPage_addComment(commentId, comment1);

		Thread.sleep(1000);

		comment2 = testGetCommentCommentsPage_addComment(commentId, comment2);

		for (EntityField entityField : entityFields) {
			Page<Comment> ascPage = invokeGetCommentCommentsPage(
				commentId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(comment1, comment2),
				(List<Comment>)ascPage.getItems());

			Page<Comment> descPage = invokeGetCommentCommentsPage(
				commentId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(comment2, comment1),
				(List<Comment>)descPage.getItems());
		}
	}

	@Test
	public void testGetCommentCommentsPageWithSortString() throws Exception {
		List<EntityField> entityFields = getEntityFields(
			EntityField.Type.STRING);

		if (entityFields.isEmpty()) {
			return;
		}

		Long commentId = testGetCommentCommentsPage_getCommentId();

		Comment comment1 = randomComment();
		Comment comment2 = randomComment();

		for (EntityField entityField : entityFields) {
			BeanUtils.setProperty(comment1, entityField.getName(), "Aaa");
			BeanUtils.setProperty(comment2, entityField.getName(), "Bbb");
		}

		comment1 = testGetCommentCommentsPage_addComment(commentId, comment1);

		comment2 = testGetCommentCommentsPage_addComment(commentId, comment2);

		for (EntityField entityField : entityFields) {
			Page<Comment> ascPage = invokeGetCommentCommentsPage(
				commentId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":asc");

			assertEquals(
				Arrays.asList(comment1, comment2),
				(List<Comment>)ascPage.getItems());

			Page<Comment> descPage = invokeGetCommentCommentsPage(
				commentId, null, null, Pagination.of(1, 2),
				entityField.getName() + ":desc");

			assertEquals(
				Arrays.asList(comment2, comment1),
				(List<Comment>)descPage.getItems());
		}
	}

	protected Comment testGetCommentCommentsPage_addComment(
			Long commentId, Comment comment)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetCommentCommentsPage_getCommentId() throws Exception {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Long testGetCommentCommentsPage_getIrrelevantCommentId()
		throws Exception {

		return null;
	}

	protected Page<Comment> invokeGetCommentCommentsPage(
			Long commentId, String search, String filterString,
			Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath("/comments/{comment-id}/comments", commentId);

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
			new TypeReference<Page<Comment>>() {
			});
	}

	protected Http.Response invokeGetCommentCommentsPageResponse(
			Long commentId, String search, String filterString,
			Pagination pagination, String sortString)
		throws Exception {

		Http.Options options = _createHttpOptions();

		String location =
			_resourceURL +
				_toPath("/comments/{comment-id}/comments", commentId);

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
	public void testPostCommentComment() throws Exception {
		Comment randomComment = randomComment();

		Comment postComment = testPostCommentComment_addComment(randomComment);

		assertEquals(randomComment, postComment);
		assertValid(postComment);
	}

	protected Comment testPostCommentComment_addComment(Comment comment)
		throws Exception {

		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected Comment invokePostCommentComment(Long commentId, Comment comment)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(comment),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath("/comments/{comment-id}/comments", commentId);

		options.setLocation(location);

		options.setPost(true);

		String string = HttpUtil.URLtoString(options);

		if (_log.isDebugEnabled()) {
			_log.debug("HTTP response: " + string);
		}

		try {
			return _outputObjectMapper.readValue(string, Comment.class);
		}
		catch (Exception e) {
			_log.error("Unable to process HTTP response: " + string, e);

			throw e;
		}
	}

	protected Http.Response invokePostCommentCommentResponse(
			Long commentId, Comment comment)
		throws Exception {

		Http.Options options = _createHttpOptions();

		options.setBody(
			_inputObjectMapper.writeValueAsString(comment),
			ContentTypes.APPLICATION_JSON, StringPool.UTF8);

		String location =
			_resourceURL +
				_toPath("/comments/{comment-id}/comments", commentId);

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

	protected void assertEquals(Comment comment1, Comment comment2) {
		Assert.assertTrue(
			comment1 + " does not equal " + comment2,
			equals(comment1, comment2));
	}

	protected void assertEquals(
		List<Comment> comments1, List<Comment> comments2) {

		Assert.assertEquals(comments1.size(), comments2.size());

		for (int i = 0; i < comments1.size(); i++) {
			Comment comment1 = comments1.get(i);
			Comment comment2 = comments2.get(i);

			assertEquals(comment1, comment2);
		}
	}

	protected void assertEqualsIgnoringOrder(
		List<Comment> comments1, List<Comment> comments2) {

		Assert.assertEquals(comments1.size(), comments2.size());

		for (Comment comment1 : comments1) {
			boolean contains = false;

			for (Comment comment2 : comments2) {
				if (equals(comment1, comment2)) {
					contains = true;

					break;
				}
			}

			Assert.assertTrue(
				comments2 + " does not contain " + comment1, contains);
		}
	}

	protected void assertValid(Comment comment) {
		throw new UnsupportedOperationException(
			"This method needs to be implemented");
	}

	protected void assertValid(Page<Comment> page) {
		boolean valid = false;

		Collection<Comment> comments = page.getItems();

		int size = comments.size();

		if ((page.getLastPage() > 0) && (page.getPage() > 0) &&
			(page.getPageSize() > 0) && (page.getTotalCount() > 0) &&
			(size > 0)) {

			valid = true;
		}

		Assert.assertTrue(valid);
	}

	protected boolean equals(Comment comment1, Comment comment2) {
		if (comment1 == comment2) {
			return true;
		}

		return false;
	}

	protected Collection<EntityField> getEntityFields() throws Exception {
		if (!(_commentResource instanceof EntityModelResource)) {
			throw new UnsupportedOperationException(
				"Resource is not an instance of EntityModelResource");
		}

		EntityModelResource entityModelResource =
			(EntityModelResource)_commentResource;

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
		EntityField entityField, String operator, Comment comment) {

		StringBundler sb = new StringBundler();

		String entityFieldName = entityField.getName();

		sb.append(entityFieldName);

		sb.append(" ");
		sb.append(operator);
		sb.append(" ");

		if (entityFieldName.equals("creator")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("dateCreated")) {
			sb.append(_dateFormat.format(comment.getDateCreated()));

			return sb.toString();
		}

		if (entityFieldName.equals("dateModified")) {
			sb.append(_dateFormat.format(comment.getDateModified()));

			return sb.toString();
		}

		if (entityFieldName.equals("id")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("numberOfComments")) {
			throw new IllegalArgumentException(
				"Invalid entity field " + entityFieldName);
		}

		if (entityFieldName.equals("text")) {
			sb.append("'");
			sb.append(String.valueOf(comment.getText()));
			sb.append("'");

			return sb.toString();
		}

		throw new IllegalArgumentException(
			"Invalid entity field " + entityFieldName);
	}

	protected Comment randomComment() {
		return new Comment() {
			{
				dateCreated = RandomTestUtil.nextDate();
				dateModified = RandomTestUtil.nextDate();
				id = RandomTestUtil.randomLong();
				text = RandomTestUtil.randomString();
			}
		};
	}

	protected Comment randomIrrelevantComment() {
		return randomComment();
	}

	protected Comment randomPatchComment() {
		return randomComment();
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
		BaseCommentResourceTestCase.class);

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
	private CommentResource _commentResource;

	private URL _resourceURL;

}