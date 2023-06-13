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

package com.liferay.portal.security.sso.openid.connect.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.UserEmailAddressException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.sso.openid.connect.OpenIdConnectServiceException;
import com.liferay.portal.security.sso.openid.connect.internal.exception.StrangersNotAllowedException;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(immediate = true, service = OpenIdConnectUserInfoProcessor.class)
public class OpenIdConnectUserInfoProcessorImpl
	implements OpenIdConnectUserInfoProcessor {

	@Override
	public long processUserInfo(
			UserInfo userInfo, long companyId, String issuer, String mainPath,
			String portalURL)
		throws PortalException {

		String emailAddress = userInfo.getEmailAddress();

		User user = _userLocalService.fetchUserByEmailAddress(
			companyId, emailAddress);

		if (user != null) {
			return user.getUserId();
		}

		_checkAddUser(companyId, emailAddress);

		String firstName = userInfo.getGivenName();
		String lastName = userInfo.getFamilyName();

		if (Validator.isNull(firstName) || Validator.isNull(lastName) ||
			Validator.isNull(emailAddress)) {

			throw new OpenIdConnectServiceException.UserMappingException(
				StringBundler.concat(
					"Unable to map OpenId Connect user to the portal, missing ",
					"or invalid profile information: {emailAddresss=",
					emailAddress, ", firstName=", firstName, ", lastName=",
					lastName, "}"));
		}

		long creatorUserId = 0;
		boolean autoPassword = true;
		String password1 = null;
		String password2 = null;
		boolean autoScreenName = true;
		String screenName = StringPool.BLANK;

		Company company = _companyLocalService.getCompany(companyId);

		Locale locale = company.getLocale();

		long prefixId = 0;
		long suffixId = 0;
		boolean male = true;
		int birthdayMonth = Calendar.JANUARY;
		int birthdayDay = 1;
		int birthdayYear = 1970;
		String jobTitle = StringPool.BLANK;
		long[] groupIds = null;
		long[] organizationIds = null;
		long[] roleIds = _getRoleIds(companyId, issuer);
		long[] userGroupIds = null;
		boolean sendEmail = false;

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setPathMain(mainPath);
		serviceContext.setPortalURL(portalURL);

		user = _userLocalService.addUser(
			creatorUserId, companyId, autoPassword, password1, password2,
			autoScreenName, screenName, emailAddress, locale, firstName,
			userInfo.getMiddleName(), lastName, prefixId, suffixId, male,
			birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds,
			organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);

		user = _userLocalService.updatePasswordReset(user.getUserId(), false);

		return user.getUserId();
	}

	private void _checkAddUser(long companyId, String emailAddress)
		throws PortalException {

		Company company = _companyLocalService.getCompany(companyId);

		if (!company.isStrangers()) {
			throw new StrangersNotAllowedException(companyId);
		}

		if (!company.isStrangersWithMx() &&
			company.hasCompanyMx(emailAddress)) {

			throw new UserEmailAddressException.MustNotUseCompanyMx(
				emailAddress);
		}
	}

	private long[] _getRoleIds(long companyId, String issuer) {
		if (Validator.isNull(issuer) ||
			Objects.equals(
				issuer,
				_props.get(
					"open.id.connect.user.info.processor.impl.issuer"))) {

			return null;
		}

		String roleName = _props.get(
			"open.id.connect.user.info.processor.impl.regular.role");

		if (Validator.isNull(roleName)) {
			return null;
		}

		Role role = _roleLocalService.fetchRole(companyId, roleName);

		if (role == null) {
			return null;
		}

		if (role.getType() == RoleConstants.TYPE_REGULAR) {
			return new long[] {role.getRoleId()};
		}

		if (_log.isInfoEnabled()) {
			_log.info("Role " + roleName + " is not a regular role");
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		OpenIdConnectUserInfoProcessorImpl.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private Props _props;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private UserLocalService _userLocalService;

}