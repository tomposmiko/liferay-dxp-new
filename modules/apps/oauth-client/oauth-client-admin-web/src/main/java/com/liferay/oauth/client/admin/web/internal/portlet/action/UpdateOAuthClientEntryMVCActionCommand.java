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

package com.liferay.oauth.client.admin.web.internal.portlet.action;

import com.liferay.oauth.client.admin.web.internal.constants.OAuthClientAdminPortletKeys;
import com.liferay.oauth.client.persistence.service.OAuthClientEntryService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Arthur Chan
 */
@Component(
	property = {
		"javax.portlet.name=" + OAuthClientAdminPortletKeys.OAUTH_CLIENT_ADMIN,
		"mvc.command.name=/oauth_client_admin/update_oauth_client_entry"
	},
	service = MVCActionCommand.class
)
public class UpdateOAuthClientEntryMVCActionCommand
	implements MVCActionCommand {

	@Override
	public boolean processAction(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		try {
			long oAuthClientEntryId = ParamUtil.getLong(
				actionRequest, "oAuthClientEntryId");

			String authRequestParametersJSON = ParamUtil.getString(
				actionRequest, "authRequestParametersJSON");
			String authServerWellKnownURI = ParamUtil.getString(
				actionRequest, "authServerWellKnownURI");
			String infoJSON = ParamUtil.getString(actionRequest, "infoJSON");
			String tokenRequestParametersJSON = ParamUtil.getString(
				actionRequest, "tokenRequestParametersJSON");

			if (oAuthClientEntryId > 0) {
				_oAuthClientEntryService.updateOAuthClientEntry(
					oAuthClientEntryId, authRequestParametersJSON,
					authServerWellKnownURI, infoJSON,
					tokenRequestParametersJSON);
			}
			else {
				ThemeDisplay themeDisplay =
					(ThemeDisplay)actionRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				_oAuthClientEntryService.addOAuthClientEntry(
					themeDisplay.getUserId(), authRequestParametersJSON,
					authServerWellKnownURI, infoJSON,
					tokenRequestParametersJSON);
			}

			return true;
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.error(portalException);
			}

			Class<?> clazz = portalException.getClass();

			SessionErrors.add(actionRequest, clazz.getName(), portalException);

			return false;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpdateOAuthClientEntryMVCActionCommand.class);

	@Reference
	private OAuthClientEntryService _oAuthClientEntryService;

}