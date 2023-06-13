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

package com.liferay.journal.internal.upgrade.v5_1_0;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeProcessFactory;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.Portal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lourdes Fernández Besada
 */
public class JournalArticleDDMStructureIdUpgradeProcess extends UpgradeProcess {

	public JournalArticleDDMStructureIdUpgradeProcess(
		ClassNameLocalService classNameLocalService,
		DDMStructureLocalService ddmStructureLocalService, Portal portal) {

		_classNameLocalService = classNameLocalService;
		_ddmStructureLocalService = ddmStructureLocalService;
		_portal = portal;
	}

	@Override
	protected void doUpgrade() throws Exception {
		Map<String, Map<Long, Long>> ddmStructureKeysMap =
			new ConcurrentHashMap<>();

		long classNameId = _classNameLocalService.getClassNameId(
			JournalArticle.class.getName());

		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			processConcurrently(
				"select distinct groupId, DDMStructureKey from JournalArticle",
				"update JournalArticle set DDMStructureId = ? where groupId " +
					"= ? and DDMStructureKey = ?",
				resultSet -> new Object[] {
					resultSet.getLong(1),
					GetterUtil.getString(resultSet.getString(2))
				},
				(values, preparedStatement) -> {
					long groupId = (Long)values[0];

					String ddmStructureKey = (String)values[1];

					Map<Long, Long> groupIdsMap =
						ddmStructureKeysMap.computeIfAbsent(
							ddmStructureKey, key -> new ConcurrentHashMap<>());

					if (!groupIdsMap.containsKey(groupId)) {
						DDMStructure ddmStructure =
							_ddmStructureLocalService.getStructure(
								_portal.getSiteGroupId(groupId), classNameId,
								ddmStructureKey, true);

						groupIdsMap.put(groupId, ddmStructure.getStructureId());
					}

					preparedStatement.setLong(1, groupIdsMap.get(groupId));

					preparedStatement.setLong(2, groupId);
					preparedStatement.setString(3, ddmStructureKey);

					preparedStatement.addBatch();
				},
				"Unable to set journal article DDMStructureId");
		}
	}

	@Override
	protected UpgradeStep[] getPreUpgradeSteps() {
		return new UpgradeStep[] {
			UpgradeProcessFactory.addColumns(
				"JournalArticle", "DDMStructureId LONG")
		};
	}

	private final ClassNameLocalService _classNameLocalService;
	private final DDMStructureLocalService _ddmStructureLocalService;
	private final Portal _portal;

}