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

package com.liferay.portal.search.web.internal.sort.portlet.shared.search;

import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.query.Queries;
import com.liferay.portal.search.searcher.SearchRequestBuilder;
import com.liferay.portal.search.sort.FieldSort;
import com.liferay.portal.search.sort.NestedSort;
import com.liferay.portal.search.sort.Sort;
import com.liferay.portal.search.sort.SortBuilder;
import com.liferay.portal.search.sort.SortBuilderFactory;
import com.liferay.portal.search.sort.SortOrder;
import com.liferay.portal.search.sort.Sorts;
import com.liferay.portal.search.web.internal.sort.constants.SortPortletKeys;
import com.liferay.portal.search.web.internal.sort.portlet.SortPortletPreferences;
import com.liferay.portal.search.web.internal.sort.portlet.SortPortletPreferencesImpl;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchContributor;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Wade Cao
 */
@Component(
	property = "javax.portlet.name=" + SortPortletKeys.SORT,
	service = PortletSharedSearchContributor.class
)
public class SortPortletSharedSearchContributor
	implements PortletSharedSearchContributor {

	@Override
	public void contribute(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		SortPortletPreferences sortPortletPreferences =
			new SortPortletPreferencesImpl(
				portletSharedSearchSettings.getPortletPreferencesOptional());

		SearchRequestBuilder searchRequestBuilder =
			portletSharedSearchSettings.getSearchRequestBuilder();

		searchRequestBuilder.sorts(
			_buildSorts(portletSharedSearchSettings, sortPortletPreferences));
	}

	@Reference
	protected DDMIndexer ddmIndexer;

	private Sort _buildDDMFieldArraySort(
			String fieldValue, Locale locale, SortOrder sortOrder)
		throws PortalException {

		String[] ddmFieldArrayParts = StringUtil.split(
			fieldValue, StringPool.PERIOD);

		if (ddmFieldArrayParts.length != 3) {
			return null;
		}

		return ddmIndexer.createDDMStructureFieldSort(
			ddmFieldArrayParts[1], locale, sortOrder);
	}

	private Sort _buildNestedFieldArraySort(
		String fieldValue, SortOrder sortOrder) {

		String[] fieldValueParts = StringUtil.split(
			fieldValue, StringPool.PERIOD);

		if (fieldValueParts.length != 3) {
			return null;
		}

		return _buildNestedFieldSort(
			"fieldName", fieldValueParts[1], "nestedFieldArray",
			fieldValueParts[2], sortOrder);
	}

	private FieldSort _buildNestedFieldSort(
		String filterField, String filterValue, String path, String sortField,
		SortOrder sortOrder) {

		FieldSort fieldSort = _sorts.field(
			StringBundler.concat(path, StringPool.PERIOD, sortField),
			sortOrder);

		NestedSort nestedSort = _sorts.nested(path);

		nestedSort.setFilterQuery(
			_queries.term(
				StringBundler.concat(path, StringPool.PERIOD, filterField),
				filterValue));

		fieldSort.setNestedSort(nestedSort);

		return fieldSort;
	}

	private Sort _buildSort(String fieldValue, Locale locale) {
		SortOrder sortOrder = SortOrder.ASC;

		if (fieldValue.endsWith("+")) {
			fieldValue = fieldValue.substring(0, fieldValue.length() - 1);
		}
		else if (fieldValue.endsWith("-")) {
			fieldValue = fieldValue.substring(0, fieldValue.length() - 1);
			sortOrder = SortOrder.DESC;
		}

		try {
			if (fieldValue.startsWith(DDMIndexer.DDM_FIELD_ARRAY)) {
				return _buildDDMFieldArraySort(fieldValue, locale, sortOrder);
			}
			else if (fieldValue.startsWith(DDMIndexer.DDM_FIELD_PREFIX)) {
				return ddmIndexer.createDDMStructureFieldSort(
					fieldValue, locale, sortOrder);
			}
			else if (fieldValue.startsWith("nestedFieldArray")) {
				return _buildNestedFieldArraySort(fieldValue, sortOrder);
			}
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException);
			}

			return null;
		}

		SortBuilder sortBuilder = _sortBuilderFactory.getSortBuilder();

		return sortBuilder.field(
			fieldValue
		).locale(
			locale
		).sortOrder(
			sortOrder
		).build();
	}

	private Sort[] _buildSorts(
		PortletSharedSearchSettings portletSharedSearchSettings,
		SortPortletPreferences sortPortletPreferences) {

		List<Sort> sorts = new ArrayList<>();

		List<String> fieldValues = _getFieldValues(
			sortPortletPreferences.getParameterName(),
			portletSharedSearchSettings);

		ThemeDisplay themeDisplay =
			portletSharedSearchSettings.getThemeDisplay();

		for (String fieldValue : fieldValues) {
			if (Validator.isBlank(fieldValue)) {
				continue;
			}

			Sort sort = _buildSort(fieldValue, themeDisplay.getLocale());

			if (sort != null) {
				sorts.add(sort);
			}
			else if (_log.isWarnEnabled()) {
				_log.warn(fieldValue + " is an invalid field name");
			}
		}

		return sorts.toArray(new Sort[0]);
	}

	private List<String> _getFieldValues(
		String parameterName,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		String[] fieldValues = portletSharedSearchSettings.getParameterValues(
			parameterName);

		if (ArrayUtil.isNotEmpty(fieldValues)) {
			return Arrays.asList(fieldValues);
		}

		String portletId = portletSharedSearchSettings.getPortletId();
		ThemeDisplay themeDisplay =
			portletSharedSearchSettings.getThemeDisplay();

		try {
			PortletPreferences portletPreferences =
				PortletPreferencesFactoryUtil.getExistingPortletSetup(
					themeDisplay.getLayout(), portletId);

			SortPortletPreferences sortPortletPreferences =
				new SortPortletPreferencesImpl(Optional.of(portletPreferences));

			JSONArray fieldsJSONArray =
				sortPortletPreferences.getFieldsJSONArray();

			if (fieldsJSONArray.length() == 0) {
				return Collections.emptyList();
			}

			JSONObject jsonObject = fieldsJSONArray.getJSONObject(0);

			String fieldValue = jsonObject.getString("field");

			return ListUtil.fromArray(fieldValue);
		}
		catch (PortalException portalException) {
			throw new RuntimeException(portalException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SortPortletSharedSearchContributor.class);

	@Reference
	private Queries _queries;

	@Reference
	private SortBuilderFactory _sortBuilderFactory;

	@Reference
	private Sorts _sorts;

}