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

package com.liferay.portal.servlet.filters.password.modified;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.servlet.filters.BasePortalFilter;

import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Marta Medio
 * @author Stian Sigvartsen
 */
public class PasswordModifiedFilter extends BasePortalFilter {

	@Override
	protected void processFilter(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws Exception {

		String requestPath = _getRequestPath(httpServletRequest);

		if (!requestPath.equals("/c/portal/logout") &&
			_isPasswordModified(httpServletRequest)) {

			httpServletResponse.sendRedirect(
				PortalUtil.getPathMain() + "/portal/logout");
		}
		else {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

	private String _getRequestPath(HttpServletRequest httpServletRequest) {
		String requestURI = httpServletRequest.getRequestURI();

		String contextPath = httpServletRequest.getContextPath();

		if (Validator.isNotNull(contextPath) &&
			!contextPath.equals(StringPool.SLASH)) {

			requestURI = requestURI.substring(contextPath.length());
		}

		return HttpComponentsUtil.removePathParameters(requestURI);
	}

	private boolean _isPasswordModified(HttpServletRequest httpServletRequest) {
		HttpSession httpSession = httpServletRequest.getSession(false);

		if ((httpSession == null) ||
			!httpServletRequest.isRequestedSessionIdValid()) {

			return false;
		}

		try {
			User user = PortalUtil.getUser(httpServletRequest);

			if ((user == null) || user.isGuestUser() ||
				!_isValidRealUserId(httpSession, user)) {

				return false;
			}

			Date passwordModifiedDate = user.getPasswordModifiedDate();

			if (passwordModifiedDate == null) {
				return false;
			}

			if (!httpServletRequest.isRequestedSessionIdValid() ||
				(httpSession.getCreationTime() <
					passwordModifiedDate.getTime())) {

				return true;
			}

			return false;
		}
		catch (PortalException portalException) {
			_log.error(portalException);

			return false;
		}
	}

	private boolean _isValidRealUserId(HttpSession httpSession, User user) {
		Long realUserId = (Long)httpSession.getAttribute(WebKeys.USER_ID);

		if ((realUserId == null) || (user.getUserId() != realUserId)) {
			return false;
		}

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PasswordModifiedFilter.class);

}