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

package com.liferay.change.tracking.web.internal.spi.display;

import com.liferay.change.tracking.spi.display.BaseCTDisplayRenderer;
import com.liferay.change.tracking.spi.display.CTDisplayRenderer;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletPreferenceValue;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.PortletPreferenceValueLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.util.Portal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author David Truong
 */
@Component(service = CTDisplayRenderer.class)
public class PortletPreferenceValueCTDisplayRenderer
	extends BaseCTDisplayRenderer<PortletPreferenceValue> {

	@Override
	public Class<PortletPreferenceValue> getModelClass() {
		return PortletPreferenceValue.class;
	}

	@Override
	public String getTitle(
		Locale locale, PortletPreferenceValue portletPreferenceValue) {

		PortletPreferences portletPreferences =
			_portletPreferencesLocalService.fetchPortletPreferences(
				portletPreferenceValue.getPortletPreferencesId());

		if (portletPreferences == null) {
			return null;
		}

		List<String> arguments = new ArrayList<>(2);

		Portlet portlet = _portletLocalService.getPortletById(
			portletPreferences.getCompanyId(),
			portletPreferences.getPortletId());

		try {
			arguments.add(_portal.getPortletTitle(portlet, locale));
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(exception);
			}

			arguments.add(portlet.getPortletName());
		}

		Layout layout = _layoutLocalService.fetchLayout(
			portletPreferences.getPlid());

		if (layout == null) {
			arguments.add(_language.get(locale, "control-panel"));
		}
		else {
			arguments.add(layout.getName(locale));
		}

		return _language.format(
			locale, "preferences-for-x-on-x-page",
			arguments.toArray(new String[0]), false);
	}

	@Override
	public boolean isHideable(PortletPreferenceValue portletPreferenceValue) {
		PortletPreferences portletPreferences =
			_portletPreferencesLocalService.fetchPortletPreferences(
				portletPreferenceValue.getPortletPreferencesId());

		if (portletPreferences == null) {
			return true;
		}

		Portlet portlet = _portletLocalService.getPortletById(
			portletPreferences.getCompanyId(),
			portletPreferences.getPortletId());

		if (portlet.isSystem()) {
			return true;
		}

		Layout layout = _layoutLocalService.fetchLayout(
			portletPreferences.getPlid());

		if ((layout == null) ||
			layout.isPortletEmbedded(
				portletPreferences.getPortletId(), layout.getGroupId()) ||
			layout.isSystem() || layout.isTypeControlPanel()) {

			return true;
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortletPreferenceValueCTDisplayRenderer.class);

	@Reference
	private Language _language;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

	@Reference
	private PortletPreferencesLocalService _portletPreferencesLocalService;

	@Reference
	private PortletPreferenceValueLocalService
		_portletPreferenceValueLocalService;

}