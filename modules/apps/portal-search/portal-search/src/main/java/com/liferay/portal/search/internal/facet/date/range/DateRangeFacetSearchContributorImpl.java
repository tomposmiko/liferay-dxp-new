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

package com.liferay.portal.search.internal.facet.date.range;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.search.facet.Facet;
import com.liferay.portal.search.facet.date.range.DateRangeFacetFactory;
import com.liferay.portal.search.facet.date.range.DateRangeFacetSearchContributor;
import com.liferay.portal.search.internal.facet.DateRangeFacetImpl;
import com.liferay.portal.search.searcher.SearchRequestBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(service = DateRangeFacetSearchContributor.class)
public class DateRangeFacetSearchContributorImpl
	implements DateRangeFacetSearchContributor {

	@Override
	public void contribute(
		SearchRequestBuilder searchRequestBuilder,
		Consumer<DateRangeFacetBuilder> dateRangeFacetBuilderConsumer) {

		Facet facet = searchRequestBuilder.withSearchContextGet(
			searchContext -> {
				DateRangeFacetBuilderImpl dateRangeFacetBuilderImpl =
					new DateRangeFacetBuilderImpl(searchContext);

				dateRangeFacetBuilderConsumer.accept(dateRangeFacetBuilderImpl);

				return dateRangeFacetBuilderImpl.build();
			});

		searchRequestBuilder.withFacetContext(
			facetContext -> facetContext.addFacet(facet));
	}

	@Reference
	private DateRangeFacetFactory _dateRangeFacetFactory;

	@Reference
	private JSONFactory _jsonFactory;

	private class DateRangeFacetBuilderImpl implements DateRangeFacetBuilder {

		public DateRangeFacetBuilderImpl(SearchContext searchContext) {
			_searchContext = searchContext;
		}

		@Override
		public DateRangeFacetBuilder aggregationName(String aggregationName) {
			_aggregationName = aggregationName;

			return this;
		}

		public Facet build() {
			DateRangeFacetImpl dateRangeFacetImpl =
				(DateRangeFacetImpl)_dateRangeFacetFactory.newInstance(
					_searchContext);

			dateRangeFacetImpl.setAggregationName(_aggregationName);
			dateRangeFacetImpl.setFacetConfiguration(
				_buildFacetConfiguration(dateRangeFacetImpl));
			dateRangeFacetImpl.setFieldName(_field);
			dateRangeFacetImpl.select(_getSelectedRanges());

			return dateRangeFacetImpl;
		}

		@Override
		public DateRangeFacetBuilder field(String field) {
			_field = field;

			return this;
		}

		@Override
		public DateRangeFacetBuilder format(String format) {
			_format = format;

			return this;
		}

		@Override
		public DateRangeFacetBuilder frequencyThreshold(
			int frequencyThreshold) {

			_frequencyThreshold = frequencyThreshold;

			return this;
		}

		@Override
		public DateRangeFacetBuilder maxTerms(int maxTerms) {
			_maxTerms = maxTerms;

			return this;
		}

		@Override
		public DateRangeFacetBuilder order(String order) {
			_order = order;

			return this;
		}

		@Override
		public DateRangeFacetBuilder rangesJSONArray(
			JSONArray rangesJSONArray) {

			_rangesJSONArray = rangesJSONArray;

			return this;
		}

		@Override
		public DateRangeFacetBuilder selectedRanges(String... selectedRanges) {
			_selectedRanges = selectedRanges;

			return this;
		}

		private FacetConfiguration _buildFacetConfiguration(Facet facet) {
			FacetConfiguration facetConfiguration = new FacetConfiguration();

			facetConfiguration.setFieldName(facet.getFieldName());
			facetConfiguration.setLabel("any-time");
			facetConfiguration.setOrder(_order);
			facetConfiguration.setStatic(false);
			facetConfiguration.setWeight(1.0);

			JSONObject jsonObject = facetConfiguration.getData();

			jsonObject.put(
				"format", _format
			).put(
				"frequencyThreshold", _frequencyThreshold
			).put(
				"maxTerms", _maxTerms
			).put(
				"ranges", _getRangesJSONArray()
			);

			return facetConfiguration;
		}

		private JSONArray _getRangesJSONArray() {
			if (_rangesJSONArray == null) {
				return _jsonFactory.createJSONArray();
			}

			return _rangesJSONArray;
		}

		private Map<String, String> _getRangesMap(JSONArray rangesJSONArray) {
			Map<String, String> rangesMap = new HashMap<>();

			for (int i = 0; i < rangesJSONArray.length(); i++) {
				JSONObject rangeJSONObject = rangesJSONArray.getJSONObject(i);

				rangesMap.put(
					rangeJSONObject.getString("label"),
					rangeJSONObject.getString("range"));
			}

			return rangesMap;
		}

		private String[] _getSelectedRanges() {
			List<String> ranges = new ArrayList<>();

			if (!ArrayUtil.isEmpty(_selectedRanges)) {
				Map<String, String> rangesMap = _getRangesMap(_rangesJSONArray);

				for (String selectedRange : _selectedRanges) {
					if (rangesMap.containsValue(selectedRange)) {
						ranges.add(selectedRange);
					}
				}
			}

			return ranges.toArray(new String[0]);
		}

		private String _aggregationName;
		private String _field;
		private String _format;
		private int _frequencyThreshold;
		private int _maxTerms;
		private String _order;
		private JSONArray _rangesJSONArray;
		private final SearchContext _searchContext;
		private String[] _selectedRanges;

	}

}