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

package com.liferay.opensocial.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link GadgetService}.
 *
 * @author Brian Wing Shun Chan
 * @see GadgetService
 * @generated
 */
public class GadgetServiceWrapper
	implements GadgetService, ServiceWrapper<GadgetService> {

	public GadgetServiceWrapper(GadgetService gadgetService) {
		_gadgetService = gadgetService;
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link GadgetServiceUtil} to access the gadget remote service. Add custom service methods to <code>com.liferay.opensocial.service.impl.GadgetServiceImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	@Override
	public com.liferay.opensocial.model.Gadget addGadget(
			long companyId, String url, String portletCategoryNames,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _gadgetService.addGadget(
			companyId, url, portletCategoryNames, serviceContext);
	}

	@Override
	public void deleteGadget(
			long gadgetId,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		_gadgetService.deleteGadget(gadgetId, serviceContext);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _gadgetService.getOSGiServiceIdentifier();
	}

	@Override
	public void updateGadget(
			long gadgetId, String portletCategoryNames,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		_gadgetService.updateGadget(
			gadgetId, portletCategoryNames, serviceContext);
	}

	@Override
	public GadgetService getWrappedService() {
		return _gadgetService;
	}

	@Override
	public void setWrappedService(GadgetService gadgetService) {
		_gadgetService = gadgetService;
	}

	private GadgetService _gadgetService;

}