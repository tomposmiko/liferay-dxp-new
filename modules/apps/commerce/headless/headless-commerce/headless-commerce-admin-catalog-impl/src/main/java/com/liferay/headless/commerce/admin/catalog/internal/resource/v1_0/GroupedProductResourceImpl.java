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

package com.liferay.headless.commerce.admin.catalog.internal.resource.v1_0;

import com.liferay.commerce.product.exception.NoSuchCPDefinitionException;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.service.CPDefinitionService;
import com.liferay.commerce.product.type.grouped.model.CPDefinitionGroupedEntry;
import com.liferay.commerce.product.type.grouped.service.CPDefinitionGroupedEntryService;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.GroupedProduct;
import com.liferay.headless.commerce.admin.catalog.internal.dto.v1_0.converter.GroupedProductDTOConverter;
import com.liferay.headless.commerce.admin.catalog.resource.v1_0.GroupedProductResource;
import com.liferay.headless.commerce.core.util.ServiceContextHelper;
import com.liferay.portal.kernel.change.tracking.CTAware;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/grouped-product.properties",
	scope = ServiceScope.PROTOTYPE, service = GroupedProductResource.class
)
@CTAware
public class GroupedProductResourceImpl extends BaseGroupedProductResourceImpl {

	@Override
	public void deleteGroupedProduct(Long groupedProductId) throws Exception {
		_cpDefinitionGroupedEntryService.deleteCPDefinitionGroupedEntry(
			groupedProductId);
	}

	@Override
	public Page<GroupedProduct>
			getProductByExternalReferenceCodeGroupedProductsPage(
				String externalReferenceCode, Pagination pagination)
		throws Exception {

		CPDefinition cpDefinition =
			_cpDefinitionService.
				fetchCPDefinitionByCProductExternalReferenceCode(
					externalReferenceCode, contextCompany.getCompanyId());

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find product with external reference code " +
					externalReferenceCode);
		}

		return _getGroupedProductsPage(
			cpDefinition.getCPDefinitionId(), pagination);
	}

	@Override
	public Page<GroupedProduct> getProductIdGroupedProductsPage(
			Long productId, Pagination pagination)
		throws Exception {

		CPDefinition cpDefinition =
			_cpDefinitionService.fetchCPDefinitionByCProductId(productId);

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find product with ID " + productId);
		}

		return _getGroupedProductsPage(
			cpDefinition.getCPDefinitionId(), pagination);
	}

	@Override
	public GroupedProduct patchGroupedProduct(
			Long groupedProductId, GroupedProduct groupedProduct)
		throws Exception {

		CPDefinitionGroupedEntry cpDefinitionGroupedEntry =
			_cpDefinitionGroupedEntryService.getCPDefinitionGroupedEntry(
				groupedProductId);

		cpDefinitionGroupedEntry =
			_cpDefinitionGroupedEntryService.updateCPDefinitionGroupedEntry(
				cpDefinitionGroupedEntry.getCPDefinitionGroupedEntryId(),
				GetterUtil.getDouble(
					groupedProduct.getPriority(),
					cpDefinitionGroupedEntry.getPriority()),
				GetterUtil.getInteger(
					groupedProduct.getQuantity(),
					cpDefinitionGroupedEntry.getQuantity()));

		return _toGroupedProduct(
			cpDefinitionGroupedEntry.getCPDefinitionGroupedEntryId());
	}

	@Override
	public GroupedProduct postProductByExternalReferenceCodeGroupedProduct(
			String externalReferenceCode, GroupedProduct groupedProduct)
		throws Exception {

		CPDefinition cpDefinition =
			_cpDefinitionService.
				fetchCPDefinitionByCProductExternalReferenceCode(
					externalReferenceCode, contextCompany.getCompanyId());

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find product with external reference code " +
					externalReferenceCode);
		}

		CPDefinitionGroupedEntry cpDefinitionGroupedEntry =
			_addCPDefinitionGroupedEntry(cpDefinition, groupedProduct);

		return _toGroupedProduct(
			cpDefinitionGroupedEntry.getCPDefinitionGroupedEntryId());
	}

	@Override
	public GroupedProduct postProductIdGroupedProduct(
			Long productId, GroupedProduct groupedProduct)
		throws Exception {

		CPDefinition cpDefinition =
			_cpDefinitionService.fetchCPDefinitionByCProductId(productId);

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find product with ID " + productId);
		}

		CPDefinitionGroupedEntry cpDefinitionGroupedEntry =
			_addCPDefinitionGroupedEntry(cpDefinition, groupedProduct);

		return _toGroupedProduct(
			cpDefinitionGroupedEntry.getCPDefinitionGroupedEntryId());
	}

	private CPDefinitionGroupedEntry _addCPDefinitionGroupedEntry(
			CPDefinition cpDefinition, GroupedProduct groupedProduct)
		throws Exception {

		CPDefinition entryCPDefinition = null;

		if (Validator.isNotNull(
				groupedProduct.getEntryProductExternalReferenceCode())) {

			entryCPDefinition =
				_cpDefinitionService.
					fetchCPDefinitionByCProductExternalReferenceCode(
						groupedProduct.getEntryProductExternalReferenceCode(),
						contextCompany.getCompanyId());
		}

		if (entryCPDefinition == null) {
			entryCPDefinition =
				_cpDefinitionService.fetchCPDefinitionByCProductId(
					groupedProduct.getEntryProductId());
		}

		if (entryCPDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find entry product with ID " +
					groupedProduct.getEntryProductId());
		}

		return _cpDefinitionGroupedEntryService.addCPDefinitionGroupedEntry(
			cpDefinition.getCPDefinitionId(), entryCPDefinition.getCProductId(),
			GetterUtil.getDouble(groupedProduct.getPriority()),
			GetterUtil.getInteger(groupedProduct.getQuantity()),
			_serviceContextHelper.getServiceContext(contextUser));
	}

	private Page<GroupedProduct> _getGroupedProductsPage(
			long cpDefinitionId, Pagination pagination)
		throws Exception {

		return Page.of(
			transform(
				_cpDefinitionGroupedEntryService.getCPDefinitionGroupedEntries(
					cpDefinitionId, pagination.getStartPosition(),
					pagination.getEndPosition(), null),
				cpDefinitionGroupedEntry -> _toGroupedProduct(
					cpDefinitionGroupedEntry.getCPDefinitionGroupedEntryId())),
			pagination,
			_cpDefinitionGroupedEntryService.getCPDefinitionGroupedEntriesCount(
				cpDefinitionId));
	}

	private GroupedProduct _toGroupedProduct(long cpDefinitionGroupedEntryId)
		throws Exception {

		return _groupedProductDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				contextAcceptLanguage.isAcceptAllLanguages(), null,
				_dtoConverterRegistry, cpDefinitionGroupedEntryId,
				contextAcceptLanguage.getPreferredLocale(), contextUriInfo,
				contextUser));
	}

	@Reference
	private CPDefinitionGroupedEntryService _cpDefinitionGroupedEntryService;

	@Reference
	private CPDefinitionService _cpDefinitionService;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

	@Reference
	private GroupedProductDTOConverter _groupedProductDTOConverter;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

}