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

package com.liferay.commerce.internal.events;

import com.liferay.account.constants.AccountConstants;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountEntryUserRelLocalService;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luca Pellizzon
 */
@Component(property = "key=login.events.post", service = LifecycleAction.class)
public class LoginPostAction extends Action {

	@Override
	public void run(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		try {
			Cookie[] cookies = httpServletRequest.getCookies();

			if (cookies == null) {
				return;
			}

			for (Cookie cookie : cookies) {
				String name = cookie.getName();

				if (name.startsWith(
						CommerceOrder.class.getName() + StringPool.POUND)) {

					HttpServletRequest originalHttpServletRequest =
						_portal.getOriginalServletRequest(httpServletRequest);

					HttpSession httpSession =
						originalHttpServletRequest.getSession();

					httpSession.setAttribute(name, cookie.getValue());

					_updateGuestCommerceOrder(
						cookie.getValue(),
						Long.valueOf(
							StringUtil.extractLast(name, StringPool.POUND)),
						httpServletRequest);

					break;
				}
			}
		}
		catch (Exception exception) {
			_log.error(exception);
		}
	}

	private void _updateGuestCommerceOrder(
			String commerceOrderUuid, long commerceChannelGroupId,
			HttpServletRequest httpServletRequest)
		throws Exception {

		CommerceOrder commerceOrder = null;

		try {
			commerceOrder =
				_commerceOrderLocalService.getCommerceOrderByUuidAndGroupId(
					commerceOrderUuid, commerceChannelGroupId);
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return;
		}

		if (commerceOrder.getCommerceAccountId() !=
				AccountConstants.ACCOUNT_ENTRY_ID_GUEST) {

			return;
		}

		long userId = _portal.getUserId(httpServletRequest);

		AccountEntry accountEntry =
			_accountEntryLocalService.fetchPersonAccountEntry(userId);

		if (accountEntry == null) {
			ServiceContext serviceContext = new ServiceContext();

			User user = _portal.getUser(httpServletRequest);

			serviceContext.setCompanyId(user.getCompanyId());

			serviceContext.setUserId(userId);

			accountEntry = _accountEntryLocalService.addAccountEntry(
				userId, AccountConstants.PARENT_ACCOUNT_ENTRY_ID_DEFAULT,
				user.getFullName(), null, null, user.getEmailAddress(), null,
				StringPool.BLANK, AccountConstants.ACCOUNT_ENTRY_TYPE_PERSON,
				WorkflowConstants.STATUS_APPROVED, serviceContext);

			_accountEntryUserRelLocalService.addAccountEntryUserRel(
				accountEntry.getAccountEntryId(), userId);
		}

		CommerceOrder userCommerceOrder =
			_commerceOrderLocalService.fetchCommerceOrder(
				accountEntry.getAccountEntryId(), commerceChannelGroupId,
				userId, CommerceOrderConstants.ORDER_STATUS_OPEN);

		if (userCommerceOrder != null) {
			CommerceContext commerceContext = _commerceContextFactory.create(
				_portal.getCompanyId(httpServletRequest),
				commerceChannelGroupId, userId,
				userCommerceOrder.getCommerceOrderId(),
				accountEntry.getAccountEntryId());

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				httpServletRequest);

			PermissionThreadLocal.setPermissionChecker(
				PermissionCheckerFactoryUtil.create(
					_portal.getUser(httpServletRequest)));

			_commerceOrderLocalService.mergeGuestCommerceOrder(
				userId, commerceOrder.getCommerceOrderId(),
				userCommerceOrder.getCommerceOrderId(), commerceContext,
				serviceContext);
		}
		else {
			_commerceOrderLocalService.updateAccount(
				commerceOrder.getCommerceOrderId(), userId,
				accountEntry.getAccountEntryId());
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LoginPostAction.class);

	@Reference
	private AccountEntryLocalService _accountEntryLocalService;

	@Reference
	private AccountEntryUserRelLocalService _accountEntryUserRelLocalService;

	@Reference
	private CommerceContextFactory _commerceContextFactory;

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private Portal _portal;

}