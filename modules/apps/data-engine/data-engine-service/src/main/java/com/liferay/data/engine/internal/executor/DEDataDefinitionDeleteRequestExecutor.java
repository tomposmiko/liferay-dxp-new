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

package com.liferay.data.engine.internal.executor;

import com.liferay.data.engine.service.DEDataDefinitionDeleteRequest;
import com.liferay.data.engine.service.DEDataDefinitionDeleteResponse;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.service.DDLRecordSetLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;

/**
 * @author Jeyvison Nascimento
 */
public class DEDataDefinitionDeleteRequestExecutor {

	public DEDataDefinitionDeleteRequestExecutor(
		DDLRecordSetLocalService ddlRecordSetLocalService,
		DDMStructureLocalService ddmStructureLocalService) {

		_ddlRecordSetLocalService = ddlRecordSetLocalService;
		_ddmStructureLocalService = ddmStructureLocalService;
	}

	public DEDataDefinitionDeleteResponse execute(
			DEDataDefinitionDeleteRequest deDataDefinitionDeleteRequest)
		throws Exception {

		long deDataDefinitionId =
			deDataDefinitionDeleteRequest.getDEDataDefinitionId();

		deleteDDLRecordSet(deDataDefinitionId);

		_ddmStructureLocalService.deleteDDMStructure(deDataDefinitionId);

		return DEDataDefinitionDeleteResponse.Builder.of(deDataDefinitionId);
	}

	protected void deleteDDLRecordSet(long deDataDefinitionId) {
		ActionableDynamicQuery actionableDynamicQuery =
			_ddlRecordSetLocalService.getActionableDynamicQuery();

		actionableDynamicQuery.setAddCriteriaMethod(
			dynamicQuery -> {
				Property ddmStructureIdProperty = PropertyFactoryUtil.forName(
					"DDMStructureId");

				dynamicQuery.add(ddmStructureIdProperty.eq(deDataDefinitionId));
			});
		actionableDynamicQuery.setPerformActionMethod(
			(DDLRecordSet ddlRecordSet) ->
				_ddlRecordSetLocalService.deleteDDLRecordSet(ddlRecordSet));
	}

	private final DDLRecordSetLocalService _ddlRecordSetLocalService;
	private final DDMStructureLocalService _ddmStructureLocalService;

}