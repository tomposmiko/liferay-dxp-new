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

package com.liferay.data.engine.rest.internal.resource.v1_0;

import com.liferay.data.engine.rest.dto.v1_0.DataDefinition;
import com.liferay.data.engine.rest.dto.v1_0.DataDefinitionPermission;
import com.liferay.data.engine.rest.internal.constants.DataActionKeys;
import com.liferay.data.engine.rest.internal.constants.DataDefinitionConstants;
import com.liferay.data.engine.rest.internal.dto.v1_0.util.DataDefinitionUtil;
import com.liferay.data.engine.rest.internal.model.InternalDataDefinition;
import com.liferay.data.engine.rest.internal.resource.v1_0.util.DataEnginePermissionUtil;
import com.liferay.data.engine.rest.resource.v1_0.DataDefinitionResource;
import com.liferay.data.engine.spi.field.type.util.LocalizedValueUtil;
import com.liferay.dynamic.data.lists.service.DDLRecordSetLocalService;
import com.liferay.dynamic.data.mapping.exception.RequiredStructureException;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureService;
import com.liferay.dynamic.data.mapping.service.DDMStructureVersionLocalService;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Jeyvison Nascimento
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/data-definition.properties",
	scope = ServiceScope.PROTOTYPE, service = DataDefinitionResource.class
)
public class DataDefinitionResourceImpl extends BaseDataDefinitionResourceImpl {

	@Override
	public void deleteDataDefinition(Long dataDefinitionId) throws Exception {
		_modelResourcePermission.check(
			PermissionThreadLocal.getPermissionChecker(), dataDefinitionId,
			ActionKeys.DELETE);

		DDMStructure ddmStructure = _ddmStructureLocalService.getDDMStructure(
			dataDefinitionId);

		if (_ddlRecordSetLocalService.getRecordSetsCount(
				ddmStructure.getGroupId(), dataDefinitionId, false) > 0) {

			throw new RequiredStructureException.
				MustNotDeleteStructureReferencedByStructureLinks(
					dataDefinitionId);
		}

		List<DDMStructureVersion> ddmStructureVersions =
			_ddmStructureVersionLocalService.getStructureVersions(
				dataDefinitionId);

		for (DDMStructureVersion ddmStructureVersion : ddmStructureVersions) {
			_ddmStructureVersionLocalService.deleteDDMStructureVersion(
				ddmStructureVersion);
		}

		_ddmStructureLocalService.deleteDDMStructure(dataDefinitionId);
	}

	@Override
	public DataDefinition getDataDefinition(Long dataDefinitionId)
		throws Exception {

		_modelResourcePermission.check(
			PermissionThreadLocal.getPermissionChecker(), dataDefinitionId,
			ActionKeys.VIEW);

		return DataDefinitionUtil.toDataDefinition(
			_ddmStructureLocalService.getStructure(dataDefinitionId));
	}

	@Override
	public Page<DataDefinition> getSiteDataDefinitionsPage(
			Long siteId, String keywords, Pagination pagination)
		throws Exception {

		if (Validator.isNull(keywords)) {
			return Page.of(
				transform(
					_ddmStructureService.getStructures(
						contextCompany.getCompanyId(), new long[] {siteId},
						_getClassNameId(), pagination.getStartPosition(),
						pagination.getEndPosition(), null),
					DataDefinitionUtil::toDataDefinition),
				pagination,
				_ddmStructureService.getStructuresCount(
					contextCompany.getCompanyId(), new long[] {siteId},
					_getClassNameId()));
		}

		return Page.of(
			transform(
				_ddmStructureService.search(
					contextCompany.getCompanyId(), new long[] {siteId},
					_getClassNameId(), keywords, WorkflowConstants.STATUS_ANY,
					pagination.getStartPosition(), pagination.getEndPosition(),
					null),
				DataDefinitionUtil::toDataDefinition),
			pagination,
			_ddmStructureService.searchCount(
				contextCompany.getCompanyId(), new long[] {siteId},
				_getClassNameId(), keywords, WorkflowConstants.STATUS_ANY));
	}

	@Override
	public void postDataDefinitionDataDefinitionPermission(
			Long dataDefinitionId, String operation,
			DataDefinitionPermission dataDefinitionPermission)
		throws Exception {

		DDMStructure ddmStructure = _ddmStructureLocalService.getStructure(
			dataDefinitionId);

		DataEnginePermissionUtil.checkOperationPermission(
			_groupLocalService, operation, ddmStructure.getGroupId());

		List<String> actionIds = new ArrayList<>();

		if (GetterUtil.getBoolean(dataDefinitionPermission.getDelete())) {
			actionIds.add(ActionKeys.DELETE);
		}

		if (GetterUtil.getBoolean(dataDefinitionPermission.getUpdate())) {
			actionIds.add(ActionKeys.UPDATE);
		}

		if (GetterUtil.getBoolean(dataDefinitionPermission.getView())) {
			actionIds.add(ActionKeys.VIEW);
		}

		if (actionIds.isEmpty()) {
			return;
		}

		DataEnginePermissionUtil.persistModelPermission(
			actionIds, contextCompany, dataDefinitionId, operation,
			DataDefinitionConstants.RESOURCE_NAME,
			_resourcePermissionLocalService, _roleLocalService,
			dataDefinitionPermission.getRoleNames(), ddmStructure.getGroupId());
	}

	@Override
	public DataDefinition postSiteDataDefinition(
			Long siteId, DataDefinition dataDefinition)
		throws Exception {

		DataEnginePermissionUtil.checkPermission(
			DataActionKeys.ADD_DATA_DEFINITION, _groupLocalService, siteId);

		ServiceContext serviceContext = new ServiceContext();

		dataDefinition = DataDefinitionUtil.toDataDefinition(
			_ddmStructureLocalService.addStructure(
				PrincipalThreadLocal.getUserId(), siteId,
				DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
				_getClassNameId(), null,
				LocalizedValueUtil.toLocaleStringMap(dataDefinition.getName()),
				LocalizedValueUtil.toLocaleStringMap(
					dataDefinition.getDescription()),
				DataDefinitionUtil.toJSON(dataDefinition),
				dataDefinition.getStorageType(), serviceContext));

		_resourceLocalService.addModelResources(
			contextCompany.getCompanyId(), siteId,
			PrincipalThreadLocal.getUserId(),
			InternalDataDefinition.class.getName(), dataDefinition.getId(),
			serviceContext.getModelPermissions());

		return dataDefinition;
	}

	@Override
	public void postSiteDataDefinitionPermission(
			Long siteId, String operation,
			DataDefinitionPermission dataDefinitionPermission)
		throws Exception {

		DataEnginePermissionUtil.checkOperationPermission(
			_groupLocalService, operation, siteId);

		List<String> actionIds = new ArrayList<>();

		if (GetterUtil.getBoolean(
				dataDefinitionPermission.getAddDataDefinition())) {

			actionIds.add(DataActionKeys.ADD_DATA_DEFINITION);
		}

		if (GetterUtil.getBoolean(
				dataDefinitionPermission.getDefinePermissions())) {

			actionIds.add(DataActionKeys.DEFINE_PERMISSIONS);
		}

		if (actionIds.isEmpty()) {
			return;
		}

		DataEnginePermissionUtil.persistPermission(
			actionIds, contextCompany, operation,
			_resourcePermissionLocalService, _roleLocalService,
			dataDefinitionPermission.getRoleNames());
	}

	@Override
	public DataDefinition putDataDefinition(
			Long dataDefinitionId, DataDefinition dataDefinition)
		throws Exception {

		_modelResourcePermission.check(
			PermissionThreadLocal.getPermissionChecker(), dataDefinitionId,
			ActionKeys.UPDATE);

		return DataDefinitionUtil.toDataDefinition(
			_ddmStructureLocalService.updateStructure(
				PrincipalThreadLocal.getUserId(), dataDefinitionId,
				DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
				LocalizedValueUtil.toLocaleStringMap(dataDefinition.getName()),
				LocalizedValueUtil.toLocaleStringMap(
					dataDefinition.getDescription()),
				DataDefinitionUtil.toJSON(dataDefinition),
				new ServiceContext()));
	}

	@Reference(
		target = "(model.class.name=com.liferay.data.engine.rest.internal.model.InternalDataDefinition)",
		unbind = "-"
	)
	protected void setModelResourcePermission(
		ModelResourcePermission<InternalDataDefinition>
			modelResourcePermission) {

		_modelResourcePermission = modelResourcePermission;
	}

	private long _getClassNameId() {
		return _portal.getClassNameId(InternalDataDefinition.class);
	}

	@Reference
	private DDLRecordSetLocalService _ddlRecordSetLocalService;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private DDMStructureService _ddmStructureService;

	@Reference
	private DDMStructureVersionLocalService _ddmStructureVersionLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	private ModelResourcePermission<InternalDataDefinition>
		_modelResourcePermission;

	@Reference
	private Portal _portal;

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

}