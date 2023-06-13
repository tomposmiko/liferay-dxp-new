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

package com.liferay.dynamic.data.mapping.util;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.filter.QueryFilter;
import com.liferay.portal.search.sort.Sort;
import com.liferay.portal.search.sort.SortOrder;

import java.io.Serializable;

import java.util.Locale;

/**
 * @author Alexander Chow
 */
public interface DDMIndexer {

	public static final String DDM_FIELD_ARRAY = "ddmFieldArray";

	public static final String DDM_FIELD_NAME = "ddmFieldName";

	public static final String DDM_FIELD_NAMESPACE = "ddm";

	public static final String DDM_FIELD_PREFIX =
		DDM_FIELD_NAMESPACE + DDMIndexer.DDM_FIELD_SEPARATOR;

	public static final String DDM_FIELD_SEPARATOR =
		StringPool.DOUBLE_UNDERLINE;

	public static final String DDM_VALUE_FIELD_NAME = "ddmValueFieldName";

	public static final String DDM_VALUE_FIELD_NAME_PREFIX = "ddmFieldValue";

	public void addAttributes(
		Document document, DDMStructure ddmStructure,
		DDMFormValues ddmFormValues);

	public default Sort createDDMStructureFieldSort(
			DDMStructure ddmStructure, String fieldName, Locale locale,
			SortOrder sortOrder)
		throws PortalException {

		throw new UnsupportedOperationException();
	}

	public default Sort createDDMStructureFieldSort(
			String ddmStructureFieldName, Locale locale, SortOrder sortOrder)
		throws PortalException {

		throw new UnsupportedOperationException();
	}

	public default QueryFilter createFieldValueQueryFilter(
			DDMStructure ddmStructure, String fieldName, Locale locale,
			Serializable value)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	public QueryFilter createFieldValueQueryFilter(
			String ddmStructureFieldName, Serializable ddmStructureFieldValue,
			Locale locale)
		throws Exception;

	public String encodeName(long ddmStructureId, String fieldName);

	public String encodeName(
		long ddmStructureId, String fieldName, Locale locale);

	public String extractIndexableAttributes(
		DDMStructure ddmStructure, DDMFormValues ddmFormValues, Locale locale);

	public default String getValueFieldName(String indexType) {
		throw new UnsupportedOperationException();
	}

	public default String getValueFieldName(String indexType, Locale locale) {
		throw new UnsupportedOperationException();
	}

	public default boolean isLegacyDDMIndexFieldsEnabled() {
		return false;
	}

}