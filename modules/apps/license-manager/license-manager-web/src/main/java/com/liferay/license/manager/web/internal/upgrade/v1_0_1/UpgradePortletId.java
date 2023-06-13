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

package com.liferay.license.manager.web.internal.upgrade.v1_0_1;

import com.liferay.license.manager.web.internal.constants.LicenseManagerPortletKeys;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.upgrade.BasePortletIdUpgradeProcess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author David Zhang
 * @author Alberto Chaparro
 */
public class UpgradePortletId extends BasePortletIdUpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"select id_ from Portlet where portletId = '176'");
			ResultSet resultSet = preparedStatement.executeQuery()) {

			if (resultSet.next()) {
				_removeDuplicatePortletPreferences();
				_removeDuplicateResourcePermissions();

				runSQL(
					"delete from Portlet where portletId = '" +
						LicenseManagerPortletKeys.LICENSE_MANAGER + "'");

				super.doUpgrade();
			}
		}
	}

	@Override
	protected String[][] getRenamePortletIdsArray() {
		return new String[][] {
			{"176", LicenseManagerPortletKeys.LICENSE_MANAGER}
		};
	}

	private void _removeDuplicatePortletPreferences() throws SQLException {
		try (PreparedStatement preparedStatement1 = connection.prepareStatement(
				"select ownerId, ownerType, plid from PortletPreferences " +
					"where portletId = '176'");
			ResultSet resultSet = preparedStatement1.executeQuery();
			PreparedStatement preparedStatement2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"delete from PortletPreferences where ownerId = ? and " +
						"ownerType = ? and plid = ? and portletId = ?")) {

			while (resultSet.next()) {
				preparedStatement2.setLong(1, resultSet.getLong(1));
				preparedStatement2.setInt(2, resultSet.getInt(2));
				preparedStatement2.setLong(3, resultSet.getLong(3));
				preparedStatement2.setString(
					4, LicenseManagerPortletKeys.LICENSE_MANAGER);

				preparedStatement2.addBatch();
			}

			preparedStatement2.executeBatch();
		}
	}

	private void _removeDuplicateResourcePermissions() throws SQLException {
		try (PreparedStatement preparedStatement1 = connection.prepareStatement(
				"select companyId, scope, primKey, roleId from " +
					"ResourcePermission where name = '176'");
			ResultSet resultSet = preparedStatement1.executeQuery();
			PreparedStatement preparedStatement2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"delete from ResourcePermission where companyId = ? and " +
						"name = ? and scope = ? and primkey = ? and roleId = " +
							"?")) {

			while (resultSet.next()) {
				preparedStatement2.setLong(1, resultSet.getLong(1));
				preparedStatement2.setString(
					2, LicenseManagerPortletKeys.LICENSE_MANAGER);
				preparedStatement2.setInt(3, resultSet.getInt(2));
				preparedStatement2.setString(4, resultSet.getString(3));
				preparedStatement2.setLong(5, resultSet.getLong(4));

				preparedStatement2.addBatch();
			}

			preparedStatement2.executeBatch();
		}
	}

}