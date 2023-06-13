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

package com.liferay.layout.page.template.internal.upgrade.v3_4_4;

import com.liferay.portal.dao.orm.common.SQLTransformer;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Márk Gulácsy
 */
public class LayoutPageTemplateStructureUpgradeProcess extends UpgradeProcess {

	public LayoutPageTemplateStructureUpgradeProcess(
		LayoutLocalService layoutLocalService) {

		_layoutLocalService = layoutLocalService;
	}

	@Override
	protected void doUpgrade() throws Exception {
		_deleteOrphanLayoutPageTemplateStructures();
		_processLayoutPageTemplateStructuresOfWidgetLayouts();
	}

	private void _deleteOrphanLayoutPageTemplateStructures() throws Exception {
		try (PreparedStatement selectPreparedStatement =
				connection.prepareStatement(
					"select layoutPageTemplateStructureId from " +
						"LayoutPageTemplateStructure where classPK not in " +
							"(select plid from Layout)");
			PreparedStatement deletePreparedStatement =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"delete from LayoutPageTemplateStructureRel where " +
						"layoutPageTemplateStructureId = ?")) {

			try (ResultSet resultSet = selectPreparedStatement.executeQuery()) {
				while (resultSet.next()) {
					deletePreparedStatement.setLong(
						1, resultSet.getLong("layoutPageTemplateStructureId"));

					deletePreparedStatement.addBatch();
				}
			}

			deletePreparedStatement.executeBatch();
		}

		runSQL(
			"delete from LayoutPageTemplateStructure where classPK not in " +
				"(select plid from Layout)");
	}

	private void _processLayoutPageTemplateStructuresOfWidgetLayouts()
		throws Exception {

		List<Long> plids = new ArrayList<>();

		try (PreparedStatement preparedStatement1 = connection.prepareStatement(
				SQLTransformer.transform(
					"select layoutPageTemplateStructureId, classPK from " +
						"LayoutPageTemplateStructure where classPK in " +
							"(select plid from Layout where type_ = ?)"));
			PreparedStatement preparedStatement2 =
				AutoBatchPreparedStatementUtil.autoBatch(
					connection.prepareStatement(
						"delete from LayoutPageTemplateStructure where " +
							"classPK = ?"));
			PreparedStatement preparedStatement3 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"delete from LayoutPageTemplateStructureRel where " +
						"layoutPageTemplateStructureId = ?")) {

			preparedStatement1.setString(1, LayoutConstants.TYPE_PORTLET);

			ResultSet resultSet = preparedStatement1.executeQuery();

			while (resultSet.next()) {
				long classPK = resultSet.getLong("classPK");

				plids.add(classPK);

				preparedStatement2.setLong(1, classPK);

				preparedStatement2.addBatch();

				preparedStatement3.setLong(
					1, resultSet.getLong("layoutPageTemplateStructureId"));

				preparedStatement3.addBatch();
			}

			preparedStatement2.executeBatch();

			preparedStatement3.executeBatch();
		}

		ServiceContext serviceContext = new ServiceContext();

		for (long plid : plids) {
			Layout layout = _layoutLocalService.fetchLayout(plid);

			if (layout.getStatus() != WorkflowConstants.STATUS_DRAFT) {
				continue;
			}

			_layoutLocalService.updateStatus(
				PortalUtil.getValidUserId(
					layout.getCompanyId(), layout.getUserId()),
				plid, WorkflowConstants.STATUS_APPROVED, serviceContext);
		}
	}

	private final LayoutLocalService _layoutLocalService;

}