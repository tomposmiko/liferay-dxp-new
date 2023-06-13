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

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

				if (!_tableNames.contains(StringUtil.toLowerCase(tableName))) {
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

				StringBundler sb = new StringBundler(5);

				sb.append("ALTER TABLE ");
				sb.append(tableName);
				sb.append(" MODIFY ");
				sb.append(columnName);
				sb.append(" datetime(6)");

				String sql = sb.toString();

				if (_log.isInfoEnabled()) {
					_log.info(
						StringBundler.concat(
							"Updating table ", tableName, " column ",
							columnName, " to datetime(6)"));
				}

				statement.executeUpdate(sql);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(UpgradeMySQL.class);

	private static final Set<String> _tableNames = new HashSet<>(
		Arrays.asList(
			"account_", "address", "announcementsdelivery",
			"announcementsentry", "announcementsflag", "assetcategory",
			"assetcategoryproperty", "assetentries_assetcategories",
			"assetentries_assettags", "assetentry", "assetlink", "assettag",
			"assettagstats", "assetvocabulary", "backgroundtask", "blogsentry",
			"blogsstatsuser", "bookmarksentry", "bookmarksfolder",
			"browsertracker", "classname_", "clustergroup", "company",
			"contact_", "counter", "country", "ddlrecord", "ddlrecordset",
			"ddlrecordversion", "ddmcontent", "ddmstoragelink", "ddmstructure",
			"ddmstructurelink", "ddmtemplate", "dlcontent", "dlfileentry",
			"dlfileentrymetadata", "dlfileentrytype",
			"dlfileentrytypes_dlfolders", "dlfilerank", "dlfileshortcut",
			"dlfileversion", "dlfolder", "dlsyncevent", "emailaddress",
			"expandocolumn", "expandorow", "expandotable", "expandovalue",
			"exportimportconfiguration", "group_", "groups_orgs",
			"groups_roles", "groups_usergroups", "image", "journalarticle",
			"journalarticleimage", "journalarticleresource",
			"journalcontentsearch", "journalfeed", "journalfolder", "layout",
			"layoutbranch", "layoutfriendlyurl", "layoutprototype",
			"layoutrevision", "layoutset", "layoutsetbranch",
			"layoutsetprototype", "listtype", "lock_", "mbban", "mbcategory",
			"mbdiscussion", "mbmailinglist", "mbmessage", "mbstatsuser",
			"mbthread", "mbthreadflag", "mdraction", "mdrrule", "mdrrulegroup",
			"mdrrulegroupinstance", "membershiprequest", "organization_",
			"orggrouprole", "orglabor", "passwordpolicy", "passwordpolicyrel",
			"passwordtracker", "phone", "pluginsetting", "pollschoice",
			"pollsquestion", "pollsvote", "portalpreferences", "portlet",
			"portletitem", "portletpreferences", "ratingsentry", "ratingsstats",
			"recentlayoutbranch", "recentlayoutrevision",
			"recentlayoutsetbranch", "region", "release_", "repository",
			"repositoryentry", "resourceaction", "resourceblock",
			"resourceblockpermission", "resourcepermission",
			"resourcetypepermission", "role_", "servicecomponent",
			"shoppingcart", "shoppingcategory", "shoppingcoupon",
			"shoppingitem", "shoppingitemfield", "shoppingitemprice",
			"shoppingorder", "shoppingorderitem", "socialactivity",
			"socialactivityachievement", "socialactivitycounter",
			"socialactivitylimit", "socialactivityset", "socialactivitysetting",
			"socialrelation", "socialrequest", "subscription", "systemevent",
			"team", "ticket", "trashentry", "trashversion",
			"usernotificationdelivery", "user_", "usergroup",
			"usergroupgrouprole", "usergrouprole", "usergroups_teams",
			"useridmapper", "usernotificationevent", "users_groups",
			"users_orgs", "users_roles", "users_teams", "users_usergroups",
			"usertracker", "usertrackerpath", "virtualhost", "webdavprops",
			"website", "wikinode", "wikipage", "wikipageresource",
			"workflowdefinitionlink", "workflowinstancelink"));

}