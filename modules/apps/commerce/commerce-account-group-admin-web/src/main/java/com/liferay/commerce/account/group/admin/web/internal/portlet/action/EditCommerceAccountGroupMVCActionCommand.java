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

package com.liferay.commerce.account.group.admin.web.internal.portlet.action;

import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.commerce.account.constants.CommerceAccountPortletKeys;
import com.liferay.commerce.account.exception.CommerceAccountGroupNameException;
import com.liferay.commerce.account.exception.NoSuchAccountGroupException;
import com.liferay.commerce.account.model.CommerceAccountGroup;
import com.liferay.commerce.account.service.CommerceAccountGroupService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	enabled = false, immediate = true,
	property = {
		"javax.portlet.name=" + CommerceAccountPortletKeys.COMMERCE_ACCOUNT_GROUP_ADMIN,
		"mvc.command.name=/commerce_account_group_admin/edit_commerce_account_group"
	},
	service = MVCActionCommand.class
)
public class EditCommerceAccountGroupMVCActionCommand
	extends BaseMVCActionCommand {

	protected void deleteCommerceAccountGroups(ActionRequest actionRequest)
		throws PortalException {

		long[] deleteCommerceAccountGroupIds = null;

		long commerceAccountGroupId = ParamUtil.getLong(
			actionRequest, "commerceAccountGroupId");

		if (commerceAccountGroupId > 0) {
			deleteCommerceAccountGroupIds = new long[] {commerceAccountGroupId};
		}
		else {
			deleteCommerceAccountGroupIds = StringUtil.split(
				ParamUtil.getString(
					actionRequest, "deleteCommerceAccountGroupIds"),
				0L);
		}

		for (long deleteCommerceAccountGroupId :
				deleteCommerceAccountGroupIds) {

			_commerceAccountGroupService.deleteCommerceAccountGroup(
				deleteCommerceAccountGroupId);
		}
	}

	@Override
	protected void doProcessAction(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				CommerceAccountGroup commerceAccountGroup =
					updateCommerceAccountGroup(actionRequest);

				String redirect = getSaveAndContinueRedirect(
					actionRequest, commerceAccountGroup);

				sendRedirect(actionRequest, actionResponse, redirect);
			}
			else if (cmd.equals(Constants.DELETE)) {
				deleteCommerceAccountGroups(actionRequest);
			}
		}
		catch (Exception exception) {
			if (exception instanceof CommerceAccountGroupNameException) {
				hideDefaultErrorMessage(actionRequest);

				SessionErrors.add(actionRequest, exception.getClass());

				actionResponse.setRenderParameter(
					"mvcRenderCommandName",
					"/commerce_account_group_admin" +
						"/edit_commerce_account_group");
			}
			else if (exception instanceof NoSuchAccountGroupException ||
					 exception instanceof PrincipalException) {

				SessionErrors.add(actionRequest, exception.getClass());

				actionResponse.setRenderParameter("mvcPath", "/error.jsp");
			}
			else {
				_log.error(exception, exception);
			}
		}
	}

	protected String getSaveAndContinueRedirect(
		ActionRequest actionRequest,
		CommerceAccountGroup commerceAccountGroup) {

		PortletURL portletURL = _portal.getControlPanelPortletURL(
			actionRequest,
			CommerceAccountPortletKeys.COMMERCE_ACCOUNT_GROUP_ADMIN,
			PortletRequest.RENDER_PHASE);

		if (commerceAccountGroup != null) {
			portletURL.setParameter(
				"mvcRenderCommandName",
				"/commerce_account_group_admin/edit_commerce_account_group");
			portletURL.setParameter(
				"commerceAccountGroupId",
				String.valueOf(
					commerceAccountGroup.getCommerceAccountGroupId()));

			String backURL = ParamUtil.getString(
				actionRequest, "backURL", portletURL.toString());

			portletURL.setParameter("backURL", backURL);

			String redirect = ParamUtil.getString(actionRequest, "redirect");

			portletURL.setParameter("redirect", redirect);
		}

		return portletURL.toString();
	}

	protected CommerceAccountGroup updateCommerceAccountGroup(
			ActionRequest actionRequest)
		throws Exception {

		long commerceAccountGroupId = ParamUtil.getLong(
			actionRequest, "commerceAccountGroupId");

		String name = ParamUtil.getString(actionRequest, "name");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			CommerceAccountGroup.class.getName(), actionRequest);

		CommerceAccountGroup commerceAccountGroup = null;

		if (commerceAccountGroupId <= 0) {
			int type = ParamUtil.getInteger(
				actionRequest, "type",
				CommerceAccountConstants.ACCOUNT_GROUP_TYPE_STATIC);

			commerceAccountGroup =
				_commerceAccountGroupService.addCommerceAccountGroup(
					_portal.getCompanyId(actionRequest), name, type, null,
					serviceContext);
		}
		else {
			commerceAccountGroup =
				_commerceAccountGroupService.updateCommerceAccountGroup(
					commerceAccountGroupId, name, serviceContext);
		}

		return commerceAccountGroup;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EditCommerceAccountGroupMVCActionCommand.class);

	@Reference
	private CommerceAccountGroupService _commerceAccountGroupService;

	@Reference
	private Portal _portal;

}