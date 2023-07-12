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

package com.liferay.depot.internal.model.listener;

import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.model.DepotEntryGroupRel;
import com.liferay.depot.service.DepotEntryGroupRelLocalService;
import com.liferay.depot.service.DepotEntryLocalService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(service = ModelListener.class)
public class GroupModelListener extends BaseModelListener<Group> {

	@Override
	public void onAfterCreate(Group group) throws ModelListenerException {
		if ((group != null) && group.isDepot() &&
			_isStaging(ServiceContextThreadLocal.getServiceContext())) {

			TransactionCommitCallbackUtil.registerCallback(
				() -> {
					_copyLiveDepotEntryGroupRelsToStaging(group);

					return null;
				});
		}
	}

	@Override
	public void onAfterRemove(Group group) throws ModelListenerException {
		super.onAfterRemove(group);

		if ((group != null) || group.isDepot()) {
			TransactionCommitCallbackUtil.registerCallback(
				() -> {
					DepotEntry depotEntry =
						_depotEntryLocalService.fetchGroupDepotEntry(
							group.getGroupId());

					if (depotEntry != null) {
						_depotEntryLocalService.deleteDepotEntry(depotEntry);
					}

					return null;
				});
		}
	}

	@Override
	public void onBeforeCreate(Group group) throws ModelListenerException {
		try {
			super.onBeforeCreate(group);

			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			if (group.isDepot() && _isStaging(serviceContext)) {
				DepotEntry depotEntry = _depotEntryLocalService.addDepotEntry(
					group, serviceContext);

				group.setClassPK(depotEntry.getDepotEntryId());
			}
		}
		catch (PortalException portalException) {
			throw new ModelListenerException(portalException);
		}
	}

	@Override
	public void onBeforeRemove(Group group) throws ModelListenerException {
		super.onBeforeRemove(group);

		if (!group.isSite()) {
			return;
		}

		_depotEntryGroupRelLocalService.deleteToGroupDepotEntryGroupRels(
			group.getGroupId());
	}

	private void _copyLiveDepotEntryGroupRelsToStaging(Group group)
		throws PortalException {

		Group liveGroup = group.getLiveGroup();

		if (liveGroup == null) {
			return;
		}

		DepotEntry liveDepotEntry =
			_depotEntryLocalService.fetchGroupDepotEntry(
				liveGroup.getGroupId());

		if (liveDepotEntry == null) {
			return;
		}

		List<DepotEntryGroupRel> depotEntryGroupRels =
			_depotEntryGroupRelLocalService.getDepotEntryGroupRels(
				liveDepotEntry);

		for (DepotEntryGroupRel depotEntryGroupRel : depotEntryGroupRels) {
			Group groupRel = _groupLocalService.getGroup(
				depotEntryGroupRel.getGroupId());

			if (groupRel.isStagingGroup()) {
				DepotEntry depotEntry =
					_depotEntryLocalService.fetchGroupDepotEntry(
						group.getGroupId());

				DepotEntryGroupRel stagedDepotEntryGroupRel =
					_depotEntryGroupRelLocalService.addDepotEntryGroupRel(
						depotEntry.getDepotEntryId(),
						depotEntryGroupRel.getGroupId());

				stagedDepotEntryGroupRel.setDdmStructuresAvailable(
					depotEntryGroupRel.getDdmStructuresAvailable());
				stagedDepotEntryGroupRel.setSearchable(
					depotEntryGroupRel.getSearchable());

				_depotEntryGroupRelLocalService.updateDepotEntryGroupRel(
					stagedDepotEntryGroupRel);
			}
		}
	}

	private boolean _isStaging(ServiceContext serviceContext) {
		if (serviceContext == null) {
			return false;
		}

		return ParamUtil.getBoolean(serviceContext, "staging");
	}

	@Reference
	private DepotEntryGroupRelLocalService _depotEntryGroupRelLocalService;

	@Reference
	private DepotEntryLocalService _depotEntryLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

}