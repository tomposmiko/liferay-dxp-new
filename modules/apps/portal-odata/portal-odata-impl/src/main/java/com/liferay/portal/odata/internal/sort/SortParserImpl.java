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

package com.liferay.portal.odata.internal.sort;

import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.odata.entity.EntityField;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.odata.sort.InvalidSortException;
import com.liferay.portal.odata.sort.SortField;
import com.liferay.portal.odata.sort.SortParser;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Parses {@code Sort} strings. This class uses a model to create a {@code
 * SortField} list.
 *
 * @author Cristina González
 */
public class SortParserImpl implements SortParser {

	public SortParserImpl(EntityModel entityModel) {
		_entityModel = entityModel;
	}

	/**
	 * Returns a {@code SortField} list from a comma-separated list of field
	 * names and sort directions.
	 *
	 * <p>
	 * Sort directions {@code desc} and {@code asc} can be appended to each sort
	 * field, separated by the {@code :} character. If a sort direction isn't
	 * provided, ascending order is used by default.
	 * </p>
	 *
	 * <p>
	 * For example,
	 * </p>
	 *
	 * <p>
	 * <pre>
	 * <code>
	 * field1,field2,field3
	 * field1:asc,field2:desc,field3
	 * field1:asc,field2,field3:desc
	 * </code>
	 * </pre></p>
	 *
	 * @param  sortString the string to parse
	 * @return the sort field list
	 */
	public List<SortField> parse(String sortString) {
		if (Validator.isNull(sortString)) {
			return Collections.emptyList();
		}

		List<String> list = StringUtil.split(sortString);

		Stream<String> stream = list.stream();

		return stream.map(
			this::getSortFieldOptional
		).flatMap(
			sortFieldOptional -> sortFieldOptional.map(
				Stream::of
			).orElseGet(
				Stream::empty
			)
		).collect(
			Collectors.toList()
		);
	}

	protected Optional<SortField> getSortFieldOptional(String sortString) {
		List<String> list = StringUtil.split(sortString, ':');

		if (list.isEmpty()) {
			return Optional.empty();
		}

		if (list.size() > 2) {
			throw new InvalidSortException(
				"Unable to parse sort string: " + sortString);
		}

		String fieldName = list.get(0);

		boolean ascending = _ASC_DEFAULT;

		if (list.size() > 1) {
			ascending = isAscending(list.get(1));
		}

		Map<String, EntityField> entityFieldsMap =
			_entityModel.getEntityFieldsMap();

		EntityField entityField = entityFieldsMap.get(fieldName);

		if (entityField == null) {
			throw new InvalidSortException(
				"Unable to sort by field: " + fieldName);
		}

		return Optional.of(new SortField(entityField, ascending));
	}

	protected boolean isAscending(String orderBy) {
		if (orderBy == null) {
			return _ASC_DEFAULT;
		}

		if (_ORDER_BY_ASC.equals(
				com.liferay.portal.kernel.util.StringUtil.toLowerCase(
					orderBy))) {

			return true;
		}

		if (_ORDER_BY_DESC.equals(
				com.liferay.portal.kernel.util.StringUtil.toLowerCase(
					orderBy))) {

			return false;
		}

		return _ASC_DEFAULT;
	}

	private static final boolean _ASC_DEFAULT = true;

	private static final String _ORDER_BY_ASC = "asc";

	private static final String _ORDER_BY_DESC = "desc";

	private final EntityModel _entityModel;

}