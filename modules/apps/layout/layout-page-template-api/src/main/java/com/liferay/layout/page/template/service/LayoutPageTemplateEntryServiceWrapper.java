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

package com.liferay.layout.page.template.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link LayoutPageTemplateEntryService}.
 *
 * @author Brian Wing Shun Chan
 * @see LayoutPageTemplateEntryService
 * @generated
 */
@ProviderType
public class LayoutPageTemplateEntryServiceWrapper
	implements LayoutPageTemplateEntryService,
		ServiceWrapper<LayoutPageTemplateEntryService> {
	public LayoutPageTemplateEntryServiceWrapper(
		LayoutPageTemplateEntryService layoutPageTemplateEntryService) {
		_layoutPageTemplateEntryService = layoutPageTemplateEntryService;
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry addLayoutPageTemplateEntry(
		long groupId, long layoutPageTemplateCollectionId, String name,
		int type, int status,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _layoutPageTemplateEntryService.addLayoutPageTemplateEntry(groupId,
			layoutPageTemplateCollectionId, name, type, status, serviceContext);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry addLayoutPageTemplateEntry(
		long groupId, long layoutPageTemplateCollectionId, String name,
		int type,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _layoutPageTemplateEntryService.addLayoutPageTemplateEntry(groupId,
			layoutPageTemplateCollectionId, name, type, serviceContext);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry addLayoutPageTemplateEntry(
		long groupId, long layoutPageTemplateCollectionId, String name,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _layoutPageTemplateEntryService.addLayoutPageTemplateEntry(groupId,
			layoutPageTemplateCollectionId, name, serviceContext);
	}

	@Override
	public void deleteLayoutPageTemplateEntries(
		long[] layoutPageTemplateEntryIds)
		throws com.liferay.portal.kernel.exception.PortalException {
		_layoutPageTemplateEntryService.deleteLayoutPageTemplateEntries(layoutPageTemplateEntryIds);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry deleteLayoutPageTemplateEntry(
		long layoutPageTemplateEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _layoutPageTemplateEntryService.deleteLayoutPageTemplateEntry(layoutPageTemplateEntryId);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry fetchDefaultLayoutPageTemplateEntry(
		long groupId, long classNameId, long classTypeId) {
		return _layoutPageTemplateEntryService.fetchDefaultLayoutPageTemplateEntry(groupId,
			classNameId, classTypeId);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry fetchLayoutPageTemplateEntry(
		long layoutPageTemplateEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _layoutPageTemplateEntryService.fetchLayoutPageTemplateEntry(layoutPageTemplateEntryId);
	}

	@Override
	public int getLayoutPageTemplateCollectionsCount(long groupId,
		long layoutPageTemplateCollectionId) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateCollectionsCount(groupId,
			layoutPageTemplateCollectionId);
	}

	@Override
	public int getLayoutPageTemplateCollectionsCount(long groupId,
		long layoutPageTemplateCollectionId, int status) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateCollectionsCount(groupId,
			layoutPageTemplateCollectionId, status);
	}

	@Override
	public int getLayoutPageTemplateCollectionsCount(long groupId,
		long layoutPageTemplateCollectionId, String name) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateCollectionsCount(groupId,
			layoutPageTemplateCollectionId, name);
	}

	@Override
	public int getLayoutPageTemplateCollectionsCount(long groupId,
		long layoutPageTemplateCollectionId, String name, int status) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateCollectionsCount(groupId,
			layoutPageTemplateCollectionId, name, status);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, int type, int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			type, status, start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			type, start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, long layoutPageTemplateCollectionId, int start, int end) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			layoutPageTemplateCollectionId, start, end);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, long layoutPageTemplateCollectionId, int status,
		int start, int end) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			layoutPageTemplateCollectionId, status, start, end);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, long layoutPageTemplateCollectionId, int status,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			layoutPageTemplateCollectionId, status, start, end,
			orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, long layoutPageTemplateCollectionId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			layoutPageTemplateCollectionId, start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, long classNameId, long classTypeId, int type) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			classNameId, classTypeId, type);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, long classNameId, long classTypeId, int type, int status) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			classNameId, classTypeId, type, status);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, long classNameId, long classTypeId, int type, int status,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			classNameId, classTypeId, type, status, start, end,
			orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, long classNameId, long classTypeId, int type, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			classNameId, classTypeId, type, start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, long classNameId, long classTypeId, String name,
		int type, int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			classNameId, classTypeId, name, type, status, start, end,
			orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, long classNameId, long classTypeId, String name,
		int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			classNameId, classTypeId, name, type, start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, long layoutPageTemplateCollectionId, String name,
		int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			layoutPageTemplateCollectionId, name, status, start, end,
			orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, long layoutPageTemplateCollectionId, String name,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			layoutPageTemplateCollectionId, name, start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, String name, int type, int status, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			name, type, status, start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntries(
		long groupId, String name, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntries(groupId,
			name, type, start, end, orderByComparator);
	}

	@Override
	public java.util.List<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> getLayoutPageTemplateEntriesByType(
		long groupId, long layoutPageTemplateCollectionId, int type, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.layout.page.template.model.LayoutPageTemplateEntry> orderByComparator) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesByType(groupId,
			layoutPageTemplateCollectionId, type, start, end, orderByComparator);
	}

	@Override
	public int getLayoutPageTemplateEntriesCount(long groupId, int type) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCount(groupId,
			type);
	}

	@Override
	public int getLayoutPageTemplateEntriesCount(long groupId, int type,
		int status) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCount(groupId,
			type, status);
	}

	@Override
	public int getLayoutPageTemplateEntriesCount(long groupId,
		long layoutPageTemplateFolderId) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCount(groupId,
			layoutPageTemplateFolderId);
	}

	@Override
	public int getLayoutPageTemplateEntriesCount(long groupId,
		long layoutPageTemplateFolderId, int status) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCount(groupId,
			layoutPageTemplateFolderId, status);
	}

	@Override
	public int getLayoutPageTemplateEntriesCount(long groupId,
		long classNameId, long classTypeId, int type) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCount(groupId,
			classNameId, classTypeId, type);
	}

	@Override
	public int getLayoutPageTemplateEntriesCount(long groupId,
		long classNameId, long classTypeId, int type, int status) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCount(groupId,
			classNameId, classTypeId, type, status);
	}

	@Override
	public int getLayoutPageTemplateEntriesCount(long groupId,
		long classNameId, long classTypeId, String name, int type) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCount(groupId,
			classNameId, classTypeId, name, type);
	}

	@Override
	public int getLayoutPageTemplateEntriesCount(long groupId,
		long classNameId, long classTypeId, String name, int type, int status) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCount(groupId,
			classNameId, classTypeId, name, type, status);
	}

	@Override
	public int getLayoutPageTemplateEntriesCount(long groupId,
		long layoutPageTemplateFolderId, String name) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCount(groupId,
			layoutPageTemplateFolderId, name);
	}

	@Override
	public int getLayoutPageTemplateEntriesCount(long groupId,
		long layoutPageTemplateFolderId, String name, int status) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCount(groupId,
			layoutPageTemplateFolderId, name, status);
	}

	@Override
	public int getLayoutPageTemplateEntriesCount(long groupId, String name,
		int type) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCount(groupId,
			name, type);
	}

	@Override
	public int getLayoutPageTemplateEntriesCount(long groupId, String name,
		int type, int status) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCount(groupId,
			name, type, status);
	}

	@Override
	public int getLayoutPageTemplateEntriesCountByType(long groupId,
		long layoutPageTemplateCollectionId, int type) {
		return _layoutPageTemplateEntryService.getLayoutPageTemplateEntriesCountByType(groupId,
			layoutPageTemplateCollectionId, type);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public String getOSGiServiceIdentifier() {
		return _layoutPageTemplateEntryService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry updateLayoutPageTemplateEntry(
		long layoutPageTemplateEntryId, boolean defaultTemplate)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _layoutPageTemplateEntryService.updateLayoutPageTemplateEntry(layoutPageTemplateEntryId,
			defaultTemplate);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry updateLayoutPageTemplateEntry(
		long layoutPageTemplateEntryId, int status)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _layoutPageTemplateEntryService.updateLayoutPageTemplateEntry(layoutPageTemplateEntryId,
			status);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry updateLayoutPageTemplateEntry(
		long layoutPageTemplateEntryId, long previewFileEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _layoutPageTemplateEntryService.updateLayoutPageTemplateEntry(layoutPageTemplateEntryId,
			previewFileEntryId);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry updateLayoutPageTemplateEntry(
		long layoutPageTemplateEntryId, long classNameId, long classTypeId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _layoutPageTemplateEntryService.updateLayoutPageTemplateEntry(layoutPageTemplateEntryId,
			classNameId, classTypeId);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry updateLayoutPageTemplateEntry(
		long layoutPageTemplateEntryId, long[] fragmentEntryIds,
		String editableValues,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _layoutPageTemplateEntryService.updateLayoutPageTemplateEntry(layoutPageTemplateEntryId,
			fragmentEntryIds, editableValues, serviceContext);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry updateLayoutPageTemplateEntry(
		long layoutPageTemplateEntryId, String name)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _layoutPageTemplateEntryService.updateLayoutPageTemplateEntry(layoutPageTemplateEntryId,
			name);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateEntry updateLayoutPageTemplateEntry(
		long layoutPageTemplateEntryId, String name, long[] fragmentEntryIds,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _layoutPageTemplateEntryService.updateLayoutPageTemplateEntry(layoutPageTemplateEntryId,
			name, fragmentEntryIds, serviceContext);
	}

	@Override
	public LayoutPageTemplateEntryService getWrappedService() {
		return _layoutPageTemplateEntryService;
	}

	@Override
	public void setWrappedService(
		LayoutPageTemplateEntryService layoutPageTemplateEntryService) {
		_layoutPageTemplateEntryService = layoutPageTemplateEntryService;
	}

	private LayoutPageTemplateEntryService _layoutPageTemplateEntryService;
}