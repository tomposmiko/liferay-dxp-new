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

package com.liferay.portal.search.solr7.internal.groupby;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.highlight.HighlightUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.search.groupby.GroupByRequest;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.GroupParams;

import org.osgi.service.component.annotations.Component;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
@Component(immediate = true, service = GroupByTranslator.class)
public class DefaultGroupByTranslator implements GroupByTranslator {

	@Override
	public void translate(
		SolrQuery solrQuery, GroupByRequest groupByRequest, Locale locale,
		String[] highlightFieldNames, boolean highlightEnabled,
		boolean highlightRequireFieldMatch, int highlightFragmentSize,
		int highlightSnippetSize) {

		configureGroups(solrQuery, groupByRequest);

		if (highlightEnabled) {
			addHighlights(
				solrQuery, highlightFragmentSize, highlightRequireFieldMatch,
				highlightSnippetSize, highlightFieldNames, locale);
		}
	}

	protected void addDocsSort(
		SolrQuery solrQuery, String sortFieldName, SolrQuery.ORDER order) {

		solrQuery.set(
			GroupParams.GROUP_SORT, sortFieldName + StringPool.SPACE + order);
	}

	protected void addDocsSorts(SolrQuery solrQuery, Sort[] sorts) {
		addSorts(solrQuery, sorts, true);
	}

	protected void addHighlightedField(
		SolrQuery solrQuery, Locale locale, String fieldName) {

		solrQuery.addHighlightField(fieldName);

		String localizedFieldName = Field.getLocalizedName(locale, fieldName);

		solrQuery.addHighlightField(localizedFieldName);
	}

	protected void addHighlights(
		SolrQuery solrQuery, int highlightFragmentSize,
		boolean highlightRequireFieldMatch, int highlightSnippetSize,
		String[] highlightFieldNames, Locale locale) {

		solrQuery.setHighlight(true);
		solrQuery.setHighlightFragsize(highlightFragmentSize);
		solrQuery.setHighlightRequireFieldMatch(highlightRequireFieldMatch);
		solrQuery.setHighlightSimplePost(HighlightUtil.HIGHLIGHT_TAG_CLOSE);
		solrQuery.setHighlightSimplePre(HighlightUtil.HIGHLIGHT_TAG_OPEN);
		solrQuery.setHighlightSnippets(highlightSnippetSize);

		for (String highlightFieldName : highlightFieldNames) {
			addHighlightedField(solrQuery, locale, highlightFieldName);
		}
	}

	protected void addSorts(
		SolrQuery solrQuery, Sort[] sorts, boolean sortDocs) {

		if (ArrayUtil.isEmpty(sorts)) {
			return;
		}

		Set<String> sortFieldNames = new HashSet<>();

		for (Sort sort : sorts) {
			if (sort == null) {
				continue;
			}

			String sortFieldName = Field.getSortFieldName(
				sort, _SOLR_SCORE_FIELD);

			if (sortFieldNames.contains(sortFieldName)) {
				continue;
			}

			sortFieldNames.add(sortFieldName);

			SolrQuery.ORDER order = SolrQuery.ORDER.asc;

			if (sort.isReverse() || sortFieldName.equals(_SOLR_SCORE_FIELD)) {
				order = SolrQuery.ORDER.desc;
			}

			if (sortDocs) {
				addDocsSort(solrQuery, sortFieldName, order);
			}
			else {
				addTermsSort(solrQuery, sortFieldName, order);
			}
		}
	}

	protected void addTermsSort(
		SolrQuery solrQuery, String sortFieldName, SolrQuery.ORDER order) {

		solrQuery.addSort(new SolrQuery.SortClause(sortFieldName, order));
	}

	protected void addTermsSorts(SolrQuery solrQuery, Sort[] sorts) {
		addSorts(solrQuery, sorts, false);
	}

	protected void configureGroups(
		SolrQuery solrQuery, GroupByRequest groupByRequest) {

		solrQuery.set(GroupParams.GROUP, true);
		solrQuery.set(GroupParams.GROUP_FIELD, groupByRequest.getField());
		solrQuery.set(GroupParams.GROUP_FORMAT, "grouped");
		solrQuery.set(GroupParams.GROUP_TOTAL_COUNT, true);

		int termsStart = GetterUtil.getInteger(groupByRequest.getTermsStart());

		if (termsStart > 0) {
			solrQuery.set("start", termsStart);
		}

		int termsSize = GetterUtil.getInteger(groupByRequest.getTermsSize());

		if (termsSize > 0) {
			solrQuery.set("rows", termsSize);
		}

		addTermsSorts(solrQuery, groupByRequest.getTermsSorts());

		int docsStart = GetterUtil.getInteger(groupByRequest.getDocsStart());

		if (docsStart > 0) {
			solrQuery.set(GroupParams.GROUP_OFFSET, docsStart);
		}

		int docsSize = GetterUtil.getInteger(groupByRequest.getDocsSize());

		if (docsSize > 0) {
			solrQuery.set(GroupParams.GROUP_LIMIT, docsSize);
		}

		addDocsSorts(solrQuery, groupByRequest.getDocsSorts());
	}

	private static final String _SOLR_SCORE_FIELD = "score";

}