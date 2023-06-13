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

package com.liferay.commerce.internal.upgrade.v2_2_0;

import com.liferay.account.constants.AccountConstants;
import com.liferay.account.model.AccountEntry;
import com.liferay.account.service.AccountEntryLocalService;
import com.liferay.account.service.AccountEntryUserRelLocalService;
import com.liferay.commerce.model.impl.CommerceOrderModelImpl;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeProcessFactory;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Ethan Bustad
 */
public class CommerceOrderUpgradeProcess extends UpgradeProcess {

	public CommerceOrderUpgradeProcess(
		AccountEntryLocalService accountEntryLocalService,
		AccountEntryUserRelLocalService accountEntryUserRelLocalService,
		UserLocalService userLocalService) {

		_accountEntryLocalService = accountEntryLocalService;
		_accountEntryUserRelLocalService = accountEntryUserRelLocalService;
		_userLocalService = userLocalService;
	}

	@Override
	protected void doUpgrade() throws Exception {
		if (hasColumn(CommerceOrderModelImpl.TABLE_NAME, "siteGroupId")) {
			runSQL("update CommerceOrder set groupId = siteGroupId");

			alterTableDropColumn(
				CommerceOrderModelImpl.TABLE_NAME, "siteGroupId");
		}

		if (!hasColumn(
				CommerceOrderModelImpl.TABLE_NAME, "orderOrganizationId") ||
			!hasColumn(CommerceOrderModelImpl.TABLE_NAME, "orderUserId")) {

			return;
		}

		String updateCommerceOrderSQL1 =
			"update CommerceOrder set commerceAccountId = ? where " +
				"orderOrganizationId = ?";
		String updateCommerceOrderSQL2 =
			"update CommerceOrder set commerceAccountId = ? where " +
				"orderUserId = ?";

		try (PreparedStatement preparedStatement1 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection, updateCommerceOrderSQL1);
			PreparedStatement preparedStatement2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection, updateCommerceOrderSQL2);
			Statement s = connection.createStatement(
				ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = s.executeQuery(
				"select distinct orderOrganizationId, orderUserId from " +
					"CommerceOrder")) {

			while (resultSet.next()) {
				long orderOrganizationId = resultSet.getLong(
					"orderOrganizationId");
				long orderUserId = resultSet.getLong("orderUserId");

				if (orderOrganizationId > 0) {
					long commerceAccountId = _getCommerceAccountId(
						orderOrganizationId);

					if (commerceAccountId == 0) {
						_log.error(
							"No CommerceAccount for orderOrganizationId " +
								orderOrganizationId);

						continue;
					}

					preparedStatement1.setLong(1, commerceAccountId);
					preparedStatement1.setLong(2, orderOrganizationId);

					preparedStatement1.execute();
				}
				else if (orderUserId > 0) {
					User user = _userLocalService.getUser(orderUserId);

					ServiceContext serviceContext = new ServiceContext();

					serviceContext.setCompanyId(user.getCompanyId());
					serviceContext.setUserId(user.getUserId());

					AccountEntry accountEntry =
						_accountEntryLocalService.addAccountEntry(
							user.getUserId(),
							AccountConstants.PARENT_ACCOUNT_ENTRY_ID_DEFAULT,
							user.getFullName(), null, null,
							user.getEmailAddress(), null, StringPool.BLANK,
							AccountConstants.ACCOUNT_ENTRY_TYPE_PERSON,
							WorkflowConstants.STATUS_APPROVED, serviceContext);

					_accountEntryUserRelLocalService.addAccountEntryUserRel(
						accountEntry.getAccountEntryId(), user.getUserId());

					preparedStatement2.setLong(
						1, accountEntry.getAccountEntryId());

					preparedStatement2.setLong(2, orderUserId);

					preparedStatement2.execute();
				}
			}

			preparedStatement1.executeBatch();
			preparedStatement2.executeBatch();
		}
	}

	@Override
	protected UpgradeStep[] getPostUpgradeSteps() {
		return new UpgradeStep[] {
			UpgradeProcessFactory.dropColumns(
				CommerceOrderModelImpl.TABLE_NAME, "orderOrganizationId",
				"orderUserId")
		};
	}

	@Override
	protected UpgradeStep[] getPreUpgradeSteps() {
		return new UpgradeStep[] {
			UpgradeProcessFactory.addColumns(
				"CommerceOrder", "commerceAccountId LONG")
		};
	}

	private long _getCommerceAccountId(long organizationId)
		throws SQLException {

		String sql =
			"select commerceAccountId from CommerceAccountOrganizationRel " +
				"where organizationId = " + organizationId;

		try (Statement s = connection.createStatement();
			ResultSet resultSet = s.executeQuery(sql)) {

			if (resultSet.next()) {
				return resultSet.getLong("commerceAccountId");
			}
		}

		return 0;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceOrderUpgradeProcess.class);

	private final AccountEntryLocalService _accountEntryLocalService;
	private final AccountEntryUserRelLocalService
		_accountEntryUserRelLocalService;
	private final UserLocalService _userLocalService;

}