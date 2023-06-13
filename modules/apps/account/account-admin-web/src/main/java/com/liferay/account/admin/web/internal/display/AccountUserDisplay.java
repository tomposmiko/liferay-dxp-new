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

package com.liferay.account.admin.web.internal.display;

import com.liferay.account.constants.AccountConstants;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.model.AccountEntryUserRel;
import com.liferay.account.service.AccountEntryLocalServiceUtil;
import com.liferay.account.service.AccountEntryUserRelLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.string.StringUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Pei-Jung Lan
 */
public class AccountUserDisplay {

	public static AccountUserDisplay of(User user) {
		if (user == null) {
			return null;
		}

		return new AccountUserDisplay(user);
	}

	public String getAccountNames(HttpServletRequest httpServletRequest) {
		List<AccountEntryUserRel> accountEntryUserRels =
			_getAccountEntryUserRels(getUserId());

		if (ListUtil.isEmpty(accountEntryUserRels)) {
			return LanguageUtil.get(httpServletRequest, "no-assigned-account");
		}

		List<String> accountEntryNames = TransformUtil.transform(
			accountEntryUserRels,
			accountEntryUserRel -> {
				AccountEntry accountEntry =
					AccountEntryLocalServiceUtil.fetchAccountEntry(
						accountEntryUserRel.getAccountEntryId());

				if (accountEntry == null) {
					return null;
				}

				return accountEntry.getName();
			});

		return StringUtil.merge(
			ListUtil.sort(accountEntryNames), StringPool.COMMA_AND_SPACE);
	}

	public String getAccountNamesStyle() {
		return _accountNamesStyle;
	}

	public String getAccountRoles() {
		return _accountRoles;
	}

	public String getEmailAddress() {
		return _emailAddress;
	}

	public String getJobTitle() {
		return _jobTitle;
	}

	public String getName() {
		return _name;
	}

	public int getStatus() {
		return _status;
	}

	public String getStatusLabel() {
		return _statusLabel;
	}

	public String getStatusLabelStyle() {
		return _statusLabelStyle;
	}

	public long getUserId() {
		return _userId;
	}

	private AccountUserDisplay(User user) {
		_accountNamesStyle = _getAccountNamesStyle(user.getUserId());
		_accountRoles = StringPool.BLANK;
		_emailAddress = user.getEmailAddress();
		_jobTitle = user.getJobTitle();
		_name = user.getFullName();
		_status = user.getStatus();
		_statusLabel = _getStatusLabel(user);
		_statusLabelStyle = _getStatusLabelStyle(user);
		_userId = user.getUserId();
	}

	private List<AccountEntryUserRel> _getAccountEntryUserRels(long userId) {
		List<AccountEntryUserRel> accountEntryUserRels =
			AccountEntryUserRelLocalServiceUtil.
				getAccountEntryUserRelsByAccountUserId(userId);

		return ListUtil.filter(
			accountEntryUserRels,
			accountEntryUserRel -> !Objects.equals(
				accountEntryUserRel.getAccountEntryId(),
				AccountConstants.ACCOUNT_ENTRY_ID_DEFAULT));
	}

	private String _getAccountNamesStyle(long userId) {
		List<AccountEntryUserRel> accountEntryUserRels =
			_getAccountEntryUserRels(userId);

		if (ListUtil.isEmpty(accountEntryUserRels)) {
			return "font-italic text-muted";
		}

		return StringPool.BLANK;
	}

	private String _getStatusLabel(User user) {
		int status = user.getStatus();

		if (status == WorkflowConstants.STATUS_APPROVED) {
			return "active";
		}

		if (status == WorkflowConstants.STATUS_INACTIVE) {
			return "inactive";
		}

		return StringPool.BLANK;
	}

	private String _getStatusLabelStyle(User user) {
		String status = _getStatusLabel(user);

		if (status.equals("active")) {
			return "success";
		}

		if (status.equals("inactive")) {
			return "secondary";
		}

		return StringPool.BLANK;
	}

	private final String _accountNamesStyle;
	private final String _accountRoles;
	private final String _emailAddress;
	private final String _jobTitle;
	private final String _name;
	private final int _status;
	private final String _statusLabel;
	private final String _statusLabelStyle;
	private final long _userId;

}