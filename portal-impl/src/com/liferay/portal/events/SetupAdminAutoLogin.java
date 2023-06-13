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

package com.liferay.portal.events;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auto.login.AutoLoginException;
import com.liferay.portal.kernel.security.auto.login.BaseAutoLogin;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.security.DefaultAdminUtil;
import com.liferay.portal.util.PropsValues;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alvaro Saugar
 */
public class SetupAdminAutoLogin extends BaseAutoLogin {

	@Override
	protected String[] doHandleException(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Exception exception)
		throws AutoLoginException {

		if (_log.isDebugEnabled()) {
			_log.debug(exception);
		}

		throw new AutoLoginException(exception);
	}

	@Override
	protected String[] doLogin(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		if (Validator.isNotNull(PropsValues.DEFAULT_ADMIN_PASSWORD)) {
			return null;
		}

		Company company = PortalUtil.getCompany(httpServletRequest);

		User user = DefaultAdminUtil.fetchDefaultAdmin(company.getCompanyId());

		if (user == null) {
			return null;
		}

		String reminderQueryAnswer = user.getReminderQueryAnswer();

		if (user.isPasswordReset() &&
			reminderQueryAnswer.equals(WorkflowConstants.LABEL_PENDING) &&
			Validator.isNull(user.getReminderQueryQuestion()) &&
			Validator.isNull(user.getLastFailedLoginDate()) &&
			Validator.isNull(user.getLockoutDate())) {

			String[] credentials = new String[3];

			credentials[0] = String.valueOf(user.getUserId());
			credentials[1] = user.getPassword();
			credentials[2] = Boolean.TRUE.toString();

			return credentials;
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SetupAdminAutoLogin.class);

}