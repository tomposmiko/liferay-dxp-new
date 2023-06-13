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

package com.liferay.commerce.product.service.impl;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.commerce.product.constants.CPActionKeys;
import com.liferay.commerce.product.constants.CPAttachmentFileEntryConstants;
import com.liferay.commerce.product.constants.CPConstants;
import com.liferay.commerce.product.exception.NoSuchCPDefinitionException;
import com.liferay.commerce.product.model.CPAttachmentFileEntry;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.service.base.CPAttachmentFileEntryServiceBaseImpl;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.asset.service.permission.AssetCategoryPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
@Component(
	property = {
		"json.web.service.context.name=commerce",
		"json.web.service.context.path=CPAttachmentFileEntry"
	},
	service = AopService.class
)
public class CPAttachmentFileEntryServiceImpl
	extends CPAttachmentFileEntryServiceBaseImpl {

	@Override
	public CPAttachmentFileEntry addCPAttachmentFileEntry(
			long groupId, long classNameId, long classPK, long fileEntryId,
			boolean cdnEnabled, String cdnURL, int displayDateMonth,
			int displayDateDay, int displayDateYear, int displayDateHour,
			int displayDateMinute, int expirationDateMonth,
			int expirationDateDay, int expirationDateYear,
			int expirationDateHour, int expirationDateMinute,
			boolean neverExpire, Map<Locale, String> titleMap, String json,
			double priority, int type, ServiceContext serviceContext)
		throws PortalException {

		_checkCPAttachmentFileEntryPermissions(
			serviceContext.getScopeGroupId(), classNameId, classPK, type);

		return cpAttachmentFileEntryLocalService.addCPAttachmentFileEntry(
			null, getUserId(), groupId, classNameId, classPK, fileEntryId,
			cdnEnabled, cdnURL, displayDateMonth, displayDateDay,
			displayDateYear, displayDateHour, displayDateMinute,
			expirationDateMonth, expirationDateDay, expirationDateYear,
			expirationDateHour, expirationDateMinute, neverExpire, titleMap,
			json, priority, type, serviceContext);
	}

	@Override
	public CPAttachmentFileEntry addOrUpdateCPAttachmentFileEntry(
			String externalReferenceCode, long groupId, long classNameId,
			long classPK, long cpAttachmentFileEntryId, long fileEntryId,
			boolean cdnEnabled, String cdnURL, int displayDateMonth,
			int displayDateDay, int displayDateYear, int displayDateHour,
			int displayDateMinute, int expirationDateMonth,
			int expirationDateDay, int expirationDateYear,
			int expirationDateHour, int expirationDateMinute,
			boolean neverExpire, Map<Locale, String> titleMap, String json,
			double priority, int type, ServiceContext serviceContext)
		throws PortalException {

		CPAttachmentFileEntry cpAttachmentFileEntry = null;

		if (cpAttachmentFileEntryId != 0) {
			cpAttachmentFileEntry =
				cpAttachmentFileEntryPersistence.fetchByPrimaryKey(
					cpAttachmentFileEntryId);
		}
		else if (Validator.isNotNull(externalReferenceCode)) {
			cpAttachmentFileEntry =
				cpAttachmentFileEntryPersistence.fetchByERC_C(
					externalReferenceCode, serviceContext.getCompanyId());
		}

		if (cpAttachmentFileEntry == null) {
			_checkCPAttachmentFileEntryPermissions(
				serviceContext.getScopeGroupId(), classNameId, classPK, type);
		}
		else {
			_checkCPAttachmentFileEntryPermissions(cpAttachmentFileEntry);
		}

		return cpAttachmentFileEntryLocalService.
			addOrUpdateCPAttachmentFileEntry(
				externalReferenceCode, getUserId(), groupId, classNameId,
				classPK, cpAttachmentFileEntryId, fileEntryId, cdnEnabled,
				cdnURL, displayDateMonth, displayDateDay, displayDateYear,
				displayDateHour, displayDateMinute, expirationDateMonth,
				expirationDateDay, expirationDateYear, expirationDateHour,
				expirationDateMinute, neverExpire, titleMap, json, priority,
				type, serviceContext);
	}

	@Override
	public void deleteCPAttachmentFileEntry(long cpAttachmentFileEntryId)
		throws PortalException {

		_checkCPAttachmentFileEntryPermissions(cpAttachmentFileEntryId);

		cpAttachmentFileEntryLocalService.deleteCPAttachmentFileEntry(
			cpAttachmentFileEntryId);
	}

	@Override
	public CPAttachmentFileEntry fetchByExternalReferenceCode(
			String externalReferenceCode, long companyId)
		throws PortalException {

		CPAttachmentFileEntry cpAttachmentFileEntry =
			cpAttachmentFileEntryLocalService.fetchByExternalReferenceCode(
				externalReferenceCode, companyId);

		if (cpAttachmentFileEntry != null) {
			long cpDefinitionClassNameId = _portal.getClassNameId(
				CPDefinition.class);

			if (cpDefinitionClassNameId ==
					cpAttachmentFileEntry.getClassNameId()) {

				_checkCommerceCatalog(
					cpAttachmentFileEntry.getClassPK(), ActionKeys.VIEW);
			}
			else {
				_checkCPAttachmentFileEntryPermissions(cpAttachmentFileEntry);
			}
		}

		return cpAttachmentFileEntry;
	}

	@Override
	public CPAttachmentFileEntry fetchCPAttachmentFileEntry(
			long cpAttachmentFileEntryId)
		throws PortalException {

		CPAttachmentFileEntry cpAttachmentFileEntry =
			cpAttachmentFileEntryLocalService.fetchCPAttachmentFileEntry(
				cpAttachmentFileEntryId);

		if (cpAttachmentFileEntry != null) {
			long cpDefinitionClassNameId = _portal.getClassNameId(
				CPDefinition.class);

			long assetCategoryClassNameId = _portal.getClassNameId(
				AssetCategory.class);

			if (cpDefinitionClassNameId ==
					cpAttachmentFileEntry.getClassNameId()) {

				_checkCommerceCatalog(
					cpAttachmentFileEntry.getClassPK(), ActionKeys.VIEW);
			}
			else if (assetCategoryClassNameId ==
						cpAttachmentFileEntry.getClassNameId()) {

				AssetCategoryPermission.check(
					getPermissionChecker(), cpAttachmentFileEntry.getClassPK(),
					ActionKeys.VIEW);
			}
			else {
				_checkCPAttachmentFileEntryPermissions(cpAttachmentFileEntryId);
			}
		}

		return cpAttachmentFileEntry;
	}

	@Override
	public List<CPAttachmentFileEntry> getCPAttachmentFileEntries(
			long classNameId, long classPK, int type, int status, int start,
			int end)
		throws PortalException {

		_checkCPAttachmentFileEntryPermissions(
			classNameId, classPK, ActionKeys.VIEW);

		List<CPAttachmentFileEntry> filteredCPAttachmentFileEntries =
			new ArrayList<>();

		List<CPAttachmentFileEntry> cpAttachmentFileEntries =
			cpAttachmentFileEntryLocalService.getCPAttachmentFileEntries(
				classNameId, classPK, type, status, start, end);

		for (CPAttachmentFileEntry cpAttachmentFileEntry :
				cpAttachmentFileEntries) {

			DLFileEntry dlFileEntry = _dlFileEntryLocalService.fetchDLFileEntry(
				cpAttachmentFileEntry.getFileEntryId());

			if ((dlFileEntry != null) &&
				_dlFileEntryModelResourcePermission.contains(
					getPermissionChecker(), dlFileEntry, ActionKeys.VIEW)) {

				filteredCPAttachmentFileEntries.add(cpAttachmentFileEntry);
			}
			else if (cpAttachmentFileEntry.isCDNEnabled()) {
				filteredCPAttachmentFileEntries.add(cpAttachmentFileEntry);
			}
		}

		return filteredCPAttachmentFileEntries;
	}

	@Override
	public List<CPAttachmentFileEntry> getCPAttachmentFileEntries(
			long classNameId, long classPK, int type, int status, int start,
			int end, OrderByComparator<CPAttachmentFileEntry> orderByComparator)
		throws PortalException {

		_checkCPAttachmentFileEntryPermissions(
			classNameId, classPK, ActionKeys.VIEW);

		List<CPAttachmentFileEntry> filteredCPAttachmentFileEntries =
			new ArrayList<>();

		List<CPAttachmentFileEntry> cpAttachmentFileEntries =
			cpAttachmentFileEntryLocalService.getCPAttachmentFileEntries(
				classNameId, classPK, type, status, start, end,
				orderByComparator);

		for (CPAttachmentFileEntry cpAttachmentFileEntry :
				cpAttachmentFileEntries) {

			DLFileEntry dlFileEntry = _dlFileEntryLocalService.fetchDLFileEntry(
				cpAttachmentFileEntry.getFileEntryId());

			if ((dlFileEntry != null) &&
				_dlFileEntryModelResourcePermission.contains(
					getPermissionChecker(), dlFileEntry, ActionKeys.VIEW)) {

				filteredCPAttachmentFileEntries.add(cpAttachmentFileEntry);
			}
			else if (cpAttachmentFileEntry.isCDNEnabled()) {
				filteredCPAttachmentFileEntries.add(cpAttachmentFileEntry);
			}
		}

		return filteredCPAttachmentFileEntries;
	}

	@Override
	public List<CPAttachmentFileEntry> getCPAttachmentFileEntries(
			long classNameId, long classPK, String keywords, int type,
			int status, int start, int end)
		throws PortalException {

		_checkCPAttachmentFileEntryPermissions(
			classNameId, classPK, ActionKeys.VIEW);

		List<CPAttachmentFileEntry> filteredCPAttachmentFileEntries =
			new ArrayList<>();

		List<CPAttachmentFileEntry> cpAttachmentFileEntries =
			cpAttachmentFileEntryLocalService.getCPAttachmentFileEntries(
				classNameId, classPK, keywords, type, status, start, end);

		for (CPAttachmentFileEntry cpAttachmentFileEntry :
				cpAttachmentFileEntries) {

			DLFileEntry dlFileEntry = _dlFileEntryLocalService.fetchDLFileEntry(
				cpAttachmentFileEntry.getFileEntryId());

			if ((dlFileEntry != null) &&
				_dlFileEntryModelResourcePermission.contains(
					getPermissionChecker(), dlFileEntry, ActionKeys.VIEW)) {

				filteredCPAttachmentFileEntries.add(cpAttachmentFileEntry);
			}
			else if (cpAttachmentFileEntry.isCDNEnabled()) {
				filteredCPAttachmentFileEntries.add(cpAttachmentFileEntry);
			}
		}

		return filteredCPAttachmentFileEntries;
	}

	@Override
	public int getCPAttachmentFileEntriesCount(
			long classNameId, long classPK, int type, int status)
		throws PortalException {

		_checkCPAttachmentFileEntryPermissions(
			classNameId, classPK, ActionKeys.VIEW);

		return cpAttachmentFileEntryLocalService.
			getCPAttachmentFileEntriesCount(classNameId, classPK, type, status);
	}

	@Override
	public int getCPAttachmentFileEntriesCount(
			long classNameId, long classPK, String keywords, int type,
			int status)
		throws PortalException {

		_checkCPAttachmentFileEntryPermissions(
			classNameId, classPK, ActionKeys.VIEW);

		return cpAttachmentFileEntryLocalService.
			getCPAttachmentFileEntriesCount(
				classNameId, classPK, keywords, type, status);
	}

	@Override
	public CPAttachmentFileEntry getCPAttachmentFileEntry(
			long cpAttachmentFileEntryId)
		throws PortalException {

		CPAttachmentFileEntry cpAttachmentFileEntry =
			cpAttachmentFileEntryLocalService.getCPAttachmentFileEntry(
				cpAttachmentFileEntryId);

		if (cpAttachmentFileEntry != null) {
			long cpDefinitionClassNameId = _portal.getClassNameId(
				CPDefinition.class);

			long assetCategoryClassNameId = _portal.getClassNameId(
				AssetCategory.class);

			if (cpDefinitionClassNameId ==
					cpAttachmentFileEntry.getClassNameId()) {

				_checkCommerceCatalog(
					cpAttachmentFileEntry.getClassPK(), ActionKeys.VIEW);
			}
			else if (assetCategoryClassNameId ==
						cpAttachmentFileEntry.getClassNameId()) {

				AssetCategoryPermission.check(
					getPermissionChecker(), cpAttachmentFileEntry.getClassPK(),
					ActionKeys.VIEW);
			}
			else {
				_checkCPAttachmentFileEntryPermissions(cpAttachmentFileEntryId);
			}
		}

		return cpAttachmentFileEntry;
	}

	@Override
	public CPAttachmentFileEntry updateCPAttachmentFileEntry(
			long cpAttachmentFileEntryId, long fileEntryId, boolean cdnEnabled,
			String cdnURL, int displayDateMonth, int displayDateDay,
			int displayDateYear, int displayDateHour, int displayDateMinute,
			int expirationDateMonth, int expirationDateDay,
			int expirationDateYear, int expirationDateHour,
			int expirationDateMinute, boolean neverExpire,
			Map<Locale, String> titleMap, String json, double priority,
			int type, ServiceContext serviceContext)
		throws PortalException {

		_checkCPAttachmentFileEntryPermissions(cpAttachmentFileEntryId);

		return cpAttachmentFileEntryLocalService.updateCPAttachmentFileEntry(
			getUserId(), cpAttachmentFileEntryId, fileEntryId, cdnEnabled,
			cdnURL, displayDateMonth, displayDateDay, displayDateYear,
			displayDateHour, displayDateMinute, expirationDateMonth,
			expirationDateDay, expirationDateYear, expirationDateHour,
			expirationDateMinute, neverExpire, titleMap, json, priority, type,
			serviceContext);
	}

	private void _checkCommerceCatalog(long cpDefinitionId, String actionId)
		throws PortalException {

		CPDefinition cpDefinition = _cpDefinitionLocalService.fetchCPDefinition(
			cpDefinitionId);

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException();
		}

		CommerceCatalog commerceCatalog =
			_commerceCatalogLocalService.fetchCommerceCatalogByGroupId(
				cpDefinition.getGroupId());

		if (commerceCatalog == null) {
			throw new PrincipalException();
		}

		_commerceCatalogModelResourcePermission.check(
			getPermissionChecker(), commerceCatalog, actionId);
	}

	private void _checkCPAttachmentFileEntryPermissions(
			CPAttachmentFileEntry cpAttachmentFileEntry)
		throws PortalException {

		_checkCPAttachmentFileEntryPermissions(
			cpAttachmentFileEntry.getGroupId(),
			cpAttachmentFileEntry.getClassNameId(),
			cpAttachmentFileEntry.getClassPK(),
			cpAttachmentFileEntry.getType());
	}

	private void _checkCPAttachmentFileEntryPermissions(
			long cpAttachmentFileEntryId)
		throws PortalException {

		_checkCPAttachmentFileEntryPermissions(
			cpAttachmentFileEntryLocalService.getCPAttachmentFileEntry(
				cpAttachmentFileEntryId));
	}

	private void _checkCPAttachmentFileEntryPermissions(
			long scopeGroupId, long classNameId, long classPK, int type)
		throws PortalException {

		String actionKey = _getActionKeyByCPAttachmentFileEntryType(type);

		_portletResourcePermission.check(
			getPermissionChecker(), scopeGroupId, actionKey);

		_checkCPAttachmentFileEntryPermissions(
			classNameId, classPK, ActionKeys.UPDATE);
	}

	private void _checkCPAttachmentFileEntryPermissions(
			long classNameId, long classPK, String actionId)
		throws PortalException {

		long cpDefinitionClassNameId = _portal.getClassNameId(
			CPDefinition.class);

		if (classNameId == cpDefinitionClassNameId) {
			_checkCommerceCatalog(classPK, actionId);
		}
	}

	private String _getActionKeyByCPAttachmentFileEntryType(int type) {
		if (type == CPAttachmentFileEntryConstants.TYPE_OTHER) {
			return CPActionKeys.MANAGE_COMMERCE_PRODUCT_ATTACHMENTS;
		}

		return CPActionKeys.MANAGE_COMMERCE_PRODUCT_IMAGES;
	}

	@Reference
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.commerce.product.model.CommerceCatalog)"
	)
	private ModelResourcePermission<CommerceCatalog>
		_commerceCatalogModelResourcePermission;

	@Reference
	private CPDefinitionLocalService _cpDefinitionLocalService;

	@Reference
	private DLFileEntryLocalService _dlFileEntryLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.document.library.kernel.model.DLFileEntry)"
	)
	private ModelResourcePermission<DLFileEntry>
		_dlFileEntryModelResourcePermission;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(resource.name=" + CPConstants.RESOURCE_NAME_PRODUCT + ")"
	)
	private PortletResourcePermission _portletResourcePermission;

}