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

package com.liferay.depot.web.internal.portlet.action;

import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.DepotEntryService;
import com.liferay.depot.web.internal.constants.DepotPortletKeys;
import com.liferay.document.library.configuration.DLSizeLimitConfigurationProvider;
import com.liferay.portal.configuration.persistence.listener.ConfigurationModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Localization;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	property = {
		"javax.portlet.name=" + DepotPortletKeys.DEPOT_ADMIN,
		"javax.portlet.name=" + DepotPortletKeys.DEPOT_SETTINGS,
		"mvc.command.name=/depot/edit_depot_entry"
	},
	service = MVCActionCommand.class
)
public class EditDepotEntryMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			long depotEntryId = ParamUtil.getLong(
				actionRequest, "depotEntryId");

			DepotEntry depotEntry = _depotEntryService.getDepotEntry(
				depotEntryId);

			Group group = depotEntry.getGroup();

			UnicodeProperties depotAppCustomizationUnicodeProperties =
				PropertiesParamUtil.getProperties(
					actionRequest, "DepotAppCustomization--");

			_depotEntryService.updateDepotEntry(
				depotEntryId,
				_localization.getLocalizationMap(
					actionRequest, "name", group.getNameMap()),
				_localization.getLocalizationMap(
					actionRequest, "description", group.getDescriptionMap()),
				_toStringBooleanMap(depotAppCustomizationUnicodeProperties),
				PropertiesParamUtil.getProperties(
					actionRequest, "TypeSettingsProperties--"),
				ServiceContextFactory.getInstance(
					DepotEntry.class.getName(), actionRequest));

			_updateDLSizeLimitConfiguration(group.getGroupId(), actionRequest);
		}
		catch (ConfigurationModelListenerException
					configurationModelListenerException) {

			SessionErrors.add(
				actionRequest, configurationModelListenerException.getClass());

			actionResponse.sendRedirect(
				ParamUtil.getString(actionRequest, "redirect"));
		}
		catch (PortalException portalException) {
			_log.error(portalException);

			SessionErrors.add(
				actionRequest, portalException.getClass(), portalException);

			actionResponse.sendRedirect(
				ParamUtil.getString(actionRequest, "redirect"));
		}
	}

	private Map<String, Boolean> _toStringBooleanMap(
		UnicodeProperties unicodeProperties) {

		Map<String, Boolean> map = new HashMap<>();

		for (Map.Entry<String, String> entry : unicodeProperties.entrySet()) {
			map.put(entry.getKey(), GetterUtil.getBoolean(entry.getValue()));
		}

		return map;
	}

	private void _updateDLSizeLimitConfiguration(
			long groupId, ActionRequest actionRequest)
		throws Exception {

		Map<String, Long> mimeTypeSizeLimits = new LinkedHashMap<>();

		Map<String, String[]> parameterMap = actionRequest.getParameterMap();

		for (int i = 0; parameterMap.containsKey("mimeType_" + i); i++) {
			String mimeType = null;

			String[] mimeTypes = parameterMap.get("mimeType_" + i);

			if ((mimeTypes.length != 0) && Validator.isNotNull(mimeTypes[0])) {
				mimeType = mimeTypes[0];
			}

			Long size = null;

			String[] sizes = parameterMap.get("size_" + i);

			if ((sizes.length != 0) && Validator.isNotNull(sizes[0])) {
				size = GetterUtil.getLong(sizes[0]);
			}

			if ((mimeType != null) || (size != null)) {
				mimeTypeSizeLimits.put(mimeType, size);
			}
		}

		_dlSizeLimitConfigurationProvider.updateGroupSizeLimit(
			groupId, 0L, mimeTypeSizeLimits);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EditDepotEntryMVCActionCommand.class);

	@Reference
	private DepotEntryService _depotEntryService;

	@Reference
	private DLSizeLimitConfigurationProvider _dlSizeLimitConfigurationProvider;

	@Reference
	private Localization _localization;

}