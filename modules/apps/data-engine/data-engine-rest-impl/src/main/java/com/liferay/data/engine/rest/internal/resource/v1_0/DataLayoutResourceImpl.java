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

import com.liferay.data.engine.rest.dto.v1_0.DataLayout;
import com.liferay.data.engine.rest.dto.v1_0.DataLayoutPermission;
import com.liferay.data.engine.rest.internal.constants.DataActionKeys;
import com.liferay.data.engine.rest.internal.constants.DataLayoutConstants;
import com.liferay.data.engine.rest.internal.dto.v1_0.util.DataLayoutUtil;
import com.liferay.data.engine.rest.internal.dto.v1_0.util.LocalizedValueUtil;
import com.liferay.data.engine.rest.internal.model.InternalDataLayout;
import com.liferay.data.engine.rest.internal.model.InternalDataRecordCollection;
import com.liferay.data.engine.rest.internal.resource.v1_0.util.DataEnginePermissionUtil;
import com.liferay.data.engine.rest.resource.v1_0.DataLayoutResource;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.dynamic.data.mapping.service.DDMStructureLayoutLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
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
import com.liferay.portal.kernel.util.ArrayUtil;
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
	properties = "OSGI-INF/liferay/rest/v1_0/data-layout.properties",
	scope = ServiceScope.PROTOTYPE, service = DataLayoutResource.class
)
public class DataLayoutResourceImpl extends BaseDataLayoutResourceImpl {

	@Override
	public void deleteDataLayout(Long dataLayoutId) throws Exception {
		_modelResourcePermission.check(
			PermissionThreadLocal.getPermissionChecker(), dataLayoutId,
			ActionKeys.DELETE);

		_ddmStructureLayoutLocalService.deleteDDMStructureLayout(dataLayoutId);
	}

	@Override
	public DataLayout getDataLayout(Long dataLayoutId) throws Exception {
		_modelResourcePermission.check(
			PermissionThreadLocal.getPermissionChecker(), dataLayoutId,
			ActionKeys.VIEW);

		return _toDataLayout(
			_ddmStructureLayoutLocalService.getDDMStructureLayout(
				dataLayoutId));
	}

	@Override
	public Page<DataLayout> getSiteDataLayoutPage(
			Long siteId, Pagination pagination)
		throws Exception {

		return Page.of(
			transform(
				_ddmStructureLayoutLocalService.getStructureLayouts(
					siteId, pagination.getStartPosition(),
					pagination.getEndPosition()),
				this::_toDataLayout),
			pagination,
			_ddmStructureLayoutLocalService.getStructureLayoutsCount(siteId));
	}

	@Override
	public DataLayout postDataDefinitionDataLayout(
			Long dataDefinitionId, DataLayout dataLayout)
		throws Exception {

		if (ArrayUtil.isEmpty(dataLayout.getName())) {
			throw new Exception("Name is required");
		}

		DDMStructure ddmStructure = _ddmStructureLocalService.getStructure(
			dataDefinitionId);

		DataEnginePermissionUtil.checkPermission(
			DataActionKeys.ADD_DATA_LAYOUT, _groupLocalService,
			ddmStructure.getGroupId());

		ServiceContext serviceContext = new ServiceContext();

		DDMStructureLayout ddmStructureLayout =
			_ddmStructureLayoutLocalService.addStructureLayout(
				PrincipalThreadLocal.getUserId(), ddmStructure.getGroupId(),
				_getDDMStructureVersionId(dataDefinitionId),
				LocalizedValueUtil.toLocalizationMap(dataLayout.getName()),
				LocalizedValueUtil.toLocalizationMap(
					dataLayout.getDescription()),
				DataLayoutUtil.toJSON(dataLayout), serviceContext);

		dataLayout.setId(ddmStructureLayout.getStructureLayoutId());

		_resourceLocalService.addModelResources(
			contextCompany.getCompanyId(), ddmStructure.getGroupId(),
			PrincipalThreadLocal.getUserId(),
			InternalDataLayout.class.getName(), dataLayout.getId(),
			serviceContext.getModelPermissions());

		return dataLayout;
	}

