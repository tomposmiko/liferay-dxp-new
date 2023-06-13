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

package com.liferay.document.library.internal.search.spi.model.query.contributor;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.generic.MatchQuery;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.query.QueryHelper;
import com.liferay.portal.search.spi.model.query.contributor.KeywordQueryContributor;
import com.liferay.portal.search.spi.model.query.contributor.helper.KeywordQueryContributorHelper;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bryan Engler
 */
@Component(
	property = "indexer.class.name=com.liferay.document.library.kernel.model.DLFileEntry",
	service = KeywordQueryContributor.class
)
public class DLFileEntryKeywordQueryContributor
	implements KeywordQueryContributor {

	@Override
	public void contribute(
		String keywords, BooleanQuery booleanQuery,
		KeywordQueryContributorHelper keywordQueryContributorHelper) {

		SearchContext searchContext =
			keywordQueryContributorHelper.getSearchContext();

		if (Validator.isNull(keywords)) {
			queryHelper.addSearchTerm(
				booleanQuery, searchContext, Field.DESCRIPTION, false);
			queryHelper.addSearchTerm(
				booleanQuery, searchContext, Field.USER_NAME, false);
		}

		queryHelper.addSearchTerm(
			booleanQuery, searchContext, "ddmContent", false);
		queryHelper.addSearchTerm(
			booleanQuery, searchContext, "extension", false);
		queryHelper.addSearchTerm(
			booleanQuery, searchContext, "fileEntryTypeId", false);
		queryHelper.addSearchLocalizedTerm(
			booleanQuery, searchContext, Field.CONTENT, false);
		queryHelper.addSearchTerm(
			booleanQuery, searchContext, Field.TITLE, false);

		if (Validator.isNotNull(keywords)) {
			try {
				booleanQuery.add(
					_getMatchQuery(
						"fileExtension", keywords,
						MatchQuery.Type.PHRASE_PREFIX),
					BooleanClauseOccur.SHOULD);
			}
			catch (ParseException parseException) {
				throw new SystemException(parseException);
			}
		}
	}

	@Reference
	protected QueryHelper queryHelper;

	private MatchQuery _getMatchQuery(
		String field, String keywords, MatchQuery.Type phrase) {

		MatchQuery matchPhraseQuery = new MatchQuery(field, keywords);

		matchPhraseQuery.setType(phrase);

		return matchPhraseQuery;
	}

}