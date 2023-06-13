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

package com.liferay.account.admin.web.internal.portlet.action;

import com.liferay.account.constants.AccountPortletKeys;
import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.UserService;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Albert Lee
 */
@Component(
	property = {
		"javax.portlet.name=" + AccountPortletKeys.ACCOUNT_USERS_ADMIN,
		"mvc.command.name=/account_admin/edit_account_users"
	},
	service = MVCActionCommand.class
)
public class EditAccountUsersMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			long[] accountUserIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "accountUserIds"), 0L);

			if (cmd.equals(Constants.DEACTIVATE)) {
				_updateUsers(
					actionRequest, accountUserIds,
					WorkflowConstants.STATUS_INACTIVE);
			}
			else if (cmd.equals(Constants.RESTORE)) {
				ThemeDisplay themeDisplay =
					(ThemeDisplay)actionRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				_userLocalService.validateMaxUsers(themeDisplay.getCompanyId());

				_updateUsers(
					actionRequest, accountUserIds,
					WorkflowConstants.STATUS_APPROVED);
			}
			else if (cmd.equals(Constants.DELETE)) {
				_deleteUsers(accountUserIds);
			}

			String redirect = ParamUtil.getString(actionRequest, "redirect");

			if (Validator.isNotNull(redirect)) {
				sendRedirect(actionRequest, actionResponse, redirect);
			}
		}
		catch (Exception exception) {
			String mvcPath = "/account_users_admin/view.jsp";

			if (exception instanceof NoSuchUserException ||
				exception instanceof PrincipalException) {

				SessionErrors.add(actionRequest, exception.getClass());

				mvcPath = "/account_users_admin/error.jsp";
			}
			else {
				throw exception;
			}

			actionResponse.setRenderParameter("mvcPath", mvcPath);
		}
	}

	private void _deleteUsers(long[] accountUserIds) throws Exception {
		for (long accountUserId : accountUserIds) {
			_userService.deleteUser(accountUserId);
		}
	}

	private void _updateUsers(
			ActionRequest actionRequest, long[] accountUserIds, int status)
		throws Exception {

		for (long accountUserId : accountUserIds) {
			_userService.updateStatus(
				accountUserId, status,
				ServiceContextFactory.getInstance(
					User.class.getName(), actionRequest));
		}
	}

	@Reference
	private UserLocalService _userLocalService;

	@Reference
	private UserService _userService;

}