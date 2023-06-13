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

package com.liferay.client.extension.internal.upgrade.v3_1_0;

import com.liferay.client.extension.constants.ClientExtensionEntryConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Objects;

/**
 * @author Brian Wing Shun Chan
 */
public class ClientExtensionEntryUpgradeProcess extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		if (hasColumn("ClientExtensionEntry", "typeSettings")) {
			return;
		}

		alterTableAddColumn(
			"ClientExtensionEntry", "typeSettings", "TEXT null");

		String selectSQL = StringBundler.concat(
			"select clientExtensionEntryId, customElementCSSURLs, ",
			"customElementHTMLElementName, customElementURLs, ",
			"customElementUseESM, friendlyURLMapping, iFrameURL, ",
			"instanceable, portletCategoryName, type_ from ",
			"ClientExtensionEntry");
		String updateSQL =
			"update ClientExtensionEntry set typeSettings = ? where " +
				"clientExtensionEntryId = ?";

		try (Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(selectSQL);
			PreparedStatement preparedStatement =
				AutoBatchPreparedStatementUtil.autoBatch(
					connection, updateSQL)) {

			while (resultSet.next()) {
				String type = resultSet.getString("type_");

				String typeSettings = _getTypeSettings(resultSet, type);

				if (typeSettings == null) {
					_log.error("Unknown client extension entry type " + type);

					continue;
				}

				preparedStatement.setString(1, typeSettings);

				preparedStatement.setLong(
					2, resultSet.getLong("clientExtensionEntryId"));

				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();
		}
		catch (Exception exception) {
			_log.error(exception);
		}

		alterTableDropColumn("ClientExtensionEntry", "customElementCSSURLs");
		alterTableDropColumn(
			"ClientExtensionEntry", "customElementHTMLElementName");
		alterTableDropColumn("ClientExtensionEntry", "customElementURLs");
		alterTableDropColumn("ClientExtensionEntry", "customElementUseESM");
		alterTableDropColumn("ClientExtensionEntry", "friendlyURLMapping");
		alterTableDropColumn("ClientExtensionEntry", "iFrameURL");
		alterTableDropColumn("ClientExtensionEntry", "instanceable");
		alterTableDropColumn("ClientExtensionEntry", "portletCategoryName");
	}

	private String _getTypeSettings(ResultSet resultSet, String type)
		throws Exception {

		String friendlyURLMapping = resultSet.getString("friendlyURLMapping");
		boolean instanceable = resultSet.getBoolean("instanceable");
		String portletCategoryName = resultSet.getString("portletCategoryName");

		if (Objects.equals(
				type, ClientExtensionEntryConstants.TYPE_CUSTOM_ELEMENT)) {

			return UnicodePropertiesBuilder.create(
				true
			).put(
				"cssURLs", resultSet.getString("customElementCSSURLs")
			).put(
				"friendlyURLMapping", friendlyURLMapping
			).put(
				"htmlElementName",
				resultSet.getString("customElementHTMLElementName")
			).put(
				"instanceable", instanceable
			).put(
				"portletCategoryName", portletCategoryName
			).put(
				"urls", resultSet.getString("customElementURLs")
			).put(
				"useESM", resultSet.getBoolean("customElementUseESM")
			).buildString();
		}
		else if (Objects.equals(
					type, ClientExtensionEntryConstants.TYPE_IFRAME)) {

			return UnicodePropertiesBuilder.create(
				true
			).put(
				"friendlyURLMapping", friendlyURLMapping
			).put(
				"instanceable", instanceable
			).put(
				"portletCategoryName", portletCategoryName
			).put(
				"url", resultSet.getString("iFrameURL")
			).buildString();
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ClientExtensionEntryUpgradeProcess.class);

}