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

package com.liferay.portal.search.rest.internal.resource.v1_0;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.LiferayRenderRequest;
import com.liferay.portal.kernel.portlet.PortletConfigFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletInstanceFactoryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.rest.dto.v1_0.Suggestion;
import com.liferay.portal.search.rest.dto.v1_0.SuggestionsContributorConfiguration;
import com.liferay.portal.search.rest.dto.v1_0.SuggestionsContributorResults;
import com.liferay.portal.search.rest.resource.v1_0.SuggestionResource;
import com.liferay.portal.search.suggestions.SuggestionsRetriever;
import com.liferay.portal.search.web.constants.SearchBarPortletKeys;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portlet.RenderRequestFactory;
import com.liferay.portlet.RenderResponseFactory;

import java.util.Collections;

import javax.portlet.PortletConfig;
import javax.portlet.PortletMode;
import javax.portlet.RenderRequest;
import javax.portlet.WindowState;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Petteri Karttunen
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/suggestion.properties",
	scope = ServiceScope.PROTOTYPE, service = SuggestionResource.class
)
public class SuggestionResourceImpl extends BaseSuggestionResourceImpl {

	@Override
	public Page<SuggestionsContributorResults> postSuggestionsPage(
			String currentURL, String destinationFriendlyURL, Long groupId,
			Long plid, String scope, String search,
			SuggestionsContributorConfiguration[]
				suggestionsContributorConfigurations)
		throws Exception {

		if (!GetterUtil.getBoolean(PropsUtil.get("feature.flag.LPS-152597")) ||
			(suggestionsContributorConfigurations == null)) {

			return Page.of(Collections.emptyList());
		}

		LiferayRenderRequest liferayRenderRequest = _createLiferayRenderRequest(
			currentURL, plid);

		return Page.of(
			transform(
				_suggestionsRetriever.getSuggestionsContributorResults(
					liferayRenderRequest,
					RenderResponseFactory.create(
						contextHttpServletResponse,
						(RenderRequest)liferayRenderRequest),
					_createSearchContext(
						destinationFriendlyURL, _getGroupId(groupId), scope,
						search, suggestionsContributorConfigurations)),
				suggestionsContributorResult ->
					new SuggestionsContributorResults() {
						{
							attributes =
								suggestionsContributorResult.getAttributes();
							displayGroupName =
								suggestionsContributorResult.
									getDisplayGroupName();
							suggestions = transformToArray(
								suggestionsContributorResult.getSuggestions(),
								suggestion -> new Suggestion() {
									{
										attributes = suggestion.getAttributes();
										score = suggestion.getScore();
										text = suggestion.getText();
									}
								},
								Suggestion.class);
						}
					}));
	}

	private LiferayRenderRequest _createLiferayRenderRequest(
			String currentURL, long plid)
		throws Exception {

		Layout layout = _layoutLocalService.getLayout(plid);

		contextHttpServletRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _createThemeDisplay(currentURL, layout));

		Portlet portlet = _portletLocalService.getPortletById(
			SearchBarPortletKeys.SEARCH_BAR);
		ServletContext servletContext =
			(ServletContext)contextHttpServletRequest.getAttribute(WebKeys.CTX);

		PortletConfig portletConfig = PortletConfigFactoryUtil.create(
			portlet, servletContext);

		LiferayRenderRequest liferayRenderRequest = RenderRequestFactory.create(
			contextHttpServletRequest, portlet,
			PortletInstanceFactoryUtil.create(portlet, servletContext),
			portletConfig.getPortletContext(), WindowState.NORMAL,
			PortletMode.VIEW, null, layout.getPlid());

		liferayRenderRequest.setPortletRequestDispatcherRequest(
			contextHttpServletRequest);

		liferayRenderRequest.defineObjects(
			portletConfig,
			RenderResponseFactory.create(
				contextHttpServletResponse, liferayRenderRequest));

		return liferayRenderRequest;
	}

	private SearchContext _createSearchContext(
			String destinationFriendlyURL, long groupId, String scope,
			String search,
			SuggestionsContributorConfiguration[]
				suggestionsContributorConfigurations)
		throws Exception {

		SearchContext searchContext = new SearchContext();

		searchContext.setAttribute(
			"search.experiences.ip.address",
			contextHttpServletRequest.getRemoteAddr());
		searchContext.setAttribute(
			"search.experiences.scope.group.id", groupId);
		searchContext.setAttribute(
			"search.suggestions.contributor.configurations",
			suggestionsContributorConfigurations);
		searchContext.setAttribute(
			"search.suggestions.destination.friendly.url",
			destinationFriendlyURL);
		searchContext.setCompanyId(contextCompany.getCompanyId());

		if (!StringUtil.equals(scope, "everything")) {
			searchContext.setGroupIds(new long[] {groupId});
		}

		searchContext.setKeywords(search);
		searchContext.setLocale(contextAcceptLanguage.getPreferredLocale());
		searchContext.setTimeZone(contextUser.getTimeZone());
		searchContext.setUserId(contextUser.getUserId());

		return searchContext;
	}

	private ThemeDisplay _createThemeDisplay(String currentURL, Layout layout)
		throws Exception {

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(contextCompany);
		themeDisplay.setLayout(layout);
		themeDisplay.setPathMain(_portal.getPathMain());
		themeDisplay.setPermissionChecker(
			PermissionThreadLocal.getPermissionChecker());
		themeDisplay.setPlid(layout.getPlid());
		themeDisplay.setRequest(contextHttpServletRequest);
		themeDisplay.setScopeGroupId(layout.getGroupId());
		themeDisplay.setSiteGroupId(layout.getGroupId());
		themeDisplay.setURLCurrent(currentURL);
		themeDisplay.setUser(contextUser);

		return themeDisplay;
	}

	private long _getGroupId(Long groupId) {
		if (groupId != null) {
			return groupId;
		}

		try {
			return contextCompany.getGroupId();
		}
		catch (PortalException portalException) {
			throw new RuntimeException(portalException);
		}
	}

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

	@Reference
	private PortletPreferencesLocalService _portletPreferencesLocalService;

	@Reference
	private SuggestionsRetriever _suggestionsRetriever;

}