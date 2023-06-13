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

package com.liferay.asset.display.page.service.impl;

import com.liferay.asset.display.page.constants.AssetDisplayPageConstants;
import com.liferay.asset.display.page.model.AssetDisplayPageEntry;
import com.liferay.asset.display.page.service.base.AssetDisplayPageEntryLocalServiceBaseImpl;
import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Eudaldo Alonso
 */
public class AssetDisplayPageEntryLocalServiceImpl
	extends AssetDisplayPageEntryLocalServiceBaseImpl {

	@Override
	public AssetDisplayPageEntry addAssetDisplayPageEntry(
			long userId, long groupId, long classNameId, long classPK,
			long layoutPageTemplateEntryId, int type,
			ServiceContext serviceContext)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		long assetDisplayPageEntryId = counterLocalService.increment();

		AssetDisplayPageEntry assetDisplayPageEntry =
			assetDisplayPageEntryPersistence.create(assetDisplayPageEntryId);

		assetDisplayPageEntry.setUuid(serviceContext.getUuid());
		assetDisplayPageEntry.setGroupId(groupId);
		assetDisplayPageEntry.setCompanyId(user.getCompanyId());
		assetDisplayPageEntry.setUserId(user.getUserId());
		assetDisplayPageEntry.setUserName(user.getFullName());
		assetDisplayPageEntry.setCreateDate(
			serviceContext.getCreateDate(new Date()));
		assetDisplayPageEntry.setModifiedDate(
			serviceContext.getModifiedDate(new Date()));
		assetDisplayPageEntry.setClassNameId(classNameId);
		assetDisplayPageEntry.setClassPK(classPK);
		assetDisplayPageEntry.setLayoutPageTemplateEntryId(
			layoutPageTemplateEntryId);
		assetDisplayPageEntry.setType(type);

		long plid = _getPlid(
			groupId, classNameId, classPK, layoutPageTemplateEntryId);

		assetDisplayPageEntry.setPlid(plid);

		assetDisplayPageEntryPersistence.update(assetDisplayPageEntry);

		return assetDisplayPageEntry;
	}

	@Override
	public AssetDisplayPageEntry addAssetDisplayPageEntry(
			long userId, long groupId, long classNameId, long classPK,
			long layoutPageTemplateEntryId, ServiceContext serviceContext)
		throws PortalException {

		return addAssetDisplayPageEntry(
			userId, groupId, classNameId, classPK, layoutPageTemplateEntryId,
			AssetDisplayPageConstants.TYPE_DEFAULT, serviceContext);
	}

	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public void deleteAssetDisplayPageEntry(
			long groupId, long classNameId, long classPK)
		throws PortalException {

		assetDisplayPageEntryPersistence.removeByG_C_C(
			groupId, classNameId, classPK);
	}

	@Override
	public AssetDisplayPageEntry fetchAssetDisplayPageEntry(
		long groupId, long classNameId, long classPK) {

		return assetDisplayPageEntryPersistence.fetchByG_C_C(
			groupId, classNameId, classPK);
	}

	@Override
	public List<AssetDisplayPageEntry>
		getAssetDisplayPageEntriesByLayoutPageTemplateEntryId(
			long layoutPageTemplateEntryId) {

		return assetDisplayPageEntryPersistence.findByLayoutPageTemplateEntryId(
			layoutPageTemplateEntryId);
	}

	@Override
	public int getAssetDisplayPageEntriesCountByLayoutPageTemplateEntryId(
		long layoutPageTemplateEntryId) {

		return assetDisplayPageEntryPersistence.
			countByLayoutPageTemplateEntryId(layoutPageTemplateEntryId);
	}

	@Override
	public AssetDisplayPageEntry updateAssetDisplayPageEntry(
			long assetDisplayPageEntryId, long layoutPageTemplateEntryId,
			int type)
		throws PortalException {

		AssetDisplayPageEntry assetDisplayPageEntry =
			assetDisplayPageEntryPersistence.findByPrimaryKey(
				assetDisplayPageEntryId);

		assetDisplayPageEntry.setModifiedDate(new Date());
		assetDisplayPageEntry.setLayoutPageTemplateEntryId(
			layoutPageTemplateEntryId);
		assetDisplayPageEntry.setType(type);

		long plid = _getPlid(
			assetDisplayPageEntry.getGroupId(),
			assetDisplayPageEntry.getClassNameId(),
			assetDisplayPageEntry.getClassPK(), layoutPageTemplateEntryId);

		assetDisplayPageEntry.setPlid(plid);

		assetDisplayPageEntryPersistence.update(assetDisplayPageEntry);

		return assetDisplayPageEntry;
	}

	private long _getPlid(
			long groupId, long classNameId, long classPK,
			long layoutPageTemplateEntryId)
		throws PortalException {

		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.
				getAssetRendererFactoryByClassNameId(classNameId);

		AssetEntry assetEntry = assetRendererFactory.getAssetEntry(
			_portal.getClassName(classNameId), classPK);

		LayoutPageTemplateEntry layoutPageTemplateEntry = Optional.ofNullable(
			_layoutPageTemplateEntryService.fetchLayoutPageTemplateEntry(
				layoutPageTemplateEntryId)
		).orElse(
			_layoutPageTemplateEntryService.fetchDefaultLayoutPageTemplateEntry(
				groupId, classNameId, assetEntry.getClassTypeId())
		);

		if (layoutPageTemplateEntry != null) {
			return layoutPageTemplateEntry.getPlid();
		}

		Layout layout = _layoutLocalService.fetchLayoutByUuidAndGroupId(
			assetEntry.getLayoutUuid(), assetEntry.getGroupId(), false);

		if (layout != null) {
			return layout.getPlid();
		}

		layout = _layoutLocalService.fetchLayoutByUuidAndGroupId(
			assetEntry.getLayoutUuid(), assetEntry.getGroupId(), true);

		if (layout != null) {
			return layout.getPlid();
		}

		return LayoutConstants.DEFAULT_PLID;
	}

	@ServiceReference(type = LayoutLocalService.class)
	private LayoutLocalService _layoutLocalService;

	@ServiceReference(type = LayoutPageTemplateEntryService.class)
	private LayoutPageTemplateEntryService _layoutPageTemplateEntryService;

	@ServiceReference(type = Portal.class)
	private Portal _portal;

}