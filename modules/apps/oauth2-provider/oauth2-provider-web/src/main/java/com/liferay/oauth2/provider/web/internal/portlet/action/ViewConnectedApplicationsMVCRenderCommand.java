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

package com.liferay.oauth2.provider.web.internal.portlet.action;

import com.liferay.oauth2.provider.model.OAuth2ApplicationScopeAliases;
import com.liferay.oauth2.provider.model.OAuth2Authorization;
import com.liferay.oauth2.provider.scope.liferay.ApplicationDescriptorLocator;
import com.liferay.oauth2.provider.scope.liferay.ScopeDescriptorLocator;
import com.liferay.oauth2.provider.scope.liferay.ScopeLocator;
import com.liferay.oauth2.provider.service.OAuth2ApplicationScopeAliasesLocalService;
import com.liferay.oauth2.provider.service.OAuth2ApplicationService;
import com.liferay.oauth2.provider.service.OAuth2AuthorizationLocalService;
import com.liferay.oauth2.provider.web.internal.AssignableScopes;
import com.liferay.oauth2.provider.web.internal.constants.OAuth2ProviderPortletKeys;
import com.liferay.oauth2.provider.web.internal.constants.OAuth2ProviderWebKeys;
import com.liferay.oauth2.provider.web.internal.display.context.OAuth2ConnectedApplicationsPortletDisplayContext;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tomas Polesovsky
 * @author Stian Sigvartsen
 */
@Component(
	property = {
		"javax.portlet.name=" + OAuth2ProviderPortletKeys.OAUTH2_CONNECTED_APPLICATIONS,
		"mvc.command.name=/", "mvc.command.name=/connected_applications/view"
	}
)
public class ViewConnectedApplicationsMVCRenderCommand
	implements MVCRenderCommand {

	@Override
	public String render(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		List<OAuth2Authorization> userOAuth2Authorizations =
			_oAuth2AuthorizationLocalService.getUserOAuth2Authorizations(
				themeDisplay.getUserId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				null);

		long oAuth2AuthorizationId = ParamUtil.getLong(
			renderRequest, "oAuth2AuthorizationId");

		if (!ListUtil.exists(
				userOAuth2Authorizations,
				userOAuth2Authorization ->
					userOAuth2Authorization.getOAuth2AuthorizationId() ==
						oAuth2AuthorizationId)) {

			OAuth2ConnectedApplicationsPortletDisplayContext
				oAuth2ConnectedApplicationsPortletDisplayContext =
					new OAuth2ConnectedApplicationsPortletDisplayContext(
						renderRequest);

			renderRequest.setAttribute(
				OAuth2ProviderWebKeys.
					OAUTH2_CONNECTED_APPLICATIONS_PORTLET_DISPLAY_CONTEXT,
				oAuth2ConnectedApplicationsPortletDisplayContext);

			return "/connected_applications/view.jsp";
		}

		long oAuth2ApplicationId = ParamUtil.getLong(
			renderRequest, "oAuth2ApplicationId");

		List<OAuth2ApplicationScopeAliases> oAuth2ApplicationScopeAliaseses =
			_oAuth2ApplicationScopeAliasesLocalService.
				getOAuth2ApplicationScopeAliaseses(
					oAuth2ApplicationId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null);

		Set<String> scopeAliases = new HashSet<>();

		for (OAuth2ApplicationScopeAliases oAuth2ApplicationScopeAliases :
				oAuth2ApplicationScopeAliaseses) {

			scopeAliases.addAll(
				oAuth2ApplicationScopeAliases.getScopeAliasesList());
		}

		AssignableScopes assignableScopes = new AssignableScopes(
			_applicationDescriptorLocator, themeDisplay.getLocale(),
			_scopeDescriptorLocator);

		for (String scopeAlias : scopeAliases) {
			assignableScopes.addLiferayOAuth2Scopes(
				_scopeLocator.getLiferayOAuth2Scopes(
					themeDisplay.getCompanyId(), scopeAlias));
		}

		OAuth2ConnectedApplicationsPortletDisplayContext
			oAuth2ConnectedApplicationsPortletDisplayContext =
				new OAuth2ConnectedApplicationsPortletDisplayContext(
					assignableScopes, renderRequest, _oAuth2ApplicationService);

		renderRequest.setAttribute(
			OAuth2ProviderWebKeys.
				OAUTH2_CONNECTED_APPLICATIONS_PORTLET_DISPLAY_CONTEXT,
			oAuth2ConnectedApplicationsPortletDisplayContext);

		return "/connected_applications/view_application.jsp";
	}

	@Reference
	private ApplicationDescriptorLocator _applicationDescriptorLocator;

	@Reference
	private OAuth2ApplicationScopeAliasesLocalService
		_oAuth2ApplicationScopeAliasesLocalService;

	@Reference
	private OAuth2ApplicationService _oAuth2ApplicationService;

	@Reference
	private OAuth2AuthorizationLocalService _oAuth2AuthorizationLocalService;

	@Reference
	private ScopeDescriptorLocator _scopeDescriptorLocator;

	@Reference
	private ScopeLocator _scopeLocator;

}