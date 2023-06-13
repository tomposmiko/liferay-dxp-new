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

package com.liferay.commerce.internal.object.system;

import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionTable;
import com.liferay.commerce.product.model.CProduct;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CProductLocalService;
import com.liferay.headless.commerce.admin.catalog.dto.v1_0.Product;
import com.liferay.headless.commerce.admin.catalog.resource.v1_0.ProductResource;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.model.ObjectField;
import com.liferay.object.system.BaseSystemObjectDefinitionMetadata;
import com.liferay.object.system.JaxRsApplicationDescriptor;
import com.liferay.object.system.SystemObjectDefinitionMetadata;
import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.Table;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author José Abelenda
 */
@Component(enabled = true, service = SystemObjectDefinitionMetadata.class)
public class CPDefinitionSystemObjectDefinitionMetadata
	extends BaseSystemObjectDefinitionMetadata {

	@Override
	public long addBaseModel(User user, Map<String, Object> values)
		throws Exception {

		ProductResource productResource = _buildProductResource(user);

		Product product = productResource.postProduct(_toProduct(values));

		setExtendedProperties(Product.class.getName(), product, user, values);

		return product.getId();
	}

	@Override
	public BaseModel<?> deleteBaseModel(BaseModel<?> baseModel)
		throws PortalException {

		return _cpDefinitionLocalService.deleteCPDefinition(
			(CPDefinition)baseModel);
	}

	@Override
	public BaseModel<?> getBaseModelByExternalReferenceCode(
			String externalReferenceCode, long companyId)
		throws PortalException {

		return _cProductLocalService.getCProductByExternalReferenceCode(
			externalReferenceCode, companyId);
	}

	@Override
	public String getExternalReferenceCode(long primaryKey)
		throws PortalException {

		CProduct cProduct = _cProductLocalService.getCProduct(primaryKey);

		return cProduct.getExternalReferenceCode();
	}

	@Override
	public JaxRsApplicationDescriptor getJaxRsApplicationDescriptor() {
		return new JaxRsApplicationDescriptor(
			"Liferay.Headless.Commerce.Admin.Catalog",
			"headless-commerce-admin-catalog", "products", "v1.0");
	}

	@Override
	public Map<Locale, String> getLabelMap() {
		return createLabelMap("cp-definition");
	}

	@Override
	public Class<?> getModelClass() {
		return CPDefinition.class;
	}

	@Override
	public List<ObjectField> getObjectFields() {
		return Arrays.asList(
			createObjectField(
				"Boolean", "Boolean", "active", "active", true, true),
			createObjectField(
				"LongInteger", "Long", "catalog-id", "catalogId", true, true),
			createObjectField(
				"Text", "String", "description", "description", false, true),
			createObjectField("Text", "String", "name", "name", true, true),
			createObjectField(
				"Text", "CPDefinitionId", "String", "product-id", "productId",
				false, true),
			createObjectField(
				"Text", "String", "product-type", "productType", true, true),
			createObjectField(
				"Text", "String", "short-description", "shortDescription",
				false, true),
			createObjectField(
				"Text", "String", "sku", "skuFormatted", false, true),
			createObjectField(
				"Text", "String", "thumbnail", "thumbnail", false, true),
			createObjectField("Text", "String", "uuid", "uuid", false, true));
	}

	@Override
	public Map<Locale, String> getPluralLabelMap() {
		return createLabelMap("cp-definitions");
	}

	@Override
	public Column<?, Long> getPrimaryKeyColumn() {
		return CPDefinitionTable.INSTANCE.CPDefinitionId;
	}

	@Override
	public String getRESTDTOIdPropertyName() {
		return "productId";
	}

	@Override
	public String getScope() {
		return ObjectDefinitionConstants.SCOPE_COMPANY;
	}

	@Override
	public Table getTable() {
		return CPDefinitionTable.INSTANCE;
	}

	@Override
	public String getTitleObjectFieldName() {
		return "name";
	}

	@Override
	public int getVersion() {
		return 2;
	}

	@Override
	public void updateBaseModel(
			long primaryKey, User user, Map<String, Object> values)
		throws Exception {

		ProductResource productResource = _buildProductResource(user);

		CPDefinition cpDefinition = _cpDefinitionLocalService.getCPDefinition(
			primaryKey);

		productResource.patchProduct(
			cpDefinition.getCProductId(), _toProduct(values));

		setExtendedProperties(
			Product.class.getName(), JSONUtil.put("id", primaryKey), user,
			values);
	}

	private ProductResource _buildProductResource(User user) {
		ProductResource.Builder builder = _productResourceFactory.create();

		return builder.checkPermissions(
			false
		).preferredLocale(
			user.getLocale()
		).user(
			user
		).build();
	}

	private Product _toProduct(Map<String, Object> values) {
		return new Product() {
			{
				active = GetterUtil.getBoolean(values.get("active"));
				catalogId = GetterUtil.getLong(values.get("catalogId"));
				description = getLanguageIdMap("description", values);
				externalReferenceCode = GetterUtil.getString(
					values.get("externalReferenceCode"));
				name = getLanguageIdMap("name", values);
				productId = GetterUtil.getLong(values.get("productId"));
				productType = GetterUtil.getString(values.get("productType"));
				shortDescription = getLanguageIdMap("shortDescription", values);
				skuFormatted = GetterUtil.getString(values.get("skuFormatted"));
				thumbnail = GetterUtil.getString(values.get("thumbnail"));
			}
		};
	}

	@Reference
	private CPDefinitionLocalService _cpDefinitionLocalService;

	@Reference
	private CProductLocalService _cProductLocalService;

	@Reference
	private ProductResource.Factory _productResourceFactory;

}