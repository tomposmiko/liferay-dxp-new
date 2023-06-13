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

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link LayoutPageTemplateCollectionService}.
 *
 * @author Brian Wing Shun Chan
 * @see LayoutPageTemplateCollectionService
 * @generated
 */
public class LayoutPageTemplateCollectionServiceWrapper
	implements LayoutPageTemplateCollectionService,
			   ServiceWrapper<LayoutPageTemplateCollectionService> {

	public LayoutPageTemplateCollectionServiceWrapper(
		LayoutPageTemplateCollectionService
			layoutPageTemplateCollectionService) {

		_layoutPageTemplateCollectionService =
			layoutPageTemplateCollectionService;
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link LayoutPageTemplateCollectionServiceUtil} to access the layout page template collection remote service. Add custom service methods to <code>com.liferay.layout.page.template.service.impl.LayoutPageTemplateCollectionServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateCollection
			addLayoutPageTemplateCollection(
				long groupId, String name, String description,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _layoutPageTemplateCollectionService.
			addLayoutPageTemplateCollection(
				groupId, name, description, serviceContext);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateCollection
			deleteLayoutPageTemplateCollection(
				long layoutPageTemplateCollectionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _layoutPageTemplateCollectionService.
			deleteLayoutPageTemplateCollection(layoutPageTemplateCollectionId);
	}

	@Override
	public void deleteLayoutPageTemplateCollections(
			long[] layoutPageTemplateCollectionIds)
		throws com.liferay.portal.kernel.exception.PortalException {

		_layoutPageTemplateCollectionService.
			deleteLayoutPageTemplateCollections(
				layoutPageTemplateCollectionIds);
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateCollection
			fetchLayoutPageTemplateCollection(
				long layoutPageTemplateCollectionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _layoutPageTemplateCollectionService.
			fetchLayoutPageTemplateCollection(layoutPageTemplateCollectionId);
	}

	@Override
	public java.util.List
		<com.liferay.layout.page.template.model.LayoutPageTemplateCollection>
			getLayoutPageTemplateCollections(long groupId) {

		return _layoutPageTemplateCollectionService.
			getLayoutPageTemplateCollections(groupId);
	}

	@Override
	public java.util.List
		<com.liferay.layout.page.template.model.LayoutPageTemplateCollection>
			getLayoutPageTemplateCollections(long groupId, int start, int end) {

		return _layoutPageTemplateCollectionService.
			getLayoutPageTemplateCollections(groupId, start, end);
	}

	@Override
	public java.util.List
		<com.liferay.layout.page.template.model.LayoutPageTemplateCollection>
			getLayoutPageTemplateCollections(
				long groupId, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.layout.page.template.model.
						LayoutPageTemplateCollection> orderByComparator) {

		return _layoutPageTemplateCollectionService.
			getLayoutPageTemplateCollections(
				groupId, start, end, orderByComparator);
	}

	@Override
	public java.util.List
		<com.liferay.layout.page.template.model.LayoutPageTemplateCollection>
			getLayoutPageTemplateCollections(
				long groupId, String name, int start, int end,
				com.liferay.portal.kernel.util.OrderByComparator
					<com.liferay.layout.page.template.model.
						LayoutPageTemplateCollection> orderByComparator) {

		return _layoutPageTemplateCollectionService.
			getLayoutPageTemplateCollections(
				groupId, name, start, end, orderByComparator);
	}

	@Override
	public int getLayoutPageTemplateCollectionsCount(long groupId) {
		return _layoutPageTemplateCollectionService.
			getLayoutPageTemplateCollectionsCount(groupId);
	}

	@Override
	public int getLayoutPageTemplateCollectionsCount(
		long groupId, String name) {

		return _layoutPageTemplateCollectionService.
			getLayoutPageTemplateCollectionsCount(groupId, name);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _layoutPageTemplateCollectionService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.layout.page.template.model.LayoutPageTemplateCollection
			updateLayoutPageTemplateCollection(
				long layoutPageTemplateCollectionId, String name,
				String description)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _layoutPageTemplateCollectionService.
			updateLayoutPageTemplateCollection(
				layoutPageTemplateCollectionId, name, description);
	}

	@Override
	public LayoutPageTemplateCollectionService getWrappedService() {
		return _layoutPageTemplateCollectionService;
	}

	@Override
	public void setWrappedService(
		LayoutPageTemplateCollectionService
			layoutPageTemplateCollectionService) {

		_layoutPageTemplateCollectionService =
			layoutPageTemplateCollectionService;
	}

	private LayoutPageTemplateCollectionService
		_layoutPageTemplateCollectionService;

}