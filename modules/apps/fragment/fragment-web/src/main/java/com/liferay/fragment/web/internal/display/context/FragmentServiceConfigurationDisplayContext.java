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

package com.liferay.fragment.web.internal.display.context;

import com.liferay.fragment.web.internal.configuration.admin.service.FragmentServiceManagedServiceFactory;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.service.PortalPreferencesLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Objects;

import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public class FragmentServiceConfigurationDisplayContext {

	public FragmentServiceConfigurationDisplayContext(
		HttpServletRequest httpServletRequest,
		LiferayPortletResponse liferayPortletResponse,
		FragmentServiceManagedServiceFactory
			fragmentServiceManagedServiceFactory,
		String scope) {

		_httpServletRequest = httpServletRequest;
		_liferayPortletResponse = liferayPortletResponse;
		_fragmentServiceManagedServiceFactory =
			fragmentServiceManagedServiceFactory;
		_scope = scope;
	}

	public String getEditFragmentServiceConfigurationURL() {
		PortletURL editFragmentServiceConfigurationURL =
			_liferayPortletResponse.createActionURL();

		editFragmentServiceConfigurationURL.setParameter(
			ActionRequest.ACTION_NAME,
			"/instance_settings/edit_fragment_service_configuration");
		editFragmentServiceConfigurationURL.setParameter(
			"redirect", PortalUtil.getCurrentURL(_httpServletRequest));
		editFragmentServiceConfigurationURL.setParameter("scope", _scope);
		editFragmentServiceConfigurationURL.setParameter(
			"scopePK", String.valueOf(_getScopePk()));

		return editFragmentServiceConfigurationURL.toString();
	}

	public String getPropagateContributedFragmentEntriesChangesURL() {
		PortletURL propagateContributedFragmentEntriesChangesURL =
			_liferayPortletResponse.createActionURL();

		propagateContributedFragmentEntriesChangesURL.setParameter(
			ActionRequest.ACTION_NAME,
			"/instance_settings" +
				"/propagate_contributed_fragment_entries_changes");
		propagateContributedFragmentEntriesChangesURL.setParameter(
			"redirect", PortalUtil.getCurrentURL(_httpServletRequest));
		propagateContributedFragmentEntriesChangesURL.setParameter(
			"scope", _scope);
		propagateContributedFragmentEntriesChangesURL.setParameter(
			"scopePK", String.valueOf(_getScopePk()));

		return propagateContributedFragmentEntriesChangesURL.toString();
	}

	public boolean isAlreadyPropagateContributedFragmentChanges() {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)_httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		PortletPreferences portletPreferences =
			PortalPreferencesLocalServiceUtil.getPreferences(
				themeDisplay.getCompanyId(),
				PortletKeys.PREFS_OWNER_TYPE_COMPANY);

		return GetterUtil.getBoolean(
			portletPreferences.getValue(
				"alreadyPropagateContributedFragmentChanges", null));
	}

	public boolean isPropagateChangesEnabled() {
		return _fragmentServiceManagedServiceFactory.isPropagateChanges(
			_scope, _getScopePk());
	}

	public boolean isPropagateContributedFragmentChangesEnabled() {
		return _fragmentServiceManagedServiceFactory.
			isPropagateContributedFragmentChanges(_scope, _getScopePk());
	}

	public boolean showInfoMessage() throws Exception {
		if (!Objects.equals(
				_scope,
				ExtendedObjectClassDefinition.Scope.COMPANY.getValue()) ||
			_fragmentServiceManagedServiceFactory.hasScopedConfiguration(
				_getScopePk())) {

			return false;
		}

		return true;
	}

	private long _getScopePk() {
		if (Objects.equals(
				_scope,
				ExtendedObjectClassDefinition.Scope.COMPANY.getValue())) {

			ThemeDisplay themeDisplay =
				(ThemeDisplay)_httpServletRequest.getAttribute(
					WebKeys.THEME_DISPLAY);

			return themeDisplay.getCompanyId();
		}
		else if (Objects.equals(
					_scope,
					ExtendedObjectClassDefinition.Scope.SYSTEM.getValue())) {

			return 0L;
		}

		throw new IllegalArgumentException("Unsupported scope: " + _scope);
	}

	private final FragmentServiceManagedServiceFactory
		_fragmentServiceManagedServiceFactory;
	private final HttpServletRequest _httpServletRequest;
	private final LiferayPortletResponse _liferayPortletResponse;
	private final String _scope;

}