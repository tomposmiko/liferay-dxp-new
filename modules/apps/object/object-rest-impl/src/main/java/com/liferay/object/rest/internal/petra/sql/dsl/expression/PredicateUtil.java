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

package com.liferay.object.rest.internal.petra.sql.dsl.expression;

import com.liferay.object.rest.internal.odata.entity.v1_0.ObjectEntryEntityModel;
import com.liferay.object.rest.internal.odata.filter.expression.PredicateExpressionVisitorImpl;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.odata.filter.Filter;
import com.liferay.portal.odata.filter.FilterParser;
import com.liferay.portal.odata.filter.FilterParserProvider;
import com.liferay.portal.odata.filter.expression.Expression;

/**
 * @author Marco Leo
 * @author Brian Wing Shun Chan
 */
public class PredicateUtil {

	public static Predicate toPredicate(
		FilterParserProvider filterParserProvider, String filterString,
		long objectDefinitionId,
		ObjectFieldLocalService objectFieldLocalService) {

		if (Validator.isNull(filterString)) {
			return null;
		}

		try {
			FilterParser filterParser = filterParserProvider.provide(
				new ObjectEntryEntityModel(
					objectFieldLocalService.getObjectFields(
						objectDefinitionId)));

			Filter oDataFilter = new Filter(filterParser.parse(filterString));

			Expression expression = oDataFilter.getExpression();

			return (Predicate)expression.accept(
				new PredicateExpressionVisitorImpl(
					objectDefinitionId, objectFieldLocalService));
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(PredicateUtil.class);

}