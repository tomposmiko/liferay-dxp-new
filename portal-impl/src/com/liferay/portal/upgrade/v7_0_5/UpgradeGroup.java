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

package com.liferay.portal.upgrade.v7_0_5;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.TreeModel;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LoggingTimer;

import java.io.Serializable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Roberto Díaz
 */
public class UpgradeGroup extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		updateParentGroup();
	}

	protected void updateParentGroup() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			StringBundler sb = new StringBundler(7);

			sb.append("select stagingGroup_.groupId, ");
			sb.append("liveGroup_.parentGroupId from Group_ stagingGroup_ ");
			sb.append("inner join Group_ liveGroup_ on (liveGroup_.groupId = ");
			sb.append("stagingGroup_.liveGroupId) where ");
			sb.append("(stagingGroup_.remoteStagingGroupCount = 0) and ");
			sb.append("(liveGroup_.parentGroupId != ");
			sb.append("stagingGroup_.parentGroupId)");

			try (PreparedStatement ps1 = connection.prepareStatement(
					sb.toString());
				PreparedStatement ps2 = connection.prepareStatement(
					"select treePath from Group_ where groupId = ?");
				PreparedStatement ps3 =
					AutoBatchPreparedStatementUtil.concurrentAutoBatch(
						connection,
						"update Group_ set parentGroupId = ?, treePath = ? " +
							"where groupId = ?");
				ResultSet rs1 = ps1.executeQuery()) {

				while (rs1.next()) {
					long groupId = rs1.getLong(1);

					long parentGroupId = rs1.getLong(2);

					ps2.setLong(1, parentGroupId);

					try (ResultSet rs2 = ps2.executeQuery()) {
						String treePath = null;

						if (rs2.next()) {
							treePath = rs2.getString("treePath");

							treePath = treePath.concat(String.valueOf(groupId));

							treePath = treePath.concat(StringPool.SLASH);
						}
						else {
							if (_log.isWarnEnabled()) {
								_log.warn(
									"Unable to find group " + parentGroupId);
							}

							StringBundler treePathSB = new StringBundler(5);

							treePathSB.append(StringPool.SLASH);
							treePathSB.append(parentGroupId);
							treePathSB.append(StringPool.SLASH);
							treePathSB.append(groupId);
							treePathSB.append(StringPool.SLASH);

							treePath = treePathSB.toString();
						}

						ps3.setLong(1, parentGroupId);
						ps3.setString(2, treePath);

						ps3.setLong(3, groupId);

						ps3.addBatch();
					}
				}

				ps3.executeBatch();
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(UpgradeGroup.class);

	private class GroupTreeModel implements TreeModel {

		public GroupTreeModel(PreparedStatement ps) {
			_ps = ps;
		}

		@Override
		public String buildTreePath() throws PortalException {
			return null;
		}

		@Override
		public Serializable getPrimaryKeyObj() {
			return _groupId;
		}

		@Override
		public String getTreePath() {
			return null;
		}

		public void setPrimaryKeyObj(Serializable primaryKeyObj) {
			_groupId = (Long)primaryKeyObj;
		}

		@Override
		public void updateTreePath(String treePath) {
			try {
				_ps.setString(1, treePath);
				_ps.setLong(2, _groupId);

				_ps.addBatch();
			}
			catch (SQLException sqlException) {
				_log.error(
					"Unable to update tree path: " + treePath, sqlException);
			}
		}

		private long _groupId;
		private final PreparedStatement _ps;

	}

}