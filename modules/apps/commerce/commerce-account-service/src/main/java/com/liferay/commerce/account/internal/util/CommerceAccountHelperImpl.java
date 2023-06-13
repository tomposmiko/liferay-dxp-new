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

package com.liferay.commerce.account.internal.util;

import com.liferay.account.constants.AccountConstants;
import com.liferay.account.constants.AccountPortletKeys;
import com.liferay.account.exception.AccountEntryTypeException;
import com.liferay.account.manager.CurrentAccountEntryManager;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.model.AccountEntryModel;
import com.liferay.account.model.AccountEntryUserRel;
import com.liferay.account.model.AccountGroupRel;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountEntryUserRelLocalService;
import com.liferay.account.service.AccountGroupRelLocalService;
import com.liferay.commerce.account.configuration.CommerceAccountGroupServiceConfiguration;
import com.liferay.commerce.account.configuration.CommerceAccountServiceConfiguration;
import com.liferay.commerce.account.constants.CommerceAccountConstants;
import com.liferay.commerce.account.util.CommerceAccountHelper;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupRoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.servlet.PortalSessionThreadLocal;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
@Component(service = CommerceAccountHelper.class)
public class CommerceAccountHelperImpl implements CommerceAccountHelper {

	@Override
	public AccountEntryUserRel addAccountEntryUserRel(
			long accountEntryId, long accountEntryUserId,
			ServiceContext serviceContext)
		throws PortalException {

		AccountEntry accountEntry = _accountEntryLocalService.getAccountEntry(
			accountEntryId);

		if (accountEntry.isPersonalAccount()) {
			List<AccountEntryUserRel> accountEntryUserRels =
				_accountEntryUserRelLocalService.
					getAccountEntryUserRelsByAccountUserId(accountEntryUserId);

			for (AccountEntryUserRel accountEntryUserRel :
					accountEntryUserRels) {

				AccountEntry curAccountEntry =
					_accountEntryLocalService.getAccountEntry(
						accountEntryUserRel.getAccountEntryId());

				if (curAccountEntry.isPersonalAccount()) {
					throw new AccountEntryTypeException();
				}
			}
		}

		AccountEntryUserRel accountEntryUserRel =
			_accountEntryUserRelLocalService.addAccountEntryUserRel(
				accountEntryId, accountEntryUserId);

		addDefaultRoles(accountEntryUserId);

		return accountEntryUserRel;
	}

	@Override
	public void addAccountEntryUserRels(
			long accountEntryId, long[] userIds, String[] emailAddresses,
			long[] roleIds, ServiceContext serviceContext)
		throws PortalException {

		AccountEntry accountEntry = _accountEntryLocalService.getAccountEntry(
			accountEntryId);

		Group group = accountEntry.getAccountEntryGroup();

		if (group == null) {
			throw new PortalException();
		}

		if (userIds != null) {
			for (long userId : userIds) {
				User user = _userLocalService.getUser(userId);

				addAccountEntryUserRel(
					accountEntryId, user.getUserId(), serviceContext);

				if (!ArrayUtil.contains(
						user.getGroupIds(), group.getGroupId())) {

					_userLocalService.addGroupUsers(
						group.getGroupId(), new long[] {userId});
				}

				if (!ArrayUtil.contains(
						user.getGroupIds(), serviceContext.getScopeGroupId())) {

					_userLocalService.addGroupUsers(
						serviceContext.getScopeGroupId(), new long[] {userId});
				}

				if (roleIds != null) {
					_userGroupRoleLocalService.addUserGroupRoles(
						user.getUserId(), group.getGroupId(), roleIds);
				}
			}
		}

		if (emailAddresses != null) {
			for (String emailAddress : emailAddresses) {
				_accountEntryUserRelLocalService.inviteUser(
					accountEntryId, roleIds, emailAddress,
					_userLocalService.getUser(accountEntry.getUserId()),
					serviceContext);
			}
		}
	}

