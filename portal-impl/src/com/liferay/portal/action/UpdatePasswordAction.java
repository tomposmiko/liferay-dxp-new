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

package com.liferay.portal.action;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.exception.UserLockoutException;
import com.liferay.portal.kernel.exception.UserPasswordException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.Ticket;
import com.liferay.portal.kernel.model.TicketConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.auth.session.AuthenticatedSessionManagerUtil;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.TicketLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.pwd.PwdToolkitUtilThreadLocal;
import com.liferay.portal.struts.Action;
import com.liferay.portal.struts.model.ActionForward;
import com.liferay.portal.struts.model.ActionMapping;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Brian Wing Shun Chan
 * @author Mika Koivisto
 */
public class UpdatePasswordAction implements Action {

	@Override
	public ActionForward execute(
			ActionMapping actionMapping, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Ticket ticket = getTicket(httpServletRequest);

		if ((ticket != null) &&
			StringUtil.equals(
				httpServletRequest.getMethod(), HttpMethods.GET)) {

			resendAsPost(httpServletRequest, httpServletResponse);

			return null;
		}

		httpServletRequest.setAttribute(WebKeys.TICKET, ticket);

		String cmd = ParamUtil.getString(httpServletRequest, Constants.CMD);

		if (Validator.isNull(cmd)) {
			if (ticket != null) {
				User user = UserLocalServiceUtil.getUser(ticket.getClassPK());

				try {
					UserLocalServiceUtil.checkLockout(user);

					UserLocalServiceUtil.updatePasswordReset(
						user.getUserId(), true);
				}
				catch (UserLockoutException ule) {
					SessionErrors.add(httpServletRequest, ule.getClass(), ule);
				}
			}

			return actionMapping.getActionForward("portal.update_password");
		}

		try {
			updatePassword(
				httpServletRequest, httpServletResponse, themeDisplay, ticket);

			String redirect = ParamUtil.getString(
				httpServletRequest, WebKeys.REFERER);

			if (Validator.isNotNull(redirect)) {
				redirect = PortalUtil.escapeRedirect(redirect);
			}

			if (Validator.isNull(redirect)) {
				redirect = themeDisplay.getPathMain();
			}

			httpServletResponse.sendRedirect(redirect);

			return null;
		}
		catch (Exception e) {
			if (e instanceof UserPasswordException) {
				SessionErrors.add(httpServletRequest, e.getClass(), e);

				return actionMapping.getActionForward("portal.update_password");
			}
			else if (e instanceof NoSuchUserException ||
					 e instanceof PrincipalException) {

				SessionErrors.add(httpServletRequest, e.getClass());

				return actionMapping.getActionForward("portal.error");
			}

			PortalUtil.sendError(e, httpServletRequest, httpServletResponse);

			return null;
		}
	}

	protected Ticket getTicket(HttpServletRequest httpServletRequest) {
		String ticketKey = ParamUtil.getString(httpServletRequest, "ticketKey");

		if (Validator.isNull(ticketKey)) {
			return null;
		}

		try {
			Ticket ticket = TicketLocalServiceUtil.fetchTicket(ticketKey);

			if ((ticket == null) ||
				(ticket.getType() != TicketConstants.TYPE_PASSWORD)) {

				return null;
			}

			if (!ticket.isExpired()) {
				return ticket;
			}

			TicketLocalServiceUtil.deleteTicket(ticket);
		}
		catch (Exception e) {
		}

		return null;
	}

	protected boolean isValidatePassword(
		HttpServletRequest httpServletRequest) {

		HttpSession session = httpServletRequest.getSession();

		Boolean setupWizardPasswordUpdated = (Boolean)session.getAttribute(
			WebKeys.SETUP_WIZARD_PASSWORD_UPDATED);

		if ((setupWizardPasswordUpdated != null) &&
			setupWizardPasswordUpdated) {

			return false;
		}

		return true;
	}

	protected void resendAsPost(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		httpServletResponse.setHeader(
			"Cache-Control", "no-cache, no-store, must-revalidate");
		httpServletResponse.setHeader("Expires", "0");
		httpServletResponse.setHeader("Pragma", "no-cache");

		PrintWriter printWriter = httpServletResponse.getWriter();

		Map<String, String[]> parameterMap =
			httpServletRequest.getParameterMap();

		StringBundler sb = new StringBundler(7 + parameterMap.size() * 5);

		sb.append("<html><body onload=\"document.fm.submit();\">");
		sb.append("<form action=\"");
		sb.append(PortalUtil.getPortalURL(httpServletRequest));
		sb.append("/c/portal/update_password\" method=\"post\" name=\"fm\">");

		for (String name : parameterMap.keySet()) {
			String value = ParamUtil.getString(httpServletRequest, name);

			sb.append("<input name=\"");
			sb.append(HtmlUtil.escapeAttribute(name));
			sb.append("\" type=\"hidden\" value=\"");
			sb.append(HtmlUtil.escapeAttribute(value));
			sb.append("\"/>");
		}

		sb.append("<noscript>");
		sb.append("<input type=\"submit\" value=\"Please continue here...\"/>");
		sb.append("</noscript></form></body></html>");

		printWriter.write(sb.toString());

		printWriter.close();
	}

	protected void updatePassword(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, ThemeDisplay themeDisplay,
			Ticket ticket)
		throws Exception {

		AuthTokenUtil.checkCSRFToken(
			httpServletRequest, UpdatePasswordAction.class.getName());

		long userId = 0;

		if (ticket != null) {
			userId = ticket.getClassPK();
		}
		else {
			userId = themeDisplay.getUserId();
		}

		String password1 = httpServletRequest.getParameter("password1");
		String password2 = httpServletRequest.getParameter("password2");
		boolean passwordReset = false;

		boolean previousValidate = PwdToolkitUtilThreadLocal.isValidate();

		try {
			boolean currentValidate = isValidatePassword(httpServletRequest);

			PwdToolkitUtilThreadLocal.setValidate(currentValidate);

			UserLocalServiceUtil.updatePassword(
				userId, password1, password2, passwordReset);
		}
		finally {
			PwdToolkitUtilThreadLocal.setValidate(previousValidate);
		}

		if (ticket != null) {
			TicketLocalServiceUtil.deleteTicket(ticket);

			UserLocalServiceUtil.updatePasswordReset(userId, false);
		}

		User user = UserLocalServiceUtil.getUser(userId);

		Company company = CompanyLocalServiceUtil.getCompanyById(
			user.getCompanyId());

		String login = null;

		String authType = company.getAuthType();

		if (authType.equals(CompanyConstants.AUTH_TYPE_EA)) {
			login = user.getEmailAddress();
		}
		else if (authType.equals(CompanyConstants.AUTH_TYPE_SN)) {
			login = user.getScreenName();
		}
		else if (authType.equals(CompanyConstants.AUTH_TYPE_ID)) {
			login = String.valueOf(userId);
		}

		AuthenticatedSessionManagerUtil.login(
			httpServletRequest, httpServletResponse, login, password1, false,
			null);
	}

}