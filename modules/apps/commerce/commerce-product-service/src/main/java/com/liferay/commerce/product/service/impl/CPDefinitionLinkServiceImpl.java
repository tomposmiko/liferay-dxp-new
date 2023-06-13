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

import com.liferay.commerce.product.exception.NoSuchCPDefinitionException;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionLink;
import com.liferay.commerce.product.model.CProduct;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.service.base.CPDefinitionLinkServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 * @author Marco Leo
 */
@Component(
	property = {
		"json.web.service.context.name=commerce",
		"json.web.service.context.path=CPDefinitionLink"
	},
	service = AopService.class
)
public class CPDefinitionLinkServiceImpl
	extends CPDefinitionLinkServiceBaseImpl {

	@Override
	public CPDefinitionLink addCPDefinitionLink(
			long cpDefinitionId, long cProductId, double priority, String type,
			ServiceContext serviceContext)
		throws PortalException {

		_checkCommerceCatalog(cpDefinitionId, ActionKeys.UPDATE);

		return cpDefinitionLinkLocalService.addCPDefinitionLinkByCProductId(
			cpDefinitionId, cProductId, priority, type, serviceContext);
	}

	@Override
	public void deleteCPDefinitionLink(long cpDefinitionLinkId)
		throws PortalException {

		CPDefinitionLink cpDefinitionLink =
			cpDefinitionLinkLocalService.getCPDefinitionLink(
				cpDefinitionLinkId);

		_checkCommerceCatalog(
			cpDefinitionLink.getCPDefinitionId(), ActionKeys.UPDATE);

		CProduct cProduct = cpDefinitionLink.getCProduct();

		_checkCommerceCatalog(
			cProduct.getPublishedCPDefinitionId(), ActionKeys.UPDATE);

		cpDefinitionLinkLocalService.deleteCPDefinitionLink(cpDefinitionLinkId);
	}

	@Override
	public CPDefinitionLink fetchCPDefinitionLink(long cpDefinitionLinkId)
		throws PortalException {

		CPDefinitionLink cpDefinitionLink =
			cpDefinitionLinkLocalService.fetchCPDefinitionLink(
				cpDefinitionLinkId);

		if (cpDefinitionLink != null) {
			_checkCommerceCatalog(
				cpDefinitionLink.getCPDefinitionId(), ActionKeys.VIEW);

			CProduct cProduct = cpDefinitionLink.getCProduct();

			_checkCommerceCatalog(
				cProduct.getPublishedCPDefinitionId(), ActionKeys.VIEW);
		}

		return cpDefinitionLink;
	}

	@Override
	public CPDefinitionLink fetchCPDefinitionLink(
			long cpDefinitionId, long cProductId, String type)
		throws PortalException {

		_checkCommerceCatalog(cpDefinitionId, ActionKeys.VIEW);

		return cpDefinitionLinkLocalService.fetchCPDefinitionLink(
			cpDefinitionId, cProductId, type);
	}

	@Override
	public CPDefinitionLink getCPDefinitionLink(long cpDefinitionLinkId)
		throws PortalException {

		CPDefinitionLink cpDefinitionLink =
			cpDefinitionLinkLocalService.getCPDefinitionLink(
				cpDefinitionLinkId);

		CProduct cProduct = cpDefinitionLink.getCProduct();

		_checkCommerceCatalog(
			cProduct.getPublishedCPDefinitionId(), ActionKeys.VIEW);

		_checkCommerceCatalog(
			cpDefinitionLink.getCPDefinitionId(), ActionKeys.VIEW);

		return cpDefinitionLinkLocalService.getCPDefinitionLink(
			cpDefinitionLinkId);
	}

	@Override
	public List<CPDefinitionLink> getCPDefinitionLinks(long cpDefinitionId)
		throws PortalException {

		_checkCommerceCatalog(cpDefinitionId, ActionKeys.VIEW);

		return cpDefinitionLinkLocalService.getCPDefinitionLinks(
			cpDefinitionId);
	}

	@Override
	public List<CPDefinitionLink> getCPDefinitionLinks(
			long cpDefinitionId, int start, int end)
		throws PortalException {

		_checkCommerceCatalog(cpDefinitionId, ActionKeys.VIEW);

		return cpDefinitionLinkLocalService.getCPDefinitionLinks(
			cpDefinitionId, start, end);
	}

	@Override
	public List<CPDefinitionLink> getCPDefinitionLinks(
			long cpDefinitionId, String type)
		throws PortalException {

		_checkCommerceCatalog(cpDefinitionId, ActionKeys.VIEW);

		return cpDefinitionLinkLocalService.getCPDefinitionLinks(
			cpDefinitionId, type);
	}

	@Override
	public List<CPDefinitionLink> getCPDefinitionLinks(
			long cpDefinitionId, String type, int start, int end,
			OrderByComparator<CPDefinitionLink> orderByComparator)
		throws PortalException {

		_checkCommerceCatalog(cpDefinitionId, ActionKeys.VIEW);

		return cpDefinitionLinkLocalService.getCPDefinitionLinks(
			cpDefinitionId, type, start, end, orderByComparator);
	}

	@Override
	public int getCPDefinitionLinksCount(long cpDefinitionId)
		throws PortalException {

		_checkCommerceCatalog(cpDefinitionId, ActionKeys.VIEW);

		return cpDefinitionLinkLocalService.getCPDefinitionLinksCount(
			cpDefinitionId);
	}

	@Override
	public int getCPDefinitionLinksCount(long cpDefinitionId, String type)
		throws PortalException {

		_checkCommerceCatalog(cpDefinitionId, ActionKeys.VIEW);

		return cpDefinitionLinkLocalService.getCPDefinitionLinksCount(
			cpDefinitionId, type);
	}

	@Override
	public CPDefinitionLink updateCPDefinitionLink(
			long cpDefinitionLinkId, double priority,
			ServiceContext serviceContext)
		throws PortalException {

		CPDefinitionLink cpDefinitionLink =
			cpDefinitionLinkLocalService.getCPDefinitionLink(
				cpDefinitionLinkId);

		_checkCommerceCatalog(
			cpDefinitionLink.getCPDefinitionId(), ActionKeys.UPDATE);

		CProduct cProduct = cpDefinitionLink.getCProduct();

		_checkCommerceCatalog(
			cProduct.getPublishedCPDefinitionId(), ActionKeys.UPDATE);

		return cpDefinitionLinkLocalService.updateCPDefinitionLink(
			cpDefinitionLinkId, priority, serviceContext);
	}

	@Override
	public void updateCPDefinitionLinks(
			long cpDefinitionId, long[] cpDefinitionIds2, String type,
			ServiceContext serviceContext)
		throws PortalException {

		_checkCommerceCatalog(cpDefinitionId, ActionKeys.UPDATE);

		if (cpDefinitionIds2 == null) {
			return;
		}

		long[] cProductIds = new long[cpDefinitionIds2.length];

		for (int i = 0; i < cProductIds.length; i++) {
			CPDefinition cpDefinition =
				_cpDefinitionLocalService.fetchCPDefinition(
					cpDefinitionIds2[i]);

			cProductIds[i] = cpDefinition.getCProductId();
		}

		cpDefinitionLinkLocalService.updateCPDefinitionLinkCProductIds(
			cpDefinitionId, cProductIds, type, serviceContext);
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

	@Reference
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	@Reference(
		target = "(model.class.name=com.liferay.commerce.product.model.CommerceCatalog)"
	)
	private ModelResourcePermission<CommerceCatalog>
		_commerceCatalogModelResourcePermission;

	@Reference
	private CPDefinitionLocalService _cpDefinitionLocalService;

}