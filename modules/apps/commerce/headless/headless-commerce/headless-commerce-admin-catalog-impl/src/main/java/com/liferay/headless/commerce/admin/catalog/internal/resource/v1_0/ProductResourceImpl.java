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

import com.liferay.account.model.AccountGroup;
import com.liferay.account.service.AccountGroupRelLocalService;
import com.liferay.account.service.AccountGroupRelService;
import com.liferay.account.service.AccountGroupService;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetTagService;
import com.liferay.commerce.price.list.service.CommercePriceEntryLocalService;
import com.liferay.commerce.price.list.service.CommercePriceListLocalService;
import com.liferay.commerce.product.configuration.CProductVersionConfiguration;
import com.liferay.commerce.product.constants.CPAttachmentFileEntryConstants;
import com.liferay.commerce.product.exception.CPDefinitionProductTypeNameException;
import com.liferay.commerce.product.exception.NoSuchCPDefinitionException;
import com.liferay.commerce.product.exception.NoSuchCatalogException;
import com.liferay.commerce.product.model.CPAttachmentFileEntry;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CProduct;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CPAttachmentFileEntryService;
import com.liferay.commerce.product.service.CPDefinitionLinkService;
import com.liferay.commerce.product.service.CPDefinitionOptionRelService;
import com.liferay.commerce.product.service.CPDefinitionOptionValueRelService;
import com.liferay.commerce.product.service.CPDefinitionService;
import com.liferay.commerce.product.service.CPDefinitionSpecificationOptionValueService;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.commerce.product.service.CPOptionService;
import com.liferay.commerce.product.service.CPSpecificationOptionService;
import com.liferay.commerce.product.service.CProductLocalService;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.service.CommerceChannelRelService;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.product.type.CPType;
import com.liferay.commerce.product.type.CPTypeRegistry;
import com.liferay.commerce.product.type.virtual.constants.VirtualCPTypeConstants;
import com.liferay.commerce.product.type.virtual.service.CPDefinitionVirtualSettingService;
import com.liferay.commerce.service.CPDefinitionInventoryService;
import com.liferay.commerce.shop.by.diagram.constants.CSDiagramCPTypeConstants;
import com.liferay.commerce.shop.by.diagram.service.CSDiagramEntryService;
import com.liferay.commerce.shop.by.diagram.service.CSDiagramPinService;
import com.liferay.commerce.shop.by.diagram.service.CSDiagramSettingService;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Attachment;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Category;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Diagram;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.MappedProduct;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Pin;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Product;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductAccountGroup;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductChannel;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductConfiguration;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductOption;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductOptionValue;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductShippingConfiguration;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductSpecification;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductSubscriptionConfiguration;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductTaxConfiguration;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.ProductVirtualSettings;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.RelatedProduct;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Sku;
import com.liferay.headless.commerce.admin.catalog.internal.dto.v1_0.util.CustomFieldsUtil;
import com.liferay.headless.commerce.admin.catalog.internal.helper.v1_0.ProductHelper;
import com.liferay.headless.commerce.admin.catalog.internal.odata.entity.v1_0.ProductEntityModel;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.AttachmentUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.DiagramUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.MappedProductUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.PinUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductConfigurationUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductOptionUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductOptionValueUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductShippingConfigurationUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductSpecificationUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductSubscriptionConfigurationUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductTaxConfigurationUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.ProductVirtualSettingsUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.RelatedProductUtil;
import com.liferay.headless.commerce.admin.catalog.internal.util.v1_0.SkuUtil;
import com.liferay.headless.commerce.admin.catalog.resource.v1_0.ProductResource;
import com.liferay.headless.commerce.core.util.DateConfig;
import com.liferay.headless.commerce.core.util.ExpandoUtil;
import com.liferay.headless.commerce.core.util.LanguageUtils;
import com.liferay.headless.commerce.core.util.ServiceContextHelper;
import com.liferay.headless.common.spi.odata.entity.EntityFieldsUtil;
import com.liferay.portal.kernel.change.tracking.CTAware;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.settings.SystemSettingsLocator;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.odata.entity.EntityModel;
import com.liferay.portal.search.expando.ExpandoBridgeIndexer;
import com.liferay.portal.vulcan.dto.converter.DTOConverter;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;
import com.liferay.portal.vulcan.dto.converter.DefaultDTOConverterContext;
import com.liferay.portal.vulcan.pagination.Page;
import com.liferay.portal.vulcan.pagination.Pagination;
import com.liferay.upload.UniqueFileNameProvider;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Zoltán Takács
 * @author Alessio Antonio Rendina
 * @author Igor Beslic
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/product.properties",
	scope = ServiceScope.PROTOTYPE, service = ProductResource.class
)
@CTAware
public class ProductResourceImpl extends BaseProductResourceImpl {

	@Override
	public void delete(
			Collection<Product> products, Map<String, Serializable> parameters)
		throws Exception {

		for (Product product : products) {
			deleteProduct(product.getProductId());
		}
	}

	@Override
	public void deleteProduct(Long id) throws Exception {
		CPDefinition cpDefinition =
			_cpDefinitionService.fetchCPDefinitionByCProductId(id);

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find Product with ID: " + id);
		}

