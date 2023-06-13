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

package com.liferay.portal.search.internal.searcher;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.search.hits.SearchHits;
import com.liferay.portal.search.searcher.SearchResponse;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author André de Oliveira
 */
public class SearchResponseImplTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testDefaultsAreNullSafe() {
		SearchResponse searchResponse = new SearchResponseImpl(
			new SearchContext());

		assertIs(searchResponse.getAggregationResult(null), nullValue());
		assertIs(searchResponse.getAggregationResultsMap(), emptyMap());
		assertIs(searchResponse.getCount(), zeroLong());
		assertIs(searchResponse.getDocuments71(), emptyList());
		assertIs(searchResponse.getDocumentsStream(), emptyStream());
		assertIs(searchResponse.getFederatedSearchKey(), blank());
		assertIs(
			searchResponse.getFederatedSearchResponse(null),
			same(searchResponse));
		assertIs(
			searchResponse.getFederatedSearchResponsesStream(), emptyStream());
		assertIs(searchResponse.getGroupByResponses(), emptyList());
		assertIs(searchResponse.getRequest(), nullValue());
		assertIs(searchResponse.getRequestString(), blank());
		assertIs(searchResponse.getResponseString(), blank());
		assertIs(searchResponse.getSearchHits(), instanceOf(SearchHits.class));
		assertIs(searchResponse.getStatsResponseMap(), emptyMap());
		assertIs(searchResponse.getTotalHits(), zeroInt());
	}

	protected static <T> void assertIs(T actual, Consumer<T> consumer) {
		consumer.accept(actual);
	}

	protected static Consumer<String> blank() {
		return string -> Assert.assertEquals(StringPool.BLANK, string);
	}

	protected static Consumer<List<?>> emptyList() {
		return list -> Assert.assertEquals("[]", String.valueOf(list));
	}

	protected static Consumer<Map<String, ?>> emptyMap() {
		return map -> Assert.assertEquals("{}", String.valueOf(map));
	}

	protected static Consumer<Stream<?>> emptyStream() {
		return stream -> Assert.assertEquals(
			"[]",
			String.valueOf(
				stream.map(
					String::valueOf
				).collect(
					Collectors.toList()
				)));
	}

	protected static Consumer<Object> instanceOf(Class<?> clazz) {
		return object -> Assert.assertTrue(clazz.isInstance(object));
	}

	protected static Consumer<Object> nullValue() {
		return object -> Assert.assertNull(object);
	}

	protected static Consumer<Object> same(Object expected) {
		return actual -> Assert.assertSame(expected, actual);
	}

	protected static Consumer<Integer> zeroInt() {
		return value -> Assert.assertEquals(0, value.intValue());
	}

	protected static Consumer<Long> zeroLong() {
		return value -> Assert.assertEquals(0, value.longValue());
	}

}