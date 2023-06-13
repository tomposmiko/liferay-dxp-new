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

package com.liferay.portal.search.web.internal.user.facet.portlet;

import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.facet.config.FacetConfiguration;
import com.liferay.portal.search.facet.Facet;
import com.liferay.portal.search.facet.user.UserFacetFactory;

/**
 * @author Lino Alves
 */
public class UserFacetBuilder {

	public UserFacetBuilder(UserFacetFactory userFacetFactory) {
		_userFacetFactory = userFacetFactory;
	}

	public Facet build() {
		Facet facet = _userFacetFactory.newInstance(_searchContext);

		facet.setFacetConfiguration(buildFacetConfiguration(facet));

		facet.select(_selectedUserNames);

		return facet;
	}

	public void setFrequencyThreshold(int frequencyThreshold) {
		_frequencyThreshold = frequencyThreshold;
	}

	public void setMaxTerms(int maxTerms) {
		_maxTerms = maxTerms;
	}

	public void setSearchContext(SearchContext searchContext) {
		_searchContext = searchContext;
	}

	public void setSelectedUserNames(String... selectedUserNames) {
		_selectedUserNames = selectedUserNames;
	}

	protected FacetConfiguration buildFacetConfiguration(Facet facet) {
		FacetConfiguration facetConfiguration = new FacetConfiguration();

		facetConfiguration.setFieldName(facet.getFieldName());
		facetConfiguration.setLabel("any-user");
		facetConfiguration.setOrder("OrderHitsDesc");
		facetConfiguration.setStatic(false);
		facetConfiguration.setWeight(1.1);

		UserFacetConfiguration userFacetConfiguration =
			new UserFacetConfigurationImpl(facetConfiguration);

		userFacetConfiguration.setFrequencyThreshold(_frequencyThreshold);
		userFacetConfiguration.setMaxTerms(_maxTerms);

		return facetConfiguration;
	}

	private int _frequencyThreshold;
	private int _maxTerms;
	private SearchContext _searchContext;
	private String[] _selectedUserNames;
	private final UserFacetFactory _userFacetFactory;

}