		_cpDefinitionService.deleteCPDefinition(
			cpDefinition.getCPDefinitionId());
	}

	@Override
	public void deleteProductByExternalReferenceCode(
			String externalReferenceCode)
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

		_cpDefinitionService.deleteCPDefinition(
			cpDefinition.getCPDefinitionId());
	}

	@Override
	public void deleteProductByExternalReferenceCodeByVersion(
			String externalReferenceCode, Integer version)
		throws Exception {

		CProduct cProduct =
			_cProductLocalService.fetchCProductByExternalReferenceCode(
				externalReferenceCode, contextCompany.getCompanyId());

		if (cProduct == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find product with external reference code " +
					externalReferenceCode);
		}

		CPDefinition cpDefinition =
			_cpDefinitionService.getCProductCPDefinition(
				cProduct.getCProductId(), version);

		_cpDefinitionService.deleteCPDefinition(
			cpDefinition.getCPDefinitionId());
	}

	@Override
	public void deleteProductByVersion(Long id, Integer version)
		throws Exception {

		CPDefinition cpDefinition =
			_cpDefinitionService.getCProductCPDefinition(id, version);

		_cpDefinitionService.deleteCPDefinition(
			cpDefinition.getCPDefinitionId());
	}

	@Override
	public EntityModel getEntityModel(MultivaluedMap multivaluedMap)
		throws Exception {

		return new ProductEntityModel(
			EntityFieldsUtil.getEntityFields(
				_portal.getClassNameId(CPDefinition.class.getName()),
				contextCompany.getCompanyId(), _expandoBridgeIndexer,
				_expandoColumnLocalService, _expandoTableLocalService));
	}

	@Override
	public Product getProduct(Long id) throws Exception {
		CPDefinition cpDefinition =
			_cpDefinitionService.fetchCPDefinitionByCProductId(id);

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find Product with ID: " + id);
		}

		return _toProduct(cpDefinition.getCPDefinitionId());
	}

	@Override
	public Product getProductByExternalReferenceCode(
			String externalReferenceCode)
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

		return _toProduct(cpDefinition.getCPDefinitionId());
	}

	@Override
	public Product getProductByExternalReferenceCodeByVersion(
			String externalReferenceCode, Integer version)
		throws Exception {

		CProduct cProduct =
			_cProductLocalService.fetchCProductByExternalReferenceCode(
				externalReferenceCode, contextCompany.getCompanyId());

		if (cProduct == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find product with external reference code " +
					externalReferenceCode);
		}

		CPDefinition cpDefinition =
			_cpDefinitionService.getCProductCPDefinition(
				cProduct.getCProductId(), version);

		return _toProduct(cpDefinition.getCPDefinitionId());
	}

	@Override
	public Product getProductByVersion(Long id, Integer version)
		throws Exception {

		CPDefinition cpDefinition =
			_cpDefinitionService.getCProductCPDefinition(id, version);

		return _toProduct(cpDefinition.getCPDefinitionId());
	}

	@Override
	public Page<Product> getProductsPage(
			String search, Filter filter, Pagination pagination, Sort[] sorts)
		throws Exception {

		return _productHelper.getProductsPage(
			contextCompany.getCompanyId(), search, filter, pagination, sorts,
			document -> _toProduct(
				GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK))),
			contextAcceptLanguage.getPreferredLocale());
	}

	@Override
	public Response patchProduct(Long id, Product product) throws Exception {
		CPDefinition cpDefinition =
			_cpDefinitionService.fetchCPDefinitionByCProductId(id);

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find Product with ID: " + id);
		}

		_updateProduct(cpDefinition, product);

		if (!Validator.isBlank(product.getExternalReferenceCode())) {
			_cpDefinitionService.updateExternalReferenceCode(
				product.getExternalReferenceCode(),
				cpDefinition.getCPDefinitionId());
		}

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	public Response patchProductByExternalReferenceCode(
			String externalReferenceCode, Product product)
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

		ServiceContext serviceContext = _serviceContextHelper.getServiceContext(
			cpDefinition.getGroupId());

		int productStatus = GetterUtil.getInteger(product.getProductStatus());

		if (productStatus == WorkflowConstants.STATUS_DRAFT) {
			serviceContext.setWorkflowAction(
				WorkflowConstants.ACTION_SAVE_DRAFT);
		}

		_updateProduct(cpDefinition, product);

		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	public Product postProduct(Product product) throws Exception {
		CPDefinition cpDefinition = _addOrUpdateProduct(product);

		return _toProduct(cpDefinition.getCPDefinitionId());
	}

	@Override
	public Product postProductByExternalReferenceCodeClone(
			String externalReferenceCode, String catalogExternalReferenceCode)
		throws Exception {

		CPDefinition cpDefinition =
			_cpDefinitionService.
				fetchCPDefinitionByCProductExternalReferenceCode(
					externalReferenceCode, contextCompany.getCompanyId());

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException();
		}

		CommerceCatalog commerceCatalog = cpDefinition.getCommerceCatalog();

		if (catalogExternalReferenceCode != null) {
			commerceCatalog =
				_commerceCatalogLocalService.
					fetchCommerceCatalogByExternalReferenceCode(
						catalogExternalReferenceCode,
						contextCompany.getCompanyId());
		}

		if (commerceCatalog == null) {
			throw new NoSuchCatalogException();
		}

		cpDefinition = _cpDefinitionService.copyCPDefinition(
			cpDefinition.getCPDefinitionId(), commerceCatalog.getGroupId(),
			WorkflowConstants.STATUS_DRAFT);

		return _toProduct(cpDefinition.getCPDefinitionId());
	}

	@Override
	public Product postProductClone(Long id, Long catalogId) throws Exception {
		CPDefinition cpDefinition =
			_cpDefinitionService.fetchCPDefinitionByCProductId(id);

		if (cpDefinition == null) {
			throw new NoSuchCPDefinitionException(
				"Unable to find Product with ID: " + id);
		}

		CommerceCatalog commerceCatalog = cpDefinition.getCommerceCatalog();

		if (catalogId != null) {
			commerceCatalog = _commerceCatalogLocalService.getCommerceCatalog(
				catalogId);
		}

		cpDefinition = _cpDefinitionService.cloneCPDefinition(
			cpDefinition.getCPDefinitionId(), commerceCatalog.getGroupId(),
			_serviceContextHelper.getServiceContext(
				commerceCatalog.getGroupId()));

		return _toProduct(cpDefinition.getCPDefinitionId());
	}

	@Override
	public void update(
			Collection<Product> products, Map<String, Serializable> parameters)
		throws Exception {

		for (Product product : products) {
			patchProduct(product.getProductId(), product);
		}
	}

	private CPDefinition _addOrUpdateProduct(Product product) throws Exception {
		CommerceCatalog commerceCatalog =
			_commerceCatalogLocalService.getCommerceCatalog(
				product.getCatalogId());

		ServiceContext serviceContext = _serviceContextHelper.getServiceContext(
			commerceCatalog.getGroupId());

		String[] assetTagNames = new String[0];

		if (product.getTags() != null) {
			assetTagNames = product.getTags();
		}

		serviceContext.setAssetTagNames(assetTagNames);

		serviceContext.setExpandoBridgeAttributes(
			_getExpandoBridgeAttributes(product));

		DateConfig displayDateConfig = DateConfig.toDisplayDateConfig(
			product.getDisplayDate(), serviceContext.getTimeZone());
		DateConfig expirationDateConfig = DateConfig.toExpirationDateConfig(
			product.getExpirationDate(), serviceContext.getTimeZone());

		ProductShippingConfiguration productShippingConfiguration =
			_getProductShippingConfiguration(product);
		ProductSubscriptionConfiguration productSubscriptionConfiguration =
			_getProductSubscriptionConfiguration(product);
		ProductTaxConfiguration productTaxConfiguration =
			_getProductTaxConfiguration(product);

		CPDefinition cpDefinition =
			_cpDefinitionService.
				fetchCPDefinitionByCProductExternalReferenceCode(
					product.getExternalReferenceCode(),
					contextCompany.getCompanyId());

		Category[] categories = product.getCategories();

		if (categories != null) {
			serviceContext.setAssetCategoryIds(
				transformToLongArray(
					Arrays.asList(categories),
					category -> {
						if (Validator.isNull(
								category.getExternalReferenceCode())) {

							return category.getId();
						}

						AssetCategory assetCategory =
							_assetCategoryLocalService.
								fetchAssetCategoryByExternalReferenceCode(
									category.getExternalReferenceCode(),
									contextCompany.getGroupId());

						return assetCategory.getCategoryId();
					}));
		}
		else if (cpDefinition != null) {
			serviceContext.setAssetCategoryIds(
				_assetCategoryLocalService.getCategoryIds(
					cpDefinition.getModelClassName(),
					cpDefinition.getCPDefinitionId()));
		}

		Map<String, String> nameMap = product.getName();

		if ((cpDefinition != null) && (nameMap == null)) {
			nameMap = LanguageUtils.getLanguageIdMap(cpDefinition.getNameMap());
		}

		Map<String, String> shortDescriptionMap = product.getShortDescription();

		if ((cpDefinition != null) && (shortDescriptionMap == null)) {
			shortDescriptionMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getShortDescriptionMap());
		}

		Map<String, String> descriptionMap = product.getDescription();

		if ((cpDefinition != null) && (descriptionMap == null)) {
			descriptionMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getDescriptionMap());
		}

		Map<String, String> urlTitleMap = product.getUrls();

		if ((cpDefinition != null) && (urlTitleMap == null)) {
			urlTitleMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getUrlTitleMap());
		}

		Map<String, String> metaTitleMap = product.getMetaTitle();

		if ((cpDefinition != null) && (metaTitleMap == null)) {
			metaTitleMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getMetaTitleMap());
		}

		Map<String, String> metaDescriptionMap = product.getMetaDescription();

		if ((cpDefinition != null) && (metaDescriptionMap == null)) {
			metaDescriptionMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getMetaDescriptionMap());
		}

		Map<String, String> metaKeywordsMap = product.getMetaKeyword();

		if ((cpDefinition != null) && (metaKeywordsMap == null)) {
			metaKeywordsMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getMetaKeywordsMap());
		}

		boolean ignoreSKUCombinations = true;

		if (cpDefinition != null) {
			ignoreSKUCombinations = cpDefinition.isIgnoreSKUCombinations();
		}

		int productStatus = GetterUtil.getInteger(product.getProductStatus());

		if (productStatus != WorkflowConstants.STATUS_APPROVED) {
			serviceContext.setWorkflowAction(
				WorkflowConstants.ACTION_SAVE_DRAFT);
		}

		cpDefinition = _cpDefinitionService.addOrUpdateCPDefinition(
			product.getExternalReferenceCode(), commerceCatalog.getGroupId(),
			LanguageUtils.getLocalizedMap(nameMap),
			LanguageUtils.getLocalizedMap(shortDescriptionMap),
			LanguageUtils.getLocalizedMap(descriptionMap),
			LanguageUtils.getLocalizedMap(urlTitleMap),
			LanguageUtils.getLocalizedMap(metaTitleMap),
			LanguageUtils.getLocalizedMap(metaDescriptionMap),
			LanguageUtils.getLocalizedMap(metaKeywordsMap),
			product.getProductType(), ignoreSKUCombinations,
			GetterUtil.getBoolean(
				productShippingConfiguration.getShippable(), true),
			GetterUtil.getBoolean(
				productShippingConfiguration.getFreeShipping(), true),
			GetterUtil.getBoolean(
				productShippingConfiguration.getShippingSeparately(), true),
			GetterUtil.getDouble(
				productShippingConfiguration.getShippingExtraPrice()),
			GetterUtil.getDouble(productShippingConfiguration.getWidth()),
			GetterUtil.getDouble(productShippingConfiguration.getHeight()),
			GetterUtil.getDouble(productShippingConfiguration.getDepth()),
			GetterUtil.getDouble(productShippingConfiguration.getWeight()),
			GetterUtil.getLong(productTaxConfiguration.getId()),
			ProductUtil.isTaxExempt(null, productTaxConfiguration), false, null,
			true, displayDateConfig.getMonth(), displayDateConfig.getDay(),
			displayDateConfig.getYear(), displayDateConfig.getHour(),
			displayDateConfig.getMinute(), expirationDateConfig.getMonth(),
			expirationDateConfig.getDay(), expirationDateConfig.getYear(),
			expirationDateConfig.getHour(), expirationDateConfig.getMinute(),
			GetterUtil.getBoolean(product.getNeverExpire(), true),
			product.getDefaultSku(),
			GetterUtil.getBoolean(productSubscriptionConfiguration.getEnable()),
			GetterUtil.getInteger(
				productSubscriptionConfiguration.getLength(), 1),
			GetterUtil.getString(
				productSubscriptionConfiguration.getSubscriptionTypeAsString()),
			null,
			GetterUtil.getLong(
				productSubscriptionConfiguration.getNumberOfLength()),
			productStatus, serviceContext);

		if ((product.getActive() != null) && !product.getActive()) {
			Map<String, Serializable> workflowContext = new HashMap<>();

			_cpDefinitionService.updateStatus(
				cpDefinition.getCPDefinitionId(),
				WorkflowConstants.STATUS_INACTIVE, serviceContext,
				workflowContext);
		}

		Map<String, ?> expando = product.getExpando();

		if ((expando != null) && !expando.isEmpty()) {
			ExpandoUtil.updateExpando(
				serviceContext.getCompanyId(), CPDefinition.class,
				cpDefinition.getPrimaryKey(), expando);
		}

		_updateNestedResources(product, cpDefinition, serviceContext);

		return cpDefinition;
	}

	private Map<String, Map<String, String>> _getActions(
		CPDefinition cpDefinition) {

		return HashMapBuilder.<String, Map<String, String>>put(
			"delete",
			addAction(
				"UPDATE", cpDefinition.getCPDefinitionId(), "deleteProduct",
				_cpDefinitionModelResourcePermission)
		).put(
			"get",
			addAction(
				"VIEW", cpDefinition.getCPDefinitionId(), "getProduct",
				_cpDefinitionModelResourcePermission)
		).put(
			"update",
			addAction(
				"UPDATE", cpDefinition.getCPDefinitionId(), "patchProduct",
				_cpDefinitionModelResourcePermission)
		).build();
	}

	private CPDefinition _getCPDefinition(
			CPDefinition cpDefinition, ServiceContext serviceContext)
		throws Exception {

		if (!cpDefinition.isDraft() &&
			(serviceContext.getWorkflowAction() ==
				WorkflowConstants.ACTION_SAVE_DRAFT)) {

			CProductVersionConfiguration cProductVersionConfiguration =
				_configurationProvider.getConfiguration(
					CProductVersionConfiguration.class,
					new SystemSettingsLocator(
						CProductVersionConfiguration.class.getName()));

			if (cProductVersionConfiguration.enabled()) {
				for (CPDefinition cProductCPDefinition :
						_cpDefinitionService.getCProductCPDefinitions(
							cpDefinition.getCProductId(),
							WorkflowConstants.STATUS_DRAFT, QueryUtil.ALL_POS,
							QueryUtil.ALL_POS)) {

					_cpDefinitionService.updateStatus(
						cProductCPDefinition.getCPDefinitionId(),
						WorkflowConstants.STATUS_INCOMPLETE, serviceContext,
						Collections.emptyMap());
				}

				cpDefinition = _cpDefinitionService.copyCPDefinition(
					cpDefinition.getCPDefinitionId(), cpDefinition.getGroupId(),
					WorkflowConstants.STATUS_DRAFT);
			}
		}

		return cpDefinition;
	}

	private Map<String, Serializable> _getExpandoBridgeAttributes(
		Attachment attachment) {

		return CustomFieldsUtil.toMap(
			CPAttachmentFileEntry.class.getName(),
			contextCompany.getCompanyId(), attachment.getCustomFields(),
			contextAcceptLanguage.getPreferredLocale());
	}

	private Map<String, Serializable> _getExpandoBridgeAttributes(
		Product product) {

		return CustomFieldsUtil.toMap(
			CPDefinition.class.getName(), contextCompany.getCompanyId(),
			product.getCustomFields(),
			contextAcceptLanguage.getPreferredLocale());
	}

	private Map<String, Serializable> _getExpandoBridgeAttributes(Sku sku) {
		return CustomFieldsUtil.toMap(
			CPInstance.class.getName(), contextCompany.getCompanyId(),
			sku.getCustomFields(), contextAcceptLanguage.getPreferredLocale());
	}

	private ProductShippingConfiguration _getProductShippingConfiguration(
		Product product) {

		ProductShippingConfiguration productShippingConfiguration =
			product.getShippingConfiguration();

		if (productShippingConfiguration != null) {
			return productShippingConfiguration;
		}

		return new ProductShippingConfiguration();
	}

	private ProductSubscriptionConfiguration
		_getProductSubscriptionConfiguration(Product product) {

		ProductSubscriptionConfiguration productSubscriptionConfiguration =
			product.getSubscriptionConfiguration();

		if (productSubscriptionConfiguration != null) {
			return productSubscriptionConfiguration;
		}

		return new ProductSubscriptionConfiguration();
	}

	private ProductTaxConfiguration _getProductTaxConfiguration(
		Product product) {

		ProductTaxConfiguration productTaxConfiguration =
			product.getTaxConfiguration();

		if (productTaxConfiguration != null) {
			return productTaxConfiguration;
		}

		return new ProductTaxConfiguration();
	}

	private Product _toProduct(Long cpDefinitionId) throws Exception {
		CPDefinition cpDefinition = _cpDefinitionService.getCPDefinition(
			cpDefinitionId);

		return _productDTOConverter.toDTO(
			new DefaultDTOConverterContext(
				contextAcceptLanguage.isAcceptAllLanguages(),
				_getActions(cpDefinition), _dtoConverterRegistry,
				cpDefinitionId, contextAcceptLanguage.getPreferredLocale(),
				contextUriInfo, contextUser));
	}

	private CPDefinition _updateNestedResources(
			Product product, CPDefinition cpDefinition,
			ServiceContext serviceContext)
		throws Exception {

		serviceContext.setExpandoBridgeAttributes(null);

		// Product configuration

		ProductConfiguration productConfiguration = product.getConfiguration();

		if (productConfiguration != null) {
			ProductConfigurationUtil.updateCPDefinitionInventory(
				_cpDefinitionInventoryService, productConfiguration,
				cpDefinition.getCPDefinitionId());
		}

		// Product shipping configuration

		ProductShippingConfiguration productShippingConfiguration =
			product.getShippingConfiguration();

		if (productShippingConfiguration != null) {
			cpDefinition =
				ProductShippingConfigurationUtil.updateCPDefinitionShippingInfo(
					_cpDefinitionService, productShippingConfiguration,
					cpDefinition, serviceContext);
		}

		// Product subscription configuration

		ProductSubscriptionConfiguration productSubscriptionConfiguration =
			product.getSubscriptionConfiguration();

		if (productSubscriptionConfiguration != null) {
			cpDefinition =
				ProductSubscriptionConfigurationUtil.
					updateCPDefinitionSubscriptionInfo(
						_cpDefinitionService, productSubscriptionConfiguration,
						cpDefinition, serviceContext);
		}

		// Product tax configuration

		ProductTaxConfiguration productTaxConfiguration =
			product.getTaxConfiguration();

		if (productTaxConfiguration != null) {
			cpDefinition =
				ProductTaxConfigurationUtil.updateCPDefinitionTaxCategoryInfo(
					_cpDefinitionService, productTaxConfiguration,
					cpDefinition);
		}

		// Product specifications

		ProductSpecification[] productSpecifications =
			product.getProductSpecifications();

		if (productSpecifications != null) {
			_cpDefinitionSpecificationOptionValueService.
				deleteCPDefinitionSpecificationOptionValues(
					cpDefinition.getCPDefinitionId());

			for (ProductSpecification productSpecification :
					productSpecifications) {

				ProductSpecificationUtil.
					addCPDefinitionSpecificationOptionValue(
						_cpDefinitionSpecificationOptionValueService,
						_cpSpecificationOptionService,
						cpDefinition.getCPDefinitionId(), productSpecification,
						serviceContext);
			}
		}

		// Product options

		ProductOption[] productOptions = product.getProductOptions();

		if (productOptions != null) {
			for (ProductOption productOption : productOptions) {
				CPDefinitionOptionRel cpDefinitionOptionRel =
					ProductOptionUtil.addOrUpdateCPDefinitionOptionRel(
						_cpDefinitionOptionRelService, _cpOptionService,
						productOption, cpDefinition.getCPDefinitionId(),
						serviceContext);

				ProductOptionValue[] productOptionValues =
					productOption.getProductOptionValues();

				if (productOptionValues != null) {
					for (ProductOptionValue productOptionValue :
							productOptionValues) {

						ProductOptionValueUtil.
							addOrUpdateCPDefinitionOptionValueRel(
								_cpDefinitionOptionValueRelService,
								productOptionValue,
								cpDefinitionOptionRel.
									getCPDefinitionOptionRelId(),
								serviceContext);
					}
				}
			}
		}

		// Related products

		RelatedProduct[] relatedProducts = product.getRelatedProducts();

		if (relatedProducts != null) {
			for (RelatedProduct relatedProduct : relatedProducts) {
				RelatedProductUtil.addOrUpdateCPDefinitionLink(
					_cpDefinitionLinkService, _cpDefinitionService,
					relatedProduct, cpDefinition.getCPDefinitionId(),
					_serviceContextHelper.getServiceContext(
						cpDefinition.getGroupId()));
			}
		}

		// Skus

		Sku[] skus = product.getSkus();

		if (skus != null) {
			for (Sku sku : skus) {
				serviceContext.setExpandoBridgeAttributes(
					_getExpandoBridgeAttributes(sku));

				CPInstance cpInstance = SkuUtil.addOrUpdateCPInstance(
					_cpInstanceService, sku, cpDefinition,
					_cpDefinitionOptionRelService,
					_cpDefinitionOptionValueRelService, serviceContext);

				serviceContext.setExpandoBridgeAttributes(null);

				SkuUtil.updateCommercePriceEntries(
					_commercePriceEntryLocalService,
					_commercePriceListLocalService, _configurationProvider,
					cpInstance,
					(BigDecimal)GetterUtil.get(
						sku.getPrice(), cpInstance.getPrice()),
					(BigDecimal)GetterUtil.get(
						sku.getPromoPrice(), cpInstance.getPromoPrice()),
					serviceContext);
			}
		}

		// Images

		Attachment[] images = product.getImages();

		if (images != null) {
			for (Attachment attachment : images) {
				serviceContext.setExpandoBridgeAttributes(
					_getExpandoBridgeAttributes(attachment));

				AttachmentUtil.addOrUpdateCPAttachmentFileEntry(
					cpDefinition.getGroupId(), _cpAttachmentFileEntryService,
					_cpDefinitionOptionRelService,
					_cpDefinitionOptionValueRelService, _cpOptionService,
					_uniqueFileNameProvider, attachment,
					_classNameLocalService.getClassNameId(
						cpDefinition.getModelClassName()),
					cpDefinition.getCPDefinitionId(),
					CPAttachmentFileEntryConstants.TYPE_IMAGE, serviceContext);
			}
		}

		// Attachments

		Attachment[] attachments = product.getAttachments();

		if (attachments != null) {
			for (Attachment attachment : attachments) {
				serviceContext.setExpandoBridgeAttributes(
					_getExpandoBridgeAttributes(attachment));

				AttachmentUtil.addOrUpdateCPAttachmentFileEntry(
					cpDefinition.getGroupId(), _cpAttachmentFileEntryService,
					_cpDefinitionOptionRelService,
					_cpDefinitionOptionValueRelService, _cpOptionService,
					_uniqueFileNameProvider, attachment,
					_classNameLocalService.getClassNameId(
						cpDefinition.getModelClassName()),
					cpDefinition.getCPDefinitionId(),
					CPAttachmentFileEntryConstants.TYPE_OTHER, serviceContext);
			}
		}

		// Channels visibility

		_cpDefinitionService.updateCPDefinitionChannelFilter(
			cpDefinition.getCPDefinitionId(),
			GetterUtil.getBoolean(
				product.getProductChannelFilter(),
				cpDefinition.isChannelFilterEnabled()));

		ProductChannel[] productChannels = product.getProductChannels();

		if (productChannels != null) {
			_commerceChannelRelService.deleteCommerceChannelRels(
				CPDefinition.class.getName(), cpDefinition.getCPDefinitionId());

			for (ProductChannel productChannel : productChannels) {
				if (productChannel.getExternalReferenceCode() == null) {
					Long commerceChannelId = productChannel.getChannelId();

					if (commerceChannelId != null) {
						_commerceChannelRelService.addCommerceChannelRel(
							CPDefinition.class.getName(),
							cpDefinition.getCPDefinitionId(), commerceChannelId,
							serviceContext);
					}

					continue;
				}

				CommerceChannel commerceChannel = null;

				try {
					commerceChannel =
						_commerceChannelService.fetchByExternalReferenceCode(
							productChannel.getExternalReferenceCode(),
							contextCompany.getCompanyId());
				}
				catch (PortalException portalException) {
					if (_log.isDebugEnabled()) {
						_log.debug(portalException);
					}
				}

				if (commerceChannel == null) {
					continue;
				}

				_commerceChannelRelService.addCommerceChannelRel(
					CPDefinition.class.getName(),
					cpDefinition.getCPDefinitionId(),
					commerceChannel.getCommerceChannelId(), serviceContext);
			}
		}

		// Account groups visibility

		_cpDefinitionService.updateCPDefinitionAccountGroupFilter(
			cpDefinition.getCPDefinitionId(),
			GetterUtil.getBoolean(
				product.getProductAccountGroupFilter(),
				cpDefinition.isAccountGroupFilterEnabled()));

		ProductAccountGroup[] productAccountGroups =
			product.getProductAccountGroups();

		if (productAccountGroups != null) {
			_accountGroupRelLocalService.deleteAccountGroupRels(
				CPDefinition.class.getName(),
				new long[] {cpDefinition.getCPDefinitionId()});

			for (ProductAccountGroup productAccountGroup :
					productAccountGroups) {

				String externalReferenceCode =
					productAccountGroup.getExternalReferenceCode();

				if (externalReferenceCode == null) {
					Long accountGroupId =
						productAccountGroup.getAccountGroupId();

					if (accountGroupId != null) {
						_accountGroupRelService.addAccountGroupRel(
							accountGroupId, CPDefinition.class.getName(),
							cpDefinition.getCPDefinitionId());
					}

					continue;
				}

				AccountGroup accountGroup = null;

				try {
					accountGroup =
						_accountGroupService.
							fetchAccountGroupByExternalReferenceCode(
								productAccountGroup.getExternalReferenceCode(),
								contextCompany.getCompanyId());
				}
				catch (PortalException portalException) {
					if (_log.isDebugEnabled()) {
						_log.debug(portalException);
					}
				}

				if (accountGroup == null) {
					continue;
				}

				_accountGroupRelService.addAccountGroupRel(
					accountGroup.getAccountGroupId(),
					CPDefinition.class.getName(),
					cpDefinition.getCPDefinitionId());
			}
		}

		CPType cpType = _cpTypeRegistry.getCPType(
			cpDefinition.getProductTypeName());

		if (cpType == null) {
			return cpDefinition;
		}

		// Diagram

		Diagram diagram = product.getDiagram();
		MappedProduct[] mappedProducts = product.getMappedProducts();
		Pin[] pins = product.getPins();

		if ((diagram != null) || (mappedProducts != null) || (pins != null)) {
			if (CSDiagramCPTypeConstants.NAME.equals(cpType.getName())) {
				if (diagram != null) {
					DiagramUtil.addOrUpdateCSDiagramSetting(
						contextCompany.getCompanyId(),
						_cpAttachmentFileEntryService,
						cpDefinition.getCPDefinitionId(),
						_cpDefinitionOptionRelService,
						_cpDefinitionOptionValueRelService, _cpOptionService,
						_csDiagramSettingService, diagram,
						cpDefinition.getGroupId(),
						contextAcceptLanguage.getPreferredLocale(),
						_serviceContextHelper, _uniqueFileNameProvider);
				}

				if (mappedProducts != null) {
					_csDiagramEntryService.deleteCSDiagramEntries(
						cpDefinition.getCPDefinitionId());

					for (MappedProduct mappedProduct : mappedProducts) {
						MappedProductUtil.addOrUpdateCSDiagramEntry(
							contextCompany.getCompanyId(),
							cpDefinition.getCPDefinitionId(),
							_cpDefinitionService, _cpInstanceService,
							_csDiagramEntryService, cpDefinition.getGroupId(),
							contextAcceptLanguage.getPreferredLocale(),
							mappedProduct, _serviceContextHelper);
					}
				}

				if (pins != null) {
					_csDiagramPinService.deleteCSDiagramPins(
						cpDefinition.getCPDefinitionId());

					for (Pin pin : pins) {
						PinUtil.addOrUpdateCSDiagramPin(
							cpDefinition.getCPDefinitionId(),
							_csDiagramPinService, pin);
					}
				}
			}
			else {
				throw new CPDefinitionProductTypeNameException();
			}
		}

		// Virtual

		ProductVirtualSettings productVirtualSettings =
			product.getProductVirtualSettings();

		if (productVirtualSettings != null) {
			if (VirtualCPTypeConstants.NAME.equals(cpType.getName())) {
				ProductVirtualSettingsUtil.addOrUpdateProductVirtualSettings(
					cpDefinition, productVirtualSettings,
					_cpDefinitionVirtualSettingService, _uniqueFileNameProvider,
					serviceContext);
			}
			else {
				throw new CPDefinitionProductTypeNameException();
			}
		}

		return cpDefinition;
	}

	private CPDefinition _updateProduct(
			CPDefinition cpDefinition, Product product)
		throws Exception {

		ServiceContext serviceContext = _serviceContextHelper.getServiceContext(
			cpDefinition.getGroupId());

		int productStatus = GetterUtil.getInteger(product.getProductStatus());

		if (productStatus == WorkflowConstants.STATUS_DRAFT) {
			serviceContext.setWorkflowAction(
				WorkflowConstants.ACTION_SAVE_DRAFT);
		}

		cpDefinition = _getCPDefinition(cpDefinition, serviceContext);

		String[] assetTagNames = product.getTags();

		if (assetTagNames == null) {
			assetTagNames = transformToArray(
				_assetTagService.getTags(
					cpDefinition.getModelClassName(),
					cpDefinition.getCPDefinitionId()),
				AssetTag::getName, String.class);
		}

		serviceContext.setAssetTagNames(assetTagNames);

		serviceContext.setExpandoBridgeAttributes(
			_getExpandoBridgeAttributes(product));

		Calendar displayCalendar = CalendarFactoryUtil.getCalendar(
			serviceContext.getTimeZone());

		DateConfig displayDateConfig = new DateConfig(displayCalendar);

		Calendar expirationCalendar = CalendarFactoryUtil.getCalendar(
			serviceContext.getTimeZone());

		expirationCalendar.add(Calendar.MONTH, 1);

		DateConfig expirationDateConfig = new DateConfig(expirationCalendar);

		Category[] categories = product.getCategories();

		if (categories == null) {
			serviceContext.setAssetCategoryIds(
				_assetCategoryLocalService.getCategoryIds(
					cpDefinition.getModelClassName(),
					cpDefinition.getCPDefinitionId()));
		}
		else {
			serviceContext.setAssetCategoryIds(
				transformToLongArray(
					Arrays.asList(categories),
					category -> {
						if (Validator.isNull(
								category.getExternalReferenceCode())) {

							return category.getId();
						}

						AssetCategory assetCategory =
							_assetCategoryLocalService.
								fetchAssetCategoryByExternalReferenceCode(
									category.getExternalReferenceCode(),
									contextCompany.getGroupId());

						return assetCategory.getCategoryId();
					}));
		}

		Map<String, String> nameMap = product.getName();

		if ((cpDefinition != null) && (nameMap == null)) {
			nameMap = LanguageUtils.getLanguageIdMap(cpDefinition.getNameMap());
		}

		Map<String, String> shortDescriptionMap = product.getShortDescription();

		if ((cpDefinition != null) && (shortDescriptionMap == null)) {
			shortDescriptionMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getShortDescriptionMap());
		}

		Map<String, String> descriptionMap = product.getDescription();

		if ((cpDefinition != null) && (descriptionMap == null)) {
			descriptionMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getDescriptionMap());
		}

		Map<String, String> urlTitleMap = product.getUrls();

		if ((cpDefinition != null) && (urlTitleMap == null)) {
			urlTitleMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getUrlTitleMap());
		}

		Map<String, String> metaTitleMap = product.getMetaTitle();

		if ((cpDefinition != null) && (metaTitleMap == null)) {
			metaTitleMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getMetaTitleMap());
		}

		Map<String, String> metaDescriptionMap = product.getMetaDescription();

		if ((cpDefinition != null) && (metaDescriptionMap == null)) {
			metaDescriptionMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getMetaDescriptionMap());
		}

		Map<String, String> metaKeywordsMap = product.getMetaKeyword();

		if ((cpDefinition != null) && (metaKeywordsMap == null)) {
			metaKeywordsMap = LanguageUtils.getLanguageIdMap(
				cpDefinition.getMetaKeywordsMap());
		}

		cpDefinition = _cpDefinitionService.updateCPDefinition(
			cpDefinition.getCPDefinitionId(),
			LanguageUtils.getLocalizedMap(nameMap),
			LanguageUtils.getLocalizedMap(shortDescriptionMap),
			LanguageUtils.getLocalizedMap(descriptionMap),
			LanguageUtils.getLocalizedMap(urlTitleMap),
			LanguageUtils.getLocalizedMap(metaTitleMap),
			LanguageUtils.getLocalizedMap(metaDescriptionMap),
			LanguageUtils.getLocalizedMap(metaKeywordsMap),
			cpDefinition.isIgnoreSKUCombinations(),
			cpDefinition.getDDMStructureKey(), true,
			displayDateConfig.getMonth(), displayDateConfig.getDay(),
			displayDateConfig.getYear(), displayDateConfig.getHour(),
			displayDateConfig.getMinute(), expirationDateConfig.getMonth(),
			expirationDateConfig.getDay(), expirationDateConfig.getYear(),
			expirationDateConfig.getHour(), expirationDateConfig.getMinute(),
			GetterUtil.getBoolean(product.getNeverExpire(), true),
			serviceContext);

		if ((product.getActive() != null) && !product.getActive()) {
			Map<String, Serializable> workflowContext = new HashMap<>();

			_cpDefinitionService.updateStatus(
				cpDefinition.getCPDefinitionId(),
				WorkflowConstants.STATUS_INACTIVE, serviceContext,
				workflowContext);
		}

		Map<String, ?> expando = product.getExpando();

		if ((expando != null) && !expando.isEmpty()) {
			ExpandoUtil.updateExpando(
				serviceContext.getCompanyId(), CPDefinition.class,
				cpDefinition.getPrimaryKey(), expando);
		}

		return _updateNestedResources(product, cpDefinition, serviceContext);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ProductResourceImpl.class);

	@Reference
	private AccountGroupRelLocalService _accountGroupRelLocalService;

	@Reference
	private AccountGroupRelService _accountGroupRelService;

	@Reference
	private AccountGroupService _accountGroupService;

	@Reference
	private AssetCategoryLocalService _assetCategoryLocalService;

	@Reference
	private AssetTagService _assetTagService;

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	@Reference
	private CommerceChannelRelService _commerceChannelRelService;

	@Reference
	private CommerceChannelService _commerceChannelService;

	@Reference
	private CommercePriceEntryLocalService _commercePriceEntryLocalService;

	@Reference
	private CommercePriceListLocalService _commercePriceListLocalService;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private CPAttachmentFileEntryService _cpAttachmentFileEntryService;

	@Reference
	private CPDefinitionInventoryService _cpDefinitionInventoryService;

	@Reference
	private CPDefinitionLinkService _cpDefinitionLinkService;

	@Reference(
		target = "(model.class.name=com.liferay.commerce.product.model.CPDefinition)"
	)
	private ModelResourcePermission<CPDefinition>
		_cpDefinitionModelResourcePermission;

	@Reference
	private CPDefinitionOptionRelService _cpDefinitionOptionRelService;

	@Reference
	private CPDefinitionOptionValueRelService
		_cpDefinitionOptionValueRelService;

	@Reference
	private CPDefinitionService _cpDefinitionService;

	@Reference
	private CPDefinitionSpecificationOptionValueService
		_cpDefinitionSpecificationOptionValueService;

	@Reference
	private CPDefinitionVirtualSettingService
		_cpDefinitionVirtualSettingService;

	@Reference
	private CPInstanceService _cpInstanceService;

	@Reference
	private CPOptionService _cpOptionService;

	@Reference
	private CProductLocalService _cProductLocalService;

	@Reference
	private CPSpecificationOptionService _cpSpecificationOptionService;

	@Reference
	private CPTypeRegistry _cpTypeRegistry;

	@Reference
	private CSDiagramEntryService _csDiagramEntryService;

	@Reference
	private CSDiagramPinService _csDiagramPinService;

	@Reference
	private CSDiagramSettingService _csDiagramSettingService;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

	@Reference
	private ExpandoBridgeIndexer _expandoBridgeIndexer;

	@Reference
	private ExpandoColumnLocalService _expandoColumnLocalService;

	@Reference
	private ExpandoTableLocalService _expandoTableLocalService;

	@Reference
	private Portal _portal;

	@Reference(
		target = "(component.name=com.liferay.headless.commerce.admin.catalog.internal.dto.v1_0.converter.ProductDTOConverter)"
	)
	private DTOConverter<CPDefinition, Product> _productDTOConverter;

	@Reference
	private ProductHelper _productHelper;

	@Reference
	private ServiceContextHelper _serviceContextHelper;

	@Reference
	private UniqueFileNameProvider _uniqueFileNameProvider;

}