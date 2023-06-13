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

package com.liferay.portal.upgrade.v7_0_0;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.PropsValues;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 * @author Amadea Fejes
 */
public class UpgradeMySQL extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		DB db = DBManagerUtil.getDB();

		if ((db.getDBType() == DBType.MARIADB) ||
			(db.getDBType() == DBType.MYSQL)) {

			upgradeDatetimePrecision();
			upgradeTableEngine();
		}
	}

	protected String getActualColumnType(
			Statement statement, String tableName, String columnName)
		throws SQLException {

		StringBundler sb = new StringBundler(5);

		sb.append("show columns from ");
		sb.append(tableName);
		sb.append(" like \"");
		sb.append(columnName);
		sb.append("\"");

		try (ResultSet rs = statement.executeQuery(sb.toString())) {
			if (!rs.next()) {
				throw new IllegalStateException(
					StringBundler.concat(
						"Table ", tableName, " does not have column ",
						columnName));
			}

			return rs.getString("Type");
		}
	}

	protected void upgradeDatetimePrecision() throws Exception {
		DatabaseMetaData databaseMetaData = connection.getMetaData();

		try (LoggingTimer loggingTimer = new LoggingTimer();
			Statement statement = connection.createStatement();
			ResultSet rs = databaseMetaData.getTables(null, null, null, null)) {

			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");

				if (!isPortal62TableName(tableName)) {
					continue;
				}

				upgradeDatetimePrecision(
					databaseMetaData, statement, rs.getString("TABLE_CAT"),
					rs.getString("TABLE_SCHEM"), tableName);
			}
		}
	}

	protected void upgradeDatetimePrecision(
			DatabaseMetaData databaseMetaData, Statement statement,
			String catalog, String schemaPattern, String tableName)
		throws SQLException {

		try (ResultSet rs = databaseMetaData.getColumns(
				catalog, schemaPattern, tableName, null)) {

			String modifyClause = StringPool.BLANK;

			while (rs.next()) {
				if (Types.TIMESTAMP != rs.getInt("DATA_TYPE")) {
					continue;
				}

				String columnName = rs.getString("COLUMN_NAME");

				String actualColumnType = getActualColumnType(
					statement, tableName, columnName);

				if (actualColumnType.equals("datetime(6)")) {
					continue;
				}

				if (!modifyClause.equals(StringPool.BLANK)) {
					modifyClause += StringPool.COMMA;
				}

				modifyClause +=
					StringBundler.concat(
						" MODIFY ", columnName, " datetime(6)");
			}

			if (modifyClause.equals(StringPool.BLANK)) {
				return;
			}

			if (_log.isInfoEnabled()) {
				_log.info(
					StringBundler.concat(
						"Updating columns for table ", tableName,
						" to datetime(6)"));
			}

			statement.executeUpdate(
				StringBundler.concat("ALTER TABLE ", tableName, modifyClause));
		}
	}

	protected void upgradeTableEngine() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("show table status")) {

			while (rs.next()) {
				String tableName = rs.getString("Name");

				if (!isPortal62TableName(tableName)) {
					continue;
				}

				String comment = GetterUtil.getString(rs.getString("Comment"));

				if (StringUtil.equalsIgnoreCase(comment, "VIEW")) {
					continue;
				}

				String engine = GetterUtil.getString(rs.getString("Engine"));

				if (StringUtil.equalsIgnoreCase(
						engine, PropsValues.DATABASE_MYSQL_ENGINE)) {

					continue;
				}

				if (_log.isInfoEnabled()) {
					_log.info(
						StringBundler.concat(
							"Updating table ", tableName, " to use engine ",
							PropsValues.DATABASE_MYSQL_ENGINE));
				}

				statement.executeUpdate(
					StringBundler.concat(
						"alter table ", tableName, " engine ",
						PropsValues.DATABASE_MYSQL_ENGINE));
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(UpgradeMySQL.class);

}