	@Override
	public void addDefaultRoles(long userId) throws PortalException {
		CommerceAccountServiceConfiguration
			commerceAccountServiceConfiguration =
				_configurationProvider.getSystemConfiguration(
					CommerceAccountServiceConfiguration.class);

		String[] siteRoles = commerceAccountServiceConfiguration.siteRoles();

		if ((siteRoles == null) && ArrayUtil.isEmpty(siteRoles)) {
			return;
		}

		User user = _userLocalService.getUser(userId);

		Set<Role> roles = new HashSet<>();

		for (String siteRole : siteRoles) {
			Role role = _roleLocalService.fetchRole(
				user.getCompanyId(), siteRole);

			if ((role == null) || (role.getType() != RoleConstants.TYPE_SITE)) {
				continue;
			}

			roles.add(role);
		}

		long[] roleIds = TransformUtil.transformToLongArray(
			roles, Role::getRoleId);

		for (AccountEntryUserRel accountEntryUserRel :
				_accountEntryUserRelLocalService.
					getAccountEntryUserRelsByAccountUserId(userId)) {

			AccountEntry accountEntry =
				_accountEntryLocalService.getAccountEntry(
					accountEntryUserRel.getAccountEntryId());

			_userGroupRoleLocalService.addUserGroupRoles(
				userId, accountEntry.getAccountEntryGroupId(), roleIds);
		}
	}

	@Override
	public int countUserCommerceAccounts(
			long userId, long commerceChannelGroupId)
		throws PortalException {

		return _accountEntryLocalService.getUserAccountEntriesCount(
			userId, AccountConstants.PARENT_ACCOUNT_ENTRY_ID_DEFAULT,
			StringPool.BLANK, getAccountEntryTypes(commerceChannelGroupId),
			WorkflowConstants.STATUS_ANY);
	}

	@Override
	public String[] getAccountEntryTypes(long commerceChannelGroupId)
		throws ConfigurationException {

		return toAccountEntryTypes(getCommerceSiteType(commerceChannelGroupId));
	}

	@Override
	public String getAccountManagementPortletURL(
			HttpServletRequest httpServletRequest)
		throws PortalException {

		long groupId = _portal.getScopeGroupId(httpServletRequest);

		long plid = _portal.getPlidFromPortletId(
			groupId, AccountPortletKeys.ACCOUNT_ENTRIES_MANAGEMENT);

		if (plid > 0) {
			PortletURL portletURL = _portletURLFactory.create(
				httpServletRequest,
				AccountPortletKeys.ACCOUNT_ENTRIES_MANAGEMENT, plid,
				PortletRequest.RENDER_PHASE);

			return portletURL.toString();
		}

		return StringPool.BLANK;
	}

	@Override
	public long[] getCommerceAccountGroupIds(long commerceAccountId) {
		List<AccountGroupRel> accountGroupRels =
			_accountGroupRelLocalService.getAccountGroupRels(
				AccountEntry.class.getName(), commerceAccountId);

		if (accountGroupRels.isEmpty()) {
			return new long[0];
		}

		return ArrayUtil.sortedUnique(
			TransformUtil.transformToLongArray(
				accountGroupRels, AccountGroupRel::getAccountGroupId));
	}

	@Override
	public int getCommerceSiteType(long commerceChannelGroupId)
		throws ConfigurationException {

		CommerceAccountGroupServiceConfiguration
			commerceAccountGroupServiceConfiguration =
				_configurationProvider.getConfiguration(
					CommerceAccountGroupServiceConfiguration.class,
					new GroupServiceSettingsLocator(
						commerceChannelGroupId,
						CommerceAccountConstants.SERVICE_NAME));

		return commerceAccountGroupServiceConfiguration.commerceSiteType();
	}

	@Override
	public AccountEntry getCurrentAccountEntry(
			long commerceChannelGroupId, HttpServletRequest httpServletRequest)
		throws PortalException {

		CommerceChannel commerceChannel =
			_commerceChannelLocalService.getCommerceChannelByGroupId(
				commerceChannelGroupId);
		long userId = _portal.getUserId(httpServletRequest);

		AccountEntry accountEntry =
			_currentAccountEntryManager.getCurrentAccountEntry(
				commerceChannel.getSiteGroupId(), userId);

		if ((accountEntry == null) ||
			(accountEntry.getStatus() != WorkflowConstants.STATUS_APPROVED)) {

			int commerceSiteType = getCommerceSiteType(commerceChannelGroupId);

			if ((commerceSiteType == CommerceAccountConstants.SITE_TYPE_B2C) ||
				(commerceSiteType == CommerceAccountConstants.SITE_TYPE_B2X)) {

				accountEntry =
					_accountEntryLocalService.fetchPersonAccountEntry(userId);

				if (accountEntry == null) {
					User user = _userLocalService.getUser(userId);

					ServiceContext serviceContext = new ServiceContext();

					serviceContext.setCompanyId(user.getCompanyId());
					serviceContext.setUserId(userId);

					accountEntry = _accountEntryLocalService.addAccountEntry(
						userId, AccountConstants.ACCOUNT_ENTRY_ID_DEFAULT,
						user.getFullName(), null, null, user.getEmailAddress(),
						null, StringPool.BLANK,
						AccountConstants.ACCOUNT_ENTRY_TYPE_PERSON,
						WorkflowConstants.STATUS_APPROVED, serviceContext);

					addAccountEntryUserRel(
						accountEntry.getAccountEntryId(), userId,
						serviceContext);
				}
			}

			if (accountEntry == null) {
				setCurrentCommerceAccount(
					httpServletRequest, commerceChannelGroupId,
					AccountConstants.ACCOUNT_ENTRY_ID_GUEST);
			}
			else {
				setCurrentCommerceAccount(
					httpServletRequest, commerceChannelGroupId,
					accountEntry.getAccountEntryId());
			}
		}
		else {
			setCurrentCommerceAccount(
				httpServletRequest, commerceChannelGroupId,
				accountEntry.getAccountEntryId());
		}

		return accountEntry;
	}

	@Override
	public long[] getUserCommerceAccountIds(
			long userId, long commerceChannelGroupId)
		throws PortalException {

		List<AccountEntry> accountEntries =
			_accountEntryLocalService.getUserAccountEntries(
				userId, AccountConstants.PARENT_ACCOUNT_ENTRY_ID_DEFAULT,
				StringPool.BLANK, getAccountEntryTypes(commerceChannelGroupId),
				WorkflowConstants.STATUS_ANY, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS);

		return ListUtil.toLongArray(
			accountEntries, AccountEntryModel::getAccountEntryId);
	}

	@Override
	public void setCurrentCommerceAccount(
			HttpServletRequest httpServletRequest, long commerceChannelGroupId,
			long commerceAccountId)
		throws PortalException {

		if (commerceAccountId > 0) {
			_checkAccountType(commerceChannelGroupId, commerceAccountId);
		}

		if (PortalSessionThreadLocal.getHttpSession() == null) {
			PortalSessionThreadLocal.setHttpSession(
				httpServletRequest.getSession());
		}

		CommerceChannel commerceChannel =
			_commerceChannelLocalService.getCommerceChannelByGroupId(
				commerceChannelGroupId);

		long userId = _portal.getUserId(httpServletRequest);

		_currentAccountEntryManager.setCurrentAccountEntry(
			commerceAccountId, commerceChannel.getGroupId(), userId);
		_currentAccountEntryManager.setCurrentAccountEntry(
			commerceAccountId, commerceChannel.getSiteGroupId(), userId);
	}

	@Override
	public String[] toAccountEntryTypes(int commerceSiteType) {
		if (commerceSiteType == CommerceAccountConstants.SITE_TYPE_B2B) {
			return new String[] {AccountConstants.ACCOUNT_ENTRY_TYPE_BUSINESS};
		}
		else if (commerceSiteType == CommerceAccountConstants.SITE_TYPE_B2C) {
			return new String[] {AccountConstants.ACCOUNT_ENTRY_TYPE_PERSON};
		}
		else if (commerceSiteType == CommerceAccountConstants.SITE_TYPE_B2X) {
			return new String[] {
				AccountConstants.ACCOUNT_ENTRY_TYPE_BUSINESS,
				AccountConstants.ACCOUNT_ENTRY_TYPE_PERSON
			};
		}

		return null;
	}

	private void _checkAccountType(
			long commerceChannelGroupId, long commerceAccountId)
		throws PortalException {

		int commerceSiteType = getCommerceSiteType(commerceChannelGroupId);

		AccountEntry accountEntry = _accountEntryLocalService.getAccountEntry(
			commerceAccountId);

		if ((commerceSiteType == CommerceAccountConstants.SITE_TYPE_B2C) &&
			accountEntry.isBusinessAccount()) {

			throw new PortalException(
				"Only personal accounts are allowed in a b2c site");
		}

		if ((commerceSiteType == CommerceAccountConstants.SITE_TYPE_B2B) &&
			accountEntry.isPersonalAccount()) {

			throw new PortalException(
				"Only business accounts are allowed in a b2b site");
		}
	}

	@Reference
	private AccountEntryLocalService _accountEntryLocalService;

	@Reference
	private AccountEntryUserRelLocalService _accountEntryUserRelLocalService;

	@Reference
	private AccountGroupRelLocalService _accountGroupRelLocalService;

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private CurrentAccountEntryManager _currentAccountEntryManager;

	@Reference
	private Portal _portal;

	@Reference
	private PortletURLFactory _portletURLFactory;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference
	private UserGroupRoleLocalService _userGroupRoleLocalService;

	@Reference
	private UserLocalService _userLocalService;

}