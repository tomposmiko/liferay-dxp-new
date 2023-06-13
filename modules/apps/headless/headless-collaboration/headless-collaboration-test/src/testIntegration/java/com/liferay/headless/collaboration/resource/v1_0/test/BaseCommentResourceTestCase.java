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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.headless.collaboration.dto.v1_0.Comment;
import com.liferay.headless.collaboration.dto.v1_0.Creator;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseCommentResourceTestCase {

	@Before
	public void setUp() throws Exception {
		testGroup = GroupTestUtil.addGroup();

		_resourceURL = new URL(
			"http://localhost:8080/o/headless-collaboration/v1.0");
	}

	@After
	public void tearDown() throws Exception {
		GroupTestUtil.deleteGroup(testGroup);
	}

	@Test
	public void testGetBlogPostingCommentsPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPostBlogPostingComment() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testDeleteComment() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetComment() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPutComment() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testGetCommentCommentsPage() throws Exception {
			Assert.assertTrue(true);
	}
	@Test
	public void testPostCommentComment() throws Exception {
			Assert.assertTrue(true);
	}

	protected void assertResponseCode(int expectedResponseCode, Http.Response actualResponse) {
		Assert.assertEquals(expectedResponseCode, actualResponse.getResponseCode());
	}

	protected Page<Comment> invokeGetBlogPostingCommentsPage(
				Long blogPostingId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/blog-postings/{blog-posting-id}/comments", blogPostingId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<CommentImpl>>() {});
	}

	protected Http.Response invokeGetBlogPostingCommentsPageResponse(
				Long blogPostingId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/blog-postings/{blog-posting-id}/comments", blogPostingId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Comment invokePostBlogPostingComment(
				Long blogPostingId,Comment comment)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(comment), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/blog-postings/{blog-posting-id}/comments", blogPostingId));

				options.setPost(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), CommentImpl.class);
	}

	protected Http.Response invokePostBlogPostingCommentResponse(
				Long blogPostingId,Comment comment)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(comment), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/blog-postings/{blog-posting-id}/comments", blogPostingId));

				options.setPost(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected boolean invokeDeleteComment(
				Long commentId)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setDelete(true);

			options.setLocation(_resourceURL + _toPath("/comments/{comment-id}", commentId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), Boolean.class);
	}

	protected Http.Response invokeDeleteCommentResponse(
				Long commentId)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setDelete(true);

			options.setLocation(_resourceURL + _toPath("/comments/{comment-id}", commentId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Comment invokeGetComment(
				Long commentId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/comments/{comment-id}", commentId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), CommentImpl.class);
	}

	protected Http.Response invokeGetCommentResponse(
				Long commentId)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/comments/{comment-id}", commentId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Comment invokePutComment(
				Long commentId,Comment comment)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(comment), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/comments/{comment-id}", commentId));

				options.setPut(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), CommentImpl.class);
	}

	protected Http.Response invokePutCommentResponse(
				Long commentId,Comment comment)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(comment), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/comments/{comment-id}", commentId));

				options.setPut(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Page<Comment> invokeGetCommentCommentsPage(
				Long commentId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/comments/{comment-id}/comments", commentId));

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), new TypeReference<Page<CommentImpl>>() {});
	}

	protected Http.Response invokeGetCommentCommentsPageResponse(
				Long commentId,Pagination pagination)
			throws Exception {

			Http.Options options = _createHttpOptions();

			options.setLocation(_resourceURL + _toPath("/comments/{comment-id}/comments", commentId));

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}
	protected Comment invokePostCommentComment(
				Long commentId,Comment comment)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(comment), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/comments/{comment-id}/comments", commentId));

				options.setPost(true);

				return _outputObjectMapper.readValue(HttpUtil.URLtoString(options), CommentImpl.class);
	}

	protected Http.Response invokePostCommentCommentResponse(
				Long commentId,Comment comment)
			throws Exception {

			Http.Options options = _createHttpOptions();

				options.setBody(_inputObjectMapper.writeValueAsString(comment), ContentTypes.APPLICATION_JSON, StringPool.UTF8);

			options.setLocation(_resourceURL + _toPath("/comments/{comment-id}/comments", commentId));

				options.setPost(true);

			HttpUtil.URLtoString(options);

			return options.getResponse();
	}

	protected void assertEquals(Comment comment1, Comment comment2) {
		Assert.assertTrue(comment1 + " does not equal " + comment2, equals(comment1, comment2));
	}

	protected void assertEquals(List<Comment> comments1, List<Comment> comments2) {
		Assert.assertEquals(comments1.size(), comments2.size());

		for (int i = 0; i < comments1.size(); i++) {
			Comment comment1 = comments1.get(i);
			Comment comment2 = comments2.get(i);

			assertEquals(comment1, comment2);
	}
	}

	protected boolean equals(Comment comment1, Comment comment2) {
		if (comment1 == comment2) {
			return true;
	}

		return false;
	}

	protected Comment randomComment() {
		return new CommentImpl() {
			{

						dateCreated = RandomTestUtil.nextDate();
						dateModified = RandomTestUtil.nextDate();
						hasComments = RandomTestUtil.randomBoolean();
						id = RandomTestUtil.randomLong();
						text = RandomTestUtil.randomString();
	}
		};
	}

	protected Group testGroup;

	protected static class CommentImpl implements Comment {

	public Comment[] getComments() {
				return comments;
	}

	public void setComments(Comment[] comments) {
				this.comments = comments;
	}

	@JsonIgnore
	public void setComments(
				UnsafeSupplier<Comment[], Throwable> commentsUnsafeSupplier) {

				try {
					comments = commentsUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Comment[] comments;
	public Creator getCreator() {
				return creator;
	}

	public void setCreator(Creator creator) {
				this.creator = creator;
	}

	@JsonIgnore
	public void setCreator(
				UnsafeSupplier<Creator, Throwable> creatorUnsafeSupplier) {

				try {
					creator = creatorUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Creator creator;
	public Date getDateCreated() {
				return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
				this.dateCreated = dateCreated;
	}

	@JsonIgnore
	public void setDateCreated(
				UnsafeSupplier<Date, Throwable> dateCreatedUnsafeSupplier) {

				try {
					dateCreated = dateCreatedUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Date dateCreated;
	public Date getDateModified() {
				return dateModified;
	}

	public void setDateModified(Date dateModified) {
				this.dateModified = dateModified;
	}

	@JsonIgnore
	public void setDateModified(
				UnsafeSupplier<Date, Throwable> dateModifiedUnsafeSupplier) {

				try {
					dateModified = dateModifiedUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Date dateModified;
	public Boolean getHasComments() {
				return hasComments;
	}

	public void setHasComments(Boolean hasComments) {
				this.hasComments = hasComments;
	}

	@JsonIgnore
	public void setHasComments(
				UnsafeSupplier<Boolean, Throwable> hasCommentsUnsafeSupplier) {

				try {
					hasComments = hasCommentsUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Boolean hasComments;
	public Long getId() {
				return id;
	}

	public void setId(Long id) {
				this.id = id;
	}

	@JsonIgnore
	public void setId(
				UnsafeSupplier<Long, Throwable> idUnsafeSupplier) {

				try {
					id = idUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected Long id;
	public String getText() {
				return text;
	}

	public void setText(String text) {
				this.text = text;
	}

	@JsonIgnore
	public void setText(
				UnsafeSupplier<String, Throwable> textUnsafeSupplier) {

				try {
					text = textUnsafeSupplier.get();
	}
				catch (Throwable t) {
					throw new RuntimeException(t);
	}
	}

	@JsonProperty
	protected String text;

	public String toString() {
			StringBundler sb = new StringBundler();

			sb.append("{");

					sb.append("comments=");

				sb.append(comments);
					sb.append(", creator=");

				sb.append(creator);
					sb.append(", dateCreated=");

				sb.append(dateCreated);
					sb.append(", dateModified=");

				sb.append(dateModified);
					sb.append(", hasComments=");

				sb.append(hasComments);
					sb.append(", id=");

				sb.append(id);
					sb.append(", text=");

				sb.append(text);

			sb.append("}");

			return sb.toString();
	}

	}

	protected static class Page<T> {

	public Collection<T> getItems() {
			return new ArrayList<>(items);
	}

	public int getItemsPerPage() {
			return itemsPerPage;
	}

	public int getLastPageNumber() {
			return lastPageNumber;
	}

	public int getPageNumber() {
			return pageNumber;
	}

	public int getTotalCount() {
			return totalCount;
	}

	@JsonProperty
	protected Collection<T> items;

	@JsonProperty
	protected int itemsPerPage;

	@JsonProperty
	protected int lastPageNumber;

	@JsonProperty
	protected int pageNumber;

	@JsonProperty
	protected int totalCount;

	}

	private Http.Options _createHttpOptions() {
		Http.Options options = new Http.Options();

		options.addHeader("Accept", "application/json");

		String userNameAndPassword = "test@liferay.com:test";

		String encodedUserNameAndPassword = Base64.encode(userNameAndPassword.getBytes());

		options.addHeader("Authorization", "Basic " + encodedUserNameAndPassword);

		options.addHeader("Content-Type", "application/json");

		return options;
	}

	private String _toPath(String template, Object value) {
		return template.replaceFirst("\\{.*\\}", String.valueOf(value));
	}

	private final static ObjectMapper _inputObjectMapper = new ObjectMapper() {
		{
			setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}
	};
	private final static ObjectMapper _outputObjectMapper = new ObjectMapper();

	private URL _resourceURL;

}