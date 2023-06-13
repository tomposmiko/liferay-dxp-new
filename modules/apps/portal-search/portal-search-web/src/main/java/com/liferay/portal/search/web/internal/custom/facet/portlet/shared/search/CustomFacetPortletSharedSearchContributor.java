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

package com.liferay.portal.search.web.internal.custom.facet.portlet.shared.search;

import com.liferay.dynamic.data.mapping.util.DDMIndexer;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.facet.custom.CustomFacetSearchContributor;
import com.liferay.portal.search.facet.nested.NestedFacetSearchContributor;
import com.liferay.portal.search.web.internal.custom.facet.constants.CustomFacetPortletKeys;
import com.liferay.portal.search.web.internal.custom.facet.portlet.CustomFacetPortletPreferences;
import com.liferay.portal.search.web.internal.custom.facet.portlet.CustomFacetPortletPreferencesImpl;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchContributor;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchSettings;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Wade Cao
 */
@Component(
	property = "javax.portlet.name=" + CustomFacetPortletKeys.CUSTOM_FACET,
	service = PortletSharedSearchContributor.class
)
public class CustomFacetPortletSharedSearchContributor
	implements PortletSharedSearchContributor {

	@Override
	public void contribute(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		CustomFacetPortletPreferences customFacetPortletPreferences =
			new CustomFacetPortletPreferencesImpl(
				portletSharedSearchSettings.getPortletPreferencesOptional());

		Optional<String> fieldToAggregateOptional =
			customFacetPortletPreferences.getAggregationFieldOptional();

		if (!fieldToAggregateOptional.isPresent()) {
			return;
		}

		String fieldToAggregate = fieldToAggregateOptional.get();

		if (!ddmIndexer.isLegacyDDMIndexFieldsEnabled() &&
			fieldToAggregate.startsWith(DDMIndexer.DDM_FIELD_ARRAY)) {

			_contributeWithDDMFieldArray(
				customFacetPortletPreferences, fieldToAggregate,
				portletSharedSearchSettings);
		}
		else if (!ddmIndexer.isLegacyDDMIndexFieldsEnabled() &&
				 fieldToAggregate.startsWith(DDMIndexer.DDM_FIELD_PREFIX)) {

			_contributeWithDDMField(
				customFacetPortletPreferences, fieldToAggregate,
				portletSharedSearchSettings);
		}
		else if (fieldToAggregate.startsWith("nestedFieldArray")) {
			_contributeWithNestedFieldArray(
				customFacetPortletPreferences, fieldToAggregate,
				portletSharedSearchSettings);
		}
		else {
			_contributeWithCustomFacet(
				customFacetPortletPreferences, fieldToAggregate,
				portletSharedSearchSettings);
		}
	}

	@Reference
	protected CustomFacetSearchContributor customFacetSearchContributor;

	@Reference
	protected DDMIndexer ddmIndexer;

	@Reference
	protected NestedFacetSearchContributor nestedFacetSearchContributor;

	private void _contributeWithCustomFacet(
		CustomFacetPortletPreferences customFacetPortletPreferences,
		String fieldToAggregate,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		customFacetSearchContributor.contribute(
			portletSharedSearchSettings.getFederatedSearchRequestBuilder(
				customFacetPortletPreferences.getFederatedSearchKey()),
			customFacetBuilder -> customFacetBuilder.aggregationName(
				portletSharedSearchSettings.getPortletId()
			).fieldToAggregate(
				fieldToAggregate
			).frequencyThreshold(
				customFacetPortletPreferences.getFrequencyThreshold()
			).maxTerms(
				customFacetPortletPreferences.getMaxTerms()
			).selectedValues(
				portletSharedSearchSettings.getParameterValues(
					_getParameterName(customFacetPortletPreferences))
			));
	}

	private void _contributeWithDDMField(
		CustomFacetPortletPreferences customFacetPortletPreferences,
		String fieldToAggregate,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		String[] ddmFieldParts = StringUtil.split(
			fieldToAggregate, DDMIndexer.DDM_FIELD_SEPARATOR);

		if (ddmFieldParts.length != 4) {
			return;
		}

		_contributeWithNestedFieldFacet(
			customFacetPortletPreferences,
			ddmIndexer.getValueFieldName(
				ddmFieldParts[1], _getSuffixLocale(ddmFieldParts[3])),
			DDMIndexer.DDM_FIELD_NAME, fieldToAggregate,
			DDMIndexer.DDM_FIELD_ARRAY, portletSharedSearchSettings);
	}

	private void _contributeWithDDMFieldArray(
		CustomFacetPortletPreferences customFacetPortletPreferences,
		String fieldToAggregate,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		String[] ddmFieldArrayParts = StringUtil.split(
			fieldToAggregate, StringPool.PERIOD);

		if (ddmFieldArrayParts.length != 3) {
			return;
		}

		_contributeWithNestedFieldFacet(
			customFacetPortletPreferences, ddmFieldArrayParts[2],
			DDMIndexer.DDM_FIELD_NAME, ddmFieldArrayParts[1],
			DDMIndexer.DDM_FIELD_ARRAY, portletSharedSearchSettings);
	}

	private void _contributeWithNestedFieldArray(
		CustomFacetPortletPreferences customFacetPortletPreferences,
		String fieldToAggregate,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		String[] fieldToAggregrateParts = StringUtil.split(
			fieldToAggregate, StringPool.PERIOD);

		if (fieldToAggregrateParts.length != 3) {
			return;
		}

		_contributeWithNestedFieldFacet(
			customFacetPortletPreferences, fieldToAggregrateParts[2],
			"fieldName", fieldToAggregrateParts[1], "nestedFieldArray",
			portletSharedSearchSettings);
	}

	private void _contributeWithNestedFieldFacet(
		CustomFacetPortletPreferences customFacetPortletPreferences,
		String fieldToAggregate, String filterField, String filterValue,
		String path, PortletSharedSearchSettings portletSharedSearchSettings) {

		nestedFacetSearchContributor.contribute(
			portletSharedSearchSettings.getFederatedSearchRequestBuilder(
				customFacetPortletPreferences.getFederatedSearchKey()),
			nestedFacetBuilder -> nestedFacetBuilder.aggregationName(
				portletSharedSearchSettings.getPortletId()
			).fieldToAggregate(
				StringBundler.concat(path, StringPool.PERIOD, fieldToAggregate)
			).filterField(
				StringBundler.concat(path, StringPool.PERIOD, filterField)
			).filterValue(
				filterValue
			).frequencyThreshold(
				customFacetPortletPreferences.getFrequencyThreshold()
			).maxTerms(
				customFacetPortletPreferences.getMaxTerms()
			).path(
				path
			).selectedValues(
				portletSharedSearchSettings.getParameterValues(
					_getParameterName(customFacetPortletPreferences))
			));
	}

	private String _getParameterName(
		CustomFacetPortletPreferences customFacetPortletPreferences) {

		Optional<String> optional = Stream.of(
			customFacetPortletPreferences.getParameterNameOptional(),
			customFacetPortletPreferences.getAggregationFieldOptional()
		).filter(
			Optional::isPresent
		).map(
			Optional::get
		).findFirst();

		return optional.orElse("customfield");
	}

	private Locale _getSuffixLocale(String string) {
		for (Locale availableLocale : _language.getAvailableLocales()) {
			String availableLanguageId = _language.getLanguageId(
				availableLocale);

			if (string.endsWith(availableLanguageId)) {
				return availableLocale;
			}
		}

		return null;
	}

	@Reference
	private Language _language;

}