	public void postDataLayoutDataLayoutPermission(
			Long dataLayoutId, String operation,
			DataLayoutPermission dataLayoutPermission)
		throws Exception {

		DDMStructureLayout ddmStructureLayout =
			_ddmStructureLayoutLocalService.getStructureLayout(dataLayoutId);

		DataEnginePermissionUtil.checkOperationPermission(
			_groupLocalService, operation, ddmStructureLayout.getGroupId());

		List<String> actionIds = new ArrayList<>();

		if (dataLayoutPermission.getDelete()) {
			actionIds.add(ActionKeys.DELETE);
		}

		if (dataLayoutPermission.getUpdate()) {
			actionIds.add(ActionKeys.UPDATE);
		}

		if (dataLayoutPermission.getView()) {
			actionIds.add(ActionKeys.VIEW);
		}

		if (actionIds.isEmpty()) {
			return;
		}

		DataEnginePermissionUtil.persistModelPermission(
			actionIds, contextCompany, dataLayoutId, operation,
			DataLayoutConstants.RESOURCE_NAME, _resourcePermissionLocalService,
			_roleLocalService, dataLayoutPermission.getRoleNames(),
			ddmStructureLayout.getGroupId());
	}

	@Override
	public void postSiteDataLayoutPermission(
			Long siteId, String operation,
			DataLayoutPermission dataLayoutPermission)
		throws Exception {

		DataEnginePermissionUtil.checkOperationPermission(
			_groupLocalService, operation, siteId);

		List<String> actionIds = new ArrayList<>();

		if (dataLayoutPermission.getAddDataLayout()) {
			actionIds.add(DataActionKeys.ADD_DATA_LAYOUT);
		}

		if (dataLayoutPermission.getDefinePermissions()) {
			actionIds.add(DataActionKeys.DEFINE_PERMISSIONS);
		}

		if (actionIds.isEmpty()) {
			return;
		}

		DataEnginePermissionUtil.persistPermission(
			actionIds, contextCompany, operation,
			_resourcePermissionLocalService, _roleLocalService,
			dataLayoutPermission.getRoleNames());
	}

	@Override
	public DataLayout putDataLayout(Long dataLayoutId, DataLayout dataLayout)
		throws Exception {

		if (ArrayUtil.isEmpty(dataLayout.getName())) {
			throw new Exception("Name is required");
		}

		_modelResourcePermission.check(
			PermissionThreadLocal.getPermissionChecker(), dataLayoutId,
			ActionKeys.UPDATE);

		return _toDataLayout(
			_ddmStructureLayoutLocalService.updateStructureLayout(
				dataLayoutId,
				_getDDMStructureVersionId(dataLayout.getDataDefinitionId()),
				LocalizedValueUtil.toLocalizationMap(dataLayout.getName()),
				LocalizedValueUtil.toLocalizationMap(
					dataLayout.getDescription()),
				DataLayoutUtil.toJSON(dataLayout), new ServiceContext()));
	}

	@Reference(
		target = "(model.class.name=com.liferay.data.engine.rest.internal.model.InternalDataLayout)",
		unbind = "-"
	)
	protected void setModelResourcePermission(
		ModelResourcePermission<InternalDataRecordCollection>
			modelResourcePermission) {

		_modelResourcePermission = modelResourcePermission;
	}

	private long _getDDMStructureId(DDMStructureLayout ddmStructureLayout)
		throws Exception {

		DDMStructureVersion ddmStructureVersion =
			_ddmStructureVersionLocalService.getDDMStructureVersion(
				ddmStructureLayout.getStructureVersionId());

		DDMStructure ddmStructure = ddmStructureVersion.getStructure();

		return ddmStructure.getStructureId();
	}

	private long _getDDMStructureVersionId(Long deDataDefinitionId)
		throws Exception {

		DDMStructureVersion ddmStructureVersion =
			_ddmStructureVersionLocalService.getLatestStructureVersion(
				deDataDefinitionId);

		return ddmStructureVersion.getStructureVersionId();
	}

	private DataLayout _toDataLayout(DDMStructureLayout ddmStructureLayout)
		throws Exception {

		DataLayout dataLayout = DataLayoutUtil.toDataLayout(
			ddmStructureLayout.getDefinition());

		dataLayout.setDateCreated(ddmStructureLayout.getCreateDate());
		dataLayout.setDataDefinitionId(_getDDMStructureId(ddmStructureLayout));
		dataLayout.setId(ddmStructureLayout.getStructureLayoutId());
		dataLayout.setDescription(
			LocalizedValueUtil.toLocalizedValues(
				ddmStructureLayout.getDescriptionMap()));
		dataLayout.setDateModified(ddmStructureLayout.getModifiedDate());
		dataLayout.setName(
			LocalizedValueUtil.toLocalizedValues(
				ddmStructureLayout.getNameMap()));
		dataLayout.setUserId(ddmStructureLayout.getUserId());

		return dataLayout;
	}

	@Reference
	private DDMStructureLayoutLocalService _ddmStructureLayoutLocalService;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private DDMStructureVersionLocalService _ddmStructureVersionLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	private ModelResourcePermission<InternalDataRecordCollection>
		_modelResourcePermission;

	@Reference
	private ResourceLocalService _resourceLocalService;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

}