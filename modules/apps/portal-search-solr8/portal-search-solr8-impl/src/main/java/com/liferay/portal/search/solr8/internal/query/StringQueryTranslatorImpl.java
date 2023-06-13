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

package com.liferay.portal.search.solr8.internal.query;

import com.liferay.portal.kernel.search.generic.StringQuery;

import java.util.Objects;

import org.apache.lucene.search.Query;

import org.osgi.service.component.annotations.Component;

/**
 * @author Michael C. Han
 */
@Component(service = StringQueryTranslator.class)
public class StringQueryTranslatorImpl implements StringQueryTranslator {

	@Override
	public Query translate(StringQuery stringQuery) {
		return new Query() {

			@Override
			public boolean equals(Object object) {
				String query = stringQuery.getQuery();

				return query.equals(object);
			}

			@Override
			public int hashCode() {
				return Objects.hashCode(stringQuery.getQuery());
			}

			@Override
			public String toString(String field) {
				return stringQuery.getQuery();
			}

		};
	}

}