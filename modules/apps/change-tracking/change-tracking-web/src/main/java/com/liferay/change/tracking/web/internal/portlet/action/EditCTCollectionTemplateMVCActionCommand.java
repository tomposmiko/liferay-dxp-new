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

package com.liferay.change.tracking.web.internal.portlet.action;

import com.liferay.change.tracking.constants.CTPortletKeys;
import com.liferay.change.tracking.service.CTCollectionTemplateService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Máté Thurzó
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CTPortletKeys.PUBLICATIONS,
		"mvc.command.name=/change_tracking/edit_ct_collection_template"
	},
	service = MVCActionCommand.class
)
public class EditCTCollectionTemplateMVCActionCommand
	extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException {

		long ctCollectionTemplateId = ParamUtil.getLong(
			actionRequest, "ctCollectionTemplateId");

		String name = ParamUtil.getString(actionRequest, "name");
		String description = ParamUtil.getString(actionRequest, "description");
		String json = JSONUtil.put(
			"description",
			ParamUtil.getString(actionRequest, "publicationDescription")
		).put(
			"name", ParamUtil.getString(actionRequest, "publicationName")
		).put(
			"publicationsUserRoleUserIds",
			ParamUtil.getLongValues(
				actionRequest, "publicationsUserRoleUserIds")
		).put(
			"roleValues",
			ParamUtil.getIntegerValues(actionRequest, "roleValues")
		).put(
			"userIds", ParamUtil.getLongValues(actionRequest, "userIds")
		).toString();

		try {
			if (ctCollectionTemplateId > 0) {
				_ctCollectionTemplateService.updateCTCollectionTemplate(
					ctCollectionTemplateId, name, description, json);
			}
			else {
				_ctCollectionTemplateService.addCTCollectionTemplate(
					name, description, json);
			}
		}
		catch (PortalException portalException) {
			SessionErrors.add(actionRequest, portalException.getClass());

			_portal.copyRequestParameters(actionRequest, actionResponse);

			actionResponse.setRenderParameter(
				"mvcPath", "/edit_ct_collection_template.jsp");
		}

		String redirect = ParamUtil.getString(actionRequest, "redirect");

		if (Validator.isNotNull(redirect)) {
			sendRedirect(actionRequest, actionResponse, redirect);
		}
	}

	@Reference
	private CTCollectionTemplateService _ctCollectionTemplateService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Portal _portal;

}