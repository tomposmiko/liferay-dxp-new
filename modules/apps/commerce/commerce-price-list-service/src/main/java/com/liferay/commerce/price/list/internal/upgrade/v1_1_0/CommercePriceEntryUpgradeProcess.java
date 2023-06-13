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

package com.liferay.commerce.price.list.internal.upgrade.v1_1_0;

import com.liferay.commerce.price.list.internal.upgrade.base.BaseCommercePriceListUpgradeProcess;
import com.liferay.commerce.price.list.model.impl.CommercePriceEntryModelImpl;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.portal.kernel.dao.db.IndexMetadata;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.List;

/**
 * @author Alec Sloan
 * @author Alessio Antonio Rendina
 */
public class CommercePriceEntryUpgradeProcess
	extends BaseCommercePriceListUpgradeProcess {

	public CommercePriceEntryUpgradeProcess(
		CPDefinitionLocalService cpDefinitionLocalService,
		CPInstanceLocalService cpInstanceLocalService) {

		_cpDefinitionLocalService = cpDefinitionLocalService;
		_cpInstanceLocalService = cpInstanceLocalService;
	}

	@Override
	protected void doUpgrade() throws Exception {
		addColumn(
			CommercePriceEntryModelImpl.class,
			CommercePriceEntryModelImpl.TABLE_NAME, "CPInstanceUuid",
			"VARCHAR(75)");
		addColumn(
			CommercePriceEntryModelImpl.class,
			CommercePriceEntryModelImpl.TABLE_NAME, "CProductId", "LONG");

		_addIndexes(CommercePriceEntryModelImpl.TABLE_NAME);

		try (PreparedStatement ps = connection.prepareStatement(
				"update CommercePriceEntry set CProductId = ?," +
					"CPInstanceUuid = ? where CPInstanceId = ?");
			Statement s = connection.createStatement();
			ResultSet rs = s.executeQuery(
				"select distinct CPInstanceId from CommercePriceEntry")) {

			while (rs.next()) {
				long cpInstanceId = rs.getLong("CPInstanceId");

				CPInstance cpInstance = _cpInstanceLocalService.getCPInstance(
					cpInstanceId);

				CPDefinition cpDefinition =
					_cpDefinitionLocalService.getCPDefinition(
						cpInstance.getCPDefinitionId());

				ps.setLong(1, cpDefinition.getCProductId());

				ps.setString(2, cpInstance.getCPInstanceUuid());

				ps.setLong(3, cpInstanceId);

				ps.execute();
			}
		}

		if (hasIndex("CommercePriceEntry", "IX_2083879C")) {
			runSQL("drop index IX_2083879C on CommercePriceEntry");
		}

		runSQL("alter table CommercePriceEntry drop column CPInstanceId");
	}

	private void _addIndexes(String tableName) throws Exception {
		Class<?> clazz = getClass();

		List<ObjectValuePair<String, IndexMetadata>> indexesSQL = getIndexesSQL(
			clazz.getClassLoader(), tableName);

		for (ObjectValuePair<String, IndexMetadata> indexSQL : indexesSQL) {
			IndexMetadata indexMetadata = indexSQL.getValue();

			if (_log.isInfoEnabled()) {
				_log.info(
					String.format(
						"Adding index %s to table %s",
						indexMetadata.getIndexName(), tableName));
			}

			if (!hasIndex(tableName, indexMetadata.getIndexName())) {
				runSQL(indexMetadata.getCreateSQL(null));
			}
			else if (_log.isInfoEnabled()) {
				_log.info(
					String.format(
						"Index %s already exists on table %s",
						indexMetadata.getIndexName(), tableName));
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CommercePriceEntryUpgradeProcess.class);

	private final CPDefinitionLocalService _cpDefinitionLocalService;
	private final CPInstanceLocalService _cpInstanceLocalService;

}