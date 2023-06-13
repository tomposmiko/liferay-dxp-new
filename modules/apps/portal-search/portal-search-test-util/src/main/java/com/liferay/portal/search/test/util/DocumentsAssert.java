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

package com.liferay.portal.search.test.util;

import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.document.Document;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;

/**
 * @author André de Oliveira
 */
public class DocumentsAssert {

	public static void assertCount(
		String message, com.liferay.portal.kernel.search.Document[] documents,
		String fieldName, int expectedCount) {

		if (documents.length == expectedCount) {
			return;
		}

		List<String> actualValues = _getFieldValueStrings(fieldName, documents);

		Assert.assertEquals(
			message + "->" + actualValues, expectedCount, documents.length);
	}

	public static void assertValues(
		String message, com.liferay.portal.kernel.search.Document[] documents,
		String fieldName, List<String> expectedValues) {

		assertValues(
			message, documents, fieldName, String.valueOf(expectedValues));
	}

	public static void assertValues(
		String message, com.liferay.portal.kernel.search.Document[] documents,
		String fieldName, String expected) {

		List<String> actualValues = _getFieldValueStrings(fieldName, documents);

		Assert.assertEquals(
			_getMessage(message, documents, actualValues), expected,
			String.valueOf(actualValues));
	}

	public static void assertValues(
		String message, List<Document> documentsList, String fieldName,
		String expected) {

		Document[] documents = documentsList.toArray(new Document[0]);

		List<String> actualValues = _getFieldValueStrings(fieldName, documents);

		Assert.assertEquals(
			_getMessage(message, documents, actualValues), expected,
			String.valueOf(actualValues));
	}

	public static void assertValuesIgnoreRelevance(
		String message, com.liferay.portal.kernel.search.Document[] documents,
		String fieldName, List<String> expectedValues) {

		List<String> actualValues = _getFieldValueStrings(fieldName, documents);

		Assert.assertEquals(
			_getMessage(message, documents, actualValues),
			_getSortedString(expectedValues), _getSortedString(actualValues));
	}

	public static void assertValuesIgnoreRelevance(
		String message, List<Document> documentsList, String fieldName,
		List<String> expectedValues) {

		Document[] documents = documentsList.toArray(new Document[0]);

		List<String> actualValues = _getFieldValueStrings(fieldName, documents);

		Assert.assertEquals(
			_getMessage(message, documents, actualValues),
			_getSortedString(expectedValues), _getSortedString(actualValues));
	}

	public static void assertValuesIgnoreRelevance(
		String message, List<Document> documentsList, String fieldName,
		String expected) {

		Document[] documents = documentsList.toArray(new Document[0]);

		List<String> actualValues = _getFieldValueStrings(fieldName, documents);

		Assert.assertEquals(
			_getMessage(message, documents, actualValues), expected,
			_getSortedString(actualValues));
	}

	private static List<Object> _getFieldValues(
		String fieldName, com.liferay.portal.kernel.search.Document document) {

		return Arrays.asList((Object[])document.getValues(fieldName));
	}

	private static String _getFieldValueString(List<Object> fieldValues) {
		if (fieldValues.isEmpty()) {
			return StringPool.BLANK;
		}

		if (fieldValues.size() == 1) {
			return String.valueOf(fieldValues.get(0));
		}

		return _getSortedString(
			TransformUtil.transform(fieldValues, String::valueOf));
	}

	private static List<String> _getFieldValueStrings(
		String fieldName,
		com.liferay.portal.kernel.search.Document... documents) {

		return TransformUtil.transformToList(
			documents,
			document -> _getFieldValueString(
				_getFieldValues(fieldName, document)));
	}

	private static List<String> _getFieldValueStrings(
		String fieldName, Document... documents) {

		return TransformUtil.transformToList(
			documents,
			document -> _getFieldValueString(document.getValues(fieldName)));
	}

	private static String _getMessage(
		String message, Object[] objects, Collection<String> values) {

		return StringBundler.concat(
			message, "->", StringUtil.merge(objects), "->", values);
	}

	private static String _getSortedString(List<String> strings) {
		Collections.sort(strings);

		return strings.toString();
	}

}