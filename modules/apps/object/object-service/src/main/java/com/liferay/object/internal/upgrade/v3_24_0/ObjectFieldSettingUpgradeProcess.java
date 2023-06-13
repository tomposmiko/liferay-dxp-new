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

package com.liferay.object.internal.upgrade.v3_24_0;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.dao.orm.common.SQLTransformer;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.uuid.PortalUUID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * @author Paulo Albuquerque
 */
public class ObjectFieldSettingUpgradeProcess extends UpgradeProcess {

	public ObjectFieldSettingUpgradeProcess(PortalUUID portalUUID) {
		_portalUUID = portalUUID;
	}

	@Override
	protected void doUpgrade() throws Exception {
		try (PreparedStatement preparedStatement1 = connection.prepareStatement(
			SQLTransformer.transform(
				StringBundler.concat(
					"select ObjectField.objectFieldId, ObjectField.companyId, ",
					"ObjectField.userId, ObjectField.userName, ",
					"ObjectRelationship.objectDefinitionId1, ",
					"ObjectDefinition.name from ObjectField inner join ",
					"ObjectRelationship on ObjectField.objectFieldId = ",
					"ObjectRelationship.objectFieldId2 inner join ",
					"ObjectDefinition on ObjectDefinition.objectDefinitionId ",
					"= ObjectRelationship.objectDefinitionId1 where ",
					"ObjectField.businessType = 'Relationship'")));

			 PreparedStatement preparedStatement2 =
				 AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					 connection,
					 StringBundler.concat(
						 "insert into ObjectFieldSetting (uuid_, ",
						 "objectFieldSettingId, companyId, userId, userName, ",
						 "createDate, modifiedDate, objectFieldId, name, ",
						 "value) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"));

			ResultSet resultSet = preparedStatement1.executeQuery()) {

			while (resultSet.next()) {
				preparedStatement2.setString(1, _portalUUID.generate());
				preparedStatement2.setLong(2, increment());
				preparedStatement2.setLong(3, resultSet.getLong("companyId"));
				preparedStatement2.setLong(4, resultSet.getLong("userId"));
				preparedStatement2.setString(
					5, resultSet.getString("userName"));

				Timestamp timestamp = new Timestamp(System.currentTimeMillis());

				preparedStatement2.setTimestamp(6, timestamp);
				preparedStatement2.setTimestamp(7, timestamp);

				preparedStatement2.setLong(
					8, resultSet.getLong("objectFieldId"));
				preparedStatement2.setString(9, "ObjectDefinition1ShortName");

				String shortName = resultSet.getString("name");

				if (shortName.startsWith("C_")) {
					shortName = shortName.substring(2);
				}

				preparedStatement2.setString(10, shortName);

				preparedStatement2.addBatch();
			}

			preparedStatement2.executeBatch();
		}
	}

	private final PortalUUID _portalUUID;

}