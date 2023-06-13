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

package com.liferay.headless.collaboration.internal.resource.v1_0;

import com.liferay.headless.collaboration.dto.v1_0.Rating;
import com.liferay.headless.collaboration.resource.v1_0.RatingResource;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.util.TransformUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import javax.validation.constraints.NotNull;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
@Path("/v1.0")
public abstract class BaseRatingResourceImpl implements RatingResource {

	@Override
	@GET
	@Path("/blog-postings/{blog-posting-id}/ratings")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Rating")})
	public Page<Rating> getBlogPostingRatingsPage(
			@NotNull @PathParam("blog-posting-id") Long blogPostingId)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	@Override
	@Consumes("application/json")
	@POST
	@Path("/blog-postings/{blog-posting-id}/ratings")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Rating")})
	public Rating postBlogPostingRating(
			@NotNull @PathParam("blog-posting-id") Long blogPostingId,
			Rating rating)
		throws Exception {

		return new Rating();
	}

	@Override
	@GET
	@Path("/knowledge-base-articles/{knowledge-base-article-id}/ratings")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Rating")})
	public Page<Rating> getKnowledgeBaseArticleRatingsPage(
			@NotNull @PathParam("knowledge-base-article-id") Long
				knowledgeBaseArticleId)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	@Override
	@Consumes("application/json")
	@POST
	@Path("/knowledge-base-articles/{knowledge-base-article-id}/ratings")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Rating")})
	public Rating postKnowledgeBaseArticleRating(
			@NotNull @PathParam("knowledge-base-article-id") Long
				knowledgeBaseArticleId,
			Rating rating)
		throws Exception {

		return new Rating();
	}

	@Override
	@GET
	@Path("/message-board-messages/{message-board-message-id}/ratings")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Rating")})
	public Page<Rating> getMessageBoardMessageRatingsPage(
			@NotNull @PathParam("message-board-message-id") Long
				messageBoardMessageId)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	@Override
	@Consumes("application/json")
	@POST
	@Path("/message-board-messages/{message-board-message-id}/ratings")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Rating")})
	public Rating postMessageBoardMessageRating(
			@NotNull @PathParam("message-board-message-id") Long
				messageBoardMessageId,
			Rating rating)
		throws Exception {

		return new Rating();
	}

	@Override
	@GET
	@Path("/message-board-threads/{message-board-thread-id}/ratings")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Rating")})
	public Page<Rating> getMessageBoardThreadRatingsPage(
			@NotNull @PathParam("message-board-thread-id") Long
				messageBoardThreadId)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	@Override
	@Consumes("application/json")
	@POST
	@Path("/message-board-threads/{message-board-thread-id}/ratings")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Rating")})
	public Rating postMessageBoardThreadRating(
			@NotNull @PathParam("message-board-thread-id") Long
				messageBoardThreadId,
			Rating rating)
		throws Exception {

		return new Rating();
	}

	@Override
	@DELETE
	@Path("/ratings/{rating-id}")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Rating")})
	public void deleteRating(@NotNull @PathParam("rating-id") Long ratingId)
		throws Exception {
	}

	@Override
	@GET
	@Path("/ratings/{rating-id}")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Rating")})
	public Rating getRating(@NotNull @PathParam("rating-id") Long ratingId)
		throws Exception {

		return new Rating();
	}

	@Override
	@Consumes("application/json")
	@PUT
	@Path("/ratings/{rating-id}")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "Rating")})
	public Rating putRating(
			@NotNull @PathParam("rating-id") Long ratingId, Rating rating)
		throws Exception {

		return new Rating();
	}

	public void setContextCompany(Company contextCompany) {
		this.contextCompany = contextCompany;
	}

	protected void preparePatch(Rating rating) {
	}

	protected <T, R> List<R> transform(
		Collection<T> collection,
		UnsafeFunction<T, R, Exception> unsafeFunction) {

		return TransformUtil.transform(collection, unsafeFunction);
	}

	protected <T, R> R[] transform(
		T[] array, UnsafeFunction<T, R, Exception> unsafeFunction,
		Class<?> clazz) {

		return TransformUtil.transform(array, unsafeFunction, clazz);
	}

	protected <T, R> R[] transformToArray(
		Collection<T> collection,
		UnsafeFunction<T, R, Exception> unsafeFunction, Class<?> clazz) {

		return TransformUtil.transformToArray(
			collection, unsafeFunction, clazz);
	}

	protected <T, R> List<R> transformToList(
		T[] array, UnsafeFunction<T, R, Exception> unsafeFunction) {

		return TransformUtil.transformToList(array, unsafeFunction);
	}

	@Context
	protected AcceptLanguage contextAcceptLanguage;

	@Context
	protected Company contextCompany;

	@Context
	protected UriInfo contextUriInfo;

}