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

package com.liferay.portal.search.web.internal.search.bar.portlet.shared.search;

import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.search.constants.SearchContextAttributes;
import com.liferay.portal.search.web.internal.display.context.Keywords;
import com.liferay.portal.search.web.internal.display.context.SearchScope;
import com.liferay.portal.search.web.internal.display.context.SearchScopePreference;
import com.liferay.portal.search.web.internal.portlet.preferences.PortletPreferencesLookup;
import com.liferay.portal.search.web.internal.search.bar.constants.SearchBarPortletKeys;
import com.liferay.portal.search.web.internal.search.bar.portlet.SearchBarPortletDestinationUtil;
import com.liferay.portal.search.web.internal.search.bar.portlet.SearchBarPortletPreferences;
import com.liferay.portal.search.web.internal.search.bar.portlet.SearchBarPortletPreferencesImpl;
import com.liferay.portal.search.web.internal.util.SearchOptionalUtil;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchContributor;
import com.liferay.portal.search.web.portlet.shared.search.PortletSharedSearchSettings;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author André de Oliveira
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + SearchBarPortletKeys.SEARCH_BAR,
	service = PortletSharedSearchContributor.class
)
public class SearchBarPortletSharedSearchContributor
	implements PortletSharedSearchContributor {

	@Override
	public void contribute(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		SearchBarPortletPreferences searchBarPortletPreferences =
			new SearchBarPortletPreferencesImpl(
				portletSharedSearchSettings.getPortletPreferences());

		if (!shouldContributeToCurrentPageSearch(
				searchBarPortletPreferences, portletSharedSearchSettings)) {

			return;
		}

		setKeywords(searchBarPortletPreferences, portletSharedSearchSettings);

		filterByThisSite(
			searchBarPortletPreferences, portletSharedSearchSettings);
	}

	protected void filterByThisSite(
		SearchBarPortletPreferences searchBarPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		SearchScope searchScope = getSearchScope(
			searchBarPortletPreferences, portletSharedSearchSettings);

		if (searchScope != SearchScope.THIS_SITE) {
			return;
		}

		SearchContext searchContext =
			portletSharedSearchSettings.getSearchContext();

		searchContext.setGroupIds(
			new long[] {getScopeGroupId(portletSharedSearchSettings)});
	}

	protected Optional<Portlet> findTopSearchBarPortletOptional(
		ThemeDisplay themeDisplay) {

		Stream<Portlet> stream = getPortlets(themeDisplay);

		return stream.filter(
			this::isTopSearchBar
		).findAny();
	}

	protected SearchScope getDefaultSearchScope() {
		SearchBarPortletPreferences searchBarPortletPreferences =
			new SearchBarPortletPreferencesImpl(Optional.empty());

		SearchScopePreference searchScopePreference =
			searchBarPortletPreferences.getSearchScopePreference();

		return searchScopePreference.getSearchScope();
	}

	protected Stream<Portlet> getPortlets(ThemeDisplay themeDisplay) {
		Layout layout = themeDisplay.getLayout();

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		List<Portlet> portlets = layoutTypePortlet.getAllPortlets(false);

		return portlets.stream();
	}

	protected long getScopeGroupId(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		ThemeDisplay themeDisplay =
			portletSharedSearchSettings.getThemeDisplay();

		return themeDisplay.getScopeGroupId();
	}

	protected SearchBarPortletPreferences getSearchBarPortletPreferences(
		Portlet portlet, ThemeDisplay themeDisplay) {

		return new SearchBarPortletPreferencesImpl(
			portletPreferencesLookup.fetchPreferences(portlet, themeDisplay));
	}

	protected SearchScope getSearchScope(
		SearchBarPortletPreferences searchBarPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		SearchScopePreference searchScopePreference =
			searchBarPortletPreferences.getSearchScopePreference();

		if (searchScopePreference !=
				SearchScopePreference.LET_THE_USER_CHOOSE) {

			return searchScopePreference.getSearchScope();
		}

		Optional<String> optional = portletSharedSearchSettings.getParameter(
			searchBarPortletPreferences.getScopeParameterName());

		return optional.map(
			SearchScope::getSearchScope
		).orElseGet(
			this::getDefaultSearchScope
		);
	}

	protected boolean isLuceneSyntax(
		SearchBarPortletPreferences searchBarPortletPreferences,
		Keywords keywords) {

		if (searchBarPortletPreferences.isUseAdvancedSearchSyntax() ||
			keywords.isLuceneSyntax()) {

			return true;
		}

		return false;
	}

	protected boolean isSamePortlet(
		Portlet portlet,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		if (Objects.equals(
				portlet.getPortletId(),
				portletSharedSearchSettings.getPortletId())) {

			return true;
		}

		return false;
	}

	protected boolean isSearchBarInBodyWithTopSearchBarAlreadyPresent(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		ThemeDisplay themeDisplay =
			portletSharedSearchSettings.getThemeDisplay();

		Optional<Portlet> optional = findTopSearchBarPortletOptional(
			themeDisplay);

		if (!optional.isPresent()) {
			return false;
		}

		Portlet portlet = optional.get();

		if (isSamePortlet(portlet, portletSharedSearchSettings)) {
			return false;
		}

		SearchBarPortletPreferences searchBarPortletPreferences =
			getSearchBarPortletPreferences(portlet, themeDisplay);

		if (!SearchBarPortletDestinationUtil.isSameDestination(
				searchBarPortletPreferences, themeDisplay)) {

			return false;
		}

		return true;
	}

	protected boolean isTopSearchBar(Portlet portlet) {
		if (portlet.isStatic()) {
			if (Objects.equals(
					portlet.getPortletName(),
					SearchBarPortletKeys.SEARCH_BAR)) {

				return true;
			}
		}

		return false;
	}

	protected void setKeywords(
		SearchBarPortletPreferences searchBarPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		String parameterName =
			searchBarPortletPreferences.getKeywordsParameterName();

		portletSharedSearchSettings.setKeywordsParameterName(parameterName);

		SearchOptionalUtil.copy(
			() -> portletSharedSearchSettings.getParameter(parameterName),
			value -> {
				Keywords keywords = new Keywords(value);

				portletSharedSearchSettings.setKeywords(keywords.getKeywords());

				if (isLuceneSyntax(searchBarPortletPreferences, keywords)) {
					setLuceneSyntax(portletSharedSearchSettings);
				}
			});
	}

	protected void setLuceneSyntax(
		PortletSharedSearchSettings portletSharedSearchSettings) {

		SearchContext searchContext =
			portletSharedSearchSettings.getSearchContext();

		searchContext.setAttribute(
			SearchContextAttributes.ATTRIBUTE_KEY_LUCENE_SYNTAX, Boolean.TRUE);
	}

	protected boolean shouldContributeToCurrentPageSearch(
		SearchBarPortletPreferences searchBarPortletPreferences,
		PortletSharedSearchSettings portletSharedSearchSettings) {

		if (!SearchBarPortletDestinationUtil.isSameDestination(
				searchBarPortletPreferences,
				portletSharedSearchSettings.getThemeDisplay())) {

			return false;
		}

		if (isSearchBarInBodyWithTopSearchBarAlreadyPresent(
				portletSharedSearchSettings)) {

			return false;
		}

		return true;
	}

	@Reference
	protected PortletPreferencesLookup portletPreferencesLookup;

}