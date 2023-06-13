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

package com.liferay.portal.search.web.internal.modified.facet.display.context.builder;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.search.facet.Facet;
import com.liferay.portal.kernel.search.facet.collector.FacetCollector;
import com.liferay.portal.kernel.search.facet.collector.TermCollector;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.DateFormatFactory;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.web.internal.facet.display.context.BucketDisplayContext;
import com.liferay.portal.search.web.internal.modified.facet.builder.DateRangeFactory;
import com.liferay.portal.search.web.internal.modified.facet.configuration.ModifiedFacetPortletInstanceConfiguration;
import com.liferay.portal.search.web.internal.modified.facet.display.context.ModifiedFacetCalendarDisplayContext;
import com.liferay.portal.search.web.internal.modified.facet.display.context.ModifiedFacetDisplayContext;
import com.liferay.portal.search.web.internal.util.comparator.BucketDisplayContextComparatorFactoryUtil;

import java.io.Serializable;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import javax.portlet.RenderRequest;

/**
 * @author Lino Alves
 * @author Adam Brandizzi
 */
public class ModifiedFacetDisplayContextBuilder implements Serializable {

	public ModifiedFacetDisplayContextBuilder(
			DateFormatFactory dateFormatFactory, RenderRequest renderRequest)
		throws ConfigurationException {

		_dateFormatFactory = dateFormatFactory;

		_dateRangeFactory = new DateRangeFactory(dateFormatFactory);

		_themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		_modifiedFacetPortletInstanceConfiguration =
			portletDisplay.getPortletInstanceConfiguration(
				ModifiedFacetPortletInstanceConfiguration.class);
	}

	public ModifiedFacetDisplayContext build() {
		ModifiedFacetDisplayContext modifiedFacetDisplayContext =
			new ModifiedFacetDisplayContext();

		modifiedFacetDisplayContext.setCalendarDisplayContext(
			_buildCalendarDisplayContext());

		if ((_dateFormatFactory != null) && (_dateRangeFactory != null)) {
			modifiedFacetDisplayContext.setCustomRangeBucketDisplayContext(
				_buildCustomRangeModifiedTermDisplayContext());
		}

		modifiedFacetDisplayContext.setBucketDisplayContexts(
			_buildTermDisplayContexts());
		modifiedFacetDisplayContext.setDefaultBucketDisplayContext(
			_buildDefaultBucketDisplayContext());
		modifiedFacetDisplayContext.setDisplayStyleGroupId(
			getDisplayStyleGroupId());
		modifiedFacetDisplayContext.
			setModifiedFacetPortletInstanceConfiguration(
				_modifiedFacetPortletInstanceConfiguration);
		modifiedFacetDisplayContext.setNothingSelected(isNothingSelected());
		modifiedFacetDisplayContext.setPaginationStartParameterName(
			_paginationStartParameterName);
		modifiedFacetDisplayContext.setParameterName(_parameterName);
		modifiedFacetDisplayContext.setRenderNothing(isRenderNothing());

		return modifiedFacetDisplayContext;
	}

	public void setCurrentURL(String currentURL) {
		_currentURL = currentURL;
	}

	public void setFacet(Facet facet) {
		_facet = facet;
	}

	public void setFrequenciesVisible(boolean frequenciesVisible) {
		_frequenciesVisible = frequenciesVisible;
	}

	public void setFrequencyThreshold(int frequencyThreshold) {
		_frequencyThreshold = frequencyThreshold;
	}

	public void setFromParameterValue(String from) {
		_from = from;
	}

	public void setLocale(Locale locale) {
		_locale = locale;
	}

	public void setOrder(String order) {
		_order = order;
	}

	public void setPaginationStartParameterName(
		String paginationStartParameterName) {

		_paginationStartParameterName = paginationStartParameterName;
	}

	public void setParameterName(String parameterName) {
		_parameterName = parameterName;
	}

	public void setParameterValues(String... parameterValues) {
		_selectedRanges = Arrays.asList(
			Objects.requireNonNull(parameterValues));
	}

	public void setTimeZone(TimeZone timeZone) {
		_timeZone = timeZone;
	}

	public void setToParameterValue(String to) {
		_to = to;
	}

	public void setTotalHits(int totalHits) {
		_totalHits = totalHits;
	}

	protected long getDisplayStyleGroupId() {
		long displayStyleGroupId =
			_modifiedFacetPortletInstanceConfiguration.displayStyleGroupId();

		if (displayStyleGroupId <= 0) {
			displayStyleGroupId = _themeDisplay.getScopeGroupId();
		}

		return displayStyleGroupId;
	}

	protected int getFrequency(TermCollector termCollector) {
		if (termCollector != null) {
			return termCollector.getFrequency();
		}

		return 0;
	}

	protected TermCollector getTermCollector(String range) {
		if (_facet == null) {
			return null;
		}

		FacetCollector facetCollector = _facet.getFacetCollector();

		if (facetCollector == null) {
			return null;
		}

		return facetCollector.getTermCollector(range);
	}

	protected boolean isNothingSelected() {
		if (!_selectedRanges.isEmpty() ||
			(!Validator.isBlank(_from) && !Validator.isBlank(_to))) {

			return false;
		}

		return true;
	}

	protected boolean isRenderNothing() {
		if (_totalHits > 0) {
			return false;
		}

		return isNothingSelected();
	}

	private ModifiedFacetCalendarDisplayContext _buildCalendarDisplayContext() {
		ModifiedFacetCalendarDisplayContextBuilder
			modifiedFacetCalendarDisplayContextBuilder =
				new ModifiedFacetCalendarDisplayContextBuilder();

		for (String selectedRange : _selectedRanges) {
			if (selectedRange.startsWith(StringPool.OPEN_CURLY_BRACE)) {
				modifiedFacetCalendarDisplayContextBuilder.setRangeString(
					selectedRange);
			}
		}

		modifiedFacetCalendarDisplayContextBuilder.setFrom(_from);
		modifiedFacetCalendarDisplayContextBuilder.setLocale(_locale);
		modifiedFacetCalendarDisplayContextBuilder.setTimeZone(_timeZone);
		modifiedFacetCalendarDisplayContextBuilder.setTo(_to);

		return modifiedFacetCalendarDisplayContextBuilder.build();
	}

	private BucketDisplayContext _buildCustomRangeModifiedTermDisplayContext() {
		boolean selected = _isCustomRangeSelected();

		BucketDisplayContext bucketDisplayContext = new BucketDisplayContext();

		bucketDisplayContext.setBucketText("custom-range");
		bucketDisplayContext.setFilterValue(_getCustomRangeURL());
		bucketDisplayContext.setFrequency(
			getFrequency(_getCustomRangeTermCollector(selected)));
		bucketDisplayContext.setFrequencyVisible(_frequenciesVisible);
		bucketDisplayContext.setSelected(selected);

		return bucketDisplayContext;
	}

	private BucketDisplayContext _buildDefaultBucketDisplayContext() {
		if (_facet == null) {
			return null;
		}

		FacetConfiguration facetConfiguration = _facet.getFacetConfiguration();

		String label = facetConfiguration.getLabel();

		BucketDisplayContext bucketDisplayContext = new BucketDisplayContext();

		bucketDisplayContext.setBucketText(label);
		bucketDisplayContext.setSelected(true);

		return bucketDisplayContext;
	}

	private BucketDisplayContext _buildTermDisplayContext(
		String label, String range) {

		BucketDisplayContext bucketDisplayContext = new BucketDisplayContext();

		bucketDisplayContext.setBucketText(label);
		bucketDisplayContext.setFilterValue(_getLabeledRangeURL(label));
		bucketDisplayContext.setFrequency(
			getFrequency(getTermCollector(range)));
		bucketDisplayContext.setFrequencyVisible(_frequenciesVisible);
		bucketDisplayContext.setSelected(_selectedRanges.contains(label));

		return bucketDisplayContext;
	}

	private List<BucketDisplayContext> _buildTermDisplayContexts() {
		JSONArray rangesJSONArray = _getRangesJSONArray();

		if (rangesJSONArray == null) {
			return null;
		}

		List<BucketDisplayContext> bucketDisplayContexts = new ArrayList<>();

		for (int i = 0; i < rangesJSONArray.length(); i++) {
			JSONObject jsonObject = rangesJSONArray.getJSONObject(i);

			String range = jsonObject.getString("range");

			if ((_frequencyThreshold > 0) &&
				(_frequencyThreshold > getFrequency(getTermCollector(range)))) {

				continue;
			}

			bucketDisplayContexts.add(
				_buildTermDisplayContext(jsonObject.getString("label"), range));
		}

		if (!_order.equals("OrderHitsDesc")) {
			bucketDisplayContexts.sort(
				BucketDisplayContextComparatorFactoryUtil.
					getBucketDisplayContextComparator(_order));
		}

		return bucketDisplayContexts;
	}

	private TermCollector _getCustomRangeTermCollector(boolean selected) {
		if (!selected) {
			return null;
		}

		FacetCollector facetCollector = _facet.getFacetCollector();

		return facetCollector.getTermCollector(
			_dateRangeFactory.getRangeString(_from, _to));
	}

	private String _getCustomRangeURL() {
		DateFormat format = _dateFormatFactory.getSimpleDateFormat(
			"yyyy-MM-dd");

		Calendar calendar = CalendarFactoryUtil.getCalendar(_timeZone);

		String to = format.format(calendar.getTime());

		calendar.add(Calendar.DATE, -1);

		String from = format.format(calendar.getTime());

		String rangeURL = HttpComponentsUtil.removeParameter(
			_currentURL, "modified");

		rangeURL = HttpComponentsUtil.removeParameter(
			rangeURL, _paginationStartParameterName);

		rangeURL = HttpComponentsUtil.setParameter(
			rangeURL, "modifiedFrom", from);

		return HttpComponentsUtil.setParameter(rangeURL, "modifiedTo", to);
	}

	private String _getLabeledRangeURL(String label) {
		String rangeURL = HttpComponentsUtil.removeParameter(
			_currentURL, "modifiedFrom");

		rangeURL = HttpComponentsUtil.removeParameter(rangeURL, "modifiedTo");

		rangeURL = HttpComponentsUtil.removeParameter(
			rangeURL, _paginationStartParameterName);

		return HttpComponentsUtil.setParameter(rangeURL, "modified", label);
	}

	private JSONArray _getRangesJSONArray() {
		if (_facet == null) {
			return null;
		}

		FacetConfiguration facetConfiguration = _facet.getFacetConfiguration();

		JSONObject dataJSONObject = facetConfiguration.getData();

		return dataJSONObject.getJSONArray("ranges");
	}

	private boolean _isCustomRangeSelected() {
		if (Validator.isBlank(_from) && Validator.isBlank(_to)) {
			return false;
		}

		return true;
	}

	private String _currentURL;
	private final DateFormatFactory _dateFormatFactory;
	private final DateRangeFactory _dateRangeFactory;
	private Facet _facet;
	private boolean _frequenciesVisible;
	private int _frequencyThreshold;
	private String _from;
	private Locale _locale;
	private final ModifiedFacetPortletInstanceConfiguration
		_modifiedFacetPortletInstanceConfiguration;
	private String _order;
	private String _paginationStartParameterName;
	private String _parameterName;
	private List<String> _selectedRanges = Collections.emptyList();
	private final ThemeDisplay _themeDisplay;
	private TimeZone _timeZone;
	private String _to;
	private int _totalHits;

}