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

package com.liferay.apio.architect.sample.internal.dao;

import static java.util.concurrent.TimeUnit.DAYS;

import com.github.javafaker.DateAndTime;
import com.github.javafaker.Faker;
import com.github.javafaker.Shakespeare;

import com.liferay.apio.architect.sample.internal.dto.BlogPostingCommentModel;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Contains methods for creating, retrieving, updating, and deleting blog post
 * comments in an in-memory database with fake data.
 *
 * @author Alejandro Hernández
 */
@Component(immediate = true, service = BlogPostingCommentModelService.class)
public class BlogPostingCommentModelService {

	/**
	 * Computes the fake data for this service.
	 */
	@Activate
	public void activate() {
		int blogPostingModelCount = _blogPostingModelService.getCount();

		for (long index = 0; index < blogPostingModelCount; index++) {
			Map<Long, BlogPostingCommentModel> blogPostingCommentModels =
				new HashMap<>();
			Random random = new Random();

			for (int i = 0; i < random.nextInt(70); i++) {
				long creatorId = random.nextInt(_personModelService.getCount());

				Faker faker = new Faker();

				Shakespeare shakespeare = faker.shakespeare();

				DateAndTime dateAndTime = faker.date();

				Date date = dateAndTime.past(400, DAYS);

				BlogPostingCommentModel blogPostingCommentModel =
					new BlogPostingCommentModel(
						creatorId, _count.get(), index,
						shakespeare.hamletQuote(), date, date);

				blogPostingCommentModels.put(
					_count.getAndIncrement(), blogPostingCommentModel);
			}

			_blogPostingCommentModels.put(index, blogPostingCommentModels);
		}
	}

	/**
	 * Adds a new blog posting comment.
	 *
	 * @param  creatorId the ID of the blog posting comment's creator
	 * @param  blogPostingModelId the blog posting comment's ID
	 * @param  content the blog posting comment's content
	 * @return the new blog posting comment
	 */
	public BlogPostingCommentModel create(
		long creatorId, long blogPostingModelId, String content) {

		BlogPostingCommentModel blogPostingCommentModel =
			new BlogPostingCommentModel(
				creatorId, _count.get(), blogPostingModelId, content,
				new Date(), new Date());

		Map<Long, BlogPostingCommentModel> blogPostingCommentModels =
			_blogPostingCommentModels.computeIfAbsent(
				blogPostingModelId, __ -> new HashMap<>());

		blogPostingCommentModels.put(
			_count.getAndIncrement(), blogPostingCommentModel);

		return blogPostingCommentModel;
	}

	/**
	 * Returns the blog posting comment that matches the specified ID, if that
	 * comment exists; returns {@code Optional#empty()} otherwise.
	 *
	 * @param  id the blog posting comment's ID
	 * @return the blog posting comment, if present; {@code Optional#empty()}
	 *         otherwise
	 */
	public Optional<BlogPostingCommentModel> get(long id) {
		Collection<Map<Long, BlogPostingCommentModel>>
			blogPostingCommentModels = _blogPostingCommentModels.values();

		Stream<Map<Long, BlogPostingCommentModel>> stream =
			blogPostingCommentModels.stream();

		return stream.map(
			Map::values
		).map(
			Collection::stream
		).flatMap(
			Function.identity()
		).filter(
			blogPostingCommentModel -> blogPostingCommentModel.getId() == id
		).findFirst();
	}

	/**
	 * Returns the total number of comments for a blog posting.
	 *
	 * @param  blogPostingModelId the blog posting's ID
	 * @return the total number of comments
	 */
	public int getCount(long blogPostingModelId) {
		return Optional.of(
			blogPostingModelId
		).map(
			_blogPostingCommentModels::get
		).map(
			Map::size
		).orElse(
			0
		);
	}

	/**
	 * Returns the page of comments for a blog posting, as specified by the
	 * page's start and end positions.
	 *
	 * @param  blogPostingModelId the blog posting's ID
	 * @param  start the page's start position
	 * @param  end the page's end position
	 * @return the page of blog posting comments
	 */
	public List<BlogPostingCommentModel> getPage(
		long blogPostingModelId, int start, int end) {

		return Optional.of(
			blogPostingModelId
		).map(
			_blogPostingCommentModels::get
		).map(
			Map::values
		).map(
			Collection::stream
		).orElseGet(
			Stream::empty
		).skip(
			start
		).limit(
			end
		).collect(
			Collectors.toList()
		);
	}

	/**
	 * Deletes the blog posting comment that matches the specified ID.
	 *
	 * @param id the blog posting comment's ID
	 */
	public void remove(long id) {
		_blogPostingCommentModels.remove(id);
	}

	/**
	 * Updates the blog posting comment that matches the specified ID, if that
	 * blog posting comment exists; returns {@code Optional#empty()} otherwise.
	 *
	 * @param  id the blog posting comment's ID
	 * @param  content the blog posting comment's new content
	 * @return the updated blog posting comment, if present; {@code
	 *         Optional#empty()} otherwise
	 */
	public Optional<BlogPostingCommentModel> update(long id, String content) {
		Optional<BlogPostingCommentModel> oldOptional = get(id);

		Optional<BlogPostingCommentModel> newOptional = oldOptional.map(
			blogPostingCommentModel -> new BlogPostingCommentModel(
				blogPostingCommentModel.getCreatorId(), id,
				blogPostingCommentModel.getBlogPostingModelId(), content,
				blogPostingCommentModel.getCreateDate(), new Date()));

		newOptional.ifPresent(
			blogPostingCommentModel -> {
				long blogPostingModelId =
					blogPostingCommentModel.getBlogPostingModelId();

				Map<Long, BlogPostingCommentModel> blogPostingCommentModels =
					_blogPostingCommentModels.computeIfAbsent(
						blogPostingModelId, __ -> new HashMap<>());

				blogPostingCommentModels.put(id, blogPostingCommentModel);
			});

		return newOptional;
	}

	private final Map<Long, Map<Long, BlogPostingCommentModel>>
		_blogPostingCommentModels = new ConcurrentHashMap<>();

	@Reference
	private BlogPostingModelService _blogPostingModelService;

	private final AtomicLong _count = new AtomicLong(0);

	@Reference
	private PersonModelService _personModelService